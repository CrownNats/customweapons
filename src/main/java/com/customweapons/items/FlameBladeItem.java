package com.customweapons.items;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
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
 * Flame Blade
 * Passive: Hitting an enemy sets it on fire for 4 seconds.
 * Active (Right-Click): Releases a fire nova that damages and ignites
 *                        all enemies within 4 blocks. Cooldown: 16s (320 ticks).
 */
public class FlameBladeItem extends SwordItem {

    private static final int ABILITY_COOLDOWN_TICKS = 320; // 16 seconds
    private static final double ABILITY_RADIUS = 4.0D;
    private static final float ABILITY_DAMAGE = 6.0F;

    public FlameBladeItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);
        // Passive ability: ignite the target on every hit
        target.setSecondsOnFire(4);
        return result;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide) {
            // Damage and ignite everything nearby
            List<LivingEntity> targets = level.getEntitiesOfClass(
                    LivingEntity.class,
                    player.getBoundingBox().inflate(ABILITY_RADIUS),
                    e -> e != player && e.isAlive()
            );

            DamageSource fireDamage = level.damageSources().playerAttack(player);
            for (LivingEntity target : targets) {
                target.hurt(fireDamage, ABILITY_DAMAGE);
                target.setSecondsOnFire(6);
            }

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.FLAME,
                        player.getX(), player.getY() + 0.5, player.getZ(),
                        80, ABILITY_RADIUS / 2.0, 0.5, ABILITY_RADIUS / 2.0, 0.05);
                serverLevel.sendParticles(ParticleTypes.LAVA,
                        player.getX(), player.getY() + 0.2, player.getZ(),
                        20, ABILITY_RADIUS / 2.0, 0.1, ABILITY_RADIUS / 2.0, 0.0);
            }

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.5F, 0.8F);
        }

        player.getCooldowns().addCooldown(this, ABILITY_COOLDOWN_TICKS);
        player.swing(hand, true);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 1; // instant use, doesn't open a charging animation
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.customweapons.flame_blade"));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
