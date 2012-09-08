package au.com.mineauz.SkyQuest.pedestals;

import java.util.Collection;

import net.minecraft.server.NBTTagCompound;

import org.bukkit.Bukkit;
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
import au.com.mineauz.SkyQuest.Util;

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
	private boolean mIsLoaded;
	
	protected PedestalBase(ItemStack pedistalType, ItemStack hoverItem, Location pedestalLocation)
	{
		mIsLoaded = true;
		mLocation = pedestalLocation.clone();
		mLocation.getBlock().setTypeIdAndData(pedistalType.getTypeId(), pedistalType.getData().getData(), true);
		
		mHoverItemTemplate = hoverItem.clone();
		
		// Check for existing item
		Collection<Item> items = mLocation.getWorld().getEntitiesByClass(Item.class);
		Location loc = mLocation.clone().add(0, 1, 0);
		
		for(Item item : items)
		{
			if(item.getItemStack().equals(hoverItem) && item.getLocation().distanceSquared(loc) < 1D && !item.isDead())
			{
				// Reattach the item
				mHoveringItem = item;
				SkyQuestPlugin.instance.getLogger().fine("Attaching old item");
				break;
			}
		}
		// Spawn a new item if needed
		if(mHoveringItem == null)
		{
			SkyQuestPlugin.instance.getLogger().fine("Spawning new item");
			mHoveringItem = mLocation.getWorld().dropItem(mLocation.clone().add(0.5, 1, 0.5), mHoverItemTemplate);
			mHoveringItem.setVelocity(new Vector(0,0,0));
		}
		
		// Register the event listener
		mEventListener = new PedestalListener();
		SkyQuestPlugin.instance.getServer().getPluginManager().registerEvents(mEventListener, SkyQuestPlugin.instance);
	}
	/**
	 * Used for loading a pedestal from file. Do not use normally
	 */
	protected PedestalBase()
	{
		mIsLoaded = true;
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
	 * Called if the chunk is unloaded, the plugin is unloaded, etc.
	 */
	public void onRemove()
	{
		if(mHoveringItem != null)
		{
			mHoveringItem.remove();
			mHoveringItem = null;
		}
		
		if(mEventListener != null)
		{
			mEventListener = null;
		}
		
		mIsLoaded = false;
	}
	
	/**
	 * Called when saving data. 
	 * If overriding, remember to call this version
	 */
	public void writeToNBT(NBTTagCompound root)
	{
		root.setString("World", mLocation.getWorld().getName());
		root.setDouble("X", mLocation.getX());
		root.setDouble("Y", mLocation.getY());
		root.setDouble("Z", mLocation.getZ());
		
		Util.writeItemStackToNBT(mHoverItemTemplate, root);
	}
	
	/**
	 * Called when loading data. 
	 * If overriding, remember to call this version
	 */
	public void readFromNBT(NBTTagCompound root)
	{
		mLocation = new Location(Bukkit.getWorld(root.getString("World")), root.getDouble("X"), root.getDouble("Y"), root.getDouble("Z"));
		
		mHoverItemTemplate = Util.readItemStackFromNBT(root);
		
		// Check for existing item
		Collection<Item> items = mLocation.getWorld().getEntitiesByClass(Item.class);
		Location loc = mLocation.clone().add(0, 1, 0);
		
		for(Item item : items)
		{
			if(item.getItemStack().equals(mHoverItemTemplate) && item.getLocation().distanceSquared(loc) < 1D && !item.isDead())
			{
				SkyQuestPlugin.instance.getLogger().fine("Attaching old item");
				// Reattach the item
				mHoveringItem = item;
				break;
			}
		}
		// Spawn a new item if needed
		if(mHoveringItem == null)
		{
			SkyQuestPlugin.instance.getLogger().fine("Spawning new item");
			mHoveringItem = mLocation.getWorld().dropItem(mLocation.clone().add(0.5, 1, 0.5), mHoverItemTemplate);
			mHoveringItem.setVelocity(new Vector(0,0,0));
		}
		
		// Register the event listener
		mEventListener = new PedestalListener();
		SkyQuestPlugin.instance.getServer().getPluginManager().registerEvents(mEventListener, SkyQuestPlugin.instance);
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
			if(!mIsLoaded)
				return;
			if(mHoveringItem != null && event.getEntity().getEntityId() == mHoveringItem.getEntityId())
				event.setCancelled(true);
		}
		
		/**
		 * Prevent the hovering item from being picked up
		 */
		@EventHandler(priority = EventPriority.HIGHEST)
		private void onItemPickup(PlayerPickupItemEvent event)
		{
			if(!mIsLoaded)
				return;
			if(mHoveringItem != null && event.getItem().getEntityId() == mHoveringItem.getEntityId())
				event.setCancelled(true);
		}
		
		/**
		 * Remove the item on chunk unload
		 */
		@EventHandler
		private void onChunkUnload(ChunkUnloadEvent event)
		{
			if(!mIsLoaded)
				return;
			if(event.getChunk() == mLocation.getChunk())
			{
				onRemove();
			}
		}
		/**
		 * Re make the item on chunk load
		 */
		@EventHandler
		private void onChunkLoad(ChunkLoadEvent event)
		{
			if(!mIsLoaded)
				return;
			if(event.getChunk() == mLocation.getChunk())
			{
				onRemove();
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
			if(!mIsLoaded)
				return;
			if(event.getWorld() == mLocation.getWorld())
			{
				onRemove();
			}
		}
		/**
		 * Recreate the item on world load if the chunk is also loaded
		 */
		@EventHandler
		private void onWorldLoad(WorldLoadEvent event)
		{
			if(!mIsLoaded)
				return;
			if(event.getWorld() == mLocation.getWorld() && mLocation.getChunk().isLoaded())
			{
				onRemove();

				mHoveringItem = mLocation.getWorld().dropItem(mLocation.clone().add(0.5, 1, 0.5), mHoverItemTemplate);
				mHoveringItem.setVelocity(new Vector(0,0,0));
			}
		}
		
		@EventHandler
		private void onBlockBreak(BlockBreakEvent event)
		{
			if(!mIsLoaded)
				return;
			if(event.getBlock().getWorld() == mLocation.getWorld())
			{
				if(event.getBlock().getLocation().distanceSquared(mLocation) < 1D)
				{
					onRemove();
					
					onPedestalDestroyed();
				}
			}
		}
		
		@EventHandler
		private void onPlayerInteract(PlayerInteractEvent event)
		{
			if(!mIsLoaded)
				return;
			if(mLocation.getWorld() != event.getPlayer().getWorld())
				return;
			
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasBlock() && event.getClickedBlock().getLocation().distanceSquared(mLocation) < 1D)
			{
				onPlayerActivatePedestal(event.getPlayer());
				//event.setUseInteractedBlock(Result.DENY);
				event.setCancelled(true);
			}
		}
	}
}
