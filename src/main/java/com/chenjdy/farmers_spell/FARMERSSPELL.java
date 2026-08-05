package com.chenjdy.farmers_spell;

import com.chenjdy.farmers_spell.creativetab.FTSInternal;
import com.chenjdy.farmers_spell.entity.BadAppleEntity;
import com.chenjdy.farmers_spell.entity.FoodgeistEntity;
import com.chenjdy.farmers_spell.init.*;
import com.chenjdy.farmers_spell.network.NetworkHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib.GeckoLib;

@Mod(FARMERSSPELL.MODID)
public class FARMERSSPELL
{
    public static final String MODID = "farmers_spell";
    public FARMERSSPELL(FMLJavaModLoadingContext context)
    {
        GeckoLib.initialize();
        IEventBus modEventBus = context.getModEventBus();
        ModAttributes.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipeTypes.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModSchools.register(modEventBus);
        ModSpells.register(modEventBus);
        ModEffects.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);
        ModLoots.register(modEventBus);
        ModFluids.register(modEventBus);
        ModTriggers.register(modEventBus);

        modEventBus.addListener(this::onEntityAttributeCreation);
        modEventBus.addListener(this::onCommonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(FTSInternal.class);
        MinecraftForge.EVENT_BUS.addListener(FoodgeistEntity::onPlayerTick);
    }
    
    private void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(ModEntities.BAD_APPLE_ENTITY.get(), BadAppleEntity.createAttributes().build());
        event.put(ModEntities.FOODGEIST.get(), FoodgeistEntity.createAttributes().build());
    }
    
    private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            NetworkHandler.register();
            ModTriggers.registerTriggers();
        });
    }

}
