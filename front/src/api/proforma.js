import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/asynclocation/api',
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  },
});

export async function listProformas(params = {}) {
  const res = await api.get('/proforma', { params });
  return res.data; // ApiResponse
}

export async function getProforma(id) {
  const res = await api.get('/proforma', { params: { id } });
  return res.data;
}

export async function createProforma(payload) {
  const res = await api.post('/proforma', payload);
  return res.data;
}

export async function updateProforma(id, payload) {
  const res = await api.put('/proforma', payload, { params: { id } });
  return res.data;
}

export async function deleteProforma(id) {
  const res = await api.delete('/proforma', { params: { id } });
  return res.data;
}

export async function addDetail(idProforma, detail) {
  const res = await api.post('/proforma/details', detail, { params: { idProforma } });
  return res.data;
}

export async function calculateTotal(id) {
  const res = await api.get('/proforma/calculate', { params: { id } });
  return res.data;
}
