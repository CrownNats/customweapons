package com.customweapons.items;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

/**
 * Thunder Hammer
 * Passive: Every hit has a 20% chance to call down a lightning bolt on the target.
 * Active (Right-Click): Strikes lightning on whatever block/entity the player is
 *                        looking at, up to 24 blocks away. Cooldown: 30s (600 ticks).
 */
public class ThunderHammerItem extends SwordItem {

    private static final int ABILITY_COOLDOWN_TICKS = 600; // 30 seconds
    private static final double ABILITY_RANGE = 24.0D;
    private static final float ON_HIT_LIGHTNING_CHANCE = 0.20F;

    public ThunderHammerItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);

        if (!attacker.level().isClientSide && attacker.getRandom().nextFloat() < ON_HIT_LIGHTNING_CHANCE) {
            strikeLightningAt(attacker.level(), target.getX(), target.getY(), target.getZ());
        }

        return result;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide) {
            Vec3 eyePos = player.getEyePosition(1.0F);
            Vec3 lookVec = player.getLookAngle();
            Vec3 reachPos = eyePos.add(lookVec.x * ABILITY_RANGE, lookVec.y * ABILITY_RANGE, lookVec.z * ABILITY_RANGE);

            ClipContext context = new ClipContext(eyePos, reachPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player);
            HitResult hitResult = level.clip(context);

            Vec3 strikePos = hitResult.getLocation();
            strikeLightningAt(level, strikePos.x, strikePos.y, strikePos.z);

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.TRIDENT_THUNDER, SoundSource.PLAYERS, 2.0F, 1.0F);
        }

        player.getCooldowns().addCooldown(this, ABILITY_COOLDOWN_TICKS);
        player.swing(hand, true);
        return InteractionResultHolder.success(stack);
    }

    private void strikeLightningAt(Level level, double x, double y, double z) {
        if (level instanceof ServerLevel serverLevel) {
            BlockPos pos = BlockPos.containing(x, y, z);
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
            if (bolt != null) {
                bolt.moveTo(Vec3.atBottomCenterOf(pos));
                bolt.setVisualOnly(false); // deals real damage and can ignite
                serverLevel.addFreshEntity(bolt);
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 1;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.customweapons.thunder_hammer"));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
