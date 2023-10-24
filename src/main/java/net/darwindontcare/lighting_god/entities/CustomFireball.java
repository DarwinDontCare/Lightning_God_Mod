package net.darwindontcare.lighting_god.entities;

import net.darwindontcare.lighting_god.event.EntityGlideEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CustomFireball extends LargeFireball {
    private int explosionPower = 1;
    private float damage = 6.0f;
    public CustomFireball(EntityType<? extends LargeFireball> entityType, Level level) {
        super(entityType, level);
    }

    public CustomFireball(Level level, LivingEntity entity, double x, double y, double z, int power) {
        super(level, entity, x, y, z, power);
        this.explosionPower = power;
    }

    private void HeatEffect(Vec3 currentPos) {
        if (!level().isClientSide) {
            List<LivingEntity> nearbyEntities = level().getEntitiesOfClass(
                    LivingEntity.class,
                    new AABB(currentPos.x - 3, currentPos.y - 3, currentPos.z - 3, currentPos.x + 3, currentPos.y + 3, currentPos.z + 3)
            );

            for (LivingEntity entity : nearbyEntities) {
                entity.setTicksFrozen(0);
                if (EntityGlideEvent.cancelLivingEntityUpdate.contains(entity)) {
                    EntityGlideEvent.cancelLivingEntityUpdate.remove(entity);
                }
            }
            ServerLevel serverLevel = (ServerLevel) level();
            Block[] iceBlocks = {Blocks.ICE, Blocks.FROSTED_ICE, Blocks.PACKED_ICE, Blocks.BLUE_ICE};
            for (int y = -2; y < 2; y++) {
                for (int x = -2; x < 2; x++) {
                    for (int z = -2; z < 2; z++) {
                        BlockPos blockPos = new BlockPos((int) currentPos.x + x, (int) currentPos.y + y, (int) currentPos.z + z);
                        for (Block block : iceBlocks) {
                            if (serverLevel.getBlockState(blockPos).getBlock() == block) {
                                serverLevel.removeBlock(blockPos, false);
                                serverLevel.gameEvent(getOwner(), GameEvent.BLOCK_DESTROY, blockPos);
                                serverLevel.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getY(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                                for (int i = 0; i < 15; i++)
                                    serverLevel.sendParticles(ParticleTypes.CLOUD, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1, this.getRandomY() * 0.005, this.getRandomY() * 0.005, 0, this.getRandomY() * 0.005);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        //super.onHit(hitResult);
        HeatEffect(hitResult.getLocation());
        level().explode(this.getOwner(), this.position().x, this.position().y, this.position().z, explosionPower, true, Level.ExplosionInteraction.NONE);
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        //super.onHitEntity(entityHitResult);
        HeatEffect(entityHitResult.getLocation());
        level().explode(this.getOwner(), this.position().x, this.position().y, this.position().z, explosionPower, true, Level.ExplosionInteraction.NONE);
        if (!this.level().isClientSide) {
            Entity entity = entityHitResult.getEntity();
            Entity entity1 = this.getOwner();
            entity.hurt(this.damageSources().fireball(this, entity1), damage);
            if (entity1 instanceof LivingEntity) {
                this.doEnchantDamageEffects((LivingEntity)entity1, entity);
            }
        }
        //this.discard();
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!level().isClientSide) {
            Vec3 currentPos = this.position();
            ServerLevel serverLevel = (ServerLevel) level();
            Block[] iceBlocks = {Blocks.ICE, Blocks.FROSTED_ICE, Blocks.PACKED_ICE, Blocks.BLUE_ICE};
            for (int y = -2; y < 2; y++) {
                for (int x = -2; x < 2; x++) {
                    for (int z = -2; z < 2; z++) {
                        BlockPos blockPos = new BlockPos((int) currentPos.x + x, (int) currentPos.y + y, (int) currentPos.z + z);
                        for (Block block : iceBlocks) {
                            if (serverLevel.getBlockState(blockPos).getBlock() == block) {
                                serverLevel.removeBlock(blockPos, false);
                                serverLevel.gameEvent(getOwner(), GameEvent.BLOCK_DESTROY, blockPos);
                                serverLevel.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getY(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                                for (int i = 0; i < 15; i++)
                                    serverLevel.sendParticles(ParticleTypes.CLOUD, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1, this.getRandomY() * 0.005, this.getRandomY() * 0.005, 0, this.getRandomY() * 0.005);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void doEnchantDamageEffects(LivingEntity p_19971_, Entity p_19972_) {
        super.doEnchantDamageEffects(p_19971_, p_19972_);
    }
}
