# Topic 50: Testing Routes - Comprehensive Router Testing Strategies

## Overview

Testing routes and navigation is essential for ensuring reliable React applications. This comprehensive guide covers unit testing, integration testing, end-to-end testing for React Router, including testing protected routes, loaders, actions, and complex navigation flows.

**What You'll Master:**
- Unit testing routes and navigation
- Testing protected routes and auth flows
- Testing loaders and actions
- Integration testing with React Testing Library
- E2E testing with Cypress/Playwright
- Mocking React Router hooks
- Testing navigation state and history
- Best practices for router testing

## 1. Testing Setup

### 1.1 Test Environment Configuration

```bash
# Install testing dependencies
npm install --save-dev @testing-library/react @testing-library/jest-dom
npm install --save-dev @testing-library/user-event
npm install --save-dev jest-environment-jsdom
npm install --save-dev @types/jest
```

```typescript
// jest.config.js
module.exports = {
  testEnvironment: 'jsdom',
  setupFilesAfterEnv: ['<rootDir>/src/setupTests.ts'],
  moduleNameMapper: {
    '\\.(css|less|scss|sass)$': 'identity-obj-proxy',
  },
  transform: {
    '^.+\\.(ts|tsx)$': 'ts-jest',
  },
  collectCoverageFrom: [
    'src/**/*.{ts,tsx}',
    '!src/**/*.d.ts',
    '!src/**/*.stories.tsx',
  ],
};

// setupTests.ts
import '@testing-library/jest-dom';
import { cleanup } from '@testing-library/react';

afterEach(() => {
  cleanup();
});
```

### 1.2 Router Test Utilities

```typescript
// test-utils.tsx - Custom render with router
import React, { ReactElement } from 'react';
import { render, RenderOptions } from '@testing-library/react';
import { BrowserRouter, MemoryRouter } from 'react-router-dom';

interface ExtendedRenderOptions extends Omit<RenderOptions, 'wrapper'> {
  initialEntries?: string[];
  initialIndex?: number;
}

// Render with BrowserRouter
export const renderWithRouter = (
  ui: ReactElement,
  options?: ExtendedRenderOptions
) => {
  const Wrapper: React.FC<{ children: React.ReactNode }> = ({ children }) => (
    <BrowserRouter>{children}</BrowserRouter>
  );

  return render(ui, { wrapper: Wrapper, ...options });
};

// Render with MemoryRouter (better for testing)
export const renderWithMemoryRouter = (
  ui: ReactElement,
  { initialEntries = ['/'], initialIndex = 0, ...options }: ExtendedRenderOptions = {}
) => {
  const Wrapper: React.FC<{ children: React.ReactNode }> = ({ children }) => (
    <MemoryRouter initialEntries={initialEntries} initialIndex={initialIndex}>
      {children}
    </MemoryRouter>
  );

  return render(ui, { wrapper: Wrapper, ...options });
};

// Render with full router configuration
export const renderWithRoutes = (
  routes: React.ReactElement,
  { initialEntries = ['/'], ...options }: ExtendedRenderOptions = {}
) => {
  return renderWithMemoryRouter(routes, { initialEntries, ...options });
};
```

## 2. Testing Basic Navigation

### 2.1 Link Component Testing

```typescript
// Test Link components
import { screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { renderWithRouter } from './test-utils';

describe('Navigation', () => {
  it('should navigate to different routes', async () => {
    const user = userEvent.setup();

    renderWithRouter(
      <div>
        <nav>
          <Link to="/">Home</Link>
          <Link to="/about">About</Link>
        </nav>
        <Routes>
          <Route path="/" element={<div>Home Page</div>} />
          <Route path="/about" element={<div>About Page</div>} />
        </Routes>
      </div>
    );

    // Initial render
    expect(screen.getByText('Home Page')).toBeInTheDocument();

    // Click About link
    await user.click(screen.getByText('About'));

    // About page should render
    expect(screen.getByText('About Page')).toBeInTheDocument();
    expect(screen.queryByText('Home Page')).not.toBeInTheDocument();
  });

  it('should handle programmatic navigation', async () => {
    const TestComponent: React.FC = () => {
      const navigate = useNavigate();
      return (
        <div>
          <button onClick={() => navigate('/dashboard')}>
            Go to Dashboard
          </button>
        </div>
      );
    };

    renderWithRouter(
      <Routes>
        <Route path="/" element={<TestComponent />} />
        <Route path="/dashboard" element={<div>Dashboard</div>} />
      </Routes>
    );

    const user = userEvent.setup();
    await user.click(screen.getByText('Go to Dashboard'));

    expect(screen.getByText('Dashboard')).toBeInTheDocument();
  });
});
```

