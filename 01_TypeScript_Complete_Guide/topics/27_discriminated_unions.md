# 🏷️ Discriminated Unions - Type-Safe Tagged Unions

[← Previous: Type Guards](./26_type_guards.md) | [← Back to Main](../README.md) | [Next: Declaration Files →](./28_declaration_files.md)

---

## 📝 Overview

Discriminated unions (also called tagged unions or algebraic data types) are a powerful pattern for modeling data that can be one of several variants. By using a common property (the discriminant), TypeScript can narrow types automatically, enabling exhaustive type checking and safer code.

**What You'll Learn:**
- What discriminated unions are
- Creating tagged unions with discriminants
- Exhaustiveness checking
- Pattern matching with switch
- State machines with unions
- Real-world discriminated union patterns

### 🎯 Learning Objectives

- ✅ Master discriminated union syntax
- ✅ Use discriminant properties effectively
- ✅ Implement exhaustiveness checking
- ✅ Model state machines with unions
- ✅ Apply unions in production patterns
- ✅ Write type-safe variant handlers

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced
- **Estimated Time**: 4-5 hours
- **Prerequisites**: [Union Types](./14_union_intersection.md), [Type Guards](./26_type_guards.md)
- **Practice Exercises**: 12 challenges
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [What Are Discriminated Unions?](#what-are)
2. [Creating Discriminated Unions](#creating)
3. [Exhaustiveness Checking](#exhaustiveness)
4. [State Machine Pattern](#state-machine)
5. [Result/Option Types](#result-option)
6. [Real-World Patterns](#real-world)
7. [Best Practices](#best-practices)
8. [Higher-Order FAQs](#faqs)
9. [Interview Questions](#interview-questions)

---

<a name="what-are"></a>
## 1. What Are Discriminated Unions?

### 1.1 The Pattern

```typescript
// Three ingredients for discriminated union:
// 1. Common property (discriminant)
// 2. Literal type for discriminant
// 3. Union of types

interface Circle {
  kind: "circle";    // Discriminant with literal type
  radius: number;
}

interface Square {
  kind: "square";    // Same property, different literal
  size: number;
}

interface Rectangle {
  kind: "rectangle";
  width: number;
  height: number;
}

// Union of shapes
type Shape = Circle | Square | Rectangle;

// TypeScript narrows based on discriminant
function area(shape: Shape): number {
  switch (shape.kind) {
    case "circle":
      // shape: Circle
      return Math.PI * shape.radius ** 2;
    case "square":
      // shape: Square
      return shape.size ** 2;
    case "rectangle":
      // shape: Rectangle
      return shape.width * shape.height;
  }
}
```

**Version Tracking:**
- ✅ Discriminated unions (v2.0+) - Core pattern
- 🆕 Exhaustiveness checking (v2.0+) - never type
- 🆕 Control flow improvements (continuous)

---

<a name="exhaustiveness"></a>
## 2. Exhaustiveness Checking

### 2.1 Using never for Completeness

```typescript
// Exhaustiveness check with never
type Status = "idle" | "loading" | "success" | "error";

function handleStatus(status: Status): string {
  switch (status) {
    case "idle":
      return "Not started";
    case "loading":
      return "Loading...";
    case "success":
      return "Done!";
    case "error":
      return "Failed!";
    default:
      // If all cases handled, status is never here
      const _exhaustive: never = status;
      throw new Error(`Unhandled status: ${_exhaustive}`);
  }
}

// If we add new status later:
// type Status = "idle" | "loading" | "success" | "error" | "retrying";
// Compiler will error at default case, forcing us to handle "retrying"
```

### 2.2 Helper Function

```typescript
// Reusable exhaustiveness helper
function assertNever(value: never): never {
  throw new Error(`Unexpected value: ${value}`);
}

function processShape(shape: Shape): number {
  switch (shape.kind) {
    case "circle":
      return Math.PI * shape.radius ** 2;
    case "square":
      return shape.size ** 2;
    case "rectangle":
      return shape.width * shape.height;
    default:
      return assertNever(shape); // Compile error if cases missing
  }
}
```

---

<a name="state-machine"></a>
## 3. State Machine Pattern

### 3.1 Modeling States

```typescript
// State machine with discriminated unions
interface IdleState {
  status: "idle";
}

interface LoadingState {
  status: "loading";
  progress: number;
}

interface SuccessState {
  status: "success";
  data: unknown;
}

interface ErrorState {
  status: "error";
  error: Error;
}

type State = IdleState | LoadingState | SuccessState | ErrorState;

// Type-safe state handler
function renderUI(state: State): string {
  switch (state.status) {
    case "idle":
      return "Click to start";
    case "loading":
      // state: LoadingState
      return `Loading... ${state.progress}%`;
    case "success":
      // state: SuccessState
      return `Data: ${state.data}`;
    case "error":
      // state: ErrorState
      return `Error: ${state.error.message}`;
  }
}
```

---

<a name="result-option"></a>
## 4. Result/Option Types

### 4.1 Result Type

```typescript
// Result type (functional programming pattern)
interface Success<T> {
  success: true;
  value: T;
}

interface Failure<E = Error> {
  success: false;
  error: E;
}

type Result<T, E = Error> = Success<T> | Failure<E>;

// Usage
function divide(a: number, b: number): Result<number> {
  if (b === 0) {
    return { success: false, error: new Error("Division by zero") };
  }
  return { success: true, value: a / b };
}

// Type-safe handling
const result = divide(10, 2);
if (result.success) {
  // result: Success<number>
  console.log(result.value);
} else {
  // result: Failure
  console.error(result.error);
}
```

### 4.2 Option Type

```typescript
// Option/Maybe type
interface Some<T> {
  type: "some";
  value: T;
}

interface None {
  type: "none";
}

type Option<T> = Some<T> | None;

// Safe value extraction
function unwrap<T>(option: Option<T>, defaultValue: T): T {
  return option.type === "some" ? option.value : defaultValue;
}

// Usage
const some: Option<number> = { type: "some", value: 42 };
const none: Option<number> = { type: "none" };

console.log(unwrap(some, 0)); // 42
console.log(unwrap(none, 0)); // 0
```

---

<a name="real-world"></a>
## 5. Real-World Patterns

### 5.1 API Response Types

```typescript
// Type-safe API responses
interface ApiSuccess<T> {
  status: "success";
  data: T;
  timestamp: Date;
}

interface ApiError {
  status: "error";
  code: number;
  message: string;
}

interface ApiLoading {
  status: "loading";
}

type ApiResponse<T> = ApiSuccess<T> | ApiError | ApiLoading;

// Handler
function renderResponse<T>(response: ApiResponse<T>): string {
  switch (response.status) {
    case "loading":
      return "Loading...";
    case "success":
      return `Data loaded: ${JSON.stringify(response.data)}`;
    case "error":
      return `Error ${response.code}: ${response.message}`;
  }
}
```

---

<a name="best-practices"></a>
## 6. Best Practices

### 6.1 Union Design Guidelines

```typescript
// ✅ GOOD: Use string literal discriminant
interface Success {
  status: "success"; // String literal
  data: unknown;
}

// ✅ GOOD: Consistent discriminant property name
// Use 'kind', 'type', 'status', etc. consistently

// ✅ GOOD: Always include exhaustiveness check
default:
  const _exhaustive: never = value;
  throw new Error(`Unhandled: ${_exhaustive}`);

// ❌ BAD: Boolean discriminants (less clear)
interface Response {
  isSuccess: boolean; // Not as clear as "success" | "error"
  data?: unknown;
  error?: string;
}

// ❌ BAD: No exhaustiveness check
// If you add new variant, no compile error
```

---

## 🧠 Higher-Order FAQs

### FAQ 1: Why Discriminated Unions Over Simple Unions?

**Q: Why use discriminated unions with a tag property instead of just checking properties directly? What are the practical benefits?**

<details>
<summary>View Detailed Answer</summary>

**Short Answer:** Discriminant provides single source of truth, better narrowing, exhaustiveness checking, and clearer intent.

**Deep Explanation:**

**Without Discriminant (Problematic):**

```typescript
// No discriminant - check properties
interface Success {
  data: unknown;
}

interface Error {
  error: string;
}

type Result = Success | Error;

function handle(result: Result): void {
  if ("data" in result) {
    // result: Success?
    console.log(result.data);
  } else {
    // result: Error?
    console.log(result.error);
  }
}

// Problems:
// 1. What if both have 'data' property?
// 2. No exhaustiveness checking
// 3. Unclear which variant we're handling
// 4. Easy to add overlapping properties by mistake
```

**With Discriminant (Clear):**

```typescript
// With discriminant
interface Success {
  status: "success";
  data: unknown;
}

interface Error {
  status: "error";
  error: string;
}

type Result = Success | Error;

function handle(result: Result): void {
  switch (result.status) {
    case "success":
      // result: Success (certain)
      console.log(result.data);
      break;
    case "error":
      // result: Error (certain)
      console.log(result.error);
      break;
    default:
      const _exhaustive: never = result;
      // Compile error if we miss a case
  }
}
```

**Benefits:**

1. **Single Source of Truth**: One property determines variant
2. **Better Narrowing**: TypeScript knows exact type after check
3. **Exhaustiveness**: Compiler catches missing cases
4. **Self-Documenting**: Discriminant shows all possible states
5. **Prevents Overlap**: Can't accidentally add conflicting properties
6. **IDE Support**: Better autocomplete and suggestions

**Real-World Impact:**

```typescript
// Redux actions without discriminant (bad)
type Action = 
  | { user: User }
  | { count: number }
  | { message: string };

function reducer(state: State, action: Action): State {
  if ("user" in action) {
    // Fragile - what if we add 'user' to other actions?
  }
}

// Redux actions with discriminant (good)
type Action = 
  | { type: "SET_USER"; user: User }
  | { type: "INCREMENT"; count: number }
  | { type: "SET_MESSAGE"; message: string };

function reducer(state: State, action: Action): State {
  switch (action.type) {
    case "SET_USER":
      // action: { type: "SET_USER"; user: User }
      return { ...state, user: action.user };
    case "INCREMENT":
      return { ...state, count: state.count + action.count };
    case "SET_MESSAGE":
      return { ...state, message: action.message };
    default:
      const _exhaustive: never = action;
      return state;
  }
}
```

**Best Practice**: Always use discriminated unions for variant types in production code.

</details>

---

## 🎯 Key Takeaways

✅ **Discriminated unions** use common property for type narrowing

✅ **Discriminant** should be string literal type

✅ **switch statements** work perfectly with discriminants

✅ **Exhaustiveness checking** with never catches missing cases

✅ **State machines** are natural fit for discriminated unions

✅ **Result/Option types** model success/failure elegantly

✅ **Use discriminants** over property checking for clarity

---

[← Previous: Type Guards](./26_type_guards.md) | [Next: Declaration Files →](./28_declaration_files.md)

**Progress**: Topic 27 of 63 | Part IV: 70% Complete
