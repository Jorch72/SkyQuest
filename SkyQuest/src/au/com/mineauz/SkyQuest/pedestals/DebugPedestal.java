package au.com.mineauz.SkyQuest.pedestals;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.SkyQuest.MagicBook;
import au.com.mineauz.SkyQuest.spells.WarpSpell;

public class DebugPedestal extends PedestalBase
{
	static
	{
		WarpSpell.addWarpDestination("Test");
	}
	public DebugPedestal(Location pedestalLocation) 
	{
		super(new ItemStack(Material.SMOOTH_BRICK,1,(short)3), new ItemStack(Material.WRITTEN_BOOK), pedestalLocation);
	}
	/**
	 * Used for loading a pedestal from file. Do not use normally
	 */
	public DebugPedestal()
	{
		super();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPlayerActivatePedestal(Player player) 
	{
		player.sendMessage("You have activated the Debug pedestal");
		HashMap<Integer, ? extends ItemStack> books = player.getInventory().all(Material.WRITTEN_BOOK);
		
		if(books.isEmpty())
		{
			player.getInventory().addItem(new MagicBook(player.getName()));
			player.updateInventory();
			player.sendMessage("Gave you a magic book");
		}
		else
		{
			boolean found = false;
			// Find the magic book
			for(Entry<Integer, ? extends ItemStack> ent : books.entrySet())
			{
				if(MagicBook.isMagicBook(ent.getValue()))
				{
					MagicBook book = new MagicBook(ent.getValue());

					// Add the warp spell base
					WarpSpell spell = new WarpSpell();
					int subtype = WarpSpell.getWarpId("Test");
					
					if(!book.hasLearnedSpell(spell,subtype))
					{
						book.learnSpell(spell, subtype, false);
					}
					
					
					player.getInventory().setItem(ent.getKey(), book);
					player.updateInventory();
					found = true;
					break;
				}
			}
			
			if(!found)
			{
				player.getInventory().addItem(new MagicBook(player.getName()));
				player.updateInventory();
				player.sendMessage("Gave you a magic book");
			}
		}
	}

	@Override
	protected void onPedestalDestroyed() 
	{
		Pedestals.removePedestal(getLocation());
	}
}
