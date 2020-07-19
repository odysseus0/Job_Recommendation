package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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
    mapper = new ObjectMapper();
    item = Item.builder().itemId("123").name("Jesus").address("Heaven").build();
    items = new ArrayList<>();
    items.addAll(List.of(item, item));
    sampleJson = JsonNodeFactory.instance.objectNode();
    sampleJson.put("id", "123");
    sampleJson.put("title", "God");
    sampleJson.put("randomShit", "shit");
    sampleItem = Item.builder().itemId("123").name("God").keywords(new HashSet<>()).build();
  }

  @Test
  void correctFieldNamesInSerialization() throws IOException {
    JsonNode jsonNode = mapper.readTree(new File("src/test/resources/entity/item1.json"));
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
  void deserializeSampleJSON() throws IOException {
    JsonNode node = mapper.readTree(new File("src/test/resources/entity/item1.json"));
    Item item = mapper.readValue(new File("src/test/resources/entity/item1.json"), Item.class);
    Item expected = Item.builder()
        .itemId(node.get("id").textValue())
        .name(node.get("title").textValue())
        .address(node.get("location").textValue())
        .keywords(new HashSet<>())
        .imageUrl(node.get("company_logo").textValue())
        .url(node.get("url").textValue())
        .build();
    assertEquals(item, expected);
  }

  @Test
  void serializeSampleJSON() throws IOException {
    Item item = mapper.readValue(new File("src/test/resources/entity/item1.json"), Item.class);
    System.out.println(mapper.valueToTree(item).toPrettyString());
  }
}