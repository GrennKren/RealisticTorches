package com.chaosthedude.realistictorches.registry;

import com.chaosthedude.realistictorches.blocks.RealisticTorchBlock;
import com.chaosthedude.realistictorches.blocks.RealisticWallTorchBlock;
import com.chaosthedude.realistictorches.conditions.DropUnlitCondition;
import com.chaosthedude.realistictorches.config.ConfigHandler;
import com.chaosthedude.realistictorches.items.LitTorchItem;
import com.chaosthedude.realistictorches.items.MatchboxItem;
import com.chaosthedude.realistictorches.items.UnlitTorchItem;
import com.chaosthedude.realistictorches.platform.Services;
import com.chaosthedude.realistictorches.worldgen.TorchReplaceProcessor;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.function.Supplier;

public class RealisticTorchesRegistry {

    // Structure Processor for replacing vanilla torches
    @SuppressWarnings("unchecked")
    public static final Supplier<StructureProcessorType<TorchReplaceProcessor>> TORCH_REPLACE_PROCESSOR =
            (Supplier<StructureProcessorType<TorchReplaceProcessor>>) (Object) Services.PLATFORM.register(
                    Registries.STRUCTURE_PROCESSOR,
                    "torch_replace",
                    () -> (StructureProcessorType<TorchReplaceProcessor>) () -> TorchReplaceProcessor.CODEC
            );

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
     * Register all to event bus (Forge/NeoForge specific)
     */
    public static void registerAll(Object eventBus) {
        Services.PLATFORM.registerAll(eventBus);
    }

}