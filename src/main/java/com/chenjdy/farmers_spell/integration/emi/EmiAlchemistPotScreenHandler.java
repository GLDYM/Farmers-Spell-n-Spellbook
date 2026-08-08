package com.chenjdy.farmers_spell.integration.emi;

import com.chenjdy.farmers_spell.client.AlchemistPotScreen;
import com.chenjdy.farmers_spell.init.ModBlocks;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;

public final class EmiAlchemistPotScreenHandler {
    private static final int USES_AREA_X = 89;
    private static final int USES_AREA_Y = 25;
    private static final int USES_AREA_WIDTH = 24;
    private static final int USES_AREA_HEIGHT = 17;
    private static boolean registered;

    private EmiAlchemistPotScreenHandler() {
    }

    public static void register() {
        if (registered) {
            return;
        }
        registered = true;
        NeoForge.EVENT_BUS.addListener(EmiAlchemistPotScreenHandler::onRender);
        NeoForge.EVENT_BUS.addListener(EmiAlchemistPotScreenHandler::onMouseButtonPressed);
    }

    private static void onRender(ScreenEvent.Render.Post event) {
        if (!(event.getScreen() instanceof AlchemistPotScreen screen)
                || !isInUsesArea(screen, event.getMouseX(), event.getMouseY())) {
            return;
        }
        event.getGuiGraphics().renderTooltip(Minecraft.getInstance().font,
                Component.translatable("emi.category.farmers_spell.show_uses"), event.getMouseX(), event.getMouseY());
    }

    private static void onMouseButtonPressed(ScreenEvent.MouseButtonPressed.Pre event) {
        if (event.getButton() != 0 || !(event.getScreen() instanceof AlchemistPotScreen screen)
                || !isInUsesArea(screen, event.getMouseX(), event.getMouseY())) {
            return;
        }
        EmiApi.displayUses(EmiStack.of(ModBlocks.ALCHEMIST_POT.get()));
        event.setCanceled(true);
    }

    private static boolean isInUsesArea(AlchemistPotScreen screen, double mouseX, double mouseY) {
        double relativeX = mouseX - screen.getGuiLeft();
        double relativeY = mouseY - screen.getGuiTop();
        return relativeX >= USES_AREA_X && relativeX < USES_AREA_X + USES_AREA_WIDTH
                && relativeY >= USES_AREA_Y && relativeY < USES_AREA_Y + USES_AREA_HEIGHT;
    }
}
