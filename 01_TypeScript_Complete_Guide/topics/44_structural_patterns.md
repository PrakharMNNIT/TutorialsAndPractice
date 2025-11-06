# 🏛️ Structural Design Patterns - Composing Objects in TypeScript

[← Previous: Creational Patterns](./43_creational_patterns.md) | [← Back to Main](../README.md) | [Next: Behavioral Patterns →](./45_behavioral_patterns.md)

---

## 📝 Overview

Structural patterns focus on how classes and objects are composed to form larger structures. This guide covers Adapter, Decorator, Facade, and Proxy patterns in TypeScript.

### 🎯 Learning Objectives

- ✅ Implement Adapter pattern
- ✅ Use Decorator pattern
- ✅ Apply Facade pattern
- ✅ Understand Proxy pattern

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced
- **Estimated Time**: 5-6 hours
- **Prerequisites**: [Classes](./19_classes.md), [Interfaces](./12_objects_interfaces.md)
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [Adapter Pattern](#adapter)
2. [Decorator Pattern](#decorator)
3. [Facade Pattern](#facade)
4. [Proxy Pattern](#proxy)

---

<a name="adapter"></a>
## 1. Adapter Pattern

```typescript
// Adapt incompatible interfaces
interface ModernAPI {
  fetchData(): Promise<Data>;
}

class LegacyAPI {
  getData(callback: (data: Data) => void): void {
    // Old callback-based API
  }
}

class LegacyAdapter implements ModernAPI {
  constructor(private legacy: LegacyAPI) {}
  
  fetchData(): Promise<Data> {
    return new Promise((resolve) => {
      this.legacy.getData(resolve);
    });
  }
}

// Usage
const adapter = new LegacyAdapter(new LegacyAPI());
const data = await adapter.fetchData(); // Modern Promise API
```

---

<a name="decorator"></a>
## 2. Decorator Pattern

```typescript
// Add functionality dynamically
interface Coffee {
  cost(): number;
  description(): string;
}

class SimpleCoffee implements Coffee {
  cost() { return 5; }
  description() { return "Simple coffee"; }
}

class MilkDecorator implements Coffee {
  constructor(private coffee: Coffee) {}
  
  cost() { return this.coffee.cost() + 2; }
  description() { return `${this.coffee.description()}, milk`; }
}

// Usage
let coffee: Coffee = new SimpleCoffee();
coffee = new MilkDecorator(coffee);
console.log(coffee.description()); // "Simple coffee, milk"
console.log(coffee.cost()); // 7
```

---

<a name="facade"></a>
## 3. Facade Pattern

```typescript
// Simplify complex subsystem
class CPU {
  freeze(): void {}
  execute(): void {}
}

class Memory {
  load(): void {}
}

class HardDrive {
  read(): void {}
}

// Facade provides simple interface
class ComputerFacade {
  private cpu = new CPU();
  private memory = new Memory();
  private hd = new HardDrive();
  
  start(): void {
    this.cpu.freeze();
    this.memory.load();
    this.hd.read();
    this.cpu.execute();
  }
}

// Usage
const computer = new ComputerFacade();
computer.start(); // Simple interface hides complexity
```

---

## 🎯 Key Takeaways

✅ **Adapter**: Make incompatible interfaces work together

✅ **Decorator**: Add functionality dynamically

✅ **Facade**: Simplify complex subsystems

✅ **Proxy**: Control access to objects

✅ **Structural patterns** organize code relationships

---

[← Previous: Creational Patterns](./43_creational_patterns.md) | [Next: Behavioral Patterns →](./45_behavioral_patterns.md)

**Progress**: Topic 44 of 63 | Part VII: 50% Complete
