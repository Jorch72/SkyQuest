package au.com.mineauz.SkyQuest.pedestals;

import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagString;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.SkyQuest.MagicBook;
import au.com.mineauz.SkyQuest.Util;

public class ItemPedestal extends PedestalBase{
	
	NBTTagList plyList;
	
	public ItemPedestal(Location pedestalLocation, ItemStack item){
		super(new ItemStack(Material.EMERALD_BLOCK), item, pedestalLocation);
		plyList = new NBTTagList();
	}
	public ItemPedestal(){
		super();
	}

	@Override
	protected void onPlayerActivatePedestal(Player player) {
		MagicBook book = Util.getMagicBookFor(player);
		boolean hasplayer = false;
		for(int i = 0; i < plyList.size(); i++){
			if(plyList.get(i).getName().equalsIgnoreCase(player.getName())){
				hasplayer = true;
			}
		}
		if(!hasplayer){
			if(book == null)
			{
				player.sendMessage(ChatColor.RED + "You need to be carrying your magic book to activate this pedestal");
				return;
			}
			else{
				player.getInventory().addItem(getItemStack());
				plyList.add(new NBTTagString(player.getName(), player.getName()));
				player.sendMessage("You have found a " + this.getItemStack().getType().toString().replace("_", " ").toLowerCase());
			}
		}
	}

	@Override
	protected void onPedestalDestroyed() {
		Pedestals.removePedestal(getLocation());
	}
	
	@Override
	public void writeToNBT(NBTTagCompound root){
		super.writeToNBT(root);
		root.set("PlyList", plyList);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound root){
		super.readFromNBT(root);
		
		plyList = root.getList("PlyList");
	}
}
