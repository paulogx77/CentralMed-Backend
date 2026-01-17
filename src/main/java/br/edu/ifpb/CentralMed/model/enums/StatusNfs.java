package br.edu.ifpb.CentralMed.model.enums;

public enum StatusNfs {
    PENDENTE,
    EMITIDA,
    CANCELADA;

    public enum StatusGuia {
        ABERTA,
        FATURADA,
        PAGA,
        GLOSADA
    }
}