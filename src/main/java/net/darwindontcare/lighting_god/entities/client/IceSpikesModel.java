package net.darwindontcare.lighting_god.entities.client;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.entities.custom.IceSpikes;
import net.darwindontcare.lighting_god.entities.custom.MeteorProjectile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class IceSpikesModel extends GeoModel<IceSpikes> {
    @Override
    public ResourceLocation getModelResource(IceSpikes animatable) {
        return new ResourceLocation(LightningGodMod.MOD_ID, "geo/ice_spikes.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(IceSpikes animatable) {
        return new ResourceLocation(LightningGodMod.MOD_ID, "textures/entity/ice_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(IceSpikes animatable) {
        return new ResourceLocation(LightningGodMod.MOD_ID, "animations/grow_ice_spike.json");
    }
}
