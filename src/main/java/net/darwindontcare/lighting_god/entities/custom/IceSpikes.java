package net.darwindontcare.lighting_god.entities.custom;

import net.darwindontcare.lighting_god.entities.EntityInit;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.AddForceToEntityS2CPacket;
import net.darwindontcare.lighting_god.networking.packet.IceSpikeDamageC2SPacket;
import net.darwindontcare.lighting_god.networking.packet.SummonParticleC2SPacket;
import net.darwindontcare.lighting_god.utils.AddForceToEntity;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
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
import software.bernie.geckolib.util.ClientUtils;

import java.util.List;
import java.util.UUID;

public class IceSpikes extends Entity implements GeoEntity {
    private static final float ATTACK_DAMAGE = 10;
    private int lifeTime = 75;
    private int spikeCooldown = 10;
    private int positionModifier = 1;
    private float rotation;
    private boolean clientSideAttackStarted;
    @javax.annotation.Nullable
    private LivingEntity owner;
    @javax.annotation.Nullable
    private UUID ownerUUID;
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    protected static final RawAnimation GROW_SPIKES_ANIM = RawAnimation.begin().thenPlayAndHold("grow_spikes");

    public IceSpikes(EntityType<? extends IceSpikes> p_36923_, Level p_36924_) {
        super(p_36923_, p_36924_);
    }

    public IceSpikes(Level p_36926_, double x, double y, double z, float rotY, int p_36931_, LivingEntity p_36932_) {
        this(EntityInit.ICE_SPIKES.get(), p_36926_);
        this.setOwner(p_36932_);
        this.setYRot(rotY);
        this.rotation = rotY;
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
            setYRot(rotation);
            System.out.println(spikeCooldown);
            if (spikeCooldown <= 0) {
                Vec3 position = this.position().multiply(new Vec3(positionModifier, positionModifier, positionModifier));
                positionModifier++;
                List<LivingEntity> nearbyEntities = this.level().getEntitiesOfClass(
                        LivingEntity.class,
                        new AABB(position.x - 4, position.y - 4, position.z - 4, position.x + 4, position.y + 4, position.z + 4)
                );

                for (LivingEntity entity : nearbyEntities) {
                    if (entity != this.getOwner() && !(((Entity) entity instanceof ItemEntity))) {
                        if (entity instanceof Player) {
                            ModMessage.sendToPlayer(new AddForceToEntityS2CPacket(new Vec3(0, 2, 0).multiply(this.getForward()), entity, false), (ServerPlayer) entity);
                        } else {
                            AddForceToEntity.AddForce(entity, new Vec3(0, 2, 0).multiply(this.getForward()), false);
                        }
                        entity.hurt(this.damageSources().playerAttack((Player) this.getOwner()), ATTACK_DAMAGE);
                        entity.setTicksFrozen(50);
                    }
                }
                spikeCooldown = 10;
            }
            if (spikeCooldown > 0) spikeCooldown--;
            if (lifeTime > 0) lifeTime--;
            else this.kill();
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {

    }

    protected <E extends IceSpikes> PlayState animController(final AnimationState<E> event) {
        return event.setAndContinue(GROW_SPIKES_ANIM);
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "IceSpike", 0, this::animController)
                .setParticleKeyframeHandler(state -> {
                    for (int t = 0; t < 25; t++) {
                        Player player = ClientUtils.getClientPlayer();

                        if (player != null) {
                            Vec3 position = new Vec3(this.getX() + this.getRandomY() * 0.005, this.getY() + this.getRandomY() * 0.005, this.getZ() + this.getRandomY() * 0.005);
                            Vec3 movement = new Vec3(this.getRandomY() * 0.005, this.getRandomY() * 0.005, this.getRandomY() * 0.005);
                            player.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.GLASS.defaultBlockState()), true, position.x, position.y, position.z, movement.x, movement.y, movement.z);
                        }
                        if (this.getOwner() != null) {
                            Vec3 position = this.position().multiply(new Vec3(positionModifier, positionModifier, positionModifier));
                            ModMessage.sendToServer(new IceSpikeDamageC2SPacket(position, this));
                            positionModifier++;
                        }
                    }
                })
                .setSoundKeyframeHandler(state -> {
                    Player player = ClientUtils.getClientPlayer();

                    if (player != null)
                        player.level().playSound(null, this.position().x, this.position().y, this.position().z, SoundEvents.GLASS_BREAK, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
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
