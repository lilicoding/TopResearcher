package lu.uni.snt.topresearcher;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lu.uni.snt.topresearcher.model.AuthorCountPair;
import lu.uni.snt.topresearcher.model.Item;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Utils {
	
	public static void addAll(Map<String, Integer> all, Map<String, Integer> toAdd)
	{
		for (Map.Entry<String, Integer> entry : toAdd.entrySet())
		{
			String key = entry.getKey();
			int value = entry.getValue();
			
			Object obj = all.get(key);
			if (null != obj)
			{
				int num = (int) obj;
				
				all.put(key, num + value);
			}
			else
			{
				all.put(key, value);
			}
		}
	}
	
	/*
	public static Map sortByValue(Map unsortMap) 
	{	
		List list = new LinkedList(unsortMap.entrySet());
	 
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return 0 - ((Comparable) ((Map.Entry) (o1)).getValue())
							.compareTo(((Map.Entry) (o2)).getValue());
			}
		});
	 
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}*/
	
	public static List<AuthorCountPair> sortByValue(Map unsortMap) 
	{	
		List<AuthorCountPair> authorCountPairs = new ArrayList<AuthorCountPair>();
		
		List list = new LinkedList(unsortMap.entrySet());
	 
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return 0 - ((Comparable) ((Map.Entry) (o1)).getValue())
							.compareTo(((Map.Entry) (o2)).getValue());
			}
		});
	 
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			
			authorCountPairs.add(new AuthorCountPair((String) entry.getKey(), (int) entry.getValue()));
		}
		
		return authorCountPairs;
	}
	
	public static String getUriText(String httpLink)
	{
		StringBuilder response = new StringBuilder();
		
		try
		{
			URL url = new URL(httpLink);

			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;
			
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine + "\n");
			}

			in.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		return response.toString();
	}
	
	public static void toHtml_Fulllist(Map<String, Set<Item>> yearItems, String venueName)
	{
		List<String> years = new ArrayList<String>(yearItems.keySet());
		Collections.sort(years);
		
		StringBuilder sb = new StringBuilder();
		sb.append("<html>" + "\n");
		sb.append("<body>" + "\n");
		sb.append("<div>" + "\n");
		
		for (int i = years.size()-1; i >=0; i--)
		{
			String year = years.get(i);
			sb.append("<p>" + year + "</p>" + "\n");
			
			Set<Item> items = yearItems.get(year);
			sb.append("<ul>" + "\n");
			for (Item item : items)
			{
				sb.append("<li>" + item.toString() + "</li>" + "\n");
			}
			sb.append("</ul>" + "\n");
		}
		
		
		sb.append("</div>" + "\n");
		sb.append("</body>" + "\n");
		sb.append("</html>");
		
		writeResultToFile(venueName + "-fulllist.html", sb.toString());
	}
	
	public static void toHtml_Statistic(
			List<AuthorCountPair> all, 
			List<AuthorCountPair> _10Years, 
			List<AuthorCountPair> _5Years,
			String venueName)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<html>" + "\n");
		sb.append("<body>" + "\n");
		sb.append("<div align=\"center\">" + "\n");
		
		sb.append("This page maintains a list of authors who publish their papers on " + venueName.toUpperCase() + ", based on the resources provided by DBLP." + "\n");
		
		sb.append("Last modification: " + getCurrentTime() + "\n");
		sb.append("<hr />" + "\n");
		
		sb.append("<table>" + "\n");
		
		sb.append("<tr><td>ALL Years</td><td>#. of papers</td><td>Last 10 Years</td><td>#. of papers</td><td>Last 5 Years</td><td>#. of papers</td></tr>" + "\n");
		
		for (int i = 0; i < all.size(); i++)
		{
			sb.append("<tr>" + "\n");
			
			if (i < all.size())
			{
				sb.append("<td>" + all.get(i).author + "</td><td>" + all.get(i).count + "</td>" + "\n");
			}
			else
			{
				sb.append("<td></td><td></td>" + "\n");
			}
			
			
			if (i < _10Years.size())
			{
				sb.append("<td>" + _10Years.get(i).author + "</td><td>" + _10Years.get(i).count + "</td>" + "\n");
			}
			else
			{
				sb.append("<td></td><td></td>" + "\n");
			}
			
			if (i < _5Years.size())
			{
				sb.append("<td>" + _5Years.get(i).author + "</td><td>" + _5Years.get(i).count + "</td>" + "\n");
			}
			else
			{
				sb.append("<td></td><td></td>" + "\n");
			}
			
			sb.append("</tr>" + "\n");
		}
		
		
		sb.append("</table>" + "\n");
		
		sb.append("</div>" + "\n");
		sb.append("</body>" + "\n");
		sb.append("</html>");
		
		writeResultToFile(venueName + "-statistic.html", sb.toString());
	}
	
	public static void writeResultToFile(String path, String content)
	{
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path, true)));
		    out.print(content);
		    out.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	public static String getCurrentTime()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		
		return dateFormat.format(date);
	}
}
