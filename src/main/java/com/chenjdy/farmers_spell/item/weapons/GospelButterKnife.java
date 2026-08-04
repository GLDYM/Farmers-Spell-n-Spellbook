package com.chenjdy.farmers_spell.item.weapons;

import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;

public class GospelButterKnife extends MagicSwordItem {
    public GospelButterKnife() {
        super(Tiers.IRON, new Item.Properties().durability(1561).rarity(Rarity.RARE), SpellDataRegistryHolder.of());
    }
}
