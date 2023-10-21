package net.darwindontcare.lighting_god.client;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.gson.AnimationJson;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.darwindontcare.lighting_god.LightningGodMod;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.util.Random;

public class PlayerAnim {
    public static void playEarthMeteorCastAnim() {
        //Use this for setting an animation without fade
        //PlayerAnimTestmod.testAnimation.setAnimation(new KeyframeAnimationPlayer(AnimationRegistry.animations.get("two_handed_vertical_right_right")));

        ModifierLayer<IAnimation> testAnimation;
        if (new Random().nextBoolean()) {
            testAnimation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(Minecraft.getInstance().player).get(new ResourceLocation(LightningGodMod.MOD_ID, "player_animations/earth_meteor_cast.json"));
        } else {
            testAnimation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(Minecraft.getInstance().player).get(new ResourceLocation(LightningGodMod.MOD_ID, "player_animations/earth_meteor_cast.json"));
        }

        if (testAnimation.getAnimation() != null && new Random().nextBoolean()) {
            //It will fade out from the current animation, null as newAnimation means no animation.
            testAnimation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(20, Ease.LINEAR), null);
        } else {
            //Fade from current animation to a new one.
            //Will not fade if there is no animation currently.
            testAnimation.replaceAnimationWithFade(AbstractFadeModifier.functionalFadeIn(20, (modelName, type, value) -> value),
                    new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation(LightningGodMod.MOD_ID, "player_animations/earth_meteor_cast.json")))
                            .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL)
                            .setFirstPersonConfiguration(new FirstPersonConfiguration().setShowRightArm(true).setShowLeftItem(false))
            );
        }


    }
}
