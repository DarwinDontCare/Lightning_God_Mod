package net.darwindontcare.lighting_god.event;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.client.PowersCooldown;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.*;
import net.darwindontcare.lighting_god.utils.KeyBindings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
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
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (LightningGodMod.getPlayer() != null) {
                float currentMana = LightningGodMod.getCurrentMana();
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
                        ModMessage.sendToServer(new LightningTeleportC2SPacket(LightningGodMod.getTeleportCooldown(), currentMana));
                    } else if (LightningGodMod.getCurrentPower().equals("fire")) {
                        if (LightningGodMod.getFireballCooldown() <= 0) LightningGodMod.ReproduceAnimation("fireball_cast");
                        ModMessage.sendToServer(new FireballC2SPacket(LightningGodMod.getFireballCooldown(), currentMana));
                    } else if (LightningGodMod.getCurrentPower().equals("water")) {
                        ModMessage.sendToServer(new FreezeC2SPacket(LightningGodMod.getFreezeCooldown(), currentMana));
                    } else if (LightningGodMod.getCurrentPower().equals("earth")) {
                        ModMessage.sendToServer(new EarthLaunchC2SPacket(LightningGodMod.getEarthLaunchCooldown(), currentMana));
                    }
                }
                if (KeyBindings.SECOND_POWER_KEY.consumeClick()) {
                    if (LightningGodMod.getCurrentPower().equals("lightning") && LightningGodMod.getPowerTier("lightning") > 1) {
                        ModMessage.sendToServer(new ElThorC2SPacket(LightningGodMod.getElThorCooldown(), currentMana));
                    } else if (LightningGodMod.getCurrentPower().equals("fire") && LightningGodMod.getPowerTier("fire") > 1) {
                        ModMessage.sendToServer(new FirePullC2SPacket(LightningGodMod.getFirePullCooldown(), currentMana));
                    } else if (LightningGodMod.getCurrentPower().equals("water") && LightningGodMod.getPowerTier("water") > 1) {
                        if (LightningGodMod.getFireFlightCooldown() <= 0) LightningGodMod.setIsIceSliding(true);
                        else LightningGodMod.setIsIceSliding(false);
                        ModMessage.sendToServer(new IceSlideC2SPacket(LightningGodMod.getIceSlideCooldown(), currentMana));
                    } else if (LightningGodMod.getCurrentPower().equals("earth") && LightningGodMod.getPowerTier("earth") > 1) {
                        ModMessage.sendToServer(new EarthWallC2SPacket(LightningGodMod.getEarthWallCooldown(), currentMana));
                    }
                }
                if (KeyBindings.THIRD_POWER_KEY.consumeClick()) {
                    if (LightningGodMod.getCurrentPower().equals("lightning") && LightningGodMod.getPowerTier("lightning") > 2) {
                        //ModMessage.sendToServer(new ElThorC2SPacket(LightningGodMod.getElThorCooldown()));
                    } else if (LightningGodMod.getCurrentPower().equals("fire") && LightningGodMod.getPowerTier("fire") > 2) {
                        if (LightningGodMod.getFireFlightCooldown() <= 0) {
                            LightningGodMod.setAlternativeGliding(true);
                            LightningGodMod.ReproduceAnimation("fire_flyght");
                        }
                        else LightningGodMod.setAlternativeGliding(false);
                        ModMessage.sendToServer(new StartFireFlightC2SPacket(LightningGodMod.getFireFlightCooldown(), currentMana));
                    } else if (LightningGodMod.getCurrentPower().equals("water") && LightningGodMod.getPowerTier("water") > 2) {
                        ModMessage.sendToServer(new IceSpikePowerC2SPacket(LightningGodMod.getIceSpikeCooldown(), currentMana));
                    } else if (LightningGodMod.getCurrentPower().equals("earth") && LightningGodMod.getPowerTier("earth") > 2) {
                        ModMessage.sendToServer(new EarthTrapC2SPacket(LightningGodMod.getEarthTrapCooldown(), currentMana));
                    }
                }
                if (KeyBindings.FORTH_POWER_KEY.consumeClick()) {
                    if (LightningGodMod.getCurrentPower().equals("lightning") && LightningGodMod.getPowerTier("lightning") > 3) {
                        //ModMessage.sendToServer(new ElThorC2SPacket(LightningGodMod.getElThorCooldown()));
                    } else if (LightningGodMod.getCurrentPower().equals("fire") && LightningGodMod.getPowerTier("fire") > 3) {
                        ModMessage.sendToServer(new FireBurstC2SPacket(LightningGodMod.getFireBurstCooldown(), currentMana));
                    } else if (LightningGodMod.getCurrentPower().equals("water") && LightningGodMod.getPowerTier("water") > 3) {
                        //ModMessage.sendToServer(new IceSlideC2SPacket(LightningGodMod.getIceSlideCooldown()));
                    } else if (LightningGodMod.getCurrentPower().equals("earth") && LightningGodMod.getPowerTier("earth") > 3) {
                        if (LightningGodMod.getEarthMeteorCooldown() <= 0) LightningGodMod.ReproduceAnimation("earth_meteor_cast");
                        ModMessage.sendToServer(new EarthMeteorC2SPacket(LightningGodMod.getEarthMeteorCooldown(), currentMana));
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
        public static void mouseEvent(InputEvent.MouseButton event) {
            if (LightningGodMod.getCurrentPower() != null) {
                Player player = LightningGodMod.getPlayer();
                if (event.getButton() == MouseEvent.NOBUTTON) {
                    System.out.println("clicked button " + event.getButton() + " button");
                    BlockPunchEvent.resetHoldingBlock();
                } else if (event.getButton() == MouseEvent.BUTTON1 && LightningGodMod.getCurrentPower().equals("earth")) {
                    for (double reach = 0; reach < player.getBlockReach(); reach++) {
                        Vec3 currentPos = player.getEyePosition().multiply(player.getForward().multiply(new Vec3(reach, reach, reach)));
                        BlockPunchEvent.GrabBlock(new BlockPos((int) currentPos.x, (int) currentPos.y, (int) currentPos.z), player);
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
