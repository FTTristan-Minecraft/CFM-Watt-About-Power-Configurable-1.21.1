package dev.tr3ymix.cfm_wap.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.tr3ymix.cfm_wap.CFM_WAP;
import dev.tr3ymix.cfm_wap.inventory.CircuitBreakerMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class ModMenuTypes {



    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(CFM_WAP.MOD_ID, Registries.MENU);

    public static final RegistrySupplier<MenuType<CircuitBreakerMenu>> CIRCUIT_BREAKER =
            registerMenuType(CircuitBreakerMenu::new, "circuit_breaker_menu");

    private static <T extends AbstractContainerMenu> RegistrySupplier<MenuType<T>> registerMenuType
            (MenuType.MenuSupplier<T> supplier, @SuppressWarnings("SameParameterValue") String name){
        return MENU_TYPES.register(name, () -> new MenuType<>(supplier, FeatureFlagSet.of()));
    }

    public static void register() {
        MENU_TYPES.register();
    }
}
