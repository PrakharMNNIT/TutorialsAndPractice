_# 🔧 Service Workers - Offline & Caching Strategies

[← Previous: Web Workers](./40_web_workers.md) | [← Back to Main](../README.md) | [Next: JS vs Java Concurrency →](./42_js_vs_java_concurrency.md)

---

## 📝 Overview

Service Workers are a special type of Web Worker that act as network proxies, enabling offline functionality, caching strategies, and Progressive Web Apps (PWAs). This guide covers Service Workers with TypeScript.

### 🎯 Learning Objectives

- ✅ Understand Service Worker lifecycle
- ✅ Implement caching strategies
- ✅ Build offline-first apps
- ✅ Type Service Worker code
- ✅ Debug Service Workers

### 📊 Section Info

- **Difficulty**: ⭐⭐⭐⭐ Advanced
- **Estimated Time**: 5-6 hours
- **Prerequisites**: [Web Workers](./40_web_workers.md)
- **Version**: TypeScript 5.7+, Service Worker API

---

## 📚 Table of Contents

1. [Service Worker Basics](#basics)
2. [Lifecycle](#lifecycle)
3. [Caching Strategies](#caching)
4. [TypeScript Configuration](#typescript)
5. [Best Practices](#best-practices)

---

<a name="basics"></a>
## 1. Service Worker Basics

```typescript
// Register service worker
if ('serviceWorker' in navigator) {
  navigator.serviceWorker.register('/sw.js')
    .then(reg => console.log('SW registered'))
    .catch(err => console.error('SW registration failed', err));
}

// sw.ts - Service Worker
self.addEventListener('install', (event: ExtendableEvent) => {
  event.waitUntil(
    caches.open('v1').then(cache => {
      return cache.addAll([
        '/',
        '/styles.css',
        '/script.js'
      ]);
    })
  );
});

self.addEventListener('fetch', (event: FetchEvent) => {
  event.respondWith(
    caches.match(event.request)
      .then(response => response || fetch(event.request))
  );
});
```

---

<a name="caching"></a>
## 2. Caching Strategies

```typescript
// Cache First (offline-first)
self.addEventListener('fetch', (event: FetchEvent) => {
  event.respondWith(
    caches.match(event.request)
      .then(cached => cached || fetch(event.request))
  );
});

// Network First (fresh data priority)
self.addEventListener('fetch', (event: FetchEvent) => {
  event.respondWith(
    fetch(event.request)
      .catch(() => caches.match(event.request))
  );
});
```

---

## 🎯 Key Takeaways

✅ **Service Workers** enable offline functionality

✅ **Caching strategies** improve performance

✅ **PWAs** require Service Workers

✅ **TypeScript** can type SW code

✅ **Lifecycle** is complex but manageable

---

[← Previous: Web Workers](./40_web_workers.md) | [Next: JS vs Java Concurrency →](./42_js_vs_java_concurrency.md)

**Progress**: Topic 41 of 63 | Part VI: 83% Complete
