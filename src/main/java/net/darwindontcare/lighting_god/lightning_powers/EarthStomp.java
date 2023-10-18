package net.darwindontcare.lighting_god.lightning_powers;

import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.ExplodeS2CPacket;
import net.darwindontcare.lighting_god.networking.packet.SetClientCooldownS2CPacket;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class EarthStomp {

    private static final float[] DAMAGE_TABLE = {2, 4, 8, 10};
    public static float DAMAGE = 0;
    public static boolean stomping = false;
    public static void StartStomp(ServerPlayer player, int level) {
        if (!stomping) {
            stomping = true;
            Stomp(player, level);
        }
    }

    public static void StopStomp(ServerPlayer player) {
        stomping = false;
        DAMAGE = 0;
    }

    private static void Stomp(ServerPlayer player, int level) {
        new Thread(() -> {
            try {
                ServerLevel serverLevel = (ServerLevel) player.level;
                float maxDamage = DAMAGE_TABLE[level - 1];
                while (stomping) {
                    if (DAMAGE > maxDamage) {
                        DAMAGE = maxDamage;
                    }
                    if (!player.isOnGround() && DAMAGE < maxDamage && player.getDeltaMovement().y < -1 && DAMAGE < (float) (player.getDeltaMovement().y * -(level / 0.5))) {
                        DAMAGE = (float) (player.getDeltaMovement().y * -(level / 0.5));
                    } if (player.isOnGround() && DAMAGE > 1) {
                        System.out.println("damage: " + DAMAGE + ", level: " + level+", player y velocity: "+ player.getDeltaMovement().y);
                        serverLevel.explode(player, player.position().x, player.position().y, player.position().z, DAMAGE, Level.ExplosionInteraction.NONE);
                        //ModMessage.sendToPlayer(new ExplodeS2CPacket(player.position(), player, DAMAGE), player);

                        DAMAGE = 0;
                        stomping = false;
                    } else if (player.isOnGround()) DAMAGE = 0;
                    if (!player.isOnGround()) {
                        player.resetFallDistance();
                    }
                }
            } catch (Exception e) {System.out.println(e.toString());}
        }).start();
    }
}
