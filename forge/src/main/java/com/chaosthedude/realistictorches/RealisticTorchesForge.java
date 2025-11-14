package com.chaosthedude.realistictorches;

import com.chaosthedude.realistictorches.config.ConfigHandlerForge;
import com.chaosthedude.realistictorches.platform.Services;
import com.chaosthedude.realistictorches.registry.RealisticTorchesRegistry;
import com.chaosthedude.realistictorches.worldgen.TorchReplacementModifier;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Constants.MOD_ID)
public class RealisticTorchesForge {

    public RealisticTorchesForge(FMLJavaModLoadingContext ctx) {
        Constants.LOGGER.info("Hello Forge world!");

        // Register config
        new ModLoadingContext().registerConfig(ModConfig.Type.COMMON, ConfigHandlerForge.COMMON_CONFIG);
        ConfigHandlerForge.loadConfig(ConfigHandlerForge.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("realistictorches-common.toml"));

        IEventBus eventBus = ctx.getModEventBus();
        Services.PLATFORM.getBiomeModifierHelper().replaceTorchInStructures();
        // Register all registries to event bus
        RealisticTorchesRegistry.registerAll(eventBus);

        // Client setup
        //eventBus.addListener(this::clientSetup);
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

    //@OnlyIn(Dist.CLIENT)
    //private void clientSetup(final FMLClientSetupEvent event) {
    //    ItemBlockRenderTypes.setRenderLayer(RealisticTorchesRegistry.TORCH_BLOCK.get(), RenderType.cutout());
    //    ItemBlockRenderTypes.setRenderLayer(RealisticTorchesRegistry.TORCH_WALL_BLOCK.get(), RenderType.cutout());
    //}

}