package com.chenjdy.farmers_spell.entity;


import com.chenjdy.farmers_spell.client.animation.ReversibleAnimationController;
import com.chenjdy.farmers_spell.init.ModTriggers;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.network.PacketDistributor;
import net.minecraft.core.registries.Registries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class FoodgeistEntity extends PathfinderMob implements GeoEntity {

    private static final long HEAL_INTERVAL_TICKS = 20L;
    private static final float HEAL_AMOUNT = 1.0F;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final TagKey<Item> FOODGEIST_FOOD = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("farmers_spell", "foodgeist_food"));

    private static final RawAnimation STAY1 = RawAnimation.begin().thenLoop("stay1");
    private static final RawAnimation STAY2 = RawAnimation.begin().thenLoop("stay");
    private static final RawAnimation MOVE = RawAnimation.begin().thenLoop("move");
    private static final RawAnimation WANT_START = RawAnimation.begin().thenPlayAndHold("wantstart");
    private static final RawAnimation WANT = RawAnimation.begin().thenLoop("want");
    private static final RawAnimation GIVING = RawAnimation.begin().thenPlayAndHold("giving");
    private static final RawAnimation SIT_DOWN_HOLD = RawAnimation.begin().thenPlayAndHold("sit_down");
    private static final RawAnimation SIT_LOOP = RawAnimation.begin().thenLoop("sit");
    private static final RawAnimation SIT_DOWN = RawAnimation.begin().thenPlay("sit_down");

    private static final int WANT_START_TICKS = 10;
    private static final int GIVING_TICKS = 20;
    private static final double FOOD_DETECT_RANGE = 8.0D;
    private static final double SIT_DOWN_SECONDS = 1.875D;
    private static final int STAND_UP_TICKS = 38;
    private static final int SIT_DOWN_TICKS = 36;

    public static final int ANIM_NORMAL = 0;
    public static final int ANIM_WANT_START = 1;
    public static final int ANIM_WANT = 2;
    public static final int ANIM_GIVING = 3;
    public static final int ANIM_SIT = 4;
    public static final int ANIM_STAND_UP = 5;
    public static final int ANIM_SIT_IDLE = 6;

    private static final ResourceKey<LootTable> FOODGEIST_GIFT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath("farmers_spell", "gameplay/foodgeist_gift"));

    private static final EntityDataAccessor<Boolean> DATA_SATISFIED = SynchedEntityData.defineId(FoodgeistEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DATA_WAITING = SynchedEntityData.defineId(FoodgeistEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DATA_SPAWN_ATTEMPT = SynchedEntityData.defineId(FoodgeistEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Long> DATA_SPAWN_TIME = SynchedEntityData.defineId(FoodgeistEntity.class, EntityDataSerializers.LONG);

    private static final EntityDataAccessor<Boolean> DATA_GIFTED = SynchedEntityData.defineId(FoodgeistEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Long> DATA_GIFT_TIME = SynchedEntityData.defineId(FoodgeistEntity.class, EntityDataSerializers.LONG);

    private static final EntityDataAccessor<Long> DATA_BLESSING_COOLDOWN = SynchedEntityData.defineId(FoodgeistEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Integer> DATA_ANIM_STATE = SynchedEntityData.defineId(FoodgeistEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_SITTING = SynchedEntityData.defineId(FoodgeistEntity.class, EntityDataSerializers.BOOLEAN);

    private ReversibleAnimationController<FoodgeistEntity> animationController;
    private long standUpUntil;
    private long sitDownUntil;
    private long wantStartUntil;
    private long givingUntil;
    private int idleAnimVariant;
    private long nextIdleSwitchTick = -1L;

    // private int spawnAttemptCount = 0;

    // private int currentSpawnChance = 10;

    // private long nextSpawnCheckTime = 0;

    // private ItemEntity targetFoodItem = null;

    public FoodgeistEntity(EntityType<? extends FoodgeistEntity> entityType, Level level) {
        super(entityType, level);
        this.setCanPickUpLoot(false);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.35D).add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    @Override
    public void actuallyHurt(DamageSource source, float amount) {
        super.actuallyHurt(source, amount);
        if (!this.level().isClientSide && this.isSitting()) {
            this.setSitting(false);
            this.setAnimState(ANIM_NORMAL);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SATISFIED, false);
        builder.define(DATA_WAITING, false);
        builder.define(DATA_SPAWN_ATTEMPT, 0);
        builder.define(DATA_SPAWN_TIME, 0L);
        builder.define(DATA_GIFTED, false);
        builder.define(DATA_GIFT_TIME, 0L);
        builder.define(DATA_BLESSING_COOLDOWN, 0L);
        builder.define(DATA_ANIM_STATE, ANIM_NORMAL);
        builder.define(DATA_SITTING, false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FoodgeistSitGoal(this));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        // 统一速度
        this.goalSelector.addGoal(1, new FoodgeistPickupItemGoal(this, 0.8D));
        // 统一速度，1格停止
        this.goalSelector.addGoal(2, new FoodgeistFollowPlayerGoal(this, 0.8D, 1.5F));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.5D, 20));
    }

    public static boolean hasModFoodItem(Player player) {
        return player.getMainHandItem().is(FOODGEIST_FOOD) || player.getOffhandItem().is(FOODGEIST_FOOD);
    }

    public static ItemStack getModFoodItem(Player player) {
        if (player.getMainHandItem().is(FOODGEIST_FOOD)) {
            return player.getMainHandItem();
        }
        if (player.getOffhandItem().is(FOODGEIST_FOOD)) {
            return player.getOffhandItem();
        }
        return ItemStack.EMPTY;
    }

    public static boolean isModFoodItem(ItemStack stack) {
        return stack.is(FOODGEIST_FOOD);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            long currentTime = this.level().getGameTime();
            if (currentTime % HEAL_INTERVAL_TICKS == 0) {
                this.heal(HEAL_AMOUNT);
            }
            if (this.isGifted()) {
                long giftTime = this.entityData.get(DATA_GIFT_TIME);
                if (currentTime % 5 == 0) {
                    this.spawnGreenParticles();
                }
                if (currentTime - giftTime > 600L) {
                    this.discard();
                }
            }
            long blessingCooldown = this.entityData.get(DATA_BLESSING_COOLDOWN);
            if (blessingCooldown > 0) {
                if (currentTime >= blessingCooldown) {
                    this.entityData.set(DATA_BLESSING_COOLDOWN, 0L);
                }
            }
            if (this.getAnimState() == ANIM_WANT_START && currentTime >= this.wantStartUntil) {
                this.setAnimState(ANIM_WANT);
            }
            if (this.getAnimState() == ANIM_GIVING && currentTime >= this.givingUntil) {
                this.setAnimState(this.isSitting() ? ANIM_SIT_IDLE : ANIM_NORMAL);
                this.giveGiftsToPlayer();
            }
            if (this.getAnimState() == ANIM_SIT && currentTime >= this.sitDownUntil) {
                this.setAnimState(ANIM_SIT_IDLE);
            }
            if (this.getAnimState() == ANIM_STAND_UP && currentTime >= this.standUpUntil) {
                this.setAnimState(ANIM_NORMAL);
            }
        } else {
            if (this.isGifted()) {
                if (this.level().getGameTime() % 5 == 0) {
                    this.spawnGreenParticles();
                }
            }
        }
    }

    private void spawnGreenParticles() {
        double x = this.getX() + (this.random.nextDouble() - 0.5) * 0.5;
        double y = this.getY() + this.getBbHeight() * 0.5 + (this.random.nextDouble() - 0.5) * 0.5;
        double z = this.getZ() + (this.random.nextDouble() - 0.5) * 0.5;
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, x, y, z, 1, 0, 0.05, 0, 0);
        } else {
            this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0, 0.05, 0);
        }
    }

    private void spawnSoulParticles() {
        double x = this.getX() + (this.random.nextDouble() - 0.5) * 1.0;
        double y = this.getY() + this.getBbHeight() * 0.5;
        double z = this.getZ() + (this.random.nextDouble() - 0.5) * 1.0;
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SOUL, x, y, z, 1, 0, 0.05, 0, 0);
        }
    }

    private void spawnTamingParticles(boolean success) {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        ParticleOptions particle = success ? ParticleTypes.HEART : ParticleTypes.SMOKE;
        int count = success ? 7 : 6;
        for (int i = 0; i < count; i++) {
            serverLevel.sendParticles(
                    particle,
                    this.getRandomX(0.8D),
                    this.getRandomY() + 0.5D,
                    this.getRandomZ(0.8D),
                    1,
                    this.random.nextGaussian() * 0.02D,
                    this.random.nextGaussian() * 0.02D,
                    this.random.nextGaussian() * 0.02D,
                    0.02D);
        }
    }

    public boolean tryPickupFoodItem(ItemEntity itemEntity) {
        if (!isModFoodItem(itemEntity.getItem()))
            return false;
        if (this.isGifted())
            return false;
        itemEntity.discard();
        if (this.random.nextFloat() < 0.33F) {
            this.startGiving();
        } else {
            this.entityData.set(DATA_SPAWN_ATTEMPT, this.entityData.get(DATA_SPAWN_ATTEMPT) + 1);
            if (this.entityData.get(DATA_SPAWN_ATTEMPT) >= 5) {
                this.startGiving();
            } else {
                this.setAnimState(this.isSitting() ? ANIM_SIT_IDLE : ANIM_NORMAL);
                this.spawnTamingParticles(false);
            }
        }
        return true;
    }

    private void startGiving() {
        this.setSatisfied(true);
        this.setGifted(true);
        this.entityData.set(DATA_GIFT_TIME, this.level().getGameTime());
        this.givingUntil = this.level().getGameTime() + GIVING_TICKS;
        this.setAnimState(ANIM_GIVING);
        this.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
        this.spawnTamingParticles(true);
    }

    private void giveGiftsToPlayer() {
        Player nearestPlayer = this.level().getNearestPlayer(this, 10.0D);
        if (nearestPlayer == null)
            return;
        // Receiving the reward starts the blessing cooldown immediately.
        this.entityData.set(DATA_BLESSING_COOLDOWN, this.level().getGameTime() + 300L);
        if (this.level() instanceof ServerLevel serverLevel) {
            LootParams lootParams = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.ORIGIN, this.position())
                    .withParameter(LootContextParams.THIS_ENTITY, this)
                    .withLuck(nearestPlayer.getLuck())
                    .create(LootContextParamSets.GIFT);
            List<ItemStack> gifts = serverLevel.getServer().reloadableRegistries()
                    .getLootTable(FOODGEIST_GIFT_TABLE)
                    .getRandomItems(lootParams);

            boolean rareGift = false;
            for (ItemStack gift : gifts) {
                rareGift |= gift.is(Items.NETHERITE_SCRAP);
                this.spawnItemAtPlayer(nearestPlayer, gift);
            }
            if (rareGift) {
                for (int i = 0; i < 20; i++) {
                    this.spawnSoulParticles();
                }
            }
        }

        if (nearestPlayer instanceof ServerPlayer serverPlayer) {
            CompoundTag persistentData = serverPlayer.getPersistentData();
            int satisfiedCount = persistentData.getInt("foodgeist_satisfied_count") + 1;
            persistentData.putInt("foodgeist_satisfied_count", satisfiedCount);
            if (satisfiedCount >= 10) {
                ModTriggers.FOODGEIST_SATISFIED_TRIGGER.get().trigger(serverPlayer);
            }
        }
    }

    private void spawnItemAtPlayer(Player player, ItemStack stack) {
        if (this.level().isClientSide)
            return;
        ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY() + 0.5D, this.getZ(), stack);
        itemEntity.setDefaultPickUpDelay();
        this.level().addFreshEntity(itemEntity);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack heldItem = player.getItemInHand(interactionHand);
        if (this.isGifted()) {
            long blessingCooldown = this.entityData.get(DATA_BLESSING_COOLDOWN);
            long currentTime = this.level().getGameTime();
            if (blessingCooldown > 0) {
                long remainingSeconds = (blessingCooldown - currentTime) / 20;
                player.displayClientMessage(Component.translatable("message.farmers_spell.foodgeist_recovering", remainingSeconds), true);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
            long lastHurtTime = player.getLastHurtByMobTimestamp();
            boolean notInCombat = (currentTime - lastHurtTime > 100L);
            if (notInCombat) {
                // removeEffect mutates the active-effects map, so do not remove while iterating it
                List<MobEffectInstance> harmfulEffects = player.getActiveEffects().stream()
                        .filter(effect -> !effect.getEffect().value().isBeneficial())
                        .toList();
                for (MobEffectInstance effect : harmfulEffects) {
                    player.removeEffect(effect.getEffect());
                }
                player.setHealth(player.getMaxHealth());
                if (player instanceof ServerPlayer serverPlayer) {
                    MagicData magicData = MagicData.getPlayerMagicData(serverPlayer);
                    float maxMana = (float) serverPlayer.getAttributeValue(AttributeRegistry.MAX_MANA);
                    magicData.setMana(maxMana);
                    PacketDistributor.sendToPlayer(serverPlayer, new SyncManaPacket(magicData));
                }
                this.entityData.set(DATA_BLESSING_COOLDOWN, currentTime + 300L);
                player.displayClientMessage(Component.translatable("message.farmers_spell.foodgeist_blessing"), true);
                this.playSound(SoundEvents.PLAYER_LEVELUP, 1.0F, 1.0F);
            } else {
                player.displayClientMessage(Component.translatable("message.farmers_spell.foodgeist_in_combat"), true);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        if (player.isShiftKeyDown()) {
            if (!this.level().isClientSide) {
                if (this.isSitting()) {
                    this.setSitting(false);
                    this.standUpUntil = this.level().getGameTime() + STAND_UP_TICKS;
                    this.setAnimState(ANIM_STAND_UP);
                } else {
                    this.setSitting(true);
                    this.sitDownUntil = this.level().getGameTime() + SIT_DOWN_TICKS;
                    this.setAnimState(ANIM_SIT);
                }
                this.getNavigation().stop();
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        if (isModFoodItem(heldItem)) {
            if (!this.level().isClientSide) {
                heldItem.shrink(1);
                if (this.random.nextFloat() < 0.33F) {
                    this.startGiving();
                } else {
                    this.entityData.set(DATA_SPAWN_ATTEMPT, this.entityData.get(DATA_SPAWN_ATTEMPT) + 1);
                    if (this.entityData.get(DATA_SPAWN_ATTEMPT) >= 5) {
                        this.startGiving();
                    } else {
                        this.setAnimState(this.isSitting() ? ANIM_SIT_IDLE : ANIM_NORMAL);
                        this.spawnTamingParticles(false);
                    }
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else if (!heldItem.isEmpty()) {
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, interactionHand);
    }

    public boolean isSatisfied() {
        return this.entityData.get(DATA_SATISFIED);
    }

    public void setSatisfied(boolean satisfied) {
        this.entityData.set(DATA_SATISFIED, satisfied);
    }

    public boolean isWaiting() {
        return this.entityData.get(DATA_WAITING);
    }

    public void setWaiting(boolean waiting) {
        this.entityData.set(DATA_WAITING, waiting);
    }

    public boolean isGifted() {
        return this.entityData.get(DATA_GIFTED);
    }

    public void setGifted(boolean gifted) {
        this.entityData.set(DATA_GIFTED, gifted);
    }

    public int getAnimState() {
        return this.entityData.get(DATA_ANIM_STATE);
    }

    public void setAnimState(int state) {
        this.entityData.set(DATA_ANIM_STATE, state);
    }

    public boolean isSitting() {
        return this.entityData.get(DATA_SITTING);
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(DATA_SITTING, sitting);
    }

    public boolean hasFollowTarget() {
        if (this.level().isClientSide) {
            AABB checkBox = this.getBoundingBox().inflate(8.0F);
            List<Player> nearbyPlayers = this.level().getEntitiesOfClass(Player.class, checkBox);
            for (Player player : nearbyPlayers) {
                if (hasModFoodItem(player)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isWaving() {
        int state = this.getAnimState();
        return state == ANIM_WANT_START || state == ANIM_WANT;
    }

    @Nullable
    public ItemEntity findNearbyFoodItem(double range) {
        AABB checkBox = this.getBoundingBox().inflate(range);
        List<ItemEntity> items = this.level().getEntitiesOfClass(ItemEntity.class, checkBox);
        for (ItemEntity item : items) {
            if (isModFoodItem(item.getItem())) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Satisfied", this.isSatisfied());
        compound.putBoolean("Gifted", this.isGifted());
        compound.putLong("GiftTime", this.entityData.get(DATA_GIFT_TIME));
        compound.putLong("BlessingCooldown", this.entityData.get(DATA_BLESSING_COOLDOWN));
        if (this.getAnimState() == ANIM_GIVING) {
            compound.putLong("GivingUntil", this.givingUntil);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSatisfied(compound.getBoolean("Satisfied"));
        this.setGifted(compound.getBoolean("Gifted"));
        this.entityData.set(DATA_GIFT_TIME, compound.getLong("GiftTime"));
        this.entityData.set(DATA_BLESSING_COOLDOWN, compound.getLong("BlessingCooldown"));
        if (compound.contains("GivingUntil")) {
            this.givingUntil = compound.getLong("GivingUntil");
            this.setAnimState(ANIM_GIVING);
        }
    }

    private PlayState predicate(AnimationState<FoodgeistEntity> state) {
        switch (this.getAnimState()) {
            case ANIM_GIVING -> {
                this.playForward();
                state.getController().setAnimation(GIVING);
            }
            case ANIM_WANT -> {
                this.playForward();
                state.getController().setAnimation(WANT);
            }
            case ANIM_WANT_START -> {
                this.playForward();
                state.getController().setAnimation(WANT_START);
            }
            case ANIM_SIT -> {
                this.playForward();
                state.getController().setAnimation(SIT_DOWN_HOLD);
            }
            case ANIM_SIT_IDLE -> {
                this.playForward();
                state.getController().setAnimation(SIT_LOOP);
            }
            case ANIM_STAND_UP -> {
                if (this.animationController != null) {
                    this.animationController.setReversed(SIT_DOWN_SECONDS);
                }
                state.getController().setAnimation(SIT_DOWN);
            }
            default -> {
                this.playForward();
                if (state.isMoving()) {
                    state.getController().setAnimation(MOVE);
                } else {
                    state.getController().setAnimation(this.pickIdleAnimation());
                }
            }
        }
        return PlayState.CONTINUE;
    }

    private void playForward() {
        if (this.animationController != null) {
            this.animationController.setForward();
        }
    }

    private RawAnimation pickIdleAnimation() {
        long now = this.level().getGameTime();
        if (now >= this.nextIdleSwitchTick) {
            this.idleAnimVariant = this.random.nextBoolean() ? 0 : 1;
            this.nextIdleSwitchTick = now + 100 + this.random.nextInt(100);
        }
        return this.idleAnimVariant == 0 ? STAY1 : STAY2;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        this.animationController = new ReversibleAnimationController<>(this, "controller", 5, this::predicate);
        controllerRegistrar.add(this.animationController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return level.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return this.isGifted() || distanceToClosestPlayer > 128.0D;
    }

    private static class FoodgeistSitGoal extends Goal {
        private final FoodgeistEntity foodgeist;

        FoodgeistSitGoal(FoodgeistEntity foodgeist) {
            this.foodgeist = foodgeist;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            return this.foodgeist.isSitting();
        }

        @Override
        public boolean canContinueToUse() {
            return this.foodgeist.isSitting();
        }

        @Override
        public void start() {
            this.foodgeist.getNavigation().stop();
        }

        @Override
        public void tick() {
            this.foodgeist.getNavigation().stop();
            this.foodgeist.setDeltaMovement(
                    0.0D, this.foodgeist.getDeltaMovement().y, 0.0D);
        }
    }

    private static class FoodgeistPickupItemGoal extends Goal {

        private final FoodgeistEntity foodgeist;

        private final double speedModifier;

        private ItemEntity targetItem;

            public FoodgeistPickupItemGoal(FoodgeistEntity entity, double speedModifier) {
            this.foodgeist = entity;
            this.speedModifier = speedModifier;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (foodgeist.isGifted())
                return false;
            targetItem = foodgeist.findNearbyFoodItem(FOOD_DETECT_RANGE);
            return targetItem != null;
        }

        @Override
        public boolean canContinueToUse() {
            if (foodgeist.isGifted())
                return false;
            if (targetItem == null || !targetItem.isAlive())
                return false;
            if (!isModFoodItem(targetItem.getItem()))
                return false;
            if (foodgeist.getAnimState() == ANIM_WANT_START)
                return true;
            return foodgeist.distanceTo(targetItem) > 1.5D;
        }

        @Override
        public void start() {
            foodgeist.setWaiting(false);
        }

        public void tick() {
            if (targetItem == null)
                return;
            double distance = foodgeist.distanceTo(targetItem);
            if (distance < 1.5D) {
                foodgeist.tryPickupFoodItem(targetItem);
                foodgeist.getNavigation().stop();
            } else {
                foodgeist.getNavigation().moveTo(targetItem, speedModifier);
                foodgeist.lookAt(targetItem, 10.0F, foodgeist.getMaxHeadXRot());
            }
        }

        @Override
        public void stop() {
            foodgeist.setWaiting(false);
            foodgeist.getNavigation().stop();
            this.targetItem = null;
        }
    }

    private static class FoodgeistFollowPlayerGoal extends Goal {

        private final FoodgeistEntity foodgeist;

        private Player targetPlayer;

        private final double speedModifier;

        private final float stopDistance;

            public FoodgeistFollowPlayerGoal(FoodgeistEntity entity, double speedModifier, float stopDistance) {
            this.foodgeist = entity;
            this.speedModifier = speedModifier;
            this.stopDistance = stopDistance;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (foodgeist.isGifted())
                return false;
            if (foodgeist.findNearbyFoodItem(FOOD_DETECT_RANGE) != null)
                return false;
            Player player = foodgeist.level().getNearestPlayer(foodgeist, FOOD_DETECT_RANGE);
            if (player == null)
                return false;
            if (hasModFoodItem(player)) {
                this.targetPlayer = player;
                return true;
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            if (foodgeist.isGifted())
                return false;
            if (targetPlayer == null || !targetPlayer.isAlive())
                return false;
            if (!hasModFoodItem(targetPlayer))
                return false;
            int state = foodgeist.getAnimState();
            if (state == ANIM_WANT_START || state == ANIM_WANT)
                return true;
            return foodgeist.distanceTo(targetPlayer) > stopDistance;
        }

        @Override
        public void start() {
            foodgeist.setWaiting(false);
        }

        public void tick() {
            if (targetPlayer == null)
                return;
            double distance = foodgeist.distanceTo(targetPlayer);
            if (distance <= stopDistance) {
                if (foodgeist.getAnimState() != ANIM_WANT) {
                    foodgeist.setAnimState(ANIM_WANT);
                }
                foodgeist.setWaiting(true);
                foodgeist.getNavigation().stop();
                foodgeist.lookAt(targetPlayer, 90.0F, 90.0F);
            } else {
                if (foodgeist.getAnimState() == ANIM_WANT) {
                    foodgeist.setAnimState(ANIM_NORMAL);
                }
                foodgeist.setWaiting(false);
                foodgeist.getNavigation().moveTo(targetPlayer, speedModifier);
                foodgeist.lookAt(targetPlayer, 10.0F, foodgeist.getMaxHeadXRot());
            }
        }

        @Override
        public void stop() {
            foodgeist.setWaiting(false);
            foodgeist.getNavigation().stop();
            int state = foodgeist.getAnimState();
            if (state == ANIM_WANT_START || state == ANIM_WANT) {
                foodgeist.setAnimState(ANIM_NORMAL);
            }
            this.targetPlayer = null;
        }
    }

}
