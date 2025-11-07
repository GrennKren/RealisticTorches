package com.chaosthedude.realistictorches;

import com.chaosthedude.realistictorches.config.ConfigHandlerNeoForge;
import com.chaosthedude.realistictorches.platform.Services;
import com.chaosthedude.realistictorches.registry.RealisticTorchesRegistry;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(Constants.MOD_ID)
public class RealisticTorchesNeoForge {

    public RealisticTorchesNeoForge(IEventBus eventBus, ModContainer modContainer) {
        Constants.LOGGER.info("Hello NeoForge world!");

        // Register config
        modContainer.registerConfig(ModConfig.Type.COMMON, ConfigHandlerNeoForge.COMMON_CONFIG);
        //ConfigHandlerNeoForge.loadConfig(ConfigHandlerNeoForge.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("realistictorches-common.toml"));

        // Register all registries to event bus
        RealisticTorchesRegistry.registerAll(eventBus);

        // Client setup
        //eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::buildCreativeTabContents);

        //NeoForge.EVENT_BUS.register(this);

        // Initialize common code
        RealisticTorches.init();

        // Biome modifications for NeoForge are handled via JSON data files
        // See: data/realistictorches/neoforge/biome_modifier/torch_replacement.json
        //Services.PLATFORM.getBiomeModifierHelper().registerTorchBiomeModifications();
    }

    private void buildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(new ItemStack(RealisticTorchesRegistry.MATCHBOX_ITEM.get()));
        } else if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(new ItemStack(RealisticTorchesRegistry.LIT_TORCH_ITEM.get()));
        } else if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(new ItemStack(RealisticTorchesRegistry.GLOWSTONE_CRYSTAL_ITEM.get()));
            event.accept(new ItemStack(RealisticTorchesRegistry.GLOWSTONE_PASTE_ITEM.get()));
        }
    }

    //@OnlyIn(Dist.CLIENT)
    //private void clientSetup(final FMLClientSetupEvent event) {
    //    ItemBlockRenderTypes.setRenderLayer(RealisticTorchesRegistry.TORCH_BLOCK.get(), RenderType.cutout());
    //    ItemBlockRenderTypes.setRenderLayer(RealisticTorchesRegistry.TORCH_WALL_BLOCK.get(), RenderType.cutout());
    //}
}