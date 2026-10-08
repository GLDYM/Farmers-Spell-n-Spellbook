package com.chenjdy.farmers_spell.event;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.init.*;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.datagen.DamageTypeTagGenerator;
import io.redspace.ironsspellbooks.entity.spells.icicle.IcicleProjectile;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import top.theillusivec4.curios.api.CuriosApi;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = FARMERSSPELL.MODID)
public class EffectsEventHandler {

    private static final String DRUID_HEAL_COOLDOWN = "druid_heal_cooldown";
    private static final String CLEANSE_MANA_COOLDOWN = "cleanse_mana_cooldown";
    private static final String GOLDEN_ARMOR_RES_FLAG = "golden_armor_resistance_active";
    private static final String DRUID_HEAL_REGEN_FLAG = "druid_heal_regeneration_active";

    private static final int CLEANSE_INTERCEPT_PENALTY_TICKS = 30 * 20;

    private static final UUID SEAL_OIL_COOLDOWN_UUID = UUID.fromString("9C0D1E2F-3A4B-4C0D-D5E6-F7A8B9C0D1E2");
    private static final UUID GOLDEN_ARMOR_ARMOR_UUID = UUID.fromString("7C2F9A1B-4D6E-4B03-9E5A-1F2D8C6B0A77");
    private static final UUID GOLDEN_ARMOR_TOUGHNESS_UUID = UUID.fromString("8d3a0b2c-5e7f-4c14-aaf6-b2e7d9c40f18");

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
            boolean immune = false;
            for (MobEffect immuneEffect : CLEANSE_IMMUNE_VANILLA_EFFECTS) {
                if (effect.getEffect().equals(immuneEffect)) {
                    immune = true;
                    break;
                }
            }
            MobEffect ironSlowed = getIronSlowedEffect();
            if (!immune && ironSlowed != null && effect.getEffect().equals(ironSlowed)) {
                immune = true;
            }
            if (immune) {
                shortenEffect(entity, ModEffects.CLEANSE.get(), CLEANSE_INTERCEPT_PENALTY_TICKS);
                event.setResult(Event.Result.DENY);
                return;
            }
        }
    }

    private static void shortenEffect(LivingEntity entity, MobEffect effectType, int ticks) {
        MobEffectInstance current = entity.getEffect(effectType);
        if (current == null) return;
        int newDuration = current.getDuration() - ticks;
        if (newDuration <= 0) {
            entity.removeEffect(effectType);
        } else {
            entity.addEffect(new MobEffectInstance(effectType, newDuration, current.getAmplifier(),
                    current.isAmbient(), current.isVisible(), current.showIcon()));
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

        if (livingEntity.hasEffect(ModEffects.GOLDEN_ARMOR.get()) && livingEntity.isOnFire()) {
            livingEntity.clearFire();
        }

        syncGoldenArmor(livingEntity);
        syncRingBonusEffects(livingEntity);

        MobEffectInstance druidHeal = livingEntity.getEffect(ModEffects.DRUID_HEAL.get());
        if (druidHeal != null) {
            handleDruidHeal(livingEntity);
        }

        if (livingEntity.hasEffect(ModEffects.CLEANSE.get())) {
            handleCleanse(livingEntity);
        }

        syncSealOilCooldown(livingEntity);
    }

    private static void syncSealOilCooldown(LivingEntity livingEntity) {
        applyDesiredModifier(livingEntity, AttributeRegistry.COOLDOWN_REDUCTION.get(),
                SEAL_OIL_COOLDOWN_UUID, "Seal Oil Cooldown Penalty",
                livingEntity.hasEffect(ModEffects.SEAL_OIL.get()) ? -0.25D : 0.0D);
    }

    private static void syncGoldenArmor(LivingEntity livingEntity) {
        MobEffectInstance golden = livingEntity.getEffect(ModEffects.GOLDEN_ARMOR.get());
        if (golden == null) {
            applyDesiredModifier(livingEntity, Attributes.ARMOR, GOLDEN_ARMOR_ARMOR_UUID, "Golden Armor Armor", 0.0D);
            applyDesiredModifier(livingEntity, Attributes.ARMOR_TOUGHNESS, GOLDEN_ARMOR_TOUGHNESS_UUID, "Golden Armor Toughness", 0.0D);
            return;
        }
        int level = golden.getAmplifier() + 1;
        applyDesiredModifier(livingEntity, Attributes.ARMOR, GOLDEN_ARMOR_ARMOR_UUID, "Golden Armor Armor", 3.0D * level);
        applyDesiredModifier(livingEntity, Attributes.ARMOR_TOUGHNESS, GOLDEN_ARMOR_TOUGHNESS_UUID, "Golden Armor Toughness", 1.0D * level);
    }

    private static void syncRingBonusEffects(LivingEntity livingEntity) {
        if (!(livingEntity instanceof Player player)) return;

        MobEffectInstance golden = player.getEffect(ModEffects.GOLDEN_ARMOR.get());
        boolean goldenRingActive = golden != null && hasCurio(player, ItemRegistry.FIREWARD_RING.get());
        syncLinkedEffect(player, goldenRingActive, golden, MobEffects.DAMAGE_RESISTANCE, GOLDEN_ARMOR_RES_FLAG);

        MobEffectInstance druid = player.getEffect(ModEffects.DRUID_HEAL.get());
        boolean druidRingActive = druid != null && hasCurio(player, ItemRegistry.POISONWARD_RING.get());
        syncLinkedEffect(player, druidRingActive, druid, MobEffects.REGENERATION, DRUID_HEAL_REGEN_FLAG);
    }

    private static void syncLinkedEffect(Player player, boolean active, MobEffectInstance source, MobEffect linkedType, String flagKey) {
        boolean ours = player.getPersistentData().getBoolean(flagKey);
        MobEffectInstance existing = player.getEffect(linkedType);

        if (active && source != null) {
            if (existing == null) {
                player.addEffect(new MobEffectInstance(linkedType, source.getDuration(), 0,
                        source.isAmbient(), true, true));
                player.getPersistentData().putBoolean(flagKey, true);
            } else if (ours && Math.abs(existing.getDuration() - source.getDuration()) > 2) {
                player.addEffect(new MobEffectInstance(linkedType, source.getDuration(), 0,
                        source.isAmbient(), true, true));
            }
        } else if (ours) {
            player.removeEffect(linkedType);
            player.getPersistentData().putBoolean(flagKey, false);
        }
    }

    private static boolean hasCurio(Player player, Item item) {
        return CuriosApi.getCuriosInventory(player).map(handler -> handler.isEquipped(item)).orElse(false);
    }

    private static void applyDesiredModifier(LivingEntity livingEntity, Attribute attribute, UUID uuid, String name, double desired) {
        AttributeInstance instance = livingEntity.getAttribute(attribute);
        if (instance == null) return;
        AttributeModifier modifier = instance.getModifier(uuid);
        if (desired == 0.0D) {
            if (modifier != null) instance.removeModifier(uuid);
            return;
        }
        if (modifier == null || modifier.getAmount() != desired) {
            if (modifier != null) instance.removeModifier(uuid);
            instance.addPermanentModifier(new AttributeModifier(uuid, name, desired, AttributeModifier.Operation.ADDITION));
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
            if (effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                harmfulEffects.add(effect);
            }
        }

        if (harmfulEffects.isEmpty()) return;

        MobEffectInstance lastHarmful = harmfulEffects.get(harmfulEffects.size() - 1);
        livingEntity.removeEffect(lastHarmful.getEffect());

        if (livingEntity.level() instanceof ServerLevel serverLevel) {
            BlockPos center = livingEntity.blockPosition();
            for (int x = -5; x <= 5; x++) {
                for (int y = -5; y <= 5; y++) {
                    for (int z = -5; z <= 5; z++) {
                        if (Math.abs(x) + Math.abs(y) + Math.abs(z) <= 5) {
                            BoneMealItem.applyBonemeal(
                                    new ItemStack(Items.BONE_MEAL),
                                    serverLevel,
                                    center.offset(x, y, z),
                                    player
                            );
                        }
                    }
                }
            }
        }

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
                if (player instanceof ServerPlayer serverPlayer) {
                    PacketDistributor.sendToPlayer(serverPlayer, new SyncManaPacket(magicData));
                }

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
    public static void onLivingAttack(LivingAttackEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;

        MobEffectInstance oil = entity.getEffect(ModEffects.HOLY_SPIRIT_OIL.get());
        if (oil == null) return;

        DamageSource source = event.getSource();
        if (source.is(DamageTypeTags.IS_FALL)
                || source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)
                || source.is(DamageTypeTagGenerator.BYPASS_EVASION)) {
            return;
        }

        event.setCanceled(true);

        int amplifier = oil.getAmplifier();
        int remainingDuration = oil.getDuration();
        entity.removeEffect(ModEffects.HOLY_SPIRIT_OIL.get());
        if (amplifier > 0) {
            entity.addEffect(new MobEffectInstance(ModEffects.HOLY_SPIRIT_OIL.get(),
                    remainingDuration, amplifier - 1, oil.isAmbient(), oil.isVisible(), oil.showIcon()));
        }

        if (entity.level() instanceof ServerLevel serverLevel) {
            double x = entity.getX();
            double y = entity.getY() + entity.getBbHeight() / 2.0;
            double z = entity.getZ();
            org.joml.Vector3f goldColor = new org.joml.Vector3f(1.0F, 0.84F, 0.0F);
            DustParticleOptions goldDust =
                    new DustParticleOptions(goldColor, 1.5F);
            for (int i = 0; i < 16; i++) {
                double ox = (entity.getRandom().nextDouble() - 0.5) * 0.8;
                double oy = (entity.getRandom().nextDouble() - 0.5) * 0.8;
                double oz = (entity.getRandom().nextDouble() - 0.5) * 0.8;
                serverLevel.sendParticles(goldDust, x + ox, y + oy, z + oz, 1, 0, 0, 0, 0.0);
            }
            entity.playSound(SoundEvents.AMETHYST_BLOCK_CHIME, 1.0F, 1.5F);
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
            int level = sealOil.getAmplifier() + 1;
            float healAmount = entity.getMaxHealth() * (0.02f * level) + 4.0f;
            entity.heal(healAmount);
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
