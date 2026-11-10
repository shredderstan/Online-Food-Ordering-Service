import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Search, MapPin, Sparkles, ArrowUpRight, Clock, Phone, AlertCircle } from 'lucide-react';
import { restaurantApi } from '../api/client';

const RESTAURANT_IMAGES = {
  'Bella Italia Trattoria': 'https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=900&q=80',
  'Burger & Co. Craft Shack': 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=900&q=80',
  'Sakura Sushi & Ramen': 'https://images.unsplash.com/photo-1579871494447-9811cf80d66c?auto=format&fit=crop&w=900&q=80',
};

const DEFAULT_IMAGE = 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=900&q=80';

export default function HomePage() {
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [selectedCity, setSelectedCity] = useState('ALL');
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchRestaurants = async () => {
      try {
        setLoading(true);
        const res = await restaurantApi.getAll();
        setRestaurants(res.data || []);
      } catch (err) {
        console.error('Failed to load restaurants:', err);
        setError('Could not connect to the backend server. Please make sure the Spring Boot service is active.');
      } finally {
        setLoading(false);
      }
    };
    fetchRestaurants();
  }, []);

  const cities = ['ALL', ...new Set(restaurants.map((r) => r.city).filter(Boolean))];

  const filtered = restaurants.filter((r) => {
    const matchesSearch =
      r.name?.toLowerCase().includes(search.toLowerCase()) ||
      r.description?.toLowerCase().includes(search.toLowerCase()) ||
      r.city?.toLowerCase().includes(search.toLowerCase());
    const matchesCity = selectedCity === 'ALL' || r.city === selectedCity;
    return matchesSearch && matchesCity;
  });

  return (
    <div className="min-h-screen pb-24">
      {/* Editorial Luxury Hero */}
      <section className="relative bg-espresso-950 text-ivory-100 py-24 sm:py-32 px-4 sm:px-6 lg:px-8 overflow-hidden border-b border-espresso-800">
        {/* Subtle Ambient Vignette & Grain */}
        <div className="absolute inset-0 opacity-20 bg-[radial-gradient(#CBB291_1px,transparent_1px)] [background-size:24px_24px]" />
        <div className="absolute -top-32 left-1/2 -translate-x-1/2 w-[700px] h-[350px] bg-champagne-500/10 rounded-full blur-[120px] pointer-events-none" />

        <div className="max-w-4xl mx-auto text-center relative z-10 space-y-8">
          <div className="inline-flex items-center space-x-2 border border-champagne-400/30 bg-espresso-900/60 backdrop-blur-md px-4 py-1.5 rounded-full">
            <Sparkles className="w-3.5 h-3.5 text-champagne-300" />
            <span className="text-[11px] uppercase tracking-[0.22em] text-champagne-200 font-medium">
              Curated Private Tables & Gastronomy
            </span>
          </div>

          <h1 className="font-serif text-4xl sm:text-6xl md:text-7xl font-normal tracking-tight leading-[1.08] text-ivory-50">
            The Art of Dining, <br />
            <span className="italic font-light text-champagne-300">Intimately Delivered.</span>
          </h1>

          <p className="font-sans text-base sm:text-lg text-ivory-300/80 max-w-xl mx-auto font-light leading-relaxed">
            Experience hand-selected dishes from the most distinguished kitchens, prepared with passion and served with effortless grace.
          </p>

          {/* Minimalist Search Console */}
          <div className="max-w-xl mx-auto pt-4">
            <div className="relative flex items-center bg-espresso-900/80 border border-champagne-500/30 rounded-full p-2 shadow-chic-dark backdrop-blur-md focus-within:border-champagne-400 transition-colors">
              <Search className="w-4 h-4 text-champagne-400 ml-4 flex-shrink-0" />
              <input
                type="text"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                placeholder="Search culinary ateliers, cuisines, or destinations..."
                className="w-full px-4 py-2 bg-transparent text-ivory-100 placeholder:text-ivory-400/50 text-xs sm:text-sm tracking-wide focus:outline-none"
              />
              {search && (
                <button
                  onClick={() => setSearch('')}
                  className="text-xs uppercase tracking-wider text-ivory-400 hover:text-ivory-100 mr-3 transition-colors"
                >
                  Clear
                </button>
              )}
            </div>
          </div>
        </div>
      </section>

      {/* Main Curated Showcase */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pt-16">
        {/* Destination Filters */}
        {cities.length > 2 && (
          <div className="flex items-center space-x-3 overflow-x-auto pb-4 mb-10 border-b border-ivory-300">
            <span className="text-[11px] uppercase tracking-[0.2em] text-stone-400 font-semibold pr-2">
              Destinations:
            </span>
            {cities.map((city) => (
              <button
                key={city}
                onClick={() => setSelectedCity(city)}
                className={`px-5 py-2 rounded-full text-xs uppercase tracking-widest transition-all duration-300 ${
                  selectedCity === city
                    ? 'bg-espresso-950 text-champagne-300 shadow-sm border border-champagne-400/40'
                    : 'bg-white text-stone-600 hover:text-espresso-900 border border-ivory-300 hover:border-champagne-300'
                }`}
              >
                {city}
              </button>
            ))}
          </div>
        )}

        <div className="flex flex-col sm:flex-row sm:items-end justify-between mb-12 gap-4">
          <div>
            <span className="text-[11px] uppercase tracking-[0.25em] text-champagne-600 font-semibold block mb-1">
              Select Houses
            </span>
            <h2 className="font-serif text-3xl sm:text-4xl font-normal text-espresso-950 tracking-tight">
              Featured Kitchens & Ateliers
            </h2>
          </div>
          <p className="text-xs uppercase tracking-widest text-stone-400">
            {filtered.length} {filtered.length === 1 ? 'Curated Table' : 'Curated Tables'}
          </p>
        </div>

        {/* Backend Warning */}
        {error && (
          <div className="mb-10 p-5 bg-stone-100 border border-stone-200 rounded-2xl flex items-start space-x-3 text-stone-800">
            <AlertCircle className="w-5 h-5 text-amber-700 flex-shrink-0 mt-0.5" />
            <div className="text-xs">
              <p className="font-semibold uppercase tracking-wider text-stone-900">Connection Notice</p>
              <p className="text-stone-600 mt-1">{error}</p>
            </div>
          </div>
        )}

        {/* Loading Skeleton */}
        {loading ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {[1, 2, 3].map((n) => (
              <div key={n} className="bg-white rounded-3xl p-6 border border-ivory-300 shadow-sm animate-pulse space-y-4">
                <div className="h-64 bg-ivory-200 rounded-2xl" />
                <div className="h-6 bg-ivory-200 rounded w-2/3" />
                <div className="h-4 bg-ivory-200 rounded w-1/2" />
              </div>
            ))}
          </div>
        ) : filtered.length === 0 ? (
          <div className="bg-white rounded-3xl border border-ivory-300 p-16 text-center max-w-md mx-auto my-12 shadow-chic">
            <div className="w-14 h-14 rounded-full bg-champagne-100 text-champagne-700 flex items-center justify-center mx-auto mb-4 border border-champagne-300">
              <Sparkles className="w-6 h-6" />
            </div>
            <h3 className="font-serif text-xl font-medium text-espresso-950">No tables match your search</h3>
            <p className="text-xs text-stone-500 mt-2 font-light">
              Clear your criteria to reveal other fine establishments.
            </p>
          </div>
        ) : (
          /* Editorial Restaurant Cards */
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {filtered.map((restaurant) => {
              const imageUrl = RESTAURANT_IMAGES[restaurant.name] || DEFAULT_IMAGE;

              return (
                <Link
                  key={restaurant.id}
                  to={`/restaurants/${restaurant.id}`}
                  className="group bg-white rounded-3xl border border-ivory-300 hover:border-champagne-400/80 shadow-chic hover:shadow-chic-hover transition-all duration-500 overflow-hidden flex flex-col hover:-translate-y-1"
                >
                  {/* Photo Container */}
                  <div className="h-64 relative overflow-hidden bg-espresso-950">
                    <img
                      src={imageUrl}
                      alt={restaurant.name}
                      className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-700 opacity-90 group-hover:opacity-100"
                    />
                    <div className="absolute inset-0 bg-gradient-to-t from-espresso-950/80 via-transparent to-black/20" />

                    {/* Top status tag */}
                    <div className="absolute top-4 left-4 right-4 flex items-center justify-between">
                      <span className="text-[10px] uppercase tracking-widest font-semibold px-3 py-1 rounded-full backdrop-blur-md bg-espresso-950/70 text-champagne-300 border border-champagne-400/30">
                        {restaurant.city || 'Exclusive'}
                      </span>
                      <span
                        className={`text-[10px] uppercase tracking-widest font-semibold px-3 py-1 rounded-full backdrop-blur-md ${
                          restaurant.active !== false
                            ? 'bg-emerald-950/80 text-emerald-300 border border-emerald-500/30'
                            : 'bg-stone-900/80 text-stone-400 border border-stone-700'
                        }`}
                      >
                        {restaurant.active !== false ? 'Accepting Orders' : 'Currently Resting'}
                      </span>
                    </div>

                    {/* Bottom overlay details */}
                    <div className="absolute bottom-4 left-4 right-4">
                      <h3 className="font-serif text-2xl font-normal text-ivory-50 group-hover:text-champagne-200 transition-colors">
                        {restaurant.name}
                      </h3>
                      <p className="text-xs text-ivory-300/80 flex items-center space-x-1.5 mt-1 font-light">
                        <MapPin className="w-3.5 h-3.5 text-champagne-400 flex-shrink-0" />
                        <span className="truncate">{restaurant.address}</span>
                      </p>
                    </div>
                  </div>

                  {/* Body & Story */}
                  <div className="p-6 flex-1 flex flex-col justify-between space-y-6">
                    <p className="text-xs text-stone-600 line-clamp-2 leading-relaxed font-light">
                      {restaurant.description}
                    </p>

                    <div className="pt-4 border-t border-ivory-200 flex items-center justify-between">
                      <span className="text-[11px] uppercase tracking-wider text-stone-400 font-medium">
                        {restaurant.phoneNumber || 'Concierge Service'}
                      </span>
                      <span className="inline-flex items-center space-x-1 text-xs uppercase tracking-widest font-semibold text-espresso-950 group-hover:text-champagne-700 transition-colors">
                        <span>Explore Menu</span>
                        <ArrowUpRight className="w-4 h-4 transition-transform group-hover:translate-x-0.5 group-hover:-translate-y-0.5" />
                      </span>
                    </div>
                  </div>
                </Link>
              );
            })}
          </div>
        )}
      </main>
    </div>
  );
}
