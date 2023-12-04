package net.darwindontcare.lighting_god.utils;

import net.darwindontcare.lighting_god.event.EntityGlideEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;

public class FreezeHandler {
    public static ArrayList<LivingEntity> frozenEntities = new ArrayList<>();

    public static void AddFreezeEntity(LivingEntity entity, float time, ServerLevel serverLevel) {
        new Thread(() -> {
            try {
                if (frozenEntities.contains(entity))
                    return;

                float remainingTime = time;
                frozenEntities.add(entity);
                BlockPos position = new BlockPos((int) entity.position().x, (int) entity.position().y, (int) entity.position().z);
                FallingBlockEntity ice_block = FallingBlockEntity.fall(entity.level(), position, Blocks.PACKED_ICE.defaultBlockState());
                ice_block.setNoGravity(true);
                ice_block.setInvulnerable(true);
                ice_block.disableDrop();
                ice_block.getPersistentData().putString("frozen_entity_id", entity.getStringUUID());
                serverLevel.addFreshEntity(ice_block);
                serverLevel.playSound(null, entity.position().x, entity.position().y, entity.position().z, SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                CompoundTag compoundTag = entity.getPersistentData();
                compoundTag.putBoolean("isStuckInPlace", true);
                entity.addAdditionalSaveData(compoundTag);

                while (remainingTime > 0 && !entity.isDeadOrDying() && frozenEntities.contains(entity)) {
                    ice_block.setPos(entity.position());
                    entity.setSecondsOnFire(0);
                    entity.isInPowderSnow = true;
                    entity.setTicksFrozen((int) time);

                    MobEffectInstance slowness = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 255, false, false);
                    entity.setJumping(false);
                    entity.addEffect(slowness);
                    remainingTime--;
                    Thread.sleep(50);
                }
                frozenEntities.remove(entity);
                if (entity.isAlive()) {
                    compoundTag.putBoolean("isStuckInPlace", false);
                    entity.addAdditionalSaveData(compoundTag);
                }
                entity.setTicksFrozen(0);
                ice_block.kill();
                for (int i = 0; i < 15; i++) {
                    serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.PACKED_ICE.defaultBlockState()), ice_block.position().x + ice_block.getRandomY() * 0.001, ice_block.position().y + ice_block.getRandomY() * 0.001, ice_block.position().z + ice_block.getRandomY() * 0.001, 1, ice_block.getRandomY() * 0.001, ice_block.getRandomY() * 0.001, ice_block.getRandomY() * 0.001, ice_block.getRandomY() * 0.001);
                }
                serverLevel.playSound(null, ice_block.position().x, ice_block.position().y, ice_block.position().z, SoundEvents.GLASS_BREAK, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
            } catch (Exception e) {
                System.out.println(e.toString());
                frozenEntities.remove(entity);
            }
        }).start();
    }

    public static void removeFrozenEntity(LivingEntity entity) {
        frozenEntities.remove(entity);
    }
}
