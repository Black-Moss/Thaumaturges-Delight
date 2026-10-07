package com.blackmoss.thaumaturgesdelight.data;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.recipe.ArcaneCookingPotRecipeBuilder;
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
import com.leclowndu93150.thaumaturge.data.recipe.builders.workbench.ArcaneWorkbenchShapedRecipeBuilder;
import com.leclowndu93150.thaumaturge.data.recipe.builders.workbench.ArcaneWorkbenchShapelessRecipeBuilder;
import com.leclowndu93150.thaumaturge.registry.TCDataComponents;
import com.leclowndu93150.thaumaturge.registry.TCItemTags;
import com.leclowndu93150.thaumaturge.registry.TCItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
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
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public final class ModRecipeProvider extends RecipeProvider {
    private final HolderLookup.Provider lookupProvider;
    private final HolderGetter<IAspect> aspects;

    private ModRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
        this.lookupProvider = provider;
        this.aspects = registries.lookupOrThrow(IAspect.REGISTRY_KEY);
    }

    @Override
    protected void buildRecipes() {
        knife(TDItems.BRASS_KNIFE, TCItems.INGOT_BRASS, TCItems.NUGGET_BRASS);
        knife(TDItems.THAUMIUM_KNIFE, TCItems.INGOT_THAUMIUM, TCItems.NUGGET_THAUMIUM);

        new InfusionRecipeBuilder(
                aspects,
                RecipeCategory.TOOLS,
                new ItemStackTemplate(TDItems.ELEMENTAL_KNIFE.get(), DataComponentPatch.builder().set(
                                TCDataComponents.INFUSION_ENCHANTMENTS.get(),
                                new InfusionEnchantments(Map.of(
                                        TDInfusionEnchantments.BLEEDING_EDGE, 1)))
                        .build()), Ingredient.of(TDItems.THAUMIUM_KNIFE.get()))
                .component(Ingredient.of(TCItems.CRYSTAL_IGNIS.get()))
                .component(Ingredient.of(TCItems.CRYSTAL_IGNIS.get()))
                .component(tag(TCItemTags.NUGGETS_QUARTZ))
                .component(tag(TCItemTags.PLANKS_GREATWOOD))
                .aspect(TCAspects.IGNIS, 20)
                .aspect(TCAspects.METALLUM, 15)
                .aspect(TCAspects.SENSUS, 20)
                .instability(1)
                .gate(tdGate("elemental_knife"))
                .unlockedBy("has", has(TDItems.THAUMIUM_KNIFE))
                .save(output);

        infusionEnchantment(ModItems.FLINT_KNIFE.get(), Ingredient.of(TCItems.TRIPLE_MEAT_TREAT.get()))
                .aspect(TCAspects.IGNIS, 55)
                .aspect(TCAspects.PERMUTATIO, 60)
                .gate(tdGate("elemental_knife"))
                .save(output);

        essenceRockCandy();

        new ArcaneCookingPotRecipeBuilder(aspects,
                RecipeCategory.MISC, new ItemStackTemplate(TDItems.SEXTUPLE_MEAT_TREAT.get()))
                .ingredient(TCItems.CHUNK_BEEF.get())
                .ingredient(TCItems.CHUNK_CHICKEN.get())
                .ingredient(TCItems.CHUNK_PORK.get())
                .ingredient(TCItems.CHUNK_FISH.get())
                .ingredient(TCItems.CHUNK_RABBIT.get())
                .ingredient(TCItems.CHUNK_MUTTON.get())
                .aspect(TCAspects.VICTUS, 20)
                .aspect(TCAspects.PERDITIO, 5)
                .tab(CookingPotRecipeBookTab.MEALS)
                .unlockedBy("has", has(TCItems.TRIPLE_MEAT_TREAT.get()))
                .save(output);

        sealCutting();
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
                    Ingredient.of(Items.SUGAR))
                    .aspect(aspect, 5)
                    .aspect(TCAspects.VITREUS, 1)
                    .aspect(TCAspects.GELUM, 1)
                    .gate(tdGate("essentia_rock_candy"))
                    .unlockedBy("has", has(TCItems.ESSENTIA_CRYSTAL.get()))
                    .save(output, ResourceKey.create(Registries.RECIPE,
                            ThaumaturgesDelight.identifier("crucible/essentia_rock_candy/" + key.identifier().getPath())));
        }
    }

    private void sealCutting() {
        infusion(TDItems.SEAL_CUTTING.get(),
                RecipeCategory.TOOLS, TCItems.SEAL_BLANK.get())
                .component(Ingredient.of(ModItems.GOLDEN_KNIFE.get()))
                .component(tag(Tags.Items.CROPS))
                .component(tag(Tags.Items.FOODS_RAW_MEAT))
                .aspect(TCAspects.MACHINA, 20)
                .aspect(TCAspects.SENSUS, 20)
                .aspect(TCAspects.HUMANUS, 20)
                .instability(1)
                .gate(tdGate("seal_cutting"))
                .save(output);

        infusion(TDItems.SEAL_ADVANCED_CUTTING.get(),
                RecipeCategory.TOOLS, TDItems.SEAL_CUTTING.get())
                .component(tag(Tags.Items.CROPS))
                .component(tag(Tags.Items.FOODS_RAW_MEAT))
                .component(tag(Tags.Items.CROPS))
                .component(tag(Tags.Items.FOODS_RAW_MEAT))
                .aspect(TCAspects.MACHINA, 20)
                .aspect(TCAspects.SENSUS, 20)
                .aspect(TCAspects.HUMANUS, 20)
                .instability(1)
                .gate(tdGate("seal_cutting"))
                .save(output);
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
        return (InfusionRecipeBuilder) new InfusionRecipeBuilder(
                aspects,
                category,
                new ItemStackTemplate(result.asItem()),
                Ingredient.of(catalyst))
                .unlockedBy("has", this.has(result));
    }

    private static ResearchGate tdGate(String path) {
        return new ResearchGate(ThaumaturgesDelight.identifier(path), Optional.empty(), false);
    }

    private static ResearchGate ttGate(String path) {
        return new ResearchGate(TCIds.rl(path), Optional.empty(), false);
    }

    private CrucibleRecipeBuilder crucible(ItemLike result, RecipeCategory category, ItemLike catalyst) {
        return (CrucibleRecipeBuilder) new CrucibleRecipeBuilder(
                aspects,
                category,
                new ItemStackTemplate(result.asItem()),
                Ingredient.of(catalyst))
                .unlockedBy("has", this.has(result));
    }

    private CrucibleRecipeBuilder crucible(ItemLike result, ItemLike catalyst) {
        return crucible(result, RecipeCategory.MISC, catalyst);
    }


    private ArcaneWorkbenchShapelessRecipeBuilder arcaneShapeless(RecipeCategory recipeCategory, ItemStackTemplate result, int vis) {
        return new ArcaneWorkbenchShapelessRecipeBuilder(recipeCategory,
                result,
                aspects,
                vis,
                registries.lookupOrThrow(Registries.ITEM));
    }

    private ArcaneWorkbenchShapelessRecipeBuilder arcaneShapeless(RecipeCategory recipeCategory, ItemLike result, int vis) {
        return arcaneShapeless(recipeCategory, new ItemStackTemplate(result.asItem()), vis);
    }

    private ArcaneWorkbenchShapedRecipeBuilder arcaneShaped(RecipeCategory recipeCategory, ItemStackTemplate result, int vis) {
        return new ArcaneWorkbenchShapedRecipeBuilder(recipeCategory,
                result,
                this.items,
                aspects,
                vis);
    }

    private ArcaneWorkbenchShapedRecipeBuilder arcaneShaped(RecipeCategory recipeCategory, ItemLike result, int vis) {
        return (ArcaneWorkbenchShapedRecipeBuilder) arcaneShaped(recipeCategory, new ItemStackTemplate(result.asItem()), vis)
                .unlockedBy("has", this.has(result.asItem()));
    }

    private InfusionEnchantmentRecipeBuilder infusionEnchantment(Item displayCatalyst, Ingredient signature) {
        return new InfusionEnchantmentRecipeBuilder(aspects, TDInfusionEnchantments.BLEEDING_EDGE, Ingredient.of(displayCatalyst))
                .component(Ingredient.of(Items.ENCHANTED_BOOK))
                .component(signature)
                .gate(ttGate("infusion_enchantment"));
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
