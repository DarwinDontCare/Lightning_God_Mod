package net.darwindontcare.lighting_god.items;

import net.darwindontcare.lighting_god.items.client.FireScythRenderer;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.FireScythDashS2CPacket;
import net.darwindontcare.lighting_god.weapon_powers.FireScythDash;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.RenderUtils;

import java.util.List;
import java.util.function.Consumer;

public class FireScyth extends SwordItem implements GeoItem {
    private static final int  DASH_COOLDOWN = 150;
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public FireScyth(Tier tier, int attackDamage, float attackSpeed, Properties builder) {
        super(tier, attackDamage, attackSpeed, builder);

        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (!level.isClientSide) {
            ItemStack stack = player.getItemInHand(interactionHand);
            if (!player.getCooldowns().isOnCooldown(stack.getItem())) {
                //System.out.println("right clicked");
                player.swing(InteractionHand.MAIN_HAND);
                FireScythDash.Dash((ServerPlayer) player);
                player.getCooldowns().addCooldown(stack.getItem(), DASH_COOLDOWN);
            }
        }
        return super.use(level, player, interactionHand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack p_41409_, Level p_41410_, LivingEntity p_41411_) {
        return super.finishUsingItem(p_41409_, p_41410_, p_41411_);
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity hurtEntity, LivingEntity attacker) {
        Vec3 hurtEntityPos = hurtEntity.position();
        Level attackerLevel = attacker.level();
        List<LivingEntity> nearbyEntities = attackerLevel.getEntitiesOfClass(
                LivingEntity.class,
                new AABB(hurtEntityPos.x - 3, hurtEntityPos.y - 3, hurtEntityPos.z - 3, hurtEntityPos.x + 3, hurtEntityPos.y + 3, hurtEntityPos.z + 3)
        );

        System.out.println("attaker: "+attacker.getName());
        System.out.println("target: "+hurtEntity.getName());

        for(int i = 0; i < 15; i++){attackerLevel.addParticle(ParticleTypes.FLAME, hurtEntity.getX() + hurtEntity.getForward().x, (hurtEntity.getY() + hurtEntity.getForward().y) + hurtEntity.getRandom().nextDouble() * 2.0D, hurtEntity.getZ() + hurtEntity.getForward().z, hurtEntity.getRandom().nextGaussian(), hurtEntity.getRandom().nextGaussian(), hurtEntity.getRandom().nextGaussian());}

        for (LivingEntity entity : nearbyEntities) {
            if (!entity.equals(attacker)) {
                entity.setSecondsOnFire(10);
            }
        }
        return super.hurtEnemy(itemStack, attacker, hurtEntity);
    }


    @Override
    public boolean isFireResistant() {
        return true;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private FireScythRenderer renderer;
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    renderer = new FireScythRenderer();
                }
                return this.renderer;
            }
        });
    }

    private PlayState predicate(AnimationState animationState) {
        return animationState.setAndContinue(IDLE_ANIM);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "scyth_controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object itemStack) {
        return RenderUtils.getCurrentTick();
    }
}
