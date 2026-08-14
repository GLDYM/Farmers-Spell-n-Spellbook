package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.block.AlchemistPotBlock;
import com.chenjdy.farmers_spell.block.AmethystBeetrootBlock;
import com.chenjdy.farmers_spell.block.CinderousStoveBlock;
import com.chenjdy.farmers_spell.block.GluttonHotchpotchBlock;
import com.chenjdy.farmers_spell.block.PumpkinSoupBlock;
import com.chenjdy.farmers_spell.block.RedVelvetCakeBlock;
import com.chenjdy.farmers_spell.block.SaingeziChickenBlock;
import com.chenjdy.farmers_spell.block.WisewoodCabinetBlock;
import com.chenjdy.farmers_spell.item.PlaceableBlockItem;
import com.chenjdy.farmers_spell.item.SaingeziChickenItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.block.PieBlock;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, FARMERSSPELL.MODID);

    
    // 橱柜
    public static final RegistryObject<WisewoodCabinetBlock> WISEWOOD_CABINET = registerBlock("wisewood_cabinet",
            () -> new WisewoodCabinetBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .sound(SoundType.WOOD)
                    .strength(2.0F, 3.0F)));
    // 炉灶
    public static final RegistryObject<CinderousStoveBlock> CINDEROUS_STOVE = registerBlock("cinderous_stove",
            () -> new CinderousStoveBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .sound(SoundType.STONE)
                    .strength(3.0F, 6.0F)
                    .lightLevel(state -> state.getValue(CinderousStoveBlock.LIT) ? 13 : 0)));
    // 锅
    public static final RegistryObject<AlchemistPotBlock> ALCHEMIST_POT = registerBlock("alchemist_pot",
            () -> new AlchemistPotBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.METAL)
                    .strength(3.0F)));
    //紫晶甜菜 
    public static final RegistryObject<AmethystBeetrootBlock> AMETHYST_BEETROOT = BLOCKS.register("amethyst_beetroot",
            AmethystBeetrootBlock::new);
    // 红丝绒蛋糕
    public static final RegistryObject<RedVelvetCakeBlock> RED_VELVET_CAKE = registerBlockWithPlaceableItem("red_velvet_cake",
            () -> new RedVelvetCakeBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .sound(SoundType.WOOL)
                    .strength(0.5F)));
    // 神莓派
    public static final RegistryObject<PieBlock> GOODBERRY_PIE = registerBlockWithPlaceableItem("goodberry_pie",
            () -> new PieBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PINK)
                    .sound(SoundType.WOOL)
                    .strength(0.5F),
                    ModItems.GOODBERRY_PIE_SLICE));
    // 黄油金苹果派
    public static final RegistryObject<PieBlock> EDEN_APPLE_TART = registerBlockWithPlaceableItem("eden_apple_tart",
            () -> new PieBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .sound(SoundType.WOOL)
                    .strength(0.5F),
                    ModItems.EDEN_APPLE_TART_SLICE));
    // 坏苹果
    public static final RegistryObject<Block> BAD_APPLE = BLOCKS.register("bad_apple",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .noCollission()
                    .noOcclusion()
                    .sound(SoundType.GRASS)
                    .strength(0.0F)));
    // 饕餮乱炖
    public static final RegistryObject<GluttonHotchpotchBlock> GLUTTON_HOTCHPOTCH = registerBlockWithPlaceableItem("glutton_hotchpotch",
            () -> new GluttonHotchpotchBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.METAL)
                    .strength(2.0F)));
    // 橡肤南瓜浓汤
    public static final RegistryObject<PumpkinSoupBlock> PUMPKIN_SOUP = registerBlockWithPlaceableItem("pumpkin_soup",
            () -> new PumpkinSoupBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .sound(SoundType.WOOL)
                    .strength(0.5F)));
    // 成吉思鸡
    public static final RegistryObject<SaingeziChickenBlock> SAINGEZI_CHICKEN = registerBlockWithCustomItem("saingezi_chicken",
            () -> new SaingeziChickenBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .sound(SoundType.WOOL)
                    .strength(0.5F)
                    .noOcclusion()
                    .isValidSpawn((state, level, pos, type) -> false)),
            SaingeziChickenItem.class);
    // 残焰块
    public static final RegistryObject<Block> EMBER_BLOCK = registerBlock("ember_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .sound(SoundType.STONE)
                    .strength(3.0F, 6.0F)));
    // 残焰柱
    public static final RegistryObject<RotatedPillarBlock> EMBER_PILLAR = registerBlock("ember_pillar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .sound(SoundType.STONE)
                    .strength(3.0F, 6.0F)));
    // 残焰栅栏
    public static final RegistryObject<IronBarsBlock> EMBER_BARS = registerBlock("ember_bars",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .sound(SoundType.STONE)
                    .strength(3.0F, 6.0F)
                    .noOcclusion()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerBlockWithPlaceableItem(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerPlaceableBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerBlockWithCustomItem(String name, Supplier<T> block, Class<? extends BlockItem> itemClass) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerCustomBlockItem(name, toReturn, itemClass);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()));
    }

    private static <T extends Block> void registerPlaceableBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new PlaceableBlockItem(block.get(),
                new Item.Properties()));
    }

    private static <T extends Block> void registerCustomBlockItem(String name, RegistryObject<T> block, Class<? extends BlockItem> itemClass) {
        ModItems.ITEMS.register(name, () -> {
            try {
                return itemClass.getConstructor(Block.class, Item.Properties.class)
                        .newInstance(block.get(), new Item.Properties());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}