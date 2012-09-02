package au.com.mineauz.SkyQuest.spells;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import au.com.mineauz.SkyQuest.MagicBook;

public class WarpSpell extends SpellBase{

	@Override
	public String getIncantation() {
		return "Finis Stamine " /* + destinationName */; //TODO Fix up something to get the destinations from the book. 
	}

	@Override
	public String getName() {
		return "Spell of Ender Warp";
	}

	@Override
	public String getDescription() {
		return "Say " + ChatColor.LIGHT_PURPLE + "Finis Stamine" + ChatColor.BLACK + " and a destination below to be warped.\n";
	}

	@Override
	public int getExpCost() {
		return 0;
	}

	@Override
	public boolean onActivate(MagicBook book, Player forPlayer) {
		// TODO Go to predefined warp point
		return false;
	}

	@Override
	public void onLearn(MagicBook book, Player forPlayer) {
		// TODO Add tag to book (custom warp destination possible?)
		
	}

}
