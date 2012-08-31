package au.com.mineauz.SkyQuest;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.SkyQuest.pedestals.DebugPedestal;
import au.com.mineauz.SkyQuest.pedestals.Pedestals;

public class SkyQuestEvents implements Listener{
	
	private Map<OfflinePlayer, ItemStack> droppedBook = new HashMap<OfflinePlayer, ItemStack>(); //TODO: Probably move this to a Data class.
    
    @EventHandler
	private void onRightClickGround(PlayerInteractEvent event)
	{
		// Test of pedestal creation
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasBlock() && (event.hasItem() && event.getItem().getType() == Material.BOOK))
		{
			Location loc = event.getClickedBlock().getRelative(event.getBlockFace(), 1).getLocation();
			Pedestals.addPedestal(new DebugPedestal(loc));
		}
	}
    
    @EventHandler
    private void playerDeath(PlayerDeathEvent event){
    	Player player = (Player) event.getEntity();
		for(ItemStack item : event.getDrops()){
			if(item.getType() == Material.WRITTEN_BOOK && MagicBook.isMagicBook(item)){
				//Save the magic book to be given on respawn.
				droppedBook.put(player, item);
				event.getDrops().remove(item);
				break;
			}
		}
    }
    
    @EventHandler
    private void playerRespawn(PlayerRespawnEvent event){
    	if(droppedBook.containsKey(event.getPlayer())){
    		//If the player had died with a magic book, give it back to them.
    		event.getPlayer().getInventory().addItem(droppedBook.get(event.getPlayer()));
    		droppedBook.remove(event.getPlayer());
    	}
    }
    
    @EventHandler
    private void magicBookDrop(PlayerDropItemEvent event){
    	if(MagicBook.isMagicBook(event.getItemDrop().getItemStack())){
    		event.setCancelled(true);
    		event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "This book is bound to you by magic!");
    	}
    }
    @EventHandler
    private void playerLogin(PlayerJoinEvent event)
    {
    	if(droppedBook.containsKey(event.getPlayer())){
    		//If the player had died with a magic book, give it back to them.
    		// TODO: Make sure they have room for the book
    		event.getPlayer().getInventory().addItem(droppedBook.get(event.getPlayer()));
    		event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Your magic book was dropped and found its way back to you!");
    		droppedBook.remove(event.getPlayer());
    	}
    }
    @EventHandler
    private void magicBookForcedDropped(ItemSpawnEvent event){
    	if(event.getEntityType() == EntityType.DROPPED_ITEM){
    		Item item = (Item) event.getEntity();
    		if(item != null && MagicBook.isMagicBook(item.getItemStack())){
    			MagicBook mb = new MagicBook(item.getItemStack());
    			String owner = mb.getOwner();
    			
    			if(Bukkit.getServer().getPlayerExact(owner) != null){
    				Player ply = Bukkit.getServer().getPlayer(owner);
    				// TODO: Make sure they have room for the book
    				ply.getInventory().addItem(mb);
    				ply.sendMessage(ChatColor.LIGHT_PURPLE + "Your magic book was dropped and found its way back to you!");
    			}
    			else{
    				// Give it back to them on login
    				if(Bukkit.getOfflinePlayer(owner) != null)
    					droppedBook.put(Bukkit.getOfflinePlayer(owner), mb);
    			}
    			
    			// Play a totally awesome effect
    			item.getLocation().getWorld().playEffect(item.getLocation(), Effect.ENDER_SIGNAL, 0);
				item.getLocation().getWorld().playSound(item.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
				item.remove();
    		}
    	}
    }
    @EventHandler
    private void playerChat(AsyncPlayerChatEvent event){
    	// Test the fuzzy string match with save point
    	String matchStr = "Vade ad Salvare";
    	if(Util.fuzzyStringMatch(matchStr, event.getMessage()))
    	{
    		event.getPlayer().sendMessage("Match Successful");
    		for(ItemStack item : event.getPlayer().getInventory().getContents()){
    			if(item.getType() == Material.WRITTEN_BOOK && MagicBook.isMagicBook(item)){
    				MagicBook mb = new MagicBook(item);
    				if(mb.getHandle().tag.getBoolean("Saved")){
    					event.getPlayer().teleport(Util.stringToLocation(mb.getHandle().tag.getString("SavePoint")));
    					event.setMessage(ChatColor.MAGIC + event.getMessage());
    					//TODO: This needs to be done correctly in spellbase?
    				}
    				break;
    			}
    		}
    	}
    	else
    		event.getPlayer().sendMessage("Match Failed");
    }
}
