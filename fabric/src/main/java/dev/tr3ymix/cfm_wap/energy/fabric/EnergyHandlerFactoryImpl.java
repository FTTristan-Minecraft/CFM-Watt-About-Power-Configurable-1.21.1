package dev.tr3ymix.cfm_wap.energy.fabric;

import dev.tr3ymix.cfm_wap.energy.CommonEnergyStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import team.reborn.energy.api.base.SimpleEnergyStorage;

@SuppressWarnings("unused")
public class EnergyHandlerFactoryImpl {

    public static Object from(CommonEnergyStorage storage) {
        return new SimpleEnergyStorage(storage.getCapacity(), storage.getMaxTransfer(), storage.getMaxTransfer()){
            @Override
            protected void onFinalCommit() {
                storage.setEnergy((int)this.amount);
            }

            @Override
            public long insert(long maxAmount, TransactionContext transaction) {
                return storage.insertEnergy((int) maxAmount, false);
            }

            @Override
            public long extract(long maxAmount, TransactionContext transaction) {
                return storage.extractEnergy((int) maxAmount, false);
            }
        };
    }
}
