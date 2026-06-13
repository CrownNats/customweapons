package com.customweapons.init;

import com.customweapons.CustomWeaponsMod;
import com.customweapons.items.FlameBladeItem;
import com.customweapons.items.FrostEdgeItem;
import com.customweapons.items.ThunderHammerItem;
import com.customweapons.items.VampireDaggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CustomWeaponsMod.MOD_ID);

    // --- Flame Blade: ignites enemies on hit, AOE fire nova on right-click ---
    public static final RegistryObject<Item> FLAME_BLADE = ITEMS.register("flame_blade",
            () -> new FlameBladeItem(Tiers.DIAMOND, 3, -2.4F,
                    new Item.Properties().rarity(Rarity.RARE).fireResistant()));

    // --- Frost Edge: slows enemies on hit, ice nova that roots enemies on right-click ---
    public static final RegistryObject<Item> FROST_EDGE = ITEMS.register("frost_edge",
            () -> new FrostEdgeItem(Tiers.DIAMOND, 2, -2.4F,
                    new Item.Properties().rarity(Rarity.RARE)));

    // --- Thunder Hammer: chance to call lightning on hit, targeted lightning strike on right-click ---
    public static final RegistryObject<Item> THUNDER_HAMMER = ITEMS.register("thunder_hammer",
            () -> new ThunderHammerItem(Tiers.NETHERITE, 5, -3.4F,
                    new Item.Properties().rarity(Rarity.EPIC)));

    // --- Vampire Dagger: lifesteal on hit, speed burst on kill ---
    public static final RegistryObject<Item> VAMPIRE_DAGGER = ITEMS.register("vampire_dagger",
            () -> new VampireDaggerItem(Tiers.IRON, 1, -1.6F,
                    new Item.Properties().rarity(Rarity.UNCOMMON)));
}
