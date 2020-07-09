package rpc;

import static rpc.RpcHelper.writeJsonArray;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

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
    JSONArray array = new JSONArray();
    array.put(new JSONObject().put("name", "abc").put("address", "San Francisco")
        .put("time", "01/01/2017"));
    array.put(
        new JSONObject().put("name", "1234").put("address", "San Jose").put("time", "01/01/2017"));
    writeJsonArray(response, array);
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    doGet(request, response);
  }

}