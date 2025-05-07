package jsonparser;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class RecursiveJsonParser {
  private int position = 0;
  private String json;

  public Object parse(String jsonString) {
    this.json = jsonString.trim();
    this.position = 0;
    Object result = parseValue();

    // Ensure the entire JSON string has been fully parsed with no extra characters
    skipWhitespace();
    if (position < json.length()) {
      throw new RuntimeException("Unexpected content after JSON at position " + position);
    }

    return result;
  }

  private Object parseValue() {
    char c = skipWhitespace();

    if (c == '{') {
      return parseObject();
    } else if (c == '[') {
      return parseArray();
    } else if (c == '"') {
      return parseString();
    } else if (c == 't' || c == 'f') {
      return parseBoolean();
    } else if (c == 'n') {
      return parseNull();
    } else if ((c >= '0' && c <= '9') || c == '-') {
      return parseNumber();
    }

    throw new RuntimeException("Unexpected character: " + c);
  }

  private Map<String, Object> parseObject() {
    Map<String, Object> map = new HashMap<>();
    position++; // Skip the opening '{'

    while (position < json.length()) {
      char c = skipWhitespace();

      if (c == '}') {
        position++; // Skip the closing '}'
        return map;
      }

      if (c != '"') {
        throw new RuntimeException("Expected '\"' at position " + position);
      }

      String key = parseString();

      c = skipWhitespace();
      if (c != ':') {
        throw new RuntimeException("Expected ':' at position " + position);
      }
      position++; // Skip the ':'

      Object value = parseValue();
      map.put(key, value);

      c = skipWhitespace();
      if (c == ',') {
        position++; // Skip the ','

        // Check for trailing comma ',}' case
        c = skipWhitespace();
        if (c == '}') {
          throw new RuntimeException("Trailing comma not allowed at position " + (position - 1));
        }
      } else if (c != '}') {
        throw new RuntimeException("Expected ',' or '}' at position " + position);
      }
    }

    throw new RuntimeException("Unterminated object");
  }

  private List<Object> parseArray() {
    List<Object> list = new ArrayList<>();
    position++; // Skip the opening '['

    while (position < json.length()) {
      char c = skipWhitespace();

      if (c == ']') {
        position++; // Skip the closing ']'
        return list;
      }

      Object value = parseValue();
      list.add(value);

      c = skipWhitespace();
      if (c == ',') {
        position++; // Skip the ','

        // Check for trailing comma ',]' case
        c = skipWhitespace();
        if (c == ']') {
          throw new RuntimeException("Trailing comma not allowed at position " + (position - 1));
        }
      } else if (c != ']') {
        throw new RuntimeException("Expected ',' or ']' at position " + position);
      }
    }

    throw new RuntimeException("Unterminated array");
  }

  private String parseString() {
    StringBuilder sb = new StringBuilder();
    position++; // Skip the opening '"'

    boolean escaped = false;
    while (position < json.length()) {
      char c = json.charAt(position++);

      if (escaped) {
        // Handle escape characters
        switch (c) {
          case '"':
            sb.append('"');
            break;
          case '\\':
            sb.append('\\');
            break;
          case '/':
            sb.append('/');
            break;
          case 'b':
            sb.append('\b');
            break;
          case 'f':
            sb.append('\f');
            break;
          case 'n':
            sb.append('\n');
            break;
          case 'r':
            sb.append('\r');
            break;
          case 't':
            sb.append('\t');
            break;
          case 'u':
            if (position + 4 <= json.length()) {
              String hex = json.substring(position, position + 4);
              sb.append((char) Integer.parseInt(hex, 16));
              position += 4;
            }
            break;
          default:
            sb.append(c);
        }
        escaped = false;
      } else if (c == '\\') {
        escaped = true;
      } else if (c == '"') {
        return sb.toString();
      } else {
        sb.append(c);
      }
    }

    throw new RuntimeException("Unterminated string");
  }

  private Number parseNumber() {
    int startPos = position;

    if (json.charAt(position) == '-') {
      position++;
    }

    while (position < json.length() && Character.isDigit(json.charAt(position))) {
      position++;
    }

    boolean isFloat = false;

    if (position < json.length() && json.charAt(position) == '.') {
      isFloat = true;
      position++;
      while (position < json.length() && Character.isDigit(json.charAt(position))) {
        position++;
      }
    }

    if (position < json.length() && (json.charAt(position) == 'e' || json.charAt(position) == 'E')) {
      isFloat = true;
      position++;

      if (position < json.length() && (json.charAt(position) == '+' || json.charAt(position) == '-')) {
        position++;
      }

      while (position < json.length() && Character.isDigit(json.charAt(position))) {
        position++;
      }
    }

    String numStr = json.substring(startPos, position);

    if (isFloat) {
      return Double.parseDouble(numStr);
    } else {
      try {
        return Integer.parseInt(numStr);
      } catch (NumberFormatException e) {
        try {
          return Long.parseLong(numStr);
        } catch (NumberFormatException e2) {
          return Double.parseDouble(numStr);
        }
      }
    }
  }

  private Boolean parseBoolean() {
    if (json.charAt(position) == 't') {
      if (position + 3 < json.length() &&
          json.substring(position, position + 4).equals("true")) {
        position += 4;
        return Boolean.TRUE;
      }
    } else if (json.charAt(position) == 'f') {
      if (position + 4 < json.length() &&
          json.substring(position, position + 5).equals("false")) {
        position += 5;
        return Boolean.FALSE;
      }
    }

    throw new RuntimeException("Invalid boolean value at position " + position);
  }

  private Object parseNull() {
    if (position + 3 < json.length() &&
        json.substring(position, position + 4).equals("null")) {
      position += 4;
      return null;
    }

    throw new RuntimeException("Invalid null value at position " + position);
  }

  private char skipWhitespace() {
    while (position < json.length()) {
      char c = json.charAt(position);
      if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
        position++;
      } else {
        return c;
      }
    }
    return '\0';
  }
}
