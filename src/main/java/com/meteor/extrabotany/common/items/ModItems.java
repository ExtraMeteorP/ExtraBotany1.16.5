package com.meteor.extrabotany.common.items;

import com.meteor.extrabotany.ExtraBotany;
import com.meteor.extrabotany.common.items.armor.goblinslayer.ItemGoblinSlayerArmor;
import com.meteor.extrabotany.common.items.armor.maid.ItemMaidArmor;
import com.meteor.extrabotany.common.items.armor.miku.ItemMikuArmor;
import com.meteor.extrabotany.common.items.armor.shadowwarrior.ItemShadowWarriorArmor;
import com.meteor.extrabotany.common.items.armor.shootingguardian.ItemShootingGuardianArmor;
import com.meteor.extrabotany.common.items.bauble.*;
import com.meteor.extrabotany.common.items.brew.ItemCocktail;
import com.meteor.extrabotany.common.items.brew.ItemInfiniteWine;
import com.meteor.extrabotany.common.items.brew.ItemSplashGrenade;
import com.meteor.extrabotany.common.items.relic.ItemExcaliber;
import com.meteor.extrabotany.common.items.relic.ItemFailnaught;
import com.meteor.extrabotany.common.items.relic.ItemSagesManaRing;
import com.meteor.extrabotany.common.libs.LibItemNames;
import com.meteor.extrabotany.common.libs.LibMisc;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import vazkii.botania.api.BotaniaAPI;

public class ModItems {

    public static final Food SPIRITFUEL_PROP = (new Food.Builder()).hunger(4).saturation(0.3F)
            .setAlwaysEdible()
            .effect(new EffectInstance(Effects.INSTANT_HEALTH, 1, 2), 1.0F)
            .effect(new EffectInstance(Effects.NIGHT_VISION, 500), 1.0F)
            .effect(new EffectInstance(Effects.SPEED, 500), 1.0F)
            .effect(new EffectInstance(Effects.LUCK, 500), 1.0F)
            .effect(new EffectInstance(Effects.STRENGTH, 500), 1.0F)
            .build();
    public static final Food NIGHTMAREFUEL_PROP = (new Food.Builder()).hunger(4).saturation(0.3F)
            .setAlwaysEdible()
            .effect(new EffectInstance(Effects.INSTANT_DAMAGE, 1, 2), 1.0F)
            .effect(new EffectInstance(Effects.BLINDNESS, 500), 1.0F)
            .effect(new EffectInstance(Effects.SLOWNESS, 500), 1.0F)
            .effect(new EffectInstance(Effects.UNLUCK, 500), 1.0F)
            .effect(new EffectInstance(Effects.WEAKNESS, 500), 1.0F)
            .build();
    public static final Food GILDEDMASHEDPOTATO_PROP = (new Food.Builder()).hunger(5).saturation(0.2F)
            .setAlwaysEdible()
            .effect(new EffectInstance(Effects.RESISTANCE, 600, 3), 1.0F)
            .effect(new EffectInstance(Effects.SLOWNESS, 600, 3), 1.0F)
            .effect(new EffectInstance(Effects.ABSORPTION, 600, 1), 1.0F)
            .build();
    public static final Food MANADRINK_PROP = (new Food.Builder()).hunger(0).saturation(0F)
            .setAlwaysEdible()
            .effect(new EffectInstance(Effects.RESISTANCE, 1200, 0), 1.0F)
            .effect(new EffectInstance(Effects.SPEED, 1200, 0), 1.0F)
            .effect(new EffectInstance(Effects.JUMP_BOOST, 1200, 0), 1.0F)
            .build();
    public static final Food FRIEDCHICKEN_PROP = (new Food.Builder()).hunger(6).saturation(0.5F).build();

