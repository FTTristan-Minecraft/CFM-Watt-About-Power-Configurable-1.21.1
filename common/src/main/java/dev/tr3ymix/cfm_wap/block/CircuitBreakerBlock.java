package dev.tr3ymix.cfm_wap.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import com.mrcrayfish.furniture.refurbished.block.FurnitureHorizontalEntityBlock;
import com.mrcrayfish.furniture.refurbished.block.MetalType;
import com.mrcrayfish.furniture.refurbished.data.tag.BlockTagSupplier;
import dev.tr3ymix.cfm_wap.blockentity.CircuitBreakerBlockEntity;
import dev.tr3ymix.cfm_wap.util.shapes.CircuitBreakerBlockShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class CircuitBreakerBlock extends FurnitureHorizontalEntityBlock implements BlockTagSupplier {

    private final MetalType type;

    public CircuitBreakerBlock(MetalType type, Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(DIRECTION, Direction.NORTH)
                .setValue(POWERED, false)
                .setValue(OPEN, false)
        );
        this.type = type;
    }

    public MetalType getMetalType() {
        return this.type;
    }

    @Override
    protected Map<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states) {
        return ImmutableMap.copyOf(states.stream()
                .collect(Collectors.toMap(state -> state, state -> Shapes.block()))
        );
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter,
                                        BlockPos blockPos, CollisionContext collisionContext) {
        return CircuitBreakerBlockShapes.getShape(blockState.getValue(DIRECTION));
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if(blockState.getValue(DIRECTION).getOpposite() == blockHitResult.getDirection()){
            if(!level.isClientSide){
                BlockEntity blockEntity = level.getBlockEntity(blockPos);
                if(blockEntity instanceof CircuitBreakerBlockEntity circuitBreaker){
                    player.openMenu(circuitBreaker);
                    return InteractionResult.CONSUME;
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED, OPEN);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {

        return new CircuitBreakerBlockEntity(blockPos, blockState);
    }

    @Override
    public List<TagKey<Block>> getTags() {
        return List.of(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_STONE_TOOL);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Direction faceDirection = blockPlaceContext.getClickedFace();
        if(faceDirection == Direction.DOWN || faceDirection == Direction.UP) return null;

        return this.defaultBlockState().setValue(DIRECTION, faceDirection.getOpposite());
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        //noinspection DataFlowIssue
        return null;
    }
}
