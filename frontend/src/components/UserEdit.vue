<template>
  <div class="container mt-4">
    <h2 class="text-center mb-4">Edit User</h2>
    <form @submit.prevent="updateUser">
      <div class="mb-3">
        <label for="name" class="form-label">Name</label>
        <input v-model="user.name" type="text" class="form-control" id="name" required>
      </div>
      <div class="mb-3">
        <label for="email" class="form-label">Email</label>
        <input v-model="user.email" type="email" class="form-control" id="email" required>
      </div>
      <div class="mb-3">
        <label for="password" class="form-label">Password</label>
        <input v-model="user.password" type="password" class="form-control" id="password" required>
      </div>
      <div class="mb-3">
        <label for="role" class="form-label">Role</label>
        <select v-model="user.role" class="form-select" id="role" required>
          <option value="RESEARCHER">RESEARCHER</option>
          <option value="ADMIN">ADMIN</option>
        </select>
      </div>
      <button type="submit" class="btn btn-primary">Update User</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      user: {
        id: null,
        name: '',
        email: '',
        password: '',
        role: 'RESEARCHER',
      },
    };
  },
  mounted() {
    const userId = this.$route.params.id;
    this.fetchUserDetails(userId);
  },
  methods: {
    async fetchUserDetails(userId) {
      try {
        const response = await axios.get('/users/' + userId, {
          headers: { Authorization: 'Bearer ' + localStorage.getItem('token') },
        });
        this.user = response.data;
        console.log(this.user)
      } catch (error) {
        console.error('Error fetching users', error);
      }
    },
    async updateUser() {
      try {
        const request = {
          name: this.user.name,
          email: this.user.email,
          password: this.user.password,
          role: this.user.role,
        }
        await axios.patch(`/users/${this.user.id}`, request, {
          headers: { Authorization: 'Bearer ' + localStorage.getItem('token') },
        });
        this.$router.push('/users');
      } catch (error) {
        console.error('Error updating user', error);
      }
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
</style>
