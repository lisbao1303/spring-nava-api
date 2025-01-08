package com.elisbao.spring_nava_api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // Disponível em tempo de execução
@Target(ElementType.METHOD)         // Aplicável a métodos
public @interface LogOperation {
}