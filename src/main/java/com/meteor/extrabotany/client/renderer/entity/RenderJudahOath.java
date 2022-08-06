package com.meteor.extrabotany.client.renderer.entity;

import com.meteor.extrabotany.common.entities.EntityJudahOath;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderJudahOath extends EntityRenderer<EntityJudahOath> {

    protected RenderJudahOath(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(EntityJudahOath weapon, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        Minecraft mc = Minecraft.getInstance();

    }

    @Override
    public ResourceLocation getEntityTexture(EntityJudahOath entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }
}
