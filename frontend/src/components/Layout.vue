<template>
  <div class="container">
    <nav class="navbar navbar-expand-lg navbar-light bg-light mb-4">
      <router-link to="/" class="navbar-brand">GenomeLab</router-link>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav mr-auto">
          <li v-if="isLoggedIn" class="nav-item">
            <router-link to="/environments" class="nav-link">Environment List</router-link>
          </li>
        </ul>
        <ul class="navbar-nav">
          <li v-if="isLoggedIn" class="nav-item">
            <a @click="logoutAction" class="nav-link" aria-current="page" href="#">Logout</a>
          </li>
          <li v-if="!isLoggedIn" class="nav-item">
            <router-link to="/login" class="nav-link">Login</router-link>
          </li>
        </ul>
      </div>
    </nav>

    <router-view></router-view>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isLoggedIn: localStorage.getItem('token') !== null,
    };
  },
  methods: {
    logoutAction() {
      localStorage.clear();
      this.isLoggedIn = false;
      this.$router.push('/');
    },
  },
};
</script>

<style scoped>
</style>
