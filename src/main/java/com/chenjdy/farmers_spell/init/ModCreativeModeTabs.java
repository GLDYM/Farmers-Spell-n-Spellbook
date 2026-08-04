package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.creativetab.FancyTabSections;
import com.chenjdy.farmers_spell.creativetab.SectionTextured;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FarmersSpell.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FarmersSpell_TAB = CREATIVE_MODE_TABS.register("farmers_spell_tab", () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.ALCHEMIST_POT.get())).title(Component.translatable("itemGroup.farmers_spell_tab")).displayItems((pParameters, pOutput) -> {
        // 所有物品将通过FancyTabSections的Sections系统自动添加
    }).build());

    // Section的ResourceLocations
    public static final ResourceLocation COOKWARE = ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "cookware");

    public static final ResourceLocation FOOD = ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "food");

    public static final ResourceLocation DRINKS = ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "drinks");

    public static final ResourceLocation EQUIPMENT = ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "equipment");

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
        ResourceLocation tabId = ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "farmers_spell_tab");
        // 厨具 - 方块和厨具物品
        FancyTabSections.addSection(tabId, new SectionTextured(COOKWARE).setTitle(Component.translatable("section.farmers_spell.cookware")).setCollapsible(false).setTextColor(0xFFFFFFFF).addBlock(ModBlocks.WISEWOOD_CABINET).addBlock(ModBlocks.CINDEROUS_STOVE).addBlock(ModBlocks.ALCHEMIST_POT));
        // 食物 - 所有可食用物品
        FancyTabSections.addSection(tabId, new SectionTextured(FOOD).setTitle(Component.translatable("section.farmers_spell.food")).setCollapsible(false).setTextColor(0xFFFFFFFF).// 甜点
        add(ModItems.GOODBERRY).add(ModItems.AMETHYST_BEETROOT).add(ModItems.ICY_EGG).add(ModItems.WSIP_BUTTER).add(ModItems.BLOOD_TOFU).add(ModItems.FOODGEIST_CHEESE).add(ModItems.ORIGINAL_NECTAR).add(ModItems.FOODGEIST_SEASONING).add(ModItems.HOGSKIN_SLICE).add(ModItems.AMETHYST_SUGAR).add(ModItems.GOODBERRY_MUFFIN).addBlock(ModBlocks.GOODBERRY_PIE).add(ModItems.GOODBERRY_PIE_SLICE).addBlock(ModBlocks.EDEN_APPLE_TART).add(ModItems.EDEN_APPLE_TART_SLICE).addBlock(ModBlocks.RED_VELVET_CAKE).add(ModItems.RED_VELVET_CAKE_SLICE).add(ModItems.SNOWY_TART).add(ModItems.AMETHYST_MOONCAKE).add(ModItems.ENERGIZED_CARAMEL).add(ModItems.PAOFU).add(ModItems.THUNDER_COTTON_CANDY).add(ModItems.ENERGIZED_CANDY).add(ModItems.PERMAFROST_POPSICLE).add(ModItems.VEX_GINGER).// 咸食
        add(ModItems.BUTTER_POTATO).add(ModItems.AMETHYST_BURGER).add(ModItems.CERIC_CROSS_BUN).add(ModItems.SALMON_BURGER).add(ModItems.ICEBREAKER_BREAD).add(ModItems.CINDEROUS_HAM).add(ModItems.HOGSKIN_SAUSAGE).add(ModItems.WINE_BEEF_STEW).add(ModItems.WINE_RICE).add(ModItems.CINDEROUS_HOTPOT).add(ModItems.BUTTER_CHICKEN).add(ModItems.BOWL_OF_GLUTTON_HOTCHPOTCH).add(ModItems.BOWL_OF_DRAGON_SKIN_ASPIC).add(ModItems.DRAGON_PIZZA).add(ModItems.DRAGONSKIN_ASPIC).add(ModItems.PUMPKIN_SOUP).addBlock(ModBlocks.GLUTTON_HOTCHPOTCH));
        // 饮品 - 所有饮料
        FancyTabSections.addSection(tabId, new SectionTextured(DRINKS).setTitle(Component.translatable("section.farmers_spell.drinks")).setCollapsible(false).setTextColor(0xFFFFFFFF).add(ModItems.GOODBERRY_JUICE).add(ModItems.PUMPKIN_JUICE).add(ModItems.AMETHYST_TEQUILA).add(ModItems.ARCANE_COCOA).add(ModItems.BUTTERBEER).add(ModItems.CATACOMBS_WINE).add(ModItems.ICE_VENOM_WINE).add(ModItems.INK_BEER).add(ModItems.EVASION_MILK).add(ModItems.ICEBERGCREAM).add(ModItems.MOZHAO));
        // 装备 - 武器、防具和饰品
        FancyTabSections.addSection(tabId, new SectionTextured(EQUIPMENT).setTitle(Component.translatable("section.farmers_spell.equipment")).setCollapsible(false).setTextColor(0xFFFFFFFF).// 武器
        add(ModItems.CHERRY_SPOON).add(ModItems.IRIS_FORK).add(ModItems.GOSPEL).add(ModItems.HELL_KNIFE).add(ModItems.TWILIGHT_BLADE).add(ModItems.GROW_KNIFE).add(ModItems.BOREAL_KNIFE).add(ModItems.ECHOING_KNIFE).// 防具
        add(ModItems.GLUTTONY_CHEF_HAT).add(ModItems.GLUTTONY_CHEF_APRON).add(ModItems.GLUTTONY_CHEF_LEGGINGS).add(ModItems.GLUTTONY_CHEF_BOOTS).// 饰品
        add(ModItems.AFFINITY_RING_GLUTTON).add(ModItems.FOODGEIST_RING).add(ModItems.GLUTTON_RUNE).add(ModItems.WHEAT_BOOK).add(ModItems.TIRAMISU).add(ModItems.LASAGNOWLEDGE).// 其他
        add(ModItems.AMETHYST_BEETROOT_SEEDS).add(ModItems.FOODGEIST_SPAWN_EGG));
    }
}
