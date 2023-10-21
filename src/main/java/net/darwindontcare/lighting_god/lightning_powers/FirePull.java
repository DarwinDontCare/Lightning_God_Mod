package net.darwindontcare.lighting_god.lightning_powers;

import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.AddForceToEntityS2CPacket;
import net.darwindontcare.lighting_god.networking.packet.SetClientCooldownS2CPacket;
import net.darwindontcare.lighting_god.utils.AddForceToEntity;
import net.darwindontcare.lighting_god.utils.RaycastUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FirePull {
    private static final int RANGE = 15;
    private static final float FORCE = 1f;
    public static void Pull(ServerPlayer player, int cooldown) {
        if (cooldown <= 0) {
            RaycastUtil raycastUtil = new RaycastUtil();
            Vec3 entityPos = raycastUtil.getEntityInCrosshair(1.0f, RANGE).position();
            LivingEntity entity = null;
            List<LivingEntity> hitEntities = player.level.getEntitiesOfClass(
                    LivingEntity.class,
                    new AABB(entityPos.x + 1, entityPos.y + 1, entityPos.z + 1, entityPos.x - 1, entityPos.y -1, entityPos.z - 1)
            );

            for (LivingEntity entity1 : hitEntities) {
                if (!entity1.equals(player) && entity == null) {
                    entity = entity1;
                }
            }
            if (entity != null) {
                LivingEntity finalEntity = entity;
                new Thread(() -> {
                    while (finalEntity.distanceTo(player) > 2f && !finalEntity.isDeadOrDying() && finalEntity.distanceTo(player) < RANGE) {
                        Vec3 direction = new Vec3((player.getForward().x * -1) * FORCE, (player.getForward().y * -1) * FORCE, (player.getForward().z * -1) * FORCE);
                        AddForceToEntity.AddForce(finalEntity, direction, false);
                        System.out.println("entity: " + finalEntity + ", direction: " + direction + ", player forward: " + player.getForward());
                    }
                }).start();
            }
            ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("fire_pull"), player);
        }
    }
}
