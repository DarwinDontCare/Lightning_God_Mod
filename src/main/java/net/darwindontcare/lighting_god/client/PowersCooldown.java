package net.darwindontcare.lighting_god.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.event.PlayerTickEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;

public class PowersCooldown {
    private static final ResourceLocation TELEPORTATION_ICON = new ResourceLocation(LightningGodMod.MOD_ID, "textures/icons/tp_icon.png");
    private static final ResourceLocation FIREBALL_ICON = new ResourceLocation(LightningGodMod.MOD_ID, "textures/icons/fire_ball_icon.png");
    private static final ResourceLocation FREEZE_ICON = new ResourceLocation(LightningGodMod.MOD_ID, "textures/icons/freeze_icon.png");
    private static final ResourceLocation EL_THOR_ICON = new ResourceLocation(LightningGodMod.MOD_ID, "textures/icons/el_thor_icon.png");
    private static final ResourceLocation ICE_SLIDE_ICON = new ResourceLocation(LightningGodMod.MOD_ID, "textures/icons/ice_slide_icon.png");
    private static final ResourceLocation FIRE_BURST_ICON = new ResourceLocation(LightningGodMod.MOD_ID, "textures/icons/fire_burst_icon.png");
    private static final ResourceLocation FIRE_FLIGHT_ICON = new ResourceLocation(LightningGodMod.MOD_ID, "textures/icons/fire_flight_icon.png");
    private static final ResourceLocation EARTH_LAUNCH_ICON = new ResourceLocation(LightningGodMod.MOD_ID, "textures/icons/earth_launch_icon.png");
    private static final ResourceLocation EARTH_WALL_ICON = new ResourceLocation(LightningGodMod.MOD_ID, "textures/icons/earth_wall_icon.png");
    private static final ResourceLocation EARTH_METEOR_ICON = new ResourceLocation(LightningGodMod.MOD_ID, "textures/icons/earth_meteor_icon.png");
    private static final ResourceLocation EARTH_TRAP_ICON = new ResourceLocation(LightningGodMod.MOD_ID, "textures/icons/earth_trap_icon.png");
    private static final ResourceLocation POWER_COOLDOWN = new ResourceLocation(LightningGodMod.MOD_ID, "textures/icons/power_cooldown.png");

    private static final ResourceLocation POWER_WHEEL = new ResourceLocation(LightningGodMod.MOD_ID, "textures/ui/power_selector/roda-de-poderes.png");
    private static final ResourceLocation POWER_WHEEL_BACKGROUND = new ResourceLocation(LightningGodMod.MOD_ID, "textures/ui/power_selector/vinheta-escura.png");
    private static final ResourceLocation POWER_WHEEL_FIRE = new ResourceLocation(LightningGodMod.MOD_ID, "textures/ui/power_selector/fire.png");
    private static final ResourceLocation POWER_WHEEL_WATER = new ResourceLocation(LightningGodMod.MOD_ID, "textures/ui/power_selector/water.png");
    private static final ResourceLocation POWER_WHEEL_LIGHTNING = new ResourceLocation(LightningGodMod.MOD_ID, "textures/ui/power_selector/lighting.png");
    private static final ResourceLocation POWER_WHEEL_EARTH = new ResourceLocation(LightningGodMod.MOD_ID, "textures/ui/power_selector/earth.png");
    private static final ResourceLocation FIRE_GLARE = new ResourceLocation(LightningGodMod.MOD_ID, "textures/ui/power_selector/destaque-fogo.png");
    private static final ResourceLocation WATER_GLARE = new ResourceLocation(LightningGodMod.MOD_ID, "textures/ui/power_selector/destaque-agua.png");
    private static final ResourceLocation LIGHTNING_GLARE = new ResourceLocation(LightningGodMod.MOD_ID, "textures/ui/power_selector/destaque-raio.png");
    private static final ResourceLocation EARTH_GLARE = new ResourceLocation(LightningGodMod.MOD_ID, "textures/ui/power_selector/destaque-terra.png");
    private static final ResourceLocation FIRE_GLOW = new ResourceLocation(LightningGodMod.MOD_ID, "textures/ui/power_selector/brilho-fogo.png");
    private static final ResourceLocation WATER_GLOW = new ResourceLocation(LightningGodMod.MOD_ID, "textures/ui/power_selector/brilho-agua.png");
    private static final ResourceLocation LIGHTNING_GLOW = new ResourceLocation(LightningGodMod.MOD_ID, "textures/ui/power_selector/brilho-raio.png");
    private static final ResourceLocation EARTH_GLOW = new ResourceLocation(LightningGodMod.MOD_ID, "textures/ui/power_selector/brilho-terra.png");
    private static final ResourceLocation FIRE_LOCK = new ResourceLocation(LightningGodMod.MOD_ID, "textures/ui/power_selector/lock_fire.png");
    private static final ResourceLocation WATER_LOCK = new ResourceLocation(LightningGodMod.MOD_ID, "textures/ui/power_selector/lock_water.png");
    private static final ResourceLocation LIGHTNING_LOCK = new ResourceLocation(LightningGodMod.MOD_ID, "textures/ui/power_selector/lock_lightning.png");
    private static final ResourceLocation EARTH_LOCK = new ResourceLocation(LightningGodMod.MOD_ID, "textures/ui/power_selector/lock_earth.png");
    private static boolean isMouseLocked = true;

