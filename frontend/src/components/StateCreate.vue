<template>
  <div class="container" :class="{ 'change-bg': isBgChanged }">
    <h1 key="heading" class="fade-in">Evolution Simulator</h1>
    <button @click="saveState" class="btn btn-success" style="margin-bottom: 2%">Save State</button>
    <iframe
        ref="iframe"
        src="http://localhost:3500/"
        width="810"
        height="820"
        scrolling="no"
    ></iframe>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      isBgChanged: false,
    };
  },
  mounted() {
    this.$refs.iframe.width = "800";
    this.$refs.iframe.height = "820";
  },
  methods: {
    async saveState() {
      try {
        const responseJson = await fetch('http://localhost:3500/readJson');
        const data = await responseJson.json();

        const environmentResponse = await axios.get(`/environments/${this.$route.params.id}`, {
          headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
        });

        const currentEnvironment = environmentResponse.data;
        console.log(currentEnvironment)

        const currentDate = new Date().toISOString();
        const stateResponse = await axios.post('/states', {
          date: currentDate,
          environment: currentEnvironment,
        }, { headers: { Authorization: 'Bearer ' + localStorage.getItem('token') } });

        const state = stateResponse.data;

        const previousStateId = state.id;
        const organismsToDeleteResponse = await axios.get(`/organisms/deleteByState/${previousStateId}`, {
          headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
        });
        const organismsToDelete = organismsToDeleteResponse.data;

        if (organismsToDelete.length > 0) {
          await axios.delete('/organisms/deleteBatch', {
            data: organismsToDelete,
            headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
          });
        }


        const jsonData = JSON.parse(data.jsonData);
        const organismsData = jsonData.map(organism => {
          // eslint-disable-next-line no-unused-vars
          const { genes, ...organismWithoutGenes } = organism;
          return {
            ...organismWithoutGenes,
            state: {
              ...state,
              environment: currentEnvironment,
            }
          };
        });

        console.log('Organisms data:', organismsData)

        const response = await axios.post('/organisms/addBatch', organismsData, {
          headers: {
            Authorization: 'Bearer ' + localStorage.getItem('token')
          }
        });

        console.log('Organisms data saved successfully:', response.data);
        this.isBgChanged = true;

        setTimeout(() => {
          this.isBgChanged = false;
        }, 2000);

      } catch (error) {
        console.error('Error saving organisms data:', error);
      }
    },

  },
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
  transition: background-color 0.5s ease;
}

iframe {
  border: none;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

button {
  margin-top: 10px;
  padding: 8px 16px;
  font-size: 14px;
  cursor: pointer;
  background-color: #4caf50;
  color: #fff;
  border: none;
  border-radius: 4px;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #45a049;
}

.container.change-bg {
  background-color: #21D801;
}
</style>
