import { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { analyticsAPI } from '../../services/api';
import MarkCell from '../ui/MarkCell';

const item = { hidden: { opacity: 0, y: 20 }, show: { opacity: 1, y: 0 } };

export default function StudentRankList({ analytics: initialAnalytics, showFailOnly = false, examType }) {
  const [data, setData] = useState(initialAnalytics);
  const [loading, setLoading] = useState(!initialAnalytics);

  useEffect(() => {
    if (!initialAnalytics) {
      setLoading(true);
      analyticsAPI.getRankings('CSE').then(res => {
        setData(res.data);
        setLoading(false);
      }).catch(e => {
        console.error('Failed to load rankings', e);
        setLoading(false);
      });
    } else {
      setData(initialAnalytics);
    }
  }, [initialAnalytics]);

  if (loading || !data || !data.rankings) return null;

  const rankings = data.rankings;
  const failStudents = rankings.filter(r => {
    return r.subjectMarks?.some(sm => {
      const score = examType === 'CT1' ? sm.ct1Marks : sm.ct2Marks;
      return score < 30;
    });
  });

  const displayList = showFailOnly ? failStudents : rankings;

  if (displayList.length === 0) return null;

  return (
    <motion.div variants={item} className="glass overflow-hidden w-full mt-6">
      <div className="p-5 border-b border-indigo-500/10">
        <h3 className="text-sm font-bold text-slate-200">
          {showFailOnly ? 'Failure List' : 'Student Rankings'} — {examType}
        </h3>
      </div>
      <div className="overflow-x-auto">
        <table className="data-table mb-0">
          <thead>
            <tr>
              <th>Rank</th>
              <th>Reg No</th>
              <th>Name</th>
              {data.subjectAnalytics && Object.values(data.subjectAnalytics).map(sa => (
                <th key={sa.subjectId}>{sa.subjectName.split(' ').map(w => w[0]).join('')}</th>
              ))}
              <th>Total</th>
              <th>%</th>
            </tr>
          </thead>
          <tbody>
            {displayList.map((r, i) => (
              <motion.tr key={r.regNo || i} variants={item}>
                <td>
                  <span className={`inline-flex items-center justify-center w-7 h-7 rounded-lg text-xs font-bold ${
                    r.rank === 1 ? 'bg-amber-500/15 text-amber-400' :
                    r.rank === 2 ? 'bg-slate-400/15 text-slate-300' :
                    r.rank === 3 ? 'bg-orange-500/15 text-orange-400' :
                    'bg-white/5 text-slate-500'
                  }`}>
                    {r.rank}
                  </span>
                </td>
                <td className="font-mono text-xs text-slate-400">{r.regNo}</td>
                <td className="font-medium text-slate-300">{r.name}</td>
                {r.subjectMarks?.map(sm => (
                  <td key={sm.subjectId}>
                    <MarkCell value={examType === 'CT1' ? sm.ct1Marks : sm.ct2Marks} />
                  </td>
                ))}
                <td className="font-bold text-slate-200">{r.totalMarks}</td>
                <td className="font-semibold text-indigo-400">{r.overallPercentage}%</td>
              </motion.tr>
            ))}
          </tbody>
        </table>
      </div>
    </motion.div>
  );
}
