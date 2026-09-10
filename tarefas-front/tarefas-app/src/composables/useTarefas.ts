import { ref, computed, onMounted } from 'vue';
import type { Tarefa, User, LevelDificuldade, CadastrarTarefaDTO, AtualizarTarefaDTO } from '@/interfaces/tarefamodel';
import { buscarTarefas, cadastrarTarefa, atualizarTarefa, deleteTarefa, alternarDificuldade } from '@/services/tarefasService';
import { buscarUsuarios } from '@/services/userService';

export function useTarefas() {
  const tarefas = ref<Tarefa[]>([]);
  const usuarios = ref<User[]>([]);
  const loading = ref(false);
  const currentTab = ref<'All' | 'Active' | 'Done'>('All');

  const totalCompleted = computed(() => tarefas.value.filter(t => t.status === 'CONCLUIDA').length);
  const progressPercentage = computed(() => tarefas.value.length === 0 ? 0 : (totalCompleted.value / tarefas.value.length) * 100);

  const filteredTarefas = computed(() => {
    if (currentTab.value === 'Active') return tarefas.value.filter(t => t.status !== 'CONCLUIDA');
    if (currentTab.value === 'Done') return tarefas.value.filter(t => t.status === 'CONCLUIDA');
    return tarefas.value;
  });

  async function loadList() {
    loading.value = true;
    try {
      const [dadosTarefas, dadosUsers] = await Promise.all([buscarTarefas(), buscarUsuarios()]);
      tarefas.value = dadosTarefas;
      usuarios.value = dadosUsers;
    } finally {
      loading.value = false;
    }
  }

  async function handleToggleDone(tarefa: Tarefa) {
    if (!tarefa.id) return;
    const novoStatus = tarefa.status === 'CONCLUIDA' ? 'PENDENTE' : 'CONCLUIDA';
    try {
      await atualizarTarefa(tarefa.id, { descricao: tarefa.descricao });
      tarefa.status = novoStatus;
    } catch (error) {
      console.error(error);
      alert('Erro ao alterar status');
    }
  }

  async function handleCreate(dto: CadastrarTarefaDTO) {
    await cadastrarTarefa(dto);
    await loadList();
  }

  async function handleUpdate(tarefa: Tarefa, descricao: string) {
    if (!tarefa.id) return;
    await atualizarTarefa(tarefa.id, { descricao } satisfies AtualizarTarefaDTO);
    await loadList();
  }

  async function handleDelete(id: string) {
    if (confirm('Excluir tarefa?')) {
      try {
        await deleteTarefa(id);
        await loadList();
      } catch (error) {
        console.error(error);
        alert('Erro ao excluir tarefa');
      }
    }
  }

  async function handleClearDone() {
    try {
      await Promise.all(
        tarefas.value
          .filter(tarefa => tarefa.status === 'CONCLUIDA' && tarefa.id)
          .map(tarefa => deleteTarefa(tarefa.id!))
      );
      await loadList();
    } catch (error) {
      console.error(error);
      alert('Erro ao limpar tarefas concluídas');
    }
  }

  async function handleChangeLevel(id: string, level: LevelDificuldade) {
    try {
      await alternarDificuldade(id, level);
      await loadList();
    } catch (error) {
      console.error(error);
      alert('Erro ao alternar dificuldade da tarefa');
    }
  }

  onMounted(loadList);

  return {
    tarefas,
    usuarios,
    loading,
    currentTab,
    totalCompleted,
    progressPercentage,
    filteredTarefas,
    loadList,
    handleCreate,
    handleUpdate,
    handleToggleDone,
    handleDelete,
    handleClearDone,
    handleChangeLevel
  };
}