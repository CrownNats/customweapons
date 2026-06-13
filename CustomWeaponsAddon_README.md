# Custom Weapons Addon (Forge 1.20.1)

A Forge mod that adds four custom weapons, each with a passive on-hit effect
and an active right-click ability with a cooldown.

## Weapons

| Item | Tier base | Passive (on hit) | Active (right-click) | Cooldown |
|---|---|---|---|---|
| **Flame Blade** | Diamond | Ignites target for 4s | Fire nova: 6 dmg + ignite to all enemies within 4 blocks | 16s |
| **Frost Edge** | Diamond | Slowness II for 3s | Ice nova: Slowness IV + Weakness II to enemies within 5 blocks for 5s | 18s |
| **Thunder Hammer** | Netherite | 20% chance to call lightning on target | Strikes lightning wherever you're looking, up to 24 blocks | 30s |
| **Vampire Dagger** | Iron | Heals 0.75 hearts | (passive only) — killing blow grants Speed II (8s) + a Regen tick | n/a |

All four are added to the vanilla "Combat" creative tab and to a new
"Custom Weapons" creative tab.

## Project layout

```
CustomWeaponsAddon/
├── build.gradle
├── settings.gradle
├── gradle.properties
└── src/main/
    ├── java/com/customweapons/
    │   ├── CustomWeaponsMod.java        (main mod class, registration)
    │   ├── init/ModItems.java           (item registry)
    │   ├── init/ModCreativeTabs.java    (custom creative tab)
    │   ├── items/FlameBladeItem.java
    │   ├── items/FrostEdgeItem.java
    │   ├── items/ThunderHammerItem.java
    │   ├── items/VampireDaggerItem.java
    │   └── events/WeaponEventHandler.java (kill-triggered effects)
    └── resources/
        ├── META-INF/mods.toml
        ├── pack.mcmeta
        └── assets/customweapons/
            ├── lang/en_us.json
            ├── models/item/*.json
            └── textures/item/*.png  (simple placeholder textures — replace these!)
```

## How to build

This project is a standard **Forge MDK (ModDev Kit)** Gradle project targeting
**Minecraft 1.20.1** with **Forge 47.3.0**. To build it:

1. Make sure you have **JDK 17** installed.
2. Open the project folder in your terminal (or import it into IntelliJ IDEA / Eclipse).
3. Run:
   - Linux/macOS: `./gradlew build`
   - Windows: `gradlew.bat build`
4. The first run will download Forge, Minecraft, and all dependencies
   (requires an internet connection to `maven.minecraftforge.net` and
   `libraries.minecraft.net`/`piston-meta.mojang.com`).
5. The compiled mod jar will appear in `build/libs/customweapons-1.0.0.jar`.

To test it quickly in a dev environment:
```
./gradlew runClient
```
This launches a development Minecraft client with the mod already loaded.

## Installing the finished jar

1. Build the project as above (or have someone build it for you).
2. Install **Forge 47.3.0 for Minecraft 1.20.1** (from files.minecraftforge.net).
3. Drop `customweapons-1.0.0.jar` into your `.minecraft/mods/` folder.
4. Launch the Forge profile — the weapons will appear in Creative under
   "Combat" and "Custom Weapons", or can be given with:
   ```
   /give @s customweapons:flame_blade
   /give @s customweapons:frost_edge
   /give @s customweapons:thunder_hammer
   /give @s customweapons:vampire_dagger
   ```

## Notes / customization

- **Textures**: the included `.png` files in `textures/item/` are simple
  generated placeholders. Replace them with real 16x16 artwork (same
  filenames) for proper visuals — no code changes needed.
- **Balance tuning**: all numbers (damage, radius, cooldowns, effect
  durations) are defined as constants at the top of each item class in
  `src/main/java/com/customweapons/items/`, so they're easy to tweak.
- **Adding more weapons**: follow the pattern in `ModItems.java` — register
  a new item, give it a class extending `SwordItem` (or another `DiggerItem`
  subclass), override `hurtEnemy` for passive effects and `use` for an
  active ability, then add a model JSON + texture + lang entry.
- If you'd rather target a newer Minecraft/Forge version (e.g. 1.21.x),
  update `gradle.properties` (`minecraft_version`, `forge_version`,
  `mapping_version`) — the Java code here uses APIs that are stable across
  1.20.1–1.21.x, but some method names (e.g. lightning entity creation,
  particle methods) may need small adjustments depending on the exact
  mappings for that version.
