<template>
  <div class="container">
    <!-- Кнопки -->
    <div>
      <div class="button-container">
        <button type="button" class="btn btn-outline-dark category" data-bs-toggle="modal"
                data-bs-target="#categoryEditorModal">
          Категории
        </button>

        <!-- Модальное окно CategoryEditor -->
        <div class="modal fade" id="categoryEditorModal" tabindex="-1"
             aria-labelledby="categoryEditorModalLabel"
             aria-hidden="true">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="btn-close" data-bs-dismiss="modal"
                        aria-label="Close"></button>
              </div>
              <div class="modal-body">
                <CategoryEditor/>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Закрыть</button>
              </div>
            </div>
          </div>
        </div>

        <!-- Кнопка "Расходы" для открытия модального окна с ExpenseForm -->
        <button type="button" class="btn btn-outline-dark expenses" data-bs-toggle="modal"
                data-bs-target="#expensesModal">
          Операция
        </button>

        <!-- Модальное окно для расходов с ExpenseForm -->
        <div class="modal fade" id="expensesModal" tabindex="-1" aria-labelledby="expensesModalLabel"
             aria-hidden="true">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="expensesModalLabel">Операция</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"
                        aria-label="Close"></button>
              </div>
              <div class="modal-body">
                <!-- Используем компонент ExpenseForm только если есть категории -->
                <ExpenseForm :categories="categories" @save="closeModal"/>
              </div>
            </div>
          </div>
        </div>

      </div>
    </div>
    <hr>
    <!-- Инпут поле для поиска в таблице -->
    <div class="search-row-container">
      <div class="search-column-container left-column">
        <div class="search-column-container">
          <label for="search">Найти операцию</label>
          <input type="text" id="search" class="input" placeholder="Введите дату, сумму, тип или id операции"
                 v-model="searchQuery" autocomplete="off"/>
        </div>
        <!-- Фильтры по дате и объему операции -->
        <div class="search-column-container filters">
          <div class="search-row-container">
            <div class="search-column-container date-left">
              <label for="startDate">От даты:</label>
              <input type="date" id="startDate" style="cursor: pointer;" class="input"
                     v-model="startDate"/>
            </div>
            <div class="search-column-container date-right">
              <label for="endDate">До даты:</label>
              <input type="date" id="endDate" style="cursor: pointer;" class="input" v-model="endDate"/>
            </div>
          </div>
          <div class="search-column-container">
            <div class="search-column-container price">
              <label for="minAmount">От суммы:</label>
              <input type="number" id="minAmount" class="input" v-model="minAmount"/>
            </div>
            <div class="search-column-container price">
              <label for="maxAmount">До суммы:</label>
              <input type="number" id="maxAmount" class="input" v-model="maxAmount"/>
            </div>
          </div>
        </div>
      </div>
      <!-- Кнопка "Применить" -->
      <!-- <button @click="applyFilters" class="btn btn-outline-dark">Применить</button> -->
      <!-- Фильтрация применяется динамически при изменении input, так что в кнопке нет необходимости -->
      <hr>
      <!-- Таблица операций -->
      <div class="right-column">
        <table class="table table-success table-striped">
          <thead>
          <tr>
            <th>Дата</th>
            <th>Сумма</th>
            <th>Тип операции</th>
            <th>Статус операции</th>
            <th>Номер счета получателя</th>
            <th>Банк получателя</th>
            <th>Номер телефона получателя</th>
            <th>Описание</th>
            <th>Действия</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="operation in filteredOperations" :key="operation.id">
            <td style="width: 27%">
              {{
                new Date(operation.date).toLocaleString('ru-RU', {
                  year: 'numeric',
                  month: 'long',
                  day: 'numeric',
                  hour: '2-digit',
                  minute: '2-digit'
                })
              }}
            </td>            <td style="width: 18%">{{ operation.amount }}</td>
            <td>{{ operation.operationType === 'INCOME' ? 'Доход' : 'Расход'}}</td>
            <td>{{ operation.status }}</td>
            <td>{{ operation.receiverAccountId }}</td>
            <td>{{ operation.receiverBank }}</td>
            <td>{{ operation.receiverPhoneNumber }}</td>
            <td>{{ operation.description }}</td>
            <td>
              <template v-if="operation.isEditing">
                <span style="cursor: pointer;">✔️</span>
                <span style="cursor: pointer;">✘</span>
              </template>
              <template v-else>
                <span style="cursor: pointer;">✎</span>
                <span style="cursor: pointer;">🗑</span>
              </template>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>

//TODO: Auth check
import * as api from '@/api';

import CategoryEditor from './CategoryEditor.vue';
import ExpenseForm from './ExpenseForm.vue';

export default {
  components: {
    ExpenseForm,
    CategoryEditor,
  },
  data() {
    return {
      categories: [],
      searchQuery: "",
      startDate: null,
      endDate: null,
      minAmount: null,
      maxAmount: null,
      operations: [],
    };
  },
  computed: {
    filteredOperations() {
      // Реализация фильтрации данных по условиям
      // Используйте операции.filter() согласно выбранным фильтрам
      // Например:
      return this.operations.filter((operation) => {
        const dateCondition =
            (!this.startDate || operation.date >= this.startDate) &&
            (!this.endDate || operation.date <= this.endDate);

        const amountCondition =
            (!this.minAmount || operation.amount >= this.minAmount) &&
            (!this.maxAmount || operation.amount <= this.maxAmount);

        const searchCondition =
            !this.searchQuery ||
            (operation.date && operation.date.includes(this.searchQuery)) ||
            (operation.type && operation.type.value.includes(this.searchQuery));

        return dateCondition && amountCondition && searchCondition;
      });
    },
  },
  methods: {
   // applyFilters() {
      // Реализация применения фильтров
   // },

    async fetchOperations() {
      this.operations = await api.operations.getAll()
      console.log('operations are')
      console.log(this.operations)
    },
  },
  async mounted() {
    await this.fetchOperations();
  },
}
;
</script>

<style>

body {
  margin: 0;
  padding: 0;
  background: #dfdfdf;
}

.search-column-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding-top: 2px;
}

.button-container, .search-row-container {
  display: flex;
  flex-direction: row;
  padding-top: 3px;
  margin-top: 1px;
}

.filters {
  color: rgb(0, 0, 11);
  background: #9A9DAB;
  margin: 10px 0px 10px;
  border-radius: 2px;
  padding: 5px;
}

.btn {
  margin-top: 5px;
  display: inline-block;
  width: 100%;
  text-align: center;

}

.input {
  border-radius: 4px;
  border-style: none;
  background: #eaeaea;
}

.expenses {
  margin-left: 10px;
}

.date-left {
  margin-right: 10px;

}

input,
input::placeholder {
  font-size: 14px;
  margin: 5px 5px;
}

tr, th {
  font-size: 12px;
}

.left-column {
  padding: 10px;
}

.right-column {
  padding: 10px;
  width: 100%;
}

</style>