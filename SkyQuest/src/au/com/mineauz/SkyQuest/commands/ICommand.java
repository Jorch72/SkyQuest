package au.com.mineauz.SkyQuest.commands;

import org.bukkit.command.CommandSender;

public interface ICommand 
{
	/**
	 * Gets the name of the command
	 */
	public String getName();
	/**
	 * Gets any aliases this command has
	 * @return an array of the aliases or null if there are none
	 */
	public String[] getAliases();
	
	/**
	 * Gets the permission that this command needs to be used, or null if there isnt one
	 */
	public String getPermission();
	
	/**
	 * Gets the usage string for this command. This should be in the format of: "<command> <usage>"
	 * @param label This is either the name of the command or an alias if they used one.
	 * @return The usage string  
	 */
	public String getUsageString(String label);

	/**
	 * Can the sender of this command be a console?
	 */
	public boolean canBeConsole();
	
	/**
	 * Called when this command is executed. By this time the permission has been checked, and if this command does not accept the console as a sender, that wont trigger this command.
	 * @param sender The sender of this command. If canBeConsole() == false, this will only ever be an instance of a Player
	 * @param label The command name or the alias that was used to call this command
	 * @param args The arguments for this command
	 * @return True if this command was executed. False otherwise
	 */
	public boolean onCommand(CommandSender sender, String label, String[] args);
}
