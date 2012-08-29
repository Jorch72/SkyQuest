package au.com.mineauz.SkyQuest;

public class Util 
{
	/**
	 * Checks if the strings are approximately the same
	 * @param string1 The first string to compare
	 * @param string2 The second string to compare
	 * @return True if they match
	 */
	public static boolean fuzzyStringMatch(String string1, String string2)
	{
		// TODO: Fuzzy String Match
		return string1.compareToIgnoreCase(string2) == 0;
	}
}
