package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.item.ButterItem;
import com.chenjdy.farmers_spell.item.CropSeedItem;
import com.chenjdy.farmers_spell.item.curios.AffinityRingGlutton;
import com.chenjdy.farmers_spell.item.curios.FoodgeistRing;
import com.chenjdy.farmers_spell.item.curios.LasagnowledgeSpellBook;
import com.chenjdy.farmers_spell.item.curios.TiramisuSpellBook;
import com.chenjdy.farmers_spell.item.curios.WheatSpellBook;
import com.chenjdy.farmers_spell.item.irons.DrinkItem;
import com.chenjdy.farmers_spell.item.irons.PermafrostPopsicle;
import com.chenjdy.farmers_spell.item.irons.VexGinger;
import com.chenjdy.farmers_spell.item.weapons.BorealKnife;
import com.chenjdy.farmers_spell.item.weapons.CherrySpoon;
import com.chenjdy.farmers_spell.item.weapons.EchoingKnife;
import com.chenjdy.farmers_spell.item.weapons.GospelButterKnife;
import com.chenjdy.farmers_spell.item.weapons.GrowKnife;
import com.chenjdy.farmers_spell.item.weapons.HellKnife;
import com.chenjdy.farmers_spell.item.weapons.IrisFork;
import com.chenjdy.farmers_spell.item.weapons.TwilightBlade;
import com.chenjdy.farmers_spell.item.armor.*;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.item.DrinkableItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, FARMERSSPELL.MODID);

    public static final Item CONTAINERS = Items.BOWL;

    public static Item.Properties bowlFoodItem(FoodProperties food) {
        return new Item.Properties()
                .food(food)
                .craftRemainder(Items.BOWL)
                .stacksTo(16);
    }
    public static Item.Properties drinkItem(FoodProperties food) {
        return new Item.Properties()
                .food(food)
                .craftRemainder(Items.GLASS_BOTTLE)
                .stacksTo(16);
    }
    public static Item.Properties normalFoodItem(FoodProperties food) {
        return new Item.Properties()
                .food(food);
    }
    // 神圣奶酪焗土豆
    public static final RegistryObject<Item> BUTTER_POTATO = ITEMS.register("butter_potato",
            () -> new ConsumableItem(normalFoodItem(ModFoods.BUTTER_POTATO)));
    // 宝石汉堡
    public static final RegistryObject<Item> AMETHYST_BURGER = ITEMS.register("amethyst_burger",
            () -> new ConsumableItem(normalFoodItem(ModFoods.AMETHYST_BURGER)));
    // 闪烁十字面包
    public static final RegistryObject<Item> CERIC_CROSS_BUN = ITEMS.register("ceric_cross_bun",
            () -> new ConsumableItem(normalFoodItem(ModFoods.CERIC_CROSS_BUN)));
    // 神莓玛芬
    public static final RegistryObject<Item> GOODBERRY_MUFFIN = ITEMS.register("goodberry_muffin",
            () -> new ConsumableItem(normalFoodItem(ModFoods.GOODBERRY_MUFFIN)));
    // 冰挞
    public static final RegistryObject<Item> SNOWY_TART = ITEMS.register("snowy_tart",
            () -> new ConsumableItem(normalFoodItem(ModFoods.SNOWY_TART)));
    // 月饼
    public static final RegistryObject<Item> AMETHYST_MOONCAKE = ITEMS.register("amethyst_mooncake",
            () -> new ConsumableItem(normalFoodItem(ModFoods.AMETHYST_MOONCAKE)));
    // 龙鳞肉冻
    public static final RegistryObject<Item> DRAGONSKIN_ASPIC = ITEMS.register("dragonskin_aspic",
            () -> new ConsumableItem(normalFoodItem(ModFoods.DRAGONSKIN_ASPIC)));
    // 披萨
    public static final RegistryObject<Item> DRAGON_PIZZA = ITEMS.register("dragon_pizza",
            () -> new ConsumableItem(normalFoodItem(ModFoods.DRAGON_PIZZA)));
    // 过载焦糖
    public static final RegistryObject<Item> ENERGIZED_CANDY = ITEMS.register("energized_candy",
            () -> new ConsumableItem(normalFoodItem(ModFoods.ENERGIZED_CANDY)));
    // 泡芙
    public static final RegistryObject<Item> PAOFU = ITEMS.register("paofu",
            () -> new ConsumableItem(normalFoodItem(ModFoods.PAOFU)));
    // 破冰面包
    public static final RegistryObject<Item> ICEBREAKER_BREAD = ITEMS.register("icebreaker_bread",
            () -> new ConsumableItem(normalFoodItem(ModFoods.ICEBREAKER_BREAD)));
    // 火腿
    public static final RegistryObject<Item> CINDEROUS_HAM = ITEMS.register("cinderoous_ham",
            () -> new ConsumableItem(normalFoodItem(ModFoods.CINDEROUS_HAM)));
    // 北冰鲑鱼堡
    public static final RegistryObject<Item> SALMON_BURGER = ITEMS.register("salmon_burger",
            () -> new ConsumableItem(normalFoodItem(ModFoods.SALMON_BURGER)));
    // 雷云棉花糖
    public static final RegistryObject<Item> THUNDER_COTTON_CANDY = ITEMS.register("thunder_cotton_candy",
            () -> new ConsumableItem(normalFoodItem(ModFoods.THUNDER_COTTON_CANDY)));
    // 血豆腐
    public static final RegistryObject<Item> BLOOD_TOFU = ITEMS.register("blood_tofu",
            () -> new ConsumableItem(normalFoodItem(ModFoods.BLOOD_TOFU)));
    // 食灵奶酪
    public static final RegistryObject<Item> FOODGEIST_CHEESE = ITEMS.register("foodgeist_cheese",
            () -> new ConsumableItem(normalFoodItem(ModFoods.FOODGEIST_CHEESE)));
    // 黄油
    public static final RegistryObject<Item> WSIP_BUTTER = ITEMS.register("wsip_butter",
            () -> new ButterItem(normalFoodItem(ModFoods.WSIP_BUTTER)));
    // 过载焦糖
    public static final RegistryObject<Item> ENERGIZED_CARAMEL = ITEMS.register("energized_caramel",
            () -> new ConsumableItem(normalFoodItem(ModFoods.ENERGIZED_CARAMEL)));

    // 神莓
    public static final RegistryObject<Item> GOODBERRY = ITEMS.register("goodberry",
            () -> new ConsumableItem(normalFoodItem(ModFoods.GOODBERRY)));
    // 霜皮蛋
    public static final RegistryObject<Item> ICY_EGG = ITEMS.register("icy_egg",
            () -> new ConsumableItem(normalFoodItem(ModFoods.ICY_EGG)));
    // 紫晶糖
    public static final RegistryObject<Item> AMETHYST_SUGAR = ITEMS.register("amethyst_sugar",
            () -> new ConsumableItem(new Item.Properties()
                    .food(ModFoods.AMETHYST_SUGAR)
                    .craftRemainder(Items.GLASS_BOTTLE)
                    .stacksTo(16)));
    // 紫晶甜菜
    public static final RegistryObject<Item> AMETHYST_BEETROOT = ITEMS.register("amethyst_beetroot",
            () -> new ConsumableItem(normalFoodItem(ModFoods.AMETHYST_BEETROOT)));

    // 红丝绒蛋糕切片
    public static final RegistryObject<Item> RED_VELVET_CAKE_SLICE = ITEMS.register("red_velvet_cake_slice",
            () -> new ConsumableItem(normalFoodItem(ModFoods.RED_VELVET_CAKE_SLICE)));
    // 神莓派切片
    public static final RegistryObject<Item> GOODBERRY_PIE_SLICE = ITEMS.register("goodberry_pie_slice",
            () -> new ConsumableItem(normalFoodItem(ModFoods.GOODBERRY_PIE_SLICE)));
    // 黄油金苹果派切片
    public static final RegistryObject<Item> EDEN_APPLE_TART_SLICE = ITEMS.register("eden_apple_tart_slice",
            () -> new ConsumableItem(normalFoodItem(ModFoods.EDEN_APPLE_TART_SLICE)));

    //疣猪皮小香肠
    public static final RegistryObject<Item> HOGSKIN_SAUSAGE = ITEMS.register("hogskin_sausage",
            () -> new ConsumableItem(bowlFoodItem(ModFoods.HOGSKIN_SAUSAGE)));
    //血酒炖牛肉
    public static final RegistryObject<Item> WINE_BEEF_STEW = ITEMS.register("wine_beef_stew",
            () -> new ConsumableItem(bowlFoodItem(ModFoods.WINE_BEEF_STEW)));
    //红酒血汁烩饭
    public static final RegistryObject<Item> WINE_RICE = ITEMS.register("wine_rice",
            () -> new ConsumableItem(bowlFoodItem(ModFoods.WINE_RICE)));
    //炽血麻辣烫
    public static final RegistryObject<Item> CINDEROUS_HOTPOT = ITEMS.register("cinderous_hotpot",
            () -> new ConsumableItem(bowlFoodItem(ModFoods.CINDEROUS_HOTPOT)));
    // 碗装龙鳞冻
    public static final RegistryObject<Item> BOWL_OF_DRAGON_SKIN_ASPIC = ITEMS.register("bowl_of_dragon_skin_aspic",
            () -> new ConsumableItem(bowlFoodItem(ModFoods.BOWL_OF_DRAGON_SKIN_ASPIC)));
    // 冰山奶霜
    public static final RegistryObject<Item> ICEBERGCREAM = ITEMS.register("icebergcream",
            () -> new ConsumableItem(bowlFoodItem(ModFoods.ICEBERGCREAM)));

    // 神莓汁
    public static final RegistryObject<Item> GOODBERRY_JUICE = ITEMS.register("goodberry_juice",
            () -> new DrinkItem(drinkItem(ModFoods.GOODBERRY_JUICE), 10, false));
    // 南瓜汁
    public static final RegistryObject<Item> PUMPKIN_JUICE = ITEMS.register("pumpkin_juice",
            () -> new DrinkItem(drinkItem(ModFoods.PUMPKIN_JUICE), 5, false));
    // 紫水晶龙舌兰
    public static final RegistryObject<Item> AMETHYST_TEQUILA = ITEMS.register("amethyst_tequila",
            () -> new DrinkableItem(drinkItem(ModFoods.AMETHYST_TEQUILA)));
    // 奥术热可可
    public static final RegistryObject<Item> ARCANE_COCOA = ITEMS.register("arcane_cocoa",
            () -> new DrinkItem(drinkItem(ModFoods.ARCANE_COCOA), 15, true));
    // 黄油啤酒
    public static final RegistryObject<Item> BUTTERBEER = ITEMS.register("butterbeer",
            () -> new DrinkItem(drinkItem(ModFoods.BUTTERBEER), 10, false));
    // 墓穴红酒
    public static final RegistryObject<Item> CATACOMBS_WINE = ITEMS.register("catacombs_wine",
            () -> new DrinkableItem(drinkItem(ModFoods.CATACOMBS_WINE)));
    // 蛛牙冰酒
    public static final RegistryObject<Item> ICE_VENOM_WINE = ITEMS.register("ice_venom_wine",
            () -> new DrinkableItem(drinkItem(ModFoods.ICE_VENOM_WINE)));
    // 墨水啤酒
    public static final RegistryObject<Item> INK_BEER = ITEMS.register("ink_beer",
            () -> new DrinkItem(drinkItem(ModFoods.INK_BEER), 50, false));
    // 闪避拿铁
    public static final RegistryObject<Item> EVASION_MILK = ITEMS.register("evasion_milk",
            () -> new DrinkItem(drinkItem(ModFoods.EVASION_MILK), 15, false));
    // 雷爪
    public static final RegistryObject<Item> MOZHAO = ITEMS.register("mozhao",
            () -> new DrinkItem(drinkItem(ModFoods.MOZHAO), 15, false));
    public static final RegistryObject<Item> PERMAFROST_POPSICLE = ITEMS.register("permafrost_popsicle",
            () -> new PermafrostPopsicle(new Item.Properties().food(ModFoods.PERMAFROST_POPSICLE)));
    public static final RegistryObject<Item> VEX_GINGER = ITEMS.register("vex_ginger",
            () -> new VexGinger(new Item.Properties()));
    
    // 碗装饕餮乱炖
    public static final RegistryObject<Item> BOWL_OF_GLUTTON_HOTCHPOTCH = ITEMS.register("bowl_of_glutton_hotchpotch",
            () -> new ConsumableItem(bowlFoodItem(ModFoods.BOWL_OF_GLUTTON_HOTCHPOTCH)));
    // 碗装橡肤南瓜汤
    public static final RegistryObject<Item> BOWL_OF_PUMPKIN_SOUP = ITEMS.register("bowl_of_pumpkin_soup",
            () -> new ConsumableItem(bowlFoodItem(ModFoods.BOWL_OF_PUMPKIN_SOUP)));
    // 碗装成吉思鸡
    public static final RegistryObject<Item> BOWL_OF_SAINGEZI_CHICKEN = ITEMS.register("bowl_of_saingezi_chicken",
            () -> new ConsumableItem(bowlFoodItem(ModFoods.BOWL_OF_SAINGEZI_CHICKEN)));

    // 紫晶甜菜种子
    public static final RegistryObject<Item> AMETHYST_BEETROOT_SEEDS = ITEMS.register("amethyst_beetroot_seeds",
            () -> new CropSeedItem(ModBlocks.AMETHYST_BEETROOT, new Item.Properties()));

    // 源初琼浆
    public static final RegistryObject<Item> ORIGINAL_NECTAR = ITEMS.register("original_nectar",
            () -> new Item(new Item.Properties()));
    // 食灵百味瓶
    public static final RegistryObject<Item> FOODGEIST_SEASONING = ITEMS.register("foodgeist_seasoning",
            () -> new Item(new Item.Properties()));
    // 切制疣猪皮
    public static final RegistryObject<Item> HOGSKIN_SLICE = ITEMS.register("hogskin_slice",
            () -> new Item(new Item.Properties()));
    // 饕魔符文
    public static final RegistryObject<Item> GLUTTON_RUNE = ITEMS.register("glutton_rune",
            () -> new Item(new Item.Properties()));

    // 武器
    public static final RegistryObject<Item> GOSPEL = ITEMS.register("gospel",
            GospelButterKnife::new);
    public static final RegistryObject<Item> HELL_KNIFE = ITEMS.register("hell_knife",
            HellKnife::new);
    public static final RegistryObject<Item> CHERRY_SPOON = ITEMS.register("cherry_spoon",
            () -> new CherrySpoon(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> IRIS_FORK = ITEMS.register("iris_fork",
            () -> new IrisFork(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GROW_KNIFE = ITEMS.register("grow_knife",
            GrowKnife::new);
    public static final RegistryObject<Item> TWILIGHT_BLADE = ITEMS.register("twilight_blade",
            TwilightBlade::new);
    public static final RegistryObject<Item> BOREAL_KNIFE = ITEMS.register("boreal_knife",
            BorealKnife::new);
    public static final RegistryObject<Item> ECHOING_KNIFE = ITEMS.register("echoing_knife",
            EchoingKnife::new);

    // 饰品
    public static final RegistryObject<Item> AFFINITY_RING_GLUTTON = ITEMS.register("affinity_ring_glutton",
            () -> new AffinityRingGlutton(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FOODGEIST_RING = ITEMS.register("foodgeist_ring",
            () -> new FoodgeistRing(new Item.Properties().stacksTo(1)));

    // 法术书
    public static final RegistryObject<Item> WHEAT_BOOK = ITEMS.register("wheat_book",
            () -> new WheatSpellBook(6, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> TIRAMISU = ITEMS.register("tiramisu",
            () -> new TiramisuSpellBook());
    public static final RegistryObject<Item> LASAGNOWLEDGE = ITEMS.register("lasagnowledge",
            () -> new LasagnowledgeSpellBook());

    // 护甲
    public static final RegistryObject<Item> GLUTTONY_CHEF_HAT = ITEMS.register("gluttony_chef_hat",
            () -> new GluttonyChefArmorItem(ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GLUTTONY_CHEF_APRON = ITEMS.register("gluttony_chef_apron",
            () -> new GluttonyChefArmorItem(ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GLUTTONY_CHEF_LEGGINGS = ITEMS.register("gluttony_chef_leggings",
            () -> new GluttonyChefArmorItem(ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GLUTTONY_CHEF_BOOTS = ITEMS.register("gluttony_chef_boots",
            () -> new GluttonyChefArmorItem(ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));

    // 刷怪蛋
    public static final RegistryObject<Item> FOODGEIST_SPAWN_EGG = ITEMS.register("foodgeist_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.FOODGEIST, 0xC8C8C8, 0xFFCBCB,
                    new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}