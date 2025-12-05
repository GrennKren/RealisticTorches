package com.chaosthedude.realistictorches.platform;

import com.chaosthedude.realistictorches.Constants;
import com.chaosthedude.realistictorches.platform.services.IPlatformHelper;
import com.chaosthedude.realistictorches.registry.CommonClientSetup;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class FabricPlatformHelper implements IPlatformHelper {

    // Map untuk menyimpan hubungan antara Block dan BlockEntityType
    private static final Map<Block, BlockEntityType<?>> BLOCK_TO_BLOCK_ENTITY_TYPE = new HashMap<>();

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        T registeredBlock = Registry.register(
                BuiltInRegistries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name),
                block.get()
        );
        return () -> registeredBlock;
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        T registeredItem = Registry.register(
                BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name),
                item.get()
        );
        return () -> registeredItem;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(
            String name,
            BiFunction<BlockPos, BlockState, T> blockEntitySupplier,
            Supplier<? extends Block>... validBlocks) {

        // CRITICAL: Get actual Block instances, bukan supplier
        Block[] blocks = new Block[validBlocks.length];
        for (int i = 0; i < validBlocks.length; i++) {
            blocks[i] = validBlocks[i].get(); // ✅ Ini sudah benar
        }

        // ✅ Gunakan BlockEntityType.Builder (bukan FabricBlockEntityTypeBuilder)
        BlockEntityType<T> blockEntityType = BlockEntityType.Builder.of(
                blockEntitySupplier::apply,
                blocks
        ).build(null); // Pass null for data fixer

        // Register
        BlockEntityType<T> registered = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name),
                blockEntityType
        );

        // Simpan mapping
        for (Block block : blocks) {
            BLOCK_TO_BLOCK_ENTITY_TYPE.put(block, registered);
        }

        return () -> registered;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Supplier<T> register(ResourceKey<? extends Registry<T>> registryKey, String name, Supplier<T> entry) {
        Registry<T> registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(registryKey.location());
        if (registry == null) {
            throw new IllegalStateException("Registry not found: " + registryKey.location());
        }

        T registered = Registry.register(
                registry,
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name),
                entry.get()
        );
        return () -> registered;
    }

    @Override
    public void registerAll(Object eventBus) {
        // No-op on Fabric, registration happens immediately
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void registerAllClient(Object eventBus) {
        // For Fabric, we need to register renderers on the client side
        // This should be called from a ClientModInitializer
        CommonClientSetup.registerBlockEntityRenderers(new CommonClientSetup.BERendererRegistrar() {
            @Override
            public <T extends BlockEntity> void register(
                    BlockEntityType<T> type,
                    BlockEntityRendererProvider<T> provider) {
                // Register the renderer using Fabric's BlockEntityRendererRegistry
                BlockEntityRendererRegistry.register(type, provider);
            }
        });
    }

    // Method untuk mendapatkan BlockEntityType dari Block
    public static BlockEntityType<?> getBlockEntityTypeForBlock(Block block) {
        return BLOCK_TO_BLOCK_ENTITY_TYPE.get(block);
    }

    public static BlockEntityType<?> getBlockEntityTypeForBlockName(String blockName) {
        for (Map.Entry<Block, BlockEntityType<?>> entry : BLOCK_TO_BLOCK_ENTITY_TYPE.entrySet()) {
            if (entry.getKey().getDescriptionId().contains(blockName)) {
                return entry.getValue();
            }
        }
        return null;
    }
}