package com.chenjdy.farmers_spell.client.renderer;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.client.shaders.FarmersSpellRenderTypes;
import com.chenjdy.farmers_spell.entity.ChaosSlashProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ChaosSlashRender extends GeoEntityRenderer<ChaosSlashProjectile> {

    private static final float PLANE_Y = 3.0F / 16.0F;

    public ChaosSlashRender(EntityRendererProvider.Context context) {
        super(context, new ChaosSlashModel());
        this.shadowRadius = 0.0F;
    }

    @Override
    public RenderType getRenderType(ChaosSlashProjectile animatable, ResourceLocation texture,@Nullable MultiBufferSource bufferSource, float partialTick) {
        return FarmersSpellRenderTypes.CHAOS_SLASH;
    }

    @Override
    public void preRender(PoseStack poseStack, ChaosSlashProjectile animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        float oldWidth = (float) animatable.oldBB.getXsize();
        float width = animatable.getBbWidth();
        float scale = Mth.lerp(Math.min(partialTick, 1.0F), oldWidth, width);
        poseStack.translate(0.0F, PLANE_Y * (1.0F - scale), 0.0F);
        poseStack.scale(scale, scale, scale);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    protected void applyRotations(ChaosSlashProjectile animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        float yaw = Mth.lerp(partialTick, animatable.yRotO, animatable.getYRot());
        float pitch = Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot());
        float tiltAngle = switch (animatable.getSlashType()) {
            case 1 -> -22.5F;
            case 2 -> 22.5F;
            default -> 0.0F;
        };
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));
        poseStack.mulPose(Axis.ZP.rotationDegrees(tiltAngle));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
    }

    private static class ChaosSlashModel extends GeoModel<ChaosSlashProjectile> {
        private static final ResourceLocation MODEL =
                ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "geo/chaos_slash.geo.json");
        private static final ResourceLocation TEXTURE =
                ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "textures/entity/chaos_slash.png");
        private static final ResourceLocation ANIMATIONS =
                ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "animations/chaos_slash.animation.json");

        @Override
        public ResourceLocation getModelResource(ChaosSlashProjectile animatable) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(ChaosSlashProjectile animatable) {
            return TEXTURE;
        }

        @Override
        public ResourceLocation getAnimationResource(ChaosSlashProjectile animatable) {
            return ANIMATIONS;
        }
    }
}

