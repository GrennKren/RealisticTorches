package com.chaosthedude.realistictorches.platform;

import com.chaosthedude.realistictorches.Constants;
import com.chaosthedude.realistictorches.platform.services.IBiomeModifierHelper;
import com.chaosthedude.realistictorches.platform.services.IPlatformHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class NeoForgePlatformHelper implements IPlatformHelper {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Map<ResourceKey<?>, DeferredRegister<?>> REGISTRIES = new HashMap<>();
    private final IBiomeModifierHelper biomeModifierHelper = new NeoForgeBiomeModifierHelper();

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
            REGISTRIES.values().forEach(register -> register.register(bus));
        }
    }

    @Override
    public IBiomeModifierHelper getBiomeModifierHelper() {
        return biomeModifierHelper;
    }


}