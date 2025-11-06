# 💼 Interview Preparation - TypeScript Interview Questions & Answers

[← Previous: Pitfalls](./57_pitfalls_antipatterns.md) | [← Back to Main](../README.md) | [Next: Type Challenges →](./59_type_challenges.md)

---

## 📝 Overview

Preparing for TypeScript interviews requires understanding concepts deeply and articulating them clearly. This comprehensive guide covers common interview questions with detailed answers.

### 🎯 Learning Objectives

- ✅ Answer core TypeScript questions
- ✅ Explain advanced concepts
- ✅ Solve coding challenges
- ✅ Discuss design decisions

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced
- **Estimated Time**: 6-8 hours
- **Prerequisites**: All previous topics
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [Fundamental Questions](#fundamentals)
2. [Advanced Type System](#advanced)
3. [Practical Scenarios](#practical)
4. [System Design](#system-design)

---

<a name="fundamentals"></a>
## 1. Fundamental Questions

**Q1: What is TypeScript and why use it?**

**A:** TypeScript is a statically-typed superset of JavaScript that adds compile-time type checking. Benefits: early error detection, better IDE support, self-documenting code, safer refactoring, and improved maintainability for large codebases.

**Q2: Explain the difference between interface and type.**

**A:** Both define object shapes, but:
- Interfaces: Extendable, mergeable, better for objects and OOP
- Types: Can represent primitives, unions, tuples, mapped types
- Use interfaces for object contracts, types for complex type logic

---

<a name="advanced"></a>
## 2. Advanced Type System

**Q3: Explain conditional types with an example.**

**A:** Conditional types enable type-level branching:

```typescript
type IsString<T> = T extends string ? true : false;
type T1 = IsString<string>; // true
type T2 = IsString<number>; // false

// Real use: Extract return type
type ReturnType<T> = T extends (...args: any[]) => infer R ? R : never;
```

**Q4: What are mapped types?**

**A:** Mapped types transform properties:

```typescript
type Readonly<T> = {
  readonly [K in keyof T]: T[K];
};

// Makes all properties readonly
```

---

<a name="practical"></a>
## 3. Practical Scenarios

**Q5: How would you type a REST API client?**

**A:** Define API schema with endpoints and methods:

```typescript
interface API {
  "/users": {
    GET: { response: User[] };
    POST: { body: CreateUserDto; response: User };
  };
}

// Type-safe client using generics...
```

---

## 🎯 Key Takeaways

✅ **Understand fundamentals** deeply

✅ **Explain concepts** clearly

✅ **Provide examples** for all answers

✅ **Discuss trade-offs** in design decisions

✅ **Practice coding** interview questions

---

[← Previous: Pitfalls](./57_pitfalls_antipatterns.md) | [Next: Type Challenges →](./59_type_challenges.md)

**Progress**: Topic 58 of 63 | Part X: 29% Complete
