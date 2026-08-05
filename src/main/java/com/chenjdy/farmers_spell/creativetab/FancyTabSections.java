package com.chenjdy.farmers_spell.creativetab;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FancyTabSections {

    public static final String MOD_ID = FARMERSSPELL.MODID;
    public static final Map<ResourceLocation, List<Section<?>>> REGISTERED_TABS = new HashMap<>();

    public FancyTabSections() {
    }

    public static void addSection(ResourceLocation tab, Section section) {
        REGISTERED_TABS.computeIfAbsent(tab, k -> new ArrayList<>()).add(section);
    }

    public static List<Section<?>> getSections(CreativeModeTab tab) {
        ResourceLocation rl = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab);
        return REGISTERED_TABS.getOrDefault(rl, new ArrayList<>());
    }

    public static Section<?> getSection(ResourceLocation id) {
        for (List<Section<?>> entry : REGISTERED_TABS.values())
            if (entry != null)
                for (Section<?> section : entry)
                    if (section.id().equals(id))
                        return section;
        return null;
    }
}