### 2.2 Testing URL Parameters

```typescript
// Test dynamic routes with parameters
describe('Dynamic Routes', () => {
  const UserProfile: React.FC = () => {
    const { userId } = useParams<{ userId: string }>();
    return <div>User ID: {userId}</div>;
  };

  it('should render with URL parameters', () => {
    renderWithMemoryRouter(
      <Routes>
        <Route path="/users/:userId" element={<UserProfile />} />
      </Routes>,
      { initialEntries: ['/users/123'] }
    );

    expect(screen.getByText('User ID: 123')).toBeInTheDocument();
  });

  it('should update when parameters change', async () => {
    const UserList: React.FC = () => (
      <div>
        <Link to="/users/1">User 1</Link>
        <Link to="/users/2">User 2</Link>
      </div>
    );

    renderWithMemoryRouter(
      <>
        <UserList />
        <Routes>
          <Route path="/users/:userId" element={<UserProfile />} />
        </Routes>
      </>,
      { initialEntries: ['/users/1'] }
    );

    expect(screen.getByText('User ID: 1')).toBeInTheDocument();

    const user = userEvent.setup();
    await user.click(screen.getByText('User 2'));

    expect(screen.getByText('User ID: 2')).toBeInTheDocument();
  });
});
```

### 2.3 Testing Search Parameters

```typescript
// Test query parameters
describe('Search Parameters', () => {
  const SearchPage: React.FC = () => {
    const [searchParams] = useSearchParams();
    const query = searchParams.get('q') || 'none';
    
    return <div>Search: {query}</div>;
  };

  it('should read search parameters', () => {
    renderWithMemoryRouter(
      <Routes>
        <Route path="/search" element={<SearchPage />} />
      </Routes>,
      { initialEntries: ['/search?q=react'] }
    );

    expect(screen.getByText('Search: react')).toBeInTheDocument();
  });

  it('should update search parameters', async () => {
    const SearchForm: React.FC = () => {
      const [searchParams, setSearchParams] = useSearchParams();
      
      return (
        <div>
          <p>Query: {searchParams.get('q') || 'none'}</p>
          <button onClick={() => setSearchParams({ q: 'typescript' })}>
            Search TypeScript
          </button>
        </div>
      );
    };

    renderWithMemoryRouter(
      <Routes>
        <Route path="/search" element={<SearchForm />} />
      </Routes>,
      { initialEntries: ['/search'] }
    );

    expect(screen.getByText('Query: none')).toBeInTheDocument();

    const user = userEvent.setup();
    await user.click(screen.getByText('Search TypeScript'));

    expect(screen.getByText('Query: typescript')).toBeInTheDocument();
  });
});
```

## 3. Testing Protected Routes

### 3.1 Testing Auth Guards

```typescript
// Test protected route behavior
import { AuthProvider, useAuth } from '../contexts/AuthContext';

describe('Protected Routes', () => {
  const ProtectedPage: React.FC = () => <div>Protected Content</div>;
  const LoginPage: React.FC = () => <div>Login Page</div>;

  const MockAuthProvider: React.FC<{ 
    isAuthenticated: boolean; 
    children: ReactNode 
  }> = ({ isAuthenticated, children }) => {
    const mockAuth = {
      isAuthenticated,
      user: isAuthenticated ? { id: '1', name: 'Test User' } : null,
      login: jest.fn(),
      logout: jest.fn(),
    };

    return (
      <AuthContext.Provider value={mockAuth}>
        {children}
      </AuthContext.Provider>
    );
  };

  it('should redirect unauthenticated users to login', () => {
    renderWithMemoryRouter(
      <MockAuthProvider isAuthenticated={false}>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute>
                <ProtectedPage />
              </ProtectedRoute>
            }
          />
        </Routes>
      </MockAuthProvider>,
      { initialEntries: ['/dashboard'] }
    );

    expect(screen.getByText('Login Page')).toBeInTheDocument();
    expect(screen.queryByText('Protected Content')).not.toBeInTheDocument();
  });

  it('should render protected content for authenticated users', () => {
    renderWithMemoryRouter(
      <MockAuthProvider isAuthenticated={true}>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute>
                <ProtectedPage />
              </ProtectedRoute>
            }
          />
        </Routes>
      </MockAuthProvider>,
      { initialEntries: ['/dashboard'] }
    );

    expect(screen.getByText('Protected Content')).toBeInTheDocument();
    expect(screen.queryByText('Login Page')).not.toBeInTheDocument();
  });

  it('should preserve intended destination after login', async () => {
    const { rerender } = renderWithMemoryRouter(
      <MockAuthProvider isAuthenticated={false}>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute>
                <ProtectedPage />
              </ProtectedRoute>
            }
          />
        </Routes>
      </MockAuthProvider>,
      { initialEntries: ['/dashboard'] }
    );

    // Should redirect to login
    expect(screen.getByText('Login Page')).toBeInTheDocument();

    // Simulate login
    rerender(
      <MockAuthProvider isAuthenticated={true}>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute>
                <ProtectedPage />
              </ProtectedRoute>
            }
          />
        </Routes>
      </MockAuthProvider>
    );

    // Should show protected content
    expect(screen.getByText('Protected Content')).toBeInTheDocument();
  });
});
```

