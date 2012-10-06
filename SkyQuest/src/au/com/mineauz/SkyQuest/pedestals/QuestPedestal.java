package au.com.mineauz.SkyQuest.pedestals;

import net.minecraft.server.NBTTagCompound;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.SkyQuest.MagicBook;
import au.com.mineauz.SkyQuest.SkyQuestPlugin;
import au.com.mineauz.SkyQuest.Util;
import au.com.mineauz.SkyQuest.quests.Quest;
import au.com.mineauz.SkyQuest.quests.QuestFactory;
import au.com.mineauz.SkyQuest.spells.SpellFactory;

public class QuestPedestal extends PedestalBase
{
	public QuestPedestal(String quest, Location location)
	{
		super(new ItemStack(Material.SMOOTH_BRICK, 1, (short)3), new ItemStack(Material.WRITTEN_BOOK), location);
		mQuest = quest;
	}
	public QuestPedestal()
	{
		
	}
	@Override
	protected void onPlayerActivatePedestal(Player player) 
	{
		Quest quest = QuestFactory.getQuest(mQuest.toLowerCase());
		
		if(quest == null)
		{
			SkyQuestPlugin.instance.getLogger().warning("Invalid quest id " + mQuest);
			player.sendMessage(ChatColor.RED + "Invalid quest id " + mQuest);
			return;
		}
		
		MagicBook book = Util.getMagicBookFor(player);
		if(book == null)
		{
			player.sendMessage(ChatColor.RED + "You need to be carrying your magic book to activate this pedestal");
			return;
		}
		
		if(!book.hasCompletedQuest(quest))
			book.markQuestComplete(quest);
		
		if(quest.getSpellsLearned() != null && !quest.getSpellsLearned().isEmpty()){
			for(String spell : quest.getSpellsLearned()){
				String[] spellsplit = spell.split(":");
				String spellname = spellsplit[0];
				int subtype = Integer.parseInt(spellsplit[1]);
				if(SpellFactory.getSpell(spellname) != null && !book.hasLearnedSpell(SpellFactory.getSpell(spellname), subtype)){
					book.learnSpell(SpellFactory.getSpell(spellname), subtype, false);
				}
			}
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
	
	private String mQuest;
}
