package lu.uni.snt.topresearcher.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lu.uni.snt.topresearcher.model.Item;

import org.htmlcleaner.TagNode;



public class SingleVenuePageParser extends DefaultParser
{	
	public SingleVenuePageParser(String htmlContent, String year) 
	{
		super(htmlContent, year);
	}

	@SuppressWarnings("unchecked")
	public Map<String, List<Item>> obtainHeaderItems(SingleVenuePageParseResult result)
	{
		Map<String, List<Item>> headerItems = new HashMap<String, List<Item>>();
		
		if (1 == this.getPageType())
		{
			List<Item> items = new ArrayList<Item>();
			
			for (Object obj : result.data)
			{
				if (obj instanceof List)
				{
					List<Item> tmpList = (List<Item>) obj;
					items.addAll(tmpList);
				}
			}
			
			headerItems.put("Technique Research", items);
		}
		else if (2 == this.getPageType() || 3 == this.getPageType())
		{
			String h2 = null;
			
			for (Object obj : result.data)
			{
				if (obj instanceof String)
				{
					String str = (String) obj;
					if (str.startsWith("h2:"))
					{
						h2 = str.replace("h2: ", "");
					}
				}
				else if (obj instanceof List)
				{
					if (null != h2)
					{
						List<Item> tmpList = (List<Item>) obj;
						
						if (headerItems.containsKey(h2))
						{
							tmpList.addAll(headerItems.get(h2));
						}
						
						headerItems.put(h2, tmpList);
					}
				}
			}
		}
		
		return headerItems;
	}
	
	@Override
	public void parse(TagNode[] headerAndUlTags, ParseResult result) 
	{
		if (! (result instanceof SingleVenuePageParseResult) )
		{
			throw new RuntimeException("The type of ParseResult should be SingleVenuePageParseResult.");
		}
		
		SingleVenuePageParseResult svppResult = (SingleVenuePageParseResult) result;
		
		for (TagNode ele : headerAndUlTags)
		{
			if (ele.toString().equals("header"))
			{
				String headerType = ele.getChildTags()[0].toString();
				String headerText = ele.getText().toString();
				
				svppResult.data.add(headerType + ": " + headerText);
			}
			else if (ele.toString().equals("ul"))
			{
				TagNode[] itemTags = ele.getElementsByName("span", true);
				List<Item> items = parseItemTags(itemTags);
				
				svppResult.data.add(items);
			}
		}
	}
	
	public List<Item> parseItemTags(TagNode[] itemTags) 
	{
		List<Item> items = new ArrayList<Item>();
		
	    try {
			boolean newStart = true;
			Item item = null;
			
			for (TagNode ele : itemTags)
			{
				if (newStart)
				{
					item = new Item(year);
					newStart = false;
				}
				
				if (ele.hasAttribute("itemprop"))
				{
					String value = ele.getAttributeByName("itemprop");
					
					if ("author".equals(value))
					{
						String name = ele.getText().toString();
						item.getAuthors().add(name);
					}
					else if ("name".equals(value))
					{
						if (ele.hasAttribute("class") && "title".equals(ele.getAttributeByName("class")))
						{
							String title = ele.getText().toString();
							item.setTitle(title);
						}
					}
					else if ("pagination".equals(value))
					{
						try
						{
							String pageStr = ele.getText().toString();
							if (pageStr.contains("-"))
							{
								String[] nums = pageStr.split("-");
								if (nums.length == 2)
								{
									int pageNum = Integer.parseInt(nums[1]) - Integer.parseInt(nums[0]);
									item.setPage(pageNum+1);
								}
							}
							else
							{
								item.setPage(1);
							}
						}
						catch (Exception ex)
						{
							//sometimes the page number could be 7:1-7:7 or 
							item.setPage(1);
						}
						
						
						
						newStart = true;
						if (item != null)
							items.add(item);
					}
				}
			}
		} 
	    catch (Exception e) 
	    {
			e.printStackTrace();
		}
		
		return items;
	}
}
