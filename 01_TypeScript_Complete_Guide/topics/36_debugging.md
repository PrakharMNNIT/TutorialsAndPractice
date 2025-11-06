# 🐛 Debugging TypeScript - Source Maps & Dev Tools

[← Previous: Testing](./35_testing.md) | [← Back to Main](../README.md) | [Next: Concurrency Model →](./37_concurrency_model.md)

---

## 📝 Overview

Debugging TypeScript effectively requires understanding source maps, configuring your IDE, and using browser/Node.js debugging tools. This guide covers everything you need to debug TypeScript like a pro.

**What You'll Learn:**
- Source map configuration
- VS Code debugging setup
- Browser DevTools with TypeScript
- Node.js debugging
- Common debugging techniques

### 🎯 Learning Objectives

- ✅ Configure source maps
- ✅ Debug in VS Code
- ✅ Use browser DevTools
- ✅ Debug Node.js TypeScript
- ✅ Apply debugging best practices

### 📊 Section Info

- **Difficulty**: ⭐⭐ Intermediate
- **Estimated Time**: 3-4 hours
- **Prerequisites**: [TypeScript Compiler](./08_typescript_compiler.md)
- **Version**: TypeScript 5.7+ (2025)

---

## 📚 Table of Contents

1. [Source Maps](#source-maps)
2. [VS Code Debugging](#vscode)
3. [Browser DevTools](#browser)
4. [Node.js Debugging](#nodejs)
5. [Best Practices](#best-practices)

---

<a name="source-maps"></a>
## 1. Source Maps

### 1.1 Enable Source Maps

```json
// tsconfig.json
{
  "compilerOptions": {
    "sourceMap": true,
    // OR inline for single file
    "inlineSourceMap": true,
    "inlineSources": true
  }
}
```

---

<a name="vscode"></a>
## 2. VS Code Debugging

### 2.1 Launch Configuration

```json
// .vscode/launch.json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "node",
      "request": "launch",
      "name": "Debug TypeScript",
      "program": "${workspaceFolder}/src/index.ts",
      "preLaunchTask": "npm: build",
      "outFiles": ["${workspaceFolder}/dist/**/*.js"],
      "sourceMaps": true
    }
  ]
}
```

---

<a name="best-practices"></a>
## 3. Best Practices

```typescript
// ✅ GOOD: Use debugger statement
function complex() {
  debugger; // Breakpoint in code
  // Logic here
}

// ✅ GOOD: Conditional logging
if (process.env.DEBUG) {
  console.log('Debug info');
}

// ✅ GOOD: Type-safe logging
const logger = (message: string, data?: unknown) => {
  console.log(message, data);
};
```

---

## 🎯 Key Takeaways

✅ **Source maps** enable debugging TypeScript

✅ **VS Code** has excellent TypeScript debugging

✅ **Browser DevTools** work with source maps

✅ **debugger statement** creates breakpoints

✅ **Always enable** source maps in development

---

[← Previous: Testing](./35_testing.md) | [Next: Concurrency Model →](./37_concurrency_model.md)

**Progress**: Topic 36 of 63 | Part V: 100% Complete ✅
