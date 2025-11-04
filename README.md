# Java Learning Project

## Course: Java Multithreading for Senior Engineering Interviews

### Project Structure
```
.
├── src/
│   ├── Main.java  # Sample starter file
│   └── educative/
│       └── course/
│           └── multithreading/
│               └── introduction/          # Introduction Topic
│                   ├── BasicThread.java            # Thread creation methods
│                   ├── ThreadLifecycle.java        # Thread states
│                   ├── ThreadSynchronization.java  # Race conditions & sync
│                   └── ThreadJoinExample.java      # Thread coordination (FIXED)
└── README.md
```

## How to Run Examples

### Running Introduction Topic Examples:

```bash
# Basic Thread Creation
javac src/educative/course/multithreading/introduction/BasicThread.java
java -cp src educative.course.multithreading.example.introduction.BasicThread

# Thread Lifecycle
javac src/educative/course/multithreading/introduction/ThreadLifecycle.java
java -cp src educative.course.multithreading.example.introduction.ThreadLifecycle

# Thread Synchronization
javac src/educative/course/multithreading/introduction/ThreadSynchronization.java
java -cp src educative.course.multithreading.example.introduction.ThreadSynchronization

# Thread Join Example (Fixed compilation error)
javac src/educative/course/multithreading/introduction/ThreadJoinExample.java
java -cp src educative.course.multithreading.introduction.ThreadJoinExample
```

## Introduction Topic Coverage

1. **BasicThread.java** - Three ways to create threads:
   - Extending Thread class
   - Implementing Runnable interface
   - Using Lambda expressions

2. **ThreadLifecycle.java** - Thread states:
   - NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED

3. **ThreadSynchronization.java** - Concurrency issues:
   - Race conditions
   - Synchronized methods
   - Thread safety

4. **ThreadJoinExample.java** - Thread coordination:
   - Using join() to wait for threads
   - Join with timeout (FIXED: join() returns void, use isAlive() to check)

## Fixed Issues

✅ **ThreadJoinExample compilation error fixed**: 
- Problem: `join(timeout)` returns void, not boolean
- Solution: Use `thread.isAlive()` after join to check if thread completed within timeout

Ready for Java multithreading interviews! 🚀
