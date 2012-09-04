package au.com.mineauz.SkyQuest.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.SkyQuest.quests.Quest;

public class NewQuestCommand implements ICommand
{

	@Override
	public String getName() 
	{
		return "newquest";
	}

	@Override
	public String[] getAliases() 
	{
		return new String[] { "nq" };
	}

	@Override
	public String getPermission() 
	{
		return "skyquest.quest.edit";
	}

	@Override
	public String getUsageString(String label) 
	{
		return label;
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
		Player player = (Player)sender;
		
		if(player.getInventory().addItem(Quest.makeQuestTemplateBook()) != null)
			player.updateInventory();
		else
			player.sendMessage(ChatColor.RED + "You dont have any room in your inventory for the quest template");
		
		return true;
	}

}
