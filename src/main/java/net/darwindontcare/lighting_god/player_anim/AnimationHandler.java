package net.darwindontcare.lighting_god.player_anim;

import com.google.gson.Gson;
import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.utils.JsonReader;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class AnimationHandler {
    public static Animation currentAnimation;
    public static void Play(String animationName) {
        Gson gson = new Gson();
        AnimationData animationData = JsonReader.ReadJson(new ResourceLocation(LightningGodMod.MOD_ID, "player_animation/"+animationName).getPath());

        currentAnimation = animationData.animation;
    }

    public static void tick() {
        for (Bone CurrentBone: currentAnimation.bones.values()) {
            for (Map.Entry<String, Vector> entry : CurrentBone.rotation.entrySet()) {
                String key = entry.getKey();
                Vector transformation = entry.getValue();

                double[] rotationVector = transformation.vector;
            }
        }
    }
}
