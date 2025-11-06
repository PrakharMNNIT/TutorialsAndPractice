# Topic 41: React Profiler and Performance Tracking

[← Previous: Virtualization](./40_virtualization.md) | [Back to Main](../README.md) | [Next: Reconciliation →](./42_reconciliation.md)

---

## Overview

**React Profiler** API and DevTools help measure rendering performance and identify bottlenecks.

**Key Concepts:** Profiler component, DevTools Profiler, performance metrics, optimization verification

---

## Profiler Component

```tsx
import { Profiler } from 'react';

function App() {
  return (
    <Profiler id="Navigation" onRender={callback}>
      <Navigation />
    </Profiler>
  );
}

function callback(
  id,                // Profiler id
  phase,             // "mount" or "update"  
  actualDuration,    // Time spent rendering
  baseDuration,      // Time without memoization
  startTime,         // When render started
  commitTime         // When committed
) {
  console.log(`${id} ${phase}: ${actualDuration}ms`);
  
  if (actualDuration > 16) {
    console.warn(`Slow render: ${id}`);
  }
}
```

---

## DevTools Profiler

```tsx
// Usage:
// 1. Open DevTools → Profiler tab
// 2. Click record
// 3. Interact with app
// 4. Stop recording
// 5. Analyze flame graph

// Identify:
// - Slow components (red/orange)
// - Unnecessary renders
// - Render frequency
```

---

## Performance Metrics

```tsx
// Key metrics to track:
const metrics = {
  renderTime: actualDuration,      // How long render took
  renderCount: 0,                  // How many times rendered
  wastedRenders: 0,                // Renders with no changes
  baseDuration: baseDuration       // Potential without optimization
};

// baseDuration > actualDuration = memoization helping
// actualDuration > 16ms = slower than 60fps
```

---

## Best Practices

```tsx
// ✅ 1. Profile in production mode
// Development is slower (extra checks)

// ✅ 2. Profile user interactions
// Not just initial render

// ✅ 3. Set performance budgets
const RENDER_BUDGET = 16;  // 60fps

// ✅ 4. Monitor over time
// Performance can degrade
```

---

## Higher-Order Thinking FAQ

**Q: Why measure performance in production mode, not development?**

**A:** Development mode has **overhead** for better DX.

Development includes:
- Prop type checking
- Extra warnings
- Strict Mode double rendering
- DEV-only code paths

Can be 2-10x slower than production! Always profile production build for accurate results.

---

[← Previous: Virtualization](./40_virtualization.md) | [Back to Main](../README.md) | [Next: Reconciliation →](./42_reconciliation.md)

---

**Progress**: Topic 41/220 | Part IV: 67% complete
