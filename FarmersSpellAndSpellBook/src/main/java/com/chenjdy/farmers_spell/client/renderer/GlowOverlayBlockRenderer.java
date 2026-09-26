package com.chenjdy.farmers_spell.client.renderer;

import com.chenjdy.farmers_spell.block.entity.GlowOverlayBlockEntity;
import com.mojang.math.Axis;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class GlowOverlayBlockRenderer extends GeoBlockRenderer<GlowOverlayBlockEntity> {

    public GlowOverlayBlockRenderer(BlockEntityType<? extends GlowOverlayBlockEntity> blockEntityType) {
        super(new GlowOverlayGeoModel(BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(blockEntityType)));
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @Override
    protected void rotateBlock(Direction facing, PoseStack poseStack) {
        BlockState state = this.animatable.getBlockState();
        if (!state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            return;
        }
        switch (facing) {
            case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(90));
            case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(180));
            case NORTH -> poseStack.mulPose(Axis.YP.rotationDegrees(270));
            case WEST -> { }
            default -> { }
        }
    }
}
