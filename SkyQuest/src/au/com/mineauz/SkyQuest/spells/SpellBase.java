package au.com.mineauz.SkyQuest.spells;

/**
 * A base class for all spells
 * @author Schmoller
 */
public abstract class SpellBase 
{
	protected SpellBase(String incantation)
	{
		mIncantation = incantation;
	}
	/**
	 * Gets the incantation required to be said to activate this spell
	 * @return
	 */
	public String getIncantation()
	{
		return mIncantation;
	}
	
	/**
	 * When overridden, activates the spell applying any effects it has
	 * @return True if it was activated
	 */
	public abstract boolean activate();
	
	private String mIncantation;
}
