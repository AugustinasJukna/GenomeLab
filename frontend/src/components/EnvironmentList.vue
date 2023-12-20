<!-- EnvironmentList.vue -->

<template>
  <div class="container">
    <h2 class="my-4">Environments List</h2>
    <router-link to="/environments/new" class="btn btn-primary mb-4">Add Environment</router-link>
    <ConfirmationModal :message="confirmationMessage" :onConfirm="deleteEnvironment" />

    <table class="table">
      <thead>
      <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Description</th>
        <th>Mutation Coefficient</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="environment in environments" :key="environment.id">
        <td>{{ environment.id }}</td>
        <td>{{ environment.name }}</td>
        <td>{{ environment.description }}</td>
        <td>{{ environment.mutationCoefficient }}</td>
        <td>
          <router-link :to="'/environments/edit/' + environment.id" class="btn btn-warning">Edit</router-link>
          <button  @click="showConfirmationModal(environment.id)" class="btn btn-danger">Delete</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios';
import ConfirmationModal from "@/components/ConfirmationModal.vue";
export default {
  components: {ConfirmationModal},
  data() {
    return {
      environments: [],
      confirmationMessage: '',
      selectedEnvironmentId: null,
    };
  },
  mounted() {
    this.fetchEnvironments();
  },
  methods: {
    async fetchEnvironments() {
      try {
        const response = await axios.get('/environments', { headers:{Authorization: 'Bearer ' + localStorage.getItem('token')}});
        this.environments = response.data;
      } catch (error) {
        console.error('Error fetching environments', error);
      }
    },
    showConfirmationModal(environmentId) {
      this.selectedEnvironmentId = environmentId;
      this.confirmationMessage = 'Are you sure you want to delete this environment?';
      const modalElement = document.getElementById('confirmationModal');
      modalElement.classList.add('show');
      modalElement.style.display = 'block';
    },
    deleteEnvironment() {
      if (this.selectedEnvironmentId !== null) {
        axios.delete(`/environments/${this.selectedEnvironmentId}`, { headers:{Authorization: 'Bearer ' + localStorage.getItem('token')}})
            .then(() => {
              console.log('Environment deleted');
              this.fetchEnvironments();
            })
            .catch((error) => {
              console.error('Error deleting environment', error);
            })
            .finally(() => {
              // Reset the selected environment after deletion
              this.selectedEnvironmentId = null;
            });
      }
    },
  },
};
</script>
