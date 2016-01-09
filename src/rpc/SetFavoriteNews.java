package rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetFavoriteNews() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			DBConnection connection = new DBConnection();
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
			}

			JSONArray array = new JSONArray();
			array.put("success");
			RpcParser.parseOutput(response, array);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
