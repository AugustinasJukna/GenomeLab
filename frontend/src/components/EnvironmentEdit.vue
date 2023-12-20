<template>
  <div class="container">
    <h2 class="my-4">Edit Environment</h2>
    <div class="row">
      <div class="col-md-6">
        <form @submit.prevent="updateEnvironment">
          <div class="form-group">
            <label for="name">Name:</label>
            <input type="text" class="form-control" v-model="environment.name" required>
          </div>

          <div class="form-group">
            <label for="description">Description:</label>
            <textarea class="form-control" v-model="environment.description" required></textarea>
          </div>
        </form>
      </div>

      <div class="col-md-6">
        <form @submit.prevent="updateEnvironment">
          <div class="form-group">
            <label for="mutationCoefficient">Mutation Coefficient:</label>
            <input min="0" max="1" step="0.1" type="number" class="form-control" v-model="environment.mutationCoefficient" required>
          </div>

          <button type="submit" class="btn btn-primary">Update Environment</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      environment: {
        id: null,
        name: '',
        description: '',
        mutationCoefficient: 0,
      },
    };
  },
  methods: {
    async updateEnvironment() {
      try {
        const response = await axios.patch(`/environments/${this.environment.id}`, this.environment, { headers:{Authorization: 'Bearer ' + localStorage.getItem('token')}});
        console.log('Environment updated:', response.data);
        this.$router.push('/environments');
      } catch (error) {
        console.error('Error updating environment', error);
      }
    },
    async fetchEnvironmentDetails() {
      try {
        const response = await axios.get(`/environments/${this.$route.params.id}`, { headers:{Authorization: 'Bearer ' + localStorage.getItem('token')}});
        this.environment = response.data;
      } catch (error) {
        console.error('Error fetching environment details', error);
      }
    },
  },
  mounted() {
    this.fetchEnvironmentDetails();
  },
};
</script>
