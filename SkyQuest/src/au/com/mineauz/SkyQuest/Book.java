package au.com.mineauz.SkyQuest;

import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagString;

import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class Book extends CraftItemStack
{
	public Book()
	{
		super(Material.WRITTEN_BOOK);
		
		if(getHandle().tag == null)
			getHandle().tag = new NBTTagCompound();
	}
	/**
	 * Creates a book out of an existing item stack
	 * @param item The item to make a book out of. Its type must be WRITTEN_BOOK
	 */
	public Book(ItemStack item)
	{
		super(item);
		
		if(item.getType() != Material.WRITTEN_BOOK)
			throw new IllegalArgumentException("item must be of type WRITTEN_BOOK");
		
		if(item instanceof CraftItemStack)
			getHandle().tag = ((CraftItemStack)item).getHandle().tag;

		
		if(getHandle().tag == null)
			getHandle().tag = new NBTTagCompound();
	}

	/**
	 * Checks whether the title has been set
	 * @return
	 */
	public boolean hasTitle() 
	{
		return getHandle().tag.hasKey("title");
	}

	/**
	 * Checks whether the author has been set
	 */
	public boolean hasAuthor() 
	{
		return getHandle().tag.hasKey("author");
	}

	/**
	 * checks whether there are any pages in the book
	 */
	public boolean hasPages() 
	{
		return getHandle().tag.hasKey("pages");
	}

	/**
	 * Gets the title of the book or null if not set
	 */
	public String getTitle() 
	{
		return getHandle().tag.getString("title");
	}

	/**
	 * Gets the author of the book or null if not set
	 */
	public String getAuthor() 
	{
		return getHandle().tag.getString("author");
	}

	/**
	 * Gets all the pages in the book or null if there are none
	 */
	public String[] getPages() 
	{
		NBTTagList list = (NBTTagList) getHandle().tag.get("pages");
		if (list == null)
			return null;
		String[] pages = new String[list.size()];
		for (int i = 0; i < list.size(); i++) 
		{
			pages[i] = ((NBTTagString) list.get(i)).data;
		}
		return pages;
	}

	/**
	 * Sets the title of the book
	 * @param title The name of the book. This cannot be longer than 16 characters
	 */
	public void setTitle(String title) 
	{
		if (title.length() > 16) 
			throw new IllegalArgumentException("title cannot be longer than 16 characters");
		
		getHandle().tag.setString("title", title);
	}

	/**
	 * Sets the author of the book
	 * @param author The name of the author. This cannot be longer than 16 characters
	 */
	public void setAuthor(String author) 
	{
		if (author.length() > 16) 
			throw new IllegalArgumentException("author cannot be longer than 16 characters");
		
		getHandle().tag.setString("author", author);
	}

	/**
	 * Sets all the pages in the book at once
	 * @param pages An array of strings, 1 for each page. The strings cannot be longer than 256 characters
	 */
	public void setPages(String[] pages) 
	{
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < pages.length; i++) 
		{
			String page = pages[i];
			if (page.length() > 256) 
				throw new IllegalArgumentException("pageText cannot be longer than 256 characters");

			NBTTagString st = new NBTTagString(page);
			st.data = page;
			list.add(st);
		}
		
		// Apply the new pages
		getHandle().tag.set("pages", list);
	}
	
	/**
	 * Sets the text on a page
	 * @param pageText The text to set. It cannot be longer than 256 characters
	 * @param page The zero based index of the page to set. This can be larger than the number of pages existing, in which case blank pages will be inserted.
	 */
	public void setPage(String pageText, int page)
	{
		if(pageText.length() > 256)
			throw new IllegalArgumentException("pageText cannot be longer than 256 characters");
		if(page < 0)
			throw new IllegalArgumentException("page cannot be less than 0");

		// Get the existing pages
		NBTTagList list = (NBTTagList) getHandle().tag.get("pages");
		if (list == null)
			list = new NBTTagList();

		// Add in the new page or set an existing one
		if(page > list.size())
		{
			for(int i = list.size(); i < page; i++)
			{
				NBTTagString tagString = new NBTTagString(" ");
				tagString.data = " ";
				list.add(tagString);
			}
				
			NBTTagString tagString = new NBTTagString(pageText);
			tagString.data = pageText;
			list.add(tagString);
		}
		else
		{
			((NBTTagString)list.get(page)).setName(pageText);
			((NBTTagString)list.get(page)).data = pageText;
		}
		
		// Apply the pages
		getHandle().tag.set("pages", list);
	}
	
	/**
	 * Appends a page to the book
	 * @param pageText The page text to add. It cannot be longer than 256 characters
	 */
	public void addPage(String pageText)
	{
		if(pageText.length() > 256)
			throw new IllegalArgumentException("pageText cannot be longer than 256 characters");
		
		// Get the existing pages
		NBTTagList list = (NBTTagList) getHandle().tag.get("pages");
		if (list == null)
			list = new NBTTagList();
		
		// Add the new page
		NBTTagString tagString = new NBTTagString(pageText);
		tagString.data = pageText;
		list.add(tagString);
		
		// Apply the pages
		getHandle().tag.set("pages", list);
	}
}
