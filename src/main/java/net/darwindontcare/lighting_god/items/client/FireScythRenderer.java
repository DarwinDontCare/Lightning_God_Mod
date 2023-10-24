package net.darwindontcare.lighting_god.items.client;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.items.FireScyth;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class FireScythRenderer  extends GeoItemRenderer<FireScyth> {
    public FireScythRenderer() {
        super(new FireScythModel());
    }
}
