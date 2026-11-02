package dev.tr3ymix.cfm_wap;

import com.mrcrayfish.framework.FrameworkSetup;
import com.mrcrayfish.framework.api.registry.RegistryEntry;
import com.mrcrayfish.furniture.refurbished.core.ModCreativeTabs;
import dev.tr3ymix.cfm_wap.block.CircuitBreakerBlock;
import dev.tr3ymix.cfm_wap.registry.ModBlockEntities;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.state.BlockState;
import team.reborn.energy.api.EnergyStorage;

public final class CFM_WAP_Fabric implements ModInitializer {

    @Override
    public void onInitialize() {

        FrameworkSetup.run();

        CFM_WAP.init();



        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> {
                    BlockState state = blockEntity.getBlockState();
                    if(state.hasProperty(CircuitBreakerBlock.DIRECTION)){
                        return direction == state.getValue(CircuitBreakerBlock.DIRECTION) ? (EnergyStorage) blockEntity.ENERGY_STORAGE.asPlatformHandler() : null;
                    }
                    return null;
                }, ModBlockEntities.CIRCUIT_BREAKER.get());
    }
}
