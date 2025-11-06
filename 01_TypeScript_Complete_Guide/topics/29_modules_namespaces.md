# 📦 Modules & Namespaces - Organizing TypeScript Code

[← Previous: Declaration Files](./28_declaration_files.md) | [← Back to Main](../README.md) | [Next: Decorators →](./30_decorators.md)

---

## 📝 Overview

TypeScript supports two systems for organizing code: ES Modules (modern, preferred) and Namespaces (legacy). Understanding both is important for working with existing codebases and choosing the right approach for new projects.

**What You'll Learn:**
- ES Modules (import/export)
- TypeScript namespaces
- Module resolution strategies
- When to use modules vs namespaces
- Organizing large TypeScript projects
- Module best practices

### 🎯 Learning Objectives

- ✅ Master ES Module syntax
- ✅ Understand TypeScript namespaces
- ✅ Configure module resolution
- ✅ Organize code effectively
- ✅ Choose modules over namespaces
- ✅ Apply module best practices

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced
- **Estimated Time**: 4-5 hours
- **Prerequisites**: [TypeScript Setup](./07_typescript_setup.md), [Interfaces](./12_objects_interfaces.md)
- **Practice Exercises**: 10 challenges
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [ES Modules](#es-modules)
2. [Namespaces (Legacy)](#namespaces)
3. [Module Resolution](#module-resolution)
4. [Modules vs Namespaces](#modules-vs-namespaces)
5. [Organizing Projects](#organizing)
6. [Best Practices](#best-practices)
7. [Higher-Order FAQs](#faqs)
8. [Interview Questions](#interview-questions)

---

<a name="es-modules"></a>
## 1. ES Modules

### 1.1 Basic Import/Export

```typescript
// math.ts - Named exports
export const PI = 3.14159;

export function add(a: number, b: number): number {
  return a + b;
}

export class Calculator {
  multiply(a: number, b: number): number {
    return a * b;
  }
}

// main.ts - Named imports
import { PI, add, Calculator } from './math';

console.log(add(5, 3));
console.log(PI);

// Import all
import * as Math from './math';
console.log(Math.add(5, 3));
```

**Version Tracking:**
- ✅ ES Modules (v1.5+) - Modern standard
- ✅ Namespaces (v1.0+) - Legacy, avoid in new code
- 🆕 Module resolution modes (v4.7+) - node16, bundler
- 🆕 Import assertions (v4.5+) - JSON imports

### 1.2 Default Exports

```typescript
// user.ts - Default export
export default class User {
  constructor(public name: string) {}
}

// main.ts - Default import
import User from './user';

const user = new User("Alice");
```

---

<a name="namespaces"></a>
## 2. Namespaces (Legacy)

### 2.1 Namespace Syntax

```typescript
// namespace.ts - Old style (avoid in new code)
namespace Shapes {
  export interface Circle {
    radius: number;
  }
  
  export function area(circle: Circle): number {
    return Math.PI * circle.radius ** 2;
  }
}

// Usage
const circle: Shapes.Circle = { radius: 10 };
console.log(Shapes.area(circle));

// ⚠️ Namespaces are legacy - use ES modules instead
```

---

<a name="module-resolution"></a>
## 3. Module Resolution

### 3.1 Resolution Strategies

```json
// tsconfig.json
{
  "compilerOptions": {
    // Classic (legacy)
    "moduleResolution": "classic",
    
    // Node (traditional)
    "moduleResolution": "node",
    
    // Node16/NodeNext (modern, recommended)
    "moduleResolution": "node16",
    
    // Bundler (Vite, webpack)
    "moduleResolution": "bundler"
  }
}
```

### 3.2 Path Mapping

```json
// tsconfig.json
{
  "compilerOptions": {
    "baseUrl": "./src",
    "paths": {
      "@components/*": ["components/*"],
      "@utils/*": ["utils/*"],
      "@types/*": ["types/*"]
    }
  }
}
```

```typescript
// Usage with path aliases
import { Button } from '@components/Button';
import { formatDate } from '@utils/date';
import { User } from '@types/user';
```

---

<a name="modules-vs-namespaces"></a>
## 4. Modules vs Namespaces

### 4.1 Comparison

| Feature | ES Modules | Namespaces |
|---------|-----------|------------|
| **Standard** | ES6+ standard | TypeScript-specific |
| **Syntax** | import/export | namespace {} |
| **File scope** | Each file is module | Global by default |
| **Tree-shaking** | Yes | No |
| **Modern tooling** | Full support | Limited |
| **Recommendation** | ✅ Use | ❌ Avoid |

### 4.2 Migration from Namespaces

```typescript
// Old namespace approach
namespace Utils {
  export function format(value: number): string {
    return value.toFixed(2);
  }
}

// Modern module approach
// utils.ts
export function format(value: number): string {
  return value.toFixed(2);
}

// main.ts
import { format } from './utils';
```

---

<a name="organizing"></a>
## 5. Organizing Projects

### 5.1 Recommended Structure

```
src/
├── components/
│   ├── Button.ts
│   ├── Input.ts
│   └── index.ts      # Barrel export
├── utils/
│   ├── date.ts
│   ├── string.ts
│   └── index.ts
├── types/
│   ├── user.ts
│   ├── product.ts
│   └── index.ts
└── index.ts          # Main entry
```

### 5.2 Barrel Exports

```typescript
// components/index.ts - Barrel file
export { Button } from './Button';
export { Input } from './Input';
export { Card } from './Card';

// Usage - single import
import { Button, Input, Card } from './components';
```

---

<a name="best-practices"></a>
## 6. Best Practices

### 6.1 Module Guidelines

```typescript
// ✅ GOOD: Use ES modules
import { Something } from './module';

// ✅ GOOD: Named exports for multiple items
export { add, subtract, multiply };

// ✅ GOOD: Default export for single main export
export default class MainComponent {}

// ❌ BAD: Using namespaces in new code
namespace MyNamespace { } // Legacy

// ❌ BAD: Mixing default and named without reason
export default function main() {}
export const helper = () => {}; // Confusing

// ✅ GOOD: Use path aliases
import { User } from '@types/user';

// ❌ BAD: Deep relative imports
import { User } from '../../../types/user';
```

---

## 🎯 Key Takeaways

✅ **ES Modules** are the modern standard (use these)

✅ **Namespaces** are legacy (avoid in new code)

✅ **Module resolution** can be configured

✅ **Path mapping** simplifies imports

✅ **Barrel exports** provide clean API

✅ **Organize by feature** for scalability

✅ **Use ES modules** over namespaces always

---

[← Previous: Declaration Files](./28_declaration_files.md) | [Next: Decorators →](./30_decorators.md)

**Progress**: Topic 29 of 63 | Part IV: 90% Complete
