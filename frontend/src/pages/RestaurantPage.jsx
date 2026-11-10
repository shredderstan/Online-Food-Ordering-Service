import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { MapPin, Phone, ArrowLeft, Plus, Loader2, Sparkles, AlertTriangle } from 'lucide-react';
import { restaurantApi, menuApi } from '../api/client';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';

const RESTAURANT_IMAGES = {
  'Bella Italia Trattoria': 'https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=1600&q=80',
  'Burger & Co. Craft Shack': 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=1600&q=80',
  'Sakura Sushi & Ramen': 'https://images.unsplash.com/photo-1579871494447-9811cf80d66c?auto=format&fit=crop&w=1600&q=80',
};

const DEFAULT_IMAGE = 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=1600&q=80';

export default function RestaurantPage() {
  const { id } = useParams();
  const { addItem, clearCart } = useCart();
  const { user } = useAuth();

  const [restaurant, setRestaurant] = useState(null);
  const [menusWithItems, setMenusWithItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [addingId, setAddingId] = useState(null);
  const [conflictModal, setConflictModal] = useState(false);
  const [pendingItem, setPendingItem] = useState(null);
  const [errorMsg, setErrorMsg] = useState('');

  useEffect(() => {
    const fetchRestaurantDetails = async () => {
      try {
        setLoading(true);
        const restRes = await restaurantApi.getById(id);
        setRestaurant(restRes.data);

        const menusRes = await restaurantApi.getMenus(id);
        const menus = menusRes.data || [];

        const loadedMenus = await Promise.all(
          menus.map(async (menu) => {
            try {
              const itemsRes = await menuApi.getItems(menu.id);
              return { ...menu, items: itemsRes.data || [] };
            } catch {
              return { ...menu, items: [] };
            }
          })
        );

        setMenusWithItems(loadedMenus);
      } catch (err) {
        console.error('Failed to load restaurant info:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchRestaurantDetails();
  }, [id]);

  const handleAddToCart = async (item) => {
    setErrorMsg('');
    setAddingId(item.id);

    try {
      await addItem(item.id, 1);
    } catch (err) {
      if (err.message && err.message.includes('only one restaurant')) {
        setPendingItem(item);
        setConflictModal(true);
      } else {
        setErrorMsg(err.message || 'Could not add item to selection');
      }
    } finally {
      setAddingId(null);
    }
  };

  const handleClearAndAdd = async () => {
    if (!pendingItem) return;
    try {
      await clearCart();
      await addItem(pendingItem.id, 1);
      setConflictModal(false);
      setPendingItem(null);
    } catch (err) {
      setErrorMsg(err.message || 'Failed to update selection');
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <Loader2 className="w-8 h-8 text-champagne-600 animate-spin" />
      </div>
    );
  }

  if (!restaurant) {
    return (
      <div className="max-w-xl mx-auto my-24 p-12 text-center bg-white rounded-3xl border border-ivory-300 shadow-chic">
        <h2 className="font-serif text-2xl font-normal text-espresso-950">Atelier Not Found</h2>
        <p className="text-xs text-stone-500 mt-2 font-light">The requested kitchen is currently unavailable.</p>
        <Link
          to="/"
          className="inline-block mt-6 px-6 py-2.5 bg-espresso-950 text-champagne-300 rounded-full text-xs uppercase tracking-widest font-semibold"
        >
          Return to Tables
        </Link>
      </div>
    );
  }

  const bgImage = RESTAURANT_IMAGES[restaurant.name] || DEFAULT_IMAGE;

  return (
    <div className="min-h-screen pb-28">
      {/* Editorial Header Banner */}
      <div className="relative bg-espresso-950 text-ivory-100 py-16 sm:py-24 px-4 sm:px-6 lg:px-8 overflow-hidden">
        <div className="absolute inset-0 z-0">
          <img
            src={bgImage}
            alt={restaurant.name}
            className="w-full h-full object-cover opacity-25 filter brightness-75 scale-105"
          />
          <div className="absolute inset-0 bg-gradient-to-t from-espresso-950 via-espresso-950/70 to-espresso-950/40" />
        </div>

        <div className="max-w-7xl mx-auto relative z-10 space-y-6">
          <Link
            to="/"
            className="inline-flex items-center space-x-2 text-champagne-300 hover:text-white text-xs uppercase tracking-[0.2em] font-semibold transition-colors"
          >
            <ArrowLeft className="w-3.5 h-3.5" />
            <span>Curated Tables</span>
          </Link>

          <div className="space-y-4 max-w-3xl">
            <div className="flex items-center space-x-3">
              <span className="text-[10px] uppercase tracking-widest font-semibold px-3 py-1 rounded-full bg-champagne-400/20 text-champagne-300 border border-champagne-400/40">
                {restaurant.city}
              </span>
              <span
                className={`text-[10px] uppercase tracking-widest font-semibold px-3 py-1 rounded-full ${
                  restaurant.active !== false
                    ? 'bg-emerald-950/80 text-emerald-300 border border-emerald-500/30'
                    : 'bg-stone-900/80 text-stone-400 border border-stone-700'
                }`}
              >
                {restaurant.active !== false ? '● Open For Service' : 'Currently Resting'}
              </span>
            </div>

            <h1 className="font-serif text-4xl sm:text-6xl font-normal tracking-tight text-ivory-50 leading-tight">
              {restaurant.name}
            </h1>

            <p className="font-sans text-sm sm:text-base text-ivory-200/85 font-light leading-relaxed">
              {restaurant.description}
            </p>

            <div className="flex flex-wrap items-center gap-6 text-xs text-ivory-300/80 font-light pt-2 border-t border-ivory-200/10">
              <span className="flex items-center space-x-2">
                <MapPin className="w-4 h-4 text-champagne-400 flex-shrink-0" />
                <span>{restaurant.address}, {restaurant.city}</span>
              </span>
              {restaurant.phoneNumber && (
                <span className="flex items-center space-x-2">
                  <Phone className="w-4 h-4 text-champagne-400 flex-shrink-0" />
                  <span>{restaurant.phoneNumber}</span>
                </span>
              )}
            </div>
          </div>
        </div>
      </div>

      {/* Tasting Menu Sections */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pt-16">
        {errorMsg && (
          <div className="mb-8 p-4 bg-rose-50 border border-rose-200 text-rose-800 text-xs rounded-2xl">
            {errorMsg}
          </div>
        )}

        {menusWithItems.length === 0 ? (
          <div className="bg-white rounded-3xl border border-ivory-300 p-16 text-center max-w-md mx-auto my-12 shadow-chic">
            <Sparkles className="w-10 h-10 text-champagne-600 mx-auto mb-3" />
            <h3 className="font-serif text-xl font-normal text-espresso-950">Menu Under Preparation</h3>
            <p className="text-xs text-stone-500 mt-2 font-light">
              The culinary team is curating the seasonal menu selections.
            </p>
          </div>
        ) : (
          <div className="space-y-16">
            {menusWithItems.map((menu) => (
              <section key={menu.id} className="space-y-8">
                {/* Menu Section Header */}
                <div className="border-b border-ivory-300 pb-4 flex flex-col sm:flex-row sm:items-end justify-between gap-2">
                  <div>
                    <span className="text-[10px] uppercase tracking-[0.25em] text-champagne-600 font-semibold block mb-1">
                      Carte Des Plats
                    </span>
                    <h2 className="font-serif text-3xl font-normal text-espresso-950">
                      {menu.name}
                    </h2>
                  </div>
                  {menu.description && (
                    <p className="text-xs text-stone-500 font-light italic">{menu.description}</p>
                  )}
                </div>

                {menu.items.length === 0 ? (
                  <p className="text-xs text-stone-400 italic">No selections registered in this section.</p>
                ) : (
                  <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {menu.items.map((item) => (
                      <div
                        key={item.id}
                        className="bg-white rounded-2xl border border-ivory-300 p-6 shadow-chic hover:shadow-chic-hover transition-all duration-300 flex flex-col justify-between group hover:border-champagne-300"
                      >
                        <div className="space-y-2.5">
                          <div className="flex items-start justify-between gap-3">
                            <h3 className="font-serif text-lg font-medium text-espresso-950 group-hover:text-champagne-700 transition-colors">
                              {item.name}
                            </h3>
                            <span className="font-sans font-bold text-espresso-900 text-sm whitespace-nowrap">
                              ${Number(item.price).toFixed(2)}
                            </span>
                          </div>
                          <p className="text-xs text-stone-500 line-clamp-3 leading-relaxed font-light">
                            {item.description}
                          </p>
                        </div>

                        <div className="pt-5 flex items-center justify-between border-t border-ivory-200 mt-6">
                          <span
                            className={`text-[10px] uppercase tracking-wider font-semibold ${
                              item.available !== false ? 'text-emerald-700' : 'text-stone-400'
                            }`}
                          >
                            {item.available !== false ? 'Available Today' : 'Sold Out'}
                          </span>

                          <button
                            onClick={() => handleAddToCart(item)}
                            disabled={item.available === false || addingId === item.id}
                            className={`px-4 py-2 rounded-full text-xs uppercase tracking-wider font-semibold flex items-center space-x-1.5 transition-all duration-200 ${
                              item.available === false
                                ? 'bg-ivory-200 text-stone-400 cursor-not-allowed'
                                : 'bg-espresso-950 text-champagne-300 hover:bg-espresso-800 shadow-sm hover:scale-[1.02]'
                            }`}
                          >
                            {addingId === item.id ? (
                              <Loader2 className="w-3.5 h-3.5 animate-spin" />
                            ) : (
                              <>
                                <Plus className="w-3.5 h-3.5" />
                                <span>Add</span>
                              </>
                            )}
                          </button>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </section>
            ))}
          </div>
        )}
      </main>

      {/* Cart Conflict Modal */}
      {conflictModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-espresso-950/70 backdrop-blur-sm">
          <div className="bg-white rounded-3xl max-w-sm w-full p-8 shadow-2xl space-y-6 border border-ivory-300 text-center">
            <div className="w-12 h-12 rounded-full bg-champagne-100 text-champagne-700 flex items-center justify-center mx-auto border border-champagne-300">
              <AlertTriangle className="w-5 h-5" />
            </div>
            <div>
              <h3 className="font-serif text-xl font-medium text-espresso-950">Switch Table?</h3>
              <p className="text-xs text-stone-500 mt-2 font-light leading-relaxed">
                Your current selection contains plates from another kitchen. Would you like to clear your order and indulge in {restaurant.name}?
              </p>
            </div>
            <div className="grid grid-cols-2 gap-3 pt-2">
              <button
                onClick={() => setConflictModal(false)}
                className="py-2.5 px-4 text-xs uppercase tracking-wider font-semibold text-stone-600 bg-ivory-200 hover:bg-ivory-300 rounded-full transition-colors"
              >
                Keep Current
              </button>
              <button
                onClick={handleClearAndAdd}
                className="py-2.5 px-4 text-xs uppercase tracking-wider font-semibold text-ivory-100 bg-espresso-950 hover:bg-espresso-800 rounded-full shadow-sm transition-colors"
              >
                Start Anew
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
