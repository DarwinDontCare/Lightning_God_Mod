package net.darwindontcare.lighting_god.items;

import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.FireScythDashS2CPacket;
import net.darwindontcare.lighting_god.weapon_powers.FireScythDash;
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

import java.util.List;

public class FireScyth extends SwordItem {
    private static final int  DASH_COOLDOWN = 150;
    private int useTimes = 0;

    public FireScyth(Tier tier, int attackDamage, float attackSpeed, Properties builder) {
        super(tier, attackDamage, attackSpeed, builder);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (!level.isClientSide) {
            ItemStack stack = player.getItemInHand(interactionHand);
            if (!player.getCooldowns().isOnCooldown(stack.getItem()) && useTimes < 1) {
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
}
