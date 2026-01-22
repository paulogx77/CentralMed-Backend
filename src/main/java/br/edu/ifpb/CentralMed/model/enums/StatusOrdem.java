package br.edu.ifpb.CentralMed.model.enums;

public enum StatusOrdem {
    PENDENTE, // Ordem criada, aguardando entrega
    CONCLUIDA, // Itens recebidos, estoque atualizado
    CANCELADA
}