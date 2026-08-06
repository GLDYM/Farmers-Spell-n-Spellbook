package com.chenjdy.farmers_spell.init;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.DoubleValue PHANTOM_LOOT_MAX_HP;

    static {
        BUILDER.push("Spell Settings");

        PHANTOM_LOOT_MAX_HP = BUILDER
                .comment("Max health of mobs that can be affected by Phantom Loot spell")
                .defineInRange("phantomLootMaxHp", 100.0, 1.0, 1000.0);

        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static double getPhantomLootMaxHp() {
        return PHANTOM_LOOT_MAX_HP.get();
    }
}
