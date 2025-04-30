<template>
    <form @submit.prevent="submit" class="form-signin">
        <h1 class="h3 mb-3 fw-normal">Sign up</h1>

        <input v-model="data.name" class="form-control" placeholder="Name" required>

        <input v-model="data.login" class="form-control" placeholder="Login" required>

        <input v-model="data.email" type="email" class="form-control" placeholder="Email" required>

        <input v-model="data.password" type="password" class="form-control" placeholder="Password" required>

        <button class="w-100 btn btn-lg btn-primary" type="submit">Submit</button>

        <div v-if="showSuccessAlert" class="alert alert-success mt-3 show" role="alert">
          Registration Success
        </div>

        <div v-if="showErrorAlert" class="alert alert-danger mt-3 show" role="alert">
          Registration Error
        </div>
    </form>
</template>

<script>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import * as api from '@/api';

export default {
    name: "RegisterPage",
    setup() {
      const data = reactive({
          name: '',
          login: '',
          email: '',
          password: ''
      });
      const router = useRouter();
      const showSuccessAlert = ref(false);
      const showErrorAlert = ref(false);

      const submit = async () => {
        try {
          const response = await api.auth.register(data);
          console.log(response);

          showSuccessAlert.value = true;

          setTimeout(async () => {
            showSuccessAlert.value = false;
            await router.push('/login');
          }, 3000)
        } catch {
          showErrorAlert.value = true;

          setTimeout(() => {
            showErrorAlert.value = false;
          }, 2000)
        }
      }

      return {
          data,
          showSuccessAlert,
          showErrorAlert,
          submit
      }
    }
}
</script>