package net.darwindontcare.lighting_god.event;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.entities.CustomLightningBolt;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LightningGodMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LightningStrikeEvent {
    @SubscribeEvent
    public static void onEntityStruckByLightning(EntityStruckByLightningEvent event) {
        Entity entity = event.getEntity(); // A entidade prestes a ser atingida pelo raio
        LightningBolt lightning = event.getLightning(); // A instância do raio
        if (lightning instanceof CustomLightningBolt) {
            Entity owner = ((CustomLightningBolt) lightning).getOwner();
            if (entity == ((CustomLightningBolt) lightning).getOwner()) {
                event.setCanceled(true);
            } else {
                entity.hurt(owner.damageSources().playerAttack((Player) owner), ((CustomLightningBolt) lightning).getDamage());
                entity.setSecondsOnFire(1);
            }
        }
    }
}
