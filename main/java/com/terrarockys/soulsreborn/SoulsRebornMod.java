package com.terrarockys.soulsreborn;

import com.mojang.logging.LogUtils;
import com.terrarockys.soulsreborn.block.ModBlocks;
import com.terrarockys.soulsreborn.block.entity.ModBlockEntities;
import com.terrarockys.soulsreborn.item.ModItems;
import com.terrarockys.soulsreborn.player.PlayerStats;
import com.terrarockys.soulsreborn.player.StatsManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.Supplier;

@Mod(SoulsRebornMod.MODID)
public class SoulsRebornMod {
    public static final String MODID = "soulsreborn";
    public static final Logger LOGGER = LogUtils.getLogger();

    // Регистрация Creative Tab
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Регистрация Attachment
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

    // Регистрация AttachmentType для PlayerStats
    public static final Supplier<AttachmentType<PlayerStats>> PLAYER_STATS =
            ATTACHMENTS.register("player_stats",
                    () -> AttachmentType.serializable(() -> new PlayerStats())
                            .copyOnDeath()
                            .build());

    // Регистрация Creative Tab
    public static final Supplier<CreativeModeTab> SOULS_REBORN_TAB = CREATIVE_MODE_TABS.register(
            "souls_reborn_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + MODID))
                    .icon(() -> new ItemStack(Items.NETHER_STAR))
                    .displayItems((parameters, output) -> {
                        // Наши предметы
                        output.accept(ModItems.BONFIRE_KIT.get());
                        output.accept(ModItems.ESTUS_FLASK.get());
                        output.accept(ModBlocks.BONFIRE.get());

                        // Добавим некоторые ванильные предметы для теста
                        output.accept(Items.NETHER_STAR);
                        output.accept(Items.EMERALD);
                        output.accept(Items.DIAMOND_SWORD);
                        output.accept(Items.ENCHANTED_BOOK);
                    })
                    .build()
    );

    public SoulsRebornMod(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Initializing Souls Reborn Mod");

        // Регистрируем все DeferredRegister
        CREATIVE_MODE_TABS.register(modEventBus);
        ATTACHMENTS.register(modEventBus);

        // Регистрируем контент
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        // Регистрируем события
        modEventBus.addListener(this::commonSetup);

        // Регистрируем события NeoForge
        NeoForge.EVENT_BUS.register(this);

        // Регистрируем конфиг
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        LOGGER.info("Souls Reborn Mod initialized successfully");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Common setup for Souls Reborn Mod");

        event.enqueueWork(() -> {
            LOGGER.info("Post-registration setup complete");
        });
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player player && !player.level().isClientSide()) {
            // Инициализируем PlayerStats при входе игрока
            PlayerStats stats = player.getData(PLAYER_STATS.get());
            if (stats.getLevel() == 0 && stats.getSelectedClass().equals("warrior")) {
                LOGGER.info("Initializing player stats for {}", player.getName().getString());
                stats.setLevel(1);
                stats.setVigor(10);
                stats.setEndurance(10);
                stats.setStrength(10);
                stats.setDexterity(10);
                stats.setIntelligence(10);
                stats.setFaith(10);
                stats.setLuck(10);
                stats.setSouls(0);
                stats.setSelectedClass("warrior");

                // Синхронизируем души при входе (без показа HUD)
                StatsManager.syncOnLogin(player);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        Player original = event.getOriginal();
        Player clone = event.getEntity();

        if (event.isWasDeath()) {
            // Копируем данные при смерти
            PlayerStats originalStats = original.getData(PLAYER_STATS.get());
            clone.setData(PLAYER_STATS.get(), originalStats);
            LOGGER.info("Player stats copied after death for {}", clone.getName().getString());

            // Синхронизируем души
            StatsManager.syncSouls(clone, true);
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        // Игрок получает души за убийство мобов
        if (event.getSource().getEntity() instanceof Player player && !player.level().isClientSide()) {
            // Базовая награда: 100 душ за моба
            int soulsReward = 100;

            // Можно сделать сложнее: больше душ за боссов и т.д.
            if (event.getEntity() instanceof net.minecraft.world.entity.monster.Monster) {
                // Мобы дают больше душ
                soulsReward = 150;
            }

            StatsManager.addSouls(player, soulsReward);

            player.displayClientMessage(
                    Component.literal("§6+" + soulsReward + " душ"),
                    true
            );
        }
    }

    // Вспомогательные методы
    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    // Конфигурационный класс
    public static class Config {
        public static final net.neoforged.neoforge.common.ModConfigSpec.Builder BUILDER =
                new net.neoforged.neoforge.common.ModConfigSpec.Builder();
        public static final net.neoforged.neoforge.common.ModConfigSpec SPEC;

        public static final net.neoforged.neoforge.common.ModConfigSpec.IntValue MAX_STAT_LEVEL;
        public static final net.neoforged.neoforge.common.ModConfigSpec.IntValue BASE_SOULS_MULTIPLIER;
        public static final net.neoforged.neoforge.common.ModConfigSpec.ConfigValue<List<? extends String>> STARTING_CLASSES;
        public static final net.neoforged.neoforge.common.ModConfigSpec.ConfigValue<String> DEFAULT_CLASS;
        public static final net.neoforged.neoforge.common.ModConfigSpec.BooleanValue ENABLE_CLASS_SELECTION;
        public static final net.neoforged.neoforge.common.ModConfigSpec.IntValue STARTING_SOULS;
        public static final net.neoforged.neoforge.common.ModConfigSpec.IntValue MOB_SOULS_REWARD;
        public static final net.neoforged.neoforge.common.ModConfigSpec.IntValue HUD_FADE_TIME;

        static {
            BUILDER.push("Gameplay");

            MAX_STAT_LEVEL = BUILDER
                    .comment("Maximum level for any stat", "Default: 99")
                    .defineInRange("maxStatLevel", 99, 1, 999);

            BASE_SOULS_MULTIPLIER = BUILDER
                    .comment("Multiplier for soul gains", "Default: 1")
                    .defineInRange("soulsMultiplier", 1, 1, 10);

            STARTING_SOULS = BUILDER
                    .comment("Starting souls for new players", "Default: 0")
                    .defineInRange("startingSouls", 0, 0, 1000000);

            MOB_SOULS_REWARD = BUILDER
                    .comment("Base souls reward for killing mobs", "Default: 100")
                    .defineInRange("mobSoulsReward", 100, 0, 10000);

            HUD_FADE_TIME = BUILDER
                    .comment("HUD fade time in ticks (20 ticks = 1 second)", "Default: 100")
                    .defineInRange("hudFadeTime", 100, 0, 1000);

            STARTING_CLASSES = BUILDER
                    .comment("List of available starting classes")
                    .defineList("startingClasses",
                            List.of("warrior", "knight", "mage", "cleric", "thief", "deprived"),
                            o -> o instanceof String);

            DEFAULT_CLASS = BUILDER
                    .comment("Default starting class")
                    .define("defaultClass", "warrior");

            ENABLE_CLASS_SELECTION = BUILDER
                    .comment("Enable class selection screen on first join")
                    .define("enableClassSelection", true);

            BUILDER.pop();
            SPEC = BUILDER.build();
        }
    }
}