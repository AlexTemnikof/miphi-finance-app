<template>
  <div class="reports-page">
    <h1>Отчеты</h1>

    <div class="form-group">
      <label for="reportType">Тип отчета</label>
      <select v-model="selectedReportType" class="form-select" id="reportType">
        <option v-for="type in reportTypes" :key="type" :value="type.value">{{ type.label }}</option>
      </select>
    </div>

    <div class="form-group">
      <label for="fromDate">От</label>
      <input v-model="fromDate" type="date" class="form-control" id="fromDate" required>
    </div>

    <div class="form-group">
      <label for="toDate">До</label>
      <input v-model="toDate" type="date" class="form-control" id="toDate" required>
    </div>

    <div class="form-group">
      <label for="reportType">Разбиение</label>
      <select v-model="selectedInterval" class="form-select" id="reportType">
        <option v-for="interval in intervals" :key="interval" :value="interval.value">{{ interval.label }}</option>
      </select>
    </div>

    <button class="btn btn-primary" :disabled="!selectedInterval && !selectedReportType && !fromDate && !toDate" @click="downloadReport">Скачать отчет</button>
  </div>
</template>

<script>
import ReportType from "@/data/ReportType";
import Intervals from "@/data/Intervals";
import * as api from "@/api";

export default {
  name: "ReportsPage",
  data() {
    return {
      reportTypes: [
        {value: "TRANSACTIONS_TYPE", label: ReportType.TRANSACTIONS_TYPE.valueOf()},
        {value: "BANKS", label: ReportType.BANKS.valueOf()},
        {value: "PROFIT", label: ReportType.PROFIT.valueOf()},
        {value: "CATEGORIES", label: ReportType.CATEGORIES.valueOf()},
        {value: "TRANSACTIONS_COUNT", label: ReportType.TRANSACTIONS_COUNT.valueOf()},
        {value: "TRANSACTIONS_STATUS", label: ReportType.TRANSACTIONS_STATUS.valueOf()}
      ], // Получаем значения из enum
      intervals: [
        {value: "DAY", label: Intervals.DAY.valueOf()},
        {value: "MONTH", label: Intervals.MONTH.valueOf()},
        {value: "YEAR", label: Intervals.YEAR.valueOf()}
      ],
      selectedReportType: '',
      selectedInterval: '',
      fromDate: '',
      toDate: ''
    }
  },
  methods: {
    async downloadReport() {

      const buildReportDTO = {
        type: this.selectedReportType,
        from: this.fromDate,
        to: this.toDate,
        interval: this.selectedInterval
      }

      console.log("reportdto");
      console.log(buildReportDTO);

      await api.reports.download(buildReportDTO);
    }
  }
}
</script>

<style scoped>
.reports-page {
  margin: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.btn {
  margin-top: 10px;
}
</style>
