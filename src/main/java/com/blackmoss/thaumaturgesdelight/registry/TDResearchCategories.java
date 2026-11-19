package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.leclowndu93150.thaumaturge.api.research.IResearchCategory;
import net.minecraft.resources.ResourceKey;

public final class TDResearchCategories {
    public static final ResourceKey<IResearchCategory> CULINARY_MAGIC = ResourceKey.create(
            IResearchCategory.REGISTRY_KEY, ThaumaturgesDelight.identifier("culinary_magic"));
}
