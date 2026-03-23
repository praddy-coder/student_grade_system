import { useState, useEffect } from 'react';
import { useOutletContext } from 'react-router-dom';
import { motion } from 'framer-motion';
import { useAuth } from '../context/AuthContext';
import { marksAPI, analyticsAPI } from '../services/api';
import MarkCell from '../components/ui/MarkCell';
import AnimatedCounter from '../components/ui/AnimatedCounter';
import SkeletonLoader from '../components/ui/SkeletonLoader';
import StudentRankList from '../components/dashboard/StudentRankList';
import { Trophy, BookOpen, TrendingUp, AlertTriangle } from 'lucide-react';

const container = {
  hidden: { opacity: 0 },
  show: { opacity: 1, transition: { staggerChildren: 0.1 } }
};
const item = {
  hidden: { opacity: 0, y: 20 },
  show: { opacity: 1, y: 0 }
};

export default function StudentDashboard() {
  const { examType } = useOutletContext();
  const { user } = useAuth();
  const [marks, setMarks] = useState([]);
  const [ranking, setRanking] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetch = async () => {
      setLoading(true);
      try {
        const [marksRes, analyticsRes] = await Promise.all([
          marksAPI.getStudentMarks(user.refId),
          analyticsAPI.getRankings()
        ]);
        setMarks(marksRes.data);
        const myRank = analyticsRes.data.rankings.find(r => r.regNo === user.refId);
        setRanking(myRank);
      } catch (e) {
        console.error(e);
      } finally {
        setLoading(false);
      }
    };
    fetch();
  }, [user.refId]);

  if (loading) return <SkeletonLoader rows={6} cols={4} />;

  const totalMarks = marks.reduce((sum, m) => sum + (examType === 'CT1' ? m.ct1Marks : m.ct2Marks), 0);
  const maxMarks = marks.length * 60;
  const percentage = maxMarks > 0 ? (totalMarks / maxMarks * 100) : 0;
  const failCount = marks.filter(m => (examType === 'CT1' ? m.ct1Marks : m.ct2Marks) < 30).length;

  return (
    <motion.div variants={container} initial="hidden" animate="show" className="space-y-6">
      {/* Welcome Card */}
      <motion.div variants={item} className="glass p-6 relative overflow-hidden">
        <div className="absolute top-0 right-0 w-64 h-64 bg-gradient-to-bl from-indigo-500/10 to-transparent rounded-full -translate-y-1/2 translate-x-1/4" />
        <h1 className="text-2xl font-bold text-slate-100">
          Welcome back, <span className="bg-gradient-to-r from-indigo-400 to-purple-400 bg-clip-text text-transparent">{user.name}</span>
        </h1>
        <p className="text-slate-500 mt-1 text-sm">Reg No: {user.refId} · Viewing {examType} Marks</p>
      </motion.div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        {[
          { label: 'Overall Rank', value: ranking?.rank || '-', icon: Trophy, color: 'from-amber-500 to-orange-600', glow: 'shadow-amber-500/20' },
          { label: `${examType} Total`, value: totalMarks, icon: BookOpen, color: 'from-indigo-500 to-purple-600', glow: 'shadow-indigo-500/20', suffix: `/${maxMarks}` },
          { label: 'Percentage', value: percentage.toFixed(1), icon: TrendingUp, color: 'from-emerald-500 to-green-600', glow: 'shadow-emerald-500/20', suffix: '%' },
          { label: 'At Risk', value: failCount, icon: AlertTriangle, color: 'from-red-500 to-rose-600', glow: 'shadow-red-500/20', suffix: ' subjects' },
        ].map((stat, i) => (
          <motion.div key={i} variants={item} className="glass p-5">
            <div className="flex items-center justify-between mb-3">
              <span className="text-xs text-slate-500 font-semibold uppercase tracking-wider">{stat.label}</span>
              <div className={`w-9 h-9 rounded-xl bg-gradient-to-br ${stat.color} flex items-center justify-center shadow-lg ${stat.glow}`}>
                <stat.icon size={17} className="text-white" />
              </div>
            </div>
            <div className="text-2xl font-bold text-slate-100">
              <AnimatedCounter value={stat.value} suffix={stat.suffix || ''} />
            </div>
          </motion.div>
        ))}
      </div>

      {/* Marks Table */}
      <motion.div variants={item} className="glass overflow-hidden">
        <div className="p-5 border-b border-indigo-500/10">
          <h3 className="text-sm font-bold text-slate-200">{examType} Subject Marks</h3>
        </div>
        <div className="overflow-x-auto">
          <table className="data-table">
            <thead>
              <tr>
                <th>Subject</th>
                <th>Marks</th>
                <th>Out Of</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {marks.map((m, i) => {
                const score = examType === 'CT1' ? m.ct1Marks : m.ct2Marks;
                return (
                  <motion.tr
                    key={m.id}
                    initial={{ opacity: 0, x: -20 }}
                    animate={{ opacity: 1, x: 0 }}
                    transition={{ delay: i * 0.05 }}
                  >
                    <td className="font-medium text-slate-300">{m.subjectName}</td>
                    <td><MarkCell value={score} /></td>
                    <td className="text-slate-500">60</td>
                    <td>
                      {score >= 35 ? (
                        <span className="px-2.5 py-1 rounded-lg text-xs font-bold bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">PASS</span>
                      ) : score >= 30 ? (
                        <span className="px-2.5 py-1 rounded-lg text-xs font-bold bg-amber-500/10 text-amber-400 border border-amber-500/20">AT RISK</span>
                      ) : (
                        <span className="px-2.5 py-1 rounded-lg text-xs font-bold bg-red-500/10 text-red-400 border border-red-500/20">FAIL</span>
                      )}
                    </td>
                  </motion.tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </motion.div>

      {/* Global Rank List */}
      <StudentRankList examType={examType} showFailOnly={false} />
    </motion.div>
  );
}
