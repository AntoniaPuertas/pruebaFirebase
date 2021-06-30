package com.example.pruebafirebase;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TareasAdapter extends RecyclerView.Adapter<TareasAdapter.TareaHolder> {

    private ArrayList<Tarea> listaTareas;
    private LayoutInflater inflater;
    private Context context;

    public TareasAdapter(ArrayList<Tarea> listaTareas, Context context) {
        this.listaTareas = listaTareas;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public TareaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = inflater.inflate(R.layout.item_tarea2, parent, false);
        return new TareaHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull TareasAdapter.TareaHolder holder, int position) {
        Tarea currentTarea = listaTareas.get(position);
        if(currentTarea.getTipo() == Tarea.Tipo.URGENTE){
            Glide.with(context).load(R.drawable.ic_nota_urgente).into(holder.imgTipo);
        }else if(currentTarea.getTipo() == Tarea.Tipo.IMPORTANTE){
            Glide.with(context).load(R.drawable.ic_nota_amarillo).into(holder.imgTipo);
        }else{
            Glide.with(context).load(R.drawable.ic_nota_verde).into(holder.imgTipo);
        }
        holder.txtFecha.setText(currentTarea.getFecha());
        holder.txtDescripcion.setText(currentTarea.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    public void removeItem(int position) {
        listaTareas.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Tarea tarea, int position) {
        listaTareas.add(position, tarea);
        // notify item added by position
        notifyItemInserted(position);
    }

    class TareaHolder extends RecyclerView.ViewHolder{

        ImageView imgTipo;
        TextView txtFecha;
        TextView txtDescripcion;
        ImageView imgEdit;
        RelativeLayout viewBackground, viewForeground;

        public TareaHolder(@NonNull View itemView) {
            super(itemView);
            imgTipo = itemView.findViewById(R.id.imgTipo2);
            txtFecha = itemView.findViewById(R.id.txtFechaItem2);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcionItem2);
            imgEdit = itemView.findViewById(R.id.imgEdit2);

            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);

            imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posicion = getLayoutPosition();
                    Tarea tarea = listaTareas.get(posicion);
                    Intent intent = new Intent(context, EditarTareaActivity.class);
                    intent.putExtra("posicion", posicion);
                    context.startActivity(intent);
                }
            });
        }

    }
}
