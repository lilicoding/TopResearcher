package lu.uni.snt.topresearcher.parser;

import org.htmlcleaner.TagNode;

public interface IParser 
{
	public void parse(TagNode[] headerAndUlTags, ParseResult result);
}
