package com.example.pruebafirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView recycler;
    private ArrayList<Tarea> listaTareas;
    private TareasAdapter adapter;
    private FirebaseFirestore db;

    private FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = findViewById(R.id.recycler);
        floatingActionButton = findViewById(R.id.fBAdd);

        db = FirebaseFirestore.getInstance();

        listaTareas = Datos.getInstance().getListaTareas();
        getTareasServer();
        adapter = new TareasAdapter(listaTareas, this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NuevaTareaActivity.class);
                startActivity(intent);
            }
        });

    }

    public void getTareasServer(){
        db.collection("tareas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                Map<String, Object> datos = document.getData();
                                String descripcion = (String)datos.get("descripcion");
                                String fecha = (String)datos.get("fecha");
                                String tipoString = (String)datos.get("tipo");
                                Tarea.Tipo tipo = Tarea.Tipo.IMPORTANTE;
                                if(tipoString.equals("URGENTE")){
                                    tipo = Tarea.Tipo.URGENTE;
                                }else if(tipoString.equals("NORMAL")){
                                    tipo = Tarea.Tipo.NORMAL;
                                }
                                Tarea tarea = new Tarea(id, descripcion, fecha, tipo);
                                listaTareas.add(tarea);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w(TAG, "Error bajando datos del servidor.", task.getException());
                        }
                    }
                });
    }
}