    public static final Item spiritfuel = new Item(defaultBuilder().food(SPIRITFUEL_PROP));
    public static final Item nightmarefuel = new Item(defaultBuilder().food(NIGHTMAREFUEL_PROP));
    public static final Item friedchicken = new Item(defaultBuilder().food(FRIEDCHICKEN_PROP));
    public static final Item gildedmashedpotato = new Item(defaultBuilder().food(GILDEDMASHEDPOTATO_PROP));

    public static final Item motor = new ItemMotor();
    public static final Item gemofconquest = new ItemGemOfConquest();
    public static final Item firstfractal = new ItemFirstFractal();
    public static final Item cosmiccarkey = new ItemCosmicCarKey();
    public static final Item flamescionweapon = new ItemFlamescionWeapon();
    public static final Item silverbullet = new ItemSilverBullet(unstackable());

    public static final Item aerostone = new ItemAeroStone(unstackable());
    public static final Item earthstone = new ItemEarthStone(unstackable());
    public static final Item aquastone = new ItemAquaStone(unstackable());
    public static final Item ignisstone = new ItemIgnisStone(unstackable());
    public static final Item thecommunity = new ItemTheCommunity(unstackable());

    public static final Item sagesmanaring = new ItemSagesManaRing(relic());
    public static final Item excaliber = new ItemExcaliber(relic());
    public static final Item failnaught = new ItemFailnaught(relic());

    public static final Item spirit = new Item(defaultBuilder());
    public static final Item orichalcos = new Item(defaultBuilder());
    public static final Item gildedpotato = new Item(defaultBuilder());
    public static final Item heromedal = new Item(defaultBuilder());
    public static final Item shadowium = new Item(defaultBuilder());
    public static final Item goldcloth = new Item(defaultBuilder());
    public static final Item photonium = new Item(defaultBuilder());
    public static final Item emptybottle = new Item(defaultBuilder());
    public static final Item aerialite = new Item(defaultBuilder());
    public static final Item thechaos = new Item(defaultBuilder());
    public static final Item theorigin = new Item(defaultBuilder());
    public static final Item theend = new Item(defaultBuilder());
    public static final Item theuniverse = new Item(defaultBuilder());

    public static final Item manadrink = new ItemManaDrink(defaultBuilder().food(MANADRINK_PROP));
    public static final Item cocktail = new ItemCocktail(unstackable());
    public static final Item infinitewine = new ItemInfiniteWine(unstackable());
    public static final Item splashgrenade = new ItemSplashGrenade(defaultBuilder().maxStackSize(32));

