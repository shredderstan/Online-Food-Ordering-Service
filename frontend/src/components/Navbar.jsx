import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ShoppingBag, Sparkles, LogOut, Utensils, ClipboardList, ChefHat, Menu, X } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';

export default function Navbar() {
  const { user, logout, isOwner } = useAuth();
  const { itemCount, openCart } = useCart();
  const navigate = useNavigate();
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <header className="sticky top-0 z-40 bg-ivory-100/85 backdrop-blur-md border-b border-ivory-300 transition-all duration-300">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-20">
          {/* Logo / Brand */}
          <Link to="/" className="flex items-center space-x-3 group">
            <div className="w-10 h-10 rounded-full border border-champagne-400 bg-espresso-950 flex items-center justify-center text-champagne-300 shadow-sm group-hover:scale-105 transition-transform duration-300">
              <Sparkles className="w-4 h-4 text-champagne-300" />
            </div>
            <div>
              <span className="font-serif text-2xl font-semibold tracking-wider text-espresso-950 block leading-tight">
                É P I C U R E
              </span>
              <span className="text-[10px] uppercase font-sans tracking-[0.25em] text-champagne-600 block">
                Gastronomy & Dining
              </span>
            </div>
          </Link>

          {/* Desktop Navigation */}
          <nav className="hidden md:flex items-center space-x-8">
            <Link
              to="/"
              className="text-xs uppercase tracking-[0.18em] font-medium text-espresso-800 hover:text-champagne-600 transition-colors"
            >
              Curated Tables
            </Link>

            {user && (
              <Link
                to="/orders"
                className="text-xs uppercase tracking-[0.18em] font-medium text-espresso-800 hover:text-champagne-600 flex items-center space-x-1.5 transition-colors"
              >
                <span>My Orders</span>
              </Link>
            )}

            {isOwner && (
              <Link
                to="/owner"
                className="text-xs uppercase tracking-[0.18em] font-medium text-champagne-600 hover:text-espresso-900 flex items-center space-x-1.5 transition-colors border border-champagne-300/80 px-3 py-1 rounded-full bg-champagne-100/40"
              >
                <ChefHat className="w-3.5 h-3.5" />
                <span>Maitre D' Portal</span>
              </Link>
            )}
          </nav>

          {/* Action Icons */}
          <div className="flex items-center space-x-4">
            {/* Cart Icon */}
            <button
              onClick={openCart}
              className="relative p-2.5 rounded-full hover:bg-ivory-200 text-espresso-800 hover:text-espresso-950 transition-colors"
              aria-label="View Selection"
            >
              <ShoppingBag className="w-5 h-5 stroke-[1.75]" />
              {itemCount > 0 && (
                <span className="absolute 0 top-0.5 right-0.5 bg-espresso-950 text-champagne-300 border border-champagne-400 text-[10px] font-sans font-bold w-4 h-4 rounded-full flex items-center justify-center shadow-sm">
                  {itemCount}
                </span>
              )}
            </button>

            {/* Auth Buttons */}
            {user ? (
              <div className="hidden md:flex items-center space-x-3 pl-3 border-l border-ivory-300">
                <div className="flex items-center space-x-2 text-xs">
                  <div className="w-8 h-8 rounded-full bg-espresso-950 text-champagne-300 font-serif font-medium flex items-center justify-center border border-champagne-400/50">
                    {user.firstName?.charAt(0) || 'E'}
                  </div>
                  <span className="font-medium text-espresso-800 tracking-wide">{user.firstName}</span>
                </div>
                <button
                  onClick={handleLogout}
                  className="p-2 rounded-full hover:bg-rose-50 text-stone-400 hover:text-rose-600 transition-colors"
                  title="Sign out"
                >
                  <LogOut className="w-4 h-4 stroke-[1.75]" />
                </button>
              </div>
            ) : (
              <div className="hidden md:flex items-center space-x-3">
                <Link
                  to="/login"
                  className="text-xs uppercase tracking-[0.16em] font-semibold text-espresso-800 hover:text-champagne-600 px-3 py-2 transition-colors"
                >
                  Sign In
                </Link>
                <Link
                  to="/register"
                  className="text-xs uppercase tracking-[0.16em] font-semibold text-ivory-100 bg-espresso-950 hover:bg-espresso-800 px-4 py-2.5 rounded-full shadow-sm hover:shadow transition-all duration-200 border border-espresso-900"
                >
                  Join Club
                </Link>
              </div>
            )}

            {/* Mobile Toggle */}
            <button
              onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
              className="md:hidden p-2 rounded-lg text-espresso-800 hover:bg-ivory-200"
            >
              {mobileMenuOpen ? <X className="w-5 h-5" /> : <Menu className="w-5 h-5" />}
            </button>
          </div>
        </div>

        {/* Mobile Nav */}
        {mobileMenuOpen && (
          <div className="md:hidden py-4 border-t border-ivory-300 space-y-3 bg-ivory-100">
            <Link
              to="/"
              onClick={() => setMobileMenuOpen(false)}
              className="block px-3 py-2 text-xs uppercase tracking-widest font-semibold text-espresso-800 hover:bg-ivory-200 rounded-lg"
            >
              Curated Tables
            </Link>
            {user && (
              <Link
                to="/orders"
                onClick={() => setMobileMenuOpen(false)}
                className="block px-3 py-2 text-xs uppercase tracking-widest font-semibold text-espresso-800 hover:bg-ivory-200 rounded-lg"
              >
                My Orders
              </Link>
            )}
            {isOwner && (
              <Link
                to="/owner"
                onClick={() => setMobileMenuOpen(false)}
                className="block px-3 py-2 text-xs uppercase tracking-widest font-semibold text-champagne-600 hover:bg-ivory-200 rounded-lg"
              >
                Maitre D' Portal
              </Link>
            )}
            <div className="pt-3 border-t border-ivory-300">
              {user ? (
                <div className="flex items-center justify-between px-3 py-2">
                  <span className="text-xs text-stone-500">Signed in as {user.firstName}</span>
                  <button
                    onClick={handleLogout}
                    className="text-xs uppercase font-bold text-rose-600"
                  >
                    Logout
                  </button>
                </div>
              ) : (
                <div className="grid grid-cols-2 gap-2 px-2">
                  <Link
                    to="/login"
                    onClick={() => setMobileMenuOpen(false)}
                    className="text-center py-2.5 text-xs uppercase tracking-wider font-semibold text-espresso-800 border border-ivory-300 rounded-full"
                  >
                    Sign In
                  </Link>
                  <Link
                    to="/register"
                    onClick={() => setMobileMenuOpen(false)}
                    className="text-center py-2.5 text-xs uppercase tracking-wider font-semibold text-ivory-100 bg-espresso-950 rounded-full"
                  >
                    Join Club
                  </Link>
                </div>
              )}
            </div>
          </div>
        )}
      </div>
    </header>
  );
}
