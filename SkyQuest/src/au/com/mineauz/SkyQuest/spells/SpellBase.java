package au.com.mineauz.SkyQuest.spells;

import org.bukkit.entity.Player;

import au.com.mineauz.SkyQuest.MagicBook;
import au.com.mineauz.SkyQuest.Util;

/**
 * A base class for all spells
 * @author Schmoller
 */
public abstract class SpellBase 
{
	protected SpellBase()
	{
	}
	/**
	 * Gets the incantation required to be said to activate this spell
	 */
	public abstract String getIncantation();
	
	/**
	 * Gets the name of the spell
	 */
	public abstract String getName();
	
	/**
	 * Gets a brief description of the spell
	 * @return
	 */
	public abstract String getDescription();
	
	/**
	 * Gets the experience levels cost of casting the spell
	 */
	public abstract int getExpCost();
	
	
			

	/**
	 * Checks if the player can cast this spell. By default only their experience level is checked.
	 * Override this to add other checks
	 */
	public boolean canCastSpell(Player player)
	{
		if (player.getLevel() < getExpCost())
			return false;
		
		MagicBook book = Util.getMagicBookFor(player); 
		if(book == null)
			return false;
		
		return book.hasLearnedSpell(this);
	}
	/**
	 * When overridden, activates the spell applying any effects it has
	 * @return True if it was activated
	 */
	public abstract boolean onActivate(MagicBook book, Player forPlayer);
	
	/**
	 * When overridden, makes the needed changes to learn the spell
	 * @param book The players Magic book
	 * @param forPlayer The player learning the spell
	 */
	public abstract void onLearn(MagicBook book, Player forPlayer);
}
