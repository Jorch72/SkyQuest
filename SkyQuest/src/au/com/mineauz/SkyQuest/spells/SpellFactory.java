package au.com.mineauz.SkyQuest.spells;

import java.util.HashMap;

/**
 * A factory for creating spell instances by type name.
 * Spells should be statically registered with this factory using registerSpellName()
 * @author Schmoller
  */
public class SpellFactory 
{
	/**
	 * Gets an instance of a registered spell by name
	 * @param name The case insensitive name of the spell
	 * @return An instance of the spell if there was one with that name, null otherwise
	 */
	public static SpellBase getSpell(String name)
	{
		if(!sRegisteredSpells.containsKey(name.toLowerCase()))
			return null;
		
		try 
		{
			return sRegisteredSpells.get(name.toLowerCase()).newInstance();
		} 
		catch (InstantiationException | IllegalAccessException e) 
		{
			return null;
		}
	}
	
	/**
	 * Registers a spell type to the name so it can be used with getSpell()
	 * @param name The name to give the spell
	 * @param classType The class of the spell
	 * @return True if the registration was successful
	 */
	public static boolean registerSpellName(String name, Class<? extends SpellBase> classType)
	{
		if(sRegisteredSpells.containsKey(name.toLowerCase()))
			return false;
		
		sRegisteredSpells.put(name.toLowerCase(), classType);
		
		return true;
	}
	
	private static HashMap<String, Class<? extends SpellBase>> sRegisteredSpells = new HashMap<>();
}
