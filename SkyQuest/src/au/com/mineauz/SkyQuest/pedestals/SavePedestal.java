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
import au.com.mineauz.SkyQuest.spells.SavePointSpell;

public class SavePedestal extends PedestalBase{
	public SavePedestal(Location pedestalLocation){
		super(new ItemStack(Material.SANDSTONE,1,(short)2), new ItemStack(Material.BOOK_AND_QUILL), pedestalLocation);
	}
	public SavePedestal(){
		super();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPlayerActivatePedestal(Player player) {
		HashMap<Integer, ? extends ItemStack> books = player.getInventory().all(Material.WRITTEN_BOOK);
		
		boolean found = false;
		// Find the magic book
		for(Entry<Integer, ? extends ItemStack> ent : books.entrySet())
		{
			if(MagicBook.isMagicBook(ent.getValue()))
			{
				MagicBook book = new MagicBook(ent.getValue());
				SavePointSpell spell = new SavePointSpell();
				if(!book.hasLearnedSpell(spell))
				{
					book.learnSpell(spell, false);
				}
				else
				{
					book.getHandle().tag.setString("SavePoint", Util.locationToString(player.getLocation()));
					player.sendMessage("Saved your current location");
				}
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

	@Override
	protected void onPedestalDestroyed() {
		Pedestals.removePedestal(getLocation());
	}
}
