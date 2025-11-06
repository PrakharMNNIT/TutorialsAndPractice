# 📦 Objects & Interfaces in TypeScript - Structuring Complex Types

[← Previous: Functions](./11_functions.md) | [← Back to Main](../README.md) | [Next: Arrays & Tuples →](./13_arrays_tuples.md)

---

## 📝 Overview

Objects and interfaces are fundamental to TypeScript's type system. They allow you to define complex data structures with type safety, enabling better code organization, documentation, and refactoring support. This comprehensive guide covers everything about object types and interfaces.

**What You'll Learn:**
- Object type syntax and inline types
- Interface declaration and usage
- Optional and readonly properties
- Index signatures
- Extending interfaces
- Interface vs type aliases
- Advanced interface patterns

### 🎯 Learning Objectives

- ✅ Define object types inline and with interfaces
- ✅ Use optional and readonly properties
- ✅ Extend and compose interfaces
- ✅ Work with index signatures
- ✅ Understand interface vs type differences
- ✅ Apply interface best practices
- ✅ Design type-safe APIs with interfaces

### 📊 Section Info

- **Difficulty**: ⭐⭐ Intermediate
- **Estimated Time**: 4-5 hours
- **Prerequisites**: [Basic Types](./09_basic_types.md), [Functions](./11_functions.md)
- **Practice Exercises**: 12 challenges
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [Object Types](#object-types)
2. [Interface Basics](#interface-basics)
3. [Optional Properties](#optional-properties)
4. [Readonly Properties](#readonly-properties)
5. [Index Signatures](#index-signatures)
6. [Extending Interfaces](#extending-interfaces)
7. [Interface Merging](#interface-merging)
8. [Interface vs Type Alias](#interface-vs-type)
9. [Advanced Patterns](#advanced-patterns)
10. [Best Practices](#best-practices)
11. [Higher-Order FAQs](#faqs)
12. [Interview Questions](#interview-questions)

---

<a name="object-types"></a>
## 1. Object Types

### 1.1 Inline Object Types

```typescript
// Inline object type
let user: { name: string; age: number } = {
  name: "Alice",
  age: 30
};

// Nested objects
let person: {
  name: string;
  address: {
    street: string;
    city: string;
  };
} = {
  name: "Bob",
  address: {
    street: "123 Main St",
    city: "NYC"
  }
};

// Object with methods
let calculator: {
  add: (a: number, b: number) => number;
  subtract: (a: number, b: number) => number;
} = {
  add: (a, b) => a + b,
  subtract: (a, b) => a - b
};
```

### 1.2 Object Type Syntax

```typescript
// Function parameter with object type
function displayUser(user: { name: string; age: number }): void {
  console.log(`${user.name} is ${user.age} years old`);
}

// Function returning object type
function createPoint(x: number, y: number): { x: number; y: number } {
  return { x, y };
}

// Array of objects
let users: { name: string; age: number }[] = [
  { name: "Alice", age: 30 },
  { name: "Bob", age: 25 }
];
```

---

<a name="interface-basics"></a>
## 2. Interface Basics

### 2.1 Defining Interfaces

```typescript
// Basic interface
interface User {
  name: string;
  age: number;
  email: string;
}

// Using interface
const user: User = {
  name: "Alice",
  age: 30,
  email: "alice@example.com"
};

// Interface with methods
interface Calculator {
  add(a: number, b: number): number;
  subtract(a: number, b: number): number;
}

const calc: Calculator = {
  add: (a, b) => a + b,
  subtract: (a, b) => a - b
};
```

### 2.2 Interface for Functions

```typescript
// Interface describing function type
interface MathOperation {
  (a: number, b: number): number;
}

const add: MathOperation = (a, b) => a + b;
const multiply: MathOperation = (a, b) => a * b;

// Interface with call signature and properties
interface Counter {
  (start: number): string;
  interval: number;
  reset(): void;
}

// Implementing
const counter: Counter = ((start: number) => {
  return `Count: ${start}`;
}) as Counter;
counter.interval = 1000;
counter.reset = () => console.log("Reset");
```

---

<a name="optional-properties"></a>
## 3. Optional Properties

### 3.1 Optional Property Syntax

```typescript
// Optional properties with ?
interface User {
  name: string;
  age: number;
  email?: string; // Optional
  phone?: string; // Optional
}

// Valid - optional properties can be omitted
const user1: User = {
  name: "Alice",
  age: 30
};

const user2: User = {
  name: "Bob",
  age: 25,
  email: "bob@example.com"
};
```

### 3.2 Optional vs Undefined

```typescript
// Optional property
interface Config {
  port?: number;
}

const config1: Config = {}; // ✅ OK
const config2: Config = { port: 3000 }; // ✅ OK
const config3: Config = { port: undefined }; // ✅ OK

// Explicit undefined
interface Config2 {
  port: number | undefined;
}

const config4: Config2 = {}; // ❌ Error: port is missing
const config5: Config2 = { port: undefined }; // ✅ OK
const config6: Config2 = { port: 3000 }; // ✅ OK
```

---

<a name="readonly-properties"></a>
## 4. Readonly Properties

### 4.1 Readonly Modifier

```typescript
// Readonly properties
interface Point {
  readonly x: number;
  readonly y: number;
}

const point: Point = { x: 10, y: 20 };

// point.x = 15; // ❌ Error: Cannot assign to 'x' because it is read-only

// Readonly works with objects (shallow)
interface User {
  readonly id: number;
  name: string;
  readonly created: Date;
}

const user: User = {
  id: 1,
  name: "Alice",
  created: new Date()
};

user.name = "Bob"; // ✅ OK (not readonly)
// user.id = 2; // ❌ Error: Cannot assign to 'id'
// user.created = new Date(); // ❌ Error: Cannot assign to 'created'
```

### 4.2 Readonly Utility Type

```typescript
// Readonly utility makes all properties readonly
interface User {
  name: string;
  age: number;
}

type ReadonlyUser = Readonly<User>;
// Equivalent to:
// interface ReadonlyUser {
//   readonly name: string;
//   readonly age: number;
// }

const user: ReadonlyUser = { name: "Alice", age: 30 };
// user.name = "Bob"; // ❌ Error
```

---

<a name="index-signatures"></a>
## 5. Index Signatures

### 5.1 String Index Signature

```typescript
// Index signature for dynamic properties
interface StringDictionary {
  [key: string]: string;
}

const dict: StringDictionary = {
  name: "Alice",
  city: "NYC",
  country: "USA"
};

dict.email = "alice@example.com"; // ✅ OK
// dict.age = 30; // ❌ Error: Type 'number' is not assignable

// With known properties
interface Config {
  host: string;
  port: number;
  [key: string]: string | number; // Index signature
}

const config: Config = {
  host: "localhost",
  port: 3000,
  timeout: 5000,
  retries: 3
};
```

### 5.2 Number Index Signature

```typescript
// Number index signature (arrays)
interface NumberArray {
  [index: number]: number;
}

const arr: NumberArray = [1, 2, 3, 4, 5];
console.log(arr[0]); // 1

// Mixed index signatures
interface Mixed {
  [key: string]: string | number;
  [index: number]: number; // More specific for number keys
}
```

---

<a name="extending-interfaces"></a>
## 6. Extending Interfaces

### 6.1 Single Inheritance

```typescript
// Base interface
interface Animal {
  name: string;
  age: number;
}

// Extend base interface
interface Dog extends Animal {
  breed: string;
  bark(): void;
}

const dog: Dog = {
  name: "Rex",
  age: 5,
  breed: "Labrador",
  bark() {
    console.log("Woof!");
  }
};
```

### 6.2 Multiple Inheritance

```typescript
// Multiple interfaces
interface Named {
  name: string;
}

interface Aged {
  age: number;
}

interface Contactable {
  email: string;
  phone?: string;
}

// Extend multiple interfaces
interface User extends Named, Aged, Contactable {
  id: number;
}

const user: User = {
  id: 1,
  name: "Alice",
  age: 30,
  email: "alice@example.com"
};
```

---

<a name="interface-merging"></a>
## 7. Interface Merging (Declaration Merging)

### 7.1 Automatic Merging

```typescript
// First declaration
interface User {
  name: string;
}

// Second declaration (merges with first)
interface User {
  age: number;
}

// Merged result: User has both name and age
const user: User = {
  name: "Alice",
  age: 30
};
```

### 7.2 Module Augmentation

```typescript
// Extending third-party interfaces
declare module 'express' {
  interface Request {
    user?: {
      id: number;
      name: string;
    };
  }
}

// Now Request has user property
app.get('/profile', (req, res) => {
  if (req.user) {
    res.json({ name: req.user.name });
  }
});
```

---

<a name="interface-vs-type"></a>
## 8. Interface vs Type Alias

### 8.1 Similarities

```typescript
// Both can describe object shapes
interface IUser {
  name: string;
  age: number;
}

type TUser = {
  name: string;
  age: number;
};

// Both work the same way
const user1: IUser = { name: "Alice", age: 30 };
const user2: TUser = { name: "Bob", age: 25 };
```

### 8.2 Differences

```typescript
// 1. TYPE can use unions, interfaces cannot
type ID = string | number;
// interface ID = string | number; // ❌ Error

// 2. TYPE can use primitives and tuples
type Name = string;
type Point = [number, number];

// 3. INTERFACE can be merged, type cannot
interface Window {
  title: string;
}
interface Window {
  ts: number;
}
// Result: Window has both title and ts

// type Window = { title: string };
// type Window = { ts: number }; // ❌ Error: Duplicate identifier

// 4. INTERFACE can extend classes
class Control {
  private state: any;
}

interface SelectableControl extends Control {
  select(): void;
}

// 5. TYPE can use mapped types
type Readonly<T> = {
  readonly [P in keyof T]: T[P];
};
```

### 8.3 When to Use Which

| Use Interface | Use Type |
|---------------|----------|
| Object shapes | Unions and intersections |
| Can be extended | Primitive aliases |
| Declaration merging needed | Tuples |
| Public APIs | Mapped types |
| Classes | Conditional types |

---

<a name="advanced-patterns"></a>
## 9. Advanced Patterns

### 9.1 Hybrid Types

```typescript
// Interface for objects that are callable
interface Counter {
  (start: number): string;
  interval: number;
  reset(): void;
}

function createCounter(): Counter {
  const counter = ((start: number) => {
    return `Count: ${start}`;
  }) as Counter;
  
  counter.interval = 1000;
  counter.reset = () => console.log("Reset!");
  
  return counter;
}

const myCounter = createCounter();
console.log(myCounter(5)); // "Count: 5"
console.log(myCounter.interval); // 1000
myCounter.reset(); // "Reset!"
```

### 9.2 Generic Interfaces

```typescript
// Generic interface
interface Box<T> {
  value: T;
  getValue(): T;
  setValue(value: T): void;
}

const numberBox: Box<number> = {
  value: 42,
  getValue() { return this.value; },
  setValue(value) { this.value = value; }
};

const stringBox: Box<string> = {
  value: "hello",
  getValue() { return this.value; },
  setValue(value) { this.value = value; }
};
```

---

<a name="best-practices"></a>
## 10. Best Practices

### 10.1 Interface Design Guidelines

```typescript
// ✅ GOOD: Use interfaces for object shapes
interface User {
  id: number;
  name: string;
  email: string;
}

// ✅ GOOD: Optional properties
interface Config {
  host: string;
  port: number;
  ssl?: boolean;
}

// ✅ GOOD: Readonly for immutable data
interface Point {
  readonly x: number;
  readonly y: number;
}

// ❌ BAD: Don't prefix with 'I'
interface IUser {} // Old C# convention, avoid

// ✅ GOOD: Just use the name
interface User {}

// ✅ GOOD: Extend for composition
interface Admin extends User {
  permissions: string[];
}
```

---

## 📊 Self-Assessment Checkpoints

### ✅ Checkpoint 1: Interfaces

**You should be able to:**
- ✅ Define and use interfaces
- ✅ Create optional and readonly properties
- ✅ Extend interfaces

**Verification:**
```typescript
// Create an interface for a Product with:
// - id (number, readonly)
// - name (string)
// - price (number)
// - description (string, optional)
```

<details>
<summary>View Solution</summary>

```typescript
interface Product {
  readonly id: number;
  name: string;
  price: number;
  description?: string;
}

const product: Product = {
  id: 1,
  name: "Laptop",
  price: 999.99
};
```
</details>

---

## 🧠 Higher-Order Thinking FAQs

### FAQ 1: Interface vs Type - When Does It Matter?

**Q: Developers often say "use interface for objects, type for everything else", but is this too simplistic? What are the nuanced differences that matter in production code?**

<details>
<summary>View Detailed Answer</summary>

**Short Answer:** The distinction matters for extensibility, declaration merging, and performance—not just syntax preference.

**Deep Explanation:**

**Critical Difference #1: Declaration Merging**

```typescript
// Interface - can be augmented
interface User {
  name: string;
}

interface User {
  age: number;
}

// Merged automatically
const user: User = { name: "Alice", age: 30 };

// Type - cannot be augmented
type User = { name: string };
// type User = { age: number }; // ❌ Error: Duplicate identifier
```

**When merging matters:**
- Extending third-party libraries
- Plugin architectures
- Gradual type additions

**Critical Difference #2: Extension Performance**

```typescript
// Interface extends - creates flat type
interface A { a: string }
interface B extends A { b: string }
interface C extends B { c: string }
// C is flat: { a: string; b: string; c: string }

// Type intersection - nested structure
type A = { a: string }
type B = A & { b: string }
type C = B & { c: string }
// C is nested: A & { b: string } & { c: string }
// Can be slower for compiler to resolve
```

**Critical Difference #3: Error Messages**

```typescript
// Interface - clearer errors
interface User {
  name: string;
  age: number;
}

// const user: User = { name: "Alice" };
// Error: Property 'age' is missing in type '{ name: string }'

// Type - can be verbose
type User = {
  name: string;
  age: number;
}

// Errors often show full type structure (longer)
```

**Decision Matrix:**

| Scenario | Use | Reason |
|----------|-----|--------|
| Public library API | Interface | Extensible via declaration merging |
| React props | Interface | Community convention |
| Union/intersection | Type | Only option |
| Tuple | Type | Only option |
| Deep type manipulation | Type | More powerful |
| Simple object shape | Either | Personal/team preference |
| Performance critical | Interface | Faster extension resolution |

**Real-World Impact:**

```typescript
// Library design - interface for extensibility
export interface PluginAPI {
  register(name: string): void;
}

// Users can augment
declare module 'my-library' {
  interface PluginAPI {
    customMethod(): void;
  }
}

// Internal types - use type for flexibility
type RequestHandler<T> = 
  | ((req: Request) => T)
  | ((req: Request) => Promise<T>);
```

**Best Practice:**
- Use **interface** for object shapes that might be extended
- Use **type** for unions, intersections, and computed types
- Be consistent within a project
- Don't stress too much—most cases work with either

</details>

---

## 🎤 Senior SDE Interview Questions

### Interview Question 1: Type-Safe API Design

**Q:** "Design a type-safe REST API client that ensures compile-time safety for endpoints, methods, request bodies, and response types. How would you architect this using TypeScript's type system?"

**Key Concepts:**
- Interface design
- Generic constraints
- Mapped types
- Type inference

**Expected Answer:**

```typescript
// Define API schema
interface ApiSchema {
  "/users": {
    GET: {
      response: User[];
    };
    POST: {
      body: { name: string; email: string };
      response: User;
    };
  };
  "/users/:id": {
    GET: {
      response: User;
    };
    PUT: {
      body: Partial<User>;
      response: User;
    };
    DELETE: {
      response: { success: boolean };
    };
  };
}

// Type-safe client
class ApiClient {
  async request<
    Path extends keyof ApiSchema,
    Method extends keyof ApiSchema[Path]
  >(
    path: Path,
    method: Method,
    ...args: ApiSchema[Path][Method] extends { body: infer B }
      ? [body: B]
      : []
  ): Promise<ApiSchema[Path][Method] extends { response: infer R } ? R : never> {
    // Implementation
    const response = await fetch(path as string, {
      method: method as string,
      body: args[0] ? JSON.stringify(args[0]) : undefined
    });
    return response.json();
  }
}

// Usage - fully type-safe!
const client = new ApiClient();

const users = await client.request("/users", "GET");
// users: User[]

const newUser = await client.request("/users", "POST", {
  body: { name: "Alice", email: "alice@example.com" }
});
// newUser: User

// Compile errors for invalid usage:
// client.request("/users", "DELETE"); // ❌ No DELETE on /users
// client.request("/users", "POST"); // ❌ Missing body
```

**Follow-ups:**
1. "How would you add query parameters with type safety?"
2. "How would you handle authentication tokens?"
3. "What about error handling and status codes?"

**Green Flags:**
- Uses mapped types
- Leverages conditional types
- Considers variadic tuples for optional body
- Discusses runtime validation

---

## 🎯 Key Takeaways

✅ **Interfaces** define object shapes with type safety

✅ **Optional properties** use `?` modifier

✅ **Readonly properties** prevent modification

✅ **Index signatures** allow dynamic properties

✅ **Interface extension** enables composition

✅ **Declaration merging** allows augmentation

✅ **Interface vs type** - both work for objects, each has strengths

✅ **Generic interfaces** enable reusable type-safe structures

---

[← Previous: Functions](./11_functions.md) | [Next: Arrays & Tuples →](./13_arrays_tuples.md)

**Progress**: Topic 12 of 63 | Part II: 100% Complete ✅
