//tarefaModel.ts

export type StatusTarefa = 'PENDENTE' | 'EM_ANDAMENTO' | 'CONCLUIDA';

export type LevelDificuldade = 'LOW' | 'MEDIUM' | 'HIGH';

export interface Tarefa {
    id?: string | undefined;
    descricao: string;
    status?: StatusTarefa;
    dificuldade?: LevelDificuldade;
    dataCriacao?: Date;
    dataConclusao?: Date;
    user?: User;
}

export interface User{
    id?: string;
    name: string;
    email: string;
}
export interface CadastrarTarefaDTO{
    descricao: string;
    userId: string;
    levelDificuldade?: LevelDificuldade;
}