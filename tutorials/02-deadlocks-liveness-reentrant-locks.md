
# 🔒 Deadlocks, Liveness & Reentrant Locks - Complete Guide

> Master advanced synchronization challenges and solutions in concurrent programming

## 📋 Table of Contents

1. [Introduction: Understanding Locks](#-introduction-understanding-locks)
2. [What is a Deadlock?](#-what-is-a-deadlock)
3. [The Four Coffman Conditions](#-the-four-coffman-conditions)
4. [Deadlock Examples & Analysis](#-deadlock-examples--analysis)
5. [Deadlock Detection](#-deadlock-detection)
6. [Deadlock Prevention Strategies](#-deadlock-prevention-strategies)
7. [The Dining Philosophers Problem](#-the-dining-philosophers-problem)
8. [Resource Allocation Graphs](#-resource-allocation-graphs)
9. [Banker's Algorithm](#-bankers-algorithm)
10. [Liveness Problems](#-liveness-problems)
11. [ReentrantLock Deep Dive](#-reentrantlock-deep-dive)
12. [Advanced Lock Patterns](#-advanced-lock-patterns)
13. [Lock-Free Programming](#-lock-free-programming)
14. [Production Debugging](#-production-debugging)
15. [Complete Real-World Solutions](#-complete-real-world-solutions)

---

## 🔐 Introduction: Understanding Locks

### What is a Lock?

Before understanding deadlocks, let's understand locks from first principles.

**A lock is a synchronization primitive that ensures mutual exclusion** - only one thread can hold the lock at a time.

```
Without Lock:                With Lock:
━━━━━━━━━━━━━━━━━━━━━━      ━━━━━━━━━━━━━━━━━━━━━━
Thread 1: Read x (0)         Thread 1: 🔒 Lock
Thread 2: Read x (0)                   Read x (0)
Thread 1: x = 1                        x = 1
Thread 2: x = 1                        Write x
Result: x = 1 ❌                       🔓 Unlock
                             Thread 2: 🔒 Lock
                                       Read x (1)
                                       x = 2
                                       Write x
                                       🔓 Unlock
                             Result: x = 2 ✅
```

### How Locks Work at the OS Level

**Deep Dive: Lock Implementation**

```
Mutex (Mutual Exclusion Lock):
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

┌─────────────────────────────────────────────┐
│  Lock Structure                             │
│  ┌───────────────────────────────────────┐ │
│  │  State: 0 (unlocked) or 1 (locked)    │ │
│  │  Owner: Thread ID (if locked)         │ │
│  │  Wait Queue: [Thread2, Thread3, ...]  │ │
│  └───────────────────────────────────────┘ │
└─────────────────────────────────────────────┘

Lock Acquisition (pseudocode):
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
lock():
  while (true):
    // Atomic compare-and-swap
    if (state.compareAndSwap(0, 1)):
      owner = currentThread
      return  // Got the lock!
    else:
      // Failed to acquire
      if (should_spin()):
        continue  // Spin a bit
      else:
        add_to_wait_queue(currentThread)
        park(currentThread)  // Sleep
```

**Why Multiple Threads Need Locks:**

```java
/**
 * Without locks - race condition
 */
public class WithoutLockExample {
    private int balance = 1000;
    
    public void withdraw(int amount) {
        // Thread 1                    // Thread 2
        if (balance >= amount) {      if (balance >= amount) {
            // Both see 1000 >= 800      // Both see 1000 >= 800
            balance -= amount;            balance -= amount;
            // balance = 200                // balance = 200
        }                             }
        // Final balance: 200 (should be -600 or one rejected!)
    }
}

/**
 * With locks - correct behavior
 */
public class WithLockExample {
    private int balance = 1000;
    private final Object lock = new Object();
    
    public void withdraw(int amount) {
        synchronized (lock) {  // Only one thread at a time
            if (balance >= amount) {
                balance -= amount;
            } else {
                throw new InsufficientFundsException();
            }
        }
    }
}
```

### Types of Locks

Java provides several types of locks:

| Lock Type | Description | Use Case |
|-----------|-------------|----------|
| **Intrinsic Lock** | Built-in `synchronized` | Simple mutual exclusion |
| **ReentrantLock** | Explicit lock with features | Advanced control |
| **ReadWriteLock** | Separate read/write locks | Read-heavy workloads |
| **StampedLock** | Optimistic locking | High-performance scenarios |

### Lock Properties

**1. Mutual Exclusion**
```java
synchronized (lock) {
    // Only ONE thread here at a time
    criticalSection();
}
```

**2. Visibility**
```java
// Lock ensures changes are visible to other threads
synchronized (lock) {
    value = 42;  // Visible to next lock holder
}
```

**3. Ordering**
```java
// Happens-before relationship
synchronized (lock) {
    x = 1;  // Happens-before next lock acquisition
}
```

---

## 💀 What is a Deadlock?

### Definition

**A deadlock is a situation where two or more threads are permanently blocked, each waiting for a resource held by another thread in the cycle.**

```
Classic Deadlock Scenario:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Thread 1:                    Thread 2:
┌────────────────┐          ┌────────────────┐
│ 🔒 Holds Lock A│          │ 🔒 Holds Lock B│
│ ⏳ Wants Lock B│          │ ⏳ Wants Lock A│
└────────────────┘          └────────────────┘
        ↓                           ↓
        └──────────┬────────────────┘
                   ↓
            ☠️ DEADLOCK ☠️
     Neither can proceed!
```

### Real-World Analogy

**Think of a narrow bridge:**

```
Bridge Deadlock:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
         Narrow Bridge
    ───────────────────────
Car A →                    ← Car B
    ───────────────────────

Car A enters from west, Car B enters from east
Both reach middle: DEADLOCK!
Neither can proceed, neither can reverse

Solution: Traffic light (lock ordering)
         Signal (one direction at a time)
```

### Why Deadlocks Happen

**Historical Context:** Deadlocks were identified by E.W. Dijkstra in the 1960s while studying resource allocation in operating systems.

**Root Causes:**
1. **Circular waiting**: Threads form a cycle of resource dependencies
2. **No preemption**: Can't forcibly take resources from threads
3. **Multiple resources**: More than one lock/resource needed
4. **Timing**: Specific thread scheduling creates the cycle

```java
/**
 * Simple deadlock example
 */
public class SimpleDeadlock {
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();
    
    public void method1() {
        synchronized (lock1) {           // T1 acquires lock1
            System.out.println("Thread 1: Holding lock1...");
            
            try { Thread.sleep(100); }   // Simulate work
            catch (InterruptedException e) { }
            
            System.out.println("Thread 1: Waiting for lock2...");
            synchronized (lock2) {       // T1 waits for lock2
                System.out.println("Thread 1: Holding lock1 & lock2");
            }
        }
    }
    
    public void method2() {
        synchronized (lock2) {           // T2 acquires lock2
            System.out.println("Thread 2: Holding lock2...");
            
            try { Thread.sleep(100); }   // Simulate work
            catch (InterruptedException e) { }
            
            System.out.println("Thread 2: Waiting for lock1...");
            synchronized (lock1) {       // T2 waits for lock1
                System.out.println("Thread 2: Holding lock1 & lock2");
            }
        }
    }
    
    public static void main(String[] args) {
        SimpleDeadlock deadlock = new SimpleDeadlock();
        
        Thread t1 = new Thread(() -> deadlock.method1(), "Thread-1");
        Thread t2 = new Thread(() -> deadlock.method2(), "Thread-2");
        
        t1.start();
        t2.start();
        
        // Program hangs forever! ☠️
    }
}
```

**Output:**
```
Thread 1: Holding lock1...
Thread 2: Holding lock2...
Thread 1: Waiting for lock2...
Thread 2: Waiting for lock1...
(Program hangs indefinitely)
```

### Execution Timeline

```
Time    Thread-1                Lock1    Lock2    Thread-2
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
t0      method1()               🔓       🔓       
t1      synchronized(lock1)     🔒T1     🔓       
t2      "Holding lock1..."      🔒T1     🔓       method2()
t3      sleep(100)              🔒T1     🔓       synchronized(lock2)
t4      sleep(100)              🔒T1     🔒T2     "Holding lock2..."
t5      "Waiting for lock2..."  🔒T1     🔒T2     sleep(100)
t6      synchronized(lock2)     🔒T1     🔒T2     sleep(100)
        ⏳ BLOCKED                                "Waiting for lock1..."
t7                              🔒T1     🔒T2     synchronized(lock1)
                                                  ⏳ BLOCKED
t∞      ☠️ DEADLOCK - Both threads waiting forever!
```

### CPU Scheduling and Deadlocks

**Question:** Why doesn't the CPU just prevent deadlocks?

**Answer:** The CPU scheduler doesn't understand lock dependencies. It only knows:
- Which threads are RUNNABLE
- Which threads are BLOCKED
- Thread priorities

```
CPU Scheduler's View:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Thread-1: BLOCKED (waiting for lock2)
Thread-2: BLOCKED (waiting for lock1)

Scheduler: "Both blocked, can't run either"
           "I don't know WHY they're blocked"
           "I just know they are"

The scheduler can't detect the circular dependency!
```

---

## 🔑 The Four Coffman Conditions

In 1971, E.G. Coffman identified four necessary conditions for deadlock. **ALL four must be present** for a deadlock to occur.

### 1. Mutual Exclusion

**At least one resource must be held in a non-shareable mode.**

```
Visual Representation:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
┌──────────────────┐
│   Lock Object    │
│   ┌──────────┐   │
│   │  🔒 T1   │   │  ← Only Thread-1 can hold
│   └──────────┘   │
│                  │
│   ⏳ Thread-2    │  ← Thread-2 must wait
│   ⏳ Thread-3    │
│   ⏳ Thread-4    │
└──────────────────┘

If resources were shareable (e.g., read-only data):
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
┌──────────────────┐
│   Shared Data    │
│   ✅ Thread-1    │  ← All can access
│   ✅ Thread-2    │     simultaneously
│   ✅ Thread-3    │     No waiting!
│   ✅ Thread-4    │
└──────────────────┘
```

**Code Example:**

```java
public class MutualExclusionExample {
    // ❌ Mutual exclusion - can deadlock
    private final Object exclusiveLock = new Object();
    private int sharedResource = 0;
    
    public void exclusiveAccess() {
        synchronized (exclusiveLock) {  // Only one thread
            sharedResource++;
        }
    }
    
    // ✅ Shared access - cannot deadlock from this alone
    private volatile int readOnlyResource = 0;
    
    public int sharedAccess() {
        return readOnlyResource;  // Multiple threads can read
    }
}
```

**Breaking This Condition:**
- Use lock-free algorithms
- Make resources shareable (e.g., ReadWriteLock for reads)
- Use immutable data structures

### 2. Hold and Wait

**A thread holding at least one resource is waiting to acquire additional resources held by other threads.**

```
Visual Representation:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Thread-1:                    Thread-2:
┌──────────────┐            ┌──────────────┐
│ 🔒 Holds:    │            │ 🔒 Holds:    │
│    Lock A    │            │    Lock B    │
│              │            │              │
│ ⏳ Wants:    │            │ ⏳ Wants:    │
│    Lock B    │            │    Lock A    │
└──────────────┘            └──────────────┘
     Hold and Wait               Hold and Wait

Contrast with No Hold-and-Wait:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Thread-1: Requests [Lock A, Lock B] atomically
          Either gets BOTH or gets NONE
          No holding while waiting!
```

**Code Example:**

```java
public class HoldAndWaitExample {
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();
    
    // ❌ Hold-and-Wait: Can deadlock
    public void holdAndWait() {
        synchronized (lock1) {        // Hold lock1
            doWork1();
            synchronized (lock2) {    // Wait for lock2 while holding lock1
                doWork2();
            }
        }
    }
    
    // ✅ No Hold-and-Wait: Atomic acquisition
    public void noHoldAndWait() {
        // Try to acquire both locks atomically
        synchronized (lock1) {
            if (tryAcquire(lock2)) {  // Non-blocking try
                try {
                    doWork1();
                    doWork2();
                } finally {
                    release(lock2);
                }
            }
        }
    }
    
    private boolean tryAcquire(Object lock) {
        // Implementation would use tryLock
        return false;
    }
    
    private void release(Object lock) { }
    private void doWork1() { }
    private void doWork2() { }
}
```

**Breaking This Condition:**
- Acquire all locks atomically at once
- Release all locks before acquiring new ones
- Use [`tryLock()`](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/locks/Lock.html#tryLock--) to avoid waiting

### 3. No Preemption

**Resources cannot be forcibly removed from threads holding them; they must be released voluntarily.**

```
Visual Representation:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Thread-1 holds Lock:
┌────────────────────────┐
│  Thread-1              │
│  ┌──────────────────┐  │
│  │ 🔒 Lock          │  │  ← Cannot be taken away!
│  │ (Must release)   │  │
│  └──────────────────┘  │
└────────────────────────┘

With Preemption (hypothetical):
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
OS: "Thread-1, give me that lock!"
Thread-1: "Okay, taking it..." 
(But state might be inconsistent!) ❌
```

**Why Java Doesn't Allow Preemption:**

```java
public class WhyNoPreemption {
    private int balance = 1000;
    
    public synchronized void transfer() {
        balance -= 500;
        // ⚠️ If lock forcibly removed here:
        //    balance is inconsistent!
        //    Data corruption!
        sendToOtherAccount(500);
        // Lock released here naturally
    }
}
```

**Breaking This Condition:**
- Use timeout-based locking
- Request threads to release resources
- Use interruption mechanisms

### 4. Circular Wait

**There exists a circular chain of threads, where each thread holds at least one resource needed by the next thread in the chain.**

```
Visual Representation:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        T1 holds L1,
        needs L2
           ↓
    ┌──────────────┐
    │              ↓
    │         T2 holds L2,
    │         needs L3
    │              ↓
    │         ┌────────────┐
    │         │            ↓
    │         │       T3 holds L3,
    │         │       needs L1
    │         │            ↓
    └─────────┴────────────┘
            ↑
     Cycle completed!
     ☠️ DEADLOCK

Without Circular Wait (Linear Order):
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
T1: Lock L1 → Lock L2 → Lock L3
T2: Lock L1 → Lock L2 → Lock L3
T3: Lock L1 → Lock L2 → Lock L3

All acquire in SAME order → No cycle possible ✅
```

**Code Example:**

```java
public class CircularWaitExample {
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();
    private final Object lock3 = new Object();
    
    // ❌ Circular wait possible
    public void method1() {
        synchronized (lock1) {
            synchronized (lock2) {  // L1 → L2
                work();
            }
        }
    }
    
    public void method2() {
        synchronized (lock2) {
            synchronized (lock3) {  // L2 → L3
                work();
            }
        }
    }
    
    public void method3() {
        synchronized (lock3) {
            synchronized (lock1) {  // L3 → L1 (Cycle!)
                work();
            }
        }
    }
    
    // ✅ No circular wait - consistent ordering
    public void safeMethod1() {
        synchronized (lock1) {      // Always L1 first
            synchronized (lock2) {  // Then L2
                synchronized (lock3) {  // Then L3
                    work();
                }
            }
        }
    }
    
    public void safeMethod2() {
        synchronized (lock1) {      // Same order everywhere
            synchronized (lock2) {
                synchronized (lock3) {
                    work();
                }
            }
        }
    }
    
    private void work() { }
}
```

**Breaking This Condition:**
- Impose total ordering on locks
- Always acquire locks in same order
- Use lock hierarchy

### The Coffman Conditions Summary

```
Deadlock Occurs When ALL Four Conditions Are Met:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

1. Mutual Exclusion      ✅ Present
   AND
2. Hold and Wait         ✅ Present
   AND
3. No Preemption         ✅ Present
   AND
4. Circular Wait         ✅ Present
   ↓
☠️ DEADLOCK!

Break ANY ONE condition → Deadlock impossible! ✅
```

---

## 🎭 Deadlock Examples & Analysis

### Example 1: The Classic Two-Lock Deadlock

```java
/**
 * Classic deadlock with detailed analysis
 */
public class TwoLockDeadlock {
    private final Object resourceA = new Object();
    private final Object resourceB = new Object();
    
    /**
     * Thread 1 executes this
     */
    public void process1() {
        synchronized (resourceA) {
            System.out.println(Thread.currentThread().getName() + 
                             ": Locked Resource A");
            
            // Simulate some work
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            System.out.println(Thread.currentThread().getName() + 
                             ": Waiting for Resource B");
            
            synchronized (resourceB) {
                System.out.println(Thread.currentThread().getName() + 
                                 ": Locked Resource A and B");
            }
        }
    }
    
    /**
     * Thread 2 executes this
     */
    public void process2() {
        synchronized (resourceB) {
            System.out.println(Thread.currentThread().getName() + 
                             ": Locked Resource B");
            
            // Simulate some work
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            System.out.println(Thread.currentThread().getName() + 
                             ": Waiting for Resource A");
            
            synchronized (resourceA) {
                System.out.println(Thread.currentThread().getName() + 
                                 ": Locked Resource A and B");
            }
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        TwoLockDeadlock example = new TwoLockDeadlock();
        
        Thread t1 = new Thread(() -> example.process1(), "Thread-1");
        Thread t2 = new Thread(() -> example.process2(), "Thread-2");
        
        t1.start();
        t2.start();
        
        // Wait a bit, then check if threads are stuck
        Thread.sleep(5000);
        
        if (t1.isAlive() || t2.isAlive()) {
            System.out.println("\n☠️ DEADLOCK DETECTED!");
            System.out.println("Thread-1 state: " + t1.getState());
            System.out.println("Thread-2 state: " + t2.getState());
            
            // Print thread dumps
            System.out.println("\nThread dumps:");
            for (StackTraceElement[] stack : new StackTraceElement[][]{
                t1.getStackTrace(), t2.getStackTrace()
            }) {
                for (StackTraceElement element : stack) {
                    System.out.println("  " + element);
                }
                System.out.println();
            }
        }
    }
}
```

**Analysis:**

```
Coffman Conditions Check:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
1. Mutual Exclusion:  ✅ synchronized blocks
2. Hold and Wait:     ✅ Holding A while waiting for B
3. No Preemption:     ✅ Locks can't be forcibly taken
4. Circular Wait:     ✅ T1→A→B, T2→B→A (cycle!)

Result: DEADLOCK POSSIBLE (and will occur)
```

### Example 2: Bank Account Transfer Deadlock

```java
/**
 * Real-world example: transferring money between accounts
 */
public class BankAccountDeadlock {
    
    static class Account {
        private final int id;
        private int balance;
        private final Object lock = new Object();
        
        public Account(int id, int balance) {
            this.id = id;
            this.balance = balance;
        }
        
        public void withdraw(int amount) {
            balance -= amount;
        }
        
        public void deposit(int amount) {
            balance += amount;
        }
        
        public int getBalance() {
            return balance;
        }
        
        public int getId() {
            return id;
        }
        
        public Object getLock() {
            return lock;
        }
    }
    
    /**
     * ❌ DEADLOCK-PRONE: Can deadlock if two transfers happen simultaneously
     * 
     * Scenario:
     * Transfer 1: A → B (locks A, then B)
     * Transfer 2: B → A (locks B, then A)
     * DEADLOCK!
     */
    public static void transferDeadlockProne(Account from, Account to, int amount) {
        synchronized (from.getLock()) {
            System.out.println(Thread.currentThread().getName() + 
                             ": Locked " + from.getId());
            
            try {
                Thread.sleep(100);  // Simulate work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            System.out.println(Thread.currentThread().getName() + 
                             ": Waiting for " + to.getId());
            
            synchronized (to.getLock()) {
                System.out.println(Thread.currentThread().getName() + 
                                 ": Locked both accounts");
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
    }
    
    /**
     * ✅ DEADLOCK-FREE: Uses lock ordering
     * 
     * Key: Always acquire locks in consistent order (lower ID first)
     */
    public static void transferSafe(Account from, Account to, int amount) {
        Account first = from.getId() < to.getId() ? from : to;
        Account second = from.getId() < to.getId() ? to : from;
        
        synchronized (first.getLock()) {
            synchronized (second.getLock()) {
                from.withdraw(amount);
                to.deposit(amount);
                System.out.println("Transfer complete: " + from.getId() + 
                                 " → " + to.getId());
            }
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        Account accountA = new Account(1, 1000);
        Account accountB = new Account(2, 1000);
        
        System.out.println("=== Testing Deadlock-Prone Version ===\n");
        
        Thread t1 = new Thread(() -> {
            transferDeadlockProne(accountA, accountB, 100);
        }, "Transfer A→B");
        
        Thread t2 = new Thread(() -> {
            transferDeadlockProne(accountB, accountA, 100);
        }, "Transfer B→A");
        
        t1.start();
        t2.start();
        
        Thread.sleep(3000);
        
        if (t1.isAlive() || t2.isAlive()) {
            System.out.println("\n☠️ DEADLOCK OCCURRED!");
            t1.interrupt();
            t2.interrupt();
        }
        
        t1.join();
        t2.join();
        
        // Reset
        accountA = new Account(1, 1000);
        accountB = new Account(2, 1000);
        
        System.out.println("\n=== Testing Safe Version ===\n");
        
        Thread t3 = new Thread(() -> {
            transferSafe(accountA, accountB, 100);
        }, "Safe A→B");
        
        Thread t4 = new Thread(() -> {
            transferSafe(accountB, accountA, 100);
        }, "Safe B→A");
        
        t3.start();
        t4.start();
        
        t3.join();
        t4.join();
        
        System.out.println("\n✅ No deadlock with safe version!");
        System.out.println("Account A: " + accountA.getBalance());
        System.out.println("Account B: " + accountB.getBalance());
    }
}
```

### Example 3: Nested Lock Deadlock

```java
/**
 * Demonstrates how nested locks can create subtle deadlocks
 */
public class NestedLockDeadlock {
    private final Object outer = new Object();
    private final Object inner = new Object();
    
    /**
     * Method 1: outer → inner
     */
    public void method1() {
        synchronized (outer) {
            System.out.println("Method1: Locked outer");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            synchronized (inner) {
                System.out.println("Method1: Locked inner");
                // Work
            }
        }
    }
    
    /**
     * Method 2: inner → outer


     */
    public void method2() {
        synchronized (inner) {
            System.out.println("Method2: Locked inner");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            synchronized (outer) {
                System.out.println("Method2: Locked outer");
                // Work
            }
        }
    }
    
    public static void main(String[] args) {
        NestedLockDeadlock example = new NestedLockDeadlock();
        
        Thread t1 = new Thread(() -> example.method1(), "Thread-1");
        Thread t2 = new Thread(() -> example.method2(), "Thread-2");
        
        t1.start();
        t2.start();
        
        // Deadlock will occur!
    }
}
```

**Why This Deadlocks:**

```
Lock Acquisition Order:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
method1(): outer → inner
method2(): inner → outer

Different orders → Circular wait → Deadlock!

Solution: Always use same order (e.g., outer → inner everywhere)
```

---

## 🔍 Deadlock Detection

### Runtime Detection Using ThreadMXBean

Java provides built-in deadlock detection through the [`ThreadMXBean`](https://docs.oracle.com/javase/8/docs/api/java/lang/management/ThreadMXBean.html):

```java
import java.lang.management.*;

/**
 * Programmatic deadlock detection
 */
public class DeadlockDetector {
    
    /**
     * Detects deadlocks in the current JVM
     * @return Array of deadlocked thread IDs, or null if no deadlock
     */
    public static long[] findDeadlockedThreads() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        return threadMXBean.findDeadlockedThreads();
    }
    
    /**
     * Prints detailed information about deadlocked threads
     */
    public static void printDeadlockInfo() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
        
        if (deadlockedThreads == null) {
            System.out.println("✅ No deadlocks detected");
            return;
        }
        
        System.out.println("☠️ DEADLOCK DETECTED!");
        System.out.println("Number of deadlocked threads: " + deadlockedThreads.length);
        System.out.println();
        
        ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(deadlockedThreads);
        
        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println("Thread: " + threadInfo.getThreadName());
            System.out.println("  ID: " + threadInfo.getThreadId());
            System.out.println("  State: " + threadInfo.getThreadState());
            System.out.println("  Blocked on: " + threadInfo.getLockName());
            System.out.println("  Owned by: " + threadInfo.getLockOwnerName());
            System.out.println("  Stack trace:");
            
            for (StackTraceElement element : threadInfo.getStackTrace()) {
                System.out.println("    " + element);
            }
            System.out.println();
        }
    }
    
    /**
     * Monitors for deadlocks in a background thread
     */
    public static void startDeadlockMonitor() {
        Thread monitor = new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    Thread.sleep(5000);  // Check every 5 seconds
                    
                    long[] deadlocked = findDeadlockedThreads();
                    if (deadlocked != null) {
                        System.err.println("\n☠️ DEADLOCK ALERT! " + 
                                         new java.util.Date());
                        printDeadlockInfo();
                        
                        // Could send alert, log to monitoring system, etc.
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "DeadlockMonitor");
        
        monitor.setDaemon(true);
        monitor.start();
    }
    
    // Example usage
    public static void main(String[] args) throws InterruptedException {
        // Start monitoring
        startDeadlockMonitor();
        
        // Create a deadlock
        Object lock1 = new Object();
        Object lock2 = new Object();
        
        Thread t1 = new Thread(() -> {
            synchronized (lock1) {
                try { Thread.sleep(100); } catch (InterruptedException e) { }
                synchronized (lock2) {
                    System.out.println("T1 done");
                }
            }
        });
        
        Thread t2 = new Thread(() -> {
            synchronized (lock2) {
                try { Thread.sleep(100); } catch (InterruptedException e) { }
                synchronized (lock1) {
                    System.out.println("T2 done");
                }
            }
        });
        
        t1.start();
        t2.start();
        
        // Wait for detection
        Thread.sleep(10000);
    }
}
```

### Using jstack for Deadlock Detection

**Command-line tool:**

```bash
# Get process ID
jps

# Get thread dump with deadlock detection
jstack <pid>

# Or send SIGQUIT signal (Unix/Linux)
kill -3 <pid>
```

**Example Output:**

```
Found one Java-level deadlock:
=============================
"Thread-2":
  waiting to lock monitor 0x00007f8b8c004c00 (object 0x000000076d123456, a java.lang.Object),
  which is held by "Thread-1"
"Thread-1":
  waiting to lock monitor 0x00007f8b8c004d00 (object 0x000000076d123789, a java.lang.Object),
  which is held by "Thread-2"

Java stack information for the threads listed above:
===================================================
"Thread-2":
        at DeadlockExample.method2(DeadlockExample.java:45)
        - waiting to lock <0x000000076d123456> (a java.lang.Object)
        - locked <0x000000076d123789> (a java.lang.Object)
```

### Automated Testing for Deadlocks

```java
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit test that detects deadlocks
 */
public class DeadlockTest {
    
    @Test(timeout = 5000)  // Fail if takes > 5 seconds
    public void testNoDeadlock() throws InterruptedException {
        // Test code that should not deadlock
        BankAccount account1 = new BankAccount(1, 1000);
        BankAccount account2 = new BankAccount(2, 1000);
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                account1.transferTo(account2, 10);
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                account2.transferTo(account1, 10);
            }
        });
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        // If we reach here, no deadlock occurred
        assertTrue(true);
    }
    
    @Test
    public void testDeadlockDetection() throws InterruptedException {
        Object lock1 = new Object();
        Object lock2 = new Object();
        
        Thread t1 = new Thread(() -> {
            synchronized (lock1) {
                try { Thread.sleep(100); } catch (InterruptedException e) { }
                synchronized (lock2) { }
            }
        });
        
        Thread t2 = new Thread(() -> {
            synchronized (lock2) {
                try { Thread.sleep(100); } catch (InterruptedException e) { }
                synchronized (lock1) { }
            }
        });
        
        t1.start();
        t2.start();
        
        Thread.sleep(500);  // Give time for deadlock
        
        // Check for deadlock
        long[] deadlocked = DeadlockDetector.findDeadlockedThreads();
        assertNotNull("Expected deadlock to be detected", deadlocked);
        assertEquals(2, deadlocked.length);
        
        // Cleanup
        t1.interrupt();
        t2.interrupt();
    }
}
```

---

## 🛡️ Deadlock Prevention Strategies

### Strategy 1: Lock Ordering

**Principle:** Always acquire locks in a consistent global order.

```java
/**
 * Lock ordering prevents deadlocks by ensuring consistent acquisition order
 */
public class LockOrderingExample {
    
    /**
     * ❌ WRONG: Inconsistent ordering
     */
    static class Inconsistent {
        private final Object lock1 = new Object();
        private final Object lock2 = new Object();
        
        public void method1() {
            synchronized (lock1) {      // lock1 first
                synchronized (lock2) {  // then lock2
                    work();
                }
            }
        }
        
        public void method2() {
            synchronized (lock2) {      // lock2 first ❌
                synchronized (lock1) {  // then lock1
                    work();
                }
            }
        }
        
        private void work() { }
    }
    
    /**
     * ✅ CORRECT: Consistent ordering
     */
    static class Consistent {
        private final Object lock1 = new Object();
        private final Object lock2 = new Object();
        
        public void method1() {
            synchronized (lock1) {      // Always lock1 first
                synchronized (lock2) {  // Always lock2 second
                    work();
                }
            }
        }
        
        public void method2() {
            synchronized (lock1) {      // Always lock1 first ✅
                synchronized (lock2) {  // Always lock2 second
                    work();
                }
            }
        }
        
        private void work() { }
    }
    
    /**
     * ✅ BETTER: Dynamic ordering based on object ID
     */
    static class DynamicOrdering {
        static class Resource {
            private final int id;
            private final Object lock = new Object();
            
            public Resource(int id) {
                this.id = id;
            }
            
            public int getId() { return id; }
            public Object getLock() { return lock; }
        }
        
        public void transfer(Resource from, Resource to) {
            // Always lock lower ID first
            Resource first = from.getId() < to.getId() ? from : to;
            Resource second = from.getId() < to.getId() ? to : from;
            
            synchronized (first.getLock()) {
                synchronized (second.getLock()) {
                    // Transfer logic
                    System.out.println("Transfer: " + from.getId() + " → " + to.getId());
                }
            }
        }
    }
}
```

**Deep Dive: Why Lock Ordering Works**

```
Without Ordering:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Thread 1: L1 → L2
Thread 2: L2 → L1
Result: Can form cycle L1 → L2 → L1 ☠️

With Ordering:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Thread 1: L1 → L2
Thread 2: L1 → L2
Result: No cycle possible ✅

Why? If Thread 2 needs L2, it must first get L1.
But Thread 1 already has L1, so Thread 2 waits.
When Thread 1 releases L1 and L2, Thread 2 can proceed.
Linear ordering, no cycles!
```

### Strategy 2: Lock Timeout

**Principle:** Don't wait indefinitely for locks. Use timeouts to detect potential deadlocks.

```java
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

/**
 * Using lock timeout to avoid indefinite blocking
 */
public class LockTimeoutExample {
    private final ReentrantLock lock1 = new ReentrantLock();
    private final ReentrantLock lock2 = new ReentrantLock();
    
    /**
     * Attempts to acquire both locks with timeout
     * @return true if successful, false if timeout
     */
    public boolean doWorkWithTimeout() {
        boolean acquired1 = false;
        boolean acquired2 = false;
        
        try {
            // Try to acquire lock1 with 1 second timeout
            acquired1 = lock1.tryLock(1, TimeUnit.SECONDS);
            if (!acquired1) {
                System.out.println("Failed to acquire lock1");
                return false;
            }
            
            // Try to acquire lock2 with 1 second timeout
            acquired2 = lock2.tryLock(1, TimeUnit.SECONDS);
            if (!acquired2) {
                System.out.println("Failed to acquire lock2, releasing lock1");
                return false;
            }
            
            // Both locks acquired, do work
            System.out.println("Got both locks, working...");
            performWork();
            return true;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            // Always release locks in finally block
            if (acquired2) {
                lock2.unlock();
            }
            if (acquired1) {
                lock1.unlock();
            }
        }
    }
    
    /**
     * Retry pattern with exponential backoff
     */
    public boolean doWorkWithRetry(int maxRetries) {
        int retries = 0;
        long backoffMillis = 100;
        
        while (retries < maxRetries) {
            if (doWorkWithTimeout()) {
                return true;  // Success
            }
            
            // Failed, wait and retry
            retries++;
            System.out.println("Retry " + retries + " after " + backoffMillis + "ms");
            
            try {
                Thread.sleep(backoffMillis);
                backoffMillis *= 2;  // Exponential backoff
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        
        System.out.println("Failed after " + maxRetries + " retries");
        return false;
    }
    
    private void performWork() {
        // Simulate work
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

**Benefits of Timeout Approach:**
- ✅ Prevents indefinite blocking
- ✅ Allows graceful degradation
- ✅ Can log/alert on repeated failures
- ✅ Enables retry strategies

**Drawbacks:**
- ❌ More complex code
- ❌ May introduce livelock (constant retrying)
- ❌ Choosing right timeout is tricky

### Strategy 3: Try-Lock Pattern

```java
/**
 * Non-blocking lock acquisition
 */
public class TryLockPattern {
    private final ReentrantLock lock1 = new ReentrantLock();
    private final ReentrantLock lock2 = new ReentrantLock();
    
    /**
     * Try to acquire locks without blocking
     */
    public boolean tryDoWork() {
        if (lock1.tryLock()) {  // Non-blocking attempt
            try {
                if (lock2.tryLock()) {  // Non-blocking attempt
                    try {
                        performWork();
                        return true;
                    } finally {
                        lock2.unlock();
                    }
                }
            } finally {
                lock1.unlock();
            }
        }
        
        return false;  // Couldn't acquire locks
    }
    
    /**
     * Spin-wait with tryLock
     */
    public void spinTryLock() {
        while (true) {
            if (tryDoWork()) {
                break;  // Success
            }
            
            // Back off briefly
            Thread.yield();
            
            // Could add termination condition
            if (Thread.interrupted()) {
                break;
            }
        }
    }
    
    private void performWork() {
        System.out.println("Working with both locks");
    }
}
```

### Strategy 4: Single Lock (Coarse-Grained)

**Principle:** Use one lock for all related operations to eliminate multiple lock ordering issues.

```java
/**
 * Single lock approach - simple but less concurrent
 */
public class SingleLockApproach {
    private final Object globalLock = new Object();
    private int resourceA = 0;
    private int resourceB = 0;
    
    public void operation1() {
        synchronized (globalLock) {  // One lock for everything
            resourceA++;
            resourceB--;
        }
    }
    
    public void operation2() {
        synchronized (globalLock) {  // Same lock
            resourceB++;
            resourceA--;
        }
    }
    
    // ✅ No deadlock possible (only one lock!)
    // ❌ But less concurrency (operations can't run in parallel)
}
```

### Strategy 5: Lock-Free Algorithms

**Principle:** Use atomic operations instead of locks.

```java
import java.util.concurrent.atomic.*;

/**
 * Lock-free approach using atomic variables
 */
public class LockFreeApproach {
    private final AtomicInteger resourceA = new AtomicInteger(0);
    private final AtomicInteger resourceB = new AtomicInteger(0);
    
    public void operation1() {
        resourceA.incrementAndGet();  // Atomic, no lock needed
        resourceB.decrementAndGet();
    }
    
    public void operation2() {
        resourceB.incrementAndGet();
        resourceA.decrementAndGet();
    }
    
    // ✅ No locks = No deadlocks!
    // ✅ Better concurrency
    // ❌ But limited to simple operations
}
```

### Comparison of Strategies

| Strategy | Pros | Cons | Use When |
|----------|------|------|----------|
| **Lock Ordering** | Simple, effective | Need to know all locks upfront | Fixed set of locks |
| **Timeout** | Detects potential deadlocks | Complex, may waste CPU | Unknown lock dependencies |
| **Try-Lock** | Non-blocking | Can cause livelock | Quick operations |
| **Single Lock** | Simple, no deadlock | Poor concurrency | Simple cases |
| **Lock-Free** | Best concurrency | Complex, limited scope | Simple atomic operations |

---

## 🍽️ The Dining Philosophers Problem

### Problem Statement

**Classic concurrency problem** introduced by Dijkstra in 1965.

```
The Setup:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
      Phil-0
    🍴      🍴
 Phil-4        Phil-1
    🍴    🍝    🍴
 Phil-3        Phil-2
    🍴      🍴

5 philosophers sit at a round table
5 forks (one between each pair)
To eat, a philosopher needs BOTH adjacent forks
Philosophers alternate: Think → Eat → Think → Eat...

Problem: How to avoid deadlock and starvation?
```

### Naive Solution (Deadlocks!)

```java
/**
 * ❌ Naive solution - WILL DEADLOCK
 */
public class DiningPhilosophersDeadlock {
    
    static class Fork {
        private final int id;
        
        public Fork(int id) {
            this.id = id;
        }
        
        public int getId() {
            return id;
        }
    }
    
    static class Philosopher extends Thread {
        private final int id;
        private final Fork leftFork;
        private final Fork rightFork;
        private int mealsEaten = 0;
        
        public Philosopher(int id, Fork leftFork, Fork rightFork) {
            this.id = id;
            this.leftFork = leftFork;
            this.rightFork = rightFork;
            setName("Philosopher-" + id);
        }
        
        @Override
        public void run() {
            try {
                while (mealsEaten < 3) {
                    think();
                    eat();
                }
                System.out.println(getName() + " finished eating");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        private void think() throws InterruptedException {
            System.out.println(getName() + " is thinking");
            Thread.sleep((long) (Math.random() * 1000));
        }
        
        private void eat() throws InterruptedException {
            // ❌ Deadlock scenario: All grab left fork, then wait for right
            synchronized (leftFork) {
                System.out.println(getName() + " picked up left fork " + 
                                 leftFork.getId());
                
                Thread.sleep(100);  // Simulate reaching for right fork
                
                synchronized (rightFork) {
                    System.out.println(getName() + " picked up right fork " + 
                                     rightFork.getId());
                    System.out.println(getName() + " is eating");
                    Thread.sleep((long) (Math.random() * 1000));
                    mealsEaten++;
                }
            }
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        Fork[] forks = new Fork[5];
        for (int i = 0; i < 5; i++) {
            forks[i] = new Fork(i);
        }
        
        Philosopher[] philosophers = new Philosopher[5];
        for (int i = 0; i < 5; i++) {
            Fork leftFork = forks[i];
            Fork rightFork = forks[(i + 1) % 5];
            philosophers[i] = new Philosopher(i, leftFork, rightFork);
        }
        
        for (Philosopher p : philosophers) {
            p.start();
        }
        
        // Wait a bit
        Thread.sleep(5000);
        
        // Check for deadlock
        boolean allAlive = true;
        for (Philosopher p : philosophers) {
            if (!p.isAlive()) {
                allAlive = false;
                break;
            }
        }
        
        if (allAlive) {
            System.out.println("\n☠️ LIKELY DEADLOCK - All philosophers still alive but no progress");
        }
    }
}
```

**Why It Deadlocks:**

```
Deadlock Scenario:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Time  Phil-0    Phil-1    Phil-2    Phil-3    Phil-4
────────────────────────────────────────────────────────
t0    Grab F0
t1              Grab F1
t2                        Grab F2
t3                                  Grab F3
t4                                            Grab F4
t5    Wait F1   Wait F2   Wait F3   Wait F4   Wait F0
      ⏳        ⏳        ⏳        ⏳        ⏳

Circular wait: P0→F1→P1→F2→P2→F3→P3→F4→P4→F0→P0
☠️ DEADLOCK!
```

### Solution 1: Resource Ordering

```java
/**
 * ✅ Solution 1: Always pick up lower-numbered fork first
 */
public class DiningPhilosophersSolution1 {
    
    static class Philosopher extends Thread {
        private final int id;
        private final Fork firstFork;   // Lower numbered
        private final Fork secondFork;  // Higher numbered
        private int mealsEaten = 0;
        
        public Philosopher(int id, Fork leftFork, Fork rightFork) {
            this.id = id;
            // Always pick lower ID fork first
            if (leftFork.getId() < rightFork.getId()) {
                this.firstFork = leftFork;
                this.secondFork = rightFork;
            } else {
                this.firstFork = rightFork;
                this.secondFork = leftFork;
            }
            setName("Philosopher-" + id);
        }
        
        @Override
        public void run() {
            try {
                while (mealsEaten < 3) {
                    think();
                    eat();
                }
                System.out.println(getName() + " finished eating");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        private void think() throws InterruptedException {
            System.out.println(getName() + " is thinking");
            Thread.sleep((long) (Math.random() * 1000));
        }
        
        private void eat() throws InterruptedException {
            synchronized (firstFork) {  // Lower ID first
                System.out.println(getName() + " picked up fork " + 
                                 firstFork.getId());
                
                synchronized (secondFork) {  // Higher ID second
                    System.out.println(getName() + " picked up fork " + 
                                     secondFork.getId());
                    System.out.println(getName() + " is eating");
                    Thread.sleep((long) (Math.random() * 1000));
                    mealsEaten++;
                    System.out.println(getName() + " put down forks");
                }
            }
        }
    }
    
    // ✅ No circular wait possible!
    // Everyone tries to grab lower-numbered fork first
}
```

### Solution 2: Limit Concurrent Diners

```java
import java.util.concurrent.Semaphore;

/**
 * ✅ Solution 2: Allow only N-1 philosophers to eat simultaneously
 */
public class DiningPhilosophersSolution2 {
    
    static class Philosopher extends Thread {
        private final int id;
        private final Fork leftFork;
        private final Fork rightFork;
        private final Semaphore semaphore;  // Limits concurrent diners
        private int mealsEaten = 0;
        
        public Philosopher(int id, Fork leftFork, Fork rightFork, Semaphore semaphore) {
            this.id = id;
            this.leftFork = leftFork;
            this.rightFork = rightFork;
            this.semaphore = semaphore;
            setName("Philosopher-" + id);
        }
        
        @Override
        public void run() {
            try {
                while (mealsEaten < 3) {
                    think();
                    eat();
                }
                System.out.println(getName() + " finished eating");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        private void think() throws InterruptedException {
            System.out.println(getName() + " is thinking");
            Thread.sleep((long) (Math.random() * 1000));
        }
        
        private void eat() throws InterruptedException {
            // Only 4 out of 5 can try to eat at once
            semaphore.acquire();
            try {
                synchronized (leftFork) {
                    System.out.println(getName() + " picked up left fork");
                    
                    synchronized (rightFork) {
                        System.out.println(getName() + " picked up right fork");
                        System.out.println(getName() + " is eating");
                        Thread.sleep((long) (Math.random() * 1000));
                        mealsEaten++;
                        System.out.println(getName() + " put down forks");
                    }
                }
            } finally {
                semaphore.release();
            }
        }
    }
    
    public static void main(String[] args) {
        Fork[] forks = new Fork[5];
        for (int i = 0; i < 5; i++) {
            forks[i] = new Fork(i);
        }
        
        // Only 4 can dine at once
        Semaphore semaphore = new Semaphore(4);
        
        Philosopher[] philosophers = new Philosopher[5];
        for (int i = 0; i < 5; i++) {
            Fork leftFork = forks[i];
            Fork rightFork = forks[(i + 1) % 5];
            philosophers[i] = new Philosopher(i, leftFork, rightFork, semaphore);
        }
        
        for (Philosopher p : philosophers) {
            p.start();
        }
    }
}
```

**Why This Works:**

```
With 5 philosophers and 5 forks:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
All 5 try to grab left fork → Each holds 1 fork
All need right fork → Circular wait → Deadlock

With semaphore limiting to 4:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Only 4 grab left fork → 4 forks held
1 fork remains free → At least 1 philosopher
can grab both forks → Eats → Releases forks
→ Others can proceed → No deadlock ✅
```

### Solution 3: Asymmetric Solution

```java
/**
 * ✅ Solution 3: One philosopher picks up forks in opposite order
 */
public class DiningPhilosophersSolution3 {
    
    static class Philosopher extends Thread {
        private final int id;
        private final Fork leftFork;
        private final Fork rightFork;
        private final boolean pickLeftFirst;
        private int mealsEaten = 0;
        
        public Philosopher(int id, Fork leftFork, Fork rightFork, boolean pickLeftFirst) {
            this.id = id;
            this.leftFork = leftFork;
            this.rightFork = rightFork;
            this.pickLeftFirst = pickLeftFirst;
            setName("Philosopher-" + id);
        }
        
        @Override
        public void run() {
            try {
                while (mealsEaten < 3) {
                    think();
                    eat();
                }
                System.out.println(getName() + " finished eating");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }


        
        private void think() throws InterruptedException {
            System.out.println(getName() + " is thinking");
            Thread.sleep((long) (Math.random() * 1000));
        }
        
        private void eat() throws InterruptedException {
            if (pickLeftFirst) {
                synchronized (leftFork) {
                    synchronized (rightFork) {
                        performEating();
                    }
                }
            } else {
                // Pick right first!
                synchronized (rightFork) {
                    synchronized (leftFork) {
                        performEating();
                    }
                }
            }
        }
        
        private void performEating() {
            System.out.println(getName() + " is eating");
            try {
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            mealsEaten++;
        }
    }
    
    public static void main(String[] args) {
        Fork[] forks = new Fork[5];
        for (int i = 0; i < 5; i++) {
            forks[i] = new Fork(i);
        }
        
        Philosopher[] philosophers = new Philosopher[5];
        for (int i = 0; i < 5; i++) {
            Fork leftFork = forks[i];
            Fork rightFork = forks[(i + 1) % 5];
            // Last philosopher picks up in opposite order
            boolean pickLeftFirst = (i != 4);
            philosophers[i] = new Philosopher(i, leftFork, rightFork, pickLeftFirst);
        }
        
        for (Philosopher p : philosophers) {
            p.start();
        }
    }
}
```

**Comparison of Solutions:**

| Solution | Pros | Cons |
|----------|------|------|
| **Resource Ordering** | Simple, efficient | Need to assign IDs |
| **Limit Diners** | Fair distribution | Reduced concurrency |
| **Asymmetric** | Simple to implement | Less elegant |

---

## 📊 Resource Allocation Graphs

**Resource Allocation Graphs (RAG)** provide a visual way to detect deadlocks.

### Graph Components

```
Components of RAG:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Process (P):     ⭕ Circle
Resource (R):    ▢ Square
Request Edge:    P → R (Process requests resource)
Assignment Edge: R → P (Resource assigned to process)
```

### Example: No Deadlock

```
         R1                R2
         ▢                 ▢
         ↓                 ↓
         P1 ───request───> R2
         ⭕                ▢
         
P1 has R1, requests R2
No circular wait → No deadlock
```

### Example: Deadlock

```
         P1 ←─── R1
         ⭕      ▢
          ↓      ↑
          R2 ──→ P2
          ▢      ⭕
          
P1 has R1, wants R2
P2 has R2, wants R1
Cycle detected → DEADLOCK!
```

### Java Implementation

```java
import java.util.*;

/**
 * Resource Allocation Graph for deadlock detection
 */
public class ResourceAllocationGraph {
    
    static class Node {
        String id;
        boolean isResource;
        
        Node(String id, boolean isResource) {
            this.id = id;
            this.isResource = isResource;
        }
    }
    
    private Map<String, Node> nodes = new HashMap<>();
    private Map<String, Set<String>> edges = new HashMap<>();
    
    public void addProcess(String processId) {
        nodes.put(processId, new Node(processId, false));
        edges.putIfAbsent(processId, new HashSet<>());
    }
    
    public void addResource(String resourceId) {
        nodes.put(resourceId, new Node(resourceId, true));
        edges.putIfAbsent(resourceId, new HashSet<>());
    }
    
    public void addRequestEdge(String processId, String resourceId) {
        edges.get(processId).add(resourceId);
    }
    
    public void addAssignmentEdge(String resourceId, String processId) {
        edges.get(resourceId).add(processId);
    }
    
    /**
     * Detects cycles using DFS
     */
    public boolean hasCycle() {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();
        
        for (String node : nodes.keySet()) {
            if (detectCycle(node, visited, recursionStack)) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean detectCycle(String node, Set<String> visited, Set<String> recursionStack) {
        if (recursionStack.contains(node)) {
            return true;  // Cycle found!
        }
        
        if (visited.contains(node)) {
            return false;
        }
        
        visited.add(node);
        recursionStack.add(node);
        
        Set<String> neighbors = edges.get(node);
        if (neighbors != null) {
            for (String neighbor : neighbors) {
                if (detectCycle(neighbor, visited, recursionStack)) {
                    return true;
                }
            }
        }
        
        recursionStack.remove(node);
        return false;
    }
    
    public void printGraph() {
        System.out.println("Resource Allocation Graph:");
        for (Map.Entry<String, Set<String>> entry : edges.entrySet()) {
            String from = entry.getKey();
            for (String to : entry.getValue()) {
                System.out.println("  " + from + " → " + to);
            }
        }
        
        if (hasCycle()) {
            System.out.println("☠️ DEADLOCK DETECTED (Cycle exists)");
        } else {
            System.out.println("✅ No deadlock (No cycle)");
        }
    }
    
    // Example usage
    public static void main(String[] args) {
        ResourceAllocationGraph rag = new ResourceAllocationGraph();
        
        // Add processes and resources
        rag.addProcess("P1");
        rag.addProcess("P2");
        rag.addResource("R1");
        rag.addResource("R2");
        
        // P1 holds R1, requests R2
        rag.addAssignmentEdge("R1", "P1");
        rag.addRequestEdge("P1", "R2");
        
        // P2 holds R2, requests R1
        rag.addAssignmentEdge("R2", "P2");
        rag.addRequestEdge("P2", "R1");
        
        rag.printGraph();
    }
}
```

---

## 🏦 Banker's Algorithm

**Banker's Algorithm** (by Dijkstra) determines if resource allocation is safe - prevents deadlock before it happens.

### Concept

```
Bank Analogy:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Bank has $100 total
Customer A needs max $80, currently has $40
Customer B needs max $60, currently has $30
Customer C needs max $50, currently has $20

Allocated: $90, Available: $10

Question: Should bank lend more?

Safe sequence check:
1. Can any customer finish with available resources?
2. If yes, that customer returns their resources
3. Repeat until all finish or none can finish

If all can finish in some order → SAFE
If stuck → UNSAFE (potential deadlock)
```

### Implementation

```java
/**
 * Banker's Algorithm for deadlock avoidance
 */
public class BankersAlgorithm {
    private int numProcesses;
    private int numResources;
    
    // Maximum demand of each process
    private int[][] max;
    
    // Currently allocated resources
    private int[][] allocation;
    
    // Available resources
    private int[] available;
    
    // Need = Max - Allocation
    private int[][] need;
    
    public BankersAlgorithm(int numProcesses, int numResources) {
        this.numProcesses = numProcesses;
        this.numResources = numResources;
        
        max = new int[numProcesses][numResources];
        allocation = new int[numProcesses][numResources];
        available = new int[numResources];
        need = new int[numProcesses][numResources];
    }
    
    public void setMax(int process, int[] maxDemand) {
        max[process] = maxDemand.clone();
        calculateNeed();
    }
    
    public void setAllocation(int process, int[] allocated) {
        allocation[process] = allocated.clone();
        calculateNeed();
    }
    
    public void setAvailable(int[] avail) {
        available = avail.clone();
    }
    
    private void calculateNeed() {
        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numResources; j++) {
                need[i][j] = max[i][j] - allocation[i][j];
            }
        }
    }
    
    /**
     * Checks if system is in safe state
     */
    public boolean isSafe() {
        int[] work = available.clone();
        boolean[] finish = new boolean[numProcesses];
        List<Integer> safeSequence = new ArrayList<>();
        
        // Find a safe sequence
        while (safeSequence.size() < numProcesses) {
            boolean found = false;
            
            for (int i = 0; i < numProcesses; i++) {
                if (!finish[i] && canAllocate(i, work)) {
                    // Process i can finish
                    for (int j = 0; j < numResources; j++) {
                        work[j] += allocation[i][j];
                    }
                    finish[i] = true;
                    safeSequence.add(i);
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                // No process can proceed - unsafe!
                System.out.println("❌ UNSAFE STATE - No safe sequence exists");
                return false;
            }
        }
        
        System.out.println("✅ SAFE STATE");
        System.out.print("Safe sequence: ");
        for (int i = 0; i < safeSequence.size(); i++) {
            System.out.print("P" + safeSequence.get(i));
            if (i < safeSequence.size() - 1) System.out.print(" → ");
        }
        System.out.println();
        
        return true;
    }
    
    private boolean canAllocate(int process, int[] work) {
        for (int j = 0; j < numResources; j++) {
            if (need[process][j] > work[j]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Request resources for a process
     */
    public boolean requestResources(int process, int[] request) {
        // Check if request <= need
        for (int i = 0; i < numResources; i++) {
            if (request[i] > need[process][i]) {
                System.out.println("Error: Request exceeds maximum claim");
                return false;
            }
        }
        
        // Check if request <= available
        for (int i = 0; i < numResources; i++) {
            if (request[i] > available[i]) {
                System.out.println("Process must wait - resources not available");
                return false;
            }
        }
        
        // Pretend to allocate
        for (int i = 0; i < numResources; i++) {
            available[i] -= request[i];
            allocation[process][i] += request[i];
            need[process][i] -= request[i];
        }
        
        // Check if safe
        if (isSafe()) {
            System.out.println("✅ Request granted");
            return true;
        } else {
            // Rollback
            System.out.println("❌ Request denied - would lead to unsafe state");
            for (int i = 0; i < numResources; i++) {
                available[i] += request[i];
                allocation[process][i] -= request[i];
                need[process][i] += request[i];
            }
            return false;
        }
    }
    
    public void printState() {
        System.out.println("\nCurrent State:");
        System.out.println("Available: " + Arrays.toString(available));
        System.out.println("\nProcess\tAllocation\tMax\t\tNeed");
        for (int i = 0; i < numProcesses; i++) {
            System.out.print("P" + i + "\t");
            System.out.print(Arrays.toString(allocation[i]) + "\t");
            System.out.print(Arrays.toString(max[i]) + "\t");
            System.out.println(Arrays.toString(need[i]));
        }
    }
    
    // Example usage
    public static void main(String[] args) {
        // 5 processes, 3 resource types (A, B, C)
        BankersAlgorithm banker = new BankersAlgorithm(5, 3);
        
        // Set maximum demand for each process
        banker.setMax(0, new int[]{7, 5, 3});
        banker.setMax(1, new int[]{3, 2, 2});
        banker.setMax(2, new int[]{9, 0, 2});
        banker.setMax(3, new int[]{2, 2, 2});
        banker.setMax(4, new int[]{4, 3, 3});
        
        // Set current allocation
        banker.setAllocation(0, new int[]{0, 1, 0});
        banker.setAllocation(1, new int[]{2, 0, 0});
        banker.setAllocation(2, new int[]{3, 0, 2});
        banker.setAllocation(3, new int[]{2, 1, 1});
        banker.setAllocation(4, new int[]{0, 0, 2});
        
        // Set available resources
        banker.setAvailable(new int[]{3, 3, 2});
        
        banker.printState();
        banker.isSafe();
        
        // Try to request resources
        System.out.println("\nP1 requests [1, 0, 2]:");
        banker.requestResources(1, new int[]{1, 0, 2});
    }
}
```

---

## 🐌 Liveness Problems

Beyond deadlocks, there are other liveness issues that prevent progress.

### 1. Starvation

**Definition:** A thread is perpetually denied access to resources it needs.

```java
/**
 * Starvation example: Low-priority threads never execute
 */
public class StarvationExample {
    private final Object lock = new Object();
    
    static class Worker extends Thread {
        private final Object lock;
        private final int priority;
        private int workDone = 0;
        
        public Worker(Object lock, int priority, String name) {
            super(name);
            this.lock = lock;
            this.priority = priority;
            setPriority(priority);
        }
        
        @Override
        public void run() {
            while (workDone < 10) {
                synchronized (lock) {
                    workDone++;
                    System.out.println(getName() + " (priority " + priority + 
                                     "): work done = " + workDone);
                }
                Thread.yield();  // Give others a chance
            }
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        
        // Create threads with different priorities
        Worker high1 = new Worker(lock, Thread.MAX_PRIORITY, "High-1");
        Worker high2 = new Worker(lock, Thread.MAX_PRIORITY, "High-2");
        Worker low = new Worker(lock, Thread.MIN_PRIORITY, "Low");
        
        low.start();
        Thread.sleep(100);  // Let low start first
        high1.start();
        high2.start();
        
        high1.join();
        high2.join();
        low.join();
        
        // Low priority thread likely starved!
    }
}
```

**Solutions to Starvation:**

1. **Fair Locks**
```java
ReentrantLock fairLock = new ReentrantLock(true);  // Fair mode
// Threads acquire lock in FIFO order
```

2. **Priority Inheritance**
```java
// When high-priority thread waits on low-priority thread
// Temporarily boost low-priority thread's priority
```

3. **Aging**
```java
// Gradually increase priority of waiting threads
```

### 2. Livelock

**Definition:** Threads are active but make no progress (like two people trying to pass in a hallway).

```java
/**
 * Livelock example: Threads keep giving up resources for each other
 */
public class LivelockExample {
    
    static class Spoon {
        private Diner owner;
        
        public synchronized void setOwner(Diner d) {
            owner = d;
        }
        
        public synchronized Diner getOwner() {
            return owner;
        }
        
        public synchronized void use() {
            System.out.printf("%s is eating!\n", owner.name);
        }
    }
    
    static class Diner {
        private String name;
        private boolean isHungry;
        
        public Diner(String name) {
            this.name = name;
            this.isHungry = true;
        }
        
        public void eatWith(Spoon spoon, Diner spouse) {
            while (isHungry) {
                // Don't have the spoon
                if (spoon.getOwner() != this) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        continue;
                    }
                    continue;
                }
                
                // If spouse is hungry, be polite and let them eat first
                if (spouse.isHungry) {
                    System.out.printf("%s: You eat first, dear %s!\n", name, spouse.name);
                    spoon.setOwner(spouse);
                    continue;  // ❌ LIVELOCK: Both keep giving up!
                }
                
                // Eat
                spoon.use();
                isHungry = false;
                System.out.printf("%s: I'm done eating\n", name);
                spoon.setOwner(spouse);
            }
        }
    }
    
    public static void main(String[] args) {
        final Diner husband = new Diner("Husband");
        final Diner wife = new Diner("Wife");
        
        final Spoon spoon = new Spoon();
        spoon.setOwner(husband);
        
        Thread t1 = new Thread(() -> husband.eatWith(spoon, wife));
        Thread t2 = new Thread(() -> wife.eatWith(spoon, husband));
        
        t1.start();
        t2.start();
        
        // Will run forever - livelock!
    }
}
```

**Solution to Livelock:**

```java
/**
 * Solution: Add randomness/backoff
 */
if (spouse.isHungry) {
    System.out.printf("%s: You eat first, dear %s!\n", name, spouse.name);
    spoon.setOwner(spouse);
    
    // Add random delay to break symmetry
    try {
        Thread.sleep((long) (Math.random() * 100));
    } catch (InterruptedException e) { }
    
    continue;
}
```

### 3. Priority Inversion

**Definition:** High-priority thread waits for low-priority thread.

```
Scenario:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Low-priority thread (L) holds lock
High-priority thread (H) needs lock
Medium-priority thread (M) preempts L

Result: H waits for L, but L can't run because M is running!
H is effectively at mercy of M's priority

Solution: Priority Inheritance
When H waits on L's lock, temporarily boost L to H's priority
```

---

## 🔐 ReentrantLock Deep Dive

[`ReentrantLock`](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/locks/ReentrantLock.html) is a more flexible alternative to `synchronized`.

### Basic Usage

```java
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockBasics {
    private final ReentrantLock lock = new ReentrantLock();
    private int count = 0;
    
    public void increment() {
        lock.lock();  // Acquire lock
        try {
            count++;  // Critical section
        } finally {
            lock.unlock();  // ⚠️ ALWAYS unlock in finally!
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

**⚠️ Critical Rule:** ALWAYS unlock in `finally` block!

### ReentrantLock vs synchronized

| Feature | synchronized | ReentrantLock |
|---------|--------------|---------------|
| **Syntax** | Implicit | Explicit |
| **Fairness** | No | Yes (optional) |
| **Try Lock** | No | Yes |
| **Timed Lock** | No | Yes |
| **Interruptible** | No | Yes |
| **Condition Variables** | 1 (wait/notify) | Multiple |
| **Lock Status Query** | No | Yes |
| **Performance** | Similar | Similar |
| **Flexibility** | Low | High |

### Reentrancy Explained

```java
public class ReentrancyDemo {
    private final ReentrantLock lock = new ReentrantLock();
    
    public void outer() {
        lock.lock();
        try {
            System.out.println("Outer method, hold count: " + lock.getHoldCount());
            inner();  // Can re-acquire!
        } finally {
            lock.unlock();
        }
    }
    
    public void inner() {
        lock.lock();
        try {
            System.out.println("Inner method, hold count: " + lock.getHoldCount());
            // Same thread re-acquired lock
        } finally {
            lock.unlock();
        }
    }
    
    public static void main(String[] args) {
        ReentrancyDemo demo = new ReentrantLockDemo();
        demo.outer();
    }
}
```

**Output:**
```
Outer method, hold count: 1
Inner method, hold count: 2
```

**How Reentrancy Works:**

```
Lock State:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
{
  owner: Thread-1
  holdCount: 0
}

Thread-1 calls outer():
  lock.lock()  → holdCount: 1  ✅
  
  Thread-1 calls inner():
    lock.lock()  → holdCount: 2  ✅ (Same thread!)
    
    lock.unlock() → holdCount: 1
    
  lock.unlock() → holdCount: 0 (fully released)
}
```

### Fair vs Unfair Locks

```java
// Unfair lock (default) - better performance
ReentrantLock unfairLock = new ReentrantLock();

// Fair lock - prevents starvation
ReentrantLock fairLock = new ReentrantLock(true);
```

**Fair Lock:** Grants access in FIFO order (first-come, first-served)
**Unfair Lock:** Allows "barging" - new thread can jump the queue

```
Unfair Lock Performance:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Queue: [T1, T2, T3]
Lock released
T4 arrives and "barges in" → Gets lock immediately
Pro: Better throughput (no context switch to T1)
Con: T1 might wait longer (potential starvation)

Fair Lock:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Queue: [T1, T2, T3]
Lock released
T1 gets lock (waited longest)
Pro: No starvation, predictable
Con: Lower throughput (more context switches)
```

### tryLock() - Non-blocking Acquisition

```java
public class TryLockExample {
    private final ReentrantLock lock = new ReentrantLock();
    
    /**
     * Try to acquire lock without blocking
     */
    public boolean tryDoWork() {
        if (lock.tryLock()) {  // Returns immediately
            try {
                performWork();
                return true;
            } finally {
                lock.unlock();
            }
        }
        
        return false;  // Couldn't acquire
    }
    
    /**
     * Try with timeout
     */
    public boolean tryDoWorkWithTimeout() {
        try {
            if (lock.tryLock(2, TimeUnit.SECONDS)) {
                try {
                    performWork();
                    return true;
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return false;
    }
    
    private void performWork() {
        System.out.println("Working...");
    }
}
```

### Condition Variables

Multiple condition variables allow fine-grained coordination:

```java
import java.util.concurrent.locks.*;

public class BoundedBufferWithConditions {
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    
    private final Object[] items = new Object[100];
    private int putIndex, takeIndex, count;
    
    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length) {
                notFull.await();  // Wait on notFull condition
            }
            
            items[putIndex] = x;
            if (++putIndex == items.length) putIndex = 0;
            count++;
            
            notEmpty.signal();  // Signal notEmpty condition
        } finally {
            lock.unlock();
        }
    }
    
    public Object take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await();  // Wait on notEmpty condition
            }
            
            Object x = items[takeIndex];
            if (++takeIndex == items.length) takeIndex = 0;
            count--;
            
            notFull.signal();  // Signal notFull condition
            return x;
        } finally {
            lock.unlock();
        }
    }
}
```

**vs synchronized wait/notify:**

```java
// synchronized: ONE condition
synchronized (lock) {
    while (!condition) {
        lock.wait();  // Single wait queue
    }
    lock.notifyAll();  // Wakes ALL
}

// ReentrantLock: MULTIPLE conditions
lock.lock();
try {
    while (!condition1) {
        condition1Var.await();  // Separate wait queue
    }
    condition2Var.signal();  // Wakes specific condition
} finally {
    lock.unlock();
}
```

### Interruptible Lock Acquisition

```java
public class InterruptibleLocking {
    private final ReentrantLock lock = new ReentrantLock();
    
    public void doWork() throws InterruptedException {
        lock.lockInterruptibly();  // Can be interrupted while waiting
        try {
            performWork();
        } finally {
            lock.unlock();
        }
    }
    
    private void performWork() {
        // Work
    }
}
```

**Use case:** Allow threads to be cancelled while waiting for locks

---

## 🎯 Advanced Lock Patterns

### 1. Read-Write Lock

[`ReadWriteLock`](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/locks/ReadWriteLock.html) allows multiple readers OR one writer.

```java
import java.util.concurrent.locks.*;

public class ReadWriteLockExample {
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();
    
    private int value = 0;
    
    /**
     * Multiple threads can read simultaneously
     */
    public int read() {
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " reading: " + value);
            return value;
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * Only one thread can write
     */
    public void write(int newValue) {
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " writing: " + newValue);
            value = newValue;
        } finally {
            writeLock.unlock();
        }
    }
}
```

**Performance Benefits:**

```
Regular Lock:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Read → 🔒 Lock → Read (sequential)
Read → ⏳ Wait
Read → ⏳ Wait

ReadWriteLock:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Read → 📖 Read Lock → All concurrent!
Read → 📖 Read Lock
Read → 📖 Read Lock

Write → 🔒 Write Lock → Exclusive
```

### 2. StampedLock (Java 8+)

Optimistic

 locking for better performance.

```java
import java.util.concurrent.locks.StampedLock;

public class StampedLockExample {
    private final StampedLock lock = new StampedLock();
    private double x, y;
    
    /**
     * Optimistic read - no locking initially
     */
    public double distanceFromOrigin() {
        long stamp = lock.tryOptimisticRead();  // Optimistic attempt
        double currentX = x;
        double currentY = y;
        
        if (!lock.validate(stamp)) {  // Check if modified
            stamp = lock.readLock();  // Fall back to read lock
            try {
                currentX = x;
                currentY = y;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }
    
    public void move(double deltaX, double deltaY) {
        long stamp = lock.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            lock.unlockWrite(stamp);
        }
    }
}
```

---

## 📚 Summary & Key Takeaways

### Deadlock Essentials

**Four Coffman Conditions (ALL must be present):**
1. ✅ Mutual Exclusion - Resources held exclusively
2. ✅ Hold and Wait - Holding while waiting
3. ✅ No Preemption - Can't forcibly take resources
4. ✅ Circular Wait - Cyclic dependency chain

**Break ANY ONE → No deadlock possible!**

### Prevention Strategies

| Strategy | Implementation | Effectiveness |
|----------|---------------|---------------|
| **Lock Ordering** | Consistent acquisition order | ⭐⭐⭐⭐⭐ |
| **Timeout** | tryLock with timeout | ⭐⭐⭐⭐ |
| **Banker's Algorithm** | Safe state checking | ⭐⭐⭐ |
| **Single Lock** | One global lock | ⭐⭐ |
| **Lock-Free** | Atomic operations | ⭐⭐⭐⭐⭐ |

### Liveness Problems

- **Deadlock**: Circular wait, no progress
- **Livelock**: Active but no progress  
- **Starvation**: Perpetually denied resources
- **Priority Inversion**: High-priority waits on low-priority

### ReentrantLock Advantages

✅ **Fairness**: Optional FIFO ordering  
✅ **Trylock**: Non-blocking acquisition  
✅ **Timeout**: Time-limited waiting  
✅ **Interruptible**: Can be cancelled  
✅ **Conditions**: Multiple wait conditions  
✅ **Monitoring**: Lock status queries  

### Best Practices Checklist

- [ ] Use consistent lock ordering
- [ ] Keep critical sections small
- [ ] Always use try-finally with explicit locks
- [ ] Consider ReentrantLock for advanced needs
- [ ] Use fair locks to prevent starvation
- [ ] Monitor for deadlocks in production
- [ ] Test concurrent code under load
- [ ] Document locking strategy
- [ ] Prefer immutable objects when possible
- [ ] Use lock-free algorithms where applicable

---

## 🎓 Next Steps

Continue your concurrent programming journey:

1. **[Thread Interruption](./03-thread-interruption.md)** - Graceful cancellation
2. **Java Concurrency Utilities** - ExecutorService, thread pools
3. **CompletableFuture** - Async programming
4. **Reactive Programming** - Event-driven systems

---

**Congratulations!** 🎉 You now understand advanced synchronization challenges and how to prevent deadlocks in production systems.

*"In concurrent systems, it's not enough for something to work once. It must work reliably under all possible interleavings."*

**Happy Concurrent Programming!** 🔒✨
