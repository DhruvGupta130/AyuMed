import axios from 'axios';
import { BACKEND_URL } from "../configuration.js";

axios.defaults.baseURL = BACKEND_URL;

let isRedirecting = false;

const cancelTokenSource = axios.CancelToken.source();

axios.interceptors.request.use(
    (config) => {
        if (isRedirecting) {
            cancelTokenSource.cancel('Session expired. Cancelling pending requests.');
        }

        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        config.cancelToken = cancelTokenSource.token;
        return config;
    },
    (error) => Promise.reject(error)
);

axios.interceptors.response.use(
    (response) => response,
    (error) => {
        if (axios.isCancel(error)) {
            console.warn('Request canceled:', error.message);
            return Promise.reject(error);
        }

        if (error.response) {
            const { status, data } = error.response;

            console.error('API Error:', data);

            if ((status === 401 && ['Token expired', 'Unauthorized'].includes(data.message)) ||
                (status === 500 && data?.message?.includes('JWT'))) {

                if (!isRedirecting) {
                    isRedirecting = true;

                    alert('Your session has expired or is invalid. Please log in again.');
                    cancelTokenSource.cancel('Session expired. Cancelling pending requests.');
                    window.location.href = '/logout';
                }
            }
        }
        return Promise.reject(error);
    }
);