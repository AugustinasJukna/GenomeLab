<template>
  <div>
    <h2>Create Organism</h2>

    <form @submit.prevent="createOrganism">
      <div class="form-group">
        <label for="energy">Energy:</label>
        <input type="text" v-model="newOrganism.energy" class="form-control" required>
      </div>

      <div class="form-group">
        <label for="x">Position X:</label>
        <input type="text" v-model="newOrganism.x" class="form-control" required>
      </div>

      <div class="form-group">
        <label for="y">Position Y:</label>
        <input type="text" v-model="newOrganism.y" class="form-control" required>
      </div>

      <div class="form-group">
        <label for="genes">Genes:</label>
        <textarea v-model="newOrganism.genes" class="form-control" rows="4" required></textarea>
      </div>

      <button type="submit" class="btn btn-success">Create Organism</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      newOrganism: {
        energy: '',
        x: '',
        y: '',
        genes: '',
      },
    };
  },
  methods: {
    async createOrganism() {
      try {
        const environmentResponse = await axios.get(`/environments/${this.$route.params.envId}`, {
          headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
        });


        const stateResponse = await axios.get(`/states/${this.$route.params.stateId}`, {
          headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
        });

        this.newOrganism.state = stateResponse.data;
        this.newOrganism.state.environment = environmentResponse.data;

        await axios.post('/organisms/', this.newOrganism, {
          headers: { Authorization: 'Bearer ' + localStorage.getItem('token') },
        });

        this.$router.push(`/environments/view/${this.$route.params.envId}/states/${this.$route.params.stateId}/organisms`);
      } catch (error) {
        console.error('Error creating organism', error);
      }
    },
  },
};
</script>

<style scoped>
</style>
