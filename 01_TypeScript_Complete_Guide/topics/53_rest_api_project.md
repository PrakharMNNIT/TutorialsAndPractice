# 🌐 Building a Type-Safe REST API - Express + TypeScript Project

[← Previous: Migration](./52_migration.md) | [← Back to Main](../README.md) | [Next: CLI Tool Project →](./54_cli_tool_project.md)

---

## 📝 Overview

Building a production-ready REST API with TypeScript and Express demonstrates real-world TypeScript application. This comprehensive project guide covers setup, routing, validation, error handling, and deployment.

### 🎯 Learning Objectives

- ✅ Set up Express with TypeScript
- ✅ Create type-safe routes
- ✅ Implement validation
- ✅ Handle errors properly
- ✅ Test and deploy API

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced
- **Estimated Time**: 8-10 hours
- **Prerequisites**: All previous core topics
- **Version**: Express 4+, TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [Project Setup](#setup)
2. [Type-Safe Routes](#routes)
3. [Validation](#validation)
4. [Error Handling](#errors)
5. [Testing](#testing)

---

<a name="setup"></a>
## 1. Project Setup

```bash
mkdir api-project && cd api-project
npm init -y
npm install express
npm install --save-dev typescript @types/node @types/express ts-node-dev

npx tsc --init
```

```typescript
// src/server.ts
import express from 'express';

const app = express();
app.use(express.json());

app.get('/health', (req, res) => {
  res.json({ status: 'ok' });
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
```

---

<a name="routes"></a>
## 2. Type-Safe Routes

```typescript
// Types
interface User {
  id: number;
  name: string;
  email: string;
}

interface CreateUserDto {
  name: string;
  email: string;
}

// Routes
app.get('/users', async (req, res) => {
  const users: User[] = await db.getUsers();
  res.json(users);
});

app.post('/users', async (req, res) => {
  const data: CreateUserDto = req.body;
  const user: User = await db.createUser(data);
  res.status(201).json(user);
});
```

---

<a name="validation"></a>
## 3. Validation

```typescript
// Using Zod for runtime validation
import { z } from 'zod';

const CreateUserSchema = z.object({
  name: z.string().min(1),
  email: z.string().email()
});

app.post('/users', async (req, res) => {
  try {
    const data = CreateUserSchema.parse(req.body);
    const user = await db.createUser(data);
    res.status(201).json(user);
  } catch (error) {
    res.status(400).json({ error: "Validation failed" });
  }
});
```

---

## 🎯 Key Takeaways

✅ **Type routes** for compile-time safety

✅ **Validate input** at runtime

✅ **Handle errors** consistently

✅ **Test APIs** thoroughly

✅ **Express + TypeScript** is production-ready

---

[← Previous: Migration](./52_migration.md) | [Next: CLI Tool Project →](./54_cli_tool_project.md)

**Progress**: Topic 53 of 63 | Part IX: 25% Complete
