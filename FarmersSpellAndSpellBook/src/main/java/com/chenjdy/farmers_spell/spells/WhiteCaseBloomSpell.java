package com.chenjdy.farmers_spell.spells;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.entity.ChaosSlashProjectile;
import com.chenjdy.farmers_spell.init.ModSchools;
import com.chenjdy.farmers_spell.init.ModSounds;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
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
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "white_case_bloom");

    private static final int SLASH_COUNT = 8;

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getSpellPower(spellLevel, caster), 2))
        );
    }

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
        return Optional.of(ModSounds.BIAN.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.PLAYER_ATTACK_SWEEP);
    }

    // 禁止从法术卷轴释放
    // @Override
    // public CastResult canBeCastedBy(int spellLevel, CastSource castSource, MagicData playerMagicData, Player player) {
    //     if (castSource == CastSource.SCROLL) {
    //         return new CastResult(CastResult.Type.FAILURE,
    //                 Component.translatable("message.farmers_spell.cast_error_scroll").withStyle(ChatFormatting.RED));
    //     }
    //     return super.canBeCastedBy(spellLevel, castSource, playerMagicData, player);
    // }

    @Override
    public void onServerPreCast(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        super.onServerPreCast(level, spellLevel, entity, playerMagicData);
        fireVolley(level, spellLevel, entity);
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        fireVolley(world, spellLevel, entity);
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private void fireVolley(Level level, int spellLevel, LivingEntity entity) {
        double heightOffset = entity.getBbHeight() * 0.5D - (entity.isShiftKeyDown() ? 0.4D : 0.0D);
        float baseYaw = entity.getYRot();
        for (int i = 0; i < SLASH_COUNT; i++) {
            float yaw = baseYaw + 360.0f / SLASH_COUNT * i;
            Vec3 direction = Vec3.directionFromRotation(0.0f, yaw);

            ChaosSlashProjectile chaosSlash = new ChaosSlashProjectile(level, entity);
            chaosSlash.setPos(entity.position().add(0.0D, heightOffset, 0.0D));
            chaosSlash.setYRot(yaw);
            chaosSlash.setXRot(0.0f);
            chaosSlash.setSlashType(0);
            chaosSlash.shoot(direction);
            chaosSlash.setDamage(getSpellPower(spellLevel, entity));
            level.addFreshEntity(chaosSlash);
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
