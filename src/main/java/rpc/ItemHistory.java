package rpc;

import static rpc.RpcHelper.parseFavoriteItem;
import static rpc.RpcHelper.writeJsonNode;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import db.MySQLConnection;
import entity.Item;
import java.io.IOException;
import java.util.Set;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ItemHistory", urlPatterns = {"/history"})
public class ItemHistory extends HttpServlet {

  protected void doPost(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    // Parse the favorite item from the input
    JsonNode input = new ObjectMapper().readTree(request.getReader());
    String userId = input.get("user_id").textValue();
    Item item = parseFavoriteItem(input.get("favorite"));
    // Store the user's favorite item into the database
    MySQLConnection connection = new MySQLConnection();
    connection.setFavoriteItems(userId, item);
    connection.close();
    writeSuccessMsg(response);
  }

  private void writeSuccessMsg(HttpServletResponse response) throws IOException {
    writeJsonNode(response,
        JsonNodeFactory.instance.objectNode().put("result", "SUCCESS"));
  }

  protected void doGet(HttpServletRequest request,
      HttpServletResponse response)
      throws IOException {
    String userId = request.getParameter("user_id");
    MySQLConnection connection = new MySQLConnection();
    Set<Item> items = connection.getFavoriteItems(userId);
    connection.close();
    ArrayNode array = JsonNodeFactory.instance.arrayNode(items.size());
    for (Item item : items) {
      ObjectNode obj = new ObjectMapper().valueToTree(item);
      obj.put("favorite", true);
      array.add(obj);
    }
    RpcHelper.writeJsonNode(response, array);
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    MySQLConnection connection = new MySQLConnection();
    JsonNode input = new ObjectMapper().readTree(request.getReader());
    String userId = input.get("user_id").textValue();
    Item item = parseFavoriteItem(input.get("favorite"));
    connection.unsetFavoriteItems(userId, item.getItemId());
    connection.close();
    writeSuccessMsg(response);
  }
}
