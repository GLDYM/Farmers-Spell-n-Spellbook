package com.chenjdy.farmers_spell;

import com.chenjdy.farmers_spell.config.ModConfigs;
import com.chenjdy.farmers_spell.creativetab.FTSInternal;
import com.chenjdy.farmers_spell.entity.BadAppleEntity;
import com.chenjdy.farmers_spell.entity.FoodgeistEntity;
import com.chenjdy.farmers_spell.init.*;
import com.chenjdy.farmers_spell.network.NetworkHandler;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.ModContainer;
import com.chenjdy.farmers_spell.network.ModNetwork;
import com.chenjdy.farmers_spell.block.entity.AlchemistPotBlockEntity;
import com.chenjdy.farmers_spell.block.entity.WisewoodCabinetBlockEntity;
import net.neoforged.fml.loading.FMLEnvironment;


@Mod(FarmersSpell.MODID)
public class FarmersSpell {

    public static final String MODID = "farmers_spell";

    public FarmersSpell(IEventBus modEventBus, ModContainer modContainer) {
        ModAttributes.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        modEventBus.addListener(AlchemistPotBlockEntity::registerCapabilities);
        modEventBus.addListener(WisewoodCabinetBlockEntity::registerCapabilities);
        ModMenuTypes.register(modEventBus);
        ModRecipeTypes.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModSchools.register(modEventBus);
        ModSpells.register(modEventBus);
        ModEffects.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);
        ModFluids.register(modEventBus);
        ModTriggers.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, ModConfigs.SPEC);
        if (FMLEnvironment.dist.isClient()) {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }

        modEventBus.addListener(ModNetwork::register);
        modEventBus.addListener(this::onEntityAttributeCreation);
        modEventBus.addListener(this::onCommonSetup);
        
        NeoForge.EVENT_BUS.register(FTSInternal.class);
    }

    private void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(ModEntities.BAD_APPLE_ENTITY.get(), BadAppleEntity.createAttributes().build());
        event.put(ModEntities.FOODGEIST.get(), FoodgeistEntity.createAttributes().build());
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            NetworkHandler.register();
        });
    }
}
