package jsonparser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class RecursiveJsonParserTest {

  private final RecursiveJsonParser parser = new RecursiveJsonParser();

  @Test
  @DisplayName("Basic JSON object parsing")
  void testBasicJsonObject() {
    String json = "{\"name\": \"Test\", \"age\": 25}";
    Object result = parser.parse(json);

    assertThat(result).isInstanceOf(Map.class);
    Map<String, Object> map = (Map<String, Object>) result;

    assertThat(map).hasSize(2);
    assertThat(map).containsEntry("name", "Test");
    assertThat(map).containsEntry("age", 25);
  }

  @Test
  @DisplayName("Basic JSON array parsing")
  void testBasicJsonArray() {
    String json = "[\"Reading\", \"Sports\", \"Travel\"]";
    Object result = parser.parse(json);

    assertThat(result).isInstanceOf(List.class);
    List<Object> list = (List<Object>) result;

    assertThat(list).hasSize(3);
    assertThat(list).containsExactly("Reading", "Sports", "Travel");
  }

  @Test
  @DisplayName("Complex nested JSON parsing")
  void testComplexNestedJson() {
    String json = "{\n" +
        "  \"person\": {\n" +
        "    \"name\": \"John\",\n" +
        "    \"age\": 30,\n" +
        "    \"isStudent\": false,\n" +
        "    \"address\": {\n" +
        "      \"city\": \"Taipei\",\n" +
        "      \"zipcode\": \"10001\"\n" +
        "    },\n" +
        "    \"scores\": [85, 90, 78]\n" +
        "  },\n" +
        "  \"status\": \"active\",\n" +
        "  \"nullValue\": null\n" +
        "}";

    Object result = parser.parse(json);
    assertThat(result).isInstanceOf(Map.class);
    Map<String, Object> map = (Map<String, Object>) result;

    assertThat(map).hasSize(3);
    assertThat(map).containsKeys("person", "status", "nullValue");
    assertThat(map.get("status")).isEqualTo("active");
    assertThat(map.get("nullValue")).isNull();

    // Check person object
    assertThat(map.get("person")).isInstanceOf(Map.class);
    Map<String, Object> person = (Map<String, Object>) map.get("person");
    assertThat(person).hasSize(5);
    assertThat(person).containsEntry("name", "John");
    assertThat(person).containsEntry("age", 30);
    assertThat(person).containsEntry("isStudent", false);

    // Check address object
    assertThat(person.get("address")).isInstanceOf(Map.class);
    Map<String, Object> address = (Map<String, Object>) person.get("address");
    assertThat(address).hasSize(2);
    assertThat(address).containsEntry("city", "Taipei");
    assertThat(address).containsEntry("zipcode", "10001");

    // Check scores array
    assertThat(person.get("scores")).isInstanceOf(List.class);
    List<Object> scores = (List<Object>) person.get("scores");
    assertThat(scores).hasSize(3);
    assertThat(scores).containsExactly(85, 90, 78);
  }

  @Test
  @DisplayName("Number types parsing")
  void testNumberTypes() {
    String json = "{\n" +
        "  \"integer\": 123,\n" +
        "  \"negative\": -456,\n" +
        "  \"float\": 78.9,\n" +
        "  \"scientific\": 1.23e4,\n" +
        "  \"scientificNegative\": -5.67e-2,\n" +
        "  \"bigInteger\": 9223372036854775807\n" +
        "}";

    Object result = parser.parse(json);
    assertThat(result).isInstanceOf(Map.class);
    Map<String, Object> map = (Map<String, Object>) result;

    assertThat(map).hasSize(6);
    assertThat(map.get("integer")).isEqualTo(123);
    assertThat(map.get("negative")).isEqualTo(-456);
    assertThat(map.get("float")).isEqualTo(78.9);
    assertThat(map.get("scientific")).isEqualTo(12300.0);
    assertThat(map.get("scientificNegative")).isEqualTo(-0.0567);
    assertThat(map.get("bigInteger")).isEqualTo(9223372036854775807L);
  }

  @Test
  @DisplayName("Complex array parsing")
  void testComplexArray() {
    String json = "[\n" +
        "  \"String\",\n" +
        "  123,\n" +
        "  true,\n" +
        "  null,\n" +
        "  {\"key\": \"value\"},\n" +
        "  [1, 2, 3]\n" +
        "]";

    Object result = parser.parse(json);
    assertThat(result).isInstanceOf(List.class);
    List<Object> list = (List<Object>) result;

    assertThat(list).hasSize(6);
    assertThat(list.get(0)).isEqualTo("String");
    assertThat(list.get(1)).isEqualTo(123);
    assertThat(list.get(2)).isEqualTo(true);
    assertThat(list.get(3)).isNull();

    // Check embedded object
    assertThat(list.get(4)).isInstanceOf(Map.class);
    Map<String, Object> map = (Map<String, Object>) list.get(4);
    assertThat(map).hasSize(1);
    assertThat(map).containsEntry("key", "value");

    // Check embedded array
    assertThat(list.get(5)).isInstanceOf(List.class);
    List<Object> nestedList = (List<Object>) list.get(5);
    assertThat(nestedList).hasSize(3);
    assertThat(nestedList).containsExactly(1, 2, 3);
  }

  @Test
  @DisplayName("Escape characters parsing")
  void testEscapeCharacters() {
    String json = "{\n" +
        "  \"escaped\": \"Quotes\\\"，Backslash\\\\，Slash\\/，Backspace\\b，Form feed\\f，New line\\n，Carriage return\\r，Tab\\t\",\n"
        +
        "  \"unicode\": \"\\u4F60\\u597D\"\n" +
        "}";

    Object result = parser.parse(json);
    assertThat(result).isInstanceOf(Map.class);
    Map<String, Object> map = (Map<String, Object>) result;

    assertThat(map).hasSize(2);
    assertThat(map.get("escaped")).asString().contains("Quotes\"");
    assertThat(map.get("escaped")).asString().contains("Backslash\\");
    assertThat(map.get("escaped")).asString().contains("Slash/");
    assertThat(map.get("unicode")).isEqualTo("你好");
  }

  @Test
  @DisplayName("Empty objects and arrays")
  void testEmptyObjectsAndArrays() {
    // Test empty object
    Object emptyObject = parser.parse("{}");
    assertThat(emptyObject).isInstanceOf(Map.class);
    assertThat((Map<?, ?>) emptyObject).isEmpty();

    // Test empty array
    Object emptyArray = parser.parse("[]");
    assertThat(emptyArray).isInstanceOf(List.class);
    assertThat((List<?>) emptyArray).isEmpty();
  }

  @Test
  @DisplayName("JSON with whitespace and line breaks")
  void testWhitespaceHandling() {
    String json = " { \n \"key\" \t:\r\n \"value\" \n } ";

    Object result = parser.parse(json);
    assertThat(result).isInstanceOf(Map.class);
    Map<String, Object> map = (Map<String, Object>) result;

    assertThat(map).hasSize(1);
    assertThat(map).containsEntry("key", "value");
  }

  @Test
  @DisplayName("Deeply nested structure")
  void testDeeplyNestedStructure() {
    String json = "[[[[[[\"Deeply nested\"]]]]], {\"a\": {\"b\": {\"c\": {\"d\": {\"e\": \"Deeply nested\"}}}}}]";

    Object result = parser.parse(json);
    assertThat(result).isInstanceOf(List.class);
    List<Object> outerList = (List<Object>) result;

    assertThat(outerList).hasSize(2);

    // Check first element (nested array)
    assertThat(outerList.get(0)).isInstanceOf(List.class);
    List<Object> level1 = (List<Object>) outerList.get(0);
    assertThat(level1).hasSize(1);

    List<Object> level2 = (List<Object>) level1.get(0);
    assertThat(level2).hasSize(1);

    List<Object> level3 = (List<Object>) level2.get(0);
    assertThat(level3).hasSize(1);

    List<Object> level4 = (List<Object>) level3.get(0);
    assertThat(level4).hasSize(1);

    List<Object> level5 = (List<Object>) level4.get(0);
    assertThat(level5).hasSize(1);

    assertThat(level5.get(0)).isEqualTo("Deeply nested");

    // Check second element (nested object)
    assertThat(outerList.get(1)).isInstanceOf(Map.class);
    Map<String, Object> objA = (Map<String, Object>) outerList.get(1);
    Map<String, Object> objB = (Map<String, Object>) objA.get("a");
    Map<String, Object> objC = (Map<String, Object>) objB.get("b");
    Map<String, Object> objD = (Map<String, Object>) objC.get("c");
    Map<String, Object> objE = (Map<String, Object>) objD.get("d");

    assertThat(objE).containsEntry("e", "Deeply nested");
  }

  @Test
  @DisplayName("Long string parsing")
  void testLongString() {
    String longString = "Very long string".repeat(100);
    String json = "{\"longString\": \"" + longString + "\"}";

    Object result = parser.parse(json);
    assertThat(result).isInstanceOf(Map.class);
    Map<String, Object> map = (Map<String, Object>) result;

    assertThat(map).hasSize(1);
    assertThat(map.get("longString")).isEqualTo(longString);
  }

  @Test
  @DisplayName("High precision number parsing")
  void testHighPrecisionNumber() {
    String json = "{\"preciseNumber\": 1.23456789012345678901234567890}";

    Object result = parser.parse(json);
    assertThat(result).isInstanceOf(Map.class);
    Map<String, Object> map = (Map<String, Object>) result;

    assertThat(map).hasSize(1);
    assertThat(map.get("preciseNumber")).isInstanceOf(Double.class);
    // Check precision range (due to floating point precision limitations, not
    // exactly equal)
    assertThat((Double) map.get("preciseNumber")).isBetween(1.234567, 1.234568);
  }

  @Test
  @DisplayName("Large JSON parsing")
  void testLargeJson() {
    // Create an array with 100 objects
    StringBuilder largeJson = new StringBuilder("[\n");
    for (int i = 0; i < 100; i++) {
      largeJson.append("  {\"index\": ").append(i).append(", \"value\": \"Test\"");
      if (i < 99) {
        largeJson.append("},\n");
      } else {
        largeJson.append("}\n");
      }
    }
    largeJson.append("]");

    Object result = parser.parse(largeJson.toString());
    assertThat(result).isInstanceOf(List.class);
    List<Object> list = (List<Object>) result;

    assertThat(list).hasSize(100);

    // Check first and last elements
    assertThat(list.get(0)).isInstanceOf(Map.class);
    Map<String, Object> firstItem = (Map<String, Object>) list.get(0);
    assertThat(firstItem).containsEntry("index", 0);
    assertThat(firstItem).containsEntry("value", "Test");

    Map<String, Object> lastItem = (Map<String, Object>) list.get(99);
    assertThat(lastItem).containsEntry("index", 99);
    assertThat(lastItem).containsEntry("value", "Test");
  }

  // Parameterized tests - test various invalid JSON
  @ParameterizedTest(name = "Invalid JSON test: {0}")
  @MethodSource("invalidJsonProvider")
  void testInvalidJson(String testName, String invalidJson, String expectedError) {
    Exception exception = assertThrows(RuntimeException.class, () -> parser.parse(invalidJson));
    assertThat(exception.getMessage()).contains(expectedError);
  }

  // Data source for invalid JSON test cases
  private static Stream<Arguments> invalidJsonProvider() {
    return Stream.of(
        Arguments.of("Incomplete JSON", "{\"key\": \"value\"", "Expected ',' or '}'"),
        Arguments.of("Missing quotation marks", "{key: \"value\"}", "Expected '\"'"),
        Arguments.of("Invalid value format", "{\"key\": tru}", "Invalid boolean value"),
        Arguments.of("Extra comma (object)", "{\"key\": \"value\",}", "Trailing comma"),
        Arguments.of("Extra comma (array)", "[1, 2, 3,]", "Trailing comma"),
        Arguments.of("Missing colon", "{\"key\" \"value\"}", "Expected ':'"),
        Arguments.of("Missing value in object", "{\"key\":}", "Unexpected character: }"),
        Arguments.of("Invalid character", "{\"key\": @}", "Unexpected character: @"),
        Arguments.of("Unterminated string", "{\"key\": \"value}", "Unterminated string"),
        Arguments.of("Extra characters", "{\"key\": \"value\"} extra", "Unexpected content"),
        Arguments.of("Missing value in array", "[1, , 3]", "Unexpected character: ,"),
        Arguments.of("Extra colon", "{\"key\":: \"value\"}", "Unexpected character"),
        Arguments.of("Extra colon after value", "{\"key\": \"value\":}", "Expected"));
  }
}
