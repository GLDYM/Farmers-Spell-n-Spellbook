package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.block.entity.container.AlchemistPotMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, FARMERSSPELL.MODID);

    public static final RegistryObject<MenuType<AlchemistPotMenu>> ALCHEMIST_POT =
            MENUS.register("alchemist_pot",
                    () -> IForgeMenuType.create(AlchemistPotMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}