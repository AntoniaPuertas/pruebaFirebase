package com.example.pruebafirebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Datos {
    private static final String TAG = "Datos";
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

    public static ArrayList<Tarea> getListaTareas(){
        return listaTareas;
    }

    public static void addTarea(Tarea tarea){
        listaTareas.add(tarea);
    }


}
