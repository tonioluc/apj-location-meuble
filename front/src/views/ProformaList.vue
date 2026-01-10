<script setup>
import { ref, onMounted } from 'vue'
import { listProformas } from '../api/proforma'
import { formatAmount } from '../utils/format'

const loading = ref(false)
const error = ref('')
const items = ref([])

const dateDebut = ref('')
const dateFin = ref('')

function formatDate(d) {
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

async function fetchList() {
  loading.value = true
  error.value = ''
  try {
    const params = {}
    if (dateDebut.value) params.dateDebut = dateDebut.value
    if (dateFin.value) params.dateFin = dateFin.value
    const res = await listProformas(params)
    if (res.success) {
      items.value = res.data || []
    } else {
      error.value = res.message || 'Erreur lors du chargement'
    }
  } catch (e) {
    error.value = e.message || 'Erreur réseau'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  const today = new Date()
  const fifteenDaysAgo = new Date(today)
  fifteenDaysAgo.setDate(today.getDate() - 15)
  dateFin.value = formatDate(today)
  dateDebut.value = formatDate(fifteenDaysAgo)
  fetchList()
})
</script>

<template>
  <div class="container-fluid">
    <div class="d-flex align-items-center mb-3">
      <h2 class="me-3 mb-0">
        <font-awesome-icon icon="list" class="me-2" />
        Liste des proformas
      </h2>
    </div>

    <div class="card mb-3">
      <div class="card-body">
        <div class="row g-2 align-items-end">
          <div class="col-sm-4">
            <label class="form-label"><font-awesome-icon icon="calendar" class="me-1"/>Date début</label>
            <input type="date" class="form-control" v-model="dateDebut" />
          </div>
          <div class="col-sm-4">
            <label class="form-label"><font-awesome-icon icon="calendar" class="me-1"/>Date fin</label>
            <input type="date" class="form-control" v-model="dateFin" />
          </div>
          <div class="col-sm-4">
            <button class="btn btn-primary" @click="fetchList">
              <font-awesome-icon icon="filter" class="me-2" />Filtrer
            </button>
          </div>
        </div>
      </div>
    </div>

    <div v-if="error" class="alert alert-danger">{{ error }}</div>

    <div class="table-responsive">
      <table class="table table-striped">
        <thead>
          <tr>
            <th>Id</th>
            <th>Client</th>
            <th>Date</th>
            <th>Montant Total</th>
            <th>Désignation</th>
            <th>État</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="p in items" :key="p.id">
            <td>
              <RouterLink :to="`/proformas/${p.id}`">{{ p.id }}</RouterLink>
            </td>
            <td>{{ p.client }}</td>
            <td>{{ p.date }}</td>
            <td>{{ formatAmount(p.montantTotal) }}</td>
            <td>{{ p.designation }}</td>
            <td>{{ p.etatLib }}</td>
          </tr>
          <tr v-if="!loading && items.length === 0">
            <td colspan="6" class="text-center text-muted">Aucun résultat</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="loading" class="text-center py-3">Chargement...</div>
  </div>
</template>

<style scoped>
</style>