### 3.2 Testing Role-Based Access

```typescript
// Test role-based routing
describe('Role-Based Routes', () => {
  const AdminPanel: React.FC = () => <div>Admin Panel</div>;
  const Unauthorized: React.FC = () => <div>Unauthorized</div>;

  const MockAuthProvider: React.FC<{
    roles: string[];
    children: ReactNode;
  }> = ({ roles, children }) => {
    const mockAuth = {
      isAuthenticated: true,
      user: { id: '1', name: 'Test', roles },
      hasRole: (role: string) => roles.includes(role),
    };

    return (
      <AuthContext.Provider value={mockAuth}>
        {children}
      </AuthContext.Provider>
    );
  };

  it('should allow admin users to access admin panel', () => {
    renderWithMemoryRouter(
      <MockAuthProvider roles={['admin']}>
        <Routes>
          <Route path="/unauthorized" element={<Unauthorized />} />
          <Route
            path="/admin"
            element={
              <RoleProtectedRoute allowedRoles={['admin']}>
                <AdminPanel />
              </RoleProtectedRoute>
            }
          />
        </Routes>
      </MockAuthProvider>,
      { initialEntries: ['/admin'] }
    );

    expect(screen.getByText('Admin Panel')).toBeInTheDocument();
  });

  it('should deny non-admin users access to admin panel', () => {
    renderWithMemoryRouter(
      <MockAuthProvider roles={['user']}>
        <Routes>
          <Route path="/unauthorized" element={<Unauthorized />} />
          <Route
            path="/admin"
            element={
              <RoleProtectedRoute allowedRoles={['admin']}>
                <AdminPanel />
              </RoleProtectedRoute>
            }
          />
        </Routes>
      </MockAuthProvider>,
      { initialEntries: ['/admin'] }
    );

    expect(screen.getByText('Unauthorized')).toBeInTheDocument();
    expect(screen.queryByText('Admin Panel')).not.toBeInTheDocument();
  });
});
```

## 4. Testing Loaders

### 4.1 Testing Loader Functions

```typescript
// Test loader independently
import { productLoader } from '../loaders/productLoader';

describe('productLoader', () => {
  it('should load product data', async () => {
    const mockProduct = {
      id: '1',
      name: 'Test Product',
      price: 99.99,
    };

    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockProduct),
      } as Response)
    );

    const result = await productLoader({
      params: { productId: '1' },
      request: new Request('http://localhost/products/1'),
    } as any);

    expect(result).toEqual(mockProduct);
    expect(fetch).toHaveBeenCalledWith('/api/products/1');
  });

  it('should throw 404 for missing product', async () => {
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: false,
        status: 404,
      } as Response)
    );

    await expect(
      productLoader({
        params: { productId: '999' },
        request: new Request('http://localhost/products/999'),
      } as any)
    ).rejects.toThrow();
  });
});
```

### 4.2 Testing Routes with Loaders

