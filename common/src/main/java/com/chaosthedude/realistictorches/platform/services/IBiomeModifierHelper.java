package com.chaosthedude.realistictorches.platform.services;

/**
 * Platform-agnostic biome modification helper
 * Each platform implements this differently:
 * - Forge: Uses BiomeModifier system
 * - NeoForge: Uses BiomeModifier system
 * - Fabric: Uses Fabric's BiomeModificationAPI
 */
public interface IBiomeModifierHelper {

    /**
     * Register biome modifications for torch generation
     * This should be called during mod initialization
     */
    void registerTorchBiomeModifications();
}