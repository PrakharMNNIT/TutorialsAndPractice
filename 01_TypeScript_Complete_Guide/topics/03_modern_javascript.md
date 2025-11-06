# 🚀 Modern JavaScript (ES6+) - Essential Features for TypeScript

[← Previous: JavaScript Types](./02_javascript_types.md) | [← Back to Main](../README.md) | [Next: JavaScript Execution Model →](./04_javascript_execution.md)

---

## 📝 Overview

Modern JavaScript (ES6/ES2015 and beyond) introduced revolutionary features that fundamentally changed how we write JavaScript. These features form the foundation of TypeScript development and are essential for writing clean, maintainable code.

**Why This Matters for TypeScript:**
- TypeScript is built on modern JavaScript
- ES6+ features are native to TypeScript
- Understanding modern JS makes TypeScript natural
- Many TypeScript patterns leverage ES6+ syntax

### 🎯 Learning Objectives

By the end of this guide, you will:

- ✅ Master let, const, and block scoping
- ✅ Use arrow functions effectively
- ✅ Apply destructuring patterns
- ✅ Work with spread and rest operators
- ✅ Use template literals and tagged templates
- ✅ Understand classes and inheritance
- ✅ Master modules (import/export)
- ✅ Use Promises and async/await
- ✅ Apply iterators and generators
- ✅ Work with Map, Set, and other collections

### 📊 Section Info

- **Difficulty**: ⭐⭐ Intermediate
- **Estimated Time**: 4-5 hours
- **Prerequisites**: [JavaScript Fundamentals](./01_javascript_fundamentals.md)
- **Practice Exercises**: 15 challenges
- **Version**: ES2024 (covering ES6-ES2024)

---

## 📚 Table of Contents

