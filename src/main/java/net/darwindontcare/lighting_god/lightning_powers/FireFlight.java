package net.darwindontcare.lighting_god.lightning_powers;

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

public class FireFlight {
    private static boolean isFlying = false;
    private static final float SPEED = 1f;
    private static double CURRENT_SPEED = 0;
    public static void start_flight(ServerPlayer player, int cooldown) {
        if (cooldown <= 0) flight_tick(player);
    }

    private static void flight_tick(ServerPlayer player) {
        if (!isFlying) {
            isFlying = true;
            CURRENT_SPEED = player.getSpeed();
            ServerLevel serverLevel = (ServerLevel) player.level;
            new Thread(() -> {
                int particleCooldown = 0;
                //player.setPose(Pose.FALL_FLYING);
                //ItemStack itemstack = player.getItemBySlot(EquipmentSlot.CHEST);

                while (isFlying) {
                    try {
                        if (CURRENT_SPEED < SPEED) {
                            CURRENT_SPEED += 0.01f;
                        }

                        for (int i = 0; i < 2; i++) serverLevel.sendParticles(ParticleTypes.FLAME, player.getX() + player.getRandomY() * 0.001, player.getY() + player.getRandomY() * 0.001, player.getZ() + player.getRandomY() * 0.001, 1, player.getRandomY() * 0.001, player.getRandomY() * 0.001, player.getRandomY() * 0.001, player.getRandomY() * 0.001);
                        if (particleCooldown <= 0) {
                            player.level.playSound(null, player.position().x, player.position().y, player.position().z, SoundEvents.FIRE_AMBIENT, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                            particleCooldown = 10;
                        }
                        double motionX = player.getForward().x * CURRENT_SPEED;
                        double motionY = player.getForward().y * CURRENT_SPEED;
                        double motionZ = player.getForward().z * CURRENT_SPEED;

                        ModMessage.sendToPlayer(new AddForceToEntityS2CPacket(new Vec3(motionX, motionY, motionZ), player, false), player);

                        particleCooldown--;
                        player.resetFallDistance();
                        Thread.sleep(50);
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
            }).start();
        }
    }

    public static void stop_flight(ServerPlayer player, int cooldown) {
        if (isFlying && cooldown <= 0) {
            isFlying = false;
            ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("fire_flight"), player);
            player.getAbilities().flying = false;
            player.stopFallFlying();
            player.resetFallDistance();
        }
    }
}
