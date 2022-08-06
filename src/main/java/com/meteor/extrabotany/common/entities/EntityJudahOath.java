package com.meteor.extrabotany.common.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Optional;
import java.util.UUID;

public class EntityJudahOath extends ThrowableEntity {

    private LivingEntity owner;

    private static final DataParameter<Float> ROTATION = EntityDataManager.createKey(EntityJudahOath.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Float> DAMAGE = EntityDataManager.createKey(EntityJudahOath.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Integer> TYPE = EntityDataManager.createKey(EntityJudahOath.class,
            DataSerializers.VARINT);
    private static final DataParameter<Optional<UUID>> UUID = EntityDataManager.createKey(EntityJudahOath.class,
            DataSerializers.OPTIONAL_UNIQUE_ID);

    public EntityJudahOath(EntityType<? extends EntityJudahOath> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityJudahOath(World worldIn, LivingEntity owner) {
        super(ModEntities.JUDAHOATH, worldIn);
        this.owner = owner;
        this.setInvulnerable(true);
    }

    @Override
    public void registerData() {
        dataManager.register(DAMAGE, 0F);
        dataManager.register(ROTATION, 0F);
        dataManager.register(TYPE, Integer.valueOf(EntityJudahOath.Type.JUDAH.ordinal()));
        dataManager.register(UUID, Optional.empty());
    }

    public UUID getUUID(){
        return (UUID)((Optional)this.dataManager.get(UUID)).get();
    }

    public void setUUID(UUID u){
        dataManager.set(UUID, Optional.ofNullable(u));
    }

    public void setType(EntityJudahOath.Type raftType){
        this.dataManager.set(TYPE, Integer.valueOf(raftType.ordinal()));
    }

    public EntityJudahOath.Type getJudahType(){
        return EntityJudahOath.Type.byId(((Integer)this.dataManager.get(TYPE)).intValue());
    }

    public float getDamage() {
        return dataManager.get(DAMAGE);
    }

    public void setDamage(float f) {
        dataManager.set(DAMAGE, f);
    }

    public float getRotation() {
        return dataManager.get(ROTATION);
    }

    public void setRotation(float rot) {
        dataManager.set(ROTATION, rot);
    }

    public enum Status{
        INAIR,
        STANDBY;
    }

    public enum Type{
        JUDAH(0, "judah"),
        KIRA(1, "kira"),
        SAKURA(2, "sakura");

        private final String name;
        private final int metadata;

        private Type(int metadataIn, String nameIn){
            this.name = nameIn;
            this.metadata = metadataIn;
        }

        public String getName(){
            return this.name;
        }

        public int getMetadata(){
            return this.metadata;
        }

        public String toString(){
            return this.name;
        }

        public static EntityJudahOath.Type byId(int id){
            if (id < 0 || id >= values().length){
                id = 0;
            }

            return values()[id];
        }

        public static EntityJudahOath.Type getTypeFromString(String nameIn){
            for (int i = 0; i < values().length; ++i){
                if (values()[i].getName().equals(nameIn)){
                    return values()[i];
                }
            }

            return values()[0];
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
