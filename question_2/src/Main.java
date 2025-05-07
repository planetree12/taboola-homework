import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    System.out.println("===== Test class functionality test =====\n");

    // === Test 1: Constructor and Deep Copy ===
    System.out.println("Test 1: Deep Copy in Constructor");
    Date testDate = new Date();
    String testName = "Test Instance";
    List<Integer> testNumbers = new ArrayList<>(List.of(1, 2, 3, 4, 5));
    List<String> testStrings = new ArrayList<>(List.of("a", "b", "c"));

    Test testInstance = new Test(testDate, testName, testNumbers, testStrings);

    // Modify original data to test deep copy effectiveness
    testDate.setTime(0); // Set to 1970-01-01
    testNumbers.add(999);
    testStrings.add("modified");

    System.out.println("Original date modified: " + testDate);
    System.out.println("Test instance: " + testInstance);
    System.out.println("If deep copy works, the test instance should not contain '999' or 'modified'");
    System.out.println();

    // === Test 2: LinkedHashSet Operations ===
    System.out.println("Test 2: LinkedHashSet Operations");
    // Test containsNumber
    System.out.println("Contains number 3: " + testInstance.containsNumber(3));
    System.out.println("Contains number 999: " + testInstance.containsNumber(999));

    // Test removeString
    System.out.println("Before removing 'b': " + testInstance);
    testInstance.removeString("b");
    System.out.println("After removing 'b': " + testInstance);
    System.out.println();

    // === Test 3: Equals Method ===
    System.out.println("Test 3: Equals Method");

    Date sharedDate = new Date();
    Date differentDate = new Date();
    differentDate.setTime(0);

    Test base = new Test(
        sharedDate,
        testName,
        new ArrayList<>(List.of(1, 2, 3, 4, 5)),
        new ArrayList<>(List.of("a", "c")));

    Test identical = new Test(
        sharedDate,
        testName,
        new ArrayList<>(List.of(1, 2, 3, 4, 5)),
        new ArrayList<>(List.of("a", "c")));

    Test sameContentDifferentOrder = new Test(
        sharedDate,
        testName,
        new ArrayList<>(List.of(5, 4, 3, 2, 1)),
        new ArrayList<>(List.of("c", "a")));

    Test differentDateContent = new Test(
        differentDate,
        testName,
        new ArrayList<>(List.of(1, 2, 3, 4, 5)),
        new ArrayList<>(List.of("a", "c")));

    System.out.println("Same content, same order equals: " + base.equals(identical));
    System.out.println("Same content, different order equals: " + base.equals(sameContentDifferentOrder));
    System.out.println("Different content equals: " + base.equals(differentDateContent));
    System.out.println();

    // === Test 4: HashCode Method ===
    System.out.println("Test 4: HashCode Method");
    System.out.println("Base instance hashCode: " + base.hashCode());
    System.out.println("Identical instance hashCode: " + identical.hashCode());
    System.out.println("Different order instance hashCode: " + sameContentDifferentOrder.hashCode());
    System.out.println("Different content instance hashCode: " + differentDateContent.hashCode());
    System.out.println();

    // === Test 5: isHistoric Method ===
    System.out.println("Test 5: isHistoric Method");
    Date pastDate = new Date(System.currentTimeMillis() - 10000); // 10 seconds ago
    Date futureDate = new Date(System.currentTimeMillis() + 10000); // 10 seconds in the future

    Test pastTest = new Test(pastDate, "Past Test", testNumbers, testStrings);
    Test futureTest = new Test(futureDate, "Future Test", testNumbers, testStrings);

    System.out.println("Past date is historic: " + pastTest.isHistoric());
    System.out.println("Future date is historic: " + futureTest.isHistoric());
    System.out.println();

    // === Test 6: toString Method ===
    System.out.println("Test 6: toString Method");
    System.out.println("toString output:");
    System.out.println(testInstance.toString());
  }
}
