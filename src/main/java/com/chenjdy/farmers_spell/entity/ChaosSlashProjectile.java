package com.chenjdy.farmers_spell.entity;

import com.chenjdy.farmers_spell.init.ModEffects;
import com.chenjdy.farmers_spell.init.ModEntities;
import com.chenjdy.farmers_spell.init.ModSpells;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.ForgeEventFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
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

    @Override
    public float getSpeed() {
        return SPEED;
    }

    @Override
    public void trailParticles() {
    }

    @Override
    public void impactParticles(double x, double y, double z) {
    }

    @Override
    public Optional<Supplier<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_RADIUS, 0.5F);
        this.getEntityData().define(DATA_SLASH_TYPE, 0);
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
    public void tick() {
        if (this.tickCount > EXPIRE_TIME) {
            this.discard();
            return;
        }
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
        this.oldBB = this.getBoundingBox();
        this.setRadius(this.getRadius() + 0.12f);
        super.tick();
    }

    @Override
    public void handleHitDetection() {
        if (!level().isClientSide()) {
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() == HitResult.Type.BLOCK
                    && !ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                onHitBlock((BlockHitResult) hitresult);
            }
            for (Entity entity : level().getEntities(this, this.getBoundingBox()).stream()
                    .filter(target -> canHitEntity(target) && !victims.contains(target))
                    .collect(Collectors.toSet())) {
                damageEntity(entity);
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        discard();
    }

    private void damageEntity(Entity entity) {
        if (!victims.contains(entity)) {
            DamageSources.applyDamage(entity, getDamage(),
                    ModSpells.CHAOS_SLASH_SPELL.get().getDamageSource(this, getOwner()));
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addEffect(new MobEffectInstance(
                        ModEffects.CLAW_BREAK.get(),
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
