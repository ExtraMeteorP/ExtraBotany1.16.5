package com.meteor.extrabotany.common.entities.ego;

import com.meteor.extrabotany.common.entities.ModEntities;
import com.meteor.extrabotany.common.handler.DamageHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import vazkii.botania.client.fx.WispParticleData;

import java.util.List;

public class EntityEGOBeam extends Entity {

    private PlayerEntity target;
    public EntityEGO summoner;

    private static final String TAG_COLOR_R = "colorr";
    private static final String TAG_COLOR_G = "colorg";
    private static final String TAG_COLOR_B = "colorb";
    private static final String TAG_SPEED_MODIFIER = "speedmodifier";
    private static final String TAG_DAMAGE = "damage";

    private static final DataParameter<Float> COLOR_R = EntityDataManager.createKey(EntityEGOBeam.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Float> COLOR_G = EntityDataManager.createKey(EntityEGOBeam.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Float> COLOR_B = EntityDataManager.createKey(EntityEGOBeam.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Float> SPEED_MODIFIER = EntityDataManager.createKey(EntityEGOBeam.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Float> DAMAGE = EntityDataManager.createKey(EntityEGOBeam.class,
            DataSerializers.FLOAT);

    public EntityEGOBeam(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public EntityEGOBeam(World worldIn) {
        super(ModEntities.EGOBEAM, worldIn);
    }

    @Override
    public void tick() {
        super.tick();

        if(ticksExisted >= 460 || world.getDifficulty() == Difficulty.PEACEFUL)
            remove();

        float RANGE = 16F;
        AxisAlignedBB axis = new AxisAlignedBB(getPositionVec().add(-RANGE, -RANGE, -RANGE)
                , getPositionVec().add(RANGE + 1, RANGE + 1, RANGE + 1));

        if(target == null || target.removed){
            List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, axis);
            if(players.size() > 0) {
                target = players.get(0);
            }
        }else{
            Vector3d vecMotion = target.getPositionVec().subtract(this.getPositionVec()).normalize().scale(0.1D).scale(getSpeedModifier());
            Vector3d newVec = this.getPositionVec().add(vecMotion);
            this.setPositionAndUpdate(newVec.x, this.getPosY(), newVec.z);

            float m = 0.35F;
            for (int i = 0; i < 2; i++) {
                WispParticleData data = WispParticleData.wisp(0.5F, 1F, 1F, 1F);
                world.addParticle(data, getPosX(), getPosY(), getPosZ(), (float) (Math.random() - 0.5F) * m, (float) (Math.random() - 0.5F) * m, (float) (Math.random() - 0.5F) * m);
            }
            List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, getBoundingBox().grow(0, 12, 0));
            for (PlayerEntity player : players) {
                if(player.hurtResistantTime <= 5)
                    DamageHandler.INSTANCE.dmg(player, summoner, getBeamDamage(), DamageHandler.INSTANCE.MAGIC_PIERCING);
            }
        }
    }

    @Override
    protected void registerData() {
        dataManager.register(COLOR_R, 0F);
        dataManager.register(COLOR_G, 0F);
        dataManager.register(COLOR_B, 0F);
        dataManager.register(SPEED_MODIFIER, 0F);
        dataManager.register(DAMAGE, 0F);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        setColor(compound.getFloat(TAG_COLOR_R), compound.getFloat(TAG_COLOR_G), compound.getFloat(TAG_COLOR_B));
        setSpeedModifier(compound.getFloat(TAG_SPEED_MODIFIER));
        setBeamDamage(compound.getFloat(TAG_DAMAGE));
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putFloat(TAG_COLOR_R, getColorR());
        compound.putFloat(TAG_COLOR_G, getColorG());
        compound.putFloat(TAG_COLOR_B, getColorB());
        compound.putFloat(TAG_SPEED_MODIFIER, getSpeedModifier());
        compound.putFloat(TAG_DAMAGE, getBeamDamage());
    }

    public float getColorR(){
        return dataManager.get(COLOR_R);
    }

    public float getColorG(){
        return dataManager.get(COLOR_G);
    }

    public float getColorB(){
        return dataManager.get(COLOR_B);
    }

    public float getSpeedModifier(){
        return dataManager.get(SPEED_MODIFIER);
    }

    public float getBeamDamage(){
        return dataManager.get(DAMAGE);
    }

    public void setColorR(float f){
        dataManager.set(COLOR_R, f);
    }

    public void setColorG(float f){
        dataManager.set(COLOR_G, f);
    }

    public void setColorB(float f){
        dataManager.set(COLOR_B, f);
    }

    public void setSpeedModifier(float f){
        dataManager.set(SPEED_MODIFIER, f);
    }

    public void setBeamDamage(float f){
        dataManager.set(DAMAGE, f);
    }

    public void setColor(float r, float g, float b){
        setColorR(r);
        setColorG(g);
        setColorB(b);
    }

    public float[] getBeamColor(){
        return new float[]{getColorR(), getColorG(), getColorB()};
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
