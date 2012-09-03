package au.com.mineauz.SkyQuest.spells;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import au.com.mineauz.SkyQuest.MagicBook;
import au.com.mineauz.SkyQuest.Util;

public class SavePointSpell extends SpellBase
{
	public SavePointSpell() 
	{
	}

	@Override
	public boolean onActivate(MagicBook book, Player forPlayer) 
	{
		forPlayer.getWorld().playEffect(forPlayer.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
		forPlayer.getWorld().playSound(forPlayer.getLocation(), Sound.FIRE_IGNITE, 10, 1);
		forPlayer.teleport(Util.stringToLocation(book.getHandle().tag.getString("SavePoint")));
		forPlayer.getWorld().playEffect(forPlayer.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
		forPlayer.getWorld().playSound(forPlayer.getLocation(), Sound.FIRE_IGNITE, 10, 1);
		return true;
	}
	
	@Override
	public void onLearn(MagicBook book, Player forPlayer) 
	{
		book.getHandle().tag.setString("SavePoint", Util.locationToString(forPlayer.getLocation()));
		forPlayer.sendMessage("Saved your current location");
	}

	@Override
	public String getIncantation() 
	{
		return "Vade ad Salvare";
	}

	@Override
	public String getName() 
	{
		return "Spell of Saviours"; 
	}

	@Override
	public int getExpCost() 
	{
		return 0;
	}

	@Override
	public String getDescription() 
	{
		return "Say the following chant to be returned to this point at the cost of " + getExpCost() + " XP:\n" +
				ChatColor.LIGHT_PURPLE + getIncantation();
	}
	
}
