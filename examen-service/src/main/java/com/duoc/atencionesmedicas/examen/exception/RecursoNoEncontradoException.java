package com.duoc.atencionesmedicas.examen.exception;

public class RecursoNoEncontradoException extends RuntimeException {
//me sirve para decir tal cosa no  encontrada 
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

