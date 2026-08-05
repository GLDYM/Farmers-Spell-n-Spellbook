package com.chenjdy.farmers_spell.item.armor;

import com.chenjdy.farmers_spell.client.renderer.GluttonyChefArmorGeoRender;
import net.minecraft.world.item.ArmorItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class GluttonyChefHatItem extends GluttonyChefArmorItem {
    public GluttonyChefHatItem(Properties settings) {
        super(ModArmorMaterials.GLUTTONY_CHEF, ArmorItem.Type.HELMET, settings);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("unchecked")
    public GeoArmorRenderer<?> supplyRenderer() {
        return (GeoArmorRenderer<?>) new GluttonyChefArmorGeoRender();
    }
}
