package com.chenjdy.farmers_spell.creativetab;

import com.chenjdy.farmers_spell.mixins.CreativeModeTabAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.*;
import java.util.stream.Collectors;
import net.minecraft.core.RegistryAccess;

public class FTSInternal {

    public static void applyItems(CreativeModeTab tab) {
        List<ItemStack> stacksToDisplay = new ArrayList<>();

        List<Section<?>> sections = FancyTabSections.getSections(tab);

        if (sections.isEmpty()) return;

        for (Section<?> section : sections) {
            for (int i = 0; i < 9; i++)
                stacksToDisplay.add(ItemStack.EMPTY);

            if (isCollapsed(section))
                continue;

            stacksToDisplay.addAll(section.items().getStacks());

            int usedInLastRow = stacksToDisplay.size() % 9;
            if (usedInLastRow != 0)
                for (int i = 0; i < 9 - usedInLastRow; i++)
                    stacksToDisplay.add(ItemStack.EMPTY);
        }

        ((CreativeModeTabAccessor) tab).setDisplayItems(stacksToDisplay);

        ((CreativeModeTabAccessor) tab).setDisplayItemsSearchTab(
            stacksToDisplay.stream()
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

    @SubscribeEvent
    public static void tagsUpdatedEvent(TagsUpdatedEvent event) {
        refreshAllItems(event.getRegistryAccess());
        BannerRenderer.CURRENT_TAB = null;
    }

    public static void refreshAllItems(RegistryAccess registryAccess) {
        FancyTabSections.REGISTERED_TABS.forEach((rl, sections) -> {
            for (Section<?> section : sections) {
                section.items().resolveStacks(registryAccess);
            }
        });
    }

    public static int getRowForSection(Section<?> section) {
        for (List<Section<?>> list : FancyTabSections.REGISTERED_TABS.values()) {
            if (list.contains(section)) {
                int currentRow = 0;
                for (Section<?> sectionBeingChecked : list) {
                    if (sectionBeingChecked == section) {
                        int contentRows = isCollapsed(sectionBeingChecked) ? 0 : (sectionBeingChecked.items().getStacks().size() - 1) / 9 + 1;

                        int sectionEnd = currentRow + contentRows;

                        if (sectionBeingChecked instanceof StickySection sticky
                            && sticky.isSticky()
                            && BannerRenderer.CURRENT_ROW >= currentRow
                            && BannerRenderer.CURRENT_ROW <= sectionEnd)
                            return BannerRenderer.CURRENT_ROW;

                        return currentRow;
                    }

                    currentRow++;

                    if (!isCollapsed(sectionBeingChecked))
                        currentRow += (sectionBeingChecked.items().getStacks().size() - 1) / 9 + 1;
                }
            }
        }
        return -1;
    }

    public static final Set<Section<?>> COLLAPSED = new HashSet<>();

    public static boolean isCollapsed(Section<?> section) {
        return COLLAPSED.stream().anyMatch(o -> o.equals(section));
    }

    public static void toggle(Section<?> section) {
        if (!FTSInternal.isCollapsed(section))
            collapse(section, true);
        else
            expand(section, true);
    }

    public static void collapse(Section<?> section, boolean playSound) {
        FTSInternal.COLLAPSED.add(section);

        if (Minecraft.getInstance().getConnection() != null && playSound)
            Client.playSound(false);
    }

    public static void expand(Section<?> section, boolean playSound) {
        FTSInternal.COLLAPSED.removeIf(o -> o.equals(section));

        if (Minecraft.getInstance().getConnection() != null && playSound)
            Client.playSound(true);
    }

    public static boolean isBannerRow(ResourceLocation tab, int row) {
        List<Section<?>> sections = FancyTabSections.REGISTERED_TABS.get(tab);
        if (sections == null) return false;

        for (Section<?> section : sections) {
            if (getRowForSection(section) == row) return true;
        }
        return false;
    }

    public static class Client {
        public static void playSound(boolean b) {
            if (b)
                Minecraft.getInstance().getSoundManager().play(
                    SimpleSoundInstance.forUI(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 1f, 1F));
            else
                Minecraft.getInstance().getSoundManager().play(
                    SimpleSoundInstance.forUI(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_OFF, 1f, 1F));
        }
    }
}
