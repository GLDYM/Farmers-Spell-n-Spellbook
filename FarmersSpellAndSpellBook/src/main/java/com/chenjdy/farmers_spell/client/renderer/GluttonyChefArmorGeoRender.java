package com.chenjdy.farmers_spell.client.renderer;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.item.armor.GluttonyChefArmorItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.RenderUtils;

import javax.annotation.Nullable;

public class GluttonyChefArmorGeoRender extends GeoArmorRenderer<GluttonyChefArmorItem> {

    public GeoBone leggingTorsoLayerBone = null;

    public GluttonyChefArmorGeoRender() {
        super(new GluttonyChefArmorModel());
    }

    public static class GluttonyChefArmorModel extends GeoModel<GluttonyChefArmorItem> {
        @Override
        public ResourceLocation getModelResource(GluttonyChefArmorItem object) {
            return ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "geo/gluttony_chef_armor.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(GluttonyChefArmorItem object) {
            return ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "textures/armor/gluttony_chef.png");
        }

        @Override
        public ResourceLocation getAnimationResource(GluttonyChefArmorItem animatable) {
            return ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "animations/gluttony_chef_armor_animation.json");
        }
    }

    @Override
    public void scaleModelForBaby(PoseStack poseStack, GluttonyChefArmorItem animatable, float partialTick, boolean isReRender) {
        return;
    }

    @Nullable
    public GeoBone getLeggingTorsoLayerBone() {
        return this.model.getBone("armorLeggingTorsoLayer").orElse(null);
    }

    @Override
    protected void grabRelevantBones(BakedGeoModel bakedModel) {
        if (this.lastModel != bakedModel)
            this.leggingTorsoLayerBone = getLeggingTorsoLayerBone();

        super.grabRelevantBones(bakedModel);
    }

    @Override
    protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
        super.applyBoneVisibilityBySlot(currentSlot);
        if (currentSlot == EquipmentSlot.LEGS) {
            setBoneVisible(this.leggingTorsoLayerBone, true);
        }
    }

    @Override
    public void applyBoneVisibilityByPart(EquipmentSlot currentSlot, ModelPart currentPart, HumanoidModel<?> model) {
        super.applyBoneVisibilityByPart(currentSlot, currentPart, model);
        if (currentPart == model.body && currentSlot == EquipmentSlot.LEGS) {
            setBoneVisible(this.leggingTorsoLayerBone, true);
        }
    }

    @Override
    protected void applyBaseTransformations(HumanoidModel<?> baseModel) {
        super.applyBaseTransformations(baseModel);
        if (this.leggingTorsoLayerBone != null) {
            ModelPart bodyPart = baseModel.body;
            RenderUtils.matchModelPartRot(bodyPart, this.leggingTorsoLayerBone);
            this.leggingTorsoLayerBone.updatePosition(bodyPart.x, -bodyPart.y, bodyPart.z);
        }
    }

    @Override
    public void setAllVisible(boolean pVisible) {
        super.setAllVisible(pVisible);
        setBoneVisible(this.leggingTorsoLayerBone, pVisible);
    }
}