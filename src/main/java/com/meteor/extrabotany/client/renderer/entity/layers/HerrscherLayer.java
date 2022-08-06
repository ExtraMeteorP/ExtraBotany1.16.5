package com.meteor.extrabotany.client.renderer.entity.layers;

import com.meteor.extrabotany.client.handler.MiscellaneousIcons;
import com.meteor.extrabotany.common.items.ModItems;
import com.meteor.extrabotany.common.items.bauble.ItemCoreGod;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class HerrscherLayer<T extends LivingEntity, M extends BipedModel<T>> extends LayerRenderer<T, M> {

    public HerrscherLayer(IEntityRenderer<T, M> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack ms, IRenderTypeBuffer buffers, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ms.push();
        IBakedModel model = MiscellaneousIcons.INSTANCE.coregodWingsModel[0];
        float flap = 12F + (float) ((Math.sin((double) (ageInTicks + partialTicks) * (0.12F)) + 0.4F) * (5F));
        ItemCoreGod.renderHerrscher(getEntityModel(), model, new ItemStack(ModItems.coregod), ms, buffers, flap);
        ms.pop();
    }
}
