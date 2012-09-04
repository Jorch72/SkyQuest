package au.com.mineauz.SkyQuest.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import au.com.mineauz.SkyQuest.SkyQuestPlugin;

public class HelpCommand implements ICommand
{

	@Override
	public String getName() 
	{
		return "help";
	}

	@Override
	public String[] getAliases() 
	{
		return null;
	}

	@Override
	public String getPermission() 
	{
		return "skyquest.help";
	}

	@Override
	public String getUsageString(String label) 
	{
		return label;
	}

	@Override
	public boolean canBeConsole() 
	{
		return true;
	}

	@Override
	public boolean onCommand(CommandSender sender, String label, String[] args) 
	{
		sender.sendMessage(ChatColor.LIGHT_PURPLE + "Sky Quest");
		sender.sendMessage(ChatColor.GRAY + "Version: " + SkyQuestPlugin.instance.getDescription().getVersion());
		sender.sendMessage(ChatColor.GRAY + "By: " + SkyQuestPlugin.instance.getDescription().getAuthors().get(0));

		return true;
	}
	
}
