package com.blackmoss.thaumaturgesdelight.data;

import com.blackmoss.lazycat.datamap.LazyCatDataMapProvider;
import com.blackmoss.lazycat.datamap.thaumaturge.BaseAspectBuilder;
import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.efficacy.AspectEffect;
import com.blackmoss.thaumaturgesdelight.efficacy.AspectEfficacies;
import com.blackmoss.thaumaturgesdelight.efficacy.actions.*;
import com.blackmoss.thaumaturgesdelight.registry.TDAspects;
import com.blackmoss.thaumaturgesdelight.registry.TDDataMaps;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import com.leclowndu93150.thaumaturge.registry.TTMobEffects;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
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
        efficacy(TTAspects.AER,
                new MobEffectAction(MobEffects.SPEED, 30, 0),
                new ParticleAction(Identifier.withDefaultNamespace("cloud"), 6, 0.02, 0.4));
        efficacy(TTAspects.AQUA,
                new MobEffectAction(MobEffects.WATER_BREATHING, 30, 0),
                new ParticleAction(Identifier.withDefaultNamespace("bubble"), 6, 0.02, 0.4));
        efficacy(TTAspects.IGNIS,
                new IgniteAction(6),
                new ParticleAction(Identifier.withDefaultNamespace("flame"), 15, 0.02, 0.4));
        efficacy(TTAspects.ORDO,
                new MobEffectAction(TTMobEffects.WARP_WARD, 30, 0));
        efficacy(TTAspects.PERDITIO,
                new MobEffectAction(MobEffects.HASTE, 30, 0));
        efficacy(TTAspects.TERRA,
                new AttributeAction(Attributes.ARMOR_TOUGHNESS, efficacyId("armor_toughness"),
                        2.0, AttributeModifier.Operation.ADD_VALUE, 30));
        efficacy(TTAspects.VITREUS,
                new RestoreVisAction(100),
                new AttributeAction(Attributes.ARMOR, efficacyId("brittle_armor"),
                        -2.0, AttributeModifier.Operation.ADD_VALUE, 30),
                new AttributeAction(Attributes.ATTACK_DAMAGE, efficacyId("brittle_damage"),
                        2.0, AttributeModifier.Operation.ADD_VALUE, 30));
        efficacy(TTAspects.VICTUS,
                new MobEffectAction(MobEffects.REGENERATION, 200, 0),
                new MobEffectAction(MobEffects.ABSORPTION, 30, 1));
        efficacy(TTAspects.MOTUS,
                new MobEffectAction(MobEffects.SPEED, 400, 0),
                new ParticleAction(Identifier.withDefaultNamespace("crit"), 8, 0.03, 0.5));
        efficacy(TTAspects.COGNITIO,
                new GrantAspectAction(1));
        efficacy(TTAspects.LUX,
                new MobEffectAction(MobEffects.GLOWING, 30, 0));
        efficacy(TTAspects.SENSUS,
                new MobEffectAction(MobEffects.NIGHT_VISION, 30, 0));
        efficacy(TTAspects.VINCULUM,
                new MobEffectAction(MobEffects.SLOWNESS, 30, 0));
        efficacy(TTAspects.VOLATUS,
                new MobEffectAction(MobEffects.SLOW_FALLING, 30, 0));
        efficacy(TTAspects.AVERSIO,
                new AttributeAction(Attributes.ATTACK_DAMAGE, efficacyId("attack_damage"),
                        2.0, AttributeModifier.Operation.ADD_VALUE, 30));
        efficacy(TTAspects.METALLUM,
                new AttributeAction(Attributes.ARMOR_TOUGHNESS, efficacyId("armor_toughness"),
                        2.0, AttributeModifier.Operation.ADD_VALUE, 30));
        efficacy(TTAspects.PRAECANTATIO,
                new AttributeAction(Attributes.ARMOR, efficacyId("armor"),
                        2.0, AttributeModifier.Operation.ADD_VALUE, 30));
    }

    private void efficacy(
            ResourceKey<IAspect> aspect,
            AspectEffect... entries) {
        context.builder(TDDataMaps.ASPECT_EFFICACY)
                .add(aspect, new AspectEfficacies(java.util.List.of(entries)), false);
    }
}
