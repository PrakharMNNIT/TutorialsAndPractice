# 🏛️ Classes in TypeScript - Object-Oriented Programming with Types

[← Previous: Type Assertions](./18_type_assertions.md) | [← Back to Main](../README.md) | [Next: Generics Introduction →](./20_generics_intro.md)

---

## 📝 Overview

Classes in TypeScript extend JavaScript's class syntax with powerful type features: access modifiers, abstract classes, interfaces, and more. This guide covers everything you need to write type-safe object-oriented code.

**What You'll Learn:**
- Class syntax and type annotations
- Access modifiers (public, private, protected)
- Readonly properties and parameter properties
- Abstract classes and methods
- Implementing interfaces
- Static members and methods
- Class inheritance and polymorphism

### 🎯 Learning Objectives

- ✅ Master TypeScript class syntax
- ✅ Use access modifiers appropriately
- ✅ Implement interfaces with classes
- ✅ Create abstract classes
- ✅ Use static members effectively
- ✅ Apply inheritance patterns
- ✅ Write type-safe OOP code

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced
- **Estimated Time**: 5-6 hours
- **Prerequisites**: [Objects & Interfaces](./12_objects_interfaces.md), [Functions](./11_functions.md)
- **Practice Exercises**: 15 challenges
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [Class Basics](#class-basics)
2. [Access Modifiers](#access-modifiers)
3. [Parameter Properties](#parameter-properties)
4. [Readonly Properties](#readonly-properties)
5. [Getters and Setters](#getters-setters)
6. [Static Members](#static-members)
7. [Abstract Classes](#abstract-classes)
8. [Implementing Interfaces](#implementing-interfaces)
9. [Inheritance](#inheritance)
10. [Best Practices](#best-practices)
11. [Higher-Order FAQs](#faqs)
12. [Interview Questions](#interview-questions)

---

<a name="class-basics"></a>
## 1. Class Basics

### 1.1 Basic Class Syntax

```typescript
// Basic class with types
class User {
  name: string;
  age: number;
  
  constructor(name: string, age: number) {
    this.name = name;
    this.age = age;
  }
  
  greet(): string {
    return `Hello, I'm ${this.name}`;
  }
}

const user = new User("Alice", 30);
console.log(user.greet()); // "Hello, I'm Alice"
```

**Version Tracking:**
- ✅ Classes (v1.0+) - Core OOP support
- 🆕 Private fields # (v3.8+) - ECMAScript private fields
- 🆕 Parameter properties (v1.0+) - Constructor shorthand
- 🆕 Abstract classes (v1.6+) - Base classes

---

<a name="access-modifiers"></a>
## 2. Access Modifiers

### 2.1 public, private, protected

```typescript
class BankAccount {
  public accountNumber: string;     // Accessible anywhere
  private balance: number;          // Only within class
  protected accountType: string;    // Within class and subclasses
  
  constructor(accountNumber: string, initialBalance: number) {
    this.accountNumber = accountNumber;
    this.balance = initialBalance;
    this.accountType = "checking";
  }
  
  public deposit(amount: number): void {
    this.balance += amount; // ✅ Can access private member
  }
  
  public getBalance(): number {
    return this.balance; // ✅ Can access private member
  }
}

const account = new BankAccount("123", 1000);
console.log(account.accountNumber); // ✅ OK (public)
// console.log(account.balance); // ❌ Error (private)
account.deposit(500); // ✅ OK (public method)
```

### 2.2 Private Fields (#) - ECMAScript

```typescript
class Modern {
  #privateField: string; // ECMAScript private
  
  constructor(value: string) {
    this.#privateField = value;
  }
  
  getValue(): string {
    return this.#privateField;
  }
}

const obj = new Modern("secret");
// console.log(obj.#privateField); // ❌ Syntax error (truly private)
```

---

<a name="parameter-properties"></a>
## 3. Parameter Properties

### 3.1 Constructor Shorthand

```typescript
// Traditional way
class User {
  name: string;
  age: number;
  
  constructor(name: string, age: number) {
    this.name = name;
    this.age = age;
  }
}

// Parameter properties (shorthand)
class User2 {
  constructor(
    public name: string,
    public age: number
  ) {
    // Properties automatically created and assigned
  }
}

// Mixed access modifiers
class Employee {
  constructor(
    public name: string,
    private salary: number,
    protected department: string
  ) {}
  
  getSalary(): number {
    return this.salary;
  }
}
```

---

<a name="readonly-properties"></a>
## 4. Readonly Properties

### 4.1 Readonly Modifier

```typescript
class Point {
  readonly x: number;
  readonly y: number;
  
  constructor(x: number, y: number) {
    this.x = x;
    this.y = y;
  }
  
  move(): void {
    // this.x = 10; // ❌ Error: Cannot assign to readonly
  }
}

// With parameter properties
class Point2 {
  constructor(
    public readonly x: number,
    public readonly y: number
  ) {}
}

const point = new Point2(10, 20);
// point.x = 15; // ❌ Error: Cannot assign to readonly
```

---

<a name="static-members"></a>
## 5. Static Members

### 5.1 Static Properties and Methods

```typescript
class MathUtils {
  static PI: number = 3.14159;
  static E: number = 2.71828;
  
  static add(a: number, b: number): number {
    return a + b;
  }
  
  static circle Area(radius: number): number {
    return this.PI * radius ** 2; // Access static via 'this'
  }
}

// Access without instance
console.log(MathUtils.PI); // 3.14159
console.log(MathUtils.add(5, 3)); // 8
console.log(MathUtils.circleArea(10)); // 314.159
```

---

<a name="abstract-classes"></a>
## 6. Abstract Classes

### 6.1 Abstract Base Classes

```typescript
// Abstract class - cannot be instantiated
abstract class Animal {
  constructor(protected name: string) {}
  
  // Abstract method - must be implemented by subclasses
  abstract makeSound(): string;
  
  // Concrete method
  move(): void {
    console.log(`${this.name} is moving`);
  }
}

// const animal = new Animal("Generic"); // ❌ Error: Cannot instantiate

class Dog extends Animal {
  makeSound(): string {
    return "Woof!";
  }
}

class Cat extends Animal {
  makeSound(): string {
    return "Meow!";
  }
}

const dog = new Dog("Rex");
console.log(dog.makeSound()); // "Woof!"
dog.move(); // "Rex is moving"
```

---

<a name="implementing-interfaces"></a>
## 7. Implementing Interfaces

### 7.1 Class Implements Interface

```typescript
interface Serializable {
  serialize(): string;
}

interface Deserializable {
  deserialize(data: string): void;
}

class User implements Serializable, Deserializable {
  constructor(
    public name: string,
    public age: number
  ) {}
  
  serialize(): string {
    return JSON.stringify({ name: this.name, age: this.age });
  }
  
  deserialize(data: string): void {
    const obj = JSON.parse(data);
    this.name = obj.name;
    this.age = obj.age;
  }
}

const user = new User("Alice", 30);
const json = user.serialize();
console.log(json); // {"name":"Alice","age":30}
```

---

<a name="inheritance"></a>
## 8. Inheritance

### 8.1 Extending Classes

```typescript
class Animal {
  constructor(protected name: string) {}
  
  move(distance: number): void {
    console.log(`${this.name} moved ${distance}m`);
  }
}

class Dog extends Animal {
  constructor(name: string, public breed: string) {
    super(name); // Call parent constructor
  }
  
  bark(): void {
    console.log("Woof!");
  }
  
  // Override parent method
  move(distance: number): void {
    console.log("Dog running...");
    super.move(distance); // Call parent method
  }
}

const dog = new Dog("Rex", "Labrador");
dog.bark(); // "Woof!"
dog.move(10); // "Dog running..." then "Rex moved 10m"
```

---

<a name="best-practices"></a>
## 9. Best Practices

### 9.1 Class Design Guidelines

```typescript
// ✅ GOOD: Use parameter properties for conciseness
class User {
  constructor(
    public readonly id: number,
    public name: string,
    private email: string
  ) {}
}

// ✅ GOOD: Use private for encapsulation
class Counter {
  private count = 0;
  
  increment(): number {
    return ++this.count;
  }
  
  getCount(): number {
    return this.count;
  }
}

// ✅ GOOD: Use abstract for base classes
abstract class Shape {
  abstract area(): number;
  abstract perimeter(): number;
}

// ❌ BAD: Public everything
class BadDesign {
  public data = {};
  public state = "loading";
  // Everything mutable and accessible
}
```

---

## 🧠 Higher-Order FAQs

### FAQ 1: TypeScript private vs JavaScript #private

**Q: TypeScript has `private` keyword and JavaScript has `#private` fields. What's the difference, and which should you use?**

<details>
<summary>View Detailed Answer</summary>

**Short Answer:** TypeScript `private` is compile-time only; `#private` is runtime enforced. Use `#private` for true encapsulation.

**Deep Explanation:**

**TypeScript private (Compile-Time):**

```typescript
class User {
  private password: string;
  
  constructor(password: string) {
    this.password = password;
  }
}

const user = new User("secret");
// user.password; // ❌ Compile error

// But at runtime (JavaScript):
console.log(user.password); // ✅ Works! "secret"
// TypeScript private is erased at runtime
```

**JavaScript #private (Runtime):**

```typescript
class User {
  #password: string;
  
  constructor(password: string) {
    this.#password = password;
  }
}

const user = new User("secret");
// user.#password; // ❌ Syntax error (truly private)
// Cannot access even with bracket notation
```

**Key Differences:**

| Feature | TypeScript private | JavaScript #private |
|---------|-------------------|---------------------|
| **Enforcement** | Compile-time only | Runtime |
| **Visibility** | Hidden in .d.ts | Truly invisible |
| **Performance** | No overhead | Slight overhead |
| **Compatibility** | Works everywhere | ES2022+ |
| **Debugging** | Visible in debugger | Hidden |
| **Subclass access** | Can use protected | Cannot access |

**When to Use Each:**

```typescript
// Use TypeScript private:
// - Need protected (subclass access)
// - Target older JavaScript
// - Debugging/testing access needed

class Base {
  private data = {};
  protected config = {};
}

class Derived extends Base {
  method() {
    // this.data; // ❌ Error
    this.config; // ✅ OK (protected)
  }
}

// Use JavaScript #private:
// - True encapsulation required
// - Modern JavaScript target
// - Security-sensitive data

class Secure {
  #apiKey: string;
  
  constructor(key: string) {
    this.#apiKey = key;
  }
  
  // No way to access #apiKey from outside
}
```

**Recommendation:**
- **Default**: Use TypeScript `private` (better DX, broader support)
- **Security**: Use `#private` for truly sensitive data
- **Legacy**: Use `private` for older JavaScript targets

</details>

---

## 🎯 Key Takeaways

✅ **Classes** combine data and behavior

✅ **Access modifiers** control visibility (public/private/protected)

✅ **Parameter properties** simplify constructor assignments

✅ **Abstract classes** define contracts for subclasses

✅ **Interfaces** define contracts for classes

✅ **Inheritance** enables code reuse

✅ **TypeScript private** is compile-time; `#private` is runtime

---

[← Previous: Type Assertions](./18_type_assertions.md) | [Next: Generics Introduction →](./20_generics_intro.md)

**Progress**: Topic 19 of 63 | Part III: 88% Complete
