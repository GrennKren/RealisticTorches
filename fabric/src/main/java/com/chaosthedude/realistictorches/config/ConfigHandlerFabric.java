package com.chaosthedude.realistictorches.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.chaosthedude.realistictorches.Constants;

import net.fabricmc.loader.api.FabricLoader;

/**
 * Simple properties-based config for Fabric
 * You can replace this with cloth-config or another config library if preferred
 */
public class ConfigHandlerFabric {

    private static final String CONFIG_FILE_NAME = "realistictorches.properties";
    private static File configFile;
    private static Properties properties = new Properties();

    public static void loadConfig() {
        configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), CONFIG_FILE_NAME);

        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                properties.load(reader);
            } catch (IOException e) {
                Constants.LOGGER.error("Failed to load config file", e);
            }
        } else {
            // Create default config
            setDefaults();
            saveConfig();
        }

        // Parse and sync to common config
        syncToCommon();
    }

    private static void setDefaults() {
        properties.setProperty("torchBurnoutTime", "60");
        properties.setProperty("matchboxDurability", "64");
        properties.setProperty("torchNoRelight", "false");
        properties.setProperty("matchboxCreatesFire", "false");
        properties.setProperty("vanillaTorchesDropUnlit", "true");
        properties.setProperty("lightTorchItems", "");
    }

    private static void saveConfig() {
        try (FileWriter writer = new FileWriter(configFile)) {
            properties.store(writer, "Realistic Torches Configuration");
        } catch (IOException e) {
            Constants.LOGGER.error("Failed to save config file", e);
        }
    }

    private static void syncToCommon() {
        try {
            int torchBurnoutTime = Integer.parseInt(properties.getProperty("torchBurnoutTime", "60"));
            int matchboxDurability = Integer.parseInt(properties.getProperty("matchboxDurability", "64"));
            boolean noRelight = Boolean.parseBoolean(properties.getProperty("torchNoRelight", "false"));
            boolean matchboxFire = Boolean.parseBoolean(properties.getProperty("matchboxCreatesFire", "false"));
            boolean vanillaDropUnlit = Boolean.parseBoolean(properties.getProperty("vanillaTorchesDropUnlit", "true"));
            String lightItemsStr = properties.getProperty("lightTorchItems", "");

            List<String> lightItems = new ArrayList<>();
            if (!lightItemsStr.isEmpty()) {
                String[] items = lightItemsStr.split(",");
                for (String item : items) {
                    lightItems.add(item.trim());
                }
            }

            ConfigHandler.setTorchBurnoutTime(torchBurnoutTime);
            ConfigHandler.setMatchboxDurability(matchboxDurability);
            ConfigHandler.setNoRelightEnabled(noRelight);
            ConfigHandler.setMatchboxCreatesFire(matchboxFire);
            ConfigHandler.setVanillaTorchesDropUnlit(vanillaDropUnlit);
            ConfigHandler.setLightTorchItems(lightItems);

        } catch (NumberFormatException e) {
            Constants.LOGGER.error("Invalid config value, using defaults", e);
            setDefaults();
            syncToCommon();
        }
    }
}