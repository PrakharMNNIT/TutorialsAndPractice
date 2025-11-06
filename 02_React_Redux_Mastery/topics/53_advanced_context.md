# Topic 53: Advanced Context Patterns - Expert-Level Context Usage

## Overview

Advanced Context patterns enable sophisticated state management architectures in React applications. This comprehensive guide covers provider composition, context injection, lazy context initialization, context modules, and production-ready patterns for building maintainable Context-based systems.

**What You'll Master:**
- Advanced provider composition patterns
- Context injection and dependency injection
- Lazy context initialization strategies
- Context modules and code organization
- Context with async operations
- Context persistence and hydration
- Advanced TypeScript patterns for Context
- Production-ready Context architectures

## 1. Provider Composition Patterns

### 1.1 Compose Function for Providers

```typescript
// Utility to compose multiple providers elegantly
import React, { ComponentType, ReactNode } from 'react';

type Provider = ComponentType<{ children: ReactNode }>;

export const composeProviders = (...providers: Provider[]): Provider => {
  return ({ children }) => {
    return providers.reduceRight((acc, Provider) => {
      return <Provider>{acc}</Provider>;
    }, children);
  };
};

// Usage
const AppProviders = composeProviders(
  ThemeProvider,
  AuthProvider,
  CartProvider,
  NotificationProvider,
  LanguageProvider
);

const App: React.FC = () => {
  return (
    <AppProviders>
      <Router>
        <Routes>{/* routes */}</Routes>
      </Router>
    </AppProviders>
  );
};
```

### 1.2 Configurable Provider Composition

```typescript
// Dynamic provider composition based on configuration
interface ProviderConfig {
  name: string;
  Provider: ComponentType<any>;
  props?: Record<string, any>;
  enabled?: boolean;
}

class ProviderComposer {
  private providers: ProviderConfig[] = [];

  register(config: ProviderConfig): this {
    this.providers.push(config);
    return this;
  }

  build(): ComponentType<{ children: ReactNode }> {
    const enabledProviders = this.providers.filter(p => p.enabled !== false);

    return ({ children }) => {
      return enabledProviders.reduceRight((acc, config) => {
        const { Provider, props = {} } = config;
        return <Provider {...props}>{acc}</Provider>;
      }, children);
    };
  }
}

// Usage
const composer = new ProviderComposer();

composer
  .register({ name: 'theme', Provider: ThemeProvider, enabled: true })
  .register({ name: 'auth', Provider: AuthProvider, enabled: true })
  .register({ 
    name: 'feature-flag', 
    Provider: FeatureFlagProvider, 
    props: { flags: featureFlags },
    enabled: process.env.NODE_ENV === 'production'
  })
  .register({ 
    name: 'analytics', 
    Provider: AnalyticsProvider,
    enabled: process.env.ENABLE_ANALYTICS === 'true'
  });

const AppProviders = composer.build();

const App: React.FC = () => {
  return (
    <AppProviders>
      <Router />
    </AppProviders>
  );
};
```

## 2. Context Injection Pattern

### 2.1 Dependency Injection with Context

```typescript
// Implement dependency injection using Context
interface Services {
  apiClient: ApiClient;
  authService: AuthService;
  storageService: StorageService;
  analyticsService: AnalyticsService;
}

const ServicesContext = createContext<Services | undefined>(undefined);

export const useServices = () => {
  const context = useContext(ServicesContext);
  if (!context) {
    throw new Error('useServices must be used within ServicesProvider');
  }
  return context;
};

// Individual service hooks
export const useApiClient = () => useServices().apiClient;
export const useAuthService = () => useServices().authService;
export const useStorageService = () => useServices().storageService;
export const useAnalyticsService = () => useServices().analyticsService;

// Provider with service instantiation
export const ServicesProvider: React.FC<{ 
  children: ReactNode;
  config?: Partial<ServicesConfig>;
}> = ({ children, config }) => {
  const services = useMemo(() => {
    // Instantiate services once
    const apiClient = new ApiClient(config?.apiUrl || '/api');
    const storageService = new StorageService(config?.storage || 'local');
    const authService = new AuthService(apiClient, storageService);
    const analyticsService = new AnalyticsService(config?.analyticsKey);

    return {
      apiClient,
      authService,
      storageService,
      analyticsService,
    };
  }, [config]);

  return (
    <ServicesContext.Provider value={services}>
      {children}
    </ServicesContext.Provider>
  );
};

// Usage in components
const UserProfile: React.FC = () => {
  const authService = useAuthService();
  const analyticsService = useAnalyticsService();
  
  const handleLogout = async () => {
    await authService.logout();
    analyticsService.track('user_logout');
  };

  return <button onClick={handleLogout}>Logout</button>;
};
```

