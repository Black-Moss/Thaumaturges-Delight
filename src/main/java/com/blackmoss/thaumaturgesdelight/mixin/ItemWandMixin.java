package com.blackmoss.thaumaturgesdelight.mixin;

import com.blackmoss.thaumaturgesdelight.registry.TDWandParts;
import com.leclowndu93150.thaumaturge.content.wands.ItemWand;
import com.leclowndu93150.thaumaturge.content.wands.WandParts;
import com.leclowndu93150.thaumaturge.content.wands.WandVisHelper;
import com.leclowndu93150.thaumaturge.registry.TTWandParts;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(ItemWand.class)
public class ItemWandMixin {
    @Unique
    private static final String EATING_KEY = "thaumaturgesdelight.eating";
    @Unique
    private static final String EATEN_KEY = "thaumaturgesdelight.eaten";
    @Unique
    private static final int EAT_DURATION_TICKS = 32;
    @Unique
    private static final int VIS_COST_CENTIVIS = 200;

    @Unique
    private static boolean thaumaturgesdelight$hasFlag(ItemStack stack, String key) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data != null && data.copyTag().getBooleanOr(key, false);
    }

    @Unique
    private static boolean thaumaturgesdelight$isEating(ItemStack stack) {
        return thaumaturgesdelight$hasFlag(stack, EATING_KEY) || thaumaturgesdelight$hasFlag(stack, EATEN_KEY);
    }

    @Unique
    private static void thaumaturgesdelight$setFlag(ItemStack stack, String key) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putBoolean(key, true));
    }

    @Unique
    private static void thaumaturgesdelight$clearFlag(ItemStack stack, String key) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.remove(key));
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data != null && data.isEmpty()) {
            stack.remove(DataComponents.CUSTOM_DATA);
        }
    }

    @Unique
    private static int thaumaturgesdelight$edibleNutrition(ItemStack stack) {
        WandParts parts = WandVisHelper.partsOf(stack);
        if (parts == null || parts.cap() == null || parts.rod() == null) {
            return 0;
        }

        Identifier capKey = TTWandParts.caps().getKey(parts.cap());
        Identifier rodKey = TTWandParts.rods().getKey(parts.rod());
        if (capKey == null || rodKey == null) {
            return 0;
        }

        boolean ketchup = TDWandParts.KETCHUP_CAP.getId().equals(capKey);
        boolean frenchFry = TDWandParts.FRENCH_FRY_ROD.getId().equals(rodKey);
        boolean reedRod = TTWandParts.ROD_REED.getId().equals(rodKey);
        boolean reedStaff = TTWandParts.STAFF_REED.getId().equals(rodKey);
        if (!ketchup && !frenchFry && !reedRod && !reedStaff) {
            return 0;
        }

        return ketchup && frenchFry ? 3 : 1;
    }

    @Inject(method = "getName", at = @At("RETURN"), cancellable = true)
    private void thaumaturgesdelight$getName(ItemStack stack, CallbackInfoReturnable<Component> cir) {
        WandParts parts = WandVisHelper.partsOf(stack);
        if (parts == null || parts.cap() == null || parts.rod() == null) {
            return;
        }

        Identifier capKey = TTWandParts.caps().getKey(parts.cap());
        if (capKey == null || !TDWandParts.KETCHUP_CAP.getId().equals(capKey)) {
            return;
        }

        String capPath = capKey.getPath();
        Identifier rodKey = TTWandParts.rods().getKey(parts.rod());
        if (rodKey == null) {
            return;
        }

        String rodPath = rodKey.getPath();
        if (rodPath.endsWith("_staff")) {
            rodPath = rodPath.substring(0, rodPath.length() - "_staff".length());
        }

        Component cap = Component.translatable("wand.thaumaturge.cap." + capPath)
                .copy().withStyle(ChatFormatting.RED);
        Component rod = Component.translatable("wand.thaumaturge.rod." + rodPath);
        String template = parts.rod().staff()
                ? "item.thaumaturge.wand.staff"
                : (parts.sceptre() ? "item.thaumaturge.wand.sceptre" : "item.thaumaturge.wand.named");
        cir.setReturnValue(Component.translatable(template, cap, rod));
    }

    @Inject(method = "appendHoverText", at = @At("RETURN"))
    private void thaumaturgesdelight$appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            TooltipDisplay display,
            Consumer<Component> builder,
            TooltipFlag flag,
            CallbackInfo ci) {
        WandParts parts = WandVisHelper.partsOf(stack);
        if (parts == null || parts.cap() == null || parts.rod() == null) {
            return;
        }

        Identifier capKey = TTWandParts.caps().getKey(parts.cap());
        Identifier rodKey = TTWandParts.rods().getKey(parts.rod());
        if (capKey == null || rodKey == null) {
            return;
        }

        boolean ketchup = TDWandParts.KETCHUP_CAP.getId().equals(capKey);
        boolean frenchFry = TDWandParts.FRENCH_FRY_ROD.getId().equals(rodKey);
        if (!ketchup && !frenchFry) {
            return;
        }

        builder.accept(Component.translatable("tooltip.thaumaturgesdelight.ketchup_on_french_fry.can_lick")
                .copy().withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));

        if (ketchup && frenchFry) {
            builder.accept(Component.translatable("tooltip.thaumaturgesdelight.ketchup_on_french_fry")
                    .copy().withStyle(ChatFormatting.GOLD));
        }
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void thaumaturgesdelight$use(
            Level level,
            Player player,
            InteractionHand hand,
            CallbackInfoReturnable<InteractionResult> cir) {
        if (!player.isShiftKeyDown()) {
            return;
        }

        FoodData food = player.getFoodData();
        if (food.getFoodLevel() >= 20) {
            return;
        }

        ItemStack held = player.getItemInHand(hand);
        if (thaumaturgesdelight$edibleNutrition(held) <= 0) {
            return;
        }

        if (!WandVisHelper.payCosts(held, player, WandVisHelper.evenSplit(VIS_COST_CENTIVIS), false, false)) {
            return;
        }

        thaumaturgesdelight$clearFlag(held, EATEN_KEY);
        thaumaturgesdelight$setFlag(held, EATING_KEY);
        player.startUsingItem(hand);
        cir.setReturnValue(InteractionResult.CONSUME);
    }

    @Inject(method = "getUseDuration", at = @At("RETURN"), cancellable = true)
    private void thaumaturgesdelight$getUseDuration(
            ItemStack stack,
            LivingEntity user,
            CallbackInfoReturnable<Integer> cir) {
        if (thaumaturgesdelight$isEating(stack)) {
            cir.setReturnValue(EAT_DURATION_TICKS);
        }
    }

    @Inject(method = "onUseTick", at = @At("HEAD"), cancellable = true)
    private void thaumaturgesdelight$onUseTick(
            Level level,
            LivingEntity user,
            ItemStack stack,
            int remaining,
            CallbackInfo ci) {
        if (!thaumaturgesdelight$isEating(stack)) {
            return;
        }

        ci.cancel();

        if (thaumaturgesdelight$hasFlag(stack, EATEN_KEY)) {
            return;
        }

        if (!level.isClientSide() && remaining > 1 && remaining % 4 == 0) {
            level.playSound(
                    null,
                    user.getX(),
                    user.getY(),
                    user.getZ(),
                    SoundEvents.GENERIC_EAT.value(),
                    SoundSource.PLAYERS,
                    0.8F,
                    0.8F + level.getRandom().nextFloat() * 0.4F);
        }

        if (remaining > 1) {
            return;
        }

        thaumaturgesdelight$clearFlag(stack, EATING_KEY);
        thaumaturgesdelight$setFlag(stack, EATEN_KEY);
        if (!level.isClientSide() && user instanceof Player player) {
            if (WandVisHelper.payCosts(stack, player, WandVisHelper.evenSplit(VIS_COST_CENTIVIS), true, false)) {
                int nutrition = thaumaturgesdelight$edibleNutrition(stack);
                if (nutrition > 0) {
                    player.getFoodData().eat(nutrition, nutrition * 0.5F);
                }
            }
        }
    }

    @Inject(method = "releaseUsing", at = @At("HEAD"), cancellable = true)
    private void thaumaturgesdelight$releaseUsing(
            ItemStack stack,
            Level level,
            LivingEntity entity,
            int remaining,
            CallbackInfoReturnable<Boolean> cir) {
        if (!thaumaturgesdelight$isEating(stack)) {
            return;
        }

        thaumaturgesdelight$clearFlag(stack, EATING_KEY);
        thaumaturgesdelight$clearFlag(stack, EATEN_KEY);
        cir.setReturnValue(false);
    }
}
