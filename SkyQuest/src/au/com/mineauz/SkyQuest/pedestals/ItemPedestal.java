package au.com.mineauz.SkyQuest.pedestals;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.SkyQuest.MagicBook;
import au.com.mineauz.SkyQuest.Util;

public class ItemPedestal extends PedestalBase{
	
	public ItemPedestal(Location pedestalLocation, ItemStack item){
		super(new ItemStack(Material.EMERALD_BLOCK), item, pedestalLocation);
	}
	public ItemPedestal(){
		super();
	}

	@Override
	protected void onPlayerActivatePedestal(Player player) {
		MagicBook book = Util.getMagicBookFor(player);
		if(book == null)
		{
			player.sendMessage(ChatColor.RED + "You need to be carrying your magic book to activate this pedestal");
			return;
		}
		else{
			player.getInventory().addItem(getItemStack());
		}
	}

	@Override
	protected void onPedestalDestroyed() {
		Pedestals.removePedestal(getLocation());
	}

}
