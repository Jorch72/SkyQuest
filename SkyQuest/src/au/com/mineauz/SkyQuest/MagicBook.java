package au.com.mineauz.SkyQuest;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagInt;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagString;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.SkyQuest.quests.Quest;
import au.com.mineauz.SkyQuest.quests.QuestFactory;
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
	public void learnSpell(SpellBase spell, int subtype, boolean silent)
	{
		NBTTagList spells = getHandle().tag.getList("Spells");
		
		if(getHandle().tag.getList("Spells") == null)
			spells = new NBTTagList();
		
		boolean learnedWholeSpell = false;
		
		// Find it
		NBTTagCompound tag = null;
		String spellType = SpellFactory.getSpellType(spell.getClass());
		for(int i = 0; i < spells.size(); i++)
		{
			if(((NBTTagCompound)spells.get(i)).getString("Type").equalsIgnoreCase(spellType))
			{
				tag = (NBTTagCompound)spells.get(i);
				break;
			}
		}
	
		// Make sure there is a list
		if(tag == null)
		{
			tag = new NBTTagCompound();
			tag.setString("Type", spellType);
			tag.set("Sub", new NBTTagList());
			spells.add(tag);
			learnedWholeSpell = true;
		}
		
		if(subtype != -1 && spell.hasSubTypes())
		{
			// Add onto the list
			tag.getList("Sub").add(new NBTTagInt("" + subtype, subtype));
		}
		
		getHandle().tag.set("Spells", spells);
		
		// Notify them
		Player player = Bukkit.getPlayerExact(getOwner());
		if(player == null)
			return;

		if(!silent)
		{
			// Add a new page in the book
			if(learnedWholeSpell)
			{
				addPage("You have uncovered the " + ChatColor.LIGHT_PURPLE + spell.getName() + ChatColor.BLACK + "!\n" +  spell.getDescription());
				player.sendMessage("Unlocked " + ChatColor.LIGHT_PURPLE + spell.getName() + ChatColor.WHITE + "!");
			}
			
			if(subtype != -1 && spell.hasSubTypes())
			{
				player.sendMessage(spell.getUnlockString(subtype));
			}
		}
		
		if(learnedWholeSpell)
			spell.onLearn(this, player);
		else if(subtype != -1 && spell.hasSubTypes())
			spell.onLearnSubType(this, player, subtype);
	}
	
	/**
	 * Checks if this book has learned a spell
	 * @param subtype 
	 */
	public boolean hasLearnedSpell(SpellBase spell, int subtype)
	{
		NBTTagList spells = getHandle().tag.getList("Spells");
		
		if(spells == null)
			return false;
		
		String spellType = SpellFactory.getSpellType(spell.getClass());
		for(int i = 0; i < spells.size(); i++)
		{
			NBTTagCompound tag = (NBTTagCompound)spells.get(i);
			
			if(tag.getString("Type").equalsIgnoreCase(spellType))
			{
				// Subtype -1 means any subtype
				if(subtype == -1 || !spell.hasSubTypes())
					return true;
				
				// The type matches, but does its subtype match
				NBTTagList subTypes = tag.getList("Sub");
				for(int j = 0; j < subTypes.size(); j++)
				{
					if(((NBTTagInt)subTypes.get(j)).data == subtype)
						return true;
				}
			}
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
			 SpellBase spell = SpellFactory.getSpell(((NBTTagCompound)spellNames.get(i)).getString("Type"));
			
			if(spell != null)
			{
				spells.add(spell);
			}
		}
		
		return spells;
	}
	public List<Integer> getLearnedSpellSubTypes(SpellBase spell)
	{
		NBTTagList spellNames = getHandle().tag.getList("Spells");
		
		if(spellNames == null || !spell.hasSubTypes())
			return new ArrayList<Integer>();
		
		
		String spellType = SpellFactory.getSpellType(spell.getClass());

		// Find the spell
		for(int i = 0; i < spellNames.size(); i++)
		{
			NBTTagCompound data = (NBTTagCompound)spellNames.get(i);
			if(data.getString("Type").equalsIgnoreCase(spellType))
			{
				// Build the list of subtypes
				NBTTagList subTypes = data.getList("Sub");
				ArrayList<Integer> results = new ArrayList<Integer>(subTypes.size());
				
				for(int j = 0; j < subTypes.size(); j++)
				{
					results.add(((NBTTagInt)subTypes.get(j)).data);
				}
				
				return results;
			}
		}
		
		return new ArrayList<Integer>();
	}
	
	public void markQuestComplete(Quest quest)
	{
		NBTTagList quests = getHandle().tag.getList("Quests");
		
		if(quests == null)
			quests = new NBTTagList();
		
		quests.add(new NBTTagString(quest.QuestID,quest.QuestID));
		
		getHandle().tag.set("Quests", quests);
	}
	public boolean hasCompletedQuest(Quest quest)
	{
		NBTTagList quests = getHandle().tag.getList("Quests");
		
		if(quests == null)
			return false;
		
		for(int i = 0; i < quests.size(); i++)
		{
			if(((NBTTagString)quests.get(i)).data.equalsIgnoreCase(quest.QuestID))
				return true;
		}
		
		return false;
	}
	public List<Quest> getCompletedQuests()
	{
		NBTTagList quests = getHandle().tag.getList("Quests");
		
		if(quests == null)
			return new ArrayList<Quest>();
		
		ArrayList<Quest> list = new ArrayList<Quest>();
		
		for(int i = 0; i < quests.size(); i++)
		{
			list.add(QuestFactory.getQuest(((NBTTagString)quests.get(i)).data));
		}
		
		return list;
	}
	
	public void addQuestInProgress(Quest quest){
		NBTTagList quests = getHandle().tag.getList("QuestsInProgress");
		
		if(quests == null)
			quests = new NBTTagList();
		
		if(!hasQuestInProgress(quest)){
			quests.add(new NBTTagString(quest.QuestID,quest.QuestID));
			getHandle().tag.set("QuestsInProgress", quests);
		}
	}
	
	public void removeQuestInProgress(Quest quest){
		NBTTagList quests = getHandle().tag.getList("QuestsInProgress");
		
		if(quests == null)
			return;
		
		if(hasQuestInProgress(quest)){
			NBTTagList questsNew = new NBTTagList();
			for(int i = 0; i < quests.size(); i++){
				if(!((NBTTagString) quests.get(i)).data.equalsIgnoreCase(quest.QuestID)){
					questsNew.add((NBTTagString) quests.get(i));
				}
				getHandle().tag.set("QuestsInProgress", questsNew);
			}
		}
	}
	
	public boolean hasQuestInProgress(Quest quest){
		NBTTagList quests = getHandle().tag.getList("QuestsInProgress");
		
		if(quests == null){
			return false;
		}
		
		for(int i = 0; i < quests.size(); i++){
			if(((NBTTagString) quests.get(i)).data.equalsIgnoreCase(quest.QuestID))
				return true;
		}
		
		return false;
	}
			
}
