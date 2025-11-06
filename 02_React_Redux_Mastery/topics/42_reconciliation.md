# Topic 42: Keys and Reconciliation Algorithm

[← Previous: React Profiler](./41_profiler.md) | [Back to Main](../README.md) | [Next: Bundle Optimization →](./43_bundle_optimization.md)

---

## Overview

**Reconciliation** is React's algorithm for diffing trees and efficiently updating the DOM. Understanding it helps write performant components.

---

## Reconciliation Basics

```tsx
// React compares trees to find minimal changes

// Old tree:
<div>
  <span>Hello</span>
</div>

// New tree:
<div>
  <span>Hi</span>
</div>

// React identifies: Only text changed
// Updates: textContent only
// Efficient!
```

---

## Keys Importance

```tsx
// Without keys: React compares by position
[<div>A</div>, <div>B</div>, <div>C</div>]
// Add at start:
[<div>Z</div>, <div>A</div>, <div>B</div>, <div>C</div>]

// React sees:
// Position 0: A → Z (UPDATE)
// Position 1: B → A (UPDATE)
// Position 2: C → B (UPDATE)
// Position 3: - → C (CREATE)
// Inefficient! All items updated

// With keys: React identifies by key
[<div key="a">A</div>, <div key="b">B</div>, <div key="c">C</div>]
[<div key="z">Z</div>, <div key="a">A</div>, <div key="b">B</div>, <div key="c">C</div>]

// React sees:
// key="z": new (CREATE)
// key="a", "b", "c": same, just moved (MOVE)
// Efficient!
```

---

## Algorithm Optimizations

```tsx
// React's reconciliation is O(n)
// Heuristics:
// 1. Different types = replace subtree
// 2. Keys identify moved elements
// 3. Siblings reconciled in order

// Write components to help React:
// - Stable keys
// - Consistent component types
// - Avoid changing element types
```

---

## Best Practices

```tsx
// ✅ Use stable unique keys
{items.map(item => <Item key={item.id} />)}

// ❌ Don't use index for dynamic lists
{items.map((item, i) => <Item key={i} />)}

// ✅ Keep element types consistent
condition ? <div /> : <span />  // ❌ Type changes
condition ? <div className="a" /> : <div className="b" />  // ✅ Same type
```

---

## Higher-Order Thinking FAQ

**Q: Why is React's reconciliation O(n) instead of O(n³)?**

**A:** React uses **heuristics** to avoid comparing every possible tree combination.

Without heuristics: Compare every old node with every new node = O(n³)

With heuristics:
- Same type elements = update props (O(1))
- Different type = replace subtree (O(1))
- Keys identify moves (O(n))
- Total: O(n)

Tradeoff: Might not find absolute optimal diff, but fast enough for real-time UI.

---

[← Previous: React Profiler](./41_profiler.md) | [Back to Main](../README.md) | [Next: Bundle Optimization →](./43_bundle_optimization.md)

---

**Progress**: Topic 42/220 | Part IV: 78% complete
