# ⭐ TypeScript Best Practices - Production-Grade Code

[← Previous: Functional Patterns](./46_functional_patterns.md) | [← Back to Main](../README.md) | [Next: Performance Optimization →](./48_performance.md)

---

## 📝 Overview

Writing production-quality TypeScript requires following established best practices for code organization, naming conventions, type safety, and maintainability. This guide distills industry wisdom into actionable guidelines.

### 🎯 Learning Objectives

- ✅ Apply code organization patterns
- ✅ Follow naming conventions
- ✅ Write maintainable types
- ✅ Optimize for readability
- ✅ Ensure type safety

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced
- **Estimated Time**: 4-5 hours
- **Prerequisites**: All previous topics
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [Code Organization](#organization)
2. [Naming Conventions](#naming)
3. [Type Safety Guidelines](#type-safety)
4. [File Structure](#file-structure)
5. [Documentation](#documentation)

---

<a name="organization"></a>
## 1. Code Organization

```typescript
// ✅ GOOD: Organize by feature
src/
├── features/
│   ├── auth/
│   │   ├── types.ts
│   │   ├── api.ts
│   │   └── index.ts
│   └── users/
│       ├── types.ts
│       ├── api.ts
│       └── index.ts
└── shared/
    ├── utils/
    └── types/

// ❌ BAD: Organize by file type
src/
├── types/
├── apis/
└── utils/
```

---

<a name="naming"></a>
## 2. Naming Conventions

```typescript
// ✅ GOOD: Clear, descriptive names
interface UserAccount {
  id: number;
  email: string;
}

type HttpMethod = "GET" | "POST" | "PUT" | "DELETE";

function calculateTotalPrice(items: CartItem[]): number {
  return items.reduce((sum, item) => sum + item.price, 0);
}

// ❌ BAD: Unclear names
interface UA {}
type HM = string;
function calc(x: any[]): number {}

// ✅ GOOD: Consistent naming
// - Interfaces/Types: PascalCase
// - Variables/Functions: camelCase
// - Constants: UPPER_SNAKE_CASE
// - Private fields: _prefixed or #private
```

---

<a name="type-safety"></a>
## 3. Type Safety Guidelines

```typescript
// ✅ GOOD: Avoid any
function process(data: unknown): void {
  if (typeof data === "string") {
    // Type-safe handling
  }
}

// ❌ BAD: Using any
function bad(data: any): void {}

// ✅ GOOD: Strict null checks
function greet(name: string | null): string {
  return name ? `Hello, ${name}` : "Hello, Guest";
}

// ✅ GOOD: Use readonly for immutable data
interface Config {
  readonly apiUrl: string;
  readonly timeout: number;
}

// ✅ GOOD: Explicit return types for public APIs
export function calculateTotal(items: Item[]): number {
  return items.reduce((sum, item) => sum + item.price, 0);
}
```

---

## 🎯 Key Takeaways

✅ **Organize by feature** not file type

✅ **Use descriptive names** consistently

✅ **Avoid any** - use unknown or specific types

✅ **Enable strict mode** always

✅ **Document public APIs** with JSDoc

✅ **Prefer immutability** with readonly

✅ **Type everything** explicitly at boundaries

---

[← Previous: Functional Patterns](./46_functional_patterns.md) | [Next: Performance Optimization →](./48_performance.md)

**Progress**: Topic 47 of 63 | Part VIII: 17% Complete
