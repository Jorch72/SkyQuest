package au.com.mineauz.SkyQuest.commands;

import org.bukkit.command.CommandSender;

public class SetWarpPointCommand implements ICommand{

	@Override
	public String getName() {
		return "warppoint";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"wp"};
	}

	@Override
	public String getPermission() {
		return "skyquest.warppoint.add";
	}

	@Override
	public String getUsageString(String label) {
		return label;
	}

	@Override
	public boolean canBeConsole() {
		return false;
	}

	@Override
	public boolean onCommand(CommandSender sender, String label, String[] args) {
		
		return true;
	}

}
