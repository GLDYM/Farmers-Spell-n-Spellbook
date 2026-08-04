package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.block.entity.container.AlchemistPotMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, FarmersSpell.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<AlchemistPotMenu>> ALCHEMIST_POT =
            MENUS.register("alchemist_pot",
                    () -> IMenuTypeExtension.create(AlchemistPotMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}