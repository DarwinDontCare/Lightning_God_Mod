package net.darwindontcare.lighting_god.entities.custom;

import net.darwindontcare.lighting_god.entities.EntityInit;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.AddForceToEntityS2CPacket;
import net.darwindontcare.lighting_god.utils.AddForceToEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class IceSpikes extends Entity implements TraceableEntity, GeoEntity {
    private static final float ATTACK_DAMAGE = 10;
    private int lifeTime = 75;
    private int spikeCooldown = 10;
    private int positionModifier = 1;
    @javax.annotation.Nullable
    private LivingEntity owner;
    @javax.annotation.Nullable
    private UUID ownerUUID;

    public AttributeSupplier.Builder supplier;
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    protected static final RawAnimation GROW_SPIKES_ANIM = RawAnimation.begin().thenPlayAndHold("grow_spikes");

    public IceSpikes(EntityType<? extends IceSpikes> p_36923_, Level p_36924_) {
        super(p_36923_, p_36924_);
    }

    public IceSpikes(Level level, double x, double y, double z, float rotY, int rotX, LivingEntity p_36932_) {
        this(EntityInit.ICE_SPIKES.get(), level);
        this.setOwner(p_36932_);
        this.setYRot(rotY);
        System.out.println("spikes y: "+rotY);
        this.setPos(x, y, z);
    }

    public void setOwner(@javax.annotation.Nullable LivingEntity p_36939_) {
        this.owner = p_36939_;
        this.ownerUUID = p_36939_ == null ? null : p_36939_.getUUID();
    }

    @javax.annotation.Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel)this.level()).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }

        return this.owner;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void baseTick() {

        if (!this.level().isClientSide) {
            try {
                if (spikeCooldown <= 0) {
                    Vec3 position = this.position().add(this.getForward().multiply(new Vec3(positionModifier, 1, positionModifier)));
                    Vec3 direction = new Vec3(this.getForward().x * 2, 2, this.getForward().z * 2);

                    FreezeEffect(position, 4);

                    if (positionModifier < 16) {
                        List<LivingEntity> nearbyEntities = this.level().getEntitiesOfClass(
                                LivingEntity.class,
                                new AABB(position.x - 4, position.y - 4, position.z - 4, position.x + 4, position.y + 4, position.z + 4)
                        );

                        for (LivingEntity entity : nearbyEntities) {
                            if (entity != this.getOwner() && !(((Entity) entity instanceof ItemEntity))) {
                                entity.hurt(this.damageSources().playerAttack((Player) this.getOwner()), ATTACK_DAMAGE);
                                if (entity instanceof Player) {
                                    ModMessage.sendToPlayer(new AddForceToEntityS2CPacket(direction, entity, false), (ServerPlayer) entity);
                                } else {
                                    AddForceToEntity.AddForce(entity, direction, false);
                                }
                                entity.setTicksFrozen(50);
                            }
                        }
                    }
                    positionModifier += 4;
                    spikeCooldown = 10;
                }
                spikeCooldown--;
                if (lifeTime > 0) lifeTime--;
                else this.kill();
            } catch (Exception e) {System.out.println(e.toString());}
        }
    }

    private void FreezeEffect(Vec3 currentPos, int modifier) {
        ServerLevel serverLevel = (ServerLevel) level();
        for (int y = -modifier; y < modifier; y++) {
            for (int x = -modifier; x < modifier; x++) {
                for (int z = -modifier; z < modifier; z++) {
                    BlockPos blockPos = new BlockPos((int) currentPos.x + x, (int) currentPos.y + y, (int) currentPos.z + z);
                    if (serverLevel.getBlockState(blockPos).getBlock() == Blocks.LAVA) {
                        serverLevel.removeBlock(blockPos, false);
                        serverLevel.gameEvent(getOwner(), GameEvent.BLOCK_DESTROY, blockPos);
                        serverLevel.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getY(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                        for (int i = 0; i < 5; i++)
                            serverLevel.sendParticles(ParticleTypes.CLOUD, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1, this.getRandomY() * 0.005, this.getRandomY() * 0.005, 0, this.getRandomY() * 0.005);
                        serverLevel.setBlock(blockPos, Blocks.OBSIDIAN.defaultBlockState(), 3);
                    } else if (serverLevel.getBlockState(blockPos).getBlock() == Blocks.WATER) {
                        serverLevel.removeBlock(blockPos, false);
                        serverLevel.gameEvent(getOwner(), GameEvent.BLOCK_DESTROY, blockPos);
                        serverLevel.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getY(), SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                        for (int i = 0; i < 5; i++)
                            serverLevel.sendParticles(ParticleTypes.END_ROD, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1, this.getRandomY() * 0.005, this.getRandomY() * 0.005, 0, this.getRandomY() * 0.005);
                        serverLevel.setBlock(blockPos, Blocks.PACKED_ICE.defaultBlockState(), 3);
                    } else if (serverLevel.getBlockState(blockPos).getBlock() == Blocks.FIRE) {
                        serverLevel.removeBlock(blockPos, false);
                        serverLevel.gameEvent(getOwner(), GameEvent.BLOCK_DESTROY, blockPos);
                        serverLevel.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getY(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                        for (int i = 0; i < 5; i++)
                            serverLevel.sendParticles(ParticleTypes.CLOUD, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1, this.getRandomY() * 0.005, this.getRandomY() * 0.005, 0, this.getRandomY() * 0.005);
                    }
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {

    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return null;
    }

    @Override
    public void setItemSlot(EquipmentSlot p_21036_, ItemStack p_21037_) {

    }

    protected <E extends IceSpikes> PlayState animController(final AnimationState<E> event) {
        return event.setAndContinue(GROW_SPIKES_ANIM);
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "IceSpike", 0, this::animController)
                .setParticleKeyframeHandler(state -> {
                    for (int t = 0; t < 25; t++) {
                        Vec3 Mposition = this.position().multiply(new Vec3(positionModifier, positionModifier, positionModifier));
                        positionModifier++;
                        Vec3 position = new Vec3(this.getX() + this.getRandomY() * 0.005 + Mposition.x, this.getY() + this.getRandomY() * 0.005 + Mposition.y, this.getZ() + this.getRandomY() * 0.005 + Mposition.z);
                        Vec3 movement = new Vec3(this.getRandomY() * 0.005, this.getRandomY() * 0.005, this.getRandomY() * 0.005);
                        this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.GLASS.defaultBlockState()), true, position.x, position.y, position.z, movement.x, movement.y, movement.z);
                    }
                })
                .setSoundKeyframeHandler(state -> {
                    this.level().playSound(null, this.position().x, this.position().y, this.position().z, SoundEvents.GLASS_BREAK, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                })
                .setCustomInstructionKeyframeHandler(state -> {
                    this.kill();
                })
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
