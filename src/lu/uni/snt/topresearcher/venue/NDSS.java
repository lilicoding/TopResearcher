package lu.uni.snt.topresearcher.venue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lu.uni.snt.topresearcher.Utils;
import lu.uni.snt.topresearcher.model.Conf;
import lu.uni.snt.topresearcher.model.Item;

public class NDSS extends Conf
{
	String[] filterOutHeaderStrs = {
		"competition",
		"tutorial",
		"workshop",
		"symposium",
		"poster",
		"demo",
		"new idea",
		"software engineering in practice",
		"software engineering in education",
		"visions and challenges",
		"nier",
		"panel",
		"keynote"
	};
	
	public NDSS(String name, int lastNYears) {
		super(name, lastNYears);
	}
	
	public NDSS(String name) {
		super(name);
	}

	public static void main(String[] args) 
	{	
		String venueName = "ndss";
		
		Utils.toHtml_Fulllist(new NDSS(venueName).toYearItems(), venueName);
		
		Utils.toHtml_Statistic(new NDSS(venueName).rank(), new NDSS(venueName, 10).rank(), new NDSS(venueName, 5).rank(), venueName);
	}

	@Override
	public Map<String, List<Item>> filterOut(Map<String, List<Item>> headerToItems) 
	{
		Map<String, List<Item>> tmpHeaderItems = new HashMap<String, List<Item>>();
		
		//1. through header
		for (String header : headerToItems.keySet())
		{
			boolean toRemoveHeader = false;
			
			for (String str : filterOutHeaderStrs)
			{
				if (header.toLowerCase().contains(str))
				{
					toRemoveHeader = true;
					break;
				}
			}
			
			if (! toRemoveHeader)
			{
				tmpHeaderItems.put(header, headerToItems.get(header));
			}
		}
		
		//2. through pages
		for (String header : tmpHeaderItems.keySet())
		{
			List<Item> itemList = new ArrayList<Item>();
			
			List<Item> items = tmpHeaderItems.get(header);
			for (Item item : items)
			{
				//take short papers into consideration
				if (item.getPage() >= 4)
				{
					itemList.add(item);
				}
				else
				{
					System.out.println(item);
				}
			}
			
			tmpHeaderItems.put(header, itemList);
		}
		
		//3. through known non technique research track titles
		/*for (String header : tmpHeaderItems.keySet())
		{
			List<Item> itemList = new ArrayList<Item>();
			
			List<Item> items = tmpHeaderItems.get(header);
			for (Item item : items)
			{
				if (! nonTrTitles.contains(item.getTitle()))
				{
					itemList.add(item);
				}
			}
			
			tmpHeaderItems.put(header, itemList);
		}*/
		

		return tmpHeaderItems;
	}

}
