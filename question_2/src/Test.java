import java.util.Date;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.time.Instant;

public class Test {
  private Instant m_time;
  private String m_name;
  private LinkedHashSet<Integer> m_numbers;
  private LinkedHashSet<String> m_strings;

  public Test(Date time, String name, List<Integer> numbers,
      List<String> strings) {
    m_time = time != null ? time.toInstant() : null;
    m_name = name;
    m_numbers = numbers != null ? new LinkedHashSet<Integer>(numbers) : new LinkedHashSet<>();
    m_strings = strings != null ? new LinkedHashSet<String>(strings) : new LinkedHashSet<>();
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

    if (!Objects.equals(m_name, other.m_name))
      return false;

    if (!Objects.equals(m_time, other.m_time))
      return false;

    // Convert LinkedHashSet to List to preserve order for comparison
    if (orderedHashCode(m_numbers) != orderedHashCode(other.m_numbers))
      return false;

    // Same comparison for m_strings
    if (orderedHashCode(m_strings) != orderedHashCode(other.m_strings))
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    // Basic property hashCode
    result = prime * result + Objects.hashCode(m_name);
    result = prime * result + Objects.hashCode(m_time);

    // Calculate hashCode for LinkedHashSet (preserving order impact)
    result = prime * result + orderedHashCode(m_numbers);
    result = prime * result + orderedHashCode(m_strings);

    return result;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Test[");
    sb.append("name=").append(m_name);
    sb.append(", time=").append(m_time);
    sb.append(", numbers=").append(m_numbers);
    sb.append(", strings=").append(m_strings);
    sb.append("]");
    return sb.toString();
  }

  public void removeString(String str) {
    m_strings.remove(str);
  }

  public boolean containsNumber(int number) {
    return m_numbers.contains(number);
  }

  public boolean isHistoric() {
    if (m_time == null) {
      return false;
    }
    return m_time.toEpochMilli() < System.currentTimeMillis();
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
