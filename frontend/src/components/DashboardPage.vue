<template>
  <layout-div>
    <div class="row justify-content-md-center">
      <div class="col-12">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
          <div class="container-fluid">
            <a class="navbar-brand" href="#">Dashboard</a>
            <div class="d-flex">
              <ul class="navbar-nav">
                <li class="nav-item">
                  <a @click="logoutAction()" class="nav-link " aria-current="page" href="#">Logout</a>
                </li>
              </ul>
            </div>
          </div>
        </nav>
        <h2 class="text-center mt-5">Welcome, {{user?.name}}!</h2  >
      </div>
    </div>
  </layout-div>
</template>

<script>
import axios from 'axios';
import LayoutDiv from './LayoutDiv.vue';

export default {
  name: 'DashboardPage',
  components: {
    LayoutDiv,
  },
  data() {
    return {
      user: {},
    };
  },
  created() {
    console.log(this.getUser());
    if(localStorage.getItem('token') == "" || localStorage.getItem('token') == null){
      this.$router.push('/')
    }else {
      this.getUser();
    }

  },
  methods: {
    getUser() {
      axios.get(`http://localhost:8080/api/v1/users/` + localStorage.getItem('username'),  { headers:{Authorization: 'Bearer ' + localStorage.getItem('token')}})
          .then((r) => {
            this.user = r.data;
            return r
          })
          .catch((e) => {
            return e
          });
    },

    logoutAction () {
            localStorage.setItem('token', "")
            this.$router.push('/')
    }

  },
};
</script>