package org.mineplugin.locusazzurro.icaruswings.magic;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.mineplugin.locusazzurro.icaruswings.registry.EnchantmentRegistry;

@EventBusSubscriber
public class EnchantmentBlessingOfTheSky extends WingsEnchantment {

    public EnchantmentBlessingOfTheSky() {
        super(Rarity.VERY_RARE);
    }

    @Override
    public int getMinLevel() {return 1;}

    @Override
    public int getMaxLevel() {return 1;}

    @Override
    public int getMinCost(int lvl) {return lvl * 10;}

    @Override
    public int getMaxCost(int lvl) {return getMinCost(lvl) + 5;}

    @Override
    public boolean isTreasureOnly() {return true;}

    @Override
    public boolean isDiscoverable() {return false;}

    private static boolean isValidDamageType(DamageSource damage){
        return damage.isExplosion() || damage.isFire() || damage.isProjectile() || damage.isMagic();
    }

    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent e) {
        if (e.getEntity() instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) e.getEntity();
            ItemStack armor = livingEntity.getItemBySlot(EquipmentSlotType.CHEST);
            if (livingEntity.isFallFlying() && armor.getItem() instanceof ElytraItem
                && EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.blessingOfTheSky.get(), livingEntity) > 0
                && isValidDamageType(e.getSource())){
                e.setAmount(e.getAmount() / 2);
            }
        }
    }

}
