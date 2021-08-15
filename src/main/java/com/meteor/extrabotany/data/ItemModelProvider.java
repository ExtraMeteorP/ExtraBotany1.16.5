package com.meteor.extrabotany.data;

import com.meteor.extrabotany.common.libs.LibMisc;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Set;
import java.util.stream.Collectors;

import static com.meteor.extrabotany.common.items.ModItems.*;

public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider{

    public ItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        Set<Item> items = Registry.ITEM.stream().filter(i -> LibMisc.MOD_ID.equals(Registry.ITEM.getKey(i).getNamespace()))
                .collect(Collectors.toSet());
        registerItemOverrides(items);
    }

    private static String name(Item i) {
        return Registry.ITEM.getKey(i).getPath();
    }

    private static final ResourceLocation GENERATED = new ResourceLocation("item/generated");

    private void registerItemOverrides(Set<Item> items) {

        ItemModelBuilder cocktailBuilder = withExistingParent(name(cocktail), GENERATED)
                .texture("layer0", prefix("item/" + name(emptybottle)))
                .texture("layer1", prefix("item/" + name(cocktail) + "_0"));
        for (int i = 1; i <= 7; i++) {
            String overrideName = name(cocktail) + "_" + i;
            ModelFile overrideModel = withExistingParent(overrideName, GENERATED)
                    .texture("layer0", prefix("item/" + name(emptybottle)))
                    .texture("layer1", prefix("item/" + overrideName));
            cocktailBuilder.override()
                    .predicate(prefix("swigs_taken"), i)
                    .model(overrideModel).end();
        }
        items.remove(cocktail);

        ItemModelBuilder infinitewineBuilder = withExistingParent(name(infinitewine), GENERATED)
                .texture("layer0", prefix("item/" + name(infinitewine)))
                .texture("layer1", prefix("item/" + name(infinitewine) + "_0"));
        for (int i = 1; i <= 11; i++) {
            String overrideName = name(cocktail) + "_" + i;
            ModelFile overrideModel = withExistingParent(overrideName, GENERATED)
                    .texture("layer0", prefix("item/" + name(infinitewine)))
                    .texture("layer1", prefix("item/" + overrideName));
            infinitewineBuilder.override()
                    .predicate(prefix("swigs_taken"), i)
                    .model(overrideModel).end();
        }
        items.remove(infinitewine);
    }

}
