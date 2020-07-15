package external;

import static java.net.URLEncoder.encode;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Item;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      JsonNode array = objectMapper.readTree(new URL(url));
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
      if (description.equals("") || description.equals("\n")) {
        descriptionList.add(getStringFieldOrEmpty(jsonNode, "title"));
      } else {
        descriptionList.add(description);
      }
    }

    // We need to get keywords from multiple text in one request since
    // MonkeyLearnAPI has limitations on request per minute.
    List<List<String>> keywords = MonkeyLearnClient
        .extractKeywords(descriptionList.toArray(new String[0]));

    for (int i = 0; i < array.size(); i++) {
      JsonNode object = array.get(i);
      Item item = Item.builder()
          .itemId(getStringFieldOrEmpty(object, "id"))
          .name(getStringFieldOrEmpty(object, "title"))
          .address(getStringFieldOrEmpty(object, "location"))
          .url(getStringFieldOrEmpty(object, "url"))
          .imageUrl(getStringFieldOrEmpty(object, "company_logo"))
          .keywords(new HashSet<>(keywords.get(i)))
          .build();
      itemList.add(item);
    }

    return itemList;
  }

  private String getStringFieldOrEmpty(JsonNode node, String field) {
    return node.hasNonNull(field) ? node.get(field).textValue() : "";
  }
}
