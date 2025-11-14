package com.chaosthedude.realistictorches.platform;

import com.chaosthedude.realistictorches.Constants;
import com.chaosthedude.realistictorches.platform.services.IBiomeModifierHelper;
import com.chaosthedude.realistictorches.worldgen.TorchReplacementModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.ChunkEvent;

/**
 * Forge implementation using BiomeModifier system
 * The actual BiomeModifier (TorchBiomeModifier) is registered via JSON in data files
 * This helper can be used for any runtime modifications if needed
 */
public class ForgeBiomeModifierHelper implements IBiomeModifierHelper {

    @Override
    public void registerTorchBiomeModifications() {
        Constants.LOGGER.info("Forge biome modifications are handled via JSON data files");

        // In Forge, biome modifiers are registered via:
        // - data/realistictorches/forge/biome_modifier/torch_replacement.json
        // This is handled automatically by Forge's data loading system
        // No runtime registration needed here

        // The TorchBiomeModifier class and its codec are already registered
        // in RealisticTorchesRegistry.BIOME_MODIFIER_SERIALIZERS
    }

    @Override
    public void replaceTorchInStructures() {
      //  MinecraftForge.EVENT_BUS.addListener(ForgeBiomeModifierHelper::onChunkLoad);
    }

    private static void onChunkLoad(ChunkEvent.Load event) {
        if (!(event.getChunk() instanceof LevelChunk chunk)) return;
        LevelAccessor level = event.getLevel();
        if (level.isClientSide()) return;

        for (LevelChunkSection section : chunk.getSections()) {
            if (section.hasOnlyAir()) continue;
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 16; y++) {
                    for (int z = 0; z < 16; z++) {
                        BlockState state = section.getBlockState(x, y, z);
                        if (state.is(Blocks.TORCH) || state.is(Blocks.WALL_TORCH)) {
                            BlockPos pos = chunk.getPos().getBlockAt(x, SectionPos.sectionToBlockCoord(chunk.getMinSection() + y), z);
                            TorchReplacementModifier.replaceTorch(level, pos);
                        }
                    }
                }
            }
        }
    }




}