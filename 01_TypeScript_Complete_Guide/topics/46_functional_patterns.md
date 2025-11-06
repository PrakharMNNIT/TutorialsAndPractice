# 🔄 Functional Programming Patterns - Immutability & Pure Functions

[← Previous: Behavioral Patterns](./45_behavioral_patterns.md) | [← Back to Main](../README.md) | [Next: TypeScript Best Practices →](./47_best_practices.md)

---

## 📝 Overview

Functional programming patterns emphasize immutability, pure functions, and function composition. TypeScript's type system excels at typing functional patterns, making FP safe and expressive.

### 🎯 Learning Objectives

- ✅ Write pure functions
- ✅ Apply immutability patterns
- ✅ Use function composition
- ✅ Implement common FP patterns
- ✅ Type functional code safely

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced
- **Estimated Time**: 5-6 hours
- **Prerequisites**: [Functions](./11_functions.md), [Generics](./20_generics_intro.md)
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [Pure Functions](#pure-functions)
2. [Immutability](#immutability)
3. [Function Composition](#composition)
4. [Common FP Patterns](#patterns)
5. [Best Practices](#best-practices)

---

<a name="pure-functions"></a>
## 1. Pure Functions

```typescript
// Pure function - same input, same output, no side effects
function add(a: number, b: number): number {
  return a + b; // Pure
}

// Impure - depends on external state
let total = 0;
function addToTotal(n: number): number {
  total += n; // Side effect
  return total;
}

// Pure alternative
function calculateTotal(current: number, n: number): number {
  return current + n; // No side effects
}
```

---

<a name="immutability"></a>
## 2. Immutability

```typescript
// Immutable data transformations
interface User {
  readonly name: string;
  readonly age: number;
}

// Don't mutate
function updateAge(user: User, newAge: number): User {
  return { ...user, age: newAge }; // New object
}

// Deep immutability
type DeepReadonly<T> = {
  readonly [P in keyof T]: T[P] extends object
    ? DeepReadonly<T[P]>
    : T[P];
};
```

---

<a name="composition"></a>
## 3. Function Composition

```typescript
// Compose functions
const compose = <A, B, C>(
  f: (b: B) => C,
  g: (a: A) => B
) => (a: A): C => f(g(a));

const add1 = (n: number) => n + 1;
const double = (n: number) => n * 2;

const add1ThenDouble = compose(double, add1);
console.log(add1ThenDouble(5)); // 12
```

---

<a name="patterns"></a>
## 4. Common FP Patterns

```typescript
// Map, Filter, Reduce
const numbers = [1, 2, 3, 4, 5];

const doubled = numbers.map(n => n * 2);
const evens = numbers.filter(n => n % 2 === 0);
const sum = numbers.reduce((acc, n) => acc + n, 0);

// Currying
const multiply = (a: number) => (b: number) => a * b;
const double = multiply(2);
console.log(double(5)); // 10

// Partial application
const greet = (greeting: string, name: string) => `${greeting}, ${name}!`;
const sayHello = (name: string) => greet("Hello", name);
```

---

## 🎯 Key Takeaways

✅ **Pure functions**: Predictable, testable

✅ **Immutability**: Safer state management

✅ **Composition**: Build complex from simple

✅ **TypeScript**: Excellent FP support

✅ **FP patterns**: Reduce bugs, improve maintainability

---

[← Previous: Behavioral Patterns](./45_behavioral_patterns.md) | [Next: TypeScript Best Practices →](./47_best_practices.md)

**Progress**: Topic 46 of 63 | Part VII: 100% Complete ✅
