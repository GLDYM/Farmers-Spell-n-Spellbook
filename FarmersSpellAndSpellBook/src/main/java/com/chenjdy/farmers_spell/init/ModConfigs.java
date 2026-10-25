package com.chenjdy.farmers_spell.init;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.DoubleValue PHANTOM_LOOT_MAX_HP;
    public static final ForgeConfigSpec.BooleanValue PHANTOM_LOOT_ONCE_PER_MOB;

    static {
        BUILDER.push("Spell Settings");

        PHANTOM_LOOT_MAX_HP = BUILDER
                .comment("Max health of mobs that can be affected by Phantom Loot spell")
                .defineInRange("phantomLootMaxHp", 100.0, 1.0, 1000.0);

        PHANTOM_LOOT_ONCE_PER_MOB = BUILDER
                .comment("Whether each mob can only be robbed once by the Phantom Loot spell")
                .define("phantomLootOncePerMob", true);

        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static double getPhantomLootMaxHp() {
        return PHANTOM_LOOT_MAX_HP.get();
    }

    public static boolean isPhantomLootOncePerMob() {
        return PHANTOM_LOOT_ONCE_PER_MOB.get();
    }
}
