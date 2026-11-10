import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Sparkles, Lock, Mail, User, Phone, Loader2, ArrowRight } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export default function RegisterPage() {
  const { register } = useAuth();
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    password: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await register(formData);
      navigate('/');
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-[85vh] flex items-center justify-center px-4 py-16">
      <div className="max-w-md w-full bg-white rounded-3xl p-10 border border-ivory-300 shadow-chic space-y-8">
        <div className="text-center space-y-3">
          <div className="w-12 h-12 bg-espresso-950 rounded-full flex items-center justify-center text-champagne-300 mx-auto border border-champagne-400/40 shadow-sm">
            <Sparkles className="w-5 h-5" />
          </div>
          <span className="text-[10px] uppercase tracking-[0.25em] text-champagne-600 font-semibold block">
            Membership Application
          </span>
          <h2 className="font-serif text-3xl font-normal text-espresso-950">Join Épicure</h2>
          <p className="text-xs text-stone-500 font-light">Exclusive access to bespoke restaurants and fast deliveries</p>
        </div>

        {error && (
          <div className="p-3.5 bg-rose-50 text-rose-800 text-xs rounded-xl border border-rose-200">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="block text-[11px] uppercase tracking-widest font-semibold text-espresso-800 mb-1.5">
                First Name *
              </label>
              <div className="relative">
                <User className="w-4 h-4 text-stone-400 absolute left-3.5 top-3.5" />
                <input
                  type="text"
                  name="firstName"
                  required
                  value={formData.firstName}
                  onChange={handleChange}
                  placeholder="Jean"
                  className="w-full pl-10 pr-3 py-2.5 text-xs border border-ivory-300 rounded-full focus:ring-1 focus:ring-champagne-500 focus:border-champagne-500 bg-ivory-50/50"
                />
              </div>
            </div>

            <div>
              <label className="block text-[11px] uppercase tracking-widest font-semibold text-espresso-800 mb-1.5">
                Last Name *
              </label>
              <input
                type="text"
                name="lastName"
                required
                value={formData.lastName}
                onChange={handleChange}
                placeholder="Dupont"
                className="w-full px-4 py-2.5 text-xs border border-ivory-300 rounded-full focus:ring-1 focus:ring-champagne-500 focus:border-champagne-500 bg-ivory-50/50"
              />
            </div>
          </div>

          <div>
            <label className="block text-[11px] uppercase tracking-widest font-semibold text-espresso-800 mb-1.5">
              Email Address *
            </label>
            <div className="relative">
              <Mail className="w-4 h-4 text-stone-400 absolute left-3.5 top-3.5" />
              <input
                type="email"
                name="email"
                required
                value={formData.email}
                onChange={handleChange}
                placeholder="patron@example.com"
                className="w-full pl-10 pr-4 py-2.5 text-xs border border-ivory-300 rounded-full focus:ring-1 focus:ring-champagne-500 focus:border-champagne-500 bg-ivory-50/50"
              />
            </div>
          </div>

          <div>
            <label className="block text-[11px] uppercase tracking-widest font-semibold text-espresso-800 mb-1.5">
              Phone Number *
            </label>
            <div className="relative">
              <Phone className="w-4 h-4 text-stone-400 absolute left-3.5 top-3.5" />
              <input
                type="tel"
                name="phoneNumber"
                required
                value={formData.phoneNumber}
                onChange={handleChange}
                placeholder="+1 555 0199"
                className="w-full pl-10 pr-4 py-2.5 text-xs border border-ivory-300 rounded-full focus:ring-1 focus:ring-champagne-500 focus:border-champagne-500 bg-ivory-50/50"
              />
            </div>
          </div>

          <div>
            <label className="block text-[11px] uppercase tracking-widest font-semibold text-espresso-800 mb-1.5">
              Password *
            </label>
            <div className="relative">
              <Lock className="w-4 h-4 text-stone-400 absolute left-3.5 top-3.5" />
              <input
                type="password"
                name="password"
                required
                minLength={6}
                value={formData.password}
                onChange={handleChange}
                placeholder="Minimum 6 characters"
                className="w-full pl-10 pr-4 py-2.5 text-xs border border-ivory-300 rounded-full focus:ring-1 focus:ring-champagne-500 focus:border-champagne-500 bg-ivory-50/50"
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full py-3.5 bg-espresso-950 hover:bg-espresso-800 text-champagne-300 font-medium rounded-full shadow-sm flex items-center justify-center space-x-2 text-xs uppercase tracking-[0.18em] transition-all duration-200 disabled:opacity-50 mt-6"
          >
            {loading ? (
              <Loader2 className="w-4 h-4 animate-spin text-champagne-300" />
            ) : (
              <>
                <span>Submit Membership</span>
                <ArrowRight className="w-3.5 h-3.5" />
              </>
            )}
          </button>
        </form>

        <p className="text-center text-xs text-stone-500 font-light pt-4 border-t border-ivory-200">
          Already a member?{' '}
          <Link to="/login" className="font-semibold text-espresso-950 underline underline-offset-4 hover:text-champagne-600">
            Sign In
          </Link>
        </p>
      </div>
    </div>
  );
}
