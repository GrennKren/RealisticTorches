package com.chaosthedude.realistictorches.config;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigHandlerNeoForge {

    private static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
    public static ModConfigSpec COMMON_CONFIG;

    public static ModConfigSpec.IntValue torchBurnoutTime;
    public static ModConfigSpec.IntValue matchboxDurability;
    public static ModConfigSpec.BooleanValue noRelightEnabled;
    public static ModConfigSpec.BooleanValue matchboxCreatesFire;
    public static ModConfigSpec.BooleanValue vanillaTorchesDropUnlit;
    public static ModConfigSpec.ConfigValue<List<String>> lightTorchItems;

    static {
        COMMON_BUILDER.comment("General Settings").push(ConfigHandler.CATEGORY_GENERAL);
        String desc;

        desc = "The amount of time until a torch burns out, in minutes. Setting this to a negative value will disable torch burnout.";
        torchBurnoutTime = COMMON_BUILDER.comment(desc).defineInRange("torchBurnoutTime", 60, -1, 2880);

        desc = "The durability of the matchbox. Setting this to a negative value will result in unlimited uses.";
        matchboxDurability = COMMON_BUILDER.comment(desc).defineInRange("matchboxDurability", 64, -1, 512);

        desc = "Determines whether lit torches disappear after they are extinguished, rather than turning into unlit torches.";
        noRelightEnabled = COMMON_BUILDER.comment(desc).define("torchNoRelight", false);

        desc = "Determines whether matchboxes can light fires in the world like flint and steel.";
        matchboxCreatesFire = COMMON_BUILDER.comment(desc).define("matchboxCreatesFire", false);

        desc = "Determines whether vanilla torches drop unlit torches when broken.";
        vanillaTorchesDropUnlit = COMMON_BUILDER.comment(desc).define("vanillaTorchesDropUnlit", true);

        desc = "The list of items that should be allowed to light torches placed in the world, besides the matchbox and flint and steel. Ex: [\"minecraft:lava_bucket\"]";
        lightTorchItems = COMMON_BUILDER.comment(desc).define("lightTorchItems", new ArrayList<String>());

        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    /**
     * Sync NeoForge config values to common config
     * This should be called after config is loaded/changed
     */
    public static void syncToCommon() {
        ConfigHandler.setTorchBurnoutTime(torchBurnoutTime.get());
        ConfigHandler.setMatchboxDurability(matchboxDurability.get());
        ConfigHandler.setNoRelightEnabled(noRelightEnabled.get());
        ConfigHandler.setMatchboxCreatesFire(matchboxCreatesFire.get());
        ConfigHandler.setVanillaTorchesDropUnlit(vanillaTorchesDropUnlit.get());
        ConfigHandler.setLightTorchItems(lightTorchItems.get());
    }
}