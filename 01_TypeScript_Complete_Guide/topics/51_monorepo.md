# 📦 Monorepo with TypeScript - Managing Multiple Packages

[← Previous: Type-Safe APIs](./50_type_safe_apis.md) | [← Back to Main](../README.md) | [Next: Migration Strategies →](./52_migration.md)

---

## 📝 Overview

Monorepos allow multiple TypeScript packages to share code with type safety. This guide covers setting up monorepos with project references, shared types, and build optimization.

### 🎯 Learning Objectives

- ✅ Set up TypeScript monorepo
- ✅ Use project references
- ✅ Share types across packages
- ✅ Optimize monorepo builds

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐⭐ Advanced
- **Estimated Time**: 5-6 hours
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [Monorepo Structure](#structure)
2. [Project References](#project-references)
3. [Shared Types](#shared-types)
4. [Tools](#tools)

---

<a name="structure"></a>
## 1. Monorepo Structure

```
monorepo/
├── packages/
│   ├── shared/
│   │   ├── src/
│   │   ├── package.json
│   │   └── tsconfig.json
│   ├── frontend/
│   │   ├── src/
│   │   ├── package.json
│   │   └── tsconfig.json
│   └── backend/
│       ├── src/
│       ├── package.json
│       └── tsconfig.json
├── tsconfig.base.json
└── package.json
```

---

<a name="project-references"></a>
## 2. Project References

```json
// tsconfig.base.json
{
  "compilerOptions": {
    "composite": true,
    "declaration": true,
    "strict": true
  }
}

// packages/shared/tsconfig.json
{
  "extends": "../../tsconfig.base.json",
  "compilerOptions": {
    "outDir": "./dist",
    "rootDir": "./src"
  }
}

// packages/frontend/tsconfig.json
{
  "extends": "../../tsconfig.base.json",
  "references": [
    { "path": "../shared" }
  ]
}
```

---

<a name="tools"></a>
## 3. Tools

```bash
# Turborepo (recommended)
npx create-turbo@latest

# Nx
npx create-nx-workspace@latest

# Lerna (classic)
npm install -g lerna
lerna init
```

---

## 🎯 Key Takeaways

✅ **Project references** enable type sharing

✅ **Composite projects** for incremental builds

✅ **Turborepo/Nx** for task orchestration

✅ **Shared packages** reduce duplication

✅ **Type safety** across packages

---

[← Previous: Type-Safe APIs](./50_type_safe_apis.md) | [Next: Migration Strategies →](./52_migration.md)

**Progress**: Topic 51 of 63 | Part VIII: 83% Complete
