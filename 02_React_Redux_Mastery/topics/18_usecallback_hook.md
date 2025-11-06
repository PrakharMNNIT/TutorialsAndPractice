# Topic 18: useCallback Hook - Function Memoization

[← Previous: useMemo Hook](./17_usememo_hook.md) | [Back to Main](../README.md) | [Next: Custom Hooks →](./19_custom_hooks.md)

---

## Table of Contents

1. [Overview](#overview)
2. [What is useCallback?](#what-is-usecallback)
3. [useCallback Basics](#usecallback-basics)
4. [useCallback vs useMemo](#usecallback-vs-usememo)
5. [When to Use useCallback](#when-to-use-usecallback)
6. [Performance Optimization](#performance-optimization)
7. [Dependency Management](#dependency-management)
8. [Common Use Cases](#common-use-cases)
9. [TypeScript with useCallback](#typescript-with-usecallback)
10. [React Compiler Impact](#react-compiler-impact)
11. [Best Practices](#best-practices)

---

## Overview

**useCallback** is a Hook that memoizes a function definition. It returns a memoized version of the callback that only changes when dependencies change, optimizing performance by preventing unnecessary function recreations.

**What You'll Learn:**
- What useCallback is and why it exists
- Difference between useCallback and useMemo
- When to use useCallback (and when not to)
- Preventing child component re-renders
- Dependency management for callbacks
- React Compiler's impact
- Common pitfalls and solutions

**Prerequisites:**
- useMemo Hook understanding
- React.memo and component optimization
- JavaScript closures and references

**Version Coverage:**
- React 16.8+ (useCallback introduction)
- React 19 (React Compiler makes useCallback often unnecessary)
- React 19.2

---

## What is useCallback?

### Core Concept

**useCallback** memoizes a **function definition**, not its result. It returns the same function reference across renders when dependencies don't change.

```tsx
// Without useCallback: New function every render
function Component() {
  const [count, setCount] = useState(0);
  
  const handleClick = () => {
    console.log('Clicked');
  };
  // handleClick is a NEW function on every render
  
  return <button onClick={handleClick}>Click</button>;
}

// With useCallback: Same function reference
function Component() {
  const [count, setCount] = useState(0);
  
  const handleClick = useCallback(() => {
    console.log('Clicked');
  }, []);  // Dependencies
  // handleClick is the SAME function across renders
  
  return <button onClick={handleClick}>Click</button>;
}
```

### Why Functions Recreate

```tsx
function Component() {
  const [count, setCount] = useState(0);
  
  // Every render creates NEW function
  const handleClick = () => {
    console.log(count);
  };
  
  console.log(handleClick);
  // Render 1: ƒ handleClick() { ... }  // Reference A
  // Render 2: ƒ handleClick() { ... }  // Reference B (different!)
  // Render 3: ƒ handleClick() { ... }  // Reference C (different!)
  
  // handleClick !== previousHandleClick
}
```

---

## useCallback Basics

### Basic Syntax

```tsx
import { useCallback } from 'react';

const memoizedCallback = useCallback(
  () => {
    // Function body
    doSomething(a, b);
  },
  [a, b]  // Dependencies
);

// Returns same function reference when a and b don't change
```

### Simple Example

```tsx
function Component() {
  const [count, setCount] = useState(0);
  
  // ❌ Without useCallback: New function each render
  const handleClick = () => {
    console.log('Count:', count);
  };
  
  // ✅ With useCallback: Same function reference
  const handleClick = useCallback(() => {
    console.log('Count:', count);
  }, [count]);  // Recreates only when count changes
  
  return <button onClick={handleClick}>Click</button>;
}
```

### Equivalence to useMemo

```tsx
// useCallback and useMemo are related

// These are equivalent:
const memoizedCallback = useCallback(() => {
  doSomething(a, b);
}, [a, b]);

const memoizedCallback = useMemo(() => {
  return () => {
    doSomething(a, b);
  };
}, [a, b]);

// useCallback is syntactic sugar for useMemo(() => fn, deps)
```

---

## useCallback vs useMemo

### Key Differences

| Aspect | useCallback | useMemo |
|--------|-------------|---------|
| **Memoizes** | Function itself | Function's return value |
| **Returns** | Function reference | Computed value |
| **Use For** | Event handlers, callbacks | Expensive computations |
| **Typical Use** | Preventing re-renders | Avoiding recalculation |

### Side-by-Side

```tsx
function Component({ items }) {
  // useCallback: Memoize the function
  const handleClick = useCallback((id) => {
    console.log('Clicked:', id);
  }, []);
  // Returns: The function itself
  
  // useMemo: Memoize the result
  const sortedItems = useMemo(() => {
    return [...items].sort();
  }, [items]);
  // Returns: The sorted array
  
  return (
    <div>
      {sortedItems.map(item => (
        <button key={item.id} onClick={() => handleClick(item.id)}>
          {item.name}
        </button>
      ))}
    </div>
  );
}
```

---

## When to Use useCallback

### Good Use Cases

```tsx
// ✅ 1. Passing callbacks to memoized children
const MemoizedChild = React.memo(Child);

function Parent() {
  const [count, setCount] = useState(0);
  
  // Without useCallback, Child re-renders every time
  const handleClick = useCallback(() => {
    console.log('Clicked');
  }, []);
  
  return (
    <>
      <button onClick={() => setCount(count + 1)}>Count: {count}</button>
      <MemoizedChild onClick={handleClick} />
      {/* Child doesn't re-render! onClick reference stable */}
    </>
  );
}

// ✅ 2. Dependency in useEffect
function Component({ userId }) {
  const fetchUser = useCallback(() => {
    return fetch(`/api/users/${userId}`).then(res => res.json());
  }, [userId]);
  
  useEffect(() => {
    fetchUser().then(setUser);
  }, [fetchUser]);  // fetchUser stable unless userId changes
}

// ✅ 3. Passing to custom Hooks
function Component({ onSuccess }) {
  const stableOnSuccess = useCallback(onSuccess, []);
  
  useCustomHook({
    onSuccess: stableOnSuccess
  });
}

// ✅ 4. Optimizing lists
function List({ items }) {
  const handleDelete = useCallback((id) => {
    // Delete logic
  }, []);
  
  return (
    <>
      {items.map(item => (
        <MemoizedItem 
          key={item.id}
          item={item}
          onDelete={handleDelete}  // Stable reference
        />
      ))}
    </>
  );
}
```

### When NOT to Use

```tsx
// ❌ 1. Not passed to memoized children
function Component() {
  const handleClick = useCallback(() => {
    console.log('Clicked');
  }, []);
  
  // Child isn't memoized, so useCallback pointless!
  return <UnmemoizedChild onClick={handleClick} />;
}

// ❌ 2. Used in same component
function Component() {
  const handleClick = useCallback(() => {
    setState(x => x + 1);
  }, []);
  
  return <button onClick={handleClick}>Click</button>;
  // Inline function would be simpler and same performance
}

// ❌ 3. Already stable (like dispatch)
function Component() {
  const [state, dispatch] = useReducer(reducer, initial);
  
  // dispatch is already stable!
  const stableDispatch = useCallback(dispatch, []);  // Unnecessary!
}

// ❌ 4. Simple inline handlers
function Component() {
  const handleClick = useCallback(() => {
    console.log('Simple handler');
  }, []);
  
  // Just use inline:
  return <button onClick={() => console.log('Simple handler')}>Click</button>;
}
```

---

## Performance Optimization

### Preventing Child Re-renders

```tsx
// Problem setup
const Child = React.memo(function Child({ onClick }) {
  console.log('Child rendered');
  return <button onClick={onClick}>Click</button>;
});

// ❌ Child re-renders on every Parent render
function Parent() {
  const [count, setCount] = useState(0);
  
  const handleClick = () => {
    console.log('Clicked');
  };
  // New function every render!
  
  return (
    <>
      <button onClick={() => setCount(count + 1)}>Count: {count}</button>
      <Child onClick={handleClick} />
      {/* Child re-renders because onClick is new */}
    </>
  );
}

// ✅ Child only re-renders when dependencies change
function Parent() {
  const [count, setCount] = useState(0);
  
  const handleClick = useCallback(() => {
    console.log('Clicked');
  }, []);  // Stable function
  
  return (
    <>
      <button onClick={() => setCount(count + 1)}>Count: {count}</button>
      <Child onClick={handleClick} />
      {/* Child doesn't re-render! onClick stable */}
    </>
  );
}
```

### Lists with Callbacks

```tsx
interface Item {
  id: number;
  name: string;
}

const ListItem = React.memo(function ListItem({ 
  item, 
  onEdit, 
  onDelete 
}: { 
  item: Item; 
  onEdit: (id: number) => void;
  onDelete: (id: number) => void;
}) {
  console.log('Rendering item:', item.id);
  
  return (
    <div>
      {item.name}
      <button onClick={() => onEdit(item.id)}>Edit</button>
      <button onClick={() => onDelete(item.id)}>Delete</button>
    </div>
  );
});

function List({ items }: { items: Item[] }) {
  // ✅ Stable callbacks
  const handleEdit = useCallback((id: number) => {
    console.log('Edit:', id);
  }, []);
  
  const handleDelete = useCallback((id: number) => {
    console.log('Delete:', id);
  }, []);
  
  return (
    <div>
      {items.map(item => (
        <ListItem
          key={item.id}
          item={item}
          onEdit={handleEdit}
          onDelete={handleDelete}
        />
      ))}
    </div>
  );
}
```

---

## Dependency Management

### Correct Dependencies

```tsx
function Component({ userId }) {
  const [count, setCount] = useState(0);
  
  // ✅ Include all dependencies
  const fetchData = useCallback(() => {
    return fetch(`/api/users/${userId}?count=${count}`);
  }, [userId, count]);  // Both userId and count
}
```

### Avoiding Stale Closures

```tsx
// ❌ Stale closure
function Component() {
  const [count, setCount] = useState(0);
  
  const handleClick = useCallback(() => {
    console.log(count);  // Always logs initial count!
  }, []);  // Empty deps: count never updates
  
  return <button onClick={handleClick}>Log Count</button>;
}

// ✅ Include dependencies
function Component() {
  const [count, setCount] = useState(0);
  
  const handleClick = useCallback(() => {
    console.log(count);  // Logs current count
  }, [count]);  // Recreates when count changes
  
  return <button onClick={handleClick}>Log Count</button>;
}

// ✅ Alternative: Use functional setState
function Component() {
  const [count, setCount] = useState(0);
  
  const handleClick = useCallback(() => {
    setCount(c => c + 1);  // No dependency on count!
  }, []);  // Can use empty deps
  
  return <button onClick={handleClick}>Increment</button>;
}
```

### setState in Dependencies

```tsx
// useState setters are stable, don't need in deps
function Component() {
  const [count, setCount] = useState(0);
  const [name, setName] = useState('');
  
  const handleClick = useCallback(() => {
    setCount(count + 1);
    setName('Updated');
  }, [count]);  // Only count, not setCount or setName
  
  // setCount and setName are guaranteed stable by React
}
```

---

## Common Use Cases

### Event Handlers for Memoized Components

```tsx
function ParentComponent() {
  const [items, setItems] = useState([]);
  
  // ✅ Stable delete handler
  const handleDelete = useCallback((id: number) => {
    setItems(prevItems => prevItems.filter(item => item.id !== id));
  }, []);  // No dependencies! Uses functional update
  
  return (
    <div>
      {items.map(item => (
        <MemoizedItem
          key={item.id}
          item={item}
          onDelete={handleDelete}
        />
      ))}
    </div>
  );
}

const MemoizedItem = React.memo(Item);
```

### Custom Hook Callbacks

```tsx
// Custom Hook expecting stable callback
function useAsync<T>(asyncFunction: () => Promise<T>) {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState(false);
  
  useEffect(() => {
    setLoading(true);
    asyncFunction()
      .then(setData)
      .finally(() => setLoading(false));
  }, [asyncFunction]);  // Expects stable reference
  
  return { data, loading };
}

// Usage
function Component({ userId }) {
  // ✅ Memoize async function
  const fetchUser = useCallback(() => {
    return fetch(`/api/users/${userId}`).then(res => res.json());
  }, [userId]);
  
  const { data: user, loading } = useAsync(fetchUser);
  
  if (loading) return <div>Loading...</div>;
  return <div>{user?.name}</div>;
}
```

### Debounced Callbacks

```tsx
function SearchInput() {
  const [query, setQuery] = useState('');
  
  // Stable callback for debounce
  const handleSearch = useCallback((searchQuery: string) => {
    console.log('Searching for:', searchQuery);
    // API call
  }, []);
  
  // Use in effect with debounce
  useEffect(() => {
    const timer = setTimeout(() => {
      handleSearch(query);
    }, 300);
    
    return () => clearTimeout(timer);
  }, [query, handleSearch]);
  
  return (
    <input
      value={query}
      onChange={(e) => setQuery(e.target.value)}
    />
  );
}
```

---

## TypeScript with useCallback

### Type Inference

```tsx
// TypeScript infers function signature
const handleClick = useCallback(() => {
  console.log('Clicked');
}, []);
// Type: () => void

const handleChange = useCallback((value: string) => {
  console.log(value);
}, []);
// Type: (value: string) => void
```

### Explicit Typing

```tsx
// Explicit function type
const handleClick = useCallback<React.MouseEventHandler<HTMLButtonElement>>((event) => {
  console.log(event.currentTarget);
}, []);

const handleSubmit = useCallback<React.FormEventHandler<HTMLFormElement>>((event) => {
  event.preventDefault();
}, []);

// Generic callback
const handleUpdate = useCallback<(id: number, data: UpdateData) => Promise<void>>(
  async (id, data) => {
    await api.update(id, data);
  },
  []
);
```

### Generic useCallback

```tsx
function useStableCallback<T extends (...args: any[]) => any>(
  callback: T
): T {
  const callbackRef = useRef(callback);
  
  useEffect(() => {
    callbackRef.current = callback;
  }, [callback]);
  
  return useCallback(((...args) => {
    return callbackRef.current(...args);
  }) as T, []);
}

// Usage: Always stable, but calls latest version
function Component({ onUpdate }) {
  const stableOnUpdate = useStableCallback(onUpdate);
  
  useEffect(() => {
    // Effect never re-runs, but calls latest onUpdate
    someSubscription(stableOnUpdate);
  }, [stableOnUpdate]);
}
```

---

## React Compiler Impact

### Before React Compiler

```tsx
// Manual memoization required
function Component({ items }) {
  const [filter, setFilter] = useState('');
  
  // Must use useCallback
  const handleClick = useCallback((id) => {
    console.log('Clicked:', id);
  }, []);
  
  // Must use useMemo
  const filtered = useMemo(() => {
    return items.filter(item => item.name.includes(filter));
  }, [items, filter]);
  
  return (
    <>
      {filtered.map(item => (
        <MemoizedItem 
          key={item.id}
          onClick={handleClick}
        />
      ))}
    </>
  );
}
```

### With React Compiler (React 19+)

```tsx
// Automatic memoization!
function Component({ items }) {
  const [filter, setFilter] = useState('');
  
  // Compiler memoizes automatically
  const handleClick = (id) => {
    console.log('Clicked:', id);
  };
  
  const filtered = items.filter(item => item.name.includes(filter));
  
  return (
    <>
      {filtered.map(item => (
        <Item  // No React.memo needed!
          key={item.id}
          onClick={handleClick}
        />
      ))}
    </>
  );
}
```

### When to Still Use useCallback

```tsx
// Even with React Compiler, use useCallback for:

// 1. Effect dependencies
const callback = useCallback(() => {
  // Complex logic
}, [deps]);

useEffect(() => {
  // Effect depends on callback
}, [callback]);

// 2. Explicit control over function identity
const apiCall = useCallback(() => {
  // Want to control when this recreates
}, [specificDep]);

// 3. Third-party libraries expecting stable refs
const stableRef = useCallback((node) => {
  if (node) {
    thirdPartyLib.attach(node);
  }
}, []);
```

---

## Best Practices

### 1. Only Use with Memoized Children

```tsx
// ❌ Wasted effort
function Component() {
  const handleClick = useCallback(() => {}, []);
  
  return <RegularChild onClick={handleClick} />;
  // Child isn't memoized, useCallback doesn't help
}

// ✅ Use together with React.memo
function Component() {
  const handleClick = useCallback(() => {}, []);
  
  return <MemoizedChild onClick={handleClick} />;
}

const MemoizedChild = React.memo(Child);
```

### 2. Use Functional Updates

```tsx
// ❌ Requires count in dependencies
function Component() {
  const [count, setCount] = useState(0);
  
  const increment = useCallback(() => {
    setCount(count + 1);
  }, [count]);  // Recreates when count changes
}

// ✅ No dependencies needed
function Component() {
  const [count, setCount] = useState(0);
  
  const increment = useCallback(() => {
    setCount(c => c + 1);  // Functional update
  }, []);  // Never recreates!
}
```

### 3. Don't Overuse

```tsx
// ❌ Overusing useCallback
function Component() {
  const a = useCallback(() => console.log('a'), []);
  const b = useCallback(() => console.log('b'), []);
  const c = useCallback(() => console.log('c'), []);
  
  // If these aren't passed to memoized children,
  // this is just overhead!
}

// ✅ Use only when needed
function Component() {
  // Inline for simple handlers
  return <button onClick={() => console.log('a')}>A</button>;
}
```

---

## Higher-Order Thinking FAQs

### 1. Why can't React just automatically memoize all functions, and what does this reveal about JavaScript function identity?

**Deep Answer:**

Automatic function memoization would break **closure semantics** and create surprising bugs with stale values.

**The Closure Problem:**

```tsx
function Component() {
  const [count, setCount] = useState(0);
  
  // If React automatically memoized this:
  const handleClick = () => {
    console.log(count);  // Closure captures count
  };
  
  // First render: count=0, handleClick logs 0
  // Second render: count=1
  //   - If memoized: handleClick still logs 0 (stale!)
  //   - If not memoized: handleClick logs 1 (correct!)
}
```

**Why Manual Control:**

```tsx
// You must explicitly choose:

// Option 1: Fresh closure every render
const handleClick = () => {
  console.log(count);  // Always current count
};

// Option 2: Stable function, stale closure
const handleClick = useCallback(() => {
  console.log(count);  // Might be stale!
}, []);

// Option 3: Stable function, fresh closure
const handleClick = useCallback(() => {
  console.log(count);  // Current count
}, [count]);  // Update when count changes
```

**React Compiler's Approach:**

```tsx
// React Compiler (React 19+) does automatic memoization
// But it's SMART about dependencies:

function Component() {
  const [count, setCount] = useState(0);
  
  const handleClick = () => {
    console.log(count);
  };
  
  // Compiler analyzes:
  // - handleClick uses count
  // - Automatically adds count to memoization dependencies
  // - Recreates function when count changes
  // - Behaves correctly!
}
```

**Deep Implication:**

Manual memoization exists because **JavaScript closures are powerful but subtle**. React gives you control over the tradeoff between function stability (performance) and closure freshness (correctness). The React Compiler eliminates this burden by analyzing your code and memoizing correctly automatically.

### 2. When does useCallback actually improve performance, and when does it make things slower?

**Deep Answer:**

useCallback has **overhead**, so it only helps when the benefit exceeds the cost.

**useCallback Overhead:**

```tsx
// Cost of useCallback:
// 1. Function call to useCallback
// 2. Dependency array comparison
// 3. Memory to store function and deps
// 4. Additional function wrapping

// Simple benchmark:
// Creating function: ~0.001ms
// useCallback: ~0.01ms
// Overhead: 10x!
```

**When It Helps:**

```tsx
// ✅ Prevents expensive child re-renders
function Parent() {
  const [count, setCount] = useState(0);
  
  const handleClick = useCallback(() => {}, []);
  
  return (
    <>
      <button onClick={() => setCount(count + 1)}>Count</button>
      <ExpensiveChild onClick={handleClick} />
      {/* 
        ExpensiveChild render: 100ms
        useCallback overhead: 0.01ms
        Benefit: 99.99ms saved!
      */}
    </>
  );
}

const ExpensiveChild = React.memo(function ExpensiveChild({ onClick }) {
  // 100ms render time
  return <div>Expensive rendering...</div>;
});
```

**When It Hurts:**

```tsx
// ❌ No memoized children
function Component() {
  const handleClick = useCallback(() => {}, []);
  
  return <button onClick={handleClick}>Click</button>;
  
  // useCallback overhead: 0.01ms
  // Benefit: 0ms (button always re-renders with component)
  // Net: -0.01ms (slower!)
}

// ❌ Simple children
function Component() {
  const handleClick = useCallback(() => {}, []);
  
  return <SimpleChild onClick={handleClick} />;
  
  // SimpleChild render: 0.1ms
  // useCallback overhead: 0.01ms
  // Benefit: 0.1ms saved
  // Net: 0.09ms (barely worth it)
}
```

**Performance Decision Matrix:**

```tsx
// Use useCallback when:
// - Child render time > 10ms
// - Child is memoized (React.memo)
// - Callback passed to multiple children
// - Callback used in effect dependencies

// Don't use when:
// - Child render time < 1ms
// - Child isn't memoized
// - Callback used only in same component
// - Inline function is simpler
```

**Measurement Example:**

```tsx
// Measure to decide
import { Profiler } from 'react';

function Component() {
  const handleClick = useCallback(() => {}, []);
  // vs
  // const handleClick = () => {};
  
  return (
    <Profiler
      id="Child"
      onRender={(id, phase, actualDuration) => {
        console.log(`${id} took ${actualDuration}ms`);
      }}
    >
      <MemoizedChild onClick={handleClick} />
    </Profiler>
  );
}
```

**Deep Implication:**

useCallback is **not free**. It's an optimization that trades memory and function call overhead for preventing re-renders. **Profile first, optimize second**. The React Compiler eliminates this decision-making by doing it optimally for you.

### 3. How does useCallback interact with event handlers and synthetic events, and can memoized handlers cause event-related bugs?

**Deep Answer:**

useCallback with **stale closures** can cause subtle bugs with events, especially with React's Synthetic Events.

**Stale Closure Bug:**

```tsx
// ❌ Buggy: Stale closure
function Form() {
  const [formData, setFormData] = useState({ email: '', password: '' });
  
  const handleSubmit = useCallback((e: React.FormEvent) => {
    e.preventDefault();
    console.log(formData);  // Captures initial formData!
  }, []);  // Empty deps = stale closure
  
  return (
    <form onSubmit={handleSubmit}>
      <input
        value={formData.email}
        onChange={(e) => setFormData({ ...formData, email: e.target.value })}
      />
      {/* Form submits with initial (empty) formData! */}
    </form>
  );
}

// ✅ Fix: Include dependency
function Form() {
  const [formData, setFormData] = useState({ email: '', password: '' });
  
  const handleSubmit = useCallback((e: React.FormEvent) => {
    e.preventDefault();
    console.log(formData);  // Current formData
  }, [formData]);  // Updates when formData changes
  
  return <form onSubmit={handleSubmit}>...</form>;
}
```

**Event Pooling (Pre-React 17):**

```tsx
// Pre-React 17 had event pooling
function Component() {
  const handleClick = useCallback((e: React.MouseEvent) => {
    // ❌ Async usage with pooled event
    setTimeout(() => {
      console.log(e.target);  // null! Event was pooled
    }, 1000);
  }, []);
  
  // ✅ Fix: Persist or extract value
  const handleClick = useCallback((e: React.MouseEvent) => {
    e.persist();  // Keep event alive (React <17)
    // Or:
    const target = e.target;  // Extract what you need
    
    setTimeout(() => {
      console.log(target);  // Works!
    }, 1000);
  }, []);
}

// React 17+: No pooling, events work in async code naturally
```

**Deep Implication:**

Memoized handlers with wrong dependencies create **time bombs** - bugs that only appear when the stale closure is actually used. This is why ESLint's exhaustive-deps rule is crucial. React Compiler solves this by automatically analyzing what variables the function accesses and adding them to dependencies.

---

## Senior SDE Interview Questions

### Question 1: useCallback Optimization Strategy

**Question:** "You have a list of 1000 items, each with edit/delete buttons. Users report the app is slow. Walk through how you'd optimize using useCallback and explain your reasoning."

**Key Concepts Being Tested:**
- Performance optimization strategy
- useCallback proper usage
- React.memo understanding
- Measurement methodology

**Expected Answer Should Cover:**

1. **Identify the Problem:**
```tsx
// Current (slow) code
function List({ items }) {
  const handleEdit = (id) => {
    // Edit logic
  };
  
  const handleDelete = (id) => {
    // Delete logic
  };
  
  return (
    <>
      {items.map(item => (
        <ListItem
          key={item.id}
          item={item}
          onEdit={() => handleEdit(item.id)}
          onDelete={() => handleDelete(item.id)}
        />
      ))}
    </>
  );
}

// Problems:
// - 1000 inline arrow functions created each render
// - If ListItem isn't memoized, all re-render on any change
```

2. **Solution Strategy:**
```tsx
// Step 1: Memoize ListItem
const MemoizedListItem = React.memo(ListItem);

// Step 2: Stable callbacks
function List({ items }) {
  const handleEdit = useCallback((id) => {
    // Edit logic
  }, []);
  
  const handleDelete = useCallback((id) => {
    // Delete logic
  }, []);
  
  return (
    <>
      {items.map(item => (
        <MemoizedListItem
          key={item.id}
          item={item}
          onEdit={handleEdit}
          onDelete={handleDelete}
          itemId={item.id}  // Pass ID as prop
        />
      ))}
    </>
  );
}

// Step 3: ListItem uses itemId
function ListItem({ item, onEdit, onDelete, itemId }) {
  return (
    <div>
      {item.name}
      <button onClick={() => onEdit(itemId)}>Edit</button>
