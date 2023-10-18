package net.darwindontcare.lighting_god.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class AddForceToEntity {
    public static void AddForce(Entity entity, Vec3 direction, boolean horizontalMovementOnly) {
        try {
            if (!horizontalMovementOnly) entity.setDeltaMovement(direction.x, direction.y, direction.z);
            else entity.setDeltaMovement(direction.x, entity.getDeltaMovement().y, direction.z);
        } catch (Exception exception) {System.out.println(exception.toString());}
    }
}
