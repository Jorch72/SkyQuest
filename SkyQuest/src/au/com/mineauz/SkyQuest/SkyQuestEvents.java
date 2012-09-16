package au.com.mineauz.SkyQuest;

import java.util.HashMap;
import java.util.List;
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
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.SkyQuest.pedestals.DebugPedestal;
import au.com.mineauz.SkyQuest.pedestals.Pedestals;
import au.com.mineauz.SkyQuest.spells.SpellBase;

public class SkyQuestEvents implements Listener{
	
	private Map<OfflinePlayer, ItemStack> droppedBook = new HashMap<OfflinePlayer, ItemStack>();
    
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
    		Player ply = event.getPlayer();
    		ItemStack mb = droppedBook.get(event.getPlayer());
    		//event.getPlayer().getInventory().addItem(mb);
    		if(!ply.getInventory().addItem(mb).isEmpty()){
				int rand = (int) Math.round(Math.random() * (ply.getInventory().getSize() - 1));
				ItemStack itemst = ply.getInventory().getItem(rand);
				ply.getInventory().setItem(rand, mb);
				ply.getWorld().dropItem(ply.getLocation(), itemst);
			}
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
    				if(!ply.getInventory().addItem(mb).isEmpty()){
    					int rand = (int) Math.round(Math.random() * (ply.getInventory().getSize() - 1));
    					ItemStack itemst = ply.getInventory().getItem(rand);
    					ply.getInventory().setItem(rand, mb);
    					ply.getWorld().dropItem(ply.getLocation(), itemst);
    				}
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
    private void playerChat(AsyncPlayerChatEvent event)
    {
    	MagicBook book = Util.getMagicBookFor(event.getPlayer());
    	
    	if(book == null)
    		return;
    	
    	List<SpellBase> knownSpells = book.getLearnedSpells();
    	
    	for(SpellBase spell : knownSpells)
    	{
    		if(spell.hasSubTypes())
    		{
    			List<Integer> knownSubTypes = book.getLearnedSpellSubTypes(spell);
    			for(Integer subtype : knownSubTypes)
    			{
    				if(spell.canCastSpell(event.getPlayer(),subtype) && Util.fuzzyStringMatch(spell.getIncantation(subtype), event.getMessage()))
    	    		{
    	    			spell.onActivate(book, event.getPlayer(), subtype);
    	    			event.setMessage(ChatColor.MAGIC + event.getMessage());
    	    			break;
    	    		}
    			}
    		}
    		else
    		{
	    		if(spell.canCastSpell(event.getPlayer(),-1) && Util.fuzzyStringMatch(spell.getIncantation(-1), event.getMessage()))
	    		{
	    			spell.onActivate(book, event.getPlayer());
	    			event.setMessage(ChatColor.MAGIC + event.getMessage());
	    			break;
	    		}
    		}
    	}
    }
    
    @EventHandler
    private void playerTeleport(PlayerTeleportEvent event){
    	if(event.getCause() == TeleportCause.ENDER_PEARL || event.getCause() == TeleportCause.COMMAND){
    		if(event.getTo().getWorld().getName().equalsIgnoreCase("skylands")){
    			event.setCancelled(true);
    			//TODO Temporary fix to teleporting issues that may arise via other plugins. For example people setting home in the skylands.
    			//Might recommend a variable that allows players to teleport under certain circumstances if required
    		}
    	}
    }
}
