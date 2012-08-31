package au.com.mineauz.SkyQuest;

import java.io.*;
import java.util.logging.Level;

import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import au.com.mineauz.SkyQuest.pedestals.Pedestals;
import au.com.mineauz.SkyQuest.pedestals.SavePedestal;

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
		loadData();
		getServer().getPluginManager().registerEvents(new SkyQuestEvents(), this);
		getLogger().info(getDescription().getVersion() + " successfully enabled!");
		getLogger().getParent().getHandlers()[0].setLevel(Level.FINE);
		getLogger().setLevel(Level.FINE);
	}
	
	@Override
	public void onDisable()
	{
		saveData();
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
				if(player != null && player.hasPermission("skyquest.help")){
					player.sendMessage(ChatColor.LIGHT_PURPLE + "Sky Quest");
					player.sendMessage(ChatColor.GRAY + "Version: " + getDescription().getVersion());
					player.sendMessage(ChatColor.GRAY + "By: " + getDescription().getAuthors().get(0));
				}
				else if(player != null && !player.hasPermission("skyquest.help")){
					player.sendMessage(ChatColor.RED + "You do not have permission to use /skyquest help");
				}
				else{
					getLogger().info("Sky Quest");
					getLogger().info("Version: " + getDescription().getVersion());
					getLogger().info("By: " + getDescription().getAuthors().get(0));
				}
				return true;
			}
			else if(args.length == 1 && args[0].equalsIgnoreCase("savepoint")){
				if(player != null && player.hasPermission("skyquest.savepoint")){
					Location target = player.getTargetBlock(null, 25).getLocation();
					if(target != null && target.getBlock().getType() != Material.AIR){
						target.setY(target.getY() + 1);
						Pedestals.addPedestal(new SavePedestal(target));
					}
					else{
						player.sendMessage(ChatColor.RED + "No target block in range!");
					}
				}
				else if(player != null && !player.hasPermission("skyquest.savepoint")){
					player.sendMessage(ChatColor.RED + "You do not have permission to use /skyquest savepoint");
				}
				else{
					getLogger().info("You must be a player to execute this command!");
				}
			}
			else{
				if(player != null){
					player.sendMessage(ChatColor.RED + "Invalid command! Type '/skyquest help' for help");
				}
				else{
					getLogger().info("Invalid command! Type '/skyquest help' for help");
				}
				return true;
			}
		}
		
		return false;
	}
	
	public boolean saveData()
	{
		File dataFile = new File(getDataFolder(), "skyquest.dat");
		try
		{
			// Build the file path if needed
			dataFile.getParentFile().mkdirs();
			
			FileOutputStream stream = new FileOutputStream(dataFile);
			DataOutputStream dstream = new DataOutputStream(stream);
			
			NBTTagCompound root = new NBTTagCompound();
			Pedestals.savePedestals(root);
			// TODO: Any other save functions here

			NBTBase.a(root, dstream);
			
			dstream.close();
		}
		catch(IOException e)
		{
			getLogger().severe(e.getMessage());
			return false;
		}

		return true;
	}
	
	public boolean loadData()
	{
		File dataFile = new File(getDataFolder(), "skyquest.dat");
		// Nothing to load if it doesnt exist
		if(!dataFile.exists())
			return true;
		try
		{
			FileInputStream stream = new FileInputStream(dataFile);
			DataInputStream dstream = new DataInputStream(stream);
			
			NBTTagCompound root = (NBTTagCompound)NBTBase.b(dstream);
			
			Pedestals.loadPedestals(root);
			// TODO: Any other load functions here
			
			dstream.close();
		}
		catch(IOException e)
		{
			getLogger().severe(e.getMessage());
			return false;
		}
		
		return true;
	}
}
