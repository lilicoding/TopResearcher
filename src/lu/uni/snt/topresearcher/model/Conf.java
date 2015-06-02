package lu.uni.snt.topresearcher.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lu.uni.snt.topresearcher.Utils;
import lu.uni.snt.topresearcher.parser.SingleVenuePageParseResult;
import lu.uni.snt.topresearcher.parser.SingleVenuePageParser;

public abstract class Conf 
{	
	public String name;
	public Set<String> urlSet;
	public int lastNYears;
	//public Set<String> nonTrTitles;
	
	public Conf(String name, int lastNYears)
	{
		this.name = name;
		this.lastNYears = lastNYears;
		this.urlSet = new HashSet<String>();
		//this.nonTrTitles = new HashSet<String>();
		
		String urlFile = "src/lu/uni/snt/topresearcher/venue/" + name + ".txt";
		//String nonTrFile = "src/lu/uni/snt/topresearcher/venue/" + name + "-non-tr-titles.txt";
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(urlFile));
			String line = "";
			while ((line = br.readLine()) != null)
			{
				urlSet.add(line);
				
				if (urlSet.size() == lastNYears)
				{
					break;
				}
			}
			br.close();
			
			/*
			br = new BufferedReader(new FileReader(nonTrFile));
			while ((line = br.readLine()) != null)
			{
				nonTrTitles.add(line);
			}
			br.close();
			*/
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public Conf(String name)
	{
		this(name, -1);
	}
	
	public abstract Map<String, List<Item>> filterOut(Map<String, List<Item>> headerToItems);
	
	public Map<String, Set<Item>> toYearItems()
	{
		Map<String, List<Item>> headerItems = obtainHeaderItems();
		
		Map<String, Set<Item>> yearItems = new HashMap<String, Set<Item>>();
		
		for (String header : headerItems.keySet())
		{
			List<Item> items = headerItems.get(header);
			for (Item item : items)
			{
				Set<Item> itemSet = yearItems.get(item.getYear());
				if (null == itemSet)
				{
					itemSet = new HashSet<Item>();
				}
				itemSet.add(item);
				
				yearItems.put(item.getYear(), itemSet);
			}
		}

		return yearItems;
	}
	
	
	public void toUrls()
	{
		for (String url : urlSet)
		{
			String venueStr = url.substring(url.lastIndexOf("/")+1);
			if (! venueStr.startsWith(name))
			{
				continue;
			}
			
			System.out.println(url);
		}
	}
	
	/*
	public void list()
	{
		for (String url : urlSet)
		{
			String venueStr = url.substring(url.lastIndexOf("/")+1);
			if (! venueStr.startsWith(name))
			{
				continue;
			}
			
			System.out.println(url);
			
			SingleVenuePageParser svpParser = new SingleVenuePageParser();
			Venue venue = svpParser.parseVenue(url, name);
			
			Map<String, List<Item>> results = filterOut(venue.headerToItems);
			
			System.out.println(venue.year);
			for (String header : results.keySet())
			{
				List<Item> items = results.get(header);
				for (Item item : items)
				{
					System.out.println(item);
				}
				
			}
		}
	}
	*/
	
	public Map<String, List<Item>> obtainHeaderItems()
	{
		Map<String, List<Item>> headerItems = new HashMap<String, List<Item>>();
		
		for (String url : urlSet)
		{
			String venueStr = url.substring(url.lastIndexOf("/")+1);
			if (! venueStr.startsWith(name))
			{
				continue;
			}
			
			String year = venueStr.replace(name, "").replace(".html", "");
			if (year.contains("-"))
			{
				year = year.substring(0, year.indexOf('-'));
			}
			if (year.length() == 2)
			{
				year = "19" + year;
			}
			
			String htmlContent = Utils.getUriText(url);
			
			SingleVenuePageParseResult result = new SingleVenuePageParseResult();
			SingleVenuePageParser svpParser = new SingleVenuePageParser(htmlContent, year);
			svpParser.start(result);
			Map<String, List<Item>> tmpheaderItems = svpParser.obtainHeaderItems(result);
			
			Map<String, List<Item>> results = filterOut(tmpheaderItems);
			
			for (Map.Entry<String, List<Item>> entry : results.entrySet())
			{
				List<Item> items = headerItems.get(entry.getKey());
				if (null == items)
				{
					items = entry.getValue();
				}
				else
				{
					items.addAll(entry.getValue());
				}
				headerItems.put(entry.getKey(), items);
			}
		}
		
		return headerItems;
	}
	
	public List<AuthorCountPair> rank()
	{
		Map<String, Integer> authorCount = new HashMap<String, Integer>();
		
		Map<String, List<Item>> headerItems = obtainHeaderItems();
		Map<String, List<Item>> results = filterOut(headerItems);
		
		for (String header : results.keySet())
		{
			List<Item> items = results.get(header);
			for (Item item : items)
			{
				for (String author : item.getAuthors())
				{
					add(authorCount, author);
				}
			}
			
		}
		
		List<AuthorCountPair> sortedPairs = Utils.sortByValue(authorCount);
		
		return sortedPairs;
		/*
		for (String url : urlSet)
		{
			String venueStr = url.substring(url.lastIndexOf("/")+1);
			if (! venueStr.startsWith(name))
			{
				continue;
			}
			
			String year = venueStr.replace(".html", "");
			if (year.contains("-"))
			{
				year = year.substring(0, year.indexOf('-'));
			}
			if (year.length() == 2)
			{
				year = "19" + year;
			}
			
			String htmlContent = Utils.getUriText(url);
			
			SingleVenuePageParseResult result = new SingleVenuePageParseResult();
			SingleVenuePageParser svpParser = new SingleVenuePageParser(htmlContent, year);
			svpParser.start(result);
			Map<String, List<Item>> headerItems = svpParser.obtainHeaderItems(result);
			
			Map<String, List<Item>> results = filterOut(headerItems);
			
			for (String header : results.keySet())
			{
				List<Item> items = results.get(header);
				for (Item item : items)
				{
					for (String author : item.getAuthors())
					{
						add(authorCount, author);
					}
				}
				
			}
		}
		
		List<AuthorCountPair> sortedPairs = Utils.sortByValue(authorCount);
		*/
		/*
		for (Map.Entry<String, Integer> entry : map.entrySet())
		{
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}*/
	}
	
	public static void add(Map<String, Integer> stat, String key)
	{
		Object obj = stat.get(key);
		if (null != obj)
		{
			int value = (int) obj;
			stat.put(key, value+1);
		}
		else
		{
			stat.put(key, 1);
		}
	}
}
