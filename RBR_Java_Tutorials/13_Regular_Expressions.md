# Tutorial 13: Regular Expressions

**Video References:** Videos 129-137 (9 videos)

**Prerequisites:** Strings (Tutorial 12), Pattern Matching Basics

**Estimated Learning Time:** 4-5 hours

---

## Table of Contents

1. [Introduction to Regular Expressions](#introduction-to-regular-expressions)
2. [Searching with Meta Characters](#searching-with-meta-characters)
3. [Quantifiers](#quantifiers)
4. [Java Regex Core Classes](#java-regex-core-classes)
5. [Pattern Matching Examples](#pattern-matching-examples)
6. [matches() Method](#matches-method)
7. [split() Method](#split-method)
8. [Matcher Methods](#matcher-methods)
9. [Practical Regex Applications](#practical-regex-applications)
10. [Common Pitfalls and Best Practices](#common-pitfalls-and-best-practices)
11. [Interview Preparation](#interview-preparation)
12. [Practice Exercises](#practice-exercises)
13. [Further Reading](#further-reading)

---

## Introduction to Regular Expressions

### Video 129: Regular Expressions

**Video Reference:** `129.Java- Regular Expressions.mp4`

### What are Regular Expressions?

**Definition:** A Regular Expression (regex) is a sequence of characters that defines a search pattern. It's used for pattern matching, searching, and text manipulation.

```
┌──────────────────────────────────────────────┐
│       Regular Expression Concept             │
├──────────────────────────────────────────────┤
│                                              │
│  Pattern: "[a-z]+@[a-z]+\\.com"            │
│                                              │
│  Matches:                                    │
│    ✓ "john@example.com"                     │
│    ✓ "alice@test.com"                       │
│    ✗ "john@EXAMPLE.com" (uppercase)         │
│    ✗ "alice@test.net" (.net not .com)      │
│                                              │
│  Purpose: Validate email pattern            │
│                                              │
└──────────────────────────────────────────────┘
```

### Why Use Regular Expressions?

**Use Cases:**
1. **Validation:** Email, phone numbers, passwords
2. **Searching:** Find patterns in text
3. **Extraction:** Extract specific data from strings
4. **Replacement:** Find and replace patterns
5. **Parsing:** Break text into tokens

### Basic Regex Syntax

| Pattern | Matches | Example |
|---------|---------|---------|
| `.` | Any single character | `a.c` matches "abc", "a1c" |
| `^` | Start of string | `^Hello` matches "Hello World" |
| `$` | End of string | `end$` matches "The end" |
| `*` | Zero or more | `ab*` matches "a", "ab", "abbb" |
| `+` | One or more | `ab+` matches "ab", "abb" |
| `?` | Zero or one | `colou?r` matches "color", "colour" |
| `[]` | Character class | `[abc]` matches "a", "b", or "c" |
| `\|` | OR operator | `cat\|dog` matches "cat" or "dog" |

### Simple Examples

```java
public class RegexBasics {
    public static void main(String[] args) {
        // Basic pattern matching
        String text1 = "Hello123";
        String text2 = "World456";
        
        // Pattern: Contains digits
        System.out.println(text1.matches(".*\\d+.*"));  // true
        System.out.println(text2.matches(".*\\d+.*"));  // true
        
        // Pattern: Starts with capital letter
        System.out.println(text1.matches("^[A-Z].*"));  // true
        System.out.println("hello".matches("^[A-Z].*")); // false
        
        // Pattern: Ends with digits
        System.out.println(text1.matches(".*\\d+$"));    // true
        System.out.println("Hello".matches(".*\\d+$"));  // false
    }
}
```

### Real-World Applications

```java
public class RegexApplications {
    public static void main(String[] args) {
        // Email validation
        String email = "user@example.com";
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        System.out.println("Valid email: " + email.matches(emailRegex));
        
        // Phone number validation (US format)
        String phone = "123-456-7890";
        String phoneRegex = "\\d{3}-\\d{3}-\\d{4}";
        System.out.println("Valid phone: " + phone.matches(phoneRegex));
        
        // Password validation (min 8 chars, 1 uppercase, 1 digit)
        String password = "Password123";
        String passRegex = "^(?=.*[A-Z])(?=.*\\d).{8,}$";
        System.out.println("Valid password: " + password.matches(passRegex));
    }
}
```

---

## Searching with Meta Characters

### Video 130: Searching with Meta Characters

**Video Reference:** `130.Java- Searching with meta characters.mp4`

### Character Classes

**Predefined Character Classes:**

| Metacharacter | Description | Equivalent |
|---------------|-------------|------------|
| `\d` | Any digit | `[0-9]` |
| `\D` | Any non-digit | `[^0-9]` |
| `\w` | Word character | `[a-zA-Z0-9_]` |
| `\W` | Non-word character | `[^a-zA-Z0-9_]` |
| `\s` | Whitespace | `[ \t\n\r\f]` |
| `\S` | Non-whitespace | `[^ \t\n\r\f]` |
| `.` | Any character | (except newline) |

### Custom Character Classes

```java
public class CharacterClasses {
    public static void main(String[] args) {
        String text = "Hello World 123";
        
        // Digit check
        System.out.println("Contains digit: " + text.matches(".*\\d.*"));
        
        // Word characters only
        System.out.println("Only words: " + "HelloWorld".matches("\\w+"));
        
        // Has whitespace
        System.out.println("Has space: " + text.matches(".*\\s.*"));
        
        // Custom character class [abc]
        System.out.println("'a' in [abc]: " + "a".matches("[abc]"));
        System.out.println("'d' in [abc]: " + "d".matches("[abc]"));
        
        // Range [a-z]
        System.out.println("Lowercase: " + "hello".matches("[a-z]+"));
        
        // Negation [^abc]
        System.out.println("Not abc: " + "d".matches("[^abc]"));
        
        // Multiple ranges
        System.out.println("Alphanumeric: " + "Hello123".matches("[a-zA-Z0-9]+"));
    }
}
```

### Boundary Matchers

| Metacharacter | Description | Example |
|---------------|-------------|---------|
| `^` | Start of line | `^Hello` |
| `$` | End of line | `end$` |
| `\b` | Word boundary | `\bword\b` |
| `\B` | Non-word boundary | `\Bword\B` |

```java
public class BoundaryMatchers {
    public static void main(String[] args) {
        // Start of string
        System.out.println("Starts with 'Hello': " + 
                          "Hello World".matches("^Hello.*"));
        
        // End of string
        System.out.println("Ends with 'World': " + 
                          "Hello World".matches(".*World$"));
        
        // Word boundary
        String text = "The cat sat on the mat";
        System.out.println("Has word 'cat': " + 
                          text.matches(".*\\bcat\\b.*"));
        System.out.println("Has word 'at': " + 
                          text.matches(".*\\bat\\b.*"));  // false (part of 'cat')
    }
}
```

### Greedy vs Reluctant Quantifiers

```java
import java.util.regex.*;

public class GreedyVsReluctant {
    public static void main(String[] args) {
        String text = "<div>Content1</div><div>Content2</div>";
        
        // Greedy (default) - matches as much as possible
        Pattern greedy = Pattern.compile("<div>.*</div>");
        Matcher greedyMatch = greedy.matcher(text);
        if (greedyMatch.find()) {
            System.out.println("Greedy: " + greedyMatch.group());
            // Output: <div>Content1</div><div>Content2</div>
        }
        
        // Reluctant - matches as little as possible
        Pattern reluctant = Pattern.compile("<div>.*?</div>");
        Matcher reluctantMatch = reluctant.matcher(text);
        while (reluctantMatch.find()) {
            System.out.println("Reluctant: " + reluctantMatch.group());
            // Output: <div>Content1</div>
            //         <div>Content2</div>
        }
    }
}
```

---

## Quantifiers

### Video 131: Quantifiers

**Video Reference:** `131.Java- Quantifiers.mp4`

### Quantifier Types

**Basic Quantifiers:**

| Quantifier | Description | Example |
|------------|-------------|---------|
| `*` | Zero or more | `a*` matches "", "a", "aa", "aaa" |
| `+` | One or more | `a+` matches "a", "aa", "aaa" |
| `?` | Zero or one | `colou?r` matches "color", "colour" |
| `{n}` | Exactly n times | `a{3}` matches "aaa" |
| `{n,}` | At least n times | `a{2,}` matches "aa", "aaa", "aaaa" |
| `{n,m}` | Between n and m times | `a{2,4}` matches "aa", "aaa", "aaaa" |

### Quantifier Modes

| Mode | Symbol | Description |
|------|--------|-------------|
| **Greedy** | (default) | Match as much as possible |
| **Reluctant** | `?` | Match as little as possible |
| **Possessive** | `+` | Match as much as possible, no backtracking |

```java
public class QuantifiersDemo {
    public static void main(String[] args) {
        // Zero or more (*)
        System.out.println("'' matches a*: " + "".matches("a*"));           // true
        System.out.println("'a' matches a*: " + "a".matches("a*"));         // true
        System.out.println("'aaa' matches a*: " + "aaa".matches("a*"));     // true
        
        // One or more (+)
        System.out.println("'' matches a+: " + "".matches("a+"));           // false
        System.out.println("'a' matches a+: " + "a".matches("a+"));         // true
        System.out.println("'aaa' matches a+: " + "aaa".matches("a+"));     // true
        
        // Zero or one (?)
        System.out.println("'color' matches colou?r: " + 
                          "color".matches("colou?r"));                      // true
        System.out.println("'colour' matches colou?r: " + 
                          "colour".matches("colou?r"));                     // true
        
        // Exactly n times {n}
        System.out.println("'aaa' matches a{3}: " + "aaa".matches("a{3}"));  // true
        System.out.println("'aa' matches a{3}: " + "aa".matches("a{3}"));    // false
        
        // At least n times {n,}
        System.out.println("'aa' matches a{2,}: " + "aa".matches("a{2,}"));  // true
        System.out.println("'aaa' matches a{2,}: " + "aaa".matches("a{2,}")); // true
        
        // Between n and m times {n,m}
        System.out.println("'aa' matches a{2,4}: " + "aa".matches("a{2,4}")); // true
        System.out.println("'aaa' matches a{2,4}: " + "aaa".matches("a{2,4}")); // true
        System.out.println("'aaaaa' matches a{2,4}: " + "aaaaa".matches("a{2,4}")); // false
    }
}
```

### Practical Examples

```java
public class QuantifierExamples {
    public static void main(String[] args) {
        // Phone number: 10 digits
        String phone1 = "1234567890";
        String phone2 = "123456789";  // too short
        System.out.println("Valid phone 1: " + phone1.matches("\\d{10}"));  // true
        System.out.println("Valid phone 2: " + phone2.matches("\\d{10}"));  // false
        
        // Password: 8-16 characters
        String pass1 = "Pass1234";
        String pass2 = "Pass";  // too short
        System.out.println("Valid pass 1: " + pass1.matches(".{8,16}"));    // true
        System.out.println("Valid pass 2: " + pass2.matches(".{8,16}"));    // false
        
        // ZIP code: 5 or 9 digits (with optional dash)
        String zip1 = "12345";
        String zip2 = "12345-6789";
        String zipRegex = "\\d{5}(-\\d{4})?";
        System.out.println("Valid zip 1: " + zip1.matches(zipRegex));       // true
        System.out.println("Valid zip 2: " + zip2.matches(zipRegex));       // true
        
        // URL: http(s) optional
        String url1 = "http://example.com";
        String url2 = "https://example.com";
        String urlRegex = "https?://.*";
        System.out.println("Valid URL 1: " + url1.matches(urlRegex));       // true
        System.out.println("Valid URL 2: " + url2.matches(urlRegex));       // true
    }
}
```

---

## Java Regex Core Classes

### Video 132: Java Regex Core Classes

**Video Reference:** `132.Java- Java regex core classes.mp4`

### Pattern Class

**Definition:** Represents a compiled regular expression pattern.

```java
import java.util.regex.Pattern;

public class PatternClass {
    public static void main(String[] args) {
        // Compile a pattern
        Pattern pattern = Pattern.compile("[a-z]+");
        
        // Get pattern string
        System.out.println("Pattern: " + pattern.pattern());
        
        // Split string using pattern
        String text = "apple,banana,cherry";
        Pattern commaPattern = Pattern.compile(",");
        String[] fruits = commaPattern.split(text);
        for (String fruit : fruits) {
            System.out.println(fruit);
        }
        
        // Pattern flags
        Pattern caseInsensitive = Pattern.compile("hello", Pattern.CASE_INSENSITIVE);
        System.out.println("Matches HELLO: " + 
                          caseInsensitive.matcher("HELLO").matches());  // true
    }
}
```

### Pattern Flags

| Flag | Description |
|------|-------------|
| `Pattern.CASE_INSENSITIVE` | Case-insensitive matching |
| `Pattern.MULTILINE` | `^` and `$` match line boundaries |
| `Pattern.DOTALL` | `.` matches any character including newline |
| `Pattern.UNICODE_CASE` | Unicode-aware case folding |
| `Pattern.COMMENTS` | Whitespace and comments ignored |

### Matcher Class

**Definition:** Engine that performs match operations on a character sequence.

```java
import java.util.regex.*;

public class MatcherClass {
    public static void main(String[] args) {
        String text = "The price is $100 and the tax is $15";
        Pattern pattern = Pattern.compile("\\$(\\d+)");
        Matcher matcher = pattern.matcher(text);
        
        // Find all matches
        System.out.println("=== Finding Prices ===");
        while (matcher.find()) {
            System.out.println("Found: " + matcher.group());
            System.out.println("Value: " + matcher.group(1));  // Captured group
            System.out.println("Position: " + matcher.start() + "-" + matcher.end());
        }
        
        // Reset and replace
        matcher.reset();
        String replaced = matcher.replaceAll("USD \\$1");
        System.out.println("\nReplaced: " + replaced);
    }
}
```

### PatternSyntaxException

```java
import java.util.regex.*;

public class PatternExceptionHandling {
    public static void main(String[] args) {
        try {
            // Invalid regex - unclosed bracket
            Pattern pattern = Pattern.compile("[a-z");
        } catch (PatternSyntaxException e) {
            System.out.println("Invalid regex pattern!");
            System.out.println("Description: " + e.getDescription());
            System.out.println("Index: " + e.getIndex());
            System.out.println("Pattern: " + e.getPattern());
        }
    }
}
```

---

## Pattern Matching Examples

### Video 133: Example on Pattern Matching

**Video Reference:** `133.Java- Example on pattern matching.mp4`

### Email Validation

```java
import java.util.regex.*;

public class EmailValidation {
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
    public static void main(String[] args) {
        String[] emails = {
            "user@example.com",           // Valid
            "user.name@example.co.uk",    // Valid
            "user+tag@example.com",       // Valid
            "@example.com",               // Invalid - no local part
            "user@",                      // Invalid - no domain
            "user@.com",                  // Invalid - domain starts with dot
            "user@example"                // Invalid - no TLD
        };
        
        for (String email : emails) {
            System.out.println(email + " : " + isValidEmail(email));
        }
    }
}
```

### Phone Number Validation

```java
import java.util.regex.*;

public class PhoneValidation {
    // US phone formats: (123) 456-7890, 123-456-7890, 1234567890
    public static boolean isValidUSPhone(String phone) {
        String[] patterns = {
            "\\(\\d{3}\\) \\d{3}-\\d{4}",  // (123) 456-7890
            "\\d{3}-\\d{3}-\\d{4}",         // 123-456-7890
            "\\d{10}"                       // 1234567890
        };
        
        for (String pattern : patterns) {
            if (phone.matches(pattern)) {
                return true;
            }
        }
        return false;
    }
    
    public static void main(String[] args) {
        String[] phones = {
            "(123) 456-7890",    // Valid
            "123-456-7890",      // Valid
            "1234567890",        // Valid
            "123-45-6789",       // Invalid
            "12345678901"        // Invalid - too long
        };
        
        for (String phone : phones) {
            System.out.println(phone + " : " + isValidUSPhone(phone));
        }
    }
}
```

### URL Validation

```java
import java.util.regex.*;

public class URLValidation {
    public static boolean isValidURL(String url) {
        String urlRegex = "^(https?://)?" +                    // Protocol (optional)
                         "([\\w-]+\\.)+[\\w-]+" +             // Domain
                         "(/[\\w-./?%&=]*)?$";                // Path (optional)
        
        return url.matches(urlRegex);
    }
    
    public static void main(String[] args) {
        String[] urls = {
            "http://www.example.com",              // Valid
            "https://example.com/path/to/page",    // Valid
            "www.example.com",                     // Valid (no protocol)
            "example.com",                         // Valid
            "htp://example.com",                   // Invalid protocol
            "http://",                             // Invalid - no domain
        };
        
        for (String url : urls) {
            System.out.println(url + " : " + isValidURL(url));
        }
    }
}
```

### Password Strength Validation

```java
import java.util.regex.*;

public class PasswordValidation {
    public static class PasswordStrength {
        public static boolean hasMinLength(String password, int minLength) {
            return password.length() >= minLength;
        }
        
        public static boolean hasUpperCase(String password) {
            return password.matches(".*[A-Z].*");
        }
        
        public static boolean hasLowerCase(String password) {
            return password.matches(".*[a-z].*");
        }
        
        public static boolean hasDigit(String password) {
            return password.matches(".*\\d.*");
        }
        
        public static boolean hasSpecialChar(String password) {
            return password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
        }
        
        public static String checkStrength(String password) {
            int score = 0;
            
            if (hasMinLength(password, 8)) score++;
            if (hasUpperCase(password)) score++;
            if (hasLowerCase(password)) score++;
            if (hasDigit(password)) score++;
            if (hasSpecialChar(password)) score++;
            
            if (score <= 2) return "Weak";
            if (score <= 4) return "Medium";
            return "Strong";
        }
    }
    
    public static void main(String[] args) {
        String[] passwords = {
            "pass",                  // Weak
            "password123",           // Medium
            "Password123",           // Medium
            "Password123!",          // Strong
            "P@ssw0rd!123"          // Strong
        };
        
        for (String password : passwords) {
            System.out.println(password + " : " + 
                             PasswordStrength.checkStrength(password));
        }
    }
}
```

---

## matches() Method

### Video 134: matches() Method

**Video Reference:** `134.Java- matches() method.mp4`

### String.matches()

**Definition:** Tests whether the entire string matches the given regex pattern.

```java
public class MatchesMethod {
    public static void main(String[] args) {
        String text = "Hello123";
        
        // Matches entire string
        System.out.println("Contains digits: " + text.matches(".*\\d+.*"));  // true
        System.out.println("Only digits: " + text.matches("\\d+"));          // false
        System.out.println("Starts with Hello: " + text.matches("Hello.*")); // true
        
        // Case sensitive by default
        System.out.println("hello123 matches: " + "hello123".matches("Hello.*")); // false
        
        // Empty string
        System.out.println("Empty matches .*: " + "".matches(".*"));         // true
        System.out.println("Empty matches .+: " + "".matches(".+"));         // false
    }
}
```

### Pattern.matches()

```java
import java.util.regex.Pattern;

public class PatternMatches {
    public static void main(String[] args) {
        String text = "abc123";
        String pattern = "[a-z]+\\d+";
        
        // Static method
        boolean matches = Pattern.matches(pattern, text);
        System.out.println("Matches: " + matches);  // true
        
        // Equivalent to:
        boolean matches2 = text.matches(pattern);
        System.out.println("Matches2: " + matches2); // true
    }
}
```

### Matcher.matches()

```java
import java.util.regex.*;

public class MatcherMatches {
    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
        
        String[] phones = {
            "123-456-7890",   // Valid
            "123-45-6789",    // Invalid
            "1234567890"      // Invalid
        };
        
        for (String phone : phones) {
            Matcher matcher = pattern.matcher(phone);
            System.out.println(phone + " matches: " + matcher.matches());
        }
    }
}
```

### matches() vs find()

```java
import java.util.regex.*;

public class MatchesVsFind {
    public static void main(String[] args) {
        String text = "The price is $100";
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(text);
        
        // matches() - checks entire string
        System.out.println("matches(): " + matcher.matches());  // false
        
        // find() - finds pattern anywhere
        matcher.reset();  // Reset after matches()
        System.out.println("find(): " + matcher.find());        // true
        if (matcher.find()) {
            System.out.println("Found: " + matcher.group());    // 100
        }
    }
}
```

---

## split() Method

### Video 135: Example on split()

**Video Reference:** `135.Java- example on split().mp4`

### String.split()

```java
public class StringSplit {
    public static void main(String[] args) {
        String text = "apple,banana,cherry,date";
        
        // Split by comma
        String[] fruits = text.split(",");
        System.out.println("=== Split by comma ===");
        for (String fruit : fruits) {
            System.out.println(fruit);
        }
        
        // Split with limit
        String[] limited = text.split(",", 2);
        System.out.println("\n=== Split with limit 2 ===");
        for (String part : limited) {
            System.out.println(part);
        }
        
        // Split by multiple delimiters
        String text2 = "apple;banana,cherry:date";
        String[] fruits2 = text2.split("[;,:]+");
        System.out.println("\n=== Split by multiple delimiters ===");
        for (String fruit : fruits2) {
            System.out.println(fruit);
        }
        
        // Split by whitespace
        String text3 = "Hello   World   Java";
        String[] words = text3.split("\\s+");
        System.out.println("\n=== Split by whitespace ===");
        for (String word : words) {
            System.out.println(word);
        }
    }
}
```

### Pattern.split()

```java
import java.util.regex.Pattern;

public class PatternSplit {
    public static void main(String[] args) {
        // Compile pattern once, reuse multiple times
        Pattern pattern = Pattern.compile(",");
        
        String text1 = "a,b,c,d";
        String text2 = "1,2,3,4,5";
        
        String[] parts1 = pattern.split(text1);
        String[] parts2 = pattern.split(text2);
        
        System.out.println("Text 1: " + java.util.Arrays.toString(parts1));
        System.out.println("Text 2: " + java.util.Arrays.toString(parts2));
    }
}
```

### Advanced Splitting

```java
import java.util.regex.Pattern;

public class AdvancedSplit {
    public static void main(String[] args) {
        // Split by word boundaries
        String text1 = "HelloWorldJava";
        String[] words1 = text1.split("(?=[A-Z])");
        System.out.println("Camel case split: " + 
                          java.util.Arrays.toString(words1));
        
        // Split by digits
        String text2 = "Section1Part2Chapter3";
        String[] parts = text2.split("\\d+");
        System.out.println("Split by digits: " + 
                          java.util.Arrays.toString(parts));
        
        // Split and keep delimiters using lookahead
        String text3 = "apple,banana;cherry:date";
        String[] fruits = text3.split("(?=[,;:])");
        System.out.println("Keep delimiters: " + 
                          java.util.Arrays.toString(fruits));
        
        // Split CSV with quoted fields
        String csv = "\"John Doe\",25,\"New York\"";
        String[] fields = csv.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println("CSV split: " + 
                          java.util.Arrays.toString(fields));
    }
}
```

---

## Matcher Methods

### Video 136: Matcher Methods

**Video Reference:** `136.Java- Matcher methods.mp4`

### Finding Methods

```java
import java.util.regex.*;

public class MatcherFindMethods {
    public static void main(String[] args) {
        String text = "The prices are $10, $20, and $30";
        Pattern pattern = Pattern.compile("\\$(\\d+)");
        Matcher matcher = pattern.matcher(text);
        
        // find() - find next match
        while (matcher.find()) {
            System.out.println("Found: " + matcher.group());
            System.out.println("Price: $" + matcher.group(1));
            System.out.println("Start: " + matcher.start());
            System.out.println("End: " + matcher.end());
            System.out.println();
        }
        
        // lookingAt() - match at beginning
        matcher.reset();
        System.out.println("Starts with pattern: " + matcher.lookingAt());  // false
        
        // matches() - match entire string
        matcher.reset();
        System.out.println("Entire match: " + matcher.matches());  // false
    }
}
```

### Replacement Methods

```java
import java.util.regex.*;

public class MatcherReplaceMethods {
    public static void main(String[] args) {
        String text = "The price is $10 and the tax is $5";
        Pattern pattern = Pattern.compile("\\$(\\d+)");
        Matcher matcher = pattern.matcher(text);
        
        // replaceAll() - replace all matches
        String replaced1 = matcher.replaceAll("USD \\$1");
        System.out.println("replaceAll: " + replaced1);
        // Output: The price is USD $10 and the tax is USD $5
        
        // replaceFirst() - replace first match only
        matcher.reset();
        String replaced2 = matcher.replaceFirst("USD \\$1");
        System.out.println("replaceFirst: " + replaced2);
        // Output: The price is USD $10 and the tax is $5
        
        // appendReplacement() and appendTail()
        matcher.reset();
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String price = matcher.group(1);
            int doubled = Integer.parseInt(price) * 2;
            matcher.appendReplacement(sb, "\\$" + doubled);
        }
        matcher.appendTail(sb);
        System.out.println("Custom replacement: " + sb.toString());
        // Output: The price is $20 and the tax is $10
    }
}
```

### Group Methods

```java
import java.util.regex.*;

public class MatcherGroupMethods {
    public static void main(String[] args) {
        String text = "John Doe, Age: 30, Email: john@example.com";
        Pattern pattern = Pattern.compile("(\\w+)\\s(\\w+),\\sAge:\\s(\\d+)");
        Matcher matcher = pattern.matcher(text);
        
        if (matcher.find()) {
            System.out.println("Full match: " + matcher.group());      // Entire match
            System.out.println("First name: " + matcher.group(1));     // First group
            System.out.println("Last name: " + matcher.group(2));      // Second group
            System.out.println("Age: " + matcher.group(3));            // Third group
            System.out.println("Group count: " + matcher.groupCount()); // 3
        }
        
        // Named groups (Java 7+)
        Pattern namedPattern = Pattern.compile(
            "(?<first>\\w+)\\s(?<last>\\w+),\\sAge:\\s(?<age>\\d+)"
        );
        Matcher namedMatcher = namedPattern.matcher(text);
        
        if (namedMatcher.find()) {
            System.out.println("\n=== Named Groups ===");
            System.out.println("First: " + namedMatcher.group("first"));
            System.out.println("Last: " + namedMatcher.group("last"));
            System.out.println("Age: " + namedMatcher.group("age"));
        }
    }
}
```

### Video 137: Example on Matcher Methods

**Video Reference:** `137.Java- example on matcher methods.mp4`

```java
import java.util.regex.*;

public class MatcherMethodsComplete {
    public static void main(String[] args) {
        // Example 1: Extract all emails
        String text1 = "Contact us at support@example.com or sales@example.com";
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
        Matcher emailMatcher = emailPattern.matcher(text1);
        
        System.out.println("=== Extract Emails ===");
        while (emailMatcher.find()) {
            System.out.println("Email: " + emailMatcher.group());
        }
        
        // Example 2: Parse log entries
        String log = "[2024-01-15 10:30:45] ERROR: Connection failed";
        Pattern logPattern = Pattern.compile("\\[(\\d{4}-\\d{2}-\\d{2})\\s(\\d{2}:\\d{2}:\\d{2})\\]\\s(\\w+):\\s(.+)");
        Matcher logMatcher = logPattern.matcher(log);
        
        if (logMatcher.find()) {
            System.out.println("\n=== Parse Log Entry ===");
            System.out.println("Date: " + logMatcher.group(1));
            System.out.println("Time: " + logMatcher.group(2));
            System.out.println("Level: " + logMatcher.group(3));
            System.out.println("Message: " + logMatcher.group(4));
        }
        
        // Example 3: Validate and extract parts
        String phone = "Call me at (123) 456-7890";
        Pattern phonePattern = Pattern.compile("\\((\\d{3})\\)\\s(\\d{3})-(\\d{4})");
        Matcher phoneMatcher = phonePattern.matcher(phone);
        
        if (phoneMatcher.find()) {
            System.out.println("\n=== Extract Phone Parts ===");
            System.out.println("Area code: " + phoneMatcher.group(1));
            System.out.println("Exchange: " + phoneMatcher.group(2));
            System.out.println("Number: " + phoneMatcher.group(3));
        }
    }
}
```

---

## Practical Regex Applications

### HTML Tag Removal

```java
import java.util.regex.*;

public class HTMLTagRemoval {
    public static String removeHTMLTags(String html) {
        // Remove all HTML tags
        return html.replaceAll("<[^>]+>", "");
    }
    
    public static String extractText(String html) {
        // Extract text between tags
        Pattern pattern = Pattern.compile(">[^<]+<");
        Matcher matcher = pattern.matcher(html);
        StringBuilder text = new StringBuilder();
        
        while (matcher.find()) {
            String match = matcher.group();
            text.append(match.substring(1, match.length() - 1));
        }
        
        return text.toString();
    }
    
    public static void main(String[] args) {
        String html = "<p>Hello <strong>World</strong>!</p>";
        
        System.out.println("Original: " + html);
        System.out.println("Removed tags: " + removeHTMLTags(html));
        System.out.println("Extracted text: " + extractText(html));
    }
}
```

### Data Extraction from Structured Text

```java
import java.util.regex.*;
import java.util.*;

public class DataExtraction {
    public static void main(String[] args) {
        String data = """
            Name: John Doe, Age: 30, Salary: $50000
            Name: Jane Smith, Age: 28, Salary: $55000
            Name: Bob Johnson, Age: 35, Salary: $60000
            """;
        
        Pattern pattern = Pattern.compile(
            "Name: ([^,]+), Age: (\\d+), Salary: \\$(\\d+)"
        );
        Matcher matcher = pattern.matcher(data);
        
        System.out.println("=== Employee Data ===");
        while (matcher.find()) {
            String name = matcher.group(1);
            int age = Integer.parseInt(matcher.group(2));
            int salary = Integer.parseInt(matcher.group(3));
            
            System.out.printf("%-15s Age: %d  Salary: $%,d%n", 
                            name, age, salary);
        }
    }
}
```

### Log File Analysis

```java
import java.util.regex.*;
import java.util.*;

public class LogAnalysis {
    public static void main(String[] args) {
        String[] logs = {
            "[2024-01-15 10:30:45] ERROR: Database connection failed",
            "[2024-01-15 10:31:12] INFO: Retrying connection",
            "[2024-01-15 10:31:45] ERROR: Connection timeout",
            "[2024-01-15 10:32:00] INFO: Connection successful"
        };
        
        Pattern pattern = Pattern.compile(
            "\\[(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\] (\\w+): (.+)"
        );
        
        Map<String, Integer> levelCount = new HashMap<>();
        
        for (String log : logs) {
            Matcher matcher = pattern.matcher(log);
            if (matcher.find()) {
                String timestamp = matcher.group(1);
                String level = matcher.group(2);
                String message = matcher.group(3);
                
                levelCount.put(level, levelCount.getOrDefault(level, 0) + 1);
                
                if (level.equals("ERROR")) {
                    System.out.println("ERROR at " + timestamp + ": " + message);
                }
            }
        }
        
        System.out.println("\n=== Log Summary ===");
        levelCount.forEach((level, count) -> 
            System.out.println(level + ": " + count + " entries"));
    }
}
```

### Input Sanitization

```java
import java.util.regex.*;

public class InputSanitization {
    // Remove special characters except allowed ones
    public static String sanitizeUsername(String username) {
        return username.replaceAll("[^a-zA-Z0-9_-]", "");
    }
    
    // Remove SQL injection attempts
    public static String sanitizeSQL(String input) {
        return input.replaceAll("('|(--|;|/\\*|\\*/|xp_|sp_))", "");
    }
    
    // Remove script tags
    public static String sanitizeHTML(String html) {
        return html.replaceAll("<script[^>]*>.*?</script>", "");
    }
    
    // Validate and clean phone number
    public static String cleanPhoneNumber(String phone) {
        // Extract only digits
        return phone.replaceAll("[^0-9]", "");
    }
    
    public static void main(String[] args) {
        System.out.println("Username: " + sanitizeUsername("user@123!"));
        System.out.println("SQL: " + sanitizeSQL("SELECT * FROM users; DROP TABLE users;"));
        System.out.println("Phone: " + cleanPhoneNumber("(123) 456-7890"));
    }
}
```

---

## Common Pitfalls and Best Practices

### Common Mistakes

**1. Forgetting to Escape Special Characters**
```java
// ❌ Wrong - . matches any character
String pattern1 = "192.168.1.1";
System.out.println("192X168X1X1".matches(pattern1));  // true (wrong!)

// ✅ Correct - escape the dot
String pattern2 = "192\\.168\\.1\\.1";
System.out.println("192.168.1.1".matches(pattern2));  // true
System.out.println("192X168X1X1".matches(pattern2));  // false
```

**2. Using matches() Instead of find()**
```java
String text = "The price is $100";
Pattern pattern = Pattern.compile("\\d+");
Matcher matcher = pattern.matcher(text);

// ❌ Wrong - matches() checks entire string
System.out.println(matcher.matches());  // false

// ✅ Correct - find() searches within string
matcher.reset();
System.out.println(matcher.find());     // true
```

**3. Not Compiling Patterns for Reuse**
```java
// ❌ Bad - compiles pattern every iteration
for (String email : emails) {
    if (email.matches("[a-z]+@[a-z]+\\.com")) {  // Inefficient
        // ...
    }
}

// ✅ Good - compile once, reuse
Pattern pattern = Pattern.compile("[a-z]+@[a-z]+\\.com");
for (String email : emails) {
    if (pattern.matcher(email).matches()) {
        // ...
    }
}
```

**4. Greedy vs Reluctant Quantifiers**
```java
String html = "<div>Content1</div><div>Content2</div>";

// ❌ Greedy - matches too much
System.out.println(html.replaceAll("<div>.*</div>", "X"));
// Output: X

// ✅ Reluctant - matches correctly
System.out.println(html.replaceAll("<div>.*?</div>", "X"));
// Output: XX
```

### Best Practices

**1. Compile and Reuse Patterns**
```java
// ✅ Good practice
public class EmailValidator {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    
    public static boolean isValid(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
```

**2. Use Comments Mode for Complex Patterns**
```java
Pattern pattern = Pattern.compile(
    "(?x)" +                    // Enable comments mode
    "^" +                       // Start of string
    "[a-zA-Z0-9._%+-]+" +      // Local part
    "@" +                       // @ symbol
    "[a-zA-Z0-9.-]+" +         // Domain
    "\\." +                     // Dot
    "[a-zA-Z]{2,}" +           // TLD
    "$"                         // End of string
);
```

**3. Handle PatternSyntaxException**
```java
public static Pattern safeCompile(String regex) {
    try {
        return Pattern.compile(regex);
    } catch (PatternSyntaxException e) {
        System.err.println("Invalid regex: " + e.getMessage());
        return null;
    }
}
```

**4. Use Named Groups for Readability**
```java
Pattern pattern = Pattern.compile(
    "(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})"
);
Matcher matcher = pattern.matcher("2024-01-15");

if (matcher.find()) {
    System.out.println("Year: " + matcher.group("year"));
    System.out.println("Month: " + matcher.group("month"));
    System.out.println("Day: " + matcher.group("day"));
}
```

---

## Interview Preparation

### Frequently Asked Questions

**Q1: What is a regular expression?**
- Sequence of characters defining a search pattern
- Used for pattern matching, validation, extraction
- Powerful text processing tool

**Q2: What is the difference between matches() and find()?**
- `matches()`: Checks if entire string matches pattern
- `find()`: Searches for pattern anywhere in string

**Q3: What are greedy and reluctant quantifiers?**
- Greedy: Match as much as possible (default)
- Reluctant: Match as little as possible (use ?)

**Q4: How do you escape special characters?**
- Use backslash: `\\.` for literal dot
- Or use `Pattern.quote()` for string literals

**Q5: What is a capturing group?**
- Part of pattern enclosed in parentheses
- Captured for later use with `group()` method

**Q6: Difference between `\d` and `[0-9]`?**
- Functionally equivalent in basic cases
- `\d` may match Unicode digits depending on flags

**Q7: How to make regex case-insensitive?**
```java
Pattern pattern = Pattern.compile("hello", Pattern.CASE_INSENSITIVE);
// or
String text = "HELLO";
System.out.println(text.matches("(?i)hello"));  // true
```

**Q8: What is lookahead and lookbehind?**
```java
// Positive lookahead: (?=pattern)
"hello123".matches("hello(?=\\d+)");  // true

// Negative lookahead: (?!pattern)
"hello".matches("hello(?!\\d+)");     // true

// Positive lookbehind: (?<=pattern)
"123hello".matches("(?<=\\d+)hello"); // true

// Negative lookbehind: (?<!pattern)
"hello".matches("(?<!\\d+)hello");    // true
```

### Common Interview Problems

**1. Validate Email**
```java
public static boolean isValidEmail(String email) {
    return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
}
```

**2. Extract All Numbers**
```java
public static List<Integer> extractNumbers(String text) {
    List<Integer> numbers = new ArrayList<>();
    Pattern pattern = Pattern.compile("\\d+");
    Matcher matcher = pattern.matcher(text);
    
    while (matcher.find()) {
        numbers.add(Integer.parseInt(matcher.group()));
    }
    
    return numbers;
}
```

**3. Replace Multiple Spaces with Single Space**
```java
public static String normalizeSpaces(String text) {
    return text.replaceAll("\\s+", " ").trim();
}
```

---

## Practice Exercises

### Exercise 1: Basic Validation
Write regex patterns to validate:
- IPv4 addresses (e.g., 192.168.1.1)
- Time in HH:MM format
- Credit card numbers (with optional spaces/dashes)
- Hexadecimal color codes (#RGB or #RRGGBB)

### Exercise 2: Text Processing
Implement methods to:
- Extract all URLs from text
- Remove duplicate whitespace
- Convert camelCase to snake_case
- Find all hashtags in social media text

**Solution:**
```java
import java.util.regex.*;
import java.util.*;

public class TextProcessing {
    // Extract URLs
    public static List<String> extractURLs(String text) {
        List<String> urls = new ArrayList<>();
        Pattern pattern = Pattern.compile(
            "https?://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}[/\\w.-]*"
        );
        Matcher matcher = pattern.matcher(text);
        
        while (matcher.find()) {
            urls.add(matcher.group());
        }
        
        return urls;
    }
    
    // Remove duplicate whitespace
    public static String removeDuplicateSpaces(String text) {
        return text.replaceAll("\\s+", " ").trim();
    }
    
    // CamelCase to snake_case
    public static String camelToSnake(String camel) {
        return camel.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
    
    // Find hashtags
    public static List<String> findHashtags(String text) {
        List<String> hashtags = new ArrayList<>();
        Pattern pattern = Pattern.compile("#\\w+");
        Matcher matcher = pattern.matcher(text);
        
        while (matcher.find()) {
            hashtags.add(matcher.group());
        }
        
        return hashtags;
    }
    
    public static void main(String[] args) {
        String text = "Check out https://example.com and #Java #Programming";
        
        System.out.println("URLs: " + extractURLs(text));
        System.out.println("Hashtags: " + findHashtags(text));
        System.out.println("snake_case: " + camelToSnake("camelCaseText"));
    }
}
```

### Exercise 3: Data Validation
Create validators for:
- Password strength (min 8 chars, 1 uppercase, 1 digit, 1 special)
- ISBN-10 and ISBN-13
- MAC addresses
- Social Security Numbers (SSN)

### Exercise 4: Advanced Parsing
Implement:
- Parse and validate JSON-like structure
- Extract key-value pairs from config files
- Parse CSV with escaped commas
- Extract code blocks from markdown

---

## Further Reading

### Books
1. **"Mastering Regular Expressions" by Jeffrey Friedl**
   - Comprehensive regex guide
   - Performance optimization

2. **"Regular Expressions Cookbook" by Jan Goyvaerts**
   - Practical regex solutions
   - Common patterns

3. **"Java Regular Expressions" by Mehran Habibi**
   - Java-specific regex guide
   - Real-world examples

### Online Resources

**Official Documentation:**
- [Pattern API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/regex/Pattern.html)
- [Matcher API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/regex/Matcher.html)
- [Oracle Regex Tutorial](https://docs.oracle.com/javase/tutorial/essential/regex/)

**Interactive Tools:**
- [Regex101](https://regex101.com/) - Regex tester with explanation
- [RegExr](https://regexr.com/) - Learn, build, test regex
- [RegexPal](https://www.regexpal.com/) - Online regex tester

**Practice Platforms:**
- [RegexOne](https://regexone.com/) - Interactive regex tutorial
- [HackerRank Regex](https://www.hackerrank.com/domains/regex)
- [LeetCode String Problems](https://leetcode.com/tag/string/)

### Common Regex Patterns

**Email:**
```regex
^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$
```

**URL:**
```regex
^(https?://)?(www\.)?([a-zA-Z0-9-]+\.)+[a-zA-Z]{2,}(/.*)?$
```

**Phone (US):**
```regex
^\(?\d{3}\)?[-.\s]?\d{3}[-.\s]?\d{4}$
```

**Date (YYYY-MM-DD):**
```regex
^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$
```

**IPv4:**
```regex
^(\d{1,3}\.){3}\d{1,3}$
```

**Credit Card:**
```regex
^\d{4}[-\s]?\d{4}[-\s]?\d{4}[-\s]?\d{4}$
```

### Performance Tips

1. **Compile patterns once**
2. **Use specific quantifiers** ({n,m} vs +/*)
3. **Avoid catastrophic backtracking**
4. **Use possessive quantifiers** when appropriate
5. **Profile complex patterns**
6. **Consider alternatives** for simple operations

### Next Steps

After mastering regular expressions, proceed to:
1. **Tutorial 14: Packages** - Code organization
2. **Advanced String Processing** - Stream API
3. **Text Mining** - Natural language processing

---

**Congratulations!** You've completed the Regular Expressions tutorial. Regex is a powerful tool for text processing and validation.

**Key Takeaways:**
- Regular expressions define search patterns
- Java provides Pattern and Matcher classes
- Metacharacters and quantifiers are fundamental
- Use appropriate quantifiers (greedy vs reluctant)
- Compile patterns for reuse
- Practice with real-world problems

Continue practicing with regex challenges to master pattern matching!
