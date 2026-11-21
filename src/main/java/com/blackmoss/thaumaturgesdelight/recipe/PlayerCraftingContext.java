package com.blackmoss.thaumaturgesdelight.recipe;

import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

public final class PlayerCraftingContext {
    private static final ThreadLocal<Player> CURRENT = new ThreadLocal<>();

    private PlayerCraftingContext() {
    }

    public static void set(Player player) {
        CURRENT.set(player);
    }

    public static @Nullable Player get() {
        return CURRENT.get();
    }

    public static void clear() {
        CURRENT.remove();
    }
}
