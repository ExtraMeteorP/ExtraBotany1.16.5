package com.meteor.extrabotany.common.crafting.recipe;

import com.meteor.extrabotany.common.items.ModItems;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vazkii.botania.common.crafting.recipe.RecipeUtils;
import vazkii.botania.common.item.ItemManaGun;

import javax.annotation.Nonnull;

public class SilverBulletRemoveLensRecipe extends SpecialRecipe {
    public static final SpecialRecipeSerializer<SilverBulletRemoveLensRecipe> SERIALIZER = new SpecialRecipeSerializer<>(SilverBulletRemoveLensRecipe::new);

    public SilverBulletRemoveLensRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
        boolean foundGun = false;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() == ModItems.silverbullet && !ItemManaGun.getLens(stack).isEmpty()) {
                    foundGun = true;
                } else {
                    return false; // Found an invalid item, breaking the recipe
                }
            }
        }

        return foundGun;
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
        ItemStack gun = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() == ModItems.silverbullet) {
                    gun = stack;
                }
            }
        }

        ItemStack gunCopy = gun.copy();
        gunCopy.setCount(1);
        ItemManaGun.setLens(gunCopy, ItemStack.EMPTY);

        return gunCopy;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height > 0;
    }

    @Nonnull
    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Nonnull
    @Override
    public NonNullList<ItemStack> getRemainingItems(@Nonnull CraftingInventory inv) {
        return RecipeUtils.getRemainingItemsSub(inv, s -> {
            if (s.getItem() == ModItems.silverbullet) {
                ItemStack stack = ItemManaGun.getLens(s);
                stack.setCount(1);
                return stack;
            }
            return null;
        });
    }
}
