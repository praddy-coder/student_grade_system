import axios from 'axios';

const API = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
});

// Attach JWT token to every request
API.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Handle 401 responses
API.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const authAPI = {
  login: (username, password) => API.post('/auth/login', { username, password }),
};

export const marksAPI = {
  getStudentMarks: (studentId) => API.get(`/marks/${studentId}`),
  getAllMarks: (department) => API.get('/marks/all', { params: { department } }),
  updateMark: (data) => API.put('/marks/update', data),
};

export const analyticsAPI = {
  getRankings: (department) => API.get('/analytics/rankings', { params: { department } }),
};

export const exportAPI = {
  downloadReport: async (format = 'pdf', department) => {
    const response = await API.get('/export/report', {
      params: { format, department },
      responseType: 'blob',
    });
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', `marks_report.${format}`);
    document.body.appendChild(link);
    link.click();
    link.remove();
    window.URL.revokeObjectURL(url);
  },
};

export const studentAPI = {
  uploadStudents: (formData) => API.post('/cc/students/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  }),
};

export default API;
