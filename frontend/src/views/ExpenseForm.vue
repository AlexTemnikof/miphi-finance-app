<template>
  <div>
    <form @submit.prevent="saveExpense">
      <div class="mb-3">
        <label for="categoryDropdown" class="form-label">Выберите категорию:</label>
        <select class="form-select" id="categoryDropdown" v-model="selectedCategory">
          <option v-for="category in categories" :key="category.id" :value="category.id">{{ category.name }}</option>
        </select>
      </div>
      <div class="mb-3">
        <label for="amountInput" class="form-label">Введите сумму:</label>
        <input type="number" class="form-control" id="amountInput" v-model="expenseAmount">
      </div>
      <button type="submit" class="btn btn-primary">Сохранить</button>
    </form>

    <div v-if="showAlert" class="alert alert-success mt-3" role="alert">
      Расход успешно создан!
    </div>
  </div>
</template>

<script>

//TODO: Auth check

import { reactive } from "vue";
import * as api from '@/api';

export default {
  props: {
    categories: Array,
  },
  data() {
    return {
      selectedCategory: null,
      expenseAmount: null,
      showAlert: false,
    };
  },
  methods: {

    saveExpense() {
      const data = reactive({
        operationType: '',
        amount: '',
      });

      const submit = async () => {
        await api.operations.create(data);
      }

      this.showAlert = true;

      setTimeout(() => {
        this.showAlert = false;
      }, 2000);

      return {
        data,
        submit
      }
    }
  },
};
</script>
