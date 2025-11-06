t a# 🧵 Web Workers - True Parallelism in JavaScript

[← Previous: Promises & Async](./39_promises_async.md) | [← Back to Main](../README.md) | [Next: Service Workers →](./41_service_workers.md)

---

## 📝 Overview

Web Workers enable true parallel execution by running JavaScript on separate threads. This guide covers using Web Workers with TypeScript for CPU-intensive tasks without blocking the main thread.

### 🎯 Learning Objectives

- ✅ Understand Web Worker architecture
- ✅ Create and communicate with workers
- ✅ Type Worker messages in TypeScript
- ✅ Handle worker errors
- ✅ Know when to use Workers

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐⭐ Advanced
- **Estimated Time**: 5-6 hours
- **Prerequisites**: [Concurrency Model](./37_concurrency_model.md)
- **Version**: TypeScript 5.7+, Web Workers API

---

## 📚 Table of Contents

1. [Web Worker Basics](#basics)
2. [TypeScript with Workers](#typescript-workers)
3. [Communication Patterns](#communication)
4. [Use Cases](#use-cases)
5. [Best Practices](#best-practices)

---

<a name="basics"></a>
## 1. Web Worker Basics

```typescript
// main.ts - Main thread
const worker = new Worker('worker.js');

worker.postMessage({ task: 'compute', data: [1, 2, 3] });

worker.onmessage = (event) => {
  console.log('Result:', event.data);
};

// worker.ts - Worker thread
self.onmessage = (event) => {
  const { task, data } = event.data;
  
  if (task === 'compute') {
    const result = heavyComputation(data);
    self.postMessage({ result });
  }
};
```

---

<a name="typescript-workers"></a>
## 2. TypeScript with Workers

```typescript
// Type-safe worker messages
interface WorkerMessage {
  task: 'compute' | 'process';
  data: number[];
}

interface WorkerResponse {
  result: number;
}

// Main thread
const worker = new Worker('worker.js');

worker.postMessage({
  task: 'compute',
  data: [1, 2, 3]
} as WorkerMessage);

worker.onmessage = (event: MessageEvent<WorkerResponse>) => {
  console.log(event.data.result); // Type-safe!
};
```

---

<a name="use-cases"></a>
## 3. Use Cases

```typescript
// CPU-intensive tasks
// - Image processing
// - Data parsing
// - Cryptography
// - Large calculations

// Example: Heavy computation
function fibonacci(n: number): number {
  if (n <= 1) return n;
  return fibonacci(n - 1) + fibonacci(n - 2);
}

// Run in worker to avoid blocking UI
```

---

## 🎯 Key Takeaways

✅ **Web Workers** run on separate threads

✅ **True parallelism** for CPU-intensive tasks

✅ **Message passing** for communication

✅ **TypeScript** can type worker messages

✅ **Use for** heavy computations

✅ **Don't use for** everything (overhead)

---

[← Previous: Promises & Async](./39_promises_async.md) | [Next: Service Workers →](./41_service_workers.md)

**Progress**: Topic 40 of 63 | Part VI: 67% Complete
