import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { X, Plus, Minus, Trash2, ShoppingBag, ArrowRight, Loader2, MapPin, CheckCircle, Sparkles } from 'lucide-react';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import { orderApi } from '../api/client';

export default function CartDrawer() {
  const { cart, isCartOpen, closeCart, updateQuantity, removeItem, clearCart, fetchCart } = useCart();
  const { user } = useAuth();
  const navigate = useNavigate();

  const [isCheckingOut, setIsCheckingOut] = useState(false);
  const [deliveryAddress, setDeliveryAddress] = useState('');
  const [deliveryNotes, setDeliveryNotes] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [orderSuccess, setOrderSuccess] = useState(null);
  const [error, setError] = useState('');

  if (!isCartOpen) return null;

  const items = cart?.items || [];
  const totalAmount = cart?.totalAmount || 0;

  const handleStartCheckout = () => {
    if (!user) {
      closeCart();
      navigate('/login?redirect=checkout');
      return;
    }
    setError('');
    setIsCheckingOut(true);
  };

  const handlePlaceOrder = async (e) => {
    e.preventDefault();
    if (!deliveryAddress.trim()) {
      setError('Please provide a destination address for delivery');
      return;
    }

    try {
      setSubmitting(true);
      setError('');
      const res = await orderApi.checkout({
        deliveryAddress: deliveryAddress.trim(),
        deliveryNotes: deliveryNotes.trim() || undefined,
      });

      setOrderSuccess(res.data);
      await fetchCart();
      setTimeout(() => {
        setIsCheckingOut(false);
        setOrderSuccess(null);
        closeCart();
        navigate('/orders');
      }, 1600);
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to place order');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 overflow-hidden">
      {/* Backdrop */}
      <div
        className="absolute inset-0 bg-espresso-950/60 backdrop-blur-sm transition-opacity duration-300"
        onClick={closeCart}
      />

      <div className="fixed inset-y-0 right-0 max-w-full flex pl-8 sm:pl-12">
        <div className="w-screen max-w-md bg-ivory-50 shadow-2xl flex flex-col border-l border-ivory-300">
          {/* Header */}
          <div className="p-6 border-b border-ivory-300 flex items-center justify-between bg-ivory-100">
            <div className="flex items-center space-x-3">
              <ShoppingBag className="w-5 h-5 text-espresso-900 stroke-[1.75]" />
              <div>
                <h2 className="font-serif text-xl font-normal text-espresso-950">Your Selection</h2>
                <span className="text-[10px] uppercase tracking-widest text-champagne-600 font-semibold block">
                  {items.length} {items.length === 1 ? 'Dish' : 'Dishes'} Chosen
                </span>
              </div>
            </div>
            <button
              onClick={closeCart}
              className="p-2 text-stone-400 hover:text-espresso-950 rounded-full hover:bg-ivory-200 transition-colors"
            >
              <X className="w-5 h-5" />
            </button>
          </div>

          {/* Success Screen */}
          {orderSuccess ? (
            <div className="flex-1 flex flex-col items-center justify-center p-8 text-center space-y-4">
              <div className="w-16 h-16 bg-champagne-100 text-champagne-700 rounded-full flex items-center justify-center border border-champagne-300 shadow-sm">
                <Sparkles className="w-8 h-8" />
              </div>
              <h3 className="font-serif text-2xl font-normal text-espresso-950">Order Confirmed</h3>
              <p className="text-xs text-stone-500 font-light max-w-xs leading-relaxed">
                Order #{orderSuccess.id} has been transmitted to the executive chef for preparation.
              </p>
              <p className="text-[11px] uppercase tracking-widest text-champagne-600 font-semibold pt-2">
                Presenting your receipt...
              </p>
            </div>
          ) : (
            <>
              {/* Main Content Area */}
              <div className="flex-1 overflow-y-auto p-6 divide-y divide-ivory-200">
                {items.length === 0 ? (
                  <div className="h-full flex flex-col items-center justify-center text-center py-16">
                    <div className="w-16 h-16 rounded-full bg-ivory-200 text-stone-400 flex items-center justify-center mb-4">
                      <ShoppingBag className="w-6 h-6 stroke-[1.5]" />
                    </div>
                    <h3 className="font-serif text-lg font-normal text-espresso-900">Your bag is empty</h3>
                    <p className="text-xs text-stone-500 mt-2 max-w-xs font-light leading-relaxed">
                      Explore our curated dining rooms and indulge in dishes crafted with exceptional mastery.
                    </p>
                  </div>
                ) : !isCheckingOut ? (
                  // Items List
                  <div className="space-y-4 pt-2">
                    {items.map((item) => (
                      <div key={item.id} className="flex items-center justify-between py-2.5">
                        <div className="flex-1 pr-3">
                          <h4 className="font-serif text-sm font-medium text-espresso-950">{item.menuItemName}</h4>
                          <span className="text-xs text-stone-500 font-light">
                            ${Number(item.unitPrice).toFixed(2)}
                          </span>
                        </div>

                        {/* Quantity Counter */}
                        <div className="flex items-center space-x-1.5 bg-ivory-200/80 rounded-full p-1 border border-ivory-300">
                          <button
                            onClick={() => updateQuantity(item.id, item.quantity - 1)}
                            className="w-5 h-5 flex items-center justify-center hover:bg-white rounded-full text-stone-700 transition-colors shadow-xs"
                          >
                            <Minus className="w-3 h-3" />
                          </button>
                          <span className="text-xs font-semibold w-5 text-center text-espresso-900">{item.quantity}</span>
                          <button
                            onClick={() => updateQuantity(item.id, item.quantity + 1)}
                            className="w-5 h-5 flex items-center justify-center hover:bg-white rounded-full text-stone-700 transition-colors shadow-xs"
                          >
                            <Plus className="w-3 h-3" />
                          </button>
                        </div>

                        {/* Subtotal */}
                        <div className="w-16 text-right font-sans font-semibold text-xs text-espresso-900 pl-3">
                          ${Number(item.subTotal).toFixed(2)}
                        </div>

                        {/* Remove */}
                        <button
                          onClick={() => removeItem(item.id)}
                          className="p-1 text-stone-300 hover:text-rose-500 transition-colors ml-1"
                          title="Remove item"
                        >
                          <Trash2 className="w-4 h-4 stroke-[1.5]" />
                        </button>
                      </div>
                    ))}
                  </div>
                ) : (
                  // Checkout Form
                  <form id="checkout-form" onSubmit={handlePlaceOrder} className="space-y-5 pt-2">
                    <div className="flex items-center justify-between pb-3 border-b border-ivory-300">
                      <h3 className="font-serif text-base font-medium text-espresso-950">Destination & Details</h3>
                      <button
                        type="button"
                        onClick={() => setIsCheckingOut(false)}
                        className="text-[10px] uppercase tracking-wider font-semibold text-champagne-600 hover:underline"
                      >
                        Edit Selection
                      </button>
                    </div>

                    {error && (
                      <div className="p-3 bg-rose-50 text-rose-700 text-xs rounded-xl border border-rose-200">
                        {error}
                      </div>
                    )}

                    <div>
                      <label className="block text-[11px] uppercase tracking-widest font-semibold text-espresso-800 mb-1.5">
                        Delivery Residence / Address *
                      </label>
                      <div className="relative">
                        <MapPin className="w-4 h-4 absolute left-3 top-3 text-stone-400" />
                        <textarea
                          rows={3}
                          value={deliveryAddress}
                          onChange={(e) => setDeliveryAddress(e.target.value)}
                          placeholder="Residence, Suite, Street, Postal Code"
                          required
                          className="w-full pl-9 pr-3 py-2 text-xs border border-ivory-300 rounded-xl focus:ring-1 focus:ring-champagne-500 focus:border-champagne-500 bg-white"
                        />
                      </div>
                    </div>

                    <div>
                      <label className="block text-[11px] uppercase tracking-widest font-semibold text-espresso-800 mb-1.5">
                        Special Instructions (Optional)
                      </label>
                      <input
                        type="text"
                        value={deliveryNotes}
                        onChange={(e) => setDeliveryNotes(e.target.value)}
                        placeholder="e.g. Concierge delivery, gate access code"
                        className="w-full px-3 py-2 text-xs border border-ivory-300 rounded-xl focus:ring-1 focus:ring-champagne-500 focus:border-champagne-500 bg-white"
                      />
                    </div>

                    <div className="p-4 bg-champagne-100/60 rounded-2xl border border-champagne-300/60 text-xs text-stone-700 space-y-1">
                      <span className="font-serif font-medium text-espresso-950 block">Payment Method</span>
                      <p className="text-[11px] text-stone-500 font-light">
                        Settled via Cash or Card upon arrival by white-glove courier.
                      </p>
                    </div>
                  </form>
                )}
              </div>

              {/* Drawer Footer */}
              {items.length > 0 && (
                <div className="p-6 border-t border-ivory-300 bg-ivory-100 space-y-4">
                  <div className="space-y-1.5 text-xs text-stone-600 font-light">
                    <div className="flex justify-between">
                      <span>Plates Subtotal</span>
                      <span className="font-semibold text-espresso-950">${Number(totalAmount).toFixed(2)}</span>
                    </div>
                    <div className="flex justify-between">
                      <span>Courier Concierge</span>
                      <span className="text-emerald-700 font-medium uppercase text-[10px] tracking-wider">Complimentary</span>
                    </div>
                  </div>

                  <div className="flex justify-between text-base font-normal text-espresso-950 pt-3 border-t border-ivory-300">
                    <span className="font-serif text-lg">Total</span>
                    <span className="font-sans font-bold text-lg text-espresso-950">${Number(totalAmount).toFixed(2)}</span>
                  </div>

                  {!isCheckingOut ? (
                    <div className="space-y-2 pt-2">
                      <button
                        onClick={handleStartCheckout}
                        className="w-full py-3.5 bg-espresso-950 hover:bg-espresso-800 text-champagne-300 font-medium rounded-full shadow-sm flex items-center justify-center space-x-2 text-xs uppercase tracking-[0.18em] transition-all duration-200"
                      >
                        <span>Confirm Selection</span>
                        <ArrowRight className="w-3.5 h-3.5" />
                      </button>
                      <button
                        onClick={clearCart}
                        className="w-full py-1.5 text-[10px] uppercase tracking-widest font-semibold text-stone-400 hover:text-rose-600 text-center transition-colors"
                      >
                        Discard Selection
                      </button>
                    </div>
                  ) : (
                    <button
                      type="submit"
                      form="checkout-form"
                      disabled={submitting}
                      className="w-full py-3.5 bg-espresso-950 hover:bg-espresso-800 text-champagne-300 font-medium rounded-full shadow-sm flex items-center justify-center space-x-2 text-xs uppercase tracking-[0.18em] transition-all duration-200 disabled:opacity-50"
                    >
                      {submitting ? (
                        <>
                          <Loader2 className="w-4 h-4 animate-spin text-champagne-300" />
                          <span>Preparing Order...</span>
                        </>
                      ) : (
                        <span>Transmit Order (${Number(totalAmount).toFixed(2)})</span>
                      )}
                    </button>
                  )}
                </div>
              )}
            </>
          )}
        </div>
      </div>
    </div>
  );
}
