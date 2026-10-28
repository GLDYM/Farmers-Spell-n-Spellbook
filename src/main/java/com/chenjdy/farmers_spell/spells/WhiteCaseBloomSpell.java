package com.chenjdy.farmers_spell.spells;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.entity.ChaosSlashProjectile;
import com.chenjdy.farmers_spell.init.ModSchools;
import com.chenjdy.farmers_spell.init.ModSounds;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class WhiteCaseBloomSpell extends AbstractSpell {
    private static final int SLASH_COUNT = 8;
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(
            FarmersSpell.MODID, "white_case_bloom");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(ModSchools.GLUTTONY_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(5)
            .build();

    public WhiteCaseBloomSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 1;
        this.castTime = 10;
        this.baseManaCost = 100;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable(
                "ui.irons_spellbooks.damage",
                Utils.stringTruncation(getSpellPower(spellLevel, caster), 2)));
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
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(ModSounds.BAIAN.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.PLAYER_ATTACK_SWEEP);
    }

    @Override
    public void onServerPreCast(Level level, int spellLevel, LivingEntity entity,
                                @Nullable MagicData playerMagicData) {
        super.onServerPreCast(level, spellLevel, entity, playerMagicData);
        fireVolley(level, spellLevel, entity);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource,
                       MagicData playerMagicData) {
        fireVolley(level, spellLevel, entity);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private void fireVolley(Level level, int spellLevel, LivingEntity entity) {
        double heightOffset = entity.getBbHeight() * 0.5D - (entity.isShiftKeyDown() ? 0.4D : 0.0D);
        float baseYaw = entity.getYRot();
        for (int i = 0; i < SLASH_COUNT; i++) {
            float yaw = baseYaw + 360.0F / SLASH_COUNT * i;
            Vec3 direction = Vec3.directionFromRotation(0.0F, yaw);

            ChaosSlashProjectile slash = new ChaosSlashProjectile(level, entity);
            slash.setPos(entity.position().add(0.0D, heightOffset, 0.0D));
            slash.setYRot(yaw);
            slash.setXRot(0.0F);
            slash.setSlashType(0);
            slash.shoot(direction);
            slash.setDamage(getSpellPower(spellLevel, entity));
            level.addFreshEntity(slash);
        }
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.ANIMATION_LONG_CAST;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.ANIMATION_LONG_CAST_FINISH;
    }
}
