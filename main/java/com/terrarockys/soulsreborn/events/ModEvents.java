package com.terrarockys.soulsreborn.events;

import com.terrarockys.soulsreborn.SoulsRebornMod;
import com.terrarockys.soulsreborn.client.gui.ClassSelectionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = SoulsRebornMod.MODID, value = Dist.CLIENT)
public class ModEvents {

    private static boolean classScreenShown = false;

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().level().isClientSide) {
            // На сервере - устанавливаем начальные значения
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player == null) return;

        // Клавиша для открытия экрана классов (например, K)
        if (event.getKey() == GLFW.GLFW_KEY_K && event.getAction() == GLFW.GLFW_PRESS) {
            if (!classScreenShown) {
                minecraft.setScreen(new ClassSelectionScreen());
                classScreenShown = true;
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        // Можно добавить тик для обновления UI
    }
}