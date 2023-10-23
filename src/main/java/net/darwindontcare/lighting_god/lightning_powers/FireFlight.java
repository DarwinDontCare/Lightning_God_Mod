package net.darwindontcare.lighting_god.lightning_powers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.darwindontcare.lighting_god.entities.CustomPlayer;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.AddForceToEntityS2CPacket;
import net.darwindontcare.lighting_god.networking.packet.SetClientCooldownS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Dictionary;

public class FireFlight {
    private static final float MAX_SPEED = 1.5f;
    private static final double MIN_SPEED = 0.7f;
    private static final ArrayList<Boolean> flyingList = new ArrayList<>();
    private static final ArrayList<Integer> playerList = new ArrayList<>();
    public static void start_flight(ServerPlayer player, int cooldown) {
        if (cooldown <= 0) {
            if (!playerList.contains(player.getId())) {
                flyingList.add(false);
                playerList.add(player.getId());
            }
            flight_tick(player);
        }
    }

    private static void flight_tick(ServerPlayer player) {
        boolean isFlying = false;
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i) == player.getId()) {
                isFlying = flyingList.get(i);
                break;
            }
        }
        if (!isFlying) {
            System.out.println("started flying");
            System.out.println(flyingList);
            ServerLevel serverLevel = (ServerLevel) player.level();
            for (int i = 0; i < playerList.size(); i++) {
                if (playerList.get(i) == player.getId()) {
                    flyingList.set(i, true);
                }
            }
            new Thread(() -> {
                int playerIdx = 0;
                for (int i = 0; i < playerList.size(); i++) {
                    if (playerList.get(i) == player.getId()) {
                        playerIdx = i;
                        break;
                    }
                }
                try {
                    double CURRENT_SPEED = player.getSpeed();
                    int particleCooldown = 0;

                    double xSquared = player.getDeltaMovement().x * player.getDeltaMovement().x;
                    double ySquared = player.getDeltaMovement().y * player.getDeltaMovement().y;
                    double zSquared = player.getDeltaMovement().z * player.getDeltaMovement().z;

                    CURRENT_SPEED = Math.sqrt(xSquared + ySquared + zSquared);
                    double targetSpeed = CURRENT_SPEED;

                    while (flyingList.get(playerIdx)) {
                        float modifier = player.getXRot() / 35;
                        if (modifier < 1) modifier = 1;
                        if (modifier > 3) modifier = 3;
                        System.out.println(modifier);
                        if (CURRENT_SPEED < targetSpeed) {
                            CURRENT_SPEED += 0.01f * modifier;
                        } else if (CURRENT_SPEED > targetSpeed) {
                            CURRENT_SPEED -= 0.01f;
                        }
                        if (player.getXRot() != 0) targetSpeed = MAX_SPEED * player.getXRot();
                        else targetSpeed = 1f;
                        if (targetSpeed > MAX_SPEED) targetSpeed = MAX_SPEED;
                        else if (targetSpeed < MIN_SPEED) targetSpeed = MIN_SPEED;

                        for (int i = 0; i < 2; i++)
                            serverLevel.sendParticles(ParticleTypes.FLAME, player.getX() + player.getRandomY() * 0.001, player.getY() + player.getRandomY() * 0.001, player.getZ() + player.getRandomY() * 0.001, 1, player.getRandomY() * 0.001, player.getRandomY() * 0.001, player.getRandomY() * 0.001, player.getRandomY() * 0.001);
                        if (particleCooldown <= 0) {
                            player.level().playSound(null, player.position().x, player.position().y, player.position().z, SoundEvents.FIRE_AMBIENT, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                            particleCooldown = 10;
                        }
                        double motionX = player.getForward().x * CURRENT_SPEED;
                        double motionY = player.getForward().y * CURRENT_SPEED;
                        double motionZ = player.getForward().z * CURRENT_SPEED;

                        player.startFallFlying();
                        ModMessage.sendToPlayer(new AddForceToEntityS2CPacket(new Vec3(motionX, motionY, motionZ), player, false), player);

                        particleCooldown--;
                        player.resetFallDistance();
                        Thread.sleep(50);
                    }
                    flyingList.remove(playerIdx);
                    playerList.remove(playerIdx);
                } catch (Exception e) {System.out.println(e.toString());}
            }).start();
        }
    }

    public static void stop_flight(ServerPlayer player, int cooldown) {
        if (cooldown <= 0) {
            for (int i = 0; i < playerList.size(); i++) {
                if (playerList.get(i) == player.getId()) {
                    flyingList.set(i, false);
                    break;
                }
            }
            ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("fire_flight"), player);
            player.getAbilities().flying = false;
            player.stopFallFlying();
            player.resetFallDistance();
        }
    }
}
