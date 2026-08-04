package com.chenjdy.farmers_spell.spells;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.init.ModEffects;
import com.chenjdy.farmers_spell.init.ModSchools;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class SealCoatSpell extends AbstractSpell {

    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "seal_coat");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(ModSchools.GLUTTONY_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(35)
            .build();

    public SealCoatSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 0;
        this.spellPowerPerLevel = 0;
        this.castTime = 20;
        this.baseManaCost = 30;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.HONEY_BLOCK_SLIDE);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        MobEffectInstance sealOilEffect = new MobEffectInstance(ModEffects.SEAL_OIL, 300, spellLevel - 1, false, true, true);
        entity.addEffect(sealOilEffect);

        spawnParticles(level, entity);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private void spawnParticles(Level level, LivingEntity entity) {
        double x = entity.getX();
        double y = entity.getY() + entity.getBbHeight() * 0.5;
        double z = entity.getZ();

        for (int i = 0; i < 20; i++) {
            double offsetX = (Utils.random.nextDouble() - 0.5) * 1.0;
            double offsetY = Utils.random.nextDouble() * 0.5;
            double offsetZ = (Utils.random.nextDouble() - 0.5) * 1.0;

            MagicManager.spawnParticles(level, ParticleTypes.LANDING_HONEY,
                    x + offsetX, y + offsetY, z + offsetZ,
                    1, 0, 0.1, 0, 0.05, false);
        }
    }
}
