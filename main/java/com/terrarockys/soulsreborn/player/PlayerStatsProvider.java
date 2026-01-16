package com.terrarockys.soulsreborn.player;

import com.terrarockys.soulsreborn.SoulsRebornMod;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentHolder;

public class PlayerStatsProvider {
    public static PlayerStats get(Player player) {
        return ((AttachmentHolder) player).getData(SoulsRebornMod.PLAYER_STATS);
    }

    public static void set(Player player, PlayerStats stats) {
        ((AttachmentHolder) player).setData(SoulsRebornMod.PLAYER_STATS, stats);
    }
}