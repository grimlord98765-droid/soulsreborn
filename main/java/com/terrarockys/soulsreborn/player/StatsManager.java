package com.terrarockys.soulsreborn.player;

import com.terrarockys.soulsreborn.SoulsRebornMod;
import com.terrarockys.soulsreborn.network.SyncSoulsPacket;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public class StatsManager {

    public static PlayerStats getPlayerStats(Player player) {
        return player.getData(SoulsRebornMod.PLAYER_STATS.get());
    }

    public static boolean upgradeStat(Player player, Stat stat) {
        PlayerStats stats = getPlayerStats(player);
        boolean success = stats.upgradeStat(stat);
        if (success) {
            syncSouls(player, stats.getSouls(), true);
        }
        return success;
    }

    public static PlayerClass getPlayerClass(Player player) {
        PlayerStats stats = getPlayerStats(player);
        return PlayerClass.fromString(stats.getSelectedClass());
    }

    public static void setPlayerClass(Player player, PlayerClass playerClass) {
        PlayerStats stats = getPlayerStats(player);
        playerClass.applyStats(stats);
    }

    public static void addSouls(Player player, int amount) {
        if (amount <= 0) return;

        PlayerStats stats = getPlayerStats(player);
        int oldSouls = stats.getSouls();
        stats.addSouls(amount);

        // Отправляем пакет только если души изменились
        if (stats.getSouls() != oldSouls) {
            syncSouls(player, stats.getSouls(), true);
        }
    }

    public static boolean spendSouls(Player player, int amount) {
        if (amount <= 0) return false;

        PlayerStats stats = getPlayerStats(player);
        if (stats.spendSouls(amount)) {
            syncSouls(player, stats.getSouls(), true);
            return true;
        }
        return false;
    }

    public static void syncSouls(Player player, boolean showHud) {
        PlayerStats stats = getPlayerStats(player);
        syncSouls(player, stats.getSouls(), showHud);
    }

    private static void syncSouls(Player player, int souls, boolean showHud) {
        if (!player.level().isClientSide) {
            SyncSoulsPacket packet = new SyncSoulsPacket(souls, showHud);
            // Проверяем что player это ServerPlayer
            if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(serverPlayer, packet);
            }
        }
    }

    // Синхронизация при входе
    public static void syncOnLogin(Player player) {
        syncSouls(player, false); // Без показа HUD
    }
}