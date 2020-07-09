package entity;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Item {

  private final String itemId;
  private final String name;
  private final String address;
  private final Set<String> keywords;
  private final String imageUrl;
  private final String url;

}