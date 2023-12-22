<template>
  <div class="container mt-4">
    <h2 class="text-center mb-4">User Profile</h2>
    <div v-if="isLoggedIn" class="profile-table-container">
      <table class="table table-bordered table-responsive-sm mx-auto">
        <tbody>
        <tr>
          <th class="align-middle">Role:</th>
          <td class="align-middle text-break">{{ userRole }}</td>
        </tr>
        <tr>
          <th class="align-middle">Token:</th>
          <td class="align-middle text-break">{{ userToken }}</td>
        </tr>
        </tbody>
      </table>
    </div>
    <p v-else class="text-center">Please log in to view your profile.</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isLoggedIn: localStorage.getItem('token') !== null,
      userRole: '',
      userToken: '',
    };
  },
  watch: {
    $route() {
      this.isLoggedIn = localStorage.getItem('token') !== null;
      this.getUserInfo();
    },
  },
  mounted() {
    this.getUserInfo();
  },
  methods: {
    getUserInfo() {
      const isAuthenticated = localStorage.getItem('token') !== null;
      if (isAuthenticated) {
        const decodedToken = this.parseJwt(localStorage.getItem('token'));
        this.userRole = decodedToken.role;
        this.userToken = localStorage.getItem('token');
      } else {
        this.userRole = '';
        this.userToken = '';
      }
    },
    parseJwt(token) {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));

      return JSON.parse(jsonPayload);
    },
  },
};
</script>

<style scoped>
.container {
  max-width: 600px;
  margin: auto;
}

.text-center {
  text-align: center;
}

.mb-4 {
  margin-bottom: 1.5rem;
}

.profile-table-container {
  display: flex;
  justify-content: center;
}

.table th, .table td {
  height: 50px;
}

.table-responsive-sm {
  overflow-x: auto;
}

.text-break {
  word-break: break-word;
}
</style>