```typescript
// Test components that use loader data
import { createMemoryRouter, RouterProvider } from 'react-router-dom';

describe('ProductPage', () => {
  it('should render product from loader', async () => {
    const mockProduct = {
      id: '1',
      name: 'Test Product',
      price: 99.99,
    };

    const mockLoader = jest.fn(() => Promise.resolve(mockProduct));

    const router = createMemoryRouter(
      [
        {
          path: '/products/:productId',
          element: <ProductPage />,
          loader: mockLoader,
        },
      ],
      { initialEntries: ['/products/1'] }
    );

    render(<RouterProvider router={router} />);

    // Wait for data to load
    await screen.findByText('Test Product');
    expect(screen.getByText('$99.99')).toBeInTheDocument();
    expect(mockLoader).toHaveBeenCalled();
  });

  it('should show error boundary on loader failure', async () => {
    const mockLoader = jest.fn(() =>
      Promise.reject(new Response('Product not found', { status: 404 }))
    );

    const router = createMemoryRouter(
      [
        {
          path: '/products/:productId',
          element: <ProductPage />,
          loader: mockLoader,
          errorElement: <ErrorBoundary />,
        },
      ],
      { initialEntries: ['/products/999'] }
    );

    render(<RouterProvider router={router} />);

    await screen.findByText(/Product not found/i);
  });
});
```

## 5. Testing Actions

### 5.1 Testing Action Functions

```typescript
// Test action independently
import { createUserAction } from '../actions/userActions';

describe('createUserAction', () => {
  it('should create user successfully', async () => {
    const mockUser = { id: '1', name: 'John', email: 'john@example.com' };

    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockUser),
      } as Response)
    );

    const formData = new FormData();
    formData.append('name', 'John');
    formData.append('email', 'john@example.com');

    const request = new Request('http://localhost/users', {
      method: 'POST',
      body: formData,
    });

    const result = await createUserAction({
      request,
      params: {},
    } as any);

    // Should redirect
    expect(result).toHaveProperty('status', 302);
  });

  it('should return validation errors', async () => {
    const formData = new FormData();
    formData.append('name', '');
    formData.append('email', 'invalid-email');

    const request = new Request('http://localhost/users', {
      method: 'POST',
      body: formData,
    });

    const result = await createUserAction({
      request,
      params: {},
    } as any) as ActionResult;

    expect(result.success).toBe(false);
    expect(result.errors).toHaveProperty('name');
    expect(result.errors).toHaveProperty('email');
  });
});
```

### 5.2 Testing Forms with Actions

```typescript
// Test form submission with actions
describe('CreateUserForm', () => {
  it('should submit form and show success message', async () => {
    const mockAction = jest.fn(() =>
      Promise.resolve(redirect('/users/1'))
    );

    const router = createMemoryRouter(
      [
        {
          path: '/users/new',
          element: <CreateUserForm />,
          action: mockAction,
        },
        {
          path: '/users/:userId',
          element: <div>User Created</div>,
        },
      ],
      { initialEntries: ['/users/new'] }
    );

    render(<RouterProvider router={router} />);

    const user = userEvent.setup();

    // Fill form
    await user.type(screen.getByLabelText(/name/i), 'John Doe');
    await user.type(screen.getByLabelText(/email/i), 'john@example.com');

    // Submit
    await user.click(screen.getByRole('button', { name: /create/i }));

    // Should navigate to user page
    await screen.findByText('User Created');
    expect(mockAction).toHaveBeenCalled();
  });

  it('should display validation errors', async () => {
    const mockAction = jest.fn(() =>
      Promise.resolve({
        success: false,
        errors: {
          email: 'Email is invalid',
        },
      })
    );

    const router = createMemoryRouter(
      [
        {
          path: '/users/new',
          element: <CreateUserForm />,
          action: mockAction,
        },
      ],
      { initialEntries: ['/users/new'] }
    );

    render(<RouterProvider router={router} />);

    const user = userEvent.setup();

    // Submit with invalid data
    await user.type(screen.getByLabelText(/email/i), 'invalid');
    await user.click(screen.getByRole('button', { name: /create/i }));

    // Should show error
    await screen.findByText('Email is invalid');
  });
});
```

## 6. Testing Navigation State

### 6.1 Testing useNavigate

