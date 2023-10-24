package net.darwindontcare.lighting_god.items.client;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.entities.custom.MeteorProjectile;
import net.darwindontcare.lighting_god.items.FireScyth;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FireScythModel  extends GeoModel<FireScyth> {
    @Override
    public ResourceLocation getModelResource(FireScyth animatable) {
        return new ResourceLocation(LightningGodMod.MOD_ID, "geo/fire_scyth.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FireScyth animatable) {
        return new ResourceLocation(LightningGodMod.MOD_ID, "textures/item/fire_scyth_3d.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FireScyth animatable) {
        return new ResourceLocation(LightningGodMod.MOD_ID, "animations/fire_scyth_idle.json");
    }
}
