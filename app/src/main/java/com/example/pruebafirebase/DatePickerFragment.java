package com.example.pruebafirebase;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String tag = (String)this.getTag();
        if(tag.equals("nueva")){
            NuevaTareaActivity activity = (NuevaTareaActivity) getActivity();
            activity.getFechaSeleccionada(year, month, dayOfMonth);
        }else if(tag.equals("editar")){
            EditarTareaActivity activity = (EditarTareaActivity) getActivity();
            activity.getFechaSeleccionada(year, month, dayOfMonth);
        }

    }
}
