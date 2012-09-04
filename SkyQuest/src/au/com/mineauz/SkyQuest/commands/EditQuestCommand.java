package au.com.mineauz.SkyQuest.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.SkyQuest.quests.Quest;
import au.com.mineauz.SkyQuest.quests.QuestFactory;

public class EditQuestCommand implements ICommand
{

	@Override
	public String getName() 
	{
		return "editquest";
	}

	@Override
	public String[] getAliases() 
	{
		return new String[] { "eq" };
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
	public boolean canBeConsole() { return false; }

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, String label, String[] args) 
	{
		if(args.length != 1)
			return false;
		
		Quest quest = QuestFactory.getQuest(args[0].toLowerCase());
		
		if(quest == null)
		{
			sender.sendMessage(ChatColor.RED + "There is no quest with the id: '" + args[0] + "'");
			return true;
		}
		
		if(((Player)sender).getInventory().addItem(quest.makeIntoTemplateBook()) == null)
		{
			sender.sendMessage(ChatColor.RED + "There is no room in your inventory for the template book");
			return true;
		}
		
		((Player)sender).updateInventory();
		return true;
	}

}
