package com.terrarockys.soulsreborn.player;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

public class PlayerStats implements INBTSerializable<CompoundTag> {
    // Характеристики
    private int level;
    private int vigor;
    private int endurance;
    private int strength;
    private int dexterity;
    private int intelligence;
    private int faith;
    private int luck;

    // Игровая валюта (виртуальные души)
    private int souls;

    // Выбранный класс
    private String selectedClass;

    // Открытые костры
    private List<ResourceLocation> discoveredBonfires;

    public PlayerStats() {
        this.level = 1;
        this.vigor = 10;
        this.endurance = 10;
        this.strength = 10;
        this.dexterity = 10;
        this.intelligence = 10;
        this.faith = 10;
        this.luck = 10;
        this.souls = 0;
        this.selectedClass = "warrior";
        this.discoveredBonfires = new ArrayList<>();
    }

    public PlayerStats(int level, int vigor, int endurance, int strength, int dexterity,
                       int intelligence, int faith, int luck, int souls,
                       String selectedClass, List<ResourceLocation> discoveredBonfires) {
        this.level = level;
        this.vigor = vigor;
        this.endurance = endurance;
        this.strength = strength;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
        this.faith = faith;
        this.luck = luck;
        this.souls = souls;
        this.selectedClass = selectedClass;
        this.discoveredBonfires = discoveredBonfires != null ? discoveredBonfires : new ArrayList<>();
    }

    // Геттеры
    public int getLevel() { return level; }
    public int getVigor() { return vigor; }
    public int getEndurance() { return endurance; }
    public int getStrength() { return strength; }
    public int getDexterity() { return dexterity; }
    public int getIntelligence() { return intelligence; }
    public int getFaith() { return faith; }
    public int getLuck() { return luck; }
    public int getSouls() { return souls; }
    public String getSelectedClass() { return selectedClass; }
    public List<ResourceLocation> getDiscoveredBonfires() { return discoveredBonfires; }

    // Сеттеры
    public void setLevel(int level) { this.level = level; }
    public void setVigor(int vigor) { this.vigor = Math.min(vigor, 99); }
    public void setEndurance(int endurance) { this.endurance = Math.min(endurance, 99); }
    public void setStrength(int strength) { this.strength = Math.min(strength, 99); }
    public void setDexterity(int dexterity) { this.dexterity = Math.min(dexterity, 99); }
    public void setIntelligence(int intelligence) { this.intelligence = Math.min(intelligence, 99); }
    public void setFaith(int faith) { this.faith = Math.min(faith, 99); }
    public void setLuck(int luck) { this.luck = Math.min(luck, 99); }
    public void setSouls(int souls) { this.souls = Math.max(souls, 0); }
    public void setSelectedClass(String selectedClass) { this.selectedClass = selectedClass; }
    public void setDiscoveredBonfires(List<ResourceLocation> discoveredBonfires) {
        this.discoveredBonfires = discoveredBonfires;
    }

    // Методы для управления кострами
    public void addDiscoveredBonfire(ResourceLocation bonfireId) {
        if (!discoveredBonfires.contains(bonfireId)) {
            discoveredBonfires.add(bonfireId);
        }
    }

    public boolean hasDiscoveredBonfire(ResourceLocation bonfireId) {
        return discoveredBonfires.contains(bonfireId);
    }

    // Расчет производных значений
    public int getMaxHealth() {
        return 100 + vigor * 10;
    }

    public int getMaxStamina() {
        return 100 + endurance * 5;
    }

    public int getSoulsToNextLevel() {
        return level * 1000;
    }

    // Методы для управления душами
    public void addSouls(int amount) {
        this.souls += amount;
    }

    public boolean spendSouls(int amount) {
        if (souls >= amount) {
            souls -= amount;
            return true;
        }
        return false;
    }

    // Получение значения характеристики по enum
    public int getStat(Stat stat) {
        return switch (stat) {
            case VIGOR -> vigor;
            case ENDURANCE -> endurance;
            case STRENGTH -> strength;
            case DEXTERITY -> dexterity;
            case INTELLIGENCE -> intelligence;
            case FAITH -> faith;
            case LUCK -> luck;
            case VITALITY -> vigor;
        };
    }

    // Установка значения характеристики по enum
    public void setStat(Stat stat, int value) {
        switch (stat) {
            case VIGOR, VITALITY -> setVigor(value);
            case ENDURANCE -> setEndurance(value);
            case STRENGTH -> setStrength(value);
            case DEXTERITY -> setDexterity(value);
            case INTELLIGENCE -> setIntelligence(value);
            case FAITH -> setFaith(value);
            case LUCK -> setLuck(value);
        }
    }

    // Метод для улучшения характеристики
    public boolean upgradeStat(Stat stat) {
        int current = getStat(stat);
        if (current >= 99) {
            return false;
        }
        int cost = getUpgradeCost(stat, current);
        if (souls >= cost) {
            souls -= cost;
            setStat(stat, current + 1);
            level++;
            return true;
        }
        return false;
    }

    private int getUpgradeCost(Stat stat, int currentLevel) {
        return 1000 * (currentLevel + 1);
    }

    // INBTSerializable методы
    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("level", level);
        tag.putInt("vigor", vigor);
        tag.putInt("endurance", endurance);
        tag.putInt("strength", strength);
        tag.putInt("dexterity", dexterity);
        tag.putInt("intelligence", intelligence);
        tag.putInt("faith", faith);
        tag.putInt("luck", luck);
        tag.putInt("souls", souls);
        tag.putString("selectedClass", selectedClass);

        // Сохраняем костры
        CompoundTag bonfiresTag = new CompoundTag();
        for (int i = 0; i < discoveredBonfires.size(); i++) {
            bonfiresTag.putString(String.valueOf(i), discoveredBonfires.get(i).toString());
        }
        tag.put("discoveredBonfires", bonfiresTag);

        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        level = tag.getInt("level");
        vigor = tag.getInt("vigor");
        endurance = tag.getInt("endurance");
        strength = tag.getInt("strength");
        dexterity = tag.getInt("dexterity");
        intelligence = tag.getInt("intelligence");
        faith = tag.getInt("faith");
        luck = tag.getInt("luck");
        souls = tag.getInt("souls");
        selectedClass = tag.getString("selectedClass");

        // Загружаем костры
        discoveredBonfires.clear();
        CompoundTag bonfiresTag = tag.getCompound("discoveredBonfires");
        for (String key : bonfiresTag.getAllKeys()) {
            discoveredBonfires.add(ResourceLocation.parse(bonfiresTag.getString(key)));
        }
    }
}