<template>
  <div class="container" :class="{ 'change-bg': isBgChanged }">
    <h1 style="margin-top: 2%" key="heading" class="fade-in">Evolution Simulator</h1>
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
    this.sendConstants();
    this.updateIframeDimensions();
    window.addEventListener('resize', this.updateIframeDimensions);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.updateIframeDimensions);
  },
  methods: {
    updateIframeDimensions() {
      this.iframeWidth = Math.min(window.innerWidth - 20, 800);
      this.iframeHeight = Math.min(window.innerHeight - 20, 820);
    },
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
        const organismsToDeleteResponse = await axios.get(`/organisms/getByState/${previousStateId}`, {
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
          organism.genes = JSON.stringify(organism.genes);
          console.log(organismWithoutGenes)
          return {
            x: organismWithoutGenes.x,
            y: organismWithoutGenes.y,
            energy: organismWithoutGenes.energy,
            genes: organism.genes,
            state: {
              ...state,
              environment: currentEnvironment,
            }
          };
        });
        console.log(organismsData)


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
    async sendConstants() {
      try {
        const environmentResponse = await axios.get(`/environments/${this.$route.params.id}`, {
          headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
        });

        const currentEnvironment = environmentResponse.data;

        const {
          mutationCoefficient,
          organismsCount,
          foodCount,
          eliteCount
        } = currentEnvironment;

        const constantsData = {
          MUTATION_COEFFICIENT: mutationCoefficient,
          ORGANISMS_COUNT: organismsCount,
          FOOD_COUNT: foodCount,
          ELITE_COUNT: eliteCount,
          GENERATIONS_COUNT: 1,
        };

        const response = await axios.post('http://localhost:3500/writeConstants', constantsData, {
          headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
        });

        console.log('Constants sent successfully:', response.data);

      } catch (error) {
        console.error('Error sending constants:', error);
      }
    },

  },
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
  transition: background-color 0.5s ease;
}

iframe {
  border: none;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  max-width: 100%;
  max-height: 100%;
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
