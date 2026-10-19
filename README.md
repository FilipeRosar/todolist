# 🎯 Focus - Task Management App

Uma aplicação web completa para gerenciamento de tarefas e produtividade, desenvolvida como projeto acadêmico. O sistema permite criar, priorizar, editar e filtrar tarefas atribuídas a usuários em uma interface moderna e responsiva.

---

## 🚀 Tecnologias Utilizadas

### **Frontend**
- **Vue 3** (Composition API + TypeScript)
- **Vite**
- **CSS3** (Scoped styling & Flexbox)
- **Axios** (Integração de API)

### **Backend**
- **Java** / **Spring Boot**
- **Spring Data JPA / Hibernate**
- **PostgreSQL**
- **Maven**

---

## ✨ Funcionalidades

- **Gerenciamento de Tarefas:** Criação, edição *inline*, alteração de status e remoção de tarefas.
- **Níveis de Prioridade:** Atribuição e alternância dinâmica de prioridades (`LOW`, `MEDIUM`, `HIGH`).
- **Atribuição de Usuários:** Associação de tarefas a usuários cadastrados no sistema.
- **Filtros Dinâmicos:** Visualização por abas (`All`, `Active`, `Done`).
- **Métricas de Progresso:** Barra de progresso dinâmica baseada na porcentagem de tarefas concluídas.
- **Limpeza em Lote:** Botão *Clear Done* para exclusão rápida de tarefas finalizadas.
- **Interface Dark Mode:** UI inspirada em design sistemas modernos com efeitos ao passar o mouse (*hover actions*).

---

## 🛠️ Como Executar o Projeto

### **Pré-requisitos**
- [Node.js](https://nodejs.org/) (v18 ou superior)
- [JDK 17](https://www.oracle.com/java/technologies/downloads/) ou superior
- [PostgreSQL](https://www.postgresql.org/) configurado e rodando

---

### **1. Configuração do Backend (Spring Boot)**

```bash
# Clone o repositório
git clone https://github.com/FilipeRosar/todolist.git

# Acesse a pasta do backend
cd seu-repositorio/backend

# Configure o banco de dados no arquivo src/main/resources/application.properties:
# spring.datasource.url=jdbc:postgresql://localhost:5432/nome_do_banco
# spring.datasource.username=seu_usuario
# spring.datasource.password=sua_senha

# Execute a aplicação
./mvnw spring-boot:run

# Acesse a pasta do frontend
cd ../frontend

# Instale as dependências
npm install

# Execute o servidor de desenvolvimento
npm run dev


🎓 Contexto Acadêmico

Este projeto foi desenvolvido como requisito de avaliação para o curso de  Desenvolvimento de Sistemas, demonstrando a aplicação prática de:

    Arquitetura Restful e Clean Code.

    Integração Fullstack entre Spring Boot e Vue 3.

    Manipulação de estado e reatividade no Frontend.

✒️ Autor

Desenvolvido por Filipe Rosa

    GitHub: @FilipeRosar

    LinkedIn: https://www.linkedin.com/in/filipe-rosar/
