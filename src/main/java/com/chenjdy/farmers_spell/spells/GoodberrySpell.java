package com.chenjdy.farmers_spell.spells;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.init.ModItems;
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
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class GoodberrySpell extends AbstractSpell {

    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "goodberry");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(ModSchools.GLUTTONY_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(10)
            .build();

    public GoodberrySpell() {
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 0;
        this.spellPowerPerLevel = 0;
        this.castTime = 0;
        this.baseManaCost = 30;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
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
        return Optional.empty();
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (entity instanceof Player player) {
            boolean inCombat = player.getLastHurtByMob() != null
                    && player.tickCount - player.getLastHurtByMobTimestamp() < 100;

            if (inCombat) {
                player.heal(4.0f);
                player.addEffect(new MobEffectInstance(MobEffects.LUCK, 200, 0));
                player.playSound(SoundEvents.GENERIC_EAT, 1.0f, 1.0f);
            } else {
                ItemStack goodberry = new ItemStack(ModItems.GOODBERRY.get());
                if (!player.getInventory().add(goodberry)) {
                    player.drop(goodberry, false);
                }
                player.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 1.0f, 1.0f);
            }
        }

        spawnParticles(level, entity);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private void spawnParticles(Level level, LivingEntity entity) {
    double x = entity.getX();
    double y = entity.getY() + entity.getBbHeight() * 0.5;
    double z = entity.getZ();

    for (int i = 0; i < 12; i++) {
        double offsetX = (Utils.random.nextDouble() - 0.5) * 1.0;
        double offsetY = (Utils.random.nextDouble() - 0.5) * 0.5;
        double offsetZ = (Utils.random.nextDouble() - 0.5) * 1.0;

        MagicManager.spawnParticles(level, ParticleTypes.DRAGON_BREATH,
                x + offsetX, y + offsetY, z + offsetZ,
                1, 0, 0.05, 0, 0.1, false);
    }

    for (int i = 0; i < 8; i++) {
        double angle = Math.toRadians(360.0 / 8.0 * i);
        double radius = 0.8;
        MagicManager.spawnParticles(level, ParticleTypes.REVERSE_PORTAL,
                x + Math.cos(angle) * radius, y, z + Math.sin(angle) * radius,
                1, 0, 0.05, 0, 0.1, false);
    }
  }
}
