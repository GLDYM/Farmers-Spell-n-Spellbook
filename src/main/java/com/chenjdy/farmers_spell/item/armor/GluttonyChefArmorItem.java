package com.chenjdy.farmers_spell.item.armor;

import com.chenjdy.farmers_spell.init.ModAttributes;
import io.redspace.ironsspellbooks.entity.armor.priest.PriestArmorModel;
import io.redspace.ironsspellbooks.entity.armor.priest.PriestArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ImbuableChestplateArmorItem;
import io.redspace.ironsspellbooks.registries.ArmorMaterialRegistry;
import net.minecraft.world.item.ArmorItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class GluttonyChefArmorItem extends ImbuableChestplateArmorItem {
    public GluttonyChefArmorItem(ArmorItem.Type slot, Properties settings) {
        super(ArmorMaterialRegistry.SCHOOL, slot, settings, schoolAttributes(ModAttributes.GLUTTONY_SPELL_POWER));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new PriestArmorRenderer(new PriestArmorModel());
    }
}
