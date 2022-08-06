package com.meteor.extrabotany.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemSpiritFuel extends Item {

    public ItemSpiritFuel(Properties properties) {
        super(properties);
    }

    @Override
    public int getBurnTime(ItemStack stack){
        return 3600;
    }

}
