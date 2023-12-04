package net.darwindontcare.lighting_god.items;

import net.darwindontcare.lighting_god.event.EntityGlideEvent;
import net.darwindontcare.lighting_god.utils.FreezeHandler;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.commands.data.DataCommands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FreezingProjectileEntity extends Snowball {
    private static int LIFE_TIME = 50;
    private static final int FREEZE_DAMAGE = 10;
    private static final int FREEZE_TIME = 20;

    public FreezingProjectileEntity(Level p_37394_, double p_37395_, double p_37396_, double p_37397_) {
        super(p_37394_, p_37395_, p_37396_, p_37397_);
        CompoundTag nbt = this.getPersistentData();
        nbt.putInt("LifeTime", LIFE_TIME);
        this.addAdditionalSaveData(nbt);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Vec3 currentPos = this.position();
        FreezeEffect(currentPos);
        Freeze((LivingEntity) entityHitResult.getEntity());
        this.discard();
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (!this.level().isClientSide) {
            ArrayList<Block> ice = new ArrayList<>(Arrays.asList(Blocks.ICE, Blocks.PACKED_ICE, Blocks.BLUE_ICE, Blocks.FROSTED_ICE));
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockState blockState = level().getBlockState(new BlockPos((int) hitResult.getLocation().x, (int) hitResult.getLocation().y, (int) hitResult.getLocation().z));
                if (!ice.contains(blockState.getBlock())) {
                    Vec3 currentPos = this.position();
                    FreezeEffect(currentPos);
                    this.discard();
                }
            }
        }
    }

    @Override
    public boolean isNoGravity() {
        return super.isNoGravity();
    }

    private int getLifeTime() {
        CompoundTag nbt = this.getPersistentData();
        return nbt.getInt("LifeTime");
    }

    private void decresseLifeTime() {
        int lifeTime = getLifeTime();
        lifeTime--;
        CompoundTag nbt = this.getPersistentData();
        nbt.putInt("LifeTime", lifeTime);
        this.addAdditionalSaveData(nbt);
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide) Freeze(null);
        super.tick();
    }

    private void Freeze(LivingEntity hitEntity) {
        if (getLifeTime() > 0) {
            if (!this.isInvisible()) this.setInvisible(true);
            Entity owner = this.getOwner();
            Entity projectile = this;
            ServerLevel serverLevel = (ServerLevel) projectile.level();
            if (hitEntity != null) {
                FreezeEntity(hitEntity, owner, serverLevel);
            }
            Vec3 currentPos = projectile.position();

            projectile.level().playSound(null, projectile.position().x, projectile.position().y, projectile.position().z, SoundEvents.SNOW_BREAK, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));

            for (int i = 0; i < 15; i++)
                serverLevel.sendParticles(ParticleTypes.END_ROD, currentPos.x, currentPos.y, currentPos.z, 1, this.getRandomY() * 0.001, this.getRandomY() * 0.001, 0, this.getRandomY() * 0.001);

            FreezeEffect(currentPos);
            List<LivingEntity> nearbyEntities = level().getEntitiesOfClass(
                    LivingEntity.class,
                    new AABB(currentPos.x - 3, currentPos.y - 3, currentPos.z - 3, currentPos.x + 3, currentPos.y + 3, currentPos.z + 3)
            );

            for (LivingEntity entity : nearbyEntities) {
                FreezeEntity(entity, owner, serverLevel);
            }
            decresseLifeTime();
        } else {
            this.kill();
        }
    }

    private void FreezeEffect(Vec3 currentPos) {
        ServerLevel serverLevel = (ServerLevel) level();
        for (int y = -2; y < 2; y++) {
            for (int x = -2; x < 2; x++) {
                for (int z = -2; z < 2; z++) {
                    BlockPos blockPos = new BlockPos((int) currentPos.x + x, (int) currentPos.y + y, (int) currentPos.z + z);
                    if (serverLevel.getBlockState(blockPos).getBlock() == Blocks.LAVA) {
                        serverLevel.removeBlock(blockPos, false);
                        serverLevel.gameEvent(getOwner(), GameEvent.BLOCK_DESTROY, blockPos);
                        serverLevel.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getY(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                        for (int i = 0; i < 15; i++)
                            serverLevel.sendParticles(ParticleTypes.CLOUD, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1, this.getRandomY() * 0.005, this.getRandomY() * 0.005, 0, this.getRandomY() * 0.005);
                        serverLevel.setBlock(blockPos, Blocks.OBSIDIAN.defaultBlockState(), 3);
                    } else if (serverLevel.getBlockState(blockPos).getBlock() == Blocks.WATER) {
                        serverLevel.removeBlock(blockPos, false);
                        serverLevel.gameEvent(getOwner(), GameEvent.BLOCK_DESTROY, blockPos);
                        serverLevel.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getY(), SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                        for (int i = 0; i < 15; i++)
                            serverLevel.sendParticles(ParticleTypes.END_ROD, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1, this.getRandomY() * 0.005, this.getRandomY() * 0.005, 0, this.getRandomY() * 0.005);
                        serverLevel.setBlock(blockPos, Blocks.PACKED_ICE.defaultBlockState(), 3);
                    } else if (serverLevel.getBlockState(blockPos).getBlock() == Blocks.FIRE) {
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

    private void FreezeEntity(LivingEntity entity, Entity owner, ServerLevel serverLevel) {
        if (!entity.equals(owner)) {
            entity.hurt(owner.damageSources().playerAttack((Player) owner), FREEZE_DAMAGE);
            FreezeHandler.AddFreezeEntity(entity, FREEZE_TIME, serverLevel);
        }
    }
}
