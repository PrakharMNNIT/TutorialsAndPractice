# ⚛️ TypeScript for Frontend - React, Vue, Angular

[← Previous: CLI Tool](./54_cli_tool_project.md) | [← Back to Main](../README.md) | [Next: Backend Node.js →](./56_backend_nodejs.md)

---

## 📝 Overview

TypeScript enhances frontend development with type safety for components, props, state, and events. This guide covers using TypeScript with React, Vue, and Angular.

### 🎯 Learning Objectives

- ✅ Use TypeScript with React
- ✅ Use TypeScript with Vue 3
- ✅ Understand Angular's TypeScript
- ✅ Type components safely

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced
- **Estimated Time**: 6-8 hours
- **Version**: React 18+, Vue 3+, Angular 17+ (2025)

---

## 📚 Table of Contents

1. [React with TypeScript](#react)
2. [Vue 3 with TypeScript](#vue)
3. [Angular TypeScript](#angular)
4. [Best Practices](#best-practices)

---

<a name="react"></a>
## 1. React with TypeScript

```tsx
// Type-safe React component
interface ButtonProps {
  label: string;
  onClick: () => void;
  disabled?: boolean;
}

function Button({ label, onClick, disabled }: ButtonProps) {
  return (
    <button onClick={onClick} disabled={disabled}>
      {label}
    </button>
  );
}

// Usage
<Button label="Click me" onClick={() => console.log('Clicked')} />
```

---

<a name="vue"></a>
## 2. Vue 3 with TypeScript

```vue
<script setup lang="ts">
interface User {
  name: string;
  age: number;
}

const user: User = {
  name: 'Alice',
  age: 30
};

function greet(name: string): string {
  return `Hello, ${name}!`;
}
</script>

<template>
  <div>
    <h1>{{ greet(user.name) }}</h1>
    <p>Age: {{ user.age }}</p>
  </div>
</template>
```

---

## 🎯 Key Takeaways

✅ **React**: Type props and state

✅ **Vue 3**: Composition API with TypeScript

✅ **Angular**: Built with TypeScript

✅ **Type events** and handlers

✅ **Framework types** are excellent

---

[← Previous: CLI Tool](./54_cli_tool_project.md) | [Next: Backend Node.js →](./56_backend_nodejs.md)

**Progress**: Topic 55 of 63 | Part IX: 75% Complete
