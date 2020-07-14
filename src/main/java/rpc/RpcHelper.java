package rpc;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

public class RpcHelper {

  // Writes a JSONNode to http response.
  public static void writeJsonNode(HttpServletResponse response, JsonNode node)
      throws IOException {
    response.setContentType("application/json");
    response.getWriter().print(node);
  }
}
