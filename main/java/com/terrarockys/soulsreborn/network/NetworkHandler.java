package com.terrarockys.soulsreborn.network;

import com.terrarockys.soulsreborn.SoulsRebornMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkHandler {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        // Пакет выбора класса
        registrar.playToServer(
                ChooseClassPacket.TYPE,
                ChooseClassPacket.STREAM_CODEC,
                ChooseClassPacket::handle
        );

        // Пакет синхронизации душ (сервер -> клиент)
        registrar.playToClient(
                SyncSoulsPacket.TYPE,
                SyncSoulsPacket.STREAM_CODEC,
                SyncSoulsPacket::handle
        );
    }
}