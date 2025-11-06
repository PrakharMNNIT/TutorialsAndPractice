# Topic 39: Code Splitting and Lazy Loading

[← Previous: Memoization Deep Dive](./38_memoization_deep.md) | [Back to Main](../README.md) | [Next: Virtualization →](./40_virtualization.md)

---

## Table of Contents

1. [Overview](#overview)
2. [What is Code Splitting?](#what-is-code-splitting)
3. [React.lazy and Suspense](#reactlazy-and-suspense)
4. [Route-Based Splitting](#route-based-splitting)
5. [Component-Based Splitting](#component-based-splitting)
6. [Dynamic Imports](#dynamic-imports)
7. [Loading States](#loading-states)
8. [Error Handling](#error-handling)
9. [Preloading](#preloading)
10. [Best Practices](#best-practices)

---

## Overview

**Code splitting** allows you to split your app into smaller chunks that are loaded on demand, dramatically reducing initial load time. React provides built-in support through `React.lazy` and `Suspense`.

**What You'll Learn:**
- What code splitting is and why it matters
- React.lazy for dynamic imports
- Suspense for loading states
- Route-based vs component-based splitting
- Preloading strategies
- Error handling
- Bundle optimization

**Prerequisites:**
- Understanding of JavaScript modules
- Suspense basics
- React Router (for route splitting)

**Version Coverage:**
- React 16.6+ (lazy and Suspense)
- React 18+ (Concurrent Suspense)
- React 19.2

---

## What is Code Splitting?

### The Problem

```tsx
// Without code splitting: One huge bundle
import HomePage from './HomePage';  // 500KB
import Dashboard from './Dashboard';  // 800KB
import Admin from './Admin';  // 600KB
// Total: 1.9MB

// User visits HomePage
// Downloads entire 1.9MB bundle
// Waits for Dashboard and Admin code they don't need
```

### The Solution

```tsx
// With code splitting: Load on demand
const HomePage = lazy(() => import('./HomePage'));  // 500KB
const Dashboard = lazy(() => import('./Dashboard'));  // Loaded when needed
const Admin = lazy(() => import('./Admin'));  // Loaded when needed

// User visits HomePage
// Downloads only 500KB
// Dashboard and Admin loaded when accessed
```

---

## React.lazy and Suspense

### Basic Usage

```tsx
import { lazy, Suspense } from 'react';

// Lazy load component
const HeavyComponent = lazy(() => import('./HeavyComponent'));

function App() {
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <HeavyComponent />
    </Suspense>
  );
}
```

### Multiple Lazy Components

```tsx
const Home = lazy(() => import('./Home'));
const About = lazy(() => import('./About'));
const Contact = lazy(() => import('./Contact'));

function App() {
  return (
    <Suspense fallback={<Loading />}>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/about" element={<About />} />
        <Route path="/contact" element={<Contact />} />
      </Routes>
    </Suspense>
  );
}
```

---

## Route-Based Splitting

### Splitting by Route

```tsx
// Each route is a separate bundle
const Dashboard = lazy(() => import('./pages/Dashboard'));
const Profile = lazy(() => import('./pages/Profile'));
const Settings = lazy(() => import('./pages/Settings'));

function App() {
  return (
    <BrowserRouter>
      <Suspense fallback={<PageLoader />}>
        <Routes>
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/settings" element={<Settings />} />
        </Routes>
      </Suspense>
    </BrowserRouter>
  );
}
```

---

## Component-Based Splitting

### Heavy Components

```tsx
// Split heavy components
const Chart = lazy(() => import('./Chart'));  // Heavy charting library
const Editor = lazy(() => import('./Editor'));  // Rich text editor
const Map = lazy(() => import('./Map'));  // Map library

function Dashboard() {
  const [showChart, setShowChart] = useState(false);
  
  return (
    <div>
      <button onClick={() => setShowChart(true)}>
        Show Chart
      </button>
      
      {showChart && (
        <Suspense fallback={<Skeleton />}>
          <Chart data={data} />
        </Suspense>
      )}
    </div>
  );
}
```

---

## Dynamic Imports

### Programmatic Loading

```tsx
// Load based on condition
function FeatureToggle({ featureName }) {
  const [Component, setComponent] = useState(null);
  
  useEffect(() => {
    if (featureName === 'featureA') {
      import('./FeatureA').then(mod => setComponent(() => mod.default));
    } else {
      import('./FeatureB').then(mod => setComponent(() => mod.default));
    }
  }, [featureName]);
  
  if (!Component) return <Loading />;
  
  return (
    <Suspense fallback={<Loading />}>
      <Component />
    </Suspense>
  );
}
```

---

## Loading States

### Custom Fallbacks

```tsx
// Different loading states for different components
<Suspense fallback={<PageSkeleton />}>
  <Page />
</Suspense>

<Suspense fallback={<ChartSkeleton />}>
  <Chart />
</Suspense>

<Suspense fallback={<Spinner />}>
  <SmallComponent />
</Suspense>
```

---

## Error Handling

### Error Boundaries with Lazy

```tsx
class ErrorBoundary extends Component {
  state = { hasError: false };
  
  static getDerivedStateFromError() {
    return { hasError: true };
  }
  
  render() {
    if (this.state.hasError) {
      return <div>Failed to load component</div>;
    }
    return this.props.children;
  }
}

// Usage
<ErrorBoundary>
  <Suspense fallback={<Loading />}>
    <LazyComponent />
  </Suspense>
</ErrorBoundary>
```

---

## Preloading

### Preload on Hover

```tsx
// Preload component when hovering link
function Navigation() {
  const DashboardPromise = useRef(null);
  
  const preloadDashboard = () => {
    if (!DashboardPromise.current) {
      DashboardPromise.current = import('./Dashboard');
    }
  };
  
  return (
    <Link 
      to="/dashboard"
      onMouseEnter={preloadDashboard}
    >
      Dashboard
    </Link>
  );
}
```

---

## Best Practices

### 1. Split at Route Level

```tsx
// ✅ Route-based splitting (best starting point)
const routes = [
  { path: '/', component: lazy(() => import('./Home')) },
  { path: '/about', component: lazy(() => import('./About')) }
];
```

### 2. Granular Suspense Boundaries

```tsx
// ✅ Multiple boundaries
<Suspense fallback={<HeaderSkeleton />}>
  <Header />
</Suspense>

<Suspense fallback={<ContentSkeleton />}>
  <Content />
</Suspense>
```

### 3. Named Exports

```tsx
// ✅ Lazy load named exports
const Component = lazy(() => 
  import('./Components').then(mod => ({ default: mod.SpecificComponent }))
);
```

---

## Higher-Order Thinking FAQs

### 1. How does code splitting affect SEO and initial page load, and what strategies mitigate these issues?

**Deep Answer:**

Code splitting **delays content rendering** which can hurt SEO and perceived performance, but strategies exist to mitigate this.

**The SEO Problem:**

```tsx
// Client-side code splitting
const HomePage = lazy(() => import('./HomePage'));

function App() {
  return (
    <Suspense fallback={<Loading />}>
      <HomePage />
    </Suspense>
  );
}

// Bot crawls page:
// 1. Sees <Loading /> initially
// 2. JavaScript loads
// 3. HomePage loads
// 4. Content appears

// Problem: Bots might not wait for all steps
```

**Solutions:**

```tsx
// 1. Server-Side Rendering (SSR)
// Render on server, send HTML
// Code splitting still helps for client hydration

// 2. Static Generation
// Pre-render at build time
// Split code for dynamic features only

// 3. Critical path inline
// Inline critical CSS/content
// Split non-critical features

// 4. Streaming SSR (React 18+)
<Suspense fallback={<Skeleton />}>
  <LazyComponent />
</Suspense>
// Server streams HTML as components load
```

**Deep Implication:**

Code splitting trades **initial load time** for **total load time**. Good for user experience (faster first interaction) but can complicate SEO. Solution: combine with SSR/SSG where SEO matters. This is why Next.js is popular - handles code splitting + SEO automatically.

---

## Senior SDE Interview Questions

### Question 1: Code Splitting Strategy

**Question:** "Your React SPA has a 2MB bundle. Users complain about slow initial load. Design a code splitting strategy."

**Key Concepts Being Tested:**
- Code splitting understanding
- Bundle analysis
- Strategic thinking
- Measurement approach

**Expected Answer Should Cover:**

1. **Analysis:**
```tsx
// Use webpack-bundle-analyzer
// Identify:
// - Route components (split by route)
// - Heavy libraries (D3, moment, etc.)
// - Rarely used features (admin panel)
```

2. **Strategy:**
```tsx
// Route-based splitting
const Dashboard = lazy(() => import('./Dashboard'));
const Admin = lazy(() => import('./Admin'));

// Feature-based splitting
const AdvancedFeatures = lazy(() => import('./Advanced'));

// Library splitting (via webpack config)
// Separate chunk for large libraries
```

3. **Expected Results:**
```tsx
// Before: 2MB initial bundle
// After:
// - Initial: 300KB (core app)
// - Dashboard: 400KB (loaded on route)
// - Admin: 500KB (loaded on route)
// - Others: 800KB (loaded as needed)
```

**Follow-ups:**
1. "How do you measure improvement?"
2. "What about mobile users (slower networks)?"
3. "How do you prevent loading waterfalls?"

**Red Flags:**
- Splitting every component (too granular)
- No measurement plan
- Missing preloading strategy

**Green Flags:**
- Strategic splitting (routes, features)
- Mentions bundle analysis
- Considers user experience
- Plans for measurement

---

[← Previous: Memoization Deep Dive](./38_memoization_deep.md) | [Back to Main](../README.md) | [Next: Virtualization →](./40_virtualization.md)

---

**Progress**: Topic 39/220 completed | Part IV: Performance (44% complete)
