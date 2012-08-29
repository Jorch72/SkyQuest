package au.com.mineauz.SkyQuest.pedestals;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import au.com.mineauz.SkyQuest.SkyQuestPlugin;

/**
 * A base class for all pedestals
 * @author Schmoller
 */
public abstract class PedestalBase 
{
	private Item mHoveringItem;
	private ItemStack mHoverItemTemplate;
	private Location mLocation;
	private PedestalListener mEventListener;
	
	protected PedestalBase(ItemStack pedistalType, ItemStack hoverItem, Location pedestalLocation)
	{
		mLocation = pedestalLocation.clone();
		mLocation.getBlock().setTypeIdAndData(pedistalType.getTypeId(), pedistalType.getData().getData(), true);
		
		mHoverItemTemplate = hoverItem.clone();
		
		// Check for existing item
		Collection<Item> items = mLocation.getWorld().getEntitiesByClass(Item.class);
		Location loc = mLocation.clone().add(0, 1, 0);
		
		for(Item item : items)
		{
			if(item.getItemStack().equals(hoverItem) && item.getLocation().distanceSquared(loc) < 1D)
			{
				// Reattack the item
				mHoveringItem = item;
				break;
			}
		}
		// Spawn a new item if needed
		if(mHoveringItem == null)
		{
			mHoveringItem = mLocation.getWorld().dropItem(mLocation.clone().add(0.5, 1, 0.5), mHoverItemTemplate);
			mHoveringItem.setVelocity(new Vector(0,0,0));
		}
		
		// Register the event listener
		mEventListener = new PedestalListener();
		SkyQuestPlugin.instance.getServer().getPluginManager().registerEvents(mEventListener, SkyQuestPlugin.instance);
	}

			
	/**
	 * Called when a player right clicks on the pedestal 
	 * @param player The player that right clicked
	 */
	protected abstract void onPlayerActivatePedestal(Player player);
	
	/**
	 * Called when the pedestal is destroyed
	 */
	protected abstract void onPedestalDestroyed();
	
	/**
	 * Gets the location of the pedestal block
	 */
	public Location getLocation()
	{
		return mLocation;
	}
	
	/**
	 * This is used because bukkit event handers don't work inside base classes
	 */
	private class PedestalListener implements Listener
	{
		/**
		 * Prevent the hovering item from dissapearing
		 */
		@EventHandler(priority = EventPriority.HIGHEST)
		private void onItemDecay(ItemDespawnEvent event)
		{
			if(mHoveringItem != null && event.getEntity().getEntityId() == mHoveringItem.getEntityId())
				event.setCancelled(true);
		}
		
		/**
		 * Prevent the hovering item from being picked up
		 */
		@EventHandler(priority = EventPriority.HIGHEST)
		private void onItemPickup(PlayerPickupItemEvent event)
		{
			if(mHoveringItem != null && event.getItem().getEntityId() == mHoveringItem.getEntityId())
				event.setCancelled(true);
		}
		
		/**
		 * Remove the item on chunk unload
		 */
		@EventHandler
		private void onChunkUnload(ChunkUnloadEvent event)
		{
			if(event.getChunk() == mLocation.getChunk() && mHoveringItem != null)
			{
				mHoveringItem.remove();
				mHoveringItem = null;
				SkyQuestPlugin.instance.getLogger().info("Chunk unloaded");
			}
		}
		/**
		 * Re make the item on chunk load
		 */
		@EventHandler
		private void onChunkLoad(ChunkLoadEvent event)
		{
			if(event.getChunk() == mLocation.getChunk())
			{
				if(mHoveringItem != null)
					mHoveringItem.remove();
				SkyQuestPlugin.instance.getLogger().info("Chunk loaded");
				mHoveringItem = mLocation.getWorld().dropItem(mLocation.clone().add(0.5, 1, 0.5), mHoverItemTemplate);
				mHoveringItem.setVelocity(new Vector(0,0,0));
			}
		}
		/**
		 * Destroy the item on world unload
		 */
		@EventHandler
		private void onWorldUnload(WorldUnloadEvent event)
		{
			if(event.getWorld() == mLocation.getWorld() && mHoveringItem != null)
			{
				mHoveringItem.remove();
				mHoveringItem = null;
			}
		}
		/**
		 * Recreate the item on world load if the chunk is also loaded
		 */
		@EventHandler
		private void onWorldLoad(WorldLoadEvent event)
		{
			if(event.getWorld() == mLocation.getWorld() && mLocation.getChunk().isLoaded())
			{
				if(mHoveringItem != null)
					mHoveringItem.remove();

				mHoveringItem = mLocation.getWorld().dropItem(mLocation.clone().add(0.5, 1, 0.5), mHoverItemTemplate);
				mHoveringItem.setVelocity(new Vector(0,0,0));
			}
		}
		
		@EventHandler
		private void onBlockBreak(BlockBreakEvent event)
		{
			if(event.getBlock().getWorld() == mLocation.getWorld())
			{
				if(event.getBlock().getLocation().distanceSquared(mLocation) < 1D)
				{
					if(mHoveringItem != null)
						mHoveringItem.remove();
					
					mHoveringItem = null;
					
					onPedestalDestroyed();
				}
			}
		}
		
		@EventHandler
		private void onPlayerInteract(PlayerInteractEvent event)
		{
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasBlock() && event.getClickedBlock().getLocation().distanceSquared(mLocation) < 1D)
			{
				onPlayerActivatePedestal(event.getPlayer());
			}
		}
	}
}
