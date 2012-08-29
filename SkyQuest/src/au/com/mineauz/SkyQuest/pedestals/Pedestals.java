package au.com.mineauz.SkyQuest.pedestals;

import java.util.HashMap;

import org.bukkit.Location;

/**
 * This class keeps track of current pedestals
 * @author Schmoller
  */
public class Pedestals 
{
	/**
	 * Adds a new pedestal to the map
	 */
	public static void addPedestal(PedestalBase pedestal)
	{
		mPedestals.put(pedestal.getLocation(), pedestal);
	}
	/**
	 * Removes a pedestal from the map
	 * @return True if it was removed
	 */
	public static boolean removePedestal(Location pedestalLocation)
	{
		return (mPedestals.remove(pedestalLocation) != null);
	}
	
	// TODO: Add savePedestals() method
	// TODO: Add loadPedestals() method
	
	private static HashMap<Location, PedestalBase> mPedestals = new HashMap<Location, PedestalBase>();
}
