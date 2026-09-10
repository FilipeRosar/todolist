<script setup lang="ts">
  import { computed, onMounted, ref, nextTick } from 'vue';
  import { buscarTarefas, deleteTarefa, alternarDificuldade, cadastrarTarefa, atualizarTarefa } from '@/services/tarefasService';
  import { type User, type Tarefa, type CadastrarTarefaDTO, type LevelDificuldade, type AtualizarTarefaDTO } from '@/interfaces/tarefamodel';
  import { buscarUsuarios } from '@/services/userService';

  const tarefas = ref<Tarefa[]>([]);
  const descricao = ref('');
  const usuarios = ref<User[]>([]);
  const usersId = ref<string | null>(null);
  const loading = ref(false);
  const editingTaskId = ref<string | null>(null);
  const editingText = ref('');
  const editInputRef = ref<HTMLInputElement | null>(null);


  const selectedLevel = ref<LevelDificuldade>('MEDIUM')
  const levels: LevelDificuldade[] = ['LOW', 'MEDIUM', 'HIGH']
  
  const currentTab = ref<'All' | 'Active' | 'Done'>('All');
  

  const totalCompleted = computed(() => {
    return tarefas.value.filter(t => t.status === 'CONCLUIDA').length;
  })
  const progressPercentage = computed(() => {
    if(tarefas.value.length === 0) return 0;
    return (totalCompleted.value / tarefas.value.length) * 100;
  })

  const filteredTarefas = computed(() => {
    if (currentTab.value === 'All'){
      return tarefas.value;
    } else if (currentTab.value === 'Active'){
      return tarefas.value.filter(t => t.status !== 'CONCLUIDA');
    } else {
      return tarefas.value.filter(t => t.status === 'CONCLUIDA');
    }
  })

  async function loadList() {
    loading.value = true;
    try{
      const [ dadosTarefas, dadosUsers ] = await Promise.all([
        buscarTarefas(),
        buscarUsuarios()
      ])
      tarefas.value = dadosTarefas;
      usuarios.value = dadosUsers;

    } finally {
      loading.value = false;
    }
  }
  async function handleUpdateTarefa(tarefa: Tarefa){
    if(!editingText.value.trim()){
      alert('Descrição não pode ser vazia');
      return;
    }
    try{
      const payload: AtualizarTarefaDTO = {
        descricao: editingText.value
      };
      await atualizarTarefa(tarefa.id!, payload);
      editingTaskId.value = null;
      editingText.value = '';
      await loadList();
    } catch (error) {
      console.error(error);
      alert('Erro ao atualizar tarefa');
    }
  }
  async function handleStartEdit(tarefa: Tarefa){
    if (!tarefa.id) return;
    editingTaskId.value = tarefa.id;
    editingText.value = tarefa.descricao;
    await nextTick();
    editInputRef.value?.focus();
  }

  function handleCancelEdit(){
    editingTaskId.value = null;
    editingText.value = '';
  }
  async function handleSaveEdit(tarefa: Tarefa){
    if (!editingText.value.trim() || !tarefa.id) return;
    await handleUpdateTarefa(tarefa);
  }
  async function handleCadastrar(){
    if(!descricao.value.trim() || !usersId.value){
      alert('Preencha todos os campos');
      return;
    }
    try{
      const payload: CadastrarTarefaDTO = {
        descricao: descricao.value,
        userId: usersId.value,
        levelDificuldade: selectedLevel.value
      };
      await cadastrarTarefa(payload);

      descricao.value = '';
      usersId.value = '';
      selectedLevel.value = 'MEDIUM';

      await loadList();
    } catch (error) {
      console.error(error);
      alert('Erro ao cadastrar tarefa');
    }
  }
  function handleToggleDone(tarefa: Tarefa){
   tarefa.status = tarefa.status === 'CONCLUIDA' ? 'PENDENTE' : 'CONCLUIDA';
  }
  async function handleDelete(id: string){
    if(confirm('Deseja realmente excluir esta tarefa?')){
      try{
        await deleteTarefa(id);
        await loadList();
      } catch (error) {
        console.error(error);
        alert('Erro ao excluir tarefa');
      }
    }
  }
  async function handleClearDone(){
    const doneTasks = tarefas.value.filter(t => t.status === 'CONCLUIDA');

    try{
      await Promise.all(doneTasks.map(t => t.id && deleteTarefa(t.id)));
      await loadList();
    } catch (error) {
      console.error(error);
      alert('Erro ao limpar tarefas concluídas');
    }

  }
  async function handleAlternarDificuldade(id: string, event: Event){
    const target = event.target as HTMLSelectElement;

    const novoNivel = target.value as LevelDificuldade;

    try{
      await alternarDificuldade(id, novoNivel);
      await loadList();
    } catch (error) {
      console.error(error);
      alert('Erro ao alternar dificuldade da tarefa');
    }
  }
  onMounted(() => {
    loadList();
  })

</script>

