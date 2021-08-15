package com.meteor.extrabotany.client.handler;

import com.meteor.extrabotany.common.blocks.ModSubtiles;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import vazkii.botania.client.model.FloatingFlowerModel;
import vazkii.botania.client.render.tile.RenderTileSpecialFlower;

public class ModelHandler {
    static boolean registeredModels = false;
    public static void registerModels(ModelRegistryEvent evt) {
        if (!registeredModels) {
            registeredModels = true;
            ModelLoaderRegistry.registerLoader(FloatingFlowerModel.Loader.ID, FloatingFlowerModel.Loader.INSTANCE);
        }
        ClientRegistry.bindTileEntityRenderer(ModSubtiles.BELL_FLOWER, RenderTileSpecialFlower::new);
        ClientRegistry.bindTileEntityRenderer(ModSubtiles.EDELWEISS, RenderTileSpecialFlower::new);
        ClientRegistry.bindTileEntityRenderer(ModSubtiles.SUNBLESS, RenderTileSpecialFlower::new);
        ClientRegistry.bindTileEntityRenderer(ModSubtiles.MOONBLESS, RenderTileSpecialFlower::new);
        ClientRegistry.bindTileEntityRenderer(ModSubtiles.GEMINIORCHID, RenderTileSpecialFlower::new);
        ClientRegistry.bindTileEntityRenderer(ModSubtiles.TINKLEFLOWER, RenderTileSpecialFlower::new);
        ClientRegistry.bindTileEntityRenderer(ModSubtiles.OMNIVIOLET, RenderTileSpecialFlower::new);
        ClientRegistry.bindTileEntityRenderer(ModSubtiles.REIKARLILY, RenderTileSpecialFlower::new);
        ClientRegistry.bindTileEntityRenderer(ModSubtiles.ANNOYING_FLOWER, RenderTileSpecialFlower::new);
        ClientRegistry.bindTileEntityRenderer(ModSubtiles.BLOODY_ENCHANTRESS, RenderTileSpecialFlower::new);
    }

    private ModelHandler() {}
}
