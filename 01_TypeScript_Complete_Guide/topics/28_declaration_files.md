# 📄 Declaration Files (.d.ts) - Type Definitions Without Implementation

[← Previous: Discriminated Unions](./27_discriminated_unions.md) | [← Back to Main](../README.md) | [Next: Modules & Namespaces →](./29_modules_namespaces.md)

---

## 📝 Overview

Declaration files (.d.ts) provide type information for JavaScript code without including implementation. They're essential for using JavaScript libraries with TypeScript, publishing TypeScript libraries, and achieving type safety across module boundaries.

**What You'll Learn:**
- What declaration files are and why they exist
- Creating declaration files
- DefinitelyTyped and @types packages
- Ambient declarations
- Module augmentation
- Publishing types with libraries

### 🎯 Learning Objectives

- ✅ Understand declaration file purpose
- ✅ Create .d.ts files
- ✅ Use @types packages
- ✅ Write ambient declarations
- ✅ Augment third-party types
- ✅ Publish libraries with types

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced
- **Estimated Time**: 4-5 hours
- **Prerequisites**: [Modules](./09_basic_types.md), [Interfaces](./12_objects_interfaces.md)
- **Practice Exercises**: 10 challenges
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [What Are Declaration Files?](#what-are)
2. [Creating Declaration Files](#creating)
3. [DefinitelyTyped & @types](#definitelytyped)
4. [Ambient Declarations](#ambient)
5. [Module Augmentation](#module-augmentation)
6. [Publishing with Types](#publishing)
7. [Best Practices](#best-practices)
8. [Higher-Order FAQs](#faqs)
9. [Interview Questions](#interview-questions)

---

<a name="what-are"></a>
## 1. What Are Declaration Files?

### 1.1 Purpose of .d.ts Files

```typescript
// Implementation file (math.ts)
export function add(a: number, b: number): number {
  return a + b;
}

// Declaration file (math.d.ts) - types only
export declare function add(a: number, b: number): number;

// No implementation, just type signatures
```

**When Declaration Files Are Used:**

| Scenario | Purpose |
|----------|---------|
| JavaScript libraries | Provide types for untyped JS |
| Published TypeScript libraries | Types without source code |
| Global definitions | Define browser/Node APIs |
| Module augmentation | Extend third-party types |

**Version Tracking:**
- ✅ Declaration files (v1.0+) - Core feature
- 🆕 Triple-slash directives (v1.0+)
- 🆕 Module augmentation (v1.8+)
- 🆕 UMD module definitions (v1.8+)

---

<a name="creating"></a>
## 2. Creating Declaration Files

### 2.1 Manual Declaration Files

```typescript
// mylib.js - JavaScript library
function greet(name) {
  return `Hello, ${name}!`;
}

class Calculator {
  add(a, b) {
    return a + b;
  }
}

module.exports = { greet, Calculator };

// mylib.d.ts - Type declarations
export function greet(name: string): string;

export class Calculator {
  add(a: number, b: number): number;
}
```

### 2.2 Auto-Generated Declarations

```typescript
// tsconfig.json
{
  "compilerOptions": {
    "declaration": true,          // Generate .d.ts files
    "declarationMap": true,       // Generate .d.ts.map for navigation
    "emitDeclarationOnly": false  // Generate both .js and .d.ts
  }
}

// source.ts
export function double(x: number): number {
  return x * 2;
}

// Generated source.d.ts
export declare function double(x: number): number;
```

---

<a name="definitelytyped"></a>
## 3. DefinitelyTyped & @types

### 3.1 Using @types Packages

```bash
# Install types for JavaScript library
npm install --save-dev @types/node
npm install --save-dev @types/express
npm install --save-dev @types/react

# Types are automatically recognized by TypeScript
```

```typescript
// Now you have types!
import express from 'express';

const app = express(); // Fully typed!

app.get('/users', (req, res) => {
  // req and res are typed
  res.json({ users: [] });
});
```

### 3.2 Finding Type Definitions

```typescript
// Check if types exist
// 1. Look for index.d.ts in library
// 2. Check for "types" field in package.json
// 3. Search DefinitelyTyped: https://www.npmjs.com/~types

// If no types exist, create them or use any
declare module 'untyped-library' {
  export function doSomething(): void;
}
```

---

<a name="ambient"></a>
## 4. Ambient Declarations

### 4.1 Global Declarations

```typescript
// globals.d.ts - Ambient declarations
declare const API_URL: string;
declare const VERSION: number;

declare function gtag(
  command: string,
  action: string,
  params?: Record<string, any>
): void;

// Usage (no import needed)
console.log(API_URL);
gtag('event', 'click', { button: 'submit' });
```

### 4.2 Declaring Modules

```typescript
// Declare module for untyped library
declare module 'legacy-library' {
  export function oldFunction(): void;
  export class OldClass {
    constructor(value: string);
    method(): number;
  }
}

// Now can import with types
import { oldFunction, OldClass } from 'legacy-library';
```

---

<a name="module-augmentation"></a>
## 5. Module Augmentation

### 5.1 Extending Third-Party Types

```typescript
// Extend Express Request
import express from 'express';

declare module 'express-serve-static-core' {
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

### 5.2 Global Augmentation

```typescript
// Extend global Window
declare global {
  interface Window {
    myCustomProperty: string;
    myCustomMethod(): void;
  }
}

// Usage
window.myCustomProperty = "value";
window.myCustomMethod();
```

---

<a name="publishing"></a>
## 6. Publishing with Types

### 6.1 Library Package Configuration

```json
// package.json for TypeScript library
{
  "name": "my-library",
  "version": "1.0.0",
  "main": "dist/index.js",
  "types": "dist/index.d.ts",
  "files": [
    "dist"
  ],
  "scripts": {
    "build": "tsc"
  }
}
```

```typescript
// tsconfig.json
{
  "compilerOptions": {
    "declaration": true,
    "outDir": "./dist",
    "rootDir": "./src"
  }
}

// src/index.ts
export function greet(name: string): string {
  return `Hello, ${name}!`;
}

// Generated dist/index.d.ts
export declare function greet(name: string): string;
```

---

<a name="best-practices"></a>
## 7. Best Practices

### 7.1 Declaration File Guidelines

```typescript
// ✅ GOOD: Use declare for ambient
declare const CONFIG: AppConfig;

// ✅ GOOD: Provide complete type information
declare function process(
  data: unknown,
  options?: ProcessOptions
): Promise<Result>;

// ✅ GOOD: Document with JSDoc
/**
 * Processes data with given options
 * @param data - Input data
 * @param options - Processing options
 * @returns Processed result
 */
declare function process(
  data: unknown,
  options?: ProcessOptions
): Promise<Result>;

// ❌ BAD: Using any unnecessarily
declare function bad(data: any): any;

// ✅ BETTER: Use unknown or specific types
declare function good(data: unknown): Result;
```

---

## 🎯 Key Takeaways

✅ **Declaration files** provide types without implementation

✅ **.d.ts files** enable TypeScript to understand JavaScript

✅ **@types packages** from DefinitelyTyped provide common library types

✅ **Ambient declarations** define global types

✅ **Module augmentation** extends third-party types

✅ **Publishing libraries** with types improves DX

✅ **Auto-generate** declarations from TypeScript source

---

[← Previous: Discriminated Unions](./27_discriminated_unions.md) | [Next: Modules & Namespaces →](./29_modules_namespaces.md)

**Progress**: Topic 28 of 63 | Part IV: 80% Complete
