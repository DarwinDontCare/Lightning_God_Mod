package net.darwindontcare.lighting_god.entities;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;

public class LightningArrowEntity extends AbstractArrow {
    public LightningArrowEntity(EntityType<? extends AbstractArrow> p_36721_, Level p_36722_) {
        super(p_36721_, p_36722_);
    }

    protected LightningArrowEntity(EntityType<? extends AbstractArrow> p_36711_, double p_36712_, double p_36713_, double p_36714_, Level p_36715_) {
        super(p_36711_, p_36712_, p_36713_, p_36714_, p_36715_);
    }

    public LightningArrowEntity(EntityType<? extends AbstractArrow> p_36717_, LivingEntity p_36718_, Level p_36719_) {
        super(p_36717_, p_36718_, p_36719_);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public double getBaseDamage() {
        return 20;
    }

    @Override
    public void setPierceLevel(byte p_36768_) {
        p_36768_ = 100;
        super.setPierceLevel(p_36768_);
    }

    @Override
    protected void onHitEntity(EntityHitResult ray) {
        super.onHitEntity(ray);
        CustomLightningBolt lightningBolt = new CustomLightningBolt(EntityInit.CUSTOM_LIGHTNING.get(), level(), false, getOwner());
        lightningBolt.setPos(ray.getLocation());
        lightningBolt.setVisualOnly(true);
        this.level().addFreshEntity(lightningBolt);
    }

    @Override
    protected void tickDespawn() {
        if (this.inGroundTime > 0){
            CustomLightningBolt lightningBolt = new CustomLightningBolt(EntityInit.CUSTOM_LIGHTNING.get(), level(), false, getOwner());
            lightningBolt.setPos(this.position());
            lightningBolt.setVisualOnly(true);
            this.level().addFreshEntity(lightningBolt);
            this.discard();
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
