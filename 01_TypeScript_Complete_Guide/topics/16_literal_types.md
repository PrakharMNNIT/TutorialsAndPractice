# 🔤 Literal Types - Precise Type Constraints

[← Previous: Type Aliases](./15_type_aliases.md) | [← Back to Main](../README.md) | [Next: Enums →](./17_enums.md)

---

## 📝 Overview

Literal types allow you to specify exact values a variable can hold, going beyond general types like `string` or `number`. They enable precise type constraints, better autocomplete, and catch more errors at compile-time. This guide covers string literals, numeric literals, boolean literals, and template literal types.

**What You'll Learn:**
- String, number, and boolean literal types
- Combining literals with unions
- Template literal types (v4.1+)
- as const assertions
- Literal type widening and narrowing
- Real-world literal type patterns

### 🎯 Learning Objectives

- ✅ Master string literal types
- ✅ Use numeric literal types
- ✅ Understand boolean literal types  
- ✅ Create unions of literals
- ✅ Use template literal types
- ✅ Control type widening with as const
- ✅ Apply literal types in real scenarios

### 📊 Section Info

- **Difficulty**: ⭐⭐ Intermediate
- **Estimated Time**: 3-4 hours
- **Prerequisites**: [Basic Types](./09_basic_types.md), [Union Types](./14_union_intersection.md)
- **Practice Exercises**: 10 challenges
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [String Literal Types](#string-literals)
2. [Numeric Literal Types](#numeric-literals)
3. [Boolean Literal Types](#boolean-literals)
4. [Union of Literals](#union-literals)
5. [Template Literal Types](#template-literals)
6. [Type Widening Control](#type-widening)
7. [Real-World Patterns](#real-world-patterns)
8. [Best Practices](#best-practices)
9. [Higher-Order FAQs](#faqs)
10. [Interview Questions](#interview-questions)

---

<a name="string-literals"></a>
## 1. String Literal Types

### 1.1 Basic String Literals

```typescript
// String literal type - exact value
let status: "pending" = "pending";
// status = "approved"; // ❌ Error: Type '"approved"' is not assignable

// Union of string literals
type Status = "pending" | "approved" | "rejected";

let orderStatus: Status = "pending"; // ✅ OK
orderStatus = "approved"; // ✅ OK
// orderStatus = "unknown"; // ❌ Error

// Function with literal type
function setStatus(status: Status): void {
  console.log(`Status: ${status}`);
}

setStatus("approved"); // ✅ OK
// setStatus("invalid"); // ❌ Error
```

**Version Tracking:**
- ✅ String literals (v1.8+) - Core feature
- 🆕 Template literal types (v4.1+) - String manipulation at type level
- 🆕 as const (v3.4+) - Literal type inference

### 1.2 HTTP Methods Example

```typescript
// Real-world: HTTP methods
type HttpMethod = "GET" | "POST" | "PUT" | "DELETE" | "PATCH";

function makeRequest(url: string, method: HttpMethod): Promise<Response> {
  return fetch(url, { method });
}

makeRequest("/api/users", "GET"); // ✅ OK
// makeRequest("/api/users", "INVALID"); // ❌ Error
```

### 1.3 String Literal Autocompletion

```typescript
// IDE provides autocomplete
type Theme = "light" | "dark" | "auto";

function setTheme(theme: Theme): void {
  // When typing theme parameter, IDE suggests: "light" | "dark" | "auto"
}

// Type narrowing with literals
function handleTheme(theme: Theme): void {
  if (theme === "light") {
    // theme: "light"
  } else if (theme === "dark") {
    // theme: "dark"
  } else {
    // theme: "auto"
  }
}
```

---

<a name="numeric-literals"></a>
## 2. Numeric Literal Types

### 2.1 Number Literals

```typescript
// Numeric literal type
let zero: 0 = 0;
// zero = 1; // ❌ Error

// Union of numeric literals
type DiceRoll = 1 | 2 | 3 | 4 | 5 | 6;

function rollDice(): DiceRoll {
  return (Math.floor(Math.random() * 6) + 1) as DiceRoll;
}

// HTTP status codes
type SuccessStatus = 200 | 201 | 204;
type ClientErrorStatus = 400 | 401 | 403 | 404;
type ServerErrorStatus = 500 | 502 | 503;

type HttpStatus = SuccessStatus | ClientErrorStatus | ServerErrorStatus;

function handleResponse(status: HttpStatus): void {
  if (status === 200) {
    console.log("OK");
  } else if (status === 404) {
    console.log("Not Found");
  }
  // ...
}
```

### 2.2 Numeric Constants

```typescript
// Port numbers
type CommonPorts = 80 | 443 | 3000 | 8080;

interface ServerConfig {
  port: CommonPorts;
  host: string;
}

const config: ServerConfig = {
  port: 3000,
  host: "localhost"
};

// Magic numbers as types
type RGB = 0 | 255;
type ColorChannel = [RGB, RGB, RGB];

const black: ColorChannel = [0, 0, 0];
const white: ColorChannel = [255, 255, 255];
```

---

<a name="boolean-literals"></a>
## 3. Boolean Literal Types

### 3.1 Boolean Literals

```typescript
// Boolean literal types
let alwaysTrue: true = true;
// alwaysTrue = false; // ❌ Error

let alwaysFalse: false = false;
// alwaysFalse = true; // ❌ Error

// Use in discriminated unions
interface SuccessResponse {
  success: true;
  data: unknown;
}

interface ErrorResponse {
  success: false;
  error: string;
}

type Response = SuccessResponse | ErrorResponse;

function handleResponse(response: Response): void {
  if (response.success) {
    // response: SuccessResponse
    console.log(response.data);
  } else {
    // response: ErrorResponse
    console.log(response.error);
  }
}
```

---

<a name="union-literals"></a>
## 4. Union of Literals

### 4.1 Mixed Literal Unions

```typescript
// Combining different literal types
type MixedLiteral = "string" | 42 | true;

let value: MixedLiteral = "string"; // ✅ OK
value = 42; // ✅ OK
value = true; // ✅ OK
// value = "other"; // ❌ Error
// value = 43; // ❌ Error

// Practical: Config options
type LogLevel = "debug" | "info" | "warn" | "error" | 0 | 1 | 2 | 3;

function log(level: LogLevel, message: string): void {
  // Accepts string or numeric level
}

log("info", "Application started");
log(1, "Application started"); // Same as "info"
```

### 4.2 Exhaustive Checks

```typescript
// Exhaustiveness checking with never
type Direction = "north" | "south" | "east" | "west";

function move(direction: Direction): void {
  switch (direction) {
    case "north":
      console.log("Moving north");
      break;
    case "south":
      console.log("Moving south");
      break;
    case "east":
      console.log("Moving east");
      break;
    case "west":
      console.log("Moving west");
      break;
    default:
      const _exhaustive: never = direction;
      throw new Error(`Unhandled direction: ${_exhaustive}`);
  }
}

// If we add "northeast" to Direction, compiler will error
// at the default case, forcing us to handle it
```

---

<a name="template-literals"></a>
## 5. Template Literal Types (v4.1+)

### 5.1 Basic Template Literals

```typescript
// Template literal types
type World = "world";
type Greeting = `hello ${World}`; // Type: "hello world"

// With unions - creates all combinations
type Color = "red" | "blue";
type Shade = "light" | "dark";
type ColorShade = `${Shade}-${Color}`;
// Type: "light-red" | "light-blue" | "dark-red" | "dark-blue"

// Practical: Event names
type EventName = "click" | "focus" | "blur";
type EventHandler = `on${Capitalize<EventName>}`;
// Type: "onClick" | "onFocus" | "onBlur"
```

### 5.2 String Manipulation Types

```typescript
// Built-in string manipulation types
type LowercaseStr = Lowercase<"HELLO">; // "hello"
type UppercaseStr = Uppercase<"hello">; // "HELLO"
type CapitalizedStr = Capitalize<"hello">; // "Hello"
type UncapitalizedStr = Uncapitalize<"Hello">; // "hello"

// Combining with template literals
type Method = "get" | "post" | "put";
type MethodName = `${Uppercase<Method>}_Request`;
// Type: "GET_Request" | "POST_Request" | "PUT_Request"

// URL patterns
type Protocol = "http" | "https";
type Domain = "example.com" | "test.com";
type URL = `${Protocol}://${Domain}`;
// Type: "http://example.com" | "http://test.com" | "https://example.com" | "https://test.com"
```

### 5.3 Advanced Template Patterns

```typescript
// CSS properties
type CSSLength = number | `${number}px` | `${number}%` | `${number}em`;

let width: CSSLength = 100;
let height: CSSLength = "50px";
let margin: CSSLength = "10%";

// Route patterns
type Route = `/users/${string}` | `/products/${number}` | "/home";

function navigate(route: Route): void {
  console.log(`Navigating to ${route}`);
}

navigate("/users/123"); // ✅ OK
navigate("/products/456"); // ✅ OK
navigate("/home"); // ✅ OK
// navigate("/invalid"); // ❌ Error
```

---

<a name="type-widening"></a>
## 6. Type Widening Control

### 6.1 let vs const

```typescript
// let - widens to general type
let status = "pending"; // Type: string

// const - keeps literal type
const status2 = "pending"; // Type: "pending"

// Using as const
let status3 = "pending" as const; // Type: "pending"
```

### 6.2 as const Assertion

```typescript
// Without as const
const colors = ["red", "green", "blue"];
// Type: string[]

// With as const
const colors2 = ["red", "green", "blue"] as const;
// Type: readonly ["red", "green", "blue"]

// Object with as const
const config = {
  apiUrl: "https://api.example.com",
  timeout: 5000,
  retryAttempts: 3
} as const;
// Type: {
//   readonly apiUrl: "https://api.example.com";
//   readonly timeout: 5000;
//   readonly retryAttempts: 3;
// }

// Extract literal types
type ApiUrl = typeof config.apiUrl; // "https://api.example.com"
```

---

<a name="real-world-patterns"></a>
## 7. Real-World Patterns

### 7.1 Route Definitions

```typescript
// Type-safe routes
const ROUTES = {
  HOME: "/",
  USERS: "/users",
  USER_DETAIL: "/users/:id",
  PRODUCTS: "/products",
  CHECKOUT: "/checkout"
} as const;

type Route = typeof ROUTES[keyof typeof ROUTES];
// Route = "/" | "/users" | "/users/:id" | "/products" | "/checkout"

function navigate(route: Route): void {
  // Type-safe navigation
  window.location.href = route;
}

navigate(ROUTES.HOME); // ✅ OK
// navigate("/invalid"); // ❌ Error
```

### 7.2 API Endpoints

```typescript
// Type-safe API client
const API = {
  USERS: "/api/users",
  POSTS: "/api/posts",
  COMMENTS: "/api/comments"
} as const;

type Endpoint = typeof API[keyof typeof API];

async function fetchData<T>(endpoint: Endpoint): Promise<T> {
  const response = await fetch(endpoint);
  return response.json();
}

// Usage
const users = await fetchData<User[]>(API.USERS); // ✅ Type-safe
```

### 7.3 Configuration Constants

```typescript
// Environment types
const ENVIRONMENTS = ["development", "staging", "production"] as const;
type Environment = typeof ENVIRONMENTS[number];
// Type: "development" | "staging" | "production"

// Feature flags
const FEATURES = {
  NEW_UI: "new-ui",
  BETA_API: "beta-api",
  ANALYTICS: "analytics"
} as const;

type FeatureFlag = typeof FEATURES[keyof typeof FEATURES];

function isFeatureEnabled(flag: FeatureFlag): boolean {
  return true; // Check implementation
}
```

---

<a name="best-practices"></a>
## 8. Best Practices

### 8.1 Literal Type Guidelines

```typescript
// ✅ GOOD: Use literals for fixed set of values
type Status = "pending" | "success" | "error";

// ✅ GOOD: Use as const for constants
const COLORS = ["red", "green", "blue"] as const;

// ✅ GOOD: Extract types from constants
type Color = typeof COLORS[number];

// ❌ BAD: Too many literals (use enum instead)
type Country = "USA" | "UK" | "Canada" | ... // 200+ countries
// ✅ BETTER: Use enum or load from config

// ✅ GOOD: Template literals for patterns
type EventName = `on${Capitalize<string>}`;

// ❌ BAD: Over-specific literals
type UserId = 1 | 2 | 3 | 4 | 5; // Use number instead
```

---

## 🧠 Higher-Order FAQs

### FAQ 1: Template Literal Type Performance

**Q: Do template literal types that generate many combinations (e.g., cross-products of large unions) impact compilation performance?**

<details>
<summary>View Detailed Answer</summary>

**Short Answer:** Yes, exponentially. Template literals with large unions can drastically slow compilation.

**Deep Explanation:**

**Combinatorial Explosion:**

```typescript
// Small - fast
type Color = "red" | "blue";
type Shade = "light" | "dark";
type ColorShade = `${Shade}-${Color}`;
// 2 × 2 = 4 combinations

// Medium - noticeable
type Size = "xs" | "sm" | "md" | "lg" | "xl";
type Variant = "primary" | "secondary" | "success" | "danger";
type ClassName = `${Size}-${Variant}`;
// 5 × 4 = 20 combinations

// Large - SLOW!
type A = "a1" | "a2" | "a3" | "a4" | "a5";
type B = "b1" | "b2" | "b3" | "b4" | "b5";
type C = "c1" | "c2" | "c3" | "c4" | "c5";
type Combined = `${A}-${B}-${C}`;
// 5 × 5 × 5 = 125 combinations
```

**Performance Impact:**

| Combinations | Compile Time | IDE Responsiveness |
|--------------|--------------|-------------------|
| <10 | <0.1s | Instant |
| 10-50 | 0.1-0.5s | Fast |
| 50-200 | 0.5-2s | Noticeable lag |
| 200-1000 | 2-10s | Significant lag |
| >1000 | 10s+ | Very slow |

**Mitigation Strategies:**

```typescript
// ❌ BAD: Too many combinations
type AllClasses = `${Size}-${Variant}-${Color}-${State}`;
// Could be hundreds or thousands

// ✅ BETTER: Constrain the cross-product
type ImportantClasses = 
  | `${Size}-primary`
  | `${Variant}-bold`
  | `${Color}-bg`;

// ✅ BEST: Generate at runtime, validate at compile-time
const validClasses = new Set(["sm-primary", "lg-secondary"]);

function isValidClass(className: string): boolean {
  return validClasses.has(className);
}
```

**Real-World Scenario:**

```typescript
// CSS-in-JS library
// ❌ TOO SLOW: Generate all Tailwind classes
// type TailwindClass = `${"text" | "bg"}-${"red" | "blue" | ...}-${100 | 200 | ...}`;
// Thousands of combinations!

// ✅ SOLUTION: String validation at runtime
type CSSClass = string & { __brand: "CSSClass" }; // Nominal typing

function css(className: string): CSSClass {
  // Runtime validation
  if (!isValidTailwindClass(className)) {
    throw new Error(`Invalid class: ${className}`);
  }
  return className as CSSClass;
}
```

**Recommendation:**
- Keep template literal unions < 50 combinations
- Use runtime validation for large sets
- Consider code generation for extensive type definitions

</details>

---

## 🎤 Senior SDE Interview Questions

### Interview Question 1: Type-Safe Event System

**Q:** "Design a type-safe event emitter where event names are string literals and each event has its own payload type. How would you ensure compile-time safety for emit() and on() methods?"

**Key Concepts:**
- Literal types
- Mapped types
- Generic constraints
- Type safety

**Expected Answer:**

```typescript
// Event map
interface EventMap {
  "user:login": { userId: number; timestamp: Date };
  "user:logout": { userId: number };
  "data:updated": { key: string; value: unknown };
  "error": { message: string; code: number };
}

// Type-safe event emitter
class TypedEventEmitter<T extends Record<string, any>> {
  private listeners: {
    [K in keyof T]?: Array<(payload: T[K]) => void>;
  } = {};
  
  on<K extends keyof T>(
    event: K,
    handler: (payload: T[K]) => void
  ): void {
    if (!this.listeners[event]) {
      this.listeners[event] = [];
    }
    this.listeners[event]!.push(handler);
  }
  
  emit<K extends keyof T>(event: K, payload: T[K]): void {
    const handlers = this.listeners[event] || [];
    handlers.forEach(handler => handler(payload));
  }
}

// Usage - fully type-safe!
const emitter = new TypedEventEmitter<EventMap>();

emitter.on("user:login", (data) => {
  // data: { userId: number; timestamp: Date }
  console.log(`User ${data.userId} logged in`);
});

emitter.emit("user:login", {
  userId: 123,
  timestamp: new Date()
}); // ✅ OK

// Compile errors:
// emitter.emit("user:login", { userId: "123" }); // ❌ Wrong type
// emitter.emit("invalid:event", {}); // ❌ Unknown event
// emitter.emit("user:logout", { wrong: "payload" }); // ❌ Wrong payload
```

**Follow-ups:**
1. "How would you add once() for one-time listeners?"
2. "How would you implement removeListener() type-safely?"
3. "What about wildcard listeners (listen to all events)?"

**Green Flags:**
- Uses mapped types for listeners
- Understands generic constraints
- Considers type safety throughout
- Mentions real-world event systems (Node.js EventEmitter, DOM events)

---

## 🎯 Key Takeaways

✅ **Literal types** specify exact values, not general types

✅ **String literals** are most common (status codes, routes, etc.)

✅ **Template literals** enable string pattern types (v4.1+)

✅ **as const** preserves literal types

✅ **Union of literals** creates enum-like behavior

✅ **Literal types** enable exhaustiveness checking

✅ **Use literals** for fixed, known values

---

[← Previous: Type Aliases](./15_type_aliases.md) | [Next: Enums →](./17_enums.md)

**Progress**: Topic 16 of 63 | Part III: 50% Complete
