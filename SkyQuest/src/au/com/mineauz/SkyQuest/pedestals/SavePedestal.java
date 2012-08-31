package au.com.mineauz.SkyQuest.pedestals;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.SkyQuest.MagicBook;
import au.com.mineauz.SkyQuest.Util;

public class SavePedestal extends PedestalBase{
	public SavePedestal(Location pedestalLocation){
		super(new ItemStack(Material.SANDSTONE,1,(short)3), new ItemStack(Material.BOOK_AND_QUILL), pedestalLocation);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPlayerActivatePedestal(Player player) {
		HashMap<Integer, ? extends ItemStack> books = player.getInventory().all(Material.WRITTEN_BOOK);
		
		if(!books.isEmpty())
		{
			boolean found = false;
			// Find the magic book
			for(Entry<Integer, ? extends ItemStack> ent : books.entrySet())
			{
				if(MagicBook.isMagicBook(ent.getValue()))
				{
					MagicBook book = new MagicBook(ent.getValue());
					if(!book.getHandle().tag.getBoolean("Saved")){
						book.addPage("You have uncovered the " + ChatColor.LIGHT_PURPLE + "Spell of Saviours!\n" + ChatColor.BLACK +
							"Say the following chant to be returned to this point at the cost of <XP amount> XP:\n" +
							ChatColor.LIGHT_PURPLE + "Vade ad Salvare");
						player.sendMessage("Unlocked " + ChatColor.LIGHT_PURPLE + "Spell of Saviours!");
						book.getHandle().tag.setBoolean("Saved", true);
					}
					
					book.getHandle().tag.setString("SavePoint", Util.locationToString(player.getLocation()));
					player.sendMessage("Saved your current location");
					
					player.getInventory().setItem(ent.getKey(), book);
					player.updateInventory();
					found = true;
					break;
				}
			}
			
			if(!found)
			{
				player.sendMessage(ChatColor.LIGHT_PURPLE + "You do not have a magic book to save your location!");
			}
		}
		else{
			player.sendMessage(ChatColor.LIGHT_PURPLE + "You do not have a magic book to save your location!");
		}
	}

	@Override
	protected void onPedestalDestroyed() {
		Pedestals.removePedestal(getLocation());
	}
}
