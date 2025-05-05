import java.util.Date;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.time.Instant;

public class Test {
  private Instant time;
  private String name;
  private LinkedHashSet<Integer> numbers;
  private LinkedHashSet<String> strings;

  public Test(Date time, String name, List<Integer> numbers,
      List<String> strings) {
    this.time = time != null ? time.toInstant() : null;
    this.name = name;
    this.numbers = numbers != null ? new LinkedHashSet<Integer>(numbers) : new LinkedHashSet<>();
    this.strings = strings != null ? new LinkedHashSet<String>(strings) : new LinkedHashSet<>();
  }

  @Override
  public boolean equals(Object obj) {
    // Check reference equality
    if (this == obj)
      return true;

    // Check null and type
    if (obj == null || getClass() != obj.getClass())
      return false;

    Test other = (Test) obj;

    if (!Objects.equals(name, other.name))
      return false;

    if (!Objects.equals(time, other.time))
      return false;

    // Convert LinkedHashSet to List to preserve order for comparison
    if (orderedHashCode(numbers) != orderedHashCode(other.numbers))
      return false;

    // Same comparison for m_strings
    if (orderedHashCode(strings) != orderedHashCode(other.strings))
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    // Basic property hashCode
    result = prime * result + Objects.hashCode(name);
    result = prime * result + Objects.hashCode(time);

    // Calculate hashCode for LinkedHashSet (preserving order impact)
    result = prime * result + orderedHashCode(numbers);
    result = prime * result + orderedHashCode(strings);

    return result;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Test[");
    sb.append("name=").append(name);
    sb.append(", time=").append(time);
    sb.append(", numbers=").append(numbers);
    sb.append(", strings=").append(strings);
    sb.append("]");
    return sb.toString();
  }

  public void removeString(String str) {
    strings.remove(str);
  }

  public boolean containsNumber(int number) {
    return numbers.contains(number);
  }

  public boolean isHistoric() {
    if (time == null) {
      return false;
    }
    return time.toEpochMilli() < System.currentTimeMillis();
  }

  // To prevent creating a new List when calling `equals` or `hashCode`, we can
  // use this helper function
  private <T> int orderedHashCode(LinkedHashSet<T> set) {
    int result = 1;
    for (T element : set) {
      result = 31 * result + (element == null ? 0 : element.hashCode());
    }
    return result;
  }
}
