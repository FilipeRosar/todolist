import api from "@/integration/api";
import type { User } from "@/interfaces/tarefamodel";


const ROTA_BASE = 'api/v1/user';

export async function buscarUsuarios(): Promise<User[]> {
    try{
        const response = await api.get<User[]>(ROTA_BASE);
        return response.data;
    } catch(error){
        console.error("Erro ao buscar usuários: ", error);
        throw error;
    }
}
