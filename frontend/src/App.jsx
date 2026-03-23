import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { AuthProvider, useAuth } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import DashboardLayout from './components/layout/DashboardLayout';
import LoginPage from './pages/LoginPage';
import StudentDashboard from './pages/StudentDashboard';
import StaffDashboard from './pages/StaffDashboard';
import CCDashboard from './pages/CCDashboard';
import HODDashboard from './pages/HODDashboard';

function DashboardRouter() {
  const { user } = useAuth();
  const role = user?.role;

  const getDashboard = () => {
    switch (role) {
      case 'STUDENT': return <StudentDashboard />;
      case 'STAFF': return <StaffDashboard />;
      case 'CC': return <CCDashboard />;
      case 'HOD': return <HODDashboard />;
      default: return <Navigate to="/login" />;
    }
  };

  return getDashboard();
}

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Toaster
          position="top-right"
          toastOptions={{
            className: 'toast-custom',
            duration: 3000,
            style: {
              background: 'rgba(17, 24, 39, 0.9)',
              backdropFilter: 'blur(16px)',
              border: '1px solid rgba(99, 102, 241, 0.2)',
              color: '#f1f5f9',
              borderRadius: '12px',
              fontSize: '14px',
            },
          }}
        />
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute>
                <DashboardLayout />
              </ProtectedRoute>
            }
          >
            <Route index element={<DashboardRouter />} />
            <Route path="marks" element={
              <ProtectedRoute allowedRoles={['STUDENT', 'STAFF']}>
                <DashboardRouter />
              </ProtectedRoute>
            } />
            <Route path="rankings" element={
              <ProtectedRoute allowedRoles={['CC', 'HOD']}>
                <CCDashboard />
              </ProtectedRoute>
            } />
            <Route path="analytics" element={
              <ProtectedRoute allowedRoles={['HOD']}>
                <HODDashboard />
              </ProtectedRoute>
            } />
            <Route path="export" element={
              <ProtectedRoute allowedRoles={['CC', 'HOD']}>
                <CCDashboard />
              </ProtectedRoute>
            } />
          </Route>
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}
