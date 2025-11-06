# ✅ Testing TypeScript Code - Jest, Vitest & Type Testing

[← Previous: Bundlers](./34_bundlers.md) | [← Back to Main](../README.md) | [Next: Debugging →](./36_debugging.md)

---

## 📝 Overview

Testing TypeScript code requires both runtime testing (Jest/Vitest) and type testing. This guide covers setting up modern testing frameworks, writing type-safe tests, and ensuring your types are correct at compile time.

**What You'll Learn:**
- Jest with TypeScript
- Vitest (modern, faster)
- Type testing strategies
- Mocking with types
- Test coverage for TypeScript

### 🎯 Learning Objectives

- ✅ Set up Jest/Vitest with TypeScript
- ✅ Write type-safe tests
- ✅ Test types themselves
- ✅ Mock TypeScript modules
- ✅ Measure test coverage

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced
- **Estimated Time**: 5-6 hours
- **Prerequisites**: [TypeScript Setup](./07_typescript_setup.md)
- **Version**: Jest 29+, Vitest 1+ (2025)

---

## 📚 Table of Contents

1. [Jest with TypeScript](#jest)
2. [Vitest (Recommended)](#vitest)
3. [Type Testing](#type-testing)
4. [Mocking](#mocking)
5. [Best Practices](#best-practices)

---

<a name="jest"></a>
## 1. Jest with TypeScript

### 1.1 Setup

```bash
npm install --save-dev jest @types/jest ts-jest
```

```javascript
// jest.config.js
module.exports = {
  preset: 'ts-jest',
  testEnvironment: 'node',
  roots: ['<rootDir>/src'],
  testMatch: ['**/__tests__/**/*.ts', '**/?(*.)+(spec|test).ts'],
  moduleNameMapper: {
    '^@/(.*)$': '<rootDir>/src/$1'
  }
};
```

```typescript
// example.test.ts
import { add } from './math';

describe('add', () => {
  it('adds two numbers', () => {
    expect(add(2, 3)).toBe(5);
  });
  
  it('handles negative numbers', () => {
    expect(add(-1, -2)).toBe(-3);
  });
});
```

---

<a name="vitest"></a>
## 2. Vitest (Recommended)

### 2.1 Modern Testing

```bash
npm install --save-dev vitest
```

```typescript
// vitest.config.ts
import { defineConfig } from 'vitest/config';

export default defineConfig({
  test: {
    globals: true,
    environment: 'node'
  }
});
```

**Why Vitest?**
- ⚡ 10-100x faster than Jest
- 🎯 Native TypeScript support
- 🔥 Watch mode with HMR
- ✅ Jest-compatible API

---

<a name="type-testing"></a>
## 3. Type Testing

### 3.1 Testing Types

```typescript
// Type-level tests
import { expectType, expectError } from 'tsd';

// Test type is correct
const value: string = "hello";
expectType<string>(value);

// Test type error occurs
// @ts-expect-error
const invalid: number = "string";

// Using tsd library
import { add } from './math';

expectType<number>(add(1, 2));
expectError(add("1", 2));
```

---

<a name="best-practices"></a>
## 4. Best Practices

```typescript
// ✅ GOOD: Type test utilities
expect(result).toBe(5);
expectType<number>(result);

// ✅ GOOD: Mock with types
const mockFn = vi.fn<[number], string>();

// ✅ GOOD: Test both runtime and types
describe('type-safe function', () => {
  it('returns correct value', () => {
    const result = myFunction(5);
    expect(result).toBe(10);
    expectType<number>(result);
  });
});
```

---

## 🎯 Key Takeaways

✅ **Vitest** recommended for new projects (faster)

✅ **Jest** mature with huge ecosystem

✅ **Type testing** ensures types are correct

✅ **Mock with types** for better safety

✅ **Test coverage** should include TypeScript files

---

[← Previous: Bundlers](./34_bundlers.md) | [Next: Debugging →](./36_debugging.md)

**Progress**: Topic 35 of 63 | Part V: 83% Complete
