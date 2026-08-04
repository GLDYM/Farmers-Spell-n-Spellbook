package com.chenjdy.farmers_spell.entity;

import com.chenjdy.farmers_spell.init.ModBlocks;
import com.chenjdy.farmers_spell.init.ModAttributes;
import com.chenjdy.farmers_spell.init.ModEntities;
import com.chenjdy.farmers_spell.init.ModItems;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.core.registries.BuiltInRegistries;
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

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final TagKey<Item> FOODGEIST_FOOD = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("farmers_spell", "foodgeist_food"));

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");

    private static final RawAnimation HAPPY = RawAnimation.begin().thenLoop("happy");

    private static final RawAnimation WAVE = RawAnimation.begin().thenPlay("wave");

    private static final EntityDataAccessor<Boolean> DATA_SATISFIED = SynchedEntityData.defineId(FoodgeistEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DATA_WAITING = SynchedEntityData.defineId(FoodgeistEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DATA_SPAWN_ATTEMPT = SynchedEntityData.defineId(FoodgeistEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Long> DATA_SPAWN_TIME = SynchedEntityData.defineId(FoodgeistEntity.class, EntityDataSerializers.LONG);

    private static final EntityDataAccessor<Boolean> DATA_GIFTED = SynchedEntityData.defineId(FoodgeistEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Long> DATA_GIFT_TIME = SynchedEntityData.defineId(FoodgeistEntity.class, EntityDataSerializers.LONG);

    private static final EntityDataAccessor<Integer> DATA_WAVE_TICKS = SynchedEntityData.defineId(FoodgeistEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Long> DATA_BLESSING_COOLDOWN = SynchedEntityData.defineId(FoodgeistEntity.class, EntityDataSerializers.LONG);

    private int spawnAttemptCount = 0;

    private int currentSpawnChance = 10;

    private long nextSpawnCheckTime = 0;

    private ItemEntity targetFoodItem = null;

    @SuppressWarnings("this-escape")
    public FoodgeistEntity(EntityType<? extends FoodgeistEntity> entityType, Level level) {
        super(entityType, level);
        this.setCanPickUpLoot(false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.35D).add(Attributes.FOLLOW_RANGE, 16.0D);
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
        builder.define(DATA_WAVE_TICKS, 0);
        builder.define(DATA_BLESSING_COOLDOWN, 0L);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        // 统一速度
        this.goalSelector.addGoal(1, new FoodgeistPickupItemGoal(this, 0.8D));
        // 统一速度，1格停止
        this.goalSelector.addGoal(2, new FoodgeistFollowPlayerGoal(this, 0.8D, 1.0F));
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
            if (this.isGifted()) {
                long giftTime = this.entityData.get(DATA_GIFT_TIME);
                long currentTime = this.level().getGameTime();
                if (currentTime % 5 == 0) {
                    this.spawnGreenParticles();
                }
                if (currentTime - giftTime > 600L) {
                    this.discard();
                }
            }
            long blessingCooldown = this.entityData.get(DATA_BLESSING_COOLDOWN);
            if (blessingCooldown > 0) {
                long currentTime = this.level().getGameTime();
                if (currentTime >= blessingCooldown) {
                    this.entityData.set(DATA_BLESSING_COOLDOWN, 0L);
                }
            }
            int waveTicks = this.entityData.get(DATA_WAVE_TICKS);
            if (waveTicks > 0) {
                this.entityData.set(DATA_WAVE_TICKS, waveTicks - 1);
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

    public boolean tryPickupFoodItem(ItemEntity itemEntity) {
        if (!isModFoodItem(itemEntity.getItem()))
            return false;
        if (this.isGifted())
            return false;
        if (this.random.nextFloat() < 0.33F) {
            itemEntity.discard();
            this.setSatisfied(true);
            this.setGifted(true);
            this.entityData.set(DATA_GIFT_TIME, this.level().getGameTime());
            this.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
            this.giveGiftsToPlayer();
        } else {
            itemEntity.discard();
            this.entityData.set(DATA_SPAWN_ATTEMPT, this.entityData.get(DATA_SPAWN_ATTEMPT) + 1);
            if (this.entityData.get(DATA_SPAWN_ATTEMPT) >= 5) {
                this.setSatisfied(true);
                this.setGifted(true);
                this.entityData.set(DATA_GIFT_TIME, this.level().getGameTime());
                this.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                this.giveGiftsToPlayer();
            } else {
                this.entityData.set(DATA_WAVE_TICKS, 20);
            }
        }
        return true;
    }

    private void giveGiftsToPlayer() {
        Player nearestPlayer = this.level().getNearestPlayer(this, 10.0D);
        if (nearestPlayer == null)
            return;
        this.spawnItemAtPlayer(nearestPlayer, new ItemStack(ModItems.FOODGEIST_SEASONING.get(), 2));
        if (this.random.nextFloat() < 0.5F) {
            this.spawnItemAtPlayer(nearestPlayer, new ItemStack(ModItems.FOODGEIST_CHEESE.get()));
        }
        int inkCount = 1 + this.random.nextInt(2);
        this.spawnItemAtPlayer(nearestPlayer, new ItemStack(ItemRegistry.INK_EPIC.get(), inkCount));
        this.spawnItemAtPlayer(nearestPlayer, new ItemStack(ItemRegistry.ARCANE_ESSENCE.get()));
        if (this.random.nextFloat() < 0.125F) {
            this.spawnItemAtPlayer(nearestPlayer, new ItemStack(Items.NETHERITE_SCRAP));
            for (int i = 0; i < 20; i++) {
                this.spawnSoulParticles();
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
                for (MobEffectInstance effect : player.getActiveEffects()) {
                    if (!effect.getEffect().value().isBeneficial()) {
                        player.removeEffect(effect.getEffect());
                    }
                }
                player.setHealth(player.getMaxHealth());
                if (player instanceof ServerPlayer serverPlayer) {
                    MagicData magicData = MagicData.getPlayerMagicData(serverPlayer);
                    float maxMana = (float) serverPlayer.getAttributeValue(AttributeRegistry.MAX_MANA);
                    magicData.setMana(maxMana);
                }
                this.entityData.set(DATA_BLESSING_COOLDOWN, currentTime + 300L);
                player.displayClientMessage(Component.translatable("message.farmers_spell.foodgeist_blessing"), true);
                this.playSound(SoundEvents.PLAYER_LEVELUP, 1.0F, 1.0F);
            } else {
                player.displayClientMessage(Component.translatable("message.farmers_spell.foodgeist_in_combat"), true);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        if (this.isModFoodItem(heldItem)) {
            if (!this.level().isClientSide) {
                if (this.random.nextFloat() < 0.33F) {
                    heldItem.shrink(1);
                    this.setSatisfied(true);
                    this.setGifted(true);
                    this.entityData.set(DATA_GIFT_TIME, this.level().getGameTime());
                    this.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                    this.giveGiftsToPlayer();
                } else {
                    heldItem.shrink(1);
                    this.entityData.set(DATA_SPAWN_ATTEMPT, this.entityData.get(DATA_SPAWN_ATTEMPT) + 1);
                    if (this.entityData.get(DATA_SPAWN_ATTEMPT) >= 5) {
                        this.setSatisfied(true);
                        this.setGifted(true);
                        this.entityData.set(DATA_GIFT_TIME, this.level().getGameTime());
                        this.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                        this.giveGiftsToPlayer();
                    } else {
                        this.entityData.set(DATA_WAVE_TICKS, 20);
                    }
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else if (!heldItem.isEmpty()) {
            if (!this.level().isClientSide) {
                this.entityData.set(DATA_WAVE_TICKS, 20);
            }
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
        return this.entityData.get(DATA_WAVE_TICKS) > 0;
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

    public static void checkSpawnCondition(ServerLevel level, Player player) {
        long gameTime = level.getGameTime();
        boolean hasMainBlock = false;
        boolean hasFoodBlock = false;
        boolean hasCabinet = false;
        int checkRange = 10;
        int yRange = 5;
        for (int x = -checkRange; x <= checkRange && !hasMainBlock; x++) {
            for (int y = -yRange; y <= yRange && !hasMainBlock; y++) {
                for (int z = -checkRange; z <= checkRange && !hasMainBlock; z++) {
                    BlockPos pos = player.blockPosition().offset(x, y, z);
                    BlockState state = level.getBlockState(pos);
                    if (state.is(ModBlocks.ALCHEMIST_POT.get()) || state.is(ModBlocks.CINDEROUS_STOVE.get())) {
                        hasMainBlock = true;
                    }
                    if (state.is(ModBlocks.WISEWOOD_CABINET.get())) {
                        hasCabinet = true;
                    }
                    if (state.is(ModBlocks.RED_VELVET_CAKE.get()) || state.is(ModBlocks.GOODBERRY_PIE.get()) || state.is(ModBlocks.EDEN_APPLE_TART.get()) || state.is(ModBlocks.GLUTTON_HOTCHPOTCH.get())) {
                        hasFoodBlock = true;
                    }
                }
            }
        }
        if (!hasMainBlock) {
            return;
        }
        int baseInterval = 1200;
        int actualInterval = baseInterval;
        if (hasCabinet)
            actualInterval -= 300;
        if (hasFoodBlock)
            actualInterval -= 300;
        CompoundTag persistentData = player.getPersistentData();
        long nextCheckTime = persistentData.getLong("foodgeist_next_check_time");
        int spawnChance = persistentData.getInt("foodgeist_spawn_chance");
        if (spawnChance == 0)
            spawnChance = 10;
        int spawnAttempts = persistentData.getInt("foodgeist_spawn_attempts");
        if (gameTime >= nextCheckTime) {
            if (spawnAttempts >= 5) {
                spawnFoodgeist(level, player);
                persistentData.putInt("foodgeist_spawn_attempts", 0);
                persistentData.putInt("foodgeist_spawn_chance", 10);
            } else {
                float roll = level.getRandom().nextFloat() * 100.0F;
                if (roll < spawnChance) {
                    spawnFoodgeist(level, player);
                    persistentData.putInt("foodgeist_spawn_attempts", 0);
                    persistentData.putInt("foodgeist_spawn_chance", 10);
                } else {
                    spawnAttempts++;
                    spawnChance += 10;
                    persistentData.putInt("foodgeist_spawn_attempts", spawnAttempts);
                    persistentData.putInt("foodgeist_spawn_chance", Math.min(spawnChance, 40));
                }
            }
            persistentData.putLong("foodgeist_next_check_time", gameTime + actualInterval);
        }
    }

    private static void spawnFoodgeist(ServerLevel level, Player player) {
        for (int attempt = 0; attempt < 20; attempt++) {
            int offsetX = level.getRandom().nextInt(11) - 5;
            int offsetZ = level.getRandom().nextInt(11) - 5;
            BlockPos spawnPos = player.blockPosition().offset(offsetX, 0, offsetZ);
            spawnPos = findGroundPosition(level, spawnPos);
            if (spawnPos != null && level.getBlockState(spawnPos).canBeReplaced()) {
                FoodgeistEntity foodgeist = new FoodgeistEntity(ModEntities.FOODGEIST.get(), level);
                foodgeist.setPos(spawnPos.getX() + 0.5, spawnPos.getY() + 0.5, spawnPos.getZ() + 0.5);
                level.addFreshEntity(foodgeist);
                level.sendParticles(ParticleTypes.HAPPY_VILLAGER, spawnPos.getX() + 0.5, spawnPos.getY() + 1, spawnPos.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.1);
                player.playSound(SoundEvents.VILLAGER_CELEBRATE, 1.0F, 1.0F);
                return;
            }
        }
    }

    @Nullable
    private static BlockPos findGroundPosition(ServerLevel level, BlockPos startPos) {
        BlockPos.MutableBlockPos mutable = startPos.mutable();
        for (int y = 0; y < 64; y++) {
            mutable.setY(startPos.getY() - y);
            if (mutable.getY() >= level.getMinBuildHeight()) {
                BlockState state = level.getBlockState(mutable);
                if (!state.isAir() && state.isSolid()) {
                    mutable.setY(mutable.getY() + 1);
                    if (mutable.getY() < level.getMaxBuildHeight() && level.getBlockState(mutable).canBeReplaced()) {
                        return mutable.immutable();
                    }
                }
            }
        }
        mutable = startPos.mutable();
        for (int y = 1; y < 64; y++) {
            mutable.setY(startPos.getY() + y);
            if (mutable.getY() < level.getMaxBuildHeight()) {
                BlockState state = level.getBlockState(mutable);
                if (!state.isAir() && state.isSolid()) {
                    mutable.setY(mutable.getY() + 1);
                    if (mutable.getY() < level.getMaxBuildHeight() && level.getBlockState(mutable).canBeReplaced()) {
                        return mutable.immutable();
                    }
                }
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
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSatisfied(compound.getBoolean("Satisfied"));
        this.setGifted(compound.getBoolean("Gifted"));
        this.entityData.set(DATA_GIFT_TIME, compound.getLong("GiftTime"));
        this.entityData.set(DATA_BLESSING_COOLDOWN, compound.getLong("BlessingCooldown"));
    }

    private PlayState predicate(AnimationState<FoodgeistEntity> state) {
        if (this.isWaving()) {
            state.getController().setAnimation(WAVE);
        } else if (this.isGifted()) {
            state.getController().setAnimation(HAPPY);
        } else if (state.isMoving()) {
            state.getController().setAnimation(WALK);
        } else {
            state.getController().setAnimation(IDLE);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 5, this::predicate));
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

    private static class FoodgeistPickupItemGoal extends Goal {

        private final FoodgeistEntity foodgeist;

        private final double speedModifier;

        private ItemEntity targetItem;

        @SuppressWarnings("this-escape")
        public FoodgeistPickupItemGoal(FoodgeistEntity entity, double speedModifier) {
            this.foodgeist = entity;
            this.speedModifier = speedModifier;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (foodgeist.isGifted())
                return false;
            targetItem = foodgeist.findNearbyFoodItem(2.0D);
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

        @SuppressWarnings("this-escape")
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
            if (foodgeist.findNearbyFoodItem(2.0D) != null)
                return false;
            Player player = foodgeist.level().getNearestPlayer(foodgeist, 8.0D);
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
                foodgeist.setWaiting(true);
                foodgeist.getNavigation().stop();
                foodgeist.lookAt(targetPlayer, 90.0F, 90.0F);
            } else {
                foodgeist.setWaiting(false);
                foodgeist.getNavigation().moveTo(targetPlayer, speedModifier);
                foodgeist.lookAt(targetPlayer, 10.0F, foodgeist.getMaxHeadXRot());
            }
        }

        @Override
        public void stop() {
            foodgeist.setWaiting(false);
            foodgeist.getNavigation().stop();
            this.targetPlayer = null;
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide)
            return;
        if (event.getEntity().tickCount % 20 != 0)
            return;
        Player player = event.getEntity();
        ServerLevel serverLevel = (ServerLevel) player.level();
        boolean hasNearbyFoodgeist = serverLevel.getEntitiesOfClass(FoodgeistEntity.class, player.getBoundingBox().inflate(16.0D)).size() > 0;
        if (hasNearbyFoodgeist) {
            return;
        }
        checkSpawnCondition(serverLevel, player);
    }
}
