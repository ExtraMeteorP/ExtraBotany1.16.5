package com.meteor.extrabotany.common.items;

import com.meteor.extrabotany.ExtraBotany;
import com.meteor.extrabotany.common.entities.EntityPhantomSword;
import com.meteor.extrabotany.common.network.FractalLeftClickPack;
import com.meteor.extrabotany.common.network.NetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ItemFirstFractal extends SwordItem {

    public ItemFirstFractal() {
        super(ItemTier.NETHERITE, 6, -2F, new Properties().group(ExtraBotany.itemGroup).rarity(Rarity.EPIC).maxStackSize(1).setNoRepair());
        MinecraftForge.EVENT_BUS.addListener(this::leftClick);
        MinecraftForge.EVENT_BUS.addListener(this::leftClickBlock);
        MinecraftForge.EVENT_BUS.addListener(this::attackEntity);
    }

    public void attackEntity(AttackEntityEvent evt) {
        if (!evt.getPlayer().world.isRemote) {
            trySpawnPhantomSword(evt.getPlayer(), evt.getTarget());
        }
    }

    public void leftClick(PlayerInteractEvent.LeftClickEmpty evt) {
        if (!evt.getItemStack().isEmpty() && evt.getItemStack().getItem() == this) {
            NetworkHandler.INSTANCE.sendToServer(new FractalLeftClickPack());
        }
    }

    public void leftClickBlock(PlayerInteractEvent.LeftClickBlock evt) {
        if (evt.getPlayer().world.isRemote && !evt.getItemStack().isEmpty() && evt.getItemStack().getItem() == this) {
            NetworkHandler.INSTANCE.sendToServer(new FractalLeftClickPack());
        }
    }

    public void trySpawnPhantomSword(PlayerEntity player, Entity target) {
        if (!player.world.isRemote && !player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() == this
                && player.getCooledAttackStrength(0) == 1) {
            BlockPos targetpos = target == null ? raytraceFromEntity(player, 80F, true).getPos().add(0, 1, 0) : new BlockPos(target.getPositionVec()).add(0, 1, 0);
            double range = 13D;
            double j = -Math.PI + 2 * Math.PI * Math.random();
            double k;
            double x,y,z;
            for(int i = 0; i < 3; i++) {
                EntityPhantomSword sword = new EntityPhantomSword(player.world, player, targetpos);
                sword.setDelay(5 + 5 * i);
                k = 0.12F * Math.PI * Math.random() + 0.28F * Math.PI;
                x = targetpos.getX() + range * Math.sin(k) * Math.cos(j);
                y = targetpos.getY() + range * Math.cos(k);
                z = targetpos.getZ() + range * Math.sin(k) * Math.sin(j);
                j+= 2 * Math.PI * Math.random() * 0.08F + 2 * Math.PI * 0.17F;
                sword.setPosition(x, y, z);
                sword.faceTarget();
                player.world.addEntity(sword);

            }
            EntityPhantomSword sword2 = new EntityPhantomSword(player.world, player, targetpos);
            k = 0.12F * Math.PI * Math.random() + 0.28F * Math.PI;
            x = targetpos.getX() + range * Math.sin(k) * Math.cos(j);
            y = targetpos.getY() + range * Math.cos(k);
            z = targetpos.getZ() + range * Math.sin(k) * Math.sin(j);
            sword2.setPosition(x, y, z);
            sword2.faceTarget();
            sword2.setVariety(9);
            player.world.addEntity(sword2);
        }
    }

    public static BlockRayTraceResult raytraceFromEntity(Entity e, double distance, boolean fluids) {
        return (BlockRayTraceResult) e.pick(distance, 1, fluids);
    }

}
