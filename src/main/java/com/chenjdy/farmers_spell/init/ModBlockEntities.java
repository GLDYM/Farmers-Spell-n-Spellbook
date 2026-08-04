package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.block.entity.AlchemistPotBlockEntity;
import com.chenjdy.farmers_spell.block.entity.CinderousStoveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
public class ModBlockEntities {
    
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, FarmersSpell.MODID);
    
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AlchemistPotBlockEntity>> ALCHEMIST_POT = BLOCK_ENTITIES.register("alchemist_pot",
            () -> BlockEntityType.Builder.of(AlchemistPotBlockEntity::new, ModBlocks.ALCHEMIST_POT.get()).build(null));
    
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CinderousStoveBlockEntity>> CINDEROUS_STOVE = BLOCK_ENTITIES.register("cinderous_stove",
            () -> BlockEntityType.Builder.of(CinderousStoveBlockEntity::new, ModBlocks.CINDEROUS_STOVE.get()).build(null));
    
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}