# Question 2

Code changes tested with Java OpenJDK Runtime Environment Temurin-21.0.7+6 (build 21.0.7+6-LTS)

## Issues and Solutions

1. **Shallow vs Deep Copy in Constructor**

   - Issue: The constructor uses shallow copy instead of deep copy for parameters. This allows external code to modify private members without going through the class's provided interfaces.
   - Solution: Check for null and perform deep cloning

2. **Inefficient Data Structure**

   - Issue: Internal storage uses only List, which is inefficient for current operations (e.g removeString, containsNumber)
   - Solution: Use LinkedHashSet for O(1) access while preserving insertion order and check existence.

3. **Incomplete Equals Method**

   - Issue: The `equals` method only compares object names instead of performing deep comparison
   - Solution: Rewrite equals and hashCode methods. Manual calculation instead of converting to List to avoid allocating extra space. Use `Objects.equals(a, b)` instead of `a.equals(b)` for better null handling.

4. **Inefficient Date Comparison**

   - Issue: `isHistoric` creates a new Date object for each comparison
   - Solution: Use Unix epoch timestamp comparison to avoid creating new objects

5. **Incomplete toString Method**

   - Issue: The `toString` method only prints `m_name` and `m_numbers`
   - Solution: Serialize the entire instance, including name, date, numbers, and strings

6. **Mutable Date is Unsafe**

   - Issue: Date is mutable and could be accidentally modified
   - Solution: Use immutable Instant to replace Date

## Testing

1. Compile the code:

```bash
javac -d bin src/*.java
```

2. Execute:

```bash
java -cp bin Main
```
