package com.chaosthedude.realistictorches.worldgen;

import com.chaosthedude.realistictorches.blocks.RealisticTorchBlock;
import com.chaosthedude.realistictorches.blocks.RealisticWallTorchBlock;
import com.chaosthedude.realistictorches.config.ConfigHandler;
import com.chaosthedude.realistictorches.registry.RealisticTorchesRegistry;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class TorchFeature extends Feature<NoneFeatureConfiguration> {

    public static final String NAME = "replace_all_feature";

    public TorchFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        MutableBlockPos replacePos = new MutableBlockPos();

        // Note: generateLitTorches config removed, implement if needed
        boolean shouldGenerate = true; // Or add back to config if needed

        if (shouldGenerate) {
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < context.level().getHeight(); y++) {
                    for (int z = 0; z < 16; z++) {
                        replacePos.set(context.origin().getX(), 0, context.origin().getZ()).move(x, y, z);
                        if (context.level().getBlockState(replacePos).getBlock() == Blocks.TORCH) {
                            context.level().setBlock(replacePos, RealisticTorchesRegistry.TORCH_BLOCK.get().defaultBlockState().setValue(RealisticTorchBlock.getLitState(), RealisticTorchBlock.LIT).setValue(RealisticTorchBlock.getBurnTime(), RealisticTorchBlock.getInitialBurnTime()), 3);
                            context.level().scheduleTick(replacePos, context.level().getBlockState(replacePos).getBlock(), ConfigHandler.getTorchBurnoutTime());
                        } else if (context.level().getBlockState(replacePos).getBlock() == Blocks.WALL_TORCH) {
                            context.level().setBlock(replacePos, RealisticTorchesRegistry.TORCH_WALL_BLOCK.get().defaultBlockState().setValue(RealisticWallTorchBlock.getLitState(), RealisticTorchBlock.LIT).setValue(RealisticWallTorchBlock.getBurnTime(), RealisticWallTorchBlock.getInitialBurnTime()).setValue(BlockStateProperties.HORIZONTAL_FACING, context.level().getBlockState(replacePos).getValue(BlockStateProperties.HORIZONTAL_FACING)), 3);
                            context.level().scheduleTick(replacePos, context.level().getBlockState(replacePos).getBlock(), ConfigHandler.getTorchBurnoutTime());
                        }
                    }
                }
            }
        }
        return true;
    }

    public static PlacedFeature createPlacedFeature() {
        // 1. Buat instance dari ConfiguredFeature.
        // Ini adalah "resep" untuk fitur kita, yang menghubungkan kelas Feature
        // dengan konfigurasinya (dalam hal ini, NoneFeatureConfiguration).
        ConfiguredFeature<NoneFeatureConfiguration, ?> configuredFeature = new ConfiguredFeature<>(
                new TorchFeature(NoneFeatureConfiguration.CODEC),
                NoneFeatureConfiguration.INSTANCE
        );

        // 2. Bungkus ConfiguredFeature ke dalam Holder.
        // Holder adalah cara Minecraft untuk mereferensikan objek yang sudah
        // atau akan didaftarkan. Holder.direct() digunakan untuk objek yang kita buat langsung.
        Holder<ConfiguredFeature<?, ?>> configuredFeatureHolder = Holder.direct(configuredFeature);

        // 3. Buat PlacedFeature.
        // PlacedFeature memerlukan Holder dari ConfiguredFeature dan daftar PlacementModifier.
        // Karena logika penempatan kita ada di dalam metode place(), kita tidak memerlukan
        // modifier tambahan, jadi kita berikan daftar kosong.
        return new PlacedFeature(configuredFeatureHolder, List.of());
    }

}