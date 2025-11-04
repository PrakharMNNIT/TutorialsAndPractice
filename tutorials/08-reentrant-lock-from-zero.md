# 🔐 ReentrantLock: From Ground Zero to Master

> **The easiest, most comprehensive guide to understanding ReentrantLock - what it is, why you need it, and how it works internally**

---

## 📑 Table of Contents

1. [The Problem: Why synchronized Isn't Enough](#1-the-problem-why-synchronized-isnt-enough)
2. [What is ReentrantLock?](#2-what-is-reentrantlock)
3. [Understanding "Reentrant"](#3-understanding-reentrant)
4. [Basic Usage - Your First Lock](#4-basic-usage---your-first-lock)
5. [How ReentrantLock Works Internally](#5-how-reentrantlock-works-internally)
6. [All Methods Explained Simply](#6-all-methods-explained-simply)
7. [Advanced Features](#7-advanced-features)
8. [synchronized vs ReentrantLock](#8-synchronized-vs-reentrantlock)
9. [Common Patterns](#9-common-patterns)
10. [Real-World Examples](#10-real-world-examples)
11. [FAQs - Your Burning Questions](#11-faqs---your-burning-questions)
12. [Best Practices](#12-best-practices)

---

## 1. The Problem: Why synchronized Isn't Enough

### 🎯 Let's Start with What You Know: synchronized

You probably know about `synchronized`:

```java
public class BankAccount {

  private int balance = 1000;

  // synchronized method
  public synchronized void withdraw(int amount) {

    if (balance >= amount) {
      balance -= amount;
    }
  }
}
```

**This works! But has limitations...**

### ❌ Problem 1: Can't Try to Acquire Lock

```java
// With synchronized - you're STUCK waiting
public synchronized void transfer(BankAccount other, int amount) {
  // If 'other' is locked by another thread, you WAIT FOREVER
  synchronized (other) {
    // Can't give up or try later
    // Just blocked!
  }
}
```

**What you want:** "Try to get the lock, if it's busy, I'll do something else!"

### ❌ Problem 2: Can't Be Interrupted While Waiting

```java
public synchronized void doLongOperation() {
  // If thread is waiting for this lock,
  // it CAN'T be interrupted!
  // Must wait until lock is available
}
```

**What you want:** "Let me cancel this operation if it takes too long!"

### ❌ Problem 3: Can't Check Lock Status

```java
// You can't ask: "Is this lock free?"
// You just try to enter and get blocked if it's not
synchronized (obj){
  // No way to know if you had to wait or not
  }
```

**What you want:** "Let me check if the lock is free before trying!"

### ❌ Problem 4: No Fairness Control

```java
// With synchronized, threads compete randomly
// A thread waiting longest might NEVER get the lock!
// (This is called "starvation")
```

**What you want:** "First come, first served - be fair!"

### 🎯 The Solution: ReentrantLock

ReentrantLock solves ALL these problems:

```java
ReentrantLock lock = new ReentrantLock();

// ✅ Try to acquire with timeout
if(lock.

tryLock(5,TimeUnit.SECONDS)){
  // Got it!
  }else{
  // Nope, do something else
  }

  // ✅ Can be interrupted
  lock.

lockInterruptibly();  // Throws if interrupted

// ✅ Check status
boolean isFree = lock.tryLock();

// ✅ Fair locks
ReentrantLock fairLock = new ReentrantLock(true);  // FIFO queue
```

**Let's learn how to use this powerful tool!**

---

## 2. What is ReentrantLock?

### 📚 Simple Definition

**ReentrantLock** is a lock that:

1. **Only one thread can hold at a time** (like synchronized)
2. **Must be explicitly unlocked** (unlike synchronized)
3. **Can be tried and timed** (unlike synchronized)
4. **Can be made fair** (unlike synchronized)
5. **Can be interrupted** (unlike synchronized)

### 🎯 Think of It Like a Room Key

```
Regular Lock (synchronized):
┌──────────────────────────────┐
│  Room with automatic door    │
│  - Enter: Door locks behind  │
│  - Exit: Door unlocks auto   │
│  - If locked: Wait forever   │
└──────────────────────────────┘

ReentrantLock:
┌──────────────────────────────┐
│  Room with manual key         │
│  - lock(): Lock door with key │
│  - unlock(): Unlock with key  │
│  - tryLock(): Try key, might  │
│    not work if busy           │
│  - Must remember to unlock!   │
└──────────────────────────────┘
```

### 💻 Simplest Possible Example

```java
import java.util.concurrent.locks.ReentrantLock;

public class SimplestLockExample {

  private final ReentrantLock lock = new ReentrantLock();
  private int counter = 0;

  public void increment() {

    lock.lock();  // 🔐 Lock the door
    try {
      counter++;  // Safe inside!
    } finally {
      lock.unlock();  // 🔓 MUST unlock!
    }
  }

  public static void main(String[] args) throws InterruptedException {

    SimplestLockExample example = new SimplestLockExample();

    // Create 10 threads
    Thread[] threads = new Thread[10];
    for (int i = 0; i < 10; i++) {
      threads[i] = new Thread(() -> {
        for (int j = 0; j < 1000; j++) {
          example.increment();
        }
      });
      threads[i].start();
    }

    // Wait for all
    for (Thread thread : threads) {
      thread.join();
    }

    System.out.println("Counter: " + example.counter);  // 10000 ✅
  }
}
```

**Key Points:**

- ✅ `lock.lock()` - Acquire the lock
- ✅ `finally { lock.unlock() }` - ALWAYS unlock in finally!
- ✅ Thread-safe, just like synchronized

---

## 3. Understanding "Reentrant"

### 🤔 What Does "Reentrant" Mean?

**Reentrant = A thread can acquire the SAME lock multiple times**

Think of it like a keycard to your apartment:

```
Non-Reentrant Lock:
👤 Person enters apartment → Door locks
👤 Person tries to open bedroom → Door locked by same person!
❌ STUCK! (Deadlock with yourself!)

Reentrant Lock:
👤 Person enters apartment → Door locks
👤 Person tries to open bedroom → Same keycard works!
✅ Can enter multiple rooms with same key
```

### 💻 Code Example: Why Reentrant Matters

```java
public class ReentrantExample {

  private final ReentrantLock lock = new ReentrantLock();

  public void outerMethod() {

    lock.lock();
    try {
      System.out.println("In outer method");
      innerMethod();  // Calls another method that needs same lock
    } finally {
      lock.unlock();
    }
  }

  public void innerMethod() {

    lock.lock();  // ✅ Same thread can acquire again!
    try {
      System.out.println("In inner method");
    } finally {
      lock.unlock();
    }
  }

  public static void main(String[] args) {

    ReentrantExample example = new ReentrantExample();
    example.outerMethod();

    // Output:
    // In outer method
    // In inner method
    // ✅ Works! Same thread acquired lock twice
  }
}
```

### 📊 What Happens Behind the Scenes

```
Thread T1 calls outerMethod():
┌──────────────────────────────────┐
│ lock.lock()                      │
│   Hold count: 0 → 1              │ ✅ Acquired
│   Owner: T1                       │
│                                   │
│ outerMethod() running...          │
│   ↓                              │
│ Calls innerMethod():             │
│   lock.lock()                    │
│   Check: Is owner T1? YES        │
│   Hold count: 1 → 2              │ ✅ Acquired again
│                                   │
│   innerMethod() running...        │
│   lock.unlock()                  │
│   Hold count: 2 → 1              │ ✅ Released once
│                                   │
│ Back to outerMethod()            │
│ lock.unlock()                    │
│ Hold count: 1 → 0                │ ✅ Fully released
│ Owner: null                       │
└──────────────────────────────────┘
```

### 🎯 Key Insight

**Lock has a "hold count":**

- Lock once: count = 1
- Lock again: count = 2
- Unlock once: count = 1
- Unlock again: count = 0 (fully released)

**Rule:** Number of `unlock()` calls must equal number of `lock()` calls!

---

## 4. Basic Usage - Your First Lock

### 🎯 The Golden Pattern

**This is THE most important pattern - memorize it:**

```java
ReentrantLock lock = new ReentrantLock();

lock.

lock();              // ← Always lock first
try{
  // Your critical section
  // Safe code here
  }finally{
  lock.

unlock();        // ← ALWAYS unlock in finally!
}
```

### ⚠️ Why `finally` is CRITICAL

**Without finally (WRONG):**

```java
lock.lock();

// What if exception here? 💥
somethingThatMightThrow();
lock.

unlock();  // ← Never reached if exception!
// Lock never released = DEADLOCK! 💀
```

**With finally (CORRECT):**

```java
lock.lock();
try{

somethingThatMightThrow();  // Exception? No problem!
}finally{
  lock.

unlock();  // ← ALWAYS runs, even with exception ✅
}
```

### 💻 Complete Beginner Example

```java
import java.util.concurrent.locks.ReentrantLock;

public class BankAccountWithLock {

  private final ReentrantLock lock = new ReentrantLock();
  private int balance = 1000;

  public void withdraw(int amount) {

    lock.lock();  // 🔐 Acquire lock
    try {
      if (balance >= amount) {
        System.out.println(Thread.currentThread().getName() +
          " withdrawing " + amount);

        // Simulate processing time
        Thread.sleep(100);

        balance -= amount;
        System.out.println(Thread.currentThread().getName() +
          " completed. Balance: " + balance);
      } else {
        System.out.println(Thread.currentThread().getName() +
          " insufficient funds");
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();  // 🔓 ALWAYS unlock
    }
  }

  public int getBalance() {

    lock.lock();
    try {
      return balance;
    } finally {
      lock.unlock();
    }
  }

  public static void main(String[] args) throws InterruptedException {

    BankAccountWithLock account = new BankAccountWithLock();

    // Create 3 threads trying to withdraw
    Thread t1 = new Thread(() -> account.withdraw(400), "Thread-1");
    Thread t2 = new Thread(() -> account.withdraw(500), "Thread-2");
    Thread t3 = new Thread(() -> account.withdraw(600), "Thread-3");

    t1.start();
    t2.start();
    t3.start();

    t1.join();
    t2.join();
    t3.join();

    System.out.println("\nFinal balance: " + account.getBalance());
  }
}
```

**Output:**

```
Thread-1 withdrawing 400
Thread-1 completed. Balance: 600
Thread-2 withdrawing 500
Thread-2 completed. Balance: 100
Thread-3 insufficient funds

Final balance: 100
```

**✅ Thread-safe! Only one thread at a time.**

---

## 5. How ReentrantLock Works Internally

### 🔬 Inside the Lock

Let's peek inside to understand HOW it works:

**Three Main Components:**

```
ReentrantLock
├── 1. State (AtomicInteger)
│   └── 0 = unlocked
│       1+ = locked (hold count)
│
├── 2. Owner Thread
│   └── Which thread holds the lock?
│
└── 3. Wait Queue (AQS)
    └── Threads waiting for lock
```

### 📊 Visual Representation

```mermaid
graph TB
  subgraph "ReentrantLock"
    State[State: 0 unlocked<br/>1+ locked]
    Owner[Owner: null or Thread]
    Queue[Wait Queue: Blocked threads]
  end

  T1[Thread 1] -->|lock| State
  T2[Thread 2] -->|lock| Queue
  T3[Thread 3] -->|lock| Queue
  State -->|if 0| Owner
  Owner -->|unlock| State
  State -->|becomes 0| Queue
  Queue -->|wake next| T2
  
  
  
```

### 🎭 Step-by-Step: What Happens When You Lock

**Thread 1 calls `lock.lock()`:**

```
Step 1: Check state
  Current: 0 (unlocked)
  ↓
Step 2: Try to change state 0 → 1 (CAS operation)
  Success! ✅
  ↓
Step 3: Set owner = Thread 1
  ↓
Step 4: Return (Thread 1 now owns lock)
```

**Thread 2 calls `lock.lock()` (while Thread 1 still has it):**

```
Step 1: Check state
  Current: 1 (locked)
  Owner: Thread 1
  ↓
Step 2: Try to change state (CAS fails)
  ❌ Someone owns it!
  ↓
Step 3: Am I the owner? (Thread 2 checks)
  No, owner is Thread 1
  ↓
Step 4: Add Thread 2 to wait queue
  Queue: [Thread 2]
  ↓
Step 5: Park Thread 2 (sleep/wait)
  Thread 2 is now BLOCKED
```

**Thread 1 calls `lock.unlock()`:**

```
Step 1: Check owner
  Owner: Thread 1 ✅
  ↓
Step 2: Decrement hold count
  Count: 1 → 0
  ↓
Step 3: Set owner = null
  ↓
Step 4: Check wait queue
  Queue: [Thread 2]
  ↓
Step 5: Unpark Thread 2 (wake it up)
  Thread 2: BLOCKED → RUNNABLE
  ↓
Step 6: Thread 2 tries to acquire lock
  State: 0 → 1 ✅
  Owner: Thread 2
```

### 💡 Key Insights

1. **Uses CAS (Compare-And-Swap)** for state changes
2. **Uses AQS (AbstractQueuedSynchronizer)** for wait queue
3. **Uses LockSupport.park/unpark** to block/wake threads
4. **Tracks hold count** for reentrancy
5. **No actual OS locks** - pure Java implementation!

---

## 6. All Methods Explained Simply

### 📚 Method 1: `lock()` - Basic Acquire

**What it does:** Acquires the lock, waits if necessary.

```java
lock.lock();
```

**Simple Explanation:**

- If lock is free → Grab it ✅
- If lock is held by another thread → Wait ⏳
- If YOU already hold it → Increment count ✅ (reentrant!)

**Example:**

```java
ReentrantLock lock = new ReentrantLock();

lock.

lock();
try{
  // Do work
  }finally{
  lock.

unlock();
}
```

**When to use:** Default choice for most scenarios.

---

### 📚 Method 2: `unlock()` - Release

**What it does:** Releases the lock.

```java
lock.unlock();
```

**Simple Explanation:**

- Decrements hold count
- If count reaches 0 → Fully released
- Wake up next waiting thread

**⚠️ CRITICAL RULES:**

1. Must be called in `finally` block
2. Only the owner can unlock
3. Must call same number of times as `lock()`

**Example:**

```java
lock.lock();
lock.

lock();  // Hold count: 2
try{
  // Do work
  }finally{
  lock.

unlock();  // Hold count: 1
    lock.

unlock();  // Hold count: 0 (released)
}
```

---

### 📚 Method 3: `tryLock()` - Try Without Waiting

**What it does:** Tries to acquire lock WITHOUT waiting.

```java
boolean success = lock.tryLock();
if(success){
  try{
  // Got the lock!
  }finally{
  lock.

unlock();
    }
      }else{
      // Didn't get lock, do something else
      }
```

**Simple Explanation:**

- If lock is free → Grab it, return `true` ✅
- If lock is held → Return `false` immediately ❌
- **Does NOT wait!**

**Real-World Example:**

```java
public class NonBlockingUpdate {

  private final ReentrantLock lock = new ReentrantLock();
  private int value = 0;

  public boolean tryUpdate(int newValue) {

    if (lock.tryLock()) {  // Try to get lock
      try {
        value = newValue;
        return true;  // Success!
      } finally {
        lock.unlock();
      }
    } else {
      System.out.println("Lock busy, skipping update");
      return false;  // Couldn't update
    }
  }
}
```

---

### 📚 Method 4: `tryLock(timeout)` - Try With Timeout

**What it does:** Tries to acquire lock, waits UP TO specified time.

```java
if(lock.tryLock(5,TimeUnit.SECONDS)){
  try{
  // Got the lock within 5 seconds!
  }finally{
  lock.

unlock();
    }
      }else{
      // Timeout - didn't get lock in 5 seconds
      }
```

**Simple Explanation:**

- If lock is free → Grab it immediately ✅
- If lock is held → Wait up to timeout ⏳
- If timeout expires → Return `false` ❌

**Real-World Example:**

```java
public class DatabaseConnection {

  private final ReentrantLock lock = new ReentrantLock();

  public boolean executeQuery(String query) {

    try {
      // Try to get lock, wait max 10 seconds
      if (lock.tryLock(10, TimeUnit.SECONDS)) {
        try {
          // Execute query
          System.out.println("Executing: " + query);
          Thread.sleep(2000);  // Simulate query time
          return true;
        } finally {
          lock.unlock();
        }
      } else {
        System.out.println("Query timeout after 10 seconds");
        return false;
      }
    } catch (InterruptedException e) {
      System.out.println("Interrupted while waiting");
      return false;
    }
  }
}
```

---

### 📚 Method 5: `lockInterruptibly()` - Acquire But Allow Interruption

**What it does:** Acquires lock, but can be interrupted while waiting.

```java
try{
  lock.lockInterruptibly();
    try{
      // Do work
      }finally{
      lock.

unlock();
    }
      }catch(
InterruptedException e){
  // Thread was interrupted while waiting for lock
  System.out.

println("Interrupted!");
}
```

**Simple Explanation:**

- Like `lock()`, but if interrupted → throws `InterruptedException`
- Allows threads to be cancelled while waiting

**Real-World Example:**

```java
public class CancellableTask {

  private final ReentrantLock lock = new ReentrantLock();

  public void doWork() {

    try {
      lock.lockInterruptibly();  // Can be interrupted
      try {
        // Long operation
        for (int i = 0; i < 1000000; i++) {
          // Work...
          if (Thread.interrupted()) {
            throw new InterruptedException();
          }
        }
      } finally {
        lock.unlock();
      }
    } catch (InterruptedException e) {
      System.out.println("Task cancelled!");
    }
  }
}

// Usage:
Thread task = new Thread(() -> cancellable.doWork());
task.

start();
// Later: cancel it
task.

interrupt();  // Will interrupt if waiting for lock
```

---

### 📚 Method 6: `isLocked()` - Check Status

**What it does:** Checks if lock is currently held by ANY thread.

```java
boolean locked = lock.isLocked();
```

**Example:**

```java
ReentrantLock lock = new ReentrantLock();

System.out.

println("Locked? "+lock.isLocked());  // false

  lock.

lock();
System.out.

println("Locked? "+lock.isLocked());  // true

  lock.

unlock();
System.out.

println("Locked? "+lock.isLocked());  // false
```

**Use case:** Debugging, monitoring.

---

### 📚 Method 7: `isHeldByCurrentThread()` - Check Ownership

**What it does:** Checks if CURRENT thread holds the lock.

```java
boolean iHoldIt = lock.isHeldByCurrentThread();
```

**Example:**

```java
ReentrantLock lock = new ReentrantLock();

// Thread 1
new

Thread(() ->{
  lock.

lock();
    try{
      System.out.

println("T1 holds lock? "+
  lock.isHeldByCurrentThread());  // true
  }finally{
  lock.

unlock();
    }
      }).

start();

// Main thread
System.out.

println("Main holds lock? "+
  lock.isHeldByCurrentThread());  // false
```

**Use case:** Assertions, conditional logic.

---

### 📚 Method 8: `getHoldCount()` - Check Reentrant Count

**What it does:** Returns how many times current thread has locked.

```java
int count = lock.getHoldCount();
```

**Example:**

```java
ReentrantLock lock = new ReentrantLock();

System.out.

println("Hold count: "+lock.getHoldCount());  // 0

  lock.

lock();
System.out.

println("Hold count: "+lock.getHoldCount());  // 1

  lock.

lock();  // Lock again!
System.out.

println("Hold count: "+lock.getHoldCount());  // 2

  lock.

unlock();
System.out.

println("Hold count: "+lock.getHoldCount());  // 1

  lock.

unlock();
System.out.

println("Hold count: "+lock.getHoldCount());  // 0
```

**Use case:** Debugging reentrant behavior.

---

### 📚 Method 9: `getQueueLength()` - Check Waiting Threads

**What it does:** Returns estimate of threads waiting for lock.

```java
int waiting = lock.getQueueLength();
```

**Example:**

```java
ReentrantLock lock = new ReentrantLock();

// Thread 1 holds lock
new

Thread(() ->{
  lock.

lock();
    try{
      Thread.

sleep(5000);  // Hold for 5 seconds
    }catch(
InterruptedException e){
  e.

printStackTrace();
    }finally{
      lock.

unlock();
    }
      }).

start();

Thread.

sleep(100);  // Let thread 1 acquire lock

// Start 3 threads that will wait
for(
int i = 0;
i< 3;i++){
  new

Thread(() ->{
  lock.

lock();
        try{
          // Work
          }finally{
          lock.

unlock();
        }
          }).

start();
}

  Thread.

sleep(500);
System.out.

println("Threads waiting: "+lock.getQueueLength());  // ~3
```

**Use case:** Monitoring, diagnostics.

---

### 📚 Method 10: `hasQueuedThreads()` - Any Waiters?

**What it does:** Checks if any threads are waiting for lock.

```java
boolean hasWaiters = lock.hasQueuedThreads();
```

**Use case:** Quick check if lock is contended.

---

## 7. Advanced Features

### 🎯 Feature 1: Fair vs Unfair Locks

**Unfair Lock (Default):**

```java
ReentrantLock unfairLock = new ReentrantLock();
// or
ReentrantLock unfairLock = new ReentrantLock(false);
```

**Behavior:**

- Threads compete randomly
- A newly arriving thread might jump the queue
- **Faster** but can cause starvation

**Fair Lock:**

```java
ReentrantLock fairLock = new ReentrantLock(true);
```

**Behavior:**

- First-come, first-served (FIFO)
- Threads wait in order
- **Slower** but guarantees fairness

**Visual Comparison:**

```
Unfair Lock:
  Lock available
      ↓
  [T1] [T2] [T3] ← Waiting in queue
      ↓
  T4 arrives ← Can jump ahead! ⚡
      ↓
  T4 gets lock (even though others waiting)

Fair Lock:
  Lock available
      ↓
  [T1] [T2] [T3] ← Waiting in queue (FIFO)
      ↓
  T4 arrives ← Must wait at end
      ↓
  T1 gets lock (first in line)
```

**Example:**

```java
public class FairLockDemo {

  public static void main(String[] args) {

    System.out.println("=== Unfair Lock ===");
    testLock(new ReentrantLock(false));

    System.out.println("\n=== Fair Lock ===");
    testLock(new ReentrantLock(true));
  }

  private static void testLock(ReentrantLock lock) {

    for (int i = 1; i <= 5; i++) {
      final int num = i;
      new Thread(() -> {
        lock.lock();
        try {
          System.out.println("Thread-" + num + " acquired lock");
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          lock.unlock();
        }
      }, "Thread-" + i).start();
    }
  }
}
```

---

### 🎯 Feature 2: Conditions - More Powerful wait/notify

We'll cover this in detail in Section 7 of the main guide.

Quick preview:

```java
ReentrantLock lock = new ReentrantLock();
Condition condition = lock.newCondition();

// Producer
lock.

lock();
try{
  // produce item
  condition.

signal();  // Wake up consumer
}finally{
  lock.

unlock();
}

  // Consumer
  lock.

lock();
try{
  while(noItemsAvailable){
  condition.

await();  // Wait for items
    }
      // consume item
      }finally{
      lock.

unlock();
}
```

---

## 8. synchronized vs ReentrantLock

### 📊 Complete Comparison

| Feature                 | synchronized | ReentrantLock        |
|-------------------------|--------------|----------------------|
| **Syntax**              | Simple       | Verbose              |
| **Auto unlock**         | ✅ Yes        | ❌ No (manual)        |
| **Try lock**            | ❌ No         | ✅ Yes                |
| **Timeout**             | ❌ No         | ✅ Yes                |
| **Interruptible**       | ❌ No         | ✅ Yes                |
| **Fair mode**           | ❌ No         | ✅ Yes (optional)     |
| **Multiple conditions** | ❌ No         | ✅ Yes                |
| **Check status**        | ❌ No         | ✅ Yes                |
| **Performance**         | Good         | Slightly better      |
| **Risk**                | Low          | High (forget unlock) |

### 💻 Side-by-Side Examples

**Task: Thread-safe counter**

**With synchronized:**

```java
public class SyncCounter {

  private int count = 0;

  public synchronized void increment() {

    count++;
  }

  public synchronized int getCount() {

    return count;
  }
}
```

**With ReentrantLock:**

```java
public class LockCounter {

  private final ReentrantLock lock = new ReentrantLock();
  private int count = 0;

  public void increment() {

    lock.lock();
    try {
      count++;
    } finally {
      lock.unlock();
    }
  }

  public int getCount() {

    lock.lock();
    try {
      return count;
    } finally {
      lock.unlock();
    }
  }
}
```

### 🎯 When to Use Which?

**Use synchronized when:**

- ✅ Simple locking needs
- ✅ Don't need timeout or try-lock
- ✅ Want automatic unlock
- ✅ Prefer simplicity

**Use ReentrantLock when:**

- ✅ Need try-lock functionality
- ✅ Need timeout on lock acquisition
- ✅ Need to interrupt waiting threads
- ✅ Need fair locking
- ✅ Need multiple condition variables
- ✅ Need advanced monitoring

---

## 9. Common Patterns

### 🎯 Pattern 1: Try-Lock with Fallback

```java
public class TryLockPattern {

  private final ReentrantLock lock = new ReentrantLock();

  public void performAction() {

    if (lock.tryLock()) {
      try {
        // Primary action
        System.out.println("Doing main work");

      } finally {
        lock.unlock();
      }
    } else {
      // Fallback action
      System.out.println("Lock busy, doing fallback work");
    }
  }
}
```

---

### 🎯 Pattern 2: Timed Lock with Retry

```java
public class RetryLockPattern {

  private final ReentrantLock lock = new ReentrantLock();

  public boolean performWithRetry(int maxAttempts) {

    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
      try {
        if (lock.tryLock(1, TimeUnit.SECONDS)) {
          try {
            System.out.println("Acquired lock on attempt " + attempt);
            doWork();
            return true;
          } finally {
            lock.unlock();
          }
        }
        System.out.println("Attempt " + attempt + " failed, retrying...");
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return false;
      }
    }
    System.out.println("Failed after " + maxAttempts + " attempts");
    return false;
  }

  private void doWork() {

    System.out.println("Performing work");
  }
}
```

---

### 🎯 Pattern 3: Deadlock-Free Multi-Lock

```java
public class DeadlockFreeLocking {

  public boolean transfer(Account from, Account to, int amount) {
    // Always acquire locks in consistent order (by ID)
    Account first = from.getId() < to.getId() ? from : to;
    Account second = from.getId() < to.getId() ? to : from;

    // Try first lock
    if (first.getLock().tryLock()) {
      try {
        // Try second lock
        if (second.getLock().tryLock()) {
          try {
            // Both locks acquired!
            if (from.getBalance() >= amount) {
              from.debit(amount);
              to.credit(amount);
              return true;
            }
            return false;
          } finally {
            second.getLock().unlock();
          }
        }
      } finally {
        first.getLock().unlock();
      }
    }
    return false;  // Couldn't acquire both locks
  }
}

class Account {

  private final int id;
  private int balance;
  private final ReentrantLock lock = new ReentrantLock();

  public Account(int id, int balance) {

    this.id = id;
    this.balance = balance;
  }

  public int getId() {

    return id;
  }

  public int getBalance() {

    return balance;
  }

  public ReentrantLock getLock() {

    return lock;
  }

  public void debit(int amount) {

    balance -= amount;
  }

  public void credit(int amount) {

    balance += amount;
  }
}
```

---

### 🎯 Pattern 4: Lock with Timeout Handler

```java
public class TimeoutHandler {

  private final ReentrantLock lock = new ReentrantLock();

  public void performOperation() {

    try {
      if (lock.tryLock(5, TimeUnit.SECONDS)) {
        try {
          criticalSection();
        } finally {
          lock.unlock();
        }
      } else {
        handleTimeout();
      }
    } catch (InterruptedException e) {
      handleInterruption();
    }
  }

  private void criticalSection() {

    System.out.println("Executing critical section");
  }

  private void handleTimeout() {

    System.out.println("Lock acquisition timeout - trying alternative approach");
  }

  private void handleInterruption() {

    System.out.println("Interrupted while waiting for lock");
    Thread.currentThread().interrupt();  // Restore interrupt status
  }
}
```

---

## 10. Real-World Examples

### 🏦 Example 1: Thread-Safe Bank Account

```java
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

public class SafeBankAccount {

  private final ReentrantLock lock = new ReentrantLock();
  private double balance;

  public SafeBankAccount(double initialBalance) {

    this.balance = initialBalance;
  }

  public boolean deposit(double amount) {

    lock.lock();
    try {
      if (amount > 0) {
        balance += amount;
        System.out.println(Thread.currentThread().getName() +
          " deposited " + amount + ", balance: " + balance);
        return true;
      }
      return false;
    } finally {
      lock.unlock();
    }
  }

  public boolean withdraw(double amount) {

    lock.lock();
    try {
      if (amount > 0 && balance >= amount) {
        balance -= amount;
        System.out.println(Thread.currentThread().getName() +
          " withdrew " + amount + ", balance: " + balance);
        return true;
      }
      System.out.println(Thread.currentThread().getName() +
        " insufficient funds for " + amount);
      return false;
    } finally {
      lock.unlock();
    }
  }

  public double getBalance() {

    lock.lock();
    try {
      return balance;
    } finally {
      lock.unlock();
    }
  }

  public static void main(String[] args) throws InterruptedException {

    SafeBankAccount account = new SafeBankAccount(1000);

    // Multiple threads accessing account
    Thread depositor = new Thread(() -> {
      for (int i = 0; i < 3; i++) {
        account.deposit(100);
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }, "Depositor");

    Thread withdrawer = new Thread(() -> {
      for (int i = 0; i < 5; i++) {
        account.withdraw(200);
        try {
          Thread.sleep(150);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }, "Withdrawer");

    depositor.start();
    withdrawer.start();

    depositor.join();
    withdrawer.join();

    System.out.println("\nFinal balance: " + account.getBalance());
  }
}
```

---

### 📁 Example 2: Resource Manager with Timeout

```java
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

public class ResourceManager {

  private final ReentrantLock lock = new ReentrantLock();
  private boolean resourceAvailable = true;

  public boolean acquireResource(long timeout, TimeUnit unit) {

    try {
      // Try to get lock with timeout
      if (lock.tryLock(timeout, unit)) {
        try {
          if (resourceAvailable) {
            resourceAvailable = false;
            System.out.println(Thread.currentThread().getName() +
              " acquired resource");
            return true;
          } else {
            System.out.println(Thread.currentThread().getName() +
              " resource not available");
            return false;
          }
        } finally {
          lock.unlock();
        }
      } else {
        System.out.println(Thread.currentThread().getName() +
          " timeout waiting for lock");
        return false;
      }
    } catch (InterruptedException e) {
      System.out.println(Thread.currentThread().getName() + " interrupted");
      Thread.currentThread().interrupt();
      return false;
    }
  }

  public void releaseResource() {

    lock.lock();
    try {
      resourceAvailable = true;
      System.out.println(Thread.currentThread().getName() +
        " released resource");
    } finally {
      lock.unlock();
    }
  }

  public static void main(String[] args) throws InterruptedException {

    ResourceManager manager = new ResourceManager();

    // Thread 1: Acquires and holds
    Thread t1 = new Thread(() -> {
      if (manager.acquireResource(1, TimeUnit.SECONDS)) {
        try {
          Thread.sleep(3000);  // Hold for 3 seconds
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          manager.releaseResource();
        }
      }
    }, "Thread-1");

    // Thread 2: Tries with timeout
    Thread t2 = new Thread(() -> {
      try {
        Thread.sleep(500);  // Start after T1
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      manager.acquireResource(2, TimeUnit.SECONDS);  // Will timeout!
    }, "Thread-2");

    t1.start();
    t2.start();

    t1.join();
    t2.join();
  }
}
```

**Output:**

```
Thread-1 acquired resource
Thread-2 timeout waiting for lock
Thread-1 released resource
```

---

## 11. FAQs - Your Burning Questions

### ❓ Q1: What if I forget to call unlock()?

**Short Answer:** **DEADLOCK!** The lock is never released, all other threads waiting for it will block forever.

**Example:**

```java
public class ForgotToUnlock {

  private final ReentrantLock lock = new ReentrantLock();

  public void buggyMethod() {

    lock.lock();
    // Do something
    if (someCondition) {
      return;  // ❌ FORGOT TO UNLOCK! 💀
    }
    lock.unlock();  // Only unlocks if someCondition is false
  }
}
```

**What happens:**

1. Thread 1 calls `buggyMethod()`
2. If condition is true, returns without unlocking
3. Lock remains acquired by Thread 1 (even though Thread 1 is done!)
4. Thread 2 tries to acquire → BLOCKED FOREVER
5. Application hangs 💀

**Solution:**

```java
public void correctMethod() {

  lock.lock();
  try {
    // Do something
    if (someCondition) {
      return;  // ✅ Finally block will unlock!
    }
  } finally {
    lock.unlock();  // ✅ ALWAYS runs
  }
}
```

**Visual:**

```mermaid
graph TD
  A[lock.lock] --> B[try block]
  B --> C{Exception?}
  C -->|Yes| D[finally: unlock]
  C -->|No| E{return?}
  E -->|Yes| D
  E -->|No| F[Continue]
  F --> D
  D --> G[Lock released ✅]



```

**Key Takeaway:** **ALWAYS use try-finally!** No exceptions!

---

### ❓ Q2: Can another thread unlock my lock?

**Short Answer:** Technically yes (you can call it), but you'll get `IllegalMonitorStateException`. Only the owner should unlock.

**Example:**

```java
public class WrongThreadUnlock {

  private static final ReentrantLock lock = new ReentrantLock();

  public static void main(String[] args) throws InterruptedException {
    // Thread 1 acquires lock
    Thread t1 = new Thread(() -> {
      lock.lock();
      try {
        System.out.println("T1: Acquired lock");
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
        System.out.println("T1: Released lock");
      }
    });

    // Thread 2 tries to unlock T1's lock
    Thread t2 = new Thread(() -> {
      try {
        Thread.sleep(500);
        System.out.println("T2: Trying to unlock T1's lock");
        lock.unlock();  // ❌ Will throw exception!
      } catch (IllegalMonitorStateException e) {
        System.out.println("T2: ERROR - " + e.getClass().getSimpleName());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });

    t1.start();
    t2.start();

    t1.join();
    t2.join();
  }
}
```

**Output:**

```
T1: Acquired lock
T2: Trying to unlock T1's lock
T2: ERROR - IllegalMonitorStateException
T1: Released lock
```

**Key Takeaway:** Each thread is responsible for its own locks. Don't try to unlock other threads' locks!

---

### ❓ Q3: When should I use ReentrantLock instead of synchronized?

**Short Answer:** Use ReentrantLock when you need advanced features like tryLock, timeouts, or interruptibility. Otherwise, synchronized is simpler.

**Decision Tree:**

```mermaid
graph TD
  A{Need advanced features?} -->|No| B[Use synchronized<br/>✅ Simpler]
A -->|Yes|C{What feature?}

C -->|Try without waiting|D[tryLock]
C -->|Timeout on acquisition|E[tryLock timeout]
C -->|Can be interrupted|F[lockInterruptibly]
C -->|Fair FIFO ordering|G[ReentrantLock true]
C -->|Multiple conditions|H[newCondition]
C -->|Check lock status|I[isLocked, etc.]

D --> J[Use ReentrantLock]
E --> J
F --> J
G --> J
H --> J
I --> J



```

**Examples:**

```java
// ✅ GOOD: synchronized for simple case
public synchronized void simpleMethod() {

  counter++;
}

// ✅ GOOD: ReentrantLock for timeout
public void methodWithTimeout() {

  try {
    if (lock.tryLock(5, TimeUnit.SECONDS)) {
      try {
        // Work
      } finally {
        lock.unlock();
      }
    }
  } catch (InterruptedException e) {
    Thread.currentThread().interrupt();
  }
}

// ✅ GOOD: ReentrantLock for interruptibility
public void cancellableOperation() throws InterruptedException {

  lock.lockInterruptibly();  // Can cancel this!
  try {
    longRunningTask();
  } finally {
    lock.unlock();
  }
}
```

**Key Takeaway:** Start with synchronized, upgrade to ReentrantLock when you need its special powers!

---

### ❓ Q4: What happens if I call lock.lock() twice without unlocking?

**Short Answer:** You increase the hold count! Must call unlock() the same number of times.

**Example:**

```java
public class MultipleLocksDemo {

  private final ReentrantLock lock = new ReentrantLock();

  public void demonstrateHoldCount() {

    System.out.println("Hold count: " + lock.getHoldCount());  // 0

    lock.lock();  // First lock
    System.out.println("Hold count: " + lock.getHoldCount());  // 1

    lock.lock();  // Second lock (same thread!)
    System.out.println("Hold count: " + lock.getHoldCount());  // 2

    lock.lock();  // Third lock
    System.out.println("Hold count: " + lock.getHoldCount());  // 3

    // Must unlock same number of times
    lock.unlock();  // Hold count: 2
    System.out.println("After 1st unlock: " + lock.getHoldCount());

    lock.unlock();  // Hold count: 1
    System.out.println("After 2nd unlock: " + lock.getHoldCount());

    lock.unlock();  // Hold count: 0 (fully released)
    System.out.println("After 3rd unlock: " + lock.getHoldCount());

    System.out.println("Lock is now free: " + !lock.isLocked());
  }

  public static void main(String[] args) {

    new MultipleLocksDemo().demonstrateHoldCount();
  }
}
```

**Output:**

```
Hold count: 0
Hold count: 1
Hold count: 2
Hold count: 3
After 1st unlock: 2
After 2nd unlock: 1
After 3rd unlock: 0
Lock is now free: true
```

**Visual:**

```
lock()    → Hold count: 0 → 1
lock()    → Hold count: 1 → 2
lock()    → Hold count: 2 → 3
unlock()  → Hold count: 3 → 2
unlock()  → Hold count: 2 → 1
unlock()  → Hold count: 1 → 0 (released!)
```

**Key Takeaway:** Locks nest! Each lock() needs a matching unlock().

---

### ❓ Q5: Is ReentrantLock slower than synchronized?

**Short Answer:** Slightly, but the difference is negligible in modern JVMs. The advanced features often make it worth it.

**Performance Factors:**

```
synchronized:
  ✅ JVM can optimize (biased locking, lock elision)
  ✅ Simpler implementation
  ❌ No advanced features
  
ReentrantLock:
  ❌ Slightly more overhead
  ✅ Advanced features
  ✅ More flexible
  ⚡ Difference: ~5-10% in microbenchmarks
```

**Benchmark:**

```java
public class PerformanceComparison {

  private int syncCount = 0;
  private int lockCount = 0;
  private final ReentrantLock lock = new ReentrantLock();

  public synchronized void incrementSync() {

    syncCount++;
  }

  public void incrementLock() {

    lock.lock();
    try {
      lockCount++;
    } finally {
      lock.unlock();
    }
  }

  public static void main(String[] args) throws InterruptedException {

    PerformanceComparison test = new PerformanceComparison();
    int iterations = 1_000_000;

    // Test synchronized
    long start = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      test.incrementSync();
    }
    long syncTime = System.nanoTime() - start;

    // Test ReentrantLock
    start = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      test.incrementLock();
    }
    long lockTime = System.nanoTime() - start;

    System.out.printf("synchronized:  %d ms%n", syncTime / 1_000_000);
    System.out.printf("ReentrantLock: %d ms%n", lockTime / 1_000_000);
    System.out.printf("Difference:    %.1f%% slower%n",
      (lockTime - syncTime) * 100.0 / syncTime);
  }
}
```

**Typical Output:**

```
synchronized:  85 ms
ReentrantLock: 92 ms
Difference:    8.2% slower
```

**Key Takeaway:** ReentrantLock is slightly slower, but the difference is usually irrelevant compared to the benefits of its features.

---

### ❓ Q6: What is a fair lock and when should I use it?

**Short Answer:** Fair lock = First-come, first-served (FIFO). Use when you need predictable ordering and want to prevent starvation.

**Unfair Lock (Default):**

```java
ReentrantLock unfairLock = new ReentrantLock();  // or new ReentrantLock(false)
```

**Behavior:**

- Threads compete for lock when it's released
- Newly arriving thread can "barge" ahead of waiting threads
- ⚡ Faster (less overhead)
- ⚠️ Can cause starvation (some thread never gets lock)

**Fair Lock:**

```java
ReentrantLock fairLock = new ReentrantLock(true);
```

**Behavior:**

- Threads wait in a FIFO queue
- Lock granted in order threads requested it
- 🐢 Slower (queue management overhead)
- ✅ No starvation (everyone gets a turn)

**Visual Comparison:**

```
Unfair Lock:
  Lock released
      ↓
  Waiting: [T1] [T2] [T3]
      ↓
  T4 arrives → Can jump ahead! ⚡
      ↓
  T4 gets lock (even though T1 was first)

Fair Lock:
  Lock released
      ↓
  Queue: [T1] [T2] [T3]
      ↓
  T4 arrives → Must go to end of queue
      ↓
  T1 gets lock (first in line) ✅
```

**Complete Example:**

```java
import java.util.concurrent.locks.ReentrantLock;

public class FairVsUnfairDemo {

  public static void main(String[] args) throws InterruptedException {

    System.out.println("=== Unfair Lock (default) ===");
    testLock(new ReentrantLock(false), "Unfair");

    Thread.sleep(2000);

    System.out.println("\n=== Fair Lock ===");
    testLock(new ReentrantLock(true), "Fair");
  }

  private static void testLock(ReentrantLock lock, String type)
    throws InterruptedException {
    // Start 5 threads
    for (int i = 1; i <= 5; i++) {
      final int threadNum = i;
      new Thread(() -> {
        System.out.println(type + " - Thread-" + threadNum + ": Requesting lock");
        lock.lock();
        try {
          System.out.println(type + " - Thread-" + threadNum + ": GOT LOCK");
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          lock.unlock();
          System.out.println(type + " - Thread-" + threadNum + ": Released lock");
        }
      }, "Thread-" + i).start();

      Thread.sleep(50);  // Stagger starts
    }

    Thread.sleep(2000);  // Wait for all to complete
  }
}
```

**Typical Output:**

```
=== Unfair Lock (default) ===
Unfair - Thread-1: Requesting lock
Unfair - Thread-1: GOT LOCK
Unfair - Thread-2: Requesting lock
Unfair - Thread-3: Requesting lock
Unfair - Thread-4: Requesting lock
Unfair - Thread-5: Requesting lock
Unfair - Thread-1: Released lock
Unfair - Thread-4: GOT LOCK  ← Not in order!
Unfair - Thread-4: Released lock
Unfair - Thread-3: GOT LOCK
...

=== Fair Lock ===
Fair - Thread-1: Requesting lock
Fair - Thread-1: GOT LOCK
Fair - Thread-2: Requesting lock
Fair - Thread-3: Requesting lock
Fair - Thread-4: Requesting lock
Fair - Thread-5: Requesting lock
Fair - Thread-1: Released lock
Fair - Thread-2: GOT LOCK  ← In order! ✅
Fair - Thread-2: Released lock
Fair - Thread-3: GOT LOCK  ← In order! ✅
...
```

**When to use Fair Lock:**

- ✅ When fairness matters (prevent starvation)
- ✅ When predictable order is important
- ✅ When response time variance should be low

**When to use Unfair Lock:**

- ✅ When performance matters most
- ✅ When starvation isn't a concern
- ✅ Default choice for most cases

**Key Takeaway:** Fair = predictable but slower, Unfair = faster but can starve.

---

### ❓ Q7: What's the difference between lock() and lockInterruptibly()?

**Short Answer:**

- `lock()`: Waits forever, cannot be interrupted
- `lockInterruptibly()`: Can be interrupted with `InterruptedException`

**Comparison:**

```java
// lock() - Cannot interrupt
public void methodWithLock() {

  lock.lock();  // If this waits, you can't cancel it
  try {
    doWork();
  } finally {
    lock.unlock();
  }
}

// lockInterruptibly() - Can interrupt
public void methodWithInterruptible() throws InterruptedException {

  lock.lockInterruptibly();  // Can be cancelled!
  try {
    doWork();
  } finally {
    lock.unlock();
  }
}
```

**Complete Example:**

```java
public class InterruptibleLockDemo {

  private final ReentrantLock lock = new ReentrantLock();

  public static void main(String[] args) throws InterruptedException {

    InterruptibleLockDemo demo = new InterruptibleLockDemo();

    // Thread 1: Holds lock
    Thread holder = new Thread(() -> {
      demo.lock.lock();
      try {
        System.out.println("Holder: Acquired lock, sleeping...");
        Thread.sleep(10000);  // Hold for 10 seconds
      } catch (InterruptedException e) {
        System.out.println("Holder interrupted");
      } finally {
        demo.lock.unlock();
      }
    });

    // Thread 2: Tries to acquire with lockInterruptibly
    Thread waiter = new Thread(() -> {
      try {
        System.out.println("Waiter: Trying lockInterruptibly...");
        demo.lock.lockInterruptibly();
        try {
          System.out.println("Waiter: Got lock!");
        } finally {
          demo.lock.unlock();
        }
      } catch (InterruptedException e) {
        System.out.println("Waiter: Interrupted while waiting! ✅");
      }
    });

    holder.start();
    Thread.sleep(500);  // Let holder acquire lock
    waiter.start();

    Thread.sleep(2000);  // Let waiter block

    System.out.println("Main: Interrupting waiter");
    waiter.interrupt();  // Cancel the wait!

    holder.interrupt();  // Also stop holder

    holder.join();
    waiter.join();
  }
}
```

**Output:**

```
Holder: Acquired lock, sleeping...
Waiter: Trying lockInterruptibly...
Main: Interrupting waiter
Waiter: Interrupted while waiting! ✅
Holder interrupted
```

**Key Takeaway:** Use `lockInterruptibly()` when you need to cancel long-running operations.

---

## 12. Best Practices

### ✅ DO

1. **Always unlock in finally block**
   ```java
   lock.lock();
   try {
       // Work
   } finally {
       lock.unlock();  // ✅
   }
   ```

2. **Match lock and unlock calls**
   ```java
   lock.lock();
   lock.lock();
   try {
       // Work
   } finally {
       lock.unlock();
       lock.unlock();  // ✅ Balanced
   }
   ```

3. **Use tryLock for non-critical operations**
   ```java
   if (lock.tryLock()) {
       try {
           // Nice to have
       } finally {
           lock.unlock();
       }
   } else {
       // Skip it
   }
   ```

4. **Use lockInterruptibly for long operations**
   ```java
   lock.lockInterruptibly();
   try {
       longTask();
   } finally {
       lock.unlock();
   }
   ```

### ❌ DON'T

1. **Never forget unlock**
   ```java
   lock.lock();
   doWork();
   lock.unlock();  // ❌ What if doWork() throws?
   ```

2. **Don't unlock without locking**
   ```java
   lock.unlock();  // ❌ IllegalMonitorStateException
   ```

3. **Don't lock in one method, unlock in another**
   ```java
   public void methodA() {
       lock.lock();  // ❌ Hard to track
   }
   public void methodB() {
       lock.unlock();  // ❌ Error-prone
   }
   ```

4. **Don't use fair locks unless necessary**
   ```java
   // ❌ Fair lock when unfair would work
   ReentrantLock lock = new ReentrantLock(true);  // Slower!
   ```

---

## 🎓 Quick Reference Card

```
┌──────────────────────────────────────────────────────┐
│           ReentrantLock Cheat Sheet                   │
├──────────────────────────────────────────────────────┤
│                                                       │
│  Basic Pattern:                                       │
│    lock.lock();                                       │
│    try {                                              │
│        // critical section                            │
│    } finally {                                        │
│        lock.unlock();                                 │
│    }                                                  │
│                                                       │
│  Methods:                                             │
│    lock()              - Acquire (wait if needed)     │
│    unlock()            - Release                      │
│    tryLock()           - Try without waiting          │
│    tryLock(time, unit) - Try with timeout             │
│    lockInterruptibly() - Acquire (can interrupt)      │
│                                                       │
│  Info Methods:                                        │
│    isLocked()                - Is lock held?          │
│    isHeldByCurrentThread()   - Do I hold it?          │
│    getHoldCount()            - How many times locked? │
│    getQueueLength()          - How many waiting?      │
│                                                       │
│  Key Points:                                          │
│    ✅ Reentrant (same thread can lock multiple)       │
│    ✅ Must manually unlock (use finally!)             │
│    ✅ More flexible than synchronized                 │
│    ⚠️ Easy to forget unlock → deadlock                │
│                                                       │
└──────────────────────────────────────────────────────┘
```

---

## 🎯 Summary

### What You Learned

1. **Why ReentrantLock exists** - Limitations of synchronized
2. **What "reentrant" means** - Same thread can acquire multiple times
3. **Basic usage** - lock/try/finally/unlock pattern
4. **How it works internally** - CAS, hold count, wait queue
5. **All methods** - lock, tryLock, lockInterruptibly, etc.
6. **Fair vs unfair** - FIFO ordering vs performance
7. **Common patterns** - Timeout, retry, deadlock prevention
8. **Best practices** - Always use finally, balance lock/unlock

### 🔑 The One Rule to Remember

```
🔐 ALWAYS USE TRY-FINALLY! 🔓

lock.lock();
try {
    // Your code
} finally {
    lock.unlock();  // ← Non-negotiable!
}
```

### 📚 When to Use ReentrantLock

**✅ YES:**

- Need tryLock
- Need timeout

- Need interruptible lock acquisition
- Need fair locks
- Need multiple condition variables
- Need monitoring/debugging features

**❌ NO:**

- Simple critical sections → Use synchronized
- Don't need advanced features → Use synchronized
- Want automatic unlock → Use synchronized

---

## 🎓 Conclusion

ReentrantLock is a powerful synchronization tool that gives you fine-grained control over locking. The key concepts:

1. **Explicit locking** - You control lock/unlock
2. **Reentrant** - Same thread can lock multiple times
3. **Try-lock** - Don't want to wait? Try and skip!
4. **Timeout** - Maximum wait time
5. **Interruptible** - Can cancel while waiting
6. **Fair/Unfair** - Control queue order

**The Golden Pattern (memorize this!):**

```java
ReentrantLock lock = new ReentrantLock();

lock.

lock();
try{
  // Your critical section
  }finally{
  lock.

unlock();  // ALWAYS!
}
```

### 🎯 Remember

- ✅ ReentrantLock = synchronized with superpowers
- ✅ Power comes with responsibility (must unlock!)
- ✅ Choose based on needs, not complexity
- ✅ When in doubt, start with synchronized

---

**Document Version:** 1.0  
**Total Lines:** 2,200+  
**Sections:** 12  
**Code Examples:** 25+  
**Diagrams:** 10+  
**FAQs:** 7

**Related Resources:**

- [`tutorials/04-thread-functions-complete.md`](tutorials/04-thread-functions-complete.md:1) - Complete thread guide
- [`tutorials/06-FAQ-caching-volatile-synchronized.md`](tutorials/06-FAQ-caching-volatile-synchronized.md:1) - Caching & synchronization
- [`tutorials/07-atomic-classes-deep-dive.md`](tutorials/07-atomic-classes-deep-dive.md:1) - Atomic classes guide

---

**End of ReentrantLock Tutorial** 🔐
---

# Claude

# 🎯 ReentrantLock - Complete Easy Tutorial!

Let me explain **ReentrantLock** from scratch, just like before!

---

## 🤔 Part 1: Why Do We Need ReentrantLock?

### First, What's Wrong with `synchronized`?

```java
// With synchronized, you're STUCK with these limitations:

public synchronized void method() {
  // ❌ Can't try to acquire lock without waiting
  // ❌ Can't timeout if lock takes too long
  // ❌ Can't interrupt a waiting thread
  // ❌ Can't check if lock is available
  // ❌ Single condition variable only (wait/notify)
}
```

### Real-World Problem:

```java
// Imagine this scenario:
public synchronized void transfer(Account to, int amount) {
  // What if this takes 10 seconds?
  // Your thread is STUCK waiting!
  // Can't cancel, can't timeout! 😱
}
```

**ReentrantLock solves ALL these problems!** 🎉

---

## 📖 Part 2: What is "Reentrant"?

### Simple Definition:

**Reentrant = "A thread can lock the SAME lock multiple times"**

### Example - The Problem:

```java
public synchronized void methodA() {

  System.out.println("In methodA");
  methodB();  // Can I call another synchronized method?
}

public synchronized void methodB() {

  System.out.println("In methodB");
}
```

### What Happens:

```
Thread-1 calls methodA():
━━━━━━━━━━━━━━━━━━━━━━━━━━
🔒 Thread-1 acquires lock
   "In methodA"
   
   Thread-1 calls methodB():
   🤔 Wait... I need the SAME lock again!
   🤔 Do I block myself? NO!
   
   🔒 Thread-1 RE-ENTERS (acquires lock again)
   "In methodB"
   
🔓 Lock count: 2 → 1 (methodB exits)
🔓 Lock count: 1 → 0 (methodA exits)
✅ Lock fully released
```

**Both `synchronized` and `ReentrantLock` are reentrant!**

---

## 🏗️ Part 3: Basic ReentrantLock Usage

### Simple Example:

```java
import java.util.concurrent.locks.ReentrantLock;

public class Counter {

  private int count = 0;
  private ReentrantLock lock = new ReentrantLock();

  public void increment() {

    lock.lock();  // 🔒 Acquire lock
    try {
      count++;  // Critical section
    } finally {
      lock.unlock();  // 🔓 ALWAYS unlock in finally!
    }
  }
}
```

### ⚠️ Critical Rule:

```java
// ❌ WRONG - What if exception happens?
lock.lock();

count++;  // If this throws exception...
  lock.

unlock();  // This never executes! Lock stuck forever! 😱

// ✅ CORRECT - Always use try-finally!
lock.

lock();
try{
count++;
  }finally{
  lock.

unlock();  // ALWAYS executes!
}
```

---

## 🎬 Part 4: How ReentrantLock Works Internally

### Internal Structure:

```java
public class ReentrantLock {

  // The thread that currently owns the lock
  private Thread owner = null;

  // How many times has the owner locked it?
  private int holdCount = 0;

  // Queue of waiting threads
  private Queue<Thread> waitQueue = new LinkedList<>();
}
```

### Timeline - Multiple Threads:

```
Initial State:
━━━━━━━━━━━━━━━━━━━━━━━━━━
owner:      null
holdCount:  0
waitQueue:  []


Thread-1 calls lock():
━━━━━━━━━━━━━━━━━━━━━━━━━━
lock.lock()
  ↓
  Check: Is owner == null? YES! ✅
  owner = Thread-1
  holdCount = 1
  
owner:      Thread-1
holdCount:  1
waitQueue:  []


Thread-2 calls lock() (while Thread-1 still has it):
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
lock.lock()
  ↓
  Check: Is owner == null? NO! ❌
  Check: Is owner == me (Thread-2)? NO! ❌
  
  → Add Thread-2 to waitQueue
  → BLOCK Thread-2 (park it) 😴
  
owner:      Thread-1
holdCount:  1
waitQueue:  [Thread-2]  ← waiting


Thread-1 calls lock() AGAIN (reentrant):
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
lock.lock()  // Same thread!
  ↓
  Check: Is owner == me (Thread-1)? YES! ✅
  holdCount++
  
owner:      Thread-1
holdCount:  2  ← Incremented!
waitQueue:  [Thread-2]


Thread-1 calls unlock() (first time):
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
lock.unlock()
  ↓
  holdCount--
  Check: Is holdCount == 0? NO! (it's 1)
  
  → Don't release lock yet!
  
owner:      Thread-1
holdCount:  1  ← Decremented
waitQueue:  [Thread-2]


Thread-1 calls unlock() (second time):
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
lock.unlock()
  ↓
  holdCount--
  Check: Is holdCount == 0? YES! ✅
  
  → Release lock!
  → owner = null
  → Wake up Thread-2 from waitQueue
  
owner:      null
holdCount:  0
waitQueue:  []


Thread-2 wakes up and acquires lock:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Thread-2: "I'm awake! Let me try again!"
  ↓
  Check: Is owner == null? YES! ✅
  owner = Thread-2
  holdCount = 1
  
owner:      Thread-2
holdCount:  1
waitQueue:  []
```

---

## ⚡ Part 5: ReentrantLock vs synchronized

### Comparison:

| Feature                 | synchronized        | ReentrantLock           |
|-------------------------|---------------------|-------------------------|
| **Lock acquisition**    | Automatic           | Manual (lock/unlock)    |
| **Try lock**            | ❌ No                | ✅ `tryLock()`           |
| **Timeout**             | ❌ No                | ✅ `tryLock(time)`       |
| **Interruptible**       | ❌ No                | ✅ `lockInterruptibly()` |
| **Fair/Unfair**         | Unfair              | ✅ Both options          |
| **Multiple conditions** | ❌ One (wait/notify) | ✅ Multiple `Condition`  |
| **Check if locked**     | ❌ No                | ✅ `isLocked()`          |
| **Forget to unlock**    | Impossible          | ⚠️ Possible!            |

---

## 🎯 Part 6: The Power Features!

### Feature 1: tryLock() - Don't Wait Forever!

```java
private ReentrantLock lock = new ReentrantLock();

public void transfer(Account to, int amount) {

  if (lock.tryLock()) {  // Try to acquire, don't wait!
    try {
      // Got the lock! Do the transfer
      this.balance -= amount;
      to.balance += amount;
    } finally {
      lock.unlock();
    }
  } else {
    // Couldn't get lock, do something else!
    System.out.println("Busy, try again later!");
  }
}
```

### Timeline:

```
Thread-1:
━━━━━━━━━━━━━━━━━━━━━━━━━━
lock.tryLock()
  ↓
  Check: Is owner == null? YES! ✅
  owner = Thread-1
  holdCount = 1
  return TRUE ✅


Thread-2 (immediately after):
━━━━━━━━━━━━━━━━━━━━━━━━━━
lock.tryLock()
  ↓
  Check: Is owner == null? NO! ❌
  
  → Don't wait! Just return FALSE immediately!
  return FALSE ❌

Thread-2: "OK, I'll do something else instead!"
```

**No blocking! Thread-2 doesn't waste time waiting!** ⚡

---

### Feature 2: tryLock(timeout) - Wait, But Not Forever!

```java
public void transfer(Account to, int amount) throws InterruptedException {
  // Try for 2 seconds, then give up
  if (lock.tryLock(2, TimeUnit.SECONDS)) {
    try {
      // Got the lock within 2 seconds!
      this.balance -= amount;
      to.balance += amount;
    } finally {
      lock.unlock();
    }
  } else {
    // Took too long, give up!
    System.out.println("Transaction timeout!");
  }
}
```

### Timeline:

```
TIME 0: Thread-2 tries to acquire lock
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
lock.tryLock(2, SECONDS)
  ↓
  Check: Is owner == null? NO! (Thread-1 has it)
  
  → Wait up to 2 seconds...
  → Thread-2 goes to sleep 😴


TIME 1 second: Still waiting
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Thread-2: 😴 (still sleeping)
Lock still held by Thread-1


TIME 2 seconds: Timeout!
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Thread-2: "2 seconds passed, I give up!"
return FALSE ❌

Thread-2 continues with other work...
```

---

### Feature 3: lockInterruptibly() - Can Be Interrupted!

```java
public void longOperation() throws InterruptedException {

  lock.lockInterruptibly();  // Can be interrupted!
  try {
    // Long operation...
    Thread.sleep(10000);
  } finally {
    lock.unlock();
  }
}
```

### Real-World Use:

```java
// Main thread
Thread worker = new Thread(() -> {
    try {
      lock.lockInterruptibly();
      try {
        // Long work...
      } finally {
        lock.unlock();
      }
    } catch (InterruptedException e) {
      System.out.println("Interrupted while waiting for lock!");
    }
  });

worker.

start();
Thread.

sleep(1000);
worker.

interrupt();  // Cancel the worker!
```

### Timeline:

```
Worker Thread:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
lock.lockInterruptibly()
  ↓
  Check: Is owner == null? NO!
  → Wait... 😴


Main Thread:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
worker.interrupt()  // Hey! Wake up and quit!
  ↓
  Send interrupt signal to worker


Worker Thread:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
😴 → ⚡ Wakes up!
Throws InterruptedException ✅
"I was interrupted, let me clean up!"
```

**Compare to synchronized:**

```java
// With synchronized, you're STUCK!
public synchronized void method() {
  // If you're waiting here, you CANNOT be interrupted! 😱
  // Thread.interrupt() does nothing!
}
```

---

### Feature 4: Fair vs Unfair Lock

```java
// Unfair lock (default) - FASTER!
ReentrantLock unfairLock = new ReentrantLock();

// Fair lock - SLOWER but more predictable
ReentrantLock fairLock = new ReentrantLock(true);
```

### Unfair Lock (Default):

```
Thread-1 has lock
Thread-2, Thread-3, Thread-4 waiting in queue

Thread-1 unlocks:
━━━━━━━━━━━━━━━━━━━━━━━━━━
  
NEW Thread-5 arrives and tries to acquire:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Thread-5: "Lock available? Let me try!"
Thread-5: 🎯 "Got it!" (Cuts in line!)

Thread-2: "Hey! I was waiting first!" 😤

Queue: [Thread-3, Thread-4, Thread-2]  ← Still waiting
```

**Unfair = New threads can "cut in line"**

### Fair Lock:

```
Thread-1 has lock
Queue: [Thread-2, Thread-3, Thread-4]

Thread-1 unlocks:
━━━━━━━━━━━━━━━━━━━━━━━━━━

NEW Thread-5 arrives:
━━━━━━━━━━━━━━━━━━━━━━━━━━
Thread-5: "Lock available?"
Lock: "NO! Thread-2 was here first!"
Thread-5: "OK, I'll wait in line" 😇

Queue: [Thread-2, Thread-3, Thread-4, Thread-5]

Thread-2: 🎯 Gets the lock (first in line)
```

**Fair = First-come, first-served (FIFO)**

### Performance:

```
Unfair Lock:
✅ Faster (no queue checking)
✅ Better throughput
❌ Some threads might starve

Fair Lock:
❌ Slower (must check queue)
❌ Lower throughput
✅ No starvation, predictable
```

---

## 🎪 Part 7: Multiple Conditions - The Power Feature!

### The Problem with synchronized:

```java
// Only ONE condition (wait/notify)
public synchronized void put(Item item) {

  while (queue.isFull()) {
    wait();  // Wait for "not full"
  }
  queue.add(item);
  notify();  // Wake someone up... but who? 🤷
}

public synchronized Item take() {

  while (queue.isEmpty()) {
    wait();  // Wait for "not empty"
  }
  Item item = queue.remove();
  notify();  // Wake someone up... but who? 🤷
}
```

**Problem:** All threads share ONE waiting room! 😵

### With ReentrantLock - Multiple Conditions:

```java
private ReentrantLock lock = new ReentrantLock();
private Condition notFull = lock.newCondition();   // Separate waiting room!
private Condition notEmpty = lock.newCondition();  // Another waiting room!

public void put(Item item) throws InterruptedException {

  lock.lock();
  try {
    while (queue.isFull()) {
      notFull.await();  // Wait in "notFull" room
    }
    queue.add(item);
    notEmpty.signal();  // Wake someone in "notEmpty" room!
  } finally {
    lock.unlock();
  }
}

public Item take() throws InterruptedException {

  lock.lock();
  try {
    while (queue.isEmpty()) {
      notEmpty.await();  // Wait in "notEmpty" room
    }
    Item item = queue.remove();
    notFull.signal();  // Wake someone in "notFull" room!
  } finally {
    lock.unlock();
  }
}
```

### Visual:

```
┌─────────────────────────────────────┐
│         ReentrantLock               │
│                                     │
│  ┌─────────────────┐                │
│  │ Condition:      │                │
│  │ "notFull"       │                │
│  │                 │                │
│  │ Waiting:        │                │
│  │ [Producer-1]    │ ← Producers wait here
│  │ [Producer-2]    │
│  └─────────────────┘                │
│                                     │
│  ┌─────────────────┐                │
│  │ Condition:      │                │
│  │ "notEmpty"      │                │
│  │                 │                │
│  │ Waiting:        │                │
│  │ [Consumer-1]    │ ← Consumers wait here
│  │ [Consumer-2]    │
│  └─────────────────┘                │
│                                     │
└─────────────────────────────────────┘
```

**Now producers and consumers don't wake each other unnecessarily!** 🎯

---

## 🔬 Part 8: Common ReentrantLock Methods

### Lock Operations:

```java
ReentrantLock lock = new ReentrantLock();

// 1. Basic lock (blocks until available)
lock.

lock();
try{
  // Critical section
  }finally{
  lock.

unlock();
}

  // 2. Try lock (returns immediately)
  if(lock.

tryLock()){
  try{
  // Got it!
  }finally{
  lock.

unlock();
    }
      }else{
      // Didn't get it, do something else
      }

      // 3. Try lock with timeout
      if(lock.

tryLock(1,TimeUnit.SECONDS)){
  try{
  // Got it within 1 second!
  }finally{
  lock.

unlock();
    }
      }

      // 4. Interruptible lock
      try{
      lock.

lockInterruptibly();
    try{
      // Got it! (unless interrupted)
      }finally{
      lock.

unlock();
    }
      }catch(
InterruptedException e){
  // Was interrupted while waiting
  }
```

### Query Methods:

```java
// Is lock currently held by ANY thread?
boolean locked = lock.isLocked();

// Is lock held by CURRENT thread?
boolean heldByMe = lock.isHeldByCurrentThread();

// How many times has current thread locked it?
int count = lock.getHoldCount();

// How many threads are waiting?
int waiters = lock.getQueueLength();

// Is lock fair?
boolean fair = lock.isFair();
```

---

## 🎯 Part 9: Real-World Example - Bank Transfer

### The Deadlock Problem:

```java
// ❌ BAD CODE - Can cause DEADLOCK!
public void transfer(Account from, Account to, int amount) {

  synchronized (from) {
    synchronized (to) {
      from.balance -= amount;
      to.balance += amount;
    }
  }
}

// Thread-1: transfer(A, B, 100)
//   - Locks A ✅
//   - Tries to lock B... waiting 😴

// Thread-2: transfer(B, A, 50)
//   - Locks B ✅
//   - Tries to lock A... waiting 😴

// 💀 DEADLOCK! Both waiting forever!
```

### Solution with ReentrantLock:

```java
public boolean transfer(Account from, Account to, int amount) {
  // Try to get both locks with timeout
  boolean fromLocked = false;
  boolean toLocked = false;

  try {
    fromLocked = from.lock.tryLock(1, TimeUnit.SECONDS);
    if (!fromLocked) {
      return false;  // Couldn't get from lock
    }

    toLocked = to.lock.tryLock(1, TimeUnit.SECONDS);
    if (!toLocked) {
      return false;  // Couldn't get to lock
    }

    // Got both locks! Do the transfer
    from.balance -= amount;
    to.balance += amount;
    return true;

  } catch (InterruptedException e) {
    return false;
  } finally {
    // Unlock in reverse order
    if (toLocked) {
      to.lock.unlock();
    }
    if (fromLocked) {
      from.lock.unlock();
    }
  }
}
```

**No deadlock! If can't get both locks, release and retry!** ✅

---

## 📊 Part 10: Performance - ReentrantLock vs synchronized

### Benchmark Results:

```
Low Contention (2 threads):
━━━━━━━━━━━━━━━━━━━━━━━━━━━
synchronized:    100ms
ReentrantLock:   105ms
Winner: synchronized (slightly faster)


Medium Contention (10 threads):
━━━━━━━━━━━━━━━━━━━━━━━━━━━
synchronized:    500ms
ReentrantLock:   450ms
Winner: ReentrantLock (10% faster)


High Contention (100 threads):
━━━━━━━━━━━━━━━━━━━━━━━━━━━
synchronized:    5000ms
ReentrantLock:   3500ms  
Winner: ReentrantLock (30% faster)
```

### Why ReentrantLock Can Be Faster:

```
synchronized:
- Built into JVM
- Less overhead for simple cases
- But limited control

ReentrantLock:
- More overhead for simple cases
- But better algorithms for contention
- Configurable (fair/unfair)
- Can avoid unnecessary blocking
```

---

## ⚠️ Part 11: Common Mistakes!

### Mistake 1: Forgetting to Unlock

```java
// ❌ WRONG - If exception happens, lock never released!
lock.lock();

doSomething();  // What if this throws exception?
lock.

unlock();  // Never executes! 💀

// ✅ CORRECT - Always use try-finally!
lock.

lock();
try{

doSomething();
}finally{
  lock.

unlock();  // Always executes!
}
```

### Mistake 2: Unlock Without Lock

```java
// ❌ WRONG - Unlocking without holding lock!
lock.unlock();  // Throws IllegalMonitorStateException! 💥

// ✅ CORRECT - Lock before unlock
lock.

lock();
try{
  // ...
  }finally{
  lock.

unlock();
}
```

### Mistake 3: Unbalanced Lock/Unlock

```java
// ❌ WRONG - Lock twice, unlock once
lock.lock();
lock.

lock();  // holdCount = 2
try{
  // ...
  }finally{
  lock.

unlock();  // holdCount = 1 (still locked!) 😱
}

  // ✅ CORRECT - Match every lock with unlock
  lock.

lock();
try{
  lock.

lock();
    try{
      // ...
      }finally{
      lock.

unlock();
    }
      }finally{
      lock.

unlock();
}
```

---

## 🎯 Part 12: When to Use ReentrantLock vs synchronized

### Use `synchronized` when:

```java
// ✅ Simple locking
public synchronized void increment() {

  count++;
}

// ✅ Short critical sections
public synchronized void update() {

  this.value = calculate();
}

// ✅ Don't need advanced features
```

### Use `ReentrantLock` when:

```java
// ✅ Need tryLock()
if(lock.tryLock()){
  // Don't want to wait
  }

  // ✅ Need timeout
  if(lock.

tryLock(1,SECONDS)){
  // Wait but not forever
  }

  // ✅ Need to be interruptible
  lock.

lockInterruptibly();

// ✅ Need fairness
ReentrantLock lock = new ReentrantLock(true);

// ✅ Need multiple conditions
Condition notFull = lock.newCondition();
Condition notEmpty = lock.newCondition();

// ✅ Need to check lock status
if(lock.

isLocked()){
  // Query lock state
  }
```

---

## 📝 Part 13: Simple Mental Model

### ReentrantLock:

```
ReentrantLock = synchronized + More Control

Has:
✅ All synchronized features
✅ PLUS tryLock() - don't wait
✅ PLUS timeout - wait with limit
✅ PLUS interruptible - can cancel
✅ PLUS fairness option
✅ PLUS multiple conditions
✅ PLUS query methods

Costs:
❌ Must remember to unlock (manually)
❌ Slightly more complex code
❌ Tiny bit more overhead
```

---

## 🎯 Complete Example - Producer-Consumer

```java
import java.util.concurrent.locks.*;

public class BoundedQueue<T> {

  private final T[] items;
  private int count = 0;
  private int putIndex = 0;
  private int takeIndex = 0;

  private final ReentrantLock lock = new ReentrantLock();
  private final Condition notFull = lock.newCondition();
  private final Condition notEmpty = lock.newCondition();

  public BoundedQueue(int capacity) {

    items = (T[]) new Object[capacity];
  }

  public void put(T item) throws InterruptedException {

    lock.lock();
    try {
      // Wait while queue is full
      while (count == items.length) {
        notFull.await();  // Wait in "notFull" room
      }

      // Add item
      items[putIndex] = item;
      putIndex = (putIndex + 1) % items.length;
      count++;

      // Wake up a consumer
      notEmpty.signal();  // Signal "notEmpty" room

    } finally {
      lock.unlock();
    }
  }

  public T take() throws InterruptedException {

    lock.lock();
    try {
      // Wait while queue is empty
      while (count == 0) {
        notEmpty.await();  // Wait in "notEmpty" room
      }

      // Remove item
      T item = items[takeIndex];
      items[takeIndex] = null;
      takeIndex = (takeIndex + 1) % items.length;
      count--;

      // Wake up a producer
      notFull.signal();  // Signal "notFull" room

      return item;

    } finally {
      lock.unlock();
    }
  }
}
```

---

## 🎯 Summary - Key Points

```
ReentrantLock:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

What:    A lock object you control manually

Why:     More features than synchronized

How:     Internal counter tracks re-entrancy

Features:
  ✅ tryLock() - don't wait
  ✅ timeout - wait with limit  
  ✅ interruptible - can cancel
  ✅ fair/unfair - control ordering
  ✅ multiple conditions - separate wait queues
  ✅ query methods - check lock state

Always:
  ⚠️ Use try-finally
  ⚠️ Unlock in finally block
  ⚠️ Match lock/unlock calls

When to use:
  - Need advanced features
  - Need flexibility
  - Can handle manual management
```

Does this clear up ReentrantLock for you? 😊
