package com.chenjdy.farmers_spell.entity;

import com.chenjdy.farmers_spell.init.ModBlocks;
import com.chenjdy.farmers_spell.init.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import com.chenjdy.farmers_spell.network.BadApplePacket;

import java.util.List;
import static java.awt.Color.getHSBColor;
import net.minecraft.core.particles.ColorParticleOption;


public class BadAppleEntity extends LivingEntity {
    
    private BlockState blockState;
    private int lifetime = 0;
    private int maxLifetime = 6228;
    private boolean musicPlaying = false;
    private Vec3 spawnPos;
    private int clientTick = 0;
    private int spellLevel = 1;
    
    public BadAppleEntity(EntityType<? extends BadAppleEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
        this.setNoGravity(true);
        this.setDeltaMovement(0, 0, 0);
        this.blockState = ModBlocks.BAD_APPLE.get().defaultBlockState();
    }
    
    public BadAppleEntity(Level pLevel, Vec3 pos, int spellLevel) {
        this(ModEntities.BAD_APPLE_ENTITY.get(), pLevel);
        this.spellLevel = spellLevel;
        configureMaxHealth();
        setHealth(getMaxHealth());
        this.spawnPos = pos;
        this.setPos(pos.x + 0.5, pos.y, pos.z + 0.5);
        this.blockState = ModBlocks.BAD_APPLE.get().defaultBlockState();
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 100.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.ARMOR, 10.0);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SpawnX")) {
            spawnPos = new Vec3(tag.getDouble("SpawnX"), tag.getDouble("SpawnY"), tag.getDouble("SpawnZ"));
        }
        if (tag.contains("SpellLevel")) {
            spellLevel = tag.getInt("SpellLevel");
            configureMaxHealth();
        }
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (spawnPos != null) {
            tag.putDouble("SpawnX", spawnPos.x);
            tag.putDouble("SpawnY", spawnPos.y);
            tag.putDouble("SpawnZ", spawnPos.z);
        }
        tag.putInt("SpellLevel", spellLevel);
    }
    
        public void tick() {
        super.tick();
        
        if (!level().isClientSide) {
            if (spawnPos != null) {
                this.setPos(spawnPos.x + 0.5, spawnPos.y, spawnPos.z + 0.5);
            }
            this.setDeltaMovement(0, 0, 0);
            
            lifetime++;
            
            if (lifetime >= maxLifetime) {
                this.discard();
                return;
            }
            
            double tauntRange = 8.0 + 2.0 * (spellLevel - 1);
            List<Mob> nearbyMobs = level().getEntitiesOfClass(Mob.class, 
                this.getBoundingBox().inflate(tauntRange + 3.0), 
                mob -> mob.getTarget() == null || mob.getTarget() != this);
            
            for (Mob mob : nearbyMobs) {
                if (mob.distanceTo(this) <= tauntRange) {
                    mob.setTarget(this);
                }
            }
            
            if (!musicPlaying) {
                playMusic();
                musicPlaying = true;
            }
        } else {
            clientTick++;
            spawnRainbowParticles();
        }
    }
    
    private void spawnRainbowParticles() {
        float hue = (clientTick * 0.02f) % 1.0f;
        
        if (clientTick % 3 == 0) {
            double offset = 0.5;
            double x = this.getX() + (Math.random() - 0.5) * offset;
            double y = this.getY() + 0.5 + (Math.random() - 0.5) * 0.2;
            double z = this.getZ() + (Math.random() - 0.5) * offset;
            
            float[] rgb = getHSBColor(hue, 1.0f, 1.0f).getRGBColorComponents(null);
            level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, (float)(rgb[0]), (float)(rgb[1]), (float)(rgb[2])), x, y, z, 0.0D, 0.0D, 0.0D);
        }

        if (clientTick % 5 == 0) {
            int particleCount = 4;
            for (int i = 0; i < particleCount; i++) {
                double angle = (clientTick * 0.1) + (i * Math.PI * 2 / particleCount);
                double radius = 0.8 + Math.sin(clientTick * 0.05) * 0.2;
                double px = this.getX() + Math.cos(angle) * radius;
                double pz = this.getZ() + Math.sin(angle) * radius;
                double py = this.getY() + 0.5 + Math.sin(clientTick * 0.1 + i) * 0.3;
                
                float particleHue = (hue + (float)i / particleCount) % 1.0f;
                float[] rgb = getHSBColor(particleHue, 1.0f, 1.0f).getRGBColorComponents(null);
                level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, (float)(rgb[0]), (float)(rgb[1]), (float)(rgb[2])), px, py, pz, 0.0D, 0.0D, 0.0D);
            }
        }

        if (clientTick % 10 == 0) {
            double x = this.getX() + (Math.random() - 0.5) * 0.3;
            double y = this.getY() + 0.5;
            double z = this.getZ() + (Math.random() - 0.5) * 0.3;
            
            float[] rgb = getHSBColor(hue + 0.3f, 1.0f, 0.8f).getRGBColorComponents(null);
            level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, (float)(rgb[0]), (float)(rgb[1]), (float)(rgb[2])), x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
    
    @Override
    public void remove(RemovalReason reason) {
        if (!level().isClientSide) {
            stopMusic();
        }
        super.remove(reason);
    }

    @Override
    public void die(DamageSource cause) {
        if (musicPlaying) {
            stopMusic();
        }
        super.die(cause);
    }
    
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.IN_WALL)) {
            return false;
        }
        return super.hurt(source, amount);
    }
    
    private void playMusic() {
        BadApplePacket.sendToAll(level(), getUUID(), this.blockPosition(), true);
    }

    private void stopMusic() {
        BadApplePacket.sendToAll(level(), getUUID(), this.blockPosition(), false);
        musicPlaying = false;
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
    
    @Override
    public boolean isPushable() {
        return false;
    }
    
    public BlockState getBlockState() {
        return blockState;
    }
    
    public int getClientTick() {
        return clientTick;
    }
    
    public int getSpellLevel() {
        return spellLevel;
    }

    private void configureMaxHealth() {
        float maxHealth = 50.0f + 10.0f * spellLevel;
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHealth);
    }
    
    
    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }
    
    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {}
    
    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return List.of();
    }
    
    @Override
    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }
}