### 2.2 Factory Pattern with Context

```typescript
// Factory pattern for creating context instances
interface ContextFactory<T> {
  create: () => T;
  dependencies?: string[];
}

class ContextManager {
  private contexts = new Map<string, any>();
  private factories = new Map<string, ContextFactory<any>>();

  registerFactory<T>(name: string, factory: ContextFactory<T>): void {
    this.factories.set(name, factory);
  }

  get<T>(name: string): T {
    if (this.contexts.has(name)) {
      return this.contexts.get(name);
    }

    const factory = this.factories.get(name);
    if (!factory) {
      throw new Error(`No factory registered for ${name}`);
    }

    // Resolve dependencies first
    if (factory.dependencies) {
      factory.dependencies.forEach(dep => this.get(dep));
    }

    // Create and cache instance
    const instance = factory.create();
    this.contexts.set(name, instance);
    return instance;
  }

  clear(): void {
    this.contexts.clear();
  }
}

// Usage
const manager = new ContextManager();

manager.registerFactory('apiClient', {
  create: () => new ApiClient('/api'),
});

manager.registerFactory('authService', {
  create: () => new AuthService(manager.get('apiClient')),
  dependencies: ['apiClient'],
});

manager.registerFactory('userService', {
  create: () => new UserService(
    manager.get('apiClient'),
    manager.get('authService')
  ),
  dependencies: ['apiClient', 'authService'],
});

// Provider using factory
const ServicesProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const services = useMemo(() => ({
    apiClient: manager.get('apiClient'),
    authService: manager.get('authService'),
    userService: manager.get('userService'),
  }), []);

  return (
    <ServicesContext.Provider value={services}>
      {children}
    </ServicesContext.Provider>
  );
};
```

## 3. Lazy Context Initialization

### 3.1 Lazy Provider Loading

```typescript
// Lazy load context providers for code splitting
import { lazy, Suspense } from 'react';

// Lazy load provider
const LazyFeatureProvider = lazy(() => 
  import('./providers/FeatureProvider').then(m => ({ 
    default: m.FeatureProvider 
  }))
);

// Conditional provider rendering
const ConditionalProvider: React.FC<{
  condition: boolean;
  Provider: ComponentType<{ children: ReactNode }>;
  fallback?: ReactNode;
  children: ReactNode;
}> = ({ condition, Provider, fallback = null, children }) => {
  if (!condition) {
    return <>{fallback || children}</>;
  }

  return (
    <Suspense fallback={<div>Loading provider...</div>}>
      <Provider>{children}</Provider>
    </Suspense>
  );
};

// Usage
const App: React.FC = () => {
  const { features } = useConfig();

  return (
    <ThemeProvider>
      <AuthProvider>
        <ConditionalProvider
          condition={features.advancedAnalytics}
          Provider={LazyFeatureProvider}
        >
          <Router />
        </ConditionalProvider>
      </AuthProvider>
    </ThemeProvider>
  );
};
```

### 3.2 On-Demand Context Activation

