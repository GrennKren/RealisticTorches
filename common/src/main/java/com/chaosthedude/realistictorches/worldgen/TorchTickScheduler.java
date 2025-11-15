// (Di dalam package utility atau worldgen Anda)
package com.chaosthedude.realistictorches.worldgen;

import net.minecraft.core.BlockPos;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TorchTickScheduler {

    public static final Set<BlockPos> PENDING_TORCHES = Collections.synchronizedSet(new HashSet<>());

}