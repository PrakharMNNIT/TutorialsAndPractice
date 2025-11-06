# 🛡️ Type Guards & Narrowing - Runtime Type Safety

[← Previous: Template Literals](./25_template_literal_types.md) | [← Back to Main](../README.md) | [Next: Discriminated Unions →](./27_discriminated_unions.md)

---

## 📝 Overview

Type guards enable runtime type checking that TypeScript understands, allowing you to narrow union types to specific types safely. They bridge compile-time types with runtime values, crucial for handling dynamic data and user input.

**What You'll Learn:**
- Built-in type guards (typeof, instanceof, in)
- Custom type guard functions
- Type predicates (value is Type)
- Assertion functions (asserts)
- Control flow analysis
- Type narrowing patterns

### 🎯 Learning Objectives

- ✅ Master built-in type guards
- ✅ Create custom type guards
- ✅ Use type predicates effectively
- ✅ Apply assertion functions
- ✅ Understand control flow narrowing
- ✅ Write safe runtime checks

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced
- **Estimated Time**: 4-5 hours
- **Prerequisites**: [Union Types](./14_union_intersection.md), [Functions](./11_functions.md)
- **Practice Exercises**: 12 challenges
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [Built-in Type Guards](#builtin-guards)
2. [Custom Type Guards](#custom-guards)
3. [Type Predicates](#type-predicates)
4. [Assertion Functions](#assertion-functions)
5. [Control Flow Analysis](#control-flow)
6. [Advanced Narrowing](#advanced-narrowing)
7. [Best Practices](#best-practices)
8. [Higher-Order FAQs](#faqs)
9. [Interview Questions](#interview-questions)

---

<a name="builtin-guards"></a>
## 1. Built-in Type Guards

### 1.1 typeof Guard

```typescript
// typeof for primitives
function process(value: string | number): string {
  if (typeof value === "string") {
    // value: string
    return value.toUpperCase();
  } else {
    // value: number
    return value.toFixed(2);
  }
}

// Multiple typeof checks
function handle(input: string | number | boolean): string {
  if (typeof input === "string") {
    return input;
  } else if (typeof input === "number") {
    return input.toString();
  } else {
    return input ? "true" : "false";
  }
}
```

**Version Tracking:**
- ✅ typeof, instanceof, in guards (v1.0+)
- 🆕 Type predicates (v1.6+) - value is Type
- 🆕 Assertion functions (v3.7+) - asserts
- 🆕 Control flow analysis improvements (continuous)

### 1.2 instanceof Guard

```typescript
// instanceof for classes
class Dog {
  bark() { return "Woof!"; }
}

class Cat {
  meow() { return "Meow!"; }
}

function makeSound(animal: Dog | Cat): string {
  if (animal instanceof Dog) {
    // animal: Dog
    return animal.bark();
  } else {
    // animal: Cat
    return animal.meow();
  }
}

// With built-in types
function processValue(value: Date | string): string {
  if (value instanceof Date) {
    // value: Date
    return value.toISOString();
  } else {
    // value: string
    return value;
  }
}
```

### 1.3 in Operator Guard

```typescript
// in checks property existence
interface Bird {
  fly(): void;
  layEggs(): void;
}

interface Fish {
  swim(): void;
  layEggs(): void;
}

function move(animal: Bird | Fish): void {
  if ("fly" in animal) {
    // animal: Bird
    animal.fly();
  } else {
    // animal: Fish
    animal.swim();
  }
}
```

---

<a name="custom-guards"></a>
## 2. Custom Type Guards

### 2.1 Type Predicate Functions

```typescript
// Custom type guard with 'is' keyword
function isString(value: unknown): value is string {
  return typeof value === "string";
}

function process(value: unknown): void {
  if (isString(value)) {
    // value: string
    console.log(value.toUpperCase());
  }
}

// Generic type guard
function isArray<T>(value: unknown): value is T[] {
  return Array.isArray(value);
}

if (isArray<number>(data)) {
  // data: number[]
  console.log(data[0].toFixed());
}
```

### 2.2 Complex Type Guards

```typescript
// Type guard for interfaces
interface User {
  name: string;
  email: string;
}

function isUser(value: unknown): value is User {
  return (
    typeof value === "object" &&
    value !== null &&
    "name" in value &&
    "email" in value &&
    typeof (value as any).name === "string" &&
    typeof (value as any).email === "string"
  );
}

// Usage with API responses
async function fetchUser(): Promise<User> {
  const response = await fetch("/api/user");
  const data = await response.json();
  
  if (!isUser(data)) {
    throw new Error("Invalid user data");
  }
  
  return data; // Type: User
}
```

---

<a name="type-predicates"></a>
## 3. Type Predicates

### 3.1 is Keyword

```typescript
// Type predicate narrows type
function isNumber(value: unknown): value is number {
  return typeof value === "number" && !isNaN(value);
}

function calculate(value: unknown): number {
  if (isNumber(value)) {
    // value: number
    return value * 2;
  }
  return 0;
}

// Discriminated union guard
interface Success {
  status: "success";
  data: unknown;
}

interface Error {
  status: "error";
  message: string;
}

type Result = Success | Error;

function isSuccess(result: Result): result is Success {
  return result.status === "success";
}

function handle(result: Result): void {
  if (isSuccess(result)) {
    // result: Success
    console.log(result.data);
  } else {
    // result: Error
    console.log(result.message);
  }
}
```

---

<a name="assertion-functions"></a>
## 4. Assertion Functions

### 4.1 asserts Keyword

```typescript
// Assertion function throws if condition fails
function assert(condition: boolean, message: string): asserts condition {
  if (!condition) {
    throw new Error(message);
  }
}

function processValue(value: unknown): void {
  assert(typeof value === "string", "Expected string");
  // value: string (after assertion)
  console.log(value.toUpperCase());
}

// Type assertion function
function assertIsUser(value: unknown): asserts value is User {
  if (!isUser(value)) {
    throw new Error("Invalid user");
  }
}

function handleData(data: unknown): void {
  assertIsUser(data);
  // data: User (after assertion)
  console.log(data.name);
}
```

---

<a name="control-flow"></a>
## 5. Control Flow Analysis

### 5.1 Automatic Narrowing

```typescript
// TypeScript tracks type through control flow
function process(value: string | null): void {
  if (value === null) {
    return;
  }
  
  // value: string (null eliminated)
  console.log(value.toUpperCase());
}

// Truthiness narrowing
function printLength(str: string | null): void {
  if (str) {
    // str: string (null is falsy)
    console.log(str.length);
  }
}

// Multiple paths
function handle(value: string | number | null): void {
  if (!value) {
    // value: null | 0 | "" (falsy values)
    return;
  }
  
  if (typeof value === "string") {
    // value: string (and truthy)
    console.log(value);
  } else {
    // value: number (and truthy, non-zero)
    console.log(value);
  }
}
```

---

<a name="best-practices"></a>
## 6. Best Practices

### 6.1 Type Guard Guidelines

```typescript
// ✅ GOOD: Validate all required properties
function isUser(value: unknown): value is User {
  return (
    typeof value === "object" &&
    value !== null &&
    "name" in value &&
    "email" in value &&
    typeof (value as any).name === "string" &&
    typeof (value as any).email === "string"
  );
}

// ❌ BAD: Incomplete validation
function badIsUser(value: unknown): value is User {
  return typeof value === "object"; // Not enough!
}

// ✅ GOOD: Use assertion for invariants
function assertPositive(n: number): asserts n is number {
  assert(n > 0, "Must be positive");
}

// ✅ GOOD: Combine guards
if (isUser(data) && data.age >= 18) {
  // data: User with age check
}
```

---

## 🧠 Higher-Order FAQs

### FAQ 1: Type Guards vs Assertions

**Q: When should you use type guard functions (value is Type) versus assertion functions (asserts)? What are the trade-offs?**

<details>
<summary>View Detailed Answer</summary>

**Short Answer:** Type guards return boolean for branching logic; assertions throw errors for invariant checking. Different control flow patterns.

**Deep Explanation:**

**Type Guards** - For conditional logic:

```typescript
function isUser(value: unknown): value is User {
  return (
    typeof value === "object" &&
    value !== null &&
    "name" in value
  );
}

// Allows branching
if (isUser(data)) {
  console.log(data.name); // Handle valid case
} else {
  console.log("Not a user"); // Handle invalid case
}
```

**Assertions** - For invariants:

```typescript
function assertIsUser(value: unknown): asserts value is User {
  if (!isUser(value)) {
    throw new Error("Expected user");
  }
}

// Crashes if condition fails
assertIsUser(data);
console.log(data.name); // data is User or code doesn't reach here
```

**When to Use Each:**

| Scenario | Use | Reason |
|----------|-----|--------|
| Optional data | Type guard | Handle both cases |
| Required invariant | Assertion | Fail fast on violation |
| API response | Type guard | Graceful error handling |
| Function precondition | Assertion | Contract enforcement |
| User input | Type guard | Validate and provide feedback |
| Internal state | Assertion | Should never fail |

**Practical Comparison:**

```typescript
// API endpoint - use type guard
async function getUser(id: number): Promise<User | null> {
  const response = await fetch(`/api/users/${id}`);
  const data = await response.json();
  
  if (isUser(data)) {
    return data; // Valid user
  }
  
  console.error("Invalid user data");
  return null; // Handle gracefully
}

// Internal function - use assertion
function processUser(data: unknown): void {
  assertIsUser(data);
  // data should ALWAYS be User here
  // If not, it's a bug - fail loud
  
  updateDatabase(data);
}
```

**Error Handling Philosophy:**

```typescript
// Type guard - defensive programming
export function parseJSON(json: string): unknown | null {
  try {
    const data = JSON.parse(json);
    return data;
  } catch {
    return null; // Recover from error
  }
}

// Assertion - programming by contract
export function parseJSON(json: string): unknown {
  const data = JSON.parse(json);
  assert(data !== undefined, "Invalid JSON");
  return data; // Assume valid or crash
}
```

**Best Practice:**
- **Public APIs**: Use type guards (callers handle both paths)
- **Private/internal**: Use assertions (bugs should crash)
- **User input**: Type guards with helpful error messages
- **Developer errors**: Assertions to catch bugs early

</details>

---

## 🎯 Key Takeaways

✅ **typeof** guards primitives

✅ **instanceof** guards classes

✅ **in** guards check property existence

✅ **Type predicates** (value is Type) enable custom guards

✅ **Assertion functions** (asserts) throw on failure

✅ **Control flow** analysis tracks types automatically

✅ **Type guards** are essential for unknown/any types

---

[← Previous: Template Literals](./25_template_literal_types.md) | [Next: Discriminated Unions →](./27_discriminated_unions.md)

**Progress**: Topic 26 of 63 | Part IV: 60% Complete
