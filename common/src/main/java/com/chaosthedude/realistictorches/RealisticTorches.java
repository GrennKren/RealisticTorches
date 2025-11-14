package com.chaosthedude.realistictorches;

import com.chaosthedude.realistictorches.platform.Services;

public class RealisticTorches {

    /**
     * Common initialization that runs on all platforms
     */
    public static void init() {
        Constants.LOGGER.info("Initializing Realistic Torches on {} in {} environment",
                Services.PLATFORM.getPlatformName(),
                Services.PLATFORM.getEnvironmentName()
        );
    }
}