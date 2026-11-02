package dev.tr3ymix.cfm_wap.energy.neoforge;

import dev.tr3ymix.cfm_wap.energy.CommonEnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class EnergyHandlerFactoryImpl {

    public static Object from(CommonEnergyStorage storage) {
        return new IEnergyStorage() {

            @Override
            public int receiveEnergy(int i, boolean b) {
                return storage.insertEnergy(i, b);
            }

            @Override
            public int extractEnergy(int i, boolean b) {
                return storage.extractEnergy(i, b);
            }

            @Override
            public int getEnergyStored() {
                return storage.getEnergy();
            }

            @Override
            public int getMaxEnergyStored() {
                return storage.getCapacity();
            }

            @Override
            public boolean canExtract() {
                return true;
            }

            @Override
            public boolean canReceive() {
                return true;
            }
        };
    }
}
