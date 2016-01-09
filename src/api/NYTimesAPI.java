package api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class NYTimesAPI {

	private static final String URL = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
	private static final String API_KEY = "71938bd80d6e287ae6e09e2599fbd012%3A15%3A73961429";

	public static JSONArray searchNews(String keyword) {
		try {
			// Follow the requirement of NYTimes API and convert space to +.
			keyword = keyword.replaceAll(" ", "+");

			String url = URL + "?q=" + keyword + "&api-key=" + API_KEY;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			int responseCode = con.getResponseCode();
			System.out
					.println("\nNYTimes API is sending 'GET' request to URL :\n"
							+ url);
			System.out.println("\nNYTimes API Response Code : " + responseCode);

			if (responseCode != 200) {
				System.out.println("\nNYTimes API cannot connect to NYTimes.");
				return null;
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			return new JSONObject(response.toString())
					.getJSONObject("response").getJSONArray("docs");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
