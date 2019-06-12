package net.darkhax.pillagers;

import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.util.EntityUtils;
import net.darkhax.bookshelf.util.MathsUtils;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = "pillagers", name = "Pillagers", version = "@VERSION@", dependencies = "required-after:bookshelf@[2.3.526,);", certificateFingerprint = "@FINGERPRINT@")
public class Pillagers {

    private static ConfigurationHandler config = new ConfigurationHandler("config/pillagers.cfg");

    @EventHandler
    public void onPreInit (FMLPreInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onMobKilled (LivingDropsEvent event) {

        // Checks if the initial conditions are right. These conditions include a configurable probability based on the looting enchantment, the entity being a villager, and the damage source being from an entity.
        if (MathsUtils.tryPercentage(ConfigurationHandler.basePercent + ConfigurationHandler.lootingPercent * event.getLootingLevel()) && event.getEntityLiving() instanceof EntityVillager && event.getSource().getTrueSource() != null) {

            // A secondary check to see if the damage was from a player. This check can be bypassed using the configuration file.
            if (ConfigurationHandler.allowFakePlayers ? PlayerUtils.isPlayerDamage(event.getSource()) : PlayerUtils.isPlayerReal(event.getSource().getTrueSource())) {

                final EntityVillager villager = (EntityVillager) event.getEntityLiving();
                final EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();

                // No looting kids!
                if (!villager.isChild()) {

                    final MerchantRecipeList trades = villager.getRecipes(player);

                    // Ensure the villager actually has trades to offer.
                    if (!trades.isEmpty()) {

                        ItemStack item = new ItemStack(Items.EMERALD);
                        int attempts = 0;

                        // Makes three attempts to find a valid recipe.
                        while (attempts < 3) {

                            // Gets a random recipe from the villager's trades with the player.
                            final MerchantRecipe recipe = trades.get(Constants.RANDOM.nextInt(trades.size()));

                            // Recipe can't be disabled, and must have a buying item and a selling
                            // item.
                            if (!recipe.isRecipeDisabled() && !recipe.getItemToSell().isEmpty() && !recipe.getItemToBuy().isEmpty()) {

                                item = recipe.getItemToSell().copy();
                                break;
                            }

                            attempts++;
                        }

                        // Add the item to the list of EntityItem spawned into the world.
                        EntityUtils.addDrop(item, event);
                    }
                }
            }
        }
    }
}