package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FarmersSpell;
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
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.block.PieBlock;

import java.util.function.Supplier;

import net.minecraft.core.registries.Registries;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, FarmersSpell.MODID);

    public static final DeferredHolder<Block, WisewoodCabinetBlock> WISEWOOD_CABINET = registerBlock("wisewood_cabinet",
            () -> new WisewoodCabinetBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .sound(SoundType.WOOD)
                    .strength(2.0F, 3.0F)));

    public static final DeferredHolder<Block, CinderousStoveBlock> CINDEROUS_STOVE = registerBlock("cinderous_stove",
            () -> new CinderousStoveBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .sound(SoundType.STONE)
                    .strength(3.0F, 6.0F)
                    .lightLevel(state -> state.getValue(CinderousStoveBlock.LIT) ? 13 : 0)));

    public static final DeferredHolder<Block, AlchemistPotBlock> ALCHEMIST_POT = registerBlock("alchemist_pot",
            () -> new AlchemistPotBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.METAL)
                    .strength(3.0F)));

    public static final DeferredHolder<Block, AmethystBeetrootBlock> AMETHYST_BEETROOT = BLOCKS.register("amethyst_beetroot",
            () -> new AmethystBeetrootBlock());

    public static final DeferredHolder<Block, RedVelvetCakeBlock> RED_VELVET_CAKE = registerBlockWithPlaceableItem("red_velvet_cake",
            () -> new RedVelvetCakeBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .sound(SoundType.WOOL)
                    .strength(0.5F)));

    public static final DeferredHolder<Block, PieBlock> GOODBERRY_PIE = registerBlockWithPlaceableItem("goodberry_pie",
            () -> new PieBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PINK)
                    .sound(SoundType.WOOL)
                    .strength(0.5F),
                    ModItems.GOODBERRY_PIE_SLICE));

    public static final DeferredHolder<Block, PieBlock> EDEN_APPLE_TART = registerBlockWithPlaceableItem("eden_apple_tart",
            () -> new PieBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .sound(SoundType.WOOL)
                    .strength(0.5F),
                    ModItems.EDEN_APPLE_TART_SLICE));

    public static final DeferredHolder<Block, Block> BAD_APPLE = BLOCKS.register("bad_apple",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .noCollission()
                    .noOcclusion()
                    .sound(SoundType.GRASS)
                    .strength(0.0F)));

    public static final DeferredHolder<Block, GluttonHotchpotchBlock> GLUTTON_HOTCHPOTCH = registerBlockWithPlaceableItem("glutton_hotchpotch",
            () -> new GluttonHotchpotchBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.METAL)
                    .strength(2.0F)));

    public static final DeferredHolder<Block, PumpkinSoupBlock> PUMPKIN_SOUP = registerBlockWithPlaceableItem("pumpkin_soup",
            () -> new PumpkinSoupBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .sound(SoundType.WOOL)
                    .strength(0.5F)));

    public static final DeferredHolder<Block, SaingeziChickenBlock> SAINGEZI_CHICKEN = registerBlockWithCustomItem("saingezi_chicken",
            () -> new SaingeziChickenBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .sound(SoundType.WOOL)
                    .strength(0.5F)
                    .noOcclusion()
                    .isValidSpawn((state, level, pos, type) -> false)),
            SaingeziChickenItem.class);

    public static final DeferredHolder<Block, Block> EMBER_BLOCK = registerBlock("ember_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .sound(SoundType.STONE)
                    .strength(3.0F, 6.0F)));

    public static final DeferredHolder<Block, RotatedPillarBlock> EMBER_PILLAR = registerBlock("ember_pillar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .sound(SoundType.STONE)
                    .strength(3.0F, 6.0F)));

    public static final DeferredHolder<Block, IronBarsBlock> EMBER_BARS = registerBlock("ember_bars",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .sound(SoundType.STONE)
                    .strength(3.0F, 6.0F)
                    .noOcclusion()));

    private static <T extends Block> DeferredHolder<Block, T> registerBlock(String name, Supplier<T> block) {
        DeferredHolder<Block, T> deferredBlock = BLOCKS.register(name, block);
        registerBlockItem(name, deferredBlock);
        return deferredBlock;
    }

    private static <T extends Block> DeferredHolder<Block, T> registerBlockWithPlaceableItem(String name, Supplier<T> block) {
        DeferredHolder<Block, T> deferredBlock = BLOCKS.register(name, block);
        registerPlaceableBlockItem(name, deferredBlock);
        return deferredBlock;
    }

    private static <T extends Block> DeferredHolder<Block, T> registerBlockWithCustomItem(String name, Supplier<T> block, Class<? extends BlockItem> itemClass) {
        DeferredHolder<Block, T> deferredBlock = BLOCKS.register(name, block);
        registerCustomBlockItem(name, deferredBlock, itemClass);
        return deferredBlock;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredHolder<Block, T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static <T extends Block> void registerPlaceableBlockItem(String name, DeferredHolder<Block, T> block) {
        ModItems.ITEMS.register(name, () -> new PlaceableBlockItem(block.get(), new Item.Properties()));
    }

    private static <T extends Block> void registerCustomBlockItem(String name, DeferredHolder<Block, T> block, Class<? extends BlockItem> itemClass) {
        ModItems.ITEMS.register(name, () -> {
            try {
                return itemClass.getConstructor(Block.class, Item.Properties.class)
                        .newInstance(block.get(), new Item.Properties());
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
