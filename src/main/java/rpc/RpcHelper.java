package rpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Item;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

public class RpcHelper {

  // Writes a JSONNode to http response.
  public static void writeJsonNode(HttpServletResponse response, JsonNode node)
      throws IOException {
    response.setContentType("application/json");
    response.getWriter().print(node);
  }

  // Convert a JSON Object to Item Object
  public static Item parseFavoriteItem(JsonNode favoriteItem) throws JsonProcessingException {
    return new ObjectMapper().treeToValue(favoriteItem, Item.class);
  }

}
