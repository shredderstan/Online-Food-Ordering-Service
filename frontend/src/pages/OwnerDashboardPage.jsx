import React, { useState, useEffect } from 'react';
import { ChefHat, Store, Package, Sparkles, AlertCircle, Loader2 } from 'lucide-react';
import { restaurantApi, orderApi } from '../api/client';
import { useAuth } from '../context/AuthContext';

const statusFlow = [
  'PENDING',
  'CONFIRMED',
  'PREPARING',
  'READY_FOR_PICKUP',
  'OUT_FOR_DELIVERY',
  'DELIVERED',
];

export default function OwnerDashboardPage() {
  const { user } = useAuth();
  const [restaurants, setRestaurants] = useState([]);
  const [selectedRestaurantId, setSelectedRestaurantId] = useState(null);
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [updatingId, setUpdatingId] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchMyRestaurants = async () => {
      try {
        setLoading(true);
        const res = await restaurantApi.getMyRestaurants();
        setRestaurants(res.data || []);
        if (res.data?.length > 0) {
          setSelectedRestaurantId(res.data[0].id);
        }
      } catch (err) {
        setError(err.response?.data?.message || 'Failed to load your culinary houses');
      } finally {
        setLoading(false);
      }
    };
    fetchMyRestaurants();
  }, []);

  const fetchOrders = async (restaurantId) => {
    if (!restaurantId) return;
    try {
      const res = await orderApi.getOrdersForRestaurant(restaurantId);
      setOrders(res.data || []);
    } catch (err) {
      console.error('Failed to load restaurant orders:', err);
    }
  };

  useEffect(() => {
    if (selectedRestaurantId) {
      fetchOrders(selectedRestaurantId);
    }
  }, [selectedRestaurantId]);

  const handleUpdateStatus = async (orderId, newStatus) => {
    try {
      setUpdatingId(orderId);
      await orderApi.updateStatus(orderId, newStatus);
      await fetchOrders(selectedRestaurantId);
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to update order status');
    } finally {
      setUpdatingId(null);
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
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
      <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4 mb-12 border-b border-ivory-300 pb-6">
        <div>
          <span className="text-[10px] uppercase tracking-[0.25em] text-champagne-600 font-semibold block mb-1">
            Executive Kitchen Station
          </span>
          <h1 className="font-serif text-3xl sm:text-4xl font-normal text-espresso-950">
            Live Service & Expediting
          </h1>
        </div>

        {restaurants.length > 1 && (
          <div className="flex items-center space-x-2">
            <Store className="w-4 h-4 text-stone-400" />
            <select
              value={selectedRestaurantId}
              onChange={(e) => setSelectedRestaurantId(Number(e.target.value))}
              className="text-xs uppercase tracking-wider font-semibold bg-white border border-ivory-300 rounded-full px-4 py-2 focus:ring-1 focus:ring-champagne-500"
            >
              {restaurants.map((r) => (
                <option key={r.id} value={r.id}>
                  {r.name}
                </option>
              ))}
            </select>
          </div>
        )}
      </div>

      {restaurants.length === 0 ? (
        <div className="bg-white rounded-3xl border border-ivory-300 p-16 text-center max-w-md mx-auto my-12 shadow-chic">
          <Store className="w-12 h-12 text-stone-300 mx-auto mb-4 stroke-[1.25]" />
          <h3 className="font-serif text-xl font-normal text-espresso-950">No Establishments Registered</h3>
          <p className="text-xs text-stone-500 mt-2 font-light">
            You do not currently possess active kitchens associated with this profile.
          </p>
        </div>
      ) : orders.length === 0 ? (
        <div className="bg-white rounded-3xl border border-ivory-300 p-16 text-center max-w-md mx-auto my-12 shadow-chic">
          <Sparkles className="w-10 h-10 text-champagne-600 mx-auto mb-4" />
          <h3 className="font-serif text-xl font-normal text-espresso-950">All Orders Expedited</h3>
          <p className="text-xs text-stone-500 mt-2 font-light">
            Incoming guest orders will display here in real time as patrons submit requests.
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          {orders.map((order) => {
            const currentIndex = statusFlow.indexOf(order.status);
            const nextStatus = currentIndex >= 0 && currentIndex < statusFlow.length - 1 ? statusFlow[currentIndex + 1] : null;

            return (
              <div
                key={order.id}
                className="bg-white rounded-3xl border border-ivory-300 shadow-chic p-6 flex flex-col justify-between space-y-6"
              >
                <div className="space-y-4">
                  <div className="flex items-center justify-between pb-3 border-b border-ivory-200">
                    <span className="font-mono text-xs text-stone-400">Order #{order.id}</span>
                    <span className="text-[10px] uppercase tracking-widest font-semibold px-3 py-1 rounded-full bg-champagne-100 text-champagne-800 border border-champagne-300">
                      {order.status}
                    </span>
                  </div>

                  <div>
                    <h3 className="font-serif text-xl font-normal text-espresso-950">{order.customerName}</h3>
                    <p className="text-xs text-stone-500 font-light">{order.customerEmail}</p>
                    <p className="text-xs text-stone-600 mt-2 font-light">📍 {order.deliveryAddress}</p>
                    {order.deliveryNotes && (
                      <p className="text-xs italic text-champagne-800 bg-champagne-100/60 rounded-xl p-2.5 mt-2 border border-champagne-300/40">
                        Chef Note: {order.deliveryNotes}
                      </p>
                    )}
                  </div>

                  {/* Items list */}
                  <div className="pt-2 border-t border-ivory-200 space-y-2">
                    {order.items?.map((item) => (
                      <div key={item.id} className="flex justify-between text-xs">
                        <span className="font-serif text-espresso-900">
                          {item.quantity}x {item.itemName}
                        </span>
                        <span className="font-sans font-semibold text-stone-600">${Number(item.subTotal).toFixed(2)}</span>
                      </div>
                    ))}
                    <div className="pt-2 border-t border-ivory-200 flex justify-between text-xs font-semibold text-espresso-950">
                      <span>Total Check</span>
                      <span>${Number(order.totalAmount).toFixed(2)}</span>
                    </div>
                  </div>
                </div>

                {/* Status action buttons */}
                {order.status !== 'DELIVERED' && order.status !== 'CANCELLED' && (
                  <div className="pt-4 border-t border-ivory-200 space-y-2">
                    {nextStatus && (
                      <button
                        onClick={() => handleUpdateStatus(order.id, nextStatus)}
                        disabled={updatingId === order.id}
                        className="w-full py-3 bg-espresso-950 hover:bg-espresso-800 text-champagne-300 font-medium rounded-full text-xs uppercase tracking-[0.16em] shadow-sm flex items-center justify-center space-x-1.5 transition-colors"
                      >
                        {updatingId === order.id ? (
                          <Loader2 className="w-3.5 h-3.5 animate-spin text-champagne-300" />
                        ) : (
                          <span>Advance to {nextStatus.replace(/_/g, ' ')}</span>
                        )}
                      </button>
                    )}

                    <button
                      onClick={() => handleUpdateStatus(order.id, 'CANCELLED')}
                      disabled={updatingId === order.id}
                      className="w-full py-1.5 text-[10px] uppercase tracking-wider font-semibold text-stone-400 hover:text-rose-600 transition-colors"
                    >
                      Revoke / Refuse Order
                    </button>
                  </div>
                )}
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}