```typescript
// Activate context only when needed
interface LazyContextState<T> {
  data: T | null;
  isInitialized: boolean;
  initialize: () => Promise<void>;
}

function createLazyContext<T>(
  initializer: () => Promise<T>
) {
  const Context = createContext<LazyContextState<T> | undefined>(undefined);

  const Provider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [data, setData] = useState<T | null>(null);
    const [isInitialized, setIsInitialized] = useState(false);
    const initializeRef = useRef<Promise<void> | null>(null);

    const initialize = useCallback(async () => {
      if (initializeRef.current) {
        return initializeRef.current;
      }

      initializeRef.current = (async () => {
        try {
          const result = await initializer();
          setData(result);
          setIsInitialized(true);
        } catch (error) {
          console.error('Failed to initialize context:', error);
          throw error;
        }
      })();

      return initializeRef.current;
    }, []);

    const value = useMemo<LazyContextState<T>>(() => ({
      data,
      isInitialized,
      initialize,
    }), [data, isInitialized, initialize]);

    return <Context.Provider value={value}>{children}</Context.Provider>;
  };

  const useContext = () => {
    const context = useContext(Context);
    if (!context) {
      throw new Error('Context must be used within Provider');
    }
    return context;
  };

  return { Provider, useContext };
}

// Usage
const { Provider: ConfigProvider, useContext: useConfig } = createLazyContext(
  async () => {
    const response = await fetch('/api/config');
    return response.json();
  }
);

const FeatureComponent: React.FC = () => {
  const { data, isInitialized, initialize } = useConfig();

  useEffect(() => {
    if (!isInitialized) {
      initialize();
    }
  }, [isInitialized, initialize]);

  if (!isInitialized) {
    return <div>Loading configuration...</div>;
  }

  return <div>Config loaded: {data.appName}</div>;
};
```

## 4. Context Modules Pattern

### 4.1 Modular Context Organization

```typescript
// Organize contexts into modules
// contexts/auth/index.ts
export { AuthProvider, useAuth } from './AuthProvider';
export type { AuthContextType, User } from './types';

// contexts/auth/AuthProvider.tsx
import { createContext, useContext } from 'react';
import { AuthContextType } from './types';
import { useAuthState } from './useAuthState';
import { useAuthActions } from './useAuthActions';

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within AuthProvider');
  return context;
};

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const state = useAuthState();
  const actions = useAuthActions(state);

  const value = useMemo(() => ({
    ...state,
    ...actions,
  }), [state, actions]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

// contexts/auth/useAuthState.ts
export const useAuthState = () => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    checkAuth().then(user => {
      setUser(user);
      setIsLoading(false);
    });
  }, []);

  return { user, isLoading, isAuthenticated: !!user, setUser };
};

// contexts/auth/useAuthActions.ts
export const useAuthActions = (state: AuthState) => {
  const login = useCallback(async (email: string, password: string) => {
    const user = await authAPI.login(email, password);
    state.setUser(user);
  }, [state.setUser]);

  const logout = useCallback(async () => {
    await authAPI.logout();
    state.setUser(null);
  }, [state.setUser]);

  return { login, logout };
};
```

### 4.2 Plugin-Based Context System

```typescript
// Extensible context system with plugins
interface ContextPlugin<T> {
  name: string;
  init?: (context: T) => void;
  enhance?: (value: T) => T;
  cleanup?: () => void;
}

class ContextPluginManager<T> {
  private plugins: ContextPlugin<T>[] = [];

  register(plugin: ContextPlugin<T>): this {
    this.plugins.push(plugin);
    return this;
  }

  initialize(context: T): void {
    this.plugins.forEach(plugin => {
      plugin.init?.(context);
    });
  }

  enhance(value: T): T {
    return this.plugins.reduce((acc, plugin) => {
      return plugin.enhance?.(acc) || acc;
    }, value);
  }

  cleanup(): void {
    this.plugins.forEach(plugin => {
      plugin.cleanup?.();
    });
  }
}

// Example: Auth context with plugins
const authPluginManager = new ContextPluginManager<AuthContextType>();

// Logger plugin
authPluginManager.register({
  name: 'logger',
  enhance: (value) => ({
    ...value,
    login: async (...args) => {
      console.log('Login attempt');
      const result = await value.login(...args);
      console.log('Login successful');
      return result;
    },
  }),
});

// Analytics plugin
authPluginManager.register({
  name: 'analytics',
  enhance: (value) => ({
    ...value,
    logout: async () => {
      await value.logout();
      trackEvent('user_logout');
    },
  }),
});

// Provider with plugins
export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const baseValue = useAuthBase();
  
  useEffect(() => {
    authPluginManager.initialize(baseValue);
    return () => authPluginManager.cleanup();
  }, []);

  const enhancedValue = useMemo(
    () => authPluginManager.enhance(baseValue),
    [baseValue]
  );

  return (
    <AuthContext.Provider value={enhancedValue}>
      {children}
    </AuthContext.Provider>
  );
};
```

## 5. Async Context Operations

### 5.1 Context with Async Initialization

