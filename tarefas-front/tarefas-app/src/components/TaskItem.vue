<script setup lang="ts">
import { ref, nextTick } from 'vue';
import type { Tarefa, LevelDificuldade } from '@/interfaces/tarefamodel';

const props = defineProps<{ tarefa: Tarefa; levels: LevelDificuldade[] }>();
const emit = defineEmits<{
  'toggle-done': [tarefa: Tarefa];
  delete: [id: string | undefined];
  update: [tarefa: Tarefa, descricao: string];
  'change-level': [id: string, level: LevelDificuldade];
}>();

const isEditing = ref(false);
const editingText = ref('');
const editInputRef = ref<HTMLInputElement | null>(null);

async function startEdit() {
  if (!props.tarefa.id) return;
  isEditing.value = true;
  editingText.value = props.tarefa.descricao;
  await nextTick();
  editInputRef.value?.focus();
}

function cancelEdit() {
  isEditing.value = false;
  editingText.value = '';
}

function saveEdit() {
  if (editingText.value.trim() && editingText.value !== props.tarefa.descricao) {
    emit('update', props.tarefa, editingText.value);
  }
  cancelEdit();
}

function changeLevel(event: Event) {
  if (props.tarefa.id) {
    emit('change-level', props.tarefa.id, (event.target as HTMLSelectElement).value as LevelDificuldade);
  }
}
</script>

<template>
  <div class="task-item" :class="{ completed: tarefa.status === 'CONCLUIDA' }">
    <div class="task-left">
      <button class="circle-checkbox" :class="{ checked: tarefa.status === 'CONCLUIDA' }" @click="emit('toggle-done', tarefa)">
        <svg v-if="tarefa.status === 'CONCLUIDA'" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3">
          <polyline points="20 6 9 17 4 12"></polyline>
        </svg>
      </button>

      <div class="task-info">
        <input v-if="isEditing" ref="editInputRef" v-model="editingText" class="edit-input" @keyup.enter="saveEdit" @keyup.esc="cancelEdit" @blur="saveEdit" />
        <span v-else class="task-title" @dblclick="startEdit">{{ tarefa.descricao }}</span>
        <span class="task-user" v-if="tarefa.user">@{{ tarefa.user.name }}</span>
      </div>
    </div>

    <div class="task-right">
      <div class="priority-indicator-wrapper">
        <select :value="tarefa.level" class="hidden-select" @change="changeLevel">
          <option v-for="level in levels" :key="level" :value="level">{{ level }}</option>
        </select>
        <span class="dot" :class="{
          'bg-blue': tarefa.level === 'LOW',
          'bg-orange': tarefa.level === 'MEDIUM',
          'bg-red': tarefa.level === 'HIGH'
        }"></span>
      </div>
      <button class="btn-edit" title="Editar" @click="startEdit">✎</button>
      <button class="btn-delete-hover" title="Remover" @click="emit('delete', tarefa.id)">✕</button>
    </div>
  </div>
</template>