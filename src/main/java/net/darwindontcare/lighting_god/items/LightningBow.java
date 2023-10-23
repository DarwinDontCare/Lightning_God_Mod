package net.darwindontcare.lighting_god.items;

import net.darwindontcare.lighting_god.entities.LightningArrowEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public class LightningBow  extends BowItem {
    public LightningBow(Properties builder) {
        super(builder);
    }

    @Override
    public void releaseUsing(ItemStack p_40667_, Level p_40668_, LivingEntity p_40669_, int p_40670_) {
        super.releaseUsing(p_40667_, p_40668_, p_40669_, p_40670_);
    }

    @Override
    public int getUseDuration(ItemStack p_40680_) {
        return super.getUseDuration(p_40680_);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_40678_) {
        return super.getUseAnimation(p_40678_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_40672_, Player p_40673_, InteractionHand p_40674_) {
        return super.use(p_40672_, p_40673_, p_40674_);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return super.getAllSupportedProjectiles();
    }

    @Override
    public AbstractArrow customArrow(AbstractArrow arrow) {
        LightningArrowEntity lightningArrow = new LightningArrowEntity(EntityType.ARROW, arrow.level());
        lightningArrow.setPos(arrow.position());
        lightningArrow.setDeltaMovement(arrow.getDeltaMovement());
        lightningArrow.setOwner(arrow.getOwner());
        return super.customArrow(lightningArrow);
    }

    @Override
    public int getDefaultProjectileRange() {
        return 3500;
    }
}
