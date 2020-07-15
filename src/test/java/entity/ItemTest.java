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

  @BeforeEach
  void setUp() {
    item = Item.builder().itemId("123").name("Jesus").address("Heaven").build();
    mapper = new ObjectMapper();
    items = new ArrayList<>();
    items.addAll(List.of(item, item));
  }

  @Test
  void whenSerializeAndDeserializeUsingJackson_thenCorrect() throws JsonProcessingException {
    String jsonStr = mapper.writeValueAsString(item);
    Item result = mapper.readValue(jsonStr, Item.class);
    assertEquals(item.toString(), result.toString());
  }

  @Test
  void deserializeUnknownProperties() throws JsonProcessingException {
    ObjectNode node = JsonNodeFactory.instance.objectNode();
    node.put("id", "123");
    node.put("title", "God");
    node.put("randomShit", "shit");

    Item result = mapper.readValue(node.toString(), Item.class);
    Item expected = Item.builder().itemId("123").name("God").build();
    assertEquals(result.toString(), expected.toString());
  }

  @Test
  void serializeListOfItems() {
    ArrayNode expected = mapper.createArrayNode();
    expected.add(mapper.valueToTree(item));
    expected.add(mapper.valueToTree(item));
    assertEquals(expected, mapper.valueToTree(items));
  }
}