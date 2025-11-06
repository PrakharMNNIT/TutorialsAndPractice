# Topic 40: List Virtualization

[← Previous: Code Splitting](./39_code_splitting.md) | [Back to Main](../README.md) | [Next: React Profiler →](./41_profiler.md)

---

## Overview

**Virtualization** renders only visible items in large lists, dramatically improving performance for lists with thousands of items.

**What You'll Learn:** Virtualization concepts, react-window usage, when to virtualize, performance impact

**Prerequisites:** Lists and keys, performance basics

---

## What is Virtualization?

```tsx
// Without virtualization: Render all 10,000 items
function List({ items }) {
  return (
    <div style={{ height: '600px', overflow: 'auto' }}>
      {items.map(item => <Item key={item.id} item={item} />)}
    </div>
  );
}
// DOM: 10,000 nodes, slow scroll

// With virtualization: Render only ~20 visible items
import { FixedSizeList } from 'react-window';

function VirtualList({ items }) {
  return (
    <FixedSizeList
      height={600}
      itemCount={items.length}
      itemSize={50}
      width="100%"
    >
      {({ index, style }) => (
        <div style={style}>
          <Item item={items[index]} />
        </div>
      )}
    </FixedSizeList>
  );
}
// DOM: ~20 nodes, smooth scroll
```

---

## When to Virtualize

```tsx
// ✅ Use virtualization when:
// - List has 100+ items
// - Items are uniform size
// - Scrolling performance matters

// ❌ Don't virtualize when:
// - List has <50 items
// - Items vary wildly in size
// - SEO is critical (virtual items not in initial HTML)
```

---

## react-window Library

```tsx
import { FixedSizeList, VariableSizeList } from 'react-window';

// Fixed size items
<FixedSizeList
  height={600}
  itemCount={items.length}
  itemSize={50}
  width="100%"
>
  {({ index, style }) => (
    <div style={style}>{items[index].name}</div>
  )}
</FixedSizeList>

// Variable size items
<VariableSizeList
  height={600}
  itemCount={items.length}
  itemSize={(index) => items[index].height}  // Dynamic height
  width="100%"
>
  {({ index, style }) => (
    <div style={style}>{items[index].content}</div>
  )}
</VariableSizeList>
```

---

## Best Practices

```tsx
// ✅ 1. Use for large lists (>100 items)
// ✅ 2. Measure performance impact
// ✅ 3. Consider SEO implications
// ✅ 4. Handle dynamic item sizes carefully
```

---

## Higher-Order Thinking FAQ

**Q: Why doesn't React virtualize lists by default?**

**A:** Virtualization adds **complexity** and **constraints** that most lists don't need.

Tradeoffs:
- ✅ Performance for huge lists
- ❌ More complex API
- ❌ Fixed container height required
- ❌ Accessibility challenges (screen readers)
- ❌ SEO issues (items not in DOM)

React prioritizes **simplicity** by default, provides virtualization as opt-in when needed.

---

[← Previous: Code Splitting](./39_code_splitting.md) | [Back to Main](../README.md) | [Next: React Profiler →](./41_profiler.md)

---

**Progress**: Topic 40/220 | Part IV: 56% complete
