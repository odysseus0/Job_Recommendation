package entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.Set;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "ItemBuilder")
@JsonInclude(Include.NON_NULL)
@JsonDeserialize(builder = Item.ItemBuilder.class)
public class Item {

  @JsonProperty("id")
  String itemId;
  @JsonProperty("title")
  String name;
  @JsonProperty("location")
  String address;
  Set<String> keywords;
  @JsonProperty("company_logo")
  String imageUrl;
  String url;

  @JsonPOJOBuilder(withPrefix = "")
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ItemBuilder {

  }
}