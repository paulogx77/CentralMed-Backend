package br.edu.ifpb.CentralMed.config.auditoria;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // A anotação será usada em métodos
@Retention(RetentionPolicy.RUNTIME) // Ficará disponível em tempo de execução
public @interface Auditable {
    String acao() default "ACESSO_PRONTUARIO";
}