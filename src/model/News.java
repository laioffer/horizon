package model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class News {

	private String id;
	private String title;
	private String url;
	private String snippet;
	private String imageUrl;
	private List<String> keywords;

	private static final String NYTIMES_URL = "http://www.nytimes.com/";

	/**
	 * Construct a News object from a JSONObject
	 */
	public News(JSONObject jsonObject) {
		try {
			this.id = jsonObject.getString("_id");
			this.title = jsonObject.getJSONObject("headline").getString("main");
			this.url = jsonObject.getString("web_url");
			this.snippet = jsonObject.getString("snippet");
			JSONArray multimedia = jsonObject.getJSONArray("multimedia");
			if (multimedia.length() > 0) {
				this.imageUrl = NYTIMES_URL
						+ multimedia.getJSONObject(0).getString("url");
			}
			JSONArray array = jsonObject.getJSONArray("keywords");
			this.keywords = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				this.keywords.add(array.getJSONObject(i).getString("value"));
			}
		} catch (Exception e) {
			System.out
					.println("Exception in converting a JSON object to a News object");
			e.printStackTrace();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
}
