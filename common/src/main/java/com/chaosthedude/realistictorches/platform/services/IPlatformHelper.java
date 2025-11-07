package com.chaosthedude.realistictorches.platform.services;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }

    /**
     * Register a block to the platform's registry
     *
     * @param name The registry name
     * @param block The block supplier
     * @return The registered block supplier
     */
    <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block);

    /**
     * Register an item to the platform's registry
     *
     * @param name The registry name
     * @param item The item supplier
     * @return The registered item supplier
     */
    <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item);

    /**
     * Register a generic registry entry
     *
     * @param registryKey The registry key
     * @param name The registry name
     * @param entry The entry supplier
     * @return The registered entry supplier
     */
    <T> Supplier<T> register(ResourceKey<? extends Registry<T>> registryKey, String name, Supplier<T> entry);

    /**
     * Register all deferred registries (Forge/NeoForge specific, no-op on Fabric)
     *
     * @param eventBus The mod event bus
     */
    void registerAll(Object eventBus);

    /**
     * Get the biome modifier helper for this platform
     *
     * @return The biome modifier helper
     */
    IBiomeModifierHelper getBiomeModifierHelper();

}