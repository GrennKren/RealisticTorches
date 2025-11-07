package com.chaosthedude.realistictorches.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Common config handler with platform-agnostic access
 */
public class ConfigHandler {

    public static final String CATEGORY_GENERAL = "general";

    // Default values
    private static int torchBurnoutTimeValue = 60;
    private static int matchboxDurabilityValue = 64;
    private static boolean noRelightEnabledValue = false;
    private static boolean matchboxCreatesFireValue = false;
    private static boolean vanillaTorchesDropUnlitValue = true;
    private static List<String> lightTorchItemsValue = new ArrayList<>();

    // Getters untuk digunakan di common code
    public static int getTorchBurnoutTime() {
        return torchBurnoutTimeValue;
    }

    public static int getMatchboxDurability() {
        return matchboxDurabilityValue;
    }

    public static boolean isNoRelightEnabled() {
        return noRelightEnabledValue;
    }

    public static boolean isMatchboxCreatesFire() {
        return matchboxCreatesFireValue;
    }

    public static boolean isVanillaTorchesDropUnlit() {
        return vanillaTorchesDropUnlitValue;
    }

    public static List<String> getLightTorchItems() {
        return lightTorchItemsValue;
    }

    // Setters untuk platform-specific code
    public static void setTorchBurnoutTime(int value) {
        torchBurnoutTimeValue = value;
    }

    public static void setMatchboxDurability(int value) {
        matchboxDurabilityValue = value;
    }

    public static void setNoRelightEnabled(boolean value) {
        noRelightEnabledValue = value;
    }

    public static void setMatchboxCreatesFire(boolean value) {
        matchboxCreatesFireValue = value;
    }

    public static void setVanillaTorchesDropUnlit(boolean value) {
        vanillaTorchesDropUnlitValue = value;
    }

    public static void setLightTorchItems(List<String> value) {
        lightTorchItemsValue = value;
    }
}