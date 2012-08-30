package au.com.mineauz.SkyQuest;

import java.util.Arrays;

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
		assert string1 != null;
		assert string2 != null;
		
		string1 = string1.toLowerCase();
		string2 = string2.toLowerCase();
		
		// Compute the edit distance of the 2 strings
		int editdistance = levenshteinDistance(string1, string2);
		
		if(editdistance <= 3)
		{
			SkyQuestPlugin.instance.getLogger().fine("Fuzzy Match: '" + string1 + "' == '" + string2 + "' { dist: " + editdistance + " }");
			return true;
		}
			
		SkyQuestPlugin.instance.getLogger().fine("Fuzzy Match: '" + string1 + "' != '" + string2 + "' { dist: " + editdistance + " }");
		return false;
	}
	private static int levenshteinDistance(String s, String t)
	{
		int lenS = s.length();
		int lenT = t.length();
		
		int matrixSize = (lenS + 1) * (lenT + 1);
		
		int width = lenS + 1;
		
		// Prepare the distance matrix
		Integer[] matrix = new Integer[matrixSize];
		Arrays.fill(matrix, 0);
		
		for(int i = 1; i < lenS + 1; ++i)
			matrix[i] = i;
		for(int j = 1; j < lenT + 1; ++j)
			matrix[j * width] = j;
		
		// Compute the matrix
		for(int j = 1; j < lenT+1; ++j)
		{
			for(int i = 1; i < lenS+1; ++i)
			{
				if(s.charAt(i-1) == t.charAt(j-1))
					// Same distance as before
					matrix[i + j * width] = matrix[i - 1 + (j - 1) * width];
				else
					matrix[i + j * width] = Math.min(Math.min(
							matrix[i-1 + j * width] + 1, // Deletion
							matrix[i + (j-1) * width] + 1), // Insertion
							matrix[i-1 + (j-1) * width] + 1); // Substitution
			}
		}
		
		// The bottom right corner now holds the distance
		return matrix[lenS + lenT * width];
	}
}
