package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.block.entity.AlchemistPotBlockEntity;
import com.chenjdy.farmers_spell.block.entity.CinderousStoveBlockEntity;
import com.chenjdy.farmers_spell.block.entity.EdenAppleTartGlowBlockEntity;
import com.chenjdy.farmers_spell.block.entity.RedVelvetCakeGlowBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, FARMERSSPELL.MODID);
    
    public static final RegistryObject<BlockEntityType<AlchemistPotBlockEntity>> ALCHEMIST_POT = BLOCK_ENTITIES.register("alchemist_pot",
            () -> BlockEntityType.Builder.of(AlchemistPotBlockEntity::new, ModBlocks.ALCHEMIST_POT.get()).build(null));
    
    public static final RegistryObject<BlockEntityType<CinderousStoveBlockEntity>> CINDEROUS_STOVE = BLOCK_ENTITIES.register("cinderous_stove",
            () -> BlockEntityType.Builder.of(CinderousStoveBlockEntity::new, ModBlocks.CINDEROUS_STOVE.get()).build(null));

    public static final RegistryObject<BlockEntityType<RedVelvetCakeGlowBlockEntity>> RED_VELVET_CAKE_GLOW = BLOCK_ENTITIES.register("red_velvet_cake_glow",
            () -> BlockEntityType.Builder.of(RedVelvetCakeGlowBlockEntity::new, ModBlocks.RED_VELVET_CAKE.get()).build(null));

    public static final RegistryObject<BlockEntityType<EdenAppleTartGlowBlockEntity>> EDEN_APPLE_TART_GLOW = BLOCK_ENTITIES.register("eden_apple_tart_glow",
            () -> BlockEntityType.Builder.of(EdenAppleTartGlowBlockEntity::new, ModBlocks.EDEN_APPLE_TART.get()).build(null));
    
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}