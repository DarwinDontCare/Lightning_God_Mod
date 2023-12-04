package net.darwindontcare.lighting_god.event;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.client.PowersCooldown;
import net.darwindontcare.lighting_god.lightning_powers.EarthLaunch;
import net.darwindontcare.lighting_god.lightning_powers.FireFlight;
import net.darwindontcare.lighting_god.lightning_powers.IceSlide;
import net.darwindontcare.lighting_god.lightning_powers.LightningBeam;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.*;
import net.darwindontcare.lighting_god.utils.KeyBindings;
import net.darwindontcare.lighting_god.utils.RaycastUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.event.MouseEvent;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = LightningGodMod.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        private static float earthLaunchPower = 1.5f;

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (LightningGodMod.getPlayer() != null && !(Minecraft.getInstance().screen instanceof ChatScreen)) {
//                if (LightningGodMod.getPlayer().getPersistentData().contains("isStuckInPlace") && LightningGodMod.getPlayer().getPersistentData().getBoolean("isStuckInPlace")) {
//                    System.out.println("canceled key binding");
//                    event.setCanceled(true);
//                }
                float currentMana = LightningGodMod.getCurrentMana();
                if (event.getAction() == 0) {
                    if (event.getKey() == KeyBindings.FIRST_POWER_KEY.getKey().getValue()) {
                        if (LightningGodMod.getCurrentPower().equals("earth")) {
                            ModMessage.sendToServer(new EarthLaunchC2SPacket(LightningGodMod.getEarthLaunchCooldown(), currentMana, earthLaunchPower));
                            earthLaunchPower = 1.5f;
                            PowersCooldown.EarthJumpPower = 0;
                        }
                    }
                    if (event.getKey() == KeyBindings.SECOND_POWER_KEY.getKey().getValue()) {
                        if (LightningGodMod.getCurrentPower().equals("water")) {
                            ModMessage.sendToServer(new StopIceSlideC2SPacket(LightningGodMod.getIceSlideCooldown()));
                        }
                    }
                    if (event.getKey() == KeyBindings.THIRD_POWER_KEY.getKey().getValue()) {
                        if (LightningGodMod.getCurrentPower().equals("lightning") && LightningGodMod.getPowerTier("lightning") > 2) {
                            ModMessage.sendToServer(new StopLightningBeamC2SPacket(LightningGodMod.getLightningBeamCooldown()));
                        } else if (LightningGodMod.getCurrentPower().equals("fire") && LightningGodMod.getPowerTier("fire") > 2) {
                            ModMessage.sendToServer(new StopFireFlightC2SPacket(LightningGodMod.getFireFlightCooldown()));
                        } else if (LightningGodMod.getCurrentPower().equals("water") && LightningGodMod.getPowerTier("water") > 2) {
                            //ModMessage.sendToServer(new IceSlideC2SPacket(LightningGodMod.getIceSlideCooldown()));
                        } else if (LightningGodMod.getCurrentPower().equals("earth") && LightningGodMod.getPowerTier("earth") > 2) {
                            //ModMessage.sendToServer(new EarthWallC2SPacket(LightningGodMod.getEarthWallCooldown()));
                        }
                    }
                    if (!LightningGodMod.getPlayer().isShiftKeyDown() && !LightningGodMod.getPlayer().isCrouching()) {
                        if (LightningGodMod.getCurrentPower().equals("earth")) {
                            ModMessage.sendToServer(new StopEarthStompC2SPacket());
                        }
                    }
                }
            }
        }
        private static final float[] FORCE_TABLE = {3, 4, 5, 6};
        public static void calcEarthLaunchForce(int cooldown, int level) {
            float maxForce = FORCE_TABLE[level - 1];
            if (cooldown <= 0 && earthLaunchPower < maxForce && LightningGodMod.getCurrentMana() >= EarthLaunch.ManaCost) {
                earthLaunchPower += 0.1f;
                PowersCooldown.EarthJumpPower = earthLaunchPower;
                PowersCooldown.MaxEarthJumpPower = maxForce;
            }
        }


        @SubscribeEvent
        public static void mouseEvent(InputEvent.MouseButton event) {
            if (LightningGodMod.getCurrentPower() != null) {
                Player player = LightningGodMod.getPlayer();
                if (event.getButton() == MouseEvent.NOBUTTON) {
                    if (LightningGodMod.getCurrentPower().equals("earth")) {
                        HitResult hitResult = player.pick(player.getBlockReach(), 1, true);
                        if (hitResult.getType() == HitResult.Type.BLOCK && !BlockPunchEvent.isHoldingBlock()) {
                            BlockPos currentBlockPos = ((BlockHitResult)hitResult).getBlockPos();
                            BlockPunchEvent.LaunchBlock(currentBlockPos, player);
                        } else {
                            BlockPunchEvent.resetHoldingBlock(player);
                        }
                    }
                }
                if (event.getButton() == MouseEvent.BUTTON1) {
                    if (LightningGodMod.getCurrentPower().equals("earth") && !BlockPunchEvent.isHoldingBlock()) {
                        HitResult hitResult = player.pick(player.getBlockReach(), 1, true);
                        if (hitResult.getType() == HitResult.Type.BLOCK) {
                            BlockPos currentBlockPos = ((BlockHitResult)hitResult).getBlockPos();
                            BlockPunchEvent.GrabBlock(currentBlockPos, player);
                        }
                    }
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = LightningGodMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvent {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBindings.FIRST_POWER_KEY);
            event.register(KeyBindings.SECOND_POWER_KEY);
            event.register(KeyBindings.THIRD_POWER_KEY);
            event.register(KeyBindings.FORTH_POWER_KEY);
            event.register(KeyBindings.POWER_WHEEL_KEY);
        }

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("powers", PowersCooldown.HUD_POWERS);
        }
    }

    @Mod.EventBusSubscriber(modid = LightningGodMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class GeneralEvents {
        @SubscribeEvent
        public static void onEntityAttributeModificationEvent(final EntityAttributeModificationEvent event) {
            event.add(EntityType.PLAYER, ForgeMod.ENTITY_REACH.get());
        }
    }
}
