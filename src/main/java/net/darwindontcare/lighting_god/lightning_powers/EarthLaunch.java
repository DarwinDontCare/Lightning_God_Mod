package net.darwindontcare.lighting_god.lightning_powers;

import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.AddForceToEntityS2CPacket;
import net.darwindontcare.lighting_god.networking.packet.SetClientCooldownS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EarthLaunch {
    private static final int POWER = 6;
    public static void Launch(ServerPlayer player, int cooldown) {
        if (cooldown <= 0) {
            double motionX = player.getForward().x * POWER;
            double motionY = player.getForward().y * POWER;
            double motionZ = player.getForward().z * POWER;
            ModMessage.sendToPlayer(new AddForceToEntityS2CPacket(new Vec3(motionX, motionY, motionZ), player, false), player);
            player.level.explode(player, player.getX(), player.getY(0.0625D), player.getZ(), 3.0F, Level.ExplosionInteraction.NONE);
            ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("earth_launch"), player);
        }
    }
}
