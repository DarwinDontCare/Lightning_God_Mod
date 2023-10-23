package net.darwindontcare.lighting_god.entities.custom;

import net.darwindontcare.lighting_god.entities.EntityInit;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.IceSpikeDamageC2SPacket;
import net.darwindontcare.lighting_god.networking.packet.SummonParticleC2SPacket;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
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

import java.util.UUID;

public class IceSpikes extends Entity implements TraceableEntity, GeoEntity {
    public static final int ATTACK_DURATION = 20;
    public static final int LIFE_OFFSET = 2;
    public static final int ATTACK_TRIGGER_TICKS = 14;
    private int warmupDelayTicks;
    private boolean sentSpikeEvent;
    private int lifeTicks = 22;
    private int lifeTime = 100;
    private boolean clientSideAttackStarted;
    @javax.annotation.Nullable
    private LivingEntity owner;
    @javax.annotation.Nullable
    private UUID ownerUUID;
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    protected static final RawAnimation GROW_SPIKES_ANIM = RawAnimation.begin().thenPlay("grow_spikes");

    public IceSpikes(EntityType<? extends IceSpikes> p_36923_, Level p_36924_) {
        super(p_36923_, p_36924_);
    }

    public IceSpikes(Level p_36926_, double x, double y, double z, float rotX, int p_36931_, LivingEntity p_36932_) {
        this(EntityInit.ICE_SPIKES.get(), p_36926_);
        this.setOwner(p_36932_);
        this.setYRot(rotX);
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
        if (lifeTime > 0)lifeTime--;
        else this.kill();
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

    private int positionModifier = 1;

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
