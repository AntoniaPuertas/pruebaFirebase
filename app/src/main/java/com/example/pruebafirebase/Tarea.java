package com.example.pruebafirebase;

import java.io.Serializable;

public class Tarea implements Serializable {
    public enum Tipo{
        URGENTE,
        IMPORTANTE,
        NORMAL
    }
    private String id;
    private String descripcion;
    private String fecha;
    private Tipo tipo;

    public Tarea(String id, String descripcion, String fecha, Tipo tipo) {
        this.id = id;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }
}
