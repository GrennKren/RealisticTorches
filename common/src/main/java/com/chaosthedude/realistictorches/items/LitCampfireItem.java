package com.chaosthedude.realistictorches.items;

import com.chaosthedude.realistictorches.registry.RealisticTorchesRegistry;
import net.minecraft.world.item.BlockItem;

public class LitCampfireItem extends BlockItem {
    public static final String NAME = "lit_campfire";

    public LitCampfireItem(Properties properties) {
        super(RealisticTorchesRegistry.CAMPFIRE_BLOCK.get(), properties);
    }
}