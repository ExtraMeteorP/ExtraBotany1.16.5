package com.meteor.extrabotany.client.handler;

import com.meteor.extrabotany.common.libs.LibMisc;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;

public class MiscellaneousIcons {

    public static final MiscellaneousIcons INSTANCE = new MiscellaneousIcons();

    public final IBakedModel[] firstFractalWeaponModels = new IBakedModel[10];
    public final IBakedModel[] strengthenSlashModel = new IBakedModel[1];
    public final IBakedModel[] flamescionringModel = new IBakedModel[1];

    public void onModelRegister(ModelRegistryEvent evt) {
        for (int i = 0; i < 10; i++) {
            ModelLoader.addSpecialModel(new ResourceLocation(LibMisc.MOD_ID,"icon/sworddomain_" + i));
        }
        ModelLoader.addSpecialModel(new ResourceLocation(LibMisc.MOD_ID,"icon/strengthenslash"));
        ModelLoader.addSpecialModel(new ResourceLocation(LibMisc.MOD_ID,"icon/flamescionring"));
    }

    public void onModelBake(ModelBakeEvent evt) {
        for(int i = 0; i < firstFractalWeaponModels.length; i++){
            firstFractalWeaponModels[i] = evt.getModelRegistry().get(new ResourceLocation(LibMisc.MOD_ID, "icon/sworddomain_" + i));
        }
        strengthenSlashModel[0] = evt.getModelRegistry().get(new ResourceLocation(LibMisc.MOD_ID, "icon/strengthenslash"));
        flamescionringModel[0] = evt.getModelRegistry().get(new ResourceLocation(LibMisc.MOD_ID, "icon/flamescionring"));
    }

}
