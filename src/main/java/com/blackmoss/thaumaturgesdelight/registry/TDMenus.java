package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.menu.ArcaneCookingPotMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TDMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, ThaumaturgesDelight.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<ArcaneCookingPotMenu>> ARCANE_COOKING_POT = MENUS.register("arcane_cooking_pot",
            () -> IMenuTypeExtension.create(ArcaneCookingPotMenu::new));

    private TDMenus() {
    }
}
