package com.blackmoss.thaumaturgesdelight.data;

import com.blackmoss.lazycat.datamap.LazyCatDataMapProvider;
import com.blackmoss.lazycat.datamap.thaumaturge.BaseAspectBuilder;
import com.blackmoss.lazycat.thaumaturgesdelight.efficacy.EfficacyDataMapBuilder;
import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.registry.TDAspects;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import com.leclowndu93150.thaumaturge.registry.TTMobEffects;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import vectorwing.farmersdelight.common.registry.ModEffects;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.concurrent.CompletableFuture;

public final class ModDataMapProvider extends LazyCatDataMapProvider {
    public ModDataMapProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, ThaumaturgesDelight.MODID);
    }

    private static Identifier efficacyId(String path) {
        return ThaumaturgesDelight.identifier("efficacy/" + path);
    }

    @Override
    protected void buildDataMaps() {
        new BaseAspectBuilder(context, Items.BREAD)
                .aspect(TDAspects.CUPPEDIA, 1)
                .save();
        new BaseAspectBuilder(context, Tags.Items.FOODS_COOKED_MEAT)
                .aspect(TDAspects.CUPPEDIA, 1)
                .save();
        new BaseAspectBuilder(context, ModTags.Items.SWEETS)
                .aspect(TDAspects.CUPPEDIA, 2)
                .save();
        new BaseAspectBuilder(context, ModItems.CANVAS.get())
                .aspect(TTAspects.HERBA, 2)
                .save();
        new BaseAspectBuilder(context, CommonTags.Items.FOODS_CABBAGE)
                .aspect(TTAspects.HERBA)
                .aspect(TTAspects.VICTUS)
                .save();
        new BaseAspectBuilder(context, CommonTags.Items.FOODS_TOMATO)
                .aspect(TTAspects.HERBA)
                .aspect(TTAspects.VICTUS)
                .save();
        new BaseAspectBuilder(context, CommonTags.Items.FOODS_ONION)
                .aspect(TTAspects.HERBA)
                .aspect(TTAspects.VICTUS)
                .aspect(TTAspects.ALKIMIA, 1)
                .save();
        new BaseAspectBuilder(context, ModItems.ROPE.get())
                .aspect(TTAspects.INSTRUMENTUM, 1)
                .save();
        new BaseAspectBuilder(context, CommonTags.Items.CROPS_RICE)
                .aspect(TTAspects.HERBA)
                .aspect(TTAspects.VICTUS, 1)
                .save();
        new BaseAspectBuilder(context, CommonTags.Items.CROPS_CABBAGE)
                .aspect(TTAspects.HERBA)
                .aspect(TTAspects.VICTUS, 1)
                .save();
        new BaseAspectBuilder(context, ModItems.RICE_PANICLE.get())
                .aspect(TTAspects.HERBA)
                .aspect(TTAspects.VICTUS, 1)
                .save();
        new BaseAspectBuilder(context, ModItems.RICH_SOIL.get())
                .aspect(TTAspects.TERRA)
                .aspect(TTAspects.VICTUS)
                .save();
        new BaseAspectBuilder(context, ModItems.ROTTEN_TOMATO.get())
                .aspect(TTAspects.HERBA)
                .aspect(TTAspects.MORTUUS)
                .save();
        new BaseAspectBuilder(context, ModItems.HAM.get())
                .aspect(TTAspects.BESTIA, 12)
                .aspect(TTAspects.AVERSIO)
                .aspect(TTAspects.IGNIS, 3)
                .save();

        aspectEfficacy();
    }

    private void aspectEfficacy() {
        new EfficacyDataMapBuilder(context, TTAspects.AER)
                .effects(MobEffects.SPEED)
                .particle(Identifier.withDefaultNamespace("cloud"), 6, 0.02, 0.4)
                .save();
        new EfficacyDataMapBuilder(context, TTAspects.AQUA)
                .effects(MobEffects.WATER_BREATHING)
                .particle(Identifier.withDefaultNamespace("bubble"), 6, 0.02, 0.4)
                .save();
        new EfficacyDataMapBuilder(context, TTAspects.IGNIS)
                .ignite(6)
                .particle(Identifier.withDefaultNamespace("flame"), 15, 0.02, 0.4)
                .save();
        new EfficacyDataMapBuilder(context, TTAspects.ORDO)
                .effects(TTMobEffects.WARP_WARD)
                .save();
        new EfficacyDataMapBuilder(context, TTAspects.PERDITIO)
                .effects(MobEffects.HASTE)
                .save();
        new EfficacyDataMapBuilder(context, TTAspects.TERRA)
                .attribute(Attributes.ARMOR_TOUGHNESS, efficacyId("armor_toughness"), 2.0)
                .save();
        new EfficacyDataMapBuilder(context, TTAspects.VITREUS)
                .restoreVis(100)
                .attribute(Attributes.ARMOR, efficacyId("brittle_armor"), -2.0)
                .attribute(Attributes.ATTACK_DAMAGE, efficacyId("brittle_damage"), 2.0)
                .save();
        new EfficacyDataMapBuilder(context, TTAspects.VICTUS)
                .effects(MobEffects.REGENERATION, 200, 0)
                .effects(MobEffects.ABSORPTION, 30, 1)
                .save();
        new EfficacyDataMapBuilder(context, TTAspects.MOTUS)
                .effects(MobEffects.SPEED, 400, 0)
                .particle(Identifier.withDefaultNamespace("crit"), 8, 0.03, 0.5)
                .save();
        new EfficacyDataMapBuilder(context, TTAspects.COGNITIO)
                .grantAspect(1)
                .save();
        new EfficacyDataMapBuilder(context, TTAspects.LUX)
                .effects(MobEffects.GLOWING)
                .save();
        new EfficacyDataMapBuilder(context, TTAspects.SENSUS)
                .effects(MobEffects.NIGHT_VISION)
                .save();
        new EfficacyDataMapBuilder(context, TTAspects.VINCULUM)
                .effects(MobEffects.SLOWNESS)
                .save();
        new EfficacyDataMapBuilder(context, TTAspects.VOLATUS)
                .effects(MobEffects.SLOW_FALLING)
                .save();
        new EfficacyDataMapBuilder(context, TTAspects.AVERSIO)
                .attribute(Attributes.ATTACK_DAMAGE, efficacyId("attack_damage"), 2.0)
                .save();
        new EfficacyDataMapBuilder(context, TTAspects.METALLUM)
                .attribute(Attributes.ARMOR_TOUGHNESS, efficacyId("armor_toughness"), 2.0)
                .save();
        new EfficacyDataMapBuilder(context, TTAspects.PRAECANTATIO)
                .attribute(Attributes.ARMOR, efficacyId("armor"), 2.0)
                .save();
        new EfficacyDataMapBuilder(context, TDAspects.CUPPEDIA)
                .effects(ModEffects.COMFORT)
                .save();
    }
}
