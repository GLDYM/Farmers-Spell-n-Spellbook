package com.chenjdy.farmers_spell.client.renderer;

import com.chenjdy.farmers_spell.block.entity.GlowOverlayBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class GlowOverlayGeoModel extends DefaultedBlockGeoModel<GlowOverlayBlockEntity> {

    private final ResourceLocation assetSubpath;

    public GlowOverlayGeoModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
        this.assetSubpath = assetSubpath;
    }

    @Override
    public ResourceLocation getModelResource(GlowOverlayBlockEntity animatable) {
        BlockState state = animatable.getBlockState();
        return buildFormattedModelPath(
                ResourceLocation.fromNamespaceAndPath(assetSubpath.getNamespace(), assetSubpath.getPath() + stateSuffix(state)));
    }

    private static String stateSuffix(BlockState state) {
        for (Property<?> property : state.getProperties()) {
            String name = property.getName();
            if (("bites".equals(name) || "servings".equals(name)) && property instanceof IntegerProperty intProperty) {
                return "_" + name + state.getValue(intProperty);
            }
        }
        return "";
    }
}
