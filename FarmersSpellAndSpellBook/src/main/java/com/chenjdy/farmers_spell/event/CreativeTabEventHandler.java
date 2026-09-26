package com.chenjdy.farmers_spell.event;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.init.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FARMERSSPELL.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CreativeTabEventHandler {

    @SubscribeEvent
    public static void addItemsToTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().location().equals(ResourceLocation.fromNamespaceAndPath("minecraft", "spawn_eggs"))) {
            event.accept(ModItems.FOODGEIST_SPAWN_EGG.get());
        }
    }
}