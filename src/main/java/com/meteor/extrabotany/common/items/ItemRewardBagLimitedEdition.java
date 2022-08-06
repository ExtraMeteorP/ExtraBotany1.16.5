package com.meteor.extrabotany.common.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemRewardBagLimitedEdition extends Item {

    private static final String TAG_HAS_POOL = "has_pool";
    private static final String TAG_LIMITED_POOL = "limited_pool";

    public ItemRewardBagLimitedEdition(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
        ItemStack itemstack = player.getHeldItem(handIn);
        if(worldIn.isRemote)
            return ActionResult.resultFail(itemstack);
        CompoundNBT data = player.getPersistentData();
        if(!data.contains(PlayerEntity.PERSISTED_NBT_TAG)){
            data.put(PlayerEntity.PERSISTED_NBT_TAG, new CompoundNBT());
        }
        if (data.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
            CompoundNBT cmp = data.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
            if(!cmp.contains(TAG_HAS_POOL)){
                cmp.putBoolean(TAG_HAS_POOL, true);
                ItemStackHandler handler = new ItemStackHandler(100);

                List<ItemStack> stacks = new ArrayList<ItemStack>();
                for(int i = 0; i < 30; i++)
                    stacks.add(new ItemStack(ModItems.rewardbagb, 6));
                for(int i = 0; i < 20; i++)
                    stacks.add(new ItemStack(ModItems.rewardbagc, 4));
                for(int i = 0; i < 20; i++)
                    stacks.add(new ItemStack(ModItems.rewardbagd, 4));
                for(int i = 0; i < 10; i++)
                    stacks.add(new ItemStack(ModItems.heromedal));
                for(int i = 0; i < 4; i++)
                    stacks.add(new ItemStack(ModItems.lenssupercondutor));
                stacks.add(new ItemStack(ModItems.friggaapple));
                Collections.shuffle(stacks, player.world.rand);

                for(int i = 0; i < stacks.size(); i++){
                    handler.setStackInSlot(i, stacks.get(i));
                }
                cmp.put(TAG_LIMITED_POOL, handler.serializeNBT());
            }

            if(cmp.getBoolean(TAG_HAS_POOL)){
                ItemStackHandler handler = new ItemStackHandler();
                handler.deserializeNBT(cmp.getCompound(TAG_LIMITED_POOL));
                ItemStack reward = ItemStack.EMPTY;
                for(int i = 0; i < handler.getSlots(); i++){
                    if(!handler.getStackInSlot(i).isEmpty()){
                        reward = handler.getStackInSlot(i).copy();
                        handler.setStackInSlot(i, ItemStack.EMPTY);
                        break;
                    }
                }
                cmp.put(TAG_LIMITED_POOL, handler.serializeNBT());

                if(reward.isEmpty()){
                    reward = new ItemStack(Items.EMERALD, 2);
                }

                worldIn.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ARROW_SHOOT,
                        SoundCategory.PLAYERS, 0.5F, 0.4F / (worldIn.rand.nextFloat() * 0.4F + 0.8F));
                player.entityDropItem(reward).setNoPickupDelay();
                if (!player.abilities.isCreativeMode) {
                    itemstack.shrink(1);
                }
                return ActionResult.resultSuccess(itemstack);
            }
        }
        return ActionResult.resultFail(itemstack);
    }

}
