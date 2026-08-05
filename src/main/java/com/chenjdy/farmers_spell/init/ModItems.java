package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.item.ButterItem;
import com.chenjdy.farmers_spell.item.CropSeedItem;
import com.chenjdy.farmers_spell.item.armor.GluttonyChefArmorItem;
import com.chenjdy.farmers_spell.item.curios.AffinityRingGlutton;
import com.chenjdy.farmers_spell.item.curios.FoodgeistRing;
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
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.item.DrinkableItem;

import net.minecraft.core.registries.Registries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, FarmersSpell.MODID);

    public static final Item CONTAINERS = Items.BOWL;

    public static Item.Properties bowlFoodItem(FoodProperties food) {
        return new Item.Properties().food(food).craftRemainder(Items.BOWL).stacksTo(16);
    }

    public static Item.Properties drinkItem(FoodProperties food) {
        return new Item.Properties().food(food).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16);
    }

    public static Item.Properties normalFoodItem(FoodProperties food) {
        return new Item.Properties().food(food);
    }

    public static Item.Properties handheldCastingItem(double attackDamage, double attackSpeed, double spellPower, double castTimeReduction, double cooldownReduction) {
        ItemAttributeModifiers attributes = ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, attackDamage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.SPELL_POWER.get()), new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "spell_power"), spellPower, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND)
                .add(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.CAST_TIME_REDUCTION.get()), new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "cast_time"), castTimeReduction, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND)
                .add(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.COOLDOWN_REDUCTION.get()), new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "cooldown"), cooldownReduction, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND)
                .build();
        return new Item.Properties().stacksTo(1).attributes(attributes);
    }

    public static final DeferredHolder<Item, Item> BUTTER_POTATO = ITEMS.register("butter_potato",
            () -> new ConsumableItem(normalFoodItem(ModFoods.BUTTER_POTATO)));
    public static final DeferredHolder<Item, Item> AMETHYST_BURGER = ITEMS.register("amethyst_burger",
            () -> new ConsumableItem(normalFoodItem(ModFoods.AMETHYST_BURGER)));
    public static final DeferredHolder<Item, Item> CERIC_CROSS_BUN = ITEMS.register("ceric_cross_bun",
            () -> new ConsumableItem(normalFoodItem(ModFoods.CERIC_CROSS_BUN)));
    public static final DeferredHolder<Item, Item> GOODBERRY_MUFFIN = ITEMS.register("goodberry_muffin",
            () -> new ConsumableItem(normalFoodItem(ModFoods.GOODBERRY_MUFFIN)));
    public static final DeferredHolder<Item, Item> SNOWY_TART = ITEMS.register("snowy_tart",
            () -> new ConsumableItem(normalFoodItem(ModFoods.SNOWY_TART)));
    public static final DeferredHolder<Item, Item> AMETHYST_MOONCAKE = ITEMS.register("amethyst_mooncake",
            () -> new ConsumableItem(normalFoodItem(ModFoods.AMETHYST_MOONCAKE)));
    public static final DeferredHolder<Item, Item> DRAGONSKIN_ASPIC = ITEMS.register("dragonskin_aspic",
            () -> new ConsumableItem(normalFoodItem(ModFoods.DRAGONSKIN_ASPIC)));
    public static final DeferredHolder<Item, Item> DRAGON_PIZZA = ITEMS.register("dragon_pizza",
            () -> new ConsumableItem(normalFoodItem(ModFoods.DRAGON_PIZZA)));
    public static final DeferredHolder<Item, Item> ENERGIZED_CANDY = ITEMS.register("energized_candy",
            () -> new ConsumableItem(normalFoodItem(ModFoods.ENERGIZED_CANDY)));
    public static final DeferredHolder<Item, Item> PAOFU = ITEMS.register("paofu",
            () -> new ConsumableItem(normalFoodItem(ModFoods.PAOFU)));
    public static final DeferredHolder<Item, Item> ICEBREAKER_BREAD = ITEMS.register("icebreaker_bread",
            () -> new ConsumableItem(normalFoodItem(ModFoods.ICEBREAKER_BREAD)));
    public static final DeferredHolder<Item, Item> CINDEROUS_HAM = ITEMS.register("cinderoous_ham",
            () -> new ConsumableItem(normalFoodItem(ModFoods.CINDEROUS_HAM)));
    public static final DeferredHolder<Item, Item> SALMON_BURGER = ITEMS.register("salmon_burger",
            () -> new ConsumableItem(normalFoodItem(ModFoods.SALMON_BURGER)));
    public static final DeferredHolder<Item, Item> THUNDER_COTTON_CANDY = ITEMS.register("thunder_cotton_candy",
            () -> new ConsumableItem(normalFoodItem(ModFoods.THUNDER_COTTON_CANDY)));
    public static final DeferredHolder<Item, Item> BLOOD_TOFU = ITEMS.register("blood_tofu",
            () -> new ConsumableItem(normalFoodItem(ModFoods.BLOOD_TOFU)));
    public static final DeferredHolder<Item, Item> FOODGEIST_CHEESE = ITEMS.register("foodgeist_cheese",
            () -> new ConsumableItem(normalFoodItem(ModFoods.FOODGEIST_CHEESE)));
    public static final DeferredHolder<Item, Item> WSIP_BUTTER = ITEMS.register("wsip_butter",
            () -> new ButterItem(normalFoodItem(ModFoods.WSIP_BUTTER)));
    public static final DeferredHolder<Item, Item> ENERGIZED_CARAMEL = ITEMS.register("energized_caramel",
            () -> new ConsumableItem(normalFoodItem(ModFoods.ENERGIZED_CARAMEL)));
    public static final DeferredHolder<Item, Item> PUMPKIN_SOUP = ITEMS.register("pumpkin_soup",
            () -> new ConsumableItem(normalFoodItem(ModFoods.PUMPKIN_SOUP)));
    public static final DeferredHolder<Item, Item> GOODBERRY = ITEMS.register("goodberry",
            () -> new ConsumableItem(normalFoodItem(ModFoods.GOODBERRY)));
    public static final DeferredHolder<Item, Item> ICY_EGG = ITEMS.register("icy_egg",
            () -> new ConsumableItem(normalFoodItem(ModFoods.ICY_EGG)));
    public static final DeferredHolder<Item, Item> AMETHYST_SUGAR = ITEMS.register("amethyst_sugar",
            () -> new ConsumableItem(new Item.Properties().food(ModFoods.AMETHYST_SUGAR).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16)));
    public static final DeferredHolder<Item, Item> AMETHYST_BEETROOT = ITEMS.register("amethyst_beetroot",
            () -> new ConsumableItem(normalFoodItem(ModFoods.AMETHYST_BEETROOT)));
    public static final DeferredHolder<Item, Item> RED_VELVET_CAKE_SLICE = ITEMS.register("red_velvet_cake_slice",
            () -> new ConsumableItem(normalFoodItem(ModFoods.RED_VELVET_CAKE_SLICE)));
    public static final DeferredHolder<Item, Item> GOODBERRY_PIE_SLICE = ITEMS.register("goodberry_pie_slice",
            () -> new ConsumableItem(normalFoodItem(ModFoods.GOODBERRY_PIE_SLICE)));
    public static final DeferredHolder<Item, Item> EDEN_APPLE_TART_SLICE = ITEMS.register("eden_apple_tart_slice",
            () -> new ConsumableItem(normalFoodItem(ModFoods.EDEN_APPLE_TART_SLICE)));

    public static final DeferredHolder<Item, Item> HOGSKIN_SAUSAGE = ITEMS.register("hogskin_sausage",
            () -> new ConsumableItem(bowlFoodItem(ModFoods.HOGSKIN_SAUSAGE)));
    public static final DeferredHolder<Item, Item> WINE_BEEF_STEW = ITEMS.register("wine_beef_stew",
            () -> new ConsumableItem(bowlFoodItem(ModFoods.WINE_BEEF_STEW)));
    public static final DeferredHolder<Item, Item> WINE_RICE = ITEMS.register("wine_rice",
            () -> new ConsumableItem(bowlFoodItem(ModFoods.WINE_RICE)));
    public static final DeferredHolder<Item, Item> CINDEROUS_HOTPOT = ITEMS.register("cinderous_hotpot",
            () -> new ConsumableItem(bowlFoodItem(ModFoods.CINDEROUS_HOTPOT)));
    public static final DeferredHolder<Item, Item> BUTTER_CHICKEN = ITEMS.register("butter_chicken",
            () -> new ConsumableItem(bowlFoodItem(ModFoods.BUTTER_CHICKEN)));
    public static final DeferredHolder<Item, Item> BOWL_OF_DRAGON_SKIN_ASPIC = ITEMS.register("bowl_of_dragon_skin_aspic",
            () -> new ConsumableItem(bowlFoodItem(ModFoods.BOWL_OF_DRAGON_SKIN_ASPIC)));
    public static final DeferredHolder<Item, Item> ICEBERGCREAM = ITEMS.register("icebergcream",
            () -> new ConsumableItem(bowlFoodItem(ModFoods.ICEBERGCREAM)));

    public static final DeferredHolder<Item, Item> GOODBERRY_JUICE = ITEMS.register("goodberry_juice",
            () -> new DrinkItem(drinkItem(ModFoods.GOODBERRY_JUICE), 10, false));
    public static final DeferredHolder<Item, Item> PUMPKIN_JUICE = ITEMS.register("pumpkin_juice",
            () -> new DrinkItem(drinkItem(ModFoods.PUMPKIN_JUICE), 5, false));
    public static final DeferredHolder<Item, Item> AMETHYST_TEQUILA = ITEMS.register("amethyst_tequila",
            () -> new DrinkableItem(drinkItem(ModFoods.AMETHYST_TEQUILA)));
    public static final DeferredHolder<Item, Item> ARCANE_COCOA = ITEMS.register("arcane_cocoa",
            () -> new DrinkItem(drinkItem(ModFoods.ARCANE_COCOA), 15, true));
    public static final DeferredHolder<Item, Item> BUTTERBEER = ITEMS.register("butterbeer",
            () -> new DrinkItem(drinkItem(ModFoods.BUTTERBEER), 10, false));
    public static final DeferredHolder<Item, Item> CATACOMBS_WINE = ITEMS.register("catacombs_wine",
            () -> new DrinkableItem(drinkItem(ModFoods.CATACOMBS_WINE)));
    public static final DeferredHolder<Item, Item> ICE_VENOM_WINE = ITEMS.register("ice_venom_wine",
            () -> new DrinkableItem(drinkItem(ModFoods.ICE_VENOM_WINE)));
    public static final DeferredHolder<Item, Item> INK_BEER = ITEMS.register("ink_beer",
            () -> new DrinkItem(drinkItem(ModFoods.INK_BEER), 50, false));
    public static final DeferredHolder<Item, Item> EVASION_MILK = ITEMS.register("evasion_milk",
            () -> new DrinkItem(drinkItem(ModFoods.EVASION_MILK), 15, false));
    public static final DeferredHolder<Item, Item> MOZHAO = ITEMS.register("mozhao",
            () -> new DrinkItem(drinkItem(ModFoods.MOZHAO), 15, false));
    public static final DeferredHolder<Item, Item> PERMAFROST_POPSICLE = ITEMS.register("permafrost_popsicle",
            () -> new PermafrostPopsicle(new Item.Properties().food(ModFoods.PERMAFROST_POPSICLE)));
    public static final DeferredHolder<Item, Item> VEX_GINGER = ITEMS.register("vex_ginger",
            () -> new VexGinger(new Item.Properties()));

    public static final DeferredHolder<Item, Item> BOWL_OF_GLUTTON_HOTCHPOTCH = ITEMS.register("bowl_of_glutton_hotchpotch",
            () -> new ConsumableItem(bowlFoodItem(ModFoods.BOWL_OF_GLUTTON_HOTCHPOTCH)));

    public static final DeferredHolder<Item, Item> AMETHYST_BEETROOT_SEEDS = ITEMS.register("amethyst_beetroot_seeds",
            () -> new CropSeedItem(ModBlocks.AMETHYST_BEETROOT, new Item.Properties()));
    public static final DeferredHolder<Item, Item> ORIGINAL_NECTAR = ITEMS.register("original_nectar",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> FOODGEIST_SEASONING = ITEMS.register("foodgeist_seasoning",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> HOGSKIN_SLICE = ITEMS.register("hogskin_slice",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> GLUTTON_RUNE = ITEMS.register("glutton_rune",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> GOSPEL = ITEMS.register("gospel", GospelButterKnife::new);
    public static final DeferredHolder<Item, Item> HELL_KNIFE = ITEMS.register("hell_knife", HellKnife::new);
    public static final DeferredHolder<Item, Item> CHERRY_SPOON = ITEMS.register("cherry_spoon",
            () -> new CherrySpoon(handheldCastingItem(3.0, -3.0, 0.10, 0.20, 0.10)));
    public static final DeferredHolder<Item, Item> IRIS_FORK = ITEMS.register("iris_fork",
            () -> new IrisFork(handheldCastingItem(8.0, -3.0, 0.15, 0.15, 0.15)));
    public static final DeferredHolder<Item, Item> GROW_KNIFE = ITEMS.register("grow_knife", GrowKnife::new);
    public static final DeferredHolder<Item, Item> TWILIGHT_BLADE = ITEMS.register("twilight_blade", TwilightBlade::new);
    public static final DeferredHolder<Item, Item> BOREAL_KNIFE = ITEMS.register("boreal_knife", BorealKnife::new);
    public static final DeferredHolder<Item, Item> ECHOING_KNIFE = ITEMS.register("echoing_knife", EchoingKnife::new);

    public static final DeferredHolder<Item, Item> AFFINITY_RING_GLUTTON = ITEMS.register("affinity_ring_glutton",
            () -> new AffinityRingGlutton(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> FOODGEIST_RING = ITEMS.register("foodgeist_ring",
            () -> new FoodgeistRing(new Item.Properties().stacksTo(1)));

    public static final DeferredHolder<Item, Item> WHEAT_BOOK = ITEMS.register("wheat_book",
            () -> new SpellBook(6, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final DeferredHolder<Item, Item> TIRAMISU = ITEMS.register("tiramisu",
            () -> new SpellBook(12, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC))
                    .withSpellbookAttributes(
                        new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), 
                        new AttributeContainer(AttributeRegistry.MAX_MANA, 150, AttributeModifier.Operation.ADD_VALUE),
                        new AttributeContainer(AttributeRegistry.COOLDOWN_REDUCTION, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ));
    public static final DeferredHolder<Item, Item> LASAGNOWLEDGE = ITEMS.register("lasagnowledge",
            () -> new SpellBook(10, new Item.Properties().stacksTo(1).rarity(Rarity.RARE))
                    .withSpellbookAttributes(
                        new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                        new AttributeContainer(AttributeRegistry.MAX_MANA, 100, AttributeModifier.Operation.ADD_VALUE)
                    ));

    public static final DeferredHolder<Item, Item> GLUTTONY_CHEF_HAT = ITEMS.register("gluttony_chef_hat",
            () -> new GluttonyChefArmorItem(ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1).durability(ArmorItem.Type.HELMET.getDurability(37))));
    public static final DeferredHolder<Item, Item> GLUTTONY_CHEF_APRON = ITEMS.register("gluttony_chef_apron",
            () -> new GluttonyChefArmorItem(ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1).durability(ArmorItem.Type.CHESTPLATE.getDurability(37))));
    public static final DeferredHolder<Item, Item> GLUTTONY_CHEF_LEGGINGS = ITEMS.register("gluttony_chef_leggings",
            () -> new GluttonyChefArmorItem(ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1).durability(ArmorItem.Type.LEGGINGS.getDurability(37))));
    public static final DeferredHolder<Item, Item> GLUTTONY_CHEF_BOOTS = ITEMS.register("gluttony_chef_boots",
            () -> new GluttonyChefArmorItem(ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1).durability(ArmorItem.Type.BOOTS.getDurability(37))));

    public static final DeferredHolder<Item, Item> FOODGEIST_SPAWN_EGG = ITEMS.register("foodgeist_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.FOODGEIST, 0xC8C8C8, 0xFFCBCB, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
