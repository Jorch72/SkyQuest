package au.com.mineauz.SkyQuest;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagString;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.SkyQuest.spells.SpellBase;
import au.com.mineauz.SkyQuest.spells.SpellFactory;

public class MagicBook extends Book
{
	public MagicBook(String owner)
	{
		setTitle("The Sky Quest");
		setAuthor("Unknown");
		
		addPage("Something something dark side");
		
		getHandle().tag.setBoolean("MagicBook", true);
		setOwner(owner);
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
	
	public void setOwner(String player){
		getHandle().tag.setString("Owner", player);
	}
	
	public String getOwner(){
		return getHandle().tag.getString("Owner");
	}
	
	/**
	 * Teaches this magic book a spell
	 */
	public void learnSpell(SpellBase spell, boolean silent)
	{
		NBTTagList spells = getHandle().tag.getList("Spells");
		
		if(getHandle().tag.getList("Spells") == null)
			spells = new NBTTagList();
		
		// Add the spell to the list
		spells.add(new NBTTagString(SpellFactory.getSpellType(spell.getClass()), SpellFactory.getSpellType(spell.getClass())));
		
		getHandle().tag.set("Spells", spells);
		
		// Notify them
		Player player = Bukkit.getPlayerExact(getOwner());
		if(player == null)
			return;

		if(!silent)
		{
			// Add a new page in the book
			addPage("You have uncovered the " + ChatColor.LIGHT_PURPLE + spell.getName() + ChatColor.BLACK + "!\n" +  spell.getDescription());
			player.sendMessage("Unlocked " + ChatColor.LIGHT_PURPLE + spell.getName() + ChatColor.WHITE + "!");
		}
		
		spell.onLearn(this, player);
	}
	
	/**
	 * Checks if this book has learned a spell
	 */
	public boolean hasLearnedSpell(SpellBase spell)
	{
		NBTTagList spells = getHandle().tag.getList("Spells");
		
		if(spells == null)
			return false;
		
		String spellType = SpellFactory.getSpellType(spell.getClass());
		for(int i = 0; i < spells.size(); i++)
		{
			if(((NBTTagString)spells.get(i)).data.equalsIgnoreCase(spellType))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Gets a list of all the spells this book has learned
	 */
	public List<SpellBase> getLearnedSpells()
	{
		NBTTagList spellNames = getHandle().tag.getList("Spells");
		
		if(spellNames == null)
			return new ArrayList<SpellBase>();
		
		
		// Make the list of spells
		ArrayList<SpellBase> spells = new ArrayList<SpellBase>(spellNames.size());
		for(int i = 0; i < spellNames.size(); i++)
		{
			SpellBase spell = SpellFactory.getSpell(((NBTTagString)spellNames.get(i)).data);
			if(spell != null)
			{
				spells.add(spell);
			}
		}
		
		return spells;
	}
}