    public static final Item foxear = new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.FOX_EAR, unstackable());
    public static final Item foxmask = new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.FOX_MASK, unstackable());
    public static final Item blackglasses = new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.BLACK_GLASSES, unstackable());
    public static final Item thuglife = new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.THUG_LIFE, unstackable());
    public static final Item redscarf = new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.RED_SCARF, unstackable());
    public static final Item supercrown = new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.SUPER_CROWN, unstackable());
    public static final Item pylon = new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.PYLON, unstackable());
    public static final Item mask = new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.MASK, unstackable());

    public static final Item armor_maid_helm = new ItemMaidArmor(EquipmentSlotType.HEAD, unstackable());
    public static final Item armor_maid_chest = new ItemMaidArmor(EquipmentSlotType.CHEST, unstackable());
    public static final Item armor_maid_legs = new ItemMaidArmor(EquipmentSlotType.LEGS, unstackable());
    public static final Item armor_maid_boots = new ItemMaidArmor(EquipmentSlotType.FEET, unstackable());

    public static final Item armor_miku_helm = new ItemMikuArmor(EquipmentSlotType.HEAD, unstackable());
    public static final Item armor_miku_chest = new ItemMikuArmor(EquipmentSlotType.CHEST, unstackable());
    public static final Item armor_miku_legs = new ItemMikuArmor(EquipmentSlotType.LEGS, unstackable());
    public static final Item armor_miku_boots = new ItemMikuArmor(EquipmentSlotType.FEET, unstackable());

    public static final Item armor_goblinslayer_helm = new ItemGoblinSlayerArmor(EquipmentSlotType.HEAD, unstackable());
    public static final Item armor_goblinslayer_chest = new ItemGoblinSlayerArmor(EquipmentSlotType.CHEST, unstackable());
    public static final Item armor_goblinslayer_legs = new ItemGoblinSlayerArmor(EquipmentSlotType.LEGS, unstackable());
    public static final Item armor_goblinslayer_boots = new ItemGoblinSlayerArmor(EquipmentSlotType.FEET, unstackable());

    public static final Item armor_shadowwarrior_helm = new ItemShadowWarriorArmor(EquipmentSlotType.HEAD, unstackable());
    public static final Item armor_shadowwarrior_chest = new ItemShadowWarriorArmor(EquipmentSlotType.CHEST, unstackable());
    public static final Item armor_shadowwarrior_legs = new ItemShadowWarriorArmor(EquipmentSlotType.LEGS, unstackable());
    public static final Item armor_shadowwarrior_boots = new ItemShadowWarriorArmor(EquipmentSlotType.FEET, unstackable());

    public static final Item armor_shootingguardian_helm = new ItemShootingGuardianArmor(EquipmentSlotType.HEAD, unstackable());
    public static final Item armor_shootingguardian_chest = new ItemShootingGuardianArmor(EquipmentSlotType.CHEST, unstackable());
    public static final Item armor_shootingguardian_legs = new ItemShootingGuardianArmor(EquipmentSlotType.LEGS, unstackable());
    public static final Item armor_shootingguardian_boots = new ItemShootingGuardianArmor(EquipmentSlotType.FEET, unstackable());

    public static Item.Properties defaultBuilder() {
        return new Item.Properties().group(ExtraBotany.itemGroup);
    }

    private static Item.Properties unstackable() {
        return defaultBuilder().maxStackSize(1);
    }

    private static Item.Properties relic(){
        return defaultBuilder().rarity(BotaniaAPI.instance().getRelicRarity()).setNoRepair();
    };

    public static void registerItems(RegistryEvent.Register<Item> evt) {
        IForgeRegistry<Item> r = evt.getRegistry();
        register(r, LibItemNames.SPIRITFUEL, spiritfuel);
        register(r, LibItemNames.NIGHTMAREFUEL, nightmarefuel);
        register(r, LibItemNames.GILDEDMASHEDPOTATO, gildedmashedpotato);
        register(r, LibItemNames.FRIEDCHICKEN, friedchicken);

        register(r, LibItemNames.MOTOR, motor);
        register(r, LibItemNames.GEMOFCONQUEST, gemofconquest);
        register(r, LibItemNames.FIRSTFRACTAL, firstfractal);
        register(r, LibItemNames.COSMICCARKEY, cosmiccarkey);
        register(r, LibItemNames.FLAMESCIONWEAPON, flamescionweapon);
        register(r, LibItemNames.SILVERBULLET, silverbullet);

        register(r, LibItemNames.AEROSTONE, aerostone);
        register(r, LibItemNames.EARTHSTONE, earthstone);
        register(r, LibItemNames.AQUASTONE, aquastone);
        register(r, LibItemNames.IGNISSTONE, ignisstone);
        register(r, LibItemNames.THECOMMUNITY, thecommunity);

        register(r, LibItemNames.EXCALIBER, excaliber);
        register(r, LibItemNames.SAGES_MANA_RING, sagesmanaring);
        register(r, LibItemNames.FAILNAUGHT, failnaught);

        register(r, LibItemNames.MANADRINK, manadrink);
        register(r, LibItemNames.BREW_COCKTAIL, cocktail);
        register(r, LibItemNames.BREW_INFINITEWINE, infinitewine);
        register(r, LibItemNames.BREW_SPLASHGRENADE, splashgrenade);

        register(r, LibItemNames.SPIRIT, spirit);
        register(r, LibItemNames.ORICHALCOS, orichalcos);
        register(r, LibItemNames.PHONTONIUM, photonium);
        register(r, LibItemNames.SHADOWIUM, shadowium);
        register(r, LibItemNames.AERIALITE, aerialite);
        register(r, LibItemNames.GILDEDPOTATO, gildedpotato);
        register(r, LibItemNames.HEROMEDAL, heromedal);
        register(r, LibItemNames.GOLDCLOTH, goldcloth);
        register(r, LibItemNames.EMPTYBOTTLE, emptybottle);
        register(r, LibItemNames.THECHAOS, thechaos);
        register(r, LibItemNames.THEORIGIN, theorigin);
        register(r, LibItemNames.THEEND, theend);
        register(r, LibItemNames.THEUNIVERSE, theuniverse);

        register(r, LibItemNames.FOX_EAR, foxear);
        register(r, LibItemNames.FOX_MASK, foxmask);
        register(r, LibItemNames.PYLON, pylon);
        register(r, LibItemNames.BLACK_GLASSES, blackglasses);
        register(r, LibItemNames.RED_SCARF, redscarf);
        register(r, LibItemNames.THUG_LIFE, thuglife);
        register(r, LibItemNames.SUPER_CROWN, supercrown);
        register(r, LibItemNames.MASK, mask);

        register(r, LibItemNames.ARMOR_MAID_HELM, armor_maid_helm);
        register(r, LibItemNames.ARMOR_MAID_CHEST, armor_maid_chest);
        register(r, LibItemNames.ARMOR_MAID_LEGS, armor_maid_legs);
        register(r, LibItemNames.ARMOR_MAID_BOOTS, armor_maid_boots);

        register(r, LibItemNames.ARMOR_MIKU_HELM, armor_miku_helm);
        register(r, LibItemNames.ARMOR_MIKU_CHEST, armor_miku_chest);
        register(r, LibItemNames.ARMOR_MIKU_LEGS, armor_miku_legs);
        register(r, LibItemNames.ARMOR_MIKU_BOOTS, armor_miku_boots);

        register(r, LibItemNames.ARMOR_GOBLINSLAYER_HELM, armor_goblinslayer_helm);
        register(r, LibItemNames.ARMOR_GOBLINSLAYER_CHEST, armor_goblinslayer_chest);
        register(r, LibItemNames.ARMOR_GOBLINSLAYER_LEGS, armor_goblinslayer_legs);
        register(r, LibItemNames.ARMOR_GOBLINSLAYER_BOOTS, armor_goblinslayer_boots);

        register(r, LibItemNames.ARMOR_SHADOWWARRIOR_HELM, armor_shadowwarrior_helm);
        register(r, LibItemNames.ARMOR_SHADOWWARRIOR_CHEST, armor_shadowwarrior_chest);
        register(r, LibItemNames.ARMOR_SHADOWWARRIOR_LEGS, armor_shadowwarrior_legs);
        register(r, LibItemNames.ARMOR_SHADOWWARRIOR_BOOTS, armor_shadowwarrior_boots);

        register(r, LibItemNames.ARMOR_SHOOTINGGUARDIAN_HELM, armor_shootingguardian_helm);
        register(r, LibItemNames.ARMOR_SHOOTINGGUARDIAN_CHEST, armor_shootingguardian_chest);
        register(r, LibItemNames.ARMOR_SHOOTINGGUARDIAN_LEGS, armor_shootingguardian_legs);
        register(r, LibItemNames.ARMOR_SHOOTINGGUARDIAN_BOOTS, armor_shootingguardian_boots);
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, ResourceLocation name, IForgeRegistryEntry<V> thing) {
        reg.register(thing.setRegistryName(name));
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, String name, IForgeRegistryEntry<V> thing) {
        register(reg, prefix(name), thing);
    }

    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(LibMisc.MOD_ID, path);
    }
}
