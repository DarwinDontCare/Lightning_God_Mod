package net.darwindontcare.lighting_god.utils;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static final String KEY_CATEGORY_LIGHTNING_GOD = "key.category.lightning_god.lightning";
    public static final String KEY_FIRST_POWER = "key.lightning_god.first_power";
    public static final String KEY_SECOND_POWER = "key.lightning_god.second_power";
    public static final String KEY_THIRD_POWER = "key.lightning_god.third_power";
    public static final String KEY_FORTH_POWER = "key.lightning_god.fourth_power";
    public static final String KEY_POWER_WHEEL = "key.lightning_god.power_wheel";
    public static final String KEY_SHIFT = "key.lightning_god.shift_key";
    public static final String KEY_SPACE = "key.lightning_god.space_key";

    public static final KeyMapping FIRST_POWER_KEY = new KeyMapping(KEY_FIRST_POWER, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, KEY_CATEGORY_LIGHTNING_GOD);
    public static final KeyMapping SECOND_POWER_KEY = new KeyMapping(KEY_SECOND_POWER, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, KEY_CATEGORY_LIGHTNING_GOD);
    public static final KeyMapping THIRD_POWER_KEY = new KeyMapping(KEY_THIRD_POWER, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_X, KEY_CATEGORY_LIGHTNING_GOD);
    public static final KeyMapping FORTH_POWER_KEY = new KeyMapping(KEY_FORTH_POWER, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, KEY_CATEGORY_LIGHTNING_GOD);
    public static final KeyMapping SHIFT_KEY = new KeyMapping(KEY_SHIFT, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_SHIFT, KEY_CATEGORY_LIGHTNING_GOD);
    public static final KeyMapping SPACE_KEY = new KeyMapping(KEY_SPACE, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_SPACE, KEY_CATEGORY_LIGHTNING_GOD);



    public static final KeyMapping POWER_WHEEL_KEY = new KeyMapping(KEY_POWER_WHEEL, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, KEY_CATEGORY_LIGHTNING_GOD);


    public KeyBindings() {

    }
}
