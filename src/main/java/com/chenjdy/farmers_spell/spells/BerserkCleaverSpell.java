/*package com.chenjdy.farmers_spell.spells;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.entity.KinfeProjectile;
import com.chenjdy.farmers_spell.init.ModSchools;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.spells.TargetAreaCastData;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class BerserkCleaverSpell extends AbstractSpell {
    
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "berserk_cleaver");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getDamage(spellLevel, caster), 2)),
                Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation(getRadius(caster), 1))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(ModSchools.GLUTTONY_RESOURCE)
            .setMaxLevel(8)
            .setCooldownSeconds(16)
            .build();

    public BerserkCleaverSpell() {
        this.manaCostPerLevel = 1;
        this.baseSpellPower = 0;
        this.spellPowerPerLevel = 0;
        this.castTime = 160;
        this.baseManaCost = 5;
    }

    @Override
    public CastType getCastType() {
        return CastType.CONTINUOUS;
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
        return Optional.of(SoundEvents.CHAIN_PLACE);
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
    if (!(playerMagicData.getAdditionalCastData() instanceof TargetAreaCastData)) {
        Vec3 targetArea = Utils.moveToRelativeGroundLevel(world, Utils.raycastForEntity(world, entity, 40, true).getLocation(), 12);
        playerMagicData.setAdditionalCastData(new TargetAreaCastData(targetArea, null));
    }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        if (playerMagicData != null && (playerMagicData.getCastDurationRemaining() + 1) % 4 == 0)
            if (playerMagicData.getAdditionalCastData() instanceof TargetAreaCastData targetAreaCastData) {
                Vec3 center = targetAreaCastData.getCenter();
                float radius = getRadius(entity);
                for (int i = 0; i < 2; i++) {
                    Vec3 spawn = center.add(new Vec3(0, 0, entity.getRandom().nextFloat() * radius).yRot(entity.getRandom().nextInt(360)));
                    spawn = raiseWithCollision(spawn, 12, level);
                    shootCleaver(level, spellLevel, entity, spawn);
                    MagicManager.spawnParticles(level, ParticleHelper.FOG, spawn.x, spawn.y, spawn.z, 1, 1, 1, 1, 1, false);
                    MagicManager.spawnParticles(level, ParticleHelper.FOG, spawn.x, spawn.y, spawn.z, 1, 1, 1, 1, 1, true);
                }
            }
    }

    private Vec3 raiseWithCollision(Vec3 start, int blocks, Level level) {
        for (int i = 0; i < blocks; i++) {
            Vec3 raised = start.add(0, 1, 0);
            if (level.getBlockState(BlockPos.containing(raised)).isAir())
                start = raised;
            else
                break;
        }
        return start;
    }

    private float getDamage(int spellLevel, LivingEntity caster) {
        return 5.0f + spellLevel * 0.5f;
    }

    private float getRadius(LivingEntity caster) {
        return 6;
    }

    public void shootCleaver(Level world, int spellLevel, LivingEntity entity, Vec3 spawn) {
        KinfeProjectile cleaver = new KinfeProjectile(world, entity);
        cleaver.setPos(spawn.add(-1, 0, 0));
        cleaver.shoot(new Vec3(.15f, -.85f, 0), .075f);
        cleaver.setDamage(getDamage(spellLevel, entity));
        cleaver.setExplosionRadius(2f);
        world.addFreshEntity(cleaver);
        world.playSound(null, spawn.x, spawn.y, spawn.z, SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 3.0f, 0.7f + Utils.random.nextFloat() * .3f);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.ANIMATION_CONTINUOUS_OVERHEAD;
    }
}*/