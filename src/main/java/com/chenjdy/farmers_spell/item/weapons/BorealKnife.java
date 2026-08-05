package com.chenjdy.farmers_spell.item.weapons;

import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

public class BorealKnife extends MagicSwordItem {
    public BorealKnife() {
        super(
            Tiers.IRON, 
            new Item.Properties()
                .durability(1561)
                .rarity(Rarity.RARE)
                .attributes(SwordItem.createAttributes(Tiers.IRON, 5, -2F)), 
            SpellDataRegistryHolder.of()
        );
    }
    
    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }

        if (!ISpellContainer.isSpellContainer(itemStack)) {
            ISpellContainer spellContainer = ISpellContainer.create(1, true, false);
            ISpellContainer.set(itemStack, spellContainer);
        }
    }
}
