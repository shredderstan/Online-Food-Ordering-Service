import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import CartDrawer from './components/CartDrawer';
import HomePage from './pages/HomePage';
import RestaurantPage from './pages/RestaurantPage';
import OrdersPage from './pages/OrdersPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import OwnerDashboardPage from './pages/OwnerDashboardPage';
import { useAuth } from './context/AuthContext';

function ProtectedRoute({ children, ownerOnly = false }) {
  const { user, loading, isOwner } = useAuth();

  if (loading) {
    return <div className="min-h-screen flex items-center justify-center font-serif text-stone-500">Curating...</div>;
  }

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  if (ownerOnly && !isOwner) {
    return <Navigate to="/" replace />;
  }

  return children;
}

export default function App() {
  return (
    <div className="min-h-screen flex flex-col bg-ivory-100 text-espresso-950 font-sans selection:bg-champagne-300 selection:text-espresso-950">
      <Navbar />
      <CartDrawer />
      <div className="flex-1">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/restaurants/:id" element={<RestaurantPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route
            path="/orders"
            element={
              <ProtectedRoute>
                <OrdersPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/owner"
            element={
              <ProtectedRoute ownerOnly>
                <OwnerDashboardPage />
              </ProtectedRoute>
            }
          />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </div>

      <footer className="border-t border-ivory-300 bg-white py-12 text-center text-xs text-stone-500 space-y-2">
        <div className="font-serif text-lg tracking-widest text-espresso-950 font-normal">
          É P I C U R E
        </div>
        <p className="font-light text-[11px] uppercase tracking-[0.2em] text-champagne-700">
          Atelier Gastronomique & Fine Dining Delivery
        </p>
        <p className="text-[11px] text-stone-400 font-light pt-2">
          © 2026 Épicure. Handcrafted with Spring Boot & React.
        </p>
      </footer>
    </div>
  );
}