```typescript
// Handle async initialization in context
interface AsyncContextState<T> {
  data: T | null;
  error: Error | null;
  isLoading: boolean;
  retry: () => void;
}

function createAsyncContext<T>(
  fetcher: () => Promise<T>,
  options?: {
    retryCount?: number;
    retryDelay?: number;
  }
) {
  const Context = createContext<AsyncContextState<T> | undefined>(undefined);

  const Provider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [data, setData] = useState<T | null>(null);
    const [error, setError] = useState<Error | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [retryCount, setRetryCount] = useState(0);

    const fetchData = useCallback(async () => {
      setIsLoading(true);
      setError(null);

      let attempts = 0;
      const maxAttempts = options?.retryCount || 3;

      while (attempts < maxAttempts) {
        try {
          const result = await fetcher();
          setData(result);
          setError(null);
          setIsLoading(false);
          return;
        } catch (err) {
          attempts++;
          if (attempts >= maxAttempts) {
            setError(err as Error);
            setIsLoading(false);
            return;
          }
          // Wait before retry
          await new Promise(resolve => 
            setTimeout(resolve, options?.retryDelay || 1000)
          );
        }
      }
    }, [retryCount]);

    useEffect(() => {
      fetchData();
    }, [fetchData]);

    const retry = useCallback(() => {
      setRetryCount(prev => prev + 1);
    }, []);

    const value = useMemo<AsyncContextState<T>>(() => ({
      data,
      error,
      isLoading,
      retry,
    }), [data, error, isLoading, retry]);

    return <Context.Provider value={value}>{children}</Context.Provider>;
  };

  const useContext = () => {
    const context = useContext(Context);
    if (!context) {
      throw new Error('Context must be used within Provider');
    }
    return context;
  };

  return { Provider, useContext };
}

// Usage
const { Provider: ConfigProvider, useContext: useConfig } = createAsyncContext(
  () => fetch('/api/config').then(r => r.json()),
  { retryCount: 3, retryDelay: 2000 }
);

const FeatureComponent: React.FC = () => {
  const { data, error, isLoading, retry } = useConfig();

  if (isLoading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message} <button onClick={retry}>Retry</button></div>;
  if (!data) return null;

  return <div>App: {data.appName}</div>;
};
```

### 5.2 Context with Optimistic Updates

```typescript
// Optimistic updates in context
interface OptimisticState<T> {
  data: T;
  optimisticData: T | null;
  isPending: boolean;
}

function createOptimisticContext<T>(initialData: T) {
  const Context = createContext<{
    state: OptimisticState<T>;
    updateOptimistically: (
      optimisticUpdate: (data: T) => T,
      actualUpdate: () => Promise<T>
    ) => Promise<void>;
  } | undefined>(undefined);

  const Provider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [state, setState] = useState<OptimisticState<T>>({
      data: initialData,
      optimisticData: null,
      isPending: false,
    });

    const updateOptimistically = useCallback(
      async (
        optimisticUpdate: (data: T) => T,
        actualUpdate: () => Promise<T>
      ) => {
        // Apply optimistic update immediately
        const optimisticData = optimisticUpdate(state.data);
        setState(prev => ({
          ...prev,
          optimisticData,
          isPending: true,
        }));

        try {
          // Perform actual update
          const actualData = await actualUpdate();
          
          setState({
            data: actualData,
            optimisticData: null,
            isPending: false,
          });
        } catch (error) {
          // Revert optimistic update on error
          setState(prev => ({
            ...prev,
            optimisticData: null,
            isPending: false,
          }));
          throw error;
        }
      },
      [state.data]
    );

    const value = useMemo(() => ({
      state,
      updateOptimistically,
    }), [state, updateOptimistically]);

    return <Context.Provider value={value}>{children}</Context.Provider>;
  };

  const useContext = () => {
    const context = useContext(Context);
    if (!context) throw new Error('Must be used within Provider');
    return context;
  };

  return { Provider, useContext };
}

// Usage
interface Todo {
  id: string;
  text: string;
  completed: boolean;
}

const { Provider: TodoProvider, useContext: useTodos } = createOptimisticContext<Todo[]>([]);

const TodoList: React.FC = () => {
  const { state, updateOptimistically } = useTodos();
  
  // Display optimistic data if available, otherwise actual data
  const displayData = state.optimisticData || state.data;

  const handleToggle = (id: string) => {
    updateOptimistically(
      // Optimistic update
      (todos) => todos.map(todo =>
        todo.id === id ? { ...todo, completed: !todo.completed } : todo
      ),
      // Actual update
      () => fetch(`/api/todos/${id}/toggle`, { method: 'POST' })
        .then(r => r.json())
    );
  };

  return (
    <div>
      {state.isPending && <div className="pending-indicator">Saving...</div>}
      {displayData.map(todo => (
        <div key={todo.id}>
          <input
            type="checkbox"
            checked={todo.completed}
            onChange={() => handleToggle(todo.id)}
          />
          {todo.text}
        </div>
      ))}
    </div>
  );
};
```

