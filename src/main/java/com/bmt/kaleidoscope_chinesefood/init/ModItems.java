package com.bmt.kaleidoscope_chinesefood.init;

import com.bmt.kaleidoscope_chinesefood.KaleidoscopeChineseFood;
import com.github.ysbbbbbb.kaleidoscopecookery.item.BowlFoodOnlyItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, KaleidoscopeChineseFood.MODID);

    // 麻辣抄手
    public static final RegistryObject<Item> SICHUAN_WONTON =
            ITEMS.register("sichuan_wonton", () -> new BowlFoodOnlyItem(ModFoods.SICHUAN_WONTON));

    // 云吞面
    public static final RegistryObject<Item> WONTON_NOODLES =
            ITEMS.register("wonton_noodles", () -> new BowlFoodOnlyItem(ModFoods.WONTON_NOODLES));

    // 冒菜
    public static final RegistryObject<Item> MAOCAI =
            ITEMS.register("maocai", () -> new BowlFoodOnlyItem(ModFoods.MAOCAI));

    // 紫菜蛋花汤
    public static final RegistryObject<Item> SEAWEED_EGG_DROP_SOUP =
            ITEMS.register("seaweed_egg_drop_soup", () -> new BowlFoodOnlyItem(ModFoods.SEAWEED_EGG_DROP_SOUP));

    // 水煮肉片
    public static final RegistryObject<Item> SICHUAN_BOILED_PORK_SLICES =
            ITEMS.register("sichuan_boiled_pork_slices", () -> new BowlFoodOnlyItem(ModFoods.SICHUAN_BOILED_PORK_SLICES));

    // 水煮鱼
    public static final RegistryObject<Item> SICHUAN_BOILED_FISH =
            ITEMS.register("sichuan_boiled_fish", () -> new BowlFoodOnlyItem(ModFoods.SICHUAN_BOILED_FISH));

    // 回锅肉
    public static final RegistryObject<Item> TWICE_COOKED_PORK =
            ITEMS.register("twice_cooked_pork", () -> new BowlFoodOnlyItem(ModFoods.TWICE_COOKED_PORK));

    // 回锅肉盖饭
    public static final RegistryObject<Item> TWICE_COOKED_PORK_RICE =
            ITEMS.register("twice_cooked_pork_rice", () -> new BowlFoodOnlyItem(ModFoods.TWICE_COOKED_PORK_RICE));

    // 小炒黄牛肉
    public static final RegistryObject<Item> STIR_FRIED_YELLOW_BEEF =
            ITEMS.register("stir_fried_yellow_beef", () -> new BowlFoodOnlyItem(ModFoods.STIR_FRIED_YELLOW_BEEF));

    // 小炒黄牛肉盖饭
    public static final RegistryObject<Item> STIR_FRIED_YELLOW_BEEF_RICE =
            ITEMS.register("stir_fried_yellow_beef_rice", () -> new BowlFoodOnlyItem(ModFoods.STIR_FRIED_YELLOW_BEEF_RICE));

    // 滑蛋牛肉
    public static final RegistryObject<Item> BEEF_WITH_SCRAMBLED_EGGS =
            ITEMS.register("beef_with_scrambled_eggs", () -> new BowlFoodOnlyItem(ModFoods.BEEF_WITH_SCRAMBLED_EGGS));

    // 滑蛋牛肉盖饭
    public static final RegistryObject<Item> BEEF_WITH_SCRAMBLED_EGGS_RICE =
            ITEMS.register("beef_with_scrambled_eggs_rice", () -> new BowlFoodOnlyItem(ModFoods.BEEF_WITH_SCRAMBLED_EGGS_RICE));

    // 地三鲜
    public static final RegistryObject<Item> STIR_FRIED_THREE_FRESH_VEGETABLES =
            ITEMS.register("stir_fried_three_fresh_vegetables", () -> new BowlFoodOnlyItem(ModFoods.STIR_FRIED_THREE_FRESH_VEGETABLES));

    // 地三鲜盖饭
    public static final RegistryObject<Item> STIR_FRIED_THREE_FRESH_VEGETABLES_RICE =
            ITEMS.register("stir_fried_three_fresh_vegetables_rice", () -> new BowlFoodOnlyItem(ModFoods.STIR_FRIED_THREE_FRESH_VEGETABLES_RICE));

    // 四喜丸子
    public static final RegistryObject<Item> FOUR_JOY_MEATBALLS =
            ITEMS.register("four_joy_meatballs", () -> new BowlFoodOnlyItem(ModFoods.FOUR_JOY_MEATBALLS));

    // 干锅土豆片
    public static final RegistryObject<Item> DRY_POT_POTATOES =
            ITEMS.register("dry_pot_potatoes", () -> new BowlFoodOnlyItem(ModFoods.DRY_POT_POTATOES));

    // 干锅鸡
    public static final RegistryObject<Item> DRY_POT_CHICKEN =
            ITEMS.register("dry_pot_chicken", () -> new BowlFoodOnlyItem(ModFoods.DRY_POT_CHICKEN));

    // 干锅排骨
    public static final RegistryObject<Item> DRY_POT_SPARE_RIBS =
            ITEMS.register("dry_pot_spare_ribs", () -> new BowlFoodOnlyItem(ModFoods.DRY_POT_SPARE_RIBS));

    // 扬州炒饭
    public static final RegistryObject<Item> YANGZHOU_FRIED_RICE =
            ITEMS.register("yangzhou_fried_rice", () -> new BowlFoodOnlyItem(ModFoods.YANGZHOU_FRIED_RICE));

    // 羊肉抓饭
    public static final RegistryObject<Item> LAMB_PILAF =
            ITEMS.register("lamb_pilaf", () -> new BowlFoodOnlyItem(ModFoods.LAMB_PILAF));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}