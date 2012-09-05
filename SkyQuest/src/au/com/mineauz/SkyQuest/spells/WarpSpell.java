package au.com.mineauz.SkyQuest.spells;

import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagString;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import au.com.mineauz.SkyQuest.MagicBook;

public class WarpSpell extends SpellBase{
	
	private String warpDest = null;
	
	public WarpSpell(){}
	
	public WarpSpell(String destinationName){
		warpDest = destinationName;
	}

	@Override
	public String getIncantation() {
		return "Finis Stamine " + warpDest;
	}

	@Override
	public String getName() {
		return "Spell of Ender Warp";
	}

	@Override
	public String getDescription() {
		return "Say " + ChatColor.LIGHT_PURPLE + "Finis Stamine" + ChatColor.BLACK + 
				" and a destination below to be warped at the cost of " + getExpCost() + " XP.\n";
	}

	@Override
	public int getExpCost() {
		return 8;
	}

	@Override
	public boolean onActivate(MagicBook book, Player forPlayer) {
		// TODO Go to predefined warp point
		forPlayer.sendMessage("Warping to " + warpDest);
		return true;
	}

	@Override
	public void onLearn(MagicBook book, Player forPlayer) {
		NBTTagList warps = book.getHandle().tag.getList("WarpDests");
		
		if(book.getHandle().tag.getList("WarpDests") == null)
			warps = new NBTTagList();
		
		warps.add(new NBTTagString(warpDest, warpDest));
		
		book.getHandle().tag.set("WarpDests", warps);
		
		forPlayer.sendMessage("Added " + warpDest + " to warp destinations.");
	}

}
