package com.meteor.extrabotany.data;

import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import vazkii.botania.data.ItemModelProvider;

public class DataGenerators {

    public static void gatherData(GatherDataEvent evt) {
        if (evt.includeServer()) {
        }
        if (evt.includeClient()) {
            evt.getGenerator().addProvider(new ItemModelProvider(evt.getGenerator(), evt.getExistingFileHelper()));
        }
    }
}
