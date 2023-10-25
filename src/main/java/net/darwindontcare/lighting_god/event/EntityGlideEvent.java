package net.darwindontcare.lighting_god.event;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
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
        if (event.getEntity() == LightningGodMod.getPlayer() && LightningGodMod.getAlternativeGliding()) {
            ((Player)event.getEntity()).startFallFlying();
        } else if (event.getEntity() == LightningGodMod.getPlayer() && LightningGodMod.getIsIceSliding() && event.getEntity().isInWater()) {
            event.getEntity().setSwimming(true);
            event.getEntity().setPose(Pose.SWIMMING);
        }

        try {
            event.setCanceled(cancelLivingEntityUpdate.contains(event.getEntity()));
        }catch (Exception e) {}
    }
}
