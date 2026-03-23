export default function SkeletonLoader({ rows = 5, cols = 4 }) {
  return (
    <div className="space-y-3">
      {/* Header skeleton */}
      <div className="flex gap-4 mb-4">
        {Array.from({ length: cols }).map((_, i) => (
          <div key={i} className="skeleton h-8 flex-1 rounded-lg" />
        ))}
      </div>
      {/* Row skeletons */}
      {Array.from({ length: rows }).map((_, i) => (
        <div key={i} className="flex gap-4" style={{ animationDelay: `${i * 0.1}s` }}>
          {Array.from({ length: cols }).map((_, j) => (
            <div key={j} className="skeleton h-12 flex-1 rounded-lg" />
          ))}
        </div>
      ))}
    </div>
  );
}
