package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.entity.BadAppleEntity;
import com.chenjdy.farmers_spell.entity.ButterProjectile;
import com.chenjdy.farmers_spell.entity.ChaosSlashProjectile;
import com.chenjdy.farmers_spell.entity.FoodgeistEntity;
import com.chenjdy.farmers_spell.entity.PreserveCircleAoe;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, FarmersSpell.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<BadAppleEntity>> BAD_APPLE_ENTITY = ENTITIES.register("bad_apple",
            () -> EntityType.Builder.<BadAppleEntity>of(BadAppleEntity::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(8)
                    .updateInterval(20)
                    .build("bad_apple"));

    public static final DeferredHolder<EntityType<?>, EntityType<ChaosSlashProjectile>> CHAOS_SLASH_PROJECTILE = ENTITIES.register("chaos_slash",
            () -> EntityType.Builder.<ChaosSlashProjectile>of(ChaosSlashProjectile::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("chaos_slash"));

    public static final DeferredHolder<EntityType<?>, EntityType<ButterProjectile>> BUTTER_PROJECTILE = ENTITIES.register("butter_projectile",
            () -> EntityType.Builder.<ButterProjectile>of(ButterProjectile::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("butter_projectile"));

    public static final DeferredHolder<EntityType<?>, EntityType<PreserveCircleAoe>> PRESERVE_CIRCLE_AOE = ENTITIES.register("preserve_circle_aoe",
            () -> EntityType.Builder.<PreserveCircleAoe>of(PreserveCircleAoe::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(8)
                    .updateInterval(20)
                    .build("preserve_circle_aoe"));

    public static final DeferredHolder<EntityType<?>, EntityType<FoodgeistEntity>> FOODGEIST = ENTITIES.register("foodgeist",
            () -> EntityType.Builder.<FoodgeistEntity>of(FoodgeistEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.2F)
                    .clientTrackingRange(10)
                    .updateInterval(3)
                    .build("foodgeist"));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
