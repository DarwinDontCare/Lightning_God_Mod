package net.darwindontcare.lighting_god.entities.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.entities.custom.MeteorProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MeteorProjectileRenderer extends GeoEntityRenderer<MeteorProjectile> {
    public MeteorProjectileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MeteorProjectileModel());
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull MeteorProjectile animatable) {
        return new ResourceLocation(LightningGodMod.MOD_ID, "textures/entity/meteor_texture.png");
    }
}
