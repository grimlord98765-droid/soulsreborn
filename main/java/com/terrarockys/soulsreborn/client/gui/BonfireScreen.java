package com.terrarockys.soulsreborn.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.terrarockys.soulsreborn.SoulsRebornMod;
import com.terrarockys.soulsreborn.player.PlayerStats;
import com.terrarockys.soulsreborn.player.Stat;
import com.terrarockys.soulsreborn.player.StatsManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class BonfireScreen extends Screen {
    private static final ResourceLocation BACKGROUND = SoulsRebornMod.id("textures/gui/bonfire.png");
    private static final int BACKGROUND_WIDTH = 176;
    private static final int BACKGROUND_HEIGHT = 166;

    private final Player player;
    private PlayerStats stats;
    private int leftPos;
    private int topPos;

    public BonfireScreen(Player player) {
        super(Component.translatable("gui.soulsreborn.bonfire"));
        this.player = player;
        this.stats = StatsManager.getPlayerStats(player);
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - BACKGROUND_WIDTH) / 2;
        this.topPos = (this.height - BACKGROUND_HEIGHT) / 2;

        // Кнопки улучшения характеристик
        int buttonY = topPos + 30;
        int buttonWidth = 100;
        int buttonHeight = 20;
        int spacing = 5;

        addStatButton(Stat.VIGOR, "Vigor: " + stats.getVigor(), leftPos + 40, buttonY, buttonWidth, buttonHeight);
        addStatButton(Stat.ENDURANCE, "Endurance: " + stats.getEndurance(), leftPos + 40, buttonY + buttonHeight + spacing, buttonWidth, buttonHeight);
        addStatButton(Stat.STRENGTH, "Strength: " + stats.getStrength(), leftPos + 40, buttonY + (buttonHeight + spacing) * 2, buttonWidth, buttonHeight);
        addStatButton(Stat.DEXTERITY, "Dexterity: " + stats.getDexterity(), leftPos + 40, buttonY + (buttonHeight + spacing) * 3, buttonWidth, buttonHeight);
        addStatButton(Stat.INTELLIGENCE, "Intelligence: " + stats.getIntelligence(), leftPos + 40, buttonY + (buttonHeight + spacing) * 4, buttonWidth, buttonHeight);
        addStatButton(Stat.FAITH, "Faith: " + stats.getFaith(), leftPos + 40, buttonY + (buttonHeight + spacing) * 5, buttonWidth, buttonHeight);
        addStatButton(Stat.LUCK, "Luck: " + stats.getLuck(), leftPos + 40, buttonY + (buttonHeight + spacing) * 6, buttonWidth, buttonHeight);

        // Кнопка закрытия
        this.addRenderableWidget(Button.builder(
                Component.literal("Закрыть"),
                button -> this.minecraft.setScreen(null)
        ).bounds(leftPos + 40, topPos + BACKGROUND_HEIGHT - 30, 100, 20).build());
    }

    private void addStatButton(Stat stat, String text, int x, int y, int width, int height) {
        this.addRenderableWidget(Button.builder(
                Component.literal(text),
                button -> {
                    if (StatsManager.upgradeStat(player, stat)) {
                        this.stats = StatsManager.getPlayerStats(player); // Обновляем статистику
                        button.setMessage(Component.literal(stat.name() + ": " + stats.getStat(stat)));
                    }
                }
        ).bounds(x, y, width, height).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        // Рендерим фон
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(BACKGROUND, leftPos, topPos, 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

        // Текст заголовка
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, topPos + 10, 0xFFFFFF);

        // Отображаем уровень и души
        guiGraphics.drawString(this.font, "Уровень: " + stats.getLevel(), leftPos + 10, topPos + 20, 0xFFFFFF);
        guiGraphics.drawString(this.font, "Души: " + stats.getSouls(), leftPos + 10, topPos + 30, 0xFFD700);

        // Требуемые души для следующего уровня
        guiGraphics.drawString(this.font, "След. уровень: " + stats.getSoulsToNextLevel(), leftPos + 10, topPos + 40, 0xAAAAAA);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}