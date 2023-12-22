<template>
  <layout-div>
    <div class="row justify-content-md-center mt-5">
      <div class="col-4">
        <div class="card">
          <div class="card-body">
            <h5 class="card-title mb-4">Sign In</h5>
            <form>
              <p v-if="Object.keys(validationErrors).length != 0" class='text-center'>
                <small class='text-danger'>Incorrect Email or Password</small>
              </p>
              <div class="mb-3">
                <label htmlFor="email" class="form-label">Email address</label>
                <input
                    v-model="email"
                    type="email"
                    class="form-control"
                    id="email"
                    name="email"
                    required
                />
                <div v-if="validationErrors.email" class="flex flex-col">
                  <small class="text-danger">{{ validationErrors?.email[0] }}</small>
                </div>
              </div>
              <div class="mb-3">
                <label htmlFor="password" class="form-label">Password</label>
                <input
                    v-model="password"
                    type="password"
                    class="form-control"
                    id="password"
                    name="password"
                    required
                />
                <div v-if="validationErrors.password" class="flex flex-col">
                  <small class="text-danger">{{ validationErrors?.password[0] }}</small>
                </div>
              </div>
              <div class="d-grid gap-2">
                <button
                    :disabled="isSubmitting"
                    @click="loginAction"
                    type="button"
                    class="btn btn-primary btn-block"
                >
                  Login
                </button>
                <p class="text-center">
                  Don't have an account? <router-link to="/register">Register here </router-link>
                </p>
                <!-- Display a message if any required field is empty -->
                <div v-if="isAnyFieldEmpty" class="flex flex-col">
                  <small class="text-danger">All fields are required</small>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </layout-div>
</template>

<script>
import axios from 'axios';
import LayoutDiv from './LayoutDiv.vue';

export default {
  name: 'LoginPage',
  components: {
    LayoutDiv,
  },
  data() {
    return {
      email: '',
      password: '',
      validationErrors: {},
      isSubmitting: false,
      isAnyFieldEmpty: false, // New property to track if any required field is empty
    };
  },
  created() {
    if (localStorage.getItem('token') != "" && localStorage.getItem('token') != null) {
      this.$router.push('/environments');
    }
  },
  methods: {
    loginAction() {
      this.isSubmitting = true;

      // Check if any required field is empty
      if (this.email === '' || this.password === '') {
        this.isAnyFieldEmpty = true;
        this.isSubmitting = false;
        return; // Stop further processing if any required field is empty
      }

      let payload = {
        email: this.email,
        password: this.password,
      };

      axios.post('/auth/authenticate', payload)
          .then(response => {
            localStorage.setItem('token', response.data.token);
            axios.get(`http://localhost:8080/api/v1/users/byEmail/` + this.email, {
              headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
            })
                .then((r) => {
                  localStorage.setItem('username', r.data.id);
                  localStorage.setItem('user', JSON.stringify(r.data));
                  return r;
                })
                .catch((e) => {
                  return e;
                });
            this.$router.push('/environments');
            return response;
          })
          .catch(error => {
            this.isSubmitting = false;
            if (error.response.data.errors != undefined) {
              this.validationErrors = error.response.data.errors;
            }
            if (error.response.data.error != undefined) {
              this.validationErrors = error.response.data.error;
            }
            return error;
          });
    }
  },
};
</script>
