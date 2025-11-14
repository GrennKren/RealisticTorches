package com.chaosthedude.realistictorches;

import com.chaosthedude.realistictorches.config.ConfigHandlerNeoForge;
import com.chaosthedude.realistictorches.registry.RealisticTorchesRegistry;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(Constants.MOD_ID)
public class RealisticTorchesNeoForge {

    public RealisticTorchesNeoForge(IEventBus eventBus, ModContainer modContainer) {
        // Register config
        modContainer.registerConfig(ModConfig.Type.COMMON, ConfigHandlerNeoForge.COMMON_CONFIG);
        //ConfigHandlerNeoForge.loadConfig(ConfigHandlerNeoForge.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("realistictorches-common.toml"));

        // Register all registries to event bus
        RealisticTorchesRegistry.registerAll(eventBus);

        eventBus.addListener(this::buildCreativeTabContents);
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
}