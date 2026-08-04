package com.chenjdy.farmers_spell.item.armor;

import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.item.armor.IronsExtendedArmorMaterial;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public abstract class GluttonyChefArmorItemImbued extends GluttonyChefArmorItem implements IPresetSpellContainer {
    public GluttonyChefArmorItemImbued(IronsExtendedArmorMaterial material, Type type, Properties settings) {
        super(material, type, settings);
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }

        if (itemStack.getItem() instanceof ArmorItem armorItem && armorItem.getType() == Type.CHESTPLATE) {
            if (!ISpellContainer.isSpellContainer(itemStack)) {
                var spellContainer = ISpellContainer.create(1, true, true);
                spellContainer.save(itemStack);
            }
        }
    }
}
