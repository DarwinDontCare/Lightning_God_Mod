package net.darwindontcare.lighting_god.weapon_powers;

import net.darwindontcare.lighting_god.items.FireScyth;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.AddForceToEntityS2CPacket;
import net.darwindontcare.lighting_god.utils.AddForceToEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class FireScythDash {
    private static final int RANGE = 3;
    private static final int DAMAGE = 8;
    public static void Dash(ServerPlayer player) {
        try {
            Vec3 direction = new Vec3(player.getForward().x * RANGE, player.getForward().y * RANGE, player.getForward().z * RANGE);
            ModMessage.sendToPlayer(new AddForceToEntityS2CPacket(direction, player, false), player);
            List<LivingEntity> hitEntities = new ArrayList<>();

            for (int i = 1; i < 5; i++) {
                Vec3 currentPos = player.position().add(new Vec3(player.getForward().x * i, player.getForward().y * i, player.getForward().z * i));
                List<LivingEntity> entities = player.level.getEntitiesOfClass(
                        LivingEntity.class,
                        new AABB(currentPos.x + 1, currentPos.y + 1, currentPos.z + 1, currentPos.x - 1, currentPos.y -1, currentPos.z - 1)
                );

                hitEntities.addAll(entities);
            }

            for (LivingEntity entity : hitEntities) {
                if (!entity.equals(player)) {
                    entity.setSecondsOnFire(3);
                    entity.hurt(player.damageSources().playerAttack(player), DAMAGE);
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