## 6. Context Persistence

### 6.1 Persistent Context with Storage

```typescript
// Persist context to localStorage/sessionStorage
function createPersistedContext<T>(
  key: string,
  initialValue: T,
  storage: Storage = localStorage
) {
  const Context = createContext<{
    value: T;
    setValue: (value: T | ((prev: T) => T)) => void;
  } | undefined>(undefined);

  const Provider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [value, setValue] = useState<T>(() => {
      try {
        const stored = storage.getItem(key);
        return stored ? JSON.parse(stored) : initialValue;
      } catch {
        return initialValue;
      }
    });

    // Persist to storage on change
    useEffect(() => {
      try {
        storage.setItem(key, JSON.stringify(value));
      } catch (error) {
        console.error('Failed to persist context:', error);
      }
    }, [value]);

    // Sync across tabs
    useEffect(() => {
      const handleStorage = (e: StorageEvent) => {
        if (e.key === key && e.newValue) {
          try {
            setValue(JSON.parse(e.newValue));
          } catch {}
        }
      };

      window.addEventListener('storage', handleStorage);
      return () => window.removeEventListener('storage', handleStorage);
    }, []);

    const contextValue = useMemo(() => ({
      value,
      setValue,
    }), [value]);

    return <Context.Provider value={contextValue}>{children}</Context.Provider>;
  };

  const useContext = () => {
    const context = useContext(Context);
    if (!context) throw new Error('Must be used within Provider');
    return context;
  };

  return { Provider, useContext };
}

// Usage
const { Provider: ThemeProvider, useContext: usePersistedTheme } = 
  createPersistedContext('app-theme', 'light' as Theme);

const ThemeToggle: React.FC = () => {
  const { value: theme, setValue: setTheme } = usePersistedTheme();

  return (
    <button onClick={() => setTheme(prev => prev === 'light' ? 'dark' : 'light')}>
      Current: {theme}
    </button>
  );
};
```

### 6.2 Server-Side Context Hydration

```typescript
// Hydrate context from server-side data
interface ServerData {
  user: User | null;
  config: AppConfig;
  initialState: Record<string, any>;
}

// Provider with server data hydration
export const HydratableProvider: React.FC<{
  serverData?: ServerData;
  children: ReactNode;
}> = ({ serverData, children }) => {
  const [user, setUser] = useState<User | null>(
    serverData?.user || null
  );
  
  const [config] = useState<AppConfig>(
    serverData?.config || defaultConfig
  );

  const [isHydrated, setIsHydrated] = useState(!!serverData);

  useEffect(() => {
    if (!isHydrated) {
      // Client-side initialization
      initializeApp().then(() => {
        setIsHydrated(true);
      });
    }
  }, [isHydrated]);

  const value = useMemo(() => ({
    user,
    config,
    isHydrated,
    setUser,
  }), [user, config, isHydrated]);

  return <AppContext.Provider value={value}>{children}</AppContext.Provider>;
};

// Next.js usage
export async function getServerSideProps(context) {
  const user = await getUser(context.req);
  const config = await getConfig();

  return {
    props: {
      serverData: {
        user,
        config,
        initialState: {},
      },
    },
  };
}

const Page: React.FC<{ serverData: ServerData }> = ({ serverData }) => {
  return (
    <HydratableProvider serverData={serverData}>
      <PageContent />
    </HydratableProvider>
  );
};
```

## 7. Context Error Boundaries

### 7.1 Context-Specific Error Handling

