# Tutorial 12: Strings

**Video References:** Videos 117-128 (12 videos)

**Prerequisites:** Arrays (Tutorial 11), Variables (Tutorial 03), Classes and Objects (Tutorial 07)

**Estimated Learning Time:** 5-6 hours

---

## Table of Contents

1. [Introduction to Strings](#introduction-to-strings)
2. [String Comparison](#string-comparison)
3. [String Methods](#string-methods)
4. [String Concatenation](#string-concatenation)
5. [Reversing Strings](#reversing-strings)
6. [String Immutability](#string-immutability)
7. [StringBuffer and StringBuilder](#stringbuffer-and-stringbuilder)
8. [Comparing StringBuilder and StringBuffer](#comparing-stringbuilder-and-stringbuffer)
9. [Practical String Problems](#practical-string-problems)
10. [Common Pitfalls and Best Practices](#common-pitfalls-and-best-practices)
11. [Interview Preparation](#interview-preparation)
12. [Practice Exercises](#practice-exercises)
13. [Further Reading](#further-reading)

---

## Introduction to Strings

### Video 117: String

**Video Reference:** `117.Java- String.mp4`

### What is a String?

**Definition:** A String in Java is a sequence of characters. It's an object that represents textual data and is one of the most commonly used classes in Java.

```
┌──────────────────────────────────────────────┐
│           String Representation              │
├──────────────────────────────────────────────┤
│                                              │
│  String text = "Hello";                      │
│                                              │
│  Index:  [0] [1] [2] [3] [4]                │
│  Value:  [H] [e] [l] [l] [o]                │
│                                              │
│  • Immutable sequence of characters          │
│  • Stored in String pool                     │
│  • Implements CharSequence interface         │
│  • Zero-based indexing                       │
│                                              │
└──────────────────────────────────────────────┘
```

### Key Characteristics

| Feature | Description |
|---------|-------------|
| **Immutable** | Cannot be changed after creation |
| **Object Type** | Reference type, not primitive |
| **String Pool** | Strings are stored in special memory area |
| **Thread-Safe** | Immutability makes them thread-safe |
| **CharSequence** | Implements CharSequence interface |

### Creating Strings

**Method 1: String Literal**
```java
String str1 = "Hello";
String str2 = "Hello";
// Both reference same object in String pool
```

**Method 2: Using new Keyword**
```java
String str3 = new String("Hello");
String str4 = new String("Hello");
// Creates new objects in heap memory
```

**Method 3: Character Array**
```java
char[] chars = {'H', 'e', 'l', 'l', 'o'};
String str5 = new String(chars);
```

**Method 4: Byte Array**
```java
byte[] bytes = {72, 101, 108, 108, 111};
String str6 = new String(bytes);
```

### String Pool Concept

```
┌────────────────────────────────────────┐
│         String Pool (Heap)             │
├────────────────────────────────────────┤
│                                        │
│  String s1 = "Java";                   │
│  String s2 = "Java";    ──┐            │
│  String s3 = "Java";      │            │
│                           ├──→ "Java"  │
│  All three reference      │            │
│  the same String object ──┘            │
│                                        │
│  String s4 = new String("Java");       │
│    Creates new object → "Java" (new)   │
│                                        │
└────────────────────────────────────────┘
```

### Basic String Operations

```java
public class StringBasics {
    public static void main(String[] args) {
        // Creating strings
        String str1 = "Hello World";
        String str2 = new String("Java Programming");
        
        // Getting length
        System.out.println("Length of str1: " + str1.length());
        
        // Accessing characters
        char firstChar = str1.charAt(0);
        System.out.println("First character: " + firstChar);
        
        // Getting substring
        String sub = str1.substring(0, 5);
        System.out.println("Substring: " + sub);
        
        // Converting case
        System.out.println("Uppercase: " + str1.toUpperCase());
        System.out.println("Lowercase: " + str1.toLowerCase());
        
        // Checking content
        boolean isEmpty = str1.isEmpty();
        boolean isBlank = str1.isBlank();
        System.out.println("Is empty: " + isEmpty);
        System.out.println("Is blank: " + isBlank);
    }
}
```

**Output:**
```
Length of str1: 11
First character: H
Substring: Hello
Uppercase: HELLO WORLD
Lowercase: hello world
Is empty: false
Is blank: false
```

---

## String Comparison

### Video 118: Comparing Strings

**Video Reference:** `118.Java- comparing strings.mp4`

### Why String Comparison is Special

**Key Point:** Strings should be compared using `.equals()` method, not `==` operator.

```
┌──────────────────────────────────────────┐
│       == vs equals()                     │
├──────────────────────────────────────────┤
│                                          │
│  == Operator:                            │
│    • Compares references (memory addr)   │
│    • Returns true if same object         │
│                                          │
│  equals() Method:                        │
│    • Compares content (actual text)      │
│    • Returns true if same characters     │
│                                          │
└──────────────────────────────────────────┘
```

### Comparison Methods

**1. Using equals()**
```java
String s1 = "Hello";
String s2 = "Hello";
String s3 = new String("Hello");

System.out.println(s1.equals(s2));     // true (same content)
System.out.println(s1.equals(s3));     // true (same content)
System.out.println(s1 == s2);          // true (same reference)
System.out.println(s1 == s3);          // false (different reference)
```

**2. Using equalsIgnoreCase()**
```java
String s1 = "Hello";
String s2 = "HELLO";

System.out.println(s1.equals(s2));           // false
System.out.println(s1.equalsIgnoreCase(s2)); // true
```

**3. Using compareTo()**
```java
String s1 = "Apple";
String s2 = "Banana";
String s3 = "Apple";

System.out.println(s1.compareTo(s2));  // negative (s1 < s2)
System.out.println(s2.compareTo(s1));  // positive (s2 > s1)
System.out.println(s1.compareTo(s3));  // 0 (s1 == s3)
```

**4. Using compareToIgnoreCase()**
```java
String s1 = "apple";
String s2 = "APPLE";

System.out.println(s1.compareToIgnoreCase(s2)); // 0
```

### Complete Comparison Example

```java
public class StringComparison {
    public static void main(String[] args) {
        String str1 = "Java";
        String str2 = "Java";
        String str3 = new String("Java");
        String str4 = "JAVA";
        String str5 = "JavaScript";
        
        System.out.println("=== Using == Operator ===");
        System.out.println("str1 == str2: " + (str1 == str2));       // true
        System.out.println("str1 == str3: " + (str1 == str3));       // false
        
        System.out.println("\n=== Using equals() ===");
        System.out.println("str1.equals(str2): " + str1.equals(str2));  // true
        System.out.println("str1.equals(str3): " + str1.equals(str3));  // true
        System.out.println("str1.equals(str4): " + str1.equals(str4));  // false
        
        System.out.println("\n=== Using equalsIgnoreCase() ===");
        System.out.println("str1.equalsIgnoreCase(str4): " + 
                          str1.equalsIgnoreCase(str4));  // true
        
        System.out.println("\n=== Using compareTo() ===");
        System.out.println("str1.compareTo(str2): " + str1.compareTo(str2));  // 0
        System.out.println("str1.compareTo(str5): " + str1.compareTo(str5));  // negative
        System.out.println("str5.compareTo(str1): " + str5.compareTo(str1));  // positive
        
        System.out.println("\n=== Using startsWith() and endsWith() ===");
        System.out.println("str5.startsWith(\"Java\"): " + 
                          str5.startsWith("Java"));      // true
        System.out.println("str5.endsWith(\"Script\"): " + 
                          str5.endsWith("Script"));      // true
        
        System.out.println("\n=== Using contains() ===");
        System.out.println("str5.contains(\"Script\"): " + 
                          str5.contains("Script"));      // true
    }
}
```

### Comparison Methods Summary

| Method | Purpose | Return Type | Case-Sensitive |
|--------|---------|-------------|----------------|
| `equals()` | Content equality | boolean | Yes |
| `equalsIgnoreCase()` | Content equality | boolean | No |
| `compareTo()` | Lexicographic order | int | Yes |
| `compareToIgnoreCase()` | Lexicographic order | int | No |
| `==` | Reference equality | boolean | N/A |
| `startsWith()` | Prefix check | boolean | Yes |
| `endsWith()` | Suffix check | boolean | Yes |
| `contains()` | Substring check | boolean | Yes |

---

## String Methods

### Video 119: String Methods

**Video Reference:** `119.Java- String methods.mp4`

### Essential String Methods

### 1. Length and Character Access

```java
String str = "Hello World";

// Length
int len = str.length();                    // 11

// Character at index
char ch = str.charAt(0);                   // 'H'

// Get character array
char[] chars = str.toCharArray();

// Index of character/substring
int index1 = str.indexOf('o');             // 4 (first occurrence)
int index2 = str.lastIndexOf('o');         // 7 (last occurrence)
int index3 = str.indexOf("World");         // 6
```

### 2. Substring Operations

```java
String str = "Java Programming";

// Substring from index to end
String sub1 = str.substring(5);            // "Programming"

// Substring from start to end index
String sub2 = str.substring(0, 4);         // "Java"
String sub3 = str.substring(5, 9);         // "Prog"
```

### 3. Case Conversion

```java
String str = "Hello World";

String upper = str.toUpperCase();          // "HELLO WORLD"
String lower = str.toLowerCase();          // "hello world"
```

### 4. Trimming and Stripping

```java
String str = "  Hello World  ";

String trimmed = str.trim();               // "Hello World"
String stripped = str.strip();             // "Hello World" (Unicode-aware)
String stripLeading = str.stripLeading();  // "Hello World  "
String stripTrailing = str.stripTrailing(); // "  Hello World"
```

### 5. Replacing Characters/Strings

```java
String str = "Hello World";

String replaced1 = str.replace('o', 'a');       // "Hella Warld"
String replaced2 = str.replace("World", "Java"); // "Hello Java"
String replaced3 = str.replaceAll("o", "0");    // "Hell0 W0rld"
String replaced4 = str.replaceFirst("o", "0");  // "Hell0 World"
```

### 6. Splitting Strings

```java
String str = "Java,Python,C++,JavaScript";

String[] languages = str.split(",");
// ["Java", "Python", "C++", "JavaScript"]

// Split with limit
String[] limited = str.split(",", 2);
// ["Java", "Python,C++,JavaScript"]
```

### 7. Joining Strings

```java
String[] words = {"Hello", "World", "Java"};

String joined = String.join(" ", words);        // "Hello World Java"
String joined2 = String.join("-", "A", "B", "C"); // "A-B-C"
```

### 8. Checking String Content

```java
String str = "Hello World";

boolean isEmpty = str.isEmpty();           // false
boolean isBlank = str.isBlank();           // false
boolean starts = str.startsWith("Hello");  // true
boolean ends = str.endsWith("World");      // true
boolean contains = str.contains("lo Wo");  // true
```

### 9. String Formatting

```java
// Using format()
String formatted = String.format("Name: %s, Age: %d", "John", 25);
System.out.println(formatted);  // "Name: John, Age: 25"

// Using formatted() (Java 15+)
String formatted2 = "Name: %s, Age: %d".formatted("Alice", 30);

// Repeat (Java 11+)
String repeated = "Hello".repeat(3);       // "HelloHelloHello"
```

### Complete Example

```java
public class StringMethods {
    public static void main(String[] args) {
        String text = "  Java Programming Language  ";
        
        System.out.println("=== Original String ===");
        System.out.println("'" + text + "'");
        System.out.println("Length: " + text.length());
        
        System.out.println("\n=== Trimming ===");
        System.out.println("Trimmed: '" + text.trim() + "'");
        
        System.out.println("\n=== Case Conversion ===");
        System.out.println("Upper: " + text.toUpperCase());
        System.out.println("Lower: " + text.toLowerCase());
        
        System.out.println("\n=== Substring ===");
        System.out.println("Substring(6, 17): " + text.substring(6, 17));
        
        System.out.println("\n=== Replace ===");
        System.out.println("Replace 'a' with '@': " + text.replace('a', '@'));
        System.out.println("Replace 'Java' with 'Python': " + 
                          text.replace("Java", "Python"));
        
        System.out.println("\n=== Split ===");
        String words = "apple,banana,orange,grape";
        String[] fruits = words.split(",");
        for (String fruit : fruits) {
            System.out.println("  - " + fruit);
        }
        
        System.out.println("\n=== Join ===");
        String joined = String.join(" | ", fruits);
        System.out.println("Joined: " + joined);
        
        System.out.println("\n=== Checking Content ===");
        System.out.println("Contains 'Programming': " + 
                          text.contains("Programming"));
        System.out.println("Starts with '  Java': " + 
                          text.startsWith("  Java"));
        System.out.println("Ends with 'Language  ': " + 
                          text.endsWith("Language  "));
        
        System.out.println("\n=== Format ===");
        String formatted = String.format("Language: %s, Year: %d", "Java", 1995);
        System.out.println(formatted);
    }
}
```

---

## String Concatenation

### Video 120: Concatenation

**Video Reference:** `120.Java- concatenation.mp4`

### Ways to Concatenate Strings

### 1. Using + Operator

```java
String firstName = "John";
String lastName = "Doe";
String fullName = firstName + " " + lastName;  // "John Doe"

// Multiple concatenations
String result = "Result: " + 10 + 20;    // "Result: 1020" (left to right)
String result2 = "Result: " + (10 + 20); // "Result: 30" (parentheses first)
```

### 2. Using concat() Method

```java
String str1 = "Hello";
String str2 = " World";
String result = str1.concat(str2);        // "Hello World"

// Chaining
String result2 = "Hello".concat(" ").concat("World");
```

### 3. Using StringBuilder

```java
StringBuilder sb = new StringBuilder();
sb.append("Hello");
sb.append(" ");
sb.append("World");
String result = sb.toString();            // "Hello World"
```

### 4. Using String.join()

```java
String result = String.join(" ", "Hello", "World", "Java");
// "Hello World Java"
```

### 5. Using String.format()

```java
String name = "John";
int age = 25;
String result = String.format("%s is %d years old", name, age);
// "John is 25 years old"
```

### Performance Comparison

```java
public class ConcatenationPerformance {
    public static void main(String[] args) {
        int iterations = 10000;
        
        // Method 1: Using + operator (slowest for loops)
        long start1 = System.currentTimeMillis();
        String result1 = "";
        for (int i = 0; i < iterations; i++) {
            result1 += "a";  // Creates new String each time
        }
        long end1 = System.currentTimeMillis();
        System.out.println("+ operator: " + (end1 - start1) + " ms");
        
        // Method 2: Using StringBuilder (fastest)
        long start2 = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            sb.append("a");  // Modifies same object
        }
        String result2 = sb.toString();
        long end2 = System.currentTimeMillis();
        System.out.println("StringBuilder: " + (end2 - start2) + " ms");
        
        // Method 3: Using concat() (slow)
        long start3 = System.currentTimeMillis();
        String result3 = "";
        for (int i = 0; i < iterations; i++) {
            result3 = result3.concat("a");
        }
        long end3 = System.currentTimeMillis();
        System.out.println("concat(): " + (end3 - start3) + " ms");
    }
}
```

### Best Practices

```java
// ❌ Bad: Using + in loops
String result = "";
for (int i = 0; i < 1000; i++) {
    result += i + ",";  // Creates 1000 new String objects
}

// ✅ Good: Using StringBuilder in loops
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i).append(",");
}
String result = sb.toString();

// ✅ Good: Using + for simple concatenation
String fullName = firstName + " " + lastName;

// ✅ Good: Using String.join() for joining arrays
String[] words = {"Hello", "World", "Java"};
String joined = String.join(" ", words);
```

### Concatenation Methods Comparison

| Method | Use Case | Performance | Readability |
|--------|----------|-------------|-------------|
| `+` operator | Simple, few strings | Good for few | Excellent |
| `concat()` | Chaining | Moderate | Good |
| `StringBuilder` | Loops, many operations | Best | Good |
| `String.join()` | Joining collections | Good | Excellent |
| `String.format()` | Formatted output | Moderate | Excellent |

---

## Reversing Strings

### Video 121: Reversing a String

**Video Reference:** `121.Java- Reversing a string.mp4`

### Methods to Reverse a String

### Method 1: Using StringBuilder

```java
public class ReverseString1 {
    public static String reverse(String str) {
        return new StringBuilder(str).reverse().toString();
    }
    
    public static void main(String[] args) {
        String original = "Hello World";
        String reversed = reverse(original);
        System.out.println("Original: " + original);
        System.out.println("Reversed: " + reversed);
    }
}
```

### Method 2: Using Character Array

```java
public class ReverseString2 {
    public static String reverse(String str) {
        char[] chars = str.toCharArray();
        int left = 0;
        int right = chars.length - 1;
        
        while (left < right) {
            // Swap characters
            char temp = chars[left];
            chars[left] = chars[right];
            chars[right] = temp;
            
            left++;
            right--;
        }
        
        return new String(chars);
    }
    
    public static void main(String[] args) {
        String original = "Java Programming";
        String reversed = reverse(original);
        System.out.println("Original: " + original);
        System.out.println("Reversed: " + reversed);
    }
}
```

### Method 3: Using Recursion

```java
public class ReverseString3 {
    public static String reverse(String str) {
        if (str.isEmpty()) {
            return str;
        }
        return reverse(str.substring(1)) + str.charAt(0);
    }
    
    public static void main(String[] args) {
        String original = "Recursion";
        String reversed = reverse(original);
        System.out.println("Original: " + original);
        System.out.println("Reversed: " + reversed);
    }
}
```

### Method 4: Using Loop (Character by Character)

```java
public class ReverseString4 {
    public static String reverse(String str) {
        String reversed = "";
        for (int i = str.length() - 1; i >= 0; i--) {
            reversed += str.charAt(i);
        }
        return reversed;
    }
    
    public static void main(String[] args) {
        String original = "Algorithm";
        String reversed = reverse(original);
        System.out.println("Original: " + original);
        System.out.println("Reversed: " + reversed);
    }
}
```

### Method 5: Using Stack

```java
import java.util.Stack;

public class ReverseString5 {
    public static String reverse(String str) {
        Stack<Character> stack = new Stack<>();
        
        // Push all characters to stack
        for (char ch : str.toCharArray()) {
            stack.push(ch);
        }
        
        // Pop all characters and build reversed string
        StringBuilder reversed = new StringBuilder();
        while (!stack.isEmpty()) {
            reversed.append(stack.pop());
        }
        
        return reversed.toString();
    }
    
    public static void main(String[] args) {
        String original = "Stack Data Structure";
        String reversed = reverse(original);
        System.out.println("Original: " + original);
        System.out.println("Reversed: " + reversed);
    }
}
```

### Reverse Words in a String

```java
public class ReverseWords {
    // Reverse entire string
    public static String reverseString(String str) {
        return new StringBuilder(str).reverse().toString();
    }
    
    // Reverse words but keep word order
    public static String reverseEachWord(String str) {
        String[] words = str.split(" ");
        StringBuilder result = new StringBuilder();
        
        for (String word : words) {
            String reversed = new StringBuilder(word).reverse().toString();
            result.append(reversed).append(" ");
        }
        
        return result.toString().trim();
    }
    
    // Reverse word order
    public static String reverseWordOrder(String str) {
        String[] words = str.split(" ");
        StringBuilder result = new StringBuilder();
        
        for (int i = words.length - 1; i >= 0; i--) {
            result.append(words[i]);
            if (i > 0) result.append(" ");
        }
        
        return result.toString();
    }
    
    public static void main(String[] args) {
        String text = "Hello World Java";
        
        System.out.println("Original: " + text);
        System.out.println("Reversed string: " + reverseString(text));
        System.out.println("Reversed each word: " + reverseEachWord(text));
        System.out.println("Reversed word order: " + reverseWordOrder(text));
    }
}
```

**Output:**
```
Original: Hello World Java
Reversed string: avaJ dlroW olleH
Reversed each word: olleH dlroW avaJ
Reversed word order: Java World Hello
```

### Performance Comparison

| Method | Time Complexity | Space Complexity | Best Use Case |
|--------|----------------|------------------|---------------|
| StringBuilder | O(n) | O(n) | Best overall |
| Character Array | O(n) | O(n) | Memory efficient |
| Recursion | O(n) | O(n) | Educational |
| Loop with += | O(n²) | O(n) | Avoid for large strings |
| Stack | O(n) | O(n) | When using stack anyway |

---

## String Immutability

### Video 122: Advantages of Immutable Class

**Video Reference:** `122.Java- Advantages of immutable class.mp4`

### What is String Immutability?

**Definition:** Once a String object is created, its content cannot be modified. Any operation that appears to modify a String actually creates a new String object.

```
┌──────────────────────────────────────────┐
│       String Immutability                │
├──────────────────────────────────────────┤
│                                          │
│  String str = "Hello";                   │
│    ┌─────┐                               │
│    │Hello│  (Original object)            │
│    └─────┘                               │
│                                          │
│  str = str + " World";                   │
│    ┌─────┐   ┌───────────┐              │
│    │Hello│   │Hello World│ (New object)  │
│    └─────┘   └───────────┘              │
│   (Orphaned)   (str now points here)     │
│                                          │
└──────────────────────────────────────────┘
```

### Demonstrating Immutability

```java
public class StringImmutability {
    public static void main(String[] args) {
        String str1 = "Hello";
        System.out.println("Original: " + str1);
        System.out.println("HashCode: " + str1.hashCode());
        
        // Attempt to modify
        str1.concat(" World");  // This creates new String, but not assigned
        System.out.println("After concat (not assigned): " + str1);
        System.out.println("HashCode: " + str1.hashCode());  // Same
        
        // Actually reassign
        str1 = str1.concat(" World");  // Now assigned to new object
        System.out.println("After concat (assigned): " + str1);
        System.out.println("HashCode: " + str1.hashCode());  // Different
        
        // Replace operation
        String str2 = "Java";
        String str3 = str2.replace('a', 'o');
        System.out.println("\nOriginal str2: " + str2);  // Unchanged
        System.out.println("New str3: " + str3);         // New object
    }
}
```

### Advantages of Immutability

**1. Thread Safety**
```java
// Multiple threads can safely share same String
String sharedString = "Thread Safe";
// No synchronization needed!
```

**2. Security**
```java
// Sensitive data like passwords remain unchanged
String password = "secret123";
// Cannot be modified by malicious code
```

**3. String Pool Optimization**
```java
String s1 = "Hello";
String s2 = "Hello";
// Both reference same object - memory efficient
System.out.println(s1 == s2);  // true
```

**4. Hashcode Caching**
```java
// Hashcode calculated once and cached
String key = "myKey";
int hash1 = key.hashCode();  // Calculated
int hash2 = key.hashCode();  // Returned from cache
```

**5. Safe to use as HashMap Keys**
```java
Map<String, Integer> map = new HashMap<>();
String key = "count";
map.put(key, 100);
// Key cannot change, so mapping remains valid
```

### Disadvantages of Immutability

```java
// Problem: Multiple String creations
String result = "";
for (int i = 0; i < 1000; i++) {
    result += i;  // Creates 1000 new String objects!
}

// Solution: Use StringBuilder for mutable operations
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i);  // Modifies same object
}
String result = sb.toString();
```

### Creating Immutable Classes

```java
public final class ImmutablePerson {
    private final String name;
    private final int age;
    
    public ImmutablePerson(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() {
        return name;
    }
    
    public int getAge() {
        return age;
    }
    
    // No setters - immutable!
    
    // To "modify", create new object
    public ImmutablePerson withAge(int newAge) {
        return new ImmutablePerson(this.name, newAge);
    }
}
```

### Immutability Best Practices

```java
✅ DO:
- Use String for immutable text
- Use StringBuilder/StringBuffer for mutable operations
- Leverage String pool for memory efficiency
- Use Strings as Map keys

❌ DON'T:
- Concatenate Strings in loops using +
- Assume String modification is efficient
- Try to make Strings mutable
- Ignore StringBuilder for heavy string operations
```

---

## StringBuffer and StringBuilder

### Video 123: StringBuffer and StringBuilder

**Video Reference:** `123.Java- StringBuffer and StringBuilder.mp4`

### Why Mutable String Classes?

**Problem with String:**
```java
String str = "";
for (int i = 0; i < 1000; i++) {
    str += i;  // Creates 1000 new String objects!
}
```

**Solution: StringBuilder/StringBuffer**
```java
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i);  // Modifies same object
}
String result = sb.toString();
```

### StringBuilder Overview

**Definition:** Mutable sequence of characters, not thread-safe, faster than StringBuffer.

```java
public class StringBuilderExample {
    public static void main(String[] args) {
        // Creating StringBuilder
        StringBuilder sb = new StringBuilder();
        System.out.println("Initial capacity: " + sb.capacity());  // 16
        
        // Append operations
        sb.append("Hello");
        sb.append(" ");
        sb.append("World");
        System.out.println(sb.toString());  // "Hello World"
        
        // Insert at specific position
        sb.insert(6, "Beautiful ");
        System.out.println(sb);  // "Hello Beautiful World"
        
        // Replace portion
        sb.replace(0, 5, "Hi");
        System.out.println(sb);  // "Hi Beautiful World"
        
        // Delete portion
        sb.delete(3, 13);
        System.out.println(sb);  // "Hi World"
        
        // Reverse
        sb.reverse();
        System.out.println(sb);  // "dlroW iH"
        
        // Get length and capacity
        System.out.println("\nLength: " + sb.length());
        System.out.println("Capacity: " + sb.capacity());
    }
}
```

### StringBuilder Methods

| Method | Description | Example |
|--------|-------------|---------|
| `append()` | Add to end | `sb.append("text")` |
| `insert()` | Insert at position | `sb.insert(5, "new")` |
| `delete()` | Remove characters | `sb.delete(0, 5)` |
| `deleteCharAt()` | Remove single char | `sb.deleteCharAt(0)` |
| `replace()` | Replace portion | `sb.replace(0, 5, "new")` |
| `reverse()` | Reverse content | `sb.reverse()` |
| `substring()` | Extract portion | `sb.substring(0, 5)` |
| `charAt()` | Get character | `sb.charAt(0)` |
| `setCharAt()` | Set character | `sb.setCharAt(0, 'X')` |
| `length()` | Get length | `sb.length()` |
| `capacity()` | Get capacity | `sb.capacity()` |
| `toString()` | Convert to String | `sb.toString()` |

### StringBuffer Overview

**Definition:** Mutable sequence of characters, thread-safe (synchronized), slower than StringBuilder.

```java
public class StringBufferExample {
    public static void main(String[] args) {
        // Creating StringBuffer
        StringBuffer sbf = new StringBuffer("Hello");
        
        // Thread-safe operations
        sbf.append(" World");
        System.out.println(sbf);  // "Hello World"
        
        // All methods same as StringBuilder
        sbf.insert(5, ",");
        System.out.println(sbf);  // "Hello, World"
        
        sbf.reverse();
        System.out.println(sbf);  // "dlroW ,olleH"
    }
}
```

### Capacity Management

```java
public class CapacityManagement {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        System.out.println("Initial capacity: " + sb.capacity());  // 16
        
        sb.append("Hello World Java Programming");
        System.out.println("Length: " + sb.length());              // 28
        System.out.println("Capacity: " + sb.capacity());          // Auto-expanded
        
        // Manually set capacity
        sb.ensureCapacity(50);
        System.out.println("After ensureCapacity(50): " + sb.capacity());
        
        // Trim to size
        sb.trimToSize();
        System.out.println("After trimToSize(): " + sb.capacity());
    }
}
```

### Video 124: Reverse String Using StringBuffer

**Video Reference:** `124.Java- Reverse a string using StringBuffer.mp4`

```java
public class ReverseUsingStringBuffer {
    public static String reverse(String str) {
        StringBuffer sbf = new StringBuffer(str);
        return sbf.reverse().toString();
    }
    
    public static void main(String[] args) {
        String original = "Hello World";
        String reversed = reverse(original);
        
        System.out.println("Original: " + original);
        System.out.println("Reversed: " + reversed);
        
        // Direct method
        String reversed2 = new StringBuffer("Java Programming").reverse().toString();
        System.out.println("Reversed2: " + reversed2);
    }
}
```

---

## Comparing StringBuilder and StringBuffer

### Video 125: Difference Between StringBuilder and StringBuffer

**Video Reference:** `125.Java- Difference between StringBuilder and StringBuffer.mp4`

### Key Differences

| Feature | StringBuilder | StringBuffer |
|---------|--------------|--------------|
| **Thread Safety** | Not synchronized | Synchronized |
| **Performance** | Faster | Slower |
| **Since** | Java 5 | Java 1.0 |
| **Use Case** | Single-threaded | Multi-threaded |
| **Methods** | Same as StringBuffer | Same as StringBuilder |

### Thread Safety Demonstration

```java
public class ThreadSafetyDemo {
    
    // Using StringBuilder (not thread-safe)
    static class StringBuilderTask implements Runnable {
        private StringBuilder sb;
        
        public StringBuilderTask(StringBuilder sb) {
            this.sb = sb;
        }
        
        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                sb.append("a");
            }
        }
    }
    
    // Using StringBuffer (thread-safe)
    static class StringBufferTask implements Runnable {
        private StringBuffer sbf;
        
        public StringBufferTask(StringBuffer sbf) {
            this.sbf = sbf;
        }
        
        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                sbf.append("a");
            }
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        // Test StringBuilder (may have incorrect length)
        StringBuilder sb = new StringBuilder();
        Thread t1 = new Thread(new StringBuilderTask(sb));
        Thread t2 = new Thread(new StringBuilderTask(sb));
        
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        System.out.println("StringBuilder length: " + sb.length());
        // May not be 2000 due to race conditions
        
        // Test StringBuffer (correct length)
        StringBuffer sbf = new StringBuffer();
        Thread t3 = new Thread(new StringBufferTask(sbf));
        Thread t4 = new Thread(new StringBufferTask(sbf));
        
        t3.start();
        t4.start();
        t3.join();
        t4.join();
        
        System.out.println("StringBuffer length: " + sbf.length());
        // Always 2000 - thread-safe
    }
}
```

### Performance Comparison

```java
public class PerformanceComparison {
    public static void main(String[] args) {
        int iterations = 100000;
        
        // StringBuilder performance
        long start1 = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            sb.append("a");
        }
        long end1 = System.currentTimeMillis();
        System.out.println("StringBuilder: " + (end1 - start1) + " ms");
        
        // StringBuffer performance
        long start2 = System.currentTimeMillis();
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < iterations; i++) {
            sbf.append("a");
        }
        long end2 = System.currentTimeMillis();
        System.out.println("StringBuffer: " + (end2 - start2) + " ms");
    }
}
```

### When to Use Which?

```java
// ✅ Use StringBuilder (most common)
// Single-threaded operations
public String formatUserData(String name, int age) {
    StringBuilder sb = new StringBuilder();
    sb.append("Name: ").append(name);
    sb.append(", Age: ").append(age);
    return sb.toString();
}

// ✅ Use StringBuffer
// Multi-threaded shared mutable string
public class SharedLogger {
    private StringBuffer log = new StringBuffer();
    
    public synchronized void addLog(String message) {
        log.append(message).append("\n");
    }
    
    public String getLog() {
        return log.toString();
    }
}

// ✅ Use String
// Immutable text, no modifications needed
public String getUserName() {
    return "John Doe";
}
```

---

## Practical String Problems

### Video 126: Count Vowels in String

**Video Reference:** `126.Java- Write a java program to count number of vowels in a string.mp4`

```java
public class CountVowels {
    
    public static int countVowels(String str) {
        int count = 0;
        String vowels = "aeiouAEIOU";
        
        for (int i = 0; i < str.length(); i++) {
            if (vowels.indexOf(str.charAt(i)) != -1) {
                count++;
            }
        }
        
        return count;
    }
    
    // Alternative using enhanced loop
    public static int countVowels2(String str) {
        int count = 0;
        
        for (char ch : str.toCharArray()) {
            ch = Character.toLowerCase(ch);
            if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u') {
                count++;
            }
        }
        
        return count;
    }
    
    // Using regex
    public static int countVowels3(String str) {
        return str.replaceAll("[^aeiouAEIOU]", "").length();
    }
    
    public static void main(String[] args) {
        String text = "Hello World Java Programming";
        
        System.out.println("Text: " + text);
        System.out.println("Vowel count (method 1): " + countVowels(text));
        System.out.println("Vowel count (method 2): " + countVowels2(text));
        System.out.println("Vowel count (method 3): " + countVowels3(text));
    }
}
```

### Video 127: Palindrome Check

**Video Reference:** `127.Java- Write a java program to test whether given word is palindrome or not-.mp4`

```java
public class PalindromeCheck {
    
    // Method 1: Using StringBuilder
    public static boolean isPalindrome1(String str) {
        String reversed = new StringBuilder(str).reverse().toString();
        return str.equals(reversed);
    }
    
    // Method 2: Two-pointer approach
    public static boolean isPalindrome2(String str) {
        int left = 0;
        int right = str.length() - 1;
        
        while (left < right) {
            if (str.charAt(left) != str.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        
        return true;
    }
    
    // Method 3: Using loop
    public static boolean isPalindrome3(String str) {
        int n = str.length();
        for (int i = 0; i < n / 2; i++) {
            if (str.charAt(i) != str.charAt(n - 1 - i)) {
                return false;
            }
        }
        return true;
    }
    
    // Ignore case and spaces
    public static boolean isPalindromeIgnoreCaseAndSpaces(String str) {
        String cleaned = str.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        return isPalindrome2(cleaned);
    }
    
    public static void main(String[] args) {
        String[] testWords = {"radar", "hello", "level", "A man a plan a canal Panama"};
        
        for (String word : testWords) {
            System.out.println("\nWord: " + word);
            System.out.println("Is palindrome (method 1): " + isPalindrome1(word));
            System.out.println("Is palindrome (method 2): " + isPalindrome2(word));
            System.out.println("Is palindrome (ignoring case/spaces): " + 
                             isPalindromeIgnoreCaseAndSpaces(word));
        }
    }
}
```

### Video 128: First Non-Repeating Character

**Video Reference:** `128.Java- Write a java program to find first non repeating character in a string.mp4`

```java
import java.util.*;

public class FirstNonRepeatingChar {
    
    // Method 1: Using HashMap
    public static Character findFirstNonRepeating1(String str) {
        Map<Character, Integer> charCount = new LinkedHashMap<>();
        
        // Count occurrences
        for (char ch : str.toCharArray()) {
            charCount.put(ch, charCount.getOrDefault(ch, 0) + 1);
        }
        
        // Find first with count 1
        for (Map.Entry<Character, Integer> entry : charCount.entrySet()) {
            if (entry.getValue() == 1) {
                return entry.getKey();
            }
        }
        
        return null;  // No non-repeating character
    }
    
    // Method 2: Using indexOf and lastIndexOf
    public static Character findFirstNonRepeating2(String str) {
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (str.indexOf(ch) == str.lastIndexOf(ch)) {
                return ch;
            }
        }
        return null;
    }
    
    // Method 3: Using character array
    public static Character findFirstNonRepeating3(String str) {
        int[] count = new int[256];  // ASCII characters
        
        // Count occurrences
        for (char ch : str.toCharArray()) {
            count[ch]++;
        }
        
        // Find first with count 1
        for (char ch : str.toCharArray()) {
            if (count[ch] == 1) {
                return ch;
            }
        }
        
        return null;
    }
    
    public static void main(String[] args) {
        String[] testStrings = {
            "programming",
            "aabbcc",
            "hello",
            "aabcdee"
        };
        
        for (String str : testStrings) {
            System.out.println("\nString: " + str);
            System.out.println("First non-repeating (HashMap): " + 
                             findFirstNonRepeating1(str));
            System.out.println("First non-repeating (indexOf): " + 
                             findFirstNonRepeating2(str));
            System.out.println("First non-repeating (array): " + 
                             findFirstNonRepeating3(str));
        }
    }
}
```

---

## Common Pitfalls and Best Practices

### Common Mistakes

**1. Using == Instead of equals()**
```java
// ❌ Wrong
String s1 = new String("Hello");
String s2 = new String("Hello");
if (s1 == s2) {  // false - different objects
    System.out.println("Equal");
}

// ✅ Correct
if (s1.equals(s2)) {  // true - same content
    System.out.println("Equal");
}
```

**2. String Concatenation in Loops**
```java
// ❌ Wrong - O(n²) complexity
String result = "";
for (int i = 0; i < 1000; i++) {
    result += i;  // Creates 1000 new String objects
}

// ✅ Correct - O(n) complexity
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i);
}
String result = sb.toString();
```

**3. Ignoring Null Checks**
```java
// ❌ Wrong - NullPointerException risk
public int getLength(String str) {
    return str.length();  // Crashes if str is null
}

// ✅ Correct
public int getLength(String str) {
    if (str == null) {
        return 0;
    }
    return str.length();
}

// ✅ Better - using Objects utility
public int getLength(String str) {
    return Objects.requireNonNullElse(str, "").length();
}
```

**4. Modifying Strings Inefficiently**
```java
// ❌ Wrong
String str = "Hello";
str.concat(" World");  // New string created but lost
System.out.println(str);  // Still "Hello"

// ✅ Correct
String str = "Hello";
str = str.concat(" World");  // Reassign to new string
System.out.println(str);  // "Hello World"
```

### Best Practices

**1. Choose Right String Class**
```java
// Use String for immutable text
String name = "John Doe";

// Use StringBuilder for single-threaded mutable operations
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 100; i++) {
    sb.append(i);
}

// Use StringBuffer for multi-threaded mutable operations
StringBuffer sbf = new StringBuffer();
// Safe to use across multiple threads
```

**2. Use String Methods Wisely**
```java
// ✅ Good - method chaining
String result = str.trim().toLowerCase().replace(" ", "_");

// ✅ Good - null-safe operations
String safe = Optional.ofNullable(str).orElse("default");
```

**3. Leverage String Pool**
```java
// ✅ Good - uses string pool
String s1 = "Hello";

// ❌ Avoid unnecessary object creation
String s2 = new String("Hello");  // Creates new object

// ✅ Use intern() to add to pool if needed
String s3 = new String("Hello").intern();
```

**4. Handle Empty and Blank Strings**
```java
String str = "   ";

boolean isEmpty = str.isEmpty();      // false (has spaces)
boolean isBlank = str.isBlank();      // true (only whitespace)

// Best practice
if (str != null && !str.isBlank()) {
    // Process string
}
```

---

## Interview Preparation

### Frequently Asked Questions

**Q1: What is the difference between String, StringBuilder, and StringBuffer?**
- String: Immutable, thread-safe by nature
- StringBuilder: Mutable, not synchronized, fastest
- StringBuffer: Mutable, synchronized, thread-safe

**Q2: Why are Strings immutable in Java?**
- Thread safety without synchronization
- String pool optimization
- Security (passwords, URLs)
- Hashcode caching
- Safe as HashMap keys

**Q3: What is String pool?**
- Special memory area in heap
- Stores unique string literals
- Reuses existing strings
- Saves memory

**Q4: How do you reverse a string?**
- StringBuilder.reverse()
- Two-pointer swap
- Recursion
- Stack-based approach

**Q5: What is the difference between == and equals()?**
- ==: Compares references (memory addresses)
- equals(): Compares content (character sequence)

**Q6: How do you check if a string is palindrome?**
- Reverse and compare
- Two-pointer approach
- Compare characters symmetrically

**Q7: What is the time complexity of string concatenation using +?**
- O(n²) in loops due to immutability
- Each concatenation creates new string
- Use StringBuilder for O(n)

**Q8: How do you convert String to StringBuilder and vice versa?**
```java
// String to StringBuilder
StringBuilder sb = new StringBuilder(str);

// StringBuilder to String
String str = sb.toString();
```

### Common Interview Problems

**1. Anagram Check**
```java
public static boolean isAnagram(String s1, String s2) {
    if (s1.length() != s2.length()) return false;
    
    char[] arr1 = s1.toCharArray();
    char[] arr2 = s2.toCharArray();
    
    Arrays.sort(arr1);
    Arrays.sort(arr2);
    
    return Arrays.equals(arr1, arr2);
}
```

**2. Remove Duplicates**
```java
public static String removeDuplicates(String str) {
    StringBuilder sb = new StringBuilder();
    Set<Character> seen = new LinkedHashSet<>();
    
    for (char ch : str.toCharArray()) {
        if (seen.add(ch)) {
            sb.append(ch);
        }
    }
    
    return sb.toString();
}
```

**3. Count Words**
```java
public static int countWords(String str) {
    if (str == null || str.isBlank()) {
        return 0;
    }
    return str.trim().split("\\s+").length;
}
```

---

## Practice Exercises

### Exercise 1: String Basics
Write programs to:
- Check if string contains only digits
- Count spaces in a string
- Extract all digits from a string
- Convert first letter of each word to uppercase

### Exercise 2: String Manipulation
Implement:
- Remove all vowels from string
- Replace all spaces with underscores
- Remove duplicate consecutive characters
- Compress string ("aaabbb" → "a3b3")

**Solution:**
```java
public class StringManipulation {
    // Remove vowels
    public static String removeVowels(String str) {
        return str.replaceAll("[aeiouAEIOU]", "");
    }
    
    // Replace spaces
    public static String replaceSpaces(String str) {
        return str.replace(' ', '_');
    }
    
    // Remove duplicate consecutive characters
    public static String removeDuplicates(String str) {
        if (str.length() <= 1) return str;
        
        StringBuilder sb = new StringBuilder();
        sb.append(str.charAt(0));
        
        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) != str.charAt(i - 1)) {
                sb.append(str.charAt(i));
            }
        }
        
        return sb.toString();
    }
    
    // Compress string
    public static String compress(String str) {
        if (str.length() <= 1) return str;
        
        StringBuilder sb = new StringBuilder();
        int count = 1;
        
        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) == str.charAt(i - 1)) {
                count++;
            } else {
                sb.append(str.charAt(i - 1));
                if (count > 1) sb.append(count);
                count = 1;
            }
        }
        
        // Last character
        sb.append(str.charAt(str.length() - 1));
        if (count > 1) sb.append(count);
        
        return sb.length() < str.length() ? sb.toString() : str;
    }
    
    public static void main(String[] args) {
        String test = "Hello World";
        System.out.println("Remove vowels: " + removeVowels(test));
        System.out.println("Replace spaces: " + replaceSpaces(test));
        System.out.println("Remove duplicates: " + removeDuplicates("aaabbbccc"));
        System.out.println("Compress: " + compress("aaabbbccc"));
    }
}
```

### Exercise 3: Advanced Problems
Solve:
- Check if strings are rotations of each other
- Find longest common prefix
- Check balanced parentheses
- Convert Roman to Integer

### Exercise 4: Performance
Compare performance of:
- String concatenation methods
- String vs StringBuilder for 10,000 appends
- Different palindrome checking methods

---

## Further Reading

### Books
1. **"Effective Java" by Joshua Bloch**
   - Item 63: Beware performance of string concatenation
   - String best practices

2. **"Java Performance" by Scott Oaks**
   - String optimization techniques
   - Memory management

3. **"Core Java Volume I" by Cay S. Horstmann**
   - Comprehensive String coverage
   - String API reference

### Online Resources

**Official Documentation:**
- [String API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/String.html)
- [StringBuilder API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/StringBuilder.html)
- [StringBuffer API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/StringBuffer.html)

**Tutorials:**
- Baeldung: [Java String Guide](https://www.baeldung.com/java-string)
- GeeksforGeeks: [Strings in Java](https://www.geeksforgeeks.org/strings-in-java/)
- Oracle: [String Tutorial](https://docs.oracle.com/javase/tutorial/java/data/strings.html)

### Practice Platforms
- **LeetCode** - String problems (Easy to Hard)
- **HackerRank** - String manipulation challenges
- **CodeWars** - String kata exercises
- **InterviewBit** - Interview string problems

### Common String Algorithms
1. KMP (Knuth-Morris-Pratt) pattern matching
2. Rabin-Karp algorithm
3. Boyer-Moore algorithm
4. Aho-Corasick algorithm
5. Longest Common Subsequence
6. Edit Distance (Levenshtein)

### String Interview Topics
1. Palindrome variations
2. Anagram problems
3. Substring search
4. Pattern matching
5. String permutations
6. String compression
7. Longest/shortest substring problems
8. String reversal variations
9. Character frequency
10. String validation

### Next Steps

After mastering strings, proceed to:
1. **Tutorial 13: Regular Expressions** - Pattern matching
2. **Collections Framework** - Advanced data structures
3. **Stream API** - Functional string operations

---

**Congratulations!** You've completed the Strings tutorial. Strings are fundamental in Java programming, and mastering them is essential for effective text processing and manipulation.

**Key Takeaways:**
- Strings are immutable objects
- Use StringBuilder for mutable operations
- Understand String pool optimization
- Choose right string class for the job
- Master common string algorithms
- Practice string manipulation problems

Continue practicing with coding challenges to strengthen your string manipulation skills!
