package com.chaosthedude.realistictorches.platform;

import com.chaosthedude.realistictorches.Constants;
import com.chaosthedude.realistictorches.platform.services.IBiomeModifierHelper;

/**
 * NeoForge implementation using BiomeModifier system
 * The actual BiomeModifier (TorchBiomeModifier) is registered via JSON in data files
 * This helper can be used for any runtime modifications if needed
 */
public class NeoForgeBiomeModifierHelper implements IBiomeModifierHelper {

    @Override
    public void registerTorchBiomeModifications() {
        Constants.LOGGER.info("NeoForge biome modifications are handled via JSON data files");

        // In NeoForge, biome modifiers are registered via:
        // - data/realistictorches/neoforge/biome_modifier/torch_replacement.json
        // This is handled automatically by NeoForge's data loading system
        // No runtime registration needed here

        // The TorchBiomeModifier class and its codec are already registered
        // in RealisticTorchesRegistry.BIOME_MODIFIER_SERIALIZERS (for Forge/NeoForge)
    }
}