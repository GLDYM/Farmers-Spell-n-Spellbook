package com.chenjdy.farmers_spell.item.weapons;

import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;

public class HellKnife extends MagicSwordItem {
    public HellKnife() {
        super(Tiers.NETHERITE, new Item.Properties().rarity(Rarity.EPIC), SpellDataRegistryHolder.of());
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }
}
