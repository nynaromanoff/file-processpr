import http from 'k6/http';
import { check, sleep } from 'k6';

// Configuração do cenário de estresse
export const options = {
    stages: [
        { duration: '30s', target: 20 },
        { duration: '1m', target: 20 },
        { duration: '15s', target: 0 },
    ],
};

const BASE_URL = 'http://localhost:8080/api/v1/files';

export default function () {
    const payload = JSON.stringify({
        fileName: `arquivo_teste_${Math.floor(Math.random() * 10000)}.csv`,
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post(`${BASE_URL}/upload-request`, payload, params);

    check(res, {
        'status é 202': (r) => r.status === 202,
        'contém uploadUrl': (r) => JSON.parse(r.body).uploadUrl !== undefined,
    });

    sleep(1);
}