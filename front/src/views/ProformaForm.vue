<script setup>
import { ref } from 'vue'
import { createProforma } from '../api/proforma'

const client = ref('')
const designation = ref('')
const description = ref('')
const date = ref('')
const remise = ref(0)
const tva = ref(20)

const loading = ref(false)
const message = ref('')
const error = ref('')

async function submitForm() {
  loading.value = true
  message.value = ''
  error.value = ''
  try {
    const payload = {
      client: client.value,
      designation: designation.value,
      description: description.value,
      date: date.value || undefined,
      remise: Number(remise.value),
      tva: Number(tva.value),
    }
    const res = await createProforma(payload)
    if (res.success) {
      message.value = `Proforma créé avec succès (ID: ${res.data?.id || ''})`
      client.value = ''
      designation.value = ''
      description.value = ''
      date.value = ''
      remise.value = 0
      tva.value = 20
    } else {
      error.value = res.message || 'Erreur lors de la création'
    }
  } catch (e) {
    error.value = e.message || 'Erreur réseau'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="container">
    <div class="d-flex align-items-center mb-3">
      <h2 class="me-3 mb-0">
        <font-awesome-icon icon="plus" class="me-2" />
        Nouvelle proforma
      </h2>
    </div>

    <div v-if="message" class="alert alert-success">{{ message }}</div>
    <div v-if="error" class="alert alert-danger">{{ error }}</div>

    <form @submit.prevent="submitForm" class="card">
      <div class="card-body">
        <div class="row g-3">
          <div class="col-md-6">
            <label class="form-label">Client</label>
            <input type="text" class="form-control" v-model="client" required />
          </div>
          <div class="col-md-6">
            <label class="form-label">Désignation</label>
            <input type="text" class="form-control" v-model="designation" required />
          </div>
          <div class="col-md-12">
            <label class="form-label">Description</label>
            <textarea class="form-control" v-model="description" rows="3"></textarea>
          </div>
          <div class="col-md-4">
            <label class="form-label">Date</label>
            <input type="date" class="form-control" v-model="date" />
          </div>
          <div class="col-md-4">
            <label class="form-label">Remise (%)</label>
            <input type="number" min="0" step="0.01" class="form-control" v-model="remise" />
          </div>
          <div class="col-md-4">
            <label class="form-label">TVA (%)</label>
            <input type="number" min="0" step="0.01" class="form-control" v-model="tva" />
          </div>
        </div>
      </div>
      <div class="card-footer d-flex justify-content-end">
        <button class="btn btn-primary" type="submit" :disabled="loading">
          <font-awesome-icon icon="plus" class="me-2" />
          {{ loading ? 'En cours...' : 'Enregistrer' }}
        </button>
      </div>
    </form>
  </div>
</template>

<style scoped>
</style>
