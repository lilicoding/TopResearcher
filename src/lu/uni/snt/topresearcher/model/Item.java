package lu.uni.snt.topresearcher.model;
import java.util.ArrayList;
import java.util.List;


public class Item {

	private List<String> authors;
	private String title;
	private int page = 1;
	
	private String year;
	
	public Item()
	{
		authors = new ArrayList<String>();
	}
	
	public Item(String year)
	{
		this();
		this.year = year;
	}
	
	@Override
	public int hashCode() 
	{
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Item other = (Item) obj;
		
		if (title == null) 
		{
			if (other.title != null)
				return false;
		} 
		else if (title.equals(other.title)) //title does not equal to null
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		
		for (String author : authors)
		{
			sb.append(author + ",");
		}
		
		sb.append(title);
		
		return sb.toString();
	}
	
	public List<String> getAuthors() {
		return authors;
	}
	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
}
