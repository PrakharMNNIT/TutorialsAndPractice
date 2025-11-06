# 🎯 Enums in TypeScript - Type-Safe Named Constants

[← Previous: Literal Types](./16_literal_types.md) | [← Back to Main](../README.md) | [Next: Type Assertions →](./18_type_assertions.md)

---

## 📝 Overview

Enums (enumerations) allow you to define a set of named constants. TypeScript provides both numeric and string-based enums. While modern TypeScript often favors literal types, enums remain useful for certain scenarios and are common in existing codebases.

**What You'll Learn:**
- Numeric and string enums
- Const enums for optimization
- Heterogeneous enums
- Computed and constant members
- Enums vs literal types
- When to use each approach

### 🎯 Learning Objectives

- ✅ Master numeric enums
- ✅ Use string enums effectively
- ✅ Understand const enums
- ✅ Know enum vs literal type trade-offs
- ✅ Choose appropriate enum types
- ✅ Optimize with const enums
- ✅ Avoid enum pitfalls

### 📊 Section Info

- **Difficulty**: ⭐⭐ Intermediate
- **Estimated Time**: 3-4 hours
- **Prerequisites**: [Basic Types](./09_basic_types.md), [Literal Types](./16_literal_types.md)
- **Practice Exercises**: 10 challenges
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [Numeric Enums](#numeric-enums)
2. [String Enums](#string-enums)
3. [Heterogeneous Enums](#heterogeneous-enums)
4. [Const Enums](#const-enums)
5. [Enums at Runtime](#enums-runtime)
6. [Enums vs Literal Types](#enums-vs-literals)
7. [Best Practices](#best-practices)
8. [Higher-Order FAQs](#faqs)
9. [Interview Questions](#interview-questions)

---

<a name="numeric-enums"></a>
## 1. Numeric Enums

### 1.1 Basic Numeric Enums

```typescript
// Numeric enum - auto-increments from 0
enum Direction {
  Up,    // 0
  Down,  // 1
  Left,  // 2
  Right  // 3
}

let dir: Direction = Direction.Up;
console.log(dir); // 0

// Custom start value
enum Status {
  Pending = 1,
  Approved = 2,
  Rejected = 3
}

console.log(Status.Pending); // 1
```

**Version Tracking:**
- ✅ Numeric enums (v1.0+) - Core feature
- ✅ String enums (v2.4+) - String-based enums
- 🆕 Const enums (v1.4+) - Inlined for performance
- ⚠️ Const enum deprecation discussions (v5.0+) - May change

### 1.2 Reverse Mapping

```typescript
// Numeric enums have reverse mapping
enum Color {
  Red,
  Green,
  Blue
}

console.log(Color.Red); // 0
console.log(Color[0]); // "Red" (reverse mapping)

// Generated JavaScript:
// var Color;
// (function (Color) {
//     Color[Color["Red"] = 0] = "Red";
//     Color[Color["Green"] = 1] = "Green";
//     Color[Color["Blue"] = 2] = "Blue";
// })(Color || (Color = {}));
```

### 1.3 Computed Members

```typescript
// Computed enum members
enum FileAccess {
  None = 0,
  Read = 1 << 0,    // 1
  Write = 1 << 1,   // 2
  Execute = 1 << 2, // 4
  
  // Computed from other members
  ReadWrite = Read | Write,           // 3
  ReadExecute = Read | Execute,       // 5
  All = Read | Write | Execute        // 7
}

const access: FileAccess = FileAccess.ReadWrite;
console.log(access); // 3
```

---

<a name="string-enums"></a>
## 2. String Enums

### 2.1 Basic String Enums

```typescript
// String enum - explicit string values
enum Direction {
  Up = "UP",
  Down = "DOWN",
  Left = "LEFT",
  Right = "RIGHT"
}

let dir: Direction = Direction.Up;
console.log(dir); // "UP"

// No reverse mapping for string enums
console.log(Direction["UP"]); // undefined

// Serialization-friendly
const json = JSON.stringify({ direction: Direction.Up });
// {"direction":"UP"}
```

### 2.2 String Enum Patterns

```typescript
// HTTP Status codes
enum HttpStatus {
  OK = "200",
  Created = "201",
  BadRequest = "400",
  Unauthorized = "401",
  NotFound = "404",
  ServerError = "500"
}

// Log levels
enum LogLevel {
  Debug = "DEBUG",
  Info = "INFO",
  Warn = "WARN",
  Error = "ERROR"
}

function log(level: LogLevel, message: string): void {
  console.log(`[${level}] ${message}`);
}

log(LogLevel.Info, "Application started");
```

---

<a name="const-enums"></a>
## 3. Const Enums

### 3.1 Const Enum Optimization

```typescript
// Const enum - inlined at compile time
const enum Direction {
  Up,
  Down,
  Left,
  Right
}

let dir = Direction.Up;

// Compiled JavaScript:
// let dir = 0; (inlined, no enum object created)

// Regular enum JavaScript:
// var Direction;
// (function (Direction) { ... })(Direction || (Direction = {}));
// let dir = Direction.Up;
```

### 3.2 Performance Benefits

```typescript
// Const enum eliminates runtime object
const enum Color {
  Red = "RED",
  Green = "GREEN",
  Blue = "BLUE"
}

function setColor(color: Color): void {
  console.log(color);
}

setColor(Color.Red);

// Compiled to:
// function setColor(color) {
//     console.log(color);
// }
// setColor("RED"); // Inlined!
```

### 3.3 Const Enum Limitations

```typescript
// ❌ Cannot use const enum with preserveConstEnums
// ❌ No reverse mapping
// ❌ Cannot compute members from other enums

const enum Flags {
  None = 0,
  Read = 1,
  Write = 2,
  // All = Flags.Read | Flags.Write // ❌ Error: computed value
}
```

---

<a name="enums-vs-literals"></a>
## 4. Enums vs Literal Types

### 4.1 Comparison

```typescript
// Enum approach
enum Status {
  Pending = "PENDING",
  Approved = "APPROVED",
  Rejected = "REJECTED"
}

function processEnum(status: Status): void {
  console.log(status);
}

processEnum(Status.Pending); // Must use enum member

// Literal type approach
type StatusLiteral = "pending" | "approved" | "rejected";

function processLiteral(status: StatusLiteral): void {
  console.log(status);
}

processLiteral("pending"); // Can use string directly
```

### 4.2 Trade-offs

| Feature | Enum | Literal Type |
|---------|------|--------------|
| **Runtime object** | Yes | No |
| **Reverse mapping** | Yes (numeric) | No |
| **Namespace** | Yes | No |
| **Direct strings** | No | Yes |
| **Serialization** | Values | Values |
| **Bundle size** | Larger | Smaller |
| **Refactoring** | Change once | Change everywhere |

---

<a name="best-practices"></a>
## 5. Best Practices

### 5.1 When to Use Enums

```typescript
// ✅ Use enum when you need namespace
enum Permission {
  Read = 1 << 0,
  Write = 1 << 1,
  Execute = 1 << 2
}

if (user.permissions & Permission.Write) {
  // Has write permission
}

// ✅ Use enum for bit flags
enum Features {
  None = 0,
  DarkMode = 1 << 0,
  Notifications = 1 << 1,
  Analytics = 1 << 2,
  All = DarkMode | Notifications | Analytics
}

// ❌ DON'T use enum for simple string unions
enum Theme { Light = "light", Dark = "dark" }
// ✅ BETTER: Use literal type
type Theme = "light" | "dark";
```

---

## 🧠 Higher-Order FAQs

### FAQ 1: Enum Tree-Shaking

**Q: Do enums affect bundle size and tree-shaking? How do const enums help, and what are the trade-offs?**

<details>
<summary>View Detailed Answer</summary>

**Short Answer:** Regular enums add runtime code and can't be tree-shaken; const enums inline values but have limitations.

**Deep Explanation:**

**Regular Enum Bundle Impact:**

```typescript
// Source
enum Status {
  Pending = "PENDING",
  Approved = "APPROVED",
  Rejected = "REJECTED"
}

export function handle(status: Status) { }

// Compiled (adds ~150 bytes)
var Status;
(function (Status) {
  Status["Pending"] = "PENDING";
  Status["Approved"] = "APPROVED";
  Status["Rejected"] = "REJECTED";
})(Status || (Status = {}));
```

**Const Enum - Zero Runtime:**

```typescript
// Source
const enum Status {
  Pending = "PENDING",
  Approved = "APPROVED",
  Rejected = "REJECTED"
}

export function handle(status: Status) {
  if (status === Status.Pending) {
    // ...
  }
}

// Compiled (inlined, no enum object)
export function handle(status) {
  if (status === "PENDING") {
    // ...
  }
}
```

**Trade-offs:**

| Aspect | Regular Enum | Const Enum | Literal Type |
|--------|-------------|------------|--------------|
| Bundle size | +150-500 bytes | 0 bytes | 0 bytes |
| Tree-shaking | No (always included) | N/A (inlined) | N/A |
| Runtime access | Yes | No | No |
| Reverse mapping | Yes | No | No |
| Debugging | Shows enum name | Shows value | Shows value |
| Cross-module | Works | Requires isolatedModules:false | Works |

**Real-World Impact:**

```typescript
// Large codebase with 50 enums
// Regular enums: +7.5-25KB to bundle
// Const enums: 0KB added

// But const enums break with:
// - isolatedModules: true (needed for Babel, esbuild)
// - preserveConstEnums: true
```

**Recommendation:**

```typescript
// ✅ BEST: Use literal types (modern approach)
type Status = "PENDING" | "APPROVED" | "REJECTED";

const STATUS = {
  PENDING: "PENDING",
  APPROVED: "APPROVED",
  REJECTED: "REJECTED"
} as const;

// Get autocomplete + zero runtime + tree-shakeable
function handle(status: Status) {
  if (status === STATUS.PENDING) {
    // ...
  }
}
```

**Migration Path:**

```typescript
// From enum
enum OldStatus { Pending, Approved, Rejected }

// To literal type
type NewStatus = "pending" | "approved" | "rejected";

// Compatibility layer
function enumToLiteral(status: OldStatus): NewStatus {
  switch (status) {
    case OldStatus.Pending: return "pending";
    case OldStatus.Approved: return "approved";
    case OldStatus.Rejected: return "rejected";
  }
}
```

</details>

---

## 🎯 Key Takeaways

✅ **Numeric enums** auto-increment and have reverse mapping

✅ **String enums** use explicit string values

✅ **Const enums** inline at compile-time (zero runtime)

✅ **Enums create runtime objects** (unlike literal types)

✅ **Literal types** are often better than enums (modern approach)

✅ **Use enums** for bit flags and namespacing

✅ **Prefer literals** for simple string unions

---

[← Previous: Literal Types](./16_literal_types.md) | [Next: Type Assertions →](./18_type_assertions.md)

**Progress**: Topic 17 of 63 | Part III: 63% Complete
