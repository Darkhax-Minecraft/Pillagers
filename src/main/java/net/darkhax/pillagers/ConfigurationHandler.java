package net.darkhax.pillagers;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler {

    /**
     * An instance of Forge's configuration system.
     */
    private final Configuration config;

    /**
     * The chance that a villager will drop loot when killed.
     */
    private float basePercent = 0.05f;

    /**
     * The amount each level of looting contributes to the {@link #basePercent}.
     */
    private float lootingPercent = 0.015f;

    /**
     * Whether or not the damage must come directly from a player.
     */
    private boolean allowFakePlayers = false;

    /**
     * Constructs a configuration handler instance. This should only be done once per run of
     * the game.
     *
     * @param file The configuration file handle suggested by Forge.
     */
    ConfigurationHandler (File file) {

        this.config = new Configuration(file);
        this.syncConfigData();
    }

    /**
     * Initializes the properties used by the configuration handler. This will load the values
     * from the file if one exists, if not the file will be created.
     */
    private void syncConfigData () {

        this.basePercent = this.config.getFloat("basePercentage", Configuration.CATEGORY_GENERAL, 0.05f, 0f, 1f, "The base chance that a player will get villager loot when killing a villager.");
        this.lootingPercent = this.config.getFloat("lootingPercent", Configuration.CATEGORY_GENERAL, 0.015f, 0f, 1f, "The amount each level of looting contributes to a villager loot drop chance.");
        this.allowFakePlayers = this.config.getBoolean("allowFakePlayers", Configuration.CATEGORY_GENERAL, false, "When enabled, blocks like mob grinders and spikes which allow player kills to be automated will be able to automate villager loot drops.");

        // Check if the config has had any changes, if so save them.
        if (this.config.hasChanged()) {
            this.config.save();
        }
    }

    /**
     * Gets the base percentage chance that a villager will drop a trade item.
     *
     * @return The base percentage of a villager dropping a trade item.
     */
    public float getBasePercent () {

        return this.basePercent;
    }

    /**
     * Gets the percent amount that looting contributes to the base percentage.
     *
     * @return The amount each level of looting contributes to the base percentage.
     */
    public float getLootingPercent () {

        return this.lootingPercent;
    }

    /**
     * Checks if the villager must be killed by a real player to drop their loot.
     *
     * @return Whether or not the villager must be killed by a real player to drop their loot.
     */
    public boolean areFakePlayersAllowed () {

        return this.allowFakePlayers;
    }
}