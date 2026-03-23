import { useState, useEffect } from 'react';
import { useOutletContext } from 'react-router-dom';
import { motion } from 'framer-motion';
import { analyticsAPI, exportAPI } from '../services/api';
import AnimatedCounter from '../components/ui/AnimatedCounter';
import RippleButton from '../components/ui/RippleButton';
import SkeletonLoader from '../components/ui/SkeletonLoader';
import StudentRankList from '../components/dashboard/StudentRankList';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts';
import { Users, TrendingUp, Award, BarChart3, Download, FileText } from 'lucide-react';
import toast from 'react-hot-toast';

const container = { hidden: { opacity: 0 }, show: { opacity: 1, transition: { staggerChildren: 0.1 } } };
const item = { hidden: { opacity: 0, y: 20 }, show: { opacity: 1, y: 0 } };

const CHART_COLORS = ['#6366f1', '#8b5cf6', '#a855f7', '#06b6d4', '#22c55e'];
const PIE_COLORS = ['#22c55e', '#ef4444'];

export default function HODDashboard() {
  const { examType } = useOutletContext();
  const [analytics, setAnalytics] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetch = async () => {
      setLoading(true);
      try {
        const { data } = await analyticsAPI.getRankings('CSE');
        setAnalytics(data);
      } catch (e) { console.error(e); }
      finally { setLoading(false); }
    };
    fetch();
  }, []);

  const handleExport = async (format) => {
    try {
      await exportAPI.downloadReport(format, 'CSE');
      toast.success(`${format.toUpperCase()} report downloaded!`);
    } catch (e) { toast.error('Export failed'); }
  };

  if (loading) return <SkeletonLoader rows={6} cols={4} />;
  if (!analytics) return null;

  // Prepare chart data
  const subjectData = Object.values(analytics.subjectAnalytics || {}).map(sa => ({
    name: sa.subjectName.split(' ').slice(0, 2).join(' '),
    fullName: sa.subjectName,
    avgCt1: sa.averageCt1,
    avgCt2: sa.averageCt2,
    passRate: sa.passPercentage,
  }));

  const pieData = [
    { name: 'Pass', value: analytics.totalPass },
    { name: 'Fail', value: analytics.totalFail },
  ];

  const topPerformers = analytics.topPerformers || [];

  return (
    <motion.div variants={container} initial="hidden" animate="show" className="space-y-6">
      {/* Stats Row */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        {[
          { label: 'Total Students', value: analytics.totalStudents, icon: Users, color: 'from-indigo-500 to-purple-600' },
          { label: 'Overall Pass %', value: analytics.overallPassPercentage, icon: TrendingUp, color: 'from-emerald-500 to-green-600', suffix: '%', dec: 1 },
          { label: 'Top Performer', value: topPerformers[0]?.name || '-', icon: Award, color: 'from-amber-500 to-orange-600', isText: true },
          { label: 'Subjects', value: Object.keys(analytics.subjectAnalytics || {}).length, icon: BarChart3, color: 'from-cyan-500 to-blue-600' },
        ].map((stat, i) => (
          <motion.div key={i} variants={item} className="glass p-5">
            <div className="flex items-center justify-between mb-3">
              <span className="text-xs text-slate-500 font-semibold uppercase tracking-wider">{stat.label}</span>
              <div className={`w-9 h-9 rounded-xl bg-gradient-to-br ${stat.color} flex items-center justify-center`}>
                <stat.icon size={17} className="text-white" />
              </div>
            </div>
            <div className="text-2xl font-bold text-slate-100">
              {stat.isText ? (
                <span className="text-lg">{stat.value}</span>
              ) : (
                <AnimatedCounter value={stat.value} suffix={stat.suffix || ''} decimals={stat.dec || 0} />
              )}
            </div>
          </motion.div>
        ))}
      </div>

      {/* Charts Row */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Bar Chart — Subject Performance */}
        <motion.div variants={item} className="glass p-5 lg:col-span-2">
          <h3 className="text-sm font-bold text-slate-200 mb-4">Subject Performance — Average {examType} Marks</h3>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={subjectData} barCategoryGap="20%">
              <CartesianGrid strokeDasharray="3 3" stroke="rgba(99,102,241,0.1)" />
              <XAxis dataKey="name" tick={{ fill: '#94a3b8', fontSize: 11 }} />
              <YAxis tick={{ fill: '#94a3b8', fontSize: 11 }} domain={[0, 60]} />
              <Tooltip
                contentStyle={{
                  background: 'rgba(17,24,39,0.9)',
                  border: '1px solid rgba(99,102,241,0.2)',
                  borderRadius: '12px',
                  color: '#f1f5f9',
                  fontSize: 12,
                }}
              />
              <Bar dataKey={examType === 'CT1' ? 'avgCt1' : 'avgCt2'} radius={[6, 6, 0, 0]}>
                {subjectData.map((_, i) => (
                  <Cell key={i} fill={CHART_COLORS[i % CHART_COLORS.length]} />
                ))}
              </Bar>
            </BarChart>
          </ResponsiveContainer>
        </motion.div>

        {/* Pie Chart — Pass/Fail */}
        <motion.div variants={item} className="glass p-5">
          <h3 className="text-sm font-bold text-slate-200 mb-4">Pass vs Fail Distribution</h3>
          <ResponsiveContainer width="100%" height={200}>
            <PieChart>
              <Pie
                data={pieData}
                cx="50%"
                cy="50%"
                innerRadius={50}
                outerRadius={80}
                paddingAngle={5}
                dataKey="value"
                label={({ name, value }) => `${name}: ${value}`}
              >
                {pieData.map((_, i) => (
                  <Cell key={i} fill={PIE_COLORS[i]} />
                ))}
              </Pie>
              <Tooltip
                contentStyle={{
                  background: 'rgba(17,24,39,0.9)',
                  border: '1px solid rgba(99,102,241,0.2)',
                  borderRadius: '12px',
                  color: '#f1f5f9',
                }}
              />
            </PieChart>
          </ResponsiveContainer>
          <div className="flex justify-center gap-6 mt-3">
            <span className="flex items-center gap-2 text-xs text-slate-400">
              <span className="w-3 h-3 rounded-full bg-emerald-500" /> Pass
            </span>
            <span className="flex items-center gap-2 text-xs text-slate-400">
              <span className="w-3 h-3 rounded-full bg-red-500" /> Fail
            </span>
          </div>
        </motion.div>
      </div>

      {/* Top Performers + Export */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <motion.div variants={item} className="glass overflow-hidden">
          <div className="p-5 border-b border-indigo-500/10">
            <h3 className="text-sm font-bold text-slate-200">🏆 Top 5 Performers</h3>
          </div>
          <div className="divide-y divide-indigo-500/5">
            {topPerformers.map((p, i) => (
              <motion.div
                key={p.regNo}
                className="flex items-center gap-4 p-4 hover:bg-white/5 transition-colors"
                initial={{ opacity: 0, x: -20 }}
                animate={{ opacity: 1, x: 0 }}
                transition={{ delay: i * 0.1 }}
              >
                <span className={`w-8 h-8 rounded-lg flex items-center justify-center text-xs font-bold ${
                  i === 0 ? 'bg-amber-500/15 text-amber-400' :
                  i === 1 ? 'bg-slate-400/15 text-slate-300' :
                  i === 2 ? 'bg-orange-500/15 text-orange-400' :
                  'bg-white/5 text-slate-500'
                }`}>
                  #{i + 1}
                </span>
                <div className="flex-1">
                  <p className="text-sm font-semibold text-slate-200">{p.name}</p>
                  <p className="text-xs text-slate-500">{p.regNo}</p>
                </div>
                <span className="text-sm font-bold text-indigo-400">{p.overallPercentage}%</span>
              </motion.div>
            ))}
          </div>
        </motion.div>

        <motion.div variants={item} className="glass p-6 flex flex-col items-center justify-center gap-4">
          <div className="w-16 h-16 rounded-2xl bg-gradient-to-br from-indigo-500 to-purple-600 flex items-center justify-center shadow-lg shadow-indigo-500/25">
            <Download size={28} className="text-white" />
          </div>
          <h3 className="text-lg font-bold text-slate-200">Export Reports</h3>
          <p className="text-sm text-slate-500 text-center">Download department marks report with rankings, statistics, and analysis.</p>
          <div className="flex gap-3 mt-2">
            <RippleButton variant="primary" onClick={() => handleExport('pdf')} className="flex items-center gap-2">
              <Download size={16} /> PDF Report
            </RippleButton>
            <RippleButton variant="secondary" onClick={() => handleExport('docx')} className="flex items-center gap-2">
              <FileText size={16} /> DOCX Report
            </RippleButton>
          </div>
        </motion.div>
      </div>

      {/* Global Rank List */}
      <StudentRankList analytics={analytics} showFailOnly={false} examType={examType} />
    </motion.div>
  );
}