    public static final IGuiOverlay HUD_POWERS = ((gui, poseStack, partialTick, width, height) -> {

        String currentPower = LightningGodMod.getCurrentPower();
        if (currentPower != null) {
            if (currentPower.equals("lightning")) {
                LightningPowerUI(width, poseStack, height, gui);
            } else if (currentPower.equals("fire")) {
                FirePowerUI(width, poseStack, height, gui);
            } else if (currentPower.equals("water")) {
                WaterPowerUI(width, poseStack, height, gui);
            } else if (currentPower.equals("earth")) {
                EarthPowerUI(width, poseStack, height, gui);
            }
        }
        if (LightningGodMod.getShowPowerWheel()) PowerWheel(width, poseStack, height, gui, currentPower);
        else if (!isMouseLocked) {
            gui.getMinecraft().mouseHandler.grabMouse();
            isMouseLocked = true;
        }
    });

    private static void LightningPowerUI(int width, PoseStack poseStack, int height, ForgeGui gui) {
        LoadPowerIcon(poseStack, width, height, TELEPORTATION_ICON, 26, 40, LightningGodMod.getTeleportCooldown(), LightningGodMod.getMaxProcessedTeleportCooldown());
        if (LightningGodMod.getPowerTier("lightning") > 1) LoadPowerIcon(poseStack, width, height, EL_THOR_ICON, 26, 58, LightningGodMod.getElThorCooldown(), LightningGodMod.getMaxProcessedElThorCooldown());
    }

    private static void FirePowerUI(int width, PoseStack poseStack, int height, ForgeGui gui) {
        LoadPowerIcon(poseStack, width, height, FIREBALL_ICON, 26, 40, LightningGodMod.getFireballCooldown(), LightningGodMod.getMaxProcessedFireballCooldown());
        if (LightningGodMod.getPowerTier("fire") > 1) LoadPowerIcon(poseStack, width, height, FIRE_BURST_ICON, 26, 58, LightningGodMod.getFireBurstCooldown(), LightningGodMod.getMaxProcessedFireBurstCooldown());
        if (LightningGodMod.getPowerTier("fire") > 2) LoadPowerIcon(poseStack, width, height, FIRE_FLIGHT_ICON, 26, 76, LightningGodMod.getFireFlightCooldown(), LightningGodMod.getMaxProcessedFireFlightCooldown());

    }

    private static void WaterPowerUI(int width, PoseStack poseStack, int height, ForgeGui gui) {
        LoadPowerIcon(poseStack, width, height, FREEZE_ICON, 26, 40, LightningGodMod.getFreezeCooldown(), LightningGodMod.getMaxProcessedFreezeCooldown());
        if (LightningGodMod.getPowerTier("water") > 1) LoadPowerIcon(poseStack, width, height, ICE_SLIDE_ICON, 26, 58, LightningGodMod.getIceSlideCooldown(), LightningGodMod.getMaxProcessedIceSlideCooldown());
    }

