import { type AtualizarTarefaDTO, type CadastrarTarefaDTO, type LevelDificuldade, type Tarefa } from "@/interfaces/tarefamodel";
import api from "@/integration/api.js";

const ROTA_BASE = 'api/v1/tarefa'

export async function cadastrarTarefa(dto: CadastrarTarefaDTO): Promise<Tarefa> {
   try{
      const response = await api.post<Tarefa>(`${ROTA_BASE}/cadastrar`, dto);
      return response.data;
   } catch (error){
      console.error("Erro ao cadastrar tareffa: ", error)
      throw error;
   }
}


export async function buscarTarefas(): Promise<Tarefa[]>{
   try{
        const response = await api.get<Tarefa[]>(ROTA_BASE);
        return response.data;
   } catch(error){
    console.error("Erro ao buscar tarefas: ",error);
    throw error;
   }
}
export async function deleteTarefa(id: String): Promise<void> {
   try{
     await api.delete(`${ROTA_BASE}/${id}`)
   } catch(error){
      console.error(`Erro ao deletar a tarefa ${id}: `, error)
      throw error;
   }
}
export async function buscarPorId(id: string): Promise<Tarefa> {
   try{
      const response = await api.get(`${ROTA_BASE}/${id}`)
      return response.data;
   } catch(error){
      console.error(`Erro ao pegar id ${id}`);
      throw error;
   }
}
export async function atualizarTarefa(id: string, dto: AtualizarTarefaDTO): Promise<Tarefa>{
   try{
      const response = await api.put(`${ROTA_BASE}/${id}`, dto);
      return response.data;
   } catch(error){
      console.error(`Erro ao atualizar a tarefa ${id}: `, error);
      throw error;
   }
}
export async function alternarDificuldade(id: string, novoNivel?: LevelDificuldade): Promise<void> {
   try{
      await api.patch(`${ROTA_BASE}/${id}/level`, null, {
         params: { novoNivel :  novoNivel }
      });
   } catch(error){
      console.error(`Erro ao alternar dificuldade da tarefa ${id}: `, error);
      throw error;
   }
}
