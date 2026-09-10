<script setup lang="ts">
import { ref } from 'vue';
import TaskItem from '@/components/TaskItem.vue';
import { useTarefas } from '@/composables/useTarefas';
import type { CadastrarTarefaDTO, LevelDificuldade, Tarefa } from '@/interfaces/tarefamodel';

const {
  tarefas,
  usuarios,
  loading,
  currentTab,
  totalCompleted,
  progressPercentage,
  filteredTarefas,
  handleCreate,
  handleUpdate,
  handleToggleDone,
  handleDelete,
  handleClearDone,
  handleChangeLevel
} = useTarefas();

const descricao = ref('');
const usersId = ref<string | null>(null);
const selectedLevel = ref<LevelDificuldade>('MEDIUM');
const levels: LevelDificuldade[] = ['LOW', 'MEDIUM', 'HIGH'];

async function handleCadastrar() {
  if (!descricao.value.trim() || !usersId.value) {
    alert('Preencha todos os campos');
    return;
  }

  try {
    const payload: CadastrarTarefaDTO = {
      descricao: descricao.value,
      userId: usersId.value,
      levelDificuldade: selectedLevel.value
    };

    await handleCreate(payload);
    descricao.value = '';
    usersId.value = null;
    selectedLevel.value = 'MEDIUM';
  } catch (error) {
    console.error(error);
    alert('Erro ao cadastrar tarefa');
  }
}
</script>

<template>
  <main class="app-container">
    <header>
      <div class="focus-badge"><span class="diamond"></span> FOCUS</div>
      <h1>My Tasks</h1>
      <p class="subtitle">{{ totalCompleted }} of {{ tarefas.length }} completed</p>
      <div class="progress-bar-container">
        <div class="progress-bar-fill" :style="{ width: `${progressPercentage}%` }"></div>
      </div>
    </header>

    <section class="new-task-card">
      <div class="input-wrapper">
        <input
          v-model="descricao"
          type="text"
          placeholder="Add a new task..."
          @keyup.enter="handleCadastrar"
        >
        <select v-model="usersId" class="user-select" title="Atribuir usuário">
          <option value="" disabled>Selecione uma pessoa</option>
          <option v-for="user in usuarios" :key="user.id" :value="user.id">
            {{ user.name.split(' ')[0] }}
          </option>
        </select>
        <button type="button" class="btn-add" @click="handleCadastrar">+</button>
      </div>

      <div class="priority-selectors">
        <button
          v-for="level in levels"
          :key="level"
          type="button"
          class="priority-btn"
          :class="{ active: selectedLevel === level }"
          @click="selectedLevel = level"
        >
          <span class="dot" :class="`bg-${level === 'LOW' ? 'blue' : level === 'MEDIUM' ? 'orange' : 'red'}`"></span>
          {{ level === 'LOW' ? 'Low' : level === 'MEDIUM' ? 'Medium' : 'High' }}
        </button>
      </div>

      <nav class="tabs" aria-label="Filtro de tarefas">
        <button
          v-for="tab in ['All', 'Active', 'Done'] as const"
          :key="tab"
          class="tab"
          :class="{ active: currentTab === tab }"
          @click="currentTab = tab"
        >
          {{ tab }}
        </button>
      </nav>

      <button v-if="totalCompleted > 0" class="btn-clear-done" @click="handleClearDone">
        Clear Done
      </button>

      <div class="task-list">
        <div v-if="loading" class="empty-state">Loading tasks...</div>
        <div v-else-if="filteredTarefas.length === 0" class="empty-state">
          {{ currentTab === 'Active' ? 'Nenhuma tarefa ativa' : currentTab === 'Done' ? 'Nenhuma tarefa concluída' : 'Nenhuma tarefa cadastrada' }}
        </div>
        <TaskItem
          v-for="tarefa in filteredTarefas"
          :key="tarefa.id"
          :tarefa="tarefa"
          :levels="levels"
          @toggle-done="handleToggleDone"
          @delete="id => id && handleDelete(id)"
          @update="(task: Tarefa, text: string) => handleUpdate(task, text)"
          @change-level="handleChangeLevel"
        />
      </div>
    </section>

    <footer class="footer-hint">Drag and drop to reorder list</footer>
  </main>
</template>
