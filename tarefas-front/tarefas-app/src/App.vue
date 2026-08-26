<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { type Tarefa } from '@/interfaces/tarefamodel';

import { buscarTarefas,cadastrarTarefa, deleteUsuario,buscarPorId } from './services/tarefasService';


</script>

<template>

 <div class="container">
    <h1>Nova Tarefa</h1>
    <div class="formulario">
      <label for="descricao">
        Descrição:
      </label>
      <input 
        type="text"
        value="" 
        id="descricao" 
        placeholder="Entre com a descrição">

      <br>
      <label for="pessoa">
        Pessoa:
      </label>

      <select name="pessoa" id="pessoa">
        
      </select>
      <button type="button">Ok</button>
    </div>
 </div>
</template>

<style scoped></style>
