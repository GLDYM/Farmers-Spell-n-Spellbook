package com.chenjdy.farmers_spell.mixins.creativetab;

import com.chenjdy.farmers_spell.creativetab.BannerRenderer;
import com.chenjdy.farmers_spell.creativetab.FTSInternal;
import com.chenjdy.farmers_spell.creativetab.FancyTabSections;
import com.chenjdy.farmers_spell.creativetab.Section;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin {
    @Shadow
    private static CreativeModeTab selectedTab;

    @Shadow
    protected abstract void refreshCurrentTabContents(Collection<ItemStack> items);

    @Inject(method = "renderBg", at = @At("TAIL"))
    private void fts$renderBanners(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        ResourceLocation tab = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(selectedTab);

        if (BannerRenderer.CURRENT_TAB == null || !BannerRenderer.CURRENT_TAB.equals(tab)) {
            BannerRenderer.CURRENT_TAB = tab;
            FTSInternal.applyItems(selectedTab);
            this.refreshCurrentTabContents(selectedTab.getDisplayItems());
        }

        if (FancyTabSections.REGISTERED_TABS.containsKey(tab)) {
            BannerRenderer.render((CreativeModeInventoryScreen) (Object) this, guiGraphics,
                FancyTabSections.REGISTERED_TABS.get(tab), mouseX, mouseY);
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void fts$slotClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        CreativeModeInventoryScreen self = (CreativeModeInventoryScreen) (Object) this;

        ResourceLocation tab = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(selectedTab);
        if (!FancyTabSections.REGISTERED_TABS.containsKey(tab)) return;

        if (BannerRenderer.isInBanner(self, mouseX, mouseY))
            if (!self.getMenu().getCarried().isEmpty()) {
                self.getMenu().setCarried(ItemStack.EMPTY);
                cir.cancel();
            }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void fts$mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (button != 0) return;

        ResourceLocation tab = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(selectedTab);
        if (!FancyTabSections.REGISTERED_TABS.containsKey(tab)) return;

        CreativeModeInventoryScreen self = (CreativeModeInventoryScreen) (Object) this;

        if (!self.getMenu().getCarried().isEmpty()) return;

        List<Section<?>> sections = FancyTabSections.REGISTERED_TABS.get(tab);
        for (Section<?> section : sections) {
            if (section.collapsible() && BannerRenderer.isInToggle(self, section, mouseX, mouseY)) {
                if (Screen.hasShiftDown()) {
                    if (FTSInternal.isCollapsed(section))
                        sections.stream().filter(Section::collapsible).forEach(o -> FTSInternal.expand(o, o.equals(section)));
                    else
                        sections.stream().filter(Section::collapsible).forEach(o -> FTSInternal.collapse(o, o.equals(section)));
                } else {
                    FTSInternal.toggle(section);
                }

                FTSInternal.applyItems(selectedTab);
                this.refreshCurrentTabContents(selectedTab.getDisplayItems());

                cir.setReturnValue(true);
                return;
            }
        }
    }
}
