package au.com.mineauz.SkyQuest;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.SkyQuest.pedestals.DebugPedestal;
import au.com.mineauz.SkyQuest.pedestals.Pedestals;

public class SkyQuestEvents implements Listener{
	
	private Map<Player, ItemStack> droppedBook = new HashMap<Player, ItemStack>(); //TODO: Probably move this to a Data class.
    
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
}
