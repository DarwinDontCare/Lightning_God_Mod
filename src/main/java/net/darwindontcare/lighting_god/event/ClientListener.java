package net.darwindontcare.lighting_god.event;

import net.darwindontcare.lighting_god.client.render.LightningArrowRender;
import net.darwindontcare.lighting_god.entities.EntityInit;
import net.darwindontcare.lighting_god.entities.client.MeteorProjectileRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.GeckoLib;

@Mod.EventBusSubscriber(modid = GeckoLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientListener {

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.EARTH_METEOR.get(), MeteorProjectileRenderer::new);
        event.registerEntityRenderer(EntityInit.LIGHTNING_ARROW.get(), LightningArrowRender::new);
    }
}
