package com.chaosthedude.realistictorches;

import com.chaosthedude.realistictorches.platform.FabricPlatformHelper;
import com.chaosthedude.realistictorches.registry.RealisticTorchesRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTabs;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

public class
RealisticTorchesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Constants.LOGGER.info("Hello Fabric world!");

        // Load config for Fabric
        //ConfigHandlerFabric.loadConfig();
        //RealisticTorchesRegistry.init();
        // Set render layers for blocks (client-side only)
        setupBlockRenderLayers();

        FabricPlatformHelper platformHelper = new FabricPlatformHelper();
        platformHelper.registerAllClient(null);

        // Register items to vanilla creative tabs
        registerCreativeTabs();
    }

    private void setupBlockRenderLayers() {
        if (isClient()) {
            setupClientBlockRenderLayers();
        }
    }

    @Environment(EnvType.CLIENT)
    private void setupClientBlockRenderLayers() {
        try {
            // Set render layer untuk torch blocks
            BlockRenderLayerMap.INSTANCE.putBlock(
                    RealisticTorchesRegistry.TORCH_BLOCK.get(),
                    RenderType.cutout()
            );

            BlockRenderLayerMap.INSTANCE.putBlock(
                    RealisticTorchesRegistry.TORCH_WALL_BLOCK.get(),
                    RenderType.cutout()
            );

            BlockRenderLayerMap.INSTANCE.putBlock(
                    RealisticTorchesRegistry.CAMPFIRE_BLOCK.get(),
                    RenderType.cutout()
            );

            Constants.LOGGER.info("Block render layers set successfully");
        } catch (Exception e) {
            Constants.LOGGER.error("Failed to set block render layers: " + e.getMessage());
        }
    }

    private boolean isClient() {
        try {
            Class.forName("net.minecraft.client.Minecraft");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public void registerCreativeTabs() {
        // Fabric uses ItemGroupEvents for creative tab registration
        ItemGroupEvents.modifyEntriesEvent(
                CreativeModeTabs.TOOLS_AND_UTILITIES
        ).register(entries -> {
            entries.accept(new ItemStack(
                    RealisticTorchesRegistry.MATCHBOX_ITEM.get()
            ));
        });

        ItemGroupEvents.modifyEntriesEvent(
                CreativeModeTabs.FUNCTIONAL_BLOCKS
        ).register(entries -> {
            entries.accept(new ItemStack(
                    RealisticTorchesRegistry.LIT_TORCH_ITEM.get()
            ));
            //entries.accept(new ItemStack(
            //        RealisticTorchesRegistry.UNLIT_TORCH_ITEM.get()
            //));
        });

        ItemGroupEvents.modifyEntriesEvent(
                CreativeModeTabs.INGREDIENTS
        ).register(entries -> {
            entries.accept(new ItemStack(
                    RealisticTorchesRegistry.GLOWSTONE_CRYSTAL_ITEM.get()
            ));
            entries.accept(new ItemStack(
                    RealisticTorchesRegistry.GLOWSTONE_PASTE_ITEM.get()
            ));
        });
    }
}