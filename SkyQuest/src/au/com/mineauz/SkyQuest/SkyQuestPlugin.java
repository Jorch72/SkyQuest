package au.com.mineauz.SkyQuest;

import java.io.*;
import java.util.logging.Level;

import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;

import org.bukkit.plugin.java.JavaPlugin;

import au.com.mineauz.SkyQuest.commands.CommandDispatcher;
import au.com.mineauz.SkyQuest.pedestals.Pedestals;
import au.com.mineauz.SkyQuest.quests.QuestFactory;

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
		
		// Use the new command dispatcher instead 
		getCommand("skyquest").setExecutor(new CommandDispatcher());
	}
	
	@Override
	public void onDisable()
	{
		saveData();
		Pedestals.unloadPedestals();
		getLogger().info("successfully disabled!");
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
			QuestFactory.writeToNBT(root);
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
			QuestFactory.readFromNBT(root);
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
