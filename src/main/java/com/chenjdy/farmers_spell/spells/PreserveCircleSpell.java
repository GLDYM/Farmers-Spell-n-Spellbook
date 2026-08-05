package com.chenjdy.farmers_spell.spells;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.entity.PreserveCircleAoe;
import com.chenjdy.farmers_spell.init.ModSchools;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.spells.TargetAreaCastData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class PreserveCircleSpell extends AbstractSpell {

    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "preserve_circle");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation(getRadius(spellLevel, caster, 0), 1)),
                Component.translatable("ui.irons_spellbooks.duration", Utils.stringTruncation(getDuration(spellLevel) / 20.0, 1))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(ModSchools.GLUTTONY_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(40)
            .build();

    public PreserveCircleSpell() {
        this.manaCostPerLevel = 2;
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
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundEvents.HONEY_BLOCK_SLIDE);
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.BARREL_CLOSE);
    }

    @Override
    public int getCastTime(int spellLevel) {
        return Math.min(20 + spellLevel * 10, 160);
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return true;
    }

    @Override
    public void onServerPreCast(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        super.onServerPreCast(level, spellLevel, entity, playerMagicData);
        if (playerMagicData == null)
            return;

        float initialRadius = getRadius(spellLevel, entity, getCastTime(spellLevel));
        TargetedAreaEntity targetedAreaEntity = TargetedAreaEntity.createTargetAreaEntity(
                level, entity.position(), initialRadius, 0xFFD700, entity);
        playerMagicData.setAdditionalCastData(new TargetAreaCastData(entity.position(), targetedAreaEntity));
    }

    @Override
    public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        if (playerMagicData != null && playerMagicData.getAdditionalCastData() instanceof TargetAreaCastData castData) {
            TargetedAreaEntity previewEntity = castData.getCastingEntity();
            if (previewEntity != null) {
                previewEntity.moveTo(entity.position());
                float currentRadius = getRadius(spellLevel, entity, playerMagicData.getCastDurationRemaining());
                previewEntity.setRadius(currentRadius);
                playerMagicData.setAdditionalCastData(new TargetAreaCastData(previewEntity.position(), previewEntity));
            }
        }
        super.onServerCastTick(level, spellLevel, entity, playerMagicData);
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        Vec3 spawn = null;
        TargetedAreaEntity previewEntity = null;

        if (playerMagicData.getAdditionalCastData() instanceof TargetAreaCastData castData) {
            previewEntity = castData.getCastingEntity();
            spawn = previewEntity != null ? previewEntity.position() : castData.getCenter();
            if (previewEntity != null) {
                previewEntity.discard();
            }
        }

        if (spawn == null) {
            spawn = getTargetPosition(world, entity);
        }

        spawn = Utils.moveToRelativeGroundLevel(world, spawn, 6);

        float radius = getRadius(spellLevel, entity, playerMagicData.getCastDurationRemaining());

        int duration = getDuration(spellLevel);

        PreserveCircleAoe aoe = new PreserveCircleAoe(world);
        aoe.moveTo(spawn);
        aoe.setOwner(entity);
        aoe.setCircular();
        aoe.setRadius(radius);
        aoe.setDuration(duration);
        aoe.setSpellLevel(spellLevel);
        world.addFreshEntity(aoe);

        TargetedAreaEntity visualEntity = TargetedAreaEntity.createTargetAreaEntity(
                world, spawn, radius, 0xFFD700);
        visualEntity.setDuration(duration);
        visualEntity.setShouldFade(true);

        world.playSound(null, spawn.x, spawn.y, spawn.z, SoundEvents.BARREL_CLOSE, SoundSource.PLAYERS, 2.0f, 1.0f);

        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private Vec3 getTargetPosition(Level level, LivingEntity entity) {
        Vec3 startPos = entity.getEyePosition();
        Vec3 lookAngle = entity.getLookAngle();
        Vec3 endPos = startPos.add(lookAngle.multiply(32, 32, 32));
        return Utils.raycastForBlock(level, startPos, endPos, ClipContext.Fluid.NONE).getLocation();
    }

    private float getRadius(int spellLevel, LivingEntity caster, int castTimeRemaining) {
        float baseRadius = 1.0f + spellLevel * 0.5f;

        int castTimeElapsed = getCastTime(spellLevel) - castTimeRemaining;
        float additionalRadius = castTimeElapsed / 20.0f;

        return baseRadius + additionalRadius;
    }

    private int getDuration(int spellLevel) {
        return (int) ((spellLevel * 2.5 + 10) * 20);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.ANIMATION_CONTINUOUS_OVERHEAD;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return AnimationHolder.none();
    }

    @Override
    public boolean stopSoundOnCancel() {
        return true;
    }
}
