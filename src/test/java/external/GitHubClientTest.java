package external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import org.junit.jupiter.api.Test;

class GitHubClientTest {

  private static final String URL_TEMPLATE =
      "https://jobs.github.com/positions.json?description=%s&lat=%s&long=%s";
  private static final String DEFAULT_KEYWORD = "developer";
  private static final double lat = 37.38;
  private static final double lon = -122.08;

  @Test
  void search() {
    GitHubClient gitHubClient = new GitHubClient();
    gitHubClient.search(lat, lon, null);
  }

  @Test
  void setupJsonNodeUsingURL() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    String url = String.format(URL_TEMPLATE, DEFAULT_KEYWORD, lat, lon);
    JsonNode node = objectMapper.readTree(new URL(url));
    System.out.println(url);
    System.out.println(node.toPrettyString());
  }
}