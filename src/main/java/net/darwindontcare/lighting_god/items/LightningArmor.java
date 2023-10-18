package net.darwindontcare.lighting_god.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LightningArmor extends ArmorItem {

    public LightningArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
        super(p_40386_, p_266831_, p_40388_);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        list.add(Component.literal("20% less cooldown on lightning powers"));
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        super.onArmorTick(stack, level, player);
        MobEffectInstance haste = new MobEffectInstance(MobEffects.DIG_SPEED, 10, 3, false, false);
        player.addEffect(haste);
        if (player.isSprinting()) {
            MobEffectInstance speed = new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 2);
            player.addEffect(speed);
        }
    }
}
