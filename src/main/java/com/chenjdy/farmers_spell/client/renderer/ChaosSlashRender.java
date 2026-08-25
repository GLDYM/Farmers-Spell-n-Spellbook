package com.chenjdy.farmers_spell.client.renderer;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.entity.ChaosSlashProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ChaosSlashRender extends EntityRenderer<ChaosSlashProjectile> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "textures/entity/chaos_slash.png");

    public ChaosSlashRender(Context context) {
        super(context);
    }

    @Override
    public void render(ChaosSlashProjectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();

        float entityYaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        float entityPitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());

        poseStack.mulPose(Axis.YP.rotationDegrees(entityYaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(-entityPitch));

        float oldWidth = (float) entity.oldBB.getXsize();
        float width = entity.getBbWidth();
        width = oldWidth + (width - oldWidth) * Math.min(partialTicks, 1);

        int slashType = entity.getSlashType();
        float tiltAngle = switch (slashType) {
            case 1 -> -22.5f;
            case 2 -> 22.5f;
            default -> 0f;
        };

        poseStack.mulPose(Axis.ZP.rotationDegrees(tiltAngle));

        Pose pose = poseStack.last();
        drawSlash(pose, bufferSource, light, width);

        poseStack.popPose();

        super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    private void drawSlash(Pose pose, MultiBufferSource bufferSource, int light, float width) {
        Matrix4f poseMatrix = pose.pose();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        float halfWidth = width * .5f;

        consumer.addVertex(poseMatrix, -halfWidth, -.1f, -halfWidth).setColor(255, 255, 255, 255).setUv(0f, 1f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0f, 1f, 0f);
        consumer.addVertex(poseMatrix, halfWidth, -.1f, -halfWidth).setColor(255, 255, 255, 255).setUv(1f, 1f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0f, 1f, 0f);
        consumer.addVertex(poseMatrix, halfWidth, -.1f, halfWidth).setColor(255, 255, 255, 255).setUv(1f, 0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0f, 1f, 0f);
        consumer.addVertex(poseMatrix, -halfWidth, -.1f, halfWidth).setColor(255, 255, 255, 255).setUv(0f, 0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0f, 1f, 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(ChaosSlashProjectile entity) {
        return TEXTURE;
    }
}
