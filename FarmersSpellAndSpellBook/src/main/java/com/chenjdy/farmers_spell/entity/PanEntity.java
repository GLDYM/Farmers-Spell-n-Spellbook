// package com.chenjdy.farmers_spell.entity;

// import com.chenjdy.farmers_spell.init.ModEffects;
// import com.chenjdy.farmers_spell.init.ModEntities;
// import net.minecraft.world.effect.MobEffectInstance;
// import net.minecraft.world.entity.*;
// import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
// import net.minecraft.world.entity.player.Player;
// import net.minecraft.world.level.Level;
// import net.minecraft.world.phys.AABB;
// import net.minecraft.world.phys.Vec3;
// import software.bernie.geckolib.animatable.GeoEntity;
// import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
// import software.bernie.geckolib.core.animation.AnimatableManager;
// import software.bernie.geckolib.core.animation.AnimationController;
// import software.bernie.geckolib.core.animation.RawAnimation;
// import software.bernie.geckolib.core.object.PlayState;
// import software.bernie.geckolib.util.GeckoLibUtil;

// import java.util.Collections;

// public class PanEntity extends LivingEntity implements GeoEntity {

//     private final int ticksToLive = 25;
//     private final int doDamageTick = 12;

//     private boolean didDamage = false;
//     private int ticksAlive = 0;
//     private boolean playSwingAnimation = true;

//     private float damage;
//     private int effectDuration;
//     private LivingEntity ownerEntity;

//     public PanEntity(EntityType<? extends PanEntity> entityType, Level level) {
//         super(entityType, level);
//         this.setNoGravity(true);
//         this.setInvulnerable(true);
//     }

//     public PanEntity(Level levelIn, LivingEntity owner, float damage, int effectDuration) {
//         this(ModEntities.PAN.get(), levelIn);
//         this.ownerEntity = owner;
//         this.damage = damage;
//         this.effectDuration = effectDuration;

//         this.setYRot(owner.getYRot());
//         this.setXRot(owner.getXRot());
//         this.setYBodyRot(owner.getYRot());
//         this.setYHeadRot(owner.getYRot());
//     }

//     @Override
//     public boolean hurt(net.minecraft.world.damagesource.DamageSource pSource, float pAmount) {
//         return false;
//     }

//     @Override
//     public void tick() {
//         if (++ticksAlive >= ticksToLive) {
//             discard();
//         }

//         if (ticksAlive >= doDamageTick && !didDamage) {
//             performDamage();
//             didDamage = true;
//         }

//         super.tick();
//     }

//     private void performDamage() {
//         Vec3 pos = this.position();
//         float width = 3.0f;
//         float height = 1.0f;
//         float depth = 3.0f;

//         Vec3 forward = this.getLookAngle();
//         Vec3 centerPos = pos.add(forward.scale(1.0));

//         AABB damageBox = new AABB(
//                 centerPos.x - width / 2, pos.y - height / 2, centerPos.z - depth / 2,
//                 centerPos.x + width / 2, pos.y + height / 2, centerPos.z + depth / 2
//         );

//         for (Entity entity : level().getEntities(this, damageBox)) {
//             if (entity != ownerEntity && entity instanceof LivingEntity livingEntity) {
//                 entity.hurt(this.damageSources().magic(), damage);
//                 livingEntity.addEffect(new MobEffectInstance(
//                         ModEffects.CLAW_BREAK.get(),
//                         effectDuration * 20,
//                         0,
//                         false,
//                         true,
//                         true
//                 ));
//             }
//         }
//     }

//     @Override
//     public boolean isPushable() {
//         return false;
//     }

//     @Override
//     public boolean isInvulnerable() {
//         return true;
//     }

//     protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
//         return pDimensions.height * 0.6F;
//     }

//     @Override
//     public boolean isNoGravity() {
//         return true;
//     }

//     public static AttributeSupplier.Builder prepareAttributes() {
//         return LivingEntity.createLivingAttributes();
//     }

//     @Override
//     public Iterable<net.minecraft.world.item.ItemStack> getArmorSlots() {
//         return Collections.singleton(net.minecraft.world.item.ItemStack.EMPTY);
//     }

//     @Override
//     public net.minecraft.world.item.ItemStack getItemBySlot(EquipmentSlot pSlot) {
//         return net.minecraft.world.item.ItemStack.EMPTY;
//     }

//     @Override
//     public void setItemSlot(EquipmentSlot pSlot, net.minecraft.world.item.ItemStack pStack) {
//     }

//     @Override
//     public HumanoidArm getMainArm() {
//         return HumanoidArm.RIGHT;
//     }

//     private final RawAnimation animationBuilder = RawAnimation.begin().thenPlay("pan_swing");
//     private final AnimationController<PanEntity> animationController = new AnimationController<>(this, "controller", 0, this::predicate);

//     private PlayState predicate(software.bernie.geckolib.core.animation.AnimationState<PanEntity> event) {
//         if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
//             if (playSwingAnimation) {
//                 event.getController().setAnimation(animationBuilder);
//                 playSwingAnimation = false;
//             }
//         }
//         return PlayState.CONTINUE;
//     }

//     @Override
//     public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
//         controllerRegistrar.add(animationController);
//     }

//     @Override
//     public AnimatableInstanceCache getAnimatableInstanceCache() {
//         return this.cache;
//     }

//     private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
// }
