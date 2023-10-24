package net.darwindontcare.lighting_god.event;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.client.PowersCooldown;
import net.darwindontcare.lighting_god.lightning_powers.EarthStomp;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.*;
import net.darwindontcare.lighting_god.utils.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = LightningGodMod.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (LightningGodMod.getPlayer() != null) {
                if (event.getAction() == 0) {
                    if (event.getKey() == KeyBindings.SECOND_POWER_KEY.getKey().getValue()) {
                        if (LightningGodMod.getCurrentPower().equals("water")) {
                            ModMessage.sendToServer(new StopIceSlideC2SPacket(LightningGodMod.getIceSlideCooldown()));
                        }
                    }
                    if (event.getKey() == KeyBindings.THIRD_POWER_KEY.getKey().getValue()) {
                        if (LightningGodMod.getCurrentPower().equals("lightning") && LightningGodMod.getPowerTier("lightning") > 2) {
                            //ModMessage.sendToServer(new ElThorC2SPacket(LightningGodMod.getElThorCooldown()));
                        } else if (LightningGodMod.getCurrentPower().equals("fire") && LightningGodMod.getPowerTier("fire") > 2) {
                            ModMessage.sendToServer(new StopFireFlightC2SPacket(LightningGodMod.getFireFlightCooldown()));
                        } else if (LightningGodMod.getCurrentPower().equals("water") && LightningGodMod.getPowerTier("water") > 2) {
                            //ModMessage.sendToServer(new IceSlideC2SPacket(LightningGodMod.getIceSlideCooldown()));
                        } else if (LightningGodMod.getCurrentPower().equals("earth") && LightningGodMod.getPowerTier("earth") > 2) {
                            //ModMessage.sendToServer(new EarthWallC2SPacket(LightningGodMod.getEarthWallCooldown()));
                        }
                    }
                    if (event.getKey() == KeyBindings.SHIFT_KEY.getKey().getValue() || LightningGodMod.getPlayer().isCrouching()) {
                        if (LightningGodMod.getCurrentPower().equals("earth")) {
                            //EarthStomp.StartStomp(LightningGodMod.getPlayer(), LightningGodMod.getPowerTier("earth"));
                        }
                    }
                }

                if (KeyBindings.FIRST_POWER_KEY.consumeClick()) {
                    if (LightningGodMod.getCurrentPower().equals("lightning")) {
                        ModMessage.sendToServer(new LightningC2SPacket(LightningGodMod.getTeleportCooldown()));
                    } else if (LightningGodMod.getCurrentPower().equals("fire")) {
                        ModMessage.sendToServer(new FireballC2SPacket(LightningGodMod.getFireballCooldown()));
                    } else if (LightningGodMod.getCurrentPower().equals("water")) {
                        ModMessage.sendToServer(new FreezeC2SPacket(LightningGodMod.getFreezeCooldown()));
                    } else if (LightningGodMod.getCurrentPower().equals("earth")) {
                        ModMessage.sendToServer(new EarthLaunchC2SPacket(LightningGodMod.getEarthLaunchCooldown()));
                    }
                }
                if (KeyBindings.SECOND_POWER_KEY.consumeClick()) {
                    if (LightningGodMod.getCurrentPower().equals("lightning") && LightningGodMod.getPowerTier("lightning") > 1) {
                        ModMessage.sendToServer(new ElThorC2SPacket(LightningGodMod.getElThorCooldown()));
                    } else if (LightningGodMod.getCurrentPower().equals("fire") && LightningGodMod.getPowerTier("fire") > 1) {
                        ModMessage.sendToServer(new FirePullC2SPacket(LightningGodMod.getFirePullCooldown()));
                    } else if (LightningGodMod.getCurrentPower().equals("water") && LightningGodMod.getPowerTier("water") > 1) {
                        ModMessage.sendToServer(new IceSlideC2SPacket(LightningGodMod.getIceSlideCooldown()));
                    } else if (LightningGodMod.getCurrentPower().equals("earth") && LightningGodMod.getPowerTier("earth") > 1) {
                        ModMessage.sendToServer(new EarthWallC2SPacket(LightningGodMod.getEarthWallCooldown()));
                    }
                }
                if (KeyBindings.THIRD_POWER_KEY.consumeClick()) {
                    if (LightningGodMod.getCurrentPower().equals("lightning") && LightningGodMod.getPowerTier("lightning") > 2) {
                        //ModMessage.sendToServer(new ElThorC2SPacket(LightningGodMod.getElThorCooldown()));
                    } else if (LightningGodMod.getCurrentPower().equals("fire") && LightningGodMod.getPowerTier("fire") > 2) {
                        ModMessage.sendToServer(new StartFireFlightC2SPacket(LightningGodMod.getFireFlightCooldown()));
                    } else if (LightningGodMod.getCurrentPower().equals("water") && LightningGodMod.getPowerTier("water") > 2) {
                        ModMessage.sendToServer(new IceSpikePowerC2SPacket(LightningGodMod.getIceSpikeCooldown()));
                    } else if (LightningGodMod.getCurrentPower().equals("earth") && LightningGodMod.getPowerTier("earth") > 2) {
                        ModMessage.sendToServer(new EarthTrapC2SPacket(LightningGodMod.getEarthTrapCooldown()));
                    }
                }
                if (KeyBindings.FORTH_POWER_KEY.consumeClick()) {
                    if (LightningGodMod.getCurrentPower().equals("lightning") && LightningGodMod.getPowerTier("lightning") > 3) {
                        //ModMessage.sendToServer(new ElThorC2SPacket(LightningGodMod.getElThorCooldown()));
                    } else if (LightningGodMod.getCurrentPower().equals("fire") && LightningGodMod.getPowerTier("fire") > 3) {
                        ModMessage.sendToServer(new FireBurstC2SPacket(LightningGodMod.getFireBurstCooldown()));
                    } else if (LightningGodMod.getCurrentPower().equals("water") && LightningGodMod.getPowerTier("water") > 3) {
                        //ModMessage.sendToServer(new IceSlideC2SPacket(LightningGodMod.getIceSlideCooldown()));
                    } else if (LightningGodMod.getCurrentPower().equals("earth") && LightningGodMod.getPowerTier("earth") > 3) {
                        ModMessage.sendToServer(new EarthMeteorC2SPacket(LightningGodMod.getEarthMeteorCooldown()));
                    }
                }
                if (KeyBindings.SHIFT_KEY.consumeClick()) {
                    if (LightningGodMod.getCurrentPower().equals("earth")) {
                        ModMessage.sendToServer(new StartEarthStompC2SPacket(LightningGodMod.getPowerTier("earth"), LightningGodMod.getEarthLaunchCooldown()));
                    }
                }
                if (KeyBindings.POWER_WHEEL_KEY.consumeClick()) {
                    LightningGodMod.setShowPowerWheel(true);
                } else if (LightningGodMod.getShowPowerWheel()) {
                    LightningGodMod.setShowPowerWheel(false);
                }
            }
        }

        @SubscribeEvent
        public void onMouseEvent(InputEvent.MouseButton event) {
            if(event.getButton() == MouseEvent.BUTTON1) {
                BlockPunchEvent.resetHoldingBlock();
                System.out.println("clicked left button");
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
}
