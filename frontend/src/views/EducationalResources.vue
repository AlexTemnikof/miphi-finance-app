<template>
  <div class="educational-resources">
    <h2>Образовательные ресурсы по финансовой грамотности</h2>
    <resource-card v-for="resource in resources" :key="resource.id" :resource="resource" />
  </div>
</template>

<script>
import ResourceCard from "@/components/ResourceCard.vue";

export default {
  components: {
    ResourceCard,
  },
  data() {
    return {
      resources: [
        {
          id: 1,
          title: "Финансовая грамотность: что такое и для чего нужна",
          description: "Чтобы жить в достатке, недостаточно достойно зарабатывать. Необходимо научиться тратить деньги. В нашей стране немногие размышляют об этом, но основы финансовой грамотности доступны и выгодны всем. Эти правила помогают, например, быстрее купить собственную квартиру, иметь прочие материальные блага и не набрать при этом долгов наличными или по кредитной карте.",
          link: "http://localhost:8080/educational-resources/1",
        },
        {
          id: 2,
          title: "Для чего нужна финансовая грамотность",
          description: "Умение правильно распределять средства является базой для жизненной стабильности и благополучия",
          link: "http://localhost:8080/educational-resources/2",
        },
        {
          id: 3,
          title: "Как оценить свою финансовую грамотность?",
          description: "Небольшой список пунктов для того, чтобы оценить свою финансовую грамотность",
          link: "http://localhost:8080/educational-resources/3",
        },
      ],
    };
  },
};
</script>
<style scoped>
.educational-resources h2 {
  padding: 10px;
}

</style>