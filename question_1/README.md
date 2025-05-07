# JSON Parser - Simple JSON Parser Implementation

## Project Overview

This is a Java implementation of a recursive descent JSON parser. The parser can convert standard JSON strings into Java objects, supporting all standard JSON data types (objects, arrays, strings, numbers, booleans, and null) and capable of handling complex nested structures and escape characters.

## Environment Requirements

- OpenJDK Runtime Environment Temurin-21.0.7+6 (build 21.0.7+6-LTS)
- Gradle 8.14

## Technology Stack

- Java 21
- Gradle 8.14 (build tool)
- JUnit Jupiter (testing framework)
- AssertJ (assertion library)

## Features

- Supports parsing of all standard JSON data types
- Handles nested object and array structures
- Supports all escape characters in JSON strings
- Processes scientific notation and high-precision numbers
- Provides detailed error messages, including the position of invalid JSON
- Capable of processing large JSON files and long strings

## Run Tests

```bash
./gradlew test
```

## Usage

```java
import jsonparser.RecursiveJsonParser;

// Create parser instance
RecursiveJsonParser parser = new RecursiveJsonParser();

// Parse JSON string
String jsonString = "{\"name\": \"Test\", \"age\": 25}";
Object result = parser.parse(jsonString);

// Result will be a Map<String, Object>
Map<String, Object> jsonObject = (Map<String, Object>) result;
String name = (String) jsonObject.get("name");
int age = (int) jsonObject.get("age");
```

## Implementation Details

The parser uses a recursive descent parsing technique, recursively parsing JSON structures to build corresponding Java objects:

- JSON objects are converted to `Map<String, Object>`
- JSON arrays are converted to `List<Object>`
- JSON strings are converted to Java `String`
- JSON numbers are converted to `Integer`, `Long`, or `Double` depending on size and type
- JSON booleans are converted to Java `Boolean`
- JSON null is converted to Java `null`

## Project Structure

- `RecursiveJsonParser.java` - Core parser implementation
- `RecursiveJsonParserTest.java` - Comprehensive unit tests

## Error Handling

The parser throws `RuntimeException` when encountering invalid JSON, with detailed error descriptions and the position where the error occurred, for example:

- Unterminated objects or arrays
- Missing quotation marks, colons, or commas
- Trailing commas
- Invalid value formats
- Unterminated strings
