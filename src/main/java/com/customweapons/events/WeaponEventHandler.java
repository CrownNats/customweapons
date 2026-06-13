package com.customweapons.events;

import com.customweapons.init.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Handles weapon abilities that depend on global combat events
 * rather than the direct item-hit callback (e.g. "on kill" effects).
 */
public class WeaponEventHandler {

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        LivingEntity victim = event.getEntity();
        var sourceEntity = event.getSource().getEntity();

        if (sourceEntity instanceof Player player) {
            var heldItem = player.getMainHandItem();

            // Vampire Dagger: grant a burst of speed for landing the killing blow
            if (heldItem.getItem() == ModItems.VAMPIRE_DAGGER.get()) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 160, 1)); // Speed II, 8s
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 0));   // small regen burst
            }
        }
    }
}
