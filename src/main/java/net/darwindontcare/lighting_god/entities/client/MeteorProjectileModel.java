package net.darwindontcare.lighting_god.entities.client;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.entities.custom.MeteorProjectile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MeteorProjectileModel extends GeoModel<MeteorProjectile> {
    @Override
    public ResourceLocation getModelResource(MeteorProjectile animatable) {
        return new ResourceLocation(LightningGodMod.MOD_ID, "geo/meteor_model.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MeteorProjectile animatable) {
        return new ResourceLocation(LightningGodMod.MOD_ID, "textures/entity/meteor_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MeteorProjectile animatable) {
        return new ResourceLocation(LightningGodMod.MOD_ID, "animations/meteor_model.animation.json");
    }
}
