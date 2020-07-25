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

@WebServlet(
    name = "Register",
    urlPatterns = {"/register"})
public class Register extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    JsonNode input = new ObjectMapper().readTree(request.getReader());
    String userId = input.get("user_id").textValue();
    String password = input.get("password").textValue();
    String firstname = input.get("first_name").textValue();
    String lastname = input.get("last_name").textValue();

    MySQLConnection connection = new MySQLConnection();
    ObjectNode obj = JsonNodeFactory.instance.objectNode();
    if (connection.addUser(userId, password, firstname, lastname)) {
      obj.put("status", "OK");
    } else {
      obj.put("status", "User Already Exists");
    }
    connection.close();
    RpcHelper.writeJsonNode(response, obj);
  }
}
