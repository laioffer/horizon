package model;

import java.util.ArrayList;
import java.util.List;

public class Keyword implements Comparable<Keyword>{
	
	private String key;
	private int count;
	
	public Keyword(String key, int count) {
		super();
		this.key = key;
		this.count = count;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	/**
	 * Convert a list of Keywords to a formated string. 
	 */
	public static String convertKeywordsToString(List<String> keywords) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keywords.size(); i ++) {
			String keyword = keywords.get(i);
			sb.append(keyword);
			if (i != keywords.size() - 1) {
				// Add a semicolon to separate keywords.
				sb.append(";");
			}
		}
		return sb.toString();
	}
	
	public static List<String> convertStrToKeywords(String keywordStr) {
		String[] args = keywordStr.split(";");
		List<String> keywords = new ArrayList<>();
		for (String arg : args) {
			keywords.add(arg);
		}
		return keywords;
	}
	
	/**
	 * Both equals and hashCode are useful when we put News in a HashSet to
	 * avoid duplicate News.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Keyword) {
			return false;
		}
		Keyword anotherKeyword = (Keyword) obj;
		return this.key == anotherKeyword.key;
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}

	@Override
	public int compareTo(Keyword anotherKeyword) {
		if (this.count != anotherKeyword.count) {
			return anotherKeyword.count - this.count;
		}
		return this.key.compareTo(anotherKeyword.key);
	}
}
