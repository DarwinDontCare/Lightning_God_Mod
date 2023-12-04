package net.darwindontcare.lighting_god.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.enchantments.EnchantmentsInit;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.SummonParticleC2SPacket;
import net.darwindontcare.lighting_god.utils.AddForceToEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.security.DrbgParameters;
import java.util.ArrayList;

import static com.ibm.icu.lang.UCharacter.GraphemeClusterBreak.T;

@Mod.EventBusSubscriber(modid = LightningGodMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityGlideEvent {
    public static ArrayList<LivingEntity> cancelLivingEntityUpdate = new ArrayList<>();
    public static boolean isJumping = false;
    public static int skyJumpCooldown = 15;
    public static int skyJumpsLeft = -1;
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player) {
            int level = EnchantmentHelper.getEnchantmentLevel(EnchantmentsInit.SKY_JUMP.get(), entity);
            if (level > 0 && skyJumpCooldown <= 0 && isJumping && !((Player)entity).getAbilities().flying) {
                if (skyJumpsLeft <= -1) skyJumpsLeft = level;
                if (skyJumpsLeft > 0) {
                    AddForceToEntity.AddForce(entity, new Vec3(entity.getDeltaMovement().x, 0, entity.getDeltaMovement().z), false);
                    AddForceToEntity.AddForce(entity, new Vec3(entity.getDeltaMovement().x, 0.5, entity.getDeltaMovement().z), false);
                    for(int i = 0; i < 35; i++) {
                        Vec3 movement = new Vec3(entity.getRandom().nextFloat(), entity.getRandom().nextFloat(), entity.getRandom().nextFloat());
                        ModMessage.sendToServer(new SummonParticleC2SPacket(entity.position(), movement, entity.getRandom().nextFloat(), "cloud"));
                    }
                    skyJumpsLeft--;
                    entity.resetFallDistance();
                    entity.level().playSound(null, entity.position().x, entity.position().y, entity.position().z, SoundEvents.ENDER_DRAGON_FLAP, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                    isJumping = false;
                    skyJumpCooldown = 10;
                }
            }
            if (entity.onGround()) {
                isJumping = false;
                skyJumpCooldown = 15;
                skyJumpsLeft = -1;
            }else if (skyJumpCooldown > 0) {
                isJumping = false;
                skyJumpCooldown--;
            }
        }

        try {
            if (!cancelLivingEntityUpdate.isEmpty()) event.setCanceled(cancelLivingEntityUpdate.contains(entity));
        } catch (Exception e) {}
    }

    @SubscribeEvent
    public static void onEntityFallFly(PlayerFlyableFallEvent event) {

    }

    @SubscribeEvent
    public static void onRenderWorldLast(TickEvent.RenderTickEvent event) {

    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Post event) {
//        Player player = event.getEntity();
//        PlayerRenderer render = event.getRenderer();
//        PlayerModel<AbstractClientPlayer> model = render.getModel();
//
//        if(player != null && LightningGodMod.getAlternativeGliding()){
//            ModelPart leftArm = model.leftArm;
//            ModelPart rightArm = model.rightArm;
//            ModelPart leftLeg = model.leftLeg;
//            ModelPart rightLeg = model.rightLeg;
//            ModelPart body = model.body;
//            ModelPart head = model.head;
//
//            PoseStack poseStack = event.getPoseStack();
//            VertexConsumer buffer = event.getMultiBufferSource().getBuffer(model.renderType(((AbstractClientPlayer) player).getSkinTextureLocation()));
//            int light = event.getPackedLight();
//            int texture = OverlayTexture.NO_OVERLAY;
//
//            model.leftArm.copyFrom(leftArm);
//            model.rightArm.copyFrom(rightArm);
//            model.leftLeg.copyFrom(leftLeg);
//            model.rightLeg.copyFrom(rightLeg);
//            model.body.copyFrom(body);
//            model.head.copyFrom(head);
//
//            leftArm.render(poseStack, buffer, light, texture);
//            rightArm.render(poseStack, buffer, light, texture);
//            leftLeg.render(poseStack, buffer, light, texture);
//            rightLeg.render(poseStack, buffer, light, texture);
//            body.render(poseStack, buffer, light, texture);
//            head.render(poseStack, buffer, light, texture);
//        }
    }
}
