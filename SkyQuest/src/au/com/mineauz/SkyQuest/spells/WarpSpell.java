package au.com.mineauz.SkyQuest.spells;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import au.com.mineauz.SkyQuest.MagicBook;

public class WarpSpell extends SpellBase
{
	private static HashMap<Integer,String> mDestinations = new HashMap<Integer,String>();
	private static HashMap<String, Integer> mDestinationsReverse = new HashMap<String, Integer>();
	private static int mNextId = 0;
	
	public static void addWarpDestination(String destination)
	{
		mDestinations.put(mNextId, destination);
		mDestinationsReverse.put(destination, mNextId);
		mNextId++;
	}
	public static int getWarpId(String destination)
	{
		return mDestinationsReverse.get(destination);
	}
	
	public WarpSpell(){}

	@Override
	public String getIncantation(int subType) 
	{
		if(subType == -1)
			return "Finis Stamine";
		
		return "Finis Stamine " + mDestinations.get(subType);
	}

	@Override
	public String getName() {
		return "Spell of Ender Warp";
	}

	@Override
	public String getDescription() {
		return "Say " + ChatColor.LIGHT_PURPLE + "Finis Stamine" + ChatColor.BLACK + 
				" and a destination below to be warped at the cost of " + getExpCost(-1) + " XP.\n";
	}

	@Override
	public int getExpCost(int subType) {
		return 4;
	}

	@Override
	public boolean onActivate(MagicBook book, Player forPlayer, int subtype) {
		super.onActivate(book, forPlayer, subtype);
		// TODO Go to predefined warp point
		forPlayer.sendMessage("Warping to " + mDestinations.get(subtype));
		return true;
	}

	@Override
	public void onLearn(MagicBook book, Player forPlayer) {
	}

	@Override
	public boolean hasSubTypes() 
	{
		return true;
	}

	@Override
	public String getNameSubtype(int subtype) 
	{
		if(subtype == -1)
			return getName();
		return mDestinations.get(subtype);
	}

	@Override
	public String getUnlockString(int subtype) 
	{
		return "You can now warp to " + ChatColor.LIGHT_PURPLE + mDestinations.get(subtype) + ChatColor.RESET + "\nSay " + ChatColor.LIGHT_PURPLE + getIncantation(subtype) + ChatColor.RESET + " to activate it";
	}

	@Override
	public void onLearnSubType(MagicBook book, Player forPlayer, int subtype) 
	{
	}
}
