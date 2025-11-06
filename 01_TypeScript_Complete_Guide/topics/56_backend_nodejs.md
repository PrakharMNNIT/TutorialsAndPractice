# 🖧 TypeScript for Backend - Node.js, Express, Fastify

[← Previous: Frontend Frameworks](./55_frontend_frameworks.md) | [← Back to Main](../README.md) | [Next: Common Pitfalls →](./57_pitfalls_antipatterns.md)

---

## 📝 Overview

TypeScript excels in backend development, providing type safety for APIs, database operations, and server logic. This guide covers building robust backend applications with Node.js and TypeScript.

### 🎯 Learning Objectives

- ✅ Set up Node.js with TypeScript
- ✅ Build Express APIs
- ✅ Use Fastify (fastest)
- ✅ Type database operations
- ✅ Deploy TypeScript backends

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced
- **Estimated Time**: 6-8 hours
- **Version**: Node.js 18+, TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [Node.js Setup](#nodejs-setup)
2. [Express Backend](#express)
3. [Fastify Backend](#fastify)
4. [Database Integration](#database)
5. [Best Practices](#best-practices)

---

<a name="nodejs-setup"></a>
## 1. Node.js Setup

```bash
mkdir backend && cd backend
npm init -y
npm install express
npm install --save-dev typescript @types/node @types/express ts-node-dev
npx tsc --init
```

```json
// tsconfig.json
{
  "compilerOptions": {
    "target": "ES2022",
    "module": "commonjs",
    "outDir": "./dist",
    "rootDir": "./src",
    "strict": true,
    "esModuleInterop": true
  }
}
```

---

<a name="express"></a>
## 2. Express Backend

```typescript
// src/server.ts
import express, { Request, Response } from 'express';

const app = express();
app.use(express.json());

interface User {
  id: number;
  name: string;
  email: string;
}

app.get('/users', async (req: Request, res: Response) => {
  const users: User[] = await db.getUsers();
  res.json(users);
});

app.post('/users', async (req: Request, res: Response) => {
  const user: User = await db.createUser(req.body);
  res.status(201).json(user);
});

app.listen(3000, () => {
  console.log('Server running on port 3000');
});
```

---

<a name="fastify"></a>
## 3. Fastify (Fastest)

```typescript
import Fastify from 'fastify';

const fastify = Fastify({ logger: true });

interface User {
  id: number;
  name: string;
}

fastify.get<{ Reply: User[] }>('/users', async () => {
  return await db.getUsers();
});

fastify.post<{ Body: CreateUserDto; Reply: User }>(
  '/users',
  async (request) => {
    return await db.createUser(request.body);
  }
);

fastify.listen({ port: 3000 });
```

---

<a name="database"></a>
## 4. Database Integration

```typescript
// Prisma (type-safe ORM)
import { PrismaClient } from '@prisma/client';

const prisma = new PrismaClient();

// Fully typed database operations
const users = await prisma.user.findMany();
// users: User[]

const user = await prisma.user.create({
  data: {
    name: "Alice",
    email: "alice@example.com"
  }
});
// user: User (typed!)
```

---

## 🎯 Key Takeaways

✅ **Node.js + TypeScript** is production-ready

✅ **Express** has excellent type support

✅ **Fastify** is fastest with great types

✅ **Prisma** provides type-safe database access

✅ **Type safety** catches backend bugs early

---

[← Previous: Frontend Frameworks](./55_frontend_frameworks.md) | [Next: Common Pitfalls →](./57_pitfalls_antipatterns.md)

**Progress**: Topic 56 of 63 | Part IX: 100% Complete ✅
