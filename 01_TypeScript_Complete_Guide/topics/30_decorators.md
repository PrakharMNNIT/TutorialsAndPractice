# ✨ Decorators - Metaprogramming in TypeScript

[← Previous: Modules & Namespaces](./29_modules_namespaces.md) | [← Back to Main](../README.md) | [Next: tsconfig.json Guide →](./31_tsconfig.md)

---

## 📝 Overview

Decorators are a powerful metaprogramming feature that allows you to modify classes, methods, properties, and parameters at design time. While still experimental in JavaScript, TypeScript has provided decorators for years, making them widely used in frameworks like Angular and NestJS.

**What You'll Learn:**
- What decorators are and how they work
- Class, method, property, and parameter decorators
- Decorator factories
- Decorator composition
- Practical decorator patterns
- Stage 3 decorators vs legacy decorators

### 🎯 Learning Objectives

- ✅ Understand decorator concepts
- ✅ Create class decorators
- ✅ Use method decorators
- ✅ Apply property decorators
- ✅ Implement decorator factories
- ✅ Compose multiple decorators
- ✅ Apply decorators in real scenarios

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐⭐ Advanced
- **Estimated Time**: 4-5 hours
- **Prerequisites**: [Classes](./19_classes.md), [Functions](./11_functions.md)
- **Practice Exercises**: 12 challenges
- **Version**: TypeScript 5.7+ (Stage 3 decorators in v5.0+)

---

## 📚 Table of Contents

