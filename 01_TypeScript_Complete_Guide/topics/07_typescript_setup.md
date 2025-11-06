# 🛠️ Setting Up TypeScript - Complete Development Environment Guide

[← Previous: TypeScript Intro](./06_typescript_intro.md) | [← Back to Main](../README.md) | [Next: TypeScript Compiler →](./08_typescript_compiler.md)

---

## 📝 Overview

Setting up TypeScript correctly is crucial for a smooth development experience. This comprehensive guide covers everything from basic installation to production-ready configurations, IDE setup, and project scaffolding across different environments.

**What You'll Learn:**
- Install and configure TypeScript in any environment
- Set up your IDE for optimal TypeScript development
- Create and configure TypeScript projects from scratch
- Understand tsconfig.json basics
- Use TypeScript with popular frameworks

### 🎯 Learning Objectives

By the end of this guide, you will:

- ✅ Install TypeScript globally and locally
- ✅ Configure VS Code for TypeScript development
- ✅ Create TypeScript projects from scratch
- ✅ Understand basic tsconfig.json options
- ✅ Set up TypeScript with Node.js
- ✅ Integrate TypeScript with build tools
- ✅ Troubleshoot common setup issues

### 📊 Section Info

- **Difficulty**: ⭐ Beginner
- **Estimated Time**: 2-3 hours
- **Prerequisites**: [JavaScript Fundamentals](./01_javascript_fundamentals.md), [TypeScript Intro](./06_typescript_intro.md), Node.js installed
- **Practice Exercises**: 5 hands-on setup tasks
- **Version**: TypeScript 5.7+, Node.js 18+ (2025)

---

## 📚 Table of Contents

