import { motion } from 'framer-motion';

export default function MarkCell({ value, maxMarks = 60 }) {
  const numValue = parseInt(value) || 0;
  const threshold = maxMarks / 2; // 30 for max 60

  const getStatus = () => {
    if (numValue < threshold) return 'fail';
    if (numValue <= threshold + 5) return 'risk'; // 30-35
    return 'safe';
  };

  const status = getStatus();

  const statusConfig = {
    fail: {
      className: 'mark-fail font-bold',
      hoverAnimation: { x: [0, -2, 2, -2, 2, 0] },
      transition: { duration: 0.4 },
    },
    risk: {
      className: 'mark-risk font-semibold pulse-warning',
      hoverAnimation: {},
      transition: {},
    },
    safe: {
      className: 'mark-safe font-medium',
      hoverAnimation: {},
      transition: {},
    },
  };

  const config = statusConfig[status];

  return (
    <motion.span
      className={`inline-block px-2 py-1 rounded-md ${config.className}`}
      whileHover={config.hoverAnimation}
      transition={config.transition}
    >
      {numValue}
    </motion.span>
  );
}
