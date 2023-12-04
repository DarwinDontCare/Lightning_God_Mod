package net.darwindontcare.lighting_god.event;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.items.ModItems;
import net.darwindontcare.lighting_god.lightning_powers.EarthStomp;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

@Mod.EventBusSubscriber(modid = LightningGodMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class PlayerTickEventHandler {

    private static int maxTpCooldown = LightningGodMod.getMaxTeleportCooldown();
    private static int maxProcessedTpCooldown = LightningGodMod.getMaxProcessedTeleportCooldown();
    private static int maxElThorCooldown = LightningGodMod.getMaxElThorCooldown();
    private static int maxProcessedElThorCooldown = LightningGodMod.getMaxProcessedElThorCooldown();
    private static int maxFireballCooldown = LightningGodMod.getMaxFireballCooldown();
    private static int maxProcessedFireballCooldown = LightningGodMod.getMaxProcessedFireballCooldown();
    private static int maxFireBurstCooldown = LightningGodMod.getMaxFireBurstCooldown();
    private static int maxProcessedFireBurstCooldown = LightningGodMod.getMaxProcessedFireBurstCooldown();
    private static int maxFreezeCooldown = LightningGodMod.getMaxFreezeCooldown();
    private static int maxProcessedFreezeCooldown = LightningGodMod.getMaxProcessedFreezeCooldown();
    private static int maxIceSlideCooldown = LightningGodMod.getMaxIceSlideCooldown();
    private static int maxProcessedIceSlideCooldown = LightningGodMod.getMaxProcessedIceSlideCooldown();
    private static int maxEarthLaunchCooldown = LightningGodMod.getMaxEarthLaunchCooldown();
    private static int maxProcessedEarthLaunchCooldown = LightningGodMod.getMaxProcessedEarthLaunchCooldown();
    private static int maxEarthTrapCooldown = LightningGodMod.getMaxEarthTrapCooldown();
    private static int maxProcessedEarthTrapCooldown = LightningGodMod.getMaxProcessedEarthTrapCooldown();
    private static int maxEarthMeteorCooldown = LightningGodMod.getMaxEarthMeteorCooldown();
    private static int maxProcessedEarthMeteorCooldown = LightningGodMod.getMaxProcessedEarthMeteorCooldown();
    private static int maxEarthWallCooldown = LightningGodMod.getMaxEarthWallCooldown();
    private static int maxProcessedEarthWallCooldown = LightningGodMod.getMaxProcessedEarthWallCooldown();
    private static int maxFireFlightCooldown = LightningGodMod.getMaxEarthLaunchCooldown();
    private static int maxProcessedFireFlightCooldown = LightningGodMod.getMaxProcessedEarthLaunchCooldown();
    private static int maxFirePullCooldown = LightningGodMod.getMaxFirePullCooldown();
    private static int maxProcessedFirePullCooldown = LightningGodMod.getMaxProcessedFirePullCooldown();
    private static int maxIceSpikeCooldown = LightningGodMod.getMaxIceSpikeCooldown();
    private static int maxProcessedIceSpikeCooldown = LightningGodMod.getMaxProcessedIceSpikeCooldown();
    private static boolean[] appliedLightningArmorBuff = {false, false, false, false};
    private static boolean[] appliedFireArmorBuff = {false, false, false, false};
    private static boolean[] appliedWaterArmorBuff = {false, false, false, false};
    private static boolean[] appliedEarthArmorBuff = {false, false, false, false};

    private static boolean appliedWaterBreathing = false;
    private static boolean appliedFireResistance = false;
    private static int resistanceCooldown = 0;
    private static int fireResistanceTime = 0;

    private static Player currentPlayer;

    private static void addManaBuff(int armorPieceIndex, String type) {
        int currentManaBuff = LightningGodMod.getManaBuff();

        if (type.equals("lightning")) {
            if (armorPieceIndex == 0) LightningGodMod.setManaBuff(currentManaBuff + (LightningGodMod.getMaxMana() * 10) / 100);
            else if (armorPieceIndex == 1) LightningGodMod.setManaBuff(currentManaBuff + (LightningGodMod.getMaxMana() * 10) / 100);
            else if (armorPieceIndex == 2) LightningGodMod.setManaBuff(currentManaBuff + (LightningGodMod.getMaxMana() * 10) / 100);
            else LightningGodMod.setManaBuff(currentManaBuff + (LightningGodMod.getMaxMana() * 10) / 100);
        } else if (type.equals("fire")) {
            if (armorPieceIndex == 0) LightningGodMod.setManaBuff(currentManaBuff + (LightningGodMod.getMaxMana() * 10) / 100);
            else if (armorPieceIndex == 1) LightningGodMod.setManaBuff(currentManaBuff + (LightningGodMod.getMaxMana() * 10) / 100);
            else if (armorPieceIndex == 2) LightningGodMod.setManaBuff(currentManaBuff + (LightningGodMod.getMaxMana() * 10) / 100);
            else LightningGodMod.setManaBuff(currentManaBuff + (LightningGodMod.getMaxMana() * 10) / 100);
        } else if (type.equals("water")) {
            if (armorPieceIndex == 0) LightningGodMod.setManaBuff(currentManaBuff + (LightningGodMod.getMaxMana() * 10) / 100);
            else if (armorPieceIndex == 1) LightningGodMod.setManaBuff(currentManaBuff + (LightningGodMod.getMaxMana() * 10) / 100);
            else if (armorPieceIndex == 2) LightningGodMod.setManaBuff(currentManaBuff + (LightningGodMod.getMaxMana() * 10) / 100);
            else LightningGodMod.setManaBuff(currentManaBuff + (LightningGodMod.getMaxMana() * 10) / 100);
        } else if (type.equals("earth")) {
            if (armorPieceIndex == 0) LightningGodMod.setManaBuff(currentManaBuff + (LightningGodMod.getMaxMana() * 10) / 100);
            else if (armorPieceIndex == 1) LightningGodMod.setManaBuff(currentManaBuff + (LightningGodMod.getMaxMana() * 10) / 100);
            else if (armorPieceIndex == 2) LightningGodMod.setManaBuff(currentManaBuff + (LightningGodMod.getMaxMana() * 10) / 100);
            else LightningGodMod.setManaBuff(currentManaBuff + (LightningGodMod.getMaxMana() * 10) / 100);
        }
    }
    private static void removeManaBuff(int armorPieceIndex, String type) {
        int currentManaBuff = LightningGodMod.getManaBuff();

        if (type.equals("lightning")) {
            if (armorPieceIndex == 0) LightningGodMod.setManaBuff(currentManaBuff - (LightningGodMod.getMaxMana() * 10) / 100);
            else if (armorPieceIndex == 1) LightningGodMod.setManaBuff(currentManaBuff - (LightningGodMod.getMaxMana() * 10) / 100);
            else if (armorPieceIndex == 2) LightningGodMod.setManaBuff(currentManaBuff - (LightningGodMod.getMaxMana() * 10) / 100);
            else LightningGodMod.setManaBuff(currentManaBuff - (LightningGodMod.getMaxMana() * 10) / 100);
        } else if (type.equals("fire")) {
            if (armorPieceIndex == 0) LightningGodMod.setManaBuff(currentManaBuff - (LightningGodMod.getMaxMana() * 10) / 100);
            else if (armorPieceIndex == 1) LightningGodMod.setManaBuff(currentManaBuff - (LightningGodMod.getMaxMana() * 10) / 100);
            else if (armorPieceIndex == 2) LightningGodMod.setManaBuff(currentManaBuff - (LightningGodMod.getMaxMana() * 10) / 100);
            else LightningGodMod.setManaBuff(currentManaBuff - (LightningGodMod.getMaxMana() * 10) / 100);
        } else if (type.equals("water")) {
            if (armorPieceIndex == 0) LightningGodMod.setManaBuff(currentManaBuff - (LightningGodMod.getMaxMana() * 10) / 100);
            else if (armorPieceIndex == 1) LightningGodMod.setManaBuff(currentManaBuff - (LightningGodMod.getMaxMana() * 10) / 100);
            else if (armorPieceIndex == 2) LightningGodMod.setManaBuff(currentManaBuff - (LightningGodMod.getMaxMana() * 10) / 100);
            else LightningGodMod.setManaBuff(currentManaBuff - (LightningGodMod.getMaxMana() * 10) / 100);
        } else if (type.equals("earth")) {
            if (armorPieceIndex == 0) LightningGodMod.setManaBuff(currentManaBuff - (LightningGodMod.getMaxMana() * 10) / 100);
            else if (armorPieceIndex == 1) LightningGodMod.setManaBuff(currentManaBuff - (LightningGodMod.getMaxMana() * 10) / 100);
            else if (armorPieceIndex == 2) LightningGodMod.setManaBuff(currentManaBuff - (LightningGodMod.getMaxMana() * 10) / 100);
            else LightningGodMod.setManaBuff(currentManaBuff - (LightningGodMod.getMaxMana() * 10) / 100);
        }
    }
    
    private static void checkArmorSet(Item[] armorSet, boolean[] appliedArmorBuff, int[] maxCooldown, int[] maxProcessedCooldown, String type) {
        for (int i = 0; i < 4; i++) {
            if (currentPlayer.getInventory().getArmor(i).is(armorSet[i])) {
                if (!appliedArmorBuff[i]) {
                    addManaBuff(i, type);
                    for (int idx = 0; idx < maxCooldown.length; idx++) {
                        maxProcessedCooldown[idx] -= (maxCooldown[idx] * 20) / 100;
                        if (type.equals("lightning")) {
                            if (idx == 0) LightningGodMod.setMaxProcessedTeleportCooldown(maxProcessedCooldown[idx]);
                            if (idx == 1) LightningGodMod.setMaxProcessedElThorCooldown(maxProcessedCooldown[idx]);
                        } else if (type.equals("fire")) {
                            if (i == 0) LightningGodMod.setManaBuff((LightningGodMod.getMaxMana() * 10) / 100);
                            else if (i == 1) LightningGodMod.setManaBuff((LightningGodMod.getMaxMana() * 10) / 100);
                            else if (i == 2) LightningGodMod.setManaBuff((LightningGodMod.getMaxMana() * 10) / 100);
                            else LightningGodMod.setManaBuff((LightningGodMod.getMaxMana() * 10) / 100);
                            if (idx == 0) LightningGodMod.setMaxProcessedFireballCooldown(maxProcessedCooldown[idx]);
                            if (idx == 1) LightningGodMod.setMaxProcessedFirePullCooldown(maxProcessedCooldown[idx]);
                            if (idx == 2) LightningGodMod.setMaxProcessedFireFlightCooldown(maxProcessedCooldown[idx]);
                            if (idx == 3) LightningGodMod.setMaxProcessedFireBurstCooldown(maxProcessedCooldown[idx]);
                        } else if (type.equals("water")) {
                            if (idx == 0) LightningGodMod.setMaxProcessedFreezeCooldown(maxProcessedCooldown[idx]);
                            if (idx == 1) LightningGodMod.setMaxProcessedIceSlideCooldown(maxProcessedCooldown[idx]);
                            if (idx == 2) LightningGodMod.setMaxProcessedIceSpikeCooldown(maxProcessedCooldown[idx]);
                        } else if (type.equals("earth")) {
                            if (idx == 0) LightningGodMod.setMaxProcessedEarthLaunchCooldown(maxProcessedCooldown[idx]);
                            if (idx == 1) LightningGodMod.setMaxProcessedEarthWallCooldown(maxProcessedCooldown[idx]);
                            if (idx == 2) LightningGodMod.setMaxProcessedEarthTrapCooldown(maxProcessedCooldown[idx]);
                            if (idx == 3) LightningGodMod.setMaxProcessedEarthMeteorCooldown(maxProcessedCooldown[idx]);
                        }
                        System.out.println(type +" power "+ idx + ": " + maxProcessedCooldown[idx] + ", power cooldown removed: "+(maxCooldown[idx] * 20) / 100);
                    }
                }
                if (type.equals("lightning") && !appliedLightningArmorBuff[i]) {
                    appliedLightningArmorBuff[i] = true;
                    appliedArmorBuff = appliedLightningArmorBuff;
                } else if (type.equals("fire") && !appliedFireArmorBuff[i]) {
                    appliedFireArmorBuff[i] = true;
                    appliedArmorBuff = appliedFireArmorBuff;
                } else if (type.equals("water") && !appliedWaterArmorBuff[i]) {
                    appliedWaterArmorBuff[i] = true;
                    appliedArmorBuff = appliedWaterArmorBuff;
                } else if (type.equals("earth") && !appliedEarthArmorBuff[i]) {
                    appliedEarthArmorBuff[i] = true;
                    appliedArmorBuff = appliedEarthArmorBuff;
                }
            } else if (appliedArmorBuff[i]) {
                removeManaBuff(i, type);
                for (int idx = 0; idx < maxCooldown.length; idx++) {
                    maxProcessedCooldown[idx] += (maxCooldown[idx] * 20) / 100;
                    if (type.equals("lightning")) {
                        if (idx == 0) LightningGodMod.setMaxProcessedTeleportCooldown(maxProcessedCooldown[idx]);
                        if (idx == 1) LightningGodMod.setMaxProcessedElThorCooldown(maxProcessedCooldown[idx]);
                    } else if (type.equals("fire")) {
                        if (idx == 0) LightningGodMod.setMaxProcessedFireballCooldown(maxProcessedCooldown[idx]);
                        if (idx == 1) LightningGodMod.setMaxProcessedFirePullCooldown(maxProcessedCooldown[idx]);
                        if (idx == 2) LightningGodMod.setMaxProcessedFireFlightCooldown(maxProcessedCooldown[idx]);
                        if (idx == 3) LightningGodMod.setMaxProcessedFireBurstCooldown(maxProcessedCooldown[idx]);
                    } else if (type.equals("water")) {
                        if (idx == 0) LightningGodMod.setMaxProcessedFreezeCooldown(maxProcessedCooldown[idx]);
                        if (idx == 1) LightningGodMod.setMaxProcessedIceSlideCooldown(maxProcessedCooldown[idx]);
                        if (idx == 2) LightningGodMod.setMaxProcessedIceSpikeCooldown(maxProcessedCooldown[idx]);
                    } else if (type.equals("earth")) {
                        if (idx == 0) LightningGodMod.setMaxProcessedEarthLaunchCooldown(maxProcessedCooldown[idx]);
                        if (idx == 1) LightningGodMod.setMaxProcessedEarthWallCooldown(maxProcessedCooldown[idx]);
                        if (idx == 2) LightningGodMod.setMaxProcessedEarthTrapCooldown(maxProcessedCooldown[idx]);
                        if (idx == 3) LightningGodMod.setMaxProcessedEarthMeteorCooldown(maxProcessedCooldown[idx]);
                    }
                    if (type.equals("lightning") && appliedLightningArmorBuff[i]) appliedLightningArmorBuff[i] = false;
                    else if (type.equals("fire") && appliedFireArmorBuff[i]) appliedFireArmorBuff[i] = false;
                    else if (type.equals("water") && appliedWaterArmorBuff[i]) appliedWaterArmorBuff[i] = false;
                    else if (type.equals("earth") && appliedEarthArmorBuff[i]) appliedEarthArmorBuff[i] = false;
                    System.out.println(type + ": " + maxProcessedCooldown[idx]);
                }
            }
        }
    }

    private static boolean setIceSlideAnim = false;
    private static boolean loadedPlayerInfo = false;


    @SubscribeEvent()
    public static void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (event.player != null && !loadedPlayerInfo && !event.player.isDeadOrDying()) {
            try {
                currentPlayer = event.player;
                LightningGodMod.setPlayer(currentPlayer);
            } catch (Exception exception) {
                System.out.println(exception.toString());
            }
            loadedPlayerInfo = true;
        } else if (event.player == null && loadedPlayerInfo || event.player.isDeadOrDying() && loadedPlayerInfo) loadedPlayerInfo = false;
        if (currentPlayer != null) {
            try {
                Item[] lightningArmor = {ModItems.LIGHTNING_BOOTS.get(), ModItems.LIGHTNING_LEGGINGS.get(), ModItems.LIGHTNING_CHESTPLATE.get(), ModItems.LIGHTNING_HELMET.get()};
                Item[] fireArmor = {ModItems.FIRE_BOOTS.get(), ModItems.FIRE_LEGGINGS.get(), ModItems.FIRE_CHESTPLATE.get(), ModItems.FIRE_HELMET.get()};
                Item[] waterArmor = {ModItems.WATER_BOOTS.get(), ModItems.WATER_LEGGINGS.get(), ModItems.WATER_CHESTPLATE.get(), ModItems.WATER_HELMET.get()};
                Item[] earthArmor = {ModItems.EARTH_BOOTS.get(), ModItems.EARTH_LEGGINGS.get(), ModItems.EARTH_CHESTPLATE.get(), ModItems.EARTH_HELMET.get()};

                maxTpCooldown = LightningGodMod.getMaxTeleportCooldown();
                maxProcessedTpCooldown = LightningGodMod.getMaxProcessedTeleportCooldown();
                maxElThorCooldown = LightningGodMod.getMaxElThorCooldown();
                maxProcessedElThorCooldown = LightningGodMod.getMaxProcessedElThorCooldown();

                maxFireballCooldown = LightningGodMod.getMaxFireballCooldown();
                maxProcessedFireballCooldown = LightningGodMod.getMaxProcessedFireballCooldown();
                maxFireBurstCooldown = LightningGodMod.getMaxFireBurstCooldown();
                maxProcessedFireBurstCooldown = LightningGodMod.getMaxProcessedFireBurstCooldown();
                maxFireFlightCooldown = LightningGodMod.getMaxFireFlightCooldown();
                maxProcessedFireFlightCooldown = LightningGodMod.getMaxProcessedFireFlightCooldown();
                maxFirePullCooldown = LightningGodMod.getMaxFirePullCooldown();
                maxProcessedFirePullCooldown = LightningGodMod.getMaxProcessedFirePullCooldown();

                maxFreezeCooldown = LightningGodMod.getMaxFreezeCooldown();
                maxProcessedFreezeCooldown = LightningGodMod.getMaxProcessedFreezeCooldown();
                maxIceSlideCooldown = LightningGodMod.getMaxIceSlideCooldown();
                maxProcessedIceSlideCooldown = LightningGodMod.getMaxProcessedIceSlideCooldown();
                maxIceSpikeCooldown = LightningGodMod.getMaxIceSlideCooldown();
                maxProcessedIceSpikeCooldown = LightningGodMod.getMaxProcessedIceSlideCooldown();

                maxEarthLaunchCooldown = LightningGodMod.getMaxEarthLaunchCooldown();
                maxProcessedEarthLaunchCooldown = LightningGodMod.getMaxProcessedEarthLaunchCooldown();
                maxEarthMeteorCooldown = LightningGodMod.getMaxEarthMeteorCooldown();
                maxProcessedEarthMeteorCooldown = LightningGodMod.getMaxProcessedEarthMeteorCooldown();
                maxEarthTrapCooldown = LightningGodMod.getMaxEarthTrapCooldown();
                maxProcessedEarthTrapCooldown = LightningGodMod.getMaxProcessedEarthTrapCooldown();
                maxEarthWallCooldown = LightningGodMod.getMaxEarthWallCooldown();
                maxProcessedEarthWallCooldown = LightningGodMod.getMaxProcessedEarthWallCooldown();

                int[] fireMaxCooldonws = {maxFireballCooldown, maxFireBurstCooldown, maxFireFlightCooldown, maxFirePullCooldown};
                int[] fireMaxProcessedCooldonws = {maxProcessedFireballCooldown, maxProcessedFireBurstCooldown, maxProcessedFireFlightCooldown, maxProcessedFirePullCooldown};

                int[] lightningMaxCooldonws = {maxTpCooldown, maxElThorCooldown};
                int[] lightningMaxProcessedCooldonws = {maxProcessedTpCooldown, maxProcessedElThorCooldown};

                int[] waterMaxCooldonws = {maxFreezeCooldown, maxIceSlideCooldown, maxIceSpikeCooldown};
                int[] waterMaxProcessedCooldonws = {maxProcessedFreezeCooldown, maxProcessedIceSlideCooldown, maxProcessedIceSpikeCooldown};

                int[] earthMaxCooldonws = {maxEarthLaunchCooldown, maxEarthMeteorCooldown, maxEarthTrapCooldown, maxEarthWallCooldown};
                int[] earthMaxProcessedCooldonws = {maxProcessedEarthLaunchCooldown, maxProcessedEarthMeteorCooldown, maxProcessedEarthTrapCooldown, maxProcessedEarthWallCooldown};

                checkArmorSet(lightningArmor, appliedLightningArmorBuff, lightningMaxCooldonws, lightningMaxProcessedCooldonws, "lightning");
                checkArmorSet(fireArmor, appliedFireArmorBuff, fireMaxCooldonws, fireMaxProcessedCooldonws, "fire");
                checkArmorSet(waterArmor, appliedWaterArmorBuff, waterMaxCooldonws, waterMaxProcessedCooldonws, "water");
                checkArmorSet(earthArmor, appliedEarthArmorBuff, earthMaxCooldonws, earthMaxProcessedCooldonws, "earth");

                if (LightningGodMod.getCurrentPower() != null && LightningGodMod.getCurrentPower().equals("earth")) {
                    if (currentPlayer.isCrouching()) {
                        EarthStomp.StartStomp(LightningGodMod.getPlayer(), LightningGodMod.getPowerTier("earth"));
                    }
                }

                AddPassives();
                if (resistanceCooldown > 0) resistanceCooldown--;
                if (fireResistanceTime > 0) fireResistanceTime--;
            } catch (Exception exception) {
                System.out.println(exception.toString());
            }

            //if (LightningGodMod.getIsIceSliding()) {
                //currentPlayer.walkDist = 1;
                //currentPlayer.walkDistO = 1;
//                if (currentPlayer.isInWater() && !setFireFlightAnim) {
////                    LightningGodMod.StopAnimation("ice_slide");
////                    LightningGodMod.ReproduceAnimation("fire_flyght");
//                    setFireFlightAnim = true;
//                    setIceSlideAnim = false;
//                }
//                else if (!setIceSlideAnim && !currentPlayer.isInWater() && currentPlayer.onGround()) {
//                    LightningGodMod.StopAnimation("fire_flyght");
//                    LightningGodMod.ReproduceAnimation("ice_slide");
//                    setFireFlightAnim = false;
//                    setIceSlideAnim = true;
//                } else if (!currentPlayer.onGround()) {
//                    setIceSlideAnim = false;
//                    setFireFlightAnim = false;
//                    LightningGodMod.StopAnimation("fire_flyght");
//                    LightningGodMod.StopAnimation("ice_slide");
//                }
            //}
        }
    }

    private static void AddPassives() {
        String currentPower = LightningGodMod.getCurrentPower();
        if (currentPower.equals("water")) {
            int tier = LightningGodMod.getPowerTier("water");
            if (tier == 1) ApplyWaterBreathing(100, 1);
            else if (tier == 2) ApplyWaterBreathing(300, 2);
            else if (tier == 3) ApplyWaterBreathing(600, 2);
            else if (tier == 4) ApplyWaterBreathing(10000, 2);
        } else if (currentPower.equals("fire")) {
            int tier = LightningGodMod.getPowerTier("fire");
            if (tier == 1) ApplyFireResistance(100, 2);
            else if (tier == 2) ApplyFireResistance(300, 2);
            else if (tier == 3) ApplyFireResistance(600, 2);
            else if (tier == 4) ApplyFireResistance(10000, 2);
        } else if (currentPower.equals("earth")) {
            int tier = LightningGodMod.getPowerTier("earth");
            if (tier == 1) ApplyResistance(100, 1);
            else if (tier == 2) ApplyResistance(300, 2);
            else if (tier == 3) ApplyResistance(600, 2);
            else if (tier == 4) ApplyResistance(10000, 2);
        } else if (currentPower.equals("lightning")) {
//            int tier = LightningGodMod.getPowerTier("lightning");
//            if (tier == 1) ApplyResistance(90, 1);
//            else if (tier == 2) ApplyResistance(300, 2);
        }
    }

    private static void ApplyWaterBreathing(int time, int amplifier) {
        if (appliedWaterBreathing && !currentPlayer.isInWater()) {
            appliedWaterBreathing = false;
        } else if (!appliedWaterBreathing && currentPlayer.isInWater()) {
            MobEffectInstance effectInstance = new MobEffectInstance(MobEffects.WATER_BREATHING, time, amplifier, false, false);
            currentPlayer.addEffect(effectInstance);
            appliedWaterBreathing = true;
        }
    }

    private static void ApplyFireResistance(int time, int amplifier) {
        if (appliedFireResistance && !currentPlayer.isOnFire()) {
            appliedFireResistance = false;
        } else if (!appliedFireResistance && currentPlayer.isOnFire()) {
            MobEffectInstance effectInstance = new MobEffectInstance(MobEffects.FIRE_RESISTANCE, time, amplifier);
            currentPlayer.addEffect(effectInstance);
            appliedFireResistance = true;
            fireResistanceTime = time;
        }
        if (fireResistanceTime > 0) {
            currentPlayer.setSecondsOnFire(0);
        }
    }

    private static void ApplyResistance(int time, int amplifier) {
        if (resistanceCooldown <= 0 && currentPlayer.isHurt()) {
            MobEffectInstance effectInstance = new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, time, amplifier);
            currentPlayer.addEffect(effectInstance);
            resistanceCooldown = 100;
        }
    }
}
