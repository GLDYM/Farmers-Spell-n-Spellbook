package com.bmt.kaleidoscope_chinesefood.init;

import com.bmt.kaleidoscope_chinesefood.KaleidoscopeChineseFood;
import com.bmt.kaleidoscope_chinesefood.menu.FreezerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, KaleidoscopeChineseFood.MODID);
    
    public static final RegistryObject<MenuType<FreezerMenu>> FREEZER_MENU =
            MENUS.register("freezer_menu",
                    () -> IForgeMenuType.create(FreezerMenu::new));
    
    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}