package net.darwindontcare.lighting_god.lightning_powers;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.event.EntityGlideEvent;
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
import net.minecraft.world.level.block.Block;
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
    private static final int ManaCost = 100;
    public static void Burst(ServerPlayer player, int cooldown, float mana) {
        if (cooldown <= 0 && mana >= ManaCost) {
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
            HeatEffect(playerPos ,player);
            ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("fire_burst", ManaCost), player);
        }
    }

    private static void HeatEffect(Vec3 currentPos, ServerPlayer player) {
        new Thread(() -> {
            List<LivingEntity> nearbyEntities = player.level().getEntitiesOfClass(
                    LivingEntity.class,
                    new AABB(currentPos.x - RANGE, currentPos.y - RANGE, currentPos.z - RANGE, currentPos.x + RANGE, currentPos.y + RANGE, currentPos.z + RANGE)
            );

            for (LivingEntity entity : nearbyEntities) {
                entity.setTicksFrozen(0);
                if (EntityGlideEvent.cancelLivingEntityUpdate.contains(entity)) {
                    EntityGlideEvent.cancelLivingEntityUpdate.remove(entity);
                }
            }
            ServerLevel serverLevel = (ServerLevel) player.level();
            Block[] iceBlocks = {Blocks.ICE, Blocks.FROSTED_ICE, Blocks.PACKED_ICE, Blocks.BLUE_ICE, Blocks.WATER};
            for (int y = -RANGE; y < RANGE; y++) {
                for (int x = -RANGE; x < RANGE; x++) {
                    for (int z = -RANGE; z < RANGE; z++) {
                        BlockPos blockPos = new BlockPos((int) currentPos.x + x, (int) currentPos.y + y, (int) currentPos.z + z);
                        for (Block block : iceBlocks) {
                            if (serverLevel.getBlockState(blockPos).getBlock() == block) {
                                serverLevel.removeBlock(blockPos, false);
                                serverLevel.gameEvent(player, GameEvent.BLOCK_DESTROY, blockPos);
                                serverLevel.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getY(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                                for (int i = 0; i < 15; i++)
                                    serverLevel.sendParticles(ParticleTypes.CLOUD, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1, player.getRandomY() * 0.005, player.getRandomY() * 0.005, 0, player.getRandomY() * 0.005);
                            }
                        }
                        if (serverLevel.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) {
                            serverLevel.removeBlock(blockPos, false);
                            serverLevel.gameEvent(player, GameEvent.BLOCK_DESTROY, blockPos);
                            serverLevel.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getY(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                            for (int i = 0; i < 15; i++)
                                serverLevel.sendParticles(ParticleTypes.CLOUD, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1, player.getRandomY() * 0.005, player.getRandomY() * 0.005, 0, player.getRandomY() * 0.005);
                            serverLevel.setBlock(blockPos, Blocks.LAVA.defaultBlockState(), 3);
                            serverLevel.gameEvent(null, GameEvent.BLOCK_PLACE, new BlockPos((int) currentPos.x, (int) currentPos.y, (int) currentPos.z));
                        }
                    }
                }
            }
        }).start();
    }
}
