package com.bmt.kaleidoscope_chinesefood.init;

import com.bmt.kaleidoscope_chinesefood.KaleidoscopeChineseFood;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KaleidoscopeChineseFood.MODID);

    public static final RegistryObject<CreativeModeTab> KALEIDOSCOPE_SICHUAN_CUISINE_TAB =
            CREATIVE_MODE_TABS.register("kaleidoscope_chinesefood_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.SICHUAN_WONTON.get()))
                    .title(Component.translatable("itemGroup.kaleidoscope_chinesefood_tab"))
                    .displayItems((pParameters,pOutput) -> {
                        pOutput.accept(ModItems.RAW_STEAMED_RICE_ROLLS.get());
                        pOutput.accept(ModItems.SICHUAN_WONTON.get());
                        pOutput.accept(ModItems.WONTON_NOODLES.get());
                        pOutput.accept(ModItems.YANGROU_PAOMO.get());
                        pOutput.accept(ModItems.MAOCAI.get());
                        pOutput.accept(ModItems.SEAWEED_EGG_DROP_SOUP.get());
                        pOutput.accept(ModItems.SICHUAN_BOILED_PORK_SLICES.get());
                        pOutput.accept(ModItems.SICHUAN_BOILED_FISH.get());
                        pOutput.accept(ModItems.TWICE_COOKED_PORK.get());
                        pOutput.accept(ModItems.TWICE_COOKED_PORK_RICE.get());
                        pOutput.accept(ModItems.STIR_FRIED_YELLOW_BEEF.get());
                        pOutput.accept(ModItems.STIR_FRIED_YELLOW_BEEF_RICE.get());
                        pOutput.accept(ModItems.BEEF_WITH_SCRAMBLED_EGGS.get());
                        pOutput.accept(ModItems.BEEF_WITH_SCRAMBLED_EGGS_RICE.get());
                        pOutput.accept(ModItems.STIR_FRIED_THREE_FRESH_VEGETABLES.get());
                        pOutput.accept(ModItems.STIR_FRIED_THREE_FRESH_VEGETABLES_RICE.get());
                        pOutput.accept(ModItems.FOUR_JOY_MEATBALLS.get());
                        pOutput.accept(ModItems.DRY_POT_POTATOES.get());
                        pOutput.accept(ModItems.DRY_POT_CHICKEN.get());
                        pOutput.accept(ModItems.DRY_POT_SPARE_RIBS.get());
                        pOutput.accept(ModItems.YANGZHOU_FRIED_RICE.get());
                        pOutput.accept(ModItems.LAMB_PILAF.get());
                        pOutput.accept(ModItems.STEAMED_RICE_ROLLS.get());
                        pOutput.accept(ModBlocks.FREEZER.get());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
