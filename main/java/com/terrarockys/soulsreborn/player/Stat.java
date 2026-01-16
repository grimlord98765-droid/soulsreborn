package com.terrarockys.soulsreborn.player;

public enum Stat {
    VIGOR("vigor"),
    ENDURANCE("endurance"),
    STRENGTH("strength"),
    DEXTERITY("dexterity"),
    INTELLIGENCE("intelligence"),
    FAITH("faith"),
    LUCK("luck"),
    VITALITY("vitality"); // Добавляем VITALITY

    private final String name;

    Stat(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}