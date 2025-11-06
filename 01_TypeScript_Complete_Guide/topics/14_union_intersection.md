# 🔗 Union & Intersection Types - Combining Types Powerfully

[← Previous: Arrays & Tuples](./13_arrays_tuples.md) | [← Back to Main](../README.md) | [Next: Type Aliases →](./15_type_aliases.md)

---

## 📝 Overview

Union and intersection types are powerful tools in TypeScript's type system that allow you to combine existing types in different ways. Unions represent "either-or" relationships, while intersections represent "and" relationships. Mastering these concepts is essential for modeling complex domain logic.

**What You'll Learn:**
- Union types and type narrowing
- Intersection types and type composition
- Discriminated unions
- Type guards for unions
- When to use unions vs intersections
- Advanced combination patterns

### 🎯 Learning Objectives

- ✅ Master union type syntax and usage
- ✅ Use intersection types for composition
- ✅ Apply type narrowing techniques
- ✅ Implement discriminated unions
- ✅ Create type guards
- ✅ Combine types effectively
- ✅ Understand type algebra

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced
- **Estimated Time**: 4-5 hours
- **Prerequisites**: [Basic Types](./09_basic_types.md), [Type Annotations](./10_type_annotations.md)
- **Practice Exercises**: 15 challenges
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [Union Types](#union-types)
2. [Type Narrowing](#type-narrowing)
3. [Intersection Types](#intersection-types)
4. [Discriminated Unions](#discriminated-unions)
5. [Type Guards](#type-guards)
6. [Unions vs Intersections](#unions-vs-intersections)
7. [Advanced Patterns](#advanced-patterns)
8. [Best Practices](#best-practices)
9. [Higher-Order FAQs](#faqs)
10. [Interview Questions](#interview-questions)

---

<a name="union-types"></a>
## 1. Union Types

### 1.1 Basic Union Syntax

```typescript
// Union type with |
let id: string | number;

id = "abc123"; // ✅ OK
id = 456; // ✅ OK
// id = true; // ❌ Error

// Union of multiple types
let value: string | number | boolean;
value = "hello";
value = 42;
value = true;

// Union of literals
type Status = "pending" | "approved" | "rejected";
let status: Status = "pending"; // ✅ OK
// let invalid: Status = "unknown"; // ❌ Error
```

**Version Tracking:**
- ✅ Union types (v1.0+) - Core feature
- 🆕 Discriminated unions (v2.0+) - Tagged unions
- 🆕 Exhaustiveness checking (v2.0+) - Switch completeness

### 1.2 Union in Function Parameters

```typescript
// Function accepting multiple types
function format(value: string | number): string {
  if (typeof value === "string") {
    return value.toUpperCase();
  } else {
    return value.toFixed(2);
  }
}

console.log(format("hello")); // "HELLO"
console.log(format(3.14159)); // "3.14"

// Array of union types
let mixed: (string | number)[] = [1, "two", 3, "four"];
```

### 1.3 Union Properties

```typescript
// Only common properties accessible without narrowing
interface Bird {
  fly(): void;
  layEggs(): void;
}

interface Fish {
  swim(): void;
  layEggs(): void;
}

function getPet(): Bird | Fish {
  return Math.random() > 0.5 ? new Bird() : new Fish();
}

const pet = getPet();
pet.layEggs(); // ✅ OK (common to both)
// pet.fly(); // ❌ Error: Fish doesn't have fly
// pet.swim(); // ❌ Error: Bird doesn't have swim
```

---

<a name="type-narrowing"></a>
## 2. Type Narrowing

### 2.1 typeof Guards

```typescript
// typeof for primitive narrowing
function process(value: string | number): string {
  if (typeof value === "string") {
    // value: string
    return value.toUpperCase();
  } else {
    // value: number
    return value.toFixed();
  }
}

// Multiple type checks
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

### 2.2 instanceof Guards

```typescript
// instanceof for class narrowing
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
```

### 2.3 in Operator Guards

```typescript
// in for property checking
interface Car {
  drive(): void;
}

interface Boat {
  sail(): void;
}

function move(vehicle: Car | Boat): void {
  if ("drive" in vehicle) {
    // vehicle: Car
    vehicle.drive();
  } else {
    // vehicle: Boat
    vehicle.sail();
  }
}
```

---

<a name="intersection-types"></a>
## 3. Intersection Types

### 3.1 Basic Intersection Syntax

```typescript
// Intersection type with &
type Named = { name: string };
type Aged = { age: number };

type Person = Named & Aged;

const person: Person = {
  name: "Alice",
  age: 30
}; // Must have both properties

// Intersection of multiple types
type Employee = Named & Aged & { employeeId: number };

const employee: Employee = {
  name: "Bob",
  age: 25,
  employeeId: 12345
};
```

### 3.2 Intersection with Interfaces

```typescript
// Combining interfaces
interface Colorful {
  color: string;
}

interface Circle {
  radius: number;
}

type ColorfulCircle = Colorful & Circle;

const redCircle: ColorfulCircle = {
  color: "red",
  radius: 10
};

// Intersection of multiple interfaces
interface Named { name: string; }
interface Aged { age: number; }
interface Employed { employeeId: number; }

type Employee = Named & Aged & Employed;
```

### 3.3 Merging Conflicts

```typescript
// Conflicting types result in never
interface A {
  value: string;
}

interface B {
  value: number;
}

type C = A & B;
// C = { value: never } - impossible to satisfy both

// ❌ Cannot create valid object
// const obj: C = { value: ??? }; // No valid value

// ✅ Use union if either type is acceptable
type D = A | B;
const obj1: D = { value: "string" }; // ✅ OK
const obj2: D = { value: 42 }; // ✅ OK
```

---

<a name="discriminated-unions"></a>
## 4. Discriminated Unions

### 4.1 Tagged Unions

```typescript
// Discriminated union with 'kind' field
interface Square {
  kind: "square";
  size: number;
}

interface Circle {
  kind: "circle";
  radius: number;
}

interface Rectangle {
  kind: "rectangle";
  width: number;
  height: number;
}

type Shape = Square | Circle | Rectangle;

// Type-safe handling
function area(shape: Shape): number {
  switch (shape.kind) {
    case "square":
      // shape: Square
      return shape.size ** 2;
    case "circle":
      // shape: Circle
      return Math.PI * shape.radius ** 2;
    case "rectangle":
      // shape: Rectangle
      return shape.width * shape.height;
  }
}
```

### 4.2 Result Type Pattern

```typescript
// Success/Error result type
interface Success<T> {
  success: true;
  data: T;
}

interface Failure {
  success: false;
  error: string;
}

type Result<T> = Success<T> | Failure;

// Usage
function divide(a: number, b: number): Result<number> {
  if (b === 0) {
    return { success: false, error: "Division by zero" };
  }
  return { success: true, data: a / b };
}

const result = divide(10, 2);
if (result.success) {
  // result: Success<number>
  console.log(result.data);
} else {
  // result: Failure
  console.log(result.error);
}
```

---

<a name="type-guards"></a>
## 5. Type Guards

### 5.1 Custom Type Guards

```typescript
// Type predicate with 'is'
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

if (isArray<number>(value)) {
  // value: number[]
  console.log(value[0].toFixed());
}
```

### 5.2 Assertion Functions

```typescript
// Assertion function (v3.7+)
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
function assertIsString(value: unknown): asserts value is string {
  if (typeof value !== "string") {
    throw new Error("Not a string");
  }
}

function handleValue(value: unknown): void {
  assertIsString(value);
  // value: string
  console.log(value.length);
}
```

---

<a name="unions-vs-intersections"></a>
## 6. Unions vs Intersections

### 6.1 Conceptual Difference

```mermaid
graph TD
    A[Union A | B] --> B[Has properties of A OR B]
    C[Intersection A & B] --> D[Has properties of A AND B]
    
    B --> E[Less specific]
    D --> F[More specific]
```

**Comparison:**

| Feature | Union (A \| B) | Intersection (A & B) |
|---------|----------------|---------------------|
| **Meaning** | A OR B | A AND B |
| **Properties** | Common only | All from both |
| **Assignability** | Either type works | Must satisfy both |
| **Use Case** | Flexible input | Combine behaviors |

### 6.2 Practical Examples

```typescript
// Union - flexible
function log(message: string | Error): void {
  if (typeof message === "string") {
    console.log(message);
  } else {
    console.error(message.message);
  }
}

// Intersection - combined
type Named = { name: string };
type Aged = { age: number };
type Person = Named & Aged; // Must have BOTH

const person: Person = {
  name: "Alice",
  age: 30
  // Need both properties
};
```

---

## 🧠 Higher-Order Thinking FAQs

### FAQ 1: Union Distributivity

**Q: Why does TypeScript distribute unions over generic types (e.g., `Array<A | B>` vs `Array<A> | Array<B>`), and when does this distinction matter?**

<details>
<summary>View Detailed Answer</summary>

**Short Answer:** Union distributivity affects type relationships and assignability; understanding it prevents subtle bugs.

**Deep Explanation:**

**The Difference:**

```typescript
// Array<A | B> - array containing mixed A or B elements
type Mixed = Array<string | number>;
const mixed: Mixed = [1, "two", 3, "four"]; // ✅ OK

// Array<A> | Array<B> - array of ONLY A or ONLY B
type Separated = Array<string> | Array<number>;
const strings: Separated = ["one", "two"]; // ✅ OK
const numbers: Separated = [1, 2, 3]; // ✅ OK
// const invalid: Separated = [1, "two"]; // ❌ Error: mixed not allowed
```

**Why This Matters:**

```typescript
// Generic function with union
function process<T>(items: T[]): T {
  return items[0];
}

// With string | number
const result = process<string | number>([1, "two"]);
// result: string | number (either type)

// Real-world: API response
type ApiResponse<T> = 
  | { status: "success"; data: T }
  | { status: "error"; error: string };

// Distributed
type UserResponse = ApiResponse<User>;
// = { status: "success"; data: User }
//   | { status: "error"; error: string }

// NOT distributed (different meaning)
type MultiResponse = ApiResponse<User | Product>;
// = { status: "success"; data: User | Product }
//   | { status: "error"; error: string }
```

**Practical Impact:**

```typescript
// Problem: Want array that's either all strings or all numbers
function processHomogeneous(
  items: Array<string> | Array<number>
): void {
  // items is entirely one type
  if (typeof items[0] === "string") {
    // items: string[] (narrowed)
    items.forEach(s => console.log(s.toUpperCase()));
  }
}

// vs allowing mixed
function processMixed(
  items: Array<string | number>
): void {
  // items can be mixed
  items.forEach(item => {
    // Need to check each item
    if (typeof item === "string") {
      console.log(item.toUpperCase());
    }
  });
}
```

</details>

---

## 🎤 Senior SDE Interview Questions

### Interview Question 1: Type-Safe State Machine

**Q:** "Design a type-safe state machine for an order processing system with states: pending, processing, shipped, delivered, cancelled. Each state has different available actions and data. How would you model this with discriminated unions?"

**Key Concepts:**
- Discriminated unions
- Exhaustiveness checking
- Type narrowing
- State transitions

**Expected Answer:**

```typescript
// State definitions
interface PendingState {
  status: "pending";
  orderId: string;
  createdAt: Date;
}

interface ProcessingState {
  status: "processing";
  orderId: string;
  createdAt: Date;
  processingStarted: Date;
}

interface ShippedState {
  status: "shipped";
  orderId: string;
  createdAt: Date;
  processingStarted: Date;
  shippedAt: Date;
  trackingNumber: string;
}

interface DeliveredState {
  status: "delivered";
  orderId: string;
  createdAt: Date;
  processingStarted: Date;
  shippedAt: Date;
  deliveredAt: Date;
  trackingNumber: string;
}

interface CancelledState {
  status: "cancelled";
  orderId: string;
  createdAt: Date;
  cancelledAt: Date;
  reason: string;
}

// Union of all states
type OrderState = 
  | PendingState
  | ProcessingState
  | ShippedState
  | DeliveredState
  | CancelledState;

// Type-safe actions
type Action =
  | { type: "START_PROCESSING" }
  | { type: "SHIP"; trackingNumber: string }
  | { type: "DELIVER" }
  | { type: "CANCEL"; reason: string };

// State transition function
function transition(
  state: OrderState,
  action: Action
): OrderState {
  switch (state.status) {
    case "pending":
      if (action.type === "START_PROCESSING") {
        return {
          ...state,
          status: "processing",
          processingStarted: new Date()
        };
      }
      if (action.type === "CANCEL") {
        return {
          ...state,
          status: "cancelled",
          cancelledAt: new Date(),
          reason: action.reason
        };
      }
      throw new Error(`Invalid action ${action.type} for pending state`);
      
    case "processing":
      if (action.type === "SHIP") {
        return {
          ...state,
          status: "shipped",
          shippedAt: new Date(),
          trackingNumber: action.trackingNumber
        };
      }
      throw new Error(`Invalid action ${action.type} for processing state`);
      
    // ... other cases
      
    default:
      const _exhaustive: never = state;
      throw new Error(`Unhandled state: ${_exhaustive}`);
  }
}
```

**Follow-ups:**
1. "How would you prevent invalid state transitions (e.g., pending → delivered)?"
2. "How would you add side effects (notifications, logging)?"
3. "How would you persist and rehydrate state?"

**Green Flags:**
- Uses discriminated unions properly
- Implements exhaustiveness checking
- Considers validation of state transitions
- Discusses immutability

---

## 🎯 Key Takeaways

✅ **Union types** represent "one of several types"

✅ **Intersection types** combine multiple types

✅ **Type narrowing** restricts unions to specific types

✅ **Discriminated unions** use common property for narrowing

✅ **Type guards** enable custom narrowing logic

✅ **Unions for flexibility**, intersections for composition

✅ **Exhaustiveness checking** ensures all cases handled

---

[← Previous: Arrays & Tuples](./13_arrays_tuples.md) | [Next: Type Aliases →](./15_type_aliases.md)

**Progress**: Topic 14 of 63 | Part III: 25% Complete
