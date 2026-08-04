package com.chenjdy.farmers_spell.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import javax.annotation.Nullable;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import com.mojang.serialization.JsonOps;



public class AlchemistCookingRecipe extends CookingPotRecipe {

    @Nullable
    private final ResourceLocation requiredSchool;

    public AlchemistCookingRecipe(ResourceLocation id, String group, NonNullList<Ingredient> inputItems,
                                    ItemStack output, ItemStack container, float experience, int cookTime,
                                    @Nullable ResourceLocation requiredSchool) {
        super(id, group, null, inputItems, output, container, experience, cookTime);
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

        @Override
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
                    school = ResourceLocation.fromNamespaceAndPath(GsonHelper.getAsString(json, "required_school"));
                }

                return new AlchemistCookingRecipe(recipeId, s, nonnulllist, output, container, f, i, school);
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

        @Override
        public AlchemistCookingRecipe fromNetwork(ResourceLocation recipeId, RegistryFriendlyByteBuf buf) {
            String s = buf.readUtf();
            int i = buf.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

            for (int j = 0; j < i; ++j) {
                nonnulllist.set(j, Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
            }

            ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
            ItemStack container = ItemStack.STREAM_CODEC.decode(buf);
            float f = buf.readFloat();
            int k = buf.readVarInt();

            ResourceLocation school = null;
            if (buf.readBoolean()) {
                school = buf.readResourceLocation();
            }

            return new AlchemistCookingRecipe(recipeId, s, nonnulllist, output, container, f, k, school);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, AlchemistCookingRecipe recipe) {
            buf.writeUtf(recipe.getGroup());
            buf.writeVarInt(recipe.getIngredients().size());

            for (Ingredient ingredient : recipe.getIngredients()) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
            }

            ItemStack.STREAM_CODEC.encode(buf, recipe.getResultItem(RegistryAccess.EMPTY));
            ItemStack.STREAM_CODEC.encode(buf, recipe.getOutputContainer());
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
