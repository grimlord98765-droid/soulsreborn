package com.terrarockys.soulsreborn.player;

public enum PlayerClass {
    WARRIOR("Warrior", 14, 11, 11, 10, 10, 8, 9),
    KNIGHT("Knight", 12, 11, 15, 10, 10, 9, 7),
    MAGE("Mage", 8, 10, 9, 12, 15, 7, 12),
    CLERIC("Cleric", 10, 10, 12, 9, 8, 14, 11),
    THIEF("Thief", 10, 12, 9, 15, 10, 8, 11),
    DEPRIVED("Deprived", 10, 10, 10, 10, 10, 10, 10);

    private final String displayName;
    private final int vigor;
    private final int endurance;
    private final int strength;
    private final int dexterity;
    private final int intelligence;
    private final int faith;
    private final int luck;

    PlayerClass(String displayName, int vigor, int endurance, int strength, int dexterity,
                int intelligence, int faith, int luck) {
        this.displayName = displayName;
        this.vigor = vigor;
        this.endurance = endurance;
        this.strength = strength;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
        this.faith = faith;
        this.luck = luck;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void applyStats(PlayerStats stats) {
        stats.setVigor(this.vigor);
        stats.setEndurance(this.endurance);
        stats.setStrength(this.strength);
        stats.setDexterity(this.dexterity);
        stats.setIntelligence(this.intelligence);
        stats.setFaith(this.faith);
        stats.setLuck(this.luck);
        stats.setSelectedClass(this.name());
    }

    public static PlayerClass fromString(String name) {
        for (PlayerClass playerClass : values()) {
            if (playerClass.name().equalsIgnoreCase(name)) {
                return playerClass;
            }
        }
        return WARRIOR;
    }
}