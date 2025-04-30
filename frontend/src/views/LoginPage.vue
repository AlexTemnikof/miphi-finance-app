<template>
    <form @submit.prevent="submit" class="form-signin">
        <h1 class="h3 mb-3 fw-normal">Sign in</h1>

        <input v-model="data.login" type="text" class="form-control" placeholder="Login" required>

        <input v-model="data.password" type="password" class="form-control" placeholder="Password" required>

        <button class="w-100 btn btn-lg btn-primary" type="submit">Sign in</button>

        <div v-if="showAlert" class="alert alert-danger mt-3 show" role="alert">
          Authentication Error
        </div>
    </form>
</template>

<script>
import { reactive, ref } from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import * as api from '@/api';

export default {
    name: "LoginPage",
    setup() {
      const data = reactive({
          login: '',
          password: '',
      });

      const showAlert = ref(false);

      const router = useRouter();
      const store = useStore();

      const submit = async () => {
        try {
          const response = await api.auth.login(data)

          console.log(response);

          const { accessToken } = response;

          await store.dispatch('login', accessToken);

          await router.push('/');
        } catch(error) {
          console.log(error);
          showAlert.value = true;

          setTimeout(() => {
            showAlert.value = false;
          }, 2000)
        }
      }

      return {
        data,
        showAlert,
        submit,
      }
    }
}
</script>

