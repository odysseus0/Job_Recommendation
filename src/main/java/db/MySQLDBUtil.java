package db;

public class MySQLDBUtil {

  public static final String DB_NAME = "laiproject";
  private static final String INSTANCE = "laiproject-instance.ccbzebvorape.us-east-2.rds.amazonaws.com";
  private static final String PORT_NUM = "3306";
  private static final String USERNAME = "admin";
  private static final String PASSWORD = "oy9Nq9m484224,Q";
  public static final String URL = "jdbc:mysql://"
      + INSTANCE + ":" + PORT_NUM + "/" + DB_NAME
      + "?user=" + USERNAME + "&password=" + PASSWORD
      + "&autoReconnect=true&serverTimezone=UTC";
}
