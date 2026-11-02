package dev.tr3ymix.cfm_wap.client.renderer;

import com.mrcrayfish.furniture.refurbished.client.renderer.blockentity.ElectricBlockEntityRenderer;
import dev.tr3ymix.cfm_wap.blockentity.CircuitBreakerBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;


public class CircuitBreakerBlockEntityRenderer extends ElectricBlockEntityRenderer<CircuitBreakerBlockEntity> {

    public CircuitBreakerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
}
