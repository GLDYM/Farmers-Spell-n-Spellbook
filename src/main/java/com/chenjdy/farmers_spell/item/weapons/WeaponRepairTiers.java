package com.chenjdy.farmers_spell.item.weapons;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

/** Weapon tiers that preserve the base tier's stats while using custom repair materials. */
public final class WeaponRepairTiers {
    public static final Tier GOSPEL = create(Tiers.IRON, "irons_spellbooks", "divine_pearl");

    private WeaponRepairTiers() {
    }

    private static Tier create(Tier baseTier, String namespace, String path) {
        return new RepairTier(baseTier,
                Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(namespace, path))));
    }

    private record RepairTier(Tier baseTier, Ingredient repairIngredient) implements Tier {
        @Override
        public Ingredient getRepairIngredient() {
            return repairIngredient;
        }

        @Override
        public int getUses() {
            return baseTier.getUses();
        }

        @Override
        public float getSpeed() {
            return baseTier.getSpeed();
        }

        @Override
        public float getAttackDamageBonus() {
            return baseTier.getAttackDamageBonus();
        }

        @Override
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return baseTier.getIncorrectBlocksForDrops();
        }

        @Override
        public int getEnchantmentValue() {
            return baseTier.getEnchantmentValue();
        }
    }
}
