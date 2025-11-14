package com.chaosthedude.realistictorches;

import com.chaosthedude.realistictorches.config.ConfigHandlerForge;
import com.chaosthedude.realistictorches.platform.Services;
import com.chaosthedude.realistictorches.registry.RealisticTorchesRegistry;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Constants.MOD_ID)
public class RealisticTorchesForge {

    public RealisticTorchesForge(FMLJavaModLoadingContext ctx) {
        // Register config
        ctx.registerConfig(ModConfig.Type.COMMON, ConfigHandlerForge.COMMON_CONFIG);
        ConfigHandlerForge.loadConfig(ConfigHandlerForge.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("realistictorches-common.toml"));

        Services.PLATFORM.getBiomeModifierHelper().replaceTorchInStructures();

        // Register all registries to event bus
        IEventBus eventBus = ctx.getModEventBus();
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