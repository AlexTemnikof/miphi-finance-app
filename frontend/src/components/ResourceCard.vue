<template>
  <div class="resource-card">
    <h3>{{ resource.title }}</h3>
    <p>{{ resource.description }}</p>
    <a :href="resource.link" target="_blank">Перейти к ресурсу</a>
  </div>
</template>

<script>
export default {
  props: {
    resource: {
      type: Object,
      required: true,
    },
  },
};
</script>

<style scoped>
.resource-card {
  border: 1px solid #ccc;
  padding: 15px;
  margin: 10px;
}
</style>
