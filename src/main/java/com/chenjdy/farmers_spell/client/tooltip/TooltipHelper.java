package com.chenjdy.farmers_spell.client.tooltip;

import com.google.common.base.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class TooltipHelper {
    public static final int MAX_WIDTH_PER_LINE = 200;

    private TooltipHelper() {
    }

    public static List<Component> cutStringTextComponent(String text, Style primaryStyle,
                                                        Style highlightStyle) {
        return cutStringTextComponent(text, primaryStyle, highlightStyle, 0);
    }

    public static List<Component> cutStringTextComponent(String text, Style primaryStyle,
                                                        Style highlightStyle, int indent) {
        return cutTextComponent(Component.literal(text), primaryStyle, highlightStyle, indent);
    }

    public static List<Component> cutTextComponent(Component component, Style primaryStyle,
                                                   Style highlightStyle, int indent) {
        String text = component.getString();
        List<String> words = new LinkedList<>();
        BreakIterator iterator = BreakIterator.getLineInstance(Minecraft.getInstance().getLocale());
        iterator.setText(text);
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
            words.add(text.substring(start, end));
        }

        Font font = Minecraft.getInstance().font;
        List<String> lines = new LinkedList<>();
        StringBuilder currentLine = new StringBuilder();
        int width = 0;
        for (String word : words) {
            int newWidth = font.width(word.replaceAll("_", ""));
            if (width + newWidth > MAX_WIDTH_PER_LINE) {
                if (width > 0) {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder();
                    width = 0;
                } else {
                    lines.add(word);
                    continue;
                }
            }
            currentLine.append(word);
            width += newWidth;
        }
        if (width > 0) {
            lines.add(currentLine.toString());
        }

        MutableComponent lineStart = Component.literal(Strings.repeat(" ", indent));
        lineStart.withStyle(primaryStyle);
        List<Component> formattedLines = new ArrayList<>(lines.size());
        boolean highlighted = false;
        for (String line : lines) {
            MutableComponent current = lineStart.plainCopy();
            for (String part : line.split("_", -1)) {
                current.append(Component.literal(part)
                        .withStyle(highlighted ? highlightStyle : primaryStyle));
                highlighted = !highlighted;
            }
            formattedLines.add(current);
            highlighted = !highlighted;
        }
        return formattedLines;
    }
}
