<template>
  <div class="container mt-4">
    <h2 class="text-center mb-4">Environment Details</h2>
    <ConfirmationModal :message="confirmationMessage" :onConfirm="deleteState" />
    <div class="card mx-auto overflow-hidden">
      <div class="row no-gutters">
        <div class="col-md-6 p-3 overflow:hidden">
          <table class="table table-hover">
            <tbody>
            <tr>
              <th>ID:</th>
              <td>{{ environment.id }}</td>
            </tr>
            <tr>
              <th>Name:</th>
              <td>{{ environment.name }}</td>
            </tr>
            <tr>
              <th>Description:</th>
              <td>{{ environment.description }}</td>
            </tr>
            <tr>
              <th>Organisms Count:</th>
              <td>{{ environment.organismsCount }}</td>
            </tr>
            <tr>
              <th>Mutation Coefficient:</th>
              <td>{{ environment.mutationCoefficient }}</td>
            </tr>
            <tr>
              <th>Food Count:</th>
              <td>{{ environment.foodCount }}</td>
            </tr>
            <tr>
              <th>Elite Count:</th>
              <td>{{ environment.eliteCount }}</td>
            </tr>
            </tbody>
          </table>
        </div>
        <div class="col-md-6">
          <img src="../assets/images/env.jpg" alt="Environment Image" class="img-fluid rounded mx-auto d-block" style="box-shadow: 5px 5px 15px rgba(0, 0, 0, 0.2); max-width: 300px; max-height: 400px;">
        </div>
      </div>
    </div>
    <div class="text-center mt-3">
      <button class="btn btn-success" @click="createNewState">Create New State</button>
    </div>
    <div class="mt-4 text-center">
      <div class="table-responsive mx-auto mt-0">
        <table class="table" v-if="states.length > 0">
          <thead>
          <tr>
            <th>ID</th>
            <th>Date</th>
            <th>Actions</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="state in states" :key="state.id">
            <td>{{ state.id }}</td>
            <td>{{ state.date }}</td>
            <td>
              <button class="btn btn-primary" style="margin-right: 10px" @click="viewOrganisms(state.id)">View Organisms</button>
              <button class="btn btn-danger" @click="showConfirmationModal(state.id)">Delete</button>
            </td>
          </tr>
          </tbody>
        </table>
        <p v-else class="mt-3">No states available for this environment.</p>
      </div>
    </div>
    <transition name="fade">
      <ConfirmationModal v-if="isConfirmationVisible" :message="confirmationMessage" :onConfirm="deleteState" />
    </transition>
  </div>
</template>

<script>
import axios from 'axios';
import ConfirmationModal from "@/components/ConfirmationModal.vue";

export default {
  components: { ConfirmationModal },
  data() {
    return {
      environment: {
        id: null,
        name: '',
        description: '',
        mutationCoefficient: 0,
        organismsCount: 0,
        foodCount: 0,
        eliteCount: 0,
      },
      states: [],
      confirmationMessage: '',
      selectedStateId: null,
      isConfirmationVisible: false,
    };
  },
  mounted() {
    this.fetchEnvironment();
  },
  methods: {
    async fetchEnvironment() {
      try {
        const response = await axios.get(`/environments/${this.$route.params.id}`, { headers: { Authorization: 'Bearer ' + localStorage.getItem('token') } });
        this.environment = response.data;
        const response2 = await axios.get(`/environments/${this.$route.params.id}/states`, { headers: { Authorization: 'Bearer ' + localStorage.getItem('token') } });
        this.states = response2.data;
      } catch (error) {
        console.error('Error fetching environment', error);
        if (error.response.status === 404) {
          this.states = [];
        }
      }
    },
    launchState(stateId) {
      console.log(`Launching state with ID ${stateId}`);
    },
    showConfirmationModal(stateId) {
      this.selectedStateId = stateId;
      this.confirmationMessage = 'Are you sure you want to delete this state?';
      this.isConfirmationVisible = true;
      const modalElement = document.getElementById('confirmationModal');
      modalElement.classList.add('show');
      modalElement.style.display = 'block';
    },
    deleteState() {
      if (this.selectedStateId !== null) {
        axios.delete(`/states/${this.selectedStateId}`, { headers: { Authorization: 'Bearer ' + localStorage.getItem('token') } })
            .then(() => {
              console.log('State deleted');
              this.fetchEnvironment();
            })
            .catch((error) => {
              console.error('Error deleting state', error);
            })
            .finally(() => {
              this.selectedStateId = null;
              this.isConfirmationVisible = false;
            });
      }
    },
    viewOrganisms(stateId) {
      this.$router.push(`/states/${stateId}/organisms`);
    },
    createNewState() {
      this.$router.push(`/states/create/${this.$route.params.id}`);
    },
  },
};
</script>

<style scoped>
.card {
  width: 100%;
  margin: auto;
  display: flex;
  flex-direction: column;
}

.img-fluid {
  width: 100%;
  height: 100%;
  max-height: 100%;
  display: block;
  margin: 0 auto;
}

.table-responsive {
  overflow-x: auto;
  margin-top: 10px;
}
</style>