```typescript
// Error boundary for context initialization
class ContextErrorBoundary extends React.Component<
  { children: ReactNode; contextName: string },
  { hasError: boolean; error: Error | null }
> {
  constructor(props) {
    super(props);
    this.state = { hasError: false, error: null };
  }

  static getDerivedStateFromError(error: Error) {
    return { hasError: true, error };
  }

  componentDidCatch(error: Error, errorInfo: React.ErrorInfo) {
    console.error(`Context error in ${this.props.contextName}:`, error, errorInfo);
  }

  render() {
    if (this.state.hasError) {
      return (
        <div className="context-error">
          <h2>Failed to initialize {this.props.contextName}</h2>
          <p>{this.state.error?.message}</p>
          <button onClick={() => this.setState({ hasError: false, error: null })}>
            Retry
          </button>
        </div>
      );
    }

    return this.props.children;
  }
}

// Usage
const App: React.FC = () => {
  return (
    <ContextErrorBoundary contextName="Theme">
      <ThemeProvider>
        <ContextErrorBoundary contextName="Auth">
          <AuthProvider>
            <Router />
          </AuthProvider>
        </ContextErrorBoundary>
      </ThemeProvider>
    </ContextErrorBoundary>
  );
};
```

## 8. Higher-Order Thinking: Advanced Patterns

### FAQ 1: How do I handle context that depends on another context?

**Short Answer:** Use nested providers or context composition, ensuring dependent context is inside the parent context.

**Pattern:**

```typescript
// UserPreferences depends on Auth context
const UserPreferencesProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const { user } = useAuth(); // Depends on AuthContext

  const [preferences, setPreferences] = useState<Preferences | null>(null);

  useEffect(() => {
    if (user) {
      fetchUserPreferences(user.id).then(setPreferences);
    } else {
      setPreferences(null);
    }
  }, [user]);

  const value = useMemo(() => ({
    preferences,
    updatePreferences: (updates: Partial<Preferences>) => {
      setPreferences(prev => prev ? { ...prev, ...updates } : null);
    },
  }), [preferences]);

  return (
    <PreferencesContext.Provider value={value}>
      {children}
    </PreferencesContext.Provider>
  );
};

// Correct nesting order
<AuthProvider>
  <UserPreferencesProvider> {/* Inside AuthProvider */}
    <App />
  </UserPreferencesProvider>
</AuthProvider>
```

### FAQ 2: Can I conditionally render context providers?

**Short Answer:** Yes, but be careful with context consumers that expect the provider to exist.

**Safe Pattern:**

```typescript
const ConditionalFeatureProvider: React.FC<{ 
  enabled: boolean;
  children: ReactNode;
}> = ({ enabled, children }) => {
  if (!enabled) {
    // Provide null/default context
    return (
      <FeatureContext.Provider value={null}>
        {children}
      </FeatureContext.Provider>
    );
  }

  return (
    <FeatureProvider>
      {children}
    </FeatureProvider>
  );
};

// Consumer handles null context
const FeatureComponent: React.FC = () => {
  const feature = useFeature(); // Can be null

  if (!feature) {
    return <div>Feature not available</div>;
  }

  return <div>Feature: {feature.name}</div>;
};
```

### FAQ 3: How do I debug Context re-render issues?

**Short Answer:** Use React DevTools Profiler and custom logging hooks to track Context updates and consumer renders.

**Debug Utilities:**

```typescript
// Hook to debug context re-renders
const useContextDebug = (contextName: string, value: any) => {
  const renderCount = useRef(0);
  const previousValue = useRef(value);

  useEffect(() => {
    renderCount.current++;
    
    console.log(`${contextName} render #${renderCount.current}`);
    
    if (previousValue.current !== value) {
      console.log(`${contextName} value changed:`, {
        previous: previousValue.current,
        current: value,
      });
    }
    
    previousValue.current = value;
  });
};

// Usage in provider
const ThemeProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [theme, setTheme] = useState('light');
  
  const value = useMemo(() => ({
    theme,
    setTheme,
  }), [theme]);

  // Debug in development
  if (process.env.NODE_ENV === 'development') {
    useContextDebug('ThemeContext', value);
  }

  return <ThemeContext.Provider value={value}>{children}</ThemeContext.Provider>;
};