1. [Prerequisites Check](#prerequisites)
2. [Installing TypeScript](#installing-typescript)
3. [IDE Setup (VS Code)](#ide-setup)
4. [Your First TypeScript Project](#first-project)
5. [tsconfig.json Basics](#tsconfig-basics)
6. [Project Templates](#project-templates)
7. [TypeScript with Frameworks](#typescript-frameworks)
8. [Troubleshooting Common Issues](#troubleshooting)
9. [Higher-Order Thinking FAQs](#faqs)
10. [Senior SDE Interview Questions](#interview-questions)

---

<a name="prerequisites"></a>
## 1. Prerequisites Check

### 1.1 Required Tools

```bash
# Check Node.js version (need 18+)
node --version
# v20.10.0 or higher recommended

# Check npm version
npm --version
# 10.0.0 or higher

# Alternative: Use Yarn
yarn --version
# 1.22.0 or higher

# Alternative: Use pnpm (fastest)
pnpm --version
# 8.0.0 or higher
```

### 1.2 Installation Table

| Tool | Minimum Version | Recommended | Installation |
|------|----------------|-------------|--------------|
| **Node.js** | 18.0.0 | 20.10.0+ (LTS) | [nodejs.org](https://nodejs.org) |
| **npm** | 9.0.0 | 10.0.0+ | Comes with Node |
| **VS Code** | 1.80.0 | Latest | [code.visualstudio.com](https://code.visualstudio.com) |
| **TypeScript** | 5.0.0 | 5.7.0+ | `npm install typescript` |

### 1.3 System Requirements

```bash
# Check your system
echo "OS: $(uname -s)"
echo "Architecture: $(uname -m)"

# Recommended specs:
# - RAM: 4GB minimum, 8GB+ recommended
# - Disk: 500MB for Node.js + TypeScript
# - OS: Windows 10+, macOS 10.15+, or modern Linux
```

---

<a name="installing-typescript"></a>
## 2. Installing TypeScript

### 2.1 Global Installation

```bash
# Install TypeScript globally (available everywhere)
npm install -g typescript

# Verify installation
tsc --version
# Version 5.7.2

# Check compiler location
which tsc
# /usr/local/bin/tsc (Unix)
# C:\Users\<username>\AppData\Roaming\npm\tsc.cmd (Windows)

# Alternative: Using Yarn
yarn global add typescript

# Alternative: Using pnpm
pnpm add -g typescript
```

**⚠️ Global vs Local Installation:**

| Aspect | Global | Local (Recommended) |
|--------|--------|---------------------|
| **Command** | `npm install -g` | `npm install --save-dev` |
| **Access** | Available everywhere | Project-specific |
| **Version** | Single version | Per-project versions |
| **CI/CD** | May cause issues | Consistent builds |
| **Best For** | Quick scripts | Production projects |

### 2.2 Local Installation (Recommended)

```bash
# Create project directory
mkdir my-typescript-project
cd my-typescript-project

# Initialize npm project
npm init -y

# Install TypeScript as dev dependency
npm install --save-dev typescript

# Verify installation (use npx to run local)
npx tsc --version
# Version 5.7.2

# Add to package.json scripts
```

**package.json:**
```json
{
  "name": "my-typescript-project",
  "version": "1.0.0",
  "scripts": {
    "build": "tsc",
    "watch": "tsc --watch",
    "dev": "tsc --watch"
  },
  "devDependencies": {
    "typescript": "^5.7.0"
  }
}
```

### 2.3 TypeScript with Different Package Managers

```bash
# NPM (most common)
npm install --save-dev typescript

# Yarn (faster, deterministic)
yarn add --dev typescript

# pnpm (fastest, efficient)
pnpm add -D typescript

# Bun (newest, ultra-fast)
bun add -d typescript
```

---

<a name="ide-setup"></a>
## 3. IDE Setup (VS Code)

### 3.1 VS Code Extensions

**Essential Extensions:**

```bash
# Install from VS Code or use CLI
code --install-extension ms-vscode.vscode-typescript-next
code --install-extension dbaeumer.vscode-eslint
code --install-extension esbenp.prettier-vscode
code --install-extension usernamehw.errorlens
```

**Recommended Extensions:**

| Extension | Purpose | Priority |
|-----------|---------|----------|
| **ESLint** | Linting | 🔴 Essential |
| **Prettier** | Formatting | 🔴 Essential |
| **Error Lens** | Inline errors | 🟡 Recommended |
| **Path Intellisense** | Import autocomplete | 🟡 Recommended |
| **GitLens** | Git integration | 🟢 Optional |
| **Import Cost** | Bundle size | 🟢 Optional |

### 3.2 VS Code Settings

```json
// .vscode/settings.json
{
  // TypeScript
  "typescript.tsdk": "node_modules/typescript/lib",
  "typescript.preferences.importModuleSpecifier": "relative",
  "typescript.updateImportsOnFileMove.enabled": "always",
  "typescript.suggest.autoImports": true,
  
  // Editor
  "editor.formatOnSave": true,
  "editor.defaultFormatter": "esbenp.prettier-vscode",
  "editor.codeActionsOnSave": {
    "source.fixAll.eslint": true,
    "source.organizeImports": true
  },
  
  // Files
  "files.exclude": {
    "**/*.js": { "when": "$(basename).ts" },
    "**/*.js.map": true
  }
}
```

### 3.3 Keyboard Shortcuts

**Essential VS Code TypeScript Shortcuts:**

| Shortcut (Mac) | Shortcut (Win/Linux) | Action |
|----------------|---------------------|--------|
| `⌘ + .` | `Ctrl + .` | Quick fix |
| `F12` | `F12` | Go to definition |
| `⇧⌘ + F12` | `Shift + F12` | Find all references |
| `F2` | `F2` | Rename symbol |
| `⌃ + Space` | `Ctrl + Space` | Trigger autocomplete |
| `⌘ + Shift + O` | `Ctrl + Shift + O` | Go to symbol |

---

<a name="first-project"></a>
## 4. Your First TypeScript Project

### 4.1 Quick Start (5 Minutes)

```bash
# 1. Create project directory
mkdir hello-typescript
cd hello-typescript

# 2. Initialize npm
npm init -y

# 3. Install TypeScript
npm install --save-dev typescript

# 4. Create TypeScript config
npx tsc --init

# 5. Create source file
echo 'console.log("Hello, TypeScript!");' > hello.ts

# 6. Compile and run
npx tsc
node hello.js

# Output: Hello, TypeScript!
```

### 4.2 Project Structure

```
my-typescript-project/
├── src/
│   ├── index.ts          # Entry point
│   ├── utils/
│   │   └── helpers.ts    # Utility functions
│   └── types/
│       └── index.ts      # Type definitions
├── dist/                 # Compiled JavaScript (generated)
├── node_modules/         # Dependencies
├── package.json          # Project metadata
├── tsconfig.json         # TypeScript configuration
├── .gitignore            # Git ignore file
└── README.md            # Documentation
```

### 4.3 Complete Setup Example

```bash
# Create project
mkdir ts-project && cd ts-project

# Initialize with npm
npm init -y

# Install dependencies
npm install --save-dev typescript @types/node

# Create directory structure
mkdir src dist
touch src/index.ts

# Create tsconfig.json
npx tsc --init --rootDir src --outDir dist --esModuleInterop --resolveJsonModule --lib es2022 --module commonjs --allowJs true --noImplicitAny true
```

**src/index.ts:**
```typescript
// Your first TypeScript file
const message: string = "Hello, TypeScript!";

function greet(name: string): string {
  return `Hello, ${name}!`;
}

console.log(message);
console.log(greet("World"));
```

**package.json scripts:**
```json
{
  "scripts": {
    "build": "tsc",
    "start": "node dist/index.js",
    "dev": "tsc --watch",
    "clean": "rm -rf dist"
  }
}
```

### 4.4 Build and Run

```bash
# Compile TypeScript
npm run build

# Run compiled JavaScript
npm start

# Development mode (watch)
npm run dev

# Clean compiled files
npm run clean
```

---

<a name="tsconfig-basics"></a>
## 5. tsconfig.json Basics

### 5.1 Essential Configuration

```json
{
  "compilerOptions": {
    // 🎯 Where TypeScript looks for files
    "rootDir": "./src",
    
    // 🎯 Where compiled JavaScript goes
    "outDir": "./dist",
    
    // 🎯 JavaScript version to target
    "target": "ES2022",
    
    // 🎯 Module system
    "module": "commonjs",  // or "esnext" for ESM
    
    // 🎯 Type checking strictness
    "strict": true,
    
    // 🎯 Allow importing JSON
    "resolveJsonModule": true,
    
    // 🎯 Better module interop
    "esModuleInterop": true,
    
    // 🎯 Skip lib checking (faster)
    "skipLibCheck": true,
    
    // 🎯 Force consistent casing
    "forceConsistentCasingInFileNames": true
  },
  
  // 📁 Files to include
  "include": ["src/**/*"],
  
  // 📁 Files to exclude
  "exclude": ["node_modules", "dist"]
}
```

### 5.2 Configuration Presets

```json
// Strict Configuration (Recommended)
{
  "compilerOptions": {
    "strict": true,
    "noImplicitAny": true,
    "strictNullChecks": true,
    "strictFunctionTypes": true,
    "strictBindCallApply": true,
    "strictPropertyInitialization": true,
    "noImplicitThis": true,
    "alwaysStrict": true
  }
}

// Permissive Configuration (Migration)
{
  "compilerOptions": {
    "strict": false,
    "noImplicitAny": false,
    "allowJs": true
  }
}
```

### 5.3 Target and Module Options

```json
// Modern Browsers/Node
{
  "compilerOptions": {
    "target": "ES2022",
    "module": "esnext",
    "lib": ["ES2022", "DOM"]
  }
}

// Legacy Support
{
  "compilerOptions": {
    "target": "ES5",
    "module": "commonjs",
    "lib": ["ES2015", "DOM"]
  }
}

// Node.js Backend
{
  "compilerOptions": {
    "target": "ES2022",
    "module": "commonjs",
    "lib": ["ES2022"],
    "types": ["node"]
  }
}
```

---

<a name="project-templates"></a>
## 6. Project Templates

### 6.1 Node.js CLI Project

```bash
# Initialize project
npm init -y
npm install --save-dev typescript @types/node ts-node

# Create tsconfig.json
cat > tsconfig.json << EOF
{
  "compilerOptions": {
    "target": "ES2022",
    "module": "commonjs",
    "outDir": "./dist",
    "rootDir": "./src",
    "strict": true,
    "esModuleInterop": true,
    "skipLibCheck": true
  },
  "include": ["src/**/*"],
  "exclude": ["node_modules"]
}
EOF

# Create entry point
mkdir src
cat > src/index.ts << EOF
#!/usr/bin/env node
console.log("Hello from TypeScript CLI!");
EOF
```

### 6.2 Web Application Project

```bash
# Using Vite (recommended for modern apps)
npm create vite@latest my-app -- --template vanilla-ts
cd my-app
npm install
npm run dev

# Project structure:
# my-app/
# ├── src/
# │   ├── main.ts
# │   └── style.css
# ├── index.html
# ├── tsconfig.json
# └── package.json
```

### 6.3 Express Backend Project

```bash
# Initialize project
npm init -y
npm install express
npm install --save-dev typescript @types/node @types/express ts-node-dev

# Generate tsconfig
npx tsc --init --rootDir src --outDir dist --module commonjs --target ES2022 --strict

# Create server
mkdir src
cat > src/server.ts << EOF
import express from 'express';

const app = express();
const PORT = 3000;

app.get('/', (req, res) => {
  res.json({ message: 'Hello from TypeScript + Express!' });
});

app.listen(PORT, () => {
  console.log(\`Server running on http://localhost:\${PORT}\`);
});
EOF

# Add scripts to package.json
# "dev": "ts-node-dev --respawn src/server.ts"
# "build": "tsc"
# "start": "node dist/server.js"
```

---

<a name="typescript-frameworks"></a>
## 7. TypeScript with Frameworks

### 7.1 React + TypeScript

```bash
# Create React app with TypeScript
npx create-react-app my-app --template typescript

# Or with Vite (faster)
npm create vite@latest my-app -- --template react-ts

# Or with Next.js
npx create-next-app@latest my-app --typescript
```

**Component Example:**
```tsx
// src/components/Greeting.tsx
interface GreetingProps {
  name: string;
  age?: number;
}

export function Greeting({ name, age }: GreetingProps) {
  return (
    <div>
      <h1>Hello, {name}!</h1>
      {age && <p>Age: {age}</p>}
    </div>
  );
}
```

### 7.2 Vue 3 + TypeScript

```bash
# Create Vue app with TypeScript
npm create vue@latest my-app
# ✔ Add TypeScript? Yes
# ✔ Add JSX Support? Yes

cd my-app
npm install
npm run dev
```

**Component Example:**
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

### 7.3 NestJS (Backend Framework)

```bash
# Install NestJS CLI
npm install -g @nestjs/cli

# Create new project (includes TypeScript)
nest new my-project

# Run development server
cd my-project
npm run start:dev
```

---

<a name="troubleshooting"></a>
## 8. Troubleshooting Common Issues

### 8.1 Common Problems and Solutions

**Problem 1: "tsc: command not found"**

```bash
# Solution 1: Install TypeScript
npm install -g typescript

# Solution 2: Use npx (local installation)
npx tsc --version

# Solution 3: Add to PATH
export PATH="$PATH:./node_modules/.bin"
```

**Problem 2: "Cannot find module '@types/node'"**

```bash
# Solution: Install type definitions
npm install --save-dev @types/node
```

**Problem 3: "Property 'X' does not exist"**

```typescript
// Problem: Missing types
const obj = JSON.parse('{"name": "Alice"}');
// obj.name // ❌ Error

// Solution 1: Type assertion
const obj = JSON.parse('{"name": "Alice"}') as { name: string };

// Solution 2: Interface
interface Data {
  name: string;
}
const obj: Data = JSON.parse('{"name": "Alice"}');
```

**Problem 4: Slow Compilation**

```json
// tsconfig.json optimizations
{
  "compilerOptions": {
    "incremental": true,           // ✅ Enable incremental compilation
    "skipLibCheck": true,          // ✅ Skip type checking of declaration files
    "noEmitOnError": false,        // ✅ Emit JS even with errors (dev)
  },
  "exclude": ["node_modules", "**/*.spec.ts"] // ✅ Exclude test files
}
```

---

## 📊 Self-Assessment Checkpoints

### ✅ Checkpoint 1: Installation (After Section 2)

**At this point, you should be able to:**
- ✅ Check if TypeScript is installed (`tsc --version`)
- ✅ Install TypeScript globally or locally
- ✅ Run the TypeScript compiler

**Verification Exercise:**
```bash
# Can you run this successfully?
echo 'const x: number = 5; console.log(x);' > test.ts
tsc test.ts
node test.js
```

If this doesn't work, review [Installing TypeScript](#installing-typescript).

---

### ✅ Checkpoint 2: First Project (After Section 4)

**At this point, you should be able to:**
- ✅ Create a new TypeScript project from scratch
- ✅ Configure tsconfig.json with basic options
- ✅ Compile and run TypeScript code

**Verification Exercise:**
```typescript
// Create this file and compile it
// src/test.ts
function add(a: number, b: number): number {
  return a + b;
}

console.log(add(5, 3));
```

If compilation fails, review [Your First TypeScript Project](#first-project).

---

## 🧠 Higher-Order Thinking FAQs

### FAQ 1: Global vs Local TypeScript Installation

**Q: Why is local TypeScript installation recommended over global, even though global is more convenient?**

<details>
<summary>View Detailed Answer</summary>

**Short Answer:** Consistency and reproducibility across environments.

**Deep Explanation:**

Local installation ensures every developer and CI/CD system uses the **exact same TypeScript version**. Here's why this matters:

1. **Version Lock-in**: Global installation means one version systemwide. If Project A needs TS 4.5 and Project B needs TS 5.3, global installation creates conflicts.

2. **Reproducible Builds**: `package.json` locks TypeScript version via `devDependencies`. When teammates run `npm install`, they get identical tooling. Global installation can't guarantee this.

3. **CI/CD Reliability**: CI systems build from `package.json`. Global dependencies aren't tracked, causing "works on my machine" issues.

4. **Breaking Changes**: TypeScript evolves rapidly. A global update might break older projects. Local installation isolates projects from system-wide changes.

**Trade-offs:**
- ✅ Local: Reproducible, isolated, CI-friendly
- ❌ Local: Slightly more disk space, requires `npx` or npm scripts
- ✅ Global: Convenient for quick scripts
- ❌ Global: Version conflicts, not reproducible

**Best Practice:** Use local for projects, global only for experimentation.

</details>

---

### FAQ 2: TypeScript Configuration Philosophy

**Q: Why does TypeScript ship with `strict: false` by default when everyone recommends enabling it?**

<details>
<summary>View Detailed Answer</summary>

**Short Answer:** Gradual adoption and backward compatibility.

**Deep Explanation:**

This design decision reflects TypeScript's **pragmatic philosophy**:

1. **Migration Path**: Many developers migrate existing JavaScript codebases. If TypeScript defaulted to `strict: true`, migrating would require fixing thousands of errors immediately. `strict: false` allows incremental migration.

2. **Learning Curve**: Beginners benefit from relaxed rules initially. They can learn TypeScript's syntax without drowning in type errors. Once comfortable, they enable strict mode.

3. **Flexibility**: Some projects genuinely need relaxed type checking (e.g., prototypes, legacy code integration). Default `strict: false` accommodates this.

**However**, for new projects, `strict: true` is **strongly recommended** because:
- Catches more bugs at compile-time
- Prevents `any` type abuse
- Enforces null safety
- Produces higher quality code

**Evolution of Approach:**

| Project Type | Recommended strict Setting |
|--------------|---------------------------|
| New greenfield project | `strict: true` immediately |
| JavaScript migration | `strict: false` → gradually enable |
| Prototypes/experiments | `strict: false` (speed over safety) |
| Production libraries | `strict: true` (reliability critical) |

</details>

---

### FAQ 3: TypeScript Compiler Performance

**Q: Why is TypeScript compilation slow on large codebases, and how can project setup mitigate this?**

<details>
<summary>View Detailed Answer</summary>

**Short Answer:** Type checking is computationally expensive; proper configuration dramatically improves speed.

**Deep Explanation:**

TypeScript's type checker must:
1. Parse all source files
2. Build type graphs and relationships
3. Check every assignment, call, and reference
4. Infer types across file boundaries

**Performance Bottlenecks:**

| Issue | Impact | Solution |
|-------|--------|----------|
| Type checking `node_modules` | 10-30s | `skipLibCheck: true` |
| Full project rebuilds | 10-60s | `incremental: true` |
| Complex type inference | 5-20s | Explicit types, avoid deep inference |
| Large monorepos | 30-120s | Project references |
| Unused imports | 2-10s | `removeComments: true` |

**Optimization Setup:**

```json
{
  "compilerOptions": {
    // ⚡ Critical optimizations
    "incremental": true,                    // 70% faster rebuilds
    "skipLibCheck": true,                   // 40% faster initial compile
    
    // ⚡ Additional gains
    "noEmitOnError": false,                 // Don't block on errors (dev)
    "isolatedModules": true,                // Faster with build tools
    "useDefineForClassFields": true,        // Modern JS semantics
    
    // ⚡ Project references (monorepos)
    "composite": true,
    "declaration": true
  }
}
```

**Real-World Impact:**
- Without optimization: 45s compile time
- With optimization: 8s compile time
- With incremental rebuilds: 2s compile time

</details>

---

### FAQ 4: Configuration File Hierarchy

**Q: How do TypeScript configuration files inherit from each other, and when should you use extends?**

<details>
<summary>View Detailed Answer</summary>

**Short Answer:** `extends` allows configuration inheritance for monorepos and shared settings.

**Deep Explanation:**

TypeScript supports configuration inheritance through `extends`:

```json
// tsconfig.base.json (shared config)
{
  "compilerOptions": {
    "strict": true,
    "esModuleInterop": true,
    "skipLibCheck": true
  }
}

// tsconfig.json (extends base)
{
  "extends": "./tsconfig.base.json",
  "compilerOptions": {
    "outDir": "./dist",
    "rootDir": "./src"
  }
}
```

**When to Use Extends:**

1. **Monorepos**: Share config across packages
```
project/
├── tsconfig.base.json     # Shared settings
├── packages/
│   ├── frontend/
│   │   └── tsconfig.json  # extends: ../../tsconfig.base.json
│   └── backend/
│       └── tsconfig.json  # extends: ../../tsconfig.base.json
```

2. **Multiple Build Targets**: Different configs for dev/prod/test
```
project/
├── tsconfig.json          # Base config
├── tsconfig.prod.json     # extends: ./tsconfig.json
└── tsconfig.test.json     # extends: ./tsconfig.json
```

3. **Recommended Presets**: Use community configs
```bash
npm install --save-dev @tsconfig/node18
```

```json
{
  "extends": "@tsconfig/node18/tsconfig.json",
  "compilerOptions": {
    "outDir": "./dist"
  }
}
```

**Inheritance Rules:**
- Child config **overrides** parent settings
- Arrays are **replaced**, not merged
- `files`, `include`, `exclude` are combined intelligently

</details>

---

## 🎤 Senior SDE Interview Questions

### Interview Question 1: TypeScript Setup Architecture

**Q:** "You're joining a 50-person engineering team with 10 microservices, some using TypeScript 4.5, others on 5.3. How would you architect TypeScript configuration and tooling to ensure consistency while allowing per-service flexibility?"

**Key Concepts Being Tested:**
- Monorepo architecture understanding
- Configuration management at scale
- Versioning strategy
- Team collaboration patterns

**Expected Answer Should Cover:**

1. **Centralized Configuration Strategy**
```json
// Root tsconfig.base.json
{
  "compilerOptions": {
    "strict": true,
    "esModuleInterop": true,
    "skipLibCheck": true,
    "forceConsistentCasingInFileNames": true
  }
}

// Service-specific extends base
{
  "extends": "../../tsconfig.base.json",
  "compilerOptions": {
    "outDir": "./dist",
    "paths": { "@shared/*": ["../../shared/*"] }
  }
}
```

2. **Version Management**: Use workspace-level TypeScript in root `package.json` with service-level overrides only when necessary.

3. **Tooling Consistency**: ESLint + Prettier configs at root level, shared across all services.

4. **Migration Plan**: Gradual upgrade path from 4.5 → 5.3 with breaking change documentation.

**Follow-up Questions:**
1. "How would you handle a critical TypeScript security update across all services?"
2. "What metrics would you track to measure TypeScript adoption success?"
3. "How would you enforce these standards in code review?"

**Red Flags:**
- No mention of shared configuration
- Ignoring versioning challenges
- No migration strategy

**Green Flags:**
- Mentions monorepo tools (Nx, Turborepo, Lerna)
- Discusses automated migration tools
- Considers CI/CD implications
- Mentions incremental adoption strategy

---

### Interview Question 2: TypeScript Compiler Performance

**Q:** "Your TypeScript build time has grown from 30 seconds to 5 minutes as the codebase scaled to 500k LOC. Walk me through your debugging and optimization process."

**Key Concepts Being Tested:**
- Performance profiling
- TypeScript compiler internals
- Build optimization strategies
- Trade-off analysis

**Expected Answer Should Cover:**

1. **Profiling First**
```bash
# Generate build trace
tsc --generateTrace traceDir --diagnostics

# Analyze with @typescript/analyze-trace
npx @typescript/analyze-trace traceDir
```

2. **Common Bottlenecks & Solutions:**

| Bottleneck | Impact | Solution |
|------------|--------|----------|
| Type checking `node_modules` | 1-2min | `skipLibCheck: true` |
| Deep type inference | 30-60s | Explicit return types |
| Large union types | 20-40s | Simplify types |
| Circular dependencies | 10-30s | Refactor architecture |

3. **Configuration Optimizations:**
```json
{
  "compilerOptions": {
    "incremental": true,
    "tsBuildInfoFile": "./.tsbuildinfo",
    "skipLibCheck": true,
    "isolatedModules": true
  }
}
```

4. **Architectural Solutions:**
- Project references for monorepos
- Code splitting at module boundaries
- Lazy loading type-heavy dependencies

**Follow-up Questions:**
1. "At what point would you consider project references?"
2. "How would you measure if optimizations helped?"
3. "What trade-offs does `skipLibCheck` introduce?"

**Red Flags:**
- Jumps to solutions without profiling
-
