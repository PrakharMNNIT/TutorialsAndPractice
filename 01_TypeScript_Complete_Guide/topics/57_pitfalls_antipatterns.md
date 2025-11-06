# ⚠️ Common Pitfalls & Anti-Patterns - What to Avoid

[← Previous: Backend Node.js](./56_backend_nodejs.md) | [← Back to Main](../README.md) | [Next: Interview Prep →](./58_interview_prep.md)

---

## 📝 Overview

Learning what NOT to do is as important as learning best practices. This guide covers common TypeScript mistakes, anti-patterns, and how to avoid them.

### 🎯 Learning Objectives

- ✅ Recognize common pitfalls
- ✅ Avoid anti-patterns
- ✅ Refactor bad code
- ✅ Write better TypeScript

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced
- **Estimated Time**: 4-5 hours
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [Type-Related Pitfalls](#type-pitfalls)
2. [Code Organization Anti-Patterns](#organization)
3. [Performance Pitfalls](#performance)
4. [Common Mistakes](#mistakes)

---

<a name="type-pitfalls"></a>
## 1. Type-Related Pitfalls

```typescript
// ❌ BAD: Using any everywhere
function process(data: any): any {
  return data.value;
}

// ✅ GOOD: Use unknown or specific types
function process(data: unknown): string {
  if (typeof data === "object" && data !== null && "value" in data) {
    return String((data as { value: unknown }).value);
  }
  throw new Error("Invalid data");
}

// ❌ BAD: Type assertions without validation
const user = data as User; // Dangerous!

// ✅ GOOD: Validate before asserting
if (isUser(data)) {
  const user = data; // Type narrowed safely
}

// ❌ BAD: Non-null assertions everywhere
element!.click();
user!.name;

// ✅ GOOD: Check for null
if (element) {
  element.click();
}
```

---

<a name="organization"></a>
## 2. Code Organization Anti-Patterns

```typescript
// ❌ BAD: God objects
class Application {
  // 5000 lines of code
  // Handles everything
}

// ✅ GOOD: Single responsibility
class UserService {}
class AuthService {}
class EmailService {}

// ❌ BAD: Circular dependencies
// file-a.ts imports file-b.ts
// file-b.ts imports file-a.ts

// ✅ GOOD: Hierarchical dependencies
// shared/ ← features/ ← app.ts
```

---

<a name="performance"></a>
## 3. Performance Pitfalls

```typescript
// ❌ BAD: Complex recursive types
type DeepNested<T> = T extends object
  ? { [K in keyof T]: DeepNested<T[K]> }
  : T;
// Can cause slow compilation

// ✅ GOOD: Limit recursion depth
type DeepNested<T, Depth = 5> = ...

// ❌ BAD: Large union types
type AllClasses = Class1 | Class2 | ... | Class1000;

// ✅ GOOD: Use string with validation
type ClassName = string & { __brand: "ClassName" };
```

---

<a name="mistakes"></a>
## 4. Common Mistakes

```typescript
// ❌ BAD: Forgetting async/await
async function getData() {
  return fetch('/api'); // Returns Promise<Response>, not Response!
}

// ✅ GOOD: Always await promises
async function getData() {
  const response = await fetch('/api');
  return response.json();
}

// ❌ BAD: Implicit any in callbacks
[1, 2, 3].map(x => x * 2); // x has implicit any without types

// ✅ GOOD: Enable noImplicitAny in tsconfig
// Forces explicit types or inference
```

---

## 🎯 Key Takeaways

✅ **Avoid any** - use unknown or specific types

✅ **Validate before** type assertions

✅ **Check for null** instead of non-null assertions

✅ **Organize code** by feature not file type

✅ **Limit type complexity** for compilation speed

✅ **Enable strict mode** to catch mistakes

✅ **Learn from mistakes** - they make you better

---

[← Previous: Backend Node.js](./56_backend_nodejs.md) | [Next: Interview Prep →](./58_interview_prep.md)

**Progress**: Topic 57 of 63 | Part X: 14% Complete
