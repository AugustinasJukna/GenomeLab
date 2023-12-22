<template>
  <div class="container mt-4">
    <h2 class="text-center mb-4">User List</h2>
    <div class="table-responsive mx-auto mt-0">
      <table class="table" v-if="users.length > 0">
        <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Email</th>
          <th>Role</th>
          <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="user in users" :key="user.id">
          <td>{{ user.id }}</td>
          <td>{{ user.name }}</td>
          <td>{{ user.email }}</td>
          <td>{{ user.role }}</td>
          <td>
            <router-link :to="'/users/edit/' + user.id" class="btn btn-warning" style="margin-right: 5px">Edit</router-link>
          </td>
        </tr>
        </tbody>
      </table>
      <p v-else class="mt-3">No users available.</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      users: [],
    };
  },
  mounted() {
    this.fetchUsers();
  },
  methods: {
    async fetchUsers() {
      try {
        const response = await axios.get('/users', {
          headers: { Authorization: 'Bearer ' + localStorage.getItem('token') },
        });
        this.users = response.data;
      } catch (error) {
        console.error('Error fetching users', error);
      }
    },
  },
};
</script>

<style scoped>
.container {
  max-width: 960px;
}

.table-responsive {
  overflow-x: auto;
  margin-top: 10px;
}

.table {
  width: 100%;
  margin-bottom: 1rem;
  background-color: #fff;
  border: 1px solid rgba(0, 0, 0, 0.125);
  border-radius: 0.25rem;
}

.table th,
.table td {
  padding: 0.75rem;
  text-align: left;
  border-bottom: 1px solid #dee2e6;
}

.table th {
  vertical-align: bottom;
  border-bottom: 2px solid #dee2e6;
}

.table tbody tr:hover {
  background-color: rgba(0, 0, 0, 0.075);
}

.text-center {
  text-align: center;
}

.mb-4 {
  margin-bottom: 1.5rem;
}

.mt-3 {
  margin-top: 0.75rem;
}
</style>
