package com.chaosthedude.realistictorches.registry;

import com.chaosthedude.realistictorches.blocks.RealisticCampfireBlock;
import com.chaosthedude.realistictorches.blocks.RealisticTorchBlock;
import com.chaosthedude.realistictorches.blocks.RealisticWallTorchBlock;
import com.chaosthedude.realistictorches.blockentity.RealisticCampfireBlockEntity;
import com.chaosthedude.realistictorches.conditions.DropUnlitCondition;
import com.chaosthedude.realistictorches.config.ConfigHandler;
import com.chaosthedude.realistictorches.items.LitCampfireItem;
import com.chaosthedude.realistictorches.items.LitTorchItem;
import com.chaosthedude.realistictorches.items.MatchboxItem;
import com.chaosthedude.realistictorches.items.UnlitCampfireItem;
import com.chaosthedude.realistictorches.items.UnlitTorchItem;
import com.chaosthedude.realistictorches.platform.Services;
import com.chaosthedude.realistictorches.worldgen.TorchReplaceProcessor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.function.BiFunction;
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

    // Torch Blocks
    public static final Supplier<Block> TORCH_BLOCK = Services.PLATFORM.registerBlock(
            RealisticTorchBlock.NAME,
            RealisticTorchBlock::new
    );

    public static final Supplier<Block> TORCH_WALL_BLOCK = Services.PLATFORM.registerBlock(
            RealisticWallTorchBlock.NAME,
            RealisticWallTorchBlock::new
    );

    // Campfire Block
    public static final Supplier<Block> CAMPFIRE_BLOCK = Services.PLATFORM.registerBlock(
            RealisticCampfireBlock.NAME,
            RealisticCampfireBlock::new
    );

    // Torch Items
    public static final Supplier<Item> UNLIT_TORCH_ITEM = Services.PLATFORM.registerItem(
            UnlitTorchItem.NAME,
            () -> new UnlitTorchItem(new Item.Properties(), Direction.DOWN)
    );

    public static final Supplier<Item> LIT_TORCH_ITEM = Services.PLATFORM.registerItem(
            LitTorchItem.NAME,
            () -> new LitTorchItem(new Item.Properties(), Direction.DOWN)
    );

    // Campfire Items
    public static final Supplier<Item> UNLIT_CAMPFIRE_ITEM = Services.PLATFORM.registerItem(
            UnlitCampfireItem.NAME,
            () -> new UnlitCampfireItem(new Item.Properties())
    );

    public static final Supplier<Item> LIT_CAMPFIRE_ITEM = Services.PLATFORM.registerItem(
            LitCampfireItem.NAME,
            () -> new LitCampfireItem(new Item.Properties())
    );

    // Other Items
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

    // Campfire Block Entity - Daftarkan setelah semua block dan item
    @SuppressWarnings("unchecked")
    public static final Supplier<BlockEntityType<RealisticCampfireBlockEntity>> CAMPFIRE_BLOCK_ENTITY =
            Services.PLATFORM.registerBlockEntity(
                    "campfire",
                    RealisticCampfireBlockEntity::new,
                    CAMPFIRE_BLOCK
            );


    public static void init() {
        // Inisialisasi jika diperlukan
    }
}