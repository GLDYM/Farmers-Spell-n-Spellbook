/*package com.chenjdy.farmers_spell.client.renderer;

import com.chenjdy.farmers_spell.entity.KinfeProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class KnifeProjectileRender extends EntityRenderer<KinfeProjectile> {
    
    private final ItemRenderer itemRenderer;
    private static final ResourceLocation ITEM_ID = ResourceLocation.fromNamespaceAndPath("farmersdelight", "iron_knife");
    
    public KnifeProjectileRender(Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }
    
    @Override
    public ResourceLocation getTextureLocation(KinfeProjectile entity) {
        return ITEM_ID;
    }
    
    @Override
public void render(KinfeProjectile entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
    var item = ForgeRegistries.ITEMS.getValue(ITEM_ID);
    if (item == null) {
        return;
    }

    ItemStack itemStack = new ItemStack(item);
    if (itemStack.isEmpty()) {
        return;
    }

    poseStack.pushPose();

    float tick = entity.tickCount + partialTick;

    poseStack.translate(0, 0.15F, 0);

    if (!entity.inGround) {
        poseStack.mulPose(Axis.XP.rotationDegrees(tick * 30));
    } else {
        float f9 = entity.shakeTime - partialTick;
        if (f9 > 0.0F) {
            float f10 = -Mth.sin(f9 * 3.0F) * f9;
            poseStack.mulPose(Axis.XP.rotationDegrees(f10));
        }
    }

    poseStack.mulPose(Axis.ZP.rotationDegrees(180));
    poseStack.scale(0.8f, 0.8f, 0.8f);

    itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.level(), entity.getId());

    poseStack.popPose();
  }
}*/
