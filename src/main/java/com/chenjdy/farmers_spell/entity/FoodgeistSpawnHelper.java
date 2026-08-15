package com.chenjdy.farmers_spell.entity;

import com.chenjdy.farmers_spell.init.ModBlocks;
import com.chenjdy.farmers_spell.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.Map;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.WeakHashMap;

/** Handles Foodgeist spawning for stationary cooking block entities. */
public final class FoodgeistSpawnHelper {
    private static final int BASE_INTERVAL = 1200;
    private static final int CHECK_RANGE = 10;
    private static final int Y_RANGE = 5;
    private static final Map<BlockEntity, SpawnState> STATES = new WeakHashMap<>();
    private static final List<ScanOffset> SCAN_OFFSETS = createScanOffsets();

    private FoodgeistSpawnHelper() {
    }

    private static List<ScanOffset> createScanOffsets() {
        List<ScanOffset> offsets = new ArrayList<>((CHECK_RANGE * 2 + 1) * (Y_RANGE * 2 + 1) * (CHECK_RANGE * 2 + 1));
        for (int x = -CHECK_RANGE; x <= CHECK_RANGE; x++) {
            for (int y = -Y_RANGE; y <= Y_RANGE; y++) {
                for (int z = -CHECK_RANGE; z <= CHECK_RANGE; z++) {
                    offsets.add(new ScanOffset(x, y, z));
                }
            }
        }
        offsets.sort(Comparator.comparingInt(ScanOffset::distance));
        return List.copyOf(offsets);
    }

    public static void tick(ServerLevel level, BlockPos blockPos, BlockEntity source) {
        long gameTime = level.getGameTime();
        SpawnState state = STATES.computeIfAbsent(source, ignored -> new SpawnState());
        if (gameTime < state.nextCheckTime) {
            return;
        }

        Player player = findNearbyPlayer(level, blockPos);
        if (player == null) {
            state.nextCheckTime = gameTime + 100;
            return;
        }

        boolean hasCabinet;
        boolean hasFoodBlock;
        if (state.hasValidCachedBlocks(level)) {
            hasCabinet = state.cabinetPosition != null;
            hasFoodBlock = state.foodPosition != null;
        } else {
            state.scan(level, blockPos);
            hasCabinet = state.cabinetPosition != null;
            hasFoodBlock = state.foodPosition != null;
        }

        int interval = BASE_INTERVAL - (hasCabinet ? 300 : 0) - (hasFoodBlock ? 300 : 0);
        if (gameTime >= state.nextCheckTime) {
            if (state.spawnAttempts >= 5 || level.getRandom().nextFloat() * 100.0F < state.spawnChance) {
                spawnFoodgeist(level, blockPos, player);
                state.spawnAttempts = 0;
                state.spawnChance = 10;
            } else {
                state.spawnAttempts++;
                state.spawnChance = Math.min(state.spawnChance + 10, 40);
            }
            state.nextCheckTime = gameTime + interval;
        }
    }

