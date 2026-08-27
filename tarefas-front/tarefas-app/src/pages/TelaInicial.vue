<script setup lang="ts">
  import { onMounted, ref } from 'vue';
  import { buscarTarefas, deleteUsuario, alternarDificuldade, cadastrarTarefa, buscarPorId } from '@/services/tarefasService';
  import { type User, type Tarefa, type CadastrarTarefaDTO } from '@/interfaces/tarefamodel';
  import { buscarUsuarios } from '@/services/userService';

  const tarefas = ref<Tarefa[]>([]);
  const descricao = ref('');
  const usuarios = ref<User[]>([]);
  const usersId = ref<string | null>(null);
  const loading = ref(false);

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
      await cadastrarTarefa({descricao: descricao.value, userId: usersId.value} as CadastrarTarefaDTO);
      descricao.value = '';
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
  async function handleAlternarDificuldade(id: string){
    try{
      await alternarDificuldade(id);
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

 <div class="container">

    <h1>TODO LIST</h1>
    <div class="formulario">
      <label for="descricao">
        Descrição:
      </label>
      <input 
        type="text"
        v-model="descricao"
        id="descricao" 
        placeholder="Entre com a descrição">

      <br>
      <label for="pessoa">Pessoa: </label>

      <select name="pessoa" id="users" v-model="usersId">
        <option value="">Selecione uma pessoa</option>
        <option v-for="user in usuarios" :key="user.id" :value="user.id">
          {{ user.name }}
        </option>
      </select>
      <button type="button" @click="handleCadastrar">Ok</button>
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
                <button type="button" @click="tarefa.id && handleAlternarDificuldade(tarefa.id)">{{ tarefa.dificuldade || 'LOW' }}</button>
              </td>
              <td>{{ tarefa.user?.name || 'Nenhuma pessoa atribuída' }}</td>
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
  .container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    min-height: 100vh;
  }
  
  table {
    margin-top: 20px;
    border-collapse: collapse;
    width: 100%;
  }

  th, td {
    border: 1px solid #ccc;
    padding: 8px;
    text-align: left;
  }
</style>
