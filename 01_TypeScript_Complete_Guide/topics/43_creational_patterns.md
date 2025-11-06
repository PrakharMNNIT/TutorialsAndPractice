# 🏗️ Creational Design Patterns - Object Creation in TypeScript

[← Previous: JS vs Java](./42_js_vs_java_concurrency.md) | [← Back to Main](../README.md) | [Next: Structural Patterns →](./44_structural_patterns.md)

---

## 📝 Overview

Creational design patterns provide solutions for object creation, hiding creation logic and making systems more flexible. This guide covers implementing Singleton, Factory, Builder, and Prototype patterns in TypeScript with type safety.

### 🎯 Learning Objectives

- ✅ Implement Singleton pattern
- ✅ Use Factory pattern
- ✅ Apply Builder pattern
- ✅ Understand Prototype pattern
- ✅ Choose appropriate patterns

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced
- **Estimated Time**: 5-6 hours
- **Prerequisites**: [Classes](./19_classes.md), [Generics](./20_generics_intro.md)
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [Singleton Pattern](#singleton)
2. [Factory Pattern](#factory)
3. [Builder Pattern](#builder)
4. [Prototype Pattern](#prototype)
5. [Best Practices](#best-practices)

---

<a name="singleton"></a>
## 1. Singleton Pattern

```typescript
// Ensure only one instance exists
class DatabaseConnection {
  private static instance: DatabaseConnection;
  private constructor() {} // Private constructor
  
  static getInstance(): DatabaseConnection {
    if (!DatabaseConnection.instance) {
      DatabaseConnection.instance = new DatabaseConnection();
    }
    return DatabaseConnection.instance;
  }
  
  query(sql: string): void {
    console.log(`Executing: ${sql}`);
  }
}

// Usage
const db1 = DatabaseConnection.getInstance();
const db2 = DatabaseConnection.getInstance();
console.log(db1 === db2); // true (same instance)
```

---

<a name="factory"></a>
## 2. Factory Pattern

```typescript
// Factory creates objects based on input
interface Animal {
  speak(): string;
}

class Dog implements Animal {
  speak() { return "Woof!"; }
}

class Cat implements Animal {
  speak() { return "Meow!"; }
}

class AnimalFactory {
  static create(type: "dog" | "cat"): Animal {
    switch (type) {
      case "dog": return new Dog();
      case "cat": return new Cat();
    }
  }
}

// Usage
const dog = AnimalFactory.create("dog");
console.log(dog.speak()); // "Woof!"
```

---

<a name="builder"></a>
## 3. Builder Pattern

```typescript
// Builder for complex object construction
class User {
  constructor(
    public name: string,
    public email: string,
    public age?: number,
    public phone?: string
  ) {}
}

class UserBuilder {
  private name!: string;
  private email!: string;
  private age?: number;
  private phone?: string;
  
  setName(name: string): this {
    this.name = name;
    return this;
  }
  
  setEmail(email: string): this {
    this.email = email;
    return this;
  }
  
  setAge(age: number): this {
    this.age = age;
    return this;
  }
  
  setPhone(phone: string): this {
    this.phone = phone;
    return this;
  }
  
  build(): User {
    return new User(this.name, this.email, this.age, this.phone);
  }
}

// Usage
const user = new UserBuilder()
  .setName("Alice")
  .setEmail("alice@example.com")
  .setAge(30)
  .build();
```

---

## 🎯 Key Takeaways

✅ **Singleton**: One instance only

✅ **Factory**: Create objects based on criteria

✅ **Builder**: Construct complex objects step-by-step

✅ **Prototype**: Clone existing objects

✅ **TypeScript** adds type safety to patterns

---

[← Previous: JS vs Java](./42_js_vs_java_concurrency.md) | [Next: Structural Patterns →](./44_structural_patterns.md)

**Progress**: Topic 43 of 63 | Part VII: 25% Complete
