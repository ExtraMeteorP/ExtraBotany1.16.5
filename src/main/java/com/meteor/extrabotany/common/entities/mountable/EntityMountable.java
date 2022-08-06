package com.meteor.extrabotany.common.entities.mountable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public abstract class EntityMountable extends BoatEntity {

    private static final String TAG_PITCH = "pitch";
    private static final String TAG_ROTATION = "rotation";
    private static final String TAG_MOUNTABLE = "mountable";
    private static final String TAG_OWNERUUID = "owneruuid";

    private static final DataParameter<Float> PITCH = EntityDataManager.createKey(EntityMountable.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Float> ROTATION = EntityDataManager.createKey(EntityMountable.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Boolean> MOUNTABLE = EntityDataManager.createKey(EntityMountable.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Optional<UUID>> OWNER_UUID = EntityDataManager.createKey(EntityMountable.class,
            DataSerializers.OPTIONAL_UNIQUE_ID);

    public boolean ctrlInputDown = false;
    public boolean spaceInputDown = false;

    public EntityMountable(EntityType<? extends BoatEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.preventEntitySpawning = true;
    }

    @Override
    public void tick() {
        if(ticksExisted <= 3)
            return;
        if(getMountable()){
            if(this.getPassengers().isEmpty() || !this.getPassengers().isEmpty() && !(this.getPassengers().get(0).getUniqueID().equals(getOwnerId()))){
                remove();
                return;
            }
        }
        super.tick();
    }

    @Override
    protected SoundEvent getPaddleSound() {
        return null;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(ROTATION, 0F);
        this.dataManager.register(PITCH, 0F);
        this.dataManager.register(MOUNTABLE,false);
        this.dataManager.register(OWNER_UUID, Optional.empty());
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    public Item getItemBoat(){
        return null;
    }

    public ItemStack getItemStack(){
        return getMountable() ? ItemStack.EMPTY : new ItemStack(getItemBoat());
    }

    public void updateInput(boolean ctrlInputDown, boolean spaceInputDown) {
        this.ctrlInputDown = ctrlInputDown;
        this.spaceInputDown = spaceInputDown;
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        setRotation(compound.getFloat(TAG_ROTATION));
        setPitch(compound.getFloat(TAG_PITCH));
        setMountable(compound.getBoolean(TAG_MOUNTABLE));
        setOwnerId(compound.getUniqueId(TAG_OWNERUUID));
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putFloat(TAG_ROTATION, getRotation());
        compound.putFloat(TAG_PITCH, getPitch());
        compound.putBoolean(TAG_MOUNTABLE, getMountable());
        if (this.getOwnerId() != null) {
            compound.putUniqueId(TAG_OWNERUUID, this.getOwnerId());
        }
    }

    @Nullable
    public UUID getOwnerId() {
        return this.dataManager.get(OWNER_UUID).orElse((UUID)null);
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.dataManager.set(OWNER_UUID, Optional.ofNullable(p_184754_1_));
    }

    public void setMountable(boolean b){
        dataManager.set(MOUNTABLE, b);
    }

    public boolean getMountable(){
        return dataManager.get(MOUNTABLE);
    }

    public float getRotation() {
        return dataManager.get(ROTATION);
    }

    public void setRotation(float rot) {
        dataManager.set(ROTATION, rot);
    }

    public float getPitch() {
        return dataManager.get(PITCH);
    }

    public void setPitch(float rot) {
        dataManager.set(PITCH, rot);
    }

}
