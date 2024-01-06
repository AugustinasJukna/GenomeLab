<template>
  <div>
    <h2>Edit Organism</h2>

    <form @submit.prevent="updateOrganism">
      <div class="form-group">
        <label for="energy">Energy:</label>
        <input type="text" v-model="editedOrganism.energy" class="form-control" required>
      </div>

      <div class="form-group">
        <label for="x">Position X:</label>
        <input type="text" v-model="editedOrganism.x" class="form-control" required>
      </div>

      <div class="form-group">
        <label for="y">Position Y:</label>
        <input type="text" v-model="editedOrganism.y" class="form-control" required>
      </div>

      <div class="form-group">
        <label for="genes">Genes:</label>
        <textarea v-model="editedOrganism.genes" class="form-control" rows="4" required></textarea>
      </div>

      <button type="submit" class="btn btn-primary">Update Organism</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      editedOrganism: {
        id: null,
        energy: '',
        x: '',
        y: '',
        genes: '',
      },
    };
  },
  methods: {
    async updateOrganism() {
      try {
        console.log(this.editedOrganism)
        await axios.patch(`organisms/${this.editedOrganism.id}`, this.editedOrganism, {
          headers: { Authorization: 'Bearer ' + localStorage.getItem('token') },
        });
        const response = await axios.get(`organisms/${this.$route.params.organismId}`, {
          headers: { Authorization: 'Bearer ' + localStorage.getItem('token') },
        });
        console.log(response)
        this.$router.push(`/environments/view/${this.$route.params.envId}/states/${this.$route.params.stateId}/organisms`);
      } catch (error) {
        console.error('Error updating organism', error);
      }
    },
    async fetchOrganism() {
      try {
        const response = await axios.get(`organisms/${this.$route.params.organismId}`, {
          headers: { Authorization: 'Bearer ' + localStorage.getItem('token') },
        });
        this.editedOrganism = response.data;
      } catch (error) {
        console.error('Error fetching organism for editing', error);
      }
    },
  },
  mounted() {
    this.fetchOrganism();
  },
};
</script>

<style scoped>
</style>
