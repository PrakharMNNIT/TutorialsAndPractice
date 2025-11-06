# 🔧 Functions in TypeScript - Type-Safe Function Programming

[← Previous: Type Annotations](./10_type_annotations.md) | [← Back to Main](../README.md) | [Next: Objects & Interfaces →](./12_objects_interfaces.md)

---

## 📝 Overview

Functions are the building blocks of JavaScript and TypeScript applications. TypeScript adds powerful type checking to functions, including parameter types, return types, overloads, and more. This comprehensive guide covers everything you need to know about type-safe functions.

**What You'll Learn:**
- Function type annotations and signatures
- Optional and default parameters
- Rest parameters and spread syntax
- Function overloading
- Generic functions
- Higher-order functions
- Arrow functions vs regular functions

### 🎯 Learning Objectives

- ✅ Master function type signatures
- ✅ Use optional and default parameters effectively
- ✅ Implement function overloading
- ✅ Create generic functions
- ✅ Work with higher-order functions
- ✅ Handle callback types properly
- ✅ Use contextual typing in functions
- ✅ Apply function best practices

### 📊 Section Info

- **Difficulty**: ⭐⭐ Intermediate
- **Estimated Time**: 4-5 hours
- **Prerequisites**: [Basic Types](./09_basic_types.md), [Type Annotations](./10_type_annotations.md)
- **Practice Exercises**: 15 challenges
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [Function Type Basics](#function-basics)
2. [Parameter Types](#parameter-types)
3. [Return Types](#return-types)
4. [Function Type Expressions](#function-types)
5. [Optional and Default Parameters](#optional-default)
6. [Rest Parameters](#rest-parameters)
7. [Function Overloading](#function-overloading)
8. [Generic Functions](#generic-functions)
9. [Higher-Order Functions](#higher-order)
10. [this in Functions](#this-functions)
11. [Best Practices](#best-practices)
12. [Higher-Order FAQs](#faqs)
13. [Interview Questions](#interview-questions)

---

<a name="function-basics"></a>
## 1. Function Type Basics

### 1.1 Function Declaration

```typescript
// Basic function with types
function add(a: number, b: number): number {
  return a + b;
}

// Type annotations required for parameters
function greet(name: string): string {
  return `Hello, ${name}!`;
}

// Void return type
function log(message: string): void {
  console.log(message);
  // No return value
}

// Never return type (function never returns)
function throwError(message: string): never {
  throw new Error(message);
}
```

### 1.2 Function Expression

```typescript
// Anonymous function expression
const multiply = function(a: number, b: number): number {
  return a * b;
};

// Arrow function
const divide = (a: number, b: number): number => {
  return a / b;
};

// Concise arrow function
const subtract = (a: number, b: number): number => a - b;
```

### 1.3 Method Shorthand

```typescript
// Object method
const calculator = {
  add(a: number, b: number): number {
    return a + b;
  },
  
  multiply(a: number, b: number): number {
    return a * b;
  }
};

// Class method
class Calculator {
  add(a: number, b: number): number {
    return a + b;
  }
}
```

---

<a name="parameter-types"></a>
## 2. Parameter Types

### 2.1 Basic Parameter Types

```typescript
// Single parameter
function square(n: number): number {
  return n * n;
}

// Multiple parameters
function createUser(
  name: string,
  age: number,
  isActive: boolean
): User {
  return { name, age, isActive };
}

// Object parameter
function displayPoint(point: { x: number; y: number }): void {
  console.log(`Point: (${point.x}, ${point.y})`);
}

// Array parameter
function sum(numbers: number[]): number {
  return numbers.reduce((a, b) => a + b, 0);
}
```

### 2.2 Destructured Parameters

```typescript
// Object destructuring
function greet({ name, age }: { name: string; age: number }): string {
  return `${name} is ${age} years old`;
}

// With interface
interface User {
  name: string;
  age: number;
}

function greet({ name, age }: User): string {
  return `${name} is ${age} years old`;
}

// Array destructuring
function swapPair([a, b]: [number, number]): [number, number] {
  return [b, a];
}

console.log(swapPair([1, 2])); // [2, 1]
```

---

<a name="optional-default"></a>
## 3. Optional and Default Parameters

### 3.1 Optional Parameters

```typescript
// Optional parameter with ?
function greet(name: string, title?: string): string {
  if (title) {
    return `Hello, ${title} ${name}`;
  }
  return `Hello, ${name}`;
}

console.log(greet("Alice")); // "Hello, Alice"
console.log(greet("Bob", "Dr.")); // "Hello, Dr. Bob"

// Optional parameters must come last
function create(
  name: string,
  age?: number,
  email?: string
): User {
  return { name, age, email };
}

// ❌ Error: Required parameter cannot follow optional
// function invalid(age?: number, name: string) {}
```

### 3.2 Default Parameters

```typescript
// Default parameter values
function createUser(
  name: string,
  age: number = 18,
  role: string = "user"
): User {
  return { name, age, role };
}

console.log(createUser("Alice"));
// { name: "Alice", age: 18, role: "user" }

// Type inferred from default value
function greet(name: string, greeting = "Hello") {
  // greeting inferred as string
  return `${greeting}, ${name}`;
}

// Default can be expression
function timestamp(date: Date = new Date()): number {
  return date.getTime();
}

// Default from previous parameter
function calculateArea(width: number, height: number = width): number {
  return width * height;
}

console.log(calculateArea(5)); // 25 (square)
console.log(calculateArea(5, 10)); // 50 (rectangle)
```

---

<a name="rest-parameters"></a>
## 4. Rest Parameters

### 4.1 Basic Rest Parameters

```typescript
// Rest parameters (...args)
function sum(...numbers: number[]): number {
  return numbers.reduce((a, b) => a + b, 0);
}

console.log(sum(1, 2, 3, 4, 5)); // 15

// Rest with other parameters
function greet(greeting: string, ...names: string[]): string {
  return `${greeting} ${names.join(' and ')}!`;
}

console.log(greet("Hello", "Alice", "Bob", "Charlie"));
// "Hello Alice and Bob and Charlie!"

// Rest must be last
function invalid(...numbers: number[], message: string) {} // ❌ Error
```

### 4.2 Tuple Rest Parameters

```typescript
// Rest parameter as tuple
function format(...args: [string, number, boolean]): string {
  const [text, count, flag] = args;
  return `${text}: ${count} (${flag})`;
}

// Mixed rest parameters
function configure(
  name: string,
  ...options: [number, boolean] | [string]
): Config {
  // options can be [number, boolean] OR [string]
}
```

---

<a name="function-overloading"></a>
## 5. Function Overloading

### 5.1 Basic Overloading

```typescript
// Overload signatures
function process(value: string): string;
function process(value: number): number;
function process(value: boolean): string;

// Implementation signature
function process(value: string | number | boolean): string | number {
  if (typeof value === "string") {
    return value.toUpperCase();
  } else if (typeof value === "number") {
    return value * 2;
  } else {
    return value ? "true" : "false";
  }
}

// Usage
const str = process("hello"); // Type: string
const num = process(42); // Type: number
const bool = process(true); // Type: string
```

### 5.2 Complex Overloading

```typescript
// Overloading with different parameter counts
function createElement(tag: "div"): HTMLDivElement;
function createElement(tag: "span"): HTMLSpanElement;
function createElement(tag: "img", src: string): HTMLImageElement;

function createElement(
  tag: string,
  src?: string
): HTMLElement {
  const element = document.createElement(tag);
  if (tag === "img" && src) {
    (element as HTMLImageElement).src = src;
  }
  return element;
}

// Usage
const div = createElement("div"); // HTMLDivElement
const img = createElement("img", "photo.jpg"); // HTMLImageElement
```

### 5.3 Overloading Best Practices

```typescript
// ✅ GOOD: Overloads from most specific to most general
function format(value: Date): string;
function format(value: number): string;
function format(value: string): string;
function format(value: Date | number | string): string {
  if (value instanceof Date) {
    return value.toISOString();
  } else if (typeof value === "number") {
    return value.toFixed(2);
  } else {
    return value;
  }
}

// ❌ BAD: Implementation signature must be compatible with all overloads
function bad(x: string): string; // Overload
function bad(x: number): number; // Overload
// function bad(x: string): string {} // ❌ Error: doesn't cover number overload
```

---

<a name="generic-functions"></a>
## 6. Generic Functions

### 6.1 Basic Generic Functions

```typescript
// Generic function
function identity<T>(value: T): T {
  return value;
}

// Usage
const num = identity<number>(42); // T = number
const str = identity<string>("hello"); // T = string

// Type inference
const auto1 = identity(42); // T inferred as number
const auto2 = identity("hello"); // T inferred as string

// Generic array function
function firstElement<T>(arr: T[]): T | undefined {
  return arr[0];
}

const first = firstElement([1, 2, 3]); // Type: number | undefined
const firstStr = firstElement(["a", "b"]); // Type: string | undefined
```

### 6.2 Multiple Type Parameters

```typescript
// Multiple generics
function pair<T, U>(first: T, second: U): [T, U] {
  return [first, second];
}

const p1 = pair("hello", 42); // [string, number]
const p2 = pair(true, "world"); // [boolean, string]

// Generic constraints
function merge<T extends object, U extends object>(
  obj1: T,
  obj2: U
): T & U {
  return { ...obj1, ...obj2 };
}

const merged = merge(
  { name: "Alice" },
  { age: 30 }
); // { name: string; age: number }
```

---

<a name="higher-order"></a>
## 7. Higher-Order Functions

### 7.1 Functions Returning Functions

```typescript
// Function factory
function multiplier(factor: number): (n: number) => number {
  return (n: number) => n * factor;
}

const double = multiplier(2);
const triple = multiplier(3);

console.log(double(5)); // 10
console.log(triple(5)); // 15

// Generic factory
function createGetter<T>(value: T): () => T {
  return () => value;
}

const getName = createGetter("Alice"); // () => string
const getAge = createGetter(30); // () => number
```

### 7.2 Functions Taking Functions

```typescript
// Callback parameter
function processArray<T, U>(
  arr: T[],
  callback: (item: T) => U
): U[] {
  return arr.map(callback);
}

const numbers = [1, 2, 3];
const doubled = processArray(numbers, n => n * 2); // number[]
const strings = processArray(numbers, n => n.toString()); // string[]

// Predicate function
function filter<T>(
  arr: T[],
  predicate: (item: T) => boolean
): T[] {
  return arr.filter(predicate);
}

const evens = filter([1, 2, 3, 4], n => n % 2 === 0); // [2, 4]
```

---

<a name="this-functions"></a>
## 8. this in Functions

### 8.1 this Parameter

```typescript
// Explicit this parameter (doesn't count as real parameter)
function greet(this: User) {
  console.log(`Hello, ${this.name}`);
}

interface User {
  name: string;
  greet: (this: User) => void;
}

const user: User = {
  name: "Alice",
  greet
};

user.greet(); // ✅ OK
// greet(); // ❌ Error: void type provided, User expected
```

### 8.2 Arrow Functions and this

```typescript
class Handler {
  message: string = "Hello";
  
  // Regular function - dynamic this
  regularMethod() {
    setTimeout(function() {
      // console.log(this.message); // ❌ this is undefined
    }, 1000);
  }
  
  // Arrow function - lexical this
  arrowMethod() {
    setTimeout(() => {
      console.log(this.message); // ✅ this is Handler instance
    }, 1000);
  }
}
```

---

## 📊 Self-Assessment Checkpoints

### ✅ Checkpoint 1: Basic Function Types

**You should be able to:**
- ✅ Annotate function parameters and return types
- ✅ Use optional and default parameters
- ✅ Implement rest parameters

**Verification:**
```typescript
// Can you type this function?
function createPerson(name, age = 18, ...hobbies) {
  return { name, age, hobbies };
}
```

<details>
<summary>View Solution</summary>

```typescript
function createPerson(
  name: string,
  age: number = 18,
  ...hobbies: string[]
): { name: string; age: number; hobbies: string[] } {
  return { name, age, hobbies };
}
```
</details>

---

### ✅ Checkpoint 2: Generic Functions

**You should be able to:**
- ✅ Create generic functions
- ✅ Use generic constraints
- ✅ Implement higher-order generic functions

**Verification:**
```typescript
// Can you make this function generic?
function getProperty(obj, key) {
  return obj[key];
}
```

<details>
<summary>View Solution</summary>

```typescript
function getProperty<T, K extends keyof T>(obj: T, key: K): T[K] {
  return obj[key];
}

// Usage
const user = { name: "Alice", age: 30 };
const name = getProperty(user, "name"); // Type: string
const age = getProperty(user, "age"); // Type: number
```
</details>

---

## 🧠 Higher-Order Thinking FAQs

### FAQ 1: Function Overloading vs Union Types

**Q: When should you use function overloading versus union types for parameters? What are the trade-offs?**

<details>
<summary>View Detailed Answer</summary>

**Short Answer:** Use overloading when return type depends on input type; use unions when return type is consistent.

**Deep Explanation:**

**Union Types** - Same return type regardless of input:

```typescript
// Union type approach
function format(value: string | number): string {
  return typeof value === "string" ? value : value.toString();
}

const result = format("hello"); // Type: string
const result2 = format(42); // Type: string
```

**Function Overloading** - Different return types based on input:

```typescript
// Overloading approach
function process(value: string): string;
function process(value: number): number;
function process(value: boolean): string;
function process(value: string | number | boolean): string | number {
  if (typeof value === "string") {
    return value.toUpperCase(); // Returns string
  } else if (typeof value === "number") {
    return value * 2; // Returns number
  } else {
    return value ? "true" : "false"; // Returns string
  }
}

const str = process("hello"); // Type: string
const num = process(42); // Type: number (not string | number)
const bool = process(true); // Type: string
```

**When to Use Each:**

| Scenario | Use | Example |
|----------|-----|---------|
| Return type varies by input | Overloading | `process(string) => string, process(number) => number` |
| Return type same for all | Union | `format(string \| number) => string` |
| Parameter correlation | Overloading | `createElement("div") vs createElement("img", src)` |
| Simple type handling | Union | `log(string \| Error)` |

**Trade-offs:**

✅ **Overloading Benefits:**
- Precise return types
- Better autocomplete
- Documents different use cases

❌ **Overloading Drawbacks:**
- More verbose
- Implementation must handle all cases
- Can be complex to maintain

✅ **Union Benefits:**
- Simpler syntax
- Easier to maintain
- Clear single behavior

❌ **Union Drawbacks:**
- Less precise return types
- Requires type narrowing in implementation

**Best Practice:**
```typescript
// Start with union
function simple(value: string | number): string {
  return String(value);
}

// Use overloading when return types differ significantly
function complex(value: string): string[];
function complex(value: number): number[];
function complex(value: string | number): string[] | number[] {
  return typeof value === "string" 
    ? value.split("")
    : Array.from({ length: value }, (_, i) => i);
}
```

</details>

---

### FAQ 2: Generic Constraints Deep Dive

**Q: How do generic constraints work, and why can't TypeScript infer constraints automatically from function body?**

<details>
<summary>View Detailed Answer</summary>

**Short Answer:** Constraints must be explicit because TypeScript checks callers, not implementation; automatic inference would break type safety.

**Deep Explanation:**

**Why Constraints Are Needed:**

```typescript
// Without constraint - too permissive
function getLength<T>(value: T): number {
  return value.length; // ❌ Error: Property 'length' does not exist on T
}

// With constraint - type-safe
function getLength<T extends { length: number }>(value: T): number {
  return value.length; // ✅ OK
}

getLength("hello"); // ✅ string has length
getLength([1, 2, 3]); // ✅ array has length
// getLength(42); // ❌ Error: number doesn't have length
```

**Why TypeScript Can't Auto-Infer:**

Imagine if TypeScript inferred constraints from usage:

```typescript
// Hypothetical auto-inference
function process<T>(value: T) {
  return value.toString(); // Auto-infer: T extends { toString: () => string }
}

// Problem 1: Implementation could change
function process<T>(value: T) {
  // Later, developer changes implementation
  return value.toFixed(); // Now needs T extends { toFixed: () => string }
  // This would break all existing callers!
}

// Problem 2: Callers need to know constraints upfront
process(myValue); // What constraints does myValue need to satisfy?
```

**Design Principles:**

1. **Explicit Contracts**: Constraints are part of function signature (contract with callers)
2. **Separate Checking**: TypeScript checks callers against signature, implementation against signature
3. **Stability**: Changing implementation shouldn't break callers

**Advanced Constraint Patterns:**

```typescript
// Multiple constraints
function merge<T extends object, U extends object>(
  obj1: T,
  obj2: U
): T & U {
  return { ...obj1, ...obj2 };
}

// Constraint with keyof
function getProperty<T, K extends keyof T>(obj: T, key: K): T[K] {
  return obj[key];
}

// Constraint with conditional
function flatten<T extends any[]>(arr: T): T[number][] {
  return arr.flat();
}
```

**Real-World Application:**

```typescript
// Type-safe API client
interface Endpoint {
  path: string;
  method: "GET" | "POST" | "PUT" | "DELETE";
  body?: unknown;
}

async function apiCall<T extends Endpoint>(
  endpoint: T
): Promise<T["method"] extends "GET" ? Data : void> {
  // Constraint ensures endpoint has required shape
  // Return type depends on method
}
```

</details>

---

### FAQ 3: Callback Type Safety

**Q: What's the best way to type callbacks in TypeScript, especially when dealing with async operations and error handling?**

<details>
<summary>View Detailed Answer</summary>

**Short Answer:** Use specific function types, generic callbacks, or Promise-based patterns depending on complexity.

**Deep Explanation:**

**Pattern 1: Simple Callbacks**

```typescript
// Basic callback type
type Callback = (result: string) => void;

function fetchData(callback: Callback): void {
  setTimeout(() => {
    callback("Data loaded");
  }, 1000);
}

// Usage
fetchData((result) => {
  console.log(result); // result is string
});
```

**Pattern 2: Error-First Callbacks (Node.js style)**

```typescript
// Error-first callback
type NodeCallback<T> = (error: Error | null, data?: T) => void;

function readFile(path: string, callback: NodeCallback<string>): void {
  setTimeout(() => {
    const success = true;
    if (success) {
      callback(null, "file contents");
    } else {
      callback(new Error("File not found"));
    }
  }, 1000);
}

// Usage
readFile("file.txt", (error, data) => {
  if (error) {
    console.error(error);
    return;
  }
  console.log(data); // data is string | undefined
});
```

**Pattern 3: Generic Callbacks**

```typescript
// Generic callback for flexibility
function processData<T, R>(
  data: T,
  transform: (item: T) => R
): R {
  return transform(data);
}

const result = processData(42, n => n * 2); // R inferred as number
const text = processData("hello", s => s.toUpperCase()); // R inferred as string
```

**Pattern 4: Promise-Based (Modern)**

```typescript
// ✅ BEST: Use Promises instead of callbacks
async function fetchData(): Promise<string> {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      resolve("Data loaded");
    }, 1000);
  });
}

// Usage with async/await
try {
  const data = await fetchData();
  console.log(data); // string
} catch (error) {
  console.error(error);
}
```

**Comparison:**

| Pattern | Type Safety | Error Handling | Readability | Modern |
|---------|-------------|----------------|-------------|--------|
| Simple callback | Good | Manual | Fair | No |
| Error-first | Good | Explicit | Fair | No |
| Generic callback | Excellent | Manual | Good | Partial |
| Promise | Excellent | Built-in | Excellent | Yes |

**Recommendation:**
- New code: Use Promises/async-await
- Legacy integration: Use error-first callbacks
- Library APIs: Provide both callback and Promise versions

</details>

---

## 🎤 Senior SDE Interview Questions

### Interview Question 1: Function Overloading Architecture

**Q:** "You're designing a type-safe routing library. Users should call `route.get(path, handler)` for GET requests and `route.post(path, body, handler)` for POST. How would you type this using function overloading?"

**Key Concepts:**
- Function overloading
- Generic constraints
- Type inference
- API design

**Expected Answer:**

```typescript
interface Handler<T = void> {
  (req: Request, res: Response): T | Promise<T>;
}

interface Route {
  get(path: string, handler: Handler): void;
  post<T>(path: string, body: T, handler: Handler<T>): void;
  put<T>(path: string, body: T, handler: Handler<T>): void;
  delete(path: string, handler: Handler): void;
}

// Implementation
class Router implements Route {
  get(path: string, handler: Handler): void {
    // Register GET route
  }
  
  post<T>(path: string, body: T, handler: Handler<T>): void {
    // Register POST route with body type
  }
  
  put<T>(path: string, body: T, handler: Handler<T>): void {
    // Register PUT route
  }
  
  delete(path: string, handler: Handler): void {
    // Register DELETE route
  }
}

// Usage
const router = new Router();

router.get("/users", (req, res) => {
  res.json({ users: [] });
});

router.post("/users", { name: "Alice" }, (req, res) => {
  // Body type inferred
  res.json({ created: true });
});
```

**Follow-ups:**
1. "How would you add middleware support with types?"
2. "How would you handle route parameters (e.g., `/users/:id`)?"
3. "What if handlers need to return different types?"

**Green Flags:**
- Uses generics for body types
- Considers handler return types
- Mentions middleware patterns
- Discusses type safety for route params

---

## 🎯 Key Takeaways

✅ **Function parameters** always require type annotations

✅ **Return types** can be inferred but explicit is often better

✅ **Optional parameters** use `?` and must come after required

✅ **Default parameters** provide values and infer types

✅ **Rest parameters** collect remaining arguments as array

✅ **Function overloading** allows multiple signatures

✅ **Generic functions** work with multiple types safely

✅ **Higher-order functions** take or return functions

---

[← Previous: Type Annotations](./10_type_annotations.md) | [Next: Objects & Interfaces →](./12_objects_interfaces.md)

**Progress**: Topic 11 of 63 | Part II: 86% Complete
