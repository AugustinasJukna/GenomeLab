<template>
  <div class="container">
    <h1 style="margin-top: 2%" key="heading">Evolution Simulator</h1>
    <iframe
        ref="iframe"
        :width="iframeWidth"
        :height="iframeHeight"
        :src="iframeSrc"
        scrolling="no"
    ></iframe>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      iframeWidth: 800,
      iframeHeight: 820,
      iframeSrc: 'http://localhost:3500/',
      defaultConstants: {
        ORGANISMS_COUNT: 50,
        GENERATION_COUNT: 1,
        FOOD_COUNT: 200,
        ELITE_COUNT: 5,
        MUTATION_COEFFICIENT: 0.3,
        GENERATIONS_COUNT: 1,
      },
    };
  },
  mounted() {
    this.sendDefaultConstants();
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
    async sendDefaultConstants() {
      try {
        await axios.post('http://localhost:3500/writeConstants', this.defaultConstants);
        console.log('Default constants sent successfully');
      } catch (error) {
        console.error('Error sending default constants', error);
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
  background-color: #f5f5f5;
}

h1 {
  margin-bottom: 10px;
}

iframe {
  border: none;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  max-width: 100%;
  max-height: 100%;
  margin-bottom: 2%;
}
</style>
