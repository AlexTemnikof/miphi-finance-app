<template>
  <form @submit.prevent="submit" class="form-signin">
    <h1 class="h3 mb-3 fw-normal">Sign up</h1>

    <input v-model="data.name" class="form-control" placeholder="Name" required>

    <select class="form-select w-100 ms-1" id="categoryDropdown" v-model="data.clientType">
      <option v-for="type in clientTypes" :key="type.value" :value="type.value">{{ type.label }}</option>
    </select>

    <div class="w-100">
      <input
          v-model="data.inn"
          type="number"
          class="form-control"
          placeholder="INN"
          required
          @input="validateInn"
      >
      <span v-if="innError" style="color: red;">ИНН должен состоять только из 11 цифр.</span>
    </div>
    <input v-model="data.bankName" class="form-control" placeholder="Name of the bank" required>

    <div>
      <input
          v-model="formattedPhoneNumber"
          type="text"
          class="form-control"
          placeholder="Phone number"
          @input="formatPhoneNumber"
          required
      />
    </div>
    <input v-model="data.email" type="email" class="form-control" placeholder="Email" required>

    <input v-model="data.login" class="form-control" placeholder="Login" required>

    <input v-model="data.password" type="password" class="form-control" placeholder="Password" required>

    <button class="w-100 btn btn-lg btn-primary" :disabled="innError" type="submit">Submit</button>

    <div v-if="showSuccessAlert" class="alert alert-success mt-3 show" role="alert">
      Registration Success
    </div>

    <div v-if="showErrorAlert" class="alert alert-danger mt-3 show" role="alert">
      Registration Error
    </div>
  </form>
</template>

<script>
import {reactive, ref} from 'vue';
import {useRouter} from 'vue-router';
import * as api from '@/api';
import ClientType from "@/data/ClientType";

export default {
  name: "RegisterPage",
  data() {
    return {
      clientTypes: [
        {value: ClientType.PERSON, label: 'Физическое лицо'},
        {value: ClientType.ORGANISATION, label: 'Юридическое лицо'},
      ],
      innError: false
    }
  },
  computed: {
    formattedPhoneNumber: {
      get() {
        return this.phoneNumber;
      }
    }
  },
  methods: {
    validateInn() {
      const inn = this.data.inn;
      // Проверяем, состоит ли ИНН из 11 цифр
      const isValid = /^\d{11}$/.test(inn);
      this.innError = !isValid;
    },
    formatPhoneNumber() {
      let value = this.phoneNumber.replace(/\D/g, ''); // Убираем все нецифровые символы
      if (value.length === 0) {
        this.phoneNumber = '';
        return;
      }
      if (value[0] === '8') {
        value = value.replace(/^8/, '7'); // Заменяем 8 на 7
      }
      if (value.length > 11) {
        value = value.slice(0, 11); // Ограничиваем длину номера
      }
      this.phoneNumber = value;
    }
  },
  setup() {
    const data = reactive({
      name: '',
      clientType: null,
      inn: null,
      bankName: null,
      phoneNumber: null,
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