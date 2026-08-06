package com.chenjdy.farmers_spell.client.renderer;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.entity.FoodgeistEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FoodgeistRender extends GeoEntityRenderer<FoodgeistEntity> {

    public FoodgeistRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FoodgeistModel());
        this.shadowRadius = 0.5F;
    }

    @Override
    public RenderType getRenderType(FoodgeistEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutout(texture);
    }

    @Override
    public float getMotionAnimThreshold(FoodgeistEntity animatable) {
        return 0.001F;
    }

    @Override
    protected float getDeathMaxRotation(FoodgeistEntity entityLivingBaseIn) {
        return 180.0F;
    }
    
    @Override
    public void render(FoodgeistEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0, 0, 0);
        poseStack.scale(-1.0F, 1.0F, -1.0F);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }

    private static class FoodgeistModel extends GeoModel<FoodgeistEntity> {
        private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "geo/foodgeist.geo.json");
        private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "textures/entity/foodgeist.png");
        private static final ResourceLocation ANIMATIONS = ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "animations/foodgeist.animation.json");

        @Override
        public ResourceLocation getModelResource(FoodgeistEntity object) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(FoodgeistEntity object) {
            return TEXTURE;
        }

        @Override
        public ResourceLocation getAnimationResource(FoodgeistEntity animatable) {
            return ANIMATIONS;
        }
    }
}
