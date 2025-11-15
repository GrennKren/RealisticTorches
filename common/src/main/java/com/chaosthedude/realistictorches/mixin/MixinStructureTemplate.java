package com.chaosthedude.realistictorches.mixin;

import com.chaosthedude.realistictorches.registry.RealisticTorchesRegistry;
import com.chaosthedude.realistictorches.worldgen.TorchReplaceProcessor;
import com.chaosthedude.realistictorches.worldgen.TorchTickScheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mixin(StructureTemplate.class)
public abstract class MixinStructureTemplate {

    /**
     * Inject our torch replacement processor into all structure placements
     */
    @ModifyVariable(
            method = "placeInWorld",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private StructurePlaceSettings realistictorches$addTorchProcessor(StructurePlaceSettings settings) {
        // Check if our processor is already present
        boolean hasOurProcessor = settings.getProcessors().stream()
                .anyMatch(p -> p instanceof TorchReplaceProcessor);

        // If already has our processor, return unchanged
        if (hasOurProcessor) {
            return settings;
        }

        // Create new list with existing processors + our processor
        List<StructureProcessor> newProcessors = new ArrayList<>(settings.getProcessors());
        newProcessors.add(TorchReplaceProcessor.INSTANCE);

        // Create new settings with updated processors
        StructurePlaceSettings newSettings = settings.copy();
        newSettings.clearProcessors();

        for (StructureProcessor processor : newProcessors) {
            newSettings.addProcessor(processor);
        }

        return newSettings;
    }

    @Inject(
            method = "placeInWorld",
            at = @At("TAIL")
    )
    private void realistictorches$afterPlaceInWorld(
            ServerLevelAccessor serverLevel, BlockPos offset, BlockPos pos, StructurePlaceSettings settings, RandomSource random, int flags, CallbackInfoReturnable<Boolean> cir
    ) {
        if (TorchTickScheduler.PENDING_TORCHES.isEmpty()) {
            return;
        }

        Set<BlockPos> pending = new HashSet<>(TorchTickScheduler.PENDING_TORCHES);
        TorchTickScheduler.PENDING_TORCHES.clear();

        for (BlockPos torchPos : pending) {
            BlockState state = serverLevel.getBlockState(torchPos);

            if (state.is(RealisticTorchesRegistry.TORCH_BLOCK.get()) ||
                    state.is(RealisticTorchesRegistry.TORCH_WALL_BLOCK.get())) {

                int delay = 20 + serverLevel.getRandom().nextInt(2381); // random 1–10 detik
                serverLevel.scheduleTick(torchPos, state.getBlock(), delay);
            }
        }
    }
}