package net.darwindontcare.lighting_god.lightning_powers;

import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.ExplodeC2SPacket;
import net.darwindontcare.lighting_god.networking.packet.ExplodeS2CPacket;
import net.darwindontcare.lighting_god.networking.packet.SetClientCooldownS2CPacket;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.ArrayList;

public class EarthStomp {

    private static final float[] DAMAGE_TABLE = {2, 4, 8, 10};
    public static ArrayList<Float> DAMAGE = new ArrayList<>();
    public static ArrayList<Boolean> StartedStomp = new ArrayList<>();
    private static final ArrayList<Integer> playerList = new ArrayList<>();

    public static void StartStomp(Player player, int level) {
        if (!playerList.contains(player.getId()) && !player.onGround()) {
            DAMAGE.add(0f);
            StartedStomp.add(false);
            playerList.add(player.getId());
        }
        Stomp(player, level);
    }

    private static void Stomp(Player player, int level) {
        float maxDamage = DAMAGE_TABLE[level - 1];
        int playerIdx = 0;
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i) == player.getId()) {
                playerIdx = i;
                break;
            }
        }
        if (!StartedStomp.get(playerIdx)) {
            StartedStomp.set(playerIdx, true);
            int finalPlayerIdx = playerIdx;
            new Thread(() -> {
                try {
                    while (true) {
                        if (DAMAGE.get(finalPlayerIdx) > maxDamage) {
                            DAMAGE.set(finalPlayerIdx, maxDamage);
                        }
                        if (!player.onGround() && DAMAGE.get(finalPlayerIdx) < maxDamage && player.getDeltaMovement().y < -1 && DAMAGE.get(finalPlayerIdx) < (float) (player.getDeltaMovement().y * -(level / 0.5))) {
                            DAMAGE.set(finalPlayerIdx, (float) (player.getDeltaMovement().y * -(level / 0.5)));
                        }
                        if (player.onGround() && DAMAGE.get(finalPlayerIdx) > 1) {
                            System.out.println("damage: " + DAMAGE.get(finalPlayerIdx) + ", level: " + level + ", player y velocity: " + player.getDeltaMovement().y);
                            ModMessage.sendToServer(new ExplodeC2SPacket(player.position(), player, DAMAGE.get(finalPlayerIdx)));
                            player.resetFallDistance();
                            break;
                        } else if (player.onGround()) {
                            player.resetFallDistance();
                            break;
                        }
                        player.resetFallDistance();
                        Thread.sleep(50);
                    }
                    DAMAGE.remove(finalPlayerIdx);
                    playerList.remove(finalPlayerIdx);
                    StartedStomp.remove(finalPlayerIdx);
                } catch (Exception e) {System.out.println(e.toString());}
            }).start();
        }
    }
}
