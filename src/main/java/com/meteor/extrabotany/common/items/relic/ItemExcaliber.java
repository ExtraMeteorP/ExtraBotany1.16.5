package com.meteor.extrabotany.common.items.relic;

import com.meteor.extrabotany.ExtraBotany;
import com.meteor.extrabotany.common.network.ExcaliberLeftClickPack;
import com.meteor.extrabotany.common.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILensEffect;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.common.advancements.RelicBindTrigger;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.relic.ItemRelic;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemExcaliber extends SwordItem implements IRelic, IManaUsingItem, ILensEffect {

    private static final String TAG_ATTACKER_USERNAME = "attackerUsername";
    private static final String TAG_HOME_ID = "homeID";
    private static final String TAG_SOULBIND_UUID = "soulbindUUID";
    private static final int MANA_PER_DAMAGE = 160;

    public ItemExcaliber(Properties prop) {
        super(ItemTier.NETHERITE, 6, -2F, prop);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void leftClick(PlayerInteractEvent.LeftClickEmpty evt) {
        if (!evt.getItemStack().isEmpty() && evt.getItemStack().getItem() == this) {
            NetworkHandler.INSTANCE.sendToServer(new ExcaliberLeftClickPack());
        }
    }

    @SubscribeEvent
    public void attackEntity(AttackEntityEvent evt) {
        if (!evt.getPlayer().world.isRemote) {
            trySpawnBurst(evt.getPlayer());
        }
    }

    public void trySpawnBurst(PlayerEntity player) {
        if (!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() == this
                && player.getCooledAttackStrength(0) == 1) {
            EntityManaBurst burst = getBurst(player, player.getHeldItemMainhand());
            player.world.addEntity(burst);
            ToolCommons.damageItemIfPossible(player.getHeldItemMainhand(), 1, player, MANA_PER_DAMAGE);
            player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.terraBlade,
                    SoundCategory.PLAYERS, 0.4F, 1.4F);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isRemote && entity instanceof PlayerEntity) {
            updateRelic(stack, (PlayerEntity) entity);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(final ItemStack stack, @Nullable World world, final List<ITextComponent> tooltip, ITooltipFlag flags) {
        if (!hasUUID(stack)) {
            tooltip.add(new TranslationTextComponent("botaniamisc.relicUnbound"));
        } else {
            if (!getSoulbindUUID(stack).equals(Minecraft.getInstance().player.getUniqueID())) {
                tooltip.add(new TranslationTextComponent("botaniamisc.notYourSagittarius"));
            } else {
                tooltip.add(new TranslationTextComponent("botaniamisc.relicSoulbound", Minecraft.getInstance().player.getName()));
            }
        }
    }

    public boolean shouldDamageWrongPlayer() {
        return true;
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return Integer.MAX_VALUE;
    }

    public void updateRelic(ItemStack stack, PlayerEntity player) {
        if (stack.isEmpty() || !(stack.getItem() instanceof IRelic)) {
            return;
        }

        boolean rightPlayer = true;

        if (!hasUUID(stack)) {
            bindToUUID(player.getUniqueID(), stack);
            if (player instanceof ServerPlayerEntity) {
                RelicBindTrigger.INSTANCE.trigger((ServerPlayerEntity) player, stack);
            }
        } else if (!getSoulbindUUID(stack).equals(player.getUniqueID())) {
            rightPlayer = false;
        }

        if (!rightPlayer && player.ticksExisted % 10 == 0 && (!(stack.getItem() instanceof ItemRelic) || ((ItemRelic) stack.getItem()).shouldDamageWrongPlayer())) {
            player.attackEntityFrom(damageSource(), 2);
        }
    }

    public boolean isRightPlayer(PlayerEntity player, ItemStack stack) {
        return hasUUID(stack) && getSoulbindUUID(stack).equals(player.getUniqueID());
    }

    public static DamageSource damageSource() {
        return new DamageSource("botania-relic");
    }

    @Override
    public void bindToUUID(UUID uuid, ItemStack stack) {
        ItemNBTHelper.setString(stack, TAG_SOULBIND_UUID, uuid.toString());
    }

    @Override
    public UUID getSoulbindUUID(ItemStack stack) {
        if (ItemNBTHelper.verifyExistance(stack, TAG_SOULBIND_UUID)) {
            try {
                return UUID.fromString(ItemNBTHelper.getString(stack, TAG_SOULBIND_UUID, ""));
            } catch (IllegalArgumentException ex) { // Bad UUID in tag
                ItemNBTHelper.removeEntry(stack, TAG_SOULBIND_UUID);
            }
        }

        return null;
    }

    @Override
    public boolean hasUUID(ItemStack stack) {
        return getSoulbindUUID(stack) != null;
    }

    public static EntityManaBurst getBurst(PlayerEntity player, ItemStack stack) {
        EntityManaBurst burst = new EntityManaBurst(player);

        float motionModifier = 9F;
        burst.setColor(0xFFFF20);
        burst.setMana(MANA_PER_DAMAGE);
        burst.setStartingMana(MANA_PER_DAMAGE);
        burst.setMinManaLoss(40);
        burst.setManaLossPerTick(4F);
        burst.setGravity(0F);
        burst.setMotion(burst.getMotion().x * motionModifier, burst.getMotion().y * motionModifier, burst.getMotion().z * motionModifier);

        ItemStack lens = stack.copy();
        ItemNBTHelper.setString(lens, TAG_ATTACKER_USERNAME, player.getName().getString());
        burst.setSourceLens(lens);
        return burst;
    }

    @Override
    public void apply(ItemStack stack, BurstProperties props) {

    }

    @Override
    public boolean collideBurst(IManaBurst burst, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
        return dead;
    }

    @Override
    public void updateBurst(IManaBurst burst, ItemStack stack) {
        ThrowableEntity entity = (ThrowableEntity) burst;
        AxisAlignedBB axis = new AxisAlignedBB(entity.getPosX(), entity.getPosY(), entity.getPosZ(), entity.lastTickPosX,
                entity.lastTickPosY, entity.lastTickPosZ).grow(1);
        String attacker = ItemNBTHelper.getString(burst.getSourceLens(), TAG_ATTACKER_USERNAME, "");


        int homeID = ItemNBTHelper.getInt(stack, TAG_HOME_ID, -1);
        if (homeID == -1) {
            AxisAlignedBB axis1 = new AxisAlignedBB(entity.getPosX() - 5F, entity.getPosY() - 5F, entity.getPosZ() - 5F,
                    entity.lastTickPosX + 5F, entity.lastTickPosY + 5F, entity.lastTickPosZ + 5F);
            List<LivingEntity> entities = entity.world.getEntitiesWithinAABB(LivingEntity.class, axis1);
            for (LivingEntity living : entities) {
                if (living instanceof PlayerEntity || !(living instanceof IMob) || living.hurtTime != 0)
                    continue;
                homeID = living.getEntityId();
                ItemNBTHelper.setInt(stack, TAG_HOME_ID, homeID);
                break;
            }
        }

        List<LivingEntity> entities = entity.world.getEntitiesWithinAABB(LivingEntity.class, axis);
        if (homeID != -1) {
            Entity home = entity.world.getEntityByID(homeID);
            if (home != null) {
                Vector3 vecEntity = Vector3.fromEntityCenter(home);
                Vector3 vecThis = Vector3.fromEntityCenter(entity);
                Vector3 vecMotion = vecEntity.subtract(vecThis);
                Vector3 vecCurrentMotion = new Vector3(entity.getMotion().x, entity.getMotion().y, entity.getMotion().z);
                vecMotion.normalize().multiply(vecCurrentMotion.mag());
                entity.setMotion(vecMotion.x, vecMotion.y, vecMotion.z);
            }
        }

        for (LivingEntity living : entities) {
            if (living instanceof PlayerEntity && (living.getName().getString().equals(attacker)))
                continue;

            if (!living.removed) {
                int cost = MANA_PER_DAMAGE / 3;
                int mana = burst.getMana();
                if (mana >= cost) {
                    burst.setMana(mana - cost);
                    float damage = BotaniaAPI.instance().getTerrasteelItemTier().getAttackDamage() + 3F;
                    if (!burst.isFake() && !entity.world.isRemote) {
                        PlayerEntity player = living.world.getPlayerByUuid(getSoulbindUUID(stack));
                        living.attackEntityFrom(
                                player == null ? ItemRelic.damageSource() : DamageSource.causePlayerDamage(player),
                                damage);

                        entity.remove();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean doParticles(IManaBurst burst, ItemStack stack) {
        return true;
    }

    @Override
    public boolean usesMana(ItemStack stack) {
        return true;
    }
}
