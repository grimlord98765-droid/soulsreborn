package com.terrarockys.soulsreborn.block.entity;

import com.terrarockys.soulsreborn.SoulsRebornMod;
import com.terrarockys.soulsreborn.player.PlayerStats;
import com.terrarockys.soulsreborn.player.StatsManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BonfireBlockEntity extends BlockEntity {
    private ResourceLocation bonfireId;
    private String bonfireName;

    public BonfireBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BONFIRE.get(), pos, state);
        this.bonfireId = SoulsRebornMod.id("bonfire_" + pos.toShortString());
        this.bonfireName = "Костёр";
    }

    public void onActivate(Player player) {
        PlayerStats stats = StatsManager.getPlayerStats(player);
        stats.addDiscoveredBonfire(this.bonfireId);

        // Лечим игрока
        player.heal(player.getMaxHealth());
        player.getFoodData().setFoodLevel(20);

        // Сохраняем точку возрождения
        // В 1.21.1 используется другой метод - временно уберем
        // player.setRespawnPosition(player.level().dimension(), this.worldPosition,
        //     player.getYRot(), true, false);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putString("BonfireId", this.bonfireId.toString());
        tag.putString("BonfireName", this.bonfireName);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains("BonfireId")) {
            this.bonfireId = ResourceLocation.parse(tag.getString("BonfireId"));
        }
        if (tag.contains("BonfireName")) {
            this.bonfireName = tag.getString("BonfireName");
        }
    }

    public ResourceLocation getBonfireId() {
        return bonfireId;
    }

    public String getBonfireName() {
        return bonfireName;
    }
}