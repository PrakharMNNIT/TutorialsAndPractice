# ⚡ Promises & Async/Await - Modern Async Patterns

[← Previous: Event Loop](./38_event_loop.md) | [← Back to Main](../README.md) | [Next: Web Workers →](./40_web_workers.md)

---

## 📝 Overview

Promises and async/await are the modern way to handle asynchronous operations in TypeScript. This guide covers advanced Promise patterns, async/await best practices, and TypeScript-specific async typing.

### 🎯 Learning Objectives

- ✅ Master Promise patterns
- ✅ Use async/await effectively  
- ✅ Type async operations safely
- ✅ Handle errors properly
- ✅ Optimize async performance

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced
- **Estimated Time**: 5-6 hours
- **Prerequisites**: [Async JavaScript](./05_async_javascript.md), [Event Loop](./38_event_loop.md)
- **Version**: ES2024, TypeScript 5.7+

---

## 📚 Table of Contents

1. [Promise Patterns](#promise-patterns)
2. [Async/Await Best Practices](#async-await)
3. [TypeScript Async Types](#typescript-async)
4. [Error Handling](#error-handling)
5. [Performance](#performance)

---

<a name="promise-patterns"></a>
## 1. Promise Patterns

```typescript
// Promise.all - parallel execution
const [users, products] = await Promise.all([
  fetchUsers(),
  fetchProducts()
]);

// Promise.allSettled - wait for all (no fail-fast)
const results = await Promise.allSettled([
  fetchUsers(),
  fetchProducts()
]);

// Promise.race - first to complete
const fastest = await Promise.race([
  fetchFromCache(),
  fetchFromAPI()
]);

// Promise.any - first fulfilled
const first = await Promise.any([
  fetchFromServer1(),
  fetchFromServer2()
]);
```

---

<a name="async-await"></a>
## 2. Async/Await Best Practices

```typescript
// ✅ GOOD: Parallel when possible
const [data1, data2] = await Promise.all([
  fetch1(),
  fetch2()
]);

// ❌ BAD: Sequential when unnecessary
const data1 = await fetch1();
const data2 = await fetch2(); // Waits for data1

// ✅ GOOD: Error handling
async function fetchData(): Promise<Data> {
  try {
    const response = await fetch('/api');
    return response.json();
  } catch (error) {
    console.error(error);
    throw error;
  }
}
```

---

<a name="typescript-async"></a>
## 3. TypeScript Async Types

```typescript
// Type-safe async functions
async function fetchUser(id: number): Promise<User> {
  const response = await fetch(`/api/users/${id}`);
  const data: User = await response.json();
  return data;
}

// Async return types
type AsyncReturnType<T extends (...args: any) => Promise<any>> = 
  T extends (...args: any) => Promise<infer R> ? R : never;

type UserType = AsyncReturnType<typeof fetchUser>;
// Type: User

// Generic async functions
async function fetchData<T>(url: string): Promise<T> {
  const response = await fetch(url);
  return response.json();
}

const users = await fetchData<User[]>('/users');
```

---

## 🎯 Key Takeaways

✅ **Promises** handle async operations

✅ **Async/await** makes async code readable

✅ **TypeScript** provides type safety for async

✅ **Promise.all** for parallel execution

✅ **Try/catch** for async error handling

✅ **Type async** return values explicitly

---

[← Previous: Event Loop](./38_event_loop.md) | [Next: Web Workers →](./40_web_workers.md)

**Progress**: Topic 39 of 63 | Part VI: 50% Complete
