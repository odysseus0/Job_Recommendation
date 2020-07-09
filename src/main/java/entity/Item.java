package entity;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

@Getter
@Builder
public class Item {

  private final String itemId;
  private final String name;
  private final String address;
  private final Set<String> keywords;
  private final String imageUrl;
  private final String url;

  public JSONObject toJSONObject() {
    JSONObject object = new JSONObject();

    object.put("item_id", itemId);
    object.put("name", name);
    object.put("address", address);
    object.put("keywords", new JSONArray(keywords));
    object.put("image_url", imageUrl);
    object.put("url", url);
    return object;
  }
}