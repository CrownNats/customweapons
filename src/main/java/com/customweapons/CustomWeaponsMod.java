package com.customweapons;

import com.customweapons.init.ModCreativeTabs;
import com.customweapons.init.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(CustomWeaponsMod.MOD_ID)
public class CustomWeaponsMod {

    public static final String MOD_ID = "customweapons";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public CustomWeaponsMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register deferred registries
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        // Register the runtime event handler (combat events, abilities, etc.)
        MinecraftForge.EVENT_BUS.register(new com.customweapons.events.WeaponEventHandler());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Custom Weapons Addon: common setup complete.");
    }

    // Add our items to the vanilla "Combat" tab AND to our own custom tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.FLAME_BLADE);
            event.accept(ModItems.FROST_EDGE);
            event.accept(ModItems.THUNDER_HAMMER);
            event.accept(ModItems.VAMPIRE_DAGGER);
        }
    }
}
