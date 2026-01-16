package com.terrarockys.soulsreborn.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.Supplier;

public class PlayerDataAttachment {
    // Codec для PlayerStats (опционально, можно использовать для других целей)
    public static final Codec<PlayerStats> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("level").forGetter(PlayerStats::getLevel),
                    Codec.INT.fieldOf("vigor").forGetter(PlayerStats::getVigor),
                    Codec.INT.fieldOf("endurance").forGetter(PlayerStats::getEndurance),
                    Codec.INT.fieldOf("strength").forGetter(PlayerStats::getStrength),
                    Codec.INT.fieldOf("dexterity").forGetter(PlayerStats::getDexterity),
                    Codec.INT.fieldOf("intelligence").forGetter(PlayerStats::getIntelligence),
                    Codec.INT.fieldOf("faith").forGetter(PlayerStats::getFaith),
                    Codec.INT.fieldOf("luck").forGetter(PlayerStats::getLuck),
                    Codec.INT.fieldOf("souls").forGetter(PlayerStats::getSouls),
                    Codec.STRING.fieldOf("selectedClass").forGetter(PlayerStats::getSelectedClass),
                    Codec.list(ResourceLocation.CODEC).fieldOf("discoveredBonfires").forGetter(PlayerStats::getDiscoveredBonfires)
            ).apply(instance, PlayerStats::new)
    );

    // Supplier для создания PlayerStats (больше не нужен, можно удалить)
    // public static final Supplier<PlayerStats> SUPPLIER = PlayerStats::new;
}