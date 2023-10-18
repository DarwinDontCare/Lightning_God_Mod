package net.darwindontcare.lighting_god.client;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.items.WaterArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WaterArmorModel extends GeoModel<WaterArmorItem> {
    @Override
    public ResourceLocation getModelResource(WaterArmorItem animatable) {
        return new ResourceLocation(LightningGodMod.MOD_ID, "geo/water_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WaterArmorItem animatable) {
        return new ResourceLocation(LightningGodMod.MOD_ID, "textures/armor/water_armor_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WaterArmorItem animatable) {
        return new ResourceLocation(LightningGodMod.MOD_ID, "animations/water_armor_idle.json");
    }
}
