package com.meteor.extrabotany.common.items.relic;

import com.meteor.extrabotany.common.entities.EntityJudahOath;
import com.meteor.extrabotany.common.handler.IAdvancementRequirement;
import com.meteor.extrabotany.common.libs.LibAdvancementNames;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.relic.ItemRelic;

import javax.annotation.Nonnull;

public class ItemJudahOath extends ItemRelic implements IManaUsingItem, IAdvancementRequirement {

    public ItemJudahOath(Properties props) {
        super(props);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (isRightPlayer(player, stack)
                && ManaItemHandler.instance().requestManaExactForTool(stack, player, 2500, true)) {
            player.getCooldownTracker().setCooldown(this, 80);
            EntityJudahOath judah = new EntityJudahOath(world, player);
            judah.func_234612_a_(player, player.rotationPitch, player.rotationYaw, 0F, 0.5F, 0F);
            judah.setPosition(player.getPosX(), player.getPosY() + 1, player.getPosZ());
            judah.setRotation(MathHelper.wrapDegrees(-player.rotationYaw + 180));
            if (!world.isRemote)
                world.addEntity(judah);
        }
        return ActionResult.resultPass(stack);
    }

    @Override
    public boolean usesMana(ItemStack stack) {
        return true;
    }

    @Override
    public String getAdvancementName() {
        return LibAdvancementNames.EGODEFEAT;
    }
}
