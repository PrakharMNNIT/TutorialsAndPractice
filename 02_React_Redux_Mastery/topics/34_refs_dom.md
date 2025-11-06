# Topic 34: Refs and DOM Manipulation

[← Previous: Portals](./33_portals.md) | [Back to Main](../README.md) | [Next: Fragments →](./35_fragments.md)

---

## Table of Contents

1. [Overview](#overview)
2. [When to Manipulate DOM](#when-to-manipulate-dom)
3. [Using Refs for DOM Access](#using-refs-for-dom-access)
4. [Common DOM Operations](#common-dom-operations)
5. [Callback Refs](#callback-refs)
6. [Measuring Elements](#measuring-elements)
7. [Focus Management](#focus-management)
8. [Integration with Third-Party Libraries](#integration-with-third-party-libraries)
9. [Best Practices](#best-practices)
10. [When NOT to Use Refs](#when-not-to-use-refs)

---

## Overview

While React's declarative approach handles most UI updates, sometimes you need **direct DOM access** for focus management, measurements, animations, or integrating with non-React libraries. Refs provide this escape hatch.

**What You'll Learn:**
- When direct DOM manipulation is necessary
- Using refs to access DOM elements
- Common DOM operations (focus, scroll, measure)
- Callback refs for dynamic elements
- Integration with third-party libraries
- When to avoid refs

**Prerequisites:**
- useRef Hook understanding
- Basic DOM API knowledge
- Understanding of React rendering

**Version Coverage:**
- React 16.8+ (useRef Hook)
- React 19.2

---

## When to Manipulate DOM

### Valid Use Cases

```tsx
// ✅ Focus management
const inputRef = useRef<HTMLInputElement>(null);
inputRef.current?.focus();

// ✅ Scrolling
const elementRef = useRef<HTMLDivElement>(null);
elementRef.current?.scrollIntoView();

// ✅ Measuring elements
const divRef = useRef<HTMLDivElement>(null);
const height = divRef.current?.clientHeight;

// ✅ Animations (with libraries)
const animateRef = useRef<HTMLDivElement>(null);
gsap.to(animateRef.current, { x: 100 });

// ✅ Video/Audio control
const videoRef = useRef<HTMLVideoElement>(null);
videoRef.current?.play();

// ✅ Canvas operations
const canvasRef = useRef<HTMLCanvasElement>(null);
const ctx = canvasRef.current?.getContext('2d');
```

### Invalid Use Cases

```tsx
// ❌ Don't use refs to update text
const divRef = useRef<HTMLDivElement>(null);
divRef.current.textContent = 'Updated';  // Use state instead!

// ❌ Don't use refs to show/hide
const divRef = useRef<HTMLDivElement>(null);
divRef.current.style.display = 'none';  // Use conditional rendering!

// ❌ Don't use refs to manage component state
const divRef = useRef<HTMLDivElement>(null);
divRef.current.dataset.count = String(count);  // Use state!
```

---

## Using Refs for DOM Access

### Basic DOM Access

```tsx
function Component() {
  const inputRef = useRef<HTMLInputElement>(null);
  
  const focusInput = () => {
    inputRef.current?.focus();
  };
  
  const clearInput = () => {
    if (inputRef.current) {
      inputRef.current.value = '';
      inputRef.current.focus();
    }
  };
  
  return (
    <>
      <input ref={inputRef} />
      <button onClick={focusInput}>Focus</button>
      <button onClick={clearInput}>Clear</button>
    </>
  );
}
```

### Multiple Refs

```tsx
function MultipleInputs() {
  const input1Ref = useRef<HTMLInputElement>(null);
  const input2Ref = useRef<HTMLInputElement>(null);
  const input3Ref = useRef<HTMLInputElement>(null);
  
  const focusNext = (current: number) => {
    const refs = [input1Ref, input2Ref, input3Ref];
    const nextIndex = current + 1;
    
    if (nextIndex < refs.length) {
      refs[nextIndex].current?.focus();
    }
  };
  
  return (
    <>
      <input 
        ref={input1Ref}
        onKeyDown={(e) => e.key === 'Enter' && focusNext(0)}
      />
      <input 
        ref={input2Ref}
        onKeyDown={(e) => e.key === 'Enter' && focusNext(1)}
      />
      <input ref={input3Ref} />
    </>
  );
}
```

---

## Common DOM Operations

### Focus Management

```tsx
function AutoFocusInput() {
  const inputRef = useRef<HTMLInputElement>(null);
  
  useEffect(() => {
    // Focus on mount
    inputRef.current?.focus();
  }, []);
  
  return <input ref={inputRef} placeholder="Auto-focused" />;
}

// Focus on condition
function ConditionalFocus({ shouldFocus }) {
  const inputRef = useRef<HTMLInputElement>(null);
  
  useEffect(() => {
    if (shouldFocus) {
      inputRef.current?.focus();
    }
  }, [shouldFocus]);
  
  return <input ref={inputRef} />;
}
```

### Scrolling

```tsx
function ScrollToElement() {
  const targetRef = useRef<HTMLDivElement>(null);
  
  const scrollToTarget = () => {
    targetRef.current?.scrollIntoView({
      behavior: 'smooth',
      block: 'center'
    });
  };
  
  return (
    <>
      <button onClick={scrollToTarget}>Scroll to Target</button>
      
      <div style={{ height: '2000px' }}>Spacer</div>
      
      <div ref={targetRef} style={{ background: 'yellow' }}>
        Target Element
      </div>
    </>
  );
}

// Scroll container
function ScrollableList() {
  const containerRef = useRef<HTMLDivElement>(null);
  
  const scrollToTop = () => {
    if (containerRef.current) {
      containerRef.current.scrollTop = 0;
    }
  };
  
  const scrollToBottom = () => {
    if (containerRef.current) {
      containerRef.current.scrollTop = containerRef.current.scrollHeight;
    }
  };
  
  return (
    <>
      <button onClick={scrollToTop}>Top</button>
      <button onClick={scrollToBottom}>Bottom</button>
      
      <div ref={containerRef} style={{ height: '300px', overflow: 'auto' }}>
        {/* Long content */}
      </div>
    </>
  );
}
```

### Class Manipulation

```tsx
function HighlightOnHover() {
  const divRef = useRef<HTMLDivElement>(null);
  
  const addHighlight = () => {
    divRef.current?.classList.add('highlighted');
  };
  
  const removeHighlight = () => {
    divRef.current?.classList.remove('highlighted');
  };
  
  return (
    <div
      ref={divRef}
      onMouseEnter={addHighlight}
      onMouseLeave={removeHighlight}
    >
      Hover me
    </div>
  );
}
```

---

## Callback Refs

### Dynamic Ref Assignment

```tsx
// Callback ref for dynamic assignment
function Component() {
  const [ref, setRef] = useState<HTMLDivElement | null>(null);
  
  // Called when element mounts/unmounts
  const callbackRef = useCallback((node: HTMLDivElement | null) => {
    if (node) {
      console.log('Element mounted:', node);
      // Can perform operations immediately
      node.style.backgroundColor = 'yellow';
    } else {
      console.log('Element unmounted');
    }
    
    setRef(node);
  }, []);
  
  return <div ref={callbackRef}>Element</div>;
}
```

### Refs in Lists

```tsx
function ListWithRefs({ items }) {
  const refsMap = useRef(new Map<number, HTMLLIElement>());
  
  const scrollToItem = (id: number) => {
    const element = refsMap.current.get(id);
    element?.scrollIntoView({ behavior: 'smooth' });
  };
  
  return (
    <ul>
      {items.map(item => (
        <li
          key={item.id}
          ref={(el) => {
            if (el) {
              refsMap.current.set(item.id, el);
            } else {
              refsMap.current.delete(item.id);
            }
          }}
        >
          {item.name}
          <button onClick={() => scrollToItem(item.id)}>Scroll here</button>
        </li>
      ))}
    </ul>
  );
}
```

---

## Measuring Elements

### Dimension Measurement

```tsx
function useDimensions() {
  const ref = useRef<HTMLDivElement>(null);
  const [dimensions, setDimensions] = useState({ width: 0, height: 0 });
  
  useEffect(() => {
    if (ref.current) {
      const { width, height } = ref.current.getBoundingClientRect();
      setDimensions({ width, height });
    }
  }, []);
  
  return { ref, dimensions };
}

// Usage
function Component() {
  const { ref, dimensions } = useDimensions();
  
  return (
    <>
      <div ref={ref}>Measured element</div>
      <p>Width: {dimensions.width}px</p>
      <p>Height: {dimensions.height}px</p>
    </>
  );
}
```

### Resize Observer

```tsx
function useResizeObserver() {
  const ref = useRef<HTMLDivElement>(null);
  const [size, setSize] = useState({ width: 0, height: 0 });
  
  useEffect(() => {
    if (!ref.current) return;
    
    const observer = new ResizeObserver((entries) => {
      const entry = entries[0];
      setSize({
        width: entry.contentRect.width,
        height: entry.contentRect.height
      });
    });
    
    observer.observe(ref.current);
    
    return () => observer.disconnect();
  }, []);
  
  return { ref, size };
}

// Usage
function ResizableComponent() {
  const { ref, size } = useResizeObserver();
  
  return (
    <>
      <div ref={ref} style={{ resize: 'both', overflow: 'auto', border: '1px solid' }}>
        Resize me
      </div>
      <p>Size: {size.width} x {size.height}</p>
    </>
  );
}
```

---

## Focus Management

### Trap Focus in Modal

```tsx
function useFocusTrap(isActive: boolean) {
  const ref = useRef<HTMLDivElement>(null);
  
  useEffect(() => {
    if (!isActive || !ref.current) return;
    
    const element = ref.current;
    const focusableElements = element.querySelectorAll(
      'button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])'
    );
    
    const firstElement = focusableElements[0] as HTMLElement;
    const lastElement = focusableElements[focusableElements.length - 1] as HTMLElement;
    
    // Focus first element
    firstElement?.focus();
    
    const handleTab = (e: KeyboardEvent) => {
      if (e.key !== 'Tab') return;
      
      if (e.shiftKey) {
        if (document.activeElement === firstElement) {
          lastElement.focus();
          e.preventDefault();
        }
      } else {
        if (document.activeElement === lastElement) {
          firstElement.focus();
          e.preventDefault();
        }
      }
    };
    
    element.addEventListener('keydown', handleTab);
    return () => element.removeEventListener('keydown', handleTab);
  }, [isActive]);
  
  return ref;
}

// Usage
function Modal({ isOpen }) {
  const modalRef = useFocusTrap(isOpen);
  
  if (!isOpen) return null;
  
  return (
    <div ref={modalRef} className="modal">
      <input />
      <button>OK</button>
      <button>Cancel</button>
    </div>
  );
}
```

---

## Integration with Third-Party Libraries

### D3.js Integration

```tsx
function D3Chart({ data }) {
  const svgRef = useRef<SVGSVGElement>(null);
  
  useEffect(() => {
    if (!svgRef.current) return;
    
    // D3 manipulates DOM directly
    const svg = d3.select(svgRef.current);
    
    svg.selectAll('*').remove();  // Clear previous
    
    // Draw chart
    svg
      .selectAll('rect')
      .data(data)
      .enter()
      .append('rect')
      .attr('x', (d, i) => i * 30)
      .attr('y', d => 200 - d)
      .attr('width', 25)
      .attr('height', d => d);
  }, [data]);
  
  return <svg ref={svgRef} width={600} height={200} />;
}
```

### jQuery Integration

```tsx
function jQueryComponent() {
  const divRef = useRef<HTMLDivElement>(null);
  
  useEffect(() => {
    if (!divRef.current) return;
    
    // Initialize jQuery plugin
    $(divRef.current).somePlugin({
      option1: 'value1',
      option2: 'value2'
    });
    
    return () => {
      // Cleanup plugin
      $(divRef.current).somePlugin('destroy');
    };
  }, []);
  
  return <div ref={divRef}>jQuery enhanced</div>;
}
```

---

## Best Practices

### 1. Prefer Declarative Over Imperative

```tsx
// ❌ Imperative (using refs)
function Component() {
  const divRef = useRef<HTMLDivElement>(null);
  
  const show = () => {
    divRef.current.style.display = 'block';
  };
  
  const hide = () => {
    divRef.current.style.display = 'none';
  };
  
  return <div ref={divRef}>Content</div>;
}

// ✅ Declarative (using state)
function Component() {
  const [isVisible, setIsVisible] = useState(true);
  
  return (
    <>
      {isVisible && <div>Content</div>}
      <button onClick={() => setIsVisible(!isVisible)}>Toggle</button>
    </>
  );
}
```

### 2. Check for null

```tsx
// ✅ Always check ref.current
function Component() {
  const ref = useRef<HTMLDivElement>(null);
  
  const doSomething = () => {
    if (ref.current) {
      ref.current.scrollIntoView();
    }
    
    // Or optional chaining
    ref.current?.scrollIntoView();
  };
}
```

### 3. Use Refs as Escape Hatch

```tsx
// Refs are for when declarative approach isn't enough
// Use sparingly!

// ✅ Good: Can't be done declaratively
const videoRef = useRef<HTMLVideoElement>(null);
videoRef.current?.play();

// ❌ Bad: Can be done declaratively
const divRef = useRef<HTMLDivElement>(null);
divRef.current.textContent = text;  // Just use {text} in JSX!
```

---

## When NOT to Use Refs

### Use State Instead

```tsx
// ❌ Using ref for UI state
function BadCounter() {
  const countRef = useRef(0);
  
  const increment = () => {
    countRef.current += 1;
    // Component doesn't re-render!
    // UI doesn't update!
  };
  
  return <div>{countRef.current}</div>;  // Always shows 0
}

// ✅ Use state for UI
function GoodCounter() {
  const [count, setCount] = useState(0);
  
  return (
    <>
      <div>{count}</div>
      <button onClick={() => setCount(count + 1)}>+</button>
    </>
  );
}
```

---

## Higher-Order Thinking FAQs

### 1. Why does React discourage direct DOM manipulation, and when is it actually necessary?

**Deep Answer:**

React's declarative model assumes **React controls the DOM**, so direct manipulation breaks that assumption and can cause bugs.

**Why It's Dangerous:**

```tsx
// ❌ React and manual DOM manipulation conflict
function Component() {
  const divRef = useRef<HTMLDivElement>(null);
  const [text, setText] = useState('Initial');
  
  // Manual DOM update
  const updateManually = () => {
    divRef.current.textContent = 'Manual update';
  };
  
  // React update
  const updateReact = () => {
    setText('React update');
  };
  
  return (
    <>
      <div ref={divRef}>{text}</div>
      <button onClick={updateManually}>Manual</button>
      <button onClick={updateReact}>React</button>
    </>
  );
}

// Scenario:
// 1. Click Manual → div shows "Manual update"
// 2. Click React → setText('React update')
// 3. Component re-renders
// 4. React sees text='React update' in state
// 5. Updates div to "React update"
// 6. Manual change was lost!

// React doesn't know about manual changes
```

**When It's Safe:**

```tsx
// ✅ Operations React doesn't manage
function Component() {
  const videoRef = useRef<HTMLVideoElement>(null);
  
  // Safe: React doesn't control playback
  const play = () => videoRef.current?.play();
  const pause = () => videoRef.current?.pause();
  
  return (
    <>
      <video ref={videoRef} src={src} />
      <button onClick={play}>Play</button>
      <button onClick={pause}>Pause</button>
    </>
  );
}

// ✅ Reading (not writing)
function Component() {
  const divRef = useRef<HTMLDivElement>(null);
  
  // Safe: Just reading dimensions
  const getHeight = () => {
    return divRef.current?.clientHeight || 0;
  };
}

// ✅ Third-party library integration
function Chart() {
  const canvasRef = useRef<HTMLCanvasElement>(null);
  
  useEffect(() => {
    // Chart library owns this DOM
    const chart = new ChartLibrary(canvasRef.current);
    return () => chart.destroy();
  }, []);
  
  return <canvas ref={canvasRef} />;
}
```

**Deep Implication:**

React's model is: **state changes, React updates DOM**. Direct manipulation inverts this: **you update DOM, state unchanged**. This creates inconsistency. Use refs only when you need to escape React's model (integrations, measurements, imperitive APIs like play/pause).

### 2. How do callback refs differ from useRef refs, and when is each appropriate?

**Deep Answer:**

Callback refs provide **fine-grained control** over ref lifecycle, while useRef is simpler for most cases.

**useRef (Simple):**

```tsx
function Component() {
  const ref = useRef<HTMLDivElement>(null);
  
  // ref.current set when element mounts
  // ref.current = null when element unmounts
  // No notification of these changes
  
  useEffect(() => {
    console.log('Effect runs');
    console.log(ref.current);  // Might be null at this point!
  });
  
  return <div ref={ref}>Content</div>;
}
```

**Callback Ref (Control):**

```tsx
function Component() {
  const [node, setNode] = useState<HTMLDivElement | null>(null);
  
  // Called with node when mounts, null when unmounts
  const refCallback = useCallback((node: HTMLDivElement | null) => {
    if (node) {
      console.log('Element mounted!', node);
      // Immediate access to element
      node.style.backgroundColor = 'yellow';
    } else {
      console.log('Element unmounted!');
    }
    
    setNode(node);
  }, []);
  
  return <div ref={refCallback}>Content</div>;
}
```

**When Callback Refs Matter:**

```tsx
// ✅ Need to react to mount/unmount
function ObservedElement() {
  const refCallback = useCallback((node: HTMLDivElement | null) => {
    if (node) {
      const observer = new ResizeObserver(() => {
        // React to size changes
      });
      observer.observe(node);
      
      // Store observer for cleanup
      node._observer = observer;
    } else if (node?._observer) {
      node._observer.disconnect();
    }
  }, []);
  
  return <div ref={refCallback}>Observed</div>;
}

// ✅ Measuring on mount
function MeasureOnMount() {
  const [height, setHeight] = useState(0);
  
  const refCallback = useCallback((node: HTMLDivElement | null) => {
    if (node) {
      setHeight(node.clientHeight);  // Measure immediately
    }
  }, []);
  
  return (
    <>
      <div ref={refCallback}>Content to measure</div>
      <p>Height: {height}px</p>
    </>
  );
}
```

**Deep Implication:**

useRef is **passive** (you access when you want), callback refs are **reactive** (notified on mount/unmount). Use useRef for simple access, callback refs when you need to **react** to ref changes or measure immediately on mount.

---

## Senior SDE Interview Questions

### Question 1: DOM Integration Strategy

**Question:** "You need to integrate a complex charting library (like D3) that directly manipulates the DOM. How do you architect this in React while avoiding conflicts?"

**Key Concepts Being Tested:**
- Understanding of React/DOM interaction
- Integration strategy
- Lifecycle management
- Performance considerations

**Expected Answer Should Cover:**

1. **Separation Strategy:**
```tsx
// React owns component logic
// D3 owns SVG rendering

function Chart({ data }) {
  const svgRef = useRef<SVGSVGElement>(null);
  
  useEffect(() => {
    if (!svgRef.current) return;
    
    // Clear previous (React doesn't know about D3 changes)
    d3.select(svgRef.current).selectAll('*').remove();
    
    // D3 renders
    const svg = d3.select(svgRef.current);
    // ... D3 code
    
  }, [data]);  // Re-render on data change
  
  // React provides container, D3 fills it
  return <svg ref={svgRef} />;
}
```

2. **Boundaries:**
- React manages: component lifecycle, data, interactions
- D3 manages: SVG rendering, animations, scales
- Don't let them overlap!

3. **Performance:**
- Use useMemo for expensive D3 scale calculations
- Debounce re-renders if data changes frequently
- Consider canvas for large datasets

**Follow-ups:**
1. "How do you handle D3 transitions with React state changes?"
2. "What about accessibility (D3 doesn't handle this)?"
3. "How do you test the integration?"

**Red Flags:**
- Mixing React and D3 rendering in same elements
- Not clearing previous D3 renders
- No separation of concerns

**Green Flags:**
- Clear boundary between React and D3
- Proper cleanup
- Considers performance
- Mentions accessibility

---

[← Previous: Portals](./33_portals.md) | [Back to Main](../README.md) | [Next: Fragments →](./35_fragments.md)

---

**Progress**: Topic 34/220 completed | Part III: Advanced Patterns (90% complete)
