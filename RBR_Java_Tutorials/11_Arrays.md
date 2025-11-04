# Tutorial 11: Arrays

**Video References:** Videos 105-116 (12 videos)

**Prerequisites:** Variables (Tutorial 03), Datatypes (Tutorial 04), Control Statements (Tutorial 06)

**Estimated Learning Time:** 4-5 hours

---

## Table of Contents

1. [Introduction to Arrays](#introduction-to-arrays)
2. [Declaring, Constructing, and Initializing Arrays](#declaring-constructing-and-initializing-arrays)
3. [Arrays: Primitive or Object?](#arrays-primitive-or-object)
4. [Passing Arrays to Methods](#passing-arrays-to-methods)
5. [Copying Arrays](#copying-arrays)
6. [Practical Examples](#practical-examples)
7. [Command Line Arguments](#command-line-arguments)
8. [Parallel Arrays](#parallel-arrays)
9. [Two-Dimensional Arrays](#two-dimensional-arrays)
10. [Array Operations](#array-operations)
11. [Common Problems and Solutions](#common-problems-and-solutions)
12. [Interview Preparation](#interview-preparation)
13. [Practice Exercises](#practice-exercises)
14. [Further Reading](#further-reading)

---

## Introduction to Arrays

### Video 105: Introduction to Arrays

**Video Reference:** `105.Java- Introduction to arrays.mp4`

### What is an Array?

**Definition:** An array is a container object that holds a fixed number of values of a single type. The length of an array is established when the array is created and cannot be changed after creation.

```
┌──────────────────────────────────────────────┐
│             Array Concept                    │
├──────────────────────────────────────────────┤
│                                              │
│  Array Name: numbers                         │
│                                              │
│  Index:  [0]  [1]  [2]  [3]  [4]            │
│  Value:  [10] [20] [30] [40] [50]           │
│           ↑                        ↑         │
│         First                    Last        │
│        Element                 Element       │
│                                              │
│  • Fixed size: 5 elements                    │
│  • Same type: all integers                   │
│  • Contiguous memory locations               │
│  • Zero-based indexing                       │
│                                              │
└──────────────────────────────────────────────┘
```

### Why Use Arrays?

**Benefits:**
1. **Store Multiple Values:** Hold many values in single variable
2. **Efficient Access:** Direct access using index (O(1) time)
3. **Organized Data:** Keep related data together
4. **Easy Iteration:** Loop through elements easily
5. **Memory Efficient:** Contiguous memory allocation

### Array Characteristics

| Feature | Description |
|---------|-------------|
| **Fixed Size** | Cannot grow or shrink after creation |
| **Homogeneous** | All elements must be same type |
| **Index-Based** | Access elements using integer index (0 to length-1) |
| **Reference Type** | Arrays are objects in Java |
| **Zero-Based** | First element at index 0 |

### Real-World Analogies

```
1. PARKING LOT
   - Fixed number of parking spaces
   - Each space has a number (index)
   - Can park one car per space
   - All spaces for same purpose

2. APARTMENT BUILDING
   - Fixed number of apartments
   - Each apartment has a number
   - Each apartment similar structure
   - All in contiguous building

3. EGG CARTON
   - Fixed 12 slots
   - Each slot holds one egg
   - Numbered positions
   - All eggs similar
```

---

## Declaring, Constructing, and Initializing Arrays

### Video 106: Declaring, Constructing, and Initializing an Array

**Video Reference:** `106.Java- Declaring , constructing, intializing an array.mp4`

### Array Declaration

**Syntax:**
```java
dataType[] arrayName;           // Preferred
dataType arrayName[];           // Also valid (C-style)
```

**Examples:**
```java
int[] numbers;              // Array of integers
String[] names;             // Array of strings
double[] prices;            // Array of doubles
boolean[] flags;            // Array of booleans

// Multiple arrays in one statement
int[] arr1, arr2, arr3;

// C-style declaration (not recommended)
int numbers[];              // Valid but less readable
```

### Array Construction (Memory Allocation)

**Syntax:**
```java
arrayName = new dataType[size];
```

**Examples:**
```java
numbers = new int[5];           // Creates array of 5 integers
names = new String[10];         // Creates array of 10 strings
prices = new double[100];       // Creates array of 100 doubles
```

**Combined Declaration and Construction:**
```java
int[] numbers = new int[5];
String[] names = new String[10];
double[] prices = new double[100];
```

### Default Values

When arrays are created, elements are initialized with default values:

| Type | Default Value |
|------|---------------|
| **byte, short, int, long** | 0 |
| **float, double** | 0.0 |
| **char** | '\u0000' (null character) |
| **boolean** | false |
| **Reference types** | null |

```java
int[] numbers = new int[3];
// numbers[0] = 0
// numbers[1] = 0
// numbers[2] = 0

String[] names = new String[3];
// names[0] = null
// names[1] = null
// names[2] = null
```

### Array Initialization

**Method 1: Individual Assignment**
```java
int[] numbers = new int[3];
numbers[0] = 10;
numbers[1] = 20;
numbers[2] = 30;
```

**Method 2: Initialization at Declaration**
```java
int[] numbers = {10, 20, 30, 40, 50};
// Size automatically determined: 5

String[] days = {"Monday", "Tuesday", "Wednesday"};
// Size: 3

double[] prices = {19.99, 29.99, 39.99};
// Size: 3
```

**Method 3: Using new with Initialization**
```java
int[] numbers = new int[] {10, 20, 30, 40, 50};
String[] days = new String[] {"Monday", "Tuesday", "Wednesday"};
```

### Complete Example

```java
public class ArrayBasics {
    public static void main(String[] args) {
        // Declaration only
        int[] arr1;
        
        // Declaration and construction
        int[] arr2 = new int[5];
        
        // Declaration, construction, and initialization
        int[] arr3 = {10, 20, 30, 40, 50};
        
        // Display default values
        System.out.println("=== Array with default values ===");
        for (int i = 0; i < arr2.length; i++) {
            System.out.println("arr2[" + i + "] = " + arr2[i]);
        }
        
        // Assign values
        System.out.println("\n=== Assigning values ===");
        arr2[0] = 100;
        arr2[1] = 200;
        arr2[2] = 300;
        arr2[3] = 400;
        arr2[4] = 500;
        
        for (int i = 0; i < arr2.length; i++) {
            System.out.println("arr2[" + i + "] = " + arr2[i]);
        }
        
        // Display initialized array
        System.out.println("\n=== Initialized array ===");
        for (int i = 0; i < arr3.length; i++) {
            System.out.println("arr3[" + i + "] = " + arr3[i]);
        }
        
        // Using enhanced for loop
        System.out.println("\n=== Using enhanced for loop ===");
        for (int value : arr3) {
            System.out.println("Value: " + value);
        }
    }
}
```

**Output:**
```
=== Array with default values ===
arr2[0] = 0
arr2[1] = 0
arr2[2] = 0
arr2[3] = 0
arr2[4] = 0

=== Assigning values ===
arr2[0] = 100
arr2[1] = 200
arr2[2] = 300
arr2[3] = 400
arr2[4] = 500

=== Initialized array ===
arr3[0] = 10
arr3[1] = 20
arr3[2] = 30
arr3[3] = 40
arr3[4] = 50

=== Using enhanced for loop ===
Value: 10
Value: 20
Value: 30
Value: 40
Value: 50
```

### Array Length

Every array has a `length` property (not a method!):

```java
int[] numbers = {10, 20, 30, 40, 50};
System.out.println("Length: " + numbers.length);  // 5

// Note: It's 'length', not 'length()'
// System.out.println(numbers.length());  // ERROR!
```

---

## Arrays: Primitive or Object?

### Video 107: Is Array Primitive or Object?

**Video Reference:** `107.Java- Is array primitire or object.mp4`

### Arrays are Objects

**Key Point:** In Java, arrays are objects, even if they hold primitive values.

```java
int[] numbers = new int[5];  // 'new' keyword indicates object creation

// Arrays have properties and methods inherited from Object class
System.out.println(numbers.getClass());        // class [I
System.out.println(numbers.toString());        // [I@hashcode
System.out.println(numbers.hashCode());        // hash code value
```

### Evidence that Arrays are Objects

```java
public class ArraysAreObjects {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};
        
        // 1. Arrays created with 'new' keyword
        int[] arr2 = new int[5];
        
        // 2. Arrays have Object class methods
        System.out.println("Class: " + arr.getClass().getName());
        System.out.println("Hash code: " + arr.hashCode());
        System.out.println("toString: " + arr.toString());
        
        // 3. Arrays can be assigned to Object reference
        Object obj = arr;
        System.out.println("Assigned to Object: " + (obj instanceof Object));
        System.out.println("Is array: " + (obj instanceof int[]));
        
        // 4. Arrays are stored in heap memory (like objects)
        // 5. Arrays are passed by reference (like objects)
        
        // 6. Array class hierarchy
        System.out.println("\n=== Array Class Info ===");
        Class<?> arrayClass = arr.getClass();
        System.out.println("Class name: " + arrayClass.getName());
        System.out.println("Is array: " + arrayClass.isArray());
        System.out.println("Component type: " + arrayClass.getComponentType());
        System.out.println("Super class: " + arrayClass.getSuperclass());
    }
}
```

**Output:**
```
Class: [I
Hash code: 123456789
toString: [I@75b84c92

Assigned to Object: true
Is array: true

=== Array Class Info ===
Class name: [I
Is array: true
Component type: int
Super class: class java.lang.Object
```

### Array Type Notation

| Array Type | Class Name | Meaning |
|------------|------------|---------|
| `int[]` | `[I` | Array of int |
| `double[]` | `[D` | Array of double |
| `boolean[]` | `[Z` | Array of boolean |
| `String[]` | `[Ljava.lang.String;` | Array of String |
| `Object[]` | `[Ljava.lang.Object;` | Array of Object |

### Primitive vs Reference Type Arrays

```java
// Primitive type array
int[] primitiveArray = {1, 2, 3, 4, 5};
// Stores actual values
// [1][2][3][4][5]

// Reference type array
String[] refArray = {"Hello", "World", "Java"};
// Stores references to String objects
// [ref1][ref2][ref3] → ["Hello"]["World"]["Java"]

System.out.println("Primitive array element: " + primitiveArray[0]);
System.out.println("Reference array element: " + refArray[0]);
```

### Memory Representation

```
Primitive Array (int[]):
Stack                    Heap
┌──────────┐            ┌─────────────────────┐
│ numbers  │────────────>│  [0][1][2][3][4]   │
│ (ref)    │            │  [10][20][30][40][50]│
└──────────┘            └─────────────────────┘

Reference Array (String[]):
Stack                    Heap
┌──────────┐            ┌─────────────────┐
│  names   │────────────>│ [ref][ref][ref]│
│  (ref)   │            └──│────│────│────┘
└──────────┘               │    │    │
                           ↓    ↓    ↓
                        ┌────┬────┬────┐
                        │"A" │"B" │"C" │
                        └────┴────┴────┘
```

---

## Passing Arrays to Methods

### Video 108: Passing Array to Methods

**Video Reference:** `108.Java- Passing array to methods.mp4`

### Arrays Passed by Reference

**Key Concept:** When you pass an array to a method, you're passing a reference to the array object, not a copy.

```
┌──────────────────────────────────────────┐
│    Pass by Reference (Arrays)            │
├──────────────────────────────────────────┤
│                                          │
│  main()               method()           │
│  ┌─────┐             ┌─────┐            │
│  │ arr │──────┬──────>│ arr │            │
│  └─────┘      │      └─────┘            │
│               │                          │
│               ↓                          │
│         ┌──────────┐                     │
│         │ [1][2][3]│ (Same array)        │
│         └──────────┘                     │
│                                          │
│  Changes in method affect original!      │
└──────────────────────────────────────────┘
```

### Passing Arrays to Methods

```java
public class PassingArrays {
    
    // Method that receives an array
    public static void displayArray(int[] arr) {
        System.out.println("Array elements:");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
    
    // Method that modifies array elements
    public static void doubleValues(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] *= 2;  // Modifies original array
        }
    }
    
    // Method that finds sum
    public static int findSum(int[] arr) {
        int sum = 0;
        for (int value : arr) {
            sum += value;
        }
        return sum;
    }
    
    // Method that finds maximum
    public static int findMax(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }
        
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }
    
    // Method that returns a new array
    public static int[] createArray(int size, int initialValue) {
        int[] newArray = new int[size];
        for (int i = 0; i < size; i++) {
            newArray[i] = initialValue;
        }
        return newArray;
    }
    
    public static void main(String[] args) {
        int[] numbers = {10, 20, 30, 40, 50};
        
        // Passing array to display
        System.out.println("=== Original Array ===");
        displayArray(numbers);
        
        // Finding sum
        int sum = findSum(numbers);
        System.out.println("Sum: " + sum);
        
        // Finding maximum
        int max = findMax(numbers);
        System.out.println("Maximum: " + max);
        
        // Modifying array
        System.out.println("\n=== After Doubling ===");
        doubleValues(numbers);  // Modifies original
        displayArray(numbers);
        
        // Creating and returning array
        System.out.println("\n=== Created Array ===");
        int[] newArray = createArray(5, 100);
        displayArray(newArray);
    }
}
```

**Output:**
```
=== Original Array ===
Array elements:
10 20 30 40 50 
Sum: 150
Maximum: 50

=== After Doubling ===
Array elements:
20 40 60 80 100 
Maximum: 100

=== Created Array ===
Array elements:
100 100 100 100 100 
```

### Varargs (Variable Arguments)

Java allows passing variable number of arguments using varargs:

```java
public class VarargsExample {
    
    // Varargs method - can accept any number of integers
    public static int sum(int... numbers) {
        // numbers is treated as an array
        int total = 0;
        for (int num : numbers) {
            total += num;
        }
        return total;
    }
    
    // Varargs with other parameters (must be last)
    public static void printWithPrefix(String prefix, String... messages) {
        for (String msg : messages) {
            System.out.println(prefix + msg);
        }
    }
    
    public static void main(String[] args) {
        // Can call with different number of arguments
        System.out.println("Sum of 2 numbers: " + sum(10, 20));
        System.out.println("Sum of 3 numbers: " + sum(10, 20, 30));
        System.out.println("Sum of 5 numbers: " + sum(10, 20, 30, 40, 50));
        System.out.println("Sum of no numbers: " + sum());
        
        // Can also pass an array
        int[] arr = {1, 2, 3, 4, 5};
        System.out.println("Sum of array: " + sum(arr));
        
        System.out.println("\n=== Varargs with prefix ===");
        printWithPrefix("LOG: ", "Started", "Processing", "Completed");
    }
}
```

---

## Copying Arrays

### Video 109: Copying Array

**Video Reference:** `109.Java- Copying array.mp4`

### Why Copy Arrays?

1. **Preserve Original:** Keep original data unchanged
2. **Independent Modifications:** Change copy without affecting original
3. **Backup:** Create backup before operations
4. **Data Transformation:** Transform data in copy

### Shallow vs Deep Copy

```
Shallow Copy (Reference Copy):
arr1 → [1][2][3]
       ↑
arr2 ──┘
(Both point to same array)

Deep Copy (Content Copy):
arr1 → [1][2][3]
arr2 → [1][2][3]
(Two separate arrays with same values)
```

### Method 1: Using Loop (Manual Copy)

```java
public class ArrayCopyMethods {
    public static void main(String[] args) {
        int[] original = {10, 20, 30, 40, 50};
        
        // Method 1: Manual copy using loop
        int[] copy1 = new int[original.length];
        for (int i = 0; i < original.length; i++) {
            copy1[i] = original[i];
        }
        
        // Verify copy
        System.out.println("=== Manual Copy ===");
        System.out.println("Original: " + java.util.Arrays.toString(original));
        System.out.println("Copy: " + java.util.Arrays.toString(copy1));
        
        // Modify copy
        copy1[0] = 999;
        System.out.println("After modifying copy:");
        System.out.println("Original: " + java.util.Arrays.toString(original));
        System.out.println("Copy: " + java.util.Arrays.toString(copy1));
    }
}
```

### Method 2: Using clone()

```java
public class CloneMethod {
    public static void main(String[] args) {
        int[] original = {10, 20, 30, 40, 50};
        
        // Method 2: Using clone() - creates shallow copy
        int[] copy = original.clone();
        
        System.out.println("=== Using clone() ===");
        System.out.println("Original: " + java.util.Arrays.toString(original));
        System.out.println("Copy: " + java.util.Arrays.toString(copy));
        
        // Modify copy
        copy[0] = 999;
        System.out.println("\nAfter modifying copy:");
        System.out.println("Original: " + java.util.Arrays.toString(original));
        System.out.println("Copy: " + java.util.Arrays.toString(copy));
        
        // For primitive arrays, clone() creates independent copy
        // For object arrays, clone() creates shallow copy
    }
}
```

### Method 3: Using System.arraycopy()

```java
public class SystemArraycopy {
    public static void main(String[] args) {
        int[] original = {10, 20, 30, 40, 50, 60, 70};
        
        // Method 3: System.arraycopy(src, srcPos, dest, destPos, length)
        int[] copy = new int[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        
        System.out.println("=== Using System.arraycopy() ===");
        System.out.println("Original: " + java.util.Arrays.toString(original));
        System.out.println("Copy: " + java.util.Arrays.toString(copy));
        
        // Partial copy
        int[] partialCopy = new int[3];
        System.arraycopy(original, 2, partialCopy, 0, 3);  // Copy elements 2,3,4
        System.out.println("Partial copy (3 elements from index 2): " + 
                          java.util.Arrays.toString(partialCopy));
        
        // Copy to middle of array
        int[] target = new int[10];
        System.arraycopy(original, 0, target, 2, 5);  // Copy 5 elements starting at position 2
        System.out.println("Copy to middle: " + java.util.Arrays.toString(target));
    }
}
```

### Method 4: Using Arrays.copyOf()

```java
import java.util.Arrays;

public class ArraysCopyOf {
    public static void main(String[] args) {
        int[] original = {10, 20, 30, 40, 50};
        
        // Method 4: Arrays.copyOf(original, newLength)
        
        // Same length
        int[] copy1 = Arrays.copyOf(original, original.length);
        System.out.println("=== Arrays.copyOf() ===");
        System.out.println("Same length: " + Arrays.toString(copy1));
        
        // Longer (pads with zeros)
        int[] copy2 = Arrays.copyOf(original, 8);
        System.out.println("Longer array: " + Arrays.toString(copy2));
        
        // Shorter (truncates)
        int[] copy3 = Arrays.copyOf(original, 3);
        System.out.println("Shorter array: " + Arrays.toString(copy3));
    }
}
```

### Method 5: Using Arrays.copyOfRange()

```java
import java.util.Arrays;

public class ArraysCopyOfRange {
    public static void main(String[] args) {
        int[] original = {10, 20, 30, 40, 50, 60, 70, 80};
        
        // Method 5: Arrays.copyOfRange(original, from, to)
        // 'to' is exclusive
        
        int[] range1 = Arrays.copyOfRange(original, 2, 5);  // indices 2,3,4
        System.out.println("=== Arrays.copyOfRange() ===");
        System.out.println("Range [2,5): " + Arrays.toString(range1));
        
        int[] range2 = Arrays.copyOfRange(original, 0, 3);  // indices 0,1,2
        System.out.println("Range [0,3): " + Arrays.toString(range2));
        
        int[] range3 = Arrays.copyOfRange(original, 5, original.length);  // from 5 to end
        System.out.println("Range [5,end): " + Arrays.toString(range3));
    }
}
```

### Comparison of Copy Methods

| Method | Use Case | Performance | Flexibility |
|--------|----------|-------------|-------------|
| **Manual Loop** | Simple, educational | Slowest | Limited |
| **clone()** | Quick full copy | Fast | Low |
| **System.arraycopy()** | Partial copies, bulk operations | Fastest | High |
| **Arrays.copyOf()** | Resize while copying | Fast | Medium |
| **Arrays.copyOfRange()** | Extract subarray | Fast | High |

### Complete Copying Example

```java
import java.util.Arrays;

public class ArrayCopyingComplete {
    public static void main(String[] args) {
        int[] original = {10, 20, 30, 40, 50};
        
        System.out.println("Original: " + Arrays.toString(original));
        System.out.println();
        
        // 1. Manual copy
        int[] copy1 = new int[original.length];
        for (int i = 0; i < original.length; i++) {
            copy1[i] = original[i];
        }
        System.out.println("1. Manual copy: " + Arrays.toString(copy1));
        
        // 2. Using clone()
        int[] copy2 = original.clone();
        System.out.println("2. clone(): " + Arrays.toString(copy2));
        
        // 3. System.arraycopy()
        int[] copy3 = new int[original.length];
        System.arraycopy(original, 0, copy3, 0, original.length);
        System.out.println("3. System.arraycopy(): " + Arrays.toString(copy3));
        
        // 4. Arrays.copyOf()
        int[] copy4 = Arrays.copyOf(original, original.length);
        System.out.println("4. Arrays.copyOf(): " + Arrays.toString(copy4));
        
        // 5. Arrays.copyOfRange()
        int[] copy5 = Arrays.copyOfRange(original, 0, original.length);
        System.out.println("5. Arrays.copyOfRange(): " + Arrays.toString(copy5));
        
        // Verify independence
        System.out.println("\n=== Testing Independence ===");
        copy1[0] = 999;
        System.out.println("After modifying copy1[0]:");
        System.out.println("Original: " + Arrays.toString(original));
        System.out.println("Copy1: " + Arrays.toString(copy1));
    }
}
```

---

## Practical Examples

### Video 110: Program for Average Rainfall Using Arrays

**Video Reference:** `110.Java- Program for average rain full using arraya.mp4`

### Average Rainfall Calculator

```java
import java.util.Scanner;

public class AverageRainfall {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Get number of months
        System.out.print("Enter number of months: ");
        int months = scanner.nextInt();
        
        // Create array to store rainfall data
        double[] rainfall = new double[months];
        
        // Input rainfall for each month
        System.out.println("\nEnter rainfall (in mm) for each month:");
        for (int i = 0; i < months; i++) {
            System.out.print("Month " + (i + 1) + ": ");
            rainfall[i] = scanner.nextDouble();
        }
        
        // Calculate total and average
        double total = 0;
        for (double rain : rainfall) {
            total += rain;
        }
        double average = total / months;
        
        // Find maximum and minimum
        double max = rainfall[0];
        double min = rainfall[0];
        int maxMonth = 0;
        int minMonth = 0;
        
        for (int i = 1; i < rainfall.length; i++) {
            if (rainfall[i] > max) {
                max = rainfall[i];
                maxMonth = i;
            }
            if (rainfall[i] < min) {
                min = rainfall[i];
                minMonth = i;
            }
        }
        
        // Display results
        System.out.println("\n=== Rainfall Statistics ===");
        System.out.printf("Total Rainfall: %.2f mm%n", total);
        System.out.printf("Average Rainfall: %.2f mm%n", average);
        System.out.printf("Maximum Rainfall: %.2f mm (Month %d)%n", max, (maxMonth + 1));
        System.out.printf("Minimum Rainfall: %.2f mm (Month %d)%n", min, (minMonth + 1));
        
        // Count months above and below average
        int aboveAverage = 0;
        int belowAverage = 0;
        
        for (double rain : rainfall) {
            if (rain > average) aboveAverage++;
            else if (rain < average) belowAverage++;
        }
        
        System.out.println("\nMonths above average: " + aboveAverage);
        System.out.println("Months below average: " + belowAverage);
        
        // Display monthly data
        System.out.println("\n=== Month-wise Data ===");
        for (int i = 0; i < rainfall.length; i++) {
            String status;
            if (rainfall[i] > average) status = "Above Average";
            else if (rainfall[i] < average) status = "Below Average";
            else status = "Average";
            
            System.out.printf("Month %d: %.2f mm - %s%n", (i + 1), rainfall[i], status);
        }
        
        scanner.close();
    }
}
```

**Sample Output:**
```
Enter number of months: 6

Enter rainfall (in mm) for each month:
Month 1: 150.5
Month 2: 200.0
Month 3: 180.75
Month 4: 95.25
Month 5: 220.50
Month 6: 175.0

=== Rainfall Statistics ===
Total Rainfall: 1022.00 mm
Average Rainfall: 170.33 mm
Maximum Rainfall: 220.50 mm (Month 5)
Minimum Rainfall: 95.25 mm (Month 4)

Months above average: 3
Months below average: 2

=== Month-wise Data ===
Month 1: 150.50 mm - Below Average
Month 2: 200.00 mm - Above Average
Month 3: 180.75 mm - Above Average
Month 4: 95.25 mm - Below Average
Month 5: 220.50 mm - Above Average
Month 6: 175.00 mm - Above Average
```

---

## Command Line Arguments

### Video 111: Command Line Arguments

**Video Reference:** `111.Java- Command line argument.mp4`

### What are Command Line Arguments?

**Definition:** Command line arguments are parameters passed to a program when it's executed from the command line or terminal.

**Key Points:**
- Passed as String array to `main()` method
- Array name is typically `args`
- Accessed using index: `args[0]`, `args[1]`, etc.
- `args.length` gives number of arguments

### Syntax

```java
public static void main(String[] args) {
    // args[0] - first argument
    // args[1] - second argument
    // args.length - number of arguments
}
```

### Basic Example

```java
public class CommandLineDemo {
    public static void main(String[] args) {
        System.out.println("Number of arguments: " + args.length);
        
        if (args.length == 0) {
            System.out.println("No arguments provided");
            return;
        }
        
        System.out.println("\n=== Command Line Arguments ===");
        for (int i = 0; i < args.length; i++) {
            System.out.println("Argument " + i + ": " + args[i]);
        }
    }
}
```

**Compilation and Execution:**
```bash
# Compile
javac CommandLineDemo.java

# Run with arguments
java CommandLineDemo Hello World Java Programming

# Output:
# Number of arguments: 4
# === Command Line Arguments ===
# Argument 0: Hello
# Argument 1: World
# Argument 2: Java
# Argument 3: Programming
```

### Practical Example: Calculator

```java
public class CommandLineCalculator {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java CommandLineCalculator <num1> <operator> <num2>");
            System.out.println("Example: java CommandLineCalculator 10 + 20");
            return;
        }
        
        try {
            // Parse numbers
            double num1 = Double.parseDouble(args[0]);
            String operator = args[1];
            double num2 = Double.parseDouble(args[2]);
            
            double result = 0;
            boolean validOperation = true;
            
            // Perform operation
            switch (operator) {
                case "+":
                    result = num1 + num2;
                    break;
                case "-":
                    result = num1 - num2;
                    break;
                case "*":
                case "x":
                    result = num1 * num2;
                    break;
                case "/":
                    if (num2 == 0) {
                        System.out.println("Error: Division by zero");
                        return;
                    }
                    result = num1 / num2;
                    break;
                case "%":
                    result = num1 % num2;
                    break;
                default:
                    System.out.println("Invalid operator: " + operator);
                    System.out.println("Supported: +, -, *, /, %");
                    validOperation = false;
            }
            
            if (validOperation) {
                System.out.printf("%.2f %s %.2f = %.2f%n", num1, operator, num2, result);
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format");
            System.out.println("Please enter valid numbers");
        }
    }
}
```

**Usage Examples:**
```bash
java CommandLineCalculator 10 + 20
# Output: 10.00 + 20.00 = 30.00

java CommandLineCalculator 15 * 3
# Output: 15.00 * 3.00 = 45.00

java CommandLineCalculator 100 / 4
# Output: 100.00 / 4.00 = 25.00
```

### Example: File Operations

```java
public class FileProcessor {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java FileProcessor <filename1> <filename2> ...");
            return;
        }
        
        System.out.println("Processing " + args.length + " files:");
        
        for (String filename : args) {
            System.out.println("\nFile: " + filename);
            // Here you would add actual file processing logic
            System.out.println("  - Checking existence...");
            System.out.println("  - Reading content...");
            System.out.println("  - Processing complete");
        }
    }
}
```

---

## Parallel Arrays

### Video 112: Parallel Arrays

**Video Reference:** `112.Java- Parallel array.mp4`

### What are Parallel Arrays?

**Definition:** Parallel arrays are two or more arrays where corresponding elements at the same index are related.

```
┌────────────────────────────────────────┐
│        Parallel Arrays                 │
├────────────────────────────────────────┤
│                                        │
│  names:   [Alice][Bob][Charlie][Diana] │
│  ages:    [  25 ][ 30][  28  ][ 35  ] │
│  cities:  [NYC][LA][Chicago][Boston]   │
│                                        │
│  Index 0: Alice, 25, NYC               │
│  Index 1: Bob, 30, LA                  │
│  Index 2: Charlie, 28, Chicago         │
│  Index 3: Diana, 35, Boston            │
│                                        │
└────────────────────────────────────────┘
```

### Parallel Arrays Example

```java
public class ParallelArraysDemo {
    public static void main(String[] args) {
        // Parallel arrays to store student information
        String[] names = {"Alice", "Bob", "Charlie", "Diana", "Eve"};
        int[] ages = {20, 22, 21, 23, 20};
        double[] grades = {85.5, 92.0, 78.5, 88.0, 95.5};
        String[] majors = {"CS", "Math", "Physics", "CS", "Engineering"};
        
        // Display all student records
        System.out.println("=== Student Records ===");
        System.out.println("-------------------------------------------------------");
        System.out.printf("%-10s %-5s %-8s %-12s%n", "Name", "Age", "Grade", "Major");
        System.out.println("-------------------------------------------------------");
        
        for (int i = 0; i < names.length; i++) {
            System.out.printf("%-10s %-5d %-8.2f %-12s%n", 
                            names[i], ages[i], grades[i], majors[i]);
        }
        
        // Find student with highest grade
        double maxGrade = grades[0];
        int maxIndex = 0;
        
        for (int i = 1; i < grades.length; i++) {
            if (grades[i] > maxGrade) {
                maxGrade = grades[i];
                maxIndex = i;
            }
        }
        
        System.out.println("\n=== Top Student ===");
        System.out.println("Name: " + names[maxIndex]);
        System.out.println("Age: " + ages[maxIndex]);
        System.out.println("Grade: " + grades[maxIndex]);
        System.out.println("Major: " + majors[maxIndex]);
        
        // Calculate average grade
        double sum = 0;
        for (double grade : grades) {
            sum += grade;
        }
        double average = sum / grades.length;
        
        System.out.println("\n=== Statistics ===");
        System.out.printf("Average Grade: %.2f%n", average);
        
        // Find students in CS major
        System.out.println("\n=== CS Students ===");
        for (int i = 0; i < majors.length; i++) {
            if (majors[i].equals("CS")) {
                System.out.printf("%s (Age: %d, Grade: %.2f)%n", 
                                names[i], ages[i], grades[i]);
            }
        }
    }
}
```

**Output:**
```
=== Student Records ===
-------------------------------------------------------
Name       Age   Grade    Major       
-------------------------------------------------------
Alice      20    85.50    CS          
Bob        22    92.00    Math        
Charlie    21    78.50    Physics     
Diana      23    88.00    CS          
Eve        20    95.50    Engineering 

=== Top Student ===
Name: Eve
Age: 20
Grade: 95.5
Major: Engineering

=== Statistics ===
Average Grade: 87.90

=== CS Students ===
Alice (Age: 20, Grade: 85.50)
Diana (Age: 23, Grade: 88.00)
```

### Limitations of Parallel Arrays

1. **Error-Prone:** Easy to get arrays out of sync
2. **Maintenance:** Difficult to add/remove elements
3. **No Encapsulation:** Data not bundled together
4. **Type Safety:** No compile-time checks for related data

### Better Alternative: Using Objects

```java
class Student {
    String name;
    int age;
    double grade;
    String major;
    
    public Student(String name, int age, double grade, String major) {
        this.name = name;
        this.age = age;
        this.grade = grade;
        this.major = major;
    }
    
    @Override
    public String toString() {
        return String.format("%-10s Age:%-3d Grade:%-6.2f Major:%-12s", 
                           name, age, grade, major);
    }
}

public class ObjectArrayDemo {
    public static void main(String[] args) {
        // Much better: Array of objects
        Student[] students = {
            new Student("Alice", 20, 85.5, "CS"),
            new Student("Bob", 22, 92.0, "Math"),
            new Student("Charlie", 21, 78.5, "Physics"),
            new Student("Diana", 23, 88.0, "CS"),
            new Student("Eve", 20, 95.5, "Engineering")
        };
        
        System.out.println("=== Student Records (Object Array) ===");
        for (Student student : students) {
            System.out.println(student);
        }
    }
}
```

---

## Two-Dimensional Arrays

### Video 113: Two Dimensional Arrays

**Video Reference:** `113.Java- Two dimensional.mp4`

### What is a 2D Array?

**Definition:** A 2D array is an array of arrays - essentially a table or matrix with rows and columns.

```
┌──────────────────────────────────────────┐
│     2D Array (Matrix)                    │
├──────────────────────────────────────────┤
│                                          │
│      Col0  Col1  Col2  Col3             │
│  Row0 [ 1 ][ 2 ][ 3 ][ 4 ]              │
│  Row1 [ 5 ][ 6 ][ 7 ][ 8 ]              │
│  Row2 [ 9 ][10 ][11 ][12 ]              │
│                                          │
│  Access: array[row][column]              │
│  Example: array[1][2] = 7                │
│                                          │
└──────────────────────────────────────────┘
```

### Declaration and Initialization

```java
public class TwoDimensionalArrays {
    public static void main(String[] args) {
        // Method 1: Declaration and construction
        int[][] matrix1 = new int[3][4];  // 3 rows, 4 columns
        
        // Method 2: Declaration with initialization
        int[][] matrix2 = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12}
        };
        
        // Method 3: Row by row initialization
        int[][] matrix3 = new int[3][];
        matrix3[0] = new int[]{1, 2, 3};
        matrix3[1] = new int[]{4, 5, 6, 7};  // Jagged array
        matrix3[2] = new int[]{8, 9};
        
        // Access elements
        System.out.println("Element at [1][2]: " + matrix2[1][2]);  // 7
        
        // Get dimensions
        int rows = matrix2.length;
        int cols = matrix2[0].length;
        System.out.println("Rows: " + rows + ", Columns: " + cols);
    }
}
```

### Traversing 2D Arrays

```java
public class Traverse2DArray {
    public static void main(String[] args) {
        int[][] matrix = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12}
        };
        
        // Method 1: Using nested for loops (with index)
        System.out.println("=== Method 1: Nested for loops ===");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
        
        // Method 2: Using enhanced for loop
        System.out.println("\n=== Method 2: Enhanced for loop ===");
        for (int[] row : matrix) {
            for (int element : row) {
                System.out.print(element + "\t");
            }
            System.out.println();
        }
    }
}
```

### 2D Array Operations

```java
import java.util.Arrays;

public class Matrix Operations {
    
    // Display matrix
    public static void displayMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int element : row) {
                System.out.printf("%4d", element);
            }
            System.out.println();
        }
    }
    
    // Sum of all elements
    public static int sumAll(int[][] matrix) {
        int sum = 0;
        for (int[] row : matrix) {
            for (int element : row) {
                sum += element;
            }
        }
        return sum;
    }
    
    // Find maximum element
    public static int findMax(int[][] matrix) {
        int max = matrix[0][0];
        for (int[] row : matrix) {
            for (int element : row) {
                if (element > max) {
                    max = element;
                }
            }
        }
        return max;
    }
    
    // Transpose matrix
    public static int[][] transpose(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] transposed = new int[cols][rows];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }
        return transposed;
    }
    
    // Sum of row
    public static int rowSum(int[][] matrix, int rowIndex) {
        int sum = 0;
        for (int element : matrix[rowIndex]) {
            sum += element;
        }
        return sum;
    }
    
    // Sum of column
    public static int columnSum(int[][] matrix, int colIndex) {
        int sum = 0;
        for (int[] row : matrix) {
            sum += row[colIndex];
        }
        return sum;
    }
    
    public static void main(String[] args) {
        int[][] matrix = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12}
        };
        
        System.out.println("=== Original Matrix ===");
        displayMatrix(matrix);
        
        System.out.println("\n=== Statistics ===");
        System.out.println("Sum of all elements: " + sumAll(matrix));
        System.out.println("Maximum element: " + findMax(matrix));
        
        System.out.println("\n=== Row Sums ===");
        for (int i = 0; i < matrix.length; i++) {
            System.out.println("Row " + i + " sum: " + rowSum(matrix, i));
        }
        
        System.out.println("\n=== Column Sums ===");
        for (int j = 0; j < matrix[0].length; j++) {
            System.out.println("Column " + j + " sum: " + columnSum(matrix, j));
        }
        
        System.out.println("\n=== Transposed Matrix ===");
        int[][] transposed = transpose(matrix);
        displayMatrix(transposed);
    }
}
```

---

## Array Operations

### Video 114: Deletion in an Array

**Video Reference:** `114.Java- Deletion in an array.mp4`

### Deleting Elements from Array

**Challenge:** Arrays have fixed size, so we can't truly delete. We must:
1. Create new smaller array
2. Copy elements excluding the one to delete

```java
import java.util.Arrays;

public class ArrayDeletion {
    
    // Delete element at specific index
    public static int[] deleteAtIndex(int[] arr, int index) {
        if (index < 0 || index >= arr.length) {
            System.out.println("Invalid index");
            return arr;
        }
        
        // Create new array with size-1
        int[] newArr = new int[arr.length - 1];
        
        // Copy elements before index
        for (int i = 0; i < index; i++) {
            newArr[i] = arr[i];
        }
        
        // Copy elements after index
        for (int i = index + 1; i < arr.length; i++) {
            newArr[i - 1] = arr[i];
        }
        
        return newArr;
    }
    
    // Delete first occurrence of element
    public static int[] deleteElement(int[] arr, int element) {
        // Find element
        int index = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == element) {
                index = i;
                break;
            }
        }
        
        if (index == -1) {
            System.out.println("Element not found");
            return arr;
        }
        
        return deleteAtIndex(arr, index);
    }
    
    // Delete all occurrences of element
    public static int[] deleteAllOccurrences(int[] arr, int element) {
        // Count occurrences
        int count = 0;
        for (int value : arr) {
            if (value == element) count++;
        }
        
        if (count == 0) {
            System.out.println("Element not found");
            return arr;
        }
        
        // Create new array
        int[] newArr = new int[arr.length - count];
        int j = 0;
        
        for (int value : arr) {
            if (value != element) {
                newArr[j++] = value;
            }
        }
        
        return newArr;
    }
    
    public static void main(String[] args) {
        int[] numbers = {10, 20, 30, 40, 50, 30, 60};
        
        System.out.println("Original: " + Arrays.toString(numbers));
        
        // Delete at index 2
        int[] result1 = deleteAtIndex(numbers, 2);
        System.out.println("After deleting index 2: " + Arrays.toString(result1));
        
        // Delete first occurrence of 30
        int[] result2 = deleteElement(numbers, 30);
        System.out.println("After deleting first 30: " + Arrays.toString(result2));
        
        // Delete all occurrences of 30
        int[] result3 = deleteAllOccurrences(numbers, 30);
        System.out.println("After deleting all 30s: " + Arrays.toString(result3));
    }
}
```

**Output:**
```
Original: [10, 20, 30, 40, 50, 30, 60]
After deleting index 2: [10, 20, 40, 50, 30, 60]
After deleting first 30: [10, 20, 40, 50, 30, 60]
After deleting all 30s: [10, 20, 40, 50, 60]
```

### Video 115: Find Duplicate Elements in an Array

**Video Reference:** `115.Java- Find duplicate elements in an array.mp4`

### Finding Duplicates

```java
import java.util.*;

public class FindDuplicates {
    
    // Method 1: Using nested loops (O(n²))
    public static void findDuplicatesBasic(int[] arr) {
        System.out.println("=== Duplicates (Basic Method) ===");
        boolean[] visited = new boolean[arr.length];
        
        for (int i = 0; i < arr.length; i++) {
            if (visited[i]) continue;
            
            int count = 1;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] == arr[j]) {
                    visited[j] = true;
                    count++;
                }
            }
            
            if (count > 1) {
                System.out.println(arr[i] + " appears " + count + " times");
            }
        }
    }
    
    // Method 2: Using HashMap (O(n))
    public static void findDuplicatesHashMap(int[] arr) {
        System.out.println("\n=== Duplicates (HashMap Method) ===");
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        
        // Count frequencies
        for (int num : arr) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }
        
        // Display duplicates
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > 1) {
                System.out.println(entry.getKey() + " appears " + entry.getValue() + " times");
            }
        }
    }
    
    // Method 3: Using HashSet (for just finding if duplicates exist)
    public static List<Integer> findDuplicatesSet(int[] arr) {
        Set<Integer> seen = new HashSet<>();
        Set<Integer> duplicates = new HashSet<>();
        
        for (int num : arr) {
            if (!seen.add(num)) {  // add() returns false if already exists
                duplicates.add(num);
            }
        }
        
        return new ArrayList<>(duplicates);
    }
    
    // Method 4: For sorted array (O(n))
    public static void findDuplicatesSorted(int[] arr) {
        System.out.println("\n=== Duplicates in Sorted Array ===");
        Arrays.sort(arr);
        
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] == arr[i + 1]) {
                System.out.print(arr[i] + " ");
                // Skip all occurrences
                while (i < arr.length - 1 && arr[i] == arr[i + 1]) {
                    i++;
                }
            }
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        int[] numbers = {4, 2, 7, 2, 9, 4, 2, 7, 1, 5};
        
        System.out.println("Array: " + Arrays.toString(numbers));
        
        // Method 1
        findDuplicatesBasic(numbers);
        
        // Method 2
        findDuplicatesHashMap(numbers);
        
        // Method 3
        List<Integer> dups = findDuplicatesSet(numbers);
        System.out.println("\n=== Duplicates (Set Method) ===");
        System.out.println("Duplicate elements: " + dups);
        
        // Method 4
        int[] copy = numbers.clone();
        findDuplicatesSorted(copy);
    }
}
```

### Video 116: Number Occurring Odd Number of Times

**Video Reference:** `116.Java- Write a program to print the number which is occuring odd number of times.mp4`

### Finding Elements with Odd Frequency

```java
import java.util.*;

public class OddOccurrence {
    
    // Method 1: Using HashMap
    public static void findOddOccurrences(int[] arr) {
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        
        // Count frequencies
        for (int num : arr) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }
        
        // Find elements with odd frequency
        System.out.println("=== Elements with Odd Occurrences ===");
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() % 2 != 0) {
                System.out.println(entry.getKey() + " occurs " + entry.getValue() + " time(s)");
            }
        }
    }
    
    // Method 2: Using XOR (only if single element has odd occurrence)
    public static int findSingleOddOccurrence(int[] arr) {
        int result = 0;
        for (int num : arr) {
            result ^= num;  // XOR operation
        }
        return result;
        // Works because: a ^ a = 0, a ^ 0 = a
        // All pairs cancel out, leaving only the odd one
    }
    
    public static void main(String[] args) {
        int[] numbers1 = {1, 2, 3, 2, 3, 1, 3, 4, 4, 4};
        System.out.println("Array: " + Arrays.toString(numbers1));
        findOddOccurrences(numbers1);
        
        // Example for XOR method (only one element with odd occurrence)
        int[] numbers2 = {1, 2, 3, 2, 3, 1, 3};
        System.out.println("\n=== Single Odd Occurrence (XOR Method) ===");
        System.out.println("Array: " + Arrays.toString(numbers2));
        System.out.println("Element with odd occurrence: " + findSingleOddOccurrence(numbers2));
    }
}
```

**Output:**
```
Array: [1, 2, 3, 2, 3, 1, 3, 4, 4, 4]
=== Elements with Odd Occurrences ===
3 occurs 3 time(s)

=== Single Odd Occurrence (XOR Method) ===
Array: [1, 2, 3, 2, 3, 1, 3]
Element with odd occurrence: 3
```

---

## Common Pitfalls and Best Practices

### Common Errors

1. **Array IndexOutOfBoundsException**
```java
int[] arr = {1, 2, 3};
System.out.println(arr[3]);  // ERROR! Valid indices: 0, 1, 2
```

2. **NullPointerException**
```java
int[] arr = null;
System.out.println(arr.length);  // ERROR!
```

3. **Confusing length vs length()**
```java
int[] arr = {1, 2, 3};
System.out.println(arr.length);    // Correct
System.out.println(arr.length());  // ERROR! length is property, not method
```

### Best Practices

1. **Always check array bounds**
```java
if (index >= 0 && index < arr.length) {
    System.out.println(arr[index]);
}
```

2. **Use enhanced for loop when possible**
```java
// Good for read-only access
for (int value : array) {
    System.out.println(value);
}
```

3. **Use Arrays utility class**
```java
import java.util.Arrays;

Arrays.sort(arr);
Arrays.fill(arr, 0);
System.out.println(Arrays.toString(arr));
Arrays.equals(arr1, arr2);
```

4. **Validate input**
```java
public static void processArray(int[] arr) {
    if (arr == null || arr.length == 0) {
        throw new IllegalArgumentException("Array is null or empty");
    }
    // Process array
}
```

---

## Interview Preparation

### Frequently Asked Questions

**Q1: What is an array?**
- Fixed-size container holding elements of same type, with zero-based indexing

**Q2: Arrays in Java - primitive or object?**
- Arrays are objects, even when holding primitive types

**Q3: How to copy an array?**
- clone(), System.arraycopy(), Arrays.copyOf(), Arrays.copyOfRange()

**Q4: Difference between length and length()?**
- Arrays use `.length` (property), Strings use `.length()` (method)

**Q5: Can array size be changed?**
- No, arrays have fixed size. Use ArrayList for dynamic sizing

**Q6: What is ArrayIndexOutOfBoundsException?**
- Occurs when accessing invalid index (<0 or >=length)

**Q7: How are arrays passed to methods?**
- By reference - modifications affect original array

**Q8: What is a jagged array?**
- 2D array where rows can have different lengths

---

## Practice Exercises

### Exercise 1: Array Basics
Write a program to:
- Find second largest element
- Reverse an array
- Rotate array by k positions

### Exercise 2: Searching
Implement:
- Linear search algorithm
- Binary search algorithm
- Find first and last position of element

**Solution:**
```java
import java.util.Arrays;

public class SearchingExercises {
    
    // Linear Search
    public static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;  // Not found
    }
    
    // Binary Search (for sorted array)
    public static int binarySearch(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;  // Not found
    }
    
    // Find first and last position
    public static int[] findFirstAndLast(int[] arr, int target) {
        int[] result = {-1, -1};
        
        // Find first occurrence
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                result[0] = i;
                break;
            }
        }
        
        // Find last occurrence
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] == target) {
                result[1] = i;
                break;
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        int[] numbers = {5, 2, 8, 12, 3, 7, 3, 15, 3};
        
        System.out.println("Array: " + Arrays.toString(numbers));
        
        // Linear Search
        int target = 7;
        int index = linearSearch(numbers, target);
        System.out.println("\nLinear Search for " + target + ": " + 
                         (index != -1 ? "Found at index " + index : "Not found"));
        
        // Binary Search (on sorted array)
        int[] sorted = numbers.clone();
        Arrays.sort(sorted);
        System.out.println("\nSorted Array: " + Arrays.toString(sorted));
        index = binarySearch(sorted, target);
        System.out.println("Binary Search for " + target + ": " + 
                         (index != -1 ? "Found at index " + index : "Not found"));
        
        // First and Last Position
        int[] positions = findFirstAndLast(numbers, 3);
        System.out.println("\nFirst and last position of 3: " + 
                         Arrays.toString(positions));
    }
}
```

### Exercise 3: Sorting
Implement:
- Bubble sort
- Selection sort
- Insertion sort

**Solution:**
```java
import java.util.Arrays;

public class SortingExercises {
    
    // Bubble Sort
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    // Swap
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            // If no swaps, array is sorted
            if (!swapped) break;
        }
    }
    
    // Selection Sort
    public static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            // Find minimum element in unsorted part
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            // Swap minimum with first element
            int temp = arr[minIndex];
            arr[minIndex] = arr[i];
            arr[i] = temp;
        }
    }
    
    // Insertion Sort
    public static void insertionSort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            
            // Move elements greater than key one position ahead
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }
    
    public static void main(String[] args) {
        int[] original = {64, 34, 25, 12, 22, 11, 90};
        
        // Test Bubble Sort
        int[] arr1 = original.clone();
        System.out.println("Original: " + Arrays.toString(arr1));
        bubbleSort(arr1);
        System.out.println("Bubble Sort: " + Arrays.toString(arr1));
        
        // Test Selection Sort
        int[] arr2 = original.clone();
        selectionSort(arr2);
        System.out.println("Selection Sort: " + Arrays.toString(arr2));
        
        // Test Insertion Sort
        int[] arr3 = original.clone();
        insertionSort(arr3);
        System.out.println("Insertion Sort: " + Arrays.toString(arr3));
    }
}
```

### Exercise 4: Array Manipulation
Write methods to:
- Merge two sorted arrays
- Remove duplicates from sorted array
- Find intersection of two arrays

**Solution:**
```java
import java.util.*;

public class ArrayManipulation {
    
    // Merge two sorted arrays
    public static int[] mergeSortedArrays(int[] arr1, int[] arr2) {
        int[] merged = new int[arr1.length + arr2.length];
        int i = 0, j = 0, k = 0;
        
        while (i < arr1.length && j < arr2.length) {
            if (arr1[i] <= arr2[j]) {
                merged[k++] = arr1[i++];
            } else {
                merged[k++] = arr2[j++];
            }
        }
        
        // Copy remaining elements
        while (i < arr1.length) {
            merged[k++] = arr1[i++];
        }
        while (j < arr2.length) {
            merged[k++] = arr2[j++];
        }
        
        return merged;
    }
    
    // Remove duplicates from sorted array (returns new length)
    public static int removeDuplicates(int[] arr) {
        if (arr.length == 0) return 0;
        
        int uniqueIndex = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] != arr[uniqueIndex]) {
                uniqueIndex++;
                arr[uniqueIndex] = arr[i];
            }
        }
        return uniqueIndex + 1;
    }
    
    // Find intersection of two arrays
    public static int[] findIntersection(int[] arr1, int[] arr2) {
        Set<Integer> set1 = new HashSet<>();
        Set<Integer> intersection = new HashSet<>();
        
        for (int num : arr1) {
            set1.add(num);
        }
        
        for (int num : arr2) {
            if (set1.contains(num)) {
                intersection.add(num);
            }
        }
        
        // Convert to array
        int[] result = new int[intersection.size()];
        int i = 0;
        for (int num : intersection) {
            result[i++] = num;
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        // Test merge
        int[] arr1 = {1, 3, 5, 7};
        int[] arr2 = {2, 4, 6, 8};
        System.out.println("Merged: " + Arrays.toString(mergeSortedArrays(arr1, arr2)));
        
        // Test remove duplicates
        int[] arr3 = {1, 1, 2, 2, 3, 4, 4, 5};
        int newLength = removeDuplicates(arr3);
        System.out.println("After removing duplicates: " + 
                         Arrays.toString(Arrays.copyOf(arr3, newLength)));
        
        // Test intersection
        int[] arr4 = {1, 2, 3, 4, 5};
        int[] arr5 = {3, 4, 5, 6, 7};
        System.out.println("Intersection: " + 
                         Arrays.toString(findIntersection(arr4, arr5)));
    }
}
```

### Exercise 5: Matrix Operations
Implement:
- Matrix addition
- Matrix multiplication
- Check if matrix is symmetric

**Solution:**
```java
import java.util.Arrays;

public class MatrixOperations {
    
    // Matrix Addition
    public static int[][] addMatrices(int[][] mat1, int[][] mat2) {
        int rows = mat1.length;
        int cols = mat1[0].length;
        int[][] result = new int[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = mat1[i][j] + mat2[i][j];
            }
        }
        return result;
    }
    
    // Matrix Multiplication
    public static int[][] multiplyMatrices(int[][] mat1, int[][] mat2) {
        int rows1 = mat1.length;
        int cols1 = mat1[0].length;
        int cols2 = mat2[0].length;
        
        int[][] result = new int[rows1][cols2];
        
        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < cols2; j++) {
                for (int k = 0; k < cols1; k++) {
                    result[i][j] += mat1[i][k] * mat2[k][j];
                }
            }
        }
        return result;
    }
    
    // Check if matrix is symmetric
    public static boolean isSymmetric(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] != matrix[j][i]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    // Display matrix
    public static void displayMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }
    
    public static void main(String[] args) {
        int[][] mat1 = {{1, 2}, {3, 4}};
        int[][] mat2 = {{5, 6}, {7, 8}};
        
        System.out.println("Matrix 1:");
        displayMatrix(mat1);
        System.out.println("\nMatrix 2:");
        displayMatrix(mat2);
        
        System.out.println("\nAddition:");
        displayMatrix(addMatrices(mat1, mat2));
        
        System.out.println("\nMultiplication:");
        displayMatrix(multiplyMatrices(mat1, mat2));
        
        int[][] symmetric = {{1, 2, 3}, {2, 4, 5}, {3, 5, 6}};
        System.out.println("\nIs symmetric: " + isSymmetric(symmetric));
    }
}
```

### Exercise 6: Advanced Problems
Solve:
- Find pair with given sum
- Find subarray with maximum sum (Kadane's algorithm)
- Find missing number in array

**Solution:**
```java
import java.util.*;

public class AdvancedProblems {
    
    // Find pair with given sum
    public static boolean findPairWithSum(int[] arr, int targetSum) {
        Set<Integer> seen = new HashSet<>();
        
        for (int num : arr) {
            int complement = targetSum - num;
            if (seen.contains(complement)) {
                System.out.println("Pair found: " + num + " + " + complement + 
                                 " = " + targetSum);
                return true;
            }
            seen.add(num);
        }
        return false;
    }
    
    // Maximum subarray sum (Kadane's Algorithm)
    public static int maxSubarraySum(int[] arr) {
        int maxSoFar = arr[0];
        int maxEndingHere = arr[0];
        
        for (int i = 1; i < arr.length; i++) {
            maxEndingHere = Math.max(arr[i], maxEndingHere + arr[i]);
            maxSoFar = Math.max(maxSoFar, maxEndingHere);
        }
        
        return maxSoFar;
    }
    
    // Find missing number (1 to n)
    public static int findMissingNumber(int[] arr, int n) {
        int expectedSum = n * (n + 1) / 2;
        int actualSum = 0;
        
        for (int num : arr) {
            actualSum += num;
        }
        
        return expectedSum - actualSum;
    }
    
    public static void main(String[] args) {
        // Test pair with sum
        int[] arr1 = {1, 4, 45, 6, 10, 8};
        System.out.println("Array: " + Arrays.toString(arr1));
        findPairWithSum(arr1, 16);
        
        // Test max subarray sum
        int[] arr2 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println("\nArray: " + Arrays.toString(arr2));
        System.out.println("Maximum subarray sum: " + maxSubarraySum(arr2));
        
        // Test missing number
        int[] arr3 = {1, 2, 4, 5, 6};
        System.out.println("\nArray: " + Arrays.toString(arr3));
        System.out.println("Missing number: " + findMissingNumber(arr3, 6));
    }
}
```

### Challenge Exercise: Array Rotation
Implement array rotation by k positions (left and right) using three different methods:
1. Using temporary array
2. One by one rotation
3. Reversal algorithm

**Hint:** The reversal algorithm is most efficient:
- For left rotation by k: Reverse first k, then reverse remaining, then reverse all
- For right rotation by k: Reverse last k, then reverse remaining, then reverse all

---

## Further Reading

### Books
1. **"Introduction to Algorithms" by Cormen, Leiserson, Rivest, and Stein**
   - Chapter on arrays and sorting algorithms
   - Comprehensive coverage of array-based data structures

2. **"Java: The Complete Reference" by Herbert Schildt**
   - In-depth coverage of Java arrays
   - Arrays utility class methods

3. **"Effective Java" by Joshua Bloch**
   - Item 25: Prefer lists to arrays (understanding trade-offs)
   - Best practices for array usage

4. **"Data Structures and Algorithms in Java" by Robert Lafore**
   - Array-based data structures
   - Practical implementations

### Online Resources

**Official Documentation:**
- [Java Arrays Tutorial (Oracle)](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html)
- [Arrays Class API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Arrays.html)
- [System.arraycopy() Documentation](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/System.html#arraycopy)

**Interactive Learning:**
- [HackerRank Array Challenges](https://www.hackerrank.com/domains/data-structures/arrays)
- [LeetCode Array Problems](https://leetcode.com/tag/array/)
- [CodeSignal Array Practice](https://codesignal.com/)

**Video Tutorials:**
- FreeCodeCamp: Java Arrays Deep Dive
- Udemy: Mastering Data Structures in Java
- YouTube: Array Algorithms Playlist

**Articles and Blogs:**
- Baeldung: [Java Arrays Guide](https://www.baeldung.com/java-arrays-guide)
- GeeksforGeeks: [Arrays in Java](https://www.geeksforgeeks.org/arrays-in-java/)
- DZone: Java Array Best Practices

### Related Topics to Explore

1. **ArrayList and Collections**
   - Dynamic arrays
   - Collection framework
   - List interface

2. **Advanced Array Algorithms**
   - Two-pointer technique
   - Sliding window
   - Prefix sum arrays

3. **Memory Management**
   - Array memory layout
   - Cache locality
   - Performance considerations

4. **Multidimensional Arrays**
   - 3D arrays and beyond
   - Sparse matrices
   - Jagged arrays optimization

5. **Array-Based Data Structures**
   - Stack implementation
   - Queue implementation
   - Circular buffers

### Practice Platforms
- **LeetCode** - 150+ array problems (Easy to Hard)
- **HackerRank** - Structured array challenges
- **CodeForces** - Competitive programming arrays
- **InterviewBit** - Interview-focused array problems
- **Project Euler** - Mathematical array problems

### Algorithm Complexity Reference

| Operation | Array | ArrayList |
|-----------|-------|-----------|
| Access by index | O(1) | O(1) |
| Search (unsorted) | O(n) | O(n) |
| Search (sorted) | O(log n) | O(log n) |
| Insert at end | O(1)* | O(1) amortized |
| Insert at beginning | O(n) | O(n) |
| Delete | O(n) | O(n) |
| Space | O(n) | O(n) |

*Note: Array insertion requires creating new array

### Common Interview Topics
1. Two Sum problem
2. Maximum subarray (Kadane's)
3. Array rotation
4. Merge intervals
5. Find duplicates
6. Missing number
7. Move zeros to end
8. Product of array except self
9. Container with most water
10. Three sum problem

### Best Practices Summary

✅ **Do:**
- Always validate array bounds
- Use enhanced for loop for read-only access
- Consider Arrays utility methods
- Document array size constraints
- Use meaningful variable names
- Handle null and empty arrays

❌ **Don't:**
- Access arrays without bounds checking
- Assume array is not null
- Confuse length with length()
- Ignore ArrayIndexOutOfBoundsException
- Create unnecessarily large arrays
- Use parallel arrays when objects would be better

### Performance Tips

1. **Prefer primitive arrays** when possible (less memory overhead)
2. **Use System.arraycopy()** for bulk operations (native implementation)
3. **Consider ArrayList** for dynamic sizing needs
4. **Initialize with known size** to avoid resizing
5. **Use binary search** on sorted arrays
6. **Cache array length** in loops when possible

### Next Steps

After mastering arrays, proceed to:
1. **Tutorial 12: Strings** - Text manipulation and processing
2. **Collections Framework** - Dynamic data structures
3. **Algorithm Design** - Sorting and searching
4. **Data Structures** - Stacks, queues, linked lists

---

**Congratulations!** You've completed the Arrays tutorial. Arrays are fundamental to programming and mastering them provides a solid foundation for more complex data structures and algorithms.

**Key Takeaways:**
- Arrays are fixed-size, homogeneous containers
- Arrays are objects in Java
- Multiple ways to copy and manipulate arrays
- Understanding time/space complexity is crucial
- Practice with real problems solidifies understanding

Continue practicing with the exercises and exploring array-based algorithms to strengthen your skills!
