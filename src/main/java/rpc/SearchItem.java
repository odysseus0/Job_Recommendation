package rpc;

import static rpc.RpcHelper.writeJsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import db.MySQLConnection;
import entity.Item;
import external.GitHubClient;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
    name = "SearchItem",
    urlPatterns = {"/search"})
public class SearchItem extends HttpServlet {

  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public SearchItem() {
    super();
  }

  /** @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response) */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String userId = request.getParameter("user_id");
    double lat = Double.parseDouble(request.getParameter("lat"));
    double lon = Double.parseDouble(request.getParameter("lon"));

    GitHubClient client = new GitHubClient();
    List<Item> items = client.search(lat, lon, null);

    MySQLConnection connection = new MySQLConnection();
    Set<String> favoriteItemIds = connection.getFavoriteItemIds(userId);
    connection.close();

    ArrayNode array = JsonNodeFactory.instance.arrayNode();
    for (Item item : items) {
      ObjectNode obj = new ObjectMapper().valueToTree(item);
      obj.put("favorite", favoriteItemIds.contains(item.getItemId()));
      array.add(obj);
    }
    writeJsonNode(response, array);
  }

  /** @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response) */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    doGet(request, response);
  }
}
