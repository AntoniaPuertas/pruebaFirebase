package com.example.pruebafirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{
    private static final String TAG = "MainActivity";

    private RecyclerView recycler;
    private ArrayList<Tarea> listaTareas;
    public static TareasAdapter adapter;
    private FirebaseFirestore db;
    private ConstraintLayout constraintLayout;

    private FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = findViewById(R.id.recycler);
        constraintLayout = findViewById(R.id.constraintLayout);
        floatingActionButton = findViewById(R.id.fBAdd);

        db = FirebaseFirestore.getInstance();

        listaTareas = Datos.getInstance().getListaTareas();
        getTareasServer();
        adapter = new TareasAdapter(listaTareas, this);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recycler.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NuevaTareaActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof TareasAdapter.TareaHolder) {
            // get the removed item name to display it in snack bar
            String name = listaTareas.get(viewHolder.getAdapterPosition()).getDescripcion();

            // backup of removed item for undo purpose
            final Tarea deletedItem = listaTareas.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            adapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, name + " Eliminado de la lista!", Snackbar.LENGTH_LONG);
            snackbar.setAction("Deshacer", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    adapter.restoreItem(deletedItem, deletedIndex);
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Eliminado de la lista");
            builder.setMessage("¿Seguro?");

            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    eliminarTarea(deletedItem.getId());
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.restoreItem(deletedItem, deletedIndex);
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();

        }


    }

    public void getTareasServer(){
        db.collection("tareas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            listaTareas.clear();
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

    public void eliminarTarea(String id){
        db.collection("tareas").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}