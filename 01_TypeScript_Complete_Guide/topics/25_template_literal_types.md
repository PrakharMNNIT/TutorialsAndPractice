# 🔤 Template Literal Types - String Pattern Types

[← Previous: Mapped Types](./24_mapped_types.md) | [← Back to Main](../README.md) | [Next: Type Guards →](./26_type_guards.md)

---

## 📝 Overview

Template literal types enable string manipulation and pattern matching at the type level—one of TypeScript's most innovative features. They let you create precise string types, validate patterns, and build sophisticated APIs with string-based type safety.

**What You'll Learn:**
- Template literal type syntax
- String manipulation utilities
- Pattern matching with templates
- Combining templates with unions
- Real-world template type patterns
- Performance implications

### 🎯 Learning Objectives

- ✅ Master template literal type syntax
- ✅ Use string manipulation utilities
- ✅ Create pattern-based types
- ✅ Combine templates with unions
- ✅ Build type-safe string APIs
- ✅ Apply templates in production code

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐⭐ Advanced
- **Estimated Time**: 4-5 hours
- **Prerequisites**: [Literal Types](./16_literal_types.md), [Conditional Types](./23_conditional_types.md)
- **Practice Exercises**: 12 challenges
- **Version**: TypeScript 5.7+ (Template literals added in v4.1)

---

## 📚 Table of Contents

