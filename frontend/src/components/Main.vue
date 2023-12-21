<template>
  <div class="container">
      <h1 key="heading" >Evolution Simulator</h1>
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
export default {
  mounted() {
    this.$refs.iframe.width = "800";
    this.$refs.iframe.height = "820";
  }
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
}

iframe {
  border: none;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}


</style>
