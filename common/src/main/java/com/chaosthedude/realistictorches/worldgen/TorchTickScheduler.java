package com.chaosthedude.realistictorches.worldgen;

import net.minecraft.core.BlockPos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TorchTickScheduler {

    // Store position -> torch data (delay + burntime)
    public static final Map<BlockPos, TorchData> PENDING_TORCHES = new ConcurrentHashMap<>();

    public static void addTorch(BlockPos pos, int delay, int burnTime) {
        PENDING_TORCHES.put(pos.immutable(), new TorchData(delay, burnTime));
    }

    public static void clear() {
        PENDING_TORCHES.clear();
    }

    public static class TorchData {
        public final int delay;
        public final int burnTime;

        public TorchData(int delay, int burnTime) {
            this.delay = delay;
            this.burnTime = burnTime;
        }
    }
}