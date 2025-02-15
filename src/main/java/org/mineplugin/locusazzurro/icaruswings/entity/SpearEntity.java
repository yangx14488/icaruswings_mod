package org.mineplugin.locusazzurro.icaruswings.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import org.mineplugin.locusazzurro.icaruswings.items.SpearItem;
import org.mineplugin.locusazzurro.icaruswings.registry.EntityTypeRegistry;
import org.mineplugin.locusazzurro.icaruswings.registry.ItemRegistry;
import org.mineplugin.locusazzurro.icaruswings.registry.SoundRegistry;

import javax.annotation.Nullable;

public class SpearEntity extends AbstractArrowEntity {

    private static final DataParameter<ItemStack> SPEAR_ITEM = EntityDataManager.defineId(SpearEntity.class, DataSerializers.ITEM_STACK);
    private ItemStack spearItem;
    private boolean dealtDamage;

    public SpearEntity(EntityType<? extends SpearEntity> type, World world) {
        super(type, world);
        this.spearItem = new ItemStack(ItemRegistry.woodenSpear.get());
    }

    public SpearEntity(World worldIn, LivingEntity owner, ItemStack spear){
        super(EntityTypeRegistry.spearEntity.get(), owner, worldIn);
        this.spearItem = spear.copy();
        this.entityData.set(SPEAR_ITEM, spear);
    }

    @OnlyIn(Dist.CLIENT)
    public SpearEntity(World p_i48791_1_, double p_i48791_2_, double p_i48791_4_, double p_i48791_6_) {
        super(EntityTypeRegistry.spearEntity.get(), p_i48791_2_, p_i48791_4_, p_i48791_6_, p_i48791_1_);
        this.spearItem = new ItemStack(ItemRegistry.woodenSpear.get());
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        super.tick();
    }

    @Override
    @Nullable
    protected EntityRayTraceResult findHitEntity(Vector3d p_213866_1_, Vector3d p_213866_2_) {
        return this.dealtDamage ? null : super.findHitEntity(p_213866_1_, p_213866_2_);
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult ray) {
        Entity target = ray.getEntity();
        float f = ((SpearItem)this.spearItem.getItem()).getAttackDamage();
        if (target instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)target;
            f += EnchantmentHelper.getDamageBonus(this.spearItem, livingentity.getMobType());
        }

        Entity owner = this.getOwner();
        DamageSource damageSource = new IndirectEntityDamageSource("spear", this, (owner == null ? this : owner)).setProjectile();
        this.dealtDamage = true;
        SoundEvent soundevent = SoundRegistry.spearHit.get();
        if (target.hurt(damageSource, f)) {
            if (target.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (target instanceof LivingEntity) {
                LivingEntity targetLiving = (LivingEntity)target;
                if (owner instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(targetLiving, owner);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)owner, targetLiving);
                }

                this.doPostHurtEffects(targetLiving);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));

        this.playSound(soundevent, 1.0F, 1.0F);
    }

    //TODO: custom sounds
    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundRegistry.spearHitGround.get();
    }

    public void playerTouch(PlayerEntity playerIn) {
        Entity owner = this.getOwner();
        if (owner == null || owner.getUUID() == playerIn.getUUID()) {
            super.playerTouch(playerIn);
        }
    }

    @Override
    public ItemStack getPickupItem() {
        return this.spearItem.copy();
    }

    @Override
    public void tickDespawn() {
        if (this.pickup != AbstractArrowEntity.PickupStatus.ALLOWED) {
            super.tickDespawn();
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPEAR_ITEM, new ItemStack(ItemRegistry.woodenSpear.get()));
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("Spear", 10)) {
            this.spearItem = ItemStack.of(nbt.getCompound("Spear"));
        }

        this.dealtDamage = nbt.getBoolean("DealtDamage");
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.put("Spear", this.spearItem.save(new CompoundNBT()));
        nbt.putBoolean("DealtDamage", this.dealtDamage);
    }

    public void setSpearItemData(ItemStack stackIn){
        this.entityData.set(SPEAR_ITEM, stackIn);
    }

    public ItemStack getSpearItemData(){
        return this.entityData.get(SPEAR_ITEM);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isFoil() {
        return this.entityData.get(SPEAR_ITEM).hasFoil();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldRender(double p_145770_1_, double p_145770_3_, double p_145770_5_) {
        return true;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}

