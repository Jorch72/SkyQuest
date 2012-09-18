package au.com.mineauz.SkyQuest.quests;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.NBTTagCompound;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.SkyQuest.Book;
import au.com.mineauz.SkyQuest.spells.SpellFactory;

public class Quest
{
	private String mQuestName;
	private String mQuestStory;
	private int mExpGain;
	private List<String> mSpellsLearned;
	
	public String QuestID;
	
	/**
	 * Create a quest from a template boook
	 * @param templateBook - The template book
	 * @throws IllegalArgumentException If template book data is incorrect
	 */
	public Quest(Book templateBook)
	{
		updateFromTemplate(templateBook);
	}
	public Quest() {}
	
	/**
	 * Updates a quest from a template book
	 * @param templateBook - The template book
	 * @throws IllegalArgumentException If template book data is incorrect
	 */
	public void updateFromTemplate(Book templateBook)
	{
		String[] pages = templateBook.getPages();
		
		if(pages == null)
			throw new IllegalArgumentException("There are no pages in the book");
		
		if(pages.length < 5)
			throw new IllegalArgumentException("Some pages have been removed from the template");
		
		mQuestName = pages[1].substring(pages[1].indexOf('\n') + 1).trim();
		if(mQuestName.isEmpty())
			throw new IllegalArgumentException("The quest name cannot be empty");
		
		mQuestStory = pages[2].substring(pages[2].indexOf('\n') + 1);
		if(mQuestStory.isEmpty())
			throw new IllegalArgumentException("The quest must contain a story");
		
		String temp = pages[3].substring(pages[3].indexOf('\n') + 1).trim();
		try
		{
			mExpGain = Integer.parseInt(temp);
			
			if(mExpGain < 0)
				throw new IllegalArgumentException("Experience Gain is negative");
		}
		catch(NumberFormatException e)
		{
			throw new IllegalArgumentException("Experience Gain is not a number");
		}
		
		List<String> tempSpells = new ArrayList<String>();
		String[] spellsArr = pages[4].substring(pages[4].indexOf("\n") + 1).split(", ");
		if(spellsArr.length >= 1 && !spellsArr[0].equalsIgnoreCase("")){
			for(String spell : spellsArr){
				if(SpellFactory.getSpell(spell.substring(0, spell.indexOf(":") - 1).trim()) != null){
					tempSpells.add(spell.trim());
				}
				else{
					throw new IllegalArgumentException("Invalid spell name: \"" + spell.substring(0, spell.indexOf(":") - 1).trim() + "\"");
				}
			}
			
			if(tempSpells.size() > 0){
				mSpellsLearned = tempSpells;
			}
		}
	}
	
	public ItemStack makeIntoTemplateBook()
	{
		Book book = new Book();
		book.setType(Material.BOOK_AND_QUILL);
		
		// Info page
		book.addPage("Fill this book out with the information asked for on each page. " +
				"Then right click on a blank pedestal holding this book." +
				"\nThe required infomation is:\nQuest Name\nExperience Gain\n");

		book.addPage("Quest Name (String):\n" + mQuestName);
		book.addPage("Quest Story (String):\n" + mQuestStory);
		book.addPage("Experience Gain (Integer):\n" + mExpGain);
		
		String spells = "";
		for(int i = 0; i < mSpellsLearned.size(); i++){
			spells += mSpellsLearned.get(i);
			if(i != mSpellsLearned.size() - 1){
				spells += ", ";
			}
		}
		
		book.addPage("(Optional) Unlocked Spells (String:Integer list separated by \", \"):\n" + spells);
		
		return book;
	}
	
	public String getQuestName()
	{
		return mQuestName;
	}
	
	public int getExpGain()
	{
		return mExpGain;
	}
	
	public List<String> getSpellsLearned(){
		/*for(String spell : mSpellsLearned){
			String[] split = spell.split(":");
			String spellname = split[0];
			int spellSubtype = Integer.parseInt(split[1]);
		}*/ //TODO return spells correctly
		return mSpellsLearned;
	}
	
	public static ItemStack makeQuestTemplateBook()
	{
		Book book = new Book();
		book.setType(Material.BOOK_AND_QUILL);
		
		// Info page
		book.addPage("Fill this book out with the information asked for on each page. " +
				"Then right click on a blank pedestal holding this book.\n" +
				"The required infomation is:\nQuest Name\nQuest Story\nExperience Gain\n");

		book.addPage("Quest Name (String):\n");
		book.addPage("Quest Story (String):\n");
		book.addPage("Experience Gain (Integer):\n");
		book.addPage("(Optional) Learned Spells (String:Integer list separated by \", \"):\n");
		
		return book;
	}
	public static String makeSafeQuestName(String name)
	{
		return name.toLowerCase().trim().replace(" ", "_").replaceAll("[!@#\\$%\\^&\\*\\(\\)\\+\\-=\\|\\\\\\{\\}\\[\\];':\",\\./<>\\?]", "");
	}
	public void writeToNBT(NBTTagCompound root)
	{
		root.setString("Name", mQuestName);
		root.setInt("Exp", mExpGain);
	}
	
	public void readFromNBT(NBTTagCompound root)
	{
		mQuestName = root.getString("Name");
		mExpGain = root.getInt("Exp");
	}
}
