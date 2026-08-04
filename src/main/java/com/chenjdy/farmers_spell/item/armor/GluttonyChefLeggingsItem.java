package com.chenjdy.farmers_spell.item.armor;

import com.chenjdy.farmers_spell.client.renderer.GluttonyChefArmorGeoRender;
import net.minecraft.world.item.ArmorItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class GluttonyChefLeggingsItem extends GluttonyChefArmorItem {
    public GluttonyChefLeggingsItem(Properties settings) {
        super(ModArmorMaterials.GLUTTONY_CHEF, ArmorItem.Type.LEGGINGS, settings);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("unchecked")
    public GeoArmorRenderer<?> supplyRenderer() {
        return (GeoArmorRenderer<?>) new GluttonyChefArmorGeoRender();
    }
}
