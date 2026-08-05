package com.chenjdy.farmers_spell.spells;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.entity.BadAppleEntity;
import com.chenjdy.farmers_spell.init.ModSchools;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.RaycastBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class BadAppleSpell extends AbstractSpell {
    
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "bad_apple");
    
    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        float health = 50.0f + 10.0f * spellLevel;
        int tauntRange = 8 + 2 * (spellLevel - 1);
        return List.of(
            Component.translatable("ui.irons_spellbooks.radius", String.valueOf(tauntRange)),
            Component.translatable("ui.irons_spellbooks.duration", "5.19"),
            Component.translatable("ui.farmers_spell.health", String.valueOf((int) health))
        );
    }
    
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(ModSchools.GLUTTONY_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(30)
            .build();
    
    public BadAppleSpell() {
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 0;
        this.spellPowerPerLevel = 0;
        this.castTime = 100;
        this.baseManaCost = 50;
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
        return Optional.empty();
    }
    
    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }
    
    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        Vec3 start = entity.getEyePosition();
        Vec3 end = entity.getLookAngle().normalize().scale(30).add(start);
        HitResult hitResult = RaycastBuilder.begin(level, entity).start(start).end(end).checkForBlocks(true).build();
        Vec3 targetPos = hitResult.getLocation();
        
        if (!level.isClientSide) {
            BadAppleEntity badApple = new BadAppleEntity(level, targetPos, spellLevel);
            badApple.setCustomName(Component.literal("Bad Apple"));
            level.addFreshEntity(badApple);
        }
        
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
    
    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_RAISED_HAND;
    }
}
