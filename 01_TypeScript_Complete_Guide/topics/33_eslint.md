# 🔍 Linting with ESLint - Code Quality for TypeScript

[← Previous: Build Tools](./32_build_tools.md) | [← Back to Main](../README.md) | [Next: Bundlers →](./34_bundlers.md)

---

## 📝 Overview

ESLint with TypeScript-specific plugins helps catch bugs, enforce code standards, and maintain consistency across your codebase. This guide covers setting up ESLint for TypeScript projects, essential rules, and integrating with your development workflow.

**What You'll Learn:**
- Setting up ESLint for TypeScript
- typescript-eslint plugin
- Essential rule configurations
- Integrating with VS Code
- Custom rules and plugins
- CI/CD integration

### 🎯 Learning Objectives

- ✅ Set up ESLint with TypeScript
- ✅ Configure typescript-eslint
- ✅ Apply essential rules
- ✅ Integrate with IDE
- ✅ Automate linting in workflows
- ✅ Create custom rules

### 📊 Section Info

- **Difficulty**: ⭐⭐ Intermediate
- **Estimated Time**: 3-4 hours
- **Prerequisites**: [TypeScript Setup](./07_typescript_setup.md)
- **Practice Exercises**: 8 tasks
- **Version**: ESLint 8+, typescript-eslint 6+ (2025)

---

## 📚 Table of Contents

1. [ESLint Setup](#setup)
2. [Essential Rules](#essential-rules)
3. [IDE Integration](#ide-integration)
4. [Custom Configurations](#custom-configs)
5. [CI/CD Integration](#cicd)
6. [Best Practices](#best-practices)

---

<a name="setup"></a>
## 1. ESLint Setup

### 1.1 Installation

```bash
npm install --save-dev eslint @typescript-eslint/parser @typescript-eslint/eslint-plugin
```

### 1.2 Configuration

```javascript
// .eslintrc.js
module.exports = {
  parser: '@typescript-eslint/parser',
  parserOptions: {
    project: './tsconfig.json',
    ecmaVersion: 2022,
    sourceType: 'module'
  },
  plugins: ['@typescript-eslint'],
  extends: [
    'eslint:recommended',
    'plugin:@typescript-eslint/recommended',
    'plugin:@typescript-eslint/recommended-requiring-type-checking'
  ],
  rules: {
    '@typescript-eslint/no-unused-vars': 'error',
    '@typescript-eslint/no-explicit-any': 'warn',
    '@typescript-eslint/explicit-function-return-type': 'off'
  }
};
```

---

<a name="essential-rules"></a>
## 2. Essential Rules

### 2.1 TypeScript-Specific Rules

```javascript
{
  "rules": {
    // Type safety
    "@typescript-eslint/no-explicit-any": "warn",
    "@typescript-eslint/no-unsafe-assignment": "error",
    "@typescript-eslint/no-unsafe-member-access": "error",
    
    // Best practices
    "@typescript-eslint/prefer-nullish-coalescing": "warn",
    "@typescript-eslint/prefer-optional-chain": "warn",
    "@typescript-eslint/no-non-null-assertion": "warn",
    
    // Code quality
    "@typescript-eslint/no-unused-vars": ["error", {
      "argsIgnorePattern": "^_"
    }],
    "@typescript-eslint/naming-convention": ["error", {
      "selector": "interface",
      "format": ["PascalCase"]
    }]
  }
}
```

---

<a name="best-practices"></a>
## 3. Best Practices

### 3.1 Recommended Configuration

```javascript
// .eslintrc.js - Production-ready config
module.exports = {
  parser: '@typescript-eslint/parser',
  parserOptions: {
    project: './tsconfig.json'
  },
  extends: [
    'eslint:recommended',
    'plugin:@typescript-eslint/recommended',
    'plugin:@typescript-eslint/recommended-requiring-type-checking',
    'prettier' // Disable formatting rules (Prettier handles it)
  ],
  rules: {
    // Customize as needed
  }
};
```

**Package.json scripts:**

```json
{
  "scripts": {
    "lint": "eslint src --ext .ts,.tsx",
    "lint:fix": "eslint src --ext .ts,.tsx --fix",
    "type-check": "tsc --noEmit"
  }
}
```

---

## 🎯 Key Takeaways

✅ **ESLint** catches code quality issues

✅ **typescript-eslint** provides TS-specific rules

✅ **Integrate with IDE** for instant feedback

✅ **Run in CI/CD** to enforce standards

✅ **Combine with Prettier** for complete code quality

✅ **Customize rules** for your team's needs

---

[← Previous: Build Tools](./32_build_tools.md) | [Next: Bundlers →](./34_bundlers.md)

**Progress**: Topic 33 of 63 | Part V: 50% Complete
