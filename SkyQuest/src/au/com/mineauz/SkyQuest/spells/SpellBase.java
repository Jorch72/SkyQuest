package au.com.mineauz.SkyQuest.spells;

import java.util.ArrayList;
import java.util.List;

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
	 * When true, there are sub types of spells
	 */
	public abstract boolean hasSubTypes();
	
	/**
	 * Gets the incantation required to be said to activate this spell
	 */
	public abstract String getIncantation(int subtype);
	
	/**
	 * Gets the name of the spell
	 */
	public abstract String getName();
	
	/**
	 * Gets the name of a sub type
	 */
	public abstract String getNameSubtype(int subtype);
	
	/**
	 * Gets the string presented when the subtype is unlocked.
	 */
	public abstract String getUnlockString(int subtype);
	/**
	 * Gets a brief description of the spell
	 * @return
	 */
	public abstract String getDescription();
	
	/**
	 * Gets the experience levels cost of casting the spell
	 */
	public abstract int getExpCost(int subtype);
	
	/**
	 * Checks if the player can cast this spell. By default only their experience level is checked.
	 * Override this to add other checks
	 */
	public boolean canCastSpell(Player player, int subtype)
	{
		if (player.getLevel() < getExpCost(subtype))
			return false;
		
		MagicBook book = Util.getMagicBookFor(player); 
		if(book == null)
			return false;
		
		return book.hasLearnedSpell(this, subtype);
	}
	/**
	 * When overridden, activates the spell applying any effects it has
	 * @param subtype The subtype being activated. The meaning of this depends on the spell
	 * @return True if it was activated
	 */
	public boolean onActivate(MagicBook book, Player forPlayer, int subtype)
	{
		forPlayer.setLevel(forPlayer.getLevel() - getExpCost(subtype));
		return true;
	}
	/**
	 * When overridden, activates the spell applying any effects it has
	 * @return True if it was activated
	 */
	public boolean onActivate(MagicBook book, Player forPlayer)
	{
		return onActivate(book,forPlayer,-1);
	}
	
	/**
	 * When overridden, makes the needed changes to learn the spell
	 * @param book The players Magic book
	 * @param forPlayer The player learning the spell
	 */
	public abstract void onLearn(MagicBook book, Player forPlayer);
	
	/**
	 * When overridden, makes the needed changes to learn the sub type of spell. They should have already learnt the base spell with onLearn()
	 * @param book The players Magic Book
	 * @param forPlayer The player learning the sub type of the spell
	 * @param subtype The subtype being learnt. The meaning of this depends on the spell
	 */
	public abstract void onLearnSubType(MagicBook book, Player forPlayer, int subtype);
	
	/**
	 * Gets a list of all subtypes available.
	 * By default an empty list is returned
	 */
	public List<Integer> getSubTypes()
	{
		return new ArrayList<Integer>();
	}
}
