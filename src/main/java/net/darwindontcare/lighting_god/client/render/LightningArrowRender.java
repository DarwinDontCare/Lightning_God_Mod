package net.darwindontcare.lighting_god.client.render;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.entities.LightningArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LightningArrowRender extends ArrowRenderer<LightningArrowEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(LightningGodMod.MOD_ID, "textures/entity/lightning_arrow.png");

    public LightningArrowRender(EntityRendererProvider.Context manager) {
        super(manager);
    }

    public ResourceLocation getTextureLocation(LightningArrowEntity arrow) {
        return TEXTURE;
    }
}
