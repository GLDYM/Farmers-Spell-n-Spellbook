package com.chenjdy.farmers_spell.creativetab;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import java.util.List;
import java.util.function.Supplier;
import net.neoforged.neoforge.registries.DeferredHolder;

public interface Section<T extends Section<T>> {
    ResourceLocation id();

    default boolean collapsible() {
        return true;
    }

    ConglomerateOfItems items();

    void render(GuiGraphics guiGraphics, Font font, int topLeftX, int topLeftY);

    @SuppressWarnings("unchecked")
    default T add(Item item) {
        items().add(item);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    default T addBlock(Block block) {
        items().addBlock(block);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    default T add(ItemStack stack) {
        items().add(stack);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    default T add(DeferredHolder<Item, ? extends Item> registryObjectOfItem) {
        items().add(registryObjectOfItem);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    default T addBlock(DeferredHolder<Block, ? extends Block> registryObjectOfBlock) {
        items().addBlock(registryObjectOfBlock);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    default T add(ItemLike itemLike) {
        items().add(itemLike);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    default T add(Supplier<ItemStack> itemStackSupplier) {
        items().add(itemStackSupplier);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    default T add(List<ItemStack> listOfStacks) {
        items().add(listOfStacks);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    default T add(ConglomerateOfItems.RegistryDependentEntry entry) {
        items().add(entry);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    default T addItemTag(TagKey<Item> tag) {
        add((registry) ->
            registry.lookup(Registries.ITEM)
                .map(lookup -> lookup.get(tag)
                    .map(named -> named.stream()
                        .map(holder -> holder.value().getDefaultInstance()).toList()
                    ).orElse(List.of()))
                .orElseGet(List::of));
        return (T) this;
    }

    default void renderToggle(CreativeModeInventoryScreen screen, GuiGraphics graphics, Section<?> section, int x, int y, int w, int bannerWidth, int mouseX, int mouseY, boolean isHoveringAny) {
        if (!section.collapsible()) return;
        int tx1 = x + w + 3;
        int tx0 = tx1 - BannerRenderer.ROW_HEIGHT;
        int ty0 = y - 1;
        int ty1 = y + bannerWidth;

        int bx = tx0 + (BannerRenderer.ROW_HEIGHT - 16) / 2 + 1;
        int by = ty0 + (bannerWidth - 16) / 2 + 1;

        if (((isHoveringAny && Screen.hasShiftDown()) || (mouseX >= tx0 && mouseX < tx1 && mouseY >= ty0 && mouseY < ty1)) && screen.getMenu().getCarried().isEmpty())
            graphics.blit(BannerRenderer.COLLAPSED_BUTTON, bx, by, 0, 0, 16, 16, 16, 16);
        else
            graphics.blit(BannerRenderer.EXPANDED_BUTTON, bx, by, 0, 0, 16, 16, 16, 16);
    }
}
