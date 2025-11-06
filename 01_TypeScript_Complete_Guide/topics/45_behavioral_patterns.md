# 🎭 Behavioral Design Patterns - Object Interaction in TypeScript

[← Previous: Structural Patterns](./44_structural_patterns.md) | [← Back to Main](../README.md) | [Next: Functional Patterns →](./46_functional_patterns.md)

---

## 📝 Overview

Behavioral patterns focus on communication between objects and assignment of responsibilities. This guide covers Observer, Strategy, Command, and Iterator patterns in TypeScript.

### 🎯 Learning Objectives

- ✅ Implement Observer pattern
- ✅ Use Strategy pattern
- ✅ Apply Command pattern
- ✅ Understand Iterator pattern

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced
- **Estimated Time**: 5-6 hours
- **Prerequisites**: [Classes](./19_classes.md), [Functions](./11_functions.md)
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [Observer Pattern](#observer)
2. [Strategy Pattern](#strategy)
3. [Command Pattern](#command)
4. [Iterator Pattern](#iterator)

---

<a name="observer"></a>
## 1. Observer Pattern

```typescript
// Subject notifies observers of changes
interface Observer {
  update(data: unknown): void;
}

class Subject {
  private observers: Observer[] = [];
  
  attach(observer: Observer): void {
    this.observers.push(observer);
  }
  
  notify(data: unknown): void {
    this.observers.forEach(obs => obs.update(data));
  }
}

class ConcreteObserver implements Observer {
  update(data: unknown): void {
    console.log('Received:', data);
  }
}

// Usage
const subject = new Subject();
subject.attach(new ConcreteObserver());
subject.notify({ message: "Hello" });
```

---

<a name="strategy"></a>
## 2. Strategy Pattern

```typescript
// Interchangeable algorithms
interface SortStrategy {
  sort(data: number[]): number[];
}

class BubbleSort implements SortStrategy {
  sort(data: number[]): number[] {
    // Bubble sort implementation
    return data.sort((a, b) => a - b);
  }
}

class QuickSort implements SortStrategy {
  sort(data: number[]): number[] {
    // Quick sort implementation
    return data.sort((a, b) => a - b);
  }
}

class Sorter {
  constructor(private strategy: SortStrategy) {}
  
  setStrategy(strategy: SortStrategy): void {
    this.strategy = strategy;
  }
  
  sort(data: number[]): number[] {
    return this.strategy.sort(data);
  }
}

// Usage
const sorter = new Sorter(new BubbleSort());
sorter.sort([3, 1, 2]);
```

---

## 🎯 Key Takeaways

✅ **Observer**: Notify subscribers of changes

✅ **Strategy**: Sw
