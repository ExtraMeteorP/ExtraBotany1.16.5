package com.meteor.extrabotany.common.entities.herrscher;

import com.google.common.collect.ImmutableList;
import com.meteor.extrabotany.common.core.ConfigHandler;
import com.meteor.extrabotany.common.entities.ModEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SRemoveEntityEffectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.tags.ITag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import top.theillusivec4.curios.api.CuriosApi;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EntityHerrscher extends MobEntity implements IEntityAdditionalSpawnData {

    public static final float ARENA_RANGE = 15F;
    public static final int ARENA_HEIGHT = 5;

    public static final float MAX_HP = 600F;

    private static final String TAG_INVUL_TIME = "invulTime";
    private static final String TAG_SOURCE_X = "sourceX";
    private static final String TAG_SOURCE_Y = "sourceY";
    private static final String TAG_SOURCE_Z = "sourcesZ";
    private static final String TAG_PLAYER_COUNT = "playerCount";
    private static final String TAG_STAGE = "stage";
    private static final ITag.INamedTag<Block> BLACKLIST = ModTags.Blocks.GAIA_BREAK_BLACKLIST;

    private static final DataParameter<Integer> INVUL_TIME = EntityDataManager.createKey(EntityHerrscher.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> STAGE = EntityDataManager.createKey(EntityHerrscher.class, DataSerializers.VARINT);

    private static final List<BlockPos> PYLON_LOCATIONS = ImmutableList.of(
            new BlockPos(4, 1, 4),
            new BlockPos(4, 1, -4),
            new BlockPos(-4, 1, 4),
            new BlockPos(-4, 1, -4)
    );

    private static final List<ResourceLocation> CHEATY_BLOCKS = Arrays.asList(
            new ResourceLocation("openblocks", "beartrap"),
            new ResourceLocation("thaumictinkerer", "magnet")
    );

    private int playerCount = 0;
    private BlockPos source = BlockPos.ZERO;
    private final List<UUID> playersWhoAttacked = new ArrayList<>();
    private final ServerBossInfo bossInfo = (ServerBossInfo) new ServerBossInfo(ModEntities.EGO.getName(), BossInfo.Color.PINK, BossInfo.Overlay.PROGRESS).setCreateFog(true);;
    private UUID bossInfoUUID = bossInfo.getUniqueId();
    public PlayerEntity trueKiller = null;

    public EntityHerrscher(EntityType<? extends MobEntity> type, World worldIn) {
        super(type, worldIn);
        experienceValue = 825;
    }

    private static List<BlockPos> checkPylons(World world, BlockPos beaconPos) {
        List<BlockPos> invalidPylonBlocks = new ArrayList<>();

        for (BlockPos coords : PYLON_LOCATIONS) {
            BlockPos pos_ = beaconPos.add(coords);

            BlockState state = world.getBlockState(pos_);
            if (state.getBlock() != ModBlocks.gaiaPylon) {
                invalidPylonBlocks.add(pos_);
            }
        }

        return invalidPylonBlocks;
    }

    private static List<BlockPos> checkArena(World world, BlockPos beaconPos) {
        List<BlockPos> trippedPositions = new ArrayList<>();
        int range = (int) Math.ceil(ARENA_RANGE);
        BlockPos pos;

        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                if (Math.abs(x) == 4 && Math.abs(z) == 4 || vazkii.botania.common.core.helper.MathHelper.pointDistancePlane(x, z, 0, 0) > ARENA_RANGE) {
                    continue; // Ignore pylons and out of circle
                }

                boolean hasFloor = false;

                for (int y = -2; y <= ARENA_HEIGHT; y++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue; //the beacon
                    }

                    pos = beaconPos.add(x, y, z);

                    BlockState state = world.getBlockState(pos);

                    boolean allowBlockHere = y < 0;
                    boolean isBlockHere = !state.getCollisionShape(world, pos).isEmpty();

                    if (allowBlockHere && isBlockHere) //floor is here! good
                    {
                        hasFloor = true;
                    }

                    if (y == 0 && !hasFloor) //column is entirely missing floor
                    {
                        trippedPositions.add(pos.down());
                    }

                    if (!allowBlockHere && isBlockHere && !BLACKLIST.contains(state.getBlock())) //ceiling is obstructed in this column
                    {
                        trippedPositions.add(pos);
                    }
                }
            }
        }

        return trippedPositions;
    }

    private static void warnInvalidBlocks(World world, Iterable<BlockPos> invalidPositions) {
        WispParticleData data = WispParticleData.wisp(0.5F, 1, 0.2F, 0.2F, 8, false);
        for (BlockPos pos_ : invalidPositions) {
            world.addParticle(data, pos_.getX() + 0.5, pos_.getY() + 0.5, pos_.getZ() + 0.5, 0, 0, 0);
        }
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(1, new LookAtGoal(this, PlayerEntity.class, ARENA_RANGE * 1.5F));
    }

    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(INVUL_TIME, 0);
        dataManager.register(STAGE, 0);
    }

    public int getInvulTime() {
        return dataManager.get(INVUL_TIME);
    }

    public BlockPos getSource() {
        return source;
    }

    public void setInvulTime(int time) {
        dataManager.set(INVUL_TIME, time);
    }

    public int getStage() {
        return dataManager.get(STAGE);
    }

    public void setStage(int time) {
        dataManager.set(STAGE, time);
    }

    @Override
    public void writeAdditional(CompoundNBT cmp) {
        super.writeAdditional(cmp);
        cmp.putInt(TAG_INVUL_TIME, getInvulTime());

        cmp.putInt(TAG_SOURCE_X, source.getX());
        cmp.putInt(TAG_SOURCE_Y, source.getY());
        cmp.putInt(TAG_SOURCE_Z, source.getZ());

        cmp.putInt(TAG_PLAYER_COUNT, playerCount);
        cmp.putInt(TAG_STAGE, getStage());
    }

    @Override
    public void readAdditional(CompoundNBT cmp) {
        super.readAdditional(cmp);
        setInvulTime(cmp.getInt(TAG_INVUL_TIME));

        int x = cmp.getInt(TAG_SOURCE_X);
        int y = cmp.getInt(TAG_SOURCE_Y);
        int z = cmp.getInt(TAG_SOURCE_Z);
        source = new BlockPos(x, y, z);

        if (cmp.contains(TAG_PLAYER_COUNT)) {
            playerCount = cmp.getInt(TAG_PLAYER_COUNT);
        } else {
            playerCount = 1;
        }

        setStage(cmp.getInt(TAG_STAGE));

        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(@Nullable ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        Entity e = source.getTrueSource();
        if (e instanceof PlayerEntity && isTruePlayer(e) && getInvulTime() == 0) {
            PlayerEntity player = (PlayerEntity) e;

            if (!playersWhoAttacked.contains(player.getUniqueID())) {
                playersWhoAttacked.add(player.getUniqueID());
            }

            int cap = 25;
            float dmg = Math.min(cap, amount);

            return super.attackEntityFrom(source, dmg);
        }

        return false;
    }

    private static final Pattern FAKE_PLAYER_PATTERN = Pattern.compile("^(?:\\[.*\\])|(?:ComputerCraft)$");

    public static boolean isTruePlayer(Entity e) {
        if (!(e instanceof PlayerEntity)) {
            return false;
        }

        PlayerEntity player = (PlayerEntity) e;

        String name = player.getName().getString();
        return !(player instanceof FakePlayer || FAKE_PLAYER_PATTERN.matcher(name).matches());
    }

    @Override
    public void onDeath(@Nonnull DamageSource source) {
        super.onDeath(source);
        LivingEntity entitylivingbase = getAttackingEntity();
        if(!world.isRemote)
            sendMessageToAll("extrabotany.ego.death_" + world.rand.nextInt(4));
        playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 20F, (1F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
        world.addParticle(ParticleTypes.EXPLOSION_EMITTER, getPosX(), getPosY(), getPosZ(), 1D, 0D, 0D);
    }

    @Override
    public boolean canDespawn(double dist) {
        return false;
    }

    @Override
    protected void dropLoot(@Nonnull DamageSource source, boolean wasRecentlyHit) {
        // Save true killer, they get extra loot
        if (wasRecentlyHit && source.getTrueSource() instanceof PlayerEntity) {
            trueKiller = (PlayerEntity) source.getTrueSource();
        }

        // Generate loot table for every single attacking player
        for (UUID u : playersWhoAttacked) {
            PlayerEntity player = world.getPlayerByUuid(u);
            if (player == null) {
                continue;
            }

            PlayerEntity saveLastAttacker = attackingPlayer;
            Vector3d savePos = getPositionVec();

            attackingPlayer = player; // Fake attacking player as the killer
            // Spoof pos so drops spawn at the player
            setPosition(player.getPosX(), player.getPosY(), player.getPosZ());
            super.dropLoot(DamageSource.causePlayerDamage(player), wasRecentlyHit);
            setPosition(savePos.getX(), savePos.getY(), savePos.getZ());
            attackingPlayer = saveLastAttacker;
        }

        trueKiller = null;
    }

    public List<PlayerEntity> getPlayersAround() {
        return world.getEntitiesWithinAABB(PlayerEntity.class, getArenaBB(source), player -> isTruePlayer(player) && !player.isSpectator());
    }

    private static int countHerrscherAround(World world, BlockPos source) {
        List<EntityHerrscher> l = world.getEntitiesWithinAABB(EntityHerrscher.class, getArenaBB(source));
        return l.size();
    }

    @Nonnull
    private static AxisAlignedBB getArenaBB(@Nonnull BlockPos source) {
        double range = ARENA_RANGE + 3D;
        return new AxisAlignedBB(source.getX() + 0.5 - range, source.getY() + 0.5 - range, source.getZ() + 0.5 - range, source.getX() + 0.5 + range, source.getY() + 0.5 + range, source.getZ() + 0.5 + range);
    }

    private void smashBlocksAround(int centerX, int centerY, int centerZ, int radius) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius + 1; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    int x = centerX + dx;
                    int y = centerY + dy;
                    int z = centerZ + dz;

                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = world.getBlockState(pos);
                    Block block = state.getBlock();

                    if (state.getBlockHardness(world, pos) == -1) {
                        continue;
                    }

                    if (CHEATY_BLOCKS.contains(Registry.BLOCK.getKey(block))) {
                        world.destroyBlock(pos, true);
                    } else {
                        //don't break blacklisted blocks
                        if (BLACKLIST.contains(block)) {
                            continue;
                        }
                        //don't break the floor
                        if (y < source.getY()) {
                            continue;
                        }
                        //don't break blocks in pylon columns
                        if (Math.abs(source.getX() - x) == 4 && Math.abs(source.getZ() - z) == 4) {
                            continue;
                        }

                        world.destroyBlock(pos, true);
                    }
                }
            }
        }
    }

    private void clearPotions(PlayerEntity player) {
        List<Effect> potionsToRemove = player.getActivePotionEffects().stream()
                .filter(effect -> effect.getDuration() < 160 && effect.isAmbient() && effect.getPotion().getEffectType() != EffectType.HARMFUL)
                .map(EffectInstance::getPotion)
                .distinct()
                .collect(Collectors.toList());

        potionsToRemove.forEach(potion -> {
            player.removePotionEffect(potion);
            ((ServerWorld) world).getChunkProvider().sendToTrackingAndSelf(player,
                    new SRemoveEntityEffectPacket(player.getEntityId(), potion));
        });
    }

    private void keepInsideArena(PlayerEntity player) {
        if (vazkii.botania.common.core.helper.MathHelper.pointDistanceSpace(player.getPosX(), player.getPosY(), player.getPosZ(), source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5) >= ARENA_RANGE) {
            Vector3 sourceVector = new Vector3(source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5);
            Vector3 playerVector = Vector3.fromEntityCenter(player);
            Vector3 motion = sourceVector.subtract(playerVector).normalize();

            player.setMotion(motion.x, 0.2, motion.z);
            player.velocityChanged = true;

            if(player.getRidingEntity() != null){
                player.getRidingEntity().setMotion(motion.x, 0.2, motion.z);
                player.getRidingEntity().velocityChanged = true;
            }
        }
    }

    private void particles() {
        for (int i = 0; i < 360; i += 8) {
            float r = 0.6F;
            float g = 0F;
            float b = 0.2F;
            float m = 0.15F;
            float mv = 0.35F;

            float rad = i * (float) Math.PI / 180F;
            double x = source.getX() + 0.5 - Math.cos(rad) * ARENA_RANGE;
            double y = source.getY() + 0.5;
            double z = source.getZ() + 0.5 - Math.sin(rad) * ARENA_RANGE;

            WispParticleData data = WispParticleData.wisp(0.5F, r, g, b);
            world.addParticle(data, x, y, z, (float) (Math.random() - 0.5F) * m, (float) (Math.random() - 0.5F) * mv, (float) (Math.random() - 0.5F) * m);
        }

        if (getInvulTime() >= 20) {
            Vector3 pos = Vector3.fromEntityCenter(this).subtract(new Vector3(0, 0.2, 0));
            for (BlockPos arr : PYLON_LOCATIONS) {
                Vector3 pylonPos = new Vector3(source.getX() + arr.getX(), source.getY() + arr.getY(), source.getZ() + arr.getZ());
                double worldTime = ticksExisted;
                worldTime /= 5;

                float rad = 0.75F + (float) Math.random() * 0.05F;
                double xp = pylonPos.x + 0.5 + Math.cos(worldTime) * rad;
                double zp = pylonPos.z + 0.5 + Math.sin(worldTime) * rad;

                Vector3 partPos = new Vector3(xp, pylonPos.y, zp);
                Vector3 mot = pos.subtract(partPos).multiply(0.04);

                float r = 0.7F + (float) Math.random() * 0.3F;
                float g = (float) Math.random() * 0.3F;
                float b = 0.7F + (float) Math.random() * 0.3F;

                WispParticleData data = WispParticleData.wisp(0.25F + (float) Math.random() * 0.1F, r, g, b, 1);
                world.addParticle(data, partPos.x, partPos.y, partPos.z, 0, -(-0.075F - (float) Math.random() * 0.015F), 0);
                WispParticleData data1 = WispParticleData.wisp(0.4F, r, g, b);
                world.addParticle(data1, partPos.x, partPos.y, partPos.z, (float) mot.x, (float) mot.y, (float) mot.z);
            }
        }
    }

    public static boolean checkFeasibility(ItemStack stack){
        if(stack.isEmpty())
            return true;

        String modid = stack.getItem().getRegistryName().getNamespace();
        if(modid.contains("extrabotany") || modid.contains("botania") || modid.contains("minecraft")){
            return true;
        }
        return false;
    }

    public static boolean checkInventory(PlayerEntity player){
        if (player.isCreative() || ConfigHandler.COMMON.disableDisarm.get()) {
            return true;
        }
        for(int i = 0; i < player.inventory.getInventoryStackLimit(); i++){
            final ItemStack stack = player.inventory.getStackInSlot(i);
            if(!checkFeasibility(stack))
                return false;
        }
        return true;
    }

    public static void disarm(PlayerEntity player){
        if (!ConfigHandler.COMMON.disableDisarm.get() && !player.isCreative()) {
            for(int i = 0; i < player.inventory.getInventoryStackLimit(); i++){
                final ItemStack stack = player.inventory.getStackInSlot(i);
                if(!checkFeasibility(stack)){
                    player.dropItem(stack, false);
                    player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                }
            }
        }
    }

    public void unlegalPlayercount(){
        if(getPlayersAround().size() > playerCount){
            for(PlayerEntity player : getPlayersAround())
                if (!world.isRemote) {
                    player.sendMessage(new TranslationTextComponent("extrabotanymisc.unlegalPlayercount").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
                }
            remove();
        }
    }

    public void sendMessageToAll(String text){
        for(PlayerEntity player : getPlayersAround()){
            player.sendMessage(new TranslationTextComponent(text, getCustomName()), getUniqueID());
        }
    }

    @Override
    public void livingTick() {
        super.livingTick();

        int invul = getInvulTime();

        if (world.isRemote) {
            particles();
            PlayerEntity player = Botania.proxy.getClientPlayer();
            if (getPlayersAround().contains(player)) {
                player.abilities.isFlying &= player.abilities.isCreativeMode;
            }
            return;
        }

        bossInfo.setPercent(getHealth() / getMaxHealth());

        if (isPassenger()) {
            stopRiding();
        }

        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            remove();
        }

        for(PlayerEntity player : getPlayersAround())
            disarm(player);

        unlegalPlayercount();

        if(invul > 0){
            setInvulTime(invul - 1);

        }
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }

    @Override
    public void addTrackingPlayer(ServerPlayerEntity player) {
        super.addTrackingPlayer(player);
        bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(ServerPlayerEntity player) {
        super.removeTrackingPlayer(player);
        bossInfo.removePlayer(player);
    }

    @Override
    protected void collideWithNearbyEntities() {
        if (getInvulTime() == 0) {
            super.collideWithNearbyEntities();
        }
    }

    @Override
    public boolean canBePushed() {
        return super.canBePushed() && getInvulTime() == 0;
    }

    private void teleportRandomly() {
        //choose a location to teleport to
        double oldX = getPosX(), oldY = getPosY(), oldZ = getPosZ();
        double newX, newY = source.getY(), newZ;
        int tries = 0;

        do {
            newX = source.getX() + (rand.nextDouble() - .5) * ARENA_RANGE;
            newZ = source.getZ() + (rand.nextDouble() - .5) * ARENA_RANGE;
            tries++;
            //ensure it's inside the arena ring, and not just its bounding square
        } while (tries < 50 && vazkii.botania.common.core.helper.MathHelper.pointDistanceSpace(newX, newY, newZ, source.getX(), source.getY(), source.getZ()) > 12);

        if (tries == 50) {
            //failsafe: teleport to the beacon
            newX = source.getX() + .5;
            newY = source.getY() + 1.6;
            newZ = source.getZ() + .5;
        }

        //for low-floor arenas, ensure landing on the ground
        BlockPos tentativeFloorPos = new BlockPos(newX, newY - 1, newZ);
        if (world.getBlockState(tentativeFloorPos).getCollisionShape(world, tentativeFloorPos).isEmpty()) {
            newY--;
        }

        //teleport there
        setPositionAndUpdate(newX, newY, newZ);

        //play sound
        world.playSound(null, oldX, oldY, oldZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
        this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);

        Random random = getRNG();

        //spawn particles along the path
        int particleCount = 128;
        for (int i = 0; i < particleCount; ++i) {
            double progress = i / (double) (particleCount - 1);
            float vx = (random.nextFloat() - 0.5F) * 0.2F;
            float vy = (random.nextFloat() - 0.5F) * 0.2F;
            float vz = (random.nextFloat() - 0.5F) * 0.2F;
            double px = oldX + (newX - oldX) * progress + (random.nextDouble() - 0.5D) * getWidth() * 2.0D;
            double py = oldY + (newY - oldY) * progress + random.nextDouble() * getHeight();
            double pz = oldZ + (newZ - oldZ) * progress + (random.nextDouble() - 0.5D) * getWidth() * 2.0D;
            world.addParticle(ParticleTypes.PORTAL, px, py, pz, vx, vy, vz);
        }

        Vector3d oldPosVec = new Vector3d(oldX, oldY + getHeight() / 2, oldZ);
        Vector3d newPosVec = new Vector3d(newX, newY + getHeight() / 2, newZ);

        if (oldPosVec.squareDistanceTo(newPosVec) > 1) {
            //damage players in the path of the teleport
            for (PlayerEntity player : getPlayersAround()) {
                boolean hit = player.getBoundingBox().grow(0.25).rayTrace(oldPosVec, newPosVec)
                        .isPresent();
                if (hit) {
                    player.attackEntityFrom(DamageSource.causeMobDamage(this), 6);
                }
            }

            //break blocks in the path of the teleport
            int breakSteps = (int) oldPosVec.distanceTo(newPosVec);
            if (breakSteps >= 2) {
                for (int i = 0; i < breakSteps; i++) {
                    float progress = i / (float) (breakSteps - 1);
                    int breakX = MathHelper.floor(oldX + (newX - oldX) * progress);
                    int breakY = MathHelper.floor(oldY + (newY - oldY) * progress);
                    int breakZ = MathHelper.floor(oldZ + (newZ - oldZ) * progress);

                    smashBlocksAround(breakX, breakY, breakZ, 1);
                }
            }
        }
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeInt(playerCount);
        buffer.writeLong(source.toLong());
        buffer.writeLong(bossInfoUUID.getMostSignificantBits());
        buffer.writeLong(bossInfoUUID.getLeastSignificantBits());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void readSpawnData(PacketBuffer additionalData) {
        playerCount = additionalData.readInt();
        source = BlockPos.fromLong(additionalData.readLong());
        long msb = additionalData.readLong();
        long lsb = additionalData.readLong();
        bossInfoUUID = new UUID(msb, lsb);
    }

    @Nonnull
    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean canBeLeashedTo(PlayerEntity player) {
        return false;
    }

}
