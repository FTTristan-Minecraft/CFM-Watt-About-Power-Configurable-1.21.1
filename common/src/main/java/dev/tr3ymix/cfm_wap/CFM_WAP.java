package dev.tr3ymix.cfm_wap;

import com.mrcrayfish.furniture.refurbished.core.ModCreativeTabs;
import dev.architectury.platform.Platform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.utils.Env;
import dev.tr3ymix.cfm_wap.registry.ModBlockEntities;
import dev.tr3ymix.cfm_wap.registry.ModBlocks;
import dev.tr3ymix.cfm_wap.registry.ModMenuTypes;

public final class CFM_WAP {
    public static final String MOD_ID = "cfm_wap";

    public static void init() {
        ModBlocks.register();
        ModMenuTypes.register();
        ModBlockEntities.register();
        addCreativeModeItems();
    }

    public static void addCreativeModeItems(){

        //comment for running neoforge and uncomment for running fabric *for now



        if(Platform.getEnvironment() == Env.CLIENT && Platform.isFabric()) {
            //noinspection UnstableApiUsage
            CreativeTabRegistry.modify(
                    CreativeTabRegistry.defer(ModCreativeTabs.MAIN.getId()),
                    (featureFlagSet, creativeTabOutput, b) -> {
                        ModBlocks.LIGHT_CIRCUIT_BREAKER.ifPresent(block -> creativeTabOutput.accept(block.asItem()));
                        ModBlocks.DARK_CIRCUIT_BREAKER.ifPresent(block -> creativeTabOutput.accept(block.asItem()));
                    });
        }

         





    }
}
