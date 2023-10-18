package net.darwindontcare.lighting_god.items;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Arrays;

public class PowerScroll extends Item {
    private String power = "";
    private int tier = 1;
    public PowerScroll(Item.Properties p_41383_, int tier, String power) {
        super(p_41383_);
        this.tier = tier;
        this.power = power;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 10; // Valor em ticks (1 segundo = 20 ticks)
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        try {
            CompoundTag compoundTag = player.getPersistentData();
            boolean containsPowers = compoundTag.contains("currentPowers");
            boolean containsTiers = compoundTag.contains(this.power+"_tier");
            if (containsPowers) {
                String currentPowers = compoundTag.getString("currentPowers");
                if (!currentPowers.contains(this.power)) {
                    currentPowers += ","+this.power;
                    compoundTag.putString("currentPowers", currentPowers);
                    if (containsTiers) {
                        int power_tier = compoundTag.getInt(this.power+"_tier");
                        if (power_tier < this.tier) compoundTag.putInt(this.power + "_tier", this.tier);
                    } else {
                        compoundTag.putInt(this.power + "_tier", this.tier);
                    }
                    player.addAdditionalSaveData(compoundTag);
                    System.out.println("added power: "+currentPowers);
                } else if (containsTiers) {
                    int power_tier = compoundTag.getInt(this.power+"_tier");
                    if (power_tier < this.tier) {
                        compoundTag.putInt(this.power+"_tier", this.tier);
                        player.addAdditionalSaveData(compoundTag);
                        System.out.println(this.power+" tier: "+currentPowers);
                    }
                }
            } else {
                compoundTag.putString("currentPowers", this.power);
                compoundTag.putInt(this.power+"_tier", this.tier);
                player.addAdditionalSaveData(compoundTag);
                System.out.println(this.power);
            }
        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
        return InteractionResultHolder.consume(player.getItemInHand(interactionHand));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BLOCK;
    }
}
