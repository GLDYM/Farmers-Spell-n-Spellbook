package com.chenjdy.farmers_spell.mixins.client;

import com.chenjdy.farmers_spell.creativetab.BannerRenderer;
import com.chenjdy.farmers_spell.creativetab.FTSInternal;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen$CustomCreativeSlot")
public abstract class CustomCreativeSlotMixin extends Slot {
    public CustomCreativeSlotMixin() {
        super(null, 0, 0, 0);
    }

    @Override
    public boolean isActive() {
        int absoluteRow = BannerRenderer.CURRENT_ROW + this.index / 9;
        return !FTSInternal.isBannerRow(BannerRenderer.CURRENT_TAB, absoluteRow);
    }

    @Override
    public boolean isHighlightable() {
        int absoluteRow = BannerRenderer.CURRENT_ROW + this.index / 9;
        if (BannerRenderer.CURRENT_TAB != null && FTSInternal.isBannerRow(BannerRenderer.CURRENT_TAB, absoluteRow)) {
            return false;
        }
        return true;
    }
}
