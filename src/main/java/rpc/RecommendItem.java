package rpc;

import static rpc.RpcHelper.writeJsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "RecommendItem", urlPatterns = {"/recommend"})
public class RecommendItem extends HttpServlet {

  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public RecommendItem() {
    super();
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    ArrayNode array = objectMapper.createArrayNode();
    array.add(objectMapper.createObjectNode().put("name", "abc").put("address", "San Francisco")
        .put("time", "01/01/2017"));
    array.add(objectMapper.createObjectNode().put("name", "1234").put("address", "San Jose")
        .put("time", "01/01/2017"));
    writeJsonNode(response, array);
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    doGet(request, response);
  }

}