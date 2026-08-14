// package com.chenjdy.farmers_spell.spells;

// import com.chenjdy.farmers_spell.FARMERSSPELL;
// import com.chenjdy.farmers_spell.entity.PanEntity;
// import com.chenjdy.farmers_spell.init.ModSchools;
// import io.redspace.ironsspellbooks.api.config.DefaultConfig;
// import io.redspace.ironsspellbooks.api.magic.MagicData;
// import io.redspace.ironsspellbooks.api.spells.*;
// import io.redspace.ironsspellbooks.api.util.AnimationHolder;
// import io.redspace.ironsspellbooks.api.util.Utils;
// import net.minecraft.network.chat.Component;
// import net.minecraft.network.chat.MutableComponent;
// import net.minecraft.resources.ResourceLocation;
// import net.minecraft.sounds.SoundEvent;
// import net.minecraft.sounds.SoundEvents;
// import net.minecraft.sounds.SoundSource;
// import net.minecraft.world.entity.LivingEntity;
// import net.minecraft.world.level.Level;
// import net.minecraft.world.phys.Vec3;

// import java.util.List;
// import java.util.Optional;

// public class PanSpell extends AbstractSpell {

//     private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "pan");

//     @Override
//     public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
//         return List.of(
//                 Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getDamage(spellLevel), 1)),
//                 Component.translatable("ui.irons_spellbooks.effect_length", Utils.timeFromTicks(getEffectDuration(spellLevel), 1))
//         );
//     }

//     private final DefaultConfig defaultConfig = new DefaultConfig()
//             .setMinRarity(SpellRarity.COMMON)
//             .setSchoolResource(ModSchools.GLUTTONY_RESOURCE)
//             .setMaxLevel(5)
//             .setCooldownSeconds(5)
//             .build();

//     public PanSpell() {
//         this.manaCostPerLevel = 2;
//         this.baseSpellPower = 0;
//         this.spellPowerPerLevel = 0;
//         this.castTime = 0;
//         this.baseManaCost = 30;
//     }

//     @Override
//     public CastType getCastType() {
//         return CastType.INSTANT;
//     }

//     @Override
//     public DefaultConfig getDefaultConfig() {
//         return defaultConfig;
//     }

//     @Override
//     public ResourceLocation getSpellResource() {
//         return spellId;
//     }

//     @Override
//     public Optional<SoundEvent> getCastStartSound() {
//         return Optional.of(SoundEvents.IRON_TRAPDOOR_OPEN);
//     }

//     @Override
//     public Optional<SoundEvent> getCastFinishSound() {
//         return Optional.of(SoundEvents.ANVIL_PLACE);
//     }

//     @Override
//     public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
//         float damage = getDamage(spellLevel);
//         int effectDuration = getEffectDuration(spellLevel);

//         PanEntity panEntity = new PanEntity(world, entity, damage, effectDuration);

//         Vec3 eyePos = entity.getEyePosition();
//         Vec3 forward = entity.getLookAngle();

//         panEntity.setPos(eyePos.x, eyePos.y - 0.5, eyePos.z);
//         world.addFreshEntity(panEntity);

//         world.playSound(null, panEntity.blockPosition(),
//                 SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.PLAYERS, 1.0f, 1.0f);

//         super.onCast(world, spellLevel, entity, castSource, playerMagicData);
//     }

//     private float getDamage(int spellLevel) {
//         return 8.0f + spellLevel * 1.0f;
//     }

//     private int getEffectDuration(int spellLevel) {
//         return 600;
//     }

//     @Override
//     public AnimationHolder getCastStartAnimation() {
//         return SpellAnimations.SLASH_ANIMATION;
//     }
// }
