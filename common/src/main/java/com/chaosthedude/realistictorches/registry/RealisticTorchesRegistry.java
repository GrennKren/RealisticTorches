package com.chaosthedude.realistictorches.registry;

import com.chaosthedude.realistictorches.Constants;
import com.chaosthedude.realistictorches.blocks.RealisticTorchBlock;
import com.chaosthedude.realistictorches.blocks.RealisticWallTorchBlock;
import com.chaosthedude.realistictorches.conditions.DropUnlitCondition;
import com.chaosthedude.realistictorches.config.ConfigHandler;
import com.chaosthedude.realistictorches.items.LitTorchItem;
import com.chaosthedude.realistictorches.items.MatchboxItem;
import com.chaosthedude.realistictorches.items.UnlitTorchItem;
import com.chaosthedude.realistictorches.platform.Services;
import com.chaosthedude.realistictorches.worldgen.TorchFeature;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.function.Supplier;

public class RealisticTorchesRegistry {

    public static Supplier<Feature<?>> TORCH_FEATURE;
    public static Supplier<PlacedFeature> TORCH_PLACED_FEATURE;


    // Blocks
    public static final Supplier<Block> TORCH_BLOCK = Services.PLATFORM.registerBlock(
            RealisticTorchBlock.NAME,
            RealisticTorchBlock::new
    );

    public static final Supplier<Block> TORCH_WALL_BLOCK = Services.PLATFORM.registerBlock(
            RealisticWallTorchBlock.NAME,
            RealisticWallTorchBlock::new
    );

    // Items
    public static final Supplier<Item> UNLIT_TORCH_ITEM = Services.PLATFORM.registerItem(
            UnlitTorchItem.NAME,
            () -> new UnlitTorchItem(new Item.Properties(), Direction.DOWN)
    );

    public static final Supplier<Item> LIT_TORCH_ITEM = Services.PLATFORM.registerItem(
            LitTorchItem.NAME,
            () -> new LitTorchItem(new Item.Properties(), Direction.DOWN)
    );

    public static final Supplier<Item> MATCHBOX_ITEM = Services.PLATFORM.registerItem(
            MatchboxItem.NAME,
            () -> new MatchboxItem(ConfigHandler.getMatchboxDurability())
    );

    public static final Supplier<Item> GLOWSTONE_PASTE_ITEM = Services.PLATFORM.registerItem(
            "glowstone_paste",
            () -> new Item(new Item.Properties())
    );

    public static final Supplier<Item> GLOWSTONE_CRYSTAL_ITEM = Services.PLATFORM.registerItem(
            "glowstone_crystal",
            () -> new Item(new Item.Properties())
    );

    // Loot conditions
    public static final Supplier<LootItemConditionType> DROP_UNLIT_CONDITION = Services.PLATFORM.register(
            Registries.LOOT_CONDITION_TYPE,
            DropUnlitCondition.NAME,
            () -> new LootItemConditionType(DropUnlitCondition.CODEC)
    );

    /**
     * Initialize registries. This should be called from platform-specific entry points.
     */
    public static void init() {
        // Trigger static initialization
    }

    /**
     * Register all to event bus (Forge/NeoForge specific)
     */
    public static void registerAll(Object eventBus) {
        Services.PLATFORM.registerAll(eventBus);
    }

    /**
     * Initialize world generation registries. This must be called after the
     * worldgen registries are bootstrapped by the game.
     */
    public static void registerWorldGen() {
        Constants.LOGGER.info("Registering world generation features for Realistic Torches");

        //// PINDAHKAN REGISTRASI WORLDGEN KE SINI
        TORCH_FEATURE = Services.PLATFORM.register(
                Registries.FEATURE,
                TorchFeature.NAME,
                () -> new TorchFeature(NoneFeatureConfiguration.CODEC)
        );

        TORCH_PLACED_FEATURE = Services.PLATFORM.register(
                Registries.PLACED_FEATURE,
                TorchFeature.NAME,
                TorchFeature::createPlacedFeature
        );
//
        Constants.LOGGER.info("World generation features registered");
    }

}