<template>

 <div class="app-container">

  <header>
    <div class="focus-badge">
      <span class="diamond">
      </span> FOCUS
    </div>
    <h1>My Tasks</h1>
    <p class="subtitle">{{ totalCompleted }} of {{ tarefas.length }} completed</p>
    <div class="progress-bar-container">
      <div class="progress-bar-fill" :style="{ width: `${progressPercentage}%` }"></div>
    </div>
  </header>

    <div class="new-task-card">
      <div class="input-wrapper">
        <input 
          type="text"
          v-model="descricao"
          id="descricao" 
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

        <button type="button" 
          class="priority-btn"
          :class="{ active: selectedLevel === 'LOW' }"
          @click="selectedLevel = 'LOW'">
        <span class="dot bg-blue"></span> Low
      </button>

      <button type="button" 
          class="priority-btn"
          :class="{ active: selectedLevel === 'MEDIUM' }"
          @click="selectedLevel = 'MEDIUM'">
          <span class="dot bg-orange"></span> Medium
        </button>

        <button type="button" 
          class="priority-btn"
          :class="{ active: selectedLevel === 'HIGH' }"
          @click="selectedLevel = 'HIGH'">
          <span class="dot bg-red"></span> High
        </button>
      </div>

      <div class="tabs">
        <button class="tab" :class="{ active: currentTab === 'All' }" @click="currentTab = 'All'" >All</button>
        <button class="tab" :class="{ active: currentTab === 'Active'}" @click="currentTab = 'Active'">Active</button>
        <button class="tab" :class="{ active: currentTab === 'Done'}" @click="currentTab = 'Done'">Done</button>
      </div>

      <button class="btn-clear-done" v-if="totalCompleted > 0" @click="handleClearDone">Clear Done</button>


      <div class="task-list">
       <div v-if="loading" class="empty-state">Loading tasks...</div>
       <div v-else-if="filteredTarefas.length === 0" class="empty-state">
         {{ currentTab === 'Active' ? 'Nenhuma tarefa ativa' : currentTab === 'Done' ? 'Nenhuma tarefa concluída' : 'Nenhuma tarefa cadastrada' }}
       </div>

       <div v-for="tarefa in filteredTarefas"
        :key="tarefa.id"
        class="task-item"
        :class="{ completed: tarefa.status === 'CONCLUIDA' }">

        <div class="task-left">
          <button class="circle-checkbox"
          :class="{ checked: tarefa.status === 'CONCLUIDA' }"
          @click="handleToggleDone(tarefa)">
          <svg v-if="tarefa.status === 'CONCLUIDA'" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round">
              <polyline points="20 6 9 17 4 12"></polyline>
            </svg>
        </button>
          <div class="task-info">
          <input 
            v-if="editingTaskId === tarefa.id"
            ref="editInputRef"
            v-model="editingText"
            class="edit-input"
            @keyup.enter="handleSaveEdit(tarefa)"
            @keyup.esc="handleCancelEdit"
            @blur="handleSaveEdit(tarefa)"
          />

            <span 
              v-else 
              class="task-title"
              @dblclick="handleStartEdit(tarefa)"
            >
              {{ tarefa.descricao }}
            </span>

            <span class="task-user" v-if="tarefa.user">@{{ tarefa.user.name }}</span>
          </div>
        </div>
        
                <div class="task-right">
                  <div class="priority-indicator-wrapper">
                    <select
                      :value="tarefa.level"
                      @change="e => tarefa.id && handleAlternarDificuldade(tarefa.id, e)"
                      class="hidden-select"
                    >
                    <option v-for="nivel in levels"
                    :key="nivel" :value="nivel">{{ nivel }}</option>
                  </select>
                  <span class="dot" :class="{
                    'bg-blue': tarefa.level === 'LOW',
                    'bg-orange': tarefa.level === 'MEDIUM',
                    'bg-red': tarefa.level === 'HIGH'
                  }"></span>
                  </div>
                  <button 
                    class="btn-edit" 
                    title="Editar tarefa" 
                    @click="handleStartEdit(tarefa)"
                  >
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <path d="M12 20h9"></path>
                      <path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z"></path>
                    </svg>
                  </button>
                  <button class="btn-delete-hover" title="Remover tarefa" @click="tarefa.id && handleDelete(tarefa.id)">X</button>
                </div>
        </div>
       </div>
      </div>
      <footer class="footer-hint">
        Drag and drop to reorder list
      </footer>
    </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap');

* {
  box-sizing: border-box;
  font-family: 'Inter', sans-serif;
}

