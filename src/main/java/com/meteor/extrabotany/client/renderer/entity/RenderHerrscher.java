package com.meteor.extrabotany.client.renderer.entity;

import com.meteor.extrabotany.client.handler.MiscellaneousIcons;
import com.meteor.extrabotany.client.renderer.entity.layers.HeldFakeItemLayer;
import com.meteor.extrabotany.client.renderer.entity.layers.HerrscherLayer;
import com.meteor.extrabotany.common.items.ModItems;
import com.meteor.extrabotany.common.items.bauble.ItemCoreGod;
import com.meteor.extrabotany.common.libs.LibMisc;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class RenderHerrscher extends BipedRenderer<MobEntity, BipedModel<MobEntity>> {

    public RenderHerrscher(EntityRendererManager renderManager) {
        super(renderManager, new PlayerModel(0.0F, false), 0F);
        this.addLayer(new HeldFakeItemLayer(this));
        this.addLayer(new HerrscherLayer<>(this));
    }

    @Override
    public void render(@Nonnull MobEntity mob, float yaw, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffers, int light) {
        super.render(mob, yaw, partialTicks, ms, buffers, light);

    }

    @Nonnull
    @Override
    public ResourceLocation getEntityTexture(@Nonnull MobEntity entity) {
        return new ResourceLocation(LibMisc.MOD_ID, "textures/entity/voidherrscher.png");
    }

}
