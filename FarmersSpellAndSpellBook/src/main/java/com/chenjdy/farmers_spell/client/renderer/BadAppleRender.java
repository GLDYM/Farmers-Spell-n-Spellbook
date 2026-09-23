package com.chenjdy.farmers_spell.client.renderer;

import com.chenjdy.farmers_spell.entity.BadAppleEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;

@OnlyIn(Dist.CLIENT)
public class BadAppleRender extends EntityRenderer<BadAppleEntity> {
   private final BlockRenderDispatcher dispatcher;

   public BadAppleRender(EntityRendererProvider.Context pContext) {
      super(pContext);
      this.shadowRadius = 0.5F;
      this.dispatcher = pContext.getBlockRenderDispatcher();
   }

   public void render(BadAppleEntity entity, float pEntityYaw, float pPartialTicks, PoseStack poseStack, MultiBufferSource buffer, int pPackedLight) {
      BlockState blockstate = entity.getBlockState();
      if (blockstate == null) {
         return;
      }
      if (blockstate.getRenderShape() == RenderShape.MODEL) {
         Level level = entity.level();
         poseStack.pushPose();
         
         BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
         poseStack.translate(-0.5D, 0.0D, -0.5D);
         
         float scale = 2.0F;
         poseStack.translate(0.5,1.2, 0.5);
         poseStack.scale(scale, scale, scale);
         poseStack.translate(-0.5, -0.5, -0.5);
         
         float tick = entity.getClientTick() + pPartialTicks;
         float floatY = Mth.sin(tick * 0.05F) * 0.1F;
         poseStack.translate(0.5, floatY, 0.5);
         poseStack.mulPose(Axis.YP.rotationDegrees(tick * 2.0F));
         poseStack.translate(-0.5, 0, -0.5);
         
         var model = this.dispatcher.getBlockModel(blockstate);
         for (var renderType : model.getRenderTypes(blockstate, RandomSource.create(blockstate.getSeed(entity.blockPosition())), ModelData.EMPTY))
            this.dispatcher.getModelRenderer().tesselateBlock(level, model, blockstate, blockpos, poseStack, buffer.getBuffer(renderType), false, RandomSource.create(), blockstate.getSeed(entity.blockPosition()), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
         
         poseStack.popPose();
         super.render(entity, pEntityYaw, pPartialTicks, poseStack, buffer, pPackedLight);
      }
   }

   public ResourceLocation getTextureLocation(BadAppleEntity pEntity) {
      return TextureAtlas.LOCATION_BLOCKS;
   }
}