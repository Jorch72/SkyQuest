package au.com.mineauz.SkyQuest.pedestals;

import net.minecraft.server.NBTTagCompound;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.SkyQuest.MagicBook;
import au.com.mineauz.SkyQuest.Util;
import au.com.mineauz.SkyQuest.quests.Quest;
import au.com.mineauz.SkyQuest.quests.QuestFactory;

public class QuestStartPedestal extends PedestalBase{
	
	private String mQuest;
	
	public QuestStartPedestal(String quest, Location location)
	{
		super(new ItemStack(Material.SMOOTH_BRICK, 1, (short)3), new ItemStack(Material.EMERALD), location);
		mQuest = quest;
	}
	public QuestStartPedestal(){
		
	}
	
	@Override
	protected void onPlayerActivatePedestal(Player player) {
		MagicBook book = Util.getMagicBookFor(player);
		Quest quest = QuestFactory.getQuest(mQuest.toLowerCase());
		
		if(book == null){
			player.sendMessage(ChatColor.RED + "You need your magic book to start this quest.");
			return;
		}
		
		if(quest == null){
			player.sendMessage(ChatColor.RED + "Invalid quest! Notify an Admin!");
		}
		player.sendMessage("right clicked");
		
		if(!book.hasQuestInProgress(quest)){
			player.sendMessage("not a quest in progress");
			book.addQuestInProgress(quest);
			String spells = "";
			if(!quest.getSpellsLearned().isEmpty()){
				for(int i = 0; i < quest.getSpellsLearned().size(); i++){
					spells += quest.getSpellsLearned().get(i);
					if(i != quest.getSpellsLearned().size() - 1){
						spells += ", ";
					}
				}
			}
			else{
				spells = "None";
			}
			String page = ChatColor.LIGHT_PURPLE + quest.getQuestName() + "\n" +
					ChatColor.BLACK + quest.getQuestStory() + "\n" +
					"XP Gain: " + quest.getExpGain() + 
					"Spells Learned: " + spells;
					
			book.addPage(page);
			player.sendMessage("A new quest has been added to your magic book!");
		}
	}

	@Override
	protected void onPedestalDestroyed() {
		Pedestals.removePedestal(getLocation());
	}
	
	@Override
	public void writeToNBT(NBTTagCompound root) 
	{
		super.writeToNBT(root);
		root.setString("Quest", mQuest);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound root) 
	{
		super.readFromNBT(root);
		
		mQuest = root.getString("Quest");
	}

}
