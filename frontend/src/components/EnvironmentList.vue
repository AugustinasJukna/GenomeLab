<!-- EnvironmentList.vue -->

<template>
  <div class="container">
    <h2 class="my-4">Environments List</h2>
    <router-link to="/environments/new" class="btn btn-primary mb-4">Add Environment</router-link>
    <ConfirmationModal :message="confirmationMessage" :onConfirm="deleteEnvironment" />

    <table v-if="environments.length > 0" class="table">
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
          <router-link :to="'/environments/view/' + environment.id" class="btn btn-success" style="margin-right: 5px">View</router-link>
          <router-link :to="'/environments/edit/' + environment.id" class="btn btn-warning" style="margin-right: 5px">Edit</router-link>
          <button  @click="showConfirmationModal(environment.id)" class="btn btn-danger">Delete</button>
        </td>
      </tr>
      </tbody>
    </table>
    <div v-if="environments.length === 0">
      <h5 style="text-align: center">No environments available.</h5>
    </div>
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
        const response = await axios.get(`/environments/user/${JSON.parse(localStorage.getItem('user')).id}`, { headers:{Authorization: 'Bearer ' + localStorage.getItem('token')}});
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
              if (error.response.code === 404) {
                this.environments = [];
              }
              console.error('Error deleting environment', error);
            })
            .finally(() => {
              this.selectedEnvironmentId = null;
            });
      }
    },
  },
};
</script>
