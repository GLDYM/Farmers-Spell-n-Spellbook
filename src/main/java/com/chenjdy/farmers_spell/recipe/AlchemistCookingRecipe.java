package com.chenjdy.farmers_spell.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;



public class AlchemistCookingRecipe extends CookingPotRecipe {

    @Nullable
    private final ResourceLocation requiredSchool;

    public AlchemistCookingRecipe(String group, NonNullList<Ingredient> inputItems,
                                  ItemStack output, ItemStack container, float experience, int cookTime,
                                  @Nullable ResourceLocation requiredSchool) {
        super(group, null, inputItems, output, container, experience, cookTime);
        this.requiredSchool = requiredSchool;
    }

    @Nullable
    public ResourceLocation getRequiredSchool() {
        return this.requiredSchool;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AlchemistRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return AlchemistRecipeType.INSTANCE;
    }

    public static class AlchemistRecipeSerializer implements RecipeSerializer<AlchemistCookingRecipe> {

        public static final AlchemistRecipeSerializer INSTANCE = new AlchemistRecipeSerializer();
        private static final Codec<NonNullList<Ingredient>> INGREDIENTS_CODEC = Ingredient.CODEC_NONEMPTY.listOf().xmap(
                list -> {
                    NonNullList<Ingredient> ingredients = NonNullList.create();
                    ingredients.addAll(list);
                    return ingredients;
                },
                list -> list
        );
        private static final MapCodec<Optional<String>> REQUIRED_SCHOOL_CODEC = Codec.STRING.optionalFieldOf("required_school");
        private static final MapCodec<AlchemistCookingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(AlchemistCookingRecipe::getGroup),
                INGREDIENTS_CODEC.fieldOf("ingredients").forGetter(AlchemistCookingRecipe::getIngredients),
                ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.getResultItem(RegistryAccess.EMPTY)),
                ItemStack.CODEC.optionalFieldOf("container", ItemStack.EMPTY).forGetter(AlchemistCookingRecipe::getOutputContainer),
                Codec.FLOAT.optionalFieldOf("experience", 0.2F).forGetter(AlchemistCookingRecipe::getExperience),
                Codec.INT.optionalFieldOf("cookingtime", 200).forGetter(AlchemistCookingRecipe::getCookTime),
                REQUIRED_SCHOOL_CODEC.forGetter(recipe -> Optional.ofNullable(recipe.getRequiredSchool()).map(ResourceLocation::toString))
        ).apply(instance, (group, ingredients, result, container, experience, cookTime, requiredSchool) ->
                new AlchemistCookingRecipe(group, ingredients, result, container, experience, cookTime,
                        requiredSchool.map(ResourceLocation::parse).orElse(null))));
        private static final StreamCodec<RegistryFriendlyByteBuf, AlchemistCookingRecipe> STREAM_CODEC = StreamCodec.of(
                AlchemistRecipeSerializer::toNetwork,
                AlchemistRecipeSerializer::fromNetwork
        );

        @Override
        public MapCodec<AlchemistCookingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AlchemistCookingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public AlchemistCookingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String s = GsonHelper.getAsString(json, "group", "");

            NonNullList<Ingredient> nonnulllist = NonNullList.create();
            for (int i = 0; i < GsonHelper.getAsJsonArray(json, "ingredients").size(); ++i) {
                nonnulllist.add(Ingredient.CODEC_NONEMPTY.parse(JsonOps.INSTANCE, GsonHelper.getAsJsonArray(json, "ingredients").get(i)).getOrThrow());
            }

            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for alchemist cooking recipe");
            } else if (nonnulllist.size() > CookingPotRecipe.INPUT_SLOTS) {
                throw new JsonParseException(
                        "Too many ingredients for alchemist cooking recipe! The maximum is " + CookingPotRecipe.INPUT_SLOTS);
            } else {
                JsonObject resultJson = GsonHelper.getAsJsonObject(json, "result");
                ItemStack output = readItemStack(resultJson);
                ItemStack container = ItemStack.EMPTY;
                if (json.has("container")) {
                    container = readItemStack(GsonHelper.getAsJsonObject(json, "container"));
                }
                float f = GsonHelper.getAsFloat(json, "experience", 0.2F);
                int i = GsonHelper.getAsInt(json, "cookingtime", 200);

                ResourceLocation school = null;
                if (json.has("required_school")) {
                    school = ResourceLocation.parse(GsonHelper.getAsString(json, "required_school"));
                }

                return new AlchemistCookingRecipe(s, nonnulllist, output, container, f, i, school);
            }
        }

        private static ItemStack readItemStack(JsonObject json) {
            String itemId = GsonHelper.getAsString(json, "item");
            int count = GsonHelper.getAsInt(json, "count", 1);
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemId));
            if (item == null) {
                throw new JsonParseException("Unknown item: " + itemId);
            }
            return new ItemStack(item, count);
        }

        private static AlchemistCookingRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            String s = buf.readUtf();
            int i = buf.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
            for (int j = 0; j < i; ++j) {
                nonnulllist.set(j, Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
            }

            ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
            ItemStack container = buf.readBoolean() ? ItemStack.STREAM_CODEC.decode(buf) : ItemStack.EMPTY;
            float f = buf.readFloat();
            int k = buf.readVarInt();

            ResourceLocation school = null;
            if (buf.readBoolean()) {
                school = buf.readResourceLocation();
            }

            return new AlchemistCookingRecipe(s, nonnulllist, output, container, f, k, school);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buf, AlchemistCookingRecipe recipe) {
            buf.writeUtf(recipe.getGroup());
            buf.writeVarInt(recipe.getIngredients().size());

            for (Ingredient ingredient : recipe.getIngredients()) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
            }

            ItemStack.STREAM_CODEC.encode(buf, recipe.getResultItem(RegistryAccess.EMPTY));
            ItemStack container = recipe.getOutputContainer();
            buf.writeBoolean(!container.isEmpty());
            if (!container.isEmpty()) {
                ItemStack.STREAM_CODEC.encode(buf, container);
            }
            buf.writeFloat(recipe.getExperience());
            buf.writeVarInt(recipe.getCookTime());

            if (recipe.requiredSchool != null) {
                buf.writeBoolean(true);
                buf.writeResourceLocation(recipe.requiredSchool);
            } else {
                buf.writeBoolean(false);
            }
        }
    }

    public static class AlchemistRecipeType implements RecipeType<AlchemistCookingRecipe> {
        public static final AlchemistRecipeType INSTANCE = new AlchemistRecipeType();

        private AlchemistRecipeType() {
        }
    }
}
