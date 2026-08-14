package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.entity.BadAppleEntity;
import com.chenjdy.farmers_spell.entity.ButterProjectile;
import com.chenjdy.farmers_spell.entity.ChaosSlashProjectile;
import com.chenjdy.farmers_spell.entity.FoodgeistEntity;
import com.chenjdy.farmers_spell.entity.PreserveCircleAoe;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, FARMERSSPELL.MODID);

    /*public static final RegistryObject<EntityType<KinfeProjectile>> CLEAVER_PROJECTILE = ENTITIES.register("cleaver_projectile",
            () -> EntityType.Builder.<KinfeProjectile>of(KinfeProjectile::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("cleaver_projectile"));*/

    public static final RegistryObject<EntityType<BadAppleEntity>> BAD_APPLE_ENTITY = ENTITIES.register("bad_apple",
            () -> EntityType.Builder.<BadAppleEntity>of(BadAppleEntity::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(8)
                    .updateInterval(20)
                    .build("bad_apple"));

    public static final RegistryObject<EntityType<ChaosSlashProjectile>> CHAOS_SLASH_PROJECTILE = ENTITIES.register("chaos_slash",
            () -> EntityType.Builder.<ChaosSlashProjectile>of(ChaosSlashProjectile::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("chaos_slash"));

    public static final RegistryObject<EntityType<ButterProjectile>> BUTTER_PROJECTILE = ENTITIES.register("butter_projectile",
            () -> EntityType.Builder.<ButterProjectile>of(ButterProjectile::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("butter_projectile"));

    public static final RegistryObject<EntityType<PreserveCircleAoe>> PRESERVE_CIRCLE_AOE = ENTITIES.register("preserve_circle_aoe",
            () -> EntityType.Builder.<PreserveCircleAoe>of(PreserveCircleAoe::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(8)
                    .updateInterval(20)
                    .build("preserve_circle_aoe"));

    public static final RegistryObject<EntityType<FoodgeistEntity>> FOODGEIST = ENTITIES.register("foodgeist",
            () -> EntityType.Builder.<FoodgeistEntity>of(FoodgeistEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.2F)
                    .clientTrackingRange(10)
                    .updateInterval(3)
                    .build("foodgeist"));

    /*public static final RegistryObject<EntityType<PanEntity>> PAN = ENTITIES.register("pan",
            () -> EntityType.Builder.<PanEntity>of(PanEntity::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(8)
                    .updateInterval(20)
                    .build("pan"));*/

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
