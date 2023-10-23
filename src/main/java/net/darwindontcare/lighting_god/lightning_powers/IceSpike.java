package net.darwindontcare.lighting_god.lightning_powers;

import net.darwindontcare.lighting_god.entities.custom.IceSpikes;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.SetClientCooldownS2CPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class IceSpike {
    public static void Spike(ServerPlayer player, int cooldown) {
        if (cooldown <= 0) {
            ServerLevel serverLevel = (ServerLevel) player.level();
            IceSpikes iceSpikes = new IceSpikes(player.level(), 0, 0, 0, player.getXRot(), 0, player);
            iceSpikes.setPos(player.position().add(player.getForward()));

            serverLevel.addFreshEntity(iceSpikes);
            ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("ice_spike"), player);
        }
    }
}
