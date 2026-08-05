package com.chenjdy.farmers_spell.creativetab;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractSectionWithTitle<T extends AbstractSectionWithTitle<T>> implements Section<T>, StickySection {
    final ResourceLocation id;
    public Component title = Component.empty();
    public int titleOffsetX = 5;
    public int titleOffsetY = 5;
    public int textColor = 0xFFFFFFFF;
    public int textOutline = 0x00000000;
    public boolean textShadow = true;
    boolean centered = false;
    boolean collapsible = true;
    boolean sticky = true;
    Runnable onRender = () -> {};
    ConglomerateOfItems items = ConglomerateOfItems.create();

    public AbstractSectionWithTitle(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public void render(GuiGraphics guiGraphics, Font font, int topLeftX, int topLeftY) {
        onRender.run();
        renderTitle(guiGraphics, font, topLeftX, topLeftY);
    }

    public void renderTitle(GuiGraphics guiGraphics, Font font, int topLeftX, int topLeftY) {
        topLeftX += titleOffsetX;
        topLeftY += titleOffsetY;

        if (centered) {
            if (textOutline != 0x00000000) {
                guiGraphics.drawCenteredString(font, title, topLeftX + 70, topLeftY - 1, textOutline);
                guiGraphics.drawCenteredString(font, title, topLeftX + 70, topLeftY + 1, textOutline);
                guiGraphics.drawCenteredString(font, title, topLeftX + 71, topLeftY, textOutline);
                guiGraphics.drawCenteredString(font, title, topLeftX + 69, topLeftY, textOutline);
            }
            guiGraphics.drawCenteredString(font, title, topLeftX + 70, topLeftY, textColor);
        } else {
            guiGraphics.drawString(font, title, topLeftX, topLeftY, textColor, textShadow);
        }
    }

    @SuppressWarnings("unchecked")
    public T setTitle(Component component) {
        this.title = component;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setTitle(String title) {
        this.title = Component.literal(title);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setTitleOffset(int x, int y) {
        this.titleOffsetX = x;
        this.titleOffsetY = y;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setCentered(boolean centered) {
        this.centered = centered;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setTextColor(int textColor) {
        this.textColor = textColor;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setTextOutline(int textOutline) {
        this.textOutline = textOutline;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setTextShadow(boolean textShadow) {
        this.textShadow = textShadow;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setCollapsible(boolean collapsible) {
        this.collapsible = collapsible;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setItems(ConglomerateOfItems items) {
        this.items = items;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setSticky(boolean sticky) {
        this.sticky = sticky;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setOnRender(Runnable onRender) {
        this.onRender = onRender;
        return (T) this;
    }

    @Override
    public ResourceLocation id() {
        return id;
    }

    @Override
    public ConglomerateOfItems items() {
        return items;
    }

    @Override
    public boolean collapsible() {
        return collapsible;
    }

    @Override
    public boolean isSticky() {
        return sticky;
    }
}
