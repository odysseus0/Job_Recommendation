package rpc;

import static rpc.RpcHelper.writeJsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import entity.Item;
import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import recommendation.Recommendation;

@WebServlet(
    name = "RecommendItem",
    urlPatterns = {"/recommendation"})
public class RecommendItem extends HttpServlet {

  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public RecommendItem() {
    super();
  }

  /** @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response) */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    HttpSession session = request.getSession(false);
    if (session == null) {
      response.setStatus(403);
      return;
    }
    String userId = String.valueOf(session.getAttribute("user_id"));

    double lat = Double.parseDouble(request.getParameter("lat"));
    double lon = Double.parseDouble(request.getParameter("lon"));

    Recommendation recommendation = new Recommendation();
    List<Item> items = recommendation.recommendItems(userId, lat, lon);
    ArrayNode array = JsonNodeFactory.instance.arrayNode();
    for (Item item : items) {
      array.add(new ObjectMapper().valueToTree(item));
    }
    writeJsonNode(response, array);
  }

  /** @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response) */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    doGet(request, response);
  }
}
