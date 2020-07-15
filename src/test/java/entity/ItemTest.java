package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItemTest {

  private Item item;
  private List<Item> items;
  private ObjectMapper mapper;
  private ObjectNode sampleJson;
  private Item sampleItem;

  @BeforeEach
  void setUp() {
    item = Item.builder().itemId("123").name("Jesus").address("Heaven").build();
    mapper = new ObjectMapper();
    items = new ArrayList<>();
    items.addAll(List.of(item, item));
    sampleJson = JsonNodeFactory.instance.objectNode();
    sampleJson.put("id", "123");
    sampleJson.put("title", "God");
    sampleJson.put("randomShit", "shit");
    sampleItem = Item.builder().itemId("123").name("God").build();
  }

  @Test
  void whenSerializeAndDeserializeUsingJackson_thenCorrect() throws JsonProcessingException {
    String jsonStr = mapper.writeValueAsString(item);
    Item result = mapper.readValue(jsonStr, Item.class);
    assertEquals(item, result);
  }

  @Test
  void deserializeUnknownProperties() throws JsonProcessingException {
    Item result = mapper.readValue(sampleJson.toString(), Item.class);
    assertEquals(result, sampleItem);
  }

  @Test
  void serializeListOfItems() {
    ArrayNode expected = mapper.createArrayNode();
    expected.add(mapper.valueToTree(item));
    expected.add(mapper.valueToTree(item));
    assertEquals(expected, mapper.valueToTree(items));
  }

  @Test
  void deserializeIntoBuilder() throws JsonProcessingException {
    Item item = mapper.readValue(sampleJson.toString(), Item.ItemBuilder.class).build();
    assertEquals(item, sampleItem);
  }
}