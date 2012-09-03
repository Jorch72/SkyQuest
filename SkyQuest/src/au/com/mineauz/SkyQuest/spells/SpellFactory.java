package au.com.mineauz.SkyQuest.spells;

import java.util.HashMap;

import au.com.mineauz.SkyQuest.SkyQuestPlugin;

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
		{
			SkyQuestPlugin.instance.getLogger().warning("Invalid spell type: " + name);
			return null;
		}
			
			
		
		try 
		{
			return sRegisteredSpells.get(name.toLowerCase()).newInstance();
		} 
		catch (InstantiationException | IllegalAccessException e) 
		{
			SkyQuestPlugin.instance.getLogger().warning("Invalid spell type: " + name);
			return null;
		}
	}
	
	/**
	 * Gets the type name of a registered spell
	 */
	public static String getSpellType(Class<? extends SpellBase> classType)
	{
		return sRegisteredSpellsReverse.get(classType);
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
		sRegisteredSpellsReverse.put(classType, name.toLowerCase());
		
		return true;
	}
	
	private static HashMap<String, Class<? extends SpellBase>> sRegisteredSpells = new HashMap<>();
	private static HashMap<Class<? extends SpellBase>, String> sRegisteredSpellsReverse = new HashMap<>();
	
	static
	{
		registerSpellName("save", SavePointSpell.class);
		registerSpellName("warp", WarpSpell.class);
	}
}
