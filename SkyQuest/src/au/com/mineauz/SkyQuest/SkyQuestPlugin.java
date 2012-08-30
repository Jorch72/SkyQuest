package au.com.mineauz.SkyQuest;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyQuestPlugin extends JavaPlugin
{
    public static SkyQuestPlugin instance;
	
	@Override
	public void onLoad() 
	{
		instance = this;
	}
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(new SkyQuestEvents(), this);
		getLogger().info(getDescription().getVersion() + " successfully enabled!");
		getLogger().getParent().getHandlers()[0].setLevel(Level.FINE);
		getLogger().setLevel(Level.FINE);
	}
	
	@Override
	public void onDisable()
	{
		
		getLogger().info("successfully disabled!");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args){
		Player player = null;
		if(sender instanceof Player){
			player = (Player) sender;
		}
		
		if(cmd.getName().equalsIgnoreCase("skyquest")){
			if(args.length == 1 && args[0].equalsIgnoreCase("help")){
				if(player != null && player.hasPermission("skyquest.skyquest") && player.hasPermission("skyquest.help")){
					player.sendMessage(ChatColor.LIGHT_PURPLE + "Sky Quest");
					player.sendMessage(ChatColor.GRAY + "Version: " + getDescription().getVersion());
					player.sendMessage(ChatColor.GRAY + "By: " + getDescription().getAuthors().get(0));
				}
				else{
					getLogger().info("Sky Quest");
					getLogger().info("Version: " + getDescription().getVersion());
					getLogger().info("By: " + getDescription().getAuthors().get(0));
				}
				return true;
			}
			else{
				player.sendMessage(ChatColor.RED + "Invalid command! Type '/skyquest help' for help");
				return true;
			}
		}
		
		return false;
	}
}
