package rpc;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Keyword;
import model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.NYTimesAPI;
import db.DBConnection;

/**
 * Servlet implementation class RecommendNews
 */
@WebServlet("/recommendation")
public class RecommendNews extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int MAX_RECOMMENDED_NEWS = 20;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RecommendNews() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			DBConnection connection = new DBConnection();
			HashSet<News> allNews = new HashSet<>();

			if (request.getParameterMap().containsKey("user_id")) {
				String userId = request.getParameter("user_id");
				Set<String> favoriteNewsId = connection.getFavoriteNewsId(userId);
				List<Keyword> keywords = connection.getFavoriteKeywords(userId);

				for (Keyword keyword : keywords) {
					if (allNews.size() > MAX_RECOMMENDED_NEWS) {
						break;
					}
					JSONArray jsonArray = NYTimesAPI.searchNews(keyword
							.getKey());
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);						
						News news = new News(jsonObject);
						if (!favoriteNewsId.contains(news.getId())) {
							allNews.add(new News(jsonObject));
						}
					}
				}
			}
			RpcParser.writeOutput(response, new JSONArray(allNews));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}
}
