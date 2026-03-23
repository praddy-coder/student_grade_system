import { useState, useEffect, useRef } from 'react';
import { useOutletContext } from 'react-router-dom';
import { motion } from 'framer-motion';
import { analyticsAPI, exportAPI, studentAPI } from '../services/api';
import AnimatedCounter from '../components/ui/AnimatedCounter';
import MarkCell from '../components/ui/MarkCell';
import RippleButton from '../components/ui/RippleButton';
import SkeletonLoader from '../components/ui/SkeletonLoader';
import StudentRankList from '../components/dashboard/StudentRankList';
import { Users, UserX, Trophy, Download, FileText, Filter, Upload } from 'lucide-react';
import toast from 'react-hot-toast';

const container = { hidden: { opacity: 0 }, show: { opacity: 1, transition: { staggerChildren: 0.08 } } };
const item = { hidden: { opacity: 0, y: 20 }, show: { opacity: 1, y: 0 } };

export default function CCDashboard() {
  const { examType } = useOutletContext();
  const [analytics, setAnalytics] = useState(null);
  const [loading, setLoading] = useState(true);
  const [showFailOnly, setShowFailOnly] = useState(false);
  const [exporting, setExporting] = useState(false);
  const [uploading, setUploading] = useState(false);
  const fileInputRef = useRef(null);

  useEffect(() => {
    const fetch = async () => {
      setLoading(true);
      try {
        const { data } = await analyticsAPI.getRankings('CSE');
        setAnalytics(data);
      } catch (e) {
        console.error(e);
        toast.error(e.response?.data?.error || e.message || 'Failed to fetch analytics');
      }
      finally { setLoading(false); }
    };
    fetch();
  }, []);

  const handleFileUpload = async (e) => {
    const selected = e.target.files[0];
    if (!selected) return;
    
    const formData = new FormData();
    formData.append('file', selected);
    setUploading(true);
    try {
      const { data } = await studentAPI.uploadStudents(formData);
      toast.success(data.message || 'Students uploaded successfully!');
      
      // refresh rankings
      const res = await analyticsAPI.getRankings('CSE');
      setAnalytics(res.data);
    } catch (err) {
      toast.error(err.response?.data?.error || 'Failed to upload students');
    } finally {
      setUploading(false);
      if (fileInputRef.current) fileInputRef.current.value = '';
    }
  };

  const handleExport = async (format) => {
    setExporting(true);
    try {
      await exportAPI.downloadReport(format, 'CSE');
      toast.success(`${format.toUpperCase()} report downloaded!`);
    } catch (e) {
      toast.error('Export failed');
    } finally {
      setExporting(false);
    }
  };

  if (loading) return <SkeletonLoader rows={6} cols={5} />;
  if (!analytics) return null;

  const rankings = analytics.rankings || [];
  const failStudents = rankings.filter(r => {
    return r.subjectMarks?.some(sm => {
      const score = examType === 'CT1' ? sm.ct1Marks : sm.ct2Marks;
      return score < 30;
    });
  });

  const displayList = showFailOnly ? failStudents : rankings;

  return (
    <motion.div variants={container} initial="hidden" animate="show" className="space-y-6">
      {/* Stats Row */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        {[
          { label: 'Total Students', value: analytics.totalStudents || 0, icon: Users, color: 'from-indigo-500 to-purple-600' },
          { label: 'Failures (Subjects)', value: analytics.totalFail || 0, icon: UserX, color: 'from-red-500 to-rose-600' },
          { label: 'Pass Rate', value: analytics.overallPassPercentage || 0, icon: Trophy, color: 'from-emerald-500 to-green-600', suffix: '%' },
          { label: 'At-Risk Students', value: failStudents.length || 0, icon: Filter, color: 'from-amber-500 to-orange-600' },
        ].map((stat, i) => (
          <motion.div key={i} variants={item} className="glass p-5">
            <div className="flex items-center justify-between mb-3">
              <span className="text-xs text-slate-500 font-semibold uppercase tracking-wider">{stat.label}</span>
              <div className={`w-9 h-9 rounded-xl bg-gradient-to-br ${stat.color} flex items-center justify-center`}>
                <stat.icon size={17} className="text-white" />
              </div>
            </div>
            <div className="text-2xl font-bold text-slate-100">
              <AnimatedCounter value={stat.value} suffix={stat.suffix || ''} decimals={stat.suffix === '%' ? 1 : 0} />
            </div>
          </motion.div>
        ))}
      </div>

      {/* Controls */}
      <motion.div variants={item} className="flex flex-wrap items-center gap-3">
        <RippleButton
          variant={showFailOnly ? 'danger' : 'secondary'}
          onClick={() => setShowFailOnly(!showFailOnly)}
          className="flex items-center gap-2"
        >
          <UserX size={16} />
          {showFailOnly ? 'Show All' : 'Show Failures Only'}
        </RippleButton>
        <RippleButton variant="primary" onClick={() => handleExport('pdf')} disabled={exporting} className="flex items-center gap-2">
          <Download size={16} /> Export PDF
        </RippleButton>
        <RippleButton variant="secondary" onClick={() => handleExport('docx')} disabled={exporting} className="flex items-center gap-2">
          <FileText size={16} /> Export DOCX
        </RippleButton>
        
        <input 
          type="file" 
          ref={fileInputRef} 
          onChange={handleFileUpload} 
          accept=".xlsx, .xls" 
          className="hidden" 
        />
        <RippleButton variant="success" onClick={() => fileInputRef.current?.click()} disabled={uploading} className="flex items-center gap-2 ml-auto">
          <Upload size={16} /> {uploading ? 'Uploading...' : 'Bulk Excel Register'}
        </RippleButton>
      </motion.div>

      {/* Rankings Table */}
      <StudentRankList analytics={analytics} showFailOnly={showFailOnly} examType={examType} />
    </motion.div>
  );
}
