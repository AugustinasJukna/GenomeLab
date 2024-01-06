<!-- OrganismsList.vue -->

<template>
  <div>
    <h2>Organisms List</h2>

    <a :href="'/environments/view/' + $route.params.envId + '/states/' + $route.params.stateId + '/organisms/create'" class="btn btn-success">
      Create Organism
    </a>

    <table class="table table-hover" v-if="organisms.length > 0">
      <thead>
      <tr>
        <th>ID</th>
        <th>Energy</th>
        <th>Position (X, Y)</th>
        <th>Genes</th>
        <th>Action</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="organism in organisms" :key="organism.id">
        <td>{{ organism.id }}</td>
        <td>{{ organism.energy }}</td>
        <td>{{ organism.x }}, {{ organism.y }}</td>
        <td>{{ organism.genes }}</td>
        <td>
          <button @click="editOrganism(organism.id)" class="btn btn-warning">Edit</button>
          </td>
          <td>
          <button @click="deleteOrganism(organism.id)" class="btn btn-danger">Delete</button>
        </td>
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
        const response = await axios.get(`organisms/getByState/${this.$route.params.stateId}`, { headers: { Authorization: 'Bearer ' + localStorage.getItem('token') } });
        this.organisms = response.data;
        console.log(this.organisms[0])
      } catch (error) {
        console.error('Error fetching organisms', error);
      }
    },
    editOrganism(id) {
      this.$router.push(`/environments/view/${this.$route.params.envId}/states/${this.$route.params.stateId}/organisms/edit/${id}`);
    },
    async deleteOrganism(id) {
      try {
        await axios.delete(`organisms/${id}`, { headers: { Authorization: 'Bearer ' + localStorage.getItem('token') } });
        // Assuming you want to refresh the organisms after deletion
        this.fetchOrganisms();
      } catch (error) {
        console.error('Error deleting organism', error);
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
