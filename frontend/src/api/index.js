import axios from 'axios';
import store from "@/store";

const baseURL = 'http://localhost:8080';

const api = axios.create({
    baseURL,
});

const apiAuth = axios.create({
    baseURL: `${baseURL}/api/auth`,
});

// Add an interceptor to include the Authorization header in every request
api.interceptors.request.use(
    (config) => {
        const accessToken = store.state.accessToken;
        const clientId = store.state.clientId
        console.log('interceptor client id');
        console.log(clientId);

        if (accessToken) {
            config.headers.Authorization = `Bearer ${accessToken}`;
        }
        if (clientId) {
            config.headers['X-Client-ID'] = clientId;
        }
        return config;
    },
    (error) => Promise.reject(error)
);


apiAuth.interceptors.response.use(
    (response) => response.data,
    (error) => Promise.reject(error)
);
api.interceptors.response.use(
    (response) => response.data,
    (error) => Promise.reject(error)
);

// Wrapper function for requests with global error handling
const makeRequest = async (method, url, data) => {
    try {
        const response = await method(url, data);
        console.log('the response is')// Добавлено await
        console.log(response);
        return response; // Предполагается, что ответ содержит данные в свойстве data
    } catch (error) {
        console.error('API Request Error:', error);
        throw error;
    }
};

export const auth = {
    login: async (userData) => {
        const aa = await makeRequest(apiAuth.post, '/login', userData);
        console.log(aa);
        return aa;
    },
    register: async (userData) => {
        return makeRequest(apiAuth.post, '/register', userData);
    },
}

export const categories = {
    create: async (categoryData) => {
        console.log(categoryData);
        return makeRequest(api.post, '/category', {
            name: categoryData.name,
            spendLimit: categoryData.spendLimit,
        });
    },
    getAll: async () => {
        return makeRequest(api.get, '/category/getAll');
    },
    update: async (id, categoryData) => {
        return makeRequest(api.put, `/category/${id}`, categoryData);
    },
    delete: async (id) => {
        return makeRequest(api.delete(), `/category/${id}`);
    },
}

export const reports = {
    download: async (buildReportDTO) => {
        const response = await makeRequest(api.post, '/report', buildReportDTO);

        // Создаем Blob из ответа
        const blob = new Blob([response.data], {type: 'application/octet-stream'});

        // Создаем URL для Blob
        const url = window.URL.createObjectURL(blob);

        // Создаем временный элемент <a> для скачивания
        const a = document.createElement('a');
        a.style.display = 'none';
        a.href = url;
        a.download = 'report.xlsx'; // Задаем имя файла

        // Добавляем элемент <a> в документ и инициируем клик
        document.body.appendChild(a);
        a.click();

        // Удаляем элемент <a> из документа
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
    }
}


export const operations = {
    create: async (operationData) => {
        return makeRequest(api.post, '/operation', operationData);
    },
    getAll: async () => {
        return makeRequest(api.get, '/operation/getAll');
    },
}
