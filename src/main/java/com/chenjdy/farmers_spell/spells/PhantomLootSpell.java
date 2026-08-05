package com.chenjdy.farmers_spell.spells;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.init.ModSchools;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.level.Level;
import java.util.List;
import java.util.Optional;
import static net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.ENTITY;
import static net.minecraft.world.level.storage.loot.parameters.LootContextParams.*;
import net.minecraft.resources.ResourceKey;


public class PhantomLootSpell extends AbstractSpell {

    public static final TagKey<EntityType<?>> BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "phantom_loot_blacklist"));

    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "phantom_loot");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.RARE).setSchoolResource(ModSchools.GLUTTONY_RESOURCE).setMaxLevel(1).setCooldownSeconds(10).build();

    public PhantomLootSpell() {
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 0;
        this.spellPowerPerLevel = 0;
        this.castTime = 5;
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
        return Optional.of(SoundEvents.ILLUSIONER_PREPARE_MIRROR);
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, .35f, true, (livingEntity) -> livingEntity instanceof Mob mob && isValidTarget(mob));
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData targetData) {
            var targetEntity = targetData.getTarget((ServerLevel) level);
            if (targetEntity instanceof Mob mob) {
                grantLoot((ServerLevel) level, mob, entity);
                playSounds(level, entity, mob);
            }
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private boolean isValidTarget(Mob mob) {
        if (mob.getType().is(BLACKLIST))
            return false;
        if (mob.getMaxHealth() > 100.0f)
            return false;
        if (mob.getBbWidth() > 3.0f && mob.getBbHeight() > 3.0f)
            return false;
        return true;
    }

    private void grantLoot(ServerLevel serverLevel, Mob mob, LivingEntity caster) {
        ResourceKey<LootTable> lootTableKey = mob.getType().getDefaultLootTable();
        if (lootTableKey == null)
            return;
        var lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(lootTableKey);
        Player player = (Player) caster;
        var lootParams = new Builder(serverLevel).withParameter(ORIGIN, mob.position()).withParameter(THIS_ENTITY, mob).withParameter(DAMAGE_SOURCE, serverLevel.damageSources().playerAttack(player)).withOptionalParameter(ATTACKING_ENTITY, player).withOptionalParameter(LAST_DAMAGE_PLAYER, player).create(ENTITY);
        List<ItemStack> lootItems = lootTable.getRandomItems(lootParams);
        for (ItemStack item : lootItems) {
            if (!player.getInventory().add(item)) {
                ItemEntity itemEntity = new ItemEntity(serverLevel, player.getX(), player.getY(), player.getZ(), item);
                serverLevel.addFreshEntity(itemEntity);
            }
        }
    }

    private void playSounds(Level level, LivingEntity caster, Mob target) {
        level.playSound(null, caster.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, caster.getSoundSource(), 1.0f, 1.2f);
        level.playSound(null, target.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, caster.getSoundSource(), 0.5f, 1.0f);
    }
}
