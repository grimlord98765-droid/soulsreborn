package com.terrarockys.soulsreborn.network;

import com.terrarockys.soulsreborn.SoulsRebornMod;
import com.terrarockys.soulsreborn.client.ClientEventHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record SyncSoulsPacket(int souls, boolean showHud) implements CustomPacketPayload {
    public static final Type<SyncSoulsPacket> TYPE = new Type<>(SoulsRebornMod.id("sync_souls"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, SyncSoulsPacket> STREAM_CODEC = StreamCodec.of(
            SyncSoulsPacket::write,
            SyncSoulsPacket::new
    );

    private static void write(FriendlyByteBuf buf, SyncSoulsPacket packet) {
        buf.writeInt(packet.souls);
        buf.writeBoolean(packet.showHud);
    }

    private SyncSoulsPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readBoolean());
    }

    public static void handle(SyncSoulsPacket packet, net.neoforged.neoforge.network.handling.IPayloadContext context) {
        context.enqueueWork(() -> {
            // Обновляем HUD на клиенте
            ClientEventHandler.updateSouls(packet.souls, packet.showHud);
        });
    }
}