package org.mineplugin.locusazzurro.icaruswings.magic;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;

public abstract class WingsEnchantment extends Enchantment {

    protected WingsEnchantment(Rarity rarity) {
        super(rarity, EnchantmentType.WEARABLE, new EquipmentSlotType[] {EquipmentSlotType.CHEST});
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() instanceof ElytraItem;
    }

    protected static int getWingsEnchantmentLevel(Entity entityIn, Enchantment enchantment){
        if (entityIn instanceof LivingEntity && enchantment instanceof WingsEnchantment){
            LivingEntity livingEntity = (LivingEntity) entityIn;
            if (livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() instanceof ElytraItem){
                return EnchantmentHelper.getEnchantmentLevel(enchantment, livingEntity);
            }
        }
        return 0;
    }


}