```typescript
// Mock useNavigate for testing
import * as ReactRouter from 'react-router-dom';

describe('Component with Navigation', () => {
  it('should navigate on button click', async () => {
    const mockNavigate = jest.fn();
    jest.spyOn(ReactRouter, 'useNavigate').mockReturnValue(mockNavigate);

    const Component: React.FC = () => {
      const navigate = useNavigate();
      return (
        <button onClick={() => navigate('/dashboard')}>
          Go to Dashboard
        </button>
      );
    };

    renderWithRouter(<Component />);

    const user = userEvent.setup();
    await user.click(screen.getByText('Go to Dashboard'));

    expect(mockNavigate).toHaveBeenCalledWith('/dashboard');
  });

  it('should navigate with state', async () => {
    const mockNavigate = jest.fn();
    jest.spyOn(ReactRouter, 'useNavigate').mockReturnValue(mockNavigate);

    const Component: React.FC = () => {
      const navigate = useNavigate();
      return (
        <button
          onClick={() =>
            navigate('/profile', { state: { from: 'home' } })
          }
        >
          View Profile
        </button>
      );
    };

    renderWithRouter(<Component />);

    const user = userEvent.setup();
    await user.click(screen.getByText('View Profile'));

    expect(mockNavigate).toHaveBeenCalledWith(
      '/profile',
      { state: { from: 'home' } }
    );
  });
});
```

### 6.2 Testing useLocation

```typescript
// Test location state
describe('Location State', () => {
  const TargetPage: React.FC = () => {
    const location = useLocation();
    const state = location.state as { from?: string };
    
    return <div>From: {state?.from || 'unknown'}</div>;
  };

  it('should receive location state', () => {
    const router = createMemoryRouter(
      [
        {
          path: '/target',
          element: <TargetPage />,
        },
      ],
      {
        initialEntries: [
          {
            pathname: '/target',
            state: { from: 'source' },
          },
        ],
      }
    );

    render(<RouterProvider router={router} />);

    expect(screen.getByText('From: source')).toBeInTheDocument();
  });
});
```

## 7. Testing useFetcher

### 7.1 Testing Fetcher Actions

```typescript
// Test useFetcher for non-navigation mutations
describe('NewsletterSignup', () => {
  it('should submit email and show success message', async () => {
    const mockAction = jest.fn(() =>
      Promise.resolve({ success: true })
    );

    const router = createMemoryRouter(
      [
        {
          path: '/',
          element: <NewsletterSignup />,
        },
        {
          path: '/api/newsletter',
          action: mockAction,
        },
      ],
      { initialEntries: ['/'] }
    );

    render(<RouterProvider router={router} />);

    const user = userEvent.setup();

    // Fill and submit form
    await user.type(
      screen.getByPlaceholderText(/your email/i),
      'test@example.com'
    );
    await user.click(screen.getByRole('button', { name: /subscribe/i }));

    // Should show success message
    await screen.findByText(/thanks for subscribing/i);
    expect(mockAction).toHaveBeenCalled();
  });

  it('should handle submission errors', async () => {
    const mockAction = jest.fn(() =>
      Promise.resolve({
        success: false,
        error: 'Subscription failed',
      })
    );

    const router = createMemoryRouter(
      [
        {
          path: '/',
          element: <NewsletterSignup />,
        },
        {
          path: '/api/newsletter',
          action: mockAction,
        },
      ],
      { initialEntries: ['/'] }
    );

    render(<RouterProvider router={router} />);

    const user = userEvent.setup();

    await user.type(
      screen.getByPlaceholderText(/your email/i),
      'test@example.com'
    );
    await user.click(screen.getByRole('button', { name: /subscribe/i }));

    await screen.findByText('Subscription failed');
  });
});
```

## 8. Integration Testing

### 8.1 Full User Flow Testing