// Consumer render tracking
const useRenderCount = (componentName: string) => {
  const renderCount = useRef(0);

  useEffect(() => {
    renderCount.current++;
    console.log(`${componentName} rendered ${renderCount.current} times`);
  });

  return renderCount.current;
};

// Usage in consumer
const ThemeConsumer: React.FC = () => {
  const { theme } = useTheme();
  useRenderCount('ThemeConsumer');

  return <div>Theme: {theme}</div>;
};
```

### FAQ 4: How do I share context across micro-frontends?

**Short Answer:** Use custom events or shared state management; Context is scoped to React tree.

**Cross-MFE Communication:**

```typescript
// Shared event bus for micro-frontends
class ContextEventBus {
  private listeners = new Map<string, Set<Function>>();

  subscribe(event: string, callback: Function): () => void {
    if (!this.listeners.has(event)) {
      this.listeners.set(event, new Set());
    }
    this.listeners.get(event)!.add(callback);

    return () => {
      this.listeners.get(event)?.delete(callback);
    };
  }

  publish(event: string, data: any): void {
    this.listeners.get(event)?.forEach(callback => callback(data));
  }
}

const eventBus = new ContextEventBus();

// MFE 1: Publish theme changes
const ThemeProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [theme, setTheme] = useState('light');

  useEffect(() => {
    eventBus.publish('theme:change', theme);
  }, [theme]);

  // Listen for external theme changes
  useEffect(() => {
    return eventBus.subscribe('theme:change', (newTheme) => {
      setTheme(newTheme);
    });
  }, []);

  const value = useMemo(() => ({ theme, setTheme }), [theme]);
  return <ThemeContext.Provider value={value}>{children}</ThemeContext.Provider>;
};

// MFE 2: Subscribe to theme changes
const useSharedTheme = () => {
  const [theme, setTheme] = useState('light');

  useEffect(() => {
    return eventBus.subscribe('theme:change', setTheme);
  }, []);

  return theme;
};
```

### FAQ 5: Should I use Context for form state?

**Short Answer:** Generally no; form state is local. Use Context only for forms that span multiple routes or need global access.

**Decision Guide:**

```typescript
// ❌ DON'T USE Context for simple forms
<FormContext.Provider>
  <SimpleForm /> {/* Local state is better */}
</FormContext.Provider>

// ✅ USE Context for multi-step forms across routes
<Router>
  <MultiStepFormProvider>
    <Route path="/step1" element={<Step1 />} />
    <Route path="/step2" element={<Step2 />} />
    <Route path="/step3" element={<Step3 />} />
  </MultiStepFormProvider>
</Router>

// ✅ USE Context for draft persistence
<DraftProvider> {/* Saves form state across sessions */}
  <BlogEditor />
</DraftProvider>
```

## 9. Senior SDE Interview Questions

### Question 1: Design a Context architecture for a multi-tenant SaaS application

**What Interviewers Assess:**
- Multi-tenancy understanding
- Context scalability
- Security considerations
- Production experience

**Model Answer:**

"I'd design a hierarchical Context architecture with tenant isolation:

**Architecture:**
```typescript
<App>
  <TenantProvider tenantId={currentTenant}>
    <TenantConfigProvider> {/* Tenant-specific config */}
      <AuthProvider> {/* Tenant-scoped auth */}
        <FeatureProvider> {/* Tenant feature flags */}
          <Router />
        </FeatureProvider>
      </AuthProvider>
    </TenantConfigProvider>
  </TenantProvider>
