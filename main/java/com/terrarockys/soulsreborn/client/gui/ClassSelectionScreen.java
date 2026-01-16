package com.terrarockys.soulsreborn.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.terrarockys.soulsreborn.SoulsRebornMod;
import com.terrarockys.soulsreborn.network.ChooseClassPacket;
import com.terrarockys.soulsreborn.player.PlayerClass;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

public class ClassSelectionScreen extends Screen {
    private static final ResourceLocation BACKGROUND = SoulsRebornMod.id("textures/gui/class_selection.png");
    private static final int BACKGROUND_WIDTH = 256;
    private static final int BACKGROUND_HEIGHT = 256;

    private int leftPos;
    private int topPos;

    public ClassSelectionScreen() {
        super(Component.translatable("gui.soulsreborn.class_selection"));
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - BACKGROUND_WIDTH) / 2;
        this.topPos = (this.height - BACKGROUND_HEIGHT) / 2;

        int buttonWidth = 100;
        int buttonHeight = 20;
        int buttonY = topPos + 180;
        int spacing = 5;

        // Кнопки классов
        addClassButton(PlayerClass.WARRIOR, leftPos + 20, buttonY, buttonWidth, buttonHeight);
        addClassButton(PlayerClass.KNIGHT, leftPos + 20 + buttonWidth + spacing, buttonY, buttonWidth, buttonHeight);
        addClassButton(PlayerClass.MAGE, leftPos + 20 + (buttonWidth + spacing) * 2, buttonY, buttonWidth, buttonHeight);
        addClassButton(PlayerClass.CLERIC, leftPos + 20, buttonY + buttonHeight + spacing, buttonWidth, buttonHeight);
        addClassButton(PlayerClass.THIEF, leftPos + 20 + buttonWidth + spacing, buttonY + buttonHeight + spacing, buttonWidth, buttonHeight);
        addClassButton(PlayerClass.DEPRIVED, leftPos + 20 + (buttonWidth + spacing) * 2, buttonY + buttonHeight + spacing, buttonWidth, buttonHeight);
    }

    private void addClassButton(PlayerClass playerClass, int x, int y, int width, int height) {
        this.addRenderableWidget(Button.builder(
                Component.literal(playerClass.getDisplayName()),
                button -> {
                    PacketDistributor.sendToServer(new ChooseClassPacket(playerClass));
                    this.minecraft.setScreen(null);
                }
        ).bounds(x, y, width, height).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        // Рендерим фон
        guiGraphics.blit(BACKGROUND, leftPos, topPos, 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

        // Текст заголовка
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, topPos + 20, 0xFFFFFF);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}