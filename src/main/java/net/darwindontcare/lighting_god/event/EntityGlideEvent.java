package net.darwindontcare.lighting_god.event;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = LightningGodMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityGlideEvent {
    public static ArrayList<LivingEntity> cancelLivingEntityUpdate = new ArrayList<>();
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().isFallFlying()) {
            System.out.println("is elytra flying");
        }

        try {
            event.setCanceled(cancelLivingEntityUpdate.contains(event.getEntity()));
        }catch (Exception e) {}
    }
    @SubscribeEvent
    public static void onEntityFallFly(PlayerFlyableFallEvent event) {
        //System.out.println(event.getEntity().level().isClientSide);
//        if (LightningGodMod.getAlternativeGliding()) {
//            System.out.println("is elytra flying");
//            event.setResult(Event.Result.ALLOW);
//        }
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {

    }
}
