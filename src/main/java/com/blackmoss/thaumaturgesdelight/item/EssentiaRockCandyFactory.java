package com.blackmoss.thaumaturgesdelight.item;

import com.blackmoss.thaumaturgesdelight.registry.TDItems;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.registry.TTDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;

public class EssentiaRockCandyFactory {
    public static ItemStack of(Holder<IAspect> aspect, int count) {
        ItemStack stack = new ItemStack(TDItems.ESSENTIA_ROCK_CANDY.get(), count);
        stack.set(TTDataComponents.CRYSTAL_ASPECT.get(), new AspectInstance(aspect, 1));
        return stack;
    }

    public static ItemStack of(Holder<IAspect> aspect) {
        return of(aspect, 1);
    }

    public static ItemStack of(HolderLookup.Provider registries, ResourceKey<IAspect> key) {
        Holder<IAspect> aspect = registries.lookupOrThrow(IAspect.REGISTRY_KEY).getOrThrow(key);
        return of(aspect, 1);
    }
}
