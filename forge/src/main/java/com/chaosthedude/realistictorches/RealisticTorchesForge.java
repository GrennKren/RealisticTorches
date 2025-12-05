package com.chaosthedude.realistictorches;

import com.chaosthedude.realistictorches.config.ConfigScreenForge;
import com.chaosthedude.realistictorches.platform.Services;
import com.chaosthedude.realistictorches.registry.RealisticTorchesRegistry;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class RealisticTorchesForge {

    public RealisticTorchesForge(FMLJavaModLoadingContext ctx) {
        // Register config
        ctx.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, screen) -> ConfigScreenForge.create(screen)
                )
        );

        RealisticTorchesRegistry.init();

        // Register all registries to event bus
        IEventBus eventBus = ctx.getModEventBus();
        Services.PLATFORM.registerAll(eventBus);
        Services.PLATFORM.registerAllClient(eventBus);

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