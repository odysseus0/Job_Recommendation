package rpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import entity.Item;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RpcHelper {

  // Writes a JSONNode to http response.
  public static void writeJsonNode(HttpServletResponse response, JsonNode node) throws IOException {
    response.setContentType("application/json");
    response.getWriter().print(node);
  }

  // Convert a JSON Object to Item Object
  public static Item parseFavoriteItem(JsonNode favoriteItem) throws JsonProcessingException {
    return new ObjectMapper().treeToValue(favoriteItem, Item.class);
  }

  public static boolean validateLogin(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      response.setStatus(403);
      return false;
    }
    return true;
  }

  public static void writeSuccessMsg(HttpServletResponse response) throws IOException {
    writeJsonNode(response, JsonNodeFactory.instance.objectNode().put("result", "SUCCESS"));
  }
}
