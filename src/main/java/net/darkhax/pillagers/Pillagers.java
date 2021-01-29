package net.darkhax.pillagers;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.MerchantOffers;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLNetworkConstants;

@Mod("pillagers")
public class Pillagers {

	private final Configuration config;
	
	public Pillagers () {
		
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
		this.config = new Configuration();
		MinecraftForge.EVENT_BUS.addListener(this::onMobDropsLoot);
	}

    @SubscribeEvent
    public void onMobDropsLoot (LivingDropsEvent event) {

    	final LivingEntity target = event.getEntityLiving();
    	
    	if (target instanceof AbstractVillagerEntity && event.getSource() != null && target.world instanceof ServerWorld) {
    		
    		final AbstractVillagerEntity villager = (AbstractVillagerEntity) target;
    		final VillagerProfession profession = villager instanceof VillagerEntity ? ((VillagerEntity) villager).getVillagerData().getProfession() : VillagerProfession.NONE;
    		final ServerWorld world = (ServerWorld) villager.world;
    		final Entity killer = event.getSource().getTrueSource();
    		
    		if (config.isPlayerRequired() && !(killer instanceof PlayerEntity)) {
    			
    			return;
    		}
    		
    		if (config.shouldExcludeFakePlayers() && killer instanceof FakePlayer) {
    			
    			return;
    		}
    		
    		if (!config.canLootChildren() && target.isChild()) {
    			
    			return;
    		}
    		
    		if (config.isLimitToVillages() && !world.isVillage(target.getPosition())) {
    			
    			return;
    		}
    		
    		if (config.isLimitToRaids() && !world.hasRaid(target.getPosition())) {
    			
    			return;
    		}
    		
    		if (config.isProfessionRequired() && (profession == null || profession == VillagerProfession.NONE || profession == VillagerProfession.NITWIT)) {
    			
    			return;
    		}
    		
    		if (world.rand.nextDouble() < config.getBaseChance() + (event.getLootingLevel() * config.getLootingChance()) + (world.hasRaid(target.getPosition()) ? config.getRaidChance() : 0d)) {
    			
    			final MerchantOffers offers = villager.getOffers();
    			
    			final int drops = world.rand.nextInt((config.getMaxLoot() - config.getMinLoot()) + 1) + config.getMinLoot();
    			
    			for (int i = 0; i < drops; i++) {
    				
        			if (offers != null && !offers.isEmpty()) {
        				
        				final MerchantOffer offer = offers.get(world.rand.nextInt(offers.size()));
        				
        				if (offer != null && !offer.getSellingStack().isEmpty()) {
        					
        					event.getDrops().add(new ItemEntity(world, villager.getPosX(), villager.getPosY(), villager.getPosZ(), offer.getSellingStack().copy()));
        				}
        			}
        			
        			else {
        				
    					event.getDrops().add(new ItemEntity(world, villager.getPosX(), villager.getPosY(), villager.getPosZ(), new ItemStack(Items.EMERALD, world.rand.nextInt(7))));
        			}
    			}
    			
    			if (killer instanceof LivingEntity && world.rand.nextDouble() < config.getBadOmenChance()) {
    				
    				((LivingEntity) killer).addPotionEffect(new EffectInstance(Effects.BAD_OMEN, 5 * 60 * 20));
    			}
    		}
        }
    }
}