.app-container {
  min-height: 100vh;
  width: 100%;
  background-color: #121212;
  color: #ffffff;
  padding: 40px 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

header {
  width: 100%;
  max-width: 480px;
  margin-bottom: 24px;
}

.focus-badge {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  letter-spacing: 1px;
  color: #a1a1aa;
  text-transform: uppercase;
  margin-bottom: 12px;
}

.diamond {
  width: 8px;
  height: 8px;
  background-color: #f59e0b;
  transform: rotate(45deg);
}

h1 {
  font-size: 32px;
  font-weight: 500;
  margin: 0 0 8px 0;
  letter-spacing: -0.5px;
}

.subtitle {
  color: #71717a;
  font-size: 14px;
  margin: 0;
}

.progress-bar-container {
  width: 100%;
  height: 3px;
  background-color: #27272a;
  border-radius: 2px;
  margin-top: 14px;
  overflow: hidden;
}

.progress-bar-fill {
  height: 100%;
  background: linear-gradient(90deg, #f59e0b, #10b981);
  transition: width 0.3s ease;
}

.new-task-card {
  width: 100%;
  max-width: 480px;
  background-color: #1e1e1e;
  border: 1px solid #27272a;
  border-radius: 16px;
  padding: 16px;
  margin-bottom: 24px;
}

.input-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.input-wrapper input {
  flex: 1;
  background: transparent;
  border: none;
  color: #fff;
  font-size: 15px;
  outline: none;
}

.input-wrapper input::placeholder {
  color: #52525b;
}

.user-select {
  background: #27272a;
  color: #a1a1aa;
  border: none;
  padding: 6px 10px;
  border-radius: 8px;
  font-size: 12px;
  outline: none;
  cursor: pointer;
}

.btn-add {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #27272a;
  color: #71717a;
  border: none;
  font-size: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-add:hover {
  background: #3f3f46;
  color: #fff;
}

.priority-selectors {
  display: flex;
  gap: 12px;
}

.priority-btn {
  background: transparent;
  border: 1px solid transparent;
  color: #a1a1aa;
  font-size: 13px;
  padding: 6px 12px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  transition: all 0.2s;
}

.priority-btn.active {
  background: #27272a;
  color: #fff;
  border-color: #3f3f46;
}

.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  display: inline-block;
}
.bg-blue { background-color: #3b82f6; }
.bg-orange { background-color: #f59e0b; }
.bg-red { background-color: #ef4444; }

.tabs-header {
  width: 100%;
  max-width: 480px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.tabs {
  display: flex;
  gap: 4px;
  background: #1e1e1e;
  padding: 4px;
  border-radius: 12px;
  border: 1px solid #27272a;
}

.tab {
  background: transparent;
  border: none;
  color: #71717a;
  padding: 6px 16px;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: 0.2s;
}

.tab.active {
  background: #27272a;
  color: #fff;
}

.btn-clear-done {
  background: transparent;
  border: none;
  color: #71717a;
  font-size: 13px;
  cursor: pointer;
  transition: color 0.2s;
}

.btn-clear-done:hover {
  color: #a1a1aa;
}

.task-list {
  width: 100%;
  max-width: 480px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.task-item {
  background-color: #1e1e1e;
  border: 1px solid #27272a;
  border-radius: 12px;
  padding: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: border-color 0.2s;
}

.task-item:hover {
  border-color: #3f3f46;
}

.task-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.circle-checkbox {
  width: 22px;
  height: 22px;
  border-radius: 50%;
  border: 2px solid #3f3f46;
  background: transparent;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
  padding: 0;
}

.circle-checkbox:hover {
  border-color: #10b981;
}

.circle-checkbox.checked {
  border-color: #10b981;
  color: #10b981;
}

.task-item.completed .task-title {
  text-decoration: line-through;
  color: #52525b;
}

.task-info {
  display: flex;
  flex-direction: column;
}

.task-title {
  font-size: 14px;
  color: #e4e4e7;
  transition: color 0.2s;
}

.task-user {
  font-size: 11px;
  color: #71717a;
  margin-top: 2px;
}

.task-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.priority-indicator-wrapper {
  position: relative;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.hidden-select {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  cursor: pointer;
}

.btn-delete-hover {
  background: transparent;
  border: none;
  color: #52525b;
  font-size: 14px;
  cursor: pointer;
  opacity: 0;
  transition: opacity 0.2s, color 0.2s;
  padding: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.btn-edit {
  background: #2d2318;
  border: none;
  color: #f59e0b;
  width: 28px;
  height: 28px;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0; 
  transition: opacity 0.2s, background-color 0.2s;
  padding: 0;
}

.btn-edit:hover {
  background: #3d2f1f;
  color: #fbbf24;
}

.edit-input {
  background: #27272a;
  border: 1px solid #f59e0b;
  border-radius: 6px;
  color: #ffffff;
  font-size: 14px;
  padding: 2px 8px;
  outline: none;
  width: 100%;
}
.task-item:hover .btn-edit,
.task-item:hover .btn-delete-hover {
  opacity: 1;
}

.btn-delete-hover:hover {
  color: #ef4444;
}

.empty-state {
  text-align: center;
  color: #71717a;
  padding: 20px 0;
  font-size: 14px;
}

.footer-hint {
  margin-top: 32px;
  color: #52525b;
  font-size: 12px;
}
</style>