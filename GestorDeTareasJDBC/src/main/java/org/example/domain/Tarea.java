package org.example.domain;

import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class Tarea {
    private Long id;
    private String titulo;
    private String prioridad;
    private Long usuario_id;
    private User usuario;
    private String categoria;
    private String descripcion;
}
