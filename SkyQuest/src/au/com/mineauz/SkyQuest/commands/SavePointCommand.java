package au.com.mineauz.SkyQuest.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.SkyQuest.SkyQuestPlugin;
import au.com.mineauz.SkyQuest.pedestals.Pedestals;
import au.com.mineauz.SkyQuest.pedestals.SavePedestal;

public class SavePointCommand implements ICommand
{
	@Override
	public String getName() 
	{
		return "savepoint";
	}

	@Override
	public String[] getAliases() 
	{
		return null;
	}

	@Override
	public String getPermission() 
	{
		return "skyquest.savepoint";
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

	@Override
	public boolean onCommand(CommandSender sender, String label, String[] args) 
	{
		Player player = (Player)sender;
		
		Location target = player.getTargetBlock(null, 25).getLocation();
		
		if(target != null && target.getBlock().getType() != Material.AIR)
		{
			target.setY(target.getY() + 1);
			Pedestals.addPedestal(new SavePedestal(target));
			SkyQuestPlugin.instance.saveData();
		}
		else
		{
			player.sendMessage(ChatColor.RED + "No target block in range!");
		}
		
		return true;
	}
	
}
