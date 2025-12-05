package com.chaosthedude.realistictorches.platform;

import com.chaosthedude.realistictorches.Constants;
import com.chaosthedude.realistictorches.platform.services.IPlatformHelper;
import com.chaosthedude.realistictorches.registry.CommonClientSetup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ForgePlatformHelper implements IPlatformHelper {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Constants.MOD_ID);
    private static final Map<ResourceKey<?>, DeferredRegister<?>> REGISTRIES = new HashMap<>();

    @Override
    public String getPlatformName() {
        return "Forge";
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
        RegistryObject<T> registryObject = BLOCKS.register(name, block);
        return registryObject;
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        RegistryObject<T> registryObject = ITEMS.register(name, item);
        return registryObject;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(
            String name,
            BiFunction<BlockPos, BlockState, T> blockEntitySupplier,
            Supplier<? extends Block>... validBlocks) {
        RegistryObject<BlockEntityType<T>> registryObject = BLOCK_ENTITIES.register(name, () -> {
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
        RegistryObject<T> registryObject = register.register(name, entry);
        return registryObject;
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