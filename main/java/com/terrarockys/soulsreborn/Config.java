package com.terrarockys.soulsreborn;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config {
    public static final ModConfigSpec SPEC;
    public static final ConfigValues VALUES;

    static {
        Pair<ConfigValues, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(ConfigValues::new);
        SPEC = specPair.getRight();
        VALUES = specPair.getLeft();
    }

    public static class ConfigValues {
        public final ModConfigSpec.IntValue maxStatLevel;
        public final ModConfigSpec.IntValue soulsMultiplier;
        public final ModConfigSpec.ConfigValue<String> defaultClass;

        public ConfigValues(ModConfigSpec.Builder builder) {
            builder.push("gameplay");
            maxStatLevel = builder
                    .comment("Maximum level for any stat")
                    .defineInRange("maxStatLevel", 99, 1, 999);
            soulsMultiplier = builder
                    .comment("Souls multiplier for drops")
                    .defineInRange("soulsMultiplier", 1, 1, 10);
            defaultClass = builder
                    .comment("Default starting class")
                    .define("defaultClass", "warrior");
            builder.pop();
        }
    }
}