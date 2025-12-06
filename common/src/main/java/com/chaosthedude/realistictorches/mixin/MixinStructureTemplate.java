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
import java.util.List;
import java.util.Map;

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

    /**
     * Schedule ticks for all pending torches after structure placement
     */
    @Inject(
            method = "placeInWorld",
            at = @At("RETURN")
    )
    private void realistictorches$afterPlaceInWorld(
            ServerLevelAccessor serverLevel,
            BlockPos offset,
            BlockPos pos,
            StructurePlaceSettings settings,
            RandomSource random,
            int flags,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (TorchTickScheduler.PENDING_TORCHES.isEmpty()) {
            return;
        }

        Map<BlockPos, TorchTickScheduler.TorchData> pending = Map.copyOf(TorchTickScheduler.PENDING_TORCHES);
        TorchTickScheduler.clear();

        for (Map.Entry<BlockPos, TorchTickScheduler.TorchData> entry : pending.entrySet()) {
            BlockPos torchPos = entry.getKey();
            TorchTickScheduler.TorchData data = entry.getValue();

            BlockState state = serverLevel.getBlockState(torchPos);

            if (state.is(RealisticTorchesRegistry.TORCH_BLOCK.get()) ||
                    state.is(RealisticTorchesRegistry.TORCH_WALL_BLOCK.get())) {

                // Schedule with the pre-calculated delay
                serverLevel.scheduleTick(torchPos, state.getBlock(), data.delay);
                
            }
        }
    }
}