package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FARMERSSPELL.MODID);

    public static final RegistryObject<CreativeModeTab> FARMERSSPELL_TAB =
            CREATIVE_MODE_TABS.register("farmers_spell_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.ALCHEMIST_POT.get()))
                    .title(Component.translatable("itemGroup.farmers_spell_tab"))
                    .displayItems((pParameters,pOutput) -> {
                        pOutput.accept(ModBlocks.EMBER_BLOCK.get());
                        pOutput.accept(ModBlocks.EMBER_PILLAR.get());
                        pOutput.accept(ModBlocks.EMBER_BARS.get());
                        pOutput.accept(ModBlocks.GOODBERRY_CRATE.get());
                        pOutput.accept(ModBlocks.ICY_EGG_CRATE.get());
                        pOutput.accept(ModBlocks.WISEWOOD_CABINET.get());
                        pOutput.accept(ModBlocks.CINDEROUS_STOVE.get());
                        pOutput.accept(ModBlocks.ALCHEMIST_POT.get());
                        pOutput.accept(ModItems.DECREPIT_SCRAP.get()); 
                        pOutput.accept(ModItems.GOODBERRY.get());
                        pOutput.accept(ModItems.AMETHYST_BEETROOT.get());
                        pOutput.accept(ModItems.ICY_EGG.get());
                        pOutput.accept(ModItems.WSIP_BUTTER.get());
                        pOutput.accept(ModItems.BLOOD_TOFU.get());
                        pOutput.accept(ModItems.FOODGEIST_CHEESE.get());
                        pOutput.accept(ModItems.DRAGONSKIN_ASPIC.get());
                        pOutput.accept(ModItems.AMETHYST_SUGAR.get());
                        pOutput.accept(ModItems.ORIGINAL_NECTAR.get());
                        pOutput.accept(ModItems.FOODGEIST_SEASONING.get());
                        pOutput.accept(ModItems.HOGSKIN_SLICE.get());
                        pOutput.accept(ModItems.CINDEROUS_HAM.get());
                        pOutput.accept(ModItems.BUTTER_POTATO.get());
                        pOutput.accept(ModItems.CERIC_CROSS_BUN.get());
                        pOutput.accept(ModItems.AMETHYST_BURGER.get());
                        pOutput.accept(ModBlocks.EDEN_APPLE_TART.get());
                        pOutput.accept(ModItems.EDEN_APPLE_TART_SLICE.get());
                        pOutput.accept(ModBlocks.PUMPKIN_SOUP.get());
                        pOutput.accept(ModItems.BOWL_OF_PUMPKIN_SOUP.get());
                        pOutput.accept(ModBlocks.SAINGEZI_CHICKEN.get());
                        pOutput.accept(ModItems.BOWL_OF_SAINGEZI_CHICKEN.get());
                        pOutput.accept(ModItems.GOODBERRY_MUFFIN.get());
                        pOutput.accept(ModBlocks.GOODBERRY_PIE.get());
                        pOutput.accept(ModItems.GOODBERRY_PIE_SLICE.get());
                        pOutput.accept(ModItems.SALMON_BURGER.get());
                        pOutput.accept(ModItems.SNOWY_TART.get());
                        pOutput.accept(ModBlocks.ICEBREAKER_BREAD.get());
                        pOutput.accept(ModItems.ICEBERGCREAM_SANDWICH.get());
                        pOutput.accept(ModItems.ICEBERGCREAM.get());
                        pOutput.accept(ModItems.PERMAFROST_POPSICLE.get());
                        pOutput.accept(ModItems.VEX_GINGER.get());
                        pOutput.accept(ModBlocks.GLUTTON_HOTCHPOTCH.get());
                        pOutput.accept(ModItems.BOWL_OF_GLUTTON_HOTCHPOTCH.get());
                        pOutput.accept(ModItems.CINDEROUS_HOTPOT.get());
                        pOutput.accept(ModItems.WINE_BEEF_STEW.get());
                        pOutput.accept(ModItems.WINE_RICE.get());
                        pOutput.accept(ModItems.HOGSKIN_SAUSAGE.get());
                        pOutput.accept(ModBlocks.RED_VELVET_CAKE.get());
                        pOutput.accept(ModItems.RED_VELVET_CAKE_SLICE.get());
                        //pOutput.accept(ModItems.BOWL_OF_DRAGON_SKIN_ASPIC.get());
                        pOutput.accept(ModItems.DRAGON_PIZZA.get());
                        pOutput.accept(ModItems.ENERGIZED_CARAMEL.get());
                        pOutput.accept(ModItems.PAOFU.get());
                        pOutput.accept(ModItems.THUNDER_COTTON_CANDY.get());
                        pOutput.accept(ModItems.ENERGIZED_CANDY.get());
                        pOutput.accept(ModItems.AMETHYST_MOONCAKE.get());
                        pOutput.accept(ModItems.BUTTERBEER.get());
                        pOutput.accept(ModItems.GOODBERRY_JUICE.get());
                        pOutput.accept(ModItems.ICE_VENOM_WINE.get());
                        pOutput.accept(ModItems.CATACOMBS_WINE.get());
                        pOutput.accept(ModItems.ARCANE_COCOA.get());
                        pOutput.accept(ModItems.PUMPKIN_JUICE.get());
                        pOutput.accept(ModItems.EVASION_MILK.get());
                        pOutput.accept(ModItems.AMETHYST_TEQUILA.get());
                        pOutput.accept(ModItems.INK_BEER.get());
                        pOutput.accept(ModItems.MOZHAO.get());
                        pOutput.accept(ModItems.GOSPEL.get());
                        pOutput.accept(ModItems.HELL_KNIFE.get());
                        pOutput.accept(ModItems.TWILIGHT_BLADE.get());
                        pOutput.accept(ModItems.BOREAL_KNIFE.get());
                        pOutput.accept(ModItems.ECHOING_KNIFE.get());
                        pOutput.accept(ModItems.GROW_KNIFE.get());
                        pOutput.accept(ModItems.AFFINITY_RING_GLUTTON.get());
                        pOutput.accept(ModItems.FOODGEIST_RING.get());
                        pOutput.accept(ModItems.GLUTTON_RUNE.get());
                        pOutput.accept(ModItems.GLUTTONY_UPGRADE_ORB.get());
                        pOutput.accept(ModItems.GLUTTONY_CHEF_HAT.get());
                        pOutput.accept(ModItems.GLUTTONY_CHEF_APRON.get());
                        pOutput.accept(ModItems.GLUTTONY_CHEF_LEGGINGS.get());
                        pOutput.accept(ModItems.GLUTTONY_CHEF_BOOTS.get());
                        pOutput.accept(ModItems.WHEAT_BOOK.get());
                        pOutput.accept(ModItems.TIRAMISU.get());
                        pOutput.accept(ModItems.LASAGNOWLEDGE.get());
                        pOutput.accept(ModItems.CHERRY_SPOON.get());
                        pOutput.accept(ModItems.IRIS_FORK.get());
                        pOutput.accept(ModItems.AMETHYST_BEETROOT_SEEDS.get());
                        pOutput.accept(ModItems.FOODGEIST_SPAWN_EGG.get());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
