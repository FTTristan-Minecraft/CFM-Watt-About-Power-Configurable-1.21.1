package dev.tr3ymix.cfm_wap.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.tr3ymix.cfm_wap.CFM_WAP;
import dev.tr3ymix.cfm_wap.blockentity.CircuitBreakerBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(CFM_WAP.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<CircuitBreakerBlockEntity>> CIRCUIT_BREAKER =
            BLOCK_ENTITY_TYPES.register("circuit_breaker",
                    () -> BlockEntityType.Builder.of(CircuitBreakerBlockEntity::new,
                                    ModBlocks.LIGHT_CIRCUIT_BREAKER.get(),
                                    ModBlocks.DARK_CIRCUIT_BREAKER.get()).build(null));



    public static void register() {
        BLOCK_ENTITY_TYPES.register();
    }
}
