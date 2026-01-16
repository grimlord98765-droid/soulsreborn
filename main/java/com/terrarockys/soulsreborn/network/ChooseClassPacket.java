package com.terrarockys.soulsreborn.network;

import com.terrarockys.soulsreborn.SoulsRebornMod;
import com.terrarockys.soulsreborn.player.PlayerClass;
import com.terrarockys.soulsreborn.player.StatsManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ChooseClassPacket(String className) implements CustomPacketPayload {
    public static final Type<ChooseClassPacket> TYPE = new Type<>(SoulsRebornMod.id("choose_class"));

    public ChooseClassPacket(PlayerClass playerClass) {
        this(playerClass.name());
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, ChooseClassPacket> STREAM_CODEC = StreamCodec.of(
            ChooseClassPacket::write,
            ChooseClassPacket::new
    );

    private static void write(FriendlyByteBuf buf, ChooseClassPacket packet) {
        buf.writeUtf(packet.className);
    }

    private ChooseClassPacket(FriendlyByteBuf buf) {
        this(buf.readUtf());
    }

    public static void handle(ChooseClassPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                PlayerClass selectedClass = PlayerClass.fromString(packet.className);
                StatsManager.setPlayerClass(player, selectedClass);
            }
        });
    }
}