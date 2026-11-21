package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.lazycat.api.recipe.thaumaturge.ThaumaturgeUtils;
import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.wands.WandCap;
import com.leclowndu93150.thaumaturge.api.wands.WandRod;
import com.leclowndu93150.thaumaturge.content.equipment.InfusionEnchantmentHelper;
import com.leclowndu93150.thaumaturge.content.wands.ItemWand;
import com.leclowndu93150.thaumaturge.content.wands.WandVisHelper;
import com.leclowndu93150.thaumaturge.registry.TTItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Comparator;
import java.util.Objects;

public final class TDCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ThaumaturgesDelight.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> THAUMATURGES_DELIGHT;
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ASPECT_FOODS;

    static {
        THAUMATURGES_DELIGHT = CREATIVE_MODE_TABS.register(ThaumaturgesDelight.MODID,
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("itemGroup.thaumaturgesdelight.thaumaturgesdelight"))
                        .icon(() -> TDItems.ELEMENTAL_KNIFE.get().getDefaultInstance())
                        .displayItems((_, output) -> {
                            output.accept(TDItems.BRASS_KNIFE.get());
                            output.accept(TDItems.THAUMIUM_KNIFE.get());

                            ItemStack elementalKnife = new ItemStack(TDItems.ELEMENTAL_KNIFE.get());
                            InfusionEnchantmentHelper.add(elementalKnife, TDInfusionEnchantments.BLEEDING_EDGE, 1);
                            output.accept(elementalKnife);

                            output.accept(TDItems.VOID_KNIFE.get());

                            output.accept(TDItems.KETCHUP_CAP.get());
                            output.accept(TDItems.FRENCH_FRY_ROD.get());
                            output.accept(chargedWand(TDWandParts.KETCHUP_CAP.get(), TDWandParts.FRENCH_FRY_ROD.get()));

                            output.accept(TDItems.ARCANE_COOKING_POT.get());
                            output.accept(TDItems.ARCANE_STOVE.get());
                            output.accept(TDItems.AURA_RICH_SOIL.get());
                            output.accept(TDItems.AURA_RICH_SOIL_FARMLAND.get());

                            output.accept(TDItems.ETHEREAL_PETAL.get());
                            output.accept(TDItems.SEXTUPLE_MEAT_SURPRISE.get());
                            output.accept(TDItems.DROWNEDS_TEA.get());
//                            output.accept(TDItems.DEATH_DAY_CAKE.get());
//                            output.accept(TDItems.DEATH_DAY_CAKE_SLICE.get());
                            output.accept(TDItems.TAINTED_MILK_BUCKET.get());
                            output.accept(TDItems.PURIFY_COOKIE.get());
                            output.accept(TDItems.CUTTING_SEAL.get());
                            output.accept(TDItems.ADVANCED_CUTTING_SEAL.get());
                            output.accept(TDItems.CHUNKS_FRIED_RICE);
                        }).build());
        ASPECT_FOODS = CREATIVE_MODE_TABS.register("aspect_foods",
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("itemGroup.thaumaturgesdelight.aspect_foods"))
                        .icon(() -> ThaumaturgeUtils.aspectItem(
                                Objects.requireNonNull(Minecraft.getInstance().level).registryAccess()
                                        .lookupOrThrow(IAspect.REGISTRY_KEY)
                                        .getOrThrow(TDAspects.CUPPEDIA),
                                TDItems.MANA_BEAN_DOUGH).create())
                        .withTabsBefore(ThaumaturgesDelight.identifier("thaumaturgesdelight"))
                        .displayItems((parameters, output) -> {
                            HolderLookup.RegistryLookup<IAspect> aspectRegistry = parameters.holders().lookupOrThrow(IAspect.REGISTRY_KEY);
                            for (Holder<IAspect> aspect : aspectRegistry
                                    .listElements()
                                    .sorted(Comparator.comparing((h) -> !h.value().isPrimal()))
                                    .toList()) {
                                output.accept(ThaumaturgeUtils.manaBean(aspect).create());
                                output.accept(ThaumaturgeUtils.aspectItem(aspect, TDItems.MANA_BEAN_DOUGH).create());
                                output.accept(ThaumaturgeUtils.aspectItem(aspect, TDItems.MANA_BREAD).create());
                                output.accept(ThaumaturgeUtils.aspectItem(aspect, TDItems.ESSENTIA_ROCK_CANDY).create());
                            }
                        }).build());
    }

    private static ItemStack chargedWand(WandCap cap, WandRod rod) {
        ItemStack stack = ItemWand.create(TTItems.WAND.get(), cap, rod, false);
        WandVisHelper.fill(stack);
        return stack;
    }
}
