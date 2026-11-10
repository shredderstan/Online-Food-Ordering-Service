import React, { createContext, useContext, useState, useEffect } from 'react';
import { cartApi } from '../api/client';
import { useAuth } from './AuthContext';

const CartContext = createContext(null);

export const CartProvider = ({ children }) => {
  const { token } = useAuth();
  const [cart, setCart] = useState(null);
  const [isCartOpen, setIsCartOpen] = useState(false);
  const [loading, setLoading] = useState(false);

  const fetchCart = async () => {
    if (!token) {
      setCart(null);
      return;
    }
    try {
      setLoading(true);
      const res = await cartApi.getCart();
      setCart(res.data);
    } catch (err) {
      console.error('Error fetching cart:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCart();
  }, [token]);

  const addItem = async (menuItemId, quantity = 1) => {
    if (!token) {
      throw new Error('Please log in to add items to cart');
    }
    try {
      const res = await cartApi.addItem(menuItemId, quantity);
      setCart(res.data);
      setIsCartOpen(true);
      return res.data;
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Failed to add item to cart';
      throw new Error(msg);
    }
  };

  const updateQuantity = async (itemId, newQuantity) => {
    if (newQuantity <= 0) {
      return removeItem(itemId);
    }
    try {
      const res = await cartApi.updateItem(itemId, newQuantity);
      setCart(res.data);
    } catch (err) {
      console.error('Failed to update quantity:', err);
    }
  };

  const removeItem = async (itemId) => {
    try {
      await cartApi.removeItem(itemId);
      await fetchCart();
    } catch (err) {
      console.error('Failed to remove item:', err);
    }
  };

  const clearCart = async () => {
    try {
      await cartApi.clearCart();
      setCart(null);
    } catch (err) {
      console.error('Failed to clear cart:', err);
    }
  };

  const itemCount = cart?.items?.reduce((sum, item) => sum + item.quantity, 0) || 0;

  return (
    <CartContext.Provider
      value={{
        cart,
        itemCount,
        loading,
        isCartOpen,
        setIsCartOpen,
        openCart: () => setIsCartOpen(true),
        closeCart: () => setIsCartOpen(false),
        fetchCart,
        addItem,
        updateQuantity,
        removeItem,
        clearCart,
      }}
    >
      {children}
    </CartContext.Provider>
  );
};

export const useCart = () => useContext(CartContext);

