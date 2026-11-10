import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Package, MapPin, Sparkles, Loader2, ArrowRight } from 'lucide-react';
import { orderApi } from '../api/client';
import { useAuth } from '../context/AuthContext';

const statusConfig = {
  PENDING: { label: 'Order Transmitted', bg: 'bg-amber-50 text-amber-800 border-amber-300' },
  CONFIRMED: { label: 'Kitchen Confirmed', bg: 'bg-stone-100 text-stone-800 border-stone-300' },
  PREPARING: { label: 'In Culinary Preparation', bg: 'bg-champagne-100 text-champagne-800 border-champagne-300' },
  READY_FOR_PICKUP: { label: 'Ready for Courier', bg: 'bg-purple-50 text-purple-800 border-purple-200' },
  OUT_FOR_DELIVERY: { label: 'Courier En Route', bg: 'bg-espresso-950 text-champagne-300 border-espresso-900' },
  DELIVERED: { label: 'Delivered to Table', bg: 'bg-emerald-50 text-emerald-800 border-emerald-300' },
  CANCELLED: { label: 'Cancelled', bg: 'bg-stone-100 text-stone-400 border-stone-200' },
};

export default function OrdersPage() {
  const { user } = useAuth();
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [cancellingId, setCancellingId] = useState(null);
  const [error, setError] = useState('');

  const fetchOrders = async () => {
    try {
      setLoading(true);
      const res = await orderApi.getMyOrders();
      setOrders(res.data || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch orders');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  const handleCancelOrder = async (orderId) => {
    if (!window.confirm('Do you wish to cancel this dining order?')) return;
    try {
      setCancellingId(orderId);
      await orderApi.cancelOrder(orderId);
      await fetchOrders();
    } catch (err) {
      alert(err.response?.data?.message || 'Could not cancel order');
    } finally {
      setCancellingId(null);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <Loader2 className="w-8 h-8 text-champagne-600 animate-spin" />
      </div>
    );
  }

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
      <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4 mb-12 border-b border-ivory-300 pb-6">
        <div>
          <span className="text-[10px] uppercase tracking-[0.25em] text-champagne-600 font-semibold block mb-1">
            Personal Registry
          </span>
          <h1 className="font-serif text-3xl sm:text-4xl font-normal text-espresso-950">
            Dining Folio & History
          </h1>
        </div>
        <p className="text-xs uppercase tracking-widest text-stone-400">
          {orders.length} {orders.length === 1 ? 'Folio Entry' : 'Folio Entries'}
        </p>
      </div>

      {error && (
        <div className="mb-8 p-4 bg-rose-50 text-rose-800 text-xs rounded-2xl border border-rose-200">
          {error}
        </div>
      )}

      {orders.length === 0 ? (
        <div className="bg-white rounded-3xl border border-ivory-300 p-16 text-center my-10 max-w-md mx-auto shadow-chic">
          <div className="w-14 h-14 rounded-full bg-champagne-100 text-champagne-700 flex items-center justify-center mx-auto mb-4 border border-champagne-300">
            <Package className="w-6 h-6 stroke-[1.5]" />
          </div>
          <h3 className="font-serif text-xl font-normal text-espresso-950">No Orders in Your Folio</h3>
          <p className="text-xs text-stone-500 mt-2 font-light leading-relaxed">
            Your dining experiences and deliveries will be elegantly recorded here.
          </p>
          <Link
            to="/"
            className="inline-block mt-6 px-6 py-2.5 bg-espresso-950 text-champagne-300 text-xs uppercase tracking-widest font-semibold rounded-full shadow-sm hover:bg-espresso-800 transition-colors"
          >
            Explore Curated Tables
          </Link>
        </div>
      ) : (
        <div className="space-y-8">
          {orders.map((order) => {
            const statusInfo = statusConfig[order.status] || {
              label: order.status,
              bg: 'bg-stone-100 text-stone-800 border-stone-200',
            };
            const isPending = order.status === 'PENDING';

            return (
              <div
                key={order.id}
                className="bg-white rounded-3xl border border-ivory-300 shadow-chic overflow-hidden"
              >
                {/* Header */}
                <div className="p-6 border-b border-ivory-200 flex flex-wrap items-center justify-between gap-4 bg-ivory-50/50">
                  <div className="space-y-1">
                    <div className="flex items-center space-x-2 text-xs text-stone-400 font-light">
                      <span className="font-mono uppercase tracking-wider">Folio #{order.id}</span>
                      <span>•</span>
                      <span>{order.createdAt ? new Date(order.createdAt).toLocaleDateString(undefined, { month: 'short', day: 'numeric', year: 'numeric', hour: '2-digit', minute: '2-digit' }) : ''}</span>
                    </div>
                    <h3 className="font-serif text-2xl font-normal text-espresso-950">{order.restaurantName}</h3>
                  </div>

                  <div className="flex items-center space-x-3">
                    <span
                      className={`text-[10px] uppercase tracking-widest font-semibold px-3 py-1 rounded-full border ${statusInfo.bg}`}
                    >
                      {statusInfo.label}
                    </span>

                    {isPending && (
                      <button
                        onClick={() => handleCancelOrder(order.id)}
                        disabled={cancellingId === order.id}
                        className="text-[10px] uppercase tracking-widest font-semibold text-rose-600 hover:text-rose-700 bg-rose-50 hover:bg-rose-100 px-3 py-1 rounded-full border border-rose-200 transition-colors disabled:opacity-50"
                      >
                        {cancellingId === order.id ? 'Revoking...' : 'Revoke Order'}
                      </button>
                    )}
                  </div>
                </div>

                {/* Items Breakdown */}
                <div className="p-6 divide-y divide-ivory-200 space-y-4">
                  <div className="space-y-3">
                    {order.items?.map((item) => (
                      <div key={item.id} className="flex justify-between items-center text-xs">
                        <div className="flex items-center space-x-3 text-espresso-950">
                          <span className="font-serif font-medium text-champagne-700">{item.quantity}x</span>
                          <span className="font-serif text-sm font-normal">{item.itemName}</span>
                        </div>
                        <span className="font-sans font-semibold text-stone-700">
                          ${Number(item.subTotal).toFixed(2)}
                        </span>
                      </div>
                    ))}
                  </div>

                  <div className="pt-4 flex flex-col sm:flex-row sm:items-center justify-between gap-2 text-xs text-stone-500 font-light">
                    <div className="flex items-center space-x-1.5">
                      <MapPin className="w-3.5 h-3.5 text-champagne-600 flex-shrink-0" />
                      <span>Destination: <strong className="font-medium text-espresso-900">{order.deliveryAddress}</strong></span>
                    </div>
                    {order.deliveryNotes && (
                      <span className="italic text-stone-400">"{order.deliveryNotes}"</span>
                    )}
                  </div>
                </div>

                {/* Footer Total */}
                <div className="px-6 py-4 bg-ivory-100/70 border-t border-ivory-200 flex items-center justify-between">
                  <span className="text-[10px] uppercase tracking-[0.2em] font-semibold text-stone-500">Total Settled</span>
                  <span className="font-sans font-bold text-base text-espresso-950">
                    ${Number(order.totalAmount).toFixed(2)}
                  </span>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}
