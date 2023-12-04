package net.darwindontcare.lighting_god.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class SkyJumpEnchantment extends Enchantment {

    public SkyJumpEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET});
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return super.checkCompatibility(other) && other != Enchantments.FROST_WALKER;
    }

}
