package recommendation;

import db.MySQLConnection;
import entity.Item;
import external.GitHubClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Recommendation {

  public List<Item> recommendItems(String userId, double lat, double lon) {
    // step 1: get all favorite itemIds
    var connection = new MySQLConnection();
    var favoriteItemIds = connection.getFavoriteItemIds(userId);

    // step 2, get all keywords, sort by count
    Map<String, Long> allKeywords =
        favoriteItemIds.stream()
            .map(connection::getKeywords)
            .flatMap(Set::stream)
            .collect(
                Collectors.groupingBy(Function.identity(), TreeMap::new, Collectors.counting()));
    connection.close();
    List<Entry<String, Long>> keywordList = new ArrayList<>(allKeywords.entrySet());

    // Cut down search list to only top 3
    if (keywordList.size() > 3) {
      keywordList = keywordList.subList(0, 3);
    }

    // Step 3, search based on keywords, filter out favorite items
    GitHubClient client = new GitHubClient();
    return keywordList.stream()
        .map((keyword) -> client.search(lat, lon, keyword.getKey()))
        .flatMap(List::stream)
        .filter(item -> !favoriteItemIds.contains(item.getItemId()))
        .distinct()
        .collect(Collectors.toList());
  }
}
