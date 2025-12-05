package com.chaosthedude.realistictorches.registry;

import com.chaosthedude.realistictorches.blockentity.RealisticCampfireBlockEntity;
import com.chaosthedude.realistictorches.client.renderer.RealisticCampfireRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiConsumer;

/**
 * Common client-side setup using vanilla Minecraft patterns
 * This mimics how vanilla EntityRenderers.createBlockEntityRenderers() works
 *
 * Platform-specific code just needs to call registerBlockEntityRenderers()
 * with their context implementation
 */
public class CommonClientSetup {

    @FunctionalInterface
    public interface BERendererRegistrar {
        <T extends BlockEntity> void register(
                BlockEntityType<T> type,
                BlockEntityRendererProvider<T> provider
        );
    }

    public static void registerBlockEntityRenderers(BERendererRegistrar registrar) {
        registrar.register(
                RealisticTorchesRegistry.CAMPFIRE_BLOCK_ENTITY.get(),
                RealisticCampfireRenderer::new
        );
    }
}