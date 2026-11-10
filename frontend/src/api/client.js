import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Intercept requests and attach JWT token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Intercept responses for global 401 handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      // Token expired or invalid
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      if (!window.location.pathname.includes('/login') && !window.location.pathname.includes('/register')) {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export const authApi = {
  login: (data) => api.post('/users/login', data),
  register: (data) => api.post('/users/register', data),
  me: () => api.get('/users/me'),
};

export const restaurantApi = {
  getAll: () => api.get('/restaurants'),
  getById: (id) => api.get(`/restaurants/${id}`),
  getMyRestaurants: () => api.get('/restaurants/my'),
  getMenus: (restaurantId) => api.get(`/restaurants/${restaurantId}/menus`),
};

export const menuApi = {
  getItems: (menuId) => api.get(`/menus/${menuId}/items`),
};

export const cartApi = {
  getCart: () => api.get('/cart'),
  addItem: (menuItemId, quantity = 1) => api.post('/cart/items', { menuItemId, quantity }),
  updateItem: (itemId, quantity) => api.put(`/cart/items/${itemId}`, { quantity }),
  removeItem: (itemId) => api.delete(`/cart/items/${itemId}`),
  clearCart: () => api.delete('/cart'),
};

export const orderApi = {
  checkout: (data) => api.post('/orders/checkout', data),
  getMyOrders: () => api.get('/orders/my'),
  getById: (id) => api.get(`/orders/${id}`),
  cancelOrder: (id) => api.post(`/orders/${id}/cancel`),
  getOrdersForRestaurant: (restaurantId) => api.get(`/orders/restaurant/${restaurantId}`),
  updateStatus: (id, status) => api.patch(`/orders/${id}/status`, { status }),
};

export default api;

