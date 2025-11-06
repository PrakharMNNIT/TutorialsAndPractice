# Topic 23: useActionState Hook - Action State Management (React 19)

[← Previous: useFormStatus Hook](./22_useformstatus_hook.md) | [Back to Main](../README.md) | [Next: useOptimistic Hook →](./24_useoptimistic_hook.md)

---

## Table of Contents

1. [Overview](#overview)
2. [What is useActionState?](#what-is-useactionstate)
3. [Basic Usage](#basic-usage)
4. [Form Actions](#form-actions)
5. [Error Handling](#error-handling)
6. [Optimistic Updates](#optimistic-updates)
7. [TypeScript with useActionState](#typescript-with-useactionstate)
8. [Common Patterns](#common-patterns)
9. [Best Practices](#best-practices)
10. [Real-World Examples](#real-world-examples)

---

## Overview

**useActionState** (formerly `useFormState`) is a React 19 Hook that lets you update state based on the result of a form action. It provides a way to handle async form submissions with proper state management.

**What You'll Learn:**
- What useActionState is and when to use it
- Integrating with Server Actions
- Handling form submission results
- Error handling patterns
- TypeScript usage
- Real-world form patterns

**Prerequisites:**
- useState and useEffect basics
- React 19 Server Actions
- Form handling concepts

**Version Coverage:**
- React 19+ (useActionState introduction)
- React 19.2

---

## What is useActionState?

### Core Concept

useActionState manages state that updates based on form action results, providing both the current state and a form action.

```tsx
import { useActionState } from 'react';

const [state, formAction, isPending] = useActionState(
  actionFunction,
  initialState
);

// state: Current state (updated by action)
// formAction: Function to use as form action
// isPending: Boolean indicating if action is running
```

### Basic Pattern

```tsx
// Action function
async function submitAction(previousState: any, formData: FormData) {
  // previousState: State from last execution
  // formData: Form data being submitted
  
  const name = formData.get('name');
  
  try {
    await saveToDatabase(name);
    return { success: true, message: 'Saved!' };
  } catch (error) {
    return { success: false, message: error.message };
  }
}

// Component
function Form() {
  const [state, formAction, isPending] = useActionState(
    submitAction,
    { success: false, message: '' }
  );
  
  return (
    <form action={formAction}>
      <input name="name" />
      <button disabled={isPending}>
        {isPending ? 'Saving...' : 'Save'}
      </button>
      
      {state.message && (
        <div className={state.success ? 'success' : 'error'}>
          {state.message}
        </div>
      )}
    </form>
  );
}
```

---

## Basic Usage

### Simple Form with State

```tsx
type State = {
  success: boolean;
  message: string;
  data?: any;
};

async function login(
  previousState: State,
  formData: FormData
): Promise<State> {
  const email = formData.get('email') as string;
  const password = formData.get('password') as string;
  
  try {
    const user = await authenticateUser(email, password);
    return {
      success: true,
      message: 'Login successful!',
      data: user
    };
  } catch (error) {
    return {
      success: false,
      message: 'Invalid credentials'
    };
  }
}

function LoginForm() {
  const [state, formAction, isPending] = useActionState(login, {
    success: false,
    message: ''
  });
  
  return (
    <form action={formAction}>
      <input name="email" type="email" required />
      <input name="password" type="password" required />
      
      <button type="submit" disabled={isPending}>
        {isPending ? 'Logging in...' : 'Login'}
      </button>
      
      {state.message && (
        <div className={state.success ? 'success-message' : 'error-message'}>
          {state.message}
        </div>
      )}
      
      {state.success && state.data && (
        <div>Welcome, {state.data.name}!</div>
      )}
    </form>
  );
}
```

### Progressive Enhancement

```tsx
// Works without JavaScript (progressive enhancement)
async function submitForm(prevState: State, formData: FormData) {
  'use server';
  
  // Server-side validation
  const name = formData.get('name') as string;
  
  if (!name || name.length < 3) {
    return {
      success: false,
      errors: { name: 'Name must be at least 3 characters' }
    };
  }
  
  await db.users.create({ name });
  
  return {
    success: true,
    message: 'User created successfully!'
  };
}

function Form() {
  const [state, formAction, isPending] = useActionState(submitForm, {
    success: false,
    errors: {}
  });
  
  return (
    <form action={formAction}>
      <input name="name" />
      {state.errors?.name && <span className="error">{state.errors.name}</span>}
      
      <button disabled={isPending}>
        {isPending ? 'Saving...' : 'Save'}
      </button>
      
      {state.success && <div className="success">{state.message}</div>}
    </form>
  );
}
```

---

## Form Actions

### Server Actions

```tsx
// Server Action with validation
async function createPost(
  prevState: { errors?: Record<string, string> },
  formData: FormData
) {
  'use server';
  
  const title = formData.get('title') as string;
  const content = formData.get('content') as string;
  
  // Validation
  const errors: Record<string, string> = {};
  
  if (!title) errors.title = 'Title is required';
  if (title && title.length < 5) errors.title = 'Title too short';
  if (!content) errors.content = 'Content is required';
  
  if (Object.keys(errors).length > 0) {
    return { errors };
  }
  
  // Save to database
  const post = await db.posts.create({ title, content });
  
  redirect(`/posts/${post.id}`);
}

function CreatePostForm() {
  const [state, formAction, isPending] = useActionState(createPost, {});
  
  return (
    <form action={formAction}>
      <div>
        <label>Title</label>
        <input name="title" />
        {state.errors?.title && (
          <span className="error">{state.errors.title}</span>
        )}
      </div>
      
      <div>
        <label>Content</label>
        <textarea name="content" />
        {state.errors?.content && (
          <span className="error">{state.errors.content}</span>
        )}
      </div>
      
      <button type="submit" disabled={isPending}>
        {isPending ? 'Creating...' : 'Create Post'}
      </button>
    </form>
  );
}
```

### Client Actions

```tsx
// Client-side action
type FormState = {
  success: boolean;
  message: string;
  errors?: Record<string, string>;
};

function Form() {
  const clientAction = async (
    prevState: FormState,
    formData: FormData
  ): Promise<FormState> => {
    const name = formData.get('name') as string;
    
    // Client-side validation
    if (!name) {
      return {
        success: false,
        message: '',
        errors: { name: 'Name is required' }
      };
    }
    
    // API call
    try {
      await fetch('/api/users', {
        method: 'POST',
        body: JSON.stringify({ name })
      });
      
      return {
        success: true,
        message: 'User created!'
      };
    } catch (error) {
      return {
        success: false,
        message: 'Failed to create user'
      };
    }
  };
  
  const [state, formAction, isPending] = useActionState(clientAction, {
    success: false,
    message: ''
  });
  
  return (
    <form action={formAction}>
      <input name="name" />
      {state.errors?.name && <span>{state.errors.name}</span>}
      
      <button disabled={isPending}>
        {isPending ? 'Saving...' : 'Save'}
      </button>
      
      {state.message && (
        <div className={state.success ? 'success' : 'error'}>
          {state.message}
        </div>
      )}
    </form>
  );
}
```

---

## Error Handling

### Validation Errors

```tsx
type FormState = {
  errors: Record<string, string>;
  message?: string;
};

async function validateAndSubmit(
  prevState: FormState,
  formData: FormData
): Promise<FormState> {
  const email = formData.get('email') as string;
  const password = formData.get('password') as string;
  
  // Validation
  const errors: Record<string, string> = {};
  
  if (!email) {
    errors.email = 'Email is required';
  } else if (!/\S+@\S+\.\S+/.test(email)) {
    errors.email = 'Email is invalid';
  }
  
  if (!password) {
    errors.password = 'Password is required';
  } else if (password.length < 8) {
    errors.password = 'Password must be at least 8 characters';
  }
  
  if (Object.keys(errors).length > 0) {
    return { errors };
  }
  
  // Submit
  try {
    await submitToServer({ email, password });
    return { errors: {}, message: 'Success!' };
  } catch (error) {
    return {
      errors: {},
      message: 'Server error. Please try again.'
    };
  }
}

function Form() {
  const [state, formAction, isPending] = useActionState(
    validateAndSubmit,
    { errors: {} }
  );
  
  return (
    <form action={formAction}>
      <div>
        <input name="email" type="email" />
        {state.errors.email && (
          <span className="error">{state.errors.email}</span>
        )}
      </div>
      
      <div>
        <input name="password" type="password" />
        {state.errors.password && (
          <span className="error">{state.errors.password}</span>
        )}
      </div>
      
      <button disabled={isPending}>
        {isPending ? 'Submitting...' : 'Submit'}
      </button>
      
      {state.message && (
        <div className="message">{state.message}</div>
      )}
    </form>
  );
}
```

---

## TypeScript with useActionState

### Fully Typed

```tsx
// State type
interface TodoFormState {
  success: boolean;
  message: string;
  errors?: Record<string, string>;
  todo?: Todo;
}

// Action function type
type TodoAction = (
  prevState: TodoFormState,
  formData: FormData
) => Promise<TodoFormState>;

// Server Action
const addTodo: TodoAction = async (prevState, formData) => {
  const text = formData.get('text') as string;
  
  if (!text) {
    return {
      success: false,
      message: '',
      errors: { text: 'Text is required' }
    };
  }
  
  const todo = await db.todos.create({ text });
  
  return {
    success: true,
    message: 'Todo added!',
    todo
  };
};

// Component
function TodoForm() {
  const [state, formAction, isPending] = useActionState<TodoFormState>(
    addTodo,
    { success: false, message: '' }
  );
  
  return (
    <form action={formAction}>
      <input name="text" />
      {state.errors?.text && <span>{state.errors.text}</span>}
      
      <button disabled={isPending}>
        {isPending ? 'Adding...' : 'Add Todo'}
      </button>
      
      {state.success && <div>✓ {state.message}</div>}
    </form>
  );
}
```

---

## Common Patterns

### Form with Success Reset

```tsx
function Form() {
  const [state, formAction, isPending] = useActionState(
    submitAction,
    { success: false, message: '' }
  );
  
  const formRef = useRef<HTMLFormElement>(null);
  
  useEffect(() => {
    if (state.success) {
      formRef.current?.reset();  // Clear form on success
    }
  }, [state.success]);
  
  return (
    <form ref={formRef} action={formAction}>
      <input name="email" />
      <button disabled={isPending}>Submit</button>
      
      {state.message && <div>{state.message}</div>}
    </form>
  );
}
```

### Persistent State Across Submissions

```tsx
type State = {
  attempts: number;
  lastError?: string;
  success: boolean;
};

async function loginAction(
  prevState: State,
  formData: FormData
): Promise<State> {
  const email = formData.get('email') as string;
  
  try {
    await login(email);
    return {
      attempts: prevState.attempts + 1,
      success: true
    };
  } catch (error) {
    return {
      attempts: prevState.attempts + 1,
      lastError: error.message,
      success: false
    };
  }
}

function LoginForm() {
  const [state, formAction, isPending] = useActionState(loginAction, {
    attempts: 0,
    success: false
  });
  
  return (
    <form action={formAction}>
      <p>Attempt #{state.attempts}</p>
      
      <input name="email" />
      <button disabled={isPending}>Login</button>
      
      {state.lastError && <div className="error">{state.lastError}</div>}
    </form>
  );
}
```

---

## Best Practices

### 1. Return Consistent State Shape

```tsx
// ✅ Consistent state structure
type FormState = {
  success: boolean;
  message: string;
  errors?: Record<string, string>;
  data?: any;
};

async function action(prev: FormState, formData: FormData): Promise<FormState> {
  // Always return same shape
  return {
    success: true,
    message: 'Success'
  };
}
```

### 2. Handle Loading States

```tsx
// ✅ Use isPending for UI feedback
function Form() {
  const [state, formAction, isPending] = useActionState(action, initialState);
  
  return (
    <form action={formAction}>
      <input disabled={isPending} />
      <button disabled={isPending}>
        {isPending ? 'Submitting...' : 'Submit'}
      </button>
    </form>
  );
}
```

### 3. Clear Errors on New Submission

```tsx
async function action(prevState: State, formData: FormData): Promise<State> {
  // Clear previous errors
  return {
    ...prevState,
    errors: {},  // Reset errors
    // ... new state
  };
}
```

---

## Higher-Order Thinking FAQs

### 1. Why does useActionState provide previousState to the action function, and what patterns does this enable?

**Deep Answer:**

The previousState parameter enables **stateful actions** that can make decisions based on submission history.

**Use Cases for previousState:**

```tsx
// 1. Retry counting
async function submitWithRetry(prevState, formData) {
  const retryCount = (prevState.retryCount || 0) + 1;
  
  if (retryCount > 3) {
    return {
      success: false,
      message: 'Too many attempts. Please try again later.',
      retryCount
    };
  }
  
  try {
    await submit(formData);
    return { success: true, retryCount: 0 };
  } catch (error) {
    return {
      success: false,
      message: 'Failed. Please retry.',
      retryCount
    };
  }
}

// 2. Incremental form building
async function addItem(prevState, formData) {
  const newItem = formData.get('item');
  
  return {
    items: [...(prevState.items || []), newItem],  // Accumulate items
    message: 'Item added'
  };
}

// 3. Conditional logic based on previous state
async function smartAction(prevState, formData) {
  if (prevState.lastAction === 'draft') {
    // Different behavior after draft
    return await publishDraft(formData);
  } else {
    // Different behavior for new post
    return await createNew(formData);
  }
}
```

**Deep Implication:**

previousState makes actions **stateful**, not just pure request handlers. This enables complex workflows where each submission can depend on previous attempts, accumulated data, or submission history. It's like a reducer pattern but for async form actions.

### 2. How does useActionState compare to useState + useEffect for form handling, and when should you use each?

**Deep Answer:**

useActionState is **specialized for forms**, while useState + useEffect is **general-purpose**. Each has appropriate use cases.

**useState + useEffect Pattern:**

```tsx
// Traditional pattern
function Form() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    
    try {
      const formData = new FormData(e.target);
      await submitToServer(formData);
      setSuccess(true);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };
  
  return (
    <form onSubmit={handleSubmit}>
      {/* Manual state management */}
    </form>
  );
}
```

**useActionState Pattern:**

```tsx
// Simplified with useActionState
async function submitAction(prev, formData) {
  try {
    await submitToServer(formData);
    return { success: true };
  } catch (error) {
    return { success: false, error: error.message };
  }
}

function Form() {
  const [state, formAction, isPending] = useActionState(
    submitAction,
    { success: false }
  );
  
  return (
    <form action={formAction}>
      {/* Automatic pending state */}
    </form>
  );
}
```

**Comparison:**

| Aspect | useState + useEffect | useActionState |
|--------|---------------------|----------------|
| **Code Lines** | ~20-30 | ~10-15 |
| **Loading State** | Manual | Automatic |
| **Error Handling** | try/catch | Return value |
| **Progressive Enhancement** | No | Yes (works without JS) |
| **Server Actions** | Complex | Natural |
| **Flexibility** | High | Form-specific |

**When to Use Each:**

```tsx
// ✅ useActionState:
// - Forms with Server Actions
// - Need progressive enhancement
// - Simple form → server pattern
// - Want automatic loading state

// ✅ useState + useEffect:
// - Complex client-side logic
// - Not a form submission
// - Need fine-grained control
// - Multiple async operations
```

**Deep Implication:**

useActionState is React embracing **platform primitives** (forms) while adding reactivity. It's not trying to replace all async state management - it's specialized for the form submission pattern. This specialization makes it simpler for its use case but less flexible for others.

---

## Senior SDE Interview Questions

### Question 1: Form Architecture Decision

**Question:** "You're building a checkout flow with 3 steps: cart review, shipping info, payment. Each step validates on the server. How would you architect this with useActionState?"

**Key Concepts Being Tested:**
- Multi-step form architecture
- Server validation handling
- State persistence
- UX considerations

**Expected Answer Should Cover:**

1. **Architecture Options:**
```tsx
// Option A: Single form, hidden steps
function Checkout() {
  const [step, setStep] = useState(1);
  const [state, formAction, isPending] = useActionState(submitCheckout, {});
  
  return (
    <form action={formAction}>
      <input type="hidden" name="step" value={step} />
      
      {step === 1 && <CartReview />}
      {step === 2 && <ShippingForm />}
      {step === 3 && <PaymentForm />}
      
      <button disabled={isPending}>
        {step < 3 ? 'Next' : 'Complete Purchase'}
      </button>
    </form>
  );
}

// Option B: Separate forms per step
function Checkout() {
  const [step, setStep] = useState(1);
  const [checkoutData, setCheckoutData] = useState({});
  
  if (step === 1) return <CartStep onNext={setStep} />;
  if (step === 2) return <ShippingStep onNext={setStep} />;
  return <PaymentStep data={checkoutData} />;
}
```

2. **Server Validation Strategy:**
```tsx
async function validateStep(prevState, formData) {
  const step = Number(formData.get('step'));
  
  // Step-specific validation
  const errors = await validateOnServer(step, formData);
  
  if (errors) {
    return { errors, step };
  }
  
  return { success: true, step: step + 1 };
}
```

**Follow-ups:**
1. "How do you persist data between steps?"
2. "What if users refresh the page mid-checkout?"
3. "How do you handle payment failures?"

**Red Flags:**
- Not considering server validation
- No strategy for step persistence
- Missing error recovery

**Green Flags:**
- Discusses pros/cons of architectures
- Considers UX (loading states, error handling)
- Mentions data persistence
- Talks about payment security

---

[← Previous: useFormStatus Hook](./22_useformstatus_hook.md) | [Back to Main](../README.md) | [Next: useOptimistic Hook →](./24_useoptimistic_hook.md)

---

**Progress**: Topic 23/220 completed | Part II: React Hooks (87% complete)
