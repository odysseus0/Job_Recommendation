package rpc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import db.MySQLConnection;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(
    name = "Login",
    urlPatterns = {"/login"})
public class Login extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    JsonNode input = new ObjectMapper().readTree(request.getReader());
    String userId = input.get("user_id").textValue();
    String password = input.get("password").textValue();

    MySQLConnection connection = new MySQLConnection();
    ObjectNode obj = JsonNodeFactory.instance.objectNode();
    if (connection.verifyLogin(userId, password)) {
      HttpSession session = request.getSession();
      session.setAttribute("user_id", userId);
      obj.put("status", "OK").put("user_id", userId).put("name", connection.getFullname(userId));
    } else {
      obj.put("status", "User Doesn't Exist");
      response.setStatus(401);
    }
    connection.close();
    RpcHelper.writeJsonNode(response, obj);
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    HttpSession session = request.getSession(false);
    ObjectNode obj = JsonNodeFactory.instance.objectNode();
    if (session != null) {
      MySQLConnection connection = new MySQLConnection();
      String userId = session.getAttribute("user_id").toString();
      obj.put("status", "OK").put("user_id", userId).put("name", connection.getFullname(userId));
      connection.close();
    } else {
      obj.put("status", "Invalid Session");
      response.setStatus(403);
    }
    RpcHelper.writeJsonNode(response, obj);
  }
}
