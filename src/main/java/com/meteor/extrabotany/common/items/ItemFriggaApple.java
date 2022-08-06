package com.meteor.extrabotany.common.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypePreset;

public class ItemFriggaApple extends Item {

    public ItemFriggaApple(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        PlayerEntity playerentity = entityLiving instanceof PlayerEntity ? (PlayerEntity) entityLiving : null;
        if (playerentity != null) {
            playerentity.addStat(Stats.ITEM_USED.get(this));
            if (!playerentity.abilities.isCreativeMode) {
                stack.shrink(1);
            }
            if(!worldIn.isRemote) {
                CuriosApi.getSlotHelper().growSlotType(SlotTypePreset.RING.getIdentifier(), playerentity);
                CuriosApi.getSlotHelper().growSlotType(SlotTypePreset.NECKLACE.getIdentifier(), playerentity);
                CuriosApi.getSlotHelper().growSlotType(SlotTypePreset.CHARM.getIdentifier(), playerentity);
            }
        }
        return stack;
    }
}
