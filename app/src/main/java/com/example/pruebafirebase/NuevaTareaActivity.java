package com.example.pruebafirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NuevaTareaActivity extends AppCompatActivity {
    private static final String TAG = "NuevaTareaActivity";

    private TextView txtFecha;
    private EditText editDescripcion;
    private Button btnCrearNuevaTarea;
    private RadioGroup rgTipo;
    String fecha_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_tarea);

        txtFecha = findViewById(R.id.txtFecha);
        editDescripcion = findViewById(R.id.editDescripcion);
        btnCrearNuevaTarea = findViewById(R.id.btnCrearTarea);
        rgTipo = findViewById(R.id.rgTipo);

        btnCrearNuevaTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearTarea();
            }
        });

        mostrarFechaActual();
        txtFecha.setText(fecha_string);
        txtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), getString(R.string.seleccionaFecha));
            }
        });

        rgTipo.check(R.id.rbNormal);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> tarea = new HashMap<>();
        tarea.put("descripcion", "Cortarme el pelo");
        tarea.put("fecha", "28/06/2021");
        tarea.put("tipo", "IMPORTANTE");

        // Add a new document with a generated ID
        db.collection("tareas")
                .add(tarea)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    public void getFechaSeleccionada(int year, int month, int day){
        String mes_string = Integer.toString(month + 1);
        String dia_string = Integer.toString(day);
        String anio_string = Integer.toString(year);
        fecha_string = getResources().getString(R.string.fecha_formato) + dia_string + " / " + mes_string + " / " + anio_string;
    }
    public void mostrarFechaActual(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        getFechaSeleccionada(year, month, day);
    }

    public void crearTarea(){
        String desc = editDescripcion.getText().toString();

        if(desc.trim().isEmpty()){
            Toast.makeText(NuevaTareaActivity.this, R.string.mensaje_descripcion, Toast.LENGTH_SHORT).show();
        }else{

        }
    }
}