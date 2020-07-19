package external;

import static java.net.URLEncoder.encode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Item;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

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
    try {
      JsonNode array = new ObjectMapper().readTree(new URL(url));
      return getItemList(array);
    } catch (IOException e) {
      return new ArrayList<>();
    }
  }

  private List<Item> getItemList(JsonNode array) {
    List<Item> itemList = new ArrayList<>();
    List<String> descriptionList = new ArrayList<>();

    for (JsonNode jsonNode : array) {
      // We need to extract keywords from description since GitHub API
      // doesn't return keywords.
      String description = getStringFieldOrEmpty(jsonNode, "description");
      if (description.isBlank()) {
        descriptionList.add(getStringFieldOrEmpty(jsonNode, "title"));
      } else {
        descriptionList.add(description);
      }
    }

    // We need to get keywords from multiple text in one request since
    // MonkeyLearnAPI has limitations on request per minute.
    List<List<String>> keywords = MonkeyLearnClient
        .extractKeywords(descriptionList.toArray(new String[0]));
    ObjectMapper mapper = new ObjectMapper();
    for (int i = 0; i < array.size(); i++) {
      try {
        Item item = mapper.treeToValue(array.get(i), Item.class);
        item.setKeywords(new HashSet<>(keywords.get(i)));
        itemList.add(item);
      } catch (JsonProcessingException ignored) {
      }
    }
    return itemList;
  }
  private String getStringFieldOrEmpty(JsonNode node, String field) {
    return Objects.toString(node.get(field), "");
  }
}
