package com.blazelow.dawnoftimeextras;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.StairsShape;

import java.util.function.Predicate;

final class StairShapes {

    private StairShapes() {
    }

    static StairsShape of(BlockState state, BlockGetter level, BlockPos pos,
                          DirectionProperty facingProperty, Predicate<BlockState> kin) {
        Direction facing = state.getValue(facingProperty);

        BlockState front = level.getBlockState(pos.relative(facing));
        if (kin.test(front)) {
            Direction other = front.getValue(facingProperty);
            if (other.getAxis() != facing.getAxis()
                    && squaresOff(state, level, pos, other.getOpposite(), facingProperty, kin)) {
                return other == facing.getCounterClockWise() ? StairsShape.OUTER_LEFT : StairsShape.OUTER_RIGHT;
            }
        }

        BlockState behind = level.getBlockState(pos.relative(facing.getOpposite()));
        if (kin.test(behind)) {
            Direction other = behind.getValue(facingProperty);
            if (other.getAxis() != facing.getAxis()
                    && squaresOff(state, level, pos, other, facingProperty, kin)) {
                return other == facing.getCounterClockWise() ? StairsShape.INNER_LEFT : StairsShape.INNER_RIGHT;
            }
        }

        return StairsShape.STRAIGHT;
    }

    private static boolean squaresOff(BlockState state, BlockGetter level, BlockPos pos, Direction direction,
                                      DirectionProperty facingProperty, Predicate<BlockState> kin) {
        BlockState neighbour = level.getBlockState(pos.relative(direction));
        return !kin.test(neighbour)
                || neighbour.getValue(facingProperty) != state.getValue(facingProperty);
    }
}
