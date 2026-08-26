import { type Tarefa } from "@/interfaces/tarefamodel";
import api from "@/integration/api.js";

const ROTA_BASE = 'api/v1/tarefas'

export async function cadastrarTarefa(dto: Tarefa): Promise<Tarefa> {
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
export async function deleteUsuario(id: String): Promise<void> {
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