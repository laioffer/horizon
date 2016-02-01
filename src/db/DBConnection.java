package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import model.Keyword;
import model.News;

/**
 * Performs all DB queries
 */
public class DBConnection {
	private Connection conn;

	public DBConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(DBUtil.URL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void executeUpdateStatement(String query) {
		try {
			if (conn == null) {
				return;
			}
			Statement stmt = conn.createStatement();
			System.out.println("\nDBConnection executing query:\n" + query);
			stmt.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private ResultSet executeFetchStatement(String query) {
        if (conn == null) {
            return null;
        }
        try {
            Statement stmt = conn.createStatement();
            System.out.println("\nDBConnection executing query:\n" + query);
            return stmt.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
    }
	
	private Set<String> executeQueryStatement(String key, String inputKey, String outputKey, String tableName) {
		Set<String> set = new HashSet<>();
		try {
			if (conn == null) {
				return null;
			}
			Statement stmt = conn.createStatement();
			String sql = "SELECT " + outputKey + " from " + tableName + " WHERE " + inputKey + " =\""
					+ key + "\"";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String result = rs.getString(outputKey);
				set.add(result);
			}
			return set;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return set;
	}

	public void insertNews(News news) {
		String sql = "INSERT IGNORE INTO horizon_news " + "VALUES (\""
				+ news.getId() + "\", \""
				+ news.getTitle() + "\", \""
				+ news.getUrl() + "\", \""
				+ news.getSnippet() + "\", \"" 
				+ news.getImageUrl() + "\", \""
				+ Keyword.convertKeywordsToString(news.getKeywords()) + "\")";
		executeUpdateStatement(sql);
	}
	
	public JSONObject getNewsById(String newsId, boolean isFavorite) {
		try {
			String sql = "SELECT * from "
					+ "restaurants where news_id='" + newsId + "'";
			ResultSet rs = executeFetchStatement(sql);
			if (rs.next()) {
				News news = new News(
						rs.getString("news_id"),
						rs.getString("title"),
						rs.getString("url"),
						rs.getString("snippet"),
						rs.getString("image_url"),
						Keyword.convertStrToKeywords(rs.getString("keywords")));
				JSONObject obj = news.toJSONObject();
				obj.put("is_favorite", isFavorite);
				return obj;
			}
		} catch (Exception e) { /* report an error */
			System.out.println(e.getMessage());
		}
		return null;
	}

	public void setFavoriteNews(String userId, List<String> newsIds) {
		String sql = "";
		for (String newsId : newsIds) {
			sql = "INSERT INTO horizon_favorite (`user_id`, `news_id`) VALUES (\""
					+ userId + "\", \"" + newsId + "\")";
			executeUpdateStatement(sql);
		}
	}
	
	public void unsetFavoriteNews(String userId, List<String> newsIds) {
		for (String newsId : newsIds) {
			executeUpdateStatement("DELETE FROM horizon_favorite WHERE `user_id`=\""
					+ userId + "\" and `news_id` = \"" + newsId + "\"");
		}
	}
	
	public Set<String> getFavoriteNewsId(String userId) {
		return executeQueryStatement(userId, "user_id", "news_id", "horizon_favorite");
	}
	
	private List<String> getKeywords(String newsId) {
		Set<String> keywordSet = executeQueryStatement(newsId, "news_id",
				"keywords", "horizon_news");
		for (String keywords : keywordSet) {
			return Keyword.convertStrToKeywords(keywords);
		}
		return null;
	}
	
	public List<Keyword> getFavoriteKeywords(String userId) {
		List<Keyword> keywords = new ArrayList<>();
		try {
			if (conn == null) {
				return keywords;
			}

			Set<String> newsIds = getFavoriteNewsId(userId);
			Map<String, Integer> frequencyMap = new HashMap<>();
			for (String newsId : newsIds) {
				for (String keyword : getKeywords(newsId)) {
					if (!frequencyMap.containsKey(keyword)) {
						frequencyMap.put(keyword, 0);
					}
					frequencyMap.put(keyword, 1 + frequencyMap.get(keyword));
				}
			}
			for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
				keywords.add(new Keyword(entry.getKey(), entry.getValue()));
			}
			Collections.sort(keywords);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keywords;
	}
}