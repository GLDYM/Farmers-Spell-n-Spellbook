package com.chenjdy.farmers_spell.item.weapons;

import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;

import java.util.Map;

public class GospelButterKnife extends MagicSwordItem {
    public GospelButterKnife() {
        super(Tiers.IRON, 3.5f, -2.0f,
            SpellDataRegistryHolder.of(new SpellDataRegistryHolder(SpellRegistry.WISP_SPELL, 5)),
            Map.of(),
            new Item.Properties().durability(1561).rarity(Rarity.RARE));
    }
}
