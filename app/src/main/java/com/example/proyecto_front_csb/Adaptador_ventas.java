package com.example.proyecto_front_csb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto_front_csb.model.Productos;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adaptador_ventas extends RecyclerView.Adapter<Adaptador_ventas.ProductoViewHolder> implements View.OnClickListener {
    private List<Productos> productos;
    private View.OnClickListener listener;

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }

    public Adaptador_ventas(List<Productos> productos) {
        this.productos = productos;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adaptador_ventas, parent, false);
        view.setOnClickListener(this);
        return new ProductoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {

        Productos producto = productos.get(position);
        holder.nombre.setText("Nombre: " + producto.getNombre());
        String cantidad = Integer.toString(producto.getUnidades());
        holder.cantidad.setText("Cantidad: " + cantidad);
        String precio = Double.toString(producto.getPrecio());
        holder.precio.setText("Precio: " + precio);



    }


    @Override
    public int getItemCount() {
        return productos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder{
        TextView nombre, cantidad, precio;


        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.textNombre);
            cantidad = itemView.findViewById(R.id.textCantidad);
            precio = itemView.findViewById(R.id.textPrecio);

        }
    }
}
