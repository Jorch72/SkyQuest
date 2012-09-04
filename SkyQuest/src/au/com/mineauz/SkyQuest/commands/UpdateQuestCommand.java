package au.com.mineauz.SkyQuest.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.SkyQuest.Book;
import au.com.mineauz.SkyQuest.SkyQuestPlugin;
import au.com.mineauz.SkyQuest.quests.Quest;
import au.com.mineauz.SkyQuest.quests.QuestFactory;

public class UpdateQuestCommand implements ICommand
{

	@Override
	public String getName() 
	{
		return "updatequest";
	}

	@Override
	public String[] getAliases() 
	{
		return new String[] { "addquest", "uq", "aq" };
	}

	@Override
	public String getPermission() 
	{
		return "skyquest.quest.edit";
	}

	@Override
	public String getUsageString(String label) 
	{
		return label + " <questid>";
	}

	@Override
	public boolean canBeConsole() 
	{
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, String label, String[] args) 
	{
		if(args.length != 1)
			return false;
		
		Quest quest = QuestFactory.getQuest(args[0].toLowerCase());
		Player player = (Player)sender;
		
		
		if(player.getItemInHand() == null || player.getItemInHand().getType() != Material.BOOK_AND_QUILL)
		{
			player.sendMessage(ChatColor.RED + "You must be holding the quest template book");
			return true;
		}
			
		if(quest == null)
		{
			// Add a new quest
			Book templateBook = new Book(player.getItemInHand());
			quest = new Quest(templateBook);
			try
			{
				QuestFactory.addQuest(args[0].toLowerCase(), quest);
				player.sendMessage(ChatColor.GREEN + "Quest " + args[0].toLowerCase() + " has been created");
			}
			catch(IllegalArgumentException e)
			{
				player.sendMessage(ChatColor.RED + e.getMessage());
				return true;
			}
		}
		else
		{
			// Update an existing one
			Book templateBook = new Book(player.getItemInHand());
			
			try
			{
				quest.updateFromTemplate(templateBook);
				player.sendMessage(ChatColor.GREEN + "Quest " + args[0].toLowerCase() + " has been updated");
			}
			catch(IllegalArgumentException e)
			{
				player.sendMessage(ChatColor.RED + e.getMessage());
				return true;
			}
		}
		
		player.setItemInHand(null);
		player.updateInventory();
		
		SkyQuestPlugin.instance.saveData();
		return true;
	}
	
}
