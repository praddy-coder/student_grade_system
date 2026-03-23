import { useState } from 'react';
import { Outlet } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import Sidebar from './Sidebar';
import Navbar from './Navbar';

export default function DashboardLayout() {
  const [examType, setExamType] = useState('CT1');

  return (
    <div className="flex min-h-screen">
      <Sidebar />
      <div className="flex-1 flex flex-col min-h-screen">
        <Navbar examType={examType} setExamType={setExamType} />
        <main className="flex-1 p-6">
          <AnimatePresence mode="wait">
            <motion.div
              key={examType}
              initial={{ opacity: 0, y: 10 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -10 }}
              transition={{ duration: 0.3 }}
            >
              <Outlet context={{ examType }} />
            </motion.div>
          </AnimatePresence>
        </main>
      </div>
    </div>
  );
}
