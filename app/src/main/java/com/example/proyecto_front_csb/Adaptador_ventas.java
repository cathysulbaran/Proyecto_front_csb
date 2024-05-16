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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto_front_csb.model.Productos;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adaptador_ventas extends RecyclerView.Adapter<Adaptador_ventas.ProductoViewHolder> implements View.OnClickListener {
    private List<Productos> productos;
    private List<Productos> productosSeleccionados = new ArrayList<>();
    private View.OnClickListener listener;


    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }

    public Adaptador_ventas(List<Productos> productos) {
        this.productos = productos;
        this.productosSeleccionados = new ArrayList<>();
    }

    public List<Productos> getProductosSeleccionados() {
        return productosSeleccionados;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adaptador_ventas, parent, false);
        //view.setOnClickListener(this);
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

        // Establecer el estado del CheckBox según si el producto está seleccionado o no
        holder.checkBox.setChecked(producto.isSelected());

        // Listener para marcar/desmarcar el producto como seleccionado
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                producto.setSelected(holder.checkBox.isChecked()); // Actualiza el estado seleccionado del producto
                if (holder.checkBox.isChecked()) {
                    productosSeleccionados.add(producto); // Agrega el producto a la lista de seleccionados
                } else {
                    productosSeleccionados.remove(producto); // Remueve el producto de la lista de seleccionados
                }
            }
        });
    }


    public void toggleProductoSeleccionado(int position) {
        Productos producto = productos.get(position);
        if (productosSeleccionados.contains(producto)) {
            productosSeleccionados.remove(producto);
        } else {
            productosSeleccionados.add(producto);
        }
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
        CheckBox checkBox;

        ImageView sumar, restar ;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.textNombre);
            cantidad = itemView.findViewById(R.id.textCantidad);
            precio = itemView.findViewById(R.id.textPrecio);
            sumar = itemView.findViewById(R.id.btSumar);
            restar = itemView.findViewById(R.id.btRestar);

        }
    }
}
