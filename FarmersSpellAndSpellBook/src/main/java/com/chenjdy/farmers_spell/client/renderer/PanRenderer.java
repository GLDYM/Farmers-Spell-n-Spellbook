// package com.chenjdy.farmers_spell.client.renderer;

// import com.mojang.blaze3d.vertex.PoseStack;
// import com.mojang.blaze3d.vertex.VertexConsumer;
// import com.chenjdy.farmers_spell.entity.PanEntity;
// import net.minecraft.client.renderer.MultiBufferSource;
// import net.minecraft.client.renderer.entity.EntityRendererProvider;
// import net.minecraft.resources.ResourceLocation;
// import software.bernie.geckolib.cache.object.BakedGeoModel;
// import software.bernie.geckolib.renderer.GeoEntityRenderer;

// public class PanRenderer extends GeoEntityRenderer<PanEntity> {
//     public PanRenderer(EntityRendererProvider.Context renderManager) {
//         super(renderManager, new PanModel());
//         this.shadowRadius = 0.3f;
//     }

//     @Override
//     public ResourceLocation getTextureLocation(PanEntity animatable) {
//         return PanModel.textureResource;
//     }

//     @Override
//     public void preRender(PoseStack poseStack, PanEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//         poseStack.scale(3, 3, 3);
//         super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
//     }
// }
