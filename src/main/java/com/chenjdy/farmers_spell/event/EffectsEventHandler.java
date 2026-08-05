package com.chenjdy.farmers_spell.event;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.init.ModEffects;
import com.chenjdy.farmers_spell.init.ModSchools;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.icicle.IcicleProjectile;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = FARMERSSPELL.MODID)
public class EffectsEventHandler {

    private static final String DRUID_HEAL_COOLDOWN = "druid_heal_cooldown";
    private static final String SEAL_OIL_COOLDOWN = "seal_oil_cooldown";
    private static final String CLEANSE_MANA_COOLDOWN = "cleanse_mana_cooldown";

    private static final List<MobEffect> CLEANSE_IMMUNE_VANILLA_EFFECTS = List.of(
            MobEffects.MOVEMENT_SLOWDOWN,
            MobEffects.HUNGER,
            MobEffects.WEAKNESS,
            MobEffects.DIG_SLOWDOWN,
            MobEffects.DARKNESS,
            MobEffects.BLINDNESS
    );

    private static MobEffect getIronSlowedEffect() {
        try {
            return MobEffectRegistry.SLOWED.get();
        } catch (Exception e) {
            return null;
        }
    }

    @SubscribeEvent
    public static void onMobEffectApplicable(MobEffectEvent.Applicable event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;

        MobEffectInstance effect = event.getEffectInstance();
        if (effect == null) return;

        if (effect.getEffect().equals(MobEffects.POISON) && entity.hasEffect(ModEffects.DRUID_HEAL.get())) {
            event.setResult(Event.Result.DENY);
            return;
        }

        if (entity.hasEffect(ModEffects.CLEANSE.get())) {
            for (MobEffect immuneEffect : CLEANSE_IMMUNE_VANILLA_EFFECTS) {
                if (effect.getEffect().equals(immuneEffect)) {
                    event.setResult(Event.Result.DENY);
                    return;
                }
            }
            MobEffect ironSlowed = getIronSlowedEffect();
            if (ironSlowed != null && effect.getEffect().equals(ironSlowed)) {
                event.setResult(Event.Result.DENY);
                return;
            }
        }
    }

