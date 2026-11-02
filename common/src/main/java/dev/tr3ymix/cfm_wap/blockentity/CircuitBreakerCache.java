package dev.tr3ymix.cfm_wap.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.*;

public class CircuitBreakerCache {

    private static final Map<Level, Set<BlockPos>> CIRCUIT_BREAKER_BLOCKS = new HashMap<>();


    public static void add(Level level, BlockPos pos){
        CIRCUIT_BREAKER_BLOCKS.computeIfAbsent(level, k -> new HashSet<>()).add(pos);
    }

    public static void remove(Level level, BlockPos pos){
        Set<BlockPos> set = CIRCUIT_BREAKER_BLOCKS.get(level);
        if(set != null){
            set.remove(pos);
        }
    }

    public static Set<BlockPos> get(Level level){
        return CIRCUIT_BREAKER_BLOCKS.getOrDefault(level, Collections.emptySet());
    }

}
