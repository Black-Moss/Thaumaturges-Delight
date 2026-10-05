package com.blackmoss.thaumaturgesdelight.data;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.registry.TDInfusionEnchantments;
import com.blackmoss.thaumaturgesdelight.registry.TDItems;
import com.leclowndu93150.thaumaturge.TCIds;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TCAspects;
import com.leclowndu93150.thaumaturge.api.recipe.ResearchGate;
import com.leclowndu93150.thaumaturge.content.equipment.InfusionEnchantments;
import com.leclowndu93150.thaumaturge.data.recipe.builders.CrucibleRecipeBuilder;
import com.leclowndu93150.thaumaturge.data.recipe.builders.InfusionEnchantmentRecipeBuilder;
import com.leclowndu93150.thaumaturge.data.recipe.builders.InfusionRecipeBuilder;
import com.leclowndu93150.thaumaturge.registry.TCDataComponents;
import com.leclowndu93150.thaumaturge.registry.TCItemTags;
import com.leclowndu93150.thaumaturge.registry.TCItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.NonNull;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public final class ModRecipeProvider extends RecipeProvider {
    private final HolderLookup.Provider lookupProvider;

    private ModRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
        this.lookupProvider = provider;
    }

    @Override
    protected void buildRecipes() {
        knife(TDItems.BRASS_KNIFE, TCItems.INGOT_BRASS, TCItems.NUGGET_BRASS);
        knife(TDItems.THAUMIUM_KNIFE, TCItems.INGOT_THAUMIUM, TCItems.NUGGET_THAUMIUM);

        infusion(new ItemStackTemplate(TDItems.ELEMENTAL_KNIFE.get(), DataComponentPatch.builder().set(
                                TCDataComponents.INFUSION_ENCHANTMENTS.get(),
                                new InfusionEnchantments(Map.of(
                                        TDInfusionEnchantments.COOK_DING, 1)))
                        .build()),
                RecipeCategory.TOOLS, TDItems.THAUMIUM_KNIFE)
                .component(Ingredient.of(TCItems.CRYSTAL_IGNIS.get()))
                .component(Ingredient.of(TCItems.CRYSTAL_IGNIS.get()))
                .component(tag(TCItemTags.NUGGETS_QUARTZ))
                .component(tag(TCItemTags.PLANKS_GREATWOOD))
                .aspect(TCAspects.IGNIS, 20)
                .aspect(TCAspects.METALLUM, 15)
                .aspect(TCAspects.SENSUS, 20)
                .instability(1)
                .gate(ttGate("elemental_tools"))
                .unlockedBy("has", has(TDItems.THAUMIUM_KNIFE))
                .save(output);

        infusionEnchantment(ModItems.FLINT_KNIFE.get(), Ingredient.of(TCItems.TRIPLE_MEAT_TREAT.get()))
                .aspect(TCAspects.IGNIS, 55)
                .aspect(TCAspects.PERMUTATIO, 60)
                .save(output);

        essenceRockCandy();
    }

    private void essenceRockCandy() {
        HolderLookup<IAspect> aspects = this.registries.lookupOrThrow(IAspect.REGISTRY_KEY);
        for (Holder<IAspect> aspect : aspects.listElements().toList()) {
            ResourceKey<IAspect> key = aspect.unwrapKey().orElseThrow();
            DataComponentPatch patch = DataComponentPatch.builder()
                    .set(TCDataComponents.CRYSTAL_ASPECT.get(), new AspectInstance(aspect, 1))
                    .build();
            new CrucibleRecipeBuilder(aspects, RecipeCategory.MISC,
                    new ItemStackTemplate(TDItems.ESSENTIA_ROCK_CANDY.get(), patch),
                    Ingredient.of(TCItems.ESSENTIA_CRYSTAL.get()))
                    .aspect(aspect, 5)
                    .aspect(TCAspects.VITREUS, 5)
                    .aspect(TCAspects.GELUM, 1)
                    .unlockedBy("has", has(TCItems.ESSENTIA_CRYSTAL.get()))
                    .save(output, ResourceKey.create(Registries.RECIPE,
                            ThaumaturgesDelight.identifier("crucible/essentia_rock_candy/" + key.identifier().getPath())));
        }
    }

    public void knife(ItemLike knife, ItemLike material, ItemLike materialNugget) {
        shaped(RecipeCategory.TOOLS, knife)
                .pattern("M")
                .pattern("S")
                .define('M', material)
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has", has(knife))
                .save(output);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(knife), RecipeCategory.MISC, CookingBookCategory.MISC, materialNugget, 0.1F, 200);
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(knife), RecipeCategory.MISC, CookingBookCategory.MISC, materialNugget, 0.1F, 100);
    }

    private InfusionRecipeBuilder infusion(ItemLike result, RecipeCategory category, ItemLike catalyst) {
        return infusion(new ItemStackTemplate(result.asItem()), category, catalyst);
    }

    private InfusionRecipeBuilder infusion(ItemStackTemplate result, RecipeCategory category, ItemLike catalyst) {
        return new InfusionRecipeBuilder(this.registries.lookupOrThrow(IAspect.REGISTRY_KEY), category, result, Ingredient.of(catalyst));
    }

    private InfusionEnchantmentRecipeBuilder infusionEnchantment(Item displayCatalyst, Ingredient signature) {
        return new InfusionEnchantmentRecipeBuilder(this.registries.lookupOrThrow(IAspect.REGISTRY_KEY), TDInfusionEnchantments.COOK_DING, Ingredient.of(displayCatalyst))
                .component(Ingredient.of(Items.ENCHANTED_BOOK))
                .component(signature)
                .gate(ttGate("infusion_enchantment"));
    }

    private static ResearchGate tdGate(String path) {
        return new ResearchGate(ThaumaturgesDelight.identifier(path), Optional.empty(), false);
    }

    private static ResearchGate ttGate(String path) {
        return new ResearchGate(TCIds.rl(path), Optional.empty(), false);
    }

    private HolderSet<Item> tag(String space, String path) {
        return this.lookupProvider.getOrThrow(TagKey.create(
                Registries.ITEM, Identifier.fromNamespaceAndPath(
                        space, path)));
    }

    public static final class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider provider, @NonNull RecipeOutput output) {
            return new ModRecipeProvider(provider, output);
        }

        @Override
        public @NonNull String getName() {
            return "Thaumaturge's Delight Recipes";
        }
    }
}
