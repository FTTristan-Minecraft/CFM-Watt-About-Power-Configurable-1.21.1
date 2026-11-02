package dev.tr3ymix.cfm_wap.blockentity;

import com.mrcrayfish.furniture.refurbished.blockentity.IProcessingBlock;

public interface IProcessingCircuitBreakerBlock extends IProcessingBlock {

    int getMaxEnergy();

    /**
     * @return The whole amount of energy to consume this tick. Fractional draw rates are accumulated by the
     * implementation and paid out in whole units over time.
     */
    int takeEnergyDraw();

    void removeEnergy(int var);

    @Override
    default boolean processTick() {
        boolean processing = false;
        if (this.canProcess()) {
            if (this.requiresEnergy() && this.getEnergy() < this.getMaxEnergy() && this.retrieveEnergy(true) > 0) {
                this.addEnergy(retrieveEnergy(false));
            }

            if (!this.requiresEnergy() || this.getEnergy() > 0) {
                processing = true;
                int totalProcessingTime = this.updateAndGetTotalProcessingTime();
                int processingTime = this.getProcessingTime();
                if (processingTime < totalProcessingTime) {
                    this.setProcessingTime(processingTime + 1);
                    if (this.requiresEnergy() && this.getEnergyMode() == EnergyMode.ONLY_WHEN_PROCESSING) {
                        this.removeEnergy(this.takeEnergyDraw());
                    }
                }

                if (processingTime >= totalProcessingTime) {
                    this.onCompleteProcess();
                    this.setProcessingTime(0);
                }
            }
        }
        if (this.requiresEnergy() && this.getEnergyMode() == EnergyMode.ALWAYS_CONSUME && this.getEnergy() > 0) {
            this.removeEnergy(this.takeEnergyDraw());
        }

        if (!processing) {
            this.setProcessingTime(0);
        }

        return processing;
    }
}
