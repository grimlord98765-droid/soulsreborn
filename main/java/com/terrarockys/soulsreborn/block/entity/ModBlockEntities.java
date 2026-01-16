package com.terrarockys.soulsreborn.block.entity;

import com.terrarockys.soulsreborn.SoulsRebornMod;
import com.terrarockys.soulsreborn.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, SoulsRebornMod.MODID);

    public static final Supplier<BlockEntityType<BonfireBlockEntity>> BONFIRE =
            BLOCK_ENTITIES.register("bonfire",
                    () -> BlockEntityType.Builder.of(
                            BonfireBlockEntity::new,
                            ModBlocks.BONFIRE.get()
                    ).build(null));

    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITIES.register(modEventBus);
    }
}