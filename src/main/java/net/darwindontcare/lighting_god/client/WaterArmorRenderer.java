package net.darwindontcare.lighting_god.client;

import net.darwindontcare.lighting_god.items.WaterArmorItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class WaterArmorRenderer extends GeoArmorRenderer<WaterArmorItem> {
    public WaterArmorRenderer() {
        super(new WaterArmorModel());
    }
}
