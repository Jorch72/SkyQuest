package au.com.mineauz.SkyQuest.pedestals;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.SkyQuest.Book;
import au.com.mineauz.SkyQuest.SkyQuestPlugin;
import au.com.mineauz.SkyQuest.quests.Quest;
import au.com.mineauz.SkyQuest.quests.QuestFactory;

public class BlankQuestPedestal extends PedestalBase{
	
	public BlankQuestPedestal(Location location){
		super(new ItemStack(Material.SMOOTH_BRICK, 1, (short)3), new ItemStack(Material.PAPER), location);
	}
	
	public BlankQuestPedestal(){
		super();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPlayerActivatePedestal(Player player) {
		if(player.getItemInHand().getType() == Material.BOOK_AND_QUILL){
			Book questBook = new Book(player.getItemInHand());
			boolean successful = false;
			Quest quest = null;
			String problem = "";
			try{
				quest = new Quest(questBook);
				successful = true;
			}
			catch(IllegalArgumentException e){
				problem = e.getMessage();
				successful = false;
			}
			
			if(successful){
				quest.QuestID = Quest.makeSafeQuestName(quest.getQuestName()); 
				if(!QuestFactory.addQuest(quest.QuestID, quest))
				{
					Quest existingQuest = QuestFactory.getQuest(quest.QuestID);
					if(existingQuest == null)
					{
						player.sendMessage(ChatColor.RED + "Internal Error");
						SkyQuestPlugin.instance.getLogger().severe("Failed to add quest " + quest.QuestID + " but also failed to find it in the existing quests");
						return;
					}
					else
					{
						existingQuest.updateFromTemplate(questBook);
						quest = existingQuest;
					}
				}
				Location loc = this.getLocation();
				
				if(!Pedestals.removePedestal(loc))
					SkyQuestPlugin.instance.getLogger().fine("Failed to remove pedestal");
				
				Pedestals.addPedestal(new QuestPedestal(quest.QuestID, loc));
				SkyQuestPlugin.instance.saveData();
				player.setItemInHand(null);
				player.updateInventory();
				player.sendMessage(ChatColor.GREEN + "The quest \"" + quest.QuestID + "\" was successfully created!");
			}
			else
				player.sendMessage(ChatColor.RED + "Error: " + problem);
		}
		else{
			player.sendMessage("This is a blank pedestal. Use a quest template on this pedestal.");
		}
		
	}

	@Override
	protected void onPedestalDestroyed() {
		Pedestals.removePedestal(getLocation());
	}

}
