package com.chenjdy.farmers_spell.client.renderer;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.item.weapons.CherrySpoon;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class CherrySpoonRender extends GeoItemRenderer<CherrySpoon> {
    public CherrySpoonRender() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "cherry_spoon")));
    }
}
