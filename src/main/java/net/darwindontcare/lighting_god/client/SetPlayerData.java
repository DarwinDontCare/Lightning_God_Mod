package net.darwindontcare.lighting_god.client;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;

public class SetPlayerData {
    public static void setInt(LivingEntity entity, String property, int value) {
        CompoundTag currentData = entity.getPersistentData();
        currentData.putInt(property, value);
        entity.addAdditionalSaveData(currentData);
    }

    public static void setFloat(LivingEntity entity, String property, float value) {
        CompoundTag currentData = entity.getPersistentData();
        currentData.putFloat(property, value);
        entity.addAdditionalSaveData(currentData);
    }

    public static void setDouble(LivingEntity entity, String property, double value) {
        CompoundTag currentData = entity.getPersistentData();
        currentData.putDouble(property, value);
        entity.addAdditionalSaveData(currentData);
    }

    public static void setString(LivingEntity entity, String property, String value) {
        try {
            CompoundTag currentData = entity.getPersistentData();
            currentData.putString(property, value);

            entity.addAdditionalSaveData(currentData);
        } catch (Exception exception) {System.out.println(exception.toString());}
    }
}
