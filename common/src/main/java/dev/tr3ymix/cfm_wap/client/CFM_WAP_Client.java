package dev.tr3ymix.cfm_wap.client;

import com.mrcrayfish.furniture.refurbished.client.registration.BlockEntityRendererRegister;
import com.mrcrayfish.furniture.refurbished.client.registration.ScreenRegister;
import dev.tr3ymix.cfm_wap.client.gui.screen.CircuitBreakerScreen;
import dev.tr3ymix.cfm_wap.client.renderer.CircuitBreakerBlockEntityRenderer;
import dev.tr3ymix.cfm_wap.registry.ModBlockEntities;
import dev.tr3ymix.cfm_wap.registry.ModMenuTypes;

public class CFM_WAP_Client {

    public static void registerScreens(ScreenRegister register) {
        register.apply(ModMenuTypes.CIRCUIT_BREAKER.get(), CircuitBreakerScreen::new);
    }

    public static void registerBlockEntityRenderers(BlockEntityRendererRegister register) {
        register.apply(ModBlockEntities.CIRCUIT_BREAKER.get(), CircuitBreakerBlockEntityRenderer::new);
    }
}
