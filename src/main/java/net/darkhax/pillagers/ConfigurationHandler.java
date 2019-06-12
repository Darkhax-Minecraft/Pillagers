package net.darkhax.pillagers;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler {

    public static Configuration config;
    public static float basePercent = 0.05f;
    public static float lootingPercent = 0.015f;
    public static boolean allowFakePlayers = false;

    public ConfigurationHandler (String file) {

        config = new Configuration(new File(file));
        this.syncConfigData();
    }

    private void syncConfigData () {

        basePercent = config.getFloat("basePercentage", Configuration.CATEGORY_GENERAL, 0.05f, 0f, 1f, "The base chance that a player will get villager loot when killing a villager.");
        lootingPercent = config.getFloat("lootingPercent", Configuration.CATEGORY_GENERAL, 0.015f, 0f, 1f, "The amount each level of looting contributes to a villager loot drop chance.");
        allowFakePlayers = config.getBoolean("allowFakePlayers", Configuration.CATEGORY_GENERAL, false, "When enabled, blocks like mob grinders and spikes which allow player kills to be automated will be able to automate villager loot drops.");

        if (config.hasChanged()) {
            config.save();
        }
    }
}