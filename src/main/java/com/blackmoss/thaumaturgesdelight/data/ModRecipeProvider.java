package com.blackmoss.thaumaturgesdelight.data;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.minecraft.CookingType;
import com.blackmoss.lazycat.api.recipe.thaumaturge.ThaumaturgeUtils;
import com.blackmoss.lazycat.recipe.LazyCatRecipeProvider;
import com.blackmoss.lazycat.recipe.minecraft.CookingBuilder;
import com.blackmoss.lazycat.recipe.minecraft.ShapedBuilder;
import com.blackmoss.lazycat.recipe.thaumaturge.CrucibleBuilder;
import com.blackmoss.lazycat.recipe.thaumaturge.InfusionBuilder;
import com.blackmoss.lazycat.recipe.thaumaturge.InfusionEnchantmentBuilder;
import com.blackmoss.lazycat.recipe.thaumaturgesdelight.ArcaneCookingPotBuilder;
import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.registry.TDAspects;
import com.blackmoss.thaumaturgesdelight.registry.TDInfusionEnchantments;
import com.blackmoss.thaumaturgesdelight.registry.TDItems;
import com.leclowndu93150.thaumaturge.TTIds;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import com.leclowndu93150.thaumaturge.api.recipe.ResearchGate;
import com.leclowndu93150.thaumaturge.content.equipment.InfusionEnchantments;
import com.leclowndu93150.thaumaturge.registry.TTDataComponents;
import com.leclowndu93150.thaumaturge.registry.TTItemTags;
import com.leclowndu93150.thaumaturge.registry.TTItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.NonNull;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public final class ModRecipeProvider extends LazyCatRecipeProvider {
    private final HolderGetter<IAspect> aspects;

    private ModRecipeProvider(HolderLookup.Provider provider, RecipeOutput output, String space) {
        super(provider, output, space);
        this.aspects = registries.lookupOrThrow(IAspect.REGISTRY_KEY);
    }

    private static ResearchGate tdGate(String path) {
        return new ResearchGate(ThaumaturgesDelight.identifier(path), Optional.empty(), false);
    }

    private static ResearchGate ttGate(String path) {
        return new ResearchGate(TTIds.rl(path), Optional.empty(), false);
    }

    @Override
    protected void addRecipes() {
        essenceRockCandy();
        sealCutting();
        knife(TDItems.BRASS_KNIFE, TTItemTags.INGOTS_BRASS, TTItems.NUGGET_BRASS);
        knife(TDItems.THAUMIUM_KNIFE, TTItemTags.INGOTS_THAUMIUM, TTItems.NUGGET_THAUMIUM);

        new InfusionBuilder(context, LazyIngredient.from(TDItems.ELEMENTAL_KNIFE)
                .components(DataComponentPatch.builder().set(
                                TTDataComponents.INFUSION_ENCHANTMENTS.get(),
                                new InfusionEnchantments(Map.of(
                                        TDInfusionEnchantments.BLEEDING_EDGE, 1)))
                        .build()),
                TDItems.THAUMIUM_KNIFE)
                .component(TTItems.CRYSTAL_IGNIS)
                .component(TTItems.CRYSTAL_IGNIS)
                .component(TTItemTags.NUGGETS_QUARTZ)
                .component(TTItemTags.PLANKS_GREATWOOD)
                .aspect(TTAspects.IGNIS, 20)
                .aspect(TTAspects.METALLUM, 15)
                .aspect(TTAspects.SENSUS, 20)
                .instability(1)
                .gate(tdGate("elemental_knife"))
                .save();

        new InfusionEnchantmentBuilder(context,
                TDInfusionEnchantments.BLEEDING_EDGE,
                TDItems.ELEMENTAL_KNIFE)
                .component(Items.ENCHANTED_BOOK)
                .component(ModItems.FLINT_KNIFE)
                .aspect(TTAspects.IGNIS, 55)
                .aspect(TTAspects.PERMUTATIO, 60)
                .gate(tdGate("elemental_knife"))
                .save();

        new ArcaneCookingPotBuilder(context, new ItemStackTemplate(TDItems.SEXTUPLE_MEAT_SURPRISE.get()))
                .ingredient(TTItems.CHUNK_BEEF)
                .ingredient(TTItems.CHUNK_CHICKEN)
                .ingredient(TTItems.CHUNK_PORK)
                .ingredient(TTItems.CHUNK_FISH)
                .ingredient(TTItems.CHUNK_RABBIT)
                .ingredient(TTItems.CHUNK_MUTTON)
                .aspect(TTAspects.VICTUS, 20)
                .aspect(TTAspects.PERDITIO, 5)
                .category(CookingPotRecipeBookTab.MEALS)
                .unlockedBy("has", has(TTItems.TRIPLE_MEAT_TREAT.get()))
                .save();

        // 溺水者之茶
        new ArcaneCookingPotBuilder(context, new ItemStackTemplate(TDItems.DROWNEDS_TEA.get()))
                .ingredient(ItemTags.LEAVES)
                .ingredient(Items.SUGAR)
                .container(Items.GLASS_BOTTLE)
                .aspect(TTAspects.AQUA, 20)
                .category(CookingPotRecipeBookTab.DRINKS)
                .unlockedBy("has", has(Items.OAK_LEAVES))
                .save();

        // 死日蛋糕
        new ArcaneCookingPotBuilder(context, new ItemStackTemplate(TDItems.DEATH_DAY_CAKE.get()))
                .ingredient(TTItems.TAINT_TENDRIL.get())
                .ingredient(TTItems.BRAIN.get())
                .ingredient(TDItems.TAINTED_EGG.get())
                .ingredient(TDItems.TAINTED_MILK_BUCKET.get())
                .ingredient(Items.DEAD_BUSH)
                .aspect(TTAspects.MORTUUS, 40)
                .category(CookingPotRecipeBookTab.MEALS)
                .unlockedBy("has", has(TTItems.BRAIN.get()))
                .save();

        new InfusionBuilder(context, TDItems.KETCHUP_CAP.get(), ModItems.TOMATO.get())
                .component(Ingredient.of(TTItems.SALIS_MUNDUS.get()))
                .component(crystal(TDAspects.CUPPEDIA))
                .aspect(TDAspects.CUPPEDIA, 5)
                .save();

        new InfusionBuilder(context, TDItems.FRENCH_FRY_ROD.get(), Items.BAKED_POTATO)
                .component(Ingredient.of(TTItems.SALIS_MUNDUS.get()))
                .component(crystal(TDAspects.CUPPEDIA))
                .aspect(TDAspects.CUPPEDIA, 5)
                .save();
    }

    private void essenceRockCandy() {
        HolderLookup<IAspect> lookup = (HolderLookup<IAspect>) aspects;
        for (Holder<IAspect> aspect : lookup.listElements().toList()) {
            ResourceKey<IAspect> key = aspect.unwrapKey().orElseThrow();
            DataComponentPatch patch = DataComponentPatch.builder()
                    .set(TTDataComponents.CRYSTAL_ASPECT.get(), new AspectInstance(aspect, 1))
                    .build();
            new CrucibleBuilder(context,
                    LazyIngredient.from(TDItems.ESSENTIA_ROCK_CANDY).components(patch),
                    Items.SUGAR)
                    .aspect(aspect, 5)
                    .aspect(TTAspects.VITREUS)
                    .aspect(TTAspects.GELUM)
                    .gate(tdGate("essentia_rock_candy"))
                    .id("essentia_rock_candy/" + key.identifier().getPath())
                    .save();
        }
    }

    private void sealCutting() {
        new InfusionBuilder(context, TDItems.CUTTING_SEAL.get(),
                TTItems.SEAL_BLANK.get())
                .component(ModItems.GOLDEN_KNIFE)
                .component(Tags.Items.CROPS)
                .component(Tags.Items.FOODS_RAW_MEAT)
                .aspect(TTAspects.MACHINA, 20)
                .aspect(TTAspects.SENSUS, 20)
                .aspect(TTAspects.HUMANUS, 20)
                .instability(1)
                .gate(tdGate("cutting_seal"))
                .save();

        new InfusionBuilder(context, TDItems.ADVANCED_CUTTING_SEAL,
                TDItems.CUTTING_SEAL)
                .component(Tags.Items.CROPS)
                .component(Tags.Items.FOODS_RAW_MEAT)
                .component(Tags.Items.CROPS)
                .component(Tags.Items.FOODS_RAW_MEAT)
                .aspect(TTAspects.MACHINA, 20)
                .aspect(TTAspects.SENSUS, 20)
                .aspect(TTAspects.HUMANUS, 20)
                .instability(1)
                .gate(tdGate("cutting_seal"))
                .save();
    }

    private void knife(ItemLike knife, Object material, ItemLike materialNugget) {
        new ShapedBuilder(context, knife)
                .pattern("M")
                .pattern("S")
                .define('M', material)
                .define('S', Tags.Items.RODS_WOODEN)
                .category(RecipeCategory.TOOLS)
                .save();

        new CookingBuilder(context, materialNugget, knife, CookingType.SMELTING);
        new CookingBuilder(context, materialNugget, knife, CookingType.BLASTING);
    }

    private Ingredient crystal(ResourceKey<IAspect> aspect) {
        return ThaumaturgeUtils.crystal(registries, aspect);
    }

    public static final class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider provider, @NonNull RecipeOutput output) {
            return new ModRecipeProvider(provider, output, ThaumaturgesDelight.MODID);
        }

        @Override
        public @NonNull String getName() {
            return "Thaumaturge's Delight Recipes";
        }
    }
}
