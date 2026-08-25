package com.chenjdy.farmers_spell.entity;

import com.chenjdy.farmers_spell.init.ModEffects;
import com.chenjdy.farmers_spell.init.ModEntities;
import com.chenjdy.farmers_spell.init.ModSpells;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
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
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChaosSlashProjectile extends AbstractMagicProjectile {
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(ChaosSlashProjectile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_SLASH_TYPE = SynchedEntityData.defineId(ChaosSlashProjectile.class, EntityDataSerializers.INT);
    private static final float SPEED = 1f;
    private static final int EXPIRE_TIME = 4 * 20;
    private static int slashCounter = 0;
    public AABB oldBB;
    private int effectDuration = 10;
    private final List<Entity> victims = new ArrayList<>();

    public ChaosSlashProjectile(EntityType<? extends ChaosSlashProjectile> entityType, Level level) {
        super(entityType, level);
        setRadius(.6f);
        oldBB = getBoundingBox();
        this.setNoGravity(true);
    }

    public ChaosSlashProjectile(EntityType<? extends ChaosSlashProjectile> entityType, Level levelIn, LivingEntity shooter) {
        this(entityType, levelIn);
        setOwner(shooter);
        setYRot(shooter.getYRot());
        setXRot(shooter.getXRot());
        setSlashType(getNextSlashType());
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
        super.shoot(rotation.scale(SPEED));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
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

    @Override
    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    @Override
    public void tick() {
        if (this.tickCount > EXPIRE_TIME) {
            discard();
            return;
        }
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
        oldBB = getBoundingBox();
        setRadius(getRadius() + 0.12f);
        super.tick();
    }

    @Override
    public void handleHitDetection() {
        if (level().isClientSide()) {
            return;
        }

        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            ProjectileImpactEvent event = new ProjectileImpactEvent(this, hitResult);
            NeoForge.EVENT_BUS.post(event);
            if (!event.isCanceled()) {
                onHitBlock((BlockHitResult) hitResult);
            }
        }

        for (Entity entity : level().getEntities(this, getBoundingBox()).stream()
                .filter(target -> canHitEntity(target) && !victims.contains(target))
                .collect(Collectors.toSet())) {
            damageEntity(entity);
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose p_19721_) {
        this.getBoundingBox();
        return EntityDimensions.scalable(this.getRadius() * 2.0F, 0.5F);
    }

    @Override
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
            entity.hurt(ModSpells.CHAOS_SLASH_SPELL.get().getDamageSource(this, getOwner()), getDamage());
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
    public void trailParticles() {
    }

    @Override
    public void impactParticles(double x, double y, double z) {
    }

    @Override
    public float getSpeed() {
        return SPEED;
    }

    @Override
    public java.util.Optional<net.minecraft.core.Holder<net.minecraft.sounds.SoundEvent>> getImpactSound() {
        return java.util.Optional.empty();
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return entity != getOwner() && super.canHitEntity(entity);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("EffectDuration", this.effectDuration);
        pCompound.putInt("SlashType", this.getSlashType());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.effectDuration = pCompound.getInt("EffectDuration");
        this.setSlashType(pCompound.getInt("SlashType"));
    }
}
