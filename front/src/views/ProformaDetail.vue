<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getProforma } from '../api/proforma'

const route = useRoute()
const id = route.params.id

const loading = ref(false)
const error = ref('')
const item = ref(null)

onMounted(async () => {
  loading.value = true
  error.value = ''
  try {
    const res = await getProforma(id)
    if (res.success) {
      item.value = res.data
    } else {
      error.value = res.message || 'Proforma introuvable'
    }
  } catch (e) {
    error.value = e.message || 'Erreur réseau'
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="container">
    <div class="d-flex align-items-center mb-3">
      <h2 class="me-3 mb-0">Détail proforma</h2>
      <span class="text-muted">ID: {{ id }}</span>
    </div>

    <div v-if="loading" class="text-center py-3">Chargement...</div>
    <div v-if="error" class="alert alert-danger">{{ error }}</div>

    <div v-if="item" class="card mb-3">
      <div class="card-body">
        <div class="row g-3">
          <div class="col-md-4"><strong>Client: </strong>{{ item.client }}</div>
          <div class="col-md-4"><strong>Date: </strong>{{ item.date }}</div>
          <div class="col-md-4"><strong>Montant total: </strong>{{ item.montantTotal }}</div>
          <div class="col-md-12"><strong>Désignation: </strong>{{ item.designation }}</div>
          <div class="col-md-12"><strong>Description: </strong>{{ item.description }}</div>
          <div class="col-md-3"><strong>État: </strong>{{ item.etatLib }}</div>
          <div class="col-md-3"><strong>Remise (%): </strong>{{ item.remise }}</div>
          <div class="col-md-3"><strong>TVA (%): </strong>{{ item.tva }}</div>
        </div>
      </div>
    </div>

    <div v-if="item && item.details && item.details.length" class="card">
      <div class="card-header">Détails</div>
      <div class="card-body p-0">
        <div class="table-responsive">
          <table class="table mb-0">
            <thead>
              <tr>
                <th>Id</th>
                <th>Désignation</th>
                <th>Quantité</th>
                <th>PU</th>
                <th>Remise</th>
                <th>TVA</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="d in item.details" :key="d.id">
                <td>{{ d.id }}</td>
                <td>{{ d.designation }}</td>
                <td>{{ d.quantite }}</td>
                <td>{{ d.prixUnitaire }}</td>
                <td>{{ d.remise }}</td>
                <td>{{ d.tva }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
</style>
