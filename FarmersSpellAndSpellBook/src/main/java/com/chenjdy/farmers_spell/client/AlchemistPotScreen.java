package com.chenjdy.farmers_spell.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.chenjdy.farmers_spell.block.entity.container.AlchemistPotMenu;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class AlchemistPotScreen extends AbstractContainerScreen<AlchemistPotMenu>
{
    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath("farmers_spell", "textures/gui/alchemist_pot.png");
    private static final Rectangle HEAT_ICON = new Rectangle(47, 55, 17, 15);
    private static final Rectangle PROGRESS_ARROW = new Rectangle(89, 25, 0, 17);

    public AlchemistPotScreen(AlchemistPotMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.titleLabelX = 28;
        this.inventoryLabelY = this.imageHeight - 96 + 2;
    }

    @Override
    public void render(GuiGraphics gui, final int mouseX, final int mouseY, float partialTicks) {
        this.renderBackground(gui);
        super.render(gui, mouseX, mouseY, partialTicks);
        this.renderMealDisplayTooltip(gui, mouseX, mouseY);
        this.renderHeatIndicatorTooltip(gui, mouseX, mouseY);
    }

    private void renderHeatIndicatorTooltip(GuiGraphics gui, int mouseX, int mouseY) {
        if (this.isHovering(HEAT_ICON.x, HEAT_ICON.y, HEAT_ICON.width, HEAT_ICON.height, mouseX, mouseY)) {
            String key = "farmersdelight.cooking_pot." + (this.menu.isHeated() ? "heated" : "not_heated");
            gui.renderTooltip(this.font, Component.translatable(key), mouseX, mouseY);
        }
    }

    protected void renderMealDisplayTooltip(GuiGraphics gui, int mouseX, int mouseY) {
        if (this.minecraft != null && this.minecraft.player != null && this.menu.getCarried().isEmpty()
                && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            if (this.hoveredSlot.index == 6) {
                List<Component> tooltip = new ArrayList<>();

                ItemStack mealStack = this.hoveredSlot.getItem();
                tooltip.add(((MutableComponent) mealStack.getItem().getDescription()).withStyle(mealStack.getRarity().getStyleModifier()));

                ItemStack containerStack = this.menu.blockEntity.getContainer();
                if (!containerStack.isEmpty()) {
                    String container = containerStack.getItem().getDescription().getString();
                    tooltip.add(Component.translatable("farmersdelight.cooking_pot.served_on", container).withStyle(ChatFormatting.GRAY));
                }

                gui.renderComponentTooltip(font, tooltip, mouseX, mouseY);
            } else {
                gui.renderTooltip(font, this.hoveredSlot.getItem(), mouseX, mouseY);
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics gui, int mouseX, int mouseY) {
        super.renderLabels(gui, mouseX, mouseY);
        gui.drawString(this.font, this.playerInventoryTitle, 8, this.inventoryLabelY, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics gui, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.minecraft == null)
            return;

        gui.blit(BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (this.menu.isHeated()) {
            gui.blit(BACKGROUND_TEXTURE, this.leftPos + HEAT_ICON.x, this.topPos + HEAT_ICON.y, 176, 0, HEAT_ICON.width, HEAT_ICON.height);
        }

        int l = this.menu.getCookProgressionScaled();
        gui.blit(BACKGROUND_TEXTURE, this.leftPos + PROGRESS_ARROW.x, this.topPos + PROGRESS_ARROW.y, 176, 15, l + 1, PROGRESS_ARROW.height);
    }
}