1. [What Are Decorators?](#what-are)
2. [Class Decorators](#class-decorators)
3. [Method Decorators](#method-decorators)
4. [Property Decorators](#property-decorators)
5. [Parameter Decorators](#parameter-decorators)
6. [Decorator Factories](#decorator-factories)
7. [Decorator Composition](#composition)
8. [Practical Patterns](#practical-patterns)
9. [Stage 3 vs Legacy](#stage3-vs-legacy)
10. [Best Practices](#best-practices)
11. [Higher-Order FAQs](#faqs)
12. [Interview Questions](#interview-questions)

---

<a name="what-are"></a>
## 1. What Are Decorators?

### 1.1 Enabling Decorators

```json
// tsconfig.json
{
  "compilerOptions": {
    "experimentalDecorators": true,  // Legacy decorators
    // OR
    "experimentalDecorators": false  // Stage 3 decorators (TS 5.0+)
  }
}
```

### 1.2 Basic Concept

```typescript
// Decorator is just a function
function log(target: any) {
  console.log('Class decorated:', target);
}

// Apply with @ syntax
@log
class MyClass {
  // ...
}

// Think of it as:
// MyClass = log(MyClass) || MyClass;
```

**Version Tracking:**
- ✅ Legacy decorators (v1.5+) - experimentalDecorators
- 🆕 Stage 3 decorators (v5.0+) - ECMAScript standard
- ⚠️ Breaking changes between legacy and Stage 3

---

<a name="class-decorators"></a>
## 2. Class Decorators

### 2.1 Basic Class Decorator

```typescript
// Class decorator function
function sealed(constructor: Function) {
  Object.seal(constructor);
  Object.seal(constructor.prototype);
}

@sealed
class User {
  name: string;
  constructor(name: string) {
    this.name = name;
  }
}

// Class is now sealed (can't add properties)
```

### 2.2 Class Decorator with Return

```typescript
// Decorator that replaces class
function addTimestamp<T extends { new(...args: any[]): {} }>(
  constructor: T
) {
  return class extends constructor {
    timestamp = new Date();
  };
}

@addTimestamp
class Document {
  title: string;
  constructor(title: string) {
    this.title = title;
  }
}

const doc = new Document("My Doc");
console.log(doc.timestamp); // Date object added!
```

---

<a name="method-decorators"></a>
## 3. Method Decorators

### 3.1 Basic Method Decorator

```typescript
// Method decorator
function log(
  target: any,
  propertyKey: string,
  descriptor: PropertyDescriptor
) {
  const original Method = descriptor.value;
  
  descriptor.value = function(...args: any[]) {
    console.log(`Calling ${propertyKey} with`, args);
    const result = originalMethod.apply(this, args);
    console.log(`Result:`, result);
    return result;
  };
  
  return descriptor;
}

class Calculator {
  @log
  add(a: number, b: number): number {
    return a + b;
  }
}

const calc = new Calculator();
calc.add(5, 3);
// Logs: Calling add with [5, 3]
// Logs: Result: 8
```

### 3.2 Practical Method Decorators

```typescript
// Measure execution time
function measureTime(
  target: any,
  propertyKey: string,
  descriptor: PropertyDescriptor
) {
  const originalMethod = descriptor.value;
  
  descriptor.value = async function(...args: any[]) {
    const start = performance.now();
    const result = await originalMethod.apply(this, args);
    const end = performance.now();
    console.log(`${propertyKey} took ${end - start}ms`);
    return result;
  };
  
  return descriptor;
}

class DataService {
  @measureTime
  async fetchUsers(): Promise<User[]> {
    // Fetch users
    return [];
  }
}
```

---

<a name="property-decorators"></a>
## 4. Property Decorators

### 4.1 Basic Property Decorator

```typescript
// Property decorator
function readonly(target: any, propertyKey: string) {
  Object.defineProperty(target, propertyKey, {
    writable: false
  });
}

class User {
  @readonly
  id: number = 1;
  
  name: string = "Alice";
}

const user = new User();
// user.id = 2; // ❌ Runtime error: cannot assign to readonly
user.name = "Bob"; // ✅ OK
```

---

<a name="decorator-factories"></a>
## 5. Decorator Factories

### 5.1 Parameterized Decorators

```typescript
// Decorator factory - returns decorator
function min(minValue: number) {
  return function(target: any, propertyKey: string) {
    let value: number;
    
    Object.defineProperty(target, propertyKey, {
      get() {
        return value;
      },
      set(newValue: number) {
        if (newValue < minValue) {
          throw new Error(`${propertyKey} must be >= ${minValue}`);
        }
        value = newValue;
      }
    });
  };
}

class Product {
  @min(0)
  price: number = 0;
  
  @min(1)
  quantity: number = 1;
}

const product = new Product();
// product.price = -10; // ❌ Error: price must be >= 0
product.price = 99; // ✅ OK
```

---

<a name="composition"></a>
## 6. Decorator Composition

### 6.1 Multiple Decorators

```typescript
// Decorators execute in order: bottom to top
function first() {
  console.log("first(): factory");
  return function(target: any) {
    console.log("first(): called");
  };
}

function second() {
  console.log("second(): factory");
  return function(target: any) {
    console.log("second(): called");
  };
}

@first()
@second()
class Example {}

// Output:
// first(): factory
// second(): factory
// second(): called
// first(): called
```

---

<a name="practical-patterns"></a>
## 7. Practical Patterns

### 7.1 Logging Decorator

```typescript
// Auto-log method calls
function autoLog(
  target: any,
  propertyKey: string,
  descriptor: PropertyDescriptor
) {
  const originalMethod = descriptor.value;
  
  descriptor.value = function(...args: any[]) {
    console.log(`[${propertyKey}] Called with:`, args);
    try {
      const result = originalMethod.apply(this, args);
      console.log(`[${propertyKey}] Returned:`, result);
      return result;
    } catch (error) {
      console.error(`[${propertyKey}] Error:`, error);
      throw error;
    }
  };
}

class UserService {
  @autoLog
  createUser(name: string): User {
    return { name, id: Date.now() };
  }
}
```

---

<a name="stage3-vs-legacy"></a>
## 8. Stage 3 vs Legacy Decorators

### 8.1 Key Differences

```typescript
// Legacy decorators (experimentalDecorators: true)
function legacyDecorator(target: any, context: any) {
  // ...
}

// Stage 3 decorators (TS 5.0+, experimentalDecorators: false)
function stage3Decorator(target: any, context: ClassDecoratorContext) {
  // context provides more information
  // Different signature and behavior
}

// ⚠️ NOT compatible - must choose one or the other
```

**Recommendation**: Use Stage 3 for new projects (ECMAScript standard), legacy for existing Angular/NestJS projects.

---

<a name="best-practices"></a>
## 9. Best Practices

### 9.1 Decorator Guidelines

```typescript
// ✅ GOOD: Use factories for configuration
function validate(schema: Schema) {
  return function(target: any, propertyKey: string) {
    // Use schema for validation
  };
}

// ✅ GOOD: Document decorator behavior
/**
 * Logs method execution time
 * @param target - Class prototype
 * @param propertyKey - Method name
 * @param descriptor - Property descriptor
 */
function measureTime(...) {}

// ❌ BAD: Side effects in decorator
function badDecorator(target: any) {
  makeAPICall(); // Don't do async operations!
}

// ✅ GOOD: Decorators should be pure transformations
```

---

## 🎯 Key Takeaways

✅ **Decorators** modify classes/methods/properties at design time

✅ **Enable with** experimentalDecorators (legacy) or Stage 3 mode

✅ **Class decorators** modify/replace classes

✅ **Method decorators** wrap method behavior

✅ **Decorator factories** accept parameters

✅ **Composition** allows multiple decorators

✅ **Stage 3** is the future standard

---

[← Previous: Modules & Namespaces](./29_modules_namespaces.md) | [Next: tsconfig.json Guide →](./31_tsconfig.md)

**Progress**: Topic 30 of 63 | Part IV: 100% Complete ✅
