package au.com.mineauz.SkyQuest;

import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class MagicBook extends Book
{
	public MagicBook()
	{
		setTitle("The Sky Quest");
		setAuthor("Unknown");
		
		addPage("Something something dark side");
		
		getHandle().tag.setBoolean("MagicBook", true);
	}
	public MagicBook(ItemStack item)
	{
		super(item);
		getHandle().tag.setBoolean("MagicBook", true);
		
		
	}
	
	public void addTestPage()
	{
		addPage("Test page");
	}
	
	public static boolean isMagicBook(ItemStack stack)
	{
		if(stack.getType() != Material.WRITTEN_BOOK)
			return false;
		
		if(!(stack instanceof CraftItemStack))
			return false;
		
		if(((CraftItemStack)stack).getHandle().tag == null)
			return false;
		
		if(((CraftItemStack)stack).getHandle().tag.getBoolean("MagicBook"))
			return true;
		
		return false;
	}
}
