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

import java.util.Random;

/**
 * Structure processor that replaces vanilla torches with realistic torches
 * during world generation (villages, mineshafts, etc.)
 */
public class TorchReplaceProcessor extends StructureProcessor {

    public static final TorchReplaceProcessor INSTANCE = new TorchReplaceProcessor();
    public static final MapCodec<TorchReplaceProcessor> CODEC = MapCodec.unit(INSTANCE);

    private static final ThreadLocal<Random> RANDOM = ThreadLocal.withInitial(Random::new);

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
        Random rand = RANDOM.get();

        // Replace vanilla torch with realistic torch (standing)
        if (state.is(Blocks.TORCH)) {

            int maxBurnTime = RealisticTorchBlock.getInitialBurnTime();
            int minBurnTime = (maxBurnTime * 40) / 100; // 40% - 100%
            int randomBurnTime = maxBurnTime > 0 ? minBurnTime + rand.nextInt(maxBurnTime - minBurnTime + 1) : 0;

            BlockState newState = RealisticTorchesRegistry.TORCH_BLOCK.get().defaultBlockState()
                    .setValue(RealisticTorchBlock.getLitState(), RealisticTorchBlock.LIT)
                    .setValue(RealisticTorchBlock.getBurnTime(), randomBurnTime);

            int delay = 20 + rand.nextInt(120);
            TorchTickScheduler.addTorch(currentBlockInfo.pos(), delay, randomBurnTime);

            return new StructureTemplate.StructureBlockInfo(
                    currentBlockInfo.pos(),
                    newState,
                    currentBlockInfo.nbt()
            );
        }

        // Replace vanilla wall torch with realistic wall torch
        if (state.is(Blocks.WALL_TORCH)) {
            
            int maxBurnTime = RealisticWallTorchBlock.getInitialBurnTime();
            int minBurnTime = (maxBurnTime * 40) / 100; // 40% - 100%
            int randomBurnTime = maxBurnTime > 0 ? minBurnTime + rand.nextInt(maxBurnTime - minBurnTime + 1) : 0;

            BlockState newState = RealisticTorchesRegistry.TORCH_WALL_BLOCK.get().defaultBlockState()
                    .setValue(RealisticWallTorchBlock.getLitState(), RealisticWallTorchBlock.LIT)
                    .setValue(RealisticWallTorchBlock.getBurnTime(), randomBurnTime)
                    .setValue(RealisticWallTorchBlock.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING));

            int delay = 20 + rand.nextInt(120);
            TorchTickScheduler.addTorch(currentBlockInfo.pos(), delay, randomBurnTime);

            return new StructureTemplate.StructureBlockInfo(
                    currentBlockInfo.pos(),
                    newState,
                    currentBlockInfo.nbt()
            );
        }

        return currentBlockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return RealisticTorchesRegistry.TORCH_REPLACE_PROCESSOR.get();
    }
}