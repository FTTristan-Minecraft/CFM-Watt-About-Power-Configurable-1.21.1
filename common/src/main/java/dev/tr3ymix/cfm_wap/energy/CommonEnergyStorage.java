package dev.tr3ymix.cfm_wap.energy;

public class CommonEnergyStorage {

    private int energy;

    private final int capacity;
    private final int maxTransfer;

    public CommonEnergyStorage(int capacity, int maxTransfer){
        this.capacity = capacity;
        this.energy = 0;
        this.maxTransfer = maxTransfer;
    }

    public int getEnergy(){
        return energy;
    }

    public int getCapacity(){
        return capacity;
    }
    public int getMaxTransfer(){
        return maxTransfer;
    }

    public void setEnergy(int energy){
        this.energy = Math.min(energy, capacity);
    }

    public int insertEnergy(int amount, boolean simulate){
        int space = capacity - energy;
        int inserted = Math.min(amount, space);
        if(!simulate) energy += inserted;
        return inserted;
    }

    public int extractEnergy(int amount, boolean simulate){
        int extracted = Math.min(amount, energy);
        if(!simulate) energy -= extracted;
        return extracted;
    }

    public Object asPlatformHandler(){
        return EnergyHandlerFactory.from(this);
    }
}
