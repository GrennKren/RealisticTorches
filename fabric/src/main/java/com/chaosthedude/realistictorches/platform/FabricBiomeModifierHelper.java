package com.chaosthedude.realistictorches.platform;

import com.chaosthedude.realistictorches.Constants;
import com.chaosthedude.realistictorches.platform.services.IBiomeModifierHelper;
import com.chaosthedude.realistictorches.registry.RealisticTorchesRegistry;
//import com.chaosthedude.realistictorches.worldgen.TorchFeature;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * Fabric implementation using Fabric's Biome Modification API
 */
public class FabricBiomeModifierHelper implements IBiomeModifierHelper {

    @Override
    public void replaceTorchInStructures() {

    }
}