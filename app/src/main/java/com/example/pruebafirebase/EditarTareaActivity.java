package com.example.pruebafirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditarTareaActivity extends AppCompatActivity {
    private static final String TAG = "EditarTareaActivity";

    private TextView txtTitulo;
    private TextView txtFecha;
    private EditText editDescripcion;
    private Button btnCrearNuevaTarea;
    private RadioGroup rgTipo;
    Tarea currentTarea;
    int posicion;
    String fecha_string;
    Tarea.Tipo tipo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_tarea);

        txtTitulo = findViewById(R.id.txtTitulo);
        txtFecha = findViewById(R.id.txtFecha);
        editDescripcion = findViewById(R.id.editDescripcion);
        btnCrearNuevaTarea = findViewById(R.id.btnCrearTarea);
        rgTipo = findViewById(R.id.rgTipo);

        txtTitulo.setText(R.string.modificar_tarea);
        btnCrearNuevaTarea.setText(R.string.guardar_cambios);

        Intent intent = getIntent();
        posicion = intent.getIntExtra("posicion", 0);
        currentTarea = Datos.getListaTareas().get(posicion);

        txtFecha.setText(currentTarea.getFecha());
        editDescripcion.setText(currentTarea.getDescripcion());

        btnCrearNuevaTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambios();
            }
        });

        txtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "editar");
            }
        });

        rgTipo.check(R.id.rbNormal);
        tipo = Tarea.Tipo.NORMAL;
        if(currentTarea.getTipoString().equals("IMPORTANTE")){
            rgTipo.check(R.id.rbImportante);
            tipo = Tarea.Tipo.IMPORTANTE;
        }else if(currentTarea.getTipoString().equals("NORMAL")){
            rgTipo.check(R.id.rbNormal);
            tipo = Tarea.Tipo.NORMAL;
        }



        rgTipo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rbImportante:
                        tipo = Tarea.Tipo.IMPORTANTE;
                        break;
                    case R.id.rbNormal:
                        tipo = Tarea.Tipo.NORMAL;
                        break;
                    case R.id.rbUrgente:
                        tipo = Tarea.Tipo.URGENTE;
                        break;
                }
            }
        });
    }

    public void getFechaSeleccionada(int year, int month, int day){
        String mes_string = Integer.toString(month + 1);
        String dia_string = Integer.toString(day);
        String anio_string = Integer.toString(year);
        fecha_string = getResources().getString(R.string.fecha_formato) + dia_string + " / " + mes_string + " / " + anio_string;
        txtFecha.setText(fecha_string);
    }

    public void guardarCambios(){

        String desc = editDescripcion.getText().toString();

        if(desc.trim().isEmpty()){
            Toast.makeText(EditarTareaActivity.this, R.string.mensaje_descripcion, Toast.LENGTH_SHORT).show();
        }else{
            currentTarea.setDescripcion(desc);
            currentTarea.setFecha(fecha_string);
            currentTarea.setTipo(tipo);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            // modifica la tarea
            Map<String, Object> tareaMap = new HashMap<>();
            tareaMap.put("descripcion", desc);
            tareaMap.put("fecha", fecha_string);
            tareaMap.put("tipo", currentTarea.getTipoString());



            // modifica un documento existente

            db.collection("tareas").document(currentTarea.getId())
                    .set(tareaMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            Toast.makeText(EditarTareaActivity.this, R.string.cambios_subidos, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }
    }
}