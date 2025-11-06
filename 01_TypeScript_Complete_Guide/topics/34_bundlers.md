# 📦 TypeScript with Bundlers - Vite, Webpack, Rollup

[← Previous: ESLint](./33_eslint.md) | [← Back to Main](../README.md) | [Next: Testing →](./35_testing.md)

---

## 📝 Overview

Modern bundlers handle more than just bundling—they provide dev servers, hot module replacement, optimization, and more. This guide covers using TypeScript with Vite, Webpack, and Rollup, helping you choose and configure the right bundler for your project.

**What You'll Learn:**
- Vite with TypeScript (recommended)
- Webpack configuration
- Rollup for libraries
- Performance comparisons
- When to use each bundler

### 🎯 Learning Objectives

- ✅ Use Vite with TypeScript
- ✅ Configure Webpack for TypeScript
- ✅ Set up Rollup for libraries
- ✅ Choose appropriate bundler
- ✅ Optimize bundle performance

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced
- **Estimated Time**: 4-5 hours
- **Prerequisites**: [Build Tools](./32_build_tools.md)
- **Version**: Vite 5+, Webpack 5+, Rollup 4+ (2025)

---

## 📚 Table of Contents

1. [Vite with TypeScript](#vite)
2. [Webpack Configuration](#webpack)
3. [Rollup for Libraries](#rollup)
4. [Bundler Comparison](#comparison)
5. [Best Practices](#best-practices)

---

<a name="vite"></a>
## 1. Vite with TypeScript

### 1.1 Quick Start

```bash
# Create Vite + TypeScript project
npm create vite@latest my-app -- --template vanilla-ts
cd my-app
npm install
npm run dev
```

```typescript
// vite.config.ts
import { defineConfig } from 'vite';

export default defineConfig({
  build: {
    target: 'es2020',
    sourcemap: true
  },
  resolve: {
    alias: {
      '@': '/src'
    }
  }
});
```

**Why Vite?**
- ⚡ Instant dev server start
- 🔥 Lightning-fast HMR
- 📦 Optimized production builds
- 🎯 Zero config for TypeScript

---

<a name="comparison"></a>
## 2. Bundler Comparison

| Feature | Vite | Webpack | Rollup |
|---------|------|---------|--------|
| **Speed** | Very Fast | Medium | Medium |
| **HMR** | Excellent | Good | Limited |
| **Config** | Simple | Complex | Medium |
| **Best For** | Apps | Everything | Libraries |
| **TypeScript** | Native | Plugin | Plugin |

**Recommendation 2025:**
- **Web apps**: Vite
- **Libraries**: Rollup or tsup
- **Legacy**: Webpack
- **Enterprise**: Webpack (mature)

---

<a name="best-practices"></a>
## 3. Best Practices

```json
{
  "scripts": {
    "dev": "vite",
    "build": "tsc --noEmit && vite build",
    "preview": "vite preview"
  }
}
```

---

## 🎯 Key Takeaways

✅ **Vite** is fastest for modern development

✅ **Webpack** for mature ecosystem

✅ **Rollup** optimized for libraries

✅ **Always type-check** with tsc --noEmit

✅ **Modern bundlers** are 10-100x faster than old tools

---

[← Previous: ESLint](./33_eslint.md) | [Next: Testing →](./35_testing.md)

**Progress**: Topic 34 of 63 | Part V: 67% Complete