</App>
```

**Key Components:**

```typescript
// Tenant-scoped API client
const TenantProvider: React.FC<{ 
  tenantId: string;
  children: ReactNode;
}> = ({ tenantId, children }) => {
  const apiClient = useMemo(
    () => new ApiClient(`/api/tenants/${tenantId}`),
    [tenantId]
  );

  return (
    <TenantContext.Provider value={{ tenantId, apiClient }}>
      {children}
    </TenantContext.Provider>
  );
};
```

**Security Considerations:**
- Tenant data isolation in Context
- Validate tenant access on provider mount
- Clear tenant data on logout
- Audit log tenant context access
- Rate limiting per tenant

**Performance:**
- Cache tenant config aggressively
- Lazy load tenant-specific features
- Monitor context re-renders per tenant
- Optimize for multi-tenant scale"

### Question 2: Implement a Context that automatically syncs with WebSocket updates

**What Interviewers Assess:**
- Real-time data handling
- WebSocket integration
- State synchronization
- Error handling

**Model Answer:**

```typescript
// Real-time context with WebSocket sync
function createRealtimeContext<T>(
  endpoint: string,
  initialData: T
) {
  const Context = createContext<{
    data: T;
    isConnected: boolean;
    error: Error | null;
  } | undefined>(undefined);

  const Provider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [data, setData] = useState<T>(initialData);
    const [isConnected, setIsConnected] = useState(false);
    const [error, setError] = useState<Error | null>(null);
    const wsRef = useRef<WebSocket | null>(null);

    useEffect(() => {
      const ws = new WebSocket(endpoint);
      wsRef.current = ws;

      ws.onopen = () => {
        setIsConnected(true);
        setError(null);
      };

      ws.onmessage = (event) => {
        try {
          const update = JSON.parse(event.data);
          setData(update);
        } catch (err) {
          console.error('Failed to parse WebSocket message:', err);
        }
      };

      ws.onerror = (event) => {
        setError(new Error('WebSocket error'));
        setIsConnected(false);
      };

      ws.onclose = () => {
        setIsConnected(false);
        
        // Attempt reconnect after 5 seconds
        setTimeout(() => {
          if (wsRef.current?.readyState === WebSocket.CLOSED) {
            // Trigger re-mount to reconnect
          }
        }, 5000);
      };

      return () => {
        ws.close();
      };
    }, []);

    const value = useMemo(() => ({
      data,
      isConnected,
      error,
    }), [data, isConnected, error]);

    return <Context.Provider value={value}>{children}</Context.Provider>;
  };

  const useContext = () => {
    const context = useContext(Context);
    if (!context) throw new Error('Must be used within Provider');
    return context;
  };

  return { Provider, useContext };
}

// Usage
const { Provider: LiveDataProvider, useContext: useLiveData } = 
  createRealtimeContext('ws://api.example.com/live', { value: 0 });

const LiveDashboard: React.FC = () => {
  const { data, isConnected } = useLiveData();

  return (
    <div>
      <div className={isConnected ? 'connected' : 'disconnected'}>
        {isConnected ? '🟢 Live' : '🔴 Disconnected'}
      </div>
      <div>Current Value: {data.value}</div>
    </div>
  );
};
```

**Key Points:**
- Automatic reconnection logic
- Error handling and recovery
- Connection state tracking
- Efficient state updates
- Cleanup on unmount"

## Self-Assessment Checklist

Test your advanced Context patterns mastery:

**Fundamentals:**
- [ ] Understand provider composition patterns
- [ ] Can implement dependency injection with Context
- [ ] Know lazy context initialization strategies
- [ ] Understand context modules organization

**Intermediate:**
- [ ] Implement async context operations
- [ ] Handle context persistence and hydration
- [ ] Use context error boundaries
- [ ] Debug context re-render issues

**Advanced:**
- [ ] Design plugin-based context systems
- [ ] Implement optimistic updates in context
- [ ] Handle context dependencies correctly
- [ ] Build real-time context with WebSocket

**Expert:**
- [ ] Architect multi-tenant context systems
- [ ] Design scalable context architectures
- [ ] Optimize context for production
- [ ] Mentor team on advanced patterns

## Summary

Advanced Context patterns enable sophisticated state management. Key takeaways:

1. **Provider Composition:** Clean composition with helper utilities
2. **Dependency Injection:** Services pattern with Context
3. **Lazy Initialization:** Load contexts on-demand for performance
4. **Modular Organization:** Structure contexts for maintainability
5. **Async Operations:** Handle async initialization and updates
6. **Optimistic Updates:** Immediate UI feedback with Context
7. **Persistence:** Save and sync context across sessions
8. **Error Boundaries:** Graceful error handling for contexts
9. **Debugging:** Tools and techniques for troubleshooting
10. **Production Patterns:** Real-world architectures

**Next Steps:**
- Topic 54: Multiple Contexts Management
- Topic 55: Context Performance Optimization
- Topic 56: Redux Fundamentals

---

*Advanced Context patterns transform Context from a simple prop-drilling solution into a powerful state management architecture. Master these patterns to build production-grade applications.*
