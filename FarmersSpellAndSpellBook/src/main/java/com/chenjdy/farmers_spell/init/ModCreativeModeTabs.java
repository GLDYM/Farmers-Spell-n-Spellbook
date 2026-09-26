package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FARMERSSPELL.MODID);

    public static final NonNullList<ItemStack> FOOD_ITEMS = NonNullList.create();
    public static final NonNullList<ItemStack> BLOCK_ITEMS = NonNullList.create();
    public static final NonNullList<ItemStack> EQUIPMENT_ITEMS = NonNullList.create();

    public static final RegistryObject<CreativeModeTab> FARMERSSPELL_TAB =
            CREATIVE_MODE_TABS.register("farmers_spell_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.ALCHEMIST_POT.get()))
                    .title(Component.translatable("itemGroup.farmers_spell_tab"))
                    .displayItems((pParameters, output) -> {
                        FOOD_ITEMS.clear();
                        BLOCK_ITEMS.clear();
                        EQUIPMENT_ITEMS.clear();
                        addFoodItems(output);
                        addBlockItems(output);
                        addEquipmentItems(output);
                    }).build());

    private static void addFoodItems(CreativeModeTab.Output output) {
        accept(output, FOOD_ITEMS, ModItems.GOODBERRY.get());
        accept(output, FOOD_ITEMS, ModItems.AMETHYST_BEETROOT.get());
        accept(output, FOOD_ITEMS, ModItems.ICY_EGG.get());
        accept(output, FOOD_ITEMS, ModItems.WSIP_BUTTER.get());
        accept(output, FOOD_ITEMS, ModItems.BLOOD_TOFU.get());
        accept(output, FOOD_ITEMS, ModItems.FOODGEIST_CHEESE.get());
        accept(output, FOOD_ITEMS, ModItems.DRAGONSKIN_ASPIC.get());
        accept(output, FOOD_ITEMS, ModItems.AMETHYST_SUGAR.get());
        accept(output, FOOD_ITEMS, ModItems.ORIGINAL_NECTAR.get());
        accept(output, FOOD_ITEMS, ModItems.FOODGEIST_SEASONING.get());
        accept(output, FOOD_ITEMS, ModItems.HOGSKIN_SLICE.get());
        accept(output, FOOD_ITEMS, ModItems.CINDEROUS_HAM.get());
        accept(output, FOOD_ITEMS, ModItems.BUTTER_POTATO.get());
        accept(output, FOOD_ITEMS, ModItems.CERIC_CROSS_BUN.get());
        accept(output, FOOD_ITEMS, ModItems.AMETHYST_BURGER.get());
        accept(output, FOOD_ITEMS, ModItems.EDEN_APPLE_TART_SLICE.get());
        accept(output, FOOD_ITEMS, ModItems.BOWL_OF_PUMPKIN_SOUP.get());
        accept(output, FOOD_ITEMS, ModItems.BOWL_OF_SAINGEZI_CHICKEN.get());
        accept(output, FOOD_ITEMS, ModItems.VILLAGER_CHRISM.get());
        accept(output, FOOD_ITEMS, ModItems.GOODBERRY_MUFFIN.get());
        accept(output, FOOD_ITEMS, ModItems.GOODBERRY_PIE_SLICE.get());
        accept(output, FOOD_ITEMS, ModItems.SALMON_BURGER.get());
        accept(output, FOOD_ITEMS, ModItems.SNOWY_TART.get());
        accept(output, FOOD_ITEMS, ModItems.ICEBERGCREAM_SANDWICH.get());
        accept(output, FOOD_ITEMS, ModItems.ICEBERGCREAM.get());
        accept(output, FOOD_ITEMS, ModItems.PERMAFROST_POPSICLE.get());
        accept(output, FOOD_ITEMS, ModItems.VEX_GINGER.get());
        accept(output, FOOD_ITEMS, ModItems.BOWL_OF_GLUTTON_HOTCHPOTCH.get());
        accept(output, FOOD_ITEMS, ModItems.CINDEROUS_HOTPOT.get());
        accept(output, FOOD_ITEMS, ModItems.WINE_BEEF_STEW.get());
        accept(output, FOOD_ITEMS, ModItems.WINE_RICE.get());
        accept(output, FOOD_ITEMS, ModItems.HOGSKIN_SAUSAGE.get());
        accept(output, FOOD_ITEMS, ModItems.RED_VELVET_CAKE_SLICE.get());
        accept(output, FOOD_ITEMS, ModItems.DRAGON_PIZZA.get());
        accept(output, FOOD_ITEMS, ModBlocks.RED_VELVET_CAKE.get());
        accept(output, FOOD_ITEMS, ModBlocks.GOODBERRY_PIE.get());
        accept(output, FOOD_ITEMS, ModBlocks.EDEN_APPLE_TART.get());
        accept(output, FOOD_ITEMS, ModBlocks.PUMPKIN_SOUP.get());
        accept(output, FOOD_ITEMS, ModBlocks.SAINGEZI_CHICKEN.get());
        accept(output, FOOD_ITEMS, ModBlocks.ICEBREAKER_BREAD.get());
        accept(output, FOOD_ITEMS, ModBlocks.GLUTTON_HOTCHPOTCH.get());
        accept(output, FOOD_ITEMS, ModItems.ENERGIZED_CARAMEL.get());
        accept(output, FOOD_ITEMS, ModItems.PAOFU.get());
        accept(output, FOOD_ITEMS, ModItems.THUNDER_COTTON_CANDY.get());
        accept(output, FOOD_ITEMS, ModItems.CERIC_HEART.get());
        accept(output, FOOD_ITEMS, ModItems.EDEN_BAK_APPLE.get());
        accept(output, FOOD_ITEMS, ModItems.ENERGIZED_CANDY.get());
        accept(output, FOOD_ITEMS, ModItems.AMETHYST_MOONCAKE.get());
        accept(output, FOOD_ITEMS, ModItems.HOLY_MILKSHAKE.get());
        accept(output, FOOD_ITEMS, ModItems.BUTTERBEER.get());
        accept(output, FOOD_ITEMS, ModItems.GOODBERRY_JUICE.get());
        accept(output, FOOD_ITEMS, ModItems.ICE_VENOM_WINE.get());
        accept(output, FOOD_ITEMS, ModItems.CATACOMBS_WINE.get());
        accept(output, FOOD_ITEMS, ModItems.ARCANE_COCOA.get());
        accept(output, FOOD_ITEMS, ModItems.PUMPKIN_JUICE.get());
        accept(output, FOOD_ITEMS, ModItems.EVASION_MILK.get());
        accept(output, FOOD_ITEMS, ModItems.AMETHYST_TEQUILA.get());
        accept(output, FOOD_ITEMS, ModItems.INK_BEER.get());
        accept(output, FOOD_ITEMS, ModItems.MOZHAO.get());
        accept(output, FOOD_ITEMS, ModItems.AMETHYST_BEETROOT_SEEDS.get());
    }

    private static void addBlockItems(CreativeModeTab.Output output) {
        accept(output, BLOCK_ITEMS, ModBlocks.WISEWOOD_CABINET.get());
        accept(output, BLOCK_ITEMS, ModBlocks.CINDEROUS_STOVE.get());
        accept(output, BLOCK_ITEMS, ModBlocks.ALCHEMIST_POT.get());
        accept(output, BLOCK_ITEMS, ModBlocks.GOODBERRY_CRATE.get());
        accept(output, BLOCK_ITEMS, ModBlocks.ICY_EGG_CRATE.get());
        accept(output, BLOCK_ITEMS, ModItems.DECREPIT_SCRAP.get());
        accept(output, BLOCK_ITEMS, ModBlocks.EMBER_BLOCK.get());
        accept(output, BLOCK_ITEMS, ModBlocks.GLYPHED_EMBER_BLOCK.get());
        accept(output, BLOCK_ITEMS, ModBlocks.EMBER_PILLAR.get());
        accept(output, BLOCK_ITEMS, ModBlocks.EMBER_BARS.get());
        accept(output, BLOCK_ITEMS, ModBlocks.POLISHED_EMBER_BLOCK.get());
        accept(output, BLOCK_ITEMS, ModBlocks.CHISELED_EMBER_BLOCK.get());
        accept(output, BLOCK_ITEMS, ModBlocks.EMBER_BLOCK_SLAB.get());
    }

    private static void addEquipmentItems(CreativeModeTab.Output output) {
        accept(output, EQUIPMENT_ITEMS, ModItems.GOSPEL.get());
        accept(output, EQUIPMENT_ITEMS, ModItems.HELL_KNIFE.get());
        accept(output, EQUIPMENT_ITEMS, ModItems.TWILIGHT_BLADE.get());
        accept(output, EQUIPMENT_ITEMS, ModItems.BOREAL_KNIFE.get());
        accept(output, EQUIPMENT_ITEMS, ModItems.ECHOING_KNIFE.get());
        accept(output, EQUIPMENT_ITEMS, ModItems.GROW_KNIFE.get());
        accept(output, EQUIPMENT_ITEMS, ModItems.CHERRY_SPOON.get());
        accept(output, EQUIPMENT_ITEMS, ModItems.IRIS_FORK.get());
        accept(output, EQUIPMENT_ITEMS, ModItems.AFFINITY_RING_GLUTTON.get());
        accept(output, EQUIPMENT_ITEMS, ModItems.FOODGEIST_RING.get());
        accept(output, EQUIPMENT_ITEMS, ModItems.GLUTTON_RUNE.get());
        accept(output, EQUIPMENT_ITEMS, ModItems.GLUTTONY_UPGRADE_ORB.get());
        accept(output, EQUIPMENT_ITEMS, ModItems.GLUTTONY_CHEF_HAT.get());
        accept(output, EQUIPMENT_ITEMS, ModItems.GLUTTONY_CHEF_APRON.get());
        accept(output, EQUIPMENT_ITEMS, ModItems.GLUTTONY_CHEF_LEGGINGS.get());
        accept(output, EQUIPMENT_ITEMS, ModItems.GLUTTONY_CHEF_BOOTS.get());
        accept(output, EQUIPMENT_ITEMS, ModItems.WHEAT_BOOK.get());
        accept(output, EQUIPMENT_ITEMS, ModItems.TIRAMISU.get());
        accept(output, EQUIPMENT_ITEMS, ModItems.LASAGNOWLEDGE.get());
    }

    private static void accept(CreativeModeTab.Output output, NonNullList<ItemStack> categoryItems, ItemStack stack) {
        output.accept(stack);
        categoryItems.add(stack.copy());
    }

    private static void accept(CreativeModeTab.Output output, NonNullList<ItemStack> categoryItems, ItemLike item) {
        output.accept(item);
        categoryItems.add(new ItemStack(item));
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
