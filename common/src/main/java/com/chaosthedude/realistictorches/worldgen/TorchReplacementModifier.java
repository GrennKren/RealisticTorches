package com.chaosthedude.realistictorches.worldgen;

import com.chaosthedude.realistictorches.Constants;
import com.chaosthedude.realistictorches.registry.RealisticTorchesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Helper untuk mengganti torch vanilla dengan realistic torch di worldgen.
 * Dipanggil dari IBiomeModifierHelper pada setiap struktur yang dimuat.
 */
public class TorchReplacementModifier {

    public static void replaceTorch(LevelAccessor level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        // Replace standing torch
        if (state.is(Blocks.TORCH)) {
            level.setBlock(pos, RealisticTorchesRegistry.TORCH_BLOCK.get().defaultBlockState(), 3);
        }

        // Replace wall torch
        else if (state.is(Blocks.WALL_TORCH)) {
            BlockState wallState = RealisticTorchesRegistry.TORCH_WALL_BLOCK.get().defaultBlockState();
            if (state.hasProperty(net.minecraft.world.level.block.WallTorchBlock.FACING)) {
                wallState = wallState.setValue(
                        net.minecraft.world.level.block.WallTorchBlock.FACING,
                        state.getValue(net.minecraft.world.level.block.WallTorchBlock.FACING)
                );
            }
            level.setBlock(pos, wallState, 3);
        }
    }
}
