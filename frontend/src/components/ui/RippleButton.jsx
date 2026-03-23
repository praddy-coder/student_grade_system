import { useRef } from 'react';
import { motion } from 'framer-motion';

export default function RippleButton({ children, onClick, className = '', variant = 'primary', ...props }) {
  const btnRef = useRef(null);

  const createRipple = (e) => {
    const btn = btnRef.current;
    const rect = btn.getBoundingClientRect();
    const ripple = document.createElement('span');
    const size = Math.max(rect.width, rect.height);
    ripple.style.width = ripple.style.height = `${size}px`;
    ripple.style.left = `${e.clientX - rect.left - size / 2}px`;
    ripple.style.top = `${e.clientY - rect.top - size / 2}px`;
    ripple.className = 'ripple';
    btn.appendChild(ripple);
    setTimeout(() => ripple.remove(), 600);
    onClick?.(e);
  };

  const variants = {
    primary: 'bg-gradient-to-r from-indigo-500 to-purple-600 hover:from-indigo-600 hover:to-purple-700 text-white shadow-lg shadow-indigo-500/20',
    secondary: 'bg-white/5 border border-indigo-500/20 text-slate-300 hover:bg-white/10',
    danger: 'bg-gradient-to-r from-red-500 to-rose-600 text-white shadow-lg shadow-red-500/20',
    success: 'bg-gradient-to-r from-emerald-500 to-green-600 text-white shadow-lg shadow-emerald-500/20',
  };

  return (
    <motion.button
      ref={btnRef}
      whileHover={{ scale: 1.02 }}
      whileTap={{ scale: 0.98 }}
      onClick={createRipple}
      className={`ripple-container px-5 py-2.5 rounded-xl font-semibold text-sm transition-all duration-200 cursor-pointer ${variants[variant]} ${className}`}
      {...props}
    >
      {children}
    </motion.button>
  );
}
