package com.chenjdy.farmers_spell.entity;

import com.chenjdy.farmers_spell.init.ModEffects;
import com.chenjdy.farmers_spell.init.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PanEntity extends Entity implements GeoEntity {
    private static final int LIFETIME_TICKS = 25;
    private static final int DAMAGE_TICK = 12;
    private static final RawAnimation SWING = RawAnimation.begin().thenPlay("pan_swing");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private LivingEntity owner;
    private float damage;
    private int effectDuration;
    private boolean didDamage;
    private boolean playSwingAnimation = true;

    public PanEntity(EntityType<? extends PanEntity> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
        this.setInvulnerable(true);
    }

    public PanEntity(Level level, LivingEntity owner, float damage, int effectDuration) {
        this(ModEntities.PAN.get(), level);
        this.owner = owner;
        this.damage = damage;
        this.effectDuration = effectDuration;
        this.setYRot(owner.getYRot());
        this.setXRot(owner.getXRot());
        this.setYBodyRot(owner.getYRot());
        this.setYHeadRot(owner.getYRot());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount >= LIFETIME_TICKS) {
            this.discard();
            return;
        }
        if (!this.level().isClientSide && !this.didDamage && this.tickCount >= DAMAGE_TICK) {
            this.performDamage();
            this.didDamage = true;
        }
    }

    private void performDamage() {
        Vec3 position = this.position();
        Vec3 center = position.add(this.getLookAngle());
        AABB damageBox = new AABB(
                center.x - 1.5, position.y - 0.5, center.z - 1.5,
                center.x + 1.5, position.y + 0.5, center.z + 1.5);

        for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class, damageBox,
                candidate -> candidate != this.owner)) {
            target.hurt(this.damageSources().magic(), this.damage);
            target.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                    ModEffects.CLAW_BREAK, this.effectDuration * 20, 0, false, true, true));
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<PanEntity> state) {
        if (this.playSwingAnimation
                && state.getController().getAnimationState() == AnimationController.State.STOPPED) {
            state.getController().setAnimation(SWING);
            this.playSwingAnimation = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