```typescript
// Test complete user journey
describe('Product Purchase Flow', () => {
  it('should complete purchase flow', async () => {
    const mockAuth = {
      isAuthenticated: true,
      user: { id: '1', name: 'Test User' },
    };

    const router = createMemoryRouter(
      [
        {
          path: '/',
          element: <HomePage />,
        },
        {
          path: '/products',
          element: <ProductList />,
          loader: () => Promise.resolve({ products: mockProducts }),
        },
        {
          path: '/products/:id',
          element: <ProductDetail />,
          loader: ({ params }) =>
            Promise.resolve(mockProducts.find(p => p.id === params.id)),
        },
        {
          path: '/cart',
          element: <Cart />,
        },
        {
          path: '/checkout',
          element: <Checkout />,
          action: async ({ request }) => {
            const formData = await request.formData();
            return redirect('/confirmation');
          },
        },
        {
          path: '/confirmation',
          element: <OrderConfirmation />,
        },
      ],
      { initialEntries: ['/'] }
    );

    const { container } = render(
      <AuthProvider value={mockAuth}>
        <RouterProvider router={router} />
      </AuthProvider>
    );

    const user = userEvent.setup();

    // 1. Navigate to products
    await user.click(screen.getByText(/products/i));
    await screen.findByText('Product List');

    // 2. View product detail
    await user.click(screen.getByText('Product 1'));
    await screen.findByText('Product 1 Details');

    // 3. Add to cart
    await user.click(screen.getByText(/add to cart/i));

    // 4. Go to cart
    await user.click(screen.getByText(/view cart/i));
    await screen.findByText('Shopping Cart');

    // 5. Proceed to checkout
    await user.click(screen.getByText(/checkout/i));
    await screen.findByText('Checkout');

    // 6. Complete purchase
    await user.type(screen.getByLabelText(/card number/i), '4242424242424242');
    await user.click(screen.getByText(/place order/i));

    // 7. See confirmation
    await screen.findByText(/order confirmed/i);
  });
});
```

## 9. E2E Testing

### 9.1 Cypress Router Testing

```typescript
// cypress/e2e/navigation.cy.ts
describe('Navigation', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('should navigate between pages', () => {
    // Check home page
    cy.contains('Home Page').should('be.visible');
    cy.url().should('eq', Cypress.config().baseUrl + '/');

    // Navigate to about
    cy.contains('About').click();
    cy.contains('About Page').should('be.visible');
    cy.url().should('include', '/about');

    // Use browser back button
    cy.go('back');
    cy.contains('Home Page').should('be.visible');

    // Use browser forward button
    cy.go('forward');
    cy.contains('About Page').should('be.visible');
  });

  it('should handle protected routes', () => {
    // Try to access protected route
    cy.visit('/dashboard');

    // Should redirect to login
    cy.url().should('include', '/login');
    cy.contains('Login').should('be.visible');

    // Login
    cy.get('[name="email"]').type('test@example.com');
    cy.get('[name="password"]').type('password123');
    cy.get('button[type="submit"]').click();

    // Should redirect to dashboard
    cy.url().should('include', '/dashboard');
    cy.contains('Dashboard').should('be.visible');
  });

  it('should preserve query parameters', () => {
    cy.visit('/search?q=react&sort=newest');

    cy.url().should('include', 'q=react');
    cy.url().should('include', 'sort=newest');

    cy.contains('Search: react').should('be.visible');
  });
});
```

### 9.2 Playwright Router Testing

```typescript
// tests/navigation.spec.ts
import { test, expect } from '@playwright/test';

test.describe('Navigation', () => {
  test('should navigate between pages', async ({ page }) => {
    await page.goto('/');

    // Check home page
    await expect(page.locator('h1')).toContainText('Home');

    // Navigate to about
    await page.click('text=About');
    await expect(page).toHaveURL(/\/about$/);
    await expect(page.locator('h1')).toContainText('About');

    // Browser back
    await page.goBack();
    await expect(page).toHaveURL(/\/$/);
    await expect(page.locator('h1')).toContainText('Home');
  });

  test('should handle form submission', async ({ page }) => {
    await page.goto('/users/new');

    // Fill form
    await page.fill('[name="name"]', 'John Doe');
    await page.fill('[name="email"]', 'john@example.com');
    await page.selectOption('[name="role"]', 'user');

    // Submit
    await page.click('button[type="submit"]');

    // Should navigate to user profile
    await expect(page).toHaveURL(/\/users\/\d+$/);
    await expect(page.locator('h1')).toContainText('John Doe');
  });

  test('should handle 404 pages', async ({ page }) => {
    await page.goto('/non-existent-page');

    await expect(page.locator('h1')).toContainText('404');
    await expect(page.locator('text=Page Not Found')).toBeVisible();
  });
});
```

## 10. Higher-Order Thinking: Testing Strategy

### FAQ 1: Should I test routes with MemoryRouter or BrowserRouter?

**Short Answer:** Use MemoryRouter for unit/integration tests; use actual browser (Cypress/Playwright) for E2E tests.

**MemoryRouter (Unit/Integration):**
```typescript
// ✅ GOOD: Fast, isolated, deterministic
renderWithMemoryRouter(
  <Routes>
    <Route path="/users/:id" element={<UserProfile />} />
  </Routes>,
  { initialEntries: ['/users/123'] }
);
```

