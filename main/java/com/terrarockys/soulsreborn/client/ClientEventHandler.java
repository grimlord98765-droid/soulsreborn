package com.terrarockys.soulsreborn.client;

import com.terrarockys.soulsreborn.SoulsRebornMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class ClientEventHandler {
    private static int soulsToDisplay = 0;
    private static long lastUpdateTime = 0;
    private static boolean shouldShow = false;
    private static final ResourceLocation SOULS_ICON = SoulsRebornMod.id("textures/gui/souls_icon.png");

    public static void updateSouls(int souls, boolean show) {
        soulsToDisplay = souls;
        lastUpdateTime = System.currentTimeMillis();
        shouldShow = show;
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        if (!shouldShow) return;

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.options.hideGui || minecraft.screen != null) {
            return;
        }

        // Автоматически скрываем через 5 секунд
        if (System.currentTimeMillis() - lastUpdateTime > 5000) {
            shouldShow = false;
            return;
        }

        GuiGraphics guiGraphics = event.getGuiGraphics();
        int width = minecraft.getWindow().getGuiScaledWidth();
        int height = minecraft.getWindow().getGuiScaledHeight();

        // Позиция в правом нижнем углу
        int x = width - 60;
        int y = height - 30;

        // Рисуем фон
        guiGraphics.fill(x - 5, y - 5, x + 55, y + 20, 0x80000000);

        // Рисуем иконку (простой квадрат если текстура не найдена)
        try {
            guiGraphics.blit(SOULS_ICON, x, y, 0, 0, 16, 16, 16, 16);
        } catch (Exception e) {
            guiGraphics.fill(x, y, x + 16, y + 16, 0xFFFFFF00);
        }

        // Рисуем количество душ
        String soulsText = String.valueOf(soulsToDisplay);
        guiGraphics.drawString(minecraft.font, soulsText, x + 25, y + 4, 0xFFD700, false);
    }
}