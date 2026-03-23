import { motion, AnimatePresence } from 'framer-motion';
import { Bell } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';

export default function Navbar({ examType, setExamType }) {
  const { user } = useAuth();

  return (
    <header className="sticky top-0 z-40 glass px-6 py-3 flex items-center justify-between"
      style={{ borderRadius: 0, borderBottom: '1px solid rgba(99, 102, 241, 0.1)' }}>

      <div>
        <h2 className="text-lg font-bold text-slate-100">
          {user?.role === 'STUDENT'
            ? 'Student Portal'
            : user?.role === 'STAFF'
            ? 'Staff Panel'
            : user?.role === 'CC'
            ? 'Class Coordinator'
            : 'HOD Dashboard'}
        </h2>
        <p className="text-xs text-slate-500">
          {new Date().toLocaleDateString('en-US', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })}
        </p>
      </div>

      <div className="flex items-center gap-4">
        {/* CT1/CT2 Toggle */}
        <div className="ct-toggle">
          <motion.div
            className="absolute top-[3px] bottom-[3px] rounded-[8px] bg-gradient-to-r from-indigo-500 to-purple-600"
            animate={{ left: examType === 'CT1' ? 3 : '50%', width: 'calc(50% - 3px)' }}
            transition={{ type: 'spring', stiffness: 300, damping: 30 }}
          />
          <button
            className={examType === 'CT1' ? 'active' : ''}
            onClick={() => setExamType('CT1')}
          >
            CT1
          </button>
          <button
            className={examType === 'CT2' ? 'active' : ''}
            onClick={() => setExamType('CT2')}
          >
            CT2
          </button>
        </div>

        {/* Notification bell */}
        <button className="relative p-2 rounded-xl hover:bg-white/5 transition-all text-slate-400 hover:text-slate-200">
          <Bell size={19} />
          <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-indigo-500 rounded-full"></span>
        </button>
      </div>
    </header>
  );
}