**BrowserRouter (E2E Only):**
```typescript
// ⚠️ AVOID in unit tests: Slower, requires browser APIs
renderWithRouter(<App />);
// Use Cypress/Playwright instead for real browser testing
```

**Best Practice:** MemoryRouter for 99% of tests, E2E tools for critical flows.

### FAQ 2: How do I test navigation that depends on async operations?

**Short Answer:** Use `waitFor` and `findBy` queries to wait for async navigation to complete.

```typescript
it('should navigate after async operation', async () => {
  const Component: React.FC = () => {
    const navigate = useNavigate();
    
    const handleClick = async () => {
      await saveData();
      navigate('/success');
    };
    
    return <button onClick={handleClick}>Save</button>;
  };

  renderWithMemoryRouter(
    <Routes>
      <Route path="/" element={<Component />} />
      <Route path="/success" element={<div>Success Page</div>} />
    </Routes>
  );

  const user = userEvent.setup();
  await user.click(screen.getByText('Save'));

  // Wait for navigation to complete
  await screen.findByText('Success Page');
});
```

### FAQ 3: Should I test loaders and actions separately or integrated?

**Short Answer:** Test both independently (unit tests) and integrated (integration tests) for comprehensive coverage.

**Unit Tests (Fast, Isolated):**
```typescript
// Test loader logic independently
it('should format loader data correctly', async () => {
  const result = await productLoader({ params: { id: '1' } });
  expect(result.price).toBeTypeOf('number');
  expect(result.name).toBeTruthy();
});
```

**Integration Tests (Realistic):**
```typescript
// Test loader with component
it('should render with loaded data', async () => {
  const router = createMemoryRouter([
    {
      path: '/product/:id',
      element: <ProductPage />,
      loader: productLoader,
    },
  ]);
  
  render(<RouterProvider router={router} />);
  await screen.findByText(/product name/i);
});
```

### FAQ 4: How do I test error boundaries in routes?

**Short Answer:** Throw errors from loaders/components and verify error boundary renders.

```typescript
describe('Error Boundaries', () => {
  it('should catch loader errors', async () => {
    const errorLoader = () => {
      throw new Response('Not Found', { status: 404 });
    };

    const router = createMemoryRouter([
      {
        path: '/',
        element: <HomePage />,
        loader: errorLoader,
        errorElement: <ErrorPage />,
      },
    ]);

    render(<RouterProvider router={router} />);

    await screen.findByText(/not found/i);
  });

  it('should catch component errors', () => {
    const BrokenComponent = () => {
      throw new Error('Component error');
    };

    const router = createMemoryRouter([
      {
        path: '/',
        element: <BrokenComponent />,
        errorElement: <ErrorPage />,
      },
    ]);

    render(<RouterProvider router={router} />);

    expect(screen.getByText(/something went wrong/i)).toBeInTheDocument();
  });
});
```

### FAQ 5: What's the minimum test coverage for routing?

**Short Answer:** Test critical paths (auth, payments), happy paths for all routes, and error states.

**Coverage Priorities:**

| Priority | What to Test | Example |
|----------|--------------|---------|
| Critical | Auth flows, payments | Login → Purchase → Confirmation |
| High | Main user journeys | Browse → Detail → Cart |
| Medium | All route rendering | Each route renders correctly |
| Low | Edge cases | 404, unauthorized, network errors |

**Minimum Coverage:**
```typescript
// 1. Critical: Auth (Must test)
✅ Login flow
✅ Protected route access
✅ Session timeout

// 2. High: Main journeys (Should test)
✅ Browse products → View detail
✅ Add to cart → Checkout
✅ User registration

// 3. Medium: Basic routes (Nice to test)
✅ All routes render
✅ Navigation works
✅ 404 handling

// 4. Low: Edge cases (Test if time)
⚠️ Network failures
⚠️ Race conditions
⚠️ Browser back/forward edge cases
```

## 11. Senior SDE Interview Questions

### Question 1: Design a testing strategy for a complex multi-step form with navigation

**What Interviewers Assess:**
- Testing methodology
- Coverage strategy
- Real-world problem-solving
- Quality mindset

**Model Answer:**

"I'd implement a layered testing approach:

