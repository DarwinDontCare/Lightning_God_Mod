package net.darwindontcare.lighting_god.entities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class CustomFireball extends LargeFireball {
    private int explosionPower = 1;
    private float damage = 6.0f;
    public CustomFireball(EntityType<? extends LargeFireball> entityType, Level level) {
        super(entityType, level);
    }

    public CustomFireball(Level level, LivingEntity entity, double x, double y, double z, int power) {
        super(level, entity, x, y, z, power);
        this.explosionPower = power;
    }

    @Override
    protected void onHit(HitResult hitResult) {
        //super.onHit(hitResult);
        level.explode(this.getOwner(), this.position().x, this.position().y, this.position().z, explosionPower, true, Level.ExplosionInteraction.NONE);
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        //super.onHitEntity(entityHitResult);
        level.explode(this.getOwner(), this.position().x, this.position().y, this.position().z, explosionPower, true, Level.ExplosionInteraction.NONE);
        if (!this.level.isClientSide) {
            Entity entity = entityHitResult.getEntity();
            Entity entity1 = this.getOwner();
            entity.hurt(this.damageSources().fireball(this, entity1), damage);
            if (entity1 instanceof LivingEntity) {
                this.doEnchantDamageEffects((LivingEntity)entity1, entity);
            }
        }
        //this.discard();
    }

    @Override
    public void doEnchantDamageEffects(LivingEntity p_19971_, Entity p_19972_) {
        super.doEnchantDamageEffects(p_19971_, p_19972_);
    }
}
