<script setup lang="ts">
  import { computed, onMounted, ref } from 'vue';
  import { buscarTarefas, deleteUsuario, alternarDificuldade, cadastrarTarefa, buscarPorId } from '@/services/tarefasService';
  import { type User, type Tarefa, type CadastrarTarefaDTO, type LevelDificuldade } from '@/interfaces/tarefamodel';
  import { buscarUsuarios } from '@/services/userService';

  const tarefas = ref<Tarefa[]>([]);
  const descricao = ref('');
  const usuarios = ref<User[]>([]);
  const usersId = ref<string | null>(null);
  const loading = ref(false);

  const selectedLevel = ref<LevelDificuldade>('MEDIUM')
  const levels: LevelDificuldade[] = ['LOW', 'MEDIUM', 'HIGH']
  
  const currentTab = ref('All')
  

  const totalCompleted = computed(() => {
    return 0;
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
  async function handleDelete(id: string){
    if(confirm('Deseja realmente excluir esta tarefa?')){
      try{
        await deleteUsuario(id);
        await loadList();
      } catch (error) {
        console.error(error);
        alert('Erro ao excluir tarefa');
      }
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

      <div class="priority-selectots">

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

      <div class="list-tarefas">
        <table>
          <thead>
            <tr>
              <th>Descrição</th>
              <th>Dificuldade</th>
              <th>Pessoa</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="tarefa in tarefas" :key="tarefa.id">
              <td>{{ tarefa.descricao }}</td>
              <td> 
                <select :value="tarefa.level" 
                @change="e => tarefa.id && handleAlternarDificuldade(tarefa.id, e)" 
                class="select-dificuldade">
                
                <option 
                v-for="nivel in levels" :key="nivel"
                :value="nivel">{{ nivel }}</option>
              </select>
              
              </td>
              <td>{{ tarefa.user?.name || "Usuario desconhecido"}}</td>
              <td>
                <button type="button" @click="tarefa.id && handleDelete(tarefa.id)">Excluir</button>
              </td>
            </tr>
            <tr v-if="tarefas.length === 0 && !loading">
              <td colspan="4">Nenhuma tarefa cadastrada</td>
            </tr>
            <tr v-if="loading">
              <td colspan="4">Carregando...</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
 </div>
</template>

<style scoped>
* {
  box-sizing: border-box;
}

.app-container {
  min-height: 100vh;
  width: 100%;
  padding: 60px 20px;
  background: #f5f7fb;

  display: flex;
  flex-direction: column;
  align-items: center;
}


h1 {
  margin: 0 0 35px;
  font-size: 42px;
  font-weight: 700;
  color: #1e293b;
  font-family: 'Courier New', Courier, monospace;
  letter-spacing: -1px;
}

.formulario {
  width: 100%;
  max-width: 900px;
  background: white;
  padding: 30px;
  border-radius: 16px;

  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
}

label {
  display: inline-block;
  margin-right: 8px;

  font-size: 15px;
  font-weight: 600;
  color: #475569;
}

input,
select {
  height: 40px;
  padding: 0 12px;

  border: 1px solid #cbd5e1;
  border-radius: 8px;

  background: white;
  color: #334155;

  font-size: 14px;

  outline: none;
  transition: all 0.2s ease;
}

input {
  width: 300px;
}

input:focus,
select:focus {
  border-color: #6366f1;
  box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.15);
}

#users {
  width: 220px;
}


button {
  height: 40px;
  padding: 0 18px;

  border: none;
  border-radius: 8px;

  background: #6366f1;
  color: white;

  font-size: 14px;
  font-weight: 600;

  cursor: pointer;

  transition: all 0.2s ease;
}

button:hover {
  background: #4f46e5;
  transform: translateY(-1px);
}

button:active {
  transform: translateY(0);
}

.list-tarefas {
  margin-top: 35px;
  width: 100%;
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;

  overflow: hidden;

  border: 1px solid #e2e8f0;
  border-radius: 12px;

  background: white;
}

thead {
  background: #f8fafc;
}

th {
  padding: 15px 18px;

  color: #475569;
  font-size: 14px;
  font-weight: 700;

  text-align: left;

  border-bottom: 1px solid #e2e8f0;
}

td {
  padding: 14px 18px;

  color: #334155;
  font-size: 14px;

  border-bottom: 1px solid #f1f5f9;
}

tbody tr:last-child td {
  border-bottom: none;
}

tbody tr {
  transition: background 0.2s ease;
}

tbody tr:hover {
  background: #f8fafc;
}

th:nth-child(2),
td:nth-child(2),
th:nth-child(3),
td:nth-child(3),
th:nth-child(4),
td:nth-child(4) {
  text-align: center;
}

.select-dificuldade {
  min-width: 120px;
  height: 36px;

  cursor: pointer;
}

td button {
  background: #ef4444;
}

td button:hover {
  background: #dc2626;
}

@media (max-width: 700px) {
  .container {
    padding: 30px 15px;
  }

  h1 {
    font-size: 32px;
  }

  .formulario {
    padding: 20px;
  }

  input {
    width: 100%;
    margin-bottom: 10px;
  }

  #users {
    width: 100%;
    margin-bottom: 10px;
  }

  .formulario > button {
    width: 100%;
  }

  table {
    min-width: 650px;
  }
}
</style>
