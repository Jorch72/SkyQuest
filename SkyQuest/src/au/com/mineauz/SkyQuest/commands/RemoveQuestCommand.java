package au.com.mineauz.SkyQuest.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import au.com.mineauz.SkyQuest.SkyQuestPlugin;
import au.com.mineauz.SkyQuest.quests.QuestFactory;

public class RemoveQuestCommand implements ICommand
{

	@Override
	public String getName() 
	{
		return "removequest";
	}

	@Override
	public String[] getAliases() 
	{
		return new String[] { "rq" };
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
	public boolean canBeConsole() { return true; }

	@Override
	public boolean onCommand(CommandSender sender, String label, String[] args) 
	{
		if(args.length != 1)
			return false;
		
		if(QuestFactory.removeQuest(args[0].toLowerCase()))
		{
			sender.sendMessage(ChatColor.GREEN + "The quest " + args[0] + " has now been removed");
			SkyQuestPlugin.instance.saveData();
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "The quest " + args[0] + " does not exist");
		}
		
		return true;
	}

}
