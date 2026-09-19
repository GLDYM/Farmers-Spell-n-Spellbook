package com.chenjdy.farmers_spell.item;

import com.chenjdy.farmers_spell.init.ModSpells;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.item.SpellBook;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class TiramisuBook extends SpellBook {
    public TiramisuBook(Item.Properties properties) {
        super(12, properties);
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack == null || ISpellContainer.isSpellContainer(itemStack)) {
            return;
        }

        var spellContainer = ISpellContainer.create(getMaxSpellSlots(), true, true).mutableCopy();
        spellContainer.addSpell(ModSpells.PHANTOM_LOOT_SPELL.get(), 1, true);
        ISpellContainer.set(itemStack, spellContainer.toImmutable());
    }
}
