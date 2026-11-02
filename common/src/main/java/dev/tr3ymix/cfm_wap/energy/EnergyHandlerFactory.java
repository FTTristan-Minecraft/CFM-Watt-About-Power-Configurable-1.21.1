package dev.tr3ymix.cfm_wap.energy;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class EnergyHandlerFactory {

    @ExpectPlatform
    public static Object from(CommonEnergyStorage storage) {
        throw new AssertionError();
    }
}
