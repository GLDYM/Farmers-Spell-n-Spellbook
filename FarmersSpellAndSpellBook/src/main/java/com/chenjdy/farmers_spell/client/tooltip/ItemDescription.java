package com.chenjdy.farmers_spell.client.tooltip;

import static net.minecraft.ChatFormatting.DARK_GRAY;
import static net.minecraft.ChatFormatting.GRAY;
import static net.minecraft.ChatFormatting.WHITE;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.init.ModBlocks;
import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FARMERSSPELL.MODID, value = Dist.CLIENT)
public record ItemDescription(ImmutableList<Component> lines, ImmutableList<Component> linesOnShift) {

    private static final Style PRIMARY = Style.EMPTY.applyFormat(ChatFormatting.GRAY);
    private static final Style HIGHLIGHT = Style.EMPTY.applyFormat(ChatFormatting.WHITE);
    private static final String HOLD_KEY = FARMERSSPELL.MODID + ".tooltip.holdForDescription";
    private static final String SHIFT_KEY = FARMERSSPELL.MODID + ".tooltip.keyShift";

    private static final Map<Item, ItemDescription> DESCRIPTIONS = new IdentityHashMap<>();
    private static String cachedLanguage;

    @Nullable
    public static ItemDescription create(Item item) {
        String translationKey = item.getDescriptionId() + ".tooltip";
        if (!I18n.exists(translationKey + ".line1")) {
            return null;
        }

        List<Component> linesOnShift = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            String lineKey = translationKey + ".line" + i;
            if (!I18n.exists(lineKey)) {
                break;
            }
            linesOnShift.addAll(TooltipHelper.cutStringTextComponent(I18n.get(lineKey), PRIMARY, HIGHLIGHT));
        }

        List<Component> lines = new ArrayList<>();
        lines.add(holdDescriptionHint(false));
        linesOnShift.add(0, holdDescriptionHint(true));
        linesOnShift.add(1, CommonComponents.EMPTY);

        return new ItemDescription(ImmutableList.copyOf(lines), ImmutableList.copyOf(linesOnShift));
    }

    private static MutableComponent holdDescriptionHint(boolean shiftHeld) {
        MutableComponent keyShift = Component.translatable(SHIFT_KEY)
                .withStyle(shiftHeld ? WHITE : GRAY);
        return Component.translatable(HOLD_KEY, keyShift)
                .withStyle(DARK_GRAY);
    }

    public ImmutableList<Component> getCurrentLines() {
        return Screen.hasShiftDown() ? linesOnShift : lines;
    }

    private static Set<Item> tooltipItems() {
        return Set.of(
                ModBlocks.ALCHEMIST_POT.get().asItem(),
                ModBlocks.CINDEROUS_STOVE.get().asItem());
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        if (!tooltipItems().contains(item)) {
            return;
        }
        checkLocale();
        ItemDescription description = DESCRIPTIONS.get(item);
        if (description == null) {
            return;
        }
        event.getToolTip().addAll(1, description.getCurrentLines());
    }

    private static void checkLocale() {
        String currentLanguage = Minecraft.getInstance().getLanguageManager().getSelected();
        if (currentLanguage.equals(cachedLanguage)) {
            return;
        }
        cachedLanguage = currentLanguage;
        DESCRIPTIONS.clear();
        for (Item item : tooltipItems()) {
            ItemDescription description = create(item);
            if (description != null) {
                DESCRIPTIONS.put(item, description);
            }
        }
    }

}
