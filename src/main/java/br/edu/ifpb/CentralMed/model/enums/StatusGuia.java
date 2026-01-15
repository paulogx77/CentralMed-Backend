package br.edu.ifpb.CentralMed.model.enums;

public enum StatusGuia {
    ABERTA,     // Guia criada, aguardando faturamento
    FATURADA,   // Guia enviada para o convênio
    PAGA,       // Convênio pagou
    GLOSADA     // Convênio negou o pagamento
}