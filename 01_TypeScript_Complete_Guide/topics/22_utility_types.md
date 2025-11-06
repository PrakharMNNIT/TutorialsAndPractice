# 🛠️ Utility Types - Built-in Type Transformations

[← Previous: Advanced Generics](./21_advanced_generics.md) | [← Back to Main](../README.md) | [Next: Conditional Types →](./23_conditional_types.md)

---

## 📝 Overview

TypeScript provides powerful built-in utility types that transform existing types in useful ways. These utilities—like Partial, Required, Pick, Omit, and more—are essential tools for everyday TypeScript development, enabling concise and expressive type manipulations without reinventing the wheel.

**What You'll Learn:**
- All built-in utility types
- When and how to use each utility
- How utility types are implemented
- Creating custom utility types
- Combining utilities for complex transformations
- Performance characteristics

### 🎯 Learning Objectives

- ✅ Master all built-in utility types
- ✅ Choose the right utility for each situation
- ✅ Understand utility type implementations
- ✅ Create custom utility types
- ✅ Combine utilities effectively
- ✅ Apply utilities in real scenarios

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced
- **Estimated Time**: 4-5 hours
- **Prerequisites**: [Generics](./20_generics_intro.md), [Advanced Generics](./21_advanced_generics.md)
- **Practice Exercises**: 15 challenges
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [Partial & Required](#partial-required)
2. [Readonly & Mutable](#readonly-mutable)
3. [Pick & Omit](#pick-omit)
4. [Record](#record)
5. [Extract & Exclude](#extract-exclude)
6. [NonNullable](#nonnullable)
7. [ReturnType & Parameters](#returntype-parameters)
8. [Awaited & Promise Utilities](#awaited)
9. [String Manipulation](#string-utilities)
10. [Custom Utilities](#custom-utilities)
11. [Best Practices](#best-practices)
12. [Higher-Order FAQs](#faqs)
13. [Interview Questions](#interview-questions)

---

<a name="partial-required"></a>
## 1. Partial & Required

### 1.1 Partial<T>

Makes all properties optional.

```typescript
// Original type
interface User {
  name: string;
  age: number;
  email: string;
}

// Partial makes everything optional
type PartialUser = Partial<User>;
// Equivalent to:
// {
//   name?: string;
//   age?: number;
//   email?: string;
// }

// Usage: update functions
function updateUser(user: User, updates: Partial<User>): User {
  return { ...user, ...updates };
}

const user: User = { name: "Alice", age: 30, email: "alice@example.com" };
const updated = updateUser(user, { age: 31 }); // Only update age

// Implementation
type Partial<T> = {
  [P in keyof T]?: T[P];
};
```

**Version Tracking:**
- ✅ Partial, Required (v2.1+)
- ✅ Readonly, Pick, Omit (v2.1-3.5+)
- 🆕 Awaited (v4.5+) - Promise unwrapping
- 🆕 Capitalize, Lowercase, etc. (v4.1+) - String utilities

### 1.2 Required<T>

Makes all properties required.

```typescript
// Type with optional properties
interface Config {
  host?: string;
  port?: number;
  timeout?: number;
}

// Required makes everything required
type FullConfig = Required<Config>;
// Equivalent to:
// {
//   host: string;
//   port: number;
//   timeout: number;
// }

// Usage: validation
function validateConfig(config: Config): Required<Config> {
  const defaults = {
    host: "localhost",
    port: 3000,
    timeout: 5000
  };
  
  return { ...defaults, ...config };
}

// Implementation
type Required<T> = {
  [P in keyof T]-?: T[P]; // -? removes optional
};
```

---

<a name="readonly-mutable"></a>
## 2. Readonly & Mutable

### 2.1 Readonly<T>

Makes all properties readonly.

```typescript
// Readonly utility
interface User {
  name: string;
  age: number;
}

type ReadonlyUser = Readonly<User>;
// Equivalent to:
// {
//   readonly name: string;
//   readonly age: number;
// }

const user: ReadonlyUser = { name: "Alice", age: 30 };
// user.name = "Bob"; // ❌ Error: Cannot assign to readonly

// Implementation
type Readonly<T> = {
  readonly [P in keyof T]: T[P];
};
```

### 2.2 Custom Mutable (Opposite of Readonly)

```typescript
// Remove readonly modifier
type Mutable<T> = {
  -readonly [P in keyof T]: T[P]; // -readonly removes modifier
};

interface ReadonlyPoint {
  readonly x: number;
  readonly y: number;
}

type MutablePoint = Mutable<ReadonlyPoint>;
// { x: number; y: number; } - readonly removed

const point: MutablePoint = { x: 10, y: 20 };
point.x = 15; // ✅ OK
```

---

<a name="pick-omit"></a>
## 3. Pick & Omit

### 3.1 Pick<T, K>

Select specific properties from type.

```typescript
// Original type
interface User {
  id: number;
  name: string;
  email: string;
  age: number;
  password: string;
}

// Pick specific properties
type UserPublic = Pick<User, "id" | "name" | "email">;
// { id: number; name: string; email: string; }

// Usage: API responses
function getPublicUser(user: User): UserPublic {
  const { id, name, email } = user;
  return { id, name, email };
}

// Implementation
type Pick<T, K extends keyof T> = {
  [P in K]: T[P];
};
```

### 3.2 Omit<T, K>

Exclude specific properties from type.

```typescript
// Omit unwanted properties
type UserWithoutPassword = Omit<User, "password">;
// Has all properties except password

// Omit multiple
type UserMinimal = Omit<User, "password" | "email">;

// Usage: form data
function registerUser(data: Omit<User, "id">): User {
  return {
    id: Date.now(),
    ...data
  };
}

// Implementation
type Omit<T, K extends keyof any> = Pick<T, Exclude<keyof T, K>>;
```

---

<a name="record"></a>
## 4. Record

### 4.1 Record<K, T>

Creates object type with specific keys and value type.

```typescript
// Create object type from keys
type Role = "admin" | "user" | "guest";
type Permissions = Record<Role, string[]>;

const permissions: Permissions = {
  admin: ["read", "write", "delete"],
  user: ["read", "write"],
  guest: ["read"]
};

// Dictionary pattern
type UserRecord = Record<string, User>;

const users: UserRecord = {
  "user1": { name: "Alice", age: 30 },
  "user2": { name: "Bob", age: 25 }
};

// Implementation
type Record<K extends keyof any, T> = {
  [P in K]: T;
};
```

---

<a name="extract-exclude"></a>
## 5. Extract & Exclude

### 5.1 Extract<T, U>

Extract types from union that are assignable to U.

```typescript
// Extract specific types from union
type Mixed = string | number | boolean;
type StringOrNumber = Extract<Mixed, string | number>;
// Type: string | number

// Extract function types
type Functions = (() => void) | string | number;
type OnlyFunctions = Extract<Functions, Function>;
// Type: () => void

// Implementation
type Extract<T, U> = T extends U ? T : never;
```

### 5.2 Exclude<T, U>

Exclude types from union that are assignable to U.

```typescript
// Exclude specific types
type Mixed = string | number | boolean;
type NoStrings = Exclude<Mixed, string>;
// Type: number | boolean

// Exclude null and undefined
type NonNullable<T> = Exclude<T, null | undefined>;

type Value = string | null | undefined;
type NonNullValue = NonNullable<Value>;
// Type: string

// Implementation
type Exclude<T, U> = T extends U ? never : T;
```

---

<a name="returntype-parameters"></a>
## 6. ReturnType & Parameters

### 6.1 ReturnType<T>

Extracts return type from function type.

```typescript
// Get return type
function getUser(): User {
  return { name: "Alice", age: 30 };
}

type UserType = ReturnType<typeof getUser>;
// Type: User

// With generics
function createArray<T>(value: T): T[] {
  return [value];
}

type ArrayType = ReturnType<typeof createArray<number>>;
// Type: number[]

// Implementation
type ReturnType<T extends (...args: any) => any> = 
  T extends (...args: any) => infer R ? R : any;
```

### 6.2 Parameters<T>

Extracts parameter types as tuple.

```typescript
// Get parameter types
function createUser(name: string, age: number, email: string): User {
  return { name, age, email };
}

type CreateUserParams = Parameters<typeof createUser>;
// Type: [name: string, age: number, email: string]

// Usage: wrap function
function logAndCreate(...args: Parameters<typeof createUser>): User {
  console.log("Creating user:", args);
  return createUser(...args);
}

// Implementation
type Parameters<T extends (...args: any) => any> = 
  T extends (...args: infer P) => any ? P : never;
```

---

<a name="awaited"></a>
## 7. Awaited & Promise Utilities

### 7.1 Awaited<T>

Unwraps Promise types recursively.

```typescript
// Unwrap Promise
type StringPromise = Promise<string>;
type Unwrapped = Awaited<StringPromise>;
// Type: string

// Recursive unwrapping
type NestedPromise = Promise<Promise<Promise<number>>>;
type UnwrappedNested = Awaited<NestedPromise>;
// Type: number

// Usage with async functions
async function fetchUser(): Promise<User> {
  return { name: "Alice", age: 30 };
}

type UserResult = Awaited<ReturnType<typeof fetchUser>>;
// Type: User

// Implementation (simplified)
type Awaited<T> = 
  T extends Promise<infer U> ? Awaited<U> : T;
```

---

<a name="string-utilities"></a>
## 8. String Manipulation Utilities

### 8.1 Capitalize, Uppercase, Lowercase, Uncapitalize

```typescript
// Capitalize first letter
type LowerName = "alice";
type CapitalName = Capitalize<LowerName>;
// Type: "Alice"

// Uppercase all
type UpperName = Uppercase<"hello">;
// Type: "HELLO"

// Lowercase all
type LowerName2 = Lowercase<"WORLD">;
// Type: "world"

// Uncapitalize first letter
type UnCapName = Uncapitalize<"Hello">;
// Type: "hello"

// Practical: event handlers
type EventName = "click" | "focus" | "blur";
type HandlerName = `on${Capitalize<EventName>}`;
// Type: "onClick" | "onFocus" | "onBlur"
```

---

<a name="custom-utilities"></a>
## 9. Custom Utility Types

### 9.1 Building Your Own

```typescript
// Deep Partial
type DeepPartial<T> = {
  [P in keyof T]?: T[P] extends object
    ? DeepPartial<T[P]>
    : T[P];
};

// Mutable (opposite of Readonly)
type Mutable<T> = {
  -readonly [P in keyof T]: T[P];
};

// PickByType - pick properties by value type
type PickByType<T, U> = {
  [P in keyof T as T[P] extends U ? P : never]: T[P];
};

interface Example {
  name: string;
  age: number;
  active: boolean;
  count: number;
}

type NumberProps = PickByType<Example, number>;
// { age: number; count: number; }

// OmitByType - omit properties by value type
type OmitByType<T, U> = {
  [P in keyof T as T[P] extends U ? never : P]: T[P];
};

type NonNumbers = OmitByType<Example, number>;
// { name: string; active: boolean; }
```

---

<a name="best-practices"></a>
## 10. Best Practices

### 10.1 When to Use Utilities

```typescript
// ✅ GOOD: Use Partial for updates
function updateUser(user: User, updates: Partial<User>): User {
  return { ...user, ...updates };
}

// ✅ GOOD: Use Pick for DTOs
type UserDTO = Pick<User, "name" | "email">;

// ✅ GOOD: Use Omit to exclude sensitive data
type PublicUser = Omit<User, "password" | "ssn">;

// ✅ GOOD: Use Record for dictionaries
type ErrorMessages = Record<ErrorCode, string>;

// ✅ GOOD: Use ReturnType for type inference
type ApiResult = ReturnType<typeof apiCall>;

// ❌ BAD: Don't manually recreate utilities
type MyPartial<T> = { [P in keyof T]?: T[P] }; // Just use Partial<T>
```

---

## 🧠 Higher-Order FAQs

### FAQ 1: Partial vs Optional Properties

**Q: What's the difference between Partial<T> and a type with optional properties? When does the distinction matter?**

<details>
<summary>View Detailed Answer</summary>

**Short Answer:** Partial makes existing properties optional; optional properties are defined that way. Partial is a transformation, optional is a definition.

**Deep Explanation:**

**Conceptual Difference:**

```typescript
// Defining with optional properties
interface UserOptional {
  name: string;
  age: number;
  email?: string; // Defined as optional
}

// Using Partial to make everything optional
interface UserRequired {
  name: string;
  age: number;
  email: string;
}

type UserPartial = Partial<UserRequired>;
// All properties become optional
```

**When It Matters - Update Functions:**

```typescript
// With optional properties defined
interface Config {
  host: string;
  port: number;
  ssl?: boolean; // Already optional
}

function updateConfig(config: Config, updates: Config): Config {
  return { ...config, ...updates };
  // Problem: updates requires host and port!
}

// With Partial
function updateConfig(config: Config, updates: Partial<Config>): Config {
  return { ...config, ...updates };
  // ✅ Can update any subset of properties
}

updateConfig(config, { ssl: true }); // ✅ OK
```

**Semantic Difference:**

```typescript
// Optional property means "may not exist in data model"
interface APIUser {
  id: number;
  name: string;
  phoneNumber?: string; // User might not have phone
}

// Partial means "subset for an operation"
function patchUser(id: number, updates: Partial<APIUser>): void {
  // updates is partial because we're updating, not because
  // these fields are optional in the data model
}
```

**Type Safety Difference:**

```typescript
interface Required {
  name: string;
  age: number;
}

interface WithOptional {
  name: string;
  age?: number;
}

// These are different!
type Test1 = Partial<Required>;
// { name?: string; age?: number; }

type Test2 = WithOptional;
// { name: string; age?: number; }

// Test1 allows omitting name, Test2 doesn't
const obj1: Test1 = {}; // ✅ OK
// const obj2: Test2 = {}; // ❌ Error: name is missing
```

**Production Pattern:**

```typescript
// Domain model - express actual requirements
interface User {
  id: number;
  name: string;
  email: string;
  phoneNumber?: string; // Truly optional in domain
  createdAt: Date;
}

// Use Partial for operations, not domain modeling
function updateUser(
  id: number,
  updates: Partial<Omit<User, "id" | "createdAt">>
): Promise<User> {
  // Can update any user field except id and createdAt
}
```

</details>

---

## 🎤 Senior SDE Interview Questions

### Interview Question 1: Type-Safe Form Builder

**Q:** "Design a type-safe form builder where field definitions determine validation rules and the final form data type. Use utility types to transform between form config, validation rules, and output types."

**Key Concepts:**
- Utility types
- Mapped types
- Type transformations
- Type safety through transformations

**Expected Answer:**

```typescript
// Field configuration
interface FieldConfig<T> {
  type: "text" | "number" | "email" | "checkbox";
  required: boolean;
  defaultValue?: T;
  validate?: (value: T) => boolean;
}

// Form schema
interface FormSchema {
  [fieldName: string]: FieldConfig<any>;
}

// Transform config to field types
type FieldType<C extends FieldConfig<any>> = 
  C["type"] extends "number" ? number :
  C["type"] extends "checkbox" ? boolean :
  string;

// Transform schema to form data type
type FormData<S extends FormSchema> = {
  [K in keyof S]: S[K] extends FieldConfig<infer T>
    ? T
    : never;
};

// Transform schema to validation errors
type FormErrors<S extends FormSchema> = Partial<{
  [K in keyof S]: string;
}>;

// Usage
const userFormSchema = {
  name: {
    type: "text",
    required: true
  },
  age: {
    type: "number",
    required: true,
    validate: (v: number) => v >= 18
  },
  email: {
    type: "email",
    required: true
  },
  newsletter: {
    type: "checkbox",
    required: false,
    defaultValue: false
  }
} as const;

type UserFormData = FormData<typeof userFormSchema>;
// {
//   name: string;
//   age: number;
//   email: string;
//   newsletter: boolean;
// }

type UserFormErrors = FormErrors<typeof userFormSchema>;
// {
//   name?: string;
//   age?: string;
//   email?: string;
//   newsletter?: string;
// }

// Form builder class
class FormBuilder<S extends FormSchema> {
  constructor(private schema: S) {}
  
  validate(data: FormData<S>): FormErrors<S> {
    const errors: FormErrors<S> = {};
    
    for (const field in this.schema) {
      const config = this.schema[field];
      const value = data[field];
      
      if (config.required && !value) {
        errors[field] = "Required field";
      }
      
      if (config.validate && !config.validate(value)) {
        errors[field] = "Validation failed";
      }
    }
    
    return errors;
  }
  
  getDefaults(): Partial<FormData<S>> {
    // Return default values
    const defaults: any = {};
    for (const field in this.schema) {
      if (this.schema[field].defaultValue !== undefined) {
        defaults[field] = this.schema[field].defaultValue;
      }
    }
    return defaults;
  }
}

// Type-safe usage
const form = new FormBuilder(userFormSchema);

const data: UserFormData = {
  name: "Alice",
  age: 25,
  email: "alice@example.com",
  newsletter: true
};

const errors = form.validate(data);
if (errors.age) {
  console.log(errors.age); // Type-safe error access
}
```

**Follow-ups:**
1. "How would you add conditional required fields?"
2. "How would you handle nested form sections?"
3. "What about array fields?"

**Green Flags:**
- Uses mapped types effectively
- Transforms between related type structures
- Considers validation at type level
- Mentions form libraries (React Hook Form, Formik)

---

## 🎯 Key Takeaways

✅ **Utility types** transform existing types

✅ **Partial/Required** change optional state

✅ **Pick/Omit** select/exclude properties

✅ **Record** creates object types from keys

✅ **ReturnType/Parameters** extract function types

✅ **Awaited** unwraps Promises

✅ **Built-in utilities** save time and are well-tested

---

[← Previous: Advanced Generics](./21_advanced_generics.md) | [Next: Conditional Types →](./23_conditional_types.md)

**Progress**: Topic 22 of 63 | Part IV: 20% Complete
