package com.chenjdy.farmers_spell.item.armor;

import com.chenjdy.farmers_spell.client.renderer.GluttonyChefArmorGeoRender;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.item.armor.ExtendedArmorItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class GluttonyChefArmorItem extends ExtendedArmorItem implements IPresetSpellContainer {
    public GluttonyChefArmorItem(ArmorItem.Type type, Properties settings) {
        super(ModArmorMaterials.GLUTTONY_CHEF, type, settings);
    }

    public GluttonyChefArmorItem(ArmorItem.Type type, Properties settings, AttributeContainer... attributeContainers) {
        super(ModArmorMaterials.GLUTTONY_CHEF, type, settings, attributeContainers);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GluttonyChefArmorGeoRender();
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }
        
        if (itemStack.getItem() instanceof ArmorItem armorItem && armorItem.getType() == ArmorItem.Type.CHESTPLATE) {
            if (!ISpellContainer.isSpellContainer(itemStack)) {
                var spellContainer = ISpellContainer.create(1, true, true);
                spellContainer.save(itemStack);
            }
        }
    }
}
