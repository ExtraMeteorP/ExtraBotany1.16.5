package com.meteor.extrabotany.common.core;

import com.meteor.extrabotany.common.libs.LibMisc;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModSounds {

    public static final SoundEvent cyclone = makeSoundEvent("cyclone");
    public static final SoundEvent rideon = makeSoundEvent("rideon");
    public static final SoundEvent shoot = makeSoundEvent("shoot");
    public static final SoundEvent slash = makeSoundEvent("slash");
    public static final SoundEvent paimon_0 = makeSoundEvent("paimon_0");
    public static final SoundEvent paimon_1 = makeSoundEvent("paimon_1");
    public static final SoundEvent paimon_2 = makeSoundEvent("paimon_2");
    public static final SoundEvent paimon_3 = makeSoundEvent("paimon_3");
    public static final SoundEvent paimon_4 = makeSoundEvent("paimon_4");
    public static final SoundEvent paimon_5 = makeSoundEvent("paimon_5");
    public static final SoundEvent paimon_6 = makeSoundEvent("paimon_6");
    public static final SoundEvent paimon_7 = makeSoundEvent("paimon_7");
    public static final SoundEvent paimon_8 = makeSoundEvent("paimon_8");
    public static final SoundEvent paimon_9 = makeSoundEvent("paimon_9");
    public static final SoundEvent paimon_10 = makeSoundEvent("paimon_10");
    public static final SoundEvent paimon_11 = makeSoundEvent("paimon_11");
    public static final SoundEvent paimon_12 = makeSoundEvent("paimon_12");
    public static final SoundEvent paimon_13 = makeSoundEvent("paimon_13");
    public static final SoundEvent paimon_tp_0 = makeSoundEvent("paimon_tp_0");
    public static final SoundEvent paimon_tp_1 = makeSoundEvent("paimon_tp_1");
    public static final SoundEvent paimon_tp_2 = makeSoundEvent("paimon_tp_2");
    public static final SoundEvent paimon_tp_3 = makeSoundEvent("paimon_tp_3");
    public static final SoundEvent paimon_spawn_0 = makeSoundEvent("paimon_spawn_0");
    public static final SoundEvent paimon_vanish_0 = makeSoundEvent("paimon_vanish_0");
    public static final SoundEvent paimon_vanish_1 = makeSoundEvent("paimon_vanish_1");
    public static final SoundEvent paimon_vanish_2 = makeSoundEvent("paimon_vanish_2");
    public static final SoundEvent paimon_vanish_3 = makeSoundEvent("paimon_vanish_3");
    public static final SoundEvent paimon_vanish_4 = makeSoundEvent("paimon_vanish_4");
    public static final SoundEvent paimon_vanish_5 = makeSoundEvent("paimon_vanish_5");
    public static final SoundEvent paimon_vanish_6 = makeSoundEvent("paimon_vanish_6");
    public static final SoundEvent paimon_thank_0 = makeSoundEvent("paimon_thank_0");
    public static final SoundEvent paimon_thank_1 = makeSoundEvent("paimon_thank_1");
    public static final SoundEvent paimon_thank_2 = makeSoundEvent("paimon_thank_2");
    public static final SoundEvent flamescionult = makeSoundEvent("flamescionult");

    private static SoundEvent makeSoundEvent(String name) {
        ResourceLocation loc = new ResourceLocation(LibMisc.MOD_ID, name);
        return new SoundEvent(loc).setRegistryName(loc);
    }

    public static void registerSounds(RegistryEvent.Register<SoundEvent> evt) {
        IForgeRegistry<SoundEvent> r = evt.getRegistry();
        r.register(cyclone);
        r.register(rideon);
        r.register(shoot);
        r.register(slash);
        r.register(paimon_0);
        r.register(paimon_1);
        r.register(paimon_2);
        r.register(paimon_3);
        r.register(paimon_4);
        r.register(paimon_5);
        r.register(paimon_6);
        r.register(paimon_7);
        r.register(paimon_8);
        r.register(paimon_9);
        r.register(paimon_10);
        r.register(paimon_11);
        r.register(paimon_12);
        r.register(paimon_13);
        r.register(paimon_tp_0);
        r.register(paimon_tp_1);
        r.register(paimon_tp_2);
        r.register(paimon_tp_3);
        r.register(paimon_spawn_0);
        r.register(paimon_vanish_0);
        r.register(paimon_vanish_1);
        r.register(paimon_vanish_2);
        r.register(paimon_vanish_3);
        r.register(paimon_vanish_4);
        r.register(paimon_vanish_5);
        r.register(paimon_vanish_6);
        r.register(paimon_thank_0);
        r.register(paimon_thank_1);
        r.register(paimon_thank_2);
        r.register(flamescionult);
    }

    private ModSounds() {}
}
