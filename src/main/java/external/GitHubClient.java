package external;

import static java.net.URLEncoder.encode;

import entity.Item;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class GitHubClient {

  private static final String URL_TEMPLATE =
      "https://jobs.github.com/positions.json?description=%s&lat=%s&long=%s";
  private static final String DEFAULT_KEYWORD = "developer";

  public List<Item> search(double lat, double lon, String keyword) {
    if (keyword == null) {
      keyword = DEFAULT_KEYWORD;
    }
    keyword = encode(keyword, StandardCharsets.UTF_8);
    String url = String.format(URL_TEMPLATE, keyword, lat, lon);
    CloseableHttpClient httpClient = HttpClients.createDefault();

    // Create a custom response handler
    ResponseHandler<List<Item>> responseHandler = response -> {
      if (response.getStatusLine().getStatusCode() != 200) {
        return new ArrayList<>();
      }
      HttpEntity entity = response.getEntity();
      if (entity == null) {
        return new ArrayList<>();
      }
      String responseBody = EntityUtils.toString(entity);
      JSONArray array = new JSONArray(responseBody);
      return getItemList(array);
    };

    try {
      return httpClient.execute(new HttpGet(url), responseHandler);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return new ArrayList<>();
  }

  private List<Item> getItemList(JSONArray array) {
    List<Item> itemList = new ArrayList<>();
    for (int i = 0; i < array.length(); i++) {
      JSONObject object = array.getJSONObject(i);
      Item item = Item.builder()
          .itemId(getStringFieldOrEmpty(object, "id"))
          .name(getStringFieldOrEmpty(object, "title"))
          .address(getStringFieldOrEmpty(object, "location"))
          .url(getStringFieldOrEmpty(object, "url"))
          .imageUrl(getStringFieldOrEmpty(object, "company_logo"))
          .build();
      itemList.add(item);
    }
    return itemList;
  }

  private String getStringFieldOrEmpty(JSONObject obj, String field) {
    return obj.isNull(field) ? "" : obj.getString(field);
  }
}
