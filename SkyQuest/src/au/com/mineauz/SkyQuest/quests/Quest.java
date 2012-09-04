package au.com.mineauz.SkyQuest.quests;

import net.minecraft.server.NBTTagCompound;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.SkyQuest.Book;

public class Quest
{
	private String mQuestName;
	private int mExpGain;
	
	public String QuestID;

	public Quest(Book templateBook)
	{
		updateFromTemplate(templateBook);
	}
	public Quest() {}
	
	public void updateFromTemplate(Book templateBook)
	{
		String[] pages = templateBook.getPages();
		
		if(pages == null)
			throw new IllegalArgumentException("There are no pages in the book");
		
		if(pages.length < 3)
			throw new IllegalArgumentException("Some pages have been removed from the template");
		
		mQuestName = pages[1].substring(pages[1].indexOf('\n') + 1).trim();
		
		String temp = pages[2].substring(pages[2].indexOf('\n') + 1).trim();
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
	}
	
	public ItemStack makeIntoTemplateBook()
	{
		Book book = new Book();
		book.setType(Material.BOOK_AND_QUILL);
		
		// Info page
		book.addPage("Fill this book out with the information asked for on each page. Then right click on a blank pedestal holding this book.\nThe required infomation is:\nQuest Name\nExperience Gain\n");

		book.addPage("Quest Name (String):\n" + mQuestName);
		book.addPage("Experience Gain (Integer):\n" + mExpGain);
		
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
	public static ItemStack makeQuestTemplateBook()
	{
		Book book = new Book();
		book.setType(Material.BOOK_AND_QUILL);
		
		// Info page
		book.addPage("Fill this book out with the information asked for on each page. Then right click on a blank pedestal holding this book.\nThe required infomation is:\nQuest Name\nExperience Gain\n");

		book.addPage("Quest Name (String):\n");
		book.addPage("Experience Gain (Integer):\n");
		
		return book;
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
