package com.terrarockys.soulsreborn.item;

import com.terrarockys.soulsreborn.SoulsRebornMod;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SoulsRebornMod.MODID);

    public static final DeferredItem<Item> BONFIRE_KIT = ITEMS.register("bonfire_kit",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ESTUS_FLASK = ITEMS.register("estus_flask",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}