    @SubscribeEvent
    public static void onMobEffectAdded(MobEffectEvent.Added event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;

        MobEffectInstance effect = event.getEffectInstance();
        if (effect == null) return;

        if (effect.getEffect().equals(ModEffects.GOLDEN_ARMOR.get())) {
            if (entity.isOnFire()) {
                entity.clearFire();
                entity.setSecondsOnFire(0);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity.level().isClientSide) return;

        MobEffectInstance druidHeal = livingEntity.getEffect(ModEffects.DRUID_HEAL.get());
        if (druidHeal != null) {
            handleDruidHeal(livingEntity);
        }

        if (livingEntity.hasEffect(ModEffects.CLEANSE.get())) {
            handleCleanse(livingEntity);
        }

        if (livingEntity.hasEffect(ModEffects.SEAL_OIL.get())) {
            int sealOilCooldown = livingEntity.getPersistentData().getInt(SEAL_OIL_COOLDOWN);
            if (sealOilCooldown > 0) {
                livingEntity.getPersistentData().putInt(SEAL_OIL_COOLDOWN, sealOilCooldown - 1);
            }
        }

    }

    private static void handleDruidHeal(LivingEntity livingEntity) {
        int cooldown = livingEntity.getPersistentData().getInt(DRUID_HEAL_COOLDOWN);
        if (cooldown > 0) {
            livingEntity.getPersistentData().putInt(DRUID_HEAL_COOLDOWN, cooldown - 1);
            return;
        }

        if (!(livingEntity instanceof Player player)) return;
        if (player.getFoodData().getFoodLevel() < 2) return;

        List<MobEffectInstance> harmfulEffects = new ArrayList<>();
        for (MobEffectInstance effect : livingEntity.getActiveEffects()) {
            if (effect.getEffect().getCategory() == net.minecraft.world.effect.MobEffectCategory.HARMFUL) {
                harmfulEffects.add(effect);
            }
        }

        if (harmfulEffects.isEmpty()) return;

        MobEffectInstance lastHarmful = harmfulEffects.get(harmfulEffects.size() - 1);
        livingEntity.removeEffect(lastHarmful.getEffect());

        player.getFoodData().eat(-2, 0);

        livingEntity.getPersistentData().putInt(DRUID_HEAL_COOLDOWN, 60);

        if (livingEntity.level() instanceof ServerLevel serverLevel) {
            double x = livingEntity.getX();
            double y = livingEntity.getY() + livingEntity.getBbHeight() / 2;
            double z = livingEntity.getZ();
            for (int i = 0; i < 10; i++) {
                double offsetX = (Math.random() - 0.5) * 0.5;
                double offsetY = Math.random() * 0.5;
                double offsetZ = (Math.random() - 0.5) * 0.5;
                serverLevel.sendParticles(
                        ParticleTypes.HAPPY_VILLAGER,
                        x + offsetX, y + offsetY, z + offsetZ,
                        1, 0, 0.05, 0, 0.01
                );
            }
        }
    }

    private static void handleCleanse(LivingEntity livingEntity) {
        if (livingEntity instanceof Player player) {
            int manaCooldown = player.getPersistentData().getInt(CLEANSE_MANA_COOLDOWN);
            if (manaCooldown > 0) {
                player.getPersistentData().putInt(CLEANSE_MANA_COOLDOWN, manaCooldown - 1);
            } else {
                MagicData magicData = MagicData.getPlayerMagicData(player);
                float currentMana = magicData.getMana();
                float maxMana = (float) player.getAttributeValue(AttributeRegistry.MAX_MANA.get());
                float manaRegenAmount = maxMana * 0.15f;
                float newMana = Math.min(currentMana + manaRegenAmount, maxMana);
                magicData.setMana(newMana);

                player.getPersistentData().putInt(CLEANSE_MANA_COOLDOWN, 300);

                if (livingEntity.level() instanceof ServerLevel serverLevel) {
                    double x = livingEntity.getX();
                    double y = livingEntity.getY() + livingEntity.getBbHeight() / 2;
                    double z = livingEntity.getZ();
                    for (int i = 0; i < 8; i++) {
                        double offsetX = (Math.random() - 0.5) * 0.5;
                        double offsetY = Math.random() * 0.3;
                        double offsetZ = (Math.random() - 0.5) * 0.5;
                        serverLevel.sendParticles(
                                ParticleTypes.EFFECT,
                                x + offsetX, y + offsetY, z + offsetZ,
                                1, 0, 0.02, 0, 0.01
                        );
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;

        if (entity.hasEffect(ModEffects.FROST_SHIELD.get())) {
            if (event.getSource().is(DamageTypes.FREEZE)) {
                event.setAmount(0.0f);
                return;
            }
        }

        MobEffectInstance magicalIngredient = entity.getEffect(ModEffects.MAGICAL_INGREDIENT.get());
        if (magicalIngredient != null) {
            boolean isGluttonyMagic = event.getSource().is(DamageTypes.MAGIC) || event.getSource().is(ModSchools.GLUTTONY_MAGIC);
            if (isGluttonyMagic) {
                int amplifier = magicalIngredient.getAmplifier();
                int level = amplifier + 1;

                float bonusPercent = (5.0f + level) / 100.0f;
                float newDamage = event.getAmount() * (1.0f + bonusPercent);
                event.setAmount(newDamage);
            }
        }

        MobEffectInstance sealOil = entity.getEffect(ModEffects.SEAL_OIL.get());
        if (sealOil != null) {
            if (entity.getPersistentData().getInt(SEAL_OIL_COOLDOWN) <= 0) {
                int amplifier = sealOil.getAmplifier();
                int level = amplifier + 1;

                float healPercent = level * 0.01f;
                float healAmount = entity.getMaxHealth() * healPercent + 2.0f;

                entity.heal(healAmount);

                entity.getPersistentData().putInt(SEAL_OIL_COOLDOWN, 100);
            }
        }

        MobEffectInstance frostShield = entity.getEffect(ModEffects.FROST_SHIELD.get());
        if (frostShield != null) {
            int amplifier = frostShield.getAmplifier();
            int level = amplifier + 1;

            float originalDamage = event.getAmount();
            float reducedDamage = originalDamage - (0.5f * level);
            reducedDamage = Math.max(0.0f, reducedDamage);

            event.setAmount(reducedDamage);

            ServerLevel serverLevel = (ServerLevel) entity.level();
            double x = entity.getX();
            double y = entity.getY() + entity.getBbHeight() / 2;
            double z = entity.getZ();
            for (int i = 0; i < 15; i++) {
                double offsetX = (Math.random() - 0.5) * 0.5;
                double offsetY = Math.random() * 0.5;
                double offsetZ = (Math.random() - 0.5) * 0.5;
                serverLevel.sendParticles(
                        ParticleHelper.SNOWFLAKE,
                        x + offsetX, y + offsetY, z + offsetZ,
                        1, 0, 0.05, 0, 0.01
                );
                serverLevel.sendParticles(
                        ParticleTypes.SNOWFLAKE,
                        x + offsetX, y + offsetY, z + offsetZ,
                        1, 0, 0.05, 0, 0.01
                );
            }

            if (reducedDamage > 6.0f) {
                spawnIcicles(entity, reducedDamage);
            }
        }

    }

    private static void spawnIcicles(LivingEntity entity, float damage) {
        ServerLevel serverLevel = (ServerLevel) entity.level();
        Vec3 origin = entity.position().add(0, entity.getBbHeight() / 2, 0);

        int count = 8;
        int offset = 360 / count;
        for (int i = 0; i < count; i++) {
            Vec3 motion = new Vec3(0, 0, 0.55);
            motion = motion.yRot(offset * i * Mth.DEG_TO_RAD);

            IcicleProjectile icicle = new IcicleProjectile(serverLevel, entity);
            icicle.setDamage(damage / 2.0f);
            icicle.setDeltaMovement(motion);

            Vec3 spawn = origin.add(motion.multiply(1, 0, 1).normalize().scale(0.5f));
            var angle = Utils.rotationFromDirection(motion);

            icicle.moveTo(spawn.x, spawn.y - icicle.getBoundingBox().getYsize() / 2, spawn.z, angle.y, angle.x);
            serverLevel.addFreshEntity(icicle);
        }
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;

        MobEffectInstance magicalIngredient = entity.getEffect(ModEffects.MAGICAL_INGREDIENT.get());
        if (magicalIngredient != null) {
            int amplifier = magicalIngredient.getAmplifier();
            int lootingLevel = amplifier + 1;

            for (ItemEntity drop : event.getDrops()) {
                ItemStack stack = drop.getItem();
                int extraCount = 0;
                for (int i = 0; i < lootingLevel; i++) {
                    if (entity.getRandom().nextFloat() < 0.1f) {
                        extraCount++;
                    }
                }
                if (extraCount > 0) {
                    stack.grow(extraCount);
                }
            }
        }
    }
}
