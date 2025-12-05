package com.chaosthedude.realistictorches.items;

import com.chaosthedude.realistictorches.blocks.RealisticCampfireBlock;
import com.chaosthedude.realistictorches.registry.RealisticTorchesRegistry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

public class LitCampfireItem extends BlockItem {
    public static final String NAME = "lit_campfire";

    public LitCampfireItem(Properties properties) {
        super(RealisticTorchesRegistry.CAMPFIRE_BLOCK.get(), properties);
    }

    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        if (state != null) {
            return state
                    .setValue(RealisticCampfireBlock.getLitStateProperty(), RealisticCampfireBlock.LIT_STATE)
                    .setValue(RealisticCampfireBlock.getBurnTime(), RealisticCampfireBlock.getInitialBurnTime());
        }
        return null;
    }
}