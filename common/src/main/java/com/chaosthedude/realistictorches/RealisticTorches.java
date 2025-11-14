package com.chaosthedude.realistictorches;

import com.chaosthedude.realistictorches.platform.Services;
import com.chaosthedude.realistictorches.registry.RealisticTorchesRegistry;

public class RealisticTorches {

    /**
     * Common initialization that runs on all platforms
     */
    public static void init() {
        Constants.LOGGER.info("Initializing Realistic Torches on {} in {} environment",
                Services.PLATFORM.getPlatformName(),
                Services.PLATFORM.getEnvironmentName()
        );

        // Initialize registries
        RealisticTorchesRegistry.init();
    }
}