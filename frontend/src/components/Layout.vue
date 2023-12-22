<template>
  <div class="container">
    <nav class="navbar navbar-expand-lg navbar-light bg-light mb-4">
      <router-link to="/" class="navbar-brand ml-3">GenomeLab</router-link>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav mr-auto">
          <li v-if="isLoggedIn" class="nav-item">
            <router-link to="/environments" class="nav-link">Environments</router-link>
          </li>
          <li v-if="isAdmin" class="nav-item">
            <router-link to="/users" class="nav-link">Users</router-link>
          </li>
        </ul>
        <ul class="navbar-nav">
          <li v-if="isLoggedIn" class="nav-item">
            <router-link to="/profile" class="nav-link">
              <img src="./icons/profile.svg" alt="Profile Icon" class="profile-icon" />
            </router-link>
          </li>
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
    <footer class="footer mt-4">
      <div class="container text-center" id="footerDiv">
        <p>&copy; 2023 GenomeLab. All rights reserved.</p>
      </div>
    </footer>
  </div>

</template>

<script>
export default {
  data() {
    return {
      isLoggedIn: localStorage.getItem('token') !== null,
      isAdmin: false,
    };
  },
  watch: {
    $route() {
      this.isLoggedIn = localStorage.getItem('token') !== null;
      this.checkAdminStatus();
    },
  },
  mounted() {
    this.checkAdminStatus();
  },
  methods: {
    logoutAction() {
      localStorage.clear();
      this.$router.push('/');
    },
    parseJwt(token) {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));

      return JSON.parse(jsonPayload);
    },
    checkAdminStatus() {
      const isAuthenticated = localStorage.getItem('token') !== null;
      if (isAuthenticated) {
        const decodedToken = this.parseJwt(localStorage.getItem('token'));
        const userRoles = decodedToken.role.split(',');
        this.isAdmin = userRoles.includes('ROLE_ADMIN');
      } else {
        this.isAdmin = false;
      }
    },
  },
};
</script>

<style>
body {
  font-family: 'Kalnia', serif;
}

.navbar {
  border-bottom: 1px solid #dee2e6;
}

.navbar-brand {
  font-weight: bold;
  font-size: 1.5rem;
}

.navbar-toggler-icon {
  background-color: #007bff;
}

.navbar-toggler:focus {
  outline: none;
}

.navbar-nav .nav-link {
  padding: 0.5rem 1rem;
}

.nav-item {
  margin-right: 10px;
}

.nav-link:hover {
  color: #0056b3;
}

.container {
  max-width: 960px;
}

.ml-3 {
  margin-left: 15px;
}

.profile-icon {
  width: 20px;
  height: 20px;
  margin-right: 5px;
}

.footer {
  padding: 20px 0;
  width: 100%;
  background-color: #f8f9fa;
  display: flex;
  align-items: center; /* Center the content vertically */
}

.footer > .container {
  max-width: 960px;
  margin: auto;
}

#footerDiv {
  background-color: #f8f9fa;
  text-align: center; /* Center the text horizontally */
}
</style>
