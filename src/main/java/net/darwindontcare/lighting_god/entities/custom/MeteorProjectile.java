package net.darwindontcare.lighting_god.entities.custom;

import net.darwindontcare.lighting_god.entities.EntityInit;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.AddForceToEntityC2SPacket;
import net.darwindontcare.lighting_god.networking.packet.AddForceToEntityS2CPacket;
import net.darwindontcare.lighting_god.networking.packet.SetMeteorDataC2SPacket;
import net.darwindontcare.lighting_god.networking.packet.SetMeteorDataS2CPacket;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class MeteorProjectile extends Fireball implements GeoEntity {
    private int POWER = 30;
    private Vec3 direction;
    private boolean canStartMoving;
    private int shoot_timer = 110;
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    private float currentSpeed = 0;
    private float maxSpeed = 2f;
    private int serverInstanceId;
    protected static final RawAnimation SPAWN_ANIM = RawAnimation.begin().thenPlay("spawn");

    public MeteorProjectile(Level level, LivingEntity livingEntity, double x, double y, double z, int power, Vec3 direction) {
        super(EntityInit.EARTH_METEOR.get(), livingEntity, x, y, z, level);
        POWER = power;
        this.canStartMoving = false;
        this.direction = direction;

        double xSquared = direction.x * direction.x;
        double ySquared = direction.y * direction.y;
        double zSquared = direction.z * direction.z;

        maxSpeed = (float) Math.sqrt(xSquared + ySquared + zSquared);
        System.out.println("max speed: "+maxSpeed);
    }

    public MeteorProjectile(EntityType<? extends Fireball> p_37006_, Level p_37007_) {
        super(p_37006_, p_37007_);
        this.canStartMoving = false;
    }

    public void setClientData(Vec3 direction, float maxSpeed, int serverInstance) {
        if (this.level().isClientSide) {
            this.direction = direction;
            this.maxSpeed = maxSpeed;
            this.serverInstanceId = serverInstance;
            System.out.println("received data: \ndirection="+direction+"\nmax speed="+maxSpeed+"\nserver instance id="+serverInstanceId);
        }
    }

    public void setServerData(boolean canStartMoving) {
        if (!this.level().isClientSide) {
            if (!this.canStartMoving && canStartMoving)
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENDER_DRAGON_SHOOT, SoundSource.BLOCKS, 0.5F, (float) (0.4F / (this.getRandomY() * 0.4F + 0.8F)), false);
            this.canStartMoving = canStartMoving;
            System.out.println(canStartMoving);
        }
    }

    @Override
    protected void onHit(HitResult p_37218_) {
        super.onHit(p_37218_);
        if (!this.level().isClientSide) {
            boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this.getOwner());
            this.level().explode(this.getOwner(), this.position().x, this.position().y, this.position().z, this.POWER, flag, Level.ExplosionInteraction.NONE);
            this.level().gameEvent(null, GameEvent.EXPLODE, this.position());
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult p_37216_) {
        super.onHitEntity(p_37216_);
        if (!this.level().isClientSide) {
            Entity entity = p_37216_.getEntity();
            Entity entity1 = this.getOwner();
            entity.hurt(this.damageSources().fireball(this, entity1), 6.0F);
            if (entity1 instanceof LivingEntity) {
                this.doEnchantDamageEffects((LivingEntity)entity1, entity);
            }

        }
    }

    @Override
    public void tick() {
        super.tick();
        if (canStartMoving) {
            System.out.println(this);
            this.setRemainingFireTicks(10);
            if (direction != null) {
                if (currentSpeed < maxSpeed) currentSpeed += 0.1f;
            }
        } else {
            this.setRemainingFireTicks(0);
        }
        ModMessage.sendToServer(new SetMeteorDataC2SPacket(serverInstanceId, canStartMoving));
        if (shoot_timer > 0)shoot_timer -= 1;
        else if (!canStartMoving) canStartMoving = true;
    }

    private boolean initiated = false;

    public boolean canStartMoving() {return canStartMoving;}

    @Override
    public void baseTick() {
        super.baseTick();
        try {
            if (!this.level().isClientSide) {
                if (!initiated) {
                    ModMessage.sendToPlayer(new SetMeteorDataS2CPacket(direction, maxSpeed, this), (ServerPlayer) this.getOwner());
                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENDER_DRAGON_DEATH, SoundSource.BLOCKS, 0.5F, (float) (0.4F / (this.getRandomY() * 0.4F + 0.8F)), false);
                    initiated = true;
                }

                if (canStartMoving) {
                    System.out.println("is server");
                    this.setRemainingFireTicks(10);

                    for (int i = 0; i < 15; i++) {
                        ((ServerLevel) this.level()).sendParticles(ParticleTypes.FLAME, this.getX() + this.getRandomY() * 0.01, this.getY() + this.getRandomY() * 0.01, this.getZ() + this.getRandomY() * 0.01, 1, this.getRandomY() * 0.01, this.getRandomY() * 0.01, this.getRandomY() * 0.01, this.getRandomY() * 0.01);
                    }

                    if (direction != null) {
                        System.out.println(currentSpeed);
                        Vec3 movement = new Vec3(direction.x * currentSpeed, direction.y * currentSpeed, direction.z * currentSpeed);
                        this.setDeltaMovement(movement);
                        ModMessage.sendToPlayer(new AddForceToEntityS2CPacket(movement, this, false), (ServerPlayer) this.getOwner());
                        if (currentSpeed < maxSpeed) currentSpeed += 0.1f;
                    }
                } else {
                    this.setRemainingFireTicks(0);
                    this.setDeltaMovement(0, 0, 0);
                    ModMessage.sendToPlayer(new AddForceToEntityS2CPacket(new Vec3(0, 0, 0), this, false), (ServerPlayer) this.getOwner());
                }
                if (shoot_timer > 0)shoot_timer -= 1;
                else if (!canStartMoving) canStartMoving = true;
            }
        } catch (Exception e) {System.out.println(e.toString());}
    }

    public void setPower(int power) {
        POWER = power;
    }

    protected <E extends MeteorProjectile> PlayState animController(final AnimationState<E> event) {
        if (!canStartMoving) return event.setAndContinue(SPAWN_ANIM);

        event.getController().setAnimationSpeed(((currentSpeed * 100) / maxSpeed) / 100);
        return event.setAndContinue(IDLE_ANIM);
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Meteor", 0, this::animController).setCustomInstructionKeyframeHandler(state -> {
            canStartMoving = true;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void setDeltaMovement(Vec3 p_20257_) {
        super.setDeltaMovement(p_20257_);
    }
}
