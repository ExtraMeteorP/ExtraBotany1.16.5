package com.meteor.extrabotany.common.items.armor.maid;

import com.meteor.extrabotany.api.ExtraBotanyAPI;
import com.meteor.extrabotany.client.model.armor.ModelMaidArmor;
import com.meteor.extrabotany.common.items.ModItems;
import com.meteor.extrabotany.common.items.armor.miku.ItemMikuArmor;
import com.meteor.extrabotany.common.libs.LibMisc;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.LazyValue;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class ItemMaidArmor extends ItemMikuArmor {

    public ItemMaidArmor(EquipmentSlotType type, Properties props) {
        super(type, ExtraBotanyAPI.INSTANCE.getMaidArmorMaterial(), props);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BipedModel<?> provideArmorModelForSlot(EquipmentSlotType slot) {
        return new ModelMaidArmor(slot);
    }

    @Override
    public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlotType slot) {
        return LibMisc.MOD_ID + ":textures/model/armor_maid.png";
    }

    private static final LazyValue<ItemStack[]> armorSet = new LazyValue<>(() -> new ItemStack[] {
            new ItemStack(ModItems.armor_maid_helm),
            new ItemStack(ModItems.armor_maid_chest),
            new ItemStack(ModItems.armor_maid_legs),
            new ItemStack(ModItems.armor_maid_boots)
    });

    @Override
    public ItemStack[] getArmorSetStacks() {
        return armorSet.getValue();
    }

    @Override
    public boolean hasArmorSetItem(PlayerEntity player, EquipmentSlotType slot) {
        if (player == null) {
            return false;
        }

        ItemStack stack = player.getItemStackFromSlot(slot);
        if (stack.isEmpty()) {
            return false;
        }

        switch (slot) {
            case HEAD:
                return stack.getItem() == ModItems.armor_maid_helm;
            case CHEST:
                return stack.getItem() == ModItems.armor_maid_chest;
            case LEGS:
                return stack.getItem() == ModItems.armor_maid_legs;
            case FEET:
                return stack.getItem() == ModItems.armor_maid_boots;
        }

        return false;
    }

    @Override
    public IFormattableTextComponent getArmorSetName() {
        return new TranslationTextComponent("extrabotany.armorset.maid.name");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addArmorSetDescription(ItemStack stack, List<ITextComponent> list) {
        list.add(new TranslationTextComponent("extrabotany.armorset.maid.desc0").mergeStyle(TextFormatting.GRAY));
        list.add(new TranslationTextComponent("extrabotany.armorset.maid.desc1").mergeStyle(TextFormatting.GRAY));
        list.add(new TranslationTextComponent("extrabotany.armorset.maid.desc2").mergeStyle(TextFormatting.GRAY));
    }

}
