package au.com.mineauz.SkyQuest.commands;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.SkyQuest.SkyQuestPlugin;
import au.com.mineauz.SkyQuest.pedestals.BlankQuestPedestal;
import au.com.mineauz.SkyQuest.pedestals.ItemPedestal;
import au.com.mineauz.SkyQuest.pedestals.Pedestals;
import au.com.mineauz.SkyQuest.pedestals.SavePedestal;

public class NewPedestalCommand implements ICommand{

	@Override
	public String getName() {
		return "pedestal";
	}

	@Override
	public String[] getAliases() {
		return new String[] { "ped" };
	}

	@Override
	public String getPermission() {
		return "skyquest.pedestal.create";
	}

	@Override
	public String getUsageString(String label) {
		return label + " <type>";
		//return "Creates a pedestal where you are looking";
	}

	@Override
	public boolean canBeConsole() {
		return false;
	}

	@Override
	public boolean onCommand(CommandSender sender, String label, String[] args) {
		
		Player player = (Player)sender;
		
		if(args.length >= 4)
			return false;
		
		Bukkit.getLogger().log(Level.INFO, args[0] + " " + args[1]);
		
		Location target = player.getTargetBlock(null, 25).getLocation();
		
		if(target != null && target.getBlock().getType() != Material.AIR)
		{
			target.setY(target.getY() + 1);
			if(args[0].equalsIgnoreCase("savepoint")){
				Pedestals.addPedestal(new SavePedestal(target));
				SkyQuestPlugin.instance.saveData();
			}
			else if(args[0].equalsIgnoreCase("questpoint")){
				Pedestals.addPedestal(new BlankQuestPedestal(target));
				SkyQuestPlugin.instance.saveData();
			}
			else if(args.length >= 2 && args[0].equalsIgnoreCase("item")){
				String item = null;
				short data = 0;
				boolean error = false;
				
				if(args[1].contains(":")){
					String[] split = args[1].split(":");
					item = split[0];
					
					if(split[1].matches("[0-9]+")){
						data = Short.parseShort(split[1]);
					}
					else{
						error = true;
					}
				}
				else{
					item = args[1];
				}
				
				if(!error){
					ItemStack itemstack = null;
					if(item.matches("[a-zA-Z]+_?[a-zA-Z]+") && Material.matchMaterial(item.toUpperCase()) != null){
						itemstack = new ItemStack(Material.matchMaterial(item.toUpperCase()), 1, data);
					}
					else if(item.matches("[0-9]+")){
						itemstack = new ItemStack(Integer.parseInt(item), 1, data);
					}
					else{
						error = true;
					}
					
					if(args.length == 3 && args[2].matches("[0-9]+")){
						itemstack.setAmount(Integer.parseInt(args[2]));
					}
					
					if(!error){
						Pedestals.addPedestal(new ItemPedestal(target, itemstack));
						SkyQuestPlugin.instance.saveData();
					}
					else{
						player.sendMessage(ChatColor.RED + "Error: " + args[1] + " is an invalid item ID!");
					}
				}
				else{
					player.sendMessage(ChatColor.RED + "Error: " + args[1] + " is an invalid item ID!");
				}
			}
			else{
				player.sendMessage(ChatColor.RED + "Invalid pedestal type!");
			}
		}
		else
		{
			player.sendMessage(ChatColor.RED + "No target block in range!");
		}
		return true;
	}

}
