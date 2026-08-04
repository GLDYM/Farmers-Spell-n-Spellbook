package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.block.AlchemistPotBlock;
import com.chenjdy.farmers_spell.block.AmethystBeetrootBlock;
import com.chenjdy.farmers_spell.block.CinderousStoveBlock;
import com.chenjdy.farmers_spell.block.GluttonHotchpotchBlock;
import com.chenjdy.farmers_spell.block.RedVelvetCakeBlock;
import com.chenjdy.farmers_spell.block.WisewoodCabinetBlock;
import com.chenjdy.farmers_spell.item.PlaceableBlockItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import vectorwing.farmersdelight.common.block.PieBlock;

import java.util.function.Supplier;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, FARMERSSPELL.MODID);

    
    // 橱柜
    public static final DeferredHolder<Block, WisewoodCabinetBlock> WISEWOOD_CABINET = registerBlock("wisewood_cabinet",
            () -> new WisewoodCabinetBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .sound(SoundType.WOOD)
                    .strength(2.0F, 3.0F)));
    // 炉灶
    public static final DeferredHolder<Block, CinderousStoveBlock> CINDEROUS_STOVE = registerBlock("cinderous_stove",
            () -> new CinderousStoveBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .sound(SoundType.STONE)
                    .strength(3.0F, 6.0F)
                    .lightLevel(state -> state.getValue(CinderousStoveBlock.LIT) ? 13 : 0)));
    // 锅
    public static final DeferredHolder<Block, AlchemistPotBlock> ALCHEMIST_POT = registerBlock("alchemist_pot",
            () -> new AlchemistPotBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.METAL)
                    .strength(3.0F)));
    //紫晶甜菜 
    public static final DeferredHolder<Block, AmethystBeetrootBlock> AMETHYST_BEETROOT = BLOCKS.register("amethyst_beetroot",
            () -> new AmethystBeetrootBlock());
    // 红丝绒蛋糕
    public static final DeferredHolder<Block, RedVelvetCakeBlock> RED_VELVET_CAKE = registerBlockWithPlaceableItem("red_velvet_cake",
            () -> new RedVelvetCakeBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .sound(SoundType.WOOL)
                    .strength(0.5F)));
    // 神莓派
    public static final DeferredHolder<Block, PieBlock> GOODBERRY_PIE = registerBlockWithPlaceableItem("goodberry_pie",
            () -> new PieBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PINK)
                    .sound(SoundType.WOOL)
                    .strength(0.5F),
                    ModItems.GOODBERRY_PIE_SLICE));
    // 黄油金苹果派
    public static final DeferredHolder<Block, PieBlock> EDEN_APPLE_TART = registerBlockWithPlaceableItem("eden_apple_tart",
            () -> new PieBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .sound(SoundType.WOOL)
                    .strength(0.5F),
                    ModItems.EDEN_APPLE_TART_SLICE));
    // 坏苹果
    public static final DeferredHolder<Block, Block> BAD_APPLE = BLOCKS.register("bad_apple",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .noCollission()
                    .noOcclusion()
                    .sound(SoundType.GRASS)
                    .strength(0.0F)));
    // 饕餮乱炖
    public static final DeferredHolder<Block, GluttonHotchpotchBlock> GLUTTON_HOTCHPOTCH = registerBlockWithPlaceableItem("glutton_hotchpotch",
            () -> new GluttonHotchpotchBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.METAL)
                    .strength(2.0F)));

    private static <T extends Block> DeferredHolder<Block, T> registerBlock(String name, Supplier<T> block) {
        DeferredHolder<Block, T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredHolder<Block, T> registerBlockWithPlaceableItem(String name, Supplier<T> block) {
        DeferredHolder<Block, T> toReturn = BLOCKS.register(name, block);
        registerPlaceableBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredHolder<Block, T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()));
    }

    private static <T extends Block> void registerPlaceableBlockItem(String name, DeferredHolder<Block, T> block) {
        ModItems.ITEMS.register(name, () -> new PlaceableBlockItem(block.get(),
                new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}