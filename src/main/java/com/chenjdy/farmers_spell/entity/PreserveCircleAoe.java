package com.chenjdy.farmers_spell.entity;

import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Optional;

import static com.chenjdy.farmers_spell.init.ModEffects.MAGICAL_INGREDIENT;
import static com.chenjdy.farmers_spell.init.ModEntities.PRESERVE_CIRCLE_AOE;

public class PreserveCircleAoe extends AoeEntity {
    private static final Vector3f ORANGE_COLOR = new Vector3f(1.0f, 0.5f, 0.0f);
    private static final DustParticleOptions ORANGE_DUST = new DustParticleOptions(ORANGE_COLOR, 1.0f);
    
    private int spellLevel;
    private int tickInArea;

    public PreserveCircleAoe(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.reapplicationDelay = 20;
        this.tickInArea = 0;
    }

    @SuppressWarnings("this-escape")
    public PreserveCircleAoe(Level level) {
        this(PRESERVE_CIRCLE_AOE.get(), level);
        this.setDeltaMovement(0, 0, 0);
        this.setNoGravity(true);
    }

    public void setSpellLevel(int level) {
        this.spellLevel = level;
    }

    public int getSpellLevel() {
        return spellLevel;
    }

        public void tick() {
        Vec3 pos = this.position();
        super.tick();
        setPos(pos);
    }

    @Override
    public void applyEffect(LivingEntity target) {
        MobEffectInstance slowedEffect = new MobEffectInstance(
                MobEffectRegistry.SLOWED,
                60,
                0,
                false,
                false,
                true
        );
        target.addEffect(slowedEffect);

        MobEffectInstance magicIngredientEffect = new MobEffectInstance(
                MAGICAL_INGREDIENT.get(),
                1200,
                Math.max(0, getSpellLevel() - 1),
                false,
                false,
                true
        );
        target.addEffect(magicIngredientEffect);
    }

    @Override
    public float getParticleCount() {
        return 0.15f;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.of(ORANGE_DUST);
    }
}