**1. Unit Tests (Loader/Actions):**
```typescript
// Test validation logic
describe('formValidation', () => {
  it('should validate email format', () => {
    expect(validateEmail('invalid')).toBe(false);
    expect(validateEmail('test@example.com')).toBe(true);
  });
});
```

**2. Integration Tests (Form + Navigation):**
```typescript
// Test form flow
it('should progress through multi-step form', async () => {
  const router = createMemoryRouter([
    { path: '/form/step1', element: <Step1 /> },
    { path: '/form/step2', element: <Step2 /> },
    { path: '/form/step3', element: <Step3 /> },
    { path: '/success', element: <Success /> },
  ]);

  render(<RouterProvider router={router} />);

  // Step 1
  await fillStep1();
  await clickNext();

  // Step 2
  await screen.findByText('Step 2');
  await fillStep2();
  await clickNext();

  // Step 3
  await screen.findByText('Step 3');
  await fillStep3();
  await clickSubmit();

  // Success
  await screen.findByText('Success');
});
```

**3. E2E Tests (Critical Paths):**
```typescript
// Test with real navigation
cy.visit('/form/step1');
cy.fillForm(step1Data);
cy.clickNext();
cy.url().should('include', '/step2');
// Continue through all steps
```

**Key Principles:**
- Unit test business logic
- Integration test navigation flow
- E2E test critical paths only
- Mock external dependencies
- Focus on user behavior, not implementation"

### Question 2: How would you test a route that depends on real-time data?

**What Interviewers Assess:**
- Async testing knowledge
- Mocking strategies
- Edge case handling
- Real-world experience

**Model Answer:**

```typescript
// Test real-time data updates
describe('Real-Time Dashboard', () => {
  it('should update when data changes', async () => {
    // Mock WebSocket or polling mechanism
    const mockSubscribe = jest.fn((callback) => {
      // Simulate initial data
      callback({ value: 100 });
      
      // Simulate update after 1 second
      setTimeout(() => callback({ value: 200 }), 1000);
      
      return () => {}; // Unsubscribe
    });

    jest.mock('../services/realtime', () => ({
      subscribe: mockSubscribe,
    }));

    const router = createMemoryRouter([
      {
        path: '/dashboard',
        element: <RealtimeDashboard />,
      },
    ]);

    render(<RouterProvider router={router} />);

    // Initial value
    expect(screen.getByText('Value: 100')).toBeInTheDocument();

    // Wait for update
    await screen.findByText('Value: 200');
    
    expect(mockSubscribe).toHaveBeenCalled();
  });
});
```

**Key Strategies:**
- Mock WebSocket/SSE connections
- Control timing with fake timers
- Test connection drops and reconnects
- Verify cleanup on navigation away"

## Self-Assessment Checklist

Test your router testing mastery:

**Fundamentals:**
- [ ] Can set up router testing environment
- [ ] Test basic navigation with Link components
- [ ] Test URL and search parameters
- [ ] Mock React Router hooks

**Intermediate:**
- [ ] Test protected routes and auth flows
- [ ] Test loaders and actions independently
- [ ] Test forms with actions
- [ ] Handle async navigation in tests

**Advanced:**
- [ ] Write integration tests for user flows
- [ ] Test error boundaries and error states
- [ ] Test useFetcher for optimistic UI
- [ ] Write E2E tests with Cypress/Playwright

**Expert:**
- [ ] Design comprehensive testing strategies
- [ ] Test real-time navigation scenarios
- [ ] Optimize test performance and reliability
- [ ] Mentor team on router testing practices

## Summary

Comprehensive router testing ensures navigation reliability. Key takeaways:

1. **MemoryRouter:** Use for unit and integration tests
2. **createMemoryRouter:** Test with loaders and actions
3. **Protected Routes:** Test auth guards and redirects
4. **Loaders/Actions:** Test independently and integrated
5. **useFetcher:** Test non-navigation mutations
6. **Navigation State:** Test useNavigate, useLocation
7. **Integration Tests:** Test complete user flows
8. **E2E Tests:** Use Cypress/Playwright for critical paths
9. **Error Handling:** Test error boundaries and 404s
10. **Best Practices:** Layer tests, mock strategically, focus on behavior

**Next Steps:**
- Topic 51: Router Performance - Final router optimization techniques
- Complete Part V: React Router
- Begin Part VI: State Management (Redux, Context, Zustand)

---

*Testing is not optional—it's insurance. Comprehensive router tests ensure your navigation remains reliable as your application grows and evolves.*
