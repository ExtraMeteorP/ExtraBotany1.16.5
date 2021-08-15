package com.meteor.extrabotany.common.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.List;

public class EntityPhantomSword extends ThrowableEntity {

    public static final int LIVE_TICKS = 30;

    private static final String TAG_VARIETY = "variety";
    private static final String TAG_ROTATION = "rotation";
    private static final String TAG_PITCH = "pitch";
    private static final String TAG_TARGETPOS = "targetpos";
    private static final String TAG_DELAY = "delay";
    private static final String TAG_FAKE = "fake";

    private static final DataParameter<Integer> VARIETY = EntityDataManager.createKey(EntityPhantomSword.class,
            DataSerializers.VARINT);
    private static final DataParameter<Integer> DELAY = EntityDataManager.createKey(EntityPhantomSword.class,
            DataSerializers.VARINT);
    private static final DataParameter<Float> ROTATION = EntityDataManager.createKey(EntityPhantomSword.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Float> PITCH = EntityDataManager.createKey(EntityPhantomSword.class,
            DataSerializers.FLOAT);
    private static final DataParameter<BlockPos> TARGET_POS = EntityDataManager.createKey(EntityPhantomSword.class,
            DataSerializers.BLOCK_POS);
    private static final DataParameter<Boolean> FAKE = EntityDataManager.createKey(EntityPhantomSword.class,
            DataSerializers.BOOLEAN);

    private static final float rgb[][] = { { 0.82F, 0.2F, 0.58F }, { 0F, 0.71F, 0.10F }, { 0.74F, 0.07F, 0.32F },
            { 0.01F, 0.45F, 0.8F }, { 0.05F, 0.39F, 0.9F }, { 0.38F, 0.34F, 0.42F }, { 0.41F, 0.31F, 0.14F },
            { 0.92F, 0.92F, 0.21F }, { 0.61F, 0.92F, 0.98F }, { 0.18F, 0.45F, 0.43F } };

    private LivingEntity thrower;

    public EntityPhantomSword(EntityType<EntityPhantomSword> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityPhantomSword(World worldIn) {
        super(ModEntities.PHANTOMSWORD, worldIn);
    }

	public EntityPhantomSword(World world, LivingEntity thrower, BlockPos targetpos) {
        super(ModEntities.PHANTOMSWORD, world);
        this.thrower = thrower;
        setTargetPos(targetpos);
        setVariety((int) (10 * Math.random()));
    }

    @Override
    public void registerData() {
        dataManager.register(VARIETY, 0);
        dataManager.register(DELAY, 0);
        dataManager.register(ROTATION, 0F);
        dataManager.register(PITCH, 0F);
        dataManager.register(TARGET_POS, BlockPos.ZERO);
        dataManager.register(FAKE, false);
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    public void faceTarget(){
        this.faceEntity(this.getTargetPos());
        Vector3d vec = new Vector3d(getTargetPos().getX() - getPosX(), getTargetPos().getY() - getPosY(), getTargetPos().getZ() - getPosZ())
                .normalize();
        this.setMotion(vec.x * 1.05F, vec.y * 1.05F, vec.z * 1.05F);
    }

    @Override
    public void tick() {

        if (getDelay() > 0) {
            setDelay(getDelay() - 1);
            return;
        }

        if (this.ticksExisted >= LIVE_TICKS)
            remove();

        if(getFake()) {
            this.setMotion(0D,0D,0D);
            return;
        }

        if (!getFake() && !world.isRemote && (thrower == null || !(thrower instanceof PlayerEntity) || thrower.removed)) {
            remove();
            return;
        }

        super.tick();

        PlayerEntity player = (PlayerEntity) thrower;
        if(!world.isRemote && !getFake() && this.ticksExisted % 6 == 0) {
            EntityPhantomSword illusion = new EntityPhantomSword(world);
            illusion.thrower = this.thrower;
            illusion.setFake(true);
            illusion.setRotation(this.getRotation());
            illusion.setPitch(this.getPitch());
            illusion.setPosition(getPosX(), getPosY(), getPosZ());
            illusion.setVariety(getVariety());
            world.addEntity(illusion);
        }

        if (!world.isRemote) {
            float dmg = 10F;
            AxisAlignedBB axis = new AxisAlignedBB(getPosX(), getPosY(), getPosZ(), lastTickPosX, lastTickPosY, lastTickPosZ).grow(2);
            List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, axis);
            for (LivingEntity living : entities) {
                if (living == thrower)
                    continue;
                if(living instanceof AnimalEntity)
                    continue;
                if (living.hurtResistantTime <= 5) {
                    living.attackEntityFrom(DamageSource.MAGIC, dmg * 0.4F);
                    attackedFrom(living, player, dmg * 0.6F);
                }
            }
        }

    }

    public static void attackedFrom(LivingEntity target, PlayerEntity player, float i) {
        if (player != null)
            target.attackEntityFrom(DamageSource.causePlayerDamage(player), i);
        else
            target.attackEntityFrom(DamageSource.GENERIC, i);
    }

    @Override
    protected void onImpact(RayTraceResult result) {

    }

    public void faceEntity(BlockPos target) {
        double d0 = target.getX() - this.getPosX();
        double d2 = target.getZ() - this.getPosZ();
        double d1 = target.getY() - this.getPosY();

        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
        float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
        float f1 = (float) (-(MathHelper.atan2(d1, d3) * (180D / Math.PI)));
        this.rotationPitch = this.updateRotation(this.rotationPitch, f1, 360F);
        this.rotationYaw = this.updateRotation(this.rotationYaw, f, 360F);

        setPitch(-this.rotationPitch);
        setRotation(MathHelper.wrapDegrees(-this.rotationYaw + 180));
    }

    private float updateRotation(float angle, float targetAngle, float maxIncrease) {
        float f = MathHelper.wrapDegrees(targetAngle - angle);

        if (f > maxIncrease) {
            f = maxIncrease;
        }

        if (f < -maxIncrease) {
            f = -maxIncrease;
        }

        return angle + f;
    }

    @Override
    public void writeAdditional(CompoundNBT cmp) {
        cmp.putInt(TAG_VARIETY, getVariety());
        cmp.putInt(TAG_DELAY, getDelay());
        cmp.putFloat(TAG_ROTATION, getRotation());
        cmp.putFloat(TAG_PITCH, getPitch());
        cmp.putLong(TAG_TARGETPOS, getTargetPos().toLong());
        cmp.putBoolean(TAG_FAKE, getFake());
    }

    @Override
    public void readAdditional(CompoundNBT cmp) {
        setVariety(cmp.getInt(TAG_VARIETY));
        setDelay(cmp.getInt(TAG_DELAY));
        setRotation(cmp.getFloat(TAG_ROTATION));
        setPitch(cmp.getFloat(TAG_PITCH));
        setTargetPos(BlockPos.fromLong(cmp.getLong(TAG_TARGETPOS)));
        setFake(cmp.getBoolean(TAG_FAKE));
    }

    public int getVariety() {
        return dataManager.get(VARIETY);
    }

    public void setVariety(int var) {
        dataManager.set(VARIETY, var);
    }

    public int getDelay() {
        return dataManager.get(DELAY);
    }

    public void setDelay(int var) {
        dataManager.set(DELAY, var);
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

    public boolean getFake() {
        return dataManager.get(FAKE);
    }

    public void setFake(boolean rot) {
        dataManager.set(FAKE, rot);
    }


    public BlockPos getTargetPos() {
        return dataManager.get(TARGET_POS);
    }

    public void setTargetPos(BlockPos pos) {
        dataManager.set(TARGET_POS, pos);
    }

    @Nonnull
    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
