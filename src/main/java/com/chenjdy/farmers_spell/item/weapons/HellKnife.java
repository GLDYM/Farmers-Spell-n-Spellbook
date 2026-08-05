package com.chenjdy.farmers_spell.item.weapons;

import com.chenjdy.farmers_spell.init.ModSpells;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

public class HellKnife extends MagicSwordItem {
    public HellKnife() {
        super(
            Tiers.NETHERITE, 
            new Item.Properties()
                .rarity(Rarity.EPIC)
                .attributes(SwordItem.createAttributes(Tiers.NETHERITE, 3, -2.0F)),
            SpellDataRegistryHolder.of(new SpellDataRegistryHolder(ModSpells.CHAOS_SLASH_SPELL, 5)));
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }
}
