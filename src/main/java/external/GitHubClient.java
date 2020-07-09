package external;

import static java.net.URLEncoder.encode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

public class GitHubClient {

  private static final String URL_TEMPLATE =
      "https://jobs.github.com/positions.json?description=%s&lat=%s&long=%s";
  private static final String DEFAULT_KEYWORD = "developer";

  public JSONArray search(double lat, double lon, String keyword) {
    if (keyword == null) {
      keyword = DEFAULT_KEYWORD;
    }
    keyword = encode(keyword, StandardCharsets.UTF_8);
    String url = String.format(URL_TEMPLATE, keyword, lat, lon);
    CloseableHttpClient httpClient = HttpClients.createDefault();

    // Create a custom response handler
    ResponseHandler<JSONArray> responseHandler = response -> {
      if (response.getStatusLine().getStatusCode() != 200) {
        return new JSONArray();
      }
      HttpEntity entity = response.getEntity();
      if (entity == null) {
        return new JSONArray();
      }
      return new JSONArray(EntityUtils.toString(entity));
    };

    try {
      return httpClient.execute(new HttpGet(url), responseHandler);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return new JSONArray();
  }
}
