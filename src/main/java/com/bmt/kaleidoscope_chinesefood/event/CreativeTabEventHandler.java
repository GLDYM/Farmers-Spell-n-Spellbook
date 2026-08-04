package com.bmt.kaleidoscope_chinesefood.event;

import com.bmt.kaleidoscope_chinesefood.KaleidoscopeChineseFood;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = KaleidoscopeChineseFood.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CreativeTabEventHandler {

    @SubscribeEvent
    public static void addItemsToTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().location().equals(KaleidoscopeChineseFood.fromNamespaceAndPath("kaleidoscope_cookery", "cookery_food"))) {
            Iterator<Map.Entry<ItemStack, CreativeModeTab.TabVisibility>> iterator = event.getEntries().iterator();
            while (iterator.hasNext()) {
                ItemStack itemStack = iterator.next().getKey();
                if (Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemStack.getItem())).getNamespace().equals(KaleidoscopeChineseFood.MODID)) {
                    iterator.remove();
                    //break;
                }
            }
        }
    }
}