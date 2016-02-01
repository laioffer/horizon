package rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;

/**
 * Servlet implementation class SetFavoriteNews
 */
@WebServlet("/favorite")
public class SetFavoriteNews extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final DBConnection connection = new DBConnection();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetFavoriteNews() {
        super();
    }

    /**
     * Handles the GET request to fetch a list of news that the user favors. 
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			JSONArray array = new JSONArray();
			if (request.getParameterMap().containsKey("user_id")) {
				String userId = request.getParameter("user_id");
				Set<String> favoriteSet = connection.getFavoriteNewsId(userId);
				for (String id : favoriteSet) {
					array.put(connection.getNewsById(id, true));
				}
				RpcParser.writeOutput(response, array);
			} else {
				RpcParser.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handles the POST request to add favorite news. 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			JSONObject input = RpcParser.parseInput(request);
			if (input.has("user_id") && input.has("favorite")) {
				String user_id = input.getString("user_id");
				JSONArray array = input.getJSONArray("favorite");
				List<String> favoriteList = new ArrayList<>(); 
				for (int i = 0; i < array.length(); i ++) {
					String newsId = array.getString(i);
					favoriteList.add(newsId);
				}
				connection.setFavoriteNews(user_id, favoriteList);
				RpcParser.writeOutput(response, new JSONObject().put("status", "OK"));
			} else {
				RpcParser.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handles the DELETE request to remove favorite news. 
	 */
	public void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			JSONObject input = RpcParser.parseInput(request);
			if (input.has("user_id") && input.has("favorite")) {
				String userId = (String) input.get("user_id");
				JSONArray array = (JSONArray) input.get("visited");
				List<String> favoriteList = new ArrayList<>();
				for (int i = 0; i < array.length(); i++) {
					String newsId = (String) array.get(i);
					favoriteList.add(newsId);
				}
				connection.unsetFavoriteNews(userId, favoriteList);
				RpcParser.writeOutput(response, new JSONObject().put("status", "OK"));
			} else {
				RpcParser.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
