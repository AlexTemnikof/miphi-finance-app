<template>
  <div>
    <form>
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
        <label for="amountInput" class="form-label">Выберите тип операции:</label>
        <select class="form-select" id="categoryDropdown" v-model="selectedOperationType">
          <option v-for="type in operationTypes" :key="type.value" :value="type.value">{{ type.label }}</option>
        </select>
      </div>
      <div class="mb-3">
        <label for="amountInput" class="form-label">Выберите статус операции:</label>
        <select class="form-select" id="statusDropdown" v-model="selectedOperationStatus">
          <option v-for="type in operationStatuses" :key="type.value" :value="type.value">{{ type.label }}</option>
        </select>
      </div>
      <div class="mb-3">
        <label for="amountInput" class="form-label">Введите банк отправителя:</label>
        <input type="text" class="form-control" id="-description" v-model="senderBank">
      </div>
      <div class="mb-3">
        <label for="amountInput" class="form-label">Введите банк получателя:</label>
        <input type="text" class="form-control" id="-description" v-model="receiverBank">
      </div>
      <div class="mb-3">
        <label for="amountInput" class="form-label">Введите счет отправителя:</label>
        <input type="text" class="form-control" id="-description" v-model="senderAccountId">
      </div>
      <div class="mb-3">
        <label for="amountInput" class="form-label">Введите счет получателя:</label>
        <input type="text" class="form-control" id="-description" v-model="receiverAccountId">
      </div>
      <div class="mb-3">
        <label for="amountInput" class="form-label">Введите телефон получателя:</label>
        <input type="text" class="form-control" id="-description" v-model="receiverPhoneNumber">
      </div>
      <div class="mb-3">
        <label for="amountInput" class="form-label">Введите комментарий:</label>
        <input type="text" class="form-control" id="-description" v-model="description">
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
import OperationStatus from "@/data/OperationStatus";
import operationStatus from "@/data/OperationStatus";
import {nextTick} from "vue";
import router from "@/router";

export default {
  data() {
    return {
      categories: [
      ],
      selectedCategory: null,
      expenseAmount: null,
      description: null,
      selectedOperationType: null,
      selectedOperationStatus: null,
      senderBank: null,
      receiverBank: null,
      senderAccountId: null,
      receiverAccountId: null,
      receiverPhoneNumber: null,
      showAlert: false,
      operationTypes: [
        { value: OperationType.INCOME, label: 'Доход' },
        { value: OperationType.EXPENSE, label: 'Расход' }
      ],
      operationStatuses: [
        { value: OperationStatus.NEW, label: 'Новая' },
        { value: OperationStatus.CONFIRMED, label: 'Подтверждена' },
        { value: OperationStatus.IN_PROGRESS, label: 'В обработке' },
        { value: OperationStatus.CANCELLED, label: 'Отменена' },
        { value: OperationStatus.PAYED, label: 'Платеж выполнен' },
        { value: OperationStatus.PAYMENT_DELETED, label: 'Платеж удален' },
        { value: OperationStatus.RETURNED, label: 'Возврат' },
      ]
    };
  },
  methods: {

    async redirectToLogin() {
      console.log('redirecting');
      await nextTick();
      await router.push('/login');
    },

    async fetchData() {
      try {
        const data = await api.categories.getAll();

        console.log('got data')
        console.log(data);

        this.categories = data.map(category => ({
          id: category.id,
          name: category.name,
          spendLimit: category.spendLimit,
        }));
      } catch (err) {
        console.log("reposnse error");
        if (err.response && err.response.status === 403) {
          await this.redirectToLogin();
        }
      }
    },

    async saveExpense() {
      const data = {
        categoryId: this.selectedCategory,
        amount: this.expenseAmount,
        description: this.description,
        operationType: this.selectedOperationType,
        status: operationStatus.NEW,
        senderBank: this.senderBank,
        receiverBank: this.receiverBank,
        senderAccountId: this.senderAccountId,
        receiverAccountId: this.receiverAccountId,
        receiverPhoneNumber: this.receiverPhoneNumber
      }

      const submit = await api.operations.create(data);

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
