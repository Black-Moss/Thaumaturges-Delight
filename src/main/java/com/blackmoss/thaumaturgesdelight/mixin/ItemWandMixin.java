package com.blackmoss.thaumaturgesdelight.mixin;

import com.blackmoss.thaumaturgesdelight.registry.TDWandParts;
import com.leclowndu93150.thaumaturge.content.wands.ItemWand;
import com.leclowndu93150.thaumaturge.content.wands.WandParts;
import com.leclowndu93150.thaumaturge.content.wands.WandVisHelper;
import com.leclowndu93150.thaumaturge.registry.TTWandParts;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemWand.class)
public class ItemWandMixin {
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
}