    private static void EarthPowerUI(int width, PoseStack poseStack, int height, ForgeGui gui) {
        LoadPowerIcon(poseStack, width, height, EARTH_LAUNCH_ICON, 26, 40, LightningGodMod.getEarthLaunchCooldown(), LightningGodMod.getMaxProcessedEarthLaunchCooldown());
        if (LightningGodMod.getPowerTier("earth") > 1) LoadPowerIcon(poseStack, width, height, EARTH_WALL_ICON, 26, 58, LightningGodMod.getEarthWallCooldown(), LightningGodMod.getMaxProcessedEarthWallCooldown());
        if (LightningGodMod.getPowerTier("earth") > 2) LoadPowerIcon(poseStack, width, height, EARTH_TRAP_ICON, 26, 76, LightningGodMod.getEarthTrapCooldown(), LightningGodMod.getMaxProcessedEarthTrapCooldown());
        if (LightningGodMod.getPowerTier("earth") > 3) LoadPowerIcon(poseStack, width, height, EARTH_METEOR_ICON, 26, 95, LightningGodMod.getEarthMeteorCooldown(), LightningGodMod.getMaxProcessedEarthMeteorCooldown());
    }


    private static void PowerWheel(int width, PoseStack poseStack, int height, ForgeGui gui, String currentPower) {
        ArrayList<String> power = LightningGodMod.getPowers();
        RenderSystem.setShaderTexture(0, POWER_WHEEL_BACKGROUND);
        GuiComponent.blit(poseStack, 0, 0, 0, 0, width, height, width, height);
        RenderSystem.setShaderTexture(0, POWER_WHEEL);
        GuiComponent.blit(poseStack, width / 2 - 103, height / 2 - 100, 0, 0, 200, 200, 200, 200);
        if (power.contains("fire")) {
            RenderSystem.setShaderTexture(0, POWER_WHEEL_FIRE);
            GuiComponent.blit(poseStack, width / 2 - 103, height / 2 - 100, 0, 0, 200, 200, 200, 200);
        } else {
            RenderSystem.setShaderTexture(0, FIRE_LOCK);
            GuiComponent.blit(poseStack, width / 2 - 103, height / 2 - 100, 0, 0, 200, 200, 200, 200);
        } if (power.contains("water")) {
            RenderSystem.setShaderTexture(0, POWER_WHEEL_WATER);
            GuiComponent.blit(poseStack, width / 2 - 103, height / 2 - 100, 0, 0, 200, 200, 200, 200);
        } else {
            RenderSystem.setShaderTexture(0, WATER_LOCK);
            GuiComponent.blit(poseStack, width / 2 - 103, height / 2 - 100, 0, 0, 200, 200, 200, 200);
        } if (power.contains("lightning")) {
            RenderSystem.setShaderTexture(0, POWER_WHEEL_LIGHTNING);
            GuiComponent.blit(poseStack, width / 2 - 103, height / 2 - 100, 0, 0, 200, 200, 200, 200);
        } else {
            RenderSystem.setShaderTexture(0, LIGHTNING_LOCK);
            GuiComponent.blit(poseStack, width / 2 - 103, height / 2 - 100, 0, 0, 200, 200, 200, 200);
        } if (power.contains("earth")) {
            RenderSystem.setShaderTexture(0, POWER_WHEEL_EARTH);
            GuiComponent.blit(poseStack, width / 2 - 103, height / 2 - 100, 0, 0, 200, 200, 200, 200);
        } else {
            RenderSystem.setShaderTexture(0, EARTH_LOCK);
            GuiComponent.blit(poseStack, width / 2 - 103, height / 2 - 100, 0, 0, 200, 200, 200, 200);
        }

        gui.getMinecraft().mouseHandler.releaseMouse();
        double screenWidth = Minecraft.getInstance().getWindow().getWidth();
        double screenHeight = Minecraft.getInstance().getWindow().getHeight();
        double mouseX = gui.getMinecraft().mouseHandler.xpos();
        double mouseY = gui.getMinecraft().mouseHandler.ypos();

        mouseX = (mouseX * 100) / screenWidth;
        mouseX = (width * mouseX) / 100;

        mouseY = (mouseY * 100) / screenHeight;
        mouseY = (height * mouseY) / 100;

        int offset = 50;

        if (isMouseLocked) isMouseLocked = false;

        if (mouseX < (int) (width / 2) - offset && mouseY < (int) (height / 2.5) + offset && mouseY > (int) (height / 2.3) - offset && power.contains("fire")) {
            RenderSystem.setShaderTexture(0, FIRE_GLARE);
            GuiComponent.blit(poseStack, width / 2 - 103, height / 2 - 100, 0, 0,200, 200, 200, 200);
            RenderSystem.setShaderTexture(0, FIRE_GLOW);
            GuiComponent.blit(poseStack, width / 2 - 103, height / 2 - 100, 0, 0, 200, 200, 200, 200);
            if (!currentPower.equals("fire")) {
                LightningGodMod.setCurrentPower("fire");
                System.out.println(LightningGodMod.getCurrentPower());
            }
        } else if (mouseX > (int) (width / 2.1) + offset && mouseY < (int) (height / 2.5) + offset && mouseY > (int) (height / 2.3) - offset && power.contains("water")) {
            RenderSystem.setShaderTexture(0, WATER_GLARE);
            GuiComponent.blit(poseStack, width / 2 - 103, height / 2 - 100, 0, 0, 200, 200, 200, 200);
            RenderSystem.setShaderTexture(0, WATER_GLOW);
            GuiComponent.blit(poseStack, width / 2 - 103, height / 2 - 100, 0, 0, 200, 200, 200, 200);
            if (!currentPower.equals("water")) {
                LightningGodMod.setCurrentPower("water");
                System.out.println(LightningGodMod.getCurrentPower());
            }
        } else if (mouseY < (int) (height / 2.2) - offset && power.contains("lightning")) {
            RenderSystem.setShaderTexture(0, LIGHTNING_GLARE);
            GuiComponent.blit(poseStack, width / 2 - 103, height / 2 - 100, 0, 0, 200, 200, 200, 200);
            RenderSystem.setShaderTexture(0, LIGHTNING_GLOW);
            GuiComponent.blit(poseStack, width / 2 - 103, height / 2 - 100, 0, 0, 200, 200, 200, 200);
            if (!currentPower.equals("lightning")) {
                LightningGodMod.setCurrentPower("lightning");
                System.out.println(LightningGodMod.getCurrentPower());
            }
        } else if (mouseY > (int) (height / 2.5) + offset && power.contains("earth")) {
            RenderSystem.setShaderTexture(0, EARTH_GLARE);
            GuiComponent.blit(poseStack, width / 2 - 103, height / 2 - 100, 0, 0, 200, 200, 200, 200);
            RenderSystem.setShaderTexture(0, EARTH_GLOW);
            GuiComponent.blit(poseStack, width / 2 - 103, height / 2 - 100, 0, 0, 200, 200, 200, 200);
            if (!currentPower.equals("earth")) {
                LightningGodMod.setCurrentPower("earth");
                System.out.println(LightningGodMod.getCurrentPower());
            }
        }
    }

    private static void LoadPowerIcon(PoseStack poseStack, int width, int height, ResourceLocation icon, int offsetWidth, int offsetHeight, int cooldown, int maxCooldown) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, icon);
        GuiComponent.blit(poseStack, width - offsetWidth, height - offsetHeight, 0, 0, 16, 16, 16, 16);

        if (cooldown > 0 && !Minecraft.getInstance().isPaused()) {
            RenderSystem.setShaderTexture(0, POWER_COOLDOWN);
            int cooldown_width = (((cooldown * 100) / maxCooldown) * 16) / 100;
            GuiComponent.blit(poseStack, width - offsetWidth, height - offsetHeight, 0, 0, cooldown_width, 16, 16, 16);
        }
    }
}
