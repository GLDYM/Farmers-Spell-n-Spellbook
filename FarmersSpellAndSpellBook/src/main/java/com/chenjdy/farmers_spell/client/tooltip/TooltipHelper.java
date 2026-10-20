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

public class TooltipHelper {

    public static final int MAX_WIDTH_PER_LINE = 200;

    public static List<Component> cutStringTextComponent(String s, Style primaryStyle, Style highlightStyle) {
        return cutStringTextComponent(s, primaryStyle, highlightStyle, 0);
    }

    public static List<Component> cutStringTextComponent(String s, Style primaryStyle, Style highlightStyle, int indent) {
        return cutTextComponent(Component.literal(s), primaryStyle, highlightStyle, indent);
    }

    public static List<Component> cutTextComponent(Component c, Style primaryStyle, Style highlightStyle, int indent) {
        String s = c.getString();

        List<String> words = new LinkedList<>();
        BreakIterator iterator = BreakIterator.getLineInstance(Minecraft.getInstance().getLocale());
        iterator.setText(s);
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
            String word = s.substring(start, end);
            words.add(word);
        }

        Font font = Minecraft.getInstance().font;
        List<String> lines = new LinkedList<>();
        StringBuilder currentLine = new StringBuilder();
        int width = 0;
        for (String word : words) {
            int newWidth = font.width(word.replaceAll("_", ""));
            if (width + newWidth > MAX_WIDTH_PER_LINE) {
                if (width > 0) {
                    String line = currentLine.toString();
                    lines.add(line);
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

        boolean currentlyHighlighted = false;
        for (String string : lines) {
            MutableComponent currentComponent = lineStart.plainCopy();
            String[] split = string.split("_", -1);
            for (String part : split) {
                currentComponent.append(Component.literal(part)
                        .withStyle(currentlyHighlighted ? highlightStyle : primaryStyle));
                currentlyHighlighted = !currentlyHighlighted;
            }

            formattedLines.add(currentComponent);
            currentlyHighlighted = !currentlyHighlighted;
        }

        return formattedLines;
    }

}
