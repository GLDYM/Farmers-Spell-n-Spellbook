package com.bmt.kaleidoscope_chinesefood;

import com.bmt.kaleidoscope_chinesefood.init.ModCreativeModeTabs;
import com.bmt.kaleidoscope_chinesefood.init.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(KaleidoscopeChineseFood.MODID)
public class KaleidoscopeChineseFood
{
    public static final String MODID = "kaleidoscope_chinesefood";
    private static final Logger LOGGER = LogUtils.getLogger();
    public KaleidoscopeChineseFood(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

    }

}
