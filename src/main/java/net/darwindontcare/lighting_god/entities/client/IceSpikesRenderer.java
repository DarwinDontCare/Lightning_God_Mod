package net.darwindontcare.lighting_god.entities.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.entities.custom.IceSpikes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class IceSpikesRenderer  extends GeoEntityRenderer<IceSpikes> {
    public IceSpikesRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new IceSpikesModel());
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull IceSpikes animatable) {
        return new ResourceLocation(LightningGodMod.MOD_ID, "textures/entity/ice_texture.png");
    }

    @Override
    public void actuallyRender(PoseStack poseStack, IceSpikes animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        try {
            if (animatable != null) {
                poseStack.mulPose(Axis.YP.rotationDegrees(Mth.wrapDegrees(animatable.getYRot() + 180) * -1));
                poseStack.mulPose(Axis.XP.rotationDegrees(-animatable.getXRot()));
                super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            }
        } catch (Exception e) {System.out.println(e.toString());}
    }
}
