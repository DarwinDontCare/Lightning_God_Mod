package net.darwindontcare.lighting_god.items;

import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FreezingProjectileEntity extends Snowball {
    private static int LIFE_TIME = 50;
    private static final int FREEZE_DAMAGE = 10;
    private static final int FREEZE_TIME = 3;

    public FreezingProjectileEntity(Level p_37394_, double p_37395_, double p_37396_, double p_37397_) {
        super(p_37394_, p_37395_, p_37396_, p_37397_);
        CompoundTag nbt = this.getPersistentData();
        nbt.putInt("LifeTime", LIFE_TIME);
        this.addAdditionalSaveData(nbt);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Freeze((LivingEntity) entityHitResult.getEntity());
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        if (!this.level().getBlockState(blockHitResult.getBlockPos()).getBlock().equals(Blocks.ICE) &&
                !this.level().getBlockState(blockHitResult.getBlockPos()).getBlock().equals(Blocks.PACKED_ICE) &&
                !this.level().getBlockState(blockHitResult.getBlockPos()).getBlock().equals(Blocks.BLUE_ICE) &&
                !this.level().getBlockState(blockHitResult.getBlockPos()).getBlock().equals(Blocks.FROSTED_ICE)) {
            this.kill();
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
                FreezeEntity(hitEntity, owner, projectile, serverLevel);
            }
            projectile.level().playSound(null, projectile.position().x, projectile.position().y, projectile.position().z, SoundEvents.SNOW_BREAK, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
            serverLevel.sendParticles(ParticleTypes.END_ROD, projectile.getX(), projectile.getY() + this.getRandomY() * 0.0005, projectile.getZ(), 1, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005);
            serverLevel.sendParticles(ParticleTypes.END_ROD, projectile.getX() - ((projectile.getForward().x + 1) * 0.2), projectile.getY() + this.getRandomY() * 0.0005, projectile.getZ() + ((projectile.getForward().z - 1) * 0.2), 1, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005);
            serverLevel.sendParticles(ParticleTypes.END_ROD, projectile.getX() - ((projectile.getForward().x + 1) * 0.4), projectile.getY() + this.getRandomY() * 0.0005, projectile.getZ() + ((projectile.getForward().z - 1) * 0.4), 1, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005);
            serverLevel.sendParticles(ParticleTypes.END_ROD, projectile.getX() - ((projectile.getForward().x + 1) * 0.6), projectile.getY() + this.getRandomY() * 0.0005, projectile.getZ() + ((projectile.getForward().z - 1) * 0.6), 1, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005);
            serverLevel.sendParticles(ParticleTypes.END_ROD, projectile.getX() - ((projectile.getForward().x + 1) * 0.8), projectile.getY() + this.getRandomY() * 0.0005, projectile.getZ() + ((projectile.getForward().z - 1) * 0.8), 1, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005);
            serverLevel.sendParticles(ParticleTypes.END_ROD, projectile.getX() - (projectile.getForward().x + 1), projectile.getY() + this.getRandomY() * 0.0005, projectile.getZ() + (projectile.getForward().z - 1), 1, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005);
            serverLevel.sendParticles(ParticleTypes.END_ROD, projectile.getX() - ((projectile.getForward().x - 1) * 0.2), projectile.getY() + this.getRandomY() * 0.0005, projectile.getZ() + ((projectile.getForward().z + 1) * 0.2), 1, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005);
            serverLevel.sendParticles(ParticleTypes.END_ROD, projectile.getX() - ((projectile.getForward().x - 1) * 0.4), projectile.getY() + this.getRandomY() * 0.0005, projectile.getZ() + ((projectile.getForward().z + 1) * 0.4), 1, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005);
            serverLevel.sendParticles(ParticleTypes.END_ROD, projectile.getX() - ((projectile.getForward().x - 1) * 0.6), projectile.getY() + this.getRandomY() * 0.0005, projectile.getZ() + ((projectile.getForward().z + 1) * 0.6), 1, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005);
            serverLevel.sendParticles(ParticleTypes.END_ROD, projectile.getX() - ((projectile.getForward().x - 1) * 0.8), projectile.getY() + this.getRandomY() * 0.0005, projectile.getZ() + ((projectile.getForward().z + 1) * 0.8), 1, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005);
            serverLevel.sendParticles(ParticleTypes.END_ROD, projectile.getX() - (projectile.getForward().x - 1), projectile.getY(), projectile.getZ() + (projectile.getForward().z + 1), 1, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005, this.getRandomY() * 0.0005);

            for (int idx = -1; idx < 1; idx++) {
                ArrayList<BlockPos> blocksPos = new ArrayList<>(Arrays.asList(new BlockPos((int) (projectile.position().x + 1), (int) (projectile.position().y + idx), (int) projectile.position().z),
                        new BlockPos((int) projectile.position().x, (int) (projectile.position().y + idx), (int) projectile.position().z),
                        new BlockPos((int) (projectile.position().x - 1), (int) (projectile.position().y + idx), (int) projectile.position().z),
                        new BlockPos((int) projectile.position().x, (int) (projectile.position().y + idx), (int) (projectile.position().z + 1)),
                        new BlockPos((int) projectile.position().x, (int) (projectile.position().y + idx), (int) (projectile.position().z - 1)),
                        new BlockPos((int) (projectile.position().x + 1), (int) (projectile.position().y + idx), (int) (projectile.position().z - 1)),
                        new BlockPos((int) (projectile.position().x - 1), (int) (projectile.position().y + idx), (int) (projectile.position().z - 1)),
                        new BlockPos((int) (projectile.position().x + 1), (int) (projectile.position().y + idx), (int) (projectile.position().z + 1)),
                        new BlockPos((int) (projectile.position().x - 1), (int) (projectile.position().y + idx), (int) (projectile.position().z + 1))));

                for (BlockPos blockPos : blocksPos) {
                    BlockState block = level().getBlockState(blockPos);
                    if (block.getBlock().equals(Blocks.WATER)) {
                        level().setBlock(blockPos, Blocks.ICE.defaultBlockState(), 3);
                        level().gameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                        projectile.level().playSound((Player)null, projectile.position().x, projectile.position().y, projectile.position().z, SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                    } else if (block.getBlock().equals(Blocks.LAVA)) {
                        level().setBlock(blockPos, Blocks.OBSIDIAN.defaultBlockState(), 3);
                        level().gameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                        projectile.level().playSound((Player)null, projectile.position().x, projectile.position().y, projectile.position().z, SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                    } else if (block.getBlock().equals(Blocks.FIRE)) {
                        level().setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                        level().gameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                        projectile.level().playSound((Player)null, projectile.position().x, projectile.position().y, projectile.position().z, SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                    }
                }
            }

            Vec3 currentPos = projectile.position();
            List<LivingEntity> nearbyEntities = level().getEntitiesOfClass(
                    LivingEntity.class,
                    new AABB(currentPos.x - 3, currentPos.y - 3, currentPos.z - 3, currentPos.x + 3, currentPos.y + 3, currentPos.z + 3)
            );

            for (LivingEntity entity : nearbyEntities) {
                FreezeEntity(entity, owner, projectile, serverLevel);
            }
            decresseLifeTime();
        } else {
            this.kill();
        }
    }

    private void FreezeEntity(LivingEntity entity, Entity owner, Entity projectile, ServerLevel serverLevel) {
        if (!entity.equals(owner)) {
            entity.setSecondsOnFire(0);
            entity.hurt(owner.damageSources().playerAttack((Player) owner), FREEZE_DAMAGE);
            ArrayList<Boolean> placedIce = new ArrayList<>();
            int iceIndex = 0;
            for (int x = -1; x < 1; x++) {
                for (int z = -1; z < 1; z++) {
                    if (!level().getBlockState(new BlockPos((int) entity.position().x + x, (int) entity.position().y, (int) entity.position().z + z)).isSolid()) {
                        placedIce.add(level().setBlock(new BlockPos((int) entity.position().x + x, (int) entity.position().y, (int) entity.position().z + z), Blocks.PACKED_ICE.defaultBlockState(), 3));
                        if (placedIce.get(iceIndex))
                            level().gameEvent(owner, GameEvent.BLOCK_PLACE, new BlockPos((int) entity.position().x, (int) entity.position().y, (int) entity.position().z));
                    } else {
                        placedIce.add(false);
                    }
                    iceIndex++;
                    if (!level().getBlockState(new BlockPos((int) entity.position().x, (int) (entity.position().y + entity.getEyeHeight()), (int) entity.position().z)).isSolid()) {
                        placedIce.add(level().setBlock(new BlockPos((int) entity.position().x, (int) (entity.position().y + entity.getEyeHeight()), (int) entity.position().z), Blocks.PACKED_ICE.defaultBlockState(), 3));
                        if (placedIce.get(iceIndex))
                            level().gameEvent(owner, GameEvent.BLOCK_PLACE, new BlockPos((int) entity.position().x, (int) (entity.position().y + entity.getEyeHeight()), (int) entity.position().z));
                    } else {
                        placedIce.add(false);
                    }
                    iceIndex++;
                }
            }
            projectile.level().playSound(null, projectile.position().x, projectile.position().y, projectile.position().z, SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
            entity.isInPowderSnow = true;
            entity.setTicksFrozen(FREEZE_DAMAGE*500);
            Thread runnable = getThread(entity, this);
            new Thread(() -> {
                Vec3 affectedEntityPos = entity.position();
                try {
                    Thread.sleep(FREEZE_TIME*1000);
                    entity.setTicksFrozen(0);
                    runnable.interrupt();
                    for (boolean placedIceBlock: placedIce) {
                        for (int x = -1; x < 1; x++) {
                            for (int z = -1; z < 1; z++) {
                                if (placedIceBlock) {
                                    level().destroyBlock(new BlockPos((int) affectedEntityPos.x + x, (int) affectedEntityPos.y, (int) affectedEntityPos.z + z), true);
                                    level().gameEvent(owner, GameEvent.BLOCK_DESTROY, new BlockPos((int) affectedEntityPos.x + x, (int) affectedEntityPos.y, (int) affectedEntityPos.z + z));
                                }
                            }
                        }
                    }
                    entity.level().playSound(null, entity.position().x, entity.position().y, entity.position().z, SoundEvents.GLASS_BREAK, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }).start();
        }
    }

    @NotNull
    private static Thread getThread(LivingEntity entity, FreezingProjectileEntity projectile) {
        Thread runnable = new Thread(() -> {
            while (projectile.getLifeTime() > 0 && entity.getTicksFrozen() > 0) {
                if (!(entity instanceof Player)) {
                    entity.getBrain().clearMemories();
                } else {
                    entity.setNoGravity(true);
                    MobEffectInstance slowness = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 255, false, false);
                    entity.addEffect(slowness);
                }
            }
        });
        runnable.start();
        return runnable;
    }
}
