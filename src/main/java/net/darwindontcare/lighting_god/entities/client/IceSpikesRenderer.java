package net.darwindontcare.lighting_god.entities.client;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.entities.custom.IceSpikes;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class IceSpikesRenderer  extends GeoEntityRenderer<IceSpikes> {
    public IceSpikesRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new IceSpikesModel());
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull IceSpikes animatable) {
        return new ResourceLocation(LightningGodMod.MOD_ID, "textures/entity/ice_texture.png");
    }
}
