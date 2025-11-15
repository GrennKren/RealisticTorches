package com.chaosthedude.realistictorches.worldgen;

import com.chaosthedude.realistictorches.blocks.RealisticTorchBlock;
import com.chaosthedude.realistictorches.blocks.RealisticWallTorchBlock;
import com.chaosthedude.realistictorches.registry.RealisticTorchesRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * Structure processor that replaces vanilla torches with realistic torches
 * during world generation (villages, mineshafts, etc.)
 */
public class TorchReplaceProcessor extends StructureProcessor {

    public static final TorchReplaceProcessor INSTANCE = new TorchReplaceProcessor();

    public static final MapCodec<TorchReplaceProcessor> CODEC = MapCodec.unit(INSTANCE);

    private TorchReplaceProcessor() {
        // Private constructor - use INSTANCE
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(
            LevelReader level,
            BlockPos templatePos,
            BlockPos blockWorldPos,
            StructureTemplate.StructureBlockInfo originalBlockInfo,
            StructureTemplate.StructureBlockInfo currentBlockInfo,
            StructurePlaceSettings settings
    ) {
        BlockState state = currentBlockInfo.state();

        // Replace vanilla torch with realistic torch (standing)
        if (state.is(Blocks.TORCH)) {
            BlockState newState = RealisticTorchesRegistry.TORCH_BLOCK.get().defaultBlockState()
                    .setValue(RealisticTorchBlock.getLitState(), RealisticTorchBlock.LIT)
                    .setValue(RealisticTorchBlock.getBurnTime(), RealisticTorchBlock.getInitialBurnTime());

            //TorchTickScheduler.PENDING_TORCHES.add(blockWorldPos.immutable());
            TorchTickScheduler.PENDING_TORCHES.add(currentBlockInfo.pos());

            return new StructureTemplate.StructureBlockInfo(
                    currentBlockInfo.pos(),
                    newState,
                    currentBlockInfo.nbt()
            );
        }

        // Replace vanilla wall torch with realistic wall torch
        if (state.is(Blocks.WALL_TORCH)) {
            BlockState newState = RealisticTorchesRegistry.TORCH_WALL_BLOCK.get().defaultBlockState()
                    .setValue(RealisticWallTorchBlock.getLitState(), RealisticWallTorchBlock.LIT)
                    .setValue(RealisticWallTorchBlock.getBurnTime(), RealisticWallTorchBlock.getInitialBurnTime())
                    .setValue(RealisticWallTorchBlock.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING));

            //TorchTickScheduler.PENDING_TORCHES.add(blockWorldPos.immutable());
            TorchTickScheduler.PENDING_TORCHES.add(currentBlockInfo.pos());

            return new StructureTemplate.StructureBlockInfo(
                    currentBlockInfo.pos(),
                    newState,
                    currentBlockInfo.nbt()
            );
        }


        // Return unchanged block if not a torch
        return currentBlockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return RealisticTorchesRegistry.TORCH_REPLACE_PROCESSOR.get();
    }
}