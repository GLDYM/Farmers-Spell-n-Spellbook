package com.chenjdy.farmers_spell.item.armor;

import com.chenjdy.farmers_spell.client.renderer.GluttonyChefArmorGeoRender;
import net.minecraft.world.item.ArmorItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class GluttonyChefBootsItem extends GluttonyChefArmorItem {
    public GluttonyChefBootsItem(Properties settings) {
        super(ArmorItem.Type.BOOTS, settings);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("unchecked")
    public GeoArmorRenderer<?> supplyRenderer() {
        return (GeoArmorRenderer<?>) new GluttonyChefArmorGeoRender();
    }
}