1. [Template Literal Basics](#basics)
2. [String Manipulation Utilities](#string-utils)
3. [Pattern Matching](#pattern-matching)
4. [Union Expansion](#union-expansion)
5. [Practical Patterns](#practical-patterns)
6. [Performance](#performance)
7. [Best Practices](#best-practices)
8. [Higher-Order FAQs](#faqs)
9. [Interview Questions](#interview-questions)

---

<a name="basics"></a>
## 1. Template Literal Basics

### 1.1 Basic Template Syntax

```typescript
// Template literal type
type Greeting = `Hello, ${string}`;

let g1: Greeting = "Hello, World"; // ✅ OK
let g2: Greeting = "Hello, Alice"; // ✅ OK
// let g3: Greeting = "Hi, World"; // ❌ Error: doesn't match pattern

// With specific strings
type World = "world";
type Hello = `hello ${World}`; // Type: "hello world"

// Multiple interpolations
type FullName = `${string} ${string}`;
let name: FullName = "John Doe"; // ✅ OK
```

**Version Tracking:**
- 🆕 Template literal types (v4.1+) - Revolutionary feature
- 🆕 String manipulation utilities (v4.1+) - Capitalize, etc.
- 🆕 Recursive template patterns (v4.1+)

---

<a name="string-utils"></a>
## 2. String Manipulation Utilities

### 2.1 Built-in String Utilities

```typescript
// Capitalize - uppercase first letter
type Cap = Capitalize<"hello">; // "Hello"

// Uppercase - all uppercase
type Upper = Uppercase<"hello">; // "HELLO"

// Lowercase - all lowercase
type Lower = Lowercase<"WORLD">; // "world"

// Uncapitalize - lowercase first letter
type Uncap = Uncapitalize<"Hello">; // "hello"

// Combining with templates
type Method = "get" | "post";
type MethodName = `${Uppercase<Method>}Request`;
// "GETRequest" | "POSTRequest"

// Event handlers
type Events = "click" | "focus" | "blur";
type Handlers = `on${Capitalize<Events>}`;
// "onClick" | "onFocus" | "onBlur"
```

---

<a name="pattern-matching"></a>
## 3. Pattern Matching

### 3.1 Extracting from Patterns

```typescript
// Extract parts from template
type ExtractName<S> = S extends `Hello, ${infer Name}` ? Name : never;

type T1 = ExtractName<"Hello, Alice">; // "Alice"
type T2 = ExtractName<"Hello, Bob">; // "Bob"
type T3 = ExtractName<"Hi, Charlie">; // never

// Extract email parts
type GetDomain<E> = E extends `${string}@${infer Domain}` ? Domain : never;

type D1 = GetDomain<"user@example.com">; // "example.com"
type D2 = GetDomain<"admin@test.org">; // "test.org"

// Extract route parameters
type GetParam<R> = R extends `/users/${infer Id}` ? Id : never;

type P1 = GetParam<"/users/123">; // "123"
type P2 = GetParam<"/users/abc">; // "abc"
```

---

<a name="union-expansion"></a>
## 4. Union Expansion

### 4.1 Cross-Product with Unions

```typescript
// Template with unions creates all combinations
type Size = "small" | "medium" | "large";
type Color = "red" | "blue";

type SizedColor = `${Size}-${Color}`;
// "small-red" | "small-blue" | "medium-red" | "medium-blue" | "large-red" | "large-blue"

// CSS class names
type Align = "left" | "center" | "right";
type AlignClass = `text-${Align}`;
// "text-left" | "text-center" | "text-right"

// HTTP methods + paths
type Method = "GET" | "POST";
type Path = "/users" | "/products";
type Route = `${Method} ${Path}`;
// "GET /users" | "GET /products" | "POST /users" | "POST /products"
```

⚠️ **Warning**: Large unions cause exponential growth!

| Union Size | Combinations | Compile Time |
|------------|--------------|--------------|
| 2 × 2 | 4 | Fast |
| 5 × 5 | 25 | OK |
| 10 × 10 | 100 | Slow |
| 20 × 20 | 400 | Very Slow |

---

<a name="practical-patterns"></a>
## 5. Practical Patterns

### 5.1 Type-Safe Routes

```typescript
// Route definition
type Route = `/users/${string}` | `/products/${number}` | "/home";

function navigate(route: Route): void {
  // Type-safe navigation
}

navigate("/users/123"); // ✅ OK
navigate("/products/456"); // ✅ OK
navigate("/home"); // ✅ OK
// navigate("/invalid"); // ❌ Error

// With parameter extraction
type ExtractRouteParams<R extends string> = 
  R extends `${infer Start}:${infer Param}/${infer Rest}`
    ? { [K in Param]: string } & ExtractRouteParams<Rest>
    : R extends `${infer Start}:${infer Param}`
      ? { [K in Param]: string }
      : {};

type Params = ExtractRouteParams<"/users/:id/posts/:postId">;
// { id: string; postId: string; }
```

### 5.2 CSS-in-JS Types

```typescript
// Type-safe CSS properties
type CSSValue = number | `${number}${"px" | "%" | "em" | "rem"}`;

interface Styles {
  width: CSSValue;
  height: CSSValue;
  margin: CSSValue;
}

const styles: Styles = {
  width: 100,
  height: "50px",
  margin: "2rem"
};
```

---

<a name="best-practices"></a>
## 6. Best Practices

### 6.1 Template Type Guidelines

```typescript
// ✅ GOOD: Use for string patterns
type HttpMethod = "GET" | "POST" | "PUT" | "DELETE";

// ✅ GOOD: Constrain combinations
type EventName = `on${Capitalize<"click" | "focus">}`;

// ❌ BAD: Too many combinations
// type AllClasses = `${Size}-${Color}-${Variant}-${State}`;
// Exponential growth!

// ✅ BETTER: Use at runtime with validation
const validClasses = ["sm-primary", "lg-secondary"];
type ClassName = typeof validClasses[number];
```

---

## 🧠 Higher-Order FAQs

### FAQ 1: Template Type Performance

**Q: How do template literal types with large unions affect compilation performance? What are the practical limits?**

<details>
<summary>View Detailed Answer</summary>

**Short Answer:** Template types with unions create combinatorial explosions; keep unions small (<10 members each) or use runtime validation.

**Deep Explanation:**

**Exponential Growth:**

```typescript
// 2 × 2 = 4 (fast)
type Small = `${("a" | "b")}-${("1" | "2")}`;
// "a-1" | "a-2" | "b-1" | "b-2"

// 5 × 4 = 20 (acceptable)
type Medium = `${Size}-${Color}`;

// 10 × 10 × 5 = 500 (slow!)
type Large = `${Brand}-${Model}-${Year}`;

// 20 × 20 × 10 = 4000 (very slow!)
type TooLarge = `${A}-${B}-${C}`;
```

**Real Impact on IDE:**

| Total Combinations | Type Checking | Autocomplete | Hover Info |
|-------------------|---------------|--------------|------------|
| <50 | Instant | Instant | Instant |
| 50-200 | <1s | Laggy | Slow |
| 200-1000 | 2-5s | Very laggy | Very slow |
| >1000 | 10s+ | Unusable | Timeout |

**Mitigation:**

```typescript
// ❌ DON'T: Generate all Tailwind classes
type TailwindClass = `${
  "text" | "bg" | "border"
}-${
  "red" | "blue" | "green" | ...
}-${
  100 | 200 | 300 | ...
}`;
// Thousands of combinations!

// ✅ DO: Runtime validation with branded type
type ClassName = string & { __brand: "ClassName" };

function cn(className: string): ClassName {
  if (!isValidClassName(className)) {
    throw new Error(`Invalid class: ${className}`);
  }
  return className as ClassName;
}

// Or use const with as const
const CLASSES = {
  primary: "btn-primary",
  secondary: "btn-secondary"
} as const;

type ValidClass = typeof CLASSES[keyof typeof CLASSES];
```

**Recommendation:**
- Template unions with <10 members each: ✅ Safe
- 10-20 members: ⚠️ Test performance
- >20 members: ❌ Use runtime validation

</details>

---

## 🎯 Key Takeaways

✅ **Template literal types** enable string pattern types

✅ **String utilities** (Capitalize, etc.) transform strings at type level

✅ **Union expansion** creates all combinations

✅ **Pattern matching** extracts parts with infer

✅ **Practical for** routes, CSS, event names

✅ **Watch performance** with large unions

✅ **TypeScript 4.1+** required for templates

---

[← Previous: Mapped Types](./24_mapped_types.md) | [Next: Type Guards →](./26_type_guards.md)

**Progress**: Topic 25 of 63 | Part IV: 50% Complete
