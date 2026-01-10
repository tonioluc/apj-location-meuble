<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div class="d-flex">
    <!-- Sidebar -->
    <div class="flex-shrink-0 p-3 bg-light border-end" style="width: 260px; min-height: 100vh;">
      <a class="d-flex align-items-center mb-3 mb-md-0 me-md-auto link-dark text-decoration-none" href="#">
        <span class="fs-5">APJ Proforma</span>
      </a>
      <hr />
      <ul class="nav nav-pills flex-column mb-auto">
        <li class="nav-item">
          <RouterLink class="nav-link" active-class="active" to="/proformas">
            <font-awesome-icon icon="list" class="me-2" /> Liste des proformas
          </RouterLink>
        </li>
        <li>
          <RouterLink class="nav-link" active-class="active" to="/proformas/new">
            <font-awesome-icon icon="plus" class="me-2" /> Nouvelle proforma
          </RouterLink>
        </li>
      </ul>
    </div>

    <!-- Content -->
    <div class="flex-grow-1">
      <nav class="navbar navbar-light bg-white border-bottom px-3">
        <span class="navbar-brand mb-0 h1">Gestion Proforma</span>
      </nav>
      <main class="p-3">
        <RouterView />
      </main>
    </div>
  </div>
</template>

<style scoped>
.nav-link.active {
  background-color: #0d6efd;
  color: #fff;
}
</style>
