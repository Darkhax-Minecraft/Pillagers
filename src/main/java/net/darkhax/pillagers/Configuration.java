package net.darkhax.pillagers;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

public class Configuration {
    
    private final DoubleValue baseChance;
    private final DoubleValue lootingChance;
    private final DoubleValue raidChance;
    private final BooleanValue requirePlayer;
    private final BooleanValue excludeFakePlayers;
    private final BooleanValue requireProfession;
    private final BooleanValue limitToVillages;
    private final BooleanValue limitToRaids;
    private final BooleanValue lootChildren;
    private final DoubleValue badOmenChance;
    private final IntValue minDrops;
    private final IntValue maxDrops;
    
    public Configuration() {
        
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        
        // General Configs
        builder.comment("General settings for the mod.");
        builder.push("general");
        
        builder.comment("The base chance for a villager to drop a trade item.");
        this.baseChance = builder.defineInRange("base-chance", 0.05d, -1d, 1d);
        
        builder.comment("Each level of the Looting enchantment will increase the base chance by this amount.");
        this.lootingChance = builder.defineInRange("looting-chance-increase", 0.015, -1d, 1d);
        
        builder.comment("Increase the base chance by this amount if the villager is killed during a raid.");
        this.raidChance = builder.defineInRange("raid-bonus-chance", 0.15, -1d, 1d);
        
        builder.comment("Should only villagers killed by a player drop loot?");
        this.requirePlayer = builder.define("player-required", true);
        
        builder.comment("Should villagers killed by fake players be excluded from getting additional loot?");
        this.excludeFakePlayers = builder.define("exclude-fake-players", true);
        
        builder.comment("Should villagers require a profession to drop additional loot?");
        this.requireProfession = builder.define("require-profession", false);
        
        builder.comment("Should villagers only drop additional loot when in a village?");
        this.limitToVillages = builder.define("only-in-villages", false);
        
        builder.comment("Should villagers only drop additional loot when in a raid?");
        this.limitToRaids = builder.define("only-in-raids", false);
        
        builder.comment("Should child villagers be lootable?");
        this.lootChildren = builder.define("loot-children", false);
        
        builder.comment("The chance that pillaging a villager will give the player the Bad Omen status effect.");
        this.badOmenChance = builder.defineInRange("bad-omen-chance", 0.15d, 0d, 1d);
        
        builder.comment("How many loots should be spawned when a villager is pillaged?");
        builder.push("loot-amount");
        
        builder.comment("The minimum number of loot to drop.");
        this.minDrops = builder.defineInRange("minimum", 1, 0, Short.MAX_VALUE);
        
        builder.comment("The maximum number of loot to drop.");
        this.maxDrops = builder.defineInRange("maximum", 3, 0, Short.MAX_VALUE);
        
        builder.pop();
        
        builder.pop();
        
        // Auto-register with Forge.
		ModLoadingContext.get().registerConfig(Type.COMMON, builder.build());
    }

	public double getBaseChance() {
		
		return baseChance.get();
	}

	public double getLootingChance() {
		
		return lootingChance.get();
	}

	public double getRaidChance() {
		
		return raidChance.get();
	}

	public boolean isPlayerRequired() {
		
		return requirePlayer.get();
	}

	public boolean shouldExcludeFakePlayers() {
		
		return excludeFakePlayers.get();
	}

	public boolean isProfessionRequired() {
		
		return requireProfession.get();
	}

	public boolean isLimitToVillages() {
		
		return limitToVillages.get();
	}

	public boolean isLimitToRaids() {
		
		return limitToRaids.get();
	}
	
	public boolean canLootChildren() {
		
		return lootChildren.get();
	}

	public double getBadOmenChance() {
		
		return badOmenChance.get();
	}
	
	public int getMinLoot() {
		
		return this.minDrops.get();
	}
	
	public int getMaxLoot() {
		
		return this.maxDrops.get();
	}
}