package com.example.pruebafirebase;

import java.util.ArrayList;

public class Datos {
    private static Datos instancia;
    private static ArrayList<Tarea> listaTareas;

    public static Datos getInstance(){
        if(instancia == null){
            instancia = new Datos();
        }
        return instancia;
    }

    private Datos(){
        listaTareas = new ArrayList<>();
    }

    public static void inicializarDatos(){
        instancia = null;
    }

    public ArrayList<Tarea> getListaTareas(){
        return listaTareas;
    }

    public void addTarea(Tarea tarea){
        listaTareas.add(tarea);
    }
}
