package com.customweapons.items;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

/**
 * Vampire Dagger
 * Passive: Heals the wielder for a flat amount on every successful hit.
 * Bonus (handled in WeaponEventHandler): Killing a mob/player with this dagger
 *        grants the wielder Speed II for 8 seconds.
 */
public class VampireDaggerItem extends SwordItem {

    /** Health points restored per hit (2.0F = 1 heart) */
    public static final float LIFESTEAL_AMOUNT = 1.5F;

    public VampireDaggerItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);

        if (attacker instanceof Player player) {
            player.heal(LIFESTEAL_AMOUNT);

            if (player.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.HEART,
                        target.getX(), target.getY() + 1.2, target.getZ(),
                        3, 0.3, 0.3, 0.3, 0.0);
            }
        }

        return result;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.customweapons.vampire_dagger"));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
