package lu.uni.snt.topresearcher.parser;

import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

public abstract class DefaultParser implements IParser 
{
	private String htmlContent = "";
	protected String year = "";
	/**
	 * pageType = 1 means ul
	 * pageType = 2 means h2, ul, h2, ul
	 * pageType = 3 emans h2, h3, ul, h3, ul, h2, h3, ul
	 */
	private int pageType = 1;
	
	public DefaultParser(String htmlContent, String year)
	{
		this.htmlContent = htmlContent;
		this.year = year;
	}
	
	public DefaultParser start(ParseResult result)
	{
		try
		{
			HtmlCleaner cleaner = new HtmlCleaner();
			TagNode rootNode = cleaner.clean(htmlContent);
			
			TagNode mainNode = rootNode.getElementsByAttValue("id", "main", true, false)[0];
			
			TagNode[] tagNodes = mainNode.getChildTags();
			
			/*
			List<TagNode> tmpTagNodes = new ArrayList<TagNode>();
			for (TagNode tag : tagNodes)
			{
				if (tag.toString().equals("header") || 
					tag.toString().equals("ul") )
				{
					tmpTagNodes.add(tag);
				}
			}
			tagNodes = tmpTagNodes.toArray(new TagNode[]{});
			*/
			
			parse(tagNodes, result);
			
			pageType = probePageType(tagNodes);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		return this;
	}
	
	private int probePageType(TagNode[] tagNodes)
	{
		List<TagNode> headerTags = new ArrayList<TagNode>();
		for (TagNode node : tagNodes)
		{
			if (node.toString().equals("header"))
			{
				headerTags.add(node);
			}
		}
		
		try
		{	
			if (headerTags.size() <= 1)
			{
				return 1; //no sub-header for titles
			}
			
			for (TagNode tag : headerTags)
			{
				if ("h3".equals(tag.getChildTags()[0].toString()))
				{
					return 3;
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		return 2;
	}

	public int getPageType() {
		return pageType;
	}
}
