package com.customweapons.items;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
 * Frost Edge
 * Passive: Hitting an enemy applies Slowness II for 3 seconds.
 * Active (Right-Click): Releases an ice nova that applies Slowness IV and
 *                        Weakness II to all enemies within 5 blocks for 5 seconds.
 *                        Cooldown: 18s (360 ticks).
 */
public class FrostEdgeItem extends SwordItem {

    private static final int ABILITY_COOLDOWN_TICKS = 360; // 18 seconds
    private static final double ABILITY_RADIUS = 5.0D;

    public FrostEdgeItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);
        // Passive ability: chill the target on every hit
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1)); // Slowness II, 3s
        return result;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide) {
            List<LivingEntity> targets = level.getEntitiesOfClass(
                    LivingEntity.class,
                    player.getBoundingBox().inflate(ABILITY_RADIUS),
                    e -> e != player && e.isAlive()
            );

            for (LivingEntity target : targets) {
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 3)); // Slowness IV, 5s
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));          // Weakness II, 5s
            }

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SNOWFLAKE,
                        player.getX(), player.getY() + 0.5, player.getZ(),
                        100, ABILITY_RADIUS / 2.0, 0.5, ABILITY_RADIUS / 2.0, 0.02);
                serverLevel.sendParticles(ParticleTypes.CLOUD,
                        player.getX(), player.getY() + 0.2, player.getZ(),
                        40, ABILITY_RADIUS / 2.0, 0.1, ABILITY_RADIUS / 2.0, 0.0);
            }

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.2F, 1.4F);
        }

        player.getCooldowns().addCooldown(this, ABILITY_COOLDOWN_TICKS);
        player.swing(hand, true);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 1;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.customweapons.frost_edge"));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
