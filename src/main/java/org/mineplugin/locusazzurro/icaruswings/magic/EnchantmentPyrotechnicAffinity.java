package org.mineplugin.locusazzurro.icaruswings.magic;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.mineplugin.locusazzurro.icaruswings.registry.EnchantmentRegistry;

@Mod.EventBusSubscriber
public class EnchantmentPyrotechnicAffinity extends WingsEnchantment {

    public EnchantmentPyrotechnicAffinity() {
        super(Rarity.RARE);
    }

    @Override
    public int getMinLevel() {return 1;}

    @Override
    public int getMaxLevel() {return 3;}

    @Override
    public int getMinCost(int lvl) {return lvl * 12;}

    @Override
    public int getMaxCost(int lvl) {return getMinCost(lvl) + 5;}

    @SubscribeEvent
    public static void fireworkExtender(EntityJoinWorldEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof FireworkRocketEntity){
            FireworkRocketEntity firework = (FireworkRocketEntity) entity;
            if (firework.isAttachedToEntity()
                    && firework.attachedToEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() instanceof ElytraItem){
                int enchLvl = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.pyrotechnicAffinity.get(), firework.attachedToEntity);
                firework.lifetime *= 1 + enchLvl * 0.1;
            }
        }
    }

}
