package com.chenjdy.farmers_spell.client.renderer;

import com.chenjdy.farmers_spell.entity.PanEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PanRenderer extends GeoEntityRenderer<PanEntity> {
    public PanRenderer(EntityRendererProvider.Context context) {
        super(context, new PanModel());
        this.shadowRadius = 0.3F;
    }

    @Override
    public void render(PanEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(3.0F, 3.0F, 3.0F);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}
