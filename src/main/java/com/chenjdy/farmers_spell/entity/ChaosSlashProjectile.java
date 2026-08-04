package com.chenjdy.farmers_spell.entity;

import com.chenjdy.farmers_spell.init.ModEffects;
import com.chenjdy.farmers_spell.init.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChaosSlashProjectile extends Projectile {
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(ChaosSlashProjectile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_SLASH_TYPE = SynchedEntityData.defineId(ChaosSlashProjectile.class, EntityDataSerializers.INT);
    private static final double SPEED = 1d;
    private static final int EXPIRE_TIME = 4 * 20;
    private static int slashCounter = 0;
    public AABB oldBB;
    private int age;
    private float damage;
    private int effectDuration = 10;
    private List<Entity> victims;
    private float initialYRot;
    private float initialXRot;
    private boolean rotationInitialized = false;

    @SuppressWarnings("this-escape")
    public ChaosSlashProjectile(EntityType<? extends ChaosSlashProjectile> entityType, Level level) {
        super(entityType, level);
        setRadius(.6f);
        oldBB = getBoundingBox();
        victims = new ArrayList<>();
        this.setNoGravity(true);
    }

    @SuppressWarnings("this-escape")
    public ChaosSlashProjectile(EntityType<? extends ChaosSlashProjectile> entityType, Level levelIn, LivingEntity shooter) {
        this(entityType, levelIn);
        setOwner(shooter);
        setYRot(shooter.getYRot());
        setXRot(shooter.getXRot());
        this.initialYRot = shooter.getYRot();
        this.initialXRot = shooter.getXRot();
        this.rotationInitialized = true;
        setSlashType(getNextSlashType());
    }

    public float getInitialYRot() {
        if (!rotationInitialized) {
            this.initialYRot = getYRot();
            this.initialXRot = getXRot();
            this.rotationInitialized = true;
        }
        return initialYRot;
    }

    public float getInitialXRot() {
        if (!rotationInitialized) {
            this.initialYRot = getYRot();
            this.initialXRot = getXRot();
            this.rotationInitialized = true;
        }
        return initialXRot;
    }

    public ChaosSlashProjectile(Level levelIn, LivingEntity shooter) {
        this(ModEntities.CHAOS_SLASH_PROJECTILE.get(), levelIn, shooter);
    }

    private static int getNextSlashType() {
        int type = slashCounter % 3;
        slashCounter++;
        return type;
    }

    public int getSlashType() {
        return this.getEntityData().get(DATA_SLASH_TYPE);
    }

    public void setSlashType(int type) {
        this.getEntityData().set(DATA_SLASH_TYPE, type);
    }

    public void shoot(Vec3 rotation) {
        setDeltaMovement(rotation.scale(SPEED));
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_RADIUS, 0.5F);
        builder.define(DATA_SLASH_TYPE, 0);
    }

    public void setRadius(float newRadius) {
        if (newRadius <= 3 && !this.level().isClientSide()) {
            this.getEntityData().set(DATA_RADIUS, Mth.clamp(newRadius, 0.0F, 3));
        }
    }

    public float getRadius() {
        return this.getEntityData().get(DATA_RADIUS);
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

        public void tick() {
        super.tick();
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
        if (++age > EXPIRE_TIME) {
            discard();
            return;
        }
        oldBB = getBoundingBox();
        setRadius(getRadius() + 0.12f);

        if (!level().isClientSide()) {
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() == HitResult.Type.BLOCK) {
                onHitBlock((BlockHitResult) hitresult);
            }
            for (Entity entity : level().getEntities(this, this.getBoundingBox()).stream().filter(target -> canHitEntity(target) && !victims.contains(target)).collect(Collectors.toSet())) {
                damageEntity(entity);
            }
        }

        setPos(position().add(getDeltaMovement()));
    }

    public EntityDimensions getDimensions(Pose p_19721_) {
        this.getBoundingBox();
        return EntityDimensions.scalable(this.getRadius() * 2.0F, 0.5F);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_19729_) {
        if (DATA_RADIUS.equals(p_19729_)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(p_19729_);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        discard();
    }

    private void damageEntity(Entity entity) {
        if (!victims.contains(entity)) {
            entity.hurt(this.damageSources().magic(), damage);
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addEffect(new MobEffectInstance(
                        ModEffects.CLAW_BREAK,
                        effectDuration * 20,
                        0,
                        false,
                        true,
                        true
                ));
            }
            victims.add(entity);
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return entity != getOwner() && super.canHitEntity(entity);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putFloat("Damage", this.damage);
        pCompound.putInt("EffectDuration", this.effectDuration);
        pCompound.putInt("SlashType", this.getSlashType());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.damage = pCompound.getFloat("Damage");
        this.effectDuration = pCompound.getInt("EffectDuration");
        this.setSlashType(pCompound.getInt("SlashType"));
    }
}
