# Miphi Finance App  
**Персональный финансовый менеджер с аналитикой**

[![Java](https://img.shields.io/badge/Java-23-%23ED8B00?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.6-%236DB33F?logo=spring)](https://spring.io/)
[![Vue](https://img.shields.io/badge/Vue-3-%234FC08D?logo=vuedotjs)](https://vuejs.org/)

---

### 🚀 Возможности
- Учет доходов/расходов с категоризацией
- Визуализация финансовой аналитики (графики, отчеты)
- Мультивалютная поддержка
- Экспорт данных в PDF (JasperReports)
- JWT-аутентификация и ролевая модель
- Уведомления по email

---

### 🛠 Технологии

**Backend (Java):**
- **Spring Boot 3.2.6** (Web, Data JPA, Security, Mail)
- **PostgreSQL** + Flyway (миграции)
- **Java 17/21** (версия требует уточнения*)
- **Lombok & MapStruct** (генерация кода)
- **JWT Auth** (jjwt 0.12.6)
- **JasperReports 6.20** (PDF-отчеты)

**Frontend (JavaScript):**
- **Vue 3** (Composition API)
- **Vue Router 4** + **Vuex 4**
- **Axios** (HTTP-клиент)
- **Vue CLI 5** (сборка)

**Инфраструктура:**
- **Maven** (сборка Java)
- **SMTP (Gmail)** для рассылки

---

### ⚙️ Установка

1. **Требования:**
   - Java 21/23
   - Node.js 18+
   - PostgreSQL 15

2. **Настройка БД:**
   ```sql
   CREATE DATABASE pfma;
   CREATE USER postgres WITH PASSWORD 'postgres';
   GRANT ALL PRIVILEGES ON DATABASE pfma TO postgres;
   ```

3. **Запуск бэкенда:**
   ```bash
   cd backend
   mvn spring-boot:run
   ```

4. **Запуск фронтенда:**
   ```bash
   cd frontend
   npm install
   npm run serve
   ```

---

### 🔧 Конфигурация

**Бэкенд (`application.properties`):**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pfma
spring.datasource.username=postgres
spring.datasource.password=postgres

# Настройки JWT
jwt.secret.access=your_secure_key
jwt.secret.refresh=your_secure_key_2

# Настройки почты (Gmail)
spring.mail.username=your-email@gmail.com
spring.mail.password=app-password
```

**Фронтенд (`.env`):**
```ini
VUE_APP_API_URL=http://localhost:8080/api/v1
```

---

### 📄 Документация

- **API Endpoints:** Доступны после запуска по адресу `http://localhost:8080/swagger-ui.html`
- **Генерация отчетов:** Примеры шаблонов в `/backend/src/main/resources/jasper`
---