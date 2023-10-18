package net.darwindontcare.lighting_god.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class SetEntityPos {
    public static void SetPos(Entity entity, Vec3 position, float rotationY, float rotationX) {
        entity.lerpMotion(position.x, position.y, position.z);
        entity.setYRot(rotationY);
        entity.setXRot(rotationX);
    }
}
