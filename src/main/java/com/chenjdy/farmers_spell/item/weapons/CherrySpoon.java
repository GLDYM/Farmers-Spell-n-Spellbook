package com.chenjdy.farmers_spell.item.weapons;

import com.chenjdy.farmers_spell.client.renderer.CherrySpoonRender;
import com.chenjdy.farmers_spell.FarmersSpell;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.CastingItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Holder;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;


public class CherrySpoon extends CastingItem implements GeoItem {
    private static final UUID ATTACK_DAMAGE_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    private static final UUID ATTACK_SPEED_UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");

    private final Multimap<Holder<Attribute>, AttributeModifier> defaultModifiers;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @SuppressWarnings("this-escape")
    public CherrySpoon(Properties properties) {
        super(properties);
        ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> builder = ImmutableMultimap.builder();

        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "weapon_modifier"), 3.0, AttributeModifier.Operation.ADD_VALUE));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "weapon_modifier"), -3.0, AttributeModifier.Operation.ADD_VALUE));
        builder.put(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.SPELL_POWER.get()), new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "spell_power"), 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        builder.put(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.CAST_TIME_REDUCTION.get()), new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "cast_time"), 0.20, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        builder.put(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.COOLDOWN_REDUCTION.get()), new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "cooldown"), 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

        this.defaultModifiers = builder.build();
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return (slot == EquipmentSlot.MAINHAND) ? this.defaultModifiers : ImmutableMultimap.of();
    }

    @SuppressWarnings("removal")
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private CherrySpoonRender renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
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
