package com.chenjdy.farmers_spell.client.creativetab;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.init.ModBlocks;
import com.chenjdy.farmers_spell.init.ModCreativeModeTabs;
import com.chenjdy.farmers_spell.init.ModItems;
import com.chenjdy.farmers_spell.mixins.accessor.CreativeModeInventoryScreenAccessor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = FARMERSSPELL.MODID, value = Dist.CLIENT)
public class CreativeTabFilter {

    private static final ResourceLocation VANILLA_TABS =
            ResourceLocation.withDefaultNamespace("textures/gui/container/creative_inventory/tabs.png");

    private static final List<FilterButton> BUTTONS = new ArrayList<>();
    private static CreativeModeTab lastTab;
    private static Category selectedCategory = Category.FOOD;

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (!(event.getScreen() instanceof CreativeModeInventoryScreen screen)) {
            return;
        }
        BUTTONS.clear();
        int x = screen.getGuiLeft() - 28;
        int top = screen.getGuiTop();
        BUTTONS.add(new FilterButton(x, top + 17, Category.FOOD));
        BUTTONS.add(new FilterButton(x, top + 44, Category.BLOCKS));
        BUTTONS.add(new FilterButton(x, top + 71, Category.EQUIPMENT));
        BUTTONS.forEach(event::addListener);
        onSwitchTab(CreativeModeInventoryScreenAccessor.fts$getSelectedTab(), screen);
    }

    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Render.Post event) {
        if (!(event.getScreen() instanceof CreativeModeInventoryScreen screen)) {
            return;
        }
        CreativeModeTab tab = CreativeModeInventoryScreenAccessor.fts$getSelectedTab();
        if (lastTab != tab) {
            onSwitchTab(tab, screen);
            lastTab = tab;
        }
    }

    private static void onSwitchTab(CreativeModeTab tab, CreativeModeInventoryScreen screen) {
        boolean isOurTab = tab == ModCreativeModeTabs.FARMERSSPELL_TAB.get();
        BUTTONS.forEach(button -> button.visible = isOurTab);
        if (isOurTab) {
            refreshItems(screen);
        }
    }

    private static void select(Category category) {
        selectedCategory = category;
        if (Minecraft.getInstance().screen instanceof CreativeModeInventoryScreen screen) {
            refreshItems(screen);
        }
    }

    private static void refreshItems(CreativeModeInventoryScreen screen) {
        NonNullList<ItemStack> items = screen.getMenu().items;
        items.clear();
        for (ItemStack stack : selectedCategory.items) {
            items.add(stack.copy());
        }
        screen.getMenu().scrollTo(0);
    }

    private enum Category {
        FOOD(ModCreativeModeTabs.FOOD_ITEMS,
                () -> new ItemStack(ModItems.GOODBERRY.get()),
                Component.translatable("gui.farmers_spell.filter.food")),
        BLOCKS(ModCreativeModeTabs.BLOCK_ITEMS,
                () -> new ItemStack(ModBlocks.CINDEROUS_STOVE.get()),
                Component.translatable("gui.farmers_spell.filter.blocks")),
        EQUIPMENT(ModCreativeModeTabs.EQUIPMENT_ITEMS,
                () -> new ItemStack(ModItems.HELL_KNIFE.get()),
                Component.translatable("gui.farmers_spell.filter.equipment"));

        private final NonNullList<ItemStack> items;
        private final Supplier<ItemStack> icon;
        private final Component title;

        Category(NonNullList<ItemStack> items, Supplier<ItemStack> icon, Component title) {
            this.items = items;
            this.icon = icon;
            this.title = title;
        }
    }

    private static class FilterButton extends Button {
        private final Category category;

        protected FilterButton(int x, int y, Category category) {
            super(x, y, 32, 26, CommonComponents.EMPTY, button -> select(category), DEFAULT_NARRATION);
            this.category = category;
            this.setTooltip(Tooltip.create(category.title));
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            boolean active = selectedCategory == this.category;
            int textureX = 26;
            int textureY = active ? 32 : 0;
            int textureWidth = active ? 32 : 28;
            int textureHeight = 26;
            RenderSystem.setShaderTexture(0, VANILLA_TABS);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
            this.drawRotatedTexture(graphics.pose().last().pose(), this.getX(), this.getY(), textureX, textureY, textureWidth, textureHeight);
            graphics.renderItem(this.category.icon.get(), this.getX() + 8, this.getY() + 5);
        }

        private void drawRotatedTexture(Matrix4f matrix4f, int x, int y, int textureX, int textureY, int textureWidth, int textureHeight) {
            float scaleX = (float) 1 / 256;
            float scaleY = (float) 1 / 256;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            BufferBuilder builder = Tesselator.getInstance().getBuilder();
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            builder.vertex(matrix4f, x, y + textureHeight, 0).uv((float) (textureX + textureHeight) * scaleX, (float) textureY * scaleY).endVertex();
            builder.vertex(matrix4f, x + textureWidth, y + textureHeight, 0).uv((float) (textureX + textureHeight) * scaleX, ((float) textureY + textureWidth) * scaleY).endVertex();
            builder.vertex(matrix4f, x + textureWidth, y, 0).uv((float) textureX * scaleX, (float) (textureY + textureWidth) * scaleY).endVertex();
            builder.vertex(matrix4f, x, y, 0).uv((float) textureX * scaleX, (float) textureY * scaleY).endVertex();
            BufferUploader.drawWithShader(builder.end());
        }

        @Override
        protected ClientTooltipPositioner createTooltipPositioner() {
            return DefaultTooltipPositioner.INSTANCE;
        }
    }
}
