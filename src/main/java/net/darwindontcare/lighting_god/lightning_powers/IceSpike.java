package net.darwindontcare.lighting_god.lightning_powers;

import net.darwindontcare.lighting_god.entities.custom.IceSpikes;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.SetClientCooldownS2CPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class IceSpike {
    private static final int ManaCost = 70;
    public static void Spike(ServerPlayer player, int cooldown, float mana) {
        if (cooldown <= 0 && mana > ManaCost) {
            try {
                ServerLevel serverLevel = (ServerLevel) player.level();
                Vec3 position = player.position().add(player.getForward());
                IceSpikes iceSpikes = new IceSpikes(player.level(), position.x, position.y, position.z, player.getYRot(), 0, player);

                serverLevel.addFreshEntity(iceSpikes);
                ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("ice_spike", ManaCost), player);
            } catch (Exception e) {System.out.println(e.toString());}
        }
    }
}
