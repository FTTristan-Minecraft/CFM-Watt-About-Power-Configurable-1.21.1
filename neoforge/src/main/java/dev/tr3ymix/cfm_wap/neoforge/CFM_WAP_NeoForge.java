package dev.tr3ymix.cfm_wap.neoforge;

import com.mrcrayfish.furniture.refurbished.core.ModCreativeTabs;
import dev.tr3ymix.cfm_wap.CFM_WAP;
import dev.tr3ymix.cfm_wap.block.CircuitBreakerBlock;
import dev.tr3ymix.cfm_wap.client.gui.screen.CircuitBreakerScreen;
import dev.tr3ymix.cfm_wap.client.renderer.CircuitBreakerBlockEntityRenderer;
import dev.tr3ymix.cfm_wap.registry.ModBlockEntities;
import dev.tr3ymix.cfm_wap.registry.ModBlocks;
import dev.tr3ymix.cfm_wap.registry.ModMenuTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(CFM_WAP.MOD_ID)
public class CFM_WAP_NeoForge {

    public CFM_WAP_NeoForge(IEventBus modEventBus) {


        CFM_WAP.init();

        modEventBus.addListener(this::addCreativeModeItems);
        modEventBus.addListener(this::onRegisterCapabilities);
    }




    private void onRegisterCapabilities(RegisterCapabilitiesEvent event){
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntities.CIRCUIT_BREAKER.get(),
                (entity, context) -> {
                    BlockState state = entity.getBlockState();
                    if(state.hasProperty(CircuitBreakerBlock.DIRECTION)){
                        return context == state.getValue(CircuitBreakerBlock.DIRECTION) ? (IEnergyStorage) entity.ENERGY_STORAGE.asPlatformHandler() : null;
                    }
                    return null;
                });
    }

    public void addCreativeModeItems(BuildCreativeModeTabContentsEvent event){
        if(event.getTab().equals(ModCreativeTabs.MAIN.get())){
            event.accept(ModBlocks.LIGHT_CIRCUIT_BREAKER.get());
            event.accept(ModBlocks.DARK_CIRCUIT_BREAKER.get());
        }
    }

    @SuppressWarnings("removal")
    @EventBusSubscriber(modid = CFM_WAP.MOD_ID, bus = EventBusSubscriber.Bus.MOD ,value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event){
            event.enqueueWork(() ->{
                BlockEntityRenderers.register(
                        ModBlockEntities.CIRCUIT_BREAKER.get(),
                        CircuitBreakerBlockEntityRenderer::new
                );
            });
        }

        @SubscribeEvent
        public static void registerMenuScreens(RegisterMenuScreensEvent event){
            event.register(ModMenuTypes.CIRCUIT_BREAKER.get(), CircuitBreakerScreen::new);
        }
    }
}
