package com.chenjdy.farmers_spell.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

/**
 * 与 {@link AutoGlowingGeoLayer} 行为一致，唯一区别是重绘发光层时传入
 * {@link LightTexture#FULL_BRIGHT}（方块光/天空光均为15）而非 {@code FULL_SKY}（仅天空光15）。
 * <p>
 * 原版渲染下发光shader不读取光照贴图，二者表现完全相同；
 * 开启Oculus光影后，方块实体阶段发光层会被当作普通表面按光照贴图着色，
 * FULL_SKY在暗处/夜晚会被压黑，FULL_BRIGHT则与光影全亮程序注入的常量一致，保证发光可见。
 */
public class FullbrightGlowingGeoLayer<T extends GeoAnimatable> extends AutoGlowingGeoLayer<T> {

    public FullbrightGlowingGeoLayer(GeoRenderer<T> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType,
                       MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick,
                       int packedLight, int packedOverlay) {
        RenderType emissiveRenderType = getRenderType(animatable);

        getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, emissiveRenderType,
                bufferSource.getBuffer(emissiveRenderType), partialTick, LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }
}
