package com.chaosthedude.realistictorches.registry;

import com.chaosthedude.realistictorches.Constants;
//import com.chaosthedude.realistictorches.worldgen.TorchBiomeModifier;
//import com.chaosthedude.realistictorches.worldgen.TorchFeature;
import com.chaosthedude.realistictorches.worldgen.TorchBiomeModifier;
import com.chaosthedude.realistictorches.worldgen.TorchFeature;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

/**
 * NeoForge-specific registry for BiomeModifier
 * This is separate from the common registry because BiomeModifier is NeoForge-only
 */
public class RealisticTorchesRegistryNeoForge {

    // Biome modifiers (NeoForge-specific)
    public static final DeferredRegister<MapCodec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Constants.MOD_ID);

    public static final Supplier<MapCodec<? extends BiomeModifier>> TORCH_BIOME_MODIFIER =
            BIOME_MODIFIER_SERIALIZERS.register(TorchFeature.NAME,
                    () -> RecordCodecBuilder.<TorchBiomeModifier>mapCodec(instance ->
                            instance.group(
                                    PlacedFeature.CODEC.fieldOf("feature").forGetter(TorchBiomeModifier::feature)
                            ).apply(instance, TorchBiomeModifier::new)
                    )
            );

    public static void register(IEventBus eventBus) {
        BIOME_MODIFIER_SERIALIZERS.register(eventBus);
    }
}