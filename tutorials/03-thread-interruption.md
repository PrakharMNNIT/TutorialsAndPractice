
# 🛑 Thread Interruption in Java - Complete Guide

> Master cooperative thread cancellation and graceful shutdown patterns

## 📋 Table of Contents

1. [Introduction: Why Interruption?](#-introduction-why-interruption)
2. [The Problem with Thread.stop()](#-the-problem-with-threadstop)
3. [The Interruption Mechanism](#-the-interruption-mechanism)
4. [Understanding Interrupted Status](#-understanding-interrupted-status)
5. [InterruptedException Deep Dive](#-interruptedexception-deep-dive)
6. [Interruption Patterns](#-interruption-patterns)
7. [Handling Non-Interruptible Operations](#-handling-non-interruptible-operations)
8. [Shutdown Strategies](#-shutdown-strategies)
9. [Cancellation Frameworks](#-cancellation-frameworks)
10. [Production Best Practices](#-production-best-practices)
11. [Debugging Interruption](#-debugging-interruption)
12. [Complete Real-World Examples](#-complete-real-world-examples)

---

## 🎯 Introduction: Why Interruption?

### The Challenge of Stopping Threads

**Question:** How do you stop a running thread?

In single-threaded programs, you just exit. But with threads, we need **cooperative cancellation** - the thread must participate in its own termination.

```
Single-threaded:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
main() {
    doWork();
    return;  // Done!
}

Multi-threaded:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Thread 1: doWork();  // Still running
Thread 2: doWork();  // Still running
Main: How do I stop them? 🤔
```

**Real-World Analogy:**

Think of a restaurant at closing time:

```
❌ Bad Approach (Thread.stop()):
Manager: "Restaurant closed!" *turns off all lights*
Chefs: *Crash! Food falls, stove still on, chaos!*

✅ Good Approach (Interruption):
Manager: "Please finish up, we're closing soon"
Chefs: *Finish current dish, turn off equipment, clean up*
Manager: *Everyone leaves safely*
```

### Historical Context

**Early Java (pre-1.2):**
- [`Thread.stop()`](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html#stop--) - Forcibly kills thread
- [`Thread.suspend()`](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html#suspend--) - Pauses thread
- [`Thread.resume()`](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html#resume--) - Resumes thread

**Problems:**
- Data corruption
- Deadlocks
- Resource leaks
- Inconsistent state

**Java 1.2+ Solution:**
- Cooperative interruption mechanism
- Thread checks its own status
- Cleans up before exiting

---

## ☠️ The Problem with Thread.stop()

### Why Thread.stop() Was Deprecated

[`Thread.stop()`](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html#stop--) forcibly terminates a thread, causing severe problems.

```java
/**
 * Demonstrates why Thread.stop() is dangerous
 * 
 * @deprecated Never use this in production!
 */
public class ThreadStopDanger {
    
    static class BankAccount {
        private int balance = 1000;
        private List<String> transactionLog = new ArrayList<>();
        
        /**
         * Transfer money - should be atomic
         */
        public synchronized void transfer(int amount) {
            balance -= amount;
            
            // ⚠️ If Thread.stop() called here:
            //    balance updated but log not updated!
            //    INCONSISTENT STATE!
            
            try {
                Thread.sleep(100);  // Simulate processing
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            
            transactionLog.add("Transferred: " + amount);
        }
        
        public synchronized void printState() {
            System.out.println("Balance: " + balance);
            System.out.println("Transactions: " + transactionLog.size());
        }
    }
    
    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccount();
        
        Thread worker = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                account.transfer(100);
            }
        });
        
        worker.start();
        Thread.sleep(150);  // Let it do some work
        
        // ❌ DANGEROUS: Force stop
        worker.stop();  // Deprecated!
        
        Thread.sleep(100);
        account.printState();
        
        // Output might show:
        // Balance: 700 (3 transfers)
        // Transactions: 2 (only 2 logged!)
        // ☠️ INCONSISTENT!
    }
}
```

**What Goes Wrong:**

```
Thread State When stop() Called:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
synchronized (lock) {
    balance -= 100;        // ✅ Done
    // → stop() called here ←
    log.add("Transfer");   // ❌ Never executed
    unlock();              // ❌ Lock never released!
}

Consequences:
1. Data corruption (balance vs log mismatch)
2. Locks held forever (deadlock risk)
3. Resources not closed (file handles, connections)
4. Invariants violated (object in inconsistent state)
```

### Detailed Problems

**1. Lock Release**

```java
public class LockProblem {
    private final Object lock = new Object();
    
    @SuppressWarnings("deprecation")
    public void危险Method() {
        synchronized (lock) {
            // Complex operation
            doWork();
            // If stop() called, lock NEVER released!
            // All other threads waiting on this lock: DEADLOCKED
        }
    }
}
```

**2. Finally Blocks Not Executed**

```java
public class FinallyProblem {
    @SuppressWarnings("deprecation")
    public void危险Method() {
        FileWriter writer = null;
        try {
            writer = new FileWriter("data.txt");
            writer.write("Important data");
            // stop() called here
        } finally {
            // ❌ NEVER EXECUTED!
            // File not closed, data not flushed
            if (writer != null) {
                writer.close();  // Never happens
            }
        }
    }
}
```

**3. Object Invariants Violated**

```java
public class InvariantProblem {
    private int[] data;
    private int size;
    
    // Invariant: data.length >= size
    
    @SuppressWarnings("deprecation")
    public void resize(int newSize) {
        data = new int[newSize];  // ✅ Updated
        // stop() called here
        size = newSize;           // ❌ Never executed
        // Invariant violated! data.length != size
    }
}
```

### The Java Documentation Warning

From official Java docs:

> **Deprecated.**  
> This method is inherently unsafe. Stopping a thread causes it to unlock all the monitors that it has locked. (The monitors are unlocked as the `ThreadDeath` exception propagates up the stack.) If any of the objects previously protected by these monitors were in an inconsistent state, other threads may now view these objects in an inconsistent state. Such objects are said to be damaged.

**Translation:**
```
stop() throws ThreadDeath exception
→ Bypasses normal exception handling
→ Releases ALL locks immediately
→ Leaves objects in inconsistent state
→ Other threads see corrupted data
→ System becomes unreliable
```

---

## 🔧 The Interruption Mechanism

### What is Interruption?

**Interruption is Java's cooperative cancellation mechanism.** It's a polite request, not a command.

```
Interruption Flow:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Thread A                      Thread B (working)
   │                              │
   │ interrupt()                  │ Check status?
   ├──────────────────────────────►│ isInterrupted()
   │                              │ → true
   │                              │
   │                              ├─ Cleanup
   │                              ├─ Release resources
   │                              └─ Exit gracefully
   │
   └───────────────────────── Wait for B to finish
```

**Real-World Analogy:**

```
Boss to Employee:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Thread.stop():
"Stop immediately!" → Employee drops everything, chaos

Interruption:
"Please wrap up when convenient" 
→ Employee finishes current task
→ Saves work
→ Logs out
→ Leaves cleanly
```

### How Interruption Works at JVM Level

**Internal Implementation:**

```
Thread Object in JVM:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
class Thread {
    // Interrupt status flag (stored in native thread structure)
    private volatile boolean interrupted = false;
    
    // Native OS thread handle
    private long nativeThread;
    
    // Wait state tracking
    private Object waitObject;
}

When interrupt() called:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
1. Set interrupted flag = true (atomic operation)
2. If thread in WAITING/TIMED_WAITING:
   - Send OS signal to native thread
   - Throw InterruptedException
   - Clear interrupted flag
3. If thread in BLOCKED:
   - Flag remains set
   - Thread unblocked when lock acquired
```

### The Three Key Methods

Java provides three methods to work with interruption:

```java
public class InterruptionMethods {
    
    /**
     * 1. interrupt() - Sets the interrupted flag
     * Called by another thread to request interruption
     */
    public void method1() {
        Thread worker = new Thread(() -> {
            // Worker code
        });
        worker.start();
        
        // Request interruption
        worker.interrupt();  // Sets worker's interrupted flag
    }
    
    /**
     * 2. isInterrupted() - Checks flag without clearing
     * Instance method, doesn't clear the flag
     */
    public void method2() {
        Thread worker = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                // Keep working while not interrupted
                doWork();
            }
            // Flag remains set
        });
    }
    
    /**
     * 3. interrupted() - Checks and clears flag
     * Static method, clears the flag after checking
     */
    public void method3() {
        Thread worker = new Thread(() -> {
            while (true) {
                if (Thread.interrupted()) {  // Checks and clears
                    System.out.println("Interrupted!");
                    break;  // Flag now cleared
                }
                doWork();
            }
        });
    }
    
    private void doWork() {
        // Simulate work
    }
}
```

**Method Comparison:**

| Method | Static? | Clears Flag? | Target | Use Case |
|--------|---------|--------------|--------|----------|
| [`interrupt()`](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html#interrupt--) | No | N/A | Any thread | Request cancellation |
| [`isInterrupted()`](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html#isInterrupted--) | No | No | Specific thread | Check status |
| [`interrupted()`](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html#interrupted--) | Yes | Yes | Current thread | Check and clear |

### Basic Example

```java
/**
 * Simple interruption example
 */
public class BasicInterruption {
    public static void main(String[] args) throws InterruptedException {
        Thread worker = new Thread(() -> {
            System.out.println("Worker started");
            int count = 0;
            
            // Work while not interrupted
            while (!Thread.currentThread().isInterrupted()) {
                count++;
                if (count % 100_000_000 == 0) {
                    System.out.println("Working... count = " + count);
                }
            }
            
            System.out.println("Worker stopping gracefully. Final count: " + count);
        }, "Worker-Thread");
        
        worker.start();
        
        // Let it work for 2 seconds
        Thread.sleep(2000);
        
        // Request interruption
        System.out.println("Main: Requesting worker to stop...");
        worker.interrupt();
        
        // Wait for graceful shutdown
        worker.join();
        
        System.out.println("Main: Worker stopped");
    }
}
```

**Output:**
```
Worker started
Working... count = 100000000
Working... count = 200000000
Working... count = 300000000
Main: Requesting worker to stop...
Worker stopping gracefully. Final count = 347892156
Main: Worker stopped
```

**Timeline:**

```
Time    Main Thread              Worker Thread
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
t0      worker.start()           Started
t1                               isInterrupted() = false
t2                               Working...
t3      interrupt()              isInterrupted() = true
t4                               Exit loop
t5                               Cleanup
t6      worker.join()            Terminated
t7      Complete
```

---

## 📊 Understanding Interrupted Status

### The Interrupted Flag

Every thread has a boolean interrupted status flag.

```
Thread's Interrupted Status:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
┌─────────────────────────────┐
│  Thread Object              │
│  ┌───────────────────────┐  │
│  │ interrupted: false    │  │ ← Initial state
│  └───────────────────────┘  │
└─────────────────────────────┘

After interrupt() called:
┌─────────────────────────────┐
│  Thread Object              │
│  ┌───────────────────────┐  │
│  │ interrupted: true     │  │ ← Flag set
│  └───────────────────────┘  │
└─────────────────────────────┘

After interrupted() called:
┌─────────────────────────────┐
│  Thread Object              │
│  ┌───────────────────────┐  │
│  │ interrupted: false    │  │ ← Flag cleared
│  └───────────────────────┘  │
└─────────────────────────────┘
```

### Flag Behavior Examples

```java
/**
 * Demonstrates interrupted flag behavior
 */
public class InterruptedFlagBehavior {
    
    public static void main(String[] args) throws InterruptedException {
        
        // Example 1: isInterrupted() doesn't clear
        Thread t1 = new Thread(() -> {
            Thread current = Thread.currentThread();
            
            System.out.println("1. Initial: " + current.isInterrupted());  // false
            
            // Simulate being interrupted
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // Restore flag
            }
            
            System.out.println("2. After interrupt: " + current.isInterrupted());  // true
            System.out.println("3. Check again: " + current.isInterrupted());      // still true
            System.out.println("4. Still true: " + current.isInterrupted());       // still true
        });
        
        t1.start();
        Thread.sleep(50);
        t1.interrupt();
        t1.join();
        
        System.out.println();
        
        // Example 2: interrupted() clears flag
        Thread t2 = new Thread(() -> {
            Thread current = Thread.currentThread();
            
            System.out.println("1. Initial: " + Thread.interrupted());  // false
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // Restore flag
            }
            
            System.out.println("2. After interrupt: " + Thread.interrupted());  // true, cleared
            System.out.println("3. Check again: " + Thread.interrupted());      // false!
        });
        
        t2.start();
        Thread.sleep(50);
        t2.interrupt();
        t2.join();
    }
}
```

**Output:**
```
1. Initial: false
2. After interrupt: true
3. Check again: true
4. Still true: true

1. Initial: false
2. After interrupt: true
3. Check again: false
```

### When to Use Which Method

```java
public class WhenToUseWhich {
    
    /**
     * Use isInterrupted() when you want to check multiple times
     */
    public void multipleChecks() {
        Thread worker = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                doWork();
                
                if (Thread.currentThread().isInterrupted()) {
                    cleanup();  // Can check again
                    break;
                }
            }
        });
    }
    
    /**
     * Use interrupted() when you want to clear and respond once
     */
    public void clearAndRespond() {
        Thread worker = new Thread(() -> {
            while (true) {
                if (Thread.interrupted()) {  // Check and clear
                    System.out.println("Interrupted, exiting");
                    return;  // Flag is now clear
                }
                doWork();
            }
        });
    }
    
    /**
     * Use interrupt() to request cancellation
     */
    public void requestCancellation(Thread thread) {
        thread.interrupt();
    }
    
    private void doWork() { }
    private void cleanup() { }
}
```

---

## ⚠️ InterruptedException Deep Dive

### What is InterruptedException?

[`InterruptedException`](https://docs.oracle.com/javase/8/docs/api/java/lang/InterruptedException.html) is thrown when a thread is interrupted while in a blocking operation.

**Methods that throw InterruptedException:**

```java
// Sleeping
Thread.sleep(1000);

// Waiting
object.wait();
object.wait(timeout);

// Joining
thread.join();
thread.join(timeout);

// Blocking queue operations
queue.take();
queue.put(item);

// Lock operations
lock.lockInterruptibly();

// And many more...
```

### The Exception Mechanism

```
Normal Execution:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Thread.sleep(5000)
  ↓
Thread enters TIMED_WAITING state
  ↓
Wait 5 seconds
  ↓
Return normally

With Interruption:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Thread.sleep(5000)
  ↓
Thread enters TIMED_WAITING state
  ↓
Another thread calls interrupt()
  ↓
JVM: Wake up sleeping thread
      Clear interrupted flag  ← IMPORTANT!
      Throw InterruptedException
  ↓
catch (InterruptedException e)
  ↓
Handle interruption
```

**Critical Point:** InterruptedException **clears the interrupted flag!**

```java
/**
 * Demonstrates that InterruptedException clears the flag
 */
public class ExceptionClearsFlag {
    public static void main(String[] args) throws InterruptedException {
        Thread worker = new Thread(() -> {
            try {
                System.out.println("Before sleep, interrupted: " + 
                    Thread.currentThread().isInterrupted());  // false
                
                Thread.sleep(5000);
                
            } catch (InterruptedException e) {
                // ⚠️ Flag is CLEARED when exception thrown!
                System.out.println("In catch, interrupted: " + 
                    Thread.currentThread().isInterrupted());  // false!
                
                // ✅ Must restore if needed
                Thread.currentThread().interrupt();
                
                System.out.println("After restore, interrupted: " + 
                    Thread.currentThread().isInterrupted());  // true
            }
        });
        
        worker.start();
        Thread.sleep(100);  // Let it start sleeping
        worker.interrupt();
        worker.join();
    }
}
```

**Output:**
```
Before sleep, interrupted: false
In catch, interrupted: false
After restore, interrupted: true
```

### Three Ways to Handle InterruptedException

#### 1. Propagate the Exception (Preferred)

```java
/**
 * ✅ BEST: Let caller handle interruption
 */
public void doWork() throws InterruptedException {
    Thread.sleep(1000);
    processData();
    Thread.sleep(1000);
    // Exception propagates to caller
}
```

**Benefits:**
- ✅ Caller decides how to handle
- ✅ Interruption signal preserved
- ✅ Clean separation of concerns

#### 2. Restore Interrupted Status

```java
/**
 * ✅ GOOD: Can't propagate, so restore flag
 */
public void doWork() {
    try {
        Thread.sleep(1000);
        processData();
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();  // ✅ Restore flag
        // Log, cleanup, etc.
        logger.warn("Task interrupted", e);
    }
}
```

**Use when:**
- Implementing `Runnable` (can't throw checked exceptions)
- Method signature doesn't allow checked exceptions
- Need to do cleanup before exiting

#### 3. Catch and Continue (Rarely Appropriate)

```java
/**
 * ⚠️ USE SPARINGLY: Only if interruption truly doesn't matter
 */
public void doWork() {
    while (hasMoreWork()) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // Interruption doesn't affect this operation
            logger.info("Sleep interrupted, continuing");
        }
        processNextItem();
    }
}
```

**Only use when:**
- Interruption genuinely doesn't affect correctness
- Operation must complete regardless
- Very rare in practice

### Common Mistakes

```java
public class InterruptionMistakes {
    
    /**
     * ❌ WRONG: Swallowing exception
     */
    public void mistake1() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Ignored - interruption is lost!
        }
    }
    
    /**
     * ❌ WRONG: Just logging
     */
    public void mistake2() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();  // Prints but doesn't restore flag
        }
    }
    
    /**
     * ❌ WRONG: Catching generic Exception
     */
    public void mistake3() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {  // Too broad!
            // InterruptedException needs special handling
        }
    }
    
    /**
     * ✅ CORRECT: Restore and handle
     */
    public void correct() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Restore
            // Then handle appropriately
            cleanup();
            return;
        }
    }
    
    private void cleanup() { }
}
```

---

## 🎨 Interruption Patterns

### Pattern 1: Polling Loop

For CPU-bound tasks without blocking operations.

```java
/**
 * Pattern 1: Regular status checking
 */
public class PollingPattern implements Runnable {
    
    @Override
    public void run() {
        System.out.println("Starting CPU-intensive task");
        
        // Check interruption periodically
        while (!Thread.currentThread().isInterrupted()) {
            performComputationChunk();
            
            // Optional: Check more frequently for long computations
            if (Thread.interrupted()) {
                System.out.println("Interrupted during computation");
                break;
            }
        }
        
        cleanup();
        System.out.println("Task completed");
    }
    
    private void performComputationChunk() {
        // Do some CPU-intensive work
        for (int i = 0; i < 1_000_000; i++) {
            Math.sqrt(i);
        }
    }
    
    private void cleanup() {
        System.out.println("Cleaning up resources");
    }
    
    public static void main(String[] args) throws InterruptedException {
        Thread worker = new Thread(new PollingPattern());
        worker.start();
        
        Thread.sleep(2000);  // Let it work
        worker.interrupt();  // Request stop
        worker.join();       // Wait for completion
    }
}
```

### Pattern 2: Blocking Operations

For I/O-bound or waiting operations.

```java
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Pattern 2: Handling blocking operations
 */
public class BlockingPattern implements Runnable {
    private final BlockingQueue<Task> queue;
    
    public BlockingPattern(BlockingQueue<Task> queue) {
        this.queue = queue;
    }
    
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Blocking operation - will throw InterruptedException
                Task task = queue.take();
                task.execute();
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted while waiting for task");
            Thread.currentThread().interrupt();  // Restore status
        } finally {
            cleanup();
        }
    }
    
    private void cleanup() {
        System.out.println("Worker shutting down");
    }
    
    static class Task {
        void execute() {
            System.out.println("Executing task");
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Task> queue = new LinkedBlockingQueue<>();
        Thread worker = new Thread(new BlockingPattern(queue));
        worker.start();
        
        // Add some tasks
        for (int i = 0; i < 5; i++) {
            queue.put(new Task());
        }
        
        Thread.sleep(1000);
        worker.interrupt();  // Stop worker
        worker.join();
    }
}
```

### Pattern 3: Mixed Operations

Combines CPU work with blocking operations.

```java
/**
 * Pattern 3: CPU + I/O operations
 */
public class MixedPattern implements Runnable {
    
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // CPU-bound work
                performComputation();
                
                // Check interruption
                if (Thread.interrupted()) {
                    throw new InterruptedException("Interrupted during computation");
                }
                
                // I/O-bound work (can throw InterruptedException)
                Thread.sleep(100);
                
                // More work
                processResults();
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted, cleaning up");
            Thread.currentThread().interrupt();
        } finally {
            cleanup();
        }
    }
    
    private void performComputation() {
        // CPU work
    }
    
    private void processResults() {
        // Process results
    }
    
    private void cleanup() {
        // Cleanup
    }
}
```

### Pattern 4: Cancellable Task with Result

Returns result or indicates cancellation.

```java
import java.util.concurrent.*;

/**
 * Pattern 4: Task with result or cancellation
 */
public class CancellableTask implements Callable<String> {
    
    @Override
    public String call() throws InterruptedException {
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < 10; i++) {
            // Check cancellation
            if (Thread.interrupted()) {
                throw new InterruptedException("Task cancelled at iteration " + i);
            }
            
            // Do work
            result.append("Step ").append(i).append(" completed\n");
            Thread.sleep(500);
        }
        
        return result.toString();
    }
    
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        
        Future<String> future = executor.submit(new CancellableTask());
        
        try {
            // Wait 2 seconds, then cancel
            Thread.sleep(2000);
            
            System.out.println("Cancelling task...");
            future.cancel(true);  // Interrupt the task
            
            // Try to get result
            String result = future.get();
            System.out.println(result);
            
        } catch (CancellationException e) {
            System.out.println("Task was cancelled");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
```

### Pattern 5: Timeout with Interruption

Combines timeout with cancellation.

```java
/**
 * Pattern 5: Time-limited operation with cancellation
 */
public class TimeoutPattern {
    
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        
        Future<String> future = executor.submit(() -> {
            

try {
                for (int i = 0; i < 100; i++) {
                    Thread.sleep(100);
                    if (Thread.interrupted()) {
                        return "Interrupted at " + i;
                    }
                }
                return "Completed all 100 iterations";
            } catch (InterruptedException e) {
                return "InterruptedException at iteration";
            }
        });
        
        try {
            // Wait maximum 3 seconds
            String result = future.get(3, TimeUnit.SECONDS);
            System.out.println("Result: " + result);
        } catch (TimeoutException e) {
            System.out.println("Timeout! Cancelling task...");
            future.cancel(true);  // Interrupt the task
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
```

---

## 🚫 Handling Non-Interruptible Operations

Some operations don't respond to interruption. Here's how to handle them.

### Problem: Non-Interruptible I/O

```java
import java.io.*;
import java.net.*;

/**
 * Problem: Socket I/O doesn't respond to interruption
 */
public class NonInterruptibleIO {
    
    /**
     * ❌ WRONG: read() blocks indefinitely, doesn't respond to interrupt
     */
    public void problematicRead() throws IOException {
        Socket socket = new Socket("example.com", 80);
        InputStream in = socket.getInputStream();
        
        // This blocks and WON'T respond to interruption!
        int data = in.read();
        
        // Thread.interrupt() has no effect on read()
    }
    
    /**
     * ✅ SOLUTION 1: Close the socket from another thread
     */
    public void solutionCloseSocket() throws IOException {
        Socket socket = new Socket("example.com", 80);
        InputStream in = socket.getInputStream();
        
        Thread reader = new Thread(() -> {
            try {
                int data = in.read();
                // Process data
            } catch (SocketException e) {
                // Socket closed, handle gracefully
                System.out.println("Socket closed, exiting");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
        reader.start();
        
        // From another thread:
        socket.close();  // This unblocks read()
    }
    
    /**
     * ✅ SOLUTION 2: Use NIO with interruptible channels
     */
    public void solutionNIO() throws IOException {
        SocketChannel channel = SocketChannel.open(
            new InetSocketAddress("example.com", 80));
        
        Thread reader = new Thread(() -> {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                // This IS interruptible!
                channel.read(buffer);
            } catch (ClosedByInterruptException e) {
                System.out.println("Channel read interrupted");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
        reader.start();
        
        // Interruption works!
        reader.interrupt();
    }
}
```

### Solution Patterns

```java
import java.io.*;
import java.nio.channels.*;

/**
 * Comprehensive solutions for non-interruptible operations
 */
public class InterruptibleIOPatterns {
    
    /**
     * Pattern 1: Wrap with timeout
     */
    public static String readWithTimeout(InputStream in, long timeoutMs) 
            throws IOException, InterruptedException {
        
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        long deadline = System.currentTimeMillis() + timeoutMs;
        
        while (System.currentTimeMillis() < deadline) {
            if (in.available() > 0) {
                int data = in.read();
                if (data == -1) break;
                buffer.write(data);
            }
            
            if (Thread.interrupted()) {
                throw new InterruptedException("Read interrupted");
            }
            
            Thread.sleep(10);  // Polling interval
        }
        
        return buffer.toString();
    }
    
    /**
     * Pattern 2: Use NIO interruptible channels
     */
    public static void interruptibleFileRead(String filename) throws IOException {
        try (FileChannel channel = FileChannel.open(
                Paths.get(filename), StandardOpenOption.READ)) {
            
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            
            // This respects interruption!
            while (channel.read(buffer) > 0) {
                buffer.flip();
                // Process buffer
                buffer.clear();
            }
            
        } catch (ClosedByInterruptException e) {
            System.out.println("File read interrupted");
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Pattern 3: Close resource from outside
     */
    static class InterruptibleReader {
        private volatile Socket socket;
        
        public void read() {
            try {
                socket = new Socket("example.com", 80);
                InputStream in = socket.getInputStream();
                
                while (!Thread.interrupted()) {
                    int data = in.read();
                    if (data == -1) break;
                    processData(data);
                }
            } catch (IOException e) {
                if (Thread.interrupted()) {
                    System.out.println("Interrupted during I/O");
                }
            }
        }
        
        public void cancel() {
            try {
                if (socket != null) {
                    socket.close();  // Unblocks read()
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        private void processData(int data) { }
    }
}
```

---

## 🔚 Shutdown Strategies

### Pattern 1: Simple Flag

```java
/**
 * Simple shutdown with volatile flag
 */
public class SimpleFlagShutdown implements Runnable {
    private volatile boolean running = true;
    
    @Override
    public void run() {
        while (running) {
            doWork();
        }
        cleanup();
    }
    
    public void shutdown() {
        running = false;
    }
    
    private void doWork() {
        // Work that doesn't block
    }
    
    private void cleanup() {
        System.out.println("Cleaning up");
    }
}
```

### Pattern 2: Interruption + Flag

```java
/**
 * Combined interruption and flag for robustness
 */
public class RobustShutdown implements Runnable {
    private volatile boolean shutdown = false;
    
    @Override
    public void run() {
        try {
            while (!shutdown && !Thread.interrupted()) {
                try {
                    doWork();
                    Thread.sleep(100);  // Interruptible
                } catch (InterruptedException e) {
                    // Interrupted during sleep
                    break;
                }
            }
        } finally {
            cleanup();
        }
    }
    
    public void shutdown() {
        shutdown = true;
    }
    
    private void doWork() { }
    private void cleanup() { }
}
```

### Pattern 3: Graceful Shutdown with Timeout

```java
/**
 * Graceful shutdown with forced termination fallback
 */
public class GracefulShutdown {
    private final ExecutorService executor;
    
    public GracefulShutdown() {
        this.executor = Executors.newFixedThreadPool(4);
    }
    
    /**
     * Attempts graceful shutdown, then forces if needed
     */
    public void shutdown() {
        System.out.println("Initiating shutdown...");
        
        // 1. Stop accepting new tasks
        executor.shutdown();
        
        try {
            // 2. Wait for existing tasks to complete (graceful)
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                System.out.println("Timeout elapsed, forcing shutdown...");
                
                // 3. Cancel running tasks (send interrupt)
                executor.shutdownNow();
                
                // 4. Wait a bit more
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    System.err.println("Executor did not terminate");
                }
            }
            
            System.out.println("Shutdown complete");
            
        } catch (InterruptedException e) {
            System.err.println("Shutdown interrupted");
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        GracefulShutdown service = new GracefulShutdown();
        
        // Submit some tasks
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            service.executor.submit(() -> {
                try {
                    System.out.println("Task " + taskId + " starting");
                    Thread.sleep(5000);
                    System.out.println("Task " + taskId + " complete");
                } catch (InterruptedException e) {
                    System.out.println("Task " + taskId + " interrupted");
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        // Wait a bit
        Thread.sleep(2000);
        
        // Initiate shutdown
        service.shutdown();
    }
}
```

### Pattern 4: Shutdown Hook

```java
/**
 * Registers shutdown hook for JVM termination
 */
public class ShutdownHookPattern {
    private final ExecutorService executor;
    private volatile boolean running = true;
    
    public ShutdownHookPattern() {
        this.executor = Executors.newFixedThreadPool(4);
        
        // Register shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown hook triggered");
            shutdown();
        }));
    }
    
    public void start() {
        executor.submit(() -> {
            while (running) {
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
    }
    
    private void shutdown() {
        running = false;
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        System.out.println("Shutdown complete");
    }
    
    private void doWork() {
        System.out.println("Working...");
    }
    
    public static void main(String[] args) throws InterruptedException {
        ShutdownHookPattern service = new ShutdownHookPattern();
        service.start();
        
        // Simulate some work
        Thread.sleep(5000);
        
        // Graceful shutdown
        service.shutdown();
        
        // Or JVM termination triggers hook:
        // System.exit(0);
    }
}
```

---

## 📚 Complete Real-World Examples

### Example 1: Background File Processor

```java
import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;

/**
 * Monitors directory and processes new files
 * Supports graceful cancellation
 */
public class FileProcessor implements Runnable {
    private final Path watchDirectory;
    private final BlockingQueue<Path> fileQueue;
    private volatile boolean running = true;
    
    public FileProcessor(Path directory) {
        this.watchDirectory = directory;
        this.fileQueue = new LinkedBlockingQueue<>();
    }
    
    @Override
    public void run() {
        Thread.currentThread().setName("FileProcessor");
        
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            watchDirectory.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
            
            System.out.println("Monitoring: " + watchDirectory);
            
            while (running && !Thread.interrupted()) {
                WatchKey key;
                try {
                    // Wait for events (interruptible)
                    key = watcher.poll(1, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    System.out.println("Watch interrupted");
                    break;
                }
                
                if (key == null) continue;
                
                for (WatchEvent<?> event : key.pollEvents()) {
                    Path file = watchDirectory.resolve((Path) event.context());
                    fileQueue.offer(file);
                    processFile(file);
                }
                
                key.reset();
            }
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            cleanup();
        }
    }
    
    private void processFile(Path file) {
        if (!running) return;
        
        try {
            System.out.println("Processing: " + file);
            
            // Check interruption before expensive operation
            if (Thread.interrupted()) {
                throw new InterruptedException("Processing interrupted");
            }
            
            // Simulate file processing
            Thread.sleep(2000);
            
            System.out.println("Completed: " + file);
            
        } catch (InterruptedException e) {
            System.out.println("File processing interrupted: " + file);
            Thread.currentThread().interrupt();
        }
    }
    
    public void shutdown() {
        System.out.println("Shutdown requested");
        running = false;
    }
    
    private void cleanup() {
        System.out.println("Cleaning up file processor");
        // Close resources, save state, etc.
    }
    
    public static void main(String[] args) throws Exception {
        Path tempDir = Files.createTempDirectory("fileprocessor");
        System.out.println("Created temp directory: " + tempDir);
        
        FileProcessor processor = new FileProcessor(tempDir);
        Thread thread = new Thread(processor);
        thread.start();
        
        // Create some test files
        for (int i = 0; i < 5; i++) {
            Thread.sleep(1000);
            Files.createFile(tempDir.resolve("file" + i + ".txt"));
        }
        
        // Let it process
        Thread.sleep(3000);
        
        // Request shutdown
        processor.shutdown();
        thread.interrupt();
        thread.join();
        
        System.out.println("Main complete");
    }
}
```

### Example 2: Network Client with Timeout

```java
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

/**
 * HTTP client with connection timeout and cancellation
 */
public class InterruptibleHttpClient {
    
    public static class HttpRequest {
        final String url;
        final int timeoutSeconds;
        
        public HttpRequest(String url, int timeoutSeconds) {
            this.url = url;
            this.timeoutSeconds = timeoutSeconds;
        }
    }
    
    public static class HttpResponse {
        final int statusCode;
        final String body;
        final boolean cancelled;
        
        public HttpResponse(int statusCode, String body, boolean cancelled) {
            this.statusCode = statusCode;
            this.body = body;
            this.cancelled = cancelled;
        }
    }
    
    /**
     * Makes HTTP request with timeout and cancellation support
     */
    public static HttpResponse makeRequest(HttpRequest request) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        
        Future<HttpResponse> future = executor.submit(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(request.url);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                
                // Check cancellation
                if (Thread.interrupted()) {
                    return new HttpResponse(0, null, true);
                }
                
                int statusCode = connection.getResponseCode();
                
                // Check cancellation before reading
                if (Thread.interrupted()) {
                    return new HttpResponse(statusCode, null, true);
                }
                
                // Read response
                StringBuilder response = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()))) {
                    
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (Thread.interrupted()) {
                            return new HttpResponse(statusCode, 
                                response.toString(), true);
                        }
                        response.append(line).append("\n");
                    }
                }
                
                return new HttpResponse(statusCode, response.toString(), false);
                
            } catch (IOException e) {
                System.err.println("Request failed: " + e.getMessage());
                return new HttpResponse(0, null, false);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
        
        try {
            // Wait with timeout
            return future.get(request.timeoutSeconds, TimeUnit.SECONDS);
            
        } catch (TimeoutException e) {
            System.out.println("Request timed out, cancelling...");
            future.cancel(true);
            return new HttpResponse(0, null, true);
            
        } catch (InterruptedException e) {
            System.out.println("Request interrupted");
            future.cancel(true);
            Thread.currentThread().interrupt();
            return new HttpResponse(0, null, true);
            
        } catch (ExecutionException e) {
            System.err.println("Request execution failed: " + e.getCause());
            return new HttpResponse(0, null, false);
            
        } finally {
            executor.shutdown();
        }
    }
    
    public static void main(String[] args) {
        // Example usage
        HttpRequest request = new HttpRequest("https://httpbin.org/delay/10", 3);
        
        System.out.println("Making request with 3 second timeout...");
        HttpResponse response = makeRequest(request);
        
        if (response.cancelled) {
            System.out.println("Request was cancelled");
        } else {
            System.out.println("Status: " + response.statusCode);
            System.out.println("Body length: " + 
                (response.body != null ? response.body.length() : 0));
        }
    }
}
```

### Example 3: Producer-Consumer with Shutdown

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Producer-Consumer pattern with graceful shutdown
 */
public class ProducerConsumerShutdown {
    
    private final BlockingQueue<Task> queue;
    private final ExecutorService producerExecutor;
    private final ExecutorService consumerExecutor;
    private final AtomicInteger producedCount = new AtomicInteger(0);
    private final AtomicInteger consumedCount = new AtomicInteger(0);
    private volatile boolean shutdown = false;
    
    public ProducerConsumerShutdown(int queueSize, int numProducers, int numConsumers) {
        this.queue = new LinkedBlockingQueue<>(queueSize);
        this.producerExecutor = Executors.newFixedThreadPool(numProducers);
        this.consumerExecutor = Executors.newFixedThreadPool(numConsumers);
    }
    
    static class Task {
        final int id;
        final String data;
        
        Task(int id, String data) {
            this.id = id;
            this.data = data;
        }
    }
    
    class Producer implements Runnable {
        private final int producerId;
        
        Producer(int id) {
            this.producerId = id;
        }
        
        @Override
        public void run() {
            try {
                while (!shutdown && !Thread.interrupted()) {
                    int taskId = producedCount.incrementAndGet();
                    Task task = new Task(taskId, "Data-" + taskId);
                    
                    // Put with timeout to allow shutdown
                    boolean added = queue.offer(task, 1, TimeUnit.SECONDS);
                    if (added) {
                        System.out.println("Producer-" + producerId + 
                            " produced: Task-" + taskId);
                    }
                    
                    Thread.sleep(100);  // Simulate work
                }
            } catch (InterruptedException e) {
                System.out.println("Producer-" + producerId + " interrupted");
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("Producer-" + producerId + " shutting down");
            }
        }
    }
    
    class Consumer implements Runnable {
        private final int consumerId;
        
        Consumer(int id) {
            this.consumerId = id;
        }
        
        @Override
        public void run() {
            try {
                while (!shutdown && !Thread.interrupted()) {
                    // Poll with timeout to allow shutdown
                    Task task = queue.poll(1, TimeUnit.SECONDS);
                    
                    if (task != null) {
                        processTask(task);
                        consumedCount.incrementAndGet();
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Consumer-" + consumerId + " interrupted");
                Thread.currentThread().interrupt();
            } finally {
                // Process remaining tasks
                System.out.println("Consumer-" + consumerId + 
                    " processing remaining tasks...");
                Task task;
                while ((task = queue.poll()) != null) {
                    try {
                        processTask(task);
                        consumedCount.incrementAndGet();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                System.out.println("Consumer-" + consumerId + " shutting down");
            }
        }
        
        private void processTask(Task task) throws InterruptedException {
            System.out.println("Consumer-" + consumerId + 
                " processing: Task-" + task.id);
            Thread.sleep(200);  // Simulate work
        }
    }
    
    public void start(int numProducers, int numConsumers) {
        for (int i = 0; i < numProducers; i++) {
            producerExecutor.submit(new Producer(i));
        }
        
        for (int i = 0; i < numConsumers; i++) {
            consumerExecutor.submit(new Consumer(i));
        }
    }
    
    public void shutdown() {
        System.out.println("\n=== Initiating Shutdown ===");
        shutdown = true;
        
        // Shutdown producers first
        producerExecutor.shutdown();
        try {
            if (!producerExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                producerExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            producerExecutor.shutdownNow();
        }
        
        // Then shutdown consumers (after queue drains)
        consumerExecutor.shutdown();
        try {
            if (!consumerExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                consumerExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            consumerExecutor.shutdownNow();
        }
        
        System.out.println("=== Shutdown Complete ===");
        System.out.println("Produced: " + producedCount.get());
        System.out.println("Consumed: " + consumedCount.get());
        System.out.println("Remaining in queue: " + queue.size());
    }
    
    public static void main(String[] args) throws InterruptedException {
        ProducerConsumerShutdown system = new ProducerConsumerShutdown(10, 2, 3);
        
        system.start(2, 3);
        
        // Run for 5 seconds
        Thread.sleep(5000);
        
        // Graceful shutdown
        system.shutdown();
    }
}
```

---

## ✅ Production Best Practices

### 1. Always Restore Interrupted Status

```java
// ✅ CORRECT Pattern
public void doWork() {
    try {
        blockingOperation();
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();  // Always restore!
        cleanup();
        return;
    }
}
```

### 2. Document Interruption Behavior

```java
/**
 * Processes data from queue until interrupted.
 * 
 * <p>This method responds to interruption by:
 * <ul>
 *   <li>Stopping immediately when interrupted during queue.take()</li>
 *   <li>Completing current item if interrupted during processing</li>
 *   <li>Saving partial results before exiting</li>
 * </ul>
 * 
 * @throws InterruptedException if interrupted while waiting for data
 */
public void processData() throws InterruptedException {
    // Implementation
}
```

### 3. Test Interruption Handling

```java
import org.junit.*;
import static org.junit.Assert.*;

public class InterruptionTest {
    
    @Test(timeout = 5000)
    public void testInterruption() throws InterruptedException {
        AtomicBoolean wasInterrupted = new AtomicBoolean(false);
        
        Thread worker = new Thread(() -> {
            try {
                while (!Thread.interrupted()) {
                    Thread.sleep(100);
                }
                wasInterrupted.set(true);
            } catch (InterruptedException e) {
                wasInterrupted.set(true);
                Thread.currentThread().interrupt();
            }
        });
        
        worker.start();
        Thread.sleep(500);
        worker.interrupt();
        worker.join(2000);
        
        assertTrue("Thread should have been interrupted", wasInterrupted.get());
        assertFalse("Thread should have terminated", worker.isAlive());
    }
}
```

### 4. Use Timeouts with Interruption

```java
public boolean doWorkWithTimeout(long timeoutMs) throws InterruptedException {
    long deadline = System.currentTimeMillis() + timeoutMs;
    
    while (!Thread.interrupted()) {
        if (System.currentTimeMillis() > deadline) {
            return false;  // Timeout
        }
        
        if (tryProcessNextItem()) {
            return true;  // Success
        }
        
        Thread.sleep(10);  // Brief pause (interruptible)
    }
    
    throw new InterruptedException("Interrupted before completion");
}
```

### 5. Graceful Degradation

```java
public class GracefulDegradation {
    public void processWithFallback() {
        try {
            primaryProcessing();
        } catch (InterruptedException e) {
            // Save partial results
            savePartialResults();
            
            // Notify monitoring
            notifyIncompleteProcessing();
            
            // Restore status
            Thread.currentThread().interrupt();
        }
    }
    
    private void primaryProcessing() throws InterruptedException {
        // Main work
    }
    
    private void savePartialResults() {
        // Save what we've done so far
    }
    
    private void notifyIncompleteProcessing() {
        // Alert monitoring system
    }
}
```

---

## 📊 Summary & Key Takeaways

### Core Concepts

| Concept | Key Points |
|---------|------------|
| **Interruption** | Cooperative cancellation mechanism |
| **Interrupted Flag** | Boolean status on each thread |
| **InterruptedException** | Thrown when interrupted while blocking |
| **Restoration** | Must restore flag if can't propagate exception |

### Three Key Methods

```java
thread.interrupt();                      // Request interruption
Thread.interrupted();                     // Check and clear (static)
Thread.currentThread().isInterrupted();  // Check without clearing
```

### Best Practices Checklist

- [ ] Always restore interrupted status if catching `InterruptedException`
- [ ] Use `try-finally` for cleanup
- [ ] Respond promptly to interruption requests
- [ ] Document interruption behavior in javadoc
- [ ] Test interruption handling
- [ ] Use timeouts combined with interruption
- [ ] Never swallow `InterruptedException`
- [ ] Handle non-interruptible operations appropriately
- [ ] Implement graceful shutdown
- [ ] Use shutdown hooks for JVM termination

### Common Mistakes

❌ **DON'T:**
- Ignore `InterruptedException`
- Swallow exceptions without restoring flag
- Use `Thread.stop()` (deprecated!)
- Forget `finally` blocks for cleanup
- Assume interruption works for all operations

✅ **DO:**
- Propagate or restore interrupted status
- Check interruption in loops
- Use interruptible I/O (NIO) when possible
- Implement timeout with interruption
- Test under interruption conditions

### Decision Tree

```
Need to stop a thread?
    │
    ├─ Blocking operations present?
    │   └─ YES → Use interruption (interrupt())
    │
    ├─ Pure CPU-bound loop?
    │   └─ Use interruption or volatile flag
    │
    ├─ Non-interruptible I/O?
    │   └─ Use NIO or close resource externally
    │
    └─ Complex shutdown sequence?
        └─ Combine flag + interruption + timeout
```

---

## 🎓 Congratulations!

You've completed the comprehensive guide to Java multithreading!

**You now understand:**
- ✅ Thread fundamentals and lifecycle
- ✅ Synchronization and race conditions  
- ✅ Deadlocks and prevention strategies
- ✅ Cooperative thread cancellation
- ✅ Production-ready patterns

**Next Steps:**
1. Java Concurrency Utilities (`java.util.concurrent`)
2. Thread Pools and ExecutorService
3. CompletableFuture and async programming
4. Reactive programming patterns

---

**Remember:** *"Concurrency is not about going fast, it's about managing complexity while avoiding chaos."*

**Happy Concurrent Programming!** 🧵🛑✨
