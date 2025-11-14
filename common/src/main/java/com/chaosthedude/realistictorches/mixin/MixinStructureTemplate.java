package com.chaosthedude.realistictorches.mixin;

import com.chaosthedude.realistictorches.worldgen.TorchReplaceProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

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
}