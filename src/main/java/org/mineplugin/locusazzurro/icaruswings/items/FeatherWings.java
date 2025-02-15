package org.mineplugin.locusazzurro.icaruswings.items;

import org.mineplugin.locusazzurro.icaruswings.data.WingsType;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class FeatherWings extends AbstractWings {

	public FeatherWings(WingsType type) {
		super(type);
	}
	
	@Deprecated
	public FeatherWings() {
		this(WingsType.FEATHER);
	}

	@Override
	public boolean canElytraFly(ItemStack stack, net.minecraft.entity.LivingEntity entity) {
		return entity.level.dimension() != World.END;
	}

	@Override
	public boolean elytraFlightTick(ItemStack stack, net.minecraft.entity.LivingEntity entity, int flightTicks) {
		if (!entity.level.isClientSide() && (flightTicks + 1) % 20 == 0) {
			int dmg = 1;
			if ((entity instanceof PlayerEntity) && (stack.getItem() instanceof FeatherWings)) {
				if (entity.level.dimension() == World.NETHER) {
					dmg = 5;
					if(((FeatherWings)stack.getItem()).getType() != WingsType.FEATHER_GOLDEN)
					entity.setSecondsOnFire(10);
				}
				else {
					int y = (int) entity.getY();
					dmg = randomRound(Math.max(1, y / 64.0), entity.level.getRandom());
				}
			}
			stack.hurtAndBreak(dmg, entity,	e -> e.broadcastBreakEvent(EquipmentSlotType.CHEST));
			return true;
		}
		return true;
	}

	int randomRound(double value, Random random) {
		int intValue = (int)Math.floor(value);
		if (random.nextDouble() < value - intValue) intValue++;
		return intValue;
	}


}
