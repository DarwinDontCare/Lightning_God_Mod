package net.darwindontcare.lighting_god.lightning_powers;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.SetClientCooldownS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Random;

public class FireBurst {
    private static final int RANGE = 10;
    private static final int DAMAGE = 10;
    public static void Burst(ServerPlayer player, int cooldown) {
        if (cooldown <= 0) {
            Vec3 playerPos = player.position();
            ServerLevel serverLevel = (ServerLevel) player.level();
            List<LivingEntity> nearbyEntities = serverLevel.getEntitiesOfClass(
                    LivingEntity.class,
                    new AABB(playerPos.x, playerPos.y - RANGE, playerPos.z - RANGE, playerPos.x, playerPos.y + RANGE, playerPos.z + RANGE)
            );
            for (LivingEntity entity : nearbyEntities) {
                if (entity != player) {
                    entity.setSecondsOnFire(10);
                    entity.hurt(entity.damageSources().inFire(), DAMAGE);
                }
            }
            for (int i = 0; i < 25; i++) {serverLevel.sendParticles(ParticleTypes.FLAME, playerPos.x + player.getRandomY() * 0.01, playerPos.y + player.getRandomY() * 0.01, playerPos.z + player.getRandomY() * 0.01, 1, player.getRandomY() * 0.01, player.getRandomY() * 0.01, player.getRandomY() * 0.01, player.getRandomY() * 0.01);}
            serverLevel.playSound(null, playerPos.x, playerPos.y, playerPos.z, SoundEvents.BLAZE_SHOOT, SoundSource.NEUTRAL, 0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
            boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(player.level(), player);
            player.level().explode(player, player.getX(), player.getY(), player.getZ(), (float)5, flag, Level.ExplosionInteraction.NONE);
            MobEffectInstance fireProtection = new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 2);
            player.addEffect(fireProtection);
            PlaceFire(player, RANGE);
            ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("fire_burst"), player);
        }
    }

    private static void PlaceFire(ServerPlayer player, int maxDistance) {
        new Thread(() -> {
            Random random = new Random();
            ServerLevel serverLevel = (ServerLevel) player.level();
            for (int distance = 1; distance < maxDistance; distance++) {
                for (int x = -1; x < 1; x++) {
                    for (int y = -1; y < 1; y++) {
                        for (int z = -1; z < 1; z++) {
                            if (x != 0 && y != 0 && z != 0) {
                                Vec3 currentPos = new Vec3(player.position().x * distance * x, player.position().y * distance * y, player.position().z * distance * z);
                                for (int i = 0; i < 30; i++) {
                                    double probability = random.nextDouble(0, 1);
                                    BlockState currentBlock = serverLevel.getBlockState(new BlockPos((int) currentPos.x, (int) currentPos.y, (int) currentPos.z));
                                    if ((probability > 0.5 && currentBlock.getBlock().equals(Blocks.WATER))) {
                                        serverLevel.setBlock(new BlockPos((int) currentPos.x, (int) currentPos.y, (int) currentPos.z), Blocks.AIR.defaultBlockState(), 3);
                                        serverLevel.gameEvent(null, GameEvent.BLOCK_PLACE, new BlockPos((int) currentPos.x, (int) currentPos.y, (int) currentPos.z));
                                        serverLevel.sendParticles(ParticleTypes.PORTAL, currentPos.x + player.getRandomY() * 0.005, currentPos.y + player.getRandomY() * 0.005, currentPos.z + player.getRandomY() * 0.005, 1, player.getRandomY() * 0.005, player.getRandomY() * 0.005, player.getRandomY() * 0.005, player.getRandomY() * 0.005);
                                        for(int idx = 0; idx < 10; idx++) {
                                            serverLevel.sendParticles(ParticleTypes.CLOUD, currentPos.x + player.getRandomY() * 0.005, currentPos.y + player.getRandomY() * 0.005, currentPos.z + player.getRandomY() * 0.005, 1, player.getRandomY() * 0.005, player.getRandomY() * 0.005, player.getRandomY() * 0.005, player.getRandomY() * 0.005);
                                        }
                                        player.level().playSound(null, currentPos.x, currentPos.y, currentPos.z, SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
                                    } else if ((probability > 0.5 && currentBlock.getBlock().equals(Blocks.OBSIDIAN))) {
                                        serverLevel.setBlock(new BlockPos((int) currentPos.x, (int) currentPos.y, (int) currentPos.z), Blocks.LAVA.defaultBlockState(), 3);
                                        serverLevel.gameEvent(null, GameEvent.BLOCK_PLACE, new BlockPos((int) currentPos.x, (int) currentPos.y, (int) currentPos.z));
                                        serverLevel.sendParticles(ParticleTypes.PORTAL, currentPos.x + player.getRandomY() * 0.005, currentPos.y + player.getRandomY() * 0.005, currentPos.z + player.getRandomY() * 0.005, 1, player.getRandomY() * 0.005, player.getRandomY() * 0.005, player.getRandomY() * 0.005, player.getRandomY() * 0.005);
                                        for(int idx = 0; idx < 10; idx++) {
                                            serverLevel.sendParticles(ParticleTypes.CLOUD, currentPos.x + player.getRandomY() * 0.005, currentPos.y + player.getRandomY() * 0.005, currentPos.z + player.getRandomY() * 0.005, 1, player.getRandomY() * 0.005, player.getRandomY() * 0.005, player.getRandomY() * 0.005, player.getRandomY() * 0.005);
                                        }
                                        player.level().playSound(null, currentPos.x, currentPos.y, currentPos.z, SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }).start();
    }
}
