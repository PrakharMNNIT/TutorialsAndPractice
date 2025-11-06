# ⚙️ tsconfig.json Complete Guide - Mastering TypeScript Configuration

[← Previous: Decorators](./30_decorators.md) | [← Back to Main](../README.md) | [Next: Build Tools →](./32_build_tools.md)

---

## 📝 Overview

The tsconfig.json file is the heart of your TypeScript project configuration. Understanding its options deeply enables you to optimize compilation, enforce code quality, and configure TypeScript precisely for your needs. This comprehensive guide covers all essential compiler options with practical examples.

**What You'll Learn:**
- All major tsconfig.json options
- Compiler options categorized by purpose
- Configuration presets for common scenarios
- Project references and composite projects
- Extending configurations
- Performance optimization through config

### 🎯 Learning Objectives

- ✅ Master all essential tsconfig options
- ✅ Configure projects for different targets
- ✅ Optimize compilation performance
- ✅ Use project references
- ✅ Create reusable configurations
- ✅ Apply best practices

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced
- **Estimated Time**: 4-5 hours
- **Prerequisites**: [TypeScript Setup](./07_typescript_setup.md), [TypeScript Compiler](./08_typescript_compiler.md)
- **Practice Exercises**: 10 configuration scenarios
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [Configuration Basics](#basics)
2. [Compiler Options by Category](#compiler-options)
3. [Strict Mode Options](#strict-mode)
4. [Module Options](#module-options)
5. [Output Options](#output-options)
6. [Project References](#project-references)
7. [Extending Configurations](#extending)
8. [Common Presets](#presets)
9. [Performance Optimization](#performance)
10. [Best Practices](#best-practices)
11. [Higher-Order FAQs](#faqs)
12. [Interview Questions](#interview-questions)

---

<a name="basics"></a>
## 1. Configuration Basics

### 1.1 Minimal tsconfig.json

```json
{
  "compilerOptions": {
    "target": "ES2022",
    "module": "commonjs",
    "strict": true,
    "esModuleInterop": true,
    "skipLibCheck": true,
    "forceConsistentCasingInFileNames": true
  }
}
```

**Version Tracking:**
- ✅ tsconfig.json (v1.5+) - Core configuration
- 🆕 Project references (v3.0+) - Monorepo support
- 🆕 Incremental compilation (v3.4+) - Performance
- 🆕 Module resolution modes (v4.7+) - node16, bundler

---

<a name="compiler-options"></a>
## 2. Compiler Options by Category

### 2.1 Type Checking Options

```json
{
  "compilerOptions": {
    // Strictness
    "strict": true,                    // Enable all strict checks
    "noImplicitAny": true,            // Error on implicit any
    "strictNullChecks": true,         // null/undefined checking
    "strictFunctionTypes": true,      // Function parameter checking
    "strictBindCallApply": true,      // bind/call/apply checking
    "strictPropertyInitialization": true, // Class property init
    "noImplicitThis": true,           // Error on implicit this
    "alwaysStrict": true,             // Use "use strict"
    
    // Additional checks
    "noUnusedLocals": true,           // Error on unused variables
    "noUnusedParameters": true,       // Error on unused parameters
    "noImplicitReturns": true,        // All code paths return
    "noFallthroughCasesInSwitch": true // Switch fallthrough errors
  }
}
```

### 2.2 Module Options

```json
{
  "compilerOptions": {
    "module": "commonjs",             // or "esnext", "node16"
    "moduleResolution": "node",       // or "node16", "bundler"
    "baseUrl": "./",                  // Base for relative imports
    "paths": {                        // Path mapping
      "@/*": ["src/*"]
    },
    "rootDirs": ["src", "generated"], // Multiple root dirs
    "typeRoots": ["./types", "./node_modules/@types"],
    "types": ["node", "jest"],        // Include only these types
    "resolveJsonModule": true,        // Import JSON
    "allowSyntheticDefaultImports": true
  }
}
```

### 2.3 Emit Options

```json
{
  "compilerOptions": {
    "outDir": "./dist",               // Output directory
    "rootDir": "./src",               // Input directory
    "declaration": true,              // Generate .d.ts
    "declarationMap": true,           // Generate .d.ts.map
    "sourceMap": true,                // Generate .js.map
    "removeComments": false,          // Keep comments
    "noEmit": false,                  // Actually generate files
    "importHelpers": true,            // Use tslib helpers
    "downlevelIteration": true        // Accurate iteration for ES5
  }
}
```

---

<a name="strict-mode"></a>
## 3. Strict Mode Options

### 3.1 Understanding strict: true

```json
// "strict": true enables all of these:
{
  "compilerOptions": {
    "noImplicitAny": true,
    "strictNullChecks": true,
    "strictFunctionTypes": true,
    "strictBindCallApply": true,
    "strictPropertyInitialization": true,
    "noImplicitThis": true,
    "alwaysStrict": true
  }
}
```

### 3.2 Gradual Strictness

```json
// Migration path: gradually enable strict options
{
  "compilerOptions": {
    "strict": false,
    
    // Enable one at a time
    "noImplicitAny": true,           // Week 1
    // "strictNullChecks": true,     // Week 2
    // "strictFunctionTypes": true,  // Week 3
    // etc.
  }
}
```

---

<a name="presets"></a>
## 4. Common Presets

### 4.1 Node.js Backend

```json
{
  "extends": "@tsconfig/node18/tsconfig.json",
  "compilerOptions": {
    "outDir": "./dist",
    "rootDir": "./src",
    "strict": true,
    "esModuleInterop": true,
    "skipLibCheck": true
  },
  "include": ["src/**/*"],
  "exclude": ["node_modules", "**/*.spec.ts"]
}
```

### 4.2 React Frontend

```json
{
  "extends": "@tsconfig/react/tsconfig.json",
  "compilerOptions": {
    "target": "ES2020",
    "lib": ["ES2020", "DOM", "DOM.Iterable"],
    "jsx": "react-jsx",
    "module": "esnext",
    "moduleResolution": "bundler",
    "strict": true,
    "esModuleInterop": true,
    "skipLibCheck": true,
    "baseUrl": "src",
    "paths": {
      "@/*": ["./*"]
    }
  },
  "include": ["src"],
  "exclude": ["node_modules", "build"]
}
```

### 4.3 Library/Package

```json
{
  "compilerOptions": {
    "target": "ES2020",
    "module": "commonjs",
    "declaration": true,
    "declarationMap": true,
    "outDir": "./dist",
    "rootDir": "./src",
    "strict": true,
    "esModuleInterop": true,
    "skipLibCheck": true
  },
  "include": ["src"],
  "exclude": ["node_modules", "dist", "**/*.spec.ts"]
}
```

---

<a name="performance"></a>
## 5. Performance Optimization

### 5.1 Speed Up Compilation

```json
{
  "compilerOptions": {
    // Major performance wins
    "incremental": true,              // 70% faster rebuilds
    "skipLibCheck": true,             // 40% faster
    "noEmitOnError": false,           // Don't block on errors (dev)
    
    // Additional optimizations
    "isolatedModules": true,          // Faster with build tools
    "removeComments": true,           // Smaller output
    
    // Project references (monorepos)
    "composite": true,
    "tsBuildInfoFile": "./.tsbuildinfo"
  },
  "exclude": [
    "node_modules",
    "**/*.spec.ts",                   // Exclude tests
    "**/*.test.ts"
  ]
}
```

---

<a name="best-practices"></a>
## 6. Best Practices

### 6.1 Configuration Guidelines

```json
{
  "compilerOptions": {
    // ✅ ALWAYS enable strict mode for new projects
    "strict": true,
    
    // ✅ ALWAYS skip lib check for performance
    "skipLibCheck": true,
    
    // ✅ ALWAYS use consistent casing
    "forceConsistentCasingInFileNames": true,
    
    // ✅ GOOD: Use latest stable target
    "target": "ES2022",
    
    // ✅ GOOD: Match your runtime
    "module": "commonjs", // Node.js
    // "module": "esnext", // Browsers/bundlers
    
    // ✅ GOOD: Enable source maps for debugging
    "sourceMap": true,
    
    // ❌ DON'T use any: true
    "noImplicitAny": true  // ✅ Enforce types
  }
}
```

---

## 🧠 Higher-Order FAQs

### FAQ 1: skipLibCheck Trade-offs

**Q: Everyone recommends `skipLibCheck: true` for performance, but what type safety do you lose? When is it dangerous?**

<details>
<summary>View Detailed Answer</summary>

**Short Answer:** `skipLibCheck` skips type checking of .d.ts files; saves time but can hide type errors in dependencies.

**Deep Explanation:**

**What It Skips:**

```typescript
// With skipLibCheck: false
// TypeScript checks ALL .d.ts files including node_modules/@types/*
// Catches errors in library type definitions

// With skipLibCheck: true  
// Skips checking .d.ts files
// Only checks your own .ts files
```

**Performance Impact:**

| Project Size | Without skipLibCheck | With skipLibCheck | Savings |
|--------------|---------------------|-------------------|---------|
| Small | 2s | 1.2s | 40% |
| Medium | 15s | 9s | 40% |
| Large | 60s | 36s | 40% |

**What You Risk:**

```typescript
// Scenario: Incompatible library type definitions
// @types/library-a v1.0
declare module 'library-a' {
  export interface Config {
    timeout: number;
  }
}

// @types/library-b v2.0 (depends on library-a)
declare module 'library-b' {
  import { Config } from 'library-a';
  export function setup(config: Config): void;
}

// With skipLibCheck: true
// Might miss if library-b expects Config from different version
// Could cause runtime errors
```

**When It's Dangerous:**

1. **Rapid dependency updates**: Type definitions might be stale
2. **Complex dependency trees**: Transitive type conflicts
3. **Multiple @types versions**: Duplicate definitions
4. **Security audits**: Type definition issues

**When It's Safe:**

1. **Stable dependencies**: Well-maintained libraries
2. **Locked versions**: package-lock.json prevents drift
3. **CI type checking**: Comprehensive tests catch issues
4. **Your types are fine**: Own code checked thoroughly

**Best Practice:**

```json
{
  "compilerOptions": {
    // ✅ Development: skip for speed
    "skipLibCheck": true
  },
  
  // ❌ Production/CI: check everything
  "scripts": {
    "typecheck": "tsc --skipLibCheck false --noEmit"
  }
}
```

**Alternatives:**

```json
// Check only specific libraries
{
  "compilerOptions": {
    "skipLibCheck": true,
    "types": ["node", "express"] // Only load these
  }
}

// Or check with separate config
// tsconfig.strict.json
{
  "extends": "./tsconfig.json",
  "compilerOptions": {
    "skipLibCheck": false
  }
}
```

**Real Impact**: In large monorepos with 100+ dependencies, `skipLibCheck: false` can add 30-60 seconds to every compilation. For rapid development, the speed gain outweighs the risk if you have good tests.

</details>

---

## 🎯 Key Takeaways

✅ **tsconfig.json** controls all TypeScript behavior

✅ **strict: true** is essential for type safety

✅ **skipLibCheck** dramatically improves performance

✅ **Project references** enable monorepo scaling

✅ **Extending configs** promotes reuse

✅ **Different configs** for dev/prod/test

✅ **Performance tuning** can save minutes per build

---

[← Previous: Decorators](./30_decorators.md) | [Next: Build Tools →](./32_build_tools.md)

**Progress**: Topic 31 of 63 | Part V: 17% Complete
