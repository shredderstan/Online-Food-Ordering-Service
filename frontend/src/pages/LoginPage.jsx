import React, { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { Sparkles, Lock, Mail, Loader2, ArrowRight } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export default function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const searchParams = new URLSearchParams(location.search);
  const redirect = searchParams.get('redirect') || '/';

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await login(email, password);
      navigate(redirect);
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid email or password credentials');
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
            Member Access
          </span>
          <h2 className="font-serif text-3xl font-normal text-espresso-950">Welcome Back</h2>
          <p className="text-xs text-stone-500 font-light">Sign in to access your culinary folio and favorites</p>
        </div>

        {error && (
          <div className="p-3.5 bg-rose-50 text-rose-800 text-xs rounded-xl border border-rose-200">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-[11px] uppercase tracking-widest font-semibold text-espresso-800 mb-1.5">
              Email Address
            </label>
            <div className="relative">
              <Mail className="w-4 h-4 text-stone-400 absolute left-3.5 top-3.5" />
              <input
                type="email"
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="patron@example.com"
                className="w-full pl-10 pr-4 py-2.5 text-xs border border-ivory-300 rounded-full focus:ring-1 focus:ring-champagne-500 focus:border-champagne-500 bg-ivory-50/50"
              />
            </div>
          </div>

          <div>
            <label className="block text-[11px] uppercase tracking-widest font-semibold text-espresso-800 mb-1.5">
              Password
            </label>
            <div className="relative">
              <Lock className="w-4 h-4 text-stone-400 absolute left-3.5 top-3.5" />
              <input
                type="password"
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="••••••••"
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
                <span>Sign In</span>
                <ArrowRight className="w-3.5 h-3.5" />
              </>
            )}
          </button>
        </form>

        <p className="text-center text-xs text-stone-500 font-light pt-4 border-t border-ivory-200">
          Not yet a member?{' '}
          <Link to="/register" className="font-semibold text-espresso-950 underline underline-offset-4 hover:text-champagne-600">
            Request an Invitation
          </Link>
        </p>
      </div>
    </div>
  );
}
