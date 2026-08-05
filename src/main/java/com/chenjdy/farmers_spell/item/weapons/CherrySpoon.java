package com.chenjdy.farmers_spell.item.weapons;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.client.renderer.CherrySpoonRender;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class CherrySpoon extends StaffItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public CherrySpoon() {
        super(
            new Item.Properties()
                .rarity(Rarity.RARE)
                .stacksTo(1)
                .attributes(
                    ItemAttributeModifiers.builder()
                        .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 3.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -3.0F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .add(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.SPELL_POWER.get()), new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "spell_power"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND)
                        .add(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.CAST_TIME_REDUCTION.get()), new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "cast_time"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND)
                        .add(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.COOLDOWN_REDUCTION.get()), new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "cooldown"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND)
                        .build()
                )
        );
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public boolean hasCustomRendering() {
        return true;
    }


    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }   

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private CherrySpoonRender renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null) {
                    this.renderer = new CherrySpoonRender();
                }
                return this.renderer;
            }
        });
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }
}