    private static Player findNearbyPlayer(ServerLevel level, BlockPos pos) {
        return level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(16.0D)).stream()
                .filter(Player::isAlive)
                .min((left, right) -> Double.compare(
                        left.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D),
                        right.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D)))
                .orElse(null);
    }

    private static void spawnFoodgeist(ServerLevel level, BlockPos sourcePos, Player player) {
        if (!level.getEntitiesOfClass(FoodgeistEntity.class, new AABB(sourcePos).inflate(16.0D)).isEmpty()) {
            return;
        }
        for (int attempt = 0; attempt < 20; attempt++) {
            int offsetX = level.getRandom().nextInt(11) - 5;
            int offsetZ = level.getRandom().nextInt(11) - 5;
            BlockPos spawnPos = findGroundPosition(level, sourcePos.offset(offsetX, 0, offsetZ));
            if (spawnPos != null && level.getBlockState(spawnPos).canBeReplaced()) {
                FoodgeistEntity foodgeist = new FoodgeistEntity(ModEntities.FOODGEIST.get(), level);
                foodgeist.setPos(spawnPos.getX() + 0.5D, spawnPos.getY() + 0.5D, spawnPos.getZ() + 0.5D);
                level.addFreshEntity(foodgeist);
                level.sendParticles(ParticleTypes.HAPPY_VILLAGER, spawnPos.getX() + 0.5D, spawnPos.getY() + 1.0D,
                        spawnPos.getZ() + 0.5D, 10, 0.5D, 0.5D, 0.5D, 0.1D);
                player.playSound(SoundEvents.VILLAGER_CELEBRATE, 1.0F, 1.0F);
                return;
            }
        }
    }

    private static BlockPos findGroundPosition(ServerLevel level, BlockPos startPos) {
        BlockPos.MutableBlockPos mutable = startPos.mutable();
        for (int y = 0; y < 64; y++) {
            mutable.setY(startPos.getY() - y);
            if (mutable.getY() >= level.getMinBuildHeight()) {
                BlockState state = level.getBlockState(mutable);
                if (!state.isAir() && state.isFaceSturdy(level, mutable, Direction.UP)) {
                    mutable.setY(mutable.getY() + 1);
                    if (mutable.getY() < level.getMaxBuildHeight() && level.getBlockState(mutable).canBeReplaced()) {
                        return mutable.immutable();
                    }
                }
            }
        }
        for (int y = 1; y < 64; y++) {
            mutable.setY(startPos.getY() + y);
            if (mutable.getY() < level.getMaxBuildHeight()) {
                BlockState state = level.getBlockState(mutable);
                if (!state.isAir() && state.isFaceSturdy(level, mutable, Direction.UP)) {
                    mutable.setY(mutable.getY() + 1);
                    if (mutable.getY() < level.getMaxBuildHeight() && level.getBlockState(mutable).canBeReplaced()) {
                        return mutable.immutable();
                    }
                }
            }
        }
        return null;
    }

    private static final class SpawnState {
        private long nextCheckTime;
        private int spawnChance = 10;
        private int spawnAttempts;
        private BlockPos cabinetPosition;
        private BlockPos foodPosition;
        private boolean cacheInitialized;

        private boolean hasValidCachedBlocks(ServerLevel level) {
            if (!cacheInitialized) {
                return false;
            }
            if (cabinetPosition != null
                    && !level.getBlockState(cabinetPosition).is(ModBlocks.WISEWOOD_CABINET.get())) {
                return false;
            }
            if (foodPosition != null && !isFoodBlock(level.getBlockState(foodPosition))) {
                return false;
            }
            return true;
        }

        private void scan(ServerLevel level, BlockPos blockPos) {
            cabinetPosition = null;
            foodPosition = null;
            BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
            for (ScanOffset offset : SCAN_OFFSETS) {
                cursor.set(blockPos.getX() + offset.x, blockPos.getY() + offset.y, blockPos.getZ() + offset.z);
                BlockState blockState = level.getBlockState(cursor);
                if (cabinetPosition == null && blockState.is(ModBlocks.WISEWOOD_CABINET.get())) {
                    cabinetPosition = cursor.immutable();
                }
                if (foodPosition == null && isFoodBlock(blockState)) {
                    foodPosition = cursor.immutable();
                }
                if (cabinetPosition != null && foodPosition != null) {
                    return;
                }
            }
            cacheInitialized = true;
        }

        private static boolean isFoodBlock(BlockState state) {
            return state.is(ModBlocks.RED_VELVET_CAKE.get())
                    || state.is(ModBlocks.GOODBERRY_PIE.get())
                    || state.is(ModBlocks.EDEN_APPLE_TART.get())
                    || state.is(ModBlocks.GLUTTON_HOTCHPOTCH.get());
        }
    }

    private record ScanOffset(int x, int y, int z) {
        private int distance() {
            return Math.max(Math.abs(x), Math.max(Math.abs(y), Math.abs(z)));
        }
    }
}
