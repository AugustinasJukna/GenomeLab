<!-- OrganismsList.vue -->

<template>
  <div>
    <h2>Organisms List</h2>

    <table class="table table-hover" v-if="organisms.length > 0">
      <thead>
      <tr>
        <th>ID</th>
        <th>Energy</th>
        <th>Position (X, Y)</th>
        <th>Genes</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="organism in organisms" :key="organism.id">
        <td>{{ organism.id }}</td>
        <td>{{ organism.energy }}</td>
        <td>{{ organism.x }}, {{ organism.y }}</td>
        <td>{{ organism.genes }}</td>
      </tr>
      </tbody>
    </table>
    <h5 v-else class="mt-3">No organisms available for this state.</h5>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      organisms: [],
    };
  },
  mounted() {
    this.fetchOrganisms();
  },
  methods: {
    async fetchOrganisms() {
      try {
        const response = await axios.get(`organisms/getByState/${this.$route.params.id}`, { headers: { Authorization: 'Bearer ' + localStorage.getItem('token') } });
        this.organisms = response.data;
        console.log(this.organisms[0])
      } catch (error) {
        console.error('Error fetching organisms', error);
      }
    },
  },
};
</script>

<style scoped>
.table-responsive {
  overflow-x: auto;
  margin-top: 10px;
}
</style>