1. [let, const, and Block Scope](#let-const-scope)
2. [Arrow Functions](#arrow-functions)
3. [Template Literals](#template-literals)
4. [Destructuring](#destructuring)
5. [Spread and Rest Operators](#spread-rest)
6. [Default Parameters](#default-parameters)
7. [Enhanced Object Literals](#object-literals)
8. [Classes](#classes)
9. [Modules (Import/Export)](#modules)
10. [Promises and Async/Await](#promises-async)
11. [Iterators and Generators](#iterators-generators)
12. [Map and Set](#map-set)
13. [Symbols](#symbols)
14. [Modern Array Methods](#array-methods)
15. [Optional Chaining & Nullish Coalescing](#optional-nullish)

---

<a name="let-const-scope"></a>
## 1. let, const, and Block Scope

### 1.1 Block Scoping with let and const

```javascript
// ES5 - var (function scoped)
function oldWay() {
  var x = 1;
  if (true) {
    var x = 2; // Same variable!
    console.log(x); // 2
  }
  console.log(x); // 2 (modified)
}

// ES6+ - let (block scoped)
function modernWay() {
  let x = 1;
  if (true) {
    let x = 2; // Different variable
    console.log(x); // 2
  }
  console.log(x); // 1 (unchanged)
}

// const - immutable binding
const PI = 3.14159;
// PI = 3.14; // ❌ Error: Assignment to constant variable

// const with objects (object is mutable, binding is not)
const user = { name: 'Alice' };
user.name = 'Bob'; // ✅ OK (modifying object)
// user = {}; // ❌ Error: Assignment to constant variable
```

### 1.2 Temporal Dead Zone (TDZ)

```javascript
// var hoisting (old behavior)
console.log(x); // undefined
var x = 5;

// let/const TDZ (modern behavior)
// console.log(y); // ❌ ReferenceError: Cannot access before initialization
let y = 5;

// TDZ in blocks
{
  // TDZ starts
  // console.log(temp); // ❌ ReferenceError
  let temp = 'value'; // TDZ ends
  console.log(temp); // ✅ 'value'
}
```

### 1.3 Best Practices

```javascript
// ✅ BEST: Use const by default
const MAX_SIZE = 100;
const config = { api: 'https://api.example.com' };

// ✅ GOOD: Use let when reassignment needed
let count = 0;
let currentUser = null;

// ❌ AVOID: Don't use var in modern code
var oldStyle = 'avoid';

// ✅ BEST: Declare at top of scope
function calculate() {
  const TAX_RATE = 0.15;
  let total = 0;
  
  // Use variables
  total = 100 * (1 + TAX_RATE);
  return total;
}
```

---

<a name="arrow-functions"></a>
## 2. Arrow Functions

### 2.1 Basic Syntax

```javascript
// Traditional function
function add(a, b) {
  return a + b;
}

// Arrow function
const add = (a, b) => a + b;

// Various syntaxes
const square = x => x * x;                    // Single param, implicit return
const greet = () => 'Hello!';                 // No params
const multiply = (a, b) => a * b;             // Multiple params
const complex = (x) => {                      // Block body, explicit return
  const result = x * 2;
  return result;
};

// Returning objects (wrap in parentheses)
const makePerson = (name, age) => ({ name, age });
console.log(makePerson('Alice', 30)); // { name: 'Alice', age: 30 }
```

### 2.2 Lexical 'this' Binding

```javascript
// Traditional function - dynamic 'this'
function Counter() {
  this.count = 0;
  
  setInterval(function() {
    this.count++; // ❌ 'this' is not Counter!
    console.log(this.count); // NaN
  }, 1000);
}

// Arrow function - lexical 'this'
function Counter() {
  this.count = 0;
  
  setInterval(() => {
    this.count++; // ✅ 'this' is Counter
    console.log(this.count); // 1, 2, 3...
  }, 1000);
}

// Practical example
class Button {
  constructor(label) {
    this.label = label;
    this.clicks = 0;
  }
  
  // Arrow function preserves 'this'
  handleClick = () => {
    this.clicks++;
    console.log(`${this.label} clicked ${this.clicks} times`);
  }
}

const btn = new Button('Submit');
document.addEventListener('click', btn.handleClick); // ✅ Works!
```

### 2.3 When NOT to Use Arrow Functions

```javascript
// ❌ DON'T use as methods (when you need dynamic 'this')
const obj = {
  name: 'Object',
  greet: () => {
    console.log(`Hello, ${this.name}`); // undefined
  }
};

// ✅ DO use regular function for methods
const obj2 = {
  name: 'Object',
  greet() {
    console.log(`Hello, ${this.name}`); // 'Hello, Object'
  }
};

// ❌ DON'T use as constructors
const Person = (name) => {
  this.name = name;
};
// new Person('Alice'); // ❌ TypeError

// ❌ DON'T use when you need 'arguments'
const sum = () => {
  // console.log(arguments); // ❌ ReferenceError
};

// ✅ DO use rest parameters instead
const sum2 = (...args) => {
  return args.reduce((a, b) => a + b, 0);
};
```

---

<a name="template-literals"></a>
## 3. Template Literals

### 3.1 Basic Template Literals

```javascript
// Old way - string concatenation
const name = 'Alice';
const age = 30;
const message = 'Hello, ' + name + '! You are ' + age + ' years old.';

// Modern way - template literals
const message2 = `Hello, ${name}! You are ${age} years old.`;

// Multi-line strings
const html = `
  <div class="card">
    <h2>${name}</h2>
    <p>Age: ${age}</p>
  </div>
`;

// Expressions in templates
const price = 19.99;
const tax = 0.15;
console.log(`Total: $${(price * (1 + tax)).toFixed(2)}`);
// Output: Total: $22.99
```

### 3.2 Tagged Templates

```javascript
// Tagged template function
function highlight(strings, ...values) {
  return strings.reduce((result, str, i) => {
    return `${result}${str}<mark>${values[i] || ''}</mark>`;
  }, '');
}

const name = 'Alice';
const score = 95;
const html = highlight`Student ${name} scored ${score}%`;
// Output: "Student <mark>Alice</mark> scored <mark>95</mark>%"

// Practical example: SQL query builder
function sql(strings, ...values) {
  return {
    query: strings.join('?'),
    values: values
  };
}

const userId = 123;
const query = sql`SELECT * FROM users WHERE id = ${userId}`;
console.log(query);
// { query: 'SELECT * FROM users WHERE id = ?', values: [123] }
```

### 3.3 Raw Strings

```javascript
// Get raw string (escape sequences not processed)
const path = String.raw`C:\Users\Desktop\file.txt`;
console.log(path); // C:\Users\Desktop\file.txt (backslashes preserved)

// Without raw
const path2 = `C:\Users\Desktop\file.txt`;
console.log(path2); // C:UsersDesktopfile.txt (backslashes processed)
```

---

<a name="destructuring"></a>
## 4. Destructuring

### 4.1 Array Destructuring

```javascript
// Basic array destructuring
const numbers = [1, 2, 3, 4, 5];
const [first, second] = numbers;
console.log(first, second); // 1, 2

// Skip elements
const [a, , c] = numbers;
console.log(a, c); // 1, 3

// Rest pattern
const [head, ...tail] = numbers;
console.log(head); // 1
console.log(tail); // [2, 3, 4, 5]

// Default values
const [x = 0, y = 0, z = 0] = [1, 2];
console.log(x, y, z); // 1, 2, 0

// Swapping variables
let var1 = 1, var2 = 2;
[var1, var2] = [var2, var1];
console.log(var1, var2); // 2, 1
```

### 4.2 Object Destructuring

```javascript
// Basic object destructuring
const user = {
  name: 'Alice',
  age: 30,
  email: 'alice@example.com'
};

const { name, age } = user;
console.log(name, age); // 'Alice', 30

// Rename variables
const { name: userName, age: userAge } = user;
console.log(userName, userAge); // 'Alice', 30

// Default values
const { name, country = 'USA' } = user;
console.log(country); // 'USA'

// Nested destructuring
const person = {
  name: 'Bob',
  address: {
    city: 'NYC',
    zip: '10001'
  }
};

const { address: { city, zip } } = person;
console.log(city, zip); // 'NYC', '10001'

// Rest in objects
const { name, ...rest } = user;
console.log(rest); // { age: 30, email: 'alice@example.com' }
```

### 4.3 Function Parameter Destructuring

```javascript
// Object parameter destructuring
function greet({ name, age }) {
  console.log(`Hello ${name}, you are ${age}`);
}

greet({ name: 'Alice', age: 30 }); // Hello Alice, you are 30

// With defaults
function createUser({ name, age = 18, role = 'user' }) {
  return { name, age, role };
}

console.log(createUser({ name: 'Bob' }));
// { name: 'Bob', age: 18, role: 'user' }

// Array parameter destructuring
function sum([a, b]) {
  return a + b;
}

console.log(sum([5, 10])); // 15
```

---

<a name="spread-rest"></a>
## 5. Spread and Rest Operators

### 5.1 Spread Operator (...)

```javascript
// Array spread
const arr1 = [1, 2, 3];
const arr2 = [4, 5, 6];
const combined = [...arr1, ...arr2];
console.log(combined); // [1, 2, 3, 4, 5, 6]

// Array copying (shallow)
const original = [1, 2, 3];
const copy = [...original];
copy.push(4);
console.log(original); // [1, 2, 3] (unchanged)
console.log(copy); // [1, 2, 3, 4]

// Object spread
const obj1 = { a: 1, b: 2 };
const obj2 = { c: 3, d: 4 };
const merged = { ...obj1, ...obj2 };
console.log(merged); // { a: 1, b: 2, c: 3, d: 4 }

// Object copying and updating
const user = { name: 'Alice', age: 30 };
const updatedUser = { ...user, age: 31 };
console.log(updatedUser); // { name: 'Alice', age: 31 }

// Function arguments
const numbers = [1, 2, 3, 4, 5];
console.log(Math.max(...numbers)); // 5

// String spread
const str = 'hello';
const chars = [...str];
console.log(chars); // ['h', 'e', 'l', 'l', 'o']
```

### 5.2 Rest Operator (...)

```javascript
// Rest in function parameters
function sum(...numbers) {
  return numbers.reduce((acc, n) => acc + n, 0);
}

console.log(sum(1, 2, 3, 4)); // 10

// Rest with named parameters
function greet(greeting, ...names) {
  return `${greeting} ${names.join(' and ')}!`;
}

console.log(greet('Hello', 'Alice', 'Bob', 'Charlie'));
// "Hello Alice and Bob and Charlie!"

// Rest in destructuring
const [first, second, ...rest] = [1, 2, 3, 4, 5];
console.log(first); // 1
console.log(second); // 2
console.log(rest); // [3, 4, 5]

const { name, ...other } = { name: 'Alice', age: 30, city: 'NYC' };
console.log(other); // { age: 30, city: 'NYC' }
```

---

<a name="default-parameters"></a>
## 6. Default Parameters

### 6.1 Basic Default Parameters

```javascript
// Old way
function greet(name) {
  name = name || 'Guest';
  return `Hello, ${name}!`;
}

// Modern way
function greet2(name = 'Guest') {
  return `Hello, ${name}!`;
}

console.log(greet2()); // "Hello, Guest!"
console.log(greet2('Alice')); // "Hello, Alice!"

// Multiple defaults
function createUser(name = 'Anonymous', age = 18, role = 'user') {
  return { name, age, role };
}

console.log(createUser());
// { name: 'Anonymous', age: 18, role: 'user' }
```

### 6.2 Computed Default Values

```javascript
// Default values can be expressions
function getDefaultName() {
  return `User_${Date.now()}`;
}

function createAccount(username = getDefaultName()) {
  return { username, created: new Date() };
}

// Default from previous parameters
function calculateArea(width, height = width) {
  return width * height;
}

console.log(calculateArea(5)); // 25 (square)
console.log(calculateArea(5, 10)); // 50 (rectangle)

// Default with destructuring
function config({
  host = 'localhost',
  port = 3000,
  protocol = 'http'
} = {}) {
  return `${protocol}://${host}:${port}`;
}

console.log(config()); // "http://localhost:3000"
console.log(config({ port: 8080 })); // "http://localhost:8080"
```

---

<a name="object-literals"></a>
## 7. Enhanced Object Literals

### 7.1 Property Shorthand

```javascript
// Old way
const name = 'Alice';
const age = 30;
const user = {
  name: name,
  age: age
};

// Modern way
const user2 = { name, age };
console.log(user2); // { name: 'Alice', age: 30 }
```

### 7.2 Method Shorthand

```javascript
// Old way
const obj = {
  greet: function() {
    return 'Hello!';
  }
};

// Modern way
const obj2 = {
  greet() {
    return 'Hello!';
  },
  
  // Async method
  async fetchData() {
    const response = await fetch('/api/data');
    return response.json();
  },
  
  // Generator method
  *numbers() {
    yield 1;
    yield 2;
    yield 3;
  }
};
```

### 7.3 Computed Property Names

```javascript
// Dynamic property names
const propName = 'score';
const obj = {
  [propName]: 95,
  ['user_' + 123]: 'Alice',
  [Symbol('id')]: 'unique'
};

console.log(obj.score); // 95
console.log(obj.user_123); // 'Alice'

// With expressions
const prefix = 'user';
const id = 123;
const user = {
  [`${prefix}_${id}`]: 'Alice'
};
console.log(user.user_123); // 'Alice'
```

---

<a name="classes"></a>
## 8. Classes

### 8.1 Basic Class Syntax

```javascript
// ES6 Class
class Person {
  constructor(name, age) {
    this.name = name;
    this.age = age;
  }
  
  greet() {
    return `Hello, I'm ${this.name}`;
  }
  
  // Getter
  get info() {
    return `${this.name} is ${this.age} years old`;
  }
  
  // Setter
  set age(value) {
    if (value < 0) throw new Error('Age cannot be negative');
    this._age = value;
  }
  
  get age() {
    return this._age;
  }
  
  // Static method
  static create(name, age) {
    return new Person(name, age);
  }
}

const person = new Person('Alice', 30);
console.log(person.greet()); // "Hello, I'm Alice"
console.log(person.info); // "Alice is 30 years old"
```

### 8.2 Class Inheritance

```javascript
class Animal {
  constructor(name) {
    this.name = name;
  }
  
  speak() {
    return `${this.name} makes a sound`;
  }
}

class Dog extends Animal {
  constructor(name, breed) {
    super(name); // Call parent constructor
    this.breed = breed;
  }
  
  // Override parent method
  speak() {
    return `${this.name} barks`;
  }
  
  // New method
  fetch() {
    return `${this.name} fetches the ball`;
  }
}

const dog = new Dog('Rex', 'Labrador');
console.log(dog.speak()); // "Rex barks"
console.log(dog.fetch()); // "Rex fetches the ball"
```

### 8.3 Private Fields (ES2022)

```javascript
class BankAccount {
  #balance = 0; // Private field
  
  constructor(initialBalance) {
    this.#balance = initialBalance;
  }
  
  deposit(amount) {
    this.#balance += amount;
  }
  
  withdraw(amount) {
    if (amount > this.#balance) {
      throw new Error('Insufficient funds');
    }
    this.#balance -= amount;
  }
  
  getBalance() {
    return this.#balance;
  }
}

const account = new Bank Account(1000);
account.deposit(500);
console.log(account.getBalance()); // 1500
// console.log(account.#balance); // ❌ SyntaxError: Private field
```

---

<a name="modules"></a>
## 9. Modules (Import/Export)

### 9.1 Named Exports/Imports

```javascript
// math.js - Named exports
export const PI = 3.14159;
export const E = 2.71828;

export function add(a, b) {
  return a + b;
}

export function multiply(a, b) {
  return a * b;
}

// Alternative syntax
const subtract = (a, b) => a - b;
const divide = (a, b) => a / b;
export { subtract, divide };

// main.js - Import specific exports
import { add, multiply, PI } from './math.js';
console.log(add(5, 3)); // 8
console.log(PI); // 3.14159

// Import with alias
import { add as sum } from './math.js';
console.log(sum(5, 3)); // 8

// Import all
import * as Math from './math.js';
console.log(Math.add(5, 3)); // 8
console.log(Math.PI); // 3.14159
```

### 9.2 Default Exports/Imports

```javascript
// user.js - Default export
export default class User {
  constructor(name) {
    this.name = name;
  }
}

// Can also export expression
// export default function() { ... }
// export default { name: 'config' };

// main.js - Import default
import User from './user.js';
const user = new User('Alice');

// Mix default and named
// utils.js
export default function logger(message) {
  console.log(message);
}

export const version = '1.0.0';
export const api = 'https://api.example.com';

// main.js
import logger, { version, api } from './utils.js';
```

### 9.3 Dynamic Imports

```javascript
// Dynamic import (async)
async function loadModule() {
  const module = await import('./math.js');
  console.log(module.add(5, 3)); // 8
}

// Conditional loading
if (condition) {
  import('./heavy-module.js')
    .then(module => {
      module.init();
    })
    .catch(err => {
      console.error('Failed to load module', err);
    });
}

// Code splitting
button.addEventListener('click', async () => {
  const { animate } = await import('./animations.js');
  animate();
});
```

---

<a name="promises-async"></a>
## 10. Promises and Async/Await

### 10.1 Promises

```javascript
// Creating a promise
const promise = new Promise((resolve, reject) => {
  setTimeout(() => {
    const success = true;
    if (success) {
      resolve('Operation successful');
    } else {
      reject('Operation failed');
    }
  }, 1000);
});

// Using promises
promise
  .then(result => {
    console.log(result); // "Operation successful"
    return result.toUpperCase();
  })
  .then(upper => {
    console.log(upper); // "OPERATION SUCCESSFUL"
  })
  .catch(error => {
    console.error(error);
  })
  .finally(() => {
    console.log('Promise completed');
  });

// Promise.all - wait for all
const p1 = Promise.resolve(1);
const p2 = Promise.resolve(2);
const p3 = Promise.resolve(3);

Promise.all([p1, p2, p3])
  .then(results => {
    console.log(results); // [1, 2, 3]
  });

// Promise.race - first to complete
Promise.race([p1, p2, p3])
  .then(result => {
    console.log(result); // 1 (first to resolve)
  });
```

### 10.2 Async/Await

```javascript
// Async function returns a promise
async function fetchUser(id) {
  const response = await fetch(`/api/users/${id}`);
  const user = await response.json();
  return user;
}

// Error handling with try/catch
async function getUserData(id) {
  try {
    const user = await fetchUser(id);
    console.log(user);
    return user;
  } catch (error) {
    console.error('Failed to fetch user:', error);
    throw error;
  }
}

// Multiple async operations
async function loadData() {
  // Sequential (slow)
  const user = await fetchUser(1);
  const posts = await fetchPosts(user.id);
  
  // Parallel (fast)
  const [user2, posts2] = await Promise.all([
    fetchUser(1),
    fetchPosts(1)
  ]);
  
  return { user: user2, posts: posts2 };
}

// Top-level await (ES2022)
// Can use await at module top level
const data = await fetchUser(1);
console.log(data);
```

---

<a name="iterators-generators"></a>
## 11. Iterators and Generators

### 11.1 Iterators

```javascript
// Custom iterator
const range = {
  from: 1,
  to: 5,
  
  [Symbol.iterator]() {
    return {
      current: this.from,
      last: this.to,
      
      next() {
        if (this.current <= this.last) {
          return { done: false, value: this.current++ };
        } else {
          return { done: true };
        }
      }
    };
  }
};

for (const num of range) {
  console.log(num); // 1, 2, 3, 4, 5
}

// Convert to array
const arr = [...range]; // [1, 2, 3, 4, 5]
```

### 11.2 Generators

```javascript
// Generator function
function* numberGenerator() {
  yield 1;
  yield 2;
  yield 3;
}

const gen = numberGenerator();
console.log(gen.next()); // { value: 1, done: false }
console.log(gen.next()); // { value: 2, done: false }
console.log(gen.next()); // { value: 3, done: false }
console.log(gen.next()); // { value: undefined, done: true }

// Infinite generator
function* fibonacci() {
  let [a, b] = [0, 1];
  while (true) {
    yield a;
    [a, b] = [b, a + b];
  }
}

const fib = fibonacci();
console.log(fib.next().value); // 0
console.log(fib.next().value); // 1
console.log(fib.next().value); // 1
console.log(fib.next().value); // 2
console.log(fib.next().value); // 3

// Generator with parameters
function* range(start, end) {
  for (let i = start; i <= end; i++) {
    yield i;
  }
}

for (const num of range(1, 5)) {
  console.log(num); // 1, 2, 3, 4, 5
}
```

---

<a name="map-set"></a>
## 12. Map and Set

### 12.1 Map

```javascript
// Creating a Map
const map = new Map();

// Setting values
map.set('name', 'Alice');
map.set('age', 30);
map.set(1, 'number key');
map.set(true, 'boolean key');

// Getting values
console.log(map.get('name')); // 'Alice'
console.log(map.get(1)); // 'number key'

// Map methods
console.log(map.has('name')); // true
console.log(map.size); // 4
map.delete('age');
console.log(map.size); // 3

// Iterating over Map
for (const [key, value] of map) {
  console.log(`${key} = ${value}`);
}

// Map from array
const map2 = new Map([
  ['key1', 'value1'],
  ['key2', 'value2']
]);

// Object vs Map
const obj = { name: 'Alice' };
const map3 = new Map();
map3.set(obj, 'metadata'); // Object as key!
console.log(map3.get(obj)); // 'metadata'
```

### 12.2 Set

```javascript
// Creating a Set
const set = new Set();

// Adding values
set.add(1);
set.add(2);
set.add(3);
set.add(2); // Duplicate ignored

console.log(set.size); // 3

// Set from array (removes duplicates)
const arr = [1, 2, 2, 3, 3, 3, 4];
const uniqueSet = new Set(arr);
console.log([...uniqueSet]); // [1, 2, 3, 4]

// Set methods
console.log(set.has(2)); // true
set.delete(2);
console.log(set.has(2)); // false

// Iterating over Set
for (const value of set) {
  console.log(value);
}

// Set operations
const setA = new Set([1, 2, 3]);
const setB = new Set([2, 3, 4]);

// Union
const union = new Set([...setA, ...setB]);
console.log([...union]); // [1, 2, 3, 4]

// Intersection
const intersection = new Set(
  [...setA].filter(x => setB
