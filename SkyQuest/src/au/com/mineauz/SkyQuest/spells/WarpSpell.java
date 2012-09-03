package au.com.mineauz.SkyQuest.spells;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import au.com.mineauz.SkyQuest.MagicBook;
import au.com.mineauz.SkyQuest.Util;

public class WarpSpell extends SpellBase{
	
	private String warpDest = null;
	
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
		return 0;
	}

	@Override
	public boolean onActivate(MagicBook book, Player forPlayer) {
		// TODO Go to predefined warp point
		return true;
	}

	@Override
	public void onLearn(MagicBook book, Player forPlayer) {
		List<String> list = new ArrayList<String>();
		if(book.getHandle().tag.getBoolean("WarpSpell")){
			list = Util.stringToList(book.getHandle().tag.getString("DestList"));
		}
		else{
			forPlayer.sendMessage("You learned The Spell of Ender Warp");
			book.getHandle().tag.setBoolean("WarpSpell", true);
		}
		
		if(!list.contains(warpDest)){
			list.add(warpDest);
			String dests = Util.listToString(list);
			book.getHandle().tag.setString("DestList", dests);

			forPlayer.sendMessage("Added the " + warpDest + " destination to The Spell of Ender Warp");
		}
	}

}
