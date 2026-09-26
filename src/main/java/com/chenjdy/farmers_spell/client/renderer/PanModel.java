package com.chenjdy.farmers_spell.client.renderer;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.entity.PanEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PanModel extends GeoModel<PanEntity> {
    @Override
    public ResourceLocation getModelResource(PanEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "geo/pan.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PanEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("farmersdelight", "textures/block/skillet_top.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PanEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "animations/pan.animation.json");
    }
}
