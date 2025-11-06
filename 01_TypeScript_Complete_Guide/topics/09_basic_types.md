# 🔤 Basic Types in TypeScript - Foundation of Type Safety

[← Previous: TypeScript Compiler](./08_typescript_compiler.md) | [← Back to Main](../README.md) | [Next: Type Annotations →](./10_type_annotations.md)

---

## 📝 Overview

TypeScript's type system starts with basic types that correspond to JavaScript's primitive and common types. Mastering these fundamentals is crucial for writing type-safe code and understanding more advanced TypeScript features.

**What You'll Learn:**
- All primitive types in TypeScript
- Array and tuple types
- Object types and interfaces basics
- Special types (any, unknown, never, void)
- Type annotations and declarations
- Type inference basics

### 🎯 Learning Objectives

- ✅ Master all primitive types (string, number, boolean, etc.)
- ✅ Use array and tuple types correctly
- ✅ Understand any, unknown, never, and void
- ✅ Write proper type annotations
- ✅ Leverage type inference
- ✅ Avoid common type mistakes
- ✅ Choose the right type for each situation

### 📊 Section Info

- **Difficulty**: ⭐ Beginner
- **Estimated Time**: 3-4 hours
- **Prerequisites**: [TypeScript Intro](./06_typescript_intro.md), [TypeScript Setup](./07_typescript_setup.md)
- **Practice Exercises**: 12 challenges
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [Primitive Types](#primitive-types)
2. [Array Types](#array-types)
3. [Tuple Types](#tuple-types)
4. [Object Types](#object-types)
5. [Special Types](#special-types)
6. [Type Inference](#type-inference)
7. [Type Annotations](#type-annotations)
8. [Best Practices](#best-practices)
9. [Higher-Order FAQs](#faqs)
10. [Interview Questions](#interview-questions)

---

<a name="primitive-types"></a>
## 1. Primitive Types

### 1.1 String Type

```typescript
// String type
let message: string = "Hello, TypeScript!";
let name: string = 'Alice';
let template: string = `Hello, ${name}`;

// ❌ Error: Type 'number' is not assignable to type 'string'
// message = 42;

// String methods are available
message.toUpperCase();
message.split(',');
```

**Version Tracking:**
- ✅ String type (v1.0+) - Core primitive
- 🆕 Template literal types (v4.1+) - Advanced string types

### 1.2 Number Type

```typescript
// Number type (all numbers are number type)
let integer: number = 42;
let float: number = 3.14159;
let hex: number = 0xFF;
let binary: number = 0b1010;
let octal: number = 0o744;
let negative: number = -273.15;

// Special number values
let infinite: number = Infinity;
let notANum: number = NaN;

// ❌ Error: Type 'string' is not assignable to type 'number'
// integer = "42";
```

**Version Tracking:**
- ✅ Number type (v1.0+) - Core primitive
- ✅ Numeric separators (v2.7+) - `1_000_000`
- 🆕 BigInt support (v3.2+) - For large integers

### 1.3 Boolean Type

```typescript
// Boolean type
let isActive: boolean = true;
let isCompleted: boolean = false;

// From expressions
let isGreater: boolean = 10 > 5;
let hasValue: boolean = !!value;

// ❌ Error: Type 'string' is not assignable to type 'boolean'
// isActive = "true";

// Truthy/falsy values don't auto-convert
let flag: boolean = 1; // ❌ Error
let flag2: boolean = Boolean(1); // ✅ OK
```

### 1.4 BigInt Type (v3.2+)

```typescript
// BigInt for very large integers
let big: bigint = 100n;
let big2: bigint = BigInt(9007199254740991);

// Operations
let sum: bigint = big + 50n;

// ❌ Cannot mix bigint and number
// let mixed = big + 50; // Error

// ✅ Must convert explicitly
let converted = big + BigInt(50);
```

### 1.5 Symbol Type

```typescript
// Symbol - unique identifiers
let sym1: symbol = Symbol("key");
let sym2: symbol = Symbol("key");

// Symbols are always unique
console.log(sym1 === sym2); // false

// Use in objects
const obj = {
  [sym1]: "value"
};

console.log(obj[sym1]); // "value"
```

### 1.6 Null and Undefined

```typescript
// Explicit null and undefined types
let n: null = null;
let u: undefined = undefined;

// With strictNullChecks: false (not recommended)
let maybeNumber: number = null; // ✅ OK

// With strictNullChecks: true (recommended)
let maybeNumber: number = null; // ❌ Error
let maybeNumber: number | null = null; // ✅ OK

// Undefined is different from null
let value: string | undefined;
value = undefined; // ✅ OK
// value = null; // ❌ Error without '| null'
```

---

<a name="array-types"></a>
## 2. Array Types

### 2.1 Array Syntax

```typescript
// Two syntaxes for arrays

// Syntax 1: Type[]
let numbers: number[] = [1, 2, 3, 4, 5];
let strings: string[] = ["hello", "world"];
let booleans: boolean[] = [true, false, true];

// Syntax 2: Array<Type>
let numbers2: Array<number> = [1, 2, 3];
let strings2: Array<string> = ["hello", "world"];

// Both are equivalent - choose one style consistently

// ❌ Type errors
// numbers.push("string"); // Error
// strings[0] = 123; // Error
```

### 2.2 Multidimensional Arrays

```typescript
// 2D array
let matrix: number[][] = [
  [1, 2, 3],
  [4, 5, 6],
  [7, 8, 9]
];

// 3D array
let cube: number[][][] = [
  [[1, 2], [3, 4]],
  [[5, 6], [7, 8]]
];

// Mixed type arrays (use union)
let mixed: (string | number)[] = [1, "two", 3, "four"];
```

### 2.3 Readonly Arrays

```typescript
// Readonly array - cannot be modified
let numbers: readonly number[] = [1, 2, 3];

// ❌ Cannot modify
// numbers.push(4); // Error: Property 'push' does not exist
// numbers[0] = 10; // Error: Index signature only permits reading

// Alternative syntax
let numbers2: ReadonlyArray<number> = [1, 2, 3];

// Can create new arrays
let doubled = numbers.map(n => n * 2); // ✅ OK
```

---

<a name="tuple-types"></a>
## 3. Tuple Types

### 3.1 Basic Tuples

```typescript
// Tuple - fixed-length array with known types
let person: [string, number] = ["Alice", 30];

// Access by index
let name: string = person[0]; // "Alice"
let age: number = person[1]; // 30

// ❌ Wrong type
// person[0] = 30; // Error
// person[1] = "thirty"; // Error

// ❌ Wrong length
// person[2] = "extra"; // Error (before TS 4.0)
```

### 3.2 Optional Tuple Elements

```typescript
// Optional elements with ?
let response: [number, string?] = [200];
let response2: [number, string?] = [200, "OK"];

// Rest elements
let tuple: [string, ...number[]] = ["first", 1, 2, 3, 4];

// Named tuple elements (v4.0+)
type Range = [start: number, end: number];
let range: Range = [0, 10];
```

### 3.3 Tuples vs Arrays

| Feature | Array | Tuple |
|---------|-------|-------|
| **Length** | Variable | Fixed |
| **Types** | Homogeneous | Heterogeneous |
| **Use Case** | Collections | Fixed structure |
| **Example** | `number[]` | `[string, number]` |

```typescript
// Array - variable length, same type
let scores: number[] = [95, 87, 91];
scores.push(88); // ✅ OK

// Tuple - fixed length, different types
let user: [string, number, boolean] = ["Alice", 30, true];
// user.push("extra"); // Works but shouldn't!

// ✅ Better: Readonly tuple
let user2: readonly [string, number, boolean] = ["Alice", 30, true];
// user2.push("extra"); // ❌ Error
```

---

<a name="object-types"></a>
## 4. Object Types

### 4.1 Inline Object Types

```typescript
// Inline object type
let user: { name: string; age: number } = {
  name: "Alice",
  age: 30
};

// Optional properties
let config: {
  host: string;
  port: number;
  ssl?: boolean; // Optional
} = {
  host: "localhost",
  port: 3000
  // ssl is optional
};

// Readonly properties
let point: {
  readonly x: number;
  readonly y: number;
} = {
  x: 10,
  y: 20
};

// point.x = 15; // ❌ Error: Cannot assign to 'x'
```

### 4.2 Interface (Preview)

```typescript
// Interface - reusable object type
interface User {
  name: string;
  age: number;
  email?: string;
}

let user: User = {
  name: "Bob",
  age: 25
};

// More on interfaces in Topic 12
```

---

<a name="special-types"></a>
## 5. Special Types

### 5.1 any Type (Escape Hatch)

```typescript
// any - opts out of type checking
let value: any = 42;
value = "string"; // ✅ OK
value = true; // ✅ OK
value = []; // ✅ OK

// No type safety
value.foo.bar.baz; // No error, but runtime crash!

// ⚠️ Use sparingly
// any defeats the purpose of TypeScript
```

### 5.2 unknown Type (Type-Safe any)

```typescript
// unknown - safer than any
let value: unknown = 42;

// ❌ Cannot use without type checking
// value.toFixed(); // Error

// ✅ Must check type first
if (typeof value === "number") {
  value.toFixed(); // ✅ OK now
}

// unknown vs any
function processAny(value: any) {
  return value.toFixed(); // No error, may crash
}

function processUnknown(value: unknown) {
  // return value.toFixed(); // ❌ Error: check type first
  
  if (typeof value === "number") {
    return value.toFixed(); // ✅ OK
  }
}
```

### 5.3 void Type

```typescript
// void - function returns nothing
function logMessage(message: string): void {
  console.log(message);
  // No return statement
}

// Can return undefined explicitly
function doNothing(): void {
  return undefined; // ✅ OK
  // return null; // ❌ Error (with strictNullChecks)
}

// Variables can be void (rarely useful)
let unusable: void = undefined;
```

### 5.4 never Type

```typescript
// never - represents values that never occur

// Function that never returns
function throwError(message: string): never {
  throw new Error(message);
  // Never reaches end
}

// Infinite loop
function infiniteLoop(): never {
  while (true) {
    // Never exits
  }
}

// Exhaustive checks
type Shape = "circle" | "square";

function getArea(shape: Shape): number {
  switch (shape) {
    case "circle":
      return Math.PI * 10 * 10;
    case "square":
      return 10 * 10;
    default:
      // If we add new shape and forget to handle it:
      const _exhaustive: never = shape;
      throw new Error(`Unhandled shape: ${_exhaustive}`);
  }
}
```

---

<a name="type-inference"></a>
## 6. Type Inference

### 6.1 Automatic Type Inference

```typescript
// TypeScript infers types automatically

// Primitives
let message = "Hello"; // inferred: string
let count = 42; // inferred: number
let isActive = true; // inferred: boolean

// Arrays
let numbers = [1, 2, 3]; // inferred: number[]
let mixed = [1, "two", true]; // inferred: (number | string | boolean)[]

// Objects
let user = {
  name: "Alice",
  age: 30
}; // inferred: { name: string; age: number }

// Functions
function add(a: number, b: number) {
  return a + b; // inferred return: number
}
```

### 6.2 Best Common Type

```typescript
// TypeScript finds best common type
let values = [1, 2, 3]; // number[]
let mixed = [1, "two"]; // (number | string)[]
let objects = [{ x: 1 }, { x: 2 }]; // { x: number }[]

// When no common type exists
class Animal {}
class Dog extends Animal {}
class Cat extends Animal {}

let pets = [new Dog(), new Cat()]; // (Dog | Cat)[]
// Not Animal[] - TypeScript doesn't infer base class

// ✅ Explicit type
let pets2: Animal[] = [new Dog(), new Cat()];
```

### 6.3 Contextual Typing

```typescript
// Type inferred from context
const numbers = [1, 2, 3];

numbers.forEach(num => {
  // num is inferred as number from array type
  console.log(num.toFixed());
});

// Event handlers
button.addEventListener("click", (event) => {
  // event inferred as MouseEvent
  console.log(event.clientX);
});

// Promise chain
Promise.resolve(42).then(value => {
  // value inferred as number
  return value * 2;
});
```

---

<a name="type-annotations"></a>
## 7. Type Annotations

### 7.1 When to Annotate

```typescript
// ❌ Unnecessary annotation (obvious from value)
let message: string = "Hello";

// ✅ Annotation needed (not initialized)
let message: string;
message = "Hello";

// ✅ Annotation for clarity (complex type)
let callback: (x: number) => number;
callback = (x) => x * 2;

// ✅ Function parameters always need types
function greet(name: string) {
  return `Hello, ${name}`;
}
```

### 7.2 Annotation Syntax

```typescript
// Variables
let name: string;
let age: number;
let isActive: boolean;

// Arrays
let numbers: number[];
let matrix: number[][];

// Objects
let user: { name: string; age: number };

// Functions
let add: (a: number, b: number) => number;

// Optional
let optional: string | undefined;

// Multiple types (union)
let id: number | string;
```

---

<a name="best-practices"></a>
## 8. Best Practices

### 8.1 Type Annotation Guidelines

```typescript
// ✅ GOOD: Let TypeScript infer when obvious
const count = 5; // inferred: number
const name = "Alice"; // inferred: string

// ✅ GOOD: Annotate function parameters
function greet(name: string): string {
  return `Hello, ${name}`;
}

// ✅ GOOD: Annotate when type isn't obvious
let data: ApiResponse;
let handler: EventHandler;

// ❌ BAD: Over-annotating
const message: string = "hello"; // Unnecessary

// ❌ BAD: Using 'any' unnecessarily
function process(data: any) { } // Defeats type safety

// ✅ BETTER: Use unknown or specific type
function process(data: unknown) { }
```

---

## 🧠 Higher-Order Thinking FAQs

### FAQ 1: any vs unknown

**Q: If 'unknown' is the type-safe version of 'any', why does 'any' still exist? When would you deliberately choose 'any' over 'unknown'?**

<details>
<summary>View Detailed Answer</summary>

**Short Answer:** `any` exists for gradual migration and escape hatches; `unknown` should be default for truly unknown data.

**Deep Explanation:**

**Historical Context:**
- `any` existed from TypeScript 1.0 (2012)
- `unknown` added in TypeScript 3.0 (2018)
- `any` kept for backward compatibility

**Legitimate 'any' Use Cases:**

1. **Prototyping/Rapid Development**
```typescript
// Quick prototype - come back to add types later
let data: any = fetchLegacyAPI();
```

2. **Migrating JavaScript to TypeScript**
```typescript
// Gradual migration - mark as any temporarily
const legacyCode: any = require('./old-js-module');
```

3. **Truly Dynamic Code**
```typescript
// JSON.parse returns any (can't know structure)
const data = JSON.parse(jsonString); // any

// But should immediately validate:
interface Expected {
  name: string;
}

function isExpected(data: any): data is Expected {
  return typeof data.name === 'string';
}

if (isExpected(data)) {
  // data is Expected here
}
```

**Why 'unknown' is Better:**

```typescript
// any - dangerous
function processAny(value: any) {
  return value.toFixed(); // No error, crashes if value isn't number
}

// unknown - safe
function processUnknown(value: unknown) {
  // return value.toFixed(); // ❌ Error: check type first
  
  if (typeof value === 'number') {
    return value.toFixed(); // ✅ OK after check
  }
  throw new Error('Expected number');
}
```

**Decision Matrix:**

| Scenario | Use `any` | Use `unknown` |
|----------|-----------|---------------|
| Third-party untyped lib | Maybe | Prefer unknown |
| Migrating JS code | Yes | No |
| Quick prototype | Yes | No |
| Unknown user input | No | Yes |
| JSON parsing | No | Yes |
| Escape hatch (last resort) | Yes | No |

**Best Practice:** Start with `unknown`, fall back to `any` only when necessary.

</details>

---

### FAQ 2: null vs undefined

**Q: TypeScript has both null and undefined types. How should developers decide which to use, and why does JavaScript have both?**

<details>
<summary>View Detailed Answer</summary>

**Short Answer:** Historical JavaScript quirk; use `undefined` for "not yet set", `null` for "explicitly empty".

**Deep Explanation:**

**JavaScript History:**
- `undefined`: Added in JavaScript 1.0 (1995)
- `null`: Inherited from Java influence
- Both exist for backward compatibility

**Semantic Differences:**

```typescript
// undefined - "not yet assigned" or "doesn't exist"
let uninit: string | undefined;
console.log(uninit); // undefined

function optional(param?: string) {
  console.log(param); // undefined if not provided
}

// null - "explicitly no value"
let empty: string | null = null;
```

**TypeScript strictNullChecks:**

```typescript
// strictNullChecks: false (old, not recommended)
let value: string = null; // ✅ OK
let value2: string = undefined; // ✅ OK

// strictNullChecks: true (recommended)
let value: string = null; // ❌ Error
let value2: string = undefined; // ❌ Error

// Must be explicit
let value3: string | null = null; // ✅ OK
let value4: string | undefined = undefined; // ✅ OK
```

**Best Practices:**

| Scenario | Use | Reason |
|----------|-----|--------|
| Optional function param | `undefined` | JavaScript default |
| API returns no data | `null` | Explicit absence |
| Uninitialized variable | `undefined` | JavaScript default |
| Reset object property | `null` | Intentional clearing |
| DOM queries (no match) | `null` | Browser API standard |

**Practical Pattern:**
```typescript
//  ✅ GOOD: Use undefined for optional
interface User {
  name: string;
  email?: string; // undefined if not provided
}

// ✅ GOOD: Use null for "no value" state
interface Response {
  data: Data | null; // null when no data
  error: Error | null; // null when no error
}

// ✅ BETTER: Use optional chaining
const email = user?.profile?.email;
const name = data ?? "default";
```

</details>

---

### FAQ 3: Type Widening and Narrowing

**Q: What is "type widening", and why does TypeScript sometimes infer a more general type than expected?**

<details>
<summary>View Detailed Answer</summary>

**Short Answer:** Type widening converts literal types to general types for flexibility; narrowing restricts types for safety.

**Deep Explanation:**

**Type Widening:**

```typescript
// Literal widened to general type
let x = 5; // Type: number (not literal 5)
let y = "hello"; // Type: string (not literal "hello")

// Why? Flexibility for reassignment
x = 10; // ✅ OK
y = "world"; // ✅ OK

// Prevent widening with const
const x2 = 5; // Type: 5 (literal)
const y2 = "hello"; // Type: "hello" (literal)
```

**When Widening Happens:**

| Context | Widened? | Example |
|---------|----------|---------|
| `let` variable | Yes | `let x = 5` → `number` |
| `const` variable | No | `const x = 5` → `5` |
| Function return | Yes | `return 5` → `number` |
| Array element | Yes | `[5]` → `number[]` |

**Type Narrowing:**

```typescript
// Narrowing through type guards
function process(value: string | number) {
  // value: string | number
  
  if (typeof value === "string") {
    // value: string (narrowed)
    console.log(value.toUpperCase());
  } else {
    // value: number (narrowed)
    console.log(value.toFixed());
  }
}
```

**Control Widening:**

```typescript
// Problem: Want literal type
let status = "pending"; // Type: string
type Status = "pending" | "approved";
let myStatus: Status = status; // ❌ Error: string not assignable

// Solution 1: const
const status2 = "pending"; // Type: "pending"
let myStatus2: Status = status2; // ✅ OK

// Solution 2: as const
let status3 = "pending" as const; // Type: "pending"

// Solution 3: Explicit type
let status4: Status = "pending";
```

**Production Impact:**
- Widening enables flexibility
- Narrowing enables safety
- Use `as const` for literal types
- Use type guards for narrowing

</details>

---

## 🎤 Senior SDE Interview Questions

### Interview Question 1: Type System Design

**Q:** "Design a type-safe configuration system for a microservices platform where services have different config shapes but share common fields. How would you model this in TypeScript?"

**Key Concepts:**
- Type composition
- Generic types
- Discrimination
- Type safety at scale

**Expected Answer:**

```typescript
// Base configuration
interface BaseConfig {
  serviceName: string;
  version: string;
  port: number;
  env: "dev" | "staging" | "prod";
}

// Service-specific configs
interface HttpServiceConfig extends BaseConfig {
  type: "http";
  cors: {
    enabled: boolean;
    origins: string[];
  };
}

interface DatabaseServiceConfig extends BaseConfig {
  type: "database";
  connection: {
    host: string;
    database: string;
  };
}

// Discriminated union
type ServiceConfig = HttpServiceConfig | DatabaseServiceConfig;

// Type-safe accessor
function getPort(config: ServiceConfig): number {
  return config.port; // ✅ Common field always accessible
}

// Type narrowing
function setup(config: ServiceConfig) {
  if (config.type === "http") {
    // config is HttpServiceConfig here
    console.log(config.cors.enabled);
  } else {
    // config is DatabaseServiceConfig here
    console.log(config.connection.host);
  }
}
```

**Follow-ups:**
1. "How would you validate runtime config matches the types?"
2. "How would you handle config inheritance across environments?"
3. "What if services need optional shared fields?"

**Green Flags:**
- Uses discriminated unions
- Considers runtime validation
- Mentions environment-specific overrides
- Discusses type guards

---

## 🎯 Key Takeaways

✅ **Primitive types** (string, number, boolean) are building blocks

✅ **Arrays** hold collections of same-type values

✅ **Tuples** represent fixed-length, heterogeneous structures

✅ **any** escapes type checking (use sparingly)

✅ **unknown** is type-safe any (prefer over any)

✅ **void** represents no return value

✅ **never** represents impossible values

✅ **Type inference** works automatically in most cases

---

[← Previous: TypeScript Compiler](./08_typescript_compiler.md) | [Next: Type Annotations →](./10_type_annotations.md)

**Progress**: Topic 9 of 63 | Part II: 43% Complete
