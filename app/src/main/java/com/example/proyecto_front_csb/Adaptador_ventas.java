package com.example.proyecto_front_csb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

    private ListenerModificarCantidad listenerModificarCantidad;

    private Context context;

    public interface ListenerModificarCantidad {
        void modificarCantidad();
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }
    public Adaptador_ventas(List<Productos> productos, Context context, ListenerModificarCantidad listenerModificarCantidad) {
        this.productos = productos;
        this.productosSeleccionados = new ArrayList<>();
        this.context = context;
        this.listenerModificarCantidad = listenerModificarCantidad;
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
        String cantidad = Integer.toString(producto.getUnidadesTotales());
        holder.cantidadTotal.setText("Cantidad disponible: " + cantidad);
        String precio = Double.toString(producto.getPrecio());
        holder.precio.setText("Precio: " + precio);
        String unidades = Integer.toString(producto.getUnidades());
        holder.sumar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                producto.setUnidades(producto.getUnidades()+1);
                holder.cantidad.setText(Integer.toString(producto.getUnidades()));
                listenerModificarCantidad.modificarCantidad();

            }
        });
        holder.restar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new AlertDialog.Builder(context).setTitle()
                producto.setUnidades(producto.getUnidades()-1);
                if(producto.getUnidades() == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Eliminar productos")
                            .setMessage("¿Estas seguro que deseas eliminar el producto?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    productos.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, productos.size());
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    producto.setUnidades(1);
                                    holder.cantidad.setText(unidades);
                                }
                            });
                    builder.create().show();

                }
                listenerModificarCantidad.modificarCantidad();
                holder.cantidad.setText(Integer.toString(producto.getUnidades()));
            }
        });

        holder.cantidad.setText(unidades);
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
        TextView nombre, cantidadTotal, precio, cantidad;

        ImageView sumar, restar ;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.textNombre);
            cantidadTotal = itemView.findViewById(R.id.textCantidad);
            precio = itemView.findViewById(R.id.textPrecio);
            sumar = itemView.findViewById(R.id.btSumar);
            restar = itemView.findViewById(R.id.btRestar);
            cantidad = itemView.findViewById(R.id.textCantidadValor);


        }
    }
}
