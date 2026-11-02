package dev.tr3ymix.cfm_wap.energy;

/**
 * Turns a draw rate that can have a fraction (for example 0.1 energy per tick) into whole energy units.
 * The rate is tracked in thousandths, so no rounding error accumulates over time.
 */
public class FractionalEnergyDraw {
    private static final long UNIT = 1000L;

    private long remainder;

    /**
     * @param perTick Energy to consume per tick
     * @return The whole amount of energy to consume this tick
     */
    public int next(double perTick) {
        this.remainder += Math.max(0L, Math.round(perTick * UNIT));
        long whole = this.remainder / UNIT;
        this.remainder -= whole * UNIT;
        return (int) Math.min(whole, Integer.MAX_VALUE);
    }
}
