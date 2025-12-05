package com.chaosthedude.realistictorches.platform;

import com.chaosthedude.realistictorches.Constants;
import com.chaosthedude.realistictorches.platform.services.IPlatformHelper;
import com.chaosthedude.realistictorches.registry.CommonClientSetup;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class NeoForgePlatformHelper implements IPlatformHelper {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
            BuiltInRegistries.BLOCK_ENTITY_TYPE, Constants.MOD_ID
    );
    private static final Map<ResourceKey<?>, DeferredRegister<?>> REGISTRIES = new HashMap<>();

    // Map untuk menyimpan registrasi renderer
    private static final Map<Supplier<? extends BlockEntityType<?>>,
            Function<BlockEntityRendererProvider.Context, ? extends BlockEntityRenderer<?>>> RENDERER_REGISTRATIONS = new HashMap<>();

    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        return ITEMS.register(name, item);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(
            String name,
            BiFunction<BlockPos, BlockState, T> blockEntitySupplier,
            Supplier<? extends Block>... validBlocks) {
        Supplier<BlockEntityType<T>> registryObject = BLOCK_ENTITIES.register(name, () -> {
            Block[] blocks = new Block[validBlocks.length];
            for (int i = 0; i < validBlocks.length; i++) {
                blocks[i] = validBlocks[i].get();
            }
            return BlockEntityType.Builder.of(blockEntitySupplier::apply, blocks).build(null);
        });
        return registryObject;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Supplier<T> register(ResourceKey<? extends Registry<T>> registryKey, String name, Supplier<T> entry) {
        DeferredRegister<T> register = (DeferredRegister<T>) REGISTRIES.computeIfAbsent(
                registryKey,
                key -> DeferredRegister.create(registryKey, Constants.MOD_ID)
        );
        return register.register(name, entry);
    }

    @Override
    public void registerAll(Object eventBus) {
        if (eventBus instanceof IEventBus bus) {
            BLOCKS.register(bus);
            ITEMS.register(bus);
            BLOCK_ENTITIES.register(bus);
            REGISTRIES.values().forEach(register -> register.register(bus));
        }
    }

    @Override
    public void registerAllClient(Object eventBus) {
        if (eventBus instanceof IEventBus bus) {
            bus.addListener((EntityRenderersEvent.RegisterRenderers event) -> {
                CommonClientSetup.registerBlockEntityRenderers(event::registerBlockEntityRenderer);
            });
        }
    }
}