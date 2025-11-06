# 🔀 Conditional Types - Type-Level Logic and Branching

[← Previous: Utility Types](./22_utility_types.md) | [← Back to Main](../README.md) | [Next: Mapped Types →](./24_mapped_types.md)

---

## 📝 Overview

Conditional types are TypeScript's most powerful type-level feature, enabling type logic that branches based on conditions—similar to ternary operators but at the type level. They're the foundation of utility types and advanced type manipulations. This guide demystifies conditional types with clear, progressive examples.

**What You'll Learn:**
- Conditional type syntax (extends ? : pattern)
- The infer keyword for type extraction
- Distributive conditional types
- Type constraints with conditionals
- Building utility types with conditionals
- Real-world conditional type patterns

### 🎯 Learning Objectives

- ✅ Master conditional type syntax
- ✅ Use infer to extract types
- ✅ Understand distributive behavior
- ✅ Create type-level logic
- ✅ Build custom utility types
- ✅ Apply conditionals in production

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐⭐ Advanced
- **Estimated Time**: 5-6 hours
- **Prerequisites**: [Advanced Generics](./21_advanced_generics.md), [Utility Types](./22_utility_types.md)
- **Practice Exercises**: 15 challenges
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [Conditional Type Basics](#basics)
2. [The infer Keyword](#infer-keyword)
3. [Distributive Conditional Types](#distributive)
4. [Non-Distributive Conditionals](#non-distributive)
5. [Nested Conditionals](#nested)
6. [Practical Patterns](#patterns)
7. [Building Utilities](#building-utilities)
8. [Performance](#performance)
9. [Best Practices](#best-practices)
10. [Higher-Order FAQs](#faqs)
11. [Interview Questions](#interview-questions)

---

<a name="basics"></a>
## 1. Conditional Type Basics

### 1.1 Syntax: T extends U ? X : Y

```typescript
// Basic conditional type
type IsString<T> = T extends string ? true : false;

type Test1 = IsString<string>; // true
type Test2 = IsString<number>; // false

// Think of it like:
// if (T extends string) {
//   return true
// } else {
//   return false
// }

// More practical example
type TypeName<T> = 
  T extends string ? "string" :
  T extends number ? "number" :
  T extends boolean ? "boolean" :
  T extends undefined ? "undefined" :
  T extends Function ? "function" :
  "object";

type T1 = TypeName<string>; // "string"
type T2 = TypeName<42>; // "number"
type T3 = TypeName<() => void>; // "function"
```

**Version Tracking:**
- ✅ Conditional types (v2.8+) - Core feature
- 🆕 infer keyword (v2.8+) - Type extraction
- 🆕 Template literal types (v4.1+) - String patterns
- 🆕 Tail-recursion optimization (v4.5+) - Performance

### 1.2 Understanding extends

```typescript
// extends checks assignability
type A = string extends string ? true : false; // true
type B = "hello" extends string ? true : false; // true (literal extends general)
type C = string extends "hello" ? true : false; // false (general doesn't extend literal)

// With objects
interface Animal {
  name: string;
}

interface Dog extends Animal {
  breed: string;
}

type Test = Dog extends Animal ? true : false; // true
```

---

<a name="infer-keyword"></a>
## 2. The infer Keyword

### 2.1 Extracting Types with infer

```typescript
// Extract array element type
type Flatten<T> = T extends Array<infer U> ? U : T;

type T1 = Flatten<number[]>; // number
type T2 = Flatten<string>; // string (not array)

// Extract function return type
type ReturnType<T> = T extends (...args: any[]) => infer R ? R : never;

type T3 = ReturnType<() => string>; // string
type T4 = ReturnType<(x: number) => boolean>; // boolean

// Extract function parameters
type Parameters<T> = T extends (...args: infer P) => any ? P : never;

type T5 = Parameters<(a: string, b: number) => void>;
// [a: string, b: number]
```

### 2.2 Multiple infer

```typescript
// Extract first and rest
type FirstAndRest<T> = 
  T extends [infer First, ...infer Rest]
    ? [First, Rest]
    : never;

type T1 = FirstAndRest<[string, number, boolean]>;
// [string, [number, boolean]]

// Extract promise value
type UnwrapPromise<T> = 
  T extends Promise<infer U> 
    ? U 
    : T;

type T2 = UnwrapPromise<Promise<string>>; // string
type T3 = UnwrapPromise<number>; // number

// Deeply unwrap nested promises
type DeepUnwrap<T> = 
  T extends Promise<infer U>
    ? DeepUnwrap<U>
    : T;

type T4 = DeepUnwrap<Promise<Promise<Promise<number>>>>;
// number
```

---

<a name="distributive"></a>
## 3. Distributive Conditional Types

### 3.1 Distribution Behavior

```typescript
// When T is union, conditional distributes
type ToArray<T> = T extends any ? T[] : never;

type T1 = ToArray<string | number>;
// Distributes to: ToArray<string> | ToArray<number>
// Result: string[] | number[]

// Not: (string | number)[]

// Extract types from union
type ExtractStrings<T> = T extends string ? T : never;

type T2 = ExtractStrings<"a" | "b" | 1 | 2>;
// Distributes over union
// Result: "a" | "b"
```

### 3.2 Distribution Examples

```typescript
// Filter union
type NonNullable<T> = T extends null | undefined ? never : T;

type T1 = NonNullable<string | null | undefined>;
// Distributes: NonNullable<string> | NonNullable<null> | NonNullable<undefined>
// Result: string | never | never → string

// Extract function types
type FunctionPropertyNames<T> = {
  [K in keyof T]: T[K] extends Function ? K : never;
}[keyof T];

interface Example {
  name: string;
  age: number;
  greet(): void;
  calculate(): number;
}

type FunctionProps = FunctionPropertyNames<Example>;
// "greet" | "calculate"
```

---

<a name="non-distributive"></a>
## 4. Non-Distributive Conditionals

### 4.1 Preventing Distribution

```typescript
// Use tuple to prevent distribution
type ToArrayNonDist<T> = [T] extends [any] ? T[] : never;

type T1 = ToArrayNonDist<string | number>;
// Does not distribute
// Result: (string | number)[] - mixed array

// Compare to distributive
type ToArrayDist<T> = T extends any ? T[] : never;
type T2 = ToArrayDist<string | number>;
// Distributes
// Result: string[] | number[] - separate arrays

// Practical: IsUnion check
type IsUnion<T> = [T] extends [infer U]
  ? U extends T
    ? [T] extends [U]
      ? false
      : true
    : false
  : false;

type T3 = IsUnion<string>; // false
type T4 = IsUnion<string | number>; // true
```

---

<a name="nested"></a>
## 5. Nested Conditionals

### 5.1 Chaining Conditions

```typescript
// Nested conditional types
type TypeDescription<T> =
  T extends string ? "text" :
  T extends number ? "numeric" :
  T extends boolean ? "boolean" :
  T extends undefined ? "undefined" :
  T extends Function ? "function" :
  T extends object ? "object" :
  "unknown";

type T1 = TypeDescription<string>; // "text"
type T2 = TypeDescription<42>; // "numeric"
type T3 = TypeDescription<{}>; // "object"

// Complex nested example
type DeepReadonly<T> = 
  T extends Function ? T :
  T extends object ? {
    readonly [P in keyof T]: DeepReadonly<T[P]>;
  } :
  T;
```

---

<a name="patterns"></a>
## 6. Practical Patterns

### 6.1 Flatten Array Types

```typescript
// Flatten one level
type Flatten<T> = T extends Array<infer U> ? U : T;

type T1 = Flatten<number[]>; // number
type T2 = Flatten<string[][]>; // string[]
type T3 = Flatten<boolean>; // boolean

// Flatten recursively
type DeepFlatten<T> = 
  T extends Array<infer U>
    ? DeepFlatten<U>
    : T;

type T4 = DeepFlatten<number[][][]>; // number
```

### 6.2 Function Composition Types

```typescript
// Compose function types
type Compose<F, G> = 
  F extends (arg: infer A) => infer B
    ? G extends (arg: B) => infer C
      ? (arg: A) => C
      : never
    : never;

type F1 = (x: number) => string;
type F2 = (x: string) => boolean;

type Composed = Compose<F1, F2>;
// (x: number) => boolean
```

---

<a name="building-utilities"></a>
## 7. Building Custom Utilities

### 7.1 Advanced Transformations

```typescript
// Make properties optional by type
type PartialByType<T, U> = {
  [P in keyof T]: T[P] extends U ? T[P] | undefined : T[P];
};

interface User {
  id: number;
  name: string;
  email: string;
  age: number;
}

type UserWithOptionalNumbers = PartialByType<User, number>;
// {
//   id: number | undefined;
//   name: string;
//   email: string;
//   age: number | undefined;
// }

// Require properties by type
type RequiredByType<T, U> = {
  [P in keyof T]-?: T[P] extends U ? T[P] : T[P] | undefined;
};
```

---

<a name="best-practices"></a>
## 8. Best Practices

### 8.1 Conditional Type Guidelines

```typescript
// ✅ GOOD: Use for type transformations
type Unwrap<T> = T extends Promise<infer U> ? U : T;

// ✅ GOOD: Extract information from types
type ElementType<T> = T extends (infer U)[] ? U : never;

// ✅ GOOD: Type-level filtering
type FunctionKeys<T> = {
  [K in keyof T]: T[K] extends Function ? K : never;
}[keyof T];

// ❌ BAD: Overly complex nested conditions
type Bad<T> = 
  T extends A ? (T extends B ? (T extends C ? D : E) : F) : G;
// Hard to understand, consider refactoring

// ✅ BETTER: Break into smaller types
type CheckA<T> = T extends A ? T : never;
type CheckB<T> = CheckA<T> extends B ? T : never;
// More readable
```

---

## 🧠 Higher-Order FAQs

### FAQ 1: Distributive vs Non-Distributive

**Q: Why do conditional types distribute over unions by default, and how does this affect real code? When is distribution helpful vs problematic?**

<details>
<summary>View Detailed Answer</summary>

**Short Answer:** Distribution applies conditional to each union member separately; helpful for filtering but can cause unexpected behavior in some cases.

**Deep Explanation:**

**Distributive Behavior:**

```typescript
// Distributes automatically
type ToArray<T> = T extends any ? T[] : never;

type Test = ToArray<string | number>;
// Distributes to: ToArray<string> | ToArray<number>
// Result: string[] | number[]

// Each union member processed separately:
// string extends any ? string[] : never → string[]
// number extends any ? number[] : never → number[]
// Final: string[] | number[]
```

**Non-Distributive (Using Tuple):**

```typescript
// Prevent distribution with tuple
type ToArrayNonDist<T> = [T] extends [any] ? T[] : never;

type Test2 = ToArrayNonDist<string | number>;
// Does not distribute
// [string | number] extends [any] ? (string | number)[] : never
// Result: (string | number)[] - mixed array!
```

**When Distribution Helps:**

```typescript
// Filter union members
type Filter<T, U> = T extends U ? T : never;

type Strings = Filter<string | number | boolean, string>;
// Distributes over union
// string extends string ? string : never → string
// number extends string ? number : never → never
// boolean extends string ? boolean : never → never
// Result: string

// Extract nullable types
type Nullable<T> = Extract<T, null | undefined>;

type Test = Nullable<string | null | number | undefined>;
// Result: null | undefined
```

**When Distribution Causes Problems:**

```typescript
// Problem: Want to check if entire type is array
type IsArray<T> = T extends any[] ? true : false;

type Test1 = IsArray<number[] | string>;
// Distributes!
// number[] extends any[] ? true : false → true
// string extends any[] ? true : false → false
// Result: true | false (not what we want!)

// Solution: Prevent distribution
type IsArrayCorrect<T> = [T] extends [any[]] ? true : false;

type Test2 = IsArrayCorrect<number[] | string>;
// [number[] | string] extends [any[]] ? true : false
// Result: false (correct!)
```

**Real-World Impact:**

```typescript
// Building type-safe event system
type EventMap = {
  "user:login": { userId: number };
  "user:logout": { userId: number };
  "data:update": { key: string; value: unknown };
};

// Want: Extract events with specific payload structure
type EventsWithUserId<M> = {
  [K in keyof M]: M[K] extends { userId: number } ? K : never;
}[keyof M];

type UserEvents = EventsWithUserId<EventMap>;
// "user:login" | "user:logout"

// Distribution is helpful here - processes each key separately
```

**Decision Guide:**

| Want | Use | Example |
|------|-----|---------|
| Filter union members | Distributive | `Extract<T, U>` |
| Transform each member | Distributive | `ToArray<T>` |
| Check whole union | Non-distributive | `[T] extends [any[]]` |
| Type-level logic per member | Distributive | Most utilities |

</details>

---

## 🎤 Senior SDE Interview Questions

### Interview Question 1: Type-Safe Route Parser

**Q:** "Design a type system for a router where route patterns like `/users/:id/posts/:postId` automatically infer parameter types. Use conditional types to extract parameter names."

**Key Concepts:**
- Template literal types
- Conditional types
- Type inference
- Recursive types

**Expected Answer:**

```typescript
// Extract params from route pattern
type ExtractParam<Path extends string> = 
  Path extends `:${infer Param}`
    ? Param
    : never;

type Test1 = ExtractParam<":id">; // "id"
type Test2 = ExtractParam<"users">; // never

// Extract all params from route
type ExtractParams<Path extends string> = 
  Path extends `${infer _Start}:${infer Param}/${infer Rest}`
    ? Param | ExtractParams<Rest>
    : Path extends `${infer _Start}:${infer Param}`
      ? Param
      : never;

type Test3 = ExtractParams<"/users/:id/posts/:postId">;
// "id" | "postId"

// Build params object type
type RouteParams<Path extends string> = {
  [K in ExtractParams<Path>]: string;
};

type UserPostParams = RouteParams<"/users/:id/posts/:postId">;
// { id: string; postId: string; }

// Type-safe router
class Router {
  get<Path extends string>(
    path: Path,
    handler: (params: RouteParams<Path>) => void
  ): void {
    // Register route
  }
}

// Usage - fully type-safe!
const router = new Router();

router.get("/users/:id/posts/:postId", (params) => {
  console.log(params.id); // ✅ Type: string
  console.log(params.postId); // ✅ Type: string
  // console.log(params.invalid); // ❌ Error
});

router.get("/static", (params) => {
  // params: {} (no parameters)
});
```

**Follow-ups:**
1. "How would you support query parameters?"
2. "How would you type path parameter constraints (numeric ids)?"
3. "What about optional route segments?"

**Green Flags:**
- Understands template literal type parsing
- Uses conditional types recursively
- Considers edge cases (no params, multiple params)
- Mentions frameworks (Express, Fastify, tRPC)

---

## 🎯 Key Takeaways

✅ **Conditional types** enable type-level branching

✅ **infer** extracts types from patterns

✅ **Distribution** applies conditional to each union member

✅ **Non-distributive** checks entire type using tuples

✅ **Nested conditionals** enable complex type logic

✅ **Conditional types** power utility types

✅ **TypeScript erases conditionals** at runtime (compile-time only)

---

[← Previous: Utility Types](./22_utility_types.md) | [Next: Mapped Types →](./24_mapped_types.md)

**Progress**: Topic 23 of 63 | Part IV: 30% Complete
