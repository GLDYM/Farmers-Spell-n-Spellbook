package com.chenjdy.farmers_spell.entity;

import com.chenjdy.farmers_spell.init.ModBlocks;
import com.chenjdy.farmers_spell.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.SpawnUtil;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

public final class FoodgeistSpawnHelper {
    private static final int BASE_INTERVAL = 1200;
    private static final int CHECK_RANGE = 10;
    private static final int Y_RANGE = 5;
    private static final int SUMMON_RANGE = 16;
    private static final int SUMMON_SEARCH_RADIUS = 4;
    private static final int[] SUMMON_Y_OFFSETS = {0, 1, -1, 2, -2};
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
        double cx = pos.getX() + 0.5D;
        double cy = pos.getY() + 0.5D;
        double cz = pos.getZ() + 0.5D;
        return level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(16.0D)).stream()
                .filter(Player::isAlive)
                .min((left, right) -> Double.compare(
                        left.distanceToSqr(cx, cy, cz),
                        right.distanceToSqr(cx, cy, cz)))
                .orElse(null);
    }

    private static void spawnFoodgeist(ServerLevel level, BlockPos sourcePos, Player player) {
        if (!level.getEntitiesOfClass(FoodgeistEntity.class, new AABB(sourcePos).inflate(16.0D)).isEmpty()) {
            return;
        }
        Optional<FoodgeistEntity> result = SpawnUtil.trySpawnMob(
                ModEntities.FOODGEIST.get(), MobSpawnType.SPAWNER, level, sourcePos,
                20, 5, 10, SpawnUtil.Strategy.ON_TOP_OF_COLLIDER);
        result.ifPresent(foodgeist -> {
            BlockPos spawnPos = foodgeist.blockPosition();
            level.sendParticles(ParticleTypes.HAPPY_VILLAGER, spawnPos.getX() + 0.5D, spawnPos.getY() + 1.0D,
                    spawnPos.getZ() + 0.5D, 10, 0.5D, 0.5D, 0.5D, 0.1D);
            player.playSound(SoundEvents.VILLAGER_CELEBRATE, 1.0F, 1.0F);
        });
    }

    public static boolean summonOrTeleport(ServerLevel level, BlockPos stovePos, Player player) {
        double cx = stovePos.getX() + 0.5D;
        double cy = stovePos.getY() + 0.5D;
        double cz = stovePos.getZ() + 0.5D;

        Optional<FoodgeistEntity> nearest = level.getEntitiesOfClass(
                        FoodgeistEntity.class, new AABB(stovePos).inflate(SUMMON_RANGE)).stream()
                .min(Comparator.comparingDouble(entity -> entity.distanceToSqr(cx, cy, cz)));

        if (nearest.isEmpty()) {
            return summonNewFoodgeist(level, stovePos);
        }

        Optional<BlockPos> spotResult = findSafeSpotNear(level, stovePos);
        if (spotResult.isEmpty()) {
            return false;
        }
        BlockPos spot = spotResult.get();

        FoodgeistEntity foodgeist = nearest.get();
        if (foodgeist.isSitting()) {
            foodgeist.setSitting(false);
        }
        foodgeist.getNavigation().stop();
        foodgeist.setAnimState(FoodgeistEntity.ANIM_NORMAL);
        foodgeist.teleportTo(spot.getX() + 0.5D, spot.getY(), spot.getZ() + 0.5D);
        level.sendParticles(ParticleTypes.HAPPY_VILLAGER, spot.getX() + 0.5D, spot.getY() + 0.5D,
                spot.getZ() + 0.5D, 8, 0.4D, 0.4D, 0.4D, 0.1D);
        player.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
        return true;
    }

    private static boolean summonNewFoodgeist(ServerLevel level, BlockPos stovePos) {
        Optional<BlockPos> spotResult = findSafeSpotNear(level, stovePos);
        if (spotResult.isEmpty()) {
            return false;
        }
        FoodgeistEntity foodgeist = ModEntities.FOODGEIST.get().create(level);
        if (foodgeist == null) {
            return false;
        }
        BlockPos spot = spotResult.get();
        foodgeist.moveTo(spot.getX() + 0.5D, spot.getY(), spot.getZ() + 0.5D,
                level.getRandom().nextFloat() * 360.0F, 0.0F);
        level.addFreshEntity(foodgeist);
        level.sendParticles(ParticleTypes.HAPPY_VILLAGER, spot.getX() + 0.5D, spot.getY() + 1.0D,
                spot.getZ() + 0.5D, 10, 0.5D, 0.5D, 0.5D, 0.1D);
        return true;
    }

    public static Optional<BlockPos> findSafeSpotNear(ServerLevel level, BlockPos origin) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int radius = 1; radius <= SUMMON_SEARCH_RADIUS; radius++) {
            for (int yOffset : SUMMON_Y_OFFSETS) {
                for (int x = origin.getX() - radius; x <= origin.getX() + radius; x++) {
                    for (int z = origin.getZ() - radius; z <= origin.getZ() + radius; z++) {
                        if (Math.max(Math.abs(x - origin.getX()), Math.abs(z - origin.getZ())) != radius) {
                            continue;
                        }
                        if (x == origin.getX() && z == origin.getZ()) {
                            continue;
                        }
                        cursor.set(x, origin.getY() + yOffset, z);
                        if (isSafeStandingSpot(level, cursor)) {
                            return Optional.of(cursor.immutable());
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

    private static boolean isSafeStandingSpot(ServerLevel level, BlockPos pos) {
        BlockPos below = pos.below();
        if (!level.isLoaded(pos) || !level.isLoaded(below)) {
            return false;
        }
        if (level.getBlockState(below).getCollisionShape(level, below).isEmpty()) {
            return false;
        }
        if (!level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                || !level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()) {
            return false;
        }
        return level.getFluidState(pos).isEmpty() && level.getFluidState(pos.above()).isEmpty();
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
            return state.is(FoodgeistEntity.FOODGEIST_FOOD_BLOCKS);
        }
    }

    private record ScanOffset(int x, int y, int z) {
        private int distance() {
            return Math.max(Math.abs(x), Math.max(Math.abs(y), Math.abs(z)));
        }
    }
}
