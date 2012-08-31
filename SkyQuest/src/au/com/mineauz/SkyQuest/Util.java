package au.com.mineauz.SkyQuest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import net.minecraft.server.NBTTagCompound;

import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
		
		// Allow 1 error per 10 chars
		int maxError = (int)Math.ceil((string1.length() + string2.length()) / 20D);
		
		// Compute the edit distance of the 2 strings
		int editdistance = levenshteinDistance(string1, string2);
		
		if(editdistance <= maxError)
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
	
	/**
	 * Converts a location into a string for data storage.
	 * @param loc - The location that needs to be converted
	 * @return A string form of the location
	 */
	public static String locationToString(Location loc){
		return loc.getWorld().getName() + "&" + loc.getX() + "&" + loc.getY() + "&" + loc.getZ() + "&" + loc.getYaw() + "&" + loc.getPitch();
	}
	
	/**
	 * Converts a location string back into a location object
	 * @param str - The string that needs to be converted back
	 * @return The Location
	 */
	public static Location stringToLocation(String str){
		if(str.contains("&")){
			String[] strarray = str.split("&");
			if(strarray.length == 6){
				World world = Bukkit.getWorld(strarray[0]);
				double x = Double.parseDouble(strarray[1]);
				double y = Double.parseDouble(strarray[2]);
				double z = Double.parseDouble(strarray[3]);
				float yaw = Float.parseFloat(strarray[4]);
				float pitch = Float.parseFloat(strarray[5]);
				
				return new Location(world, x, y, z, yaw, pitch);
			}
		}
		return null;
	}
	public static void writeItemStackToNBT(ItemStack item, NBTTagCompound root)
	{
		root.setInt("ID", item.getTypeId());
		root.setShort("Dmg", item.getDurability());
		root.setInt("Count", item.getAmount());
		
		if(item instanceof CraftItemStack)
		{
			if(((CraftItemStack)item).getHandle().tag != null)
				root.set("tag", ((CraftItemStack)item).getHandle().tag);
		}
		else if(!item.getEnchantments().isEmpty())
		{
			CraftItemStack temp = new CraftItemStack(item);
			root.set("tag", ((CraftItemStack)temp).getHandle().tag);
		}
	}
	
	public static ItemStack readItemStackFromNBT(NBTTagCompound root)
	{
		CraftItemStack item = new CraftItemStack(root.getInt("ID"));
		item.setDurability(root.getShort("Dmg"));
		item.setAmount(root.getInt("Count"));
		
		if(root.hasKey("tag"))
			item.getHandle().tag = (NBTTagCompound)root.get("tag");
		
		return item;
	}
	
	/**
	 * Gets a players magic book if they are carrying it
	 */
	public static MagicBook getMagicBookFor(Player player)
	{
		HashMap<Integer, ? extends ItemStack> books = player.getInventory().all(Material.WRITTEN_BOOK);
		for(Entry<Integer, ? extends ItemStack> ent : books.entrySet())
		{
			if(MagicBook.isMagicBook(ent.getValue()))
			{
				// Make sure it is theirs
				MagicBook mb = new MagicBook(ent.getValue());
				if(mb.getOwner().equalsIgnoreCase(player.getName()))
				{
					return mb; 
				}
			}
		}
		
		return null;
	}
}
