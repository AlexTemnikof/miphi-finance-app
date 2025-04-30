<template>
  <div>
    <h2>Категории</h2>

    <!-- Таблица с категориями -->

    <table class="table">
      <thead>
      <tr>
        <th>ID</th>
        <th>Название</th>
        <th>Лимит</th>
        <th>Действия</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="category in categories" :key="category.id">
        <td>{{ category.id }}</td>
        <td>
          <template v-if="category.isEditing">
            <input v-model="category.name" placeholder="Введите название категории" />
          </template>
          <template v-else>
            {{ category.name }}
          </template>
        </td>
        <td>
          <template v-if="category.isEditing">
            <input v-model="category.limit" placeholder="Введите лимит" />
          </template>
          <template v-else>
            {{ category.limit }}
          </template>
        </td>
        <td>
          <template v-if="category.isEditing">
            <span @click="saveCategory(category)" style="cursor: pointer;">✔️</span>
            <span @click="cancelEdit(category)" style="cursor: pointer;">✘</span>
          </template>
          <template v-else>
            <span @click="editCategory(category)" style="cursor: pointer;">✎</span>
            <span @click="deleteCategory(category)" style="cursor: pointer;">🗑</span>
          </template>
        </td>
      </tr>
      </tbody>
    </table>

    <!-- Кнопка "Добавить новую категорию" вне таблицы -->
    <div>
      <!-- Если добавление новой категории активно, покажем инпут и кнопку "Добавить" -->
      <template v-if="addingNewCategory">
        <input v-model="newCategoryName" placeholder="Введите название"/>
        <input v-model="categoryLimit" type="number" placeholder="Введите лимит"/>
        <button @click="addNewCategory" class="btn btn-outline-dark">Добавить</button>
      </template>
      <!-- Если добавление новой категории не активно, покажем кнопку "Добавить новую категорию" -->
      <template v-else>
        <button @click="startAddingNewCategory" class="btn btn-outline-dark">Добавить новую категорию</button>
      </template>
    </div>
  </div>
</template>

<script>

//TODO: Auth check

import * as api from '@/api';
import {useRouter} from "vue-router";

export default {
  setup() {
    const router = useRouter();


    const redirectToLogin = async () => {
      console.log('redirecting')
      setTimeout(async () => {
        await router.push('/login');
      }, 1)    };

    return {
      redirectToLogin
    };
  },
  data() {
    return {
      categories: [
      ],
      addingNewCategory: false,
      newCategoryName: "",
      categoryLimit: null,
      getResult: null,
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

    editCategory(category) {
      // Запоминаем оригинальное название перед началом редактирования
      category.originalName = category.name;
      // Запускаем редактирование
      category.isEditing = true;
    },

    async saveCategory(category) {

      try {
        await api.categories.update(category.id, { name: category.name });

        category.isEditing = false;
        category.originalName = category.name;

        await this.fetchData();
      } catch (err) {
        this.getResult = err.message;
      }
    },

    cancelEdit(category) {
      category.isEditing = false;
      category.name = category.originalName;
      this.fetchData()
    },

    async startAddingNewCategory() {
      // Начинаем добавление новой категории
      this.addingNewCategory = true;
    },

    async addNewCategory() {
      const data = {
        name: this.newCategoryName,
        limit: this.categoryLimit
      };

      await api.categories.create(data);

      // Сбрасываем состояние добавления новой категории
      this.addingNewCategory = false;
      this.newCategoryName = "";

      await this.fetchData()

      return {
        data
      }
    },

    async deleteCategory(category) {
      try {
        await api.categories.delete(category.id);

        await this.fetchData();
      } catch (err) {
        this.getResult = err.message;
      }
    },
  },

  async mounted() {
    await this.fetchData(); // Вызываем fetchData при монтировании компонента
  },
};
</script>
