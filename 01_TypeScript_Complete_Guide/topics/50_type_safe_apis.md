# 🔐 Type-Safe APIs - REST, GraphQL & tRPC

[← Previous: Error Handling](./49_error_handling.md) | [← Back to Main](../README.md) | [Next: Monorepo →](./51_monorepo.md)

---

## 📝 Overview

Building type-safe APIs ensures compile-time safety between client and server. This guide covers REST API typing, GraphQL with TypeScript, and tRPC for end-to-end type safety.

### 🎯 Learning Objectives

- ✅ Type REST APIs safely
- ✅ Use GraphQL with TypeScript
- ✅ Implement tRPC for full type safety
- ✅ Validate API contracts

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced  
- **Estimated Time**: 5-6 hours
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [REST API Types](#rest-api)
2. [GraphQL with TypeScript](#graphql)
3. [tRPC End-to-End Safety](#trpc)
4. [Best Practices](#best-practices)

---

<a name="rest-api"></a>
## 1. REST API Types

```typescript
// Define API schema
interface API {
  "/users": {
    GET: { response: User[] };
    POST: { body: CreateUserDto; response: User };
  };
  "/users/:id": {
    GET: { response: User };
    PUT: { body: UpdateUserDto; response: User };
  };
}

// Type-safe fetch wrapper
async function api<
  Path extends keyof API,
  Method extends keyof API[Path]
>(
  path: Path,
  method: Method,
  ...args: API[Path][Method] extends { body: infer B } ? [B] : []
): Promise<API[Path][Method] extends { response: infer R } ? R : never> {
  const response = await fetch(path as string, {
    method: method as string,
    body: args[0] ? JSON.stringify(args[0]) : undefined
  });
  return response.json();
}

// Usage - fully type-safe!
const users = await api("/users", "GET");
// users: User[]

const newUser = await api("/users", "POST", {
  name: "Alice",
  email: "alice@example.com"
});
// newUser: User
```

---

<a name="trpc"></a>
## 2. tRPC End-to-End Safety

```typescript
// tRPC provides full stack type safety
import { initTRPC } from '@trpc/server';

const t = initTRPC.create();

const appRouter = t.router({
  getUser: t.procedure
    .input((val: unknown) => {
      if (typeof val === 'number') return val;
      throw new Error('Invalid input');
    })
    .query(async ({ input }) => {
      const user = await db.user.findById(input);
      return user;
    }),
  
  createUser: t.procedure
    .input((val: unknown) => {
      // Validate input
      return val as CreateUserInput;
    })
    .mutation(async ({ input }) => {
      const user = await db.user.create(input);
      return user;
    })
});

// Client gets full type safety automatically!
// const user = await trpc.getUser.query(123);
```

---

## 🎯 Key Takeaways

✅ **Type API contracts** for safety

✅ **Validate at runtime** always

✅ **tRPC** provides best type safety

✅ **GraphQL** has good TypeScript support

✅ **REST** requires manual type definitions

---

[← Previous: Error Handling](./49_error_handling.md) | [Next: Monorepo →](./51_monorepo.md)

**Progress**: Topic 50 of 63 | Part VIII: 67% Complete
