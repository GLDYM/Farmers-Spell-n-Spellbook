package com.chenjdy.farmers_spell.spells;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.entity.PanEntity;
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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class PanSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "pan");
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(ModSchools.GLUTTONY_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(5)
            .build();

    public PanSpell() {
        this.manaCostPerLevel = 2;
        this.baseSpellPower = 0;
        this.spellPowerPerLevel = 0;
        this.castTime = 0;
        this.baseManaCost = 30;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getDamage(spellLevel), 1)),
                Component.translatable("ui.irons_spellbooks.effect_length", Utils.timeFromTicks(getEffectDuration(spellLevel), 1)));
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundEvents.IRON_TRAPDOOR_OPEN);
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.ANVIL_PLACE);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity caster, CastSource castSource, MagicData playerMagicData) {
        if (!level.isClientSide) {
            PanEntity pan = new PanEntity(level, caster, getDamage(spellLevel), getEffectDuration(spellLevel));
            pan.setPos(caster.getEyePosition().add(caster.getLookAngle().scale(0.5)));
            level.addFreshEntity(pan);
            level.playSound(null, pan.blockPosition(), SoundEvents.IRON_TRAPDOOR_CLOSE,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        super.onCast(level, spellLevel, caster, castSource, playerMagicData);
    }

    private float getDamage(int spellLevel) {
        return 8.0F + spellLevel;
    }

    private int getEffectDuration(int spellLevel) {
        return 600;
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.SLASH_ANIMATION;
    }
}
