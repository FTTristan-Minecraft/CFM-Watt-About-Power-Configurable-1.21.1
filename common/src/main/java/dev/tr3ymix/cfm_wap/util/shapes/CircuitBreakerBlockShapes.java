package dev.tr3ymix.cfm_wap.util.shapes;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CircuitBreakerBlockShapes {

    private CircuitBreakerBlockShapes() {}

    private static final VoxelShape[] BASE = new VoxelShape[] {
            Block.box(0, 0, 0, 16, 16, 5),//north
            Block.box(0, 0, 11, 16, 16, 16),//east
            Block.box(11, 0, 0, 16, 16, 16),//south
            Block.box(0, 0, 0, 5, 16, 16),//west
    };

    public static VoxelShape getShape(Direction direction) {
        return switch(direction){
            case NORTH -> BASE[0];
            case SOUTH -> BASE[1];
            case EAST -> BASE[2];
            case WEST -> BASE[3];
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        };
    }
}
