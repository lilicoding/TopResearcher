package lu.uni.snt.topresearcher;
import java.util.HashSet;
import java.util.Set;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;


public class VenuePageParser 
{
	public static void main(String[] args)
	{
		//String venueURL = "http://dblp.uni-trier.de/db/conf/kbse/";
		
		//String venueURL = "http://dblp.uni-trier.de/db/conf/ccs/";
		
		//String venueURL = "http://dblp.uni-trier.de/db/conf/sp/";
		
		//String venueURL = "http://dblp.uni-trier.de/db/conf/uss/index.html";
		
		String venueURL = "http://dblp.uni-trier.de/db/conf/ndss/";
		
		String content = Utils.getUriText(venueURL);
		parse(content, "ndss");
	}
	
	
	public static void parse(String content, String venueName) 
	{
		Set<String> urls = new HashSet<String>();
		
	    try {
	    	HtmlCleaner cleaner = new HtmlCleaner();
			TagNode rootNode = cleaner.clean(content);
			
			TagNode[] elements = rootNode.getElementsByName("a", true);
			

			for (TagNode ele : elements)
			{	
				String text = ele.getText().toString();
				if (text.contains("contents"))
				{
					text = text.substring(1, text.length()-1);
				}
				
				if ("contents".equals(text) && ele.hasAttribute("href"))
				{	
					String url = ele.getAttributeByName("href");
					urls.add(url);
				}
			}
	    }
	    catch (Exception ex)
	    {
	    	ex.printStackTrace();
	    }
	    
	    for (String url : urls)
	    {
	    	String venue = url;
	    	if (venue.contains("/"))
	    	{
	    		venue = url.substring(url.lastIndexOf('/')+1);
	    	}
	    	
	    	if (venue.startsWith(venueName) || venue.startsWith("kbse"))
	    	{
	    		System.out.println(url);
	    	}
	    }
	}
}
