<template>
  <div class="container">
    <h2 class="my-4">Create Environment</h2>
    <div class="row">
      <div class="col-md-6">
        <form @submit.prevent="createEnvironment">
          <div class="form-group">
            <label for="name">Name:</label>
            <input type="text" class="form-control" v-model="environment.name" required>
          </div>

          <div class="form-group">
            <label for="description">Description:</label>
            <textarea class="form-control" v-model="environment.description" required></textarea>
          </div>

          <div class="form-group">
            <label for="mutationCoefficient">Mutation Coefficient:</label>
            <input step="0.1" min="0" max="1" type="number" class="form-control" v-model="environment.mutationCoefficient" required>
          </div>

          <div class="form-group">
            <label for="organismsCount">Organisms Count:</label>
            <input min="0" type="number" class="form-control" v-model="environment.organismsCount" required>
          </div>

          <div class="form-group">
            <label for="foodCount">Food Count:</label>
            <input min="0" type="number" class="form-control" v-model="environment.foodCount" required>
          </div>
        </form>
      </div>

      <div class="col-md-6">
        <form @submit.prevent="createEnvironment">
          <div class="form-group">
            <label for="eliteCount">Elite Count:</label>
            <input min="0" type="number" class="form-control" v-model="environment.eliteCount" required>
          </div>


        </form>
      </div>

      <div class="col-md-12 mt-3">
        <button type="submit" class="btn btn-primary" @click="createEnvironment">Create Environment</button>
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
        name: '',
        description: '',
        mutationCoefficient: 0,
        organismsCount: 0,
        foodCount: 0,
        eliteCount: 0,
        user: {
          id: JSON.parse(localStorage.getItem('user')).id,
          name: JSON.parse(localStorage.getItem('user')).name,
          password: JSON.parse(localStorage.getItem('user')).password,
          email: JSON.parse(localStorage.getItem('user')).email,
          role: JSON.parse(localStorage.getItem('user')).role
        },
      },
    };
  },
  methods: {
    async createEnvironment() {
      try {
        console.log(this.environment);
        const response = await axios.post('/environments', this.environment, { headers:{Authorization: 'Bearer ' + localStorage.getItem('token')}});
        console.log('Environment created:', response.data);
        this.$router.push('/environments');
      } catch (error) {
        console.error('Error creating environment', error);
      }
    },
  },
};
</script>
