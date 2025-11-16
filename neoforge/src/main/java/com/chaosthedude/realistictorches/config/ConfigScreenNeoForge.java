package com.chaosthedude.realistictorches.config;

import com.chaosthedude.realistictorches.config.ConfigHandler;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.StringListListEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;

public class ConfigScreenNeoForge {

    public static Screen create(Screen parent) {

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("config.realistictorches.title"))
                .setSavingRunnable(ConfigHandler::save);

        ConfigEntryBuilder entry = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(
                Component.translatable("config.realistictorches.category.general")
        );

        general.addEntry(entry.startIntSlider(
                        Component.translatable("config.realistictorches.torchBurnoutTime"),
                        ConfigHandler.getTorchBurnoutTime(),
                        -1, 2880
                )
                .setDefaultValue(60)
                .setTooltip(Component.translatable("config.realistictorches.torchBurnoutTime.tooltip"))
                .setSaveConsumer(ConfigHandler::setTorchBurnoutTime)
                .build());

        general.addEntry(entry.startIntSlider(
                        Component.translatable("config.realistictorches.matchboxDurability"),
                        ConfigHandler.getMatchboxDurability(),
                        -1, 512
                )
                .setDefaultValue(64)
                .setTooltip(Component.translatable("config.realistictorches.matchboxDurability.tooltip"))
                .setSaveConsumer(ConfigHandler::setMatchboxDurability)
                .build());

        general.addEntry(entry.startBooleanToggle(
                        Component.translatable("config.realistictorches.torchNoRelight"),
                        ConfigHandler.isNoRelightEnabled()
                )
                .setDefaultValue(false)
                .setTooltip(Component.translatable("config.realistictorches.torchNoRelight.tooltip"))
                .setSaveConsumer(ConfigHandler::setNoRelightEnabled)
                .build());

        general.addEntry(entry.startBooleanToggle(
                        Component.translatable("config.realistictorches.matchboxCreatesFire"),
                        ConfigHandler.isMatchboxCreatesFire()
                )
                .setDefaultValue(false)
                .setTooltip(Component.translatable("config.realistictorches.matchboxCreatesFire.tooltip"))
                .setSaveConsumer(ConfigHandler::setMatchboxCreatesFire)
                .build());

        general.addEntry(entry.startBooleanToggle(
                        Component.translatable("config.realistictorches.vanillaTorchesDropUnlit"),
                        ConfigHandler.isVanillaTorchesDropUnlit()
                )
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.realistictorches.vanillaTorchesDropUnlit.tooltip"))
                .setSaveConsumer(ConfigHandler::setVanillaTorchesDropUnlit)
                .build());

        general.addEntry(entry.startStrList(
                        Component.translatable("config.realistictorches.lightTorchItems"),
                        ConfigHandler.getLightTorchItems()
                )
                .setDefaultValue(new ArrayList<>())
                .setTooltip(Component.translatable("config.realistictorches.lightTorchItems.tooltip"))
                .setSaveConsumer(ConfigHandler::setLightTorchItems)
                .setCreateNewInstance(stringList -> {
                    return new StringListListEntry.StringListCell(
                            "<new item id>",  // placeholder terlihat
                            stringList
                    );
                })
                .build());

        return builder.build();
    }
}
