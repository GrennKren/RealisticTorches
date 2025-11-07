package com.chaosthedude.realistictorches.registry;

import com.chaosthedude.realistictorches.Constants;
import com.chaosthedude.realistictorches.worldgen.TorchBiomeModifier;
import com.chaosthedude.realistictorches.worldgen.TorchFeature;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Forge-specific registry for BiomeModifier
 * This is separate from the common registry because BiomeModifier is Forge-only
 */
public class RealisticTorchesRegistryForge {

    // Biome modifiers (Forge-specific)
    public static final DeferredRegister<MapCodec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Constants.MOD_ID);

    public static final RegistryObject<MapCodec<? extends BiomeModifier>> TORCH_BIOME_MODIFIER =
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