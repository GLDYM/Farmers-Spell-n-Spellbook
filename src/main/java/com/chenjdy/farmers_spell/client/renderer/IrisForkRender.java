package com.chenjdy.farmers_spell.client.renderer;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.item.weapons.IrisFork;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class IrisForkRender extends GeoItemRenderer<IrisFork> {
    public IrisForkRender() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "iris_fork")));
    }
}
