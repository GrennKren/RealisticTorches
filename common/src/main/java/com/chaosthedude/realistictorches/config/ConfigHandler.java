package com.chaosthedude.realistictorches.config;

import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {

    public static final String CATEGORY_GENERAL = "general";

    // Config values
    private static int torchBurnoutTimeValue = 60;
    private static int matchboxDurabilityValue = 64;
    private static boolean noRelightEnabledValue = false;
    private static boolean matchboxCreatesFireValue = false;
    private static boolean vanillaTorchesDropUnlitValue = true;
    private static List<String> lightTorchItemsValue = new ArrayList<>();

    // Platform-specific save callback
    private static Runnable saveCallback = null;

    public static void setSaveCallback(Runnable callback) {
        saveCallback = callback;
    }

    public static void save() {
        if (saveCallback != null) {
            saveCallback.run();
        }
    }

    // Getters
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

    // Setters
    public static void setTorchBurnoutTime(int value) {
        torchBurnoutTimeValue = value;
        save();
    }

    public static void setMatchboxDurability(int value) {
        matchboxDurabilityValue = value;
        save();
    }

    public static void setNoRelightEnabled(boolean value) {
        noRelightEnabledValue = value;
        save();
    }

    public static void setMatchboxCreatesFire(boolean value) {
        matchboxCreatesFireValue = value;
        save();
    }

    public static void setVanillaTorchesDropUnlit(boolean value) {
        vanillaTorchesDropUnlitValue = value;
        save();
    }

    public static void setLightTorchItems(List<String> value) {
        lightTorchItemsValue = new ArrayList<>(value);
        save();
    }
}