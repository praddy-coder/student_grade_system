import { useState, useEffect } from 'react';
import { useOutletContext } from 'react-router-dom';
import { motion } from 'framer-motion';
import { marksAPI } from '../services/api';
import MarkCell from '../components/ui/MarkCell';
import RippleButton from '../components/ui/RippleButton';
import SkeletonLoader from '../components/ui/SkeletonLoader';
import StudentRankList from '../components/dashboard/StudentRankList';
import { Save, Search, Filter } from 'lucide-react';
import toast from 'react-hot-toast';

const container = { hidden: { opacity: 0 }, show: { opacity: 1, transition: { staggerChildren: 0.05 } } };
const item = { hidden: { opacity: 0, y: 15 }, show: { opacity: 1, y: 0 } };

export default function StaffDashboard() {
  const { examType } = useOutletContext();
  const [marks, setMarks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [editedCells, setEditedCells] = useState({});
  const [saving, setSaving] = useState({});
  const [search, setSearch] = useState('');

  useEffect(() => {
    const fetch = async () => {
      setLoading(true);
      try {
        const { data } = await marksAPI.getAllMarks('CSE');
        setMarks(data);
      } catch (e) { console.error(e); }
      finally { setLoading(false); }
    };
    fetch();
  }, []);

  const handleEdit = (markId, value) => {
    const num = parseInt(value);
    if (value === '' || (num >= 0 && num <= 60)) {
      setEditedCells(prev => ({ ...prev, [markId]: value }));
    }
  };

  const handleSave = async (mark) => {
    const cellKey = mark.id;
    const newValue = editedCells[cellKey];
    if (newValue === undefined || newValue === '') return;

    setSaving(prev => ({ ...prev, [cellKey]: true }));
    try {
      const { data } = await marksAPI.updateMark({
        studentId: mark.studentRegNo,
        subjectId: mark.subjectId,
        exam: examType,
        marks: parseInt(newValue),
      });
      setMarks(prev => prev.map(m =>
        m.id === mark.id ? { ...m, ct1Marks: data.ct1Marks, ct2Marks: data.ct2Marks } : m
      ));
      setEditedCells(prev => {
        const next = { ...prev };
        delete next[cellKey];
        return next;
      });
      toast.success(`Updated ${mark.studentName}'s ${examType} marks for ${mark.subjectName}`);
    } catch (e) {
      toast.error(e.response?.data?.error || 'Failed to update marks');
    } finally {
      setSaving(prev => ({ ...prev, [cellKey]: false }));
    }
  };

  const filtered = marks.filter(m =>
    m.studentName.toLowerCase().includes(search.toLowerCase()) ||
    m.studentRegNo.toLowerCase().includes(search.toLowerCase()) ||
    m.subjectName.toLowerCase().includes(search.toLowerCase())
  );

  if (loading) return <SkeletonLoader rows={8} cols={5} />;

  return (
    <motion.div variants={container} initial="hidden" animate="show" className="space-y-6">
      {/* Header */}
      <motion.div variants={item} className="flex flex-col sm:flex-row justify-between gap-4">
        <div>
          <h2 className="text-xl font-bold text-slate-100">Enter Marks — {examType}</h2>
          <p className="text-sm text-slate-500 mt-1">Click on any cell to edit, then save.</p>
        </div>
        <div className="relative">
          <Search size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-500" />
          <input
            type="text"
            placeholder="Search student or subject..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="input-glass pl-10 w-64"
          />
        </div>
      </motion.div>

      {/* Marks Table */}
      <motion.div variants={item} className="glass overflow-hidden">
        <div className="overflow-x-auto">
          <table className="data-table">
            <thead>
              <tr>
                <th>Reg No</th>
                <th>Student Name</th>
                <th>Subject</th>
                <th>{examType} Marks</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((m) => {
                const currentValue = examType === 'CT1' ? m.ct1Marks : m.ct2Marks;
                const editedValue = editedCells[m.id];
                const isEdited = editedValue !== undefined;
                const displayValue = isEdited ? editedValue : currentValue;

                return (
                  <motion.tr key={`${m.id}-${examType}`} variants={item}>
                    <td className="text-slate-400 font-mono text-xs">{m.studentRegNo}</td>
                    <td className="font-medium text-slate-300">{m.studentName}</td>
                    <td className="text-slate-400 text-sm">{m.subjectName}</td>
                    <td>
                      <div className="flex items-center gap-2">
                        <input
                          type="number"
                          min={0}
                          max={60}
                          value={displayValue}
                          onChange={(e) => handleEdit(m.id, e.target.value)}
                          className="input-glass w-20 text-center py-1.5 text-sm"
                        />
                        {!isEdited && <MarkCell value={currentValue} />}
                      </div>
                    </td>
                    <td>
                      {isEdited && (
                        <RippleButton
                          variant="success"
                          className="text-xs px-3 py-1.5"
                          onClick={() => handleSave(m)}
                          disabled={saving[m.id]}
                        >
                          {saving[m.id] ? (
                            <motion.div
                              className="w-4 h-4 border-2 border-white/30 border-t-white rounded-full inline-block"
                              animate={{ rotate: 360 }}
                              transition={{ duration: 1, repeat: Infinity, ease: 'linear' }}
                            />
                          ) : (
                            <span className="flex items-center gap-1"><Save size={13} /> Save</span>
                          )}
                        </RippleButton>
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
