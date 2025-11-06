# 🖥️ Building a CLI Tool - Node.js Command Line with TypeScript

[← Previous: REST API Project](./53_rest_api_project.md) | [← Back to Main](../README.md) | [Next: Frontend Frameworks →](./55_frontend_frameworks.md)

---

## 📝 Overview

Building command-line tools with TypeScript provides excellent developer experience. This project guide covers creating a production-ready CLI tool with argument parsing, commands, and TypeScript.

### 🎯 Learning Objectives

- ✅ Set up CLI project structure
- ✅ Parse arguments type-safely
- ✅ Implement commands
- ✅ Handle user input
- ✅ Publish CLI tool

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐ Intermediate/Advanced
- **Estimated Time**: 6-8 hours
- **Version**: TypeScript 5.7+, Node.js 18+ (2025)

---

## 📚 Table of Contents

1. [CLI Setup](#setup)
2. [Command Implementation](#commands)
3. [Argument Parsing](#arguments)
4. [Publishing](#publishing)

---

<a name="setup"></a>
## 1. CLI Setup

```bash
mkdir cli-tool && cd cli-tool
npm init -y
npm install commander chalk
npm install --save-dev typescript @types/node

npx tsc --init
```

```typescript
// src/index.ts
#!/usr/bin/env node
import { program } from 'commander';

program
  .name('my-cli')
  .description('CLI tool description')
  .version('1.0.0');

program
  .command('greet <name>')
  .description('Greet someone')
  .action((name: string) => {
    console.log(`Hello, ${name}!`);
  });

program.parse();
```

---

<a name="commands"></a>
## 2. Commands

```typescript
// Type-safe command handlers
interface GreetOptions {
  uppercase?: boolean;
  formal?: boolean;
}

program
  .command('greet <name>')
  .option('-u, --uppercase', 'uppercase output')
  .option('-f, --formal', 'formal greeting')
  .action((name: string, options: GreetOptions) => {
    let greeting = `Hello, ${name}`;
    
    if (options.formal) {
      greeting = `Good day, ${name}`;
    }
    
    if (options.uppercase) {
      greeting = greeting.toUpperCase();
    }
    
    console.log(greeting);
  });
```

---

<a name="publishing"></a>
## 3. Publishing

```json
// package.json
{
  "name": "my-cli-tool",
  "version": "1.0.0",
  "bin": {
    "my-cli": "./dist/index.js"
  },
  "files": ["dist"],
  "scripts": {
    "build": "tsc",
    "prepublishOnly": "npm run build"
  }
}
```

```bash
# Publish to npm
npm publish

# Or install globally for testing
npm link
my-cli --help
```

---

## 🎯 Key Takeaways

✅ **Type CLI** arguments and options

✅ **Commander.js** for argument parsing

✅ **Chalk** for colored output

✅ **bin field** makes CLI executable

✅ **Test locally** with npm link

---

[← Previous: REST API Project](./53_rest_api_project.md) | [Next: Frontend Frameworks →](./55_frontend_frameworks.md)

**Progress**: Topic 54 of 63 | Part IX: 50% Complete
