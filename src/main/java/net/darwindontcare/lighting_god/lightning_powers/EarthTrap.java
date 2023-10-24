package net.darwindontcare.lighting_god.lightning_powers;

import net.darwindontcare.lighting_god.event.EntityGlideEvent;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.SetClientCooldownS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EarthTrap {
    private static final float DAMAGE = 5f;
    public static void Trap(ServerPlayer player, int cooldown) {
        if (cooldown <= 0) {
            Vec3 currentPos = player.position();
            ServerLevel serverLevel = (ServerLevel) player.level();
            serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BELL_RESONATE, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
            for (int i = 0; i < 30; i++) {
                serverLevel.sendParticles(ParticleTypes.ASH, player.getX(), player.getY(), player.getZ(), 1, player.getRandomY() * 0.01, player.getRandomY() * 0.01, 0, player.getRandomY() * 0.01);
            }
            List<LivingEntity> nearbyEntities = serverLevel.getEntitiesOfClass(
                    LivingEntity.class,
                    new AABB(currentPos.x - 10, currentPos.y - 10, currentPos.z - 10, currentPos.x + 10, currentPos.y + 10, currentPos.z + 10)
            );

            for (int i = 0; i < nearbyEntities.size(); i++) {
                if (i > 5) break;
                Entity entity = nearbyEntities.get(i);
                if (!(entity instanceof ItemEntity)) {
                    TrapEntity(entity, player, serverLevel);
                }
            }
            ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("earth_trap"), player);
        }
    }

    private static void TrapEntity(Entity entity, Entity caster, ServerLevel serverLevel) {
        if (!entity.equals(caster)) {
            entity.hurt(caster.damageSources().playerAttack((Player) caster), DAMAGE);
            boolean placedIce1;
            if (!serverLevel.getBlockState(new BlockPos((int) entity.position().x, (int) entity.position().y, (int) entity.position().y)).isSolid()) {
                placedIce1 = serverLevel.setBlock(new BlockPos((int) entity.position().x, (int) entity.position().y, (int) entity.position().z), Blocks.STONE.defaultBlockState(), 3);
                if (placedIce1) serverLevel.gameEvent(caster, GameEvent.BLOCK_PLACE, new BlockPos((int) entity.position().x, (int) entity.position().y, (int) entity.position().z));
            } else {
                placedIce1 = false;
            }
            serverLevel.playSound(null, entity.position().x, entity.position().y, entity.position().z, SoundEvents.STONE_PLACE, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
            Thread runnable = getThread((LivingEntity) entity);
            new Thread(() -> {
                Vec3 affectedEntityPos = entity.position();
                try {
                    Thread.sleep((int)DAMAGE*1000);
                    runnable.interrupt();
                    if (placedIce1) {
                        serverLevel.destroyBlock(new BlockPos((int) affectedEntityPos.x, (int) affectedEntityPos.y, (int) affectedEntityPos.z), false);
                        serverLevel.gameEvent(caster, GameEvent.BLOCK_DESTROY,new BlockPos((int) affectedEntityPos.x, (int) affectedEntityPos.y, (int) affectedEntityPos.z));}
                    serverLevel.playSound(null, entity.position().x, entity.position().y, entity.position().z, SoundEvents.STONE_BREAK, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }).start();
        }
    }

    @NotNull
    private static Thread getThread(LivingEntity entity) {
        Thread runnable = new Thread(() -> {
            int freezeTime = (int) DAMAGE * 1000;
            while (freezeTime > 0) {
                if (!(entity instanceof Player)) {
                    if (!EntityGlideEvent.cancelLivingEntityUpdate.contains(entity)) {
                        EntityGlideEvent.cancelLivingEntityUpdate.add(entity);
                    }
                } else {
                    entity.setNoGravity(true);
                    MobEffectInstance slowness = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 255, false, false);
                    entity.addEffect(slowness);
                }
                freezeTime--;
            }
            if (!(entity instanceof Player)) {
                EntityGlideEvent.cancelLivingEntityUpdate.remove(entity);
            }
        });
        runnable.start();
        return runnable;
    }
}
