package au.com.mineauz.SkyQuest.quests;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;


public class QuestFactory 
{
	/**
	 * Adds a new quest
	 * @param questName The type name of the quest
	 * @param quest The filled quest object
	 * @return True if it was added, false if a quest by that name already existed
	 */
	public static boolean addQuest(String questName, Quest quest)
	{
		if(mQuests.containsKey(questName.toLowerCase()))
			return false;
		
		quest.QuestID = questName.toLowerCase();
		mQuests.put(questName.toLowerCase(), quest);
		
		return true;
	}
	
	/**
	 * Gets a Quest by name
	 * @return null if there isnt a quest by that name. otherwise the quest object is returned
	 */
	public static Quest getQuest(String name)
	{
		return mQuests.get(name.toLowerCase());
	}
	
	/**
	 * Removes a quest from the list
	 * @return True if it was removed, false if there was no quest by that name.
	 */
	public static boolean removeQuest(String name)
	{
		return mQuests.remove(name.toLowerCase()) != null;
	}
	
	public static void readFromNBT(NBTTagCompound root)
	{
		NBTTagList quests = root.getList("Quests");
		mQuests.clear();

		if(quests == null)
			return;
		
		for(int i = 0; i < quests.size(); i++)
		{
			NBTTagCompound tag = (NBTTagCompound)quests.get(i);
			
			String name = tag.getString("typename");
			Quest quest = new Quest();
			quest.readFromNBT(tag);
			quest.QuestID = name; 
			mQuests.put(name,quest);
		}
	}
	
	public static void writeToNBT(NBTTagCompound root)
	{
		NBTTagList quests = new NBTTagList();
		for(Entry<String, Quest> ent : mQuests.entrySet())
		{
			NBTTagCompound tag = new NBTTagCompound();
			ent.getValue().writeToNBT(tag);
			tag.setString("typename", ent.getKey());
			quests.add(tag);
		}
		
		root.set("Quests", quests);
		
	}
	
	private static HashMap<String, Quest> mQuests = new HashMap<String, Quest>();
}
