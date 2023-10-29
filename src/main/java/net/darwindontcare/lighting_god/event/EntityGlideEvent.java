package net.darwindontcare.lighting_god.event;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.security.DrbgParameters;
import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = LightningGodMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityGlideEvent {
    public static ArrayList<LivingEntity> cancelLivingEntityUpdate = new ArrayList<>();
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity == LightningGodMod.getPlayer() && LightningGodMod.getAlternativeGliding()) {
            EntityDimensions newDims = entity.getDimensions(entity.getPose()).scale(1.0F, 1.0F);
            try {
                System.out.println("changing bounding box");
                Field field = Entity.class.getDeclaredField("dimensions");
                field.setAccessible(true);
                field.set(entity, newDims);
                EntityDimensions newEntityDimensions = (EntityDimensions) field.get(entity);
                entity.setBoundingBox(newEntityDimensions.makeBoundingBox(
                        entity.getX(),
                        entity.getY(),
                        entity.getZ()
                ));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        try {
            event.setCanceled(cancelLivingEntityUpdate.contains(entity));
        }catch (Exception e) {}
    }
    
    @SubscribeEvent
    public static void onEntityFallFly(PlayerFlyableFallEvent event) {

    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {

    }
    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Post event) {

    }
}
