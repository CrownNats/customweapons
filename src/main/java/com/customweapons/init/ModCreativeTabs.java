package com.customweapons.init;

import com.customweapons.CustomWeaponsMod;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(ForgeRegistries.CREATIVE_MODE_TABS, CustomWeaponsMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> CUSTOM_WEAPONS_TAB = CREATIVE_MODE_TABS.register("custom_weapons_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.customweapons.custom_weapons_tab"))
                    .icon(() -> new ItemStack(ModItems.FLAME_BLADE.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.FLAME_BLADE.get());
                        output.accept(ModItems.FROST_EDGE.get());
                        output.accept(ModItems.THUNDER_HAMMER.get());
                        output.accept(ModItems.VAMPIRE_DAGGER.get());
                    })
                    .build());
}
