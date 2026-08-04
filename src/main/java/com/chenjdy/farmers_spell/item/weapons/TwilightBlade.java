package com.chenjdy.farmers_spell.item.weapons;

import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;

import java.util.Map;

public class TwilightBlade extends MagicSwordItem {
    public TwilightBlade() {
        super(Tiers.IRON, 7.0f, -2.0f,
            SpellDataRegistryHolder.of(),
            Map.of(),
            new Item.Properties().durability(1561).rarity(Rarity.RARE));
    }
    
    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }

        if (!ISpellContainer.isSpellContainer(itemStack)) {
            ISpellContainer spellContainer = ISpellContainer.create(1, true, false);
            spellContainer.save(itemStack);
        }
    }
}
