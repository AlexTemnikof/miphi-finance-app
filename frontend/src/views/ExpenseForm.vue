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
      <div class="mb-3">
        <label for="amountInput" class="form-label">Введите описание:</label>
        <input type="text" class="form-control" id="-description" v-model="description">
      </div>
      <div class="mb-3">
        <label for="amountInput" class="form-label">Выберите тип операции:</label>
        <select class="form-select" id="categoryDropdown" v-model="selectedOperationType">
          <option v-for="type in operationTypes" :key="type.value" :value="type.value">{{ type.label }}</option>
        </select>
      </div>
      <button @click="saveExpense" type="submit" class="btn btn-primary" v-if="selectedOperationType && selectedCategory && expenseAmount">Сохранить</button>
    </form>

    <div v-if="showAlert" class="alert alert-success mt-3" role="alert">
      Расход успешно создан!
    </div>
  </div>
</template>

<script>

import * as api from '@/api';
import OperationType from "@/data/OperationType";

export default {
  data() {
    return {
      categories: [
      ],
      selectedCategory: null,
      expenseAmount: null,
      description: null,
      selectedOperationType: null,
      showAlert: false,
      operationTypes: [
        { value: OperationType.INCOME, label: 'Доход' },
        { value: OperationType.EXPENSE, label: 'Расход' }
      ]
    };
  },
  methods: {

    async fetchData() {
      try {
        const data = await api.categories.getAll();

        console.log('got data')
        console.log(data);

        this.categories = data.map(category => ({
          id: category.id,
          name: category.name,
          spendLimit: category.spendLimit,
          isEditing: false,
          originalName: "",
        }));
      } catch (err) {
        console.log("reposnse error");
        if (err.response && err.response.status === 403) {
          await this.redirectToLogin(); // Вызываем редирект при ошибке 403
        }
      }
    },

    saveExpense() {
      const data = {
        categoryId: this.selectedCategory,
        amount: this.expenseAmount,
        description: this.description,
        operationType: this.selectedOperationType
      }

      console.log('sending data');
      console.log(data);

      const submit = api.operations.create(data);


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
  async mounted() {
    await this.fetchData(); // Вызываем fetchData при монтировании компонента
  },
};
